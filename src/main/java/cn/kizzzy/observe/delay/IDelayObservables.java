package cn.kizzzy.observe.delay;

import cn.kizzzy.observe.IObservables;

public interface IDelayObservables<T, R> extends IObservables<T, R> {
    
    void delayNotify(T type, R args);
}