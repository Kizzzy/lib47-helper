package cn.kizzzy.state;

public interface IState<T, R> {
    
    T getType();
    
    void SetMachine(IStateMachine<T, R> machine);
    
    void enter(T previous, R param);
    
    T update(R param);
    
    void exit(R param);
}
