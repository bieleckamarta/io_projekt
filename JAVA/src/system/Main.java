package system;

import java.awt.EventQueue;

import dane.AppParameters;
import gui.Application;
import system.Manager;

/**
 * main class of the application, invoking, directly or not, all methods and classes of this program
 * @author Marta Bielecka
 *
 */
public class Main {

	/**
	 * initializes Singleton class, passing table of program arguments to it
	 * @param args
	 */
	public static void main(String[] args) {
		
		AppParameters.initialize(args);
		view();
	}
	
	/**
	 * invokes Application class, catches thrown exceptions
	 */
	static void view()
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}


