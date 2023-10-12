package cn.kizzzy.observe.delay;

import cn.kizzzy.observe.base.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

public class DelayObservable<T> extends Observable<T> implements IDelayObservable<T> {
    
    private static final Logger logger = LoggerFactory.getLogger(DelayObservable.class);
    
    private final Queue<T> queue =
        new LinkedList<>();
    
    public void execute() {
        synchronized (queue) {
            try {
                while (!queue.isEmpty()) {
                    notify(queue.poll());
                }
            } catch (Exception e) {
                logger.error("notify error, ", e);
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
