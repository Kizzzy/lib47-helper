package cn.kizzzy.ai.fsm;

public interface State<StateType, Entity> {
    
    StateType getType();
    
    void setMachine(StateMachine<StateType, Entity> machine);
    
    void enter(Entity entity);
    
    StateType update(Entity entity);
    
    void exit(Entity entity);
}
