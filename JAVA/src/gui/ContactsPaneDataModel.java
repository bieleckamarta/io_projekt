package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

import dane.Contact;

/**
 *  this class provides contact table formatting
 * @author Marta Bielecka
 *
 */
public class ContactsPaneDataModel extends AbstractTableModel {
	/**
	 * list of contacts to be displayed in table
	 */
	private List<Contact> contacts = new ArrayList<Contact>();
	/**
	 * table of String objects containing names of columns in the table
	 */
	private static String[] columns = new String[] {"Name", "Company", "Phone number", "E-mail", ""};
	
	/**
	 * actualizes list of contacts with given list
	 * @param cont new list of contacts to be displayed
	 */
	public void setContacts(List<Contact> cont) {
		contacts = cont;
	}
	
	/**
	 * connects number of row with contatc that it contains. Returns Contact object from the given row number
	 * @param row number of row containing contact to be removed
	 * @return contact in given row
	 */
	public Contact removeContactAtRow (int row) {
		return contacts.remove(row);
	}
	
	/**
	 * sets all cells not editable
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	/**
	 * returns number of rows in table, which corresponds number of contacts to be displayed
	 */
	@Override
	public int getRowCount() {
		return contacts.size();
	}
	
	/**
	 * returns name of given column number
	 */
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	/**
	 * returns number of columns in table
	 */
	@Override
	public int getColumnCount() {
		return columns.length;
	}

	/**
	 * returns what classes does given column contain
	 */
	@Override
	public Class<?> getColumnClass(int col){
		if (col<4) {
			return String.class;
		} else {
			return JLabel.class;
		}
	}
	
	/**
	 * returns content of the table at given row and column index
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Contact c = contacts.get(rowIndex);
		switch (columnIndex) {
		case 0: return c.getName();
		case 1: return c.getCompany();
		case 2: return c.getPhone();
		case 3: return c.getEmail();
		case 4:  JLabel deleter = new JLabel("Delete");
				deleter.setHorizontalAlignment(JLabel.CENTER);
				deleter.setOpaque(true);
				deleter.setBackground(Color.GRAY);
				deleter.setForeground(Color.WHITE);
				return deleter;
		}
		return null;
	}
}
