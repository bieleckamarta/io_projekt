package gui;

import dane.Contact;
import dane.Event;
import gui.ContactsPane.ContactsRemover;
import system.Controller;
import system.events.DisplayedContactsChanged;
import system.events.DisplayedDateChanged;
import system.events.DisplayedEventsChanged;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * main GUI class of the program, sets up main visual components, adds ActionListeners to the Menu components and Buttons. Communicates with logic elements via Controller class, invoking its methods inside ActionListeners
 * @author Marta Bielecka
 *
 */
public class Application {

	/**
	 * main frame of the application, containing all other elements
	 */
	private JFrame frame;
	/**
	 * main panel of event view, containing table with events and components responsible for adding, editing, filtering and deleting events 
	 */
	private EventsPane eventsPane;
	/**
	 * main panel of contacts view, containing table with contacts and components responsible for adding and deleting contacts
	 */
	private ContactsPane contactsPane;
	
	/**
	 * returns main frame of the application
	 * @return main frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * menu bar of the application, containing JMenu components, responsible for application options
	 */
	private JMenuBar menuBar;
	/**
	 * component of main menu option
	 */
	private JMenu mnMenu;
	/**
	 * component responsible for access to setting dialog
	 */
	private JMenuItem mntmSettings;
	/**
	 * component responsible for closing application 
	 */
	private JMenuItem mntmClose;
	/**
	 * component responsible for opening import option, holding components responsible for import (from XML and database)
	 */
	private JMenu mnImport;
	/**
	 * component responsible for calling method importing from XML file
	 */
	private JMenuItem mntmFromXML;
	/**
	 * component responsible for calling method importing from database
	 */
	private JMenuItem mntmFromDatabase;
	/**
	 * component responsible for opening export option, holding components responsible for export (to XML and database)
	 */
	private JMenu mnExport;
	/**
	 * component responsible for calling method exporting to XML file
	 */
	private JMenuItem mntmToXML;
	/**
	 * component responsible for calling method exporting to database
	 */
	private JMenuItem mntmToDatabase;
	/**
	 * component activating help options
	 */
	private JMenu mnHelp;
	/**
	 * component responsible for showing dialog with informations about program
	 */
	private JMenuItem mntmAboutProgram;
	/**
	 * component containing all other application components, dividing main frame into three main parts - calendar view, contacts view and events view
	 */
	private JTabbedPane mainPane;

	/**
	 * component containing calendar view and controls to change displayed date and to add event
	 */
	private CalendarTable calendarTable;
	/**
	 * Controller object, responsible for communication between Application class and logic classes 
	 */
	private Controller control;
	/**
	 * JButton component responsible for showing event adding dialog from calendar panel
	 */
	private JButton addEventButtonCal;
	/**
	 * JButton component responsible for showing contact adding dialog from contacts panel
	 */
	private JButton addContactButton; 
	/**
	 * JButton component responsible for showing event adding dialog from events panel
	 */
	private JButton addEventButtonEv;
	/**
	 * JRadioButton component calling method showing all events
	 */
	private JRadioButton allEventsRadio;
	/**
	 * JRadioButton component calling method showing events in current day
	 */
	private JRadioButton dayEventsRadio; 
	/**
	 * JRadioButton component calling method showing events in current week
	 */
	private JRadioButton weekEventsRadio;
	/**
	 * JRadioButton component calling method showing events in current month
	 */
	private JRadioButton monthEventsRadio;
	/**
	 * JRadioButton component calling method showing events in current year
	 */
	private JRadioButton yearEventsRadio;
	/**
	 * table of string objects containing months' names
	 */
	private String [] months = 
		{"January", "February", "March", "April", 
				"May", "June", "July", "August", 
				"September", "October", "November", "December"};
	/**
	 * component allowing to change month to display in calendar panel
	 */
	private JComboBox<String> monthsCombo; 
	/**
	 * component allowing to change year to display in calendar panel
	 */
	private JSpinner yearsSpinner;
	/**
	 * option which events to display in event panel
	 */
	private int filterOptions = 0; 

