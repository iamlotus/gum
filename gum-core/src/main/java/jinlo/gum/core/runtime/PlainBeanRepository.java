package jinlo.gum.core.runtime;

import jinlo.gum.core.exception.EnvironmentException;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地创建Bean，不依赖其它IOC，保证singleton，非线程安全
 */
public class PlainBeanRepository implements BeanRepository {

    private Map<String,Object> cache;

    public PlainBeanRepository(){
        this.cache=new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String className) {
        T bean= (T)cache.get(className);
        if (bean==null){
            try {
                bean=(T)Class.forName(className).newInstance();
            } catch (Exception e) {
                throw new EnvironmentException("can not create bean of "+className,e);
            }

            cache.put(className,bean);
        }
        return bean;
    }
}
