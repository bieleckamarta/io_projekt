package gui;

import dane.Event;
import system.events.DisplayedEventsChanged;
import system.events.InternalEvent;
import system.events.InternalEventListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * this class creates events panel according to attributes set in EventsPaneDataModel.
 * @author Marta Bielecka
 *
 */
class EventsPane extends JTable implements InternalEventListener {

	/**
	 * EventRemover object enabling removing chosen Event by clicking in the table cell
	 */
    private final EventRemover remover;
    /**
	 * EventReplacer object enabling editing chosen Event by clicking in the table cell
	 */
    private final EventReplacer editor;
    /**
	 * EventsPaneDataModel object containing formatting data e.g. row and column number, column classes and names etc.
	 */
    private final EventsPaneDataModel model = new EventsPaneDataModel();

    /**
	 * class enables invoking removing or editing function after clicking in table cell containing JLabel
	 * @author Marta Bielecka
	 *
	 */
    private class ButtonClicker extends MouseAdapter{
    	/**
		 * defines what should happen after clicking mouse button inside EventsPane. Gets the position of cursor and calls proper function of removing or editing corresponding event.
		 */
        @Override
        public void mouseClicked (MouseEvent e) {
        	if (e.getComponent() instanceof EventsPane) {
        		EventsPane evPane = (EventsPane)e.getComponent();
        		int col = evPane.getColumnModel().getColumnIndexAtX(e.getX());
        		int row = e.getY() / evPane.getRowHeight();
        		
        		if(row >= 0 && row < evPane.getRowCount() && col >= 0 && col < evPane.getColumnCount()) {
        			Object valueAt = evPane.getValueAt(row, col);
        			if (valueAt instanceof JLabel) {
        			    if (col == 5) {
                            remover.removeEvent(model.getEventAtRow(row));
                        } else {
        			        editor.replaceEvent(model.getEventAtRow(row));
                        }
        			}
        		}
        	}
        }
    }

    /**
     * constructs EventsPane with given EventRemover and EventReplacer objects, sets JTable model to ContactsPaneDataModel, sets preferred width of columns.
     * @param remover EventRemover object to be added to EventsPane
     * @param editor EventReplacer object to be added to EventsPane
     */
    public EventsPane(EventRemover remover, EventReplacer editor) {
        this.remover = remover;
        this.editor = editor;
        setModel(model);
        setDefaultRenderer(JLabel.class, (table, value, isSelected, hasFocus, row, column) -> (JLabel) value);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addMouseListener(new ButtonClicker());
        setRowHeight(30);
        for (int col = 0; col < getColumnCount(); col++) {
            if (col == 1) {
                getColumnModel().getColumn(col).setPreferredWidth(200);
            } else {
                getColumnModel().getColumn(col).setPreferredWidth(50);
            }
        }
    }

    /**
	 * defines that, if displayed events change list of events in EventsPaneDataModel object is actualized and table is repainted
	 */
    @Override
    public void anEventOccurred(InternalEvent e) {
        if (e instanceof DisplayedEventsChanged) {
            DisplayedEventsChanged dec = (DisplayedEventsChanged)e;
            model.setEvents(dec.getEvents());
            this.resizeAndRepaint();
        }
    }
    
    /**
	 * interface defined to enable removing event after clicking in events table
	 * @author Marta Bielecka
	 *
	 */
    public interface EventRemover {
    	/**
		 * method removing event
		 * @param event event to remove
		 */
    	void removeEvent(dane.Event event);
    }

    /**
   	 * interface defined to enable editing event after clicking in events table
   	 * @author Marta Bielecka
   	 *
   	 */
    public interface EventReplacer {
    	/**
		 * method editing event
		 * @param eventAtRow event to edit
		 */
        void replaceEvent(Event eventAtRow);
    }
}
