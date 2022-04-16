package cn.kizzzy.helper;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ClassHelper {
    
    public static <T> Class<T> getGenericClass(Class<?> clazz) {
        return getGenericClass(clazz, 0);
    }
    
    public static <T> Class<T> getGenericClass(Class<?> clazz, int index) {
        return (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[index];
    }
    
    public static Type getParameterizedType(Class<?> rawClass, Class<?>... paramClass) {
        Type[] types = new Type[paramClass.length];
        for (int i = 0; i < paramClass.length; ++i) {
            types[i] = getGenericClass(paramClass[i]);
        }
        return ParameterizedTypeImpl.make(rawClass, types, null);
    }
    
    public interface SetterCallback<T extends Annotation> {
        void invoke(T attr, BiConsumer<Object, Object> setter, Class<?> fieldType);
    }
    
    public static <T extends Annotation> void setter(Class<?> targetType, Class<T> attrType, SetterCallback<T> callback) {
        Class<?> clazz = targetType;
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                T attr = field.getAnnotation(attrType);
                if (attr != null) {
                    callback.invoke(attr, (obj, val) -> {
                        try {
                            field.setAccessible(true);
                            field.set(obj, val);
                        } catch (Exception e) {
                            LogHelper.error(e);
                        } finally {
                            field.setAccessible(false);
                        }
                    }, field.getType());
                }
            }
            
            clazz = clazz.getSuperclass();
        }
        
    }
    
    public interface GetterCallback<T extends Annotation> {
        void invoke(T attr, Function<Object, Object> setter, Class<?> fieldType);
    }
    
    public static <T extends Annotation> void getter(Class<?> targetType, Class<T> attrType, GetterCallback<T> callback) {
        Class<?> clazz = targetType;
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                T attr = field.getAnnotation(attrType);
                if (attr != null) {
                    callback.invoke(attr, obj -> {
                        try {
                            field.setAccessible(true);
                            return field.get(obj);
                        } catch (Exception e) {
                            LogHelper.error(e);
                            return null;
                        } finally {
                            field.setAccessible(false);
                        }
                    }, field.getType());
                }
            }
            
            clazz = clazz.getSuperclass();
        }
    }
}
