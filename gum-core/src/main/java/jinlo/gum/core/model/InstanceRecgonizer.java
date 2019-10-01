package jinlo.gum.core.model;


/**
 * Indicate whether a object knows an instance or not, generally this object will not process instance if it does not know the instance.
 */
public interface InstanceRecgonizer{
    class PositiveRecgonizer implements InstanceRecgonizer{
        @Override
        public boolean knows(Object instance) {
            return true;
        }
    }

    boolean knows(Object instance);
}