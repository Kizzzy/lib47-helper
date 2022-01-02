package cn.kizzzy.clazz;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface ClassFinder {
    
    List<Class<?>> find(URL url, ClassFilter filter) throws ClassNotFoundException, IOException;
}
