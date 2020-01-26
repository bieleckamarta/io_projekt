package system;
import java.io.File;
//TODO zrobiæ nowy internalEvent - dodanie eventu przez dialog, notify kalendarz i listê zdarzeñ
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.sun.media.sound.ModelAbstractChannelMixer;

import dane.Contact;
import dane.Event;
import system.events.DisplayedContactsChanged;
import system.events.DisplayedDateChanged;
import system.events.DisplayedEventsChanged;
import system.events.InternalEvent;
import system.events.InternalEventListener;

/**
 * this class is responsible for communication between GUI events in Application class and logic classes (mainly Manager class).
 * @author Marta Bielecka
 *
 */
public class Controller {
	/**
	 * actual date to be displayed in calendar Table
	 */
	private LocalDate displayedDate;
	/**
	 * integer variable indicating which filter option in events panel is currently chosen
	 */
	private int currentOption = 0;
	/**
	 * maps events occurring in application to list of classes reacting to these changes 
	 */
	private Map<Class<? extends InternalEvent>, 
	List<InternalEventListener>> listenersMap = new HashMap<>();
	/**
	 * manager object to execute changes in applications logic classes
	 */
	private Manager manager = Controller.createManager();
	
	/**
	 * constructs Controller object with current date
	 */
	public Controller() {
		LocalDate now = LocalDate.now();
		this.displayedDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
	}

	/**
	 * initializes Controller object, refreshing event and contact data
	 */
	public void initialize() {
		refreshEventData();
		refreshContactData();
		refreshDisplayedEvents(currentOption);
	}
	
	/**
	 * notifies proper listeners that displayed date has changed, passing them list of events in displayed month	
	 */
	private void refreshEventData() {
		notifyListeners(new DisplayedDateChanged(
				displayedDate, manager.getEventsInMonth(displayedDate)));
	}
	
	/**
	 * notifies proper listeners which events to display depending on chosen filter option in events panel
	 * @param option events filter option chosen in events panel
	 */
	private void refreshDisplayedEvents(int option) {
		LocalDate date = LocalDate.now();
		if(option == 0) {
			notifyListeners(new DisplayedEventsChanged(manager.getAllEvents()));
			} else if(option == 1) {
				notifyListeners(new DisplayedEventsChanged(manager.getEventsInDay(date)));
			} else if(option == 2) {
				notifyListeners(new DisplayedEventsChanged(manager.getEventsInWeek(date)));
			} else if(option == 3) {
				notifyListeners(new DisplayedEventsChanged(manager.getEventsInMonth(date)));
			} else if(option == 4) {
				notifyListeners(new DisplayedEventsChanged(manager.getEventsInYear(date)));
			}
	}
	
	/**
	 * refreshes displayed contacts
	 */
	private void refreshContactData() {
		notifyListeners(new DisplayedContactsChanged(manager.getContacts()));
	}
	
	/**
	 * adds pairs of internal application's events type and classes, that react to these events to map of listeners
	 * @param eventType defines to what event type listeners ar reacting
	 * @param listener defines who to inform about occuring events of given type
	 */
	public void registerListener(Class<? extends InternalEvent> eventType, InternalEventListener listener) {
		List<InternalEventListener> listenersForEvent = listenersMap.get(eventType); 
		if(listenersForEvent == null) {
			listenersForEvent = new ArrayList<>();
			listenersForEvent.add(listener);
			listenersMap.put(eventType, listenersForEvent);
		}
		else listenersForEvent.add(listener);
	}
	
	/**
	 * check which classes listen to given InternalEvent and informs them, that event occurred
	 * @param e event that occurred in application
	 */
	private void notifyListeners(InternalEvent e) {
		List<InternalEventListener> listenersForEvent = listenersMap.get(e.getClass()); 
		if(listenersForEvent != null) {
			for (int i = 0; i < listenersForEvent.size(); i++){
				listenersForEvent.get(i).anEventOccurred(e);
			}
		}
	}
	
