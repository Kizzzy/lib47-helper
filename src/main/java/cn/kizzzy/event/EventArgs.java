package cn.kizzzy.event;

import java.util.EventObject;

public class EventArgs extends EventObject {
    private int type;
    private Object params;
    
    public EventArgs(Object source, int type, Object params) {
        super(source);
        this.type = type;
        this.setParams(params);
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public Object getParams() {
        return params;
    }
    
    public void setParams(Object params) {
        this.params = params;
    }
}
