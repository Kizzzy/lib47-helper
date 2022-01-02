package cn.kizzzy.observe.delay;

import cn.kizzzy.observe.base.Observables;

public class DelayObservables<T, R> extends Observables<T, R, DelayObservable<R>> implements IDelayObservables<T, R> {
    
    public DelayObservables() {
        super(DelayObservable::new);
    }
    
    public void execute() {
        observableKvs.forEach((k, v) -> v.execute());
    }
    
    @Override
    public void delayNotify(T type, R args) {
        getOrAddObservable(type).delayNotify(args);
    }
}
