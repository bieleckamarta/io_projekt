package dane;

/**
 * This class is a Singleton implementation that delivers program arguments to inner data classes, e.g. Alarm class 
 * @author Marta Bielecka
 *
 */

public class AppParameters {
	/**
	 * this field holds Singleton itself, it is initialized with null
	 */
	private static AppParameters parameters = null;
	/**
	 * holds data passed from program arguments as string value (in program Calendar holds name of audio file in particular)
	 */
	private String sound;
	
	/**
	 * access method allowing inner classes of application get program parameters
	 * @return value of sound field
	 */
	public String getSound() {
		return sound;
	}
	 /**
	  * initializes object with first parameter passed to the program, and adding this value to sound field
	  * @param arg program parameters passed to Singleton
	  */
	public static void initialize(String [] arg) {
		parameters = new AppParameters(arg[0]);
	}
	/**
	 * private constructor of the class , adding first parameter from the list to the sound field of AppParameter instance
	 * @param arg list of program parameters
	 */
	private AppParameters(String arg) {
		sound = arg;
	}
	/**
	 * static method that returns the only instance of AppParameter in any class, tha calls this method
	 * @return the only instance of AppParameters
	 */
	public static AppParameters getInstance() {
		return parameters;
	}
	
	
	
}
