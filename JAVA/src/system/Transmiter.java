package system;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dane.AppParameters;
import dane.Event;
import dane.Alarm;
import dane.Contact;

/**
 * class is responsible for import/export from/to database or XML. It opens streams, handles them and closes. Formats data to be send and recieved ones. 
 * @author Marta Bielecka
 *
 */
public class Transmiter {

	/**
	 * pattern for dates formatting
	 */
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm");

	/**
	 * imports contacts from database in given file
	 * @param baza String object containing name of a file with database
	 * @return list of imported contacts
	 */
	public ArrayList<Contact> bdImportKontakty(String baza) {
		ArrayList<Contact> contacts = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + baza)){
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Kontakty");

			String ret;
			int id = 0;
			while (rs.next()) {
				Contact temp = new Contact(rs.getString("name"));
				ret = id + ". " + rs.getString("name") + " ";
				if (rs.getString("company") != null) {
					temp.setCompany(rs.getString("company"));
					ret += rs.getString("company") + " ";
				}
				if (rs.getString("email") != null) {
					temp.setEmail(rs.getString("email"));
					ret += rs.getString("email") + " ";
				}
				if (rs.getString("phone") != null) {
					temp.setPhone(rs.getString("phone"));
					ret += rs.getString("phone") + " ";
				}
				contacts.add(temp);
				System.out.println(ret);
				id++;
			}
			return contacts;
		} catch (Exception ee) {
            throw new RuntimeException(ee);
		}
	}

	/**
	 * exports given list of contacts to database.
	 * @param kontakty list of contacts to be exported 
	 * @param baza name of a file with database
	 */
	public void bdExportKontakty(ArrayList<Contact> kontakty, String baza) {
		try (Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + baza)){

			conn.createStatement().executeUpdate("DELETE FROM Kontakty");

			PreparedStatement ps = conn.prepareStatement("INSERT INTO Kontakty VALUES (?, ?, ?, ?)");
			for (Contact contact : kontakty) {
				ps.setString(1, contact.getName());
				ps.setString(2, contact.getCompany());
				ps.setString(3, contact.getEmail());
				ps.setString(4, contact.getPhone());
				ps.executeUpdate();
			}
		} catch (Exception ee) {
            throw new RuntimeException(ee);
		}
	}

	/**
	 * import events from database in given file
	 * @param baza name of a file with database
	 * @return list of events imported from database
	 */
	public ArrayList<Event> bdImportEventy(String baza) {
		ArrayList<Event> eventy = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + baza)){
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Wydarzenia");

			String ret;
			int id = 0;
			while (rs.next()) {

				LocalDateTime start = LocalDateTime.parse(rs.getString("start"));
				LocalDateTime end = LocalDateTime.parse(rs.getString("end"));

				Event temp = new Event(rs.getString("tittle"), start, end);
				ret = id + ". " + rs.getString("tittle") + " S-" + start.toString() + " E-" + end.toString() + " ";
				if (!rs.getString("note").equals("")) {
					
					temp.setNote(rs.getString("note"));
					ret += rs.getString("note") + " ";
				}
				if (!rs.getString("place").equals("")) {
					temp.setPlace(rs.getString("place"));
					ret += rs.getString("place") + " ";
				}
				if (!rs.getString("before").equals("")) { //rs.getString("before") != ""
					LocalDateTime t = LocalDateTime.parse(rs.getString("before"));
					String snd = rs.getString("sound");
					if(snd.equals("")) {
						snd = AppParameters.getInstance().getSound();
					}
					temp.setNotification(t);
				}

				eventy.add(temp);
				System.out.println(ret);
				id++;
			}
			return eventy;
		} catch (Exception ee) {
            throw new RuntimeException(ee);
		}
	}

	/**
	 * exports list of events to database
	 * @param eventy list of events to be exported to database
	 * @param baza name of a file with database
	 */
	public void bdExportEventy(ArrayList<Event> eventy, String baza) {
		try (Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + baza)){

			conn.createStatement().executeUpdate("DELETE FROM Wydarzenia");

			PreparedStatement ps = conn.prepareStatement("INSERT INTO Wydarzenia " +
					"(tittle, start, end, note, place, sound, before) VALUES (?, ?, ?, ?, ?, ?, ?);");
			for (Event event : eventy) {
				ps.setString(1, event.getTittle());
				ps.setString(2, event.getStart().format(dateFormat));
				ps.setString(3, event.getEnd().format(dateFormat));
				ps.setString(4, event.getNote());
				ps.setString(5, event.getPlace());

				if (event.getNotification() != null) {
					ps.setString(6, event.getNotification().getSound());
					ps.setString(7, event.getNotification().getBefore().format(dateFormat));
				} else {
					ps.setString(6, "");
					ps.setString(7, "");
				}
				ps.executeUpdate();
			}
		} catch (Exception ee) {
            throw new RuntimeException(ee);
		}
	}

	/**
	 * exports list of events to XML file
	 * @param file file to export events to
	 * @param eventsToExport list of events to export
	 */
	public void xmlExport(File file, List<Event> eventsToExport) {
		try {
			XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));

			e.setPersistenceDelegate(LocalDateTime.class, // Event -- start, end
					new PersistenceDelegate() {
						@Override
						protected Expression instantiate(Object localDateTime, Encoder encdr) {
							return new Expression(localDateTime, LocalDateTime.class, "parse",
									new Object[] { localDateTime.toString() });
						}
					});

//			e.setPersistenceDelegate(LocalDateTime.class, // Alarm -- before
//					new PersistenceDelegate() {
//						@Override
//						protected Expression instantiate(Object localDateTime, Encoder encdr) {
//							return new Expression(localDateTime, LocalTime.class, "parse",
//									new Object[] { localDateTime.toString() });
//						}
//					});

			e.writeObject(eventsToExport);
			e.close();
		} catch (Exception e) {
            throw new RuntimeException(e);
		}
	}

	/**
	 * imports list of events from XML file
	 * @param file file to import events from
	 * @return list imported of events 
	 */
	@SuppressWarnings("unchecked")
	public List<Event> xmlImport(File file) {
		System.out.println("----------------------xmlImport:");
		try(XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)))) {
			return (ArrayList<Event>) d.readObject();
		} catch (Exception e) {
            throw new RuntimeException(e);
		}
	}
}
