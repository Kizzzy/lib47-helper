package cn.kizzzy.state;

import java.util.HashMap;
import java.util.Map;

public class DefaultStateMachine<T, R> implements IStateMachine<T, R> {
    protected T defaultType;
    protected IState<T, R> current;
    protected IState<T, R> previous;
    protected Map<T, IState<T, R>> stateMap = new HashMap<>();
    
    public DefaultStateMachine(IState<T, R>[] states, T defaultType) {
        for (IState<T, R> state : states) {
            addState(state);
        }
        this.defaultType = defaultType;
    }
    
    @Override
    public void addState(IState<T, R> state) {
        stateMap.put(state.getType(), state);
        state.SetMachine(this);
    }
    
    @Override
    public IState<T, R> getState(T type) {
        return stateMap.get(type);
    }
    
    public IState<T, R> getCurrent() {
        return current;
    }
    
    public IState<T, R> getPrevious() {
        return previous;
    }
    
    @Override
    public void update(R param) {
        if (current == null) {
            current = getState(defaultType);
            if (current != null) {
                current.enter(defaultType, param);
            }
        }
        
        if (current != null) {
            IState<T, R> newState = getState(current.update(param));
            if (newState != null) {
                current.exit(param);
                previous = current;
                current = newState;
                current.enter(previous.getType(), param);
            }
        }
    }
}
