package rox.main.event;

import rox.main.Main;
import rox.main.listener.MainStartedListener;
import rox.main.server.listener.ClientJoinListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventManager {

    public void loadEvents() {
        try {

            registerEvents(new MainStartedListener(), this);
            registerEvents(new ClientJoinListener(), this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void callEvent(Event event) {
        if (event.isAsync()) {
            if (Thread.holdsLock(this)) {
                throw new IllegalStateException();
            }
            fireEvent(event);
        } else {
            synchronized (this) {
                fireEvent(event);
            }
        }
    }

    private void fireEvent(Event event) {
        IHandler handlers = event.getHandler();
        RegisteredListener[] listeners = handlers.getRegisteredListeners();

        for (int i = 0; i < listeners.length; i++) {
            RegisteredListener register = listeners[i];
            if (register.getMain() != null) {
                try {
                    register.call(event);
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable e) {
                    System.out.println("Event could not executed: " + event.getEventName() + ":\n" + e);
                }
            }
        }
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) throws Exception {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return this.getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new Exception("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }

    private IHandler getEventListeners(Class<? extends Event> type) {
        Method method = null;
        try {
            method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            return (IHandler) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerEvents(Listener listener, EventManager main) throws Exception {
        Iterator iterator = createRegisteredListeners(listener, main).entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry = (Map.Entry) iterator.next();

            this.getEventListeners
                    (getRegistrationClass((Class) entry.getKey()))
                    .registerAll(entry.getValue());
        }
    }

    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, EventManager main) {
        HashMap ret = new HashMap();

        HashSet methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet(publicMethods.length + privateMethods.length, 1.0F);
            Method[] methods1 = publicMethods;
            int length = publicMethods.length;

            Method method;
            int i;
            for (i = 0; i < length; ++i) {
                method = methods1[i];
                methods.add(method);
            }

            methods1 = privateMethods;
            length = privateMethods.length;

            for (i = 0; i < length; ++i) {
                method = methods1[i];
                methods.add(method);
            }
        } catch (NoClassDefFoundError e) {
            return ret;
        }

        Iterator iterator = methods.iterator();

        while (true) {
            while (true) {
                Method method;
                EventAnnotation eh;
                do {
                    do {
                        do {
                            if (!iterator.hasNext()) {
                                return ret;
                            }

                            method = (Method) iterator.next();
                            eh = method.getAnnotation(EventAnnotation.class);
                        } while (eh == null);
                    } while (method.isBridge());
                } while (method.isSynthetic());

                Class checkClass;
                if (method.getParameterTypes().length == 1 && Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                    final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
                    method.setAccessible(true);
                    Set<RegisteredListener> eventSet = (Set) ret.get(eventClass);
                    if (eventSet == null) {
                        eventSet = new HashSet();
                        ret.put(eventClass, eventSet);
                    }

                    for (Class clazz = eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                        if (clazz.getAnnotation(Deprecated.class) != null) {
                            break;
                        }
                    }

                    final CTH timings = new CTH("CTH");
                    Method finalMethod = method;
                    EExecutor executor = (listener1, event) -> {
                        try {
                            if (eventClass.isAssignableFrom(event.getClass())) {
                                boolean isAsync = event.isAsync();
                                if (!isAsync) {
                                    timings.startTiming();
                                }

                                finalMethod.invoke(listener1, event);
                                if (!isAsync) {
                                    timings.stopTiming();
                                }

                            }
                        } catch (InvocationTargetException e) {
                            throw new Exception(e.getCause());
                        } catch (Throwable e) {
                            throw new Exception(e);
                        }
                    };
                    eventSet.add(new RegisteredListener(listener, executor, main, eh.priority()));
                } else {
                    Main.getLogger().err("[Event]", "Could not execute Handler.");
                }
            }
        }
    }

}
