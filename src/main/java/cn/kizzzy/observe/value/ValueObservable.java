package cn.kizzzy.observe.value;

import cn.kizzzy.observe.IObserver;
import cn.kizzzy.observe.base.Observable;

import java.util.Objects;
import java.util.function.BiFunction;

public class ValueObservable<T> extends Observable<ValueObserveArgs<T>> {
    
    private T value;
    
    private final BiFunction<T, T, Boolean> equality;
    
    public ValueObservable() {
        this(null, null);
    }
    
    public ValueObservable(BiFunction<T, T, Boolean> equality) {
        this(equality, null);
    }
    
    public ValueObservable(BiFunction<T, T, Boolean> equality, T value) {
        if (equality == null) {
            equality = Objects::equals;
        }
        this.equality = equality;
        this.setValue(value);
    }
    
    public T getValue() {
        return value;
    }
    
    public void setValue(T value) {
        T oldValue = this.value;
        if (!equality.apply(oldValue, value)) {
            this.value = value;
            
            notify(new ValueObserveArgs<T>(oldValue, value));
        }
    }
    
    @Override
    protected void notifyNowImpl(IObserver<ValueObserveArgs<T>> listener) {
        listener.onNotify(new ValueObserveArgs<T>(getValue(), getValue()));
    }
}