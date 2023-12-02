package cn.kizzzy.ai.bt;

public class FailIfSuccessDecorator extends Node {
    
    public FailIfSuccessDecorator(int id, String name, Node child) {
        super(id, name != null ? name : "FailIfSuccess", child);
    }
    
    @Override
    public void visit() {
        for (Node child : children) {
            child.visit();
            
            if (child.status == Status.SUCCESS) {
                this.status = Status.FAILED;
            } else {
                this.status = child.status;
            }
        }
    }
}
