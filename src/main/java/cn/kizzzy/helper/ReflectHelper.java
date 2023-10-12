package cn.kizzzy.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ReflectHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(ReflectHelper.class);
    
    public static Type getGenericClass(Class<?> clazz) {
        return getGenericClass(clazz, 0);
    }
    
    public static Type getGenericClass(Class<?> clazz, int index) {
        ParameterizedType superType = (ParameterizedType) clazz.getGenericSuperclass();
        return superType.getActualTypeArguments()[index];
    }
    
    public static Type getParameterizedType(Class<?> rawClass, Class<?>... paramClass) {
        Type[] types = new Type[paramClass.length];
        for (int i = 0; i < paramClass.length; ++i) {
            types[i] = getGenericClass(paramClass[i]);
        }
        return ParameterizedTypeImpl.make(rawClass, types, null);
    }
    
    public static Field getField(Class<?> clazz, String fieldName) {
        Class<?> temp = clazz;
        do {
            try {
                return temp.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
            
            }
            temp = temp.getSuperclass();
        } while (temp != null);
        return null;
    }
    
    public static Object getValue(Field field, Object target) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(target);
        } finally {
            field.setAccessible(accessible);
        }
    }
    
    public static void setValue(Field field, Object target, Object value) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(target, value);
        } finally {
            field.setAccessible(accessible);
        }
    }
    
    public static Object getter(Method method, Object target) throws InvocationTargetException, IllegalAccessException {
        boolean accessible = method.isAccessible();
        try {
            method.setAccessible(true);
            return method.invoke(target);
        } finally {
            method.setAccessible(accessible);
        }
    }
    
    public static void setter(Method method, Object target, Object... params) throws InvocationTargetException, IllegalAccessException {
        boolean accessible = method.isAccessible();
        try {
            method.setAccessible(true);
            method.invoke(target, params);
        } finally {
            method.setAccessible(accessible);
        }
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
                            setValue(field, obj, val);
                        } catch (Exception e) {
                            logger.error("invoke setter error", e);
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
                            return getValue(field, obj);
                        } catch (Exception e) {
                            logger.error("invoke getter error", e);
                            return null;
                        }
                    }, field.getType());
                }
            }
            
            clazz = clazz.getSuperclass();
        }
    }
}