	/**
	 * launch the application, sets main frame of the application visible
	 */
	public void mainApplication(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
					JOptionPane.showMessageDialog(frame, "Try");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * implementation of ActionListener class wrapped in exception catcher, allowing to catch all thrown exceptions. Implements design pattern "wrapper".
	 * @author Marta Bielecka
	 *
	 */
	static private class SafeActionListener implements ActionListener {
		/**
		 * ActionListener to be wrapped into exception
		 */
		private final ActionListener wrapped; 
		
		/**
		 * constructs SafeActionListener object with given ActionListener
		 * @param wrapped - ActionListener object to be wrapped
		 */
		public SafeActionListener(ActionListener wrapped) {
			this.wrapped = wrapped;
		}
		
		/**
		 * method setting action to be performed in ActionListener, containing exception catching
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				wrapped.actionPerformed(e);
			}catch(Exception exc) {
				String message = "Error ocurred: " + exc.getMessage();
				JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
				}
		}
	}
	
	/**
	 * Create the application. Constructs Controller object and registers listeners to certain actions, calls function playing alarm sounds. Sets up all components of GUI.
	 */
	public Application() {
		initialize();
		control = new Controller();
		control.registerListener(DisplayedDateChanged.class, calendarTable);
		control.registerListener(DisplayedContactsChanged.class, contactsPane);
		control.registerListener(DisplayedEventsChanged.class, eventsPane);
		
		control.playAlarms(frame);

		addCalendarListeners();	
		addMenuListeners();
				
		control.initialize();
	}

/**
 * creates calendar panel with calendar view and calendar options
 * @return calendar panel to be added to main frame
 */	
	public JSplitPane createCalendarOptionsPane () {
		JSplitPane calendarView = new JSplitPane();
		
		calendarView.setResizeWeight(0.8);
		calendarTable = new CalendarTable(); 		
		
		calendarView.setLeftComponent(new JScrollPane(calendarTable));
		calendarView.getLeftComponent().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				JScrollPane scrollPane = (JScrollPane)e.getComponent();
				JTable table = (JTable)scrollPane.getViewport().getView();
				table.setRowHeight(scrollPane.getViewport().getHeight()/6);
			}
		});
		
		JPanel calendarOptionsPane = new JPanel(new GridBagLayout()); 
		GridBagConstraints c = new GridBagConstraints();
		
		LocalDate now = LocalDate.now();
		
		monthsCombo = new JComboBox<String>(months);
		monthsCombo.setSelectedIndex(now.getMonthValue() - 1);
		
		c.gridx = 0; 
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER; 
		c.fill = GridBagConstraints.NONE;
		calendarOptionsPane.add(monthsCombo, c);		
		
		SpinnerModel yearsModel = new SpinnerNumberModel(LocalDate.now().getYear(),LocalDate.now().getYear() - 100, LocalDate.now().getYear()+100, 1 );
		yearsSpinner = new JSpinner(yearsModel);	
		c.gridx = 1; 
		c.gridy = 0;
		calendarOptionsPane.add(yearsSpinner,c);
		
		c.gridx = 1; 
		c.gridy = 1;
		calendarOptionsPane.add(addEventButtonCal,c);
		addEventButtonCal.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Event ev = EventAddingDialog.showDialog();
				if(ev != null) {
					while(ev.getStart() == null || ev.getEnd() == null) {
						if(ev.getStart() == null && ev.getEnd() == null) {
							JOptionPane.showMessageDialog(null,
									"Start and end time are incorrect", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
							ev.setStart(LocalDateTime.now());
							ev.setEnd(LocalDateTime.now());
							ev = EventEditingDialog.showDialog(ev);
							if(ev == null) break;
						} else if (ev.getEnd()== null ){
							JOptionPane.showMessageDialog(null,
									"End time is incorrect", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
							ev.setEnd(LocalDateTime.now());
							ev = EventEditingDialog.showDialog(ev);
							if(ev == null) break;

						} else {
							JOptionPane.showMessageDialog(null,
									"Start time is incorrect", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
							ev.setStart(LocalDateTime.now());
							ev = EventEditingDialog.showDialog(ev);
							if(ev == null) break;

						}
					} 
					
					if(ev != null) {

					while (ev.getStart().isAfter(ev.getEnd())) {
						JOptionPane.showMessageDialog(null,
								"Start time must be earlier than end time", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
						ev = EventEditingDialog.showDialog(ev);
						if(ev == null) break;

						}
					}
					
					if(ev != null) {
						control.addEvent(ev);
						control.changeDisplayedEvents(filterOptions);
					}
					
					
				}
			}
		});
	
		calendarView.setRightComponent(calendarOptionsPane);
		return calendarView;	
	}
	
