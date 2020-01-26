package dane;

/**
 * Contact instances hold personal data, like name, company, e-mail and phone number of a person
 * @author Mateusz Keller
 *
 */

public class Contact {

	/**
	 * name of a person
	 */
	private String name;
	/**
	 * company, where a person works
	 */
	private String company;
	/**
	 * e-mail address of a person
	 */
	private String email;
	/**
	 * phone number of a person
	 */
	private String phone;
	
	/**
	 * constructs Contact with given name and empty Strings other fields
	 * @param imieNazwisko name of a person
	 */
	public Contact(String imieNazwisko) {
		this.name = imieNazwisko; 
		this.company = "";
		this.email = "";
		this.phone = "";
		}

	/**
	 * constructs Contact with given name, company, e-mail and phone number
	 * @param name name of a person
	 * @param company company where person works
	 * @param email e-mail address of a person
	 * @param phone phone number of a person
	 */
	public Contact(String name, String company, String email, String phone) {
		this.name = name;
		this.company = company;
		this.email = email;
		this.phone = phone;
	}

	/**
	 * returns name of a person
	 * @return name field value
	 */
	public String getName() { return name; }
	/**
	 * sets value of name field
	 * @param imieNazwisko new name to be set in Contact
	 */
	public void setName(String imieNazwisko) { this.name = imieNazwisko; }
	/**
	 * returns name of a company, where a person works
	 * @return company field value
	 */
	public String getCompany() { return company; }
	/**
	 * sets value of company field
	 * @param firma new company to be set in Contact
	 */
	public void setCompany(String firma) { this.company = firma; }
	/**
	 * returns e-mail address of a person
	 * @return value of e-mail field
	 */
	public String getEmail() { return email; }
	/**
	 * sets new e-mail address
	 * @param email new e-mail address to be set
	 */
	public void setEmail(String email) { this.email = email; }
	/**
	 * returns phone number of a person
	 * @return value of phone field
	 */
	public String getPhone() { return phone; }
	/**
	 * sets value of phone field
	 * @param telefon new phone number to be set
	 */
	public void setPhone(String telefon) { this.phone = telefon; }
	
	/**
	 * returns String containing personal data 
	 */
	public String toString()
	{	
		String ret =  name + "\n";
		
		if(company != null)	ret += company + " ";
		if(email != null)	ret+= email + " ";
		if(phone != null)	ret+= phone;
		
		return ret + "\n"; 
	}
}
