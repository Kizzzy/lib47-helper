package cn.kizzzy.ai.bt;

public class SelectorNode extends Node {
    
    private int idx;
    
    public SelectorNode(int id, Node... children) {
        super(id, "Selector", children);
        this.idx = 0;
    }
    
    @Override
    public void reset() {
        super.reset();
        this.idx = 0;
    }
    
    @Override
    public void visit() {
        if (status != Status.RUNNING) {
            this.idx = 0;
        }
        
        for (; this.idx <= this.children.length; ++this.idx) {
            Node child = children[this.idx];
            child.visit();
            if (child.status == Status.RUNNING || child.status == Status.SUCCESS) {
                this.status = child.status;
            }
        }
        
        this.status = Status.FAILED;
    }
}
