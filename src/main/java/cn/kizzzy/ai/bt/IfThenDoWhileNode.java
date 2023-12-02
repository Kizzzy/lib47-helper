package cn.kizzzy.ai.bt;

import java.util.function.Supplier;

public class IfThenDoWhileNode extends ParallelNode {
    
    public IfThenDoWhileNode(int id, String name, Supplier<String> ifcond, Supplier<String> whilecond, Node child) {
        super(id, null, new MultiConditionNode(id, name, ifcond, whilecond), child);
    }
}
