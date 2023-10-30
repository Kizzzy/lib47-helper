package cn.kizzzy.ai.fsm;

import java.util.HashMap;
import java.util.Map;

public class FiniteStateMachine<StateType, Entity> implements StateMachine<StateType, Entity> {
    
    protected Map<StateType, State<StateType, Entity>> stateMap
        = new HashMap<>();
    
    protected Entity owner;
    protected StateType defaultType;
    
    protected State<StateType, Entity> current;
    protected State<StateType, Entity> previous;
    
    public FiniteStateMachine(Entity owner, StateType defaultType, State<StateType, Entity>... states) {
        this.owner = owner;
        this.defaultType = defaultType;
        
        for (State<StateType, Entity> state : states) {
            addState(state);
        }
    }
    
    @Override
    public void update() {
        if (current == null) {
            changeState(defaultType);
        }
        
        if (current != null) {
            StateType next = current.update(owner);
            changeState(next);
        }
    }
    
    protected void changeState(StateType stateType) {
        previous = current;
        if (current != null) {
            current.exit(owner);
        }
        
        current = getState(stateType);
        if (current != null) {
            current.enter(owner);
        }
    }
    
    @Override
    public void addState(State<StateType, Entity> state) {
        stateMap.put(state.getType(), state);
        state.setMachine(this);
    }
    
    @Override
    public State<StateType, Entity> getState(StateType type) {
        return type == null ? null : stateMap.get(type);
    }
    
    public State<StateType, Entity> getCurrent() {
        return current;
    }
    
    public State<StateType, Entity> getPrevious() {
        return previous;
    }
}
