package cn.kizzzy.clazz;

import cn.kizzzy.helper.LogHelper;

import java.util.List;

public abstract class BaseClassFinder implements ClassFinder {
    
    protected void loadClass(List<Class<?>> list, String name, ClassFilter filter) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(name);
        if (clazz != null && filter.accept(clazz)) {
            LogHelper.debug("find accepted class: {}", clazz);
            list.add(clazz);
        }
    }
}
