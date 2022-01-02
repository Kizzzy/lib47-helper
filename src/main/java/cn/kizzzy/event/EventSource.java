package cn.kizzzy.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventSource implements IEventSource {
    private Map<Integer, List<IEventListener>> allListeners = new HashMap<>();

    @Override
    public boolean addListener(int eventType, IEventListener listener) {
        List<IEventListener> listeners = allListeners.get(eventType);
        if (listeners == null) {
            listeners = new LinkedList<>();
            allListeners.put(eventType, listeners);
        }
        return listeners.add(listener);
    }

    @Override
    public boolean removeListener(int eventType, IEventListener listener) {
        List<IEventListener> listeners = allListeners.get(eventType);
        if (listeners == null) {
            return false;
        }
        return listeners.remove(listener);
    }

    @Override
    public boolean notifyListener(EventArgs arg) {
        if (arg == null) {
            return false;
        }

        List<IEventListener> listeners = allListeners.get(arg.getType());
        if (listeners == null) {
            return false;
        }

        for (IEventListener listener : listeners) {
            listener.onNotify(arg);
        }
        return true;
    }

    @Override
    public boolean notifyListener(int type, Object params) {
        EventArgs arg = new EventArgs(this, type, params);
        List<IEventListener> listeners = allListeners.get(arg.getType());
        if (listeners == null) {
            return false;
        }

        for (IEventListener listener : listeners) {
            listener.onNotify(arg);
        }
        return true;
    }

    @Override
    public boolean notifyListener(int type) {
        EventArgs arg = new EventArgs(this, type, null);
        List<IEventListener> listeners = allListeners.get(arg.getType());
        if (listeners == null) {
            return false;
        }

        for (IEventListener listener : listeners) {
            listener.onNotify(arg);
        }
        return true;
    }
}
