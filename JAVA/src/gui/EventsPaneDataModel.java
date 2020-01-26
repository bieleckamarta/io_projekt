package gui;

import dane.Contact;
import dane.Event;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * this class provides events table formatting
 * @author Marta Bielecka
 *
 */
class EventsPaneDataModel extends AbstractTableModel {

	/**
	 * list of events to be displayed in the panel
	 */
    private List<Event> events = new ArrayList<>();
    /**
     * table of String objects containing names of columns
     */
    private static String[] columns = new String[] {
            "Title", "Description", "Start", "End", "", ""
    };

    /**
     * actualizes list of events to be displayed
     * @param evs new list of events to be displayed
     */
    public void setEvents(List<Event> evs) {
        events = evs;
    }
    
    /**
     * returns event in given row
     * @param row number of row to get event from
     * @return event in given row
     */
    public Event getEventAtRow(int row) {
    	return events.get(row);
    }

    /**
     * returns information if given cell is editable which is false by default
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	return false;
    }

    /**
     * returns number of rows, which corresponds with length of events list
     */
    @Override
    public int getRowCount() {
        return events.size();
    }

    /**
     * returns name of given column
     */
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    /**
     * returns number of columns
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * returns class table's column
     */
    @Override
    public Class<?> getColumnClass(int col) {
        if(col < 4) {
            return String.class;
        } else {
        	return JLabel.class;
        }
    }

    /**
     * returns value in given cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Event ev = events.get(rowIndex);
        switch (columnIndex) {
            case 0: return ev.getTittle();
            case 1: return ev.getNote();
            case 2: return ev.getStart().toString();
            case 3: return ev.getEnd().toString();
            case 4: JLabel editor = new JLabel("Edit");
                    editor.setHorizontalAlignment(JLabel.CENTER);
                    editor.setOpaque(true);
                    editor.setBackground(Color.ORANGE);
                    editor.setForeground(Color.BLACK);
                    return editor;
            case 5: JLabel deleter = new JLabel("Delete");
            		deleter.setHorizontalAlignment(JLabel.CENTER);
            		deleter.setOpaque(true);
            		deleter.setBackground(Color.GRAY);
            		deleter.setForeground(Color.WHITE);
            		return deleter;
            default:
            	return null;
        }
    }
}
