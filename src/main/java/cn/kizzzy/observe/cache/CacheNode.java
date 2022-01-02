package cn.kizzzy.observe.cache;

public class CacheNode<T> {
    
    public T data;
    
    public CacheNode<T> next;
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public CacheNode<T> getNext() {
        return next;
    }
    
    public void setNext(CacheNode<T> next) {
        this.next = next;
    }
}
