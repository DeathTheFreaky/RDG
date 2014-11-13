 package worthy;
import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedHashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


/**Stores and draws all Elements in a particular context's View.
 *
 */
public abstract class View {
	
	/* Sets the contextName, to specify which Context you are working with */
	protected String contextName;
	/* Sets the origin, where this context should be drawn at in tiles numbers (left upper corner) */
	protected Point origin;
	/* Sets the last point down right in tile numbers (not pixels!) */
	protected Point downright;
	/* Sets the dimension, to specify the width and the height of the context in pixels */
	protected Dimension size;
	
	/* is needed for some values and actualizing positions */
	protected Map map;
	
	/* Collection for all Shapes, that should be drawn */
	protected LinkedHashMap<String, Element> images;
	
	/**Construct a View passing origin's single x and y locations in tile numbers.
	 * Dimension will be set automatically.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @throws SlickException
	 * @see View
	 */
	public View(String contextName, int originX, int originY)
			throws SlickException {
		this(contextName, new Point(originX, originY));
	}
	
	/**Construct a View passing origin as a point in tile numbers.
	 * Dimension will be set automatically.
	 * 
	 * @param contextName
	 * @param origin
	 * @throws SlickException
	 * @see View
	 */
	public View(String contextName, Point origin) throws SlickException {
		this(contextName, origin, new Dimension(640, 480));
	}
	
	/**Construct a View passing origin's single x and y locations in tile numbers
	 * and the View's size by its single x and y values in pixels.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @throws SlickException
	 * @see View
	 */
	public View(String contextName, int originX, int originY, int width, int height) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width, height));
	}
	
	/**Construct a View passing origin as a point in tile numbers and the View's size as a Dimension in pixels.
	 * 
	 * @param contextName
	 * @param origin
	 * @param size
	 * @throws SlickException
	 * @see View
	 */
	public View(String contextName, Point origin, Dimension size) throws SlickException {
		this.contextName = contextName;
		this.origin = origin;
		this.size = size;
		
		images = new LinkedHashMap<String, Element>();
		map = new Map().getInstance();
		downright = new Point();
	}
	
	/**Hier werden alle Shapes transformiert (in der berechneten Position)
	 * gezeichnet.
	 * 
	 * @param container
	 * @param graphics
	 */
	public abstract void draw(GameContainer container, Graphics graphics);
	
	/**Alle Werte, etc. werden hier upgedated.
	 * 
	 */
	public abstract void update();
	
	/**Draw's image at specified location in pixels in View.
	 * 
	 * @param imageName
	 * @param x
	 * @param y
	 */
	public void setPosition(String imageName, int x, int y) {
		this.images.get(imageName).getImage().draw(x * 32, y *32);
	}
	
	/**Attaches image to View's LinkedHashMap of images.
	 * 
	 * @param imageName
	 * @param shape
	 */
	public void attach(String imageName, Element shape) {
		if(!this.images.containsValue(shape)) {
			this.images.put(imageName, shape);
		}
	}
	
	/**Removes image from View's LinkedHashMap of images.
	 * 
	 * @param imageName
	 */
	public void detach(String imageName) {
		if(this.images.containsKey(imageName)) {
			this.images.remove(imageName);
		}
	}
	
	/**contextName specifies which Context you are working with.
	 * 
	 * @return contextName of the View
	 */
	public String getContextName() {
		return this.contextName;
	}
	
	/**Sets upper left corner of the View in tile numbers.
	 * 
	 * @param originX
	 * @param originY
	 */
	public void setOrigin(int originX, int originY) {
		this.origin.x = originX;
		this.origin.y = originY;
	}
	
	/**Sets upper left corner in tile numbers.
	 * 
	 * @param origin
	 */
	public void setOrigin(Point origin) {
		this.origin = origin;
	}
		
	/**
	 * @return Upper left corner of the view in tile numbers.
	 */
	public Point getOrigin() {
		return this.origin;
	}
	
	/**Sets the Dimension's size in pixels.
	 * 
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height) {
		this.size.width = width;
		this.size.height = height;
	}
	
	/**Sets the Dimension's size in pixels.
	 * 
	 * @param size
	 */
	public void setSize(Dimension size) {
		this.size = size;
	}
	
	/**
	 * @return Dimension of View in pixels
	 */
	public Dimension getSize() {
		return this.size;
	}

}
