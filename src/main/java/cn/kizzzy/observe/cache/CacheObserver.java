package cn.kizzzy.observe.cache;

import cn.kizzzy.observe.base.ActionObserver;

import java.util.function.Consumer;

public class CacheObserver<T> extends ActionObserver<T> implements ICacheObserver<T> {
    
    protected CacheNode<T> head;
    
    protected CacheNode<T> tail;
    
    public CacheObserver(Consumer<T> callback) {
        super(callback);
        head = tail = new CacheNode<>();
    }
    
    public void onNotify(T args) {
        CacheNode<T> node = new CacheNode<>();
        node.setData(args);
        
        tail.setNext(node);
        tail = node;
    }
    
    public void execute() {
        while (head != tail) {
            CacheNode<T> temp = head.getNext();
            if (temp != null) {
                head = temp;
                
                if (callback != null) {
                    callback.accept(temp.getData());
                }
            }
        }
    }
}
