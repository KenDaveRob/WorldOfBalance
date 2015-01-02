package core;

// Java Imports
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import utility.Log;

public class EventHandler {

    Map<EventTypes, List<EventListener>> listenerList = new EnumMap<EventTypes, List<EventListener>>(EventTypes.class);

    public void add(EventTypes event_type, EventListener listener) {
        List<EventListener> tempList;

        if (listenerList.containsKey(event_type)) {
            tempList = listenerList.get(event_type);
        } else {
            tempList = new ArrayList<EventListener>();
            listenerList.put(event_type, tempList);
        }

        tempList.add(listener);
    }

    public void remove(EventTypes event_type, EventListener listener) {
        if (listenerList.containsKey(event_type)) {
            List<EventListener> tempList = listenerList.get(event_type);
            tempList.remove(listener);
        }
    }

    public void execute(EventTypes event_type, Object... args) {
        if (listenerList.containsKey(event_type)) {
            for (EventListener listener : new ArrayList<EventListener>(listenerList.get(event_type))) {
                try {
                    listener.run(args);
                } catch (Exception ex) {
                    Log.println_e(ex.getMessage());
                }
            }
        }
    }
}
