package cn.kizzzy.observe;

public interface IObserver<T> {
    
    void onNotify(T args);
}
