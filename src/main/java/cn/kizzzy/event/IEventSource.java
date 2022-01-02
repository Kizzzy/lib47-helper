package cn.kizzzy.event;

public interface IEventSource {
    boolean addListener(int eventType, IEventListener listener);

    boolean removeListener(int eventType, IEventListener listener);

    boolean notifyListener(EventArgs arg);

    boolean notifyListener(int type, Object params);

    boolean notifyListener(int type);
}
