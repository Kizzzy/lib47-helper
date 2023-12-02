package cn.kizzzy.ai.bt;

import java.util.function.Supplier;

public class MultiConditionNode extends Node {
    
    private final Supplier<String> start;
    private final Supplier<String> continue_;
    
    private String running;
    
    public MultiConditionNode(int id, String name, Supplier<String> start, Supplier<String> continue_) {
        super(id, name != null ? name : "Condition");
        this.start = start;
        this.continue_ = continue_;
    }
    
    @Override
    public void visit() {
        if (this.running == null) {
            this.running = start.get();
        } else {
            this.running = continue_.get();
        }
        
        if (this.running != null) {
            this.status = Status.SUCCESS;
        } else {
            this.status = Status.FAILED;
        }
    }
}
