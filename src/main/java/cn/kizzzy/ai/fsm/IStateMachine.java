package cn.kizzzy.ai.fsm;

public interface IStateMachine<T, R> {
    
    void addState(IState<T, R> state);
    
    IState<T, R> getState(T type);
    
    void update(R param);
}