	/**
	 * creates contacts panel with contacts view and contacts options
	 * @return contacts panel to be added to main frame
	 */
	public JSplitPane createContactsOptionsPane () {
		JSplitPane contactsView = new JSplitPane();
		contactsView.setResizeWeight(0.8);
		ContactsRemover remover = new ContactsRemover() {
			
			@Override
			public void removeContact(Contact con) {
				control.removeContact(con);
				
			}
		};
		contactsPane = new ContactsPane(remover); 
		contactsView.setLeftComponent(contactsPane);
		
		JPanel contactsOptionsPane = new JPanel(); 
		JLabel options = new JLabel("contacts options"); 	
		
		contactsOptionsPane.add(options);
		contactsOptionsPane.add(addContactButton);
		
		addContactButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("contact adding aplication");
				Contact con = ContactAddingDialog.showDialog();
				if(con != null) {
					while(con.getName().equals("") && con.getEmail().equals("") && con.getEmail().equals("") && con.getCompany().equals("")) {
						JOptionPane.showMessageDialog(null,
								"At least one field must not be empty", "Wrong data", JOptionPane.INFORMATION_MESSAGE);
						con =  ContactAddingDialog.showDialog();	
						if(con == null) break;
					}
					if(con != null) {
						control.addContact(con);	
					}
				}
			}
		});
		
		contactsView.setRightComponent(contactsOptionsPane);
		return contactsView;
	}
	
	/**
	 * creates events panel with events view and events options
	 * @return events panel to be added to main frame
	 */
	public JSplitPane createEventsOptionsPane () {
		JSplitPane eventsView = new JSplitPane();
		eventsView.setResizeWeight(0.8);		
		eventsPane = new EventsPane(new EventsPane.EventRemover() {
			@Override
			public void removeEvent(Event e) {
				if (e != null) {
					control.removeEvent(e);
					control.changeDisplayedEvents(filterOptions);
				}
			}
		}, new EventsPane.EventReplacer() {
			@Override
			public void replaceEvent(Event oldEvent) {
				Event newEvent = new Event(oldEvent.getTittle(), oldEvent.getStart(), oldEvent.getEnd(),
						oldEvent.getNote(), oldEvent.getPlace(), oldEvent.getNotification().getBefore());
				///////////////
				newEvent = EventEditingDialog.showDialog(newEvent);
				if(newEvent != null) {
					while(newEvent.getStart() == null || newEvent.getEnd() == null) {
						if(newEvent.getStart() == null) {
							JOptionPane.showMessageDialog(null,
									"Start time is incorrect", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
							newEvent.setStart(LocalDateTime.now());
							newEvent = EventEditingDialog.showDialog(newEvent);
						} else {
							JOptionPane.showMessageDialog(null,
									"End time is incorrect", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
							newEvent.setEnd(LocalDateTime.now());
							newEvent = EventEditingDialog.showDialog(newEvent);
						}
					} 
					while (newEvent.getStart().isAfter(newEvent.getEnd())) {
						JOptionPane.showMessageDialog(null,
								"Start time must be earlier than end time", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
						newEvent = EventEditingDialog.showDialog(newEvent);
					}
					
					control.replaceEvent(oldEvent, newEvent);
//					control.changeDisplayedEvents(filterOptions);
				}
				///////////////
//				newEvent = EventEditingDialog.showDialog(newEvent);
//				control.replaceEvent(oldEvent, newEvent);
			}
		});
		eventsView.setLeftComponent(new JScrollPane(eventsPane));
		JPanel eventsOptionsPane = new JPanel();
		eventsOptionsPane.setLayout(new BoxLayout(eventsOptionsPane, BoxLayout.PAGE_AXIS)); 
		JLabel options = new JLabel("contacts options"); 
		eventsOptionsPane.add(options);
		
		
		JLabel select = new JLabel("show events:");
		eventsOptionsPane.add(select);
		
		allEventsRadio = new JRadioButton("all events");
		eventsOptionsPane.add(allEventsRadio);
		allEventsRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				filterOptions = 0;
				control.changeDisplayedEvents(filterOptions);
				
			}
		});
		
		dayEventsRadio = new JRadioButton("today");
		eventsOptionsPane.add(dayEventsRadio);
		dayEventsRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				filterOptions = 1;
				control.changeDisplayedEvents(filterOptions);
			}
		});
		
		weekEventsRadio = new JRadioButton("this week");
		eventsOptionsPane.add(weekEventsRadio);
		weekEventsRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				filterOptions = 2;
				control.changeDisplayedEvents(filterOptions);		
			}
		});
		
		monthEventsRadio = new JRadioButton("this month");
		eventsOptionsPane.add(monthEventsRadio);
		monthEventsRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				filterOptions = 3;
				control.changeDisplayedEvents(filterOptions);
				
			}
		});
		
		yearEventsRadio = new JRadioButton("this year");
		eventsOptionsPane.add(yearEventsRadio);
		yearEventsRadio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				filterOptions = 4;
				control.changeDisplayedEvents(filterOptions);
			}
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(allEventsRadio);
		group.add(dayEventsRadio);
		group.add(weekEventsRadio);
		group.add(monthEventsRadio);
		group.add(yearEventsRadio);
		
		allEventsRadio.setSelected(true);
		
		eventsOptionsPane.add(addEventButtonEv);
		
		addEventButtonEv.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Event ev = EventAddingDialog.showDialog();
				if(ev != null) {
					while(ev.getStart() == null || ev.getEnd() == null) {
						if(ev.getStart() == null && ev.getEnd() == null) {
							JOptionPane.showMessageDialog(null,
									"Start and end time are incorrect", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
							ev.setStart(LocalDateTime.now());
							ev.setEnd(LocalDateTime.now());
							ev = EventEditingDialog.showDialog(ev);
							if(ev == null) break;
						} else if (ev.getEnd()== null ){
							JOptionPane.showMessageDialog(null,
									"End time is incorrect", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
							ev.setEnd(LocalDateTime.now());
							ev = EventEditingDialog.showDialog(ev);
							if(ev == null) break;

						} else {
							JOptionPane.showMessageDialog(null,
									"Start time is incorrect", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
							ev.setStart(LocalDateTime.now());
							ev = EventEditingDialog.showDialog(ev);
							if(ev == null) break;

						}
					} 
					
					if(ev != null) {

					while (ev.getStart().isAfter(ev.getEnd())) {
						JOptionPane.showMessageDialog(null,
								"Start time must be earlier than end time", "Wrong date", JOptionPane.INFORMATION_MESSAGE);
						ev = EventEditingDialog.showDialog(ev);
						if(ev == null) break;

						}
					}
					
					if(ev != null) {
						control.addEvent(ev);
						control.changeDisplayedEvents(filterOptions);
					}
					
					
				}
				
			}
		});
		
		eventsView.setRightComponent(eventsOptionsPane);
		return eventsView;
	}
	
	/**
	 * adds ActionListeners to components, allowing to change displayed date
	 */
	public void addCalendarListeners() {
		monthsCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int year; 
				if(yearsSpinner.getValue()!= null) {
					year = (int)yearsSpinner.getValue();
				} else {
					year = 2018;
				}
				control.changeDisplayedDate(monthsCombo.getSelectedIndex(), year);				
			}
			
		});
		
		yearsSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				int year; 
				if(yearsSpinner.getValue()!= null) {
					year = (int)yearsSpinner.getValue();
				} else {
					year = 2018;
				}
				control.changeDisplayedDate(monthsCombo.getSelectedIndex(),year);		
			}
		});
	}
	
	/**
	 * adds ActionListeners to menu components
	 */
	public void addMenuListeners() {
		mntmClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {			
				int result = JOptionPane.showConfirmDialog(
			            frame,
			            "Are you sure you want to exit the application?",
			            "Exit Application",
			            JOptionPane.YES_NO_OPTION);
			 
			        if (result == JOptionPane.YES_OPTION) {
			        	System.exit(0);
			        }	
			}
		});

		
