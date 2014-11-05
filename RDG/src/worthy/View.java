package worthy;
import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedHashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public abstract class View {
	
	/* Sets the contextName, to specify which Context you are working with */
	protected String contextName;
	/* Sets the origin, where this context should be drawn at; specifies the center */
	protected Point origin;
	/* Sets the last point down right */
	protected Point downright;
	/* Sets the dimension, to specify the width and the height of the context */
	protected Dimension size;
	
	/* is needed for some values and actualizing positions */
	protected Map map;
	
	/* Collection for all Shapes, that should be drawn */
	protected LinkedHashMap<String, Element> images;
	
	
	
	public View(String contextName, int originX, int originY)
			throws SlickException {
		this(contextName, new Point(originX, originY));
	}
	
	public View(String contextName, Point origin) throws SlickException {
		this(contextName, origin, new Dimension(640, 480));
	}
	
	public View(String contextName, int originX, int originY, int width, int height) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width, height));
	}
	
	public View(String contextName, Point origin, Dimension size) throws SlickException {
		this.contextName = contextName;
		this.origin = origin;
		this.size = size;
		
		images = new LinkedHashMap<String, Element>();
		map = new Map().getInstance();
		downright = new Point();
	}
	
	
	/*
	 * Hier werden alle Shapes transformiert (in der berechneten Position)
	 * gezeichnet
	 */
	public abstract void draw(GameContainer container, Graphics graphics);
	
	/* Alle Werte, etc. werden hier upgedated */
	public abstract void update();
	
	
	
	public void setPosition(String imageName, int x, int y) {
		this.images.get(imageName).getImage().draw(x * 32, y *32);
	}
	
	public void attach(String imageName, Element shape) {
		if(!this.images.containsValue(shape)) {
			this.images.put(imageName, shape);
		}
	}
	
	public void detach(String imageName) {
		if(this.images.containsKey(imageName)) {
			this.images.remove(imageName);
		}
	}
	
	public String getContextName() {
		return this.contextName;
	}
	
	public void setOrigin(int originX, int originY) {
		this.origin.x = originX;
		this.origin.y = originY;
	}
	
	public void setOrigin(Point origin) {
		this.origin = origin;
	}
	
	public Point getOrigin() {
		return this.origin;
	}
	
	public void setSize(int width, int height) {
		this.size.width = width;
		this.size.height = height;
	}
	
	public void setSize(Dimension size) {
		this.size = size;
	}
	
	public Dimension getSize() {
		return this.size;
	}

}
