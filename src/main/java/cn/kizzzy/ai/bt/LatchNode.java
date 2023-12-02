package cn.kizzzy.ai.bt;

public class LatchNode extends Node {
    
    private final Inst inst;
    private final long latchduration;
    
    private long currentlatchduration;
    private long lastlatchtime;
    
    public LatchNode(int id, Inst inst, long latchduration, Node child) {
        super(id, "Latch (" + latchduration + ")", child);
        this.inst = inst;
        this.latchduration = latchduration;
        
        this.currentlatchduration = 0;
        this.lastlatchtime = Integer.MIN_VALUE;
    }
    
    @Override
    public void visit() {
        if (this.status == Status.READY) {
            if (getTime() > this.currentlatchduration + this.lastlatchtime) {
                //print("GONNA GO!", GetTime(), this.currentlatchduration, "----", GetTime() + this.currentlatchduration, ">", this.lastlatchtime);
                this.lastlatchtime = getTime();
                this.currentlatchduration = this.latchduration;
                // print("New vals:", this.currentlatchduration, this.lastlatchtime);
                
                this.status = Status.RUNNING;
            } else {
                this.status = Status.FAILED;
            }
        }
        
        if (this.status == Status.RUNNING) {
            this.children[1].visit();
            this.status = this.children[1].status;
        }
    }
}
