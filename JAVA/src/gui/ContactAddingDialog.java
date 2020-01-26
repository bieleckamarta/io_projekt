package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

import dane.Contact;

/**
 * this class is responsible for contact adding dialog looks and function
 * @author Marta Bielecka
 *
 */
public class ContactAddingDialog extends JDialog {

	/**
	 * main panel of the dialog, containing all other elements
	 */
	private final JPanel contentPanel = new JPanel();
	/**
	 * textfield where user can type new contact's name
	 */
	private JTextField nameTextfield;
	/**
	 * textfield where user can type new contact's company name
	 */
	private JTextField companyTextfield;
	/**
	 * textfield where user can type new contact's phone number
	 */
	private JTextField phoneNumberTextfield;
	/**
	 * textfield where user can type new contact's e-mail address
	 */
	private JTextField emailTextfield;
	/**
	 * JButton confirming new contact's creation, clicking it constructs new contact with given data and closes the dialog
	 */
	private JButton okButton;
	/**
	 * JButton canceling inserted changes, clicking it closes the dialog without creating new contact
	 */
	private JButton cancelButton;
	/**
	 * variable showing, if okButton was clicked by user
	 */
	private boolean okClicked = false;

	/**
	 * shows dialog, sets it visible and, if okButton was clicked by user, creates new contact with data typed in by the user.
	 * Returns newly created Contact object
	 */
	public static Contact showDialog() {
		try {
			ContactAddingDialog dialog = new ContactAddingDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			if(dialog.okClicked) {
				String name  = dialog.nameTextfield.getText();
				String company = dialog.companyTextfield.getText();
				String phonenumber = dialog.phoneNumberTextfield.getText();
				String email = dialog.emailTextfield.getText();
				
				Contact newContact = new Contact(name, company, email, phonenumber);
				dialog.dispose();
				return newContact;			
			}else { 
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates the dialog
	 * sets all components and defines the consequences of clicking okButton or cancelButton
	 */
	public ContactAddingDialog() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 255);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{23, 0, 28, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		JLabel nameLabel = new JLabel("Name:");
		GridBagConstraints gbc_nameLabel = new GridBagConstraints();
		gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nameLabel.anchor = GridBagConstraints.WEST;
		gbc_nameLabel.gridx = 1;
		gbc_nameLabel.gridy = 1;
		contentPanel.add(nameLabel, gbc_nameLabel);
		{
			nameTextfield = new JTextField();
			GridBagConstraints gbc_nameTextfield = new GridBagConstraints();
			gbc_nameTextfield.insets = new Insets(0, 0, 5, 0);
			gbc_nameTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_nameTextfield.gridx = 3;
			gbc_nameTextfield.gridy = 1;
			contentPanel.add(nameTextfield, gbc_nameTextfield);
			nameTextfield.setColumns(10);
		}
		{
			JLabel companyLabel = new JLabel("Company: ");
			GridBagConstraints gbc_companyLabel = new GridBagConstraints();
			gbc_companyLabel.insets = new Insets(0, 0, 5, 5);
			gbc_companyLabel.anchor = GridBagConstraints.WEST;
			gbc_companyLabel.gridx = 1;
			gbc_companyLabel.gridy = 2;
			contentPanel.add(companyLabel, gbc_companyLabel);
		}
		{
			companyTextfield = new JTextField();
			GridBagConstraints gbc_CompanyTextfield = new GridBagConstraints();
			gbc_CompanyTextfield.insets = new Insets(0, 0, 5, 0);
			gbc_CompanyTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_CompanyTextfield.gridx = 3;
			gbc_CompanyTextfield.gridy = 2;
			contentPanel.add(companyTextfield, gbc_CompanyTextfield);
			companyTextfield.setColumns(10);
		}
		{
			JLabel phoneNumberLabel = new JLabel("Phone number:");
			GridBagConstraints gbc_phoneNumberLabel = new GridBagConstraints();
			gbc_phoneNumberLabel.insets = new Insets(0, 0, 5, 5);
			gbc_phoneNumberLabel.gridx = 1;
			gbc_phoneNumberLabel.gridy = 3;
			contentPanel.add(phoneNumberLabel, gbc_phoneNumberLabel);
		}
		{
			phoneNumberTextfield = new JTextField();
			GridBagConstraints gbc_phoneNumberTextfield = new GridBagConstraints();
			gbc_phoneNumberTextfield.insets = new Insets(0, 0, 5, 0);
			gbc_phoneNumberTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_phoneNumberTextfield.gridx = 3;
			gbc_phoneNumberTextfield.gridy = 3;
			contentPanel.add(phoneNumberTextfield, gbc_phoneNumberTextfield);
			phoneNumberTextfield.setColumns(10);
		}
		{
			JLabel emailLabel = new JLabel("E-mail:");
			GridBagConstraints gbc_emailLabel = new GridBagConstraints();
			gbc_emailLabel.insets = new Insets(0, 0, 0, 5);
			gbc_emailLabel.anchor = GridBagConstraints.WEST;
			gbc_emailLabel.gridx = 1;
			gbc_emailLabel.gridy = 4;
			contentPanel.add(emailLabel, gbc_emailLabel);
		}
		{
			emailTextfield = new JTextField();
			GridBagConstraints gbc_EmailTextfield = new GridBagConstraints();
			gbc_EmailTextfield.fill = GridBagConstraints.HORIZONTAL;
			gbc_EmailTextfield.gridx = 3;
			gbc_EmailTextfield.gridy = 4;
			contentPanel.add(emailTextfield, gbc_EmailTextfield);
			emailTextfield.setColumns(10);
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
