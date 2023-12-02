package cn.kizzzy.ai.bt;

public class NotDecorator extends Node {
    
    public NotDecorator(int id, String name, Node child) {
        super(id, name != null ? name : "Not", child);
    }
    
    @Override
    public void visit() {
        for (Node child : children) {
            child.visit();
            
            if (child.status == Status.SUCCESS) {
                this.status = Status.FAILED;
            } else if (child.status == Status.FAILED) {
                this.status = Status.SUCCESS;
            } else {
                this.status = child.status;
            }
        }
    }
}
