package rox.main.event;

import java.util.*;

public class IHandler {
    private volatile RegisteredListener[] handlers = null;
    private final EnumMap<EPriority, ArrayList<RegisteredListener>> enumMap = new EnumMap(EPriority.class);
    private static ArrayList<IHandler> lists = new ArrayList<>();


    public IHandler() {
        EPriority[] priorities;
        int length = (priorities = EPriority.values()).length;

        for (int i = 0; i < length; ++i) {
            EPriority o = priorities[i];
            this.enumMap.put(o, new ArrayList());
        }

        synchronized (lists) {
            lists.add(this);
        }
    }

    public static void cookAll() {
        synchronized (lists) {
            Iterator iterator = lists.iterator();
            while (iterator.hasNext()) {
                IHandler handler = (IHandler) iterator.next();
                handler.cook();
            }
        }
    }

    public synchronized void cook() {
        if (handlers == null) {
            List<RegisteredListener> entries = new ArrayList<>();
            Iterator iterator = enumMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<EPriority, ArrayList<RegisteredListener>> entry = (Map.Entry) iterator.next();
                entries.addAll(entry.getValue());
            }

            handlers = entries.toArray(new RegisteredListener[entries.size()]);
        }
    }

    public static void unregisterAll() {
        synchronized (lists) {
            Iterator iterator = lists.iterator();
            while (iterator.hasNext()) {
                IHandler handler = (IHandler) iterator.next();
                synchronized (handler) {
                    for (Object o : handler.enumMap.values()) {
                        List list = (List) o;
                        list.clear();
                    }
                    handler.handlers = null;
                }
            }
        }
    }

    public static void unregisterAll(Listener listener) {
        synchronized (lists) {
            Iterator iterator = lists.iterator();

            while (iterator.hasNext()) {
                IHandler handler = (IHandler) iterator.next();
                handler.unregister(listener);
            }
        }
    }

    public synchronized void unregister(RegisteredListener listener) {
        if ((this.enumMap.get(listener.getPriority())).remove(listener)) {
            this.handlers = null;
        }

    }

    public RegisteredListener[] getRegisteredListeners() {
        while (true) {
            RegisteredListener[] handlers = this.handlers;
            if (this.handlers != null) {
                return handlers;
            }

            this.cook();
        }
    }

    public synchronized void unregister(Listener listener) {
        boolean changed = false;
        Iterator iterator = this.enumMap.values().iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();
            ListIterator i = list.listIterator();

            while (i.hasNext()) {
                if (((RegisteredListener) i.next()).getListener().equals(listener)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if (changed) {
            this.handlers = null;
        }
    }

    public synchronized void register(RegisteredListener listener) {
        if ((this.enumMap.get(listener.getPriority())).contains(listener)) {
            throw new IllegalStateException("Listener already registered with priority: " + listener.getPriority().toString());
        } else {
            this.handlers = null;
            (this.enumMap.get(listener.getPriority())).add(listener);
        }
    }

    public void registerAll(Collection<RegisteredListener> listeners) {
        Iterator iterator = listeners.iterator();

        while (iterator.hasNext()) {
            RegisteredListener listener = (RegisteredListener) iterator.next();
            this.register(listener);
        }

    }

    public static ArrayList getHandlerLists() {
        synchronized (lists) {
            return (ArrayList) lists.clone();
        }
    }

    public static ArrayList<IHandler> getLists() {
        return lists;
    }

    public EnumMap<EPriority, ArrayList<RegisteredListener>> getEnumMap() {
        return enumMap;
    }

    public RegisteredListener[] getHandlers() {
        return handlers;
    }

    public void setHandlers(RegisteredListener[] handlers) {
        this.handlers = handlers;
    }

    public static void setLists(ArrayList<IHandler> lists) {
        IHandler.lists = lists;
    }
}
