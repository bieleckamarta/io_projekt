package system.events;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * class provides handling of application window resize 
 * @author Marta Bielecka
 *
 */
public class ResizeListener implements ComponentListener {

	/**
	 * defines actions to take after application window resize. 
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		Dimension newSize = e.getComponent().getBounds().getSize();
	}

	/**
	 * defines actions to take after component is moved. 
	 */
	@Override
	public void componentMoved(ComponentEvent e) {}

	/**
	 * defines actions to take after component is shown. 
	 */
	@Override
	public void componentShown(ComponentEvent e) {}

	/**
	 * defines actions to take after component is hidden. 
	 */
	@Override
	public void componentHidden(ComponentEvent e) {}

}
