package cn.kizzzy.ai.bt;

import java.util.function.BiConsumer;

public class EventNode extends Node {
    
    private final Inst inst;
    private final String event;
    final int priority;
    
    private BiConsumer<Inst, Object> eventfn;
    private boolean triggered;
    private Object data;
    
    public EventNode(int id, Inst inst, String event, int priority, Node child) {
        super(id, "Event (" + event + ")", child);
        
        this.inst = inst;
        this.event = event;
        this.priority = priority;
        
        this.eventfn = (_inst, data) -> this.onEvent(data);
        // this.inst:ListenForEvent(this.event, this.eventfn);
        // print(this.inst, "EventNode()", this.event);
    }
    
    @Override
    public void step() {
        super.step();
        this.triggered = false;
    }
    
    @Override
    public void reset() {
        this.triggered = false;
        super.reset();
    }
    
    @Override
    public void visit() {
        if (this.status == Status.READY && this.triggered) {
            this.status = Status.RUNNING;
        }
        
        if (this.status == Status.RUNNING) {
            if (this.children.length == 1) {
                Node child = this.children[0];
                child.visit();
                this.status = child.status;
            } else {
                this.status = Status.FAILED;
            }
        }
    }
    
    @Override
    protected void onStop() {
        // print(this.inst, "EventNode:OnStop()", this.event)
        if (this.eventfn != null) {
            // this.inst:RemoveEventCallback(this.event, this.eventfn);
            this.eventfn = null;
        }
    }
    
    @Override
    protected void onEvent(Object data) {
        // print(this.inst, "EventNode:OnEvent()", this.event)
        
        if (this.status == Status.RUNNING) {
            this.children[0].reset();
        }
        this.triggered = true;
        this.data = data;
        
        if (this.inst.brain != null) {
            this.inst.brain.forceUpdate();
        }
        
        this.doToParents(node -> {
            if (node instanceof PriorityNode) {
                ((PriorityNode) node).lasttime = 0;
            }
        });
        
        // wake the parent!
    }
}
