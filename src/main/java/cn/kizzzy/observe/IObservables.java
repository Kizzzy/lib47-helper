package cn.kizzzy.observe;

import java.util.Map;
import java.util.function.Consumer;

public interface IObservables<T, R> {
    /*
    default void add(Map<T, Consumer<R>> kvs) {
        add(kvs, false);
    }
    
    void add(Map<T, Consumer<R>> kvs, boolean notifyNow);
    
    default void add(T type, Consumer<R> callback) {
        add(type, callback, false);
    }
    
    void add(T type, Consumer<R> callback, boolean notifyNow);
    */
    default void add(T type, IObserver<R> listener) {
        add(type, listener, false);
    }
    
    void add(T type, IObserver<R> listener, boolean notifyNow);
    /*
    default void remove(Map<T, Consumer<R>> kvs) {
        remove(kvs, false);
    }
    
    void remove(Map<T, Consumer<R>> kvs, boolean notifyNow);
    
    default void remove(T type, Consumer<R> callback) {
        remove(type, callback, false);
    }
    
    void remove(T type, Consumer<R> callback, boolean notifyNow);
    */
    default void remove(T type, IObserver<R> listener) {
        remove(type, listener, false);
    }
    
    void remove(T type, IObserver<R> listener, boolean notifyNow);
    
    void notify(T type, R args);
}