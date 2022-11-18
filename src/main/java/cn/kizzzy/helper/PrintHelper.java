package cn.kizzzy.helper;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class PrintHelper {
    
    private interface Getter {
        
        Object get() throws Exception;
    }
    
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
        return ToString(obj, 0, false, args, false, new HashMap<>());
    }
    
    private static String ToString(Object obj, int layer, boolean fromIterable, PrintArgs[] args, boolean arrayElement, Map<Object, Boolean> visitedKvs) {
        if (obj == null) {
            if (arrayElement) {
                StringBuilder _builder = new StringBuilder();
                for (int i = 0; i < layer; ++i) {
                    _builder.append("\t");
                }
                _builder.append("null");
                return _builder.toString();
            }
            return "null";
        }
        
        Class<?> clazz = obj.getClass();
        
        if (isSimple(clazz)) {
            return obj.toString();
        }
        
        if (clazz.isArray()) {
            return processIterable(args, layer, fromIterable, Array.getLength(obj), i -> Array.get(obj, i), visitedKvs);
        }
        
        if (List.class.isAssignableFrom(clazz)) {
            List list = (List) obj;
            return processIterable(args, layer, fromIterable, list.size(), list::get, visitedKvs);
        }
        
        if (Map.class.isAssignableFrom(clazz)) {
            return "Un Handle Map";
        }
        
        boolean visited = visitedKvs.computeIfAbsent(obj, k -> false);
        if (visited) {
            return "@" + obj.hashCode();
        }
        visitedKvs.put(obj, true);
        
        PrintArgs _args = null;
        if (args != null) {
            for (PrintArgs temp : args) {
                if (temp.clazz == clazz) {
                    _args = temp;
                }
            }
        }
        
        StringBuilder builder = new StringBuilder();
        
        if (arrayElement) {
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
        }
        
        builder.append(clazz.getSimpleName()).append("@").append(obj.hashCode());
        builder.append(" { ");
        
        if (_args == null || _args.expand) {
            builder.append("\r\n");
        }
        
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            processField(args, _args, layer, builder, field.getName(), field, () -> field.get(obj), visitedKvs);
        }
        
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameters().length == 0 && method.getName().startsWith("get") && method.getName().length() > 3) {
                String fieldName = method.getName().substring(3);
                fieldName = StringHelper.firstLower(fieldName);
                
                processField(args, _args, layer, builder, fieldName, method, () -> method.invoke(obj), visitedKvs);
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
    
    private static String processIterable(PrintArgs[] args, int layer, boolean fromIterable, int length, Function<Integer, Object> getter, Map<Object, Boolean> visitedKvs) {
        StringBuilder builder = new StringBuilder();
        
        if (fromIterable) {
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
        }
        
        builder.append("<").append(length).append("> [ ");
        
        boolean expand = false;
        for (int i = 0; i < length; ++i) {
            Object item = getter.apply(i);
            if (item != null) {
                expand = !isSimple(item.getClass());
                break;
            }
        }
        
        if (expand) {
            builder.append("\r\n");
        }
        
        for (int i = 0; i < length; ++i) {
            builder.append(ToString(getter.apply(i), layer + 1, true, args, true, visitedKvs));
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
    
    private static void processField(PrintArgs[] args, PrintArgs _args, int layer, StringBuilder builder, String fieldName, AccessibleObject accessibleObject, Getter getter, Map<Object, Boolean> visitedKvs) {
        PrintArgs.Item _item = null;
        if (_args != null && _args.items != null) {
            for (PrintArgs.Item temp : _args.items) {
                if (Objects.equals(temp.name, fieldName)) {
                    _item = temp;
                }
            }
        }
        
        if (_item != null && _item.ignore) {
            return;
        }
        
        if (_args == null || _args.expand) {
            for (int i = 0; i <= layer; ++i) {
                builder.append("\t");
            }
        }
        
        builder.append(fieldName);
        builder.append(" = ");
        
        boolean access = accessibleObject.isAccessible();
        try {
            accessibleObject.setAccessible(true);
            builder.append(ToString(getter.get(), layer + 1, false, args, false, visitedKvs));
        } catch (Exception e) {
            LogHelper.error(e);
        } finally {
            accessibleObject.setAccessible(access);
        }
        builder.append(", ");
        
        if (_args == null || _args.expand) {
            builder.append("\r\n");
        }
    }
}
