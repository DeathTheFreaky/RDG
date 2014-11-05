package worthy;
import java.awt.Point;

import org.newdawn.slick.Image;

/**
 * This class represents a Shape. Because the engine doesn't remember any
 * positions and due to the fact, that we want to have uniqe Identifiers (NAME)
 * and the feature to set Shapes visible or not, we need a Wrapper Class, that
 * remembers different states for us.
 */

public class Element {

	/* Here is the Image referenced */
	private Image image;
	/* This variable saves the position of the Shape */
	private Point position;
	/* This variable declares if the Object is visible or not */
	private boolean visible = true;
	/* saves the Name of the element */
	public String NAME = null;
	
	

	public Element(String shapeName, Image image) {
		this(shapeName, image, 0, 0);
	}

	public Element(String shapeName, Image image, int x, int y) {
		this(shapeName, image, new Point(x, y));
	}

	public Element(String shapeName, Image image, Point position) {
		this(shapeName, image, position, true);
	}

	public Element(String shapeName, Image image, Point position,
			boolean visible) {
		this.NAME = shapeName +  "Random UUID";
		this.image = image;
		this.position = position;
		this.visible = visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setPosition(int positionX, int positionY) {
		this.position = new Point(positionX, positionY);
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Point getPosition() {
		return this.position;
	}

	public Image getImage() {
		return this.image;
	}

	public void delete() {
		// Maybe we have to delete Elements
	}

}
