package cn.kizzzy.ai.bt;

public class FailIfRunningDecorator extends Node {
    
    public FailIfRunningDecorator(int id, String name, Node child) {
        super(id, name != null ? name : "FailIfRunning", child);
    }
    
    @Override
    public void visit() {
        for (Node child : children) {
            child.visit();
            
            if (child.status == Status.RUNNING) {
                this.status = Status.FAILED;
            } else {
                this.status = child.status;
            }
        }
    }
}
