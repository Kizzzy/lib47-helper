package cn.kizzzy.ai.bt;

import java.util.function.Supplier;

public class IfNode extends SequenceNode {
    
    public IfNode(int id, String name, Supplier<Boolean> fn, Node child) {
        super(id, null, new ConditionNode(id, name, fn), child);
    }
}
