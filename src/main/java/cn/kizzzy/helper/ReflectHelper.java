package cn.kizzzy.helper;

import java.lang.reflect.Field;

public class ReflectHelper {
    
    public static Field getField(Class<?> clazz, String fieldName) {
        Class<?> temp = clazz;
        do {
            try {
                Field field = temp.getDeclaredField(fieldName);
                if (field != null) {
                    return field;
                }
            } catch (NoSuchFieldException ignored) {
            
            }
            temp = temp.getSuperclass();
        } while (temp != null);
        return null;
    }
}
