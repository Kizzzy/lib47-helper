package cn.kizzzy.misc;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Similar<T> implements Iterable<T> {
    
    private final Class<T> tClass;
    
    private final List<T> list = new LinkedList<>();
    
    public Similar(final Class<T> tClass) {
        this.tClass = tClass;
    }
    
    @SuppressWarnings("unchecked")
    public void add(Object object) {
        if (tClass.isInstance(object)) {
            list.add((T) object);
        }
    }
    
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}
