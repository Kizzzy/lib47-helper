package cn.kizzzy.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PrintHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(PrintHelper.class);
    
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
        return ToString(obj, 0, false, false, new HashMap<>());
    }
    
    private static String ToString(Object obj, int layer, boolean fromIterable, boolean arrayElement, Map<Object, Boolean> visitedKvs) {
        if (obj == null) {
            return processNull(layer, arrayElement);
        }
        
        Class<?> clazz = obj.getClass();
        
        if (clazz.getAnnotation(Ignore.class) != null) {
            return "ignore";
        }
        
        if (isSimple(clazz)) {
            return obj.toString();
        }
        
        if (clazz.isArray()) {
            return processIterable(layer, fromIterable, Array.getLength(obj), i -> Array.get(obj, i), visitedKvs);
        }
        
        if (List.class.isAssignableFrom(clazz)) {
            List list = (List) obj;
            return processIterable(layer, fromIterable, list.size(), list::get, visitedKvs);
        }
        
        if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) obj;
            return processKeyValue(layer, fromIterable, map.size(), map.keySet(), map::get, visitedKvs);
        }
        
        boolean visited = visitedKvs.computeIfAbsent(obj, k -> false);
        if (visited) {
            return "@" + obj.hashCode();
        }
        visitedKvs.put(obj, true);
        
        StringBuilder builder = new StringBuilder();
        
        if (arrayElement) {
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
        }
        
        builder.append(clazz.getSimpleName()).append("@").append(obj.hashCode());
        builder.append(" { ");
        
        boolean expand = clazz.getAnnotation(Shrink.class) == null;
        if (expand) {
            builder.append("\r\n");
        }
        
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            processField(layer, builder, visitedKvs, new RealField(obj, field));
        }
        
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            processField(layer, builder, visitedKvs, new MethodField(obj, method));
        }
        
        if (expand) {
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
        }
        builder.append("}");
        
        return builder.toString();
    }
    
    private static String processNull(int layer, boolean arrayElement) {
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
    
    private static <T> String processKeyValue(int layer, boolean fromIterable, int length,
                                              Iterable<T> iterable, Function<T, Object> getter,
                                              Map<Object, Boolean> visitedKvs) {
        StringBuilder builder = new StringBuilder();
        
        if (fromIterable) {
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
        }
        
        builder.append("<").append(length).append("> {");
        
        boolean expand = false;
        for (T key : iterable) {
            Object item = getter.apply(key);
            if (item != null) {
                expand = !isSimple(item.getClass());
                break;
            }
        }
        
        if (expand) {
            builder.append("\r\n");
        }
        
        for (T key : iterable) {
            for (int i = 0; i < layer + 1; ++i) {
                builder.append("\t");
            }
            builder.append("{ ");
            builder.append(ToString(key, layer + 1, false, true, visitedKvs));
            builder.append(", ");
            builder.append(ToString(getter.apply(key), layer + 1, false, true, visitedKvs));
            builder.append("},");
            
            if (expand) {
                builder.append("\r\n");
            }
        }
        
        if (expand) {
            for (int i = 0; i < layer; ++i) {
                builder.append("\t");
            }
        }
        
        builder.append("}");
        return builder.toString();
    }
    
    private static String processIterable(int layer, boolean fromIterable, int length,
                                          Function<Integer, Object> getter, Map<Object, Boolean> visitedKvs) {
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
            builder.append(ToString(getter.apply(i), layer + 1, true, true, visitedKvs));
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
    
    private static void processField(int layer, StringBuilder builder, Map<Object, Boolean> visitedKvs, IField field) {
        if (field.ignore()) {
            return;
        }
        
        if (field.expand()) {
            for (int i = 0; i <= layer; ++i) {
                builder.append("\t");
            }
        }
        
        builder.append(field.name());
        builder.append(" = ");
        
        try {
            builder.append(ToString(field.value(), layer + 1, false, false, visitedKvs));
        } catch (Exception e) {
            logger.error("invoke getter failed", e);
        }
        builder.append(", ");
        
        if (field.expand()) {
            builder.append("\r\n");
        }
    }
}
