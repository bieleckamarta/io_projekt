package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import dane.Event;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JComboBox;

/**
 * this class is responsible for event adding dialog looks and functions
 * @author Marta Bielecka
 *
 */
public class EventAddingDialog extends JDialog {

	/**
	 * main panel of this dialog, containing all other components
	 */
	private final JPanel contentPanel = new JPanel();
	/**
	 * textfield where user can type new event's title
	 */
	private JTextField TitleTexfield;
	/**
	 * textfield where user can type short note about new event
	 */
	private JTextField NoteTextfield;
	/**
	 * textfield where user can type new event's place
	 */
	private JTextField PlaceTextfield;
	/**
	 * textfield where user can type new event's start hour
	 */
	private JTextField StartHourTextfield;
	/**
	 * textfield where user can type new event's start minute
	 */
	private JTextField StartMinuteTextfield;
	/**
	 * textfield where user can type new event's end hour
	 */
	private JTextField EndHourTextfield;
	/**
	 * textfield where user can type new event's end minute
	 */
	private JTextField EndMinuteTextfield;
	/**
	 * JButton confirming new event's creation, clicking it constructs new event with given data and closes the dialog
	 */
	private JButton okButton;
	/**
	 * JButton canceling inserted changes, clicking it closes the dialog without creating new event
	 */
	private JButton cancelButton;
	/**
	 * checkbox indicating, if new event should be constructed with alarm to display
	 */
	private JCheckBox AlarmRadio;
	/**
	 * component to choose end date of new event
	 */
	private JDateChooser endDateChooser;
	/**
	 * component to choose start date of new event
	 */
	private JDateChooser startDateChooser;
	/**
	 * textfield where user can type how many time units before start should alarm be displayed
	 */
	private JTextField AlarmTimeTextfield;
	/**
	 * allows user choose time units in which time of alarm set before beginning of the new event is counted
	 */
	private JComboBox<String> AlarmTimeCombo;
	/**
	 * String objects table with names of time units to show in AlarmTimeCombo
	 */
	private String [] alarmOptions = {"minutes", "hours", "days"};
	/**
	 * boolean variable showing if okButton was clicked by user
	 */
	private boolean okClicked = false;
	/**
	 * boolean variable showing if alarm checkbox was selected by user
	 */
	private int setAlarm = 0;

	/**
	 * Converts given Date and LocalTime objects to LocalDateTime object
	 * @param date date to be converted to LocalDateTime
	 * @param time time to be converted to LocalDateTime
	 * @return LocalDateTime object obtained from given components
	 */
	public LocalDateTime convertToLocalDateTime(Date date, LocalTime time) {
		LocalDate newDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		return LocalDateTime.of(newDate, time);
	}
	
