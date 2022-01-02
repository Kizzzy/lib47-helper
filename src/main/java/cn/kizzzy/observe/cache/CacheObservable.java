package cn.kizzzy.observe.cache;

import cn.kizzzy.observe.IObserver;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CacheObservable {
    
    private final List<Runnable> actions =
        new LinkedList<>();
    
    public <T> IObserver<T> add(ICacheObserver<T> observer) {
        actions.add(new ActionWrapper<>(observer));
        return observer;
    }
    
    public <T> void remove(ICacheObserver<T> observer) {
        actions.remove(new ActionWrapper<>(observer));
    }
    
    public void execute() {
        List<Runnable> temp = new LinkedList<>(actions);
        for (Runnable action : temp) {
            action.run();
        }
    }
    
    private static class ActionWrapper<T> implements Runnable {
        
        private final ICacheObserver<T> observer;
        
        private ActionWrapper(ICacheObserver<T> observer) {
            this.observer = observer;
        }
        
        @Override
        public void run() {
            observer.execute();
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            ActionWrapper<?> that = (ActionWrapper<?>) o;
            return Objects.equals(observer, that.observer);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(observer);
        }
    }
}
