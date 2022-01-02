package cn.kizzzy.observe.cache;

import cn.kizzzy.observe.IObserver;

public interface ICacheObserver<T> extends IObserver<T> {
    
    void execute();
}
