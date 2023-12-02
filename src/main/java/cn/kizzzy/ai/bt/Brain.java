package cn.kizzzy.ai.bt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Brain {
    
    protected Inst inst;
    protected Object currentbehaviour;
    protected Object behaviourqueue;
    protected Map<String, Consumer<Object>> events;
    private long thinkperiod;
    private long lastthinktime;
    private boolean paused;
    
    private BT bt;
    private Consumer<Brain>[] modpostinitfns;
    private boolean stopped;
    
    public Brain() {
        this.inst = null;
        this.currentbehaviour = null;
        this.behaviourqueue = null;
        this.events = new HashMap<>();
        this.thinkperiod = 0;
        this.lastthinktime = 0;
        this.paused = false;
    }
    
    public void forceUpdate() {
        if (this.bt != null) {
            this.bt.forceUpdate();
        }
        
        //// BrainManager.Wake(this);
    }
    
    public void addEventHandler(String event, Consumer<Object> fn) {
        this.events.put(event, fn);
    }
    
    public long getSleepTime() {
        if (this.bt != null) {
            return this.bt.getSleepTime();
        }
        
        return 0;
    }
    
    public void start() {
        if (this.paused) {
            return;
        }
        
        this.onStart();
        this.stopped = false;
        // BrainManager.AddInstance(this)
        this.onInitializationComplete();
        
        // apply mods
        if (this.modpostinitfns != null) {
            for (Consumer<Brain> consumer : this.modpostinitfns) {
                consumer.accept(this);
            }
        }
    }
    
    public void onUpdate() {
        this.doUpdate();
        
        if (this.bt != null) {
            this.bt.update();
        }
    }
    
    public void stop() {
        if (this.paused) {
            return;
        }
        
        this.onStop();
        if (this.bt != null) {
            this.bt.stop();
        }
        
        this.stopped = true;
        // BrainManager.RemoveInstance(this)
    }
    
    public void pushEvent(String event, Object data) {
        Consumer<Object> handler = this.events.get(event);
        if (handler != null) {
            handler.accept(data);
        }
    }
    
    public void pause() {
        this.paused = true;
        // BrainManager.RemoveInstance(this);
    }
    
    public void resume() {
        this.paused = false;
        // BrainManager.AddInstance(this);
    }
    
    protected void onStart() {
        // todo
    }
    
    protected void onInitializationComplete() {
        // todo
    }
    
    protected void doUpdate() {
        // todo
    }
    
    protected void onStop() {
        // todo
    }
}
