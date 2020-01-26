package system.events;

import java.util.List;

import dane.Event;

/**
 * class implementing displayed events change in program
 * @author Marta Bielecka
 *
 */
public class DisplayedEventsChanged implements InternalEvent{
	/**
	 * list of events to be displayed
	 */
	private List<Event> events;
	
	/**
	 * constructs object and sets its list of events to given one
	 * @param ev list of events to be set
	 */
	public DisplayedEventsChanged (List<Event> ev) {
		super();
		events = ev;
	}
	
	/**
	 * returns actual list of events from object
	 * @return list of events to be displayed
	 */
	public List<Event> getEvents () {
		return events;
	}
	
	/**
	 * sets list of events in this object to given one
	 * @param ev list of events to be attached to this object
	 */
	public void setEvents(List<Event> ev) {
		events = ev;
	}

}
