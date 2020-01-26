package system.events;

import java.util.List;

import dane.Contact;
/**
 * class implements displayed contacts change in program
 * @author Marta Bielecka
 *
 */
public class DisplayedContactsChanged implements InternalEvent{
	/**
	 * list of contacts to be displayed
	 */
	private List<Contact> contacts;
	
	/**
	 * constructs DisplayedContactsChanged and sets new list of contacts to display
	 * @param contacts contacts list to be set to DisplayedContactsChanged object
	 */
	public DisplayedContactsChanged(List<Contact> contacts) {
		super();
		this.contacts = contacts;
	}
	
	/**
	 * returns actual list of contacts attached to DisplayedContactsChanged object
	 * @return list of contacts to display
	 */
	public List<Contact> getContacts(){
		return contacts;
	}
	
	/**
	 * changes list of contacts attached to this class to the one given as the parameter
	 * @param contacts new list of contacts to be attached to DisplayedContactsChanged object
	 */
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

}
