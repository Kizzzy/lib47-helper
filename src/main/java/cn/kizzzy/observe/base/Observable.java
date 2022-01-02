package cn.kizzzy.observe.base;

import cn.kizzzy.observe.IObservable;
import cn.kizzzy.observe.IObserver;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Observable<T> implements IObservable<T> {
    
    private final List<IObserver<T>> listeners =
        new LinkedList<>();
    
    /*@Override
    public void add(Consumer<T> callback, boolean notifyNow) {
        add(new ActionObserver<T>(callback), notifyNow);
    }*/
    
    @Override
    public void add(IObserver<T> listener, boolean notifyNow) {
        listeners.add(listener);
        if (notifyNow) {
            notifyNowImpl(listener);
        }
    }
    
    /*@Override
    public void remove(Consumer<T> callback, boolean notifyNow) {
        remove(new ActionObserver<T>(callback), notifyNow);
    }*/
    
    @Override
    public void remove(IObserver<T> listener, boolean notifyNow) {
        if (listeners.remove(listener) && notifyNow) {
            notifyNowImpl(listener);
        }
    }
    
    @Override
    public void notify(T arg) {
        if (arg != null) {
            List<IObserver<T>> list = new LinkedList<>(listeners);
            for (IObserver<T> listener : list) {
                listener.onNotify(arg);
            }
        }
    }
    
    protected void notifyNowImpl(IObserver<T> listener) {
        
    }
}
