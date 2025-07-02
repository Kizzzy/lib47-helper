package cn.kizzzy.format;

import cn.kizzzy.helper.ReflectHelper;
import cn.kizzzy.helper.StringHelper;

import java.lang.reflect.Method;

public class MethodField implements IField {
    
    private final Object obj;
    private final Method method;
    
    public MethodField(Object obj, Method method) {
        this.obj = obj;
        this.method = method;
    }
    
    @Override
    public boolean ignore() {
        if (method.getAnnotation(Ignore.class) != null) {
            return true;
        }
        
        return method.getParameters().length == 0 &&
            method.getName().startsWith("get") &&
            method.getName().length() > 3;
    }
    
    @Override
    public boolean expand() {
        return method.getAnnotation(Shrink.class) == null;
    }
    
    @Override
    public String name() {
        String fieldName = method.getName().substring(3);
        fieldName = StringHelper.firstLower(fieldName);
        return fieldName;
    }
    
    @Override
    public Object value() throws Exception {
        return ReflectHelper.getter(method, obj);
    }
}