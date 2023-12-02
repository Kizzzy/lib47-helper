package cn.kizzzy.ai.bt;

import java.util.function.Supplier;

public class ConditionWaitNode extends Node {
    
    private final Supplier<Boolean> fn;
    
    public ConditionWaitNode(int id, String name, Supplier<Boolean> fn) {
        super(id, name != null ? name : "Wait");
        this.fn = fn;
    }
    
    @Override
    public void visit() {
        if (this.fn.get()) {
            this.status = Status.SUCCESS;
        } else {
            this.status = Status.RUNNING;
        }
    }
}
