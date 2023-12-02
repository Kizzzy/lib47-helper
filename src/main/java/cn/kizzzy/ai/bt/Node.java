package cn.kizzzy.ai.bt;

import java.util.function.Consumer;

public class Node {
    
    protected final int id;
    protected final String name;
    protected final Node[] children;
    
    protected Status status;
    protected Status lastresult;
    protected long nextupdatetick;
    
    protected Node parent;
    
    public Node(int id, String name, Node... children) {
        this.id = id;
        this.name = name;
        this.children = children;
        
        this.status = Status.READY;
        this.lastresult = Status.READY;
        this.nextupdatetick = 0;
        
        if (children != null) {
            for (Node child : children) {
                child.parent = this;
            }
        }
    }
    
    public void doToParents(Consumer<Node> fn) {
        if (parent != null) {
            fn.accept(parent);
            parent.doToParents(fn);
        }
    }
    
    public void sleep(long t) {
        this.nextupdatetick = getTime() + t;
    }
    
    public long getSleepTime() {
        if (status == Status.RUNNING && children != null && !(this instanceof ConditionNode)) {
            if (this.nextupdatetick > 0) {
                long time_to = this.nextupdatetick - getTime();
                if (time_to < 0) {
                    time_to = 0;
                }
                return time_to;
            }
            return 0;
        }
        return 0;
    }
    
    public long getTreeSleepTime() {
        long sleeptime = 0;
        if (children != null) {
            for (Node child : children) {
                if (child.status == Status.RUNNING) {
                    long t = child.getTreeSleepTime();
                    if (t > 0 && (sleeptime == 0 || sleeptime > t)) {
                        sleeptime = t;
                    }
                }
            }
        }
        long my_t = parent.getSleepTime();
        
        if (my_t > 0 && (sleeptime == 0 || sleeptime > my_t)) {
            sleeptime = my_t;
        }
        return sleeptime;
    }
    
    public void visit() {
        this.status = Status.FAILED;
    }
    
    public void saveStatus() {
        this.lastresult = status;
        
        if (children != null) {
            for (Node child : children) {
                child.saveStatus();
            }
        }
    }
    
    public void step() {
        if (status != Status.RUNNING) {
            this.reset();
        } else if (children != null) {
            for (Node child : children) {
                child.step();
            }
        }
    }
    
    public void reset() {
        if (status != Status.READY) {
            this.status = Status.READY;
            
            if (children != null) {
                for (Node child : children) {
                    child.step();
                }
            }
        }
    }
    
    public void stop() {
        onStop();
        
        if (children != null) {
            for (Node child : children) {
                child.stop();
            }
        }
    }
    
    protected void onEvent(Object data) {
    
    }
    
    protected void onStop() {
        // todo
    }
    
    protected long getTime() {
        return System.currentTimeMillis();
    }
}