	/**
	 * shows message dialog with given title and content
	 * @param title title of message dialog
	 * @param content content of message dialog
	 */
	public void showMessage(String title, String content){
		JOptionPane.showMessageDialog(null, content, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * invokes events import from XML file in manager, refreshes event data and displayed events
	 * @param file XML file from which events are imported
	 */
	public void importEventsFromXml(File file){
        manager.importFromXML(file);
        refreshEventData();
        refreshDisplayedEvents(currentOption);
	}
	
	/**
	 * invokes function to import events from database in Manager object, refreshes events and contacts
	 * @param db name of database file to import events from
	 */
	public void importDataFromDatabase(String db){
		manager.importFromDatabase(db);
		refreshEventData();
		refreshDisplayedEvents(currentOption);
		refreshContactData();
	}

	/**
	 * invokes method to export events to XML file in Manager class
	 * @param file XML file to export events to
	 */
	public void exportEventsToXml(File file) {
		manager.exportEventsToXml(file);
	}

	/**
	 * invokes method to export events to database in Manager class
	 * @param db name of database file to export events to
	 */
	public void exportDataToDatabase(String db){
		manager.exportToDatabase(db);
	}

	/**
	 * shows dialog with informations about program
	 */
	public void showAboutProgramWindow(){
		showMessage("About Program","Projekt zaliczeniowy: \nPrzedmiot: in¿ynieria oprogramowania\nAutorzy:\nMarta Bielecka\nMateusz Keller");
	}

	/**
	 * changes displayed date and notifies proper listeners about it
	 * @param selectedMonth new month to display
	 * @param selectedYear new year to display
	 */
	public void changeDisplayedDate(int selectedMonth, int selectedYear) {
		displayedDate = LocalDate.of(selectedYear, selectedMonth+1, 1);		
		List<Event> events = manager.getEventsInMonth(selectedYear, selectedMonth + 1);
		notifyListeners(new DisplayedDateChanged(displayedDate, events));		 		
	}
	
	/**
	 * changes displayed events in events panel based on chosen filter option
	 * @param option
	 */
	public void changeDisplayedEvents(int option) {
	    currentOption = option;
		refreshDisplayedEvents(option);
	}
	
	/**
	 * creates new Manager object to handle internal events
	 * @return newly created Manager object
	 */
	public static Manager createManager() {
		Manager manager = new Manager();	
		return manager;
	}
	
	/**
	 * invokes method to add event in Manager object, refreshes displayed events
	 * @param e event to add
	 */
	public void addEvent(Event e) {
		if(e != null) {
			manager.addEvent(e);
			refreshEventData();
			refreshDisplayedEvents(currentOption);
		}
	}
	
	/**
	 * invokes function removing old events in Manager object, refreshes displayed events 
	 * @param dueTime date and time before which expired events should be romoved
	 */
	public void removeOldEvents(LocalDateTime dueTime) {
		manager.removeOldEvents(dueTime);
		refreshEventData();
		refreshDisplayedEvents(currentOption);
	}
	
	/**
	 * invokes method to add contact in Manager object, refreshes contact data
	 * @param c contact to add
	 */
	public void addContact(Contact c) {
		if(c!=null) {
			manager.addContact(c);
			refreshContactData();
		}
	}
	
	/**
	 * invokes method to remove contact in Manager object, refreshes contact data
	 * @param c contact to be removed
	 */
	public void removeContact(Contact c) {
		manager.removeContact(c);
		refreshContactData();
	}
	
	/**
	 * invokes method to remove event in Manager object, refreshes event data
	 * @param e event to be removed
	 */
	public void removeEvent(Event e) {
		manager.deleteEvent(e);
		refreshEventData();
		refreshDisplayedEvents(currentOption);
	}

	/**
	 * replaces one event with another, deletes old event and refreshes event data
	 * @param oldEvent event to remove
	 * @param newEvent event to add
	 */
	public void replaceEvent(Event oldEvent, Event newEvent) {
		manager.replaceEvent(oldEvent, newEvent);
		refreshEventData();
		refreshDisplayedEvents(currentOption);
	}
	
	/**
	 * invokes method to play alarms in Manager class
	 * @param frame component in which message dialogs about approaching events will be shown
	 */
	public void playAlarms(JFrame frame) {
		manager.checkDueAlarms(frame);
	}
}
