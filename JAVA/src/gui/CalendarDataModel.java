package gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import dane.Event;

/**
 * this class provides calendar Table formatting and displaying events across days
 * @author Marta Bielecka
 *
 */
public class CalendarDataModel extends AbstractTableModel {
	
	/**
	 * date to be displayed in calendar
	 */
	private LocalDate dateToDisplay = LocalDate.now(); 
	/**
	 * list of events to be shown in calendar
	 */
	private List<Event> eventsToDisplay = new ArrayList<Event>();
	/**
	 * maps list of events on corresponding days of month
	 */
	private Map<Integer, List<Event>> eventsAcrossDays = new HashMap<>();
	/**
	 * table with string values containing names of the weekdays
	 */
	private static String[] weekdays = new String[]{
			"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
			};

	/**
	 * sets list of events and displayed date with given values. Maps events from the list across days of the chosen month
	 * @param dateToDisplay date to be displayed - year and month
	 * @param events - events to be displayed in a month
	 */
	public void setDataToDisplay(LocalDate dateToDisplay, List<Event> events) {
		this.dateToDisplay = dateToDisplay;
		this.eventsToDisplay = events;
		
		eventsAcrossDays = new HashMap<>();
        for (int day = 1; day <= dateToDisplay.lengthOfMonth(); ++day) {
            LocalDateTime dayBegin = LocalDateTime.of(dateToDisplay.getYear(), dateToDisplay.getMonth(), day, 0,0,1);
            LocalDateTime dayEnd = LocalDateTime.of(dateToDisplay.getYear(), dateToDisplay.getMonth(), day, 23,59,59);
            for (Event event : events) {
                if(event.getStart().isBefore(dayEnd) && event.getEnd().isAfter(dayBegin)) {
                    eventsAcrossDays.computeIfAbsent(day, none -> new ArrayList<>()).add(event);
                }
            }
        }
	}

	private static final long serialVersionUID = -7100146141891377597L;
	
	/**
	 * returns class of the table column
	 */
	@Override
	public Class<?> getColumnClass(int arg0) {
		return CalendarDay.class;
	}

	/**
	 * returns amount of columns in the table
	 */
	@Override
	public int getColumnCount() {
		return 7;
	}

	/**
	 * returns column name
	 */
	@Override
	public String getColumnName(int i) {
		return weekdays[i];
	}

	/**
	 * returns amount of rows
	 */
	@Override
	public int getRowCount() {
		return 6;
	}

	/**
	 * returns day of the month to display in given column and row of the table
	 * @param row row to display day of month in
	 * @param col column to display day of month in
	 * @return number of  day in month in given 
	 */
	private int getDayAt(int row, int col) {
        int firstDay = dateToDisplay.minusDays(dateToDisplay.getDayOfMonth()).getDayOfWeek().getValue()-1;
        int cellNumber = row*7 + col;
        return cellNumber - firstDay;
    }
	
	/**
	 * returns CalendarDay object to display in given row and column
	 */
	@Override
	public Object getValueAt(int row, int col) {
		int dayOfMonth = getDayAt(row, col);

        String dayLabel = dayOfMonth < 1 || dayOfMonth > dateToDisplay.lengthOfMonth()
                ? "-"
                : String.valueOf(dayOfMonth);
        List<Event> eventsAtDay = eventsAcrossDays.get(dayOfMonth);

        return new CalendarDay(dayLabel, eventsAtDay);
	}
}
