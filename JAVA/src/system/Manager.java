package system;

import java.io.File;
import java.time.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dane.*;
import gui.EventEditingDialog;
import system.Transmiter;

/**
 * main class of logic layer of this program, handles all changes in data and logic classes, communicates with GUI via Controller object
 * @author Mateusz Keller
 * @author Marta Bielecka
 *
 */
public class Manager {

	/**
	 * list of events
	 */
	private ArrayList<Event> eventy = new ArrayList<>();
	/**
	 * list of contacts
	 */
	private ArrayList<Contact> kontakty = new ArrayList<>();
	/**
	 * Transmiter object responsible for i/o operations and streams handling
	 */
	private Transmiter xPort = new Transmiter();
	
	/**
	 * returns list of all events
	 * @return list of all events
	 */
	public ArrayList<Event> getEventy() { return eventy; }
	/**
	 * returns list of contacts
	 * @return list of contacts
	 */
	public ArrayList<Contact> getContacts() { return kontakty; }
	/**
	 * returns Transmiter object
	 * @return value of xPort field
	 */
	public Transmiter getTransmiter() { return xPort; }
	
	/**
	 * constructs new Manager object
	 */
	public Manager() {}

	/**
	 * returns all events
	 * @return list of all events
	 */
	public List<Event> getAllEvents(){
		return eventy;
	}
	
	/**
	 * returns events in given month
	 * @param yearAndMonth month to return events list for
	 * @return list of all events in given month
	 */
	public List<Event> getEventsInMonth(LocalDate yearAndMonth) {
		return getEventsInMonth(yearAndMonth.getYear(), yearAndMonth.getMonthValue());
	}
	
	/**
	 * returns events in given month
	 * @param year year of events to return
	 * @param month month of events to return
	 * @return list of events in given month and year
	 */
	public List<Event> getEventsInMonth(int year, int month){
		ArrayList<Event> events = new ArrayList<Event>(); 
		for(int i = 0; i < eventy.size(); i++) {
			if(eventy.get(i).getStart().getYear() == year || eventy.get(i).getEnd().getYear() == year) {
				if(eventy.get(i).getStart().getMonthValue() == month || eventy.get(i).getEnd().getMonthValue() == month ) {
					events.add(eventy.get(i));
				}
		    }
		}
		return events; 
	}
	
	/**
	 * returns events in given year
	 * @param date date indicating year we want to get events for
	 * @return list of events in given year
	 */
	public List<Event> getEventsInYear (LocalDate date) {
		int year = date.getYear();
		ArrayList<Event> events = new ArrayList<Event>();
		for(int i = 0; i < eventy.size(); i++) {
			if(eventy.get(i).getStart().getYear() == year || eventy.get(i).getEnd().getYear() == year) {
				events.add(eventy.get(i));
			}
		}
		return events;
	}
	
	/**
	 * returns events in given week
	 * @param date date indicating week to get events for
	 * @return list of events in given week
	 */
	public List<Event> getEventsInWeek(LocalDate date){
		int dayindex = date.getDayOfWeek().getValue() - 1;
		int day = date.getDayOfMonth();
		int month = date.getMonthValue();
		int year = date.getYear();
		LocalDateTime startDay = LocalDateTime.of(year, month, day - dayindex,0,0,1);
		LocalDateTime endDay = LocalDateTime.of(year, month, day + 6 - dayindex,23,59,59);
		ArrayList<Event> events = new ArrayList<Event>();
		for(int i = 0; i < eventy.size(); i++) {
			if((eventy.get(i).getStart().isBefore(endDay) && eventy.get(i).getStart().isAfter(startDay))|| 
					(eventy.get(i).getEnd().isAfter(startDay) && eventy.get(i).getEnd().isBefore(endDay))) {
				events.add(eventy.get(i));
			}		
		}
		return events;		
	}
	
	/**
	 * returns events in given day
	 * @param date date indicating day to get events for
	 * @return list of events in given day
	 */
	public List<Event> getEventsInDay(LocalDate date){
		int day = date.getDayOfMonth();
		int month = date.getMonthValue();
		int year = date.getYear();
		ArrayList<Event> events = new ArrayList<Event>();
		for(int i = 0; i < eventy.size(); i++) {
			if(eventy.get(i).getStart().getYear() == year || eventy.get(i).getEnd().getYear() == year) {
				if(eventy.get(i).getStart().getMonthValue() == month || eventy.get(i).getEnd().getMonthValue() == month ) {
					if(eventy.get(i).getStart().getDayOfMonth() == day || eventy.get(i).getEnd().getDayOfMonth() == day) {
						events.add(eventy.get(i));
						}
					}	
				}		
		}
		return events;
	}

