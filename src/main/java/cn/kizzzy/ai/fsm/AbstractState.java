package cn.kizzzy.ai.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractState<StateType, Entity> implements State<StateType, Entity> {
    
    protected static final Logger logger
        = LoggerFactory.getLogger(AbstractState.class);
    
    protected final StateType stateType;
    
    protected StateMachine<StateType, Entity> machine;
    
    protected long enterTime;
    
    public AbstractState(StateType stateType) {
        this.stateType = stateType;
    }
    
    @Override
    public StateType getType() {
        return stateType;
    }
    
    @Override
    public void setMachine(StateMachine<StateType, Entity> machine) {
        this.machine = machine;
    }
    
    @Override
    public void enter(Entity entity) {
        // logger.debug("========== enter {} state ==========", stateType);
        
        this.enterTime = System.currentTimeMillis();
    }
    
    @Override
    public void exit(Entity entity) {
        // logger.debug("========== exit {} state ==========", stateType);
    }
}
