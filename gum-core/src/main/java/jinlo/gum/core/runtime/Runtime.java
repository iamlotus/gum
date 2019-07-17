package jinlo.gum.core.runtime;


import jinlo.gum.core.exception.BusinessProcessException;
import jinlo.gum.core.model.BusinessCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;


/**
 * 运行时由若干业务配置构成
 */
public class Runtime {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runtime.class);

    public static class Target {

        public static Target NULL_TARGET = new Target(null, null);

        private BusinessCode code;
        private BusinessConfig config;

        public Target(BusinessCode code, BusinessConfig config) {
            this.code = code;
            this.config = config;
        }

        public BusinessCode getCode() {
            return code;
        }

        public BusinessConfig getConfig() {
            return config;
        }

    }


    protected static abstract class AbstractBusinessProcess implements BusinessProcess {
        protected static final Logger LOGGER = LoggerFactory.getLogger(PrecompiledBusinessProcess.class);

        private String processId = UUID.randomUUID().toString();

        @Override
        public String getProcessId() {
            return processId;
        }

        @Override
        public String toString() {
            return "[BusinessProcess " + processId + "]";
        }

        @Override
        public <V> V execute(Callable<V> callback) {
            Objects.requireNonNull(callback);
            try {
                LOGGER.debug("start execute callback in {}", this);
                BusinessProcesses.set(this);
                return callback.call();
            } catch (Throwable t) {
                String msg = "meet throwable in execute call in " + this;
                LOGGER.error(msg, t);
                throw new BusinessProcessException(msg, t);
            } finally {
                BusinessProcesses.remove();
                LOGGER.debug("end execute callback in {} successfully", this);
            }
        }

        @Override
        public BusinessCode getCode(Object instance) {
            return getTarget(instance).getCode();
        }

        @Override
        public BusinessConfig getConfig(Object instance) {
            return getTarget(instance).getConfig();
        }

        protected abstract Target getTarget(Object instance);
    }

    public static class PrecompiledBusinessProcess extends AbstractBusinessProcess {

        private Map<Object, Target> instance2Target;

        public PrecompiledBusinessProcess(Map<Object, Target> instance2Target) {
            this.instance2Target = instance2Target;
        }

        protected Target getTarget(Object instance) {
            Target target = instance2Target.get(instance);
            if (target == null) {
                String msg = "can not find instance " + instance + " in " + this.toString();
                LOGGER.error(msg);
                throw new BusinessProcessException(msg);
            }
            return target;
        }
    }

    public static class LazyBusinessProcess extends AbstractBusinessProcess {

        private Map<Object, Target> instance2Target;

        private Runtime runtime;

        private boolean needSynchronized;

        public LazyBusinessProcess(Runtime runtime, boolean needSynchronized) {
            this.runtime = runtime;
            this.needSynchronized = needSynchronized;
            this.instance2Target = new HashMap<>();
        }


        protected Target getTarget(Object instance) {
            if (needSynchronized) {
                synchronized (this) {
                    return getTarget0(instance);
                }
            } else {
                return getTarget0(instance);
            }
        }

        private Target getTarget0(Object instance) {
            Target target = instance2Target.get(instance);
            if (target == Target.NULL_TARGET) {
                String msg = "can not find exactly one BusinessConfig from " + instance;
                LOGGER.error(msg);
                throw new BusinessProcessException(msg);
            } else if (target == null) {
                // init
                try {
                    target = runtime.findBusinessConfigByInstance(instance);
                    instance2Target.put(instance, target);
                } catch (BusinessProcessException e) {
                    instance2Target.put(instance, Target.NULL_TARGET);
                    throw e;
                }
            }
            return target;
        }

    }

    private Set<BusinessConfig> businessConfigs;

    public Runtime(Set<BusinessConfig> businessConfigs) {
        this.businessConfigs = businessConfigs;
    }


    /**
     * 查找指定业务对象生效的业务配置，首先parse出业务编码，再根据业务编码定位到唯一的业务配置。通常来说，对每个业务对象，这个方法应该调用最多一次。
     *
     * @param instance
     * @return
     */
    Target findBusinessConfigByInstance(Object instance) {
        Set<BusinessConfig> knows = new HashSet<>();

        for (BusinessConfig businessConfig : businessConfigs) {
            try {
                if (businessConfig.getBusinessTemplate().getParser().knows(instance)) {
                    knows.add(businessConfig);
                }
            } catch (Exception e) {
                // if one business parser throws exception, it should not impact other business
                LOGGER.warn(businessConfig.getBusinessTemplate().getCode() + " throws Exception when parse " + instance, e);
            }
        }

        if (knows.isEmpty()) {
            String msg = "no BusinessConfig knows instance " + instance;
            LOGGER.error(msg);
            throw new BusinessProcessException(msg);
        }

        if (knows.size() > 1) {
            String msg = "multiple BusinessConfig " + knows + " know source " + instance;
            LOGGER.error(msg);
            throw new BusinessProcessException(msg);
        }

        BusinessConfig businessConfig = knows.iterator().next();
        BusinessCode businessCode = businessConfig.getBusinessTemplate().getParser().parse(instance);

        if (businessCode == null) {
            String msg = businessConfig + "parse " + instance + " return null ";
            LOGGER.error(msg);
            throw new BusinessProcessException(msg);
        }

        if (!businessConfig.getBusinessCodes().contains(businessCode)) {
            LOGGER.warn("{} parse {} return {} which is not in template", businessConfig, instance, businessCode);
        }

        return new Target(businessCode, businessConfig);
    }

    /**
     * 创建一个业务进程，并存储到{@link BusinessProcesses}中。这个进程采用懒加载，所有业务对象在第一次使用时才会parse{@link BusinessCode}，
     * 这个BusinessProcess在运行过程中可能被编辑。 所以此方法返回的结果<b>非线程安全</b>，仅在在结果<b>确定后续处理不牵涉到并发操作</b>时，使用这个方法。
     *
     * @return
     */
    public BusinessProcess createProcess() {
        return new LazyBusinessProcess(this, false);
    }

    /**
     * 创建一个业务进程，并存储到{@link BusinessProcesses}中。这个进程采用懒加载，所有业务对象在第一次使用时才会parse{@link BusinessCode}，
     * 这个BusinessProcess在运行过程中可能被编辑，为了线程安全，这里的方法都会被同步。 所以此方法返回的结果性能可能较差，仅在<b>确定后续处理涉及并发操作</b>时，使用这个方法。
     *
     * @return
     */
    public BusinessProcess createConcurrentProcess() {
        return new LazyBusinessProcess(this, true);
    }

    /**
     * 创建一个业务进程，并存储到{@link BusinessProcesses}中。这个进程采用预编译，在创建时需指定所以后续处理会用到的所有业务对象，
     * 这个BusinessProcess在运行过程中是不可修改的，所以返回方法不需要同步，在<b>确定后续处理涉及并发操作</b>且需要较高性能时，使用这个方法。
     *
     * @return
     */
    public BusinessProcess createPrecompiledProcess(Object... instances) {
        LOGGER.debug("start create BusinessProcess from {}", instances);

        Map<Object, Target> source2Target = new HashMap<>();


        for (Object instance : instances) {
            if (source2Target.containsKey(instance)) {
                LOGGER.warn("find duplicate instance {}", instance);
                continue;
            }

            Target target = findBusinessConfigByInstance(instance);
            source2Target.put(instance, target);
        }

        LOGGER.debug("finish create BusinessProcess from {} successfully", instances);

        return new PrecompiledBusinessProcess(source2Target);
    }
}
