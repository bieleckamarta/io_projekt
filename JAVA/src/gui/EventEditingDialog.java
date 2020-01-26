package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.toedter.calendar.JDateChooser;

import dane.Alarm;
import dane.Event;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/**
 * this class is responsible for event editing dialog looks and functions
 * @author Marta Bielecka
 *
 */
public class EventEditingDialog extends JDialog {

	/**
	 * main panel of this dialog, containing all other components
	 */
	private final JPanel contentPanel = new JPanel();
	/**
	 * textfield where user can change title of edited event
	 */
	private JTextField titleTextfield;
	/**
	 * textfield where user can change note about edited event
	 */
	private JTextField noteTextfield;
	/**
	 * textfield where user can change place of edited event
	 */
	private JTextField placeTextfield;
	/**
	 * textfield where user can change start hour of edited event
	 */
	private JTextField startHourTextfield;
	/**
	 * textfield where user can change start minute of edited event
	 */
	private JTextField startMinuteTextfield;
	/**
	 * textfield where user can change end hour of edited event
	 */
	private JTextField endHourTextfield;
	/**
	 * textfield where user can change end minute of edited event
	 */
	private JTextField endMinuteTextfield;
	/**
	 * textfield where user can change amount of units of time before start time of edited event, when added alarm shoud be displayed
	 */
	private JTextField alarmTextfield;
	/**
	 * component to choose new start date of edited event
	 */
	private JDateChooser startDateChooser;
	/**
	 * component to choose new end date of edited event
	 */
	private JDateChooser endDateChooser;
	/**
	 * allows user choose time units in which time of alarm set before beginning of the event is counted
	 */
	private JComboBox<String> alarmCombo; 
	/**
	 * checkbox indicating, if edited event should be constructed with alarm to display
	 */
	private JCheckBox alarmCheckbox;
	/**
	 * JButton confirming event's edition , clicking it applies inserted changes and closes the dialog
	 */
	private JButton okButton;
	/**
	 * JButton canceling inserted changes, clicking it closes the dialog without applying changes
	 */
	private JButton cancelButton;
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
	 * shows dialog, sets it visible and, if okButton was clicked by user, applies inserted changes to the event.
	 * @return newly created Event object
	 */
	public static Event showDialog(Event eventToEdit) {
		try {
			EventEditingDialog dialog = new EventEditingDialog(eventToEdit);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			if(eventToEdit.getNotification() == null) {
				dialog.setAlarm = 0;
			} else {
				dialog.setAlarm = 1;
			}
			if(dialog.okClicked == true) {
				
				String title = dialog.titleTextfield.getText();
				String note = dialog.noteTextfield.getText();
				String place = dialog.placeTextfield.getText();
				
				Date startDate = dialog.startDateChooser.getDate();
				
				String startHourString = dialog.startHourTextfield.getText();
				String startMinuteString = dialog.startMinuteTextfield.getText();
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
				
//				Date startDate = dialog.startDateChooser.getDate();
				LocalTime newStartTime;
				LocalDateTime startTime;
				
				if(startHour < 0 || startHour > 23 || startMinute < 0 || startMinute > 59) {
					startTime = null; 
				} else {
					newStartTime = LocalTime.of(startHour, startMinute);
					startTime = dialog.convertToLocalDateTime(startDate, newStartTime);
				}
				eventToEdit.setStart(startTime);
				
//				int startHour = Integer.parseInt(dialog.startHourTextfield.getText());
//				int startMinute = Integer.parseInt(dialog.startMinuteTextfield.getText());
//				if(startHour < 0 || startHour > 23) {
//					JOptionPane.showMessageDialog(dialog, "Incorrect start hour value");
//					dialog.setVisible(true);
//					dialog.okClicked = false;
//				}
//				if(startMinute < 0 || startMinute > 59) {
//					JOptionPane.showMessageDialog(dialog, "Incorrect start minute value");
//					dialog.setVisible(true);
//					dialog.okClicked = false;
//				}
//				LocalTime startTime = LocalTime.of(startHour,startMinute);
//				LocalDateTime newStartDate = dialog.convertToLocalDateTime(startDate, startTime);
//				eventToEdit.setStart(newStartDate);
				
				Date endDate = dialog.endDateChooser.getDate();
				
				String endHourString = dialog.endHourTextfield.getText();
				String endMinuteString = dialog.endMinuteTextfield.getText();
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

				if(endHour < 0 || endHour > 23 || endMinute < 0 || endMinute > 59) {
					endTime = null; 
				} else {
					LocalTime newEndTime = LocalTime.of(endHour, endMinute);
					endTime = dialog.convertToLocalDateTime(endDate, newEndTime);
				}
				
				eventToEdit.setEnd(endTime);

				
//				int endHour = Integer.parseInt(dialog.startHourTextfield.getText());
//				int endMinute = Integer.parseInt(dialog.startMinuteTextfield.getText());
//				if(startHour < 0 || endHour > 23) {
//					JOptionPane.showMessageDialog(dialog, "Incorrect start hour value");
//					dialog.setVisible(true);
//					dialog.okClicked = false;
//				}
//				if(startMinute < 0 || endMinute > 59) {
//					JOptionPane.showMessageDialog(dialog, "Incorrect start minute value");
//					dialog.setVisible(true);
//					dialog.okClicked = false;
//				}
//				LocalTime newEndTime = LocalTime.of(endHour,endMinute);
//				LocalDateTime newEndDate = dialog.convertToLocalDateTime(endDate, newEndTime);
//				
//				if(newEndDate.isBefore(newStartDate)) {
//					JOptionPane.showMessageDialog(dialog, "Start date cannot be after end date");
//					dialog.setVisible(true);
//					dialog.okClicked = false;	
//				}
//				eventToEdit.setEnd(newEndDate);
				eventToEdit.setTittle(title);
				eventToEdit.setNote(note);
				eventToEdit.setPlace(place);
				
				int number;
				int option;

				if(dialog.setAlarm == 1) {
					number = Integer.parseInt(dialog.alarmTextfield.getText());
					option = dialog.alarmCombo.getSelectedIndex();
					LocalDateTime alarmTime;
					if(option == 0) {
						alarmTime = startTime.minusMinutes(number);
					} else if(option == 1) {
						alarmTime = startTime.minusHours(number);
					} else {
						alarmTime = startTime.minus(Period.ofDays(number));
					}
					eventToEdit.setNotification(alarmTime);
					dialog.dispose();
					return eventToEdit;
				} else {
					dialog.dispose();
					return eventToEdit;
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
	public EventEditingDialog(Event e) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 469, 445);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{85, 79, 82, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel titleLabel = new JLabel("Title:");
			GridBagConstraints gbc_titleLabel = new GridBagConstraints();
			gbc_titleLabel.anchor = GridBagConstraints.WEST;
			gbc_titleLabel.insets = new Insets(0, 0, 5, 5);
			gbc_titleLabel.gridx = 0;
			gbc_titleLabel.gridy = 0;
			contentPanel.add(titleLabel, gbc_titleLabel);
		}
		{
			titleTextfield = new JTextField(e.getTittle());
			GridBagConstraints gbc_titleTextfield = new GridBagConstraints();
			gbc_titleTextfield.gridwidth = 2;
			gbc_titleTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_titleTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_titleTextfield.gridx = 1;
			gbc_titleTextfield.gridy = 0;
			contentPanel.add(titleTextfield, gbc_titleTextfield);
			titleTextfield.setColumns(10);
		}
		{
			JLabel noteLabel = new JLabel("Note:");
			GridBagConstraints gbc_noteLabel = new GridBagConstraints();
			gbc_noteLabel.anchor = GridBagConstraints.WEST;
			gbc_noteLabel.insets = new Insets(0, 0, 5, 5);
			gbc_noteLabel.gridx = 0;
			gbc_noteLabel.gridy = 1;
			contentPanel.add(noteLabel, gbc_noteLabel);
		}
		{
			noteTextfield = new JTextField(e.getNote());
			GridBagConstraints gbc_noteTextfield = new GridBagConstraints();
			gbc_noteTextfield.gridwidth = 2;
			gbc_noteTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_noteTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_noteTextfield.gridx = 1;
			gbc_noteTextfield.gridy = 1;
			contentPanel.add(noteTextfield, gbc_noteTextfield);
			noteTextfield.setColumns(10);
		}
		{
			JLabel placeLabel = new JLabel("Place:");
			GridBagConstraints gbc_placeLabel = new GridBagConstraints();
			gbc_placeLabel.anchor = GridBagConstraints.WEST;
			gbc_placeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_placeLabel.gridx = 0;
			gbc_placeLabel.gridy = 2;
			contentPanel.add(placeLabel, gbc_placeLabel);
		}
		{
			placeTextfield = new JTextField(e.getPlace());
			GridBagConstraints gbc_placeTextfield = new GridBagConstraints();
			gbc_placeTextfield.gridwidth = 2;
			gbc_placeTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_placeTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_placeTextfield.gridx = 1;
			gbc_placeTextfield.gridy = 2;
			contentPanel.add(placeTextfield, gbc_placeTextfield);
			placeTextfield.setColumns(10);
		}
		{
			JLabel startTimeLabel = new JLabel("Start Time:");
			GridBagConstraints gbc_startTimeLabel = new GridBagConstraints();
			gbc_startTimeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_startTimeLabel.gridx = 0;
			gbc_startTimeLabel.gridy = 4;
			contentPanel.add(startTimeLabel, gbc_startTimeLabel);
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
			startDateChooser.setDate(java.sql.Timestamp.valueOf(e.getStart()));
			GridBagConstraints gbc_startDateChooser = new GridBagConstraints();
			gbc_startDateChooser.anchor = GridBagConstraints.NORTH;
			gbc_startDateChooser.insets = new Insets(0, 0, 5, 5);
			gbc_startDateChooser.fill = GridBagConstraints.HORIZONTAL;
			gbc_startDateChooser.gridx = 2;
			gbc_startDateChooser.gridy = 5;
			contentPanel.add(startDateChooser, gbc_startDateChooser);
		}
		{
			JLabel startHourLabel = new JLabel("Hour:");
			GridBagConstraints gbc_startHourLabel = new GridBagConstraints();
			gbc_startHourLabel.anchor = GridBagConstraints.WEST;
			gbc_startHourLabel.insets = new Insets(0, 0, 5, 5);
			gbc_startHourLabel.gridx = 1;
			gbc_startHourLabel.gridy = 6;
			contentPanel.add(startHourLabel, gbc_startHourLabel);
		}
		{
			startHourTextfield = new JTextField(Integer.toString(e.getStart().getHour()));
			GridBagConstraints gbc_startHourTextfield = new GridBagConstraints();
			gbc_startHourTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_startHourTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_startHourTextfield.gridx = 2;
			gbc_startHourTextfield.gridy = 6;
			contentPanel.add(startHourTextfield, gbc_startHourTextfield);
			startHourTextfield.setColumns(10);
		}
		{
			JLabel startMinuteLabel = new JLabel("Minute:");
			GridBagConstraints gbc_startMinuteLabel = new GridBagConstraints();
			gbc_startMinuteLabel.anchor = GridBagConstraints.WEST;
			gbc_startMinuteLabel.insets = new Insets(0, 0, 5, 5);
			gbc_startMinuteLabel.gridx = 1;
			gbc_startMinuteLabel.gridy = 7;
			contentPanel.add(startMinuteLabel, gbc_startMinuteLabel);
		}
		{
			startMinuteTextfield = new JTextField(Integer.toString(e.getStart().getMinute()));
			GridBagConstraints gbc_startMinuteTextfield = new GridBagConstraints();
			gbc_startMinuteTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_startMinuteTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_startMinuteTextfield.gridx = 2;
			gbc_startMinuteTextfield.gridy = 7;
			contentPanel.add(startMinuteTextfield, gbc_startMinuteTextfield);
			startMinuteTextfield.setColumns(10);
		}
		{
			JLabel endTimeLabel = new JLabel("End Time:");
			GridBagConstraints gbc_endTimeLabel = new GridBagConstraints();
			gbc_endTimeLabel.anchor = GridBagConstraints.WEST;
			gbc_endTimeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_endTimeLabel.gridx = 0;
			gbc_endTimeLabel.gridy = 8;
			contentPanel.add(endTimeLabel, gbc_endTimeLabel);
		}
		{
			JLabel endDateLabel = new JLabel("Date:");
			GridBagConstraints gbc_endDateLabel = new GridBagConstraints();
			gbc_endDateLabel.anchor = GridBagConstraints.NORTHWEST;
			gbc_endDateLabel.insets = new Insets(0, 0, 5, 5);
			gbc_endDateLabel.gridx = 1;
			gbc_endDateLabel.gridy = 9;
			contentPanel.add(endDateLabel, gbc_endDateLabel);
		}
		{
			endDateChooser = new JDateChooser();
			endDateChooser.setDate(java.sql.Timestamp.valueOf(e.getEnd()));
			GridBagConstraints gbc_endDateChooser = new GridBagConstraints();
			gbc_endDateChooser.anchor = GridBagConstraints.NORTH;
			gbc_endDateChooser.insets = new Insets(0, 0, 5, 5);
			gbc_endDateChooser.fill = GridBagConstraints.HORIZONTAL;
			gbc_endDateChooser.gridx = 2;
			gbc_endDateChooser.gridy = 9;
			contentPanel.add(endDateChooser, gbc_endDateChooser);
		}
		{
			JLabel endHourLabel = new JLabel("Hour:");
			GridBagConstraints gbc_endHourLabel = new GridBagConstraints();
			gbc_endHourLabel.anchor = GridBagConstraints.WEST;
			gbc_endHourLabel.insets = new Insets(0, 0, 5, 5);
			gbc_endHourLabel.gridx = 1;
			gbc_endHourLabel.gridy = 10;
			contentPanel.add(endHourLabel, gbc_endHourLabel);
		}
		{
			endHourTextfield = new JTextField(Integer.toString(e.getEnd().getHour()));
			GridBagConstraints gbc_endHourTextfield = new GridBagConstraints();
			gbc_endHourTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_endHourTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_endHourTextfield.gridx = 2;
			gbc_endHourTextfield.gridy = 10;
			contentPanel.add(endHourTextfield, gbc_endHourTextfield);
			endHourTextfield.setColumns(10);
		}
		{
			JLabel endMinuteLabel = new JLabel("Minute:");
			GridBagConstraints gbc_endMinuteLabel = new GridBagConstraints();
			gbc_endMinuteLabel.anchor = GridBagConstraints.WEST;
			gbc_endMinuteLabel.insets = new Insets(0, 0, 5, 5);
			gbc_endMinuteLabel.gridx = 1;
			gbc_endMinuteLabel.gridy = 11;
			contentPanel.add(endMinuteLabel, gbc_endMinuteLabel);
		}
		{
			endMinuteTextfield = new JTextField(Integer.toString(e.getEnd().getMinute()));
			GridBagConstraints gbc_endMinuteTextfield = new GridBagConstraints();
			gbc_endMinuteTextfield.insets = new Insets(0, 0, 5, 5);
			gbc_endMinuteTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_endMinuteTextfield.gridx = 2;
			gbc_endMinuteTextfield.gridy = 11;
			contentPanel.add(endMinuteTextfield, gbc_endMinuteTextfield);
			endMinuteTextfield.setColumns(10);
		}	
			Duration d1; 	
			Duration day = Duration.ofMinutes(24*60);
			Duration hour = Duration.ofMinutes(60);
			int option; 
			int number;
			alarmCheckbox = new JCheckBox("Alarm");
			
			if(e.getNotification().getBefore() != null) {
				alarmCheckbox.setSelected(true);
				setAlarm = 1;
				d1 = Duration.between(e.getNotification().getBefore(),e.getStart());
				System.out.println(d1.toString());
				if(d1.compareTo(day) < 0){
					if(d1.compareTo(hour) < 0) {
						option = 0;
						number = (int) ChronoUnit.MINUTES.between(e.getNotification().getBefore(), e.getStart());
					} else {
						option = 1;
						number = (int) ChronoUnit.HOURS.between(e.getNotification().getBefore(), e.getStart());
					}
				}else {
					option = 2;
					number = (int) ChronoUnit.DAYS.between(e.getNotification().getBefore(), e.getStart());
				}			
			}else {
				alarmCheckbox.setSelected(false);
				setAlarm = 0;
				option = 0; 
				number = 0;
			}
			GridBagConstraints gbc_alarmCheckbox = new GridBagConstraints();
			gbc_alarmCheckbox.insets = new Insets(0, 0, 0, 5);
			gbc_alarmCheckbox.gridx = 0;
			gbc_alarmCheckbox.gridy = 12;
			alarmCheckbox.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent ev) {
					setAlarm = ev.getStateChange();					
				}
			});
			contentPanel.add(alarmCheckbox, gbc_alarmCheckbox);
			
