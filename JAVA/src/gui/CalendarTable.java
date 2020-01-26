package gui;

import javax.swing.JTable;

//import system.DisplayedDateChangeListener;
import system.events.DisplayedDateChanged;
import system.events.InternalEvent;
import system.events.InternalEventListener;
//import system.events.ResizeListener;

/**
 * this class is responsible for drawing and formatting the calendar table, based on the CalendarDataModel and CalendarDayRenderer classes
 * @author Marta Bielecka
 *
 */

public class CalendarTable extends JTable implements InternalEventListener{
	/**
	 * corresponding CalendarDataModel object, containing data about table format e.g. rows and columns number, content of table cells, name of columns
	 */
	private CalendarDataModel model;
	
	/**
	 * constructs JTable and sets its model to CalendarDatModel, and renderer to CalendarDayRenderer
	 * sets column and row selection not allowed
	 */
	CalendarTable(){
		super();
		model = new CalendarDataModel();
		super.setModel(model);
		setDefaultRenderer(CalendarDay.class, new CalendarDayRenderer());
		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(false);
	}

	/**
	 * sets table behavior in case of date change - sets events list and date to display to new values, taken from DisplayedDateChange object and then repaints calendar table
	 */
	@Override
	public void anEventOccurred(InternalEvent e) {
		if (e instanceof DisplayedDateChanged) {
			DisplayedDateChanged newEvent = (DisplayedDateChanged) e; 
			model.setDataToDisplay(newEvent.getNewDate(), newEvent.getEvents());
			this.resizeAndRepaint();
		}		
	}
}
