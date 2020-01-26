package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import dane.Contact;
import system.events.DisplayedContactsChanged;
import system.events.DisplayedDateChanged;
import system.events.InternalEvent;
import system.events.InternalEventListener;

/**
 * This class constructs contacts panel according to attributes set in ContactPaneDataModel class. 
 * @author Marta Bielecka
 *
 */
public class ContactsPane extends JTable implements InternalEventListener{

	/**
	 * ContactsRemover object enabling removing chosen Contact by clicking in the table cell
	 */
	private final ContactsRemover remover;
	/**
	 * ContactsPaneDataModel object containing formatting data e.g. row and column number, column classes and names etc.
	 */
	private final ContactsPaneDataModel model = new ContactsPaneDataModel();
	
	/**
	 * class enables invoking a removing function after clicking in table cell containing JLabel
	 * @author Marta Bielecka
	 *
	 */
	private class ButtonClicker extends MouseAdapter{
		/**
		 * defines what should happen after clicking mouse button inside ContactsPane. Gets the position of cursor and if it is within JLabel component in the last table column it removes corresponding contact
		 */
		@Override
		public void mouseClicked (MouseEvent e) {
			if(e.getComponent() instanceof ContactsPane) {
				System.out.println("mouse");
				ContactsPane pane = (ContactsPane) e.getComponent();
				int col = pane.getColumnModel().getColumnIndexAtX(e.getX());
				int row = e.getY()/pane.getRowHeight();
				
				if(row >= 0 && row < pane.getRowCount() && col >= 0 && col<pane.getColumnCount()) {
					Object valueAt = pane.getValueAt(row, col);
					if(valueAt instanceof JLabel) {
						remover.removeContact(model.removeContactAtRow(row));
					}
				}
			}
		}
	}
	
	/**
	 * constructs ContactsPane with given ContactsRemover object, sets JTable model to ContactsPaneDataModel, sets preferred width of columns.
	 * @param remover ContactsRemover object to be added to ContactsPane
	 */
	public ContactsPane(ContactsRemover remover) {
		this.remover = remover;
		setModel(model);
		setDefaultRenderer(JLabel.class, (table, value, isSelected, hasFocus, row, column) -> (JLabel)value);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new ButtonClicker());
		setRowHeight(30);
		for(int col = 0; col < getColumnCount(); col++) {
			if(col == 1) {
				getColumnModel().getColumn(col).setPreferredWidth(200);
			}else {
				getColumnModel().getColumn(col).setPreferredWidth(50);
			}
		}
	}
	
	/**
	 * defines that, if displayed contacts change list of contacts in ContactsPaneDataModel object is actualized and table is repainted
	 */
	@Override
	public void anEventOccurred(InternalEvent e) {
		if (e instanceof DisplayedContactsChanged) {
            DisplayedContactsChanged ddc = (DisplayedContactsChanged)e;
            model.setContacts(ddc.getContacts());
            this.resizeAndRepaint();
        }	
	}

	/**
	 * interface defined to enable removing contact after clicking in contacts table
	 * @author Marta Bielecka
	 *
	 */
	public interface ContactsRemover{
		/**
		 * method removing contact
		 * @param con contact to remove
		 */
		void removeContact(Contact con);
	}	
}
