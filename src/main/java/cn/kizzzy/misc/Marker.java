package cn.kizzzy.misc;

import java.util.Objects;

public class Marker {
    
    private volatile boolean flag;
    
    private final Runnable task;
    
    public Marker(final Runnable task) {
        Objects.requireNonNull(task);
        this.task = task;
    }
    
    public void mark() {
        flag = true;
    }
    
    public void run() {
        if (flag) {
            task.run();
            flag = false;
        }
    }
}