		{
			alarmTextfield = new JTextField(Integer.toString(number));
			GridBagConstraints gbc_alarmTextfield = new GridBagConstraints();
			gbc_alarmTextfield.insets = new Insets(0, 0, 0, 5);
			gbc_alarmTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_alarmTextfield.gridx = 1;
			gbc_alarmTextfield.gridy = 12;
			contentPanel.add(alarmTextfield, gbc_alarmTextfield);
			alarmTextfield.setColumns(10);
		}
		{
			alarmCombo = new JComboBox(alarmOptions);
			alarmCombo.setSelectedIndex(option);
			GridBagConstraints gbc_alarmCombo = new GridBagConstraints();
			gbc_alarmCombo.insets = new Insets(0, 0, 0, 5);
			gbc_alarmCombo.fill = GridBagConstraints.HORIZONTAL;
			gbc_alarmCombo.gridx = 2;
			gbc_alarmCombo.gridy = 12;
			contentPanel.add(alarmCombo, gbc_alarmCombo);
		}
		{
			JLabel lblBefore = new JLabel("before");
			GridBagConstraints gbc_lblBefore = new GridBagConstraints();
			gbc_lblBefore.gridx = 3;
			gbc_lblBefore.gridy = 12;
			contentPanel.add(lblBefore, gbc_lblBefore);
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

