package cn.kizzzy.database;

public class DataObject {
    private int opType = Option.NONE__;
    
    public int getOpType() {
        return opType;
    }
    
    public void setOpType(int opType) {
        if (this.opType == Option.INSERT && opType == Option.UPDATE) {
            return;
        }
        this.opType = opType;
    }
    
    public boolean beginAdd() {
        if (getOpType() == Option.INSERT) {
            setOpType(Option.NONE__);
            return true;
        }
        return false;
    }
    
    public void commitAdd(boolean result) {
        if (!result) {
            setOpType(Option.INSERT);
        }
    }
    
    public boolean beginUpdate() {
        if (getOpType() == Option.UPDATE) {
            setOpType(Option.NONE__);
            return true;
        }
        return false;
    }
    
    public void commitUpdate(boolean result) {
        if (!result) {
            setOpType(Option.UPDATE);
        }
    }
}
