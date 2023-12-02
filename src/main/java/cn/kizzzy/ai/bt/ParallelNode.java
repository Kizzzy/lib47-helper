package cn.kizzzy.ai.bt;

public class ParallelNode extends Node {
    
    protected boolean stoponanycomplete;
    
    public ParallelNode(int id, String name, Node... children) {
        super(id, name != null ? name : "Parallel", children);
    }
    
    @Override
    public void step() {
        if (this.status != Status.RUNNING) {
            reset();
        }
        
        for (Node child : children) {
            if (child.status == Status.SUCCESS && child instanceof ConditionNode) {
                child.reset();
            }
        }
    }
    
    @Override
    public void visit() {
        boolean done = true;
        boolean any_done = false;
        for (Node child : children) {
            if (child instanceof ConditionNode) {
                child.reset();
            }
            
            if (child.status != Status.SUCCESS) {
                child.visit();
                if (child.status == Status.FAILED) {
                    this.status = Status.FAILED;
                    return;
                }
            }
            
            if (child.status == Status.RUNNING) {
                done = false;
            } else {
                any_done = true;
            }
            
        }
        
        if (done || (this.stoponanycomplete && any_done)) {
            this.status = Status.SUCCESS;
        } else {
            this.status = Status.RUNNING;
        }
    }
}
