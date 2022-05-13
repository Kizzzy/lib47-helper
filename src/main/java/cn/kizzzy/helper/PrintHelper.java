package cn.kizzzy.helper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PrintHelper {
    
    private static final List<Class<?>> classes = Arrays.asList(
        Boolean.class,
        Character.class,
        Byte.class,
        Short.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class,
        String.class
    );
    
    public static String ToString(Object obj, PrintArgs[] args) {
        return ToString(obj, 0, args);
    }
    
    private static String ToString(Object obj, int layer, PrintArgs[] args) {
        Class<?> clazz = obj.getClass();
        
        if (clazz.isPrimitive() || classes.contains(clazz) || clazz.isEnum()) {
            return obj.toString();
        }
        
        if (clazz.isArray()) {
            StringBuilder builder = new StringBuilder();
            builder.append("[\r\n");
            
            for (int i = 0, n = Array.getLength(obj); i < n; ++i) {
                builder.append(ToString(Array.get(obj, i), layer + 1, args));
                builder.append(",");
                builder.append("\r\n");
            }
            
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
            
            builder.append("]");
            return builder.toString();
        }
        
        PrintArgs _args = null;
        if (args != null) {
            for (PrintArgs temp : args) {
                if (temp.clazz == clazz) {
                    _args = temp;
                }
            }
        }
        
        Field[] fields = clazz.getFields();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < layer; ++i) {
            builder.append("\t");
        }
        
        builder.append(clazz.getSimpleName());
        builder.append("{ ");
        
        if (_args != null && _args.expand) {
            builder.append("\r\n");
        }
        
        for (Field field : fields) {
            PrintArgs.Item _item = null;
            if (_args != null && _args.items != null) {
                for (PrintArgs.Item temp : _args.items) {
                    if (Objects.equals(temp.name, field.getName())) {
                        _item = temp;
                    }
                }
            }
            
            if (_item != null && _item.ignore) {
                continue;
            }
            
            if (_args != null && _args.expand) {
                for (int i = 0; i <= layer; ++i) {
                    builder.append("\t");
                }
            }
            
            builder.append(field.getName());
            builder.append(" = ");
            
            boolean access = field.isAccessible();
            try {
                field.setAccessible(true);
                builder.append(ToString(field.get(obj), layer + 1, args));
            } catch (Exception e) {
                LogHelper.error(e);
            } finally {
                field.setAccessible(access);
            }
            
            builder.append(", ");
            
            if (_args != null && _args.expand) {
                builder.append("\r\n");
            }
        }
        
        if (_args != null && _args.expand) {
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
        }
        builder.append("}");
        return builder.toString();
    }
}
