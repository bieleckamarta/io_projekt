package system.events;

import java.time.LocalDate;
import java.util.List;

import dane.Contact;
import dane.Event;

/**
 * class implementing displayed date change in program
 * @author Marta Bielecka
 *
 */
public class DisplayedDateChanged implements InternalEvent {
	/**
	 * actually displayed date
	 */
	private LocalDate newDate;
	/**
	 * list of events corresponding with displayed date
	 */
	private List<Event> events; 
	
	/**
	 * constructs DisplayedDateChanged object with given date and list of events
	 * @param newDate
	 * @param events
	 */
	public DisplayedDateChanged(LocalDate newDate, List<Event> events) {
		super();
		this.newDate = newDate;
		this.events = events;
	}
	
	/**
	 * returns actually displayed date
	 * @return actual date
	 */
	public LocalDate getNewDate() {
		return newDate;
	}
	
	/**
	 * sets new date to display
	 * @param newDate date to display
	 */ 
	public void setNewDate(LocalDate newDate) {
		this.newDate = newDate;
	}
	/**
	 * returns list of events corresponding with actually displayed date
	 * @return list of event for a date
	 */
	public List<Event> getEvents() {
		return events;
	}
	
	/**
	 * changes list of events in events field
	 * @param events
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	
	
}