	/**
	 * shows event editing dialog
	 */
	public void eventEditing() {
		EventEditingDialog.showDialog(eventy.get(0));
	}
	
	/**
	 * adds new event to event list
	 * @param e event to add
	 */
	public void addEvent(Event e) {
		if(e != null) {
		eventy.add(e);
		}
	}

	/**
	 * deletes event from event list
	 * @param numberOfEvent index of event to delete
	 */
	public void deleteEvent(int numberOfEvent) {
		eventy.remove(numberOfEvent);
	}
	
	/**
	 * deletes event from event list
	 * @param e event to delete
	 */
	public void deleteEvent(Event e) {
		eventy.remove(e);
	}

	/**
	 * adds new contact with given data to the contact list
	 * @param name name of contact to add
	 * @param company company in contact to add
	 * @param email e-mail address in contact to add
	 * @param phone phone number of contact to add
	 */
	public void addContact(String name, String company, String email, String phone) {
		Contact temp = new Contact(name);

		if (company != "")
			temp.setCompany(company);
		if (email != "")
			temp.setEmail(email);
		if (phone != "")
			temp.setPhone(phone);

		kontakty.add(temp);
	}
	
	/**
	 * adds contact to the contacts list 
	 * @param c contact to add
	 */
	public void addContact(Contact c) {
		kontakty.add(c);
	}

	/**
	 * removes contact from the contacts list
	 * @param numberOfContact index of contact to remove
	 */
	public void removeContact(int numberOfContact) {
		kontakty.remove(numberOfContact);
	}
	
	/**
	 * removes contact from the contacts list
	 * @param c contact to remove
	 */
	public void removeContact(Contact c) {
		kontakty.remove(c);
	}

	/**
	 * removes expired events beginning before given date
	 * @param dueDate date before which old events should be removed
	 */
	public void removeOldEvents(LocalDateTime dueDate) {
		for(int i = 0; i < eventy.size(); i ++) {
			if(eventy.get(i).getEnd().isBefore(dueDate)) {
				eventy.remove(i);
			}
		}
	}

	/**
	 * invokes playAlarm method repeatedly
	 * @param frame
	 */
	public void checkDueAlarms(JFrame frame) {
		Runnable drawRunnable = new Runnable() {
			
			@Override
			public void run() {
				playAlarm(frame);	
			}
		};
		
		ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
		exec.scheduleAtFixedRate(drawRunnable, 0, 20, TimeUnit.SECONDS);
	}
	
	/**
	 * checks if any event on the list has alarm to display. If so plays sound and shows dialog with information about coming event
	 * @param frame component in which message dialog will be displayed
	 */
	public void playAlarm(JFrame frame) {
		Iterator<Event> it = eventy.iterator();

		while (it.hasNext()) {
			Event e = it.next();

			if (e.getNotification() != null && e.getNotification().getBefore() != null) {
				LocalDateTime dateOfAlarm = e.getNotification().getBefore();
				
				if (dateOfAlarm.isBefore(LocalDateTime.now())) {
					e.playAlarmSound();
					JOptionPane.showMessageDialog(frame, "Event " + e.getTittle() + " will begin soon");
					e.setNotification(null);
				}
			}
		}
	}
	
	/**
	 * imports contacts and events from database of given name
	 * @param baza name of database file to import data from
	 */
	public void importFromDatabase(String baza) {
        kontakty = xPort.bdImportKontakty(baza);
//        eventy = xPort.bdImportEventy(baza);
        eventy.addAll(xPort.bdImportEventy(baza));
	}
	
	/**
	 * exports contacts and events to database of given name
	 * @param baza database to export data to
	 */
	public void exportToDatabase(String baza) {
        xPort.bdExportKontakty(kontakty, baza);
        xPort.bdExportEventy(eventy, baza);
	}
	
	/**
	 * imports events from given XML file
	 * @param file file to import events from
	 */
	public void importFromXML(File file) {
		eventy = new ArrayList<>(xPort.xmlImport(file));
	}

	/**
	 * exports events to given XML file
	 * @param file file to export events to
	 */
	public void exportEventsToXml(File file) {
		xPort.xmlExport(file, eventy);
	}

	/**
	 * replaces one event with another on the events list
	 * @param oldEvent event to remove from the list
	 * @param newEvent event to add to the list
	 */
	public void replaceEvent(Event oldEvent, Event newEvent) {
		eventy.remove(oldEvent);
		eventy.add(newEvent);
	}
}
