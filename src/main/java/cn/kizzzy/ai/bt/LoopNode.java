package cn.kizzzy.ai.bt;

public class LoopNode extends Node {
    
    private final int maxreps;
    
    private int idx;
    private int rep;
    
    public LoopNode(int id, int maxreps, Node... children) {
        super(id, "Sequence", children);
        this.maxreps = maxreps;
        
        this.idx = 0;
        this.rep = 0;
    }
    
    @Override
    public void reset() {
        super.reset();
        
        this.idx = 0;
        this.rep = 0;
    }
    
    @Override
    public void visit() {
        if (this.status != Status.RUNNING) {
            this.idx = 0;
            this.rep = 0;
        }
        
        for (; this.idx <= this.children.length; ++this.idx) {
            Node child = children[this.idx];
            child.visit();
            if (child.status == Status.RUNNING || child.status == Status.FAILED) {
                if (child.status == Status.FAILED) {
                    System.out.println("EXIT LOOP ON FAIL");
                }
                
                this.status = child.status;
            }
        }
        
        this.idx = 0;
        this.rep++;
        if (this.maxreps > 0 && this.rep >= this.maxreps) {
            this.status = Status.SUCCESS;
        } else {
            for (Node child : children) {
                child.reset();
            }
        }
    }
}
