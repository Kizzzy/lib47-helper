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
    
    private static boolean isSimple(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.isEnum() || classes.contains(clazz);
    }
    
    public static String ToString(Object obj) {
        return ToString(obj, null);
    }
    
    public static String ToString(Object obj, PrintArgs[] args) {
        return ToString(obj, 0, args, false);
    }
    
    private static String ToString(Object obj, int layer, PrintArgs[] args, boolean arrayElement) {
        if (obj == null) {
            return "null";
        }
        
        Class<?> clazz = obj.getClass();
        
        if (isSimple(clazz)) {
            return obj.toString();
        }
        
        if (clazz.isArray()) {
            boolean expand = Array.getLength(obj) > 0 && !isSimple(Array.get(obj, 0).getClass());
            
            StringBuilder builder = new StringBuilder();
            builder.append("[ ");
            
            if (expand) {
                builder.append("\r\n");
            }
            
            for (int i = 0, n = Array.getLength(obj); i < n; ++i) {
                builder.append(ToString(Array.get(obj, i), layer + 1, args, true));
                builder.append(", ");
                
                if (expand) {
                    builder.append("\r\n");
                }
            }
            
            if (expand) {
                for (int i = 0; i < layer; ++i) {
                    builder.append("\t");
                }
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
        
        if (arrayElement) {
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
        }
        
        builder.append(clazz.getSimpleName());
        builder.append(" { ");
        
        if (_args == null || _args.expand) {
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
            
            if (_args == null || _args.expand) {
                for (int i = 0; i <= layer; ++i) {
                    builder.append("\t");
                }
            }
            
            builder.append(field.getName());
            builder.append(" = ");
            
            boolean access = field.isAccessible();
            try {
                field.setAccessible(true);
                builder.append(ToString(field.get(obj), layer + 1, args, false));
            } catch (Exception e) {
                LogHelper.error(e);
            } finally {
                field.setAccessible(access);
            }
            
            builder.append(", ");
            
            if (_args == null || _args.expand) {
                builder.append("\r\n");
            }
        }
        
        if (_args == null || _args.expand) {
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
        }
        builder.append("}");
        return builder.toString();
    }
}
