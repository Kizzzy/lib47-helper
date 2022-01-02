package cn.kizzzy.observe.base;

import cn.kizzzy.observe.IObserveArgs;

public class ObserveArgs<T> implements IObserveArgs<T> {
    
    protected final T type;
    
    protected final Object source;
    
    protected final Object params;
    
    public ObserveArgs(T type) {
        this(type, null, null);
    }
    
    public ObserveArgs(T type, Object source, Object param) {
        this.type = type;
        this.source = source;
        this.params = param;
    }
    
    public T getType() {
        return type;
    }
    
    public Object getSource() {
        return source;
    }
    
    public Object getParams() {
        return params;
    }
}
