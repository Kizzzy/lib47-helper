package cn.kizzzy.observe.delay;

import cn.kizzzy.helper.LogHelper;
import cn.kizzzy.observe.base.Observable;

import java.util.LinkedList;
import java.util.Queue;

public class DelayObservable<T> extends Observable<T> implements IDelayObservable<T> {
    
    private final Queue<T> queue =
        new LinkedList<>();
    
    public void execute() {
        synchronized (queue) {
            try {
                while (queue.size() > 0) {
                    notify(queue.poll());
                }
            } catch (Exception e) {
                LogHelper.error(e);
            }
        }
    }
    
    @Override
    public void delayNotify(T args) {
        synchronized (queue) {
            queue.offer(args);
        }
    }
}
