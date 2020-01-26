package gui;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import dane.Event;

/**
 * this class is used to enable change of color in calendar table cells, having connected event. Class implements interface TableCellRenderer
 * @author Marta Bielecka 
 *
 */
public class CalendarDayRenderer extends JList<String> implements TableCellRenderer{

	/**
	 * constructs CalendarDayRenderer object and sets basic attributes. By default background and border are initialized with null and setFocusabe is set to "false"
	 */
	public CalendarDayRenderer() {
		setBackground(null);
		setBorder(null);
		setFocusable(false);
	}
	
	/**
	 * sets table cell appearance depending on its content, if CalendarDay object connected with current cell contains any Events in the list of events, background color is set to orange, otherwise it is white
	 */
	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if(value instanceof CalendarDay) {
			CalendarDay day = (CalendarDay) value;
			
			List<String> text = new ArrayList<>();
			text.add(day.getDayOfMonth());

			if(day.getEvents() != null && !day.isEmpty()) {
				setOpaque(true);
				setBackground(Color.ORANGE);
				for(Event e : day.getEvents()) {
					text.add(e.getTittle());
				}
			} else {
				setOpaque(false);
				setBackground(Color.WHITE);
			}
			setListData(text.toArray(new String[0]));
			return this;
		}else {
			throw new RuntimeException(" value must be CalendarDay Object");
		}
	}
}
