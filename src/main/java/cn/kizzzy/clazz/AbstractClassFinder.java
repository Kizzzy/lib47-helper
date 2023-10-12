package cn.kizzzy.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractClassFinder implements ClassFinder {
    
    protected static final Logger logger = LoggerFactory.getLogger(AbstractClassFinder.class);
    
    protected void loadClass(List<Class<?>> list, String name, ClassFilter filter) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(name);
        if (filter.accept(clazz)) {
            logger.debug("accepted class: {}", clazz);
            list.add(clazz);
        }
    }
}
