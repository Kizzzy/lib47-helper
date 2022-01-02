package cn.kizzzy.helper;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
}
