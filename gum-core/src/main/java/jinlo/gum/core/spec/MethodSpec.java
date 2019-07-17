package jinlo.gum.core.spec;

import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class MethodSpec extends Spec {

    private String methodName;

    //参数类型
    private String[] paramTypes;

    //返回类型
    private String returnType;

    public MethodSpec(Method method) {
        super(toCode(method));
        saveParamTypes(method);
    }

    public MethodSpec(Method method, String name) {
        super(toCode(method), name);
        saveParamTypes(method);
    }

    private static String toCode(Method method) {
        //暂时不支持方法重载，如果有相同方法名存在，后续处理应该报错
        return method.getDeclaringClass().getTypeName()+"#"+method.getName();
    }

    private void saveParamTypes(Method method){
        methodName=method.getName();
        paramTypes=Arrays.stream(method.getParameterTypes()).map(Class::getName).toArray(String[]::new);
        returnType =method.getReturnType().getName();
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParamTypes() {
        return paramTypes;
    }

    public String getReturnType() {
        return returnType;
    }
}
