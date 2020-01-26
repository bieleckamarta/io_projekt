package dane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * this class implements simple event data container. It can contain references to Alarm class, if alarm is set, and to Contact class, when a person is bounded with an event
 * @author Mateusz Keller
 *
 */
public class Event implements java.io.Serializable {
	/**
	 * title of this event
	 */
	private String tittle;
	/**
	 * date and time when event is beginning
	 */
	private LocalDateTime start;
	/**
	 * date and time when event is ending
	 */
	private LocalDateTime end;
	/**
	 * format of a date used to parse LocalDateTime objects to String in toString method
	 */
	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE dd MMMM HH:mm");
	/**
	 * short note about event, initialized by default with empty String
	 */
	private String note = "";
	/**
	 * place of the event, initialized by default with empty String
	 */
	private String place = "";
	/**
	 * Alarm object connected with event if alarm is set, holds the date of alarm and sound to be displayed, handles sound playing, initialized by default with Alarm object holding null date 
	 */
	private Alarm notification = new Alarm(null);
	/**
	 * Contact object connected with event, if a person is bounded with it. Contains personal data of the person. Initialized by default with null
	 */
	private Contact person = null;

	/**
	 * constructs event with given title, note, place, start and end time.
	 * @param tittle title of this event
	 * @param start start date and time of this event
	 * @param end end date and time of this event
	 * @param note note describing the event
	 * @param place place of the event
	 */
	public Event(String tittle, LocalDateTime start, LocalDateTime end, String note, String place) 
	{
		this.tittle = tittle;
		this.start = start;
		this.end = end;
		this.note = note;
		this.place = place;
		this.notification = null;
	}
	
	/**
	 * constructs event with given title, start and end time. Note and place fields are by default initialized with empty String
	 * @param tittle title of this event
	 * @param start start time and date of this event
	 * @param end end time and date of this event
	 */
	public Event(String tittle, LocalDateTime start, LocalDateTime end) 
	{
		this.tittle = tittle;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * constructs event with given title, start and end time, place, note and date of alarm. 
	 * @param tittle title of this event
	 * @param start start date and time of this event
	 * @param end end date and time of this event
	 * @param note short note about this event
	 * @param place place of this event
	 * @param notification date and time of alarm added to this event
	 */
	public Event(String tittle, LocalDateTime start, LocalDateTime end, String note, String place, LocalDateTime notification)
	{
		this.tittle = tittle;
		this.start = start;
		this.end = end;
		this.note = note;
		this.place = place;
		this.notification = new Alarm(notification);
	}
	
	public Event() {
		
	}

	/**
	 * this method returns basic event data (title, start and end time, time of alarm, place and note in readable form, closed in String object 
	 */
	public String toString()
	{	
		String ret = "\n\nWydarzenie " + tittle + "\n" +
				start.format(dateFormat) + " - " + end.format(dateFormat);
		
		if(notification.getBefore() != null)	ret += "\n" + notification.toString();
		if(place != "")	ret+= "\n" + place;
		if(note != "")	ret+= "\n" + note;
		
		return ret; 
	}

	// GET/SET-ers
	
	/**
	 * returns actual content of title field of this event
	 * @return title of this event
	 */
	public String getTittle() { return tittle; }
	/**
	 * changes value of the title field of this event to this given as a parameter
	 * @param tittle new title of this event
	 */
	public void setTittle(String tittle) { this.tittle = tittle; }

	/**
	 * returns actual content of start field of this event
	 * @return start date and time of this event 
	 */
	public LocalDateTime getStart() { return start;	}
	/**
	 * changes value of the start date and time of this event to a given date and time
	 * @param start new start date and time of this event
	 */
	public void setStart(LocalDateTime start) { this.start = start;	}

	/**
	 * returns actual value of end field of this event
	 * @return end date and time of this event
	 */
	public LocalDateTime getEnd() { return end;	}
	/**
	 * changes value of the end date and time of this event to a given date and time 
	 * @param end new end date and time of this event
	 */
	public void setEnd(LocalDateTime end) { this.end = end; }

	/**
	 * returns actual value of note field of this event
	 * @return note about this event
	 */
	public String getNote() { return note; }
	/**
	 * changes value of the note field of this event to a given one
	 * @param note new note about this event
	 */
	public void setNote(String note) { this.note = note; }

	/**
	 * returns actual value of the place field of this event
	 * @return place of this event
	 */
	public String getPlace() { return place; }
	/**
	 * changes value of the place field of this event to a given one
	 * @param place new place of this event
	 */
	public void setPlace(String place) { this.place = place; }

	/**
	 * returns Alarm object from notification field of this event
	 * @return Alarm object connected with this event
	 */
	public Alarm getNotification() { return notification; }
//	public void setNotification(Alarm notification) { this.notification = notification; }
	/**
	 * changes Alarm object in notification field of this event to a new Alarm object with a given date
	 * @param date new date and time, when alarm should be displayed
	 */
	public void setNotification(LocalDateTime date) {this.notification = new Alarm(date);}

	/**
	 * returns Contact object from person field of this event
	 * @return Contact object connected with this event
	 */
	public Contact getPerson() { return person; }
	/**
	 * changes Contact object in person field of this event to a given Contact object
	 * @param person new Contact object to be added to this event
	 */
	public void setPerson(Contact person) { this.person = person; }
	
	/**
	 * calls playSound method on Alarm object in notification field of this event
	 */
	public void playAlarmSound() { 
		if(notification != null) {
			notification.playSound(); 
		}
	}
	/**
	 * calls stopSound method on Alarm object in notification field of this event
	 */
	public void stopAlarmSound() { 
		if(notification != null) {
			notification.stopSound(); 
			}
	}
}
