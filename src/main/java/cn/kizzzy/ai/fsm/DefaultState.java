package cn.kizzzy.ai.fsm;

public abstract class DefaultState<T, R> implements IState<T, R> {
    protected long enterTime;
    protected T previous;
    protected R param;
    protected IStateMachine<T, R> machine;
    
    @Override
    public void SetMachine(IStateMachine<T, R> machine) {
        this.machine = machine;
    }
    
    @Override
    public void enter(T previous, R param) {
        this.previous = previous;
        this.param = param;
        this.enterTime = System.currentTimeMillis();
    }
    
    @Override
    public void exit(R param) {
    
    }
}
