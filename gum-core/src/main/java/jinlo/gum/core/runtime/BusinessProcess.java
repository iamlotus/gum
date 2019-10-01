package jinlo.gum.core.runtime;

import jinlo.gum.core.exception.BusinessProcessException;
import jinlo.gum.core.model.BusinessCode;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 一次业务流程。通常一次业务流程可能涉及到若干业务实例以及若干业务编码，每个业务编码在运行时对应唯一的BusinessConfig。一次业务流程可以多次执行某个方法。
 */
public interface BusinessProcess {

    /**
     * 全局唯一标示
     *
     * @return
     */
    String getProcessId();

    /**
     * 查找指定业务实例对应的业务编码，所有业务实例只会被parse一次，相同业务实例一定返回相同的业务编码
     *
     * @param instance
     * @return
     */
    BusinessCode getCode(Object instance);

    /**
     * 查找指定业务实例对应的业务配置，所有业务实例只会被parse一次，相同业务实例一定返回相同的业务配置
     *
     * @param instance
     * @return
     */
    BusinessConfig getConfig(Object instance);


    /**
     * 在流程中执行一段代码并返回。如果执行过程中抛出异常，会包装成{@link BusinessProcessException}。执行过程中可确保{@link BusinessProcesses#get()}
     * 返回当前业务流程
     *
     * @param callback
     * @param <V>
     * @return
     */
    <V> V execute(Callable<V> callback);
}
