package cn.kizzzy.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassFinderHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(ClassFinderHelper.class);
    
    private static final ClassFinder fileClassFinder = new FileClassFinder();
    private static final ClassFinder jarClassFinder = new JarClassFinder();
    private static final ClassFinder nullClassFinder = new NullClassFinder();
    
    private static final ClassFilter defaultFilter = new DefaultClassFilter();
    
    private static ClassFinder getClassFinder(URL url) {
        switch (url.getProtocol()) {
            case "file":
                return fileClassFinder;
            case "jar":
                return jarClassFinder;
            default:
                return nullClassFinder;
        }
    }
    
    public static List<Class<?>> find() {
        return find(defaultFilter);
    }
    
    public static List<Class<?>> find(String packageRoot) {
        return find(new ClassFilter() {
            
            @Override
            public String packageRoot() {
                return packageRoot;
            }
            
            @Override
            public boolean isRecursive() {
                return true;
            }
            
            @Override
            public boolean accept(Class<?> clazz) {
                return true;
            }
        });
    }
    
    public static List<Class<?>> find(String packageRoot, boolean isRecursive) {
        return find(new ClassFilter() {
            
            @Override
            public String packageRoot() {
                return packageRoot;
            }
            
            @Override
            public boolean isRecursive() {
                return isRecursive;
            }
            
            @Override
            public boolean accept(Class<?> clazz) {
                return true;
            }
        });
    }
    
    public static List<Class<?>> find(ClassFilter filter) {
        return find(Thread.currentThread().getContextClassLoader(), filter);
    }
    
    public static List<Class<?>> find(ClassLoader loader, ClassFilter filter) {
        StringBuilder builder = new StringBuilder();
        
        List<Class<?>> list = new ArrayList<>();
        try {
            String path = filter.packageRoot().replaceAll("\\.", "/");
            Enumeration<URL> urls = loader.getResources(path);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                builder.append("find class at: ").append(url).append("\r\n");
                
                list.addAll(getClassFinder(url).find(url, filter));
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error("find class failed", e);
        } finally {
            for (Class<?> clazz : list) {
                builder.append("class found: ").append(clazz.getName()).append("\r\n");
            }
            logger.info(builder.toString());
        }
        return list;
    }
}
