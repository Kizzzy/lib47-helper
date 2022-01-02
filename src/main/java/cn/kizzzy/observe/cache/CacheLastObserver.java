package cn.kizzzy.observe.cache;

import cn.kizzzy.observe.base.ActionObserver;

import java.util.function.Consumer;

public class CacheLastObserver<T> extends ActionObserver<T> implements ICacheObserver<T> {
    
    protected T prev;
    
    protected T next;
    
    public CacheLastObserver(Consumer<T> callback) {
        super(callback);
    }
    
    public void onNotify(T args) {
        next = args;
    }
    
    public void execute() {
        T temp = next;
        if (prev != next) {
            prev = temp;
            
            if (callback != null) {
                callback.accept(temp);
            }
        }
    }
}
