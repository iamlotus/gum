package jinlo.gum.core.runtime;


import jinlo.gum.core.ExtensionCallback;
import jinlo.gum.core.reduce.Reducer;
import jinlo.gum.core.spec.ExtensionImplementationSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 系统中所有生效的业务流程都在这里，和线程绑定。{@link BusinessProcess#execute(Callable)}会负责self在这里的set/remove，这样
 * 执行过程中始终有着可用的{@link BusinessProcess}
 */
public class BusinessProcesses {
    // no constructor
    private BusinessProcesses() {

    }

    private static ThreadLocal<BusinessProcess> processes = new ThreadLocal<>();

    public static void set(BusinessProcess process) {
        processes.set(process);
    }

    public static BusinessProcess get() {
        return processes.get();
    }

    public static void remove() {
        processes.remove();
    }


    public static <E, T, R> R executeExtension(Object instance, BusinessProcess process, Class<E> extensionClz, Reducer<T, R> reducer, ExtensionCallback<E, T> callback) {
        return executeExtension(instance, process, extensionClz.getName(), reducer, callback);
    }

    public static <E, T, R> R executeExtension(Object instance, Class<E> extensionClz, Reducer<T, R> reducer, ExtensionCallback<E, T> callback) {
        return executeExtension(instance, get(), extensionClz.getName(), reducer, callback);
    }

    public static <E, T, R> R executeExtension(Object instance, String extensionCode, Reducer<T, R> reducer, ExtensionCallback<E, T> callback) {
        return executeExtension(instance, get(), extensionCode, reducer, callback);
    }
    /**
     * 在指定业务流程中执行扩展点
     **/
    public static <E, T, R>  R executeExtension(Object instance, BusinessProcess process, String extensionCode, Reducer<T, R> reducer, ExtensionCallback<E, T> callback) {
        return executeExtension0(instance,process,extensionCode,reducer,callback,false).getResult();
    }

    public static <E, T, R> ExtensionExecuteDetail<R, T> executeExtensionWithDetail(Object instance, BusinessProcess process, Class<E> extensionClz, Reducer<T, R> reducer, ExtensionCallback<E, T> callback) {
        return executeExtensionWithDetail(instance, process, extensionClz.getName(), reducer, callback);
    }

    public static <E, T, R> ExtensionExecuteDetail<R, T> executeExtensionWithDetail(Object instance, Class<E> extensionClz, Reducer<T, R> reducer, ExtensionCallback<E, T> callback) {
        return executeExtensionWithDetail(instance, get(), extensionClz.getName(), reducer, callback);
    }

    public static <E, T, R> ExtensionExecuteDetail<R, T> executeExtensionWithDetail(Object instance, String extensionCode, Reducer<T, R> reducer, ExtensionCallback<E, T> callback) {
        return executeExtensionWithDetail(instance, get(), extensionCode, reducer, callback);
    }

    /**
     * 在指定业务流程中执行扩展点
     **/
    public static <E, T, R> ExtensionExecuteDetail<R, T> executeExtensionWithDetail(Object instance, BusinessProcess process, String extensionCode, Reducer<T, R> reducer, ExtensionCallback<E, T> callback) {
        return executeExtension0(instance,process,extensionCode,reducer,callback,true);
    }

    /**
     * 在指定业务流程中执行扩展点
     **/
    @SuppressWarnings("unchecked")
    private static <E, T, R> ExtensionExecuteDetail<R, T> executeExtension0(Object instance, BusinessProcess process, String extensionCode, Reducer<T, R> reducer, ExtensionCallback<E, T> callback,boolean needDetail) {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(process);
        Objects.requireNonNull(extensionCode);
        Objects.requireNonNull(reducer);
        Objects.requireNonNull(callback);


        BusinessConfig config = process.getConfig(instance);
        List<ExtensionImplementationSpec> implementations = config.getImplementationsByCode(extensionCode);

        ExtensionExecuteDetail<R, T> details = new ExtensionExecuteDetail<>(extensionCode);
        List<T> results=new ArrayList<>(implementations.size());
        for (ExtensionImplementationSpec implementation : config.getImplementationsByCode(extensionCode)) {
            E implementationInstance=(E) implementation.getInstance();
            T result = callback.apply(implementationInstance);
            results.add(result);

            if (needDetail) {
                ExtensionExecuteDetail.FacadeDetail<T> detail = new ExtensionExecuteDetail.FacadeDetail<>();
                detail.setFacadeCode(implementation.getFacade().getCode());
                detail.setResult(result);
                details.getDetails().add(detail);
            }
        }

        R result = reducer.reduce(results.stream());
        details.setResult(result);

        return details;
    }


}