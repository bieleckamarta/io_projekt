package system;

import java.time.LocalDate;

/**
 * interface created to group classes reacting for change of displayed date
 * @author Marta Bielecka
 *
 */
public interface DisplayedDateChangeListener {
	/**
	 * defines action to take after changing date to new date
	 * @param newDate new date to be handled
	 */
	void dateChange(LocalDate newDate);
}
