package cn.kizzzy.ai.bt;

public class ParallelNodeAny extends ParallelNode {
    
    public ParallelNodeAny(int id, String name, Node... children) {
        super(id, name != null ? name : "Parallel", children);
        
        this.stoponanycomplete = true;
    }
}
