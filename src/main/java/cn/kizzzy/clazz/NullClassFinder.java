package cn.kizzzy.clazz;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class NullClassFinder extends BaseClassFinder {
    
    @Override
    public List<Class<?>> find(URL url, ClassFilter filter) throws ClassNotFoundException, IOException {
        return new LinkedList<>();
    }
}
