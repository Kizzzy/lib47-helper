package cn.kizzzy.observe;

import java.util.function.Consumer;

public interface IObservable<T> {
    
    /*default void add(Consumer<T> callback) {
        add(callback, false);
    }
    
    void add(Consumer<T> callback, boolean notifyNow);*/
    
    default void add(IObserver<T> listener) {
        add(listener, false);
    }
    
    void add(IObserver<T> listener, boolean notifyNow);
    
    /*default void remove(Consumer<T> callback) {
        remove(callback, false);
    }
    
    void remove(Consumer<T> callback, boolean notifyNow);*/
    
    default void remove(IObserver<T> listener) {
        remove(listener, false);
    }
    
    void remove(IObserver<T> listener, boolean notifyNow);
    
    void notify(T args);
}