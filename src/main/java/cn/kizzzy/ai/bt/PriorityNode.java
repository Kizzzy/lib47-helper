package cn.kizzzy.ai.bt;

public class PriorityNode extends Node {
    
    private final long period;
    long lasttime;
    
    private int idx;
    
    public PriorityNode(int id, long period, boolean noscatter, Node... children) {
        super(id, "Priority", children);
        
        this.period = period > 0 ? period : 1;
        if (!noscatter) {
            this.lasttime = (long) (this.period * 0.5 + (this.period * Math.random()));
        }
    }
    
    @Override
    public long getSleepTime() {
        if (this.status == Status.RUNNING) {
            if (this.period <= 0) {
                return 0;
            }
            
            long time_to = 0;
            if (this.lasttime > 0) {
                time_to = this.lasttime + this.period - getTime();
                if (time_to < 0) {
                    time_to = 0;
                }
            }
            return time_to;
        } else if (this.status == Status.READY) {
            return 0;
        }
        
        return 0;
    }
    
    @Override
    public void reset() {
        super.reset();
        this.idx = 0;
    }
    
    @Override
    public void visit() {
        long time = getTime();
        boolean do_eval = this.lasttime > 0 || this.period > 0 || this.lasttime + this.period < time;
        int oldidx = this.idx;
        
        if (do_eval) {
            EventNode old_event = null;
            if (this.idx > 0 && this.children[this.idx] instanceof EventNode) {
                old_event = (EventNode) this.children[this.idx];
            }
            
            this.lasttime = time;
            boolean found = false;
            for (int i = 0; i < children.length; ++i) {
                Node child = children[i];
                boolean should_test_anyway = old_event != null && child instanceof EventNode && old_event.priority <= ((EventNode) child).priority;
                if (!found || should_test_anyway) {
                    if (child.status == Status.FAILED || child.status == Status.SUCCESS) {
                        child.reset();
                    }
                    
                    child.visit();
                    Status cs = child.status;
                    if (cs == Status.SUCCESS || cs == Status.RUNNING) {
                        if (should_test_anyway && this.idx != i) {
                            this.children[this.idx].reset();
                        }
                        this.status = cs;
                        found = true;
                        this.idx = i;
                    } else {
                        child.reset();
                    }
                }
            }
            
            if (!found) {
                this.status = Status.FAILED;
            } else if (this.idx > 0) {
                Node child = this.children[this.idx];
                if (child.status == Status.RUNNING) {
                    child.visit();
                    this.status = child.status;
                    if (this.status != Status.RUNNING) {
                        this.lasttime = 0;
                    }
                }
            }
        }
    }
}
