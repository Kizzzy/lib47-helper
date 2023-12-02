package cn.kizzzy.ai.bt;

public class ActionNode extends Node {
    
    private final Runnable action;
    
    public ActionNode(int id, String name, Runnable action) {
        super(id, name != null ? name : "Action");
        this.action = action;
    }
    
    @Override
    public void visit() {
        this.action.run();
        this.status = Status.SUCCESS;
    }
}