//		mntmSettings.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				LocalDateTime dueTime = SettingsDialog.showSettingsDialog(); 
//				if(dueTime != null) {
//				control.removeOldEvents(dueTime);	
//				}
//			}		
//		});
		
//		mntmFromXML.addActionListener(new SafeActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				JFileChooser chooser = new JFileChooser();
//				int userChoice = chooser.showOpenDialog(null);
//
//				if (userChoice == JFileChooser.APPROVE_OPTION) {
//					File selectedFile = chooser.getSelectedFile();
//
//					if (selectedFile.exists()) {
//						control.importEventsFromXml(selectedFile);
//						JOptionPane.showMessageDialog(null,
//								"Import succeeded.", "Success!", JOptionPane.INFORMATION_MESSAGE);
//					} else {
//						JOptionPane.showMessageDialog(null,
//								"File " + selectedFile.getName() + " does not exist!", "Error",
//								JOptionPane.WARNING_MESSAGE);
//					}
//				}
//			}
//		}));
		
		mntmFromDatabase.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int userChoice = chooser.showOpenDialog(null);

				if (userChoice == JFileChooser.APPROVE_OPTION) {
					File selectedFile = chooser.getSelectedFile();

					if (selectedFile.exists()) {
						control.importDataFromDatabase(selectedFile.getAbsolutePath());
                        JOptionPane.showMessageDialog(null,
								"Import succeeded.", "Success!", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null,
								"Database " + selectedFile.getName() + " does not exist!", "Error",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
//		mntmToXML.addActionListener(new SafeActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				JFileChooser chooser = new JFileChooser();
//				int userChoice = chooser.showSaveDialog(null);
//
//				if (userChoice == JFileChooser.APPROVE_OPTION) {
//					File selectedFile = chooser.getSelectedFile();
//					if (selectedFile.exists()) {
//						userChoice = JOptionPane.showConfirmDialog(null,
//								"File " + selectedFile.getName() + " already exists, overwrite?",
//								"Overwrite?", JOptionPane.YES_NO_OPTION);
//					}
//
//					if (!selectedFile.exists() || userChoice == JOptionPane.YES_OPTION) {
//						control.exportEventsToXml(selectedFile);
//						JOptionPane.showMessageDialog(null,
//								"Export succeeded.", "Success!", JOptionPane.INFORMATION_MESSAGE);
//					}
//				}
//			}
//		}));
		
		mntmToDatabase.addActionListener(new SafeActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int userChoice = chooser.showSaveDialog(null);

				if (userChoice == JFileChooser.APPROVE_OPTION) {
					File selectedFile = chooser.getSelectedFile();
					if (selectedFile.exists()) {
						userChoice = JOptionPane.showConfirmDialog(null,
								"Selected database " + selectedFile.getName() + " will be cleared, continue?",
								"Overwrite?", JOptionPane.YES_NO_OPTION);
						if (userChoice == JOptionPane.YES_OPTION) {
							control.exportDataToDatabase(selectedFile.getAbsolutePath());
							JOptionPane.showMessageDialog(null,
									"Export succeeded.", "Success!", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"You must select existing database!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}));
		
		mntmAboutProgram.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				control.showAboutProgramWindow();			
			}			
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addWindowListener( new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		        JFrame frame = (JFrame)e.getSource();
		 
		        int result = JOptionPane.showConfirmDialog(
		            frame,
		            "Are you sure you want to exit the application?",
		            "Exit Application",
		            JOptionPane.YES_NO_OPTION);
		 
		        if (result == JOptionPane.YES_OPTION) {
		            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    } else { 
		    	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    }
		    }
		});
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		mnMenu.setMnemonic(KeyEvent.VK_M);
		
//		mntmSettings = new JMenuItem("Settings", KeyEvent.VK_N);
//		KeyStroke settingsKeyStroke = KeyStroke.getKeyStroke('S',InputEvent.CTRL_DOWN_MASK);
//		mntmSettings.setAccelerator(settingsKeyStroke);
//		mnMain.add(mntmSettings);

		
		mntmClose = new JMenuItem("Close");
		KeyStroke closeKeyStroke = KeyStroke.getKeyStroke('X',InputEvent.CTRL_DOWN_MASK);
		mntmClose.setAccelerator(closeKeyStroke);
		mnMenu.add(mntmClose);
		
		mnImport = new JMenu("Import");
		mnImport.setMnemonic(KeyEvent.VK_I);
		menuBar.add(mnImport);
		
//		mntmFromXML = new JMenuItem("from XML");
//		KeyStroke fromXMLKeyStroke = KeyStroke.getKeyStroke('I', InputEvent.CTRL_DOWN_MASK);
//		mntmFromXML.setAccelerator(fromXMLKeyStroke);
//		mnImport.add(mntmFromXML);
		
		mntmFromDatabase = new JMenuItem("from Database");
		mnImport.add(mntmFromDatabase);
		
		mnExport = new JMenu("Export");
		menuBar.add(mnExport);
		
//		mntmToXML = new JMenuItem("to XML");
//		mnExport.add(mntmToXML);
		
		mntmToDatabase = new JMenuItem("to Database");
		mnExport.add(mntmToDatabase);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmAboutProgram = new JMenuItem("About Program");
		mnHelp.add(mntmAboutProgram);
	
		addEventButtonCal = new JButton("add event");
		addEventButtonCal.setMnemonic(KeyEvent.VK_N);
		
		addEventButtonEv = new JButton("add event");
		addEventButtonEv.setMnemonic(KeyEvent.VK_N);
		
		addContactButton = new JButton("add contact");
		addContactButton.setMnemonic(KeyEvent.VK_N);
		
		mainPane = new JTabbedPane(JTabbedPane.TOP);
		frame.add(mainPane);
		
		mainPane.addTab("Calendar", createCalendarOptionsPane());
		mainPane.addTab("Contacts", createContactsOptionsPane());
		mainPane.addTab("Events", createEventsOptionsPane());	
	}
}
