package gui;

import java.util.List;

import dane.Event;

/**
 * class displayed in calendar table cell. Consists of events list and string containing day of month 
 * @author Marta Bielecka
 *
 */
public class CalendarDay {
	
	/**
	 * day of month
	 */
	private String dayOfMonth; 
	/**
	 * list of events in the day
	 */
	private List<Event> events;

	/**
	 * returns number of the day of month
	 * @return day of month
	 */
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	
	/**
	 * returns list of events in this day of month
	 * @return list of events in this day
	 */
	public List<Event> getEvents(){
		return events;
	}
	
	/**
	 * returns true if day of month is empty
	 * @return boolean telling if day of month is empty
	 */
	public boolean isEmpty() {
		return "-".equals(dayOfMonth);
	}
	
	/**
	 * constructs CalendarDay object with given day of month and list of events
	 * @param dayOfMonth day of month to be set
	 * @param events list of events to be set
	 */
	public CalendarDay(String dayOfMonth, List<Event> events) {
		this.dayOfMonth = dayOfMonth;
		this.events = events;
	}
	
}
