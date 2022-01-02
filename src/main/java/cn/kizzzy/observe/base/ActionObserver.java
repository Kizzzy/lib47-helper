package cn.kizzzy.observe.base;

import cn.kizzzy.observe.IObserver;

import java.util.Objects;
import java.util.function.Consumer;

public class ActionObserver<T> implements IObserver<T> {
    protected final Consumer<T> callback;
    
    public ActionObserver(Consumer<T> callback) {
        this.callback = callback;
    }
    
    public Consumer<T> getCallback() {
        return callback;
    }
    
    public void onNotify(T args) {
        if (callback != null) {
            callback.accept(args);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ActionObserver<?> that = (ActionObserver<?>) o;
        return Objects.equals(callback, that.callback);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(callback);
    }
}
