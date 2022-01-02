package cn.kizzzy.observe.value;

public class ValueObserveArgs<R> {
    
    protected final R oldValue;
    
    protected final R newValue;
    
    public ValueObserveArgs(R oldValue, R newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    public R getOldValue() {
        return oldValue;
    }
    
    public R getNewValue() {
        return newValue;
    }
}