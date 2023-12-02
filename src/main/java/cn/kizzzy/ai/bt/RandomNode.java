package cn.kizzzy.ai.bt;

import cn.kizzzy.helper.RandomHelper;

public class RandomNode extends Node {
    
    private int idx;
    
    public RandomNode(int id, Node... children) {
        super(id, "Random", children);
        
        this.idx = 0;
    }
    
    @Override
    public void reset() {
        super.reset();
        
        this.idx = 0;
    }
    
    @Override
    public void visit() {
        if (this.status == Status.READY) {
            this.idx = RandomHelper.next(0, children.length);
            int start = this.idx;
            
            while (true) {
                Node child = children[this.idx];
                child.visit();
                
                if (child.status != Status.FAILED) {
                    this.status = child.status;
                }
                
                this.idx++;
                if (this.idx >= children.length) {
                    this.idx = 0;
                }
                
                if (this.idx == start) {
                    this.status = Status.FAILED;
                    return;
                }
            }
        } else {
            Node child = children[this.idx];
            child.visit();
            this.status = child.status;
        }
    }
}
