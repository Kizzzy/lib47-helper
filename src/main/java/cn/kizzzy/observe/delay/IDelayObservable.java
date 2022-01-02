package cn.kizzzy.observe.delay;

import cn.kizzzy.observe.IObservable;

public interface IDelayObservable<T> extends IObservable<T> {
    
    void delayNotify(T args);
}