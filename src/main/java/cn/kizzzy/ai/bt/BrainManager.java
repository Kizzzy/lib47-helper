package cn.kizzzy.ai.bt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrainManager {
    
    private Map<Inst, Map<Inst, Boolean>> instances;
    private Map<Inst, Boolean> updaters;
    private Map<Integer, Inst> _safe_updaters;
    private Map<Long, Map<Inst, Boolean>> tickwaiters;
    private Map<Inst, Boolean> hibernaters;
    
    public BrainManager() {
        this.instances = new HashMap<>();
        this.updaters = new HashMap<>();
        this._safe_updaters = new HashMap<>();
        this.tickwaiters = new HashMap<>();
        this.hibernaters = new HashMap<>();
    }
    
    public void onRemoveEntity(Inst inst) {
        // print ("onremove", inst, debugstack());
        if (inst.brain != null && this.instances.get(inst.brain) != null) {
            this.removeInstance(inst.brain);
        }
    }
    
    
    public String nameList(List<?> list) {
        if (list == null) {
            return "null";
        } else if (list == this.updaters) {
            return "updaters";
        } else if (list == this.hibernaters) {
            return "hibernators";
        } else {
            for (Map.Entry<Long, Map<Inst, Boolean>> kv : this.tickwaiters.entrySet()) {
                if (list == kv.getValue()) {
                    return "tickwaiter " + kv.getKey();
                }
            }
        }
        
        return "Unknown";
    }
    
    public void sendToList(Inst inst, Map<Inst, Boolean> list) {
        Map<Inst, Boolean> old_list = this.instances.get(inst);
        //     print ("HI!", inst.inst, this.NameList(old_list), this.NameList(list))
        if (old_list != null && old_list != list) {
            old_list.put(inst, null);
            
            this.instances.put(inst, list);
            
            if (list != null) {
                list.put(inst, true);
            }
        }
    }
    
    public void wake(Inst inst) {
        if (this.instances.get(inst) != null) {
            this.sendToList(inst, this.updaters);
        }
    }
    
    public void hibernate(Inst inst) {
        if (this.instances.get(inst) != null) {
            this.sendToList(inst, this.hibernaters);
        }
    }
    
    public void sleep(Inst inst, long time_to_wait) {
        long sleep_ticks = time_to_wait / getTickTime();
        if (sleep_ticks == 0) {
            sleep_ticks = 1;
        }
        
        long target_tick = (long) Math.floor(getTick() + sleep_ticks);
        
        if (target_tick > getTick()) {
            Map<Inst, Boolean> waiters = this.tickwaiters.get(target_tick);
            if (waiters != null) {
                waiters = new HashMap<>();
                this.tickwaiters.put(target_tick, waiters);
            }
            
            // print ("BRAIN SLEEPS", inst.inst)
            this.sendToList(inst, waiters);
        }
    }
    
    
    public void removeInstance(Inst inst) {
        this.sendToList(inst, null);
        this.updaters.put(inst, null);
        this.hibernaters.put(inst, null);
        for (Map.Entry<Long, Map<Inst, Boolean>> kv : this.tickwaiters.entrySet()) {
            kv.getValue().put(inst, null);
        }
        this.instances.put(inst, null);
    }
    
    public void addInstance(Inst inst) {
        this.instances.put(inst, this.updaters);
        this.updaters.put(inst, true);
    }
    
    public void update(long current_tick) {
        // [[
        long num = 0;
        Map<String, Integer> types = new HashMap<>();
        for (Map.Entry<Inst, Map<Inst, Boolean>> kv : this.instances.entrySet()) {
            num = num + 1;
            //types[kv.getKey().inst.prefab] = types[kv.getKey().inst.prefab] && types[kv.getKey().inst.prefab] + 1 || 1;
        }
        // print ("NUM BRAINS.", num);
        /*for (k, v in pairs(types)){
            print ("    ",k,v);
        }*/
        // ]]
        
        
        Map<Inst, Boolean> waiters = this.tickwaiters.get(current_tick);
        if (waiters != null) {
            for (Map.Entry<Inst, Boolean> kv : waiters.entrySet()) {
                // print ("BRAIN COMES ONLINE", k.inst)
                this.updaters.put(kv.getKey(), true);
                this.instances.put(kv.getKey(), this.updaters);
            }
            this.tickwaiters.put(current_tick, null);
        }
        
        
        //  NOTES(JBK). We need to make a copy of the keys to safely iterate over the table because brains will remove &&  add onto the this.updaters table during iteration.
        int count = 0;
        for (Inst k : this.updaters.keySet()) {
            if (k.inst.entity.isValid() && !k.inst.isAsleep()) {
                count = count + 1;
                this._safe_updaters.put(count, k);
            }
        }
        for (int i = 1; i < count; ++i) {
            Inst k = this._safe_updaters.get(i);
            this._safe_updaters.put(i, null);
            
            k.onUpdate();
            long sleep_amount = k.getSleepTime();
            if (sleep_amount > 0) {
                if (sleep_amount > getTickTime()) {
                    this.sleep(k, sleep_amount);
                } else {
                    // todo
                }
            } else {
                this.hibernate(k);
            }
        }
    }
    
    private long getTick() {
        return 1;
    }
    
    private long getTickTime() {
        return 1;
    }
}