	/**
	 * shows dialog, sets it visible and, if okButton was clicked by user, creates new event with data typed in by the user.
	 * @return newly created Event object
	 */
	public static Event showDialog() {
		try {
			EventAddingDialog dialog = new EventAddingDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			if (dialog.okClicked) {
				String title = dialog.TitleTexfield.getText();
				String note = dialog.NoteTextfield.getText();
				String place = dialog.PlaceTextfield.getText();

				String startHourString = dialog.StartHourTextfield.getText();
				String startMinuteString = dialog.StartMinuteTextfield.getText();
				int startHour;
				int startMinute;
				
				if(startHourString.isEmpty()) {
					startHour = LocalDateTime.now().getHour();
				} else {
					startHour = Integer.parseInt(startHourString);
				}
				
				if(startMinuteString.isEmpty()) {
					startMinute = LocalDateTime.now().getMinute();
				} else {
					startMinute = Integer.parseInt(startMinuteString);
				}
				
				Date startDate = dialog.startDateChooser.getDate();
				
				LocalTime newStartTime;
				LocalDateTime startTime;
				
				if(startHour < 0 || startHour > 23 || startMinute < 0 || startMinute > 59 || startDate == null) {
					startTime = null; 
				} else {
					newStartTime = LocalTime.of(startHour, startMinute);
					startTime = dialog.convertToLocalDateTime(startDate, newStartTime);
				}
	
				
				Date endDate = dialog.endDateChooser.getDate();
				
				String endHourString = dialog.EndHourTextfield.getText();
				String endMinuteString = dialog.EndMinuteTextfield.getText();
				int endHour;
				int endMinute;
				
				if(endHourString.isEmpty()) {
					endHour = LocalDateTime.now().getHour();
				} else {
					endHour = Integer.parseInt(endHourString);
				}
				
				if(endMinuteString.isEmpty()) {
					endMinute = LocalDateTime.now().getMinute();
				} else {
					endMinute = Integer.parseInt(endMinuteString);
				}
								
				LocalDateTime endTime;

				if(endHour < 0 || endHour > 23 || endMinute < 0 || endMinute > 59 || endDate == null) {
					endTime = null; 
				} else {
					LocalTime newEndTime = LocalTime.of(endHour, endMinute);
					endTime = dialog.convertToLocalDateTime(endDate, newEndTime);
				}
				
//				if(endHour < 0 || startHour > 23) {
//					JOptionPane.showMessageDialog(dialog, "Incorrect end hour value");
//					dialog.setVisible(true);
//					dialog.okClicked = false;
//				}
//				if(endMinute < 0 || startMinute > 59) {
//					JOptionPane.showMessageDialog(dialog, "Incorrect end minute value");
//					dialog.setVisible(true);
//					dialog.okClicked = false;
//				}
				
				
				
//				if(endTime.isBefore(startTime)) {
//					JOptionPane.showMessageDialog(dialog, "Start date cannot be after end date");
//					dialog.setVisible(true);
//					dialog.okClicked = false;	
//				}
				int number;
				int option;
				if(dialog.setAlarm == 1) {
			
					number = Integer.parseInt(dialog.AlarmTimeTextfield.getText());
					option = dialog.AlarmTimeCombo.getSelectedIndex();
					LocalDateTime alarmTime;
					if(option == 0) {
						alarmTime = startTime.minusMinutes(number);
					} else if(option == 1) {
						alarmTime = startTime.minusHours(number);
					} else {
						alarmTime = startTime.minus(Period.ofDays(number));
					}
					System.out.println(alarmTime);
					Event event = new Event(title, startTime, endTime, note, place, alarmTime);
					dialog.dispose();
					return event;
				} else {
					Event event = new Event(title, startTime, endTime, note, place,null);
					dialog.dispose();
					return event;
				}
			} else {
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	

	/**
	 * Create the dialog. initializes and adds all components to main panel of this dialog. Defines the consequences of clicking okButton or cancelButton
	 */
	public EventAddingDialog() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 521);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{107, 61, 116, 31, 0};
		gbl_contentPanel.rowHeights = new int[]{22, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel EventTitleLabel = new JLabel("Event title:");
			GridBagConstraints gbc_EventTitleLabel = new GridBagConstraints();
			gbc_EventTitleLabel.anchor = GridBagConstraints.WEST;
			gbc_EventTitleLabel.insets = new Insets(0, 0, 5, 5);
			gbc_EventTitleLabel.gridx = 0;
			gbc_EventTitleLabel.gridy = 0;
			contentPanel.add(EventTitleLabel, gbc_EventTitleLabel);
		}
		{
			TitleTexfield = new JTextField();
			GridBagConstraints gbc_TitleTexfield = new GridBagConstraints();
			gbc_TitleTexfield.gridwidth = 2;
			gbc_TitleTexfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_TitleTexfield.anchor = GridBagConstraints.NORTH;
			gbc_TitleTexfield.insets = new Insets(0, 0, 5, 5);
			gbc_TitleTexfield.gridx = 1;
			gbc_TitleTexfield.gridy = 0;
			contentPanel.add(TitleTexfield, gbc_TitleTexfield);
			TitleTexfield.setColumns(10);
		}
		{
			JLabel NoteLabel = new JLabel("Note:");
			GridBagConstraints gbc_NoteLabel = new GridBagConstraints();
			gbc_NoteLabel.insets = new Insets(0, 0, 5, 5);
			gbc_NoteLabel.anchor = GridBagConstraints.WEST;
			gbc_NoteLabel.gridx = 0;
			gbc_NoteLabel.gridy = 1;
			contentPanel.add(NoteLabel, gbc_NoteLabel);
		}
		{
			NoteTextfield = new JTextField();
			GridBagConstraints gbc_NoteTextfield = new GridBagConstraints();
			gbc_NoteTextfield.gridwidth = 2;
			gbc_NoteTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_NoteTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_NoteTextfield.gridx = 1;
			gbc_NoteTextfield.gridy = 1;
			contentPanel.add(NoteTextfield, gbc_NoteTextfield);
			NoteTextfield.setColumns(10);
		}
		{
			JLabel PlaceLabel = new JLabel("Place:");
			GridBagConstraints gbc_PlaceLabel = new GridBagConstraints();
			gbc_PlaceLabel.anchor = GridBagConstraints.WEST;
			gbc_PlaceLabel.insets = new Insets(0, 0, 5, 5);
			gbc_PlaceLabel.gridx = 0;
			gbc_PlaceLabel.gridy = 2;
			contentPanel.add(PlaceLabel, gbc_PlaceLabel);
		}
		{
			PlaceTextfield = new JTextField();
			GridBagConstraints gbc_PlaceTextfield = new GridBagConstraints();
			gbc_PlaceTextfield.gridwidth = 2;
			gbc_PlaceTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_PlaceTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_PlaceTextfield.gridx = 1;
			gbc_PlaceTextfield.gridy = 2;
			contentPanel.add(PlaceTextfield, gbc_PlaceTextfield);
			PlaceTextfield.setColumns(10);
		}
		{
			JLabel StartTimeLabel = new JLabel("Start time:");
			GridBagConstraints gbc_StartTimeLabel = new GridBagConstraints();
			gbc_StartTimeLabel.anchor = GridBagConstraints.WEST;
			gbc_StartTimeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_StartTimeLabel.gridx = 0;
			gbc_StartTimeLabel.gridy = 3;
			contentPanel.add(StartTimeLabel, gbc_StartTimeLabel);
		}
		{
			JLabel startDateLabel = new JLabel("Date:");
			GridBagConstraints gbc_startDateLabel = new GridBagConstraints();
			gbc_startDateLabel.anchor = GridBagConstraints.NORTHWEST;
			gbc_startDateLabel.insets = new Insets(0, 0, 5, 5);
			gbc_startDateLabel.gridx = 1;
			gbc_startDateLabel.gridy = 5;
			contentPanel.add(startDateLabel, gbc_startDateLabel);
		}
		{
			startDateChooser = new JDateChooser();
			GridBagConstraints gbc_startDateChooser = new GridBagConstraints();
			gbc_startDateChooser.anchor = GridBagConstraints.NORTH;
			gbc_startDateChooser.insets = new Insets(0, 0, 5, 5);
			gbc_startDateChooser.fill = GridBagConstraints.HORIZONTAL;
			gbc_startDateChooser.gridx = 2;
			gbc_startDateChooser.gridy = 5;
			contentPanel.add(startDateChooser, gbc_startDateChooser);
		}
		{
			JLabel StartHourLabel = new JLabel("Hour:");
			GridBagConstraints gbc_StartHourLabel = new GridBagConstraints();
			gbc_StartHourLabel.anchor = GridBagConstraints.WEST;
			gbc_StartHourLabel.insets = new Insets(0, 0, 5, 5);
			gbc_StartHourLabel.gridx = 1;
			gbc_StartHourLabel.gridy = 7;
			contentPanel.add(StartHourLabel, gbc_StartHourLabel);
		}
		{
			StartHourTextfield = new JTextField();
			GridBagConstraints gbc_StartHourTextfield = new GridBagConstraints();
			gbc_StartHourTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_StartHourTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_StartHourTextfield.gridx = 2;
			gbc_StartHourTextfield.gridy = 7;
			contentPanel.add(StartHourTextfield, gbc_StartHourTextfield);
			StartHourTextfield.setColumns(10);
		}
		{
			JLabel StartMinuteLabel = new JLabel("Minute:");
			GridBagConstraints gbc_StartMinuteLabel = new GridBagConstraints();
			gbc_StartMinuteLabel.anchor = GridBagConstraints.WEST;
			gbc_StartMinuteLabel.insets = new Insets(0, 0, 5, 5);
			gbc_StartMinuteLabel.gridx = 1;
			gbc_StartMinuteLabel.gridy = 8;
			contentPanel.add(StartMinuteLabel, gbc_StartMinuteLabel);
		}
		{
			StartMinuteTextfield = new JTextField();
			GridBagConstraints gbc_StartMinuteTextfield = new GridBagConstraints();
			gbc_StartMinuteTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_StartMinuteTextfield.anchor = GridBagConstraints.NORTH;
			gbc_StartMinuteTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_StartMinuteTextfield.gridx = 2;
			gbc_StartMinuteTextfield.gridy = 8;
			contentPanel.add(StartMinuteTextfield, gbc_StartMinuteTextfield);
			StartMinuteTextfield.setColumns(10);
		}
		{
			JLabel EntTimeLabel = new JLabel("End Time:");
			GridBagConstraints gbc_EntTimeLabel = new GridBagConstraints();
			gbc_EntTimeLabel.anchor = GridBagConstraints.WEST;
			gbc_EntTimeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_EntTimeLabel.gridx = 0;
			gbc_EntTimeLabel.gridy = 9;
			contentPanel.add(EntTimeLabel, gbc_EntTimeLabel);
		}
		{
			JLabel startDateLabel = new JLabel("Date:");
			GridBagConstraints gbc_startDateLabel = new GridBagConstraints();
			gbc_startDateLabel.anchor = GridBagConstraints.NORTHWEST;
			gbc_startDateLabel.insets = new Insets(0, 0, 5, 5);
			gbc_startDateLabel.gridx = 1;
			gbc_startDateLabel.gridy = 10;
			contentPanel.add(startDateLabel, gbc_startDateLabel);
		}
		{
			endDateChooser = new JDateChooser();
			GridBagConstraints gbc_endDateChooser = new GridBagConstraints();
			gbc_endDateChooser.anchor = GridBagConstraints.NORTH;
			gbc_endDateChooser.insets = new Insets(0, 0, 5, 5);
			gbc_endDateChooser.fill = GridBagConstraints.HORIZONTAL;
			gbc_endDateChooser.gridx = 2;
			gbc_endDateChooser.gridy = 10;
			contentPanel.add(endDateChooser, gbc_endDateChooser);
		}
		{
			JLabel EndHourLabel = new JLabel("Hour:");
			GridBagConstraints gbc_EndHourLabel = new GridBagConstraints();
			gbc_EndHourLabel.anchor = GridBagConstraints.WEST;
			gbc_EndHourLabel.insets = new Insets(0, 0, 5, 5);
			gbc_EndHourLabel.gridx = 1;
			gbc_EndHourLabel.gridy = 13;
			contentPanel.add(EndHourLabel, gbc_EndHourLabel);
		}
		{
			EndHourTextfield = new JTextField();
			GridBagConstraints gbc_EndHourTextfield = new GridBagConstraints();
			gbc_EndHourTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_EndHourTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_EndHourTextfield.gridx = 2;
			gbc_EndHourTextfield.gridy = 13;
			contentPanel.add(EndHourTextfield, gbc_EndHourTextfield);
			EndHourTextfield.setColumns(10);
		}
		{
			JLabel EndMinuteLabel = new JLabel("Minute:");
			GridBagConstraints gbc_EndMinuteLabel = new GridBagConstraints();
			gbc_EndMinuteLabel.anchor = GridBagConstraints.WEST;
			gbc_EndMinuteLabel.insets = new Insets(0, 0, 5, 5);
			gbc_EndMinuteLabel.gridx = 1;
			gbc_EndMinuteLabel.gridy = 14;
			contentPanel.add(EndMinuteLabel, gbc_EndMinuteLabel);
		}
		{
			EndMinuteTextfield = new JTextField();
			GridBagConstraints gbc_EndMinuteTextfield = new GridBagConstraints();
			gbc_EndMinuteTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_EndMinuteTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_EndMinuteTextfield.gridx = 2;
			gbc_EndMinuteTextfield.gridy = 14;
			contentPanel.add(EndMinuteTextfield, gbc_EndMinuteTextfield);
			EndMinuteTextfield.setColumns(10);
		}
		{
			AlarmRadio = new JCheckBox("Alarm");
			GridBagConstraints gbc_AlarmRadio = new GridBagConstraints();
			gbc_AlarmRadio.insets = new Insets(0, 0, 0, 5);
			gbc_AlarmRadio.gridx = 0;
			gbc_AlarmRadio.gridy = 16;
			AlarmRadio.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					setAlarm = e.getStateChange();
				}
			});
			AlarmRadio.setMnemonic(KeyEvent.VK_C);
			contentPanel.add(AlarmRadio, gbc_AlarmRadio);
		}
		{
			AlarmTimeCombo = new JComboBox<String>(alarmOptions);
			GridBagConstraints gbc_AlarmTimeCombo = new GridBagConstraints();
			gbc_AlarmTimeCombo.insets = new Insets(0, 0, 0, 5);
			gbc_AlarmTimeCombo.fill = GridBagConstraints.HORIZONTAL;
			gbc_AlarmTimeCombo.gridx = 2;
			gbc_AlarmTimeCombo.gridy = 16;
			contentPanel.add(AlarmTimeCombo, gbc_AlarmTimeCombo);
		}
		{
			AlarmTimeTextfield = new JTextField();
			GridBagConstraints gbc_AlarmTimeTextfield = new GridBagConstraints();
			gbc_AlarmTimeTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_AlarmTimeTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_AlarmTimeTextfield.gridx = 1;
			gbc_AlarmTimeTextfield.gridy = 16;
			contentPanel.add(AlarmTimeTextfield, gbc_AlarmTimeTextfield);
			EndMinuteTextfield.setColumns(10);
		}
		{
			JLabel BeforeLabel = new JLabel("before");
			GridBagConstraints gbc_BeforeLabel = new GridBagConstraints();
			gbc_BeforeLabel.insets = new Insets(0, 0, 0, 5);
			gbc_BeforeLabel.gridx = 3;
			gbc_BeforeLabel.gridy = 16;
			contentPanel.add(BeforeLabel, gbc_BeforeLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			
			okButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					okClicked = true;
					setVisible(false);
				}
			});
			
			cancelButton.addActionListener(new ActionListener() {
				
				@Override			
				public void actionPerformed(ActionEvent e) {
					okClicked = false; 
					setVisible(false);
				}
			});
		}
	}
}
