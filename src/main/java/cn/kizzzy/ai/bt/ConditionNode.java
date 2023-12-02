package cn.kizzzy.ai.bt;

import java.util.function.Supplier;

public class ConditionNode extends Node {
    
    private final Supplier<Boolean> fn;
    
    public ConditionNode(int id, String name, Supplier<Boolean> fn) {
        super(id, name != null ? name : "Condition");
        this.fn = fn;
    }
    
    @Override
    public void visit() {
        if (this.fn.get()) {
            this.status = Status.SUCCESS;
        } else {
            this.status = Status.FAILED;
        }
    }
}
