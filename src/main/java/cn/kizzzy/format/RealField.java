package cn.kizzzy.format;

import cn.kizzzy.helper.ReflectHelper;

public class RealField implements IField {
    
    private final Object obj;
    private final java.lang.reflect.Field field;
    
    public RealField(Object obj, java.lang.reflect.Field field) {
        this.obj = obj;
        this.field = field;
    }
    
    @Override
    public boolean ignore() {
        if (field.getAnnotation(Ignore.class) != null) {
            return true;
        }
        return field.getType().getAnnotation(Ignore.class) != null;
    }
    
    @Override
    public boolean expand() {
        return field.getAnnotation(Shrink.class) == null;
    }
    
    @Override
    public String name() {
        return field.getName();
    }
    
    @Override
    public Object value() throws Exception {
        return ReflectHelper.getValue(field, obj);
    }
}
