package cn.kizzzy.ai.bt;

public class BT {
    
    private final Inst entity;
    
    private final Node root;
    
    private boolean forceUpdate;
    
    public BT(Inst entity, Node root) {
        this.entity = entity;
        this.root = root;
    }
    
    public void forceUpdate() {
        this.forceUpdate = true;
    }
    
    public void update() {
        root.visit();
        root.saveStatus();
        root.step();
        
        this.forceUpdate = false;
    }
    
    public void reset() {
        root.reset();
    }
    
    public void stop() {
        root.stop();
    }
    
    public long getSleepTime() {
        if (this.forceUpdate) {
            return 0;
        }
        return root.getTreeSleepTime();
    }
    
    @Override
    public String toString() {
        return root.toString();
    }
}
