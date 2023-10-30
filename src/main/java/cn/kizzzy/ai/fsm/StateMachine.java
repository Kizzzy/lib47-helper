package cn.kizzzy.ai.fsm;

public interface StateMachine<StateType, Entity> {
    
    void addState(State<StateType, Entity> state);
    
    State<StateType, Entity> getState(StateType type);
    
    void update();
}
