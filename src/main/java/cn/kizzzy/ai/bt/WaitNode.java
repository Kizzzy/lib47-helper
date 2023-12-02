package cn.kizzzy.ai.bt;

public class WaitNode extends Node {
    
    private final long wait_time;
    
    private long walk_time;
    
    public WaitNode(int id, String name, long wait_time) {
        super(id, name != null ? name : "Wait");
        this.wait_time = wait_time;
    }
    
    @Override
    public void visit() {
        long current_time = getTime();
        
        if (status != Status.RUNNING) {
            this.walk_time = current_time + this.wait_time;
            this.status = Status.SUCCESS;
        }
        
        if (status == Status.RUNNING) {
            if (current_time >= this.wait_time) {
                this.status = Status.SUCCESS;
            } else {
                sleep(current_time - this.walk_time);
            }
        }
    }
}
