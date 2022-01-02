package cn.kizzzy.observe.base;

import cn.kizzzy.observe.IObservable;
import cn.kizzzy.observe.IObservables;
import cn.kizzzy.observe.IObserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Observables<T, R, W extends IObservable<R>> implements IObservables<T, R> {
    
    protected final Supplier<W> factory;
    
    protected final Map<T, W> observableKvs =
        new ConcurrentHashMap<>();
    
    public Observables(Supplier<W> factory) {
        this.factory = factory;
    }
    /*
    @Override
    public void add(Map<T, Consumer<R>> kvs, boolean notifyNow) {
        kvs.forEach((k, v) -> add(k, v, notifyNow));
    }
    
    @Override
    public void add(T type, Consumer<R> callback, boolean notifyNow) {
        add(type, new ActionObserver<>(callback), notifyNow);
    }
    */
    @Override
    public void add(T type, IObserver<R> listener, boolean notifyNow) {
        getOrAddObservable(type).add(listener);
        if (notifyNow) {
            notifyNowImpl(listener);
        }
    }
    /*
    @Override
    public void remove(Map<T, Consumer<R>> kvs, boolean notifyNow) {
        kvs.forEach((k, v) -> remove(k, v, notifyNow));
    }
    
    @Override
    public void remove(T type, Consumer<R> callback, boolean notifyNow) {
        remove(type, new ActionObserver<>(callback), notifyNow);
    }
    */
    @Override
    public void remove(T type, IObserver<R> listener, boolean notifyNow) {
        getOrAddObservable(type).remove(listener);
        if (notifyNow) {
            notifyNowImpl(listener);
        }
    }
    
    @Override
    public void notify(T type, R arg) {
        if (arg != null) {
            getOrAddObservable(type).notify(arg);
        }
    }
    
    protected void notifyNowImpl(IObserver<R> listener) {
    
    }
    
    protected W getOrAddObservable(T type) {
        return observableKvs.computeIfAbsent(type, k -> factory.get());
    }
}
