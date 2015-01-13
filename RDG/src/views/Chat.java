package views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextField;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import views.font.TrueTypeFont;
import at.RDG.network.NetworkManager;
import at.RDG.network.communication.NetworkMessage;
import views.chat.InputField;
import views.chat.Message;
import gameEssentials.Game;
import general.ResourceManager;
import general.Enums.Channels;
import org.newdawn.slick.gui.TextField;

/**
 * Chat is used to display game status messages and exchange text messages
 * between players.<br>
 * Chat extends a View in the Game context.
 * 
 * @see View
 */
public class Chat extends View {

	/* Maximum for saved Messages and Message Length */
	private final int MAXIMUM_MESSAGES = 7;
	private final int MAXIMUM_LENGTH = 39;

	/* Input Field for typing messages */
	private InputField input;

	/* List for all Messages, that are currently saved */
	private LinkedList<Message> messages;

	/* tracks the 4 Messages, that are currently displayed */
	private boolean shown[] = new boolean[MAXIMUM_MESSAGES];

	/* For displaying row by row */
	private int zeile = 0;

	/* Different Colors */
	private Color BLACK = new Color(0f, 0f, 0f);
	private Color WHITE = new Color(1f, 1f, 1f);
	private Color DARK_GREY = new Color(0.2f, 0.2f, 0.2f);
	private Color TURQUIS = new Color(0.2f, 0.5f, 0.9f);
	private Color BLUE = new Color(0f, 0f, 1f);

	/* Chat Window Values */
	private int positionX;
	private int positionY;
	private final int strokeSize = 5;
	private final int timeSpace = 68;
	private final int inputFieldHeight = 20;
	private final int inputFieldWidth = size.width - 3 * strokeSize - timeSpace;

	/* Scrolling Bar Values */
	private final int scrollBarX = size.width - 20;
	private final int scrollBarWidth = 12;
	private final float scrollBarHeight = (size.height - 3f * strokeSize - inputFieldHeight) / 7f;

	/* Time */
	private int hour = -1;
	private int minute = -1;
	
	/* network manager for transferring messages to other pc */
	NetworkManager networkManager;
	
	/* resource manager for fonts */
	ResourceManager resources;

	/**
	 * Constructs a Chat passing its origin's position as single x and y
	 * coordinates in tile numbers.<br>
	 * The Chat's View dimensions will be set automatically to default values in
	 * pixels.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param container
	 * @throws SlickException
	 * @see CopyOfChat
	 */
	public Chat(String contextName, int originX, int originY,
			GameContainer container) throws SlickException {
		this(contextName, new Point(originX, originY), container);
	}

	/**
	 * Constructs a Chat passing its origin's position as a point in tile
	 * numbers.<br>
	 * The Chat's View dimensions will be set automatically to default values in
	 * pixels.
	 * 
	 * @param contextName
	 * @param origin
	 * @param container
	 * @throws SlickException
	 * @see CopyOfChat
	 */
	public Chat(String contextName, Point origin, GameContainer container)
			throws SlickException {
		this(contextName, origin.x, origin.y, 640, 400, container);
	}

	/**
	 * Constructs a Chat passing its origin's position as single x and y
	 * coordinates in tile numbers and the dimension of its superclass View as
	 * single x and y coordinates in pixels.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @param container
	 * @throws SlickException
	 * @see CopyOfChat
	 */
	public Chat(String contextName, int originX, int originY, int width,
			int height, GameContainer container) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height), container);
	}

	/**
	 * Constructs a Chat passing its origin's position as a Point in tile
	 * numbers and the dimension of its superclass View as a Dimension in
	 * pixels.
	 * 
	 * @param contextName
	 * @param origin
	 * @param size
	 * @param container
	 * @throws SlickException
	 * @see CopyOfChat
	 */
	public Chat(String contextName, Point origin, Dimension size,
			GameContainer container) throws SlickException {
		super(contextName, origin, size);
		
		try {
			this.networkManager = NetworkManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}

		resources = new ResourceManager().getInstance();
		
		positionX = origin.x * GameEnvironment.BLOCK_SIZE;
		positionY = origin.y * GameEnvironment.BLOCK_SIZE;
		
		Calendar cal = Calendar.getInstance();

		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);

		messages = new LinkedList<Message>();

		/* set first 4 messages to be shown */
		for (int i = 0; i < MAXIMUM_MESSAGES; i++) {
			if (i < 4) {
				shown[i] = true;
			} else {
				shown[i] = false;
			}
		}

		/* create an inputfield and clear it when message is sent */
		input = new InputField(container, resources.DEFAULT_FONTS.get("input"), strokeSize * 2 + timeSpace,
				positionY + size.height - inputFieldHeight - strokeSize,
				inputFieldWidth, inputFieldHeight) {
			@Override
			public void keyPressed(int key, char c) {
				super.keyPressed(key, c);
				if (this.getText().length() >= this.getMaxLength()
						&& this.isFocused())
					container.getInput().consumeEvent();
				if (key == 28 && this.isFocused() && this.getText() != "") {
					Chat.this.newMessage(new Message(Game.getInstance().getPlayer().NAME + ": " + this.getText(), Calendar
							.getInstance(), Channels.PUBLIC));
					this.setText("");
					this.deactivate();
				}
			}
		};
		input.setBackgroundColor(new Color(1f, 1f, 1f));
		input.setBorderColor(new Color(1f, 1f, 1f));
		input.setTextColor(new Color(0f, 0f, 0f));
		input.setMaxLength(MAXIMUM_LENGTH);
	}
	
	 /*
	 * Synchronzied to avoid concurrent modification exception caused by 
	 * modifiying list form two threads at same time.
	 */
	@Override
	public synchronized void draw(GameContainer container, Graphics graphics) {
		graphics.setColor(DARK_GREY);
		graphics.fillRect(origin.x * GameEnvironment.BLOCK_SIZE, origin.y
				* GameEnvironment.BLOCK_SIZE, size.width, size.height);

		graphics.setColor(WHITE);
		graphics.fillRect(positionX + strokeSize, positionY + strokeSize,
				size.width - 2 * strokeSize, size.height - 3 * strokeSize
						- inputFieldHeight);
		graphics.fillRect(positionX + strokeSize, positionY + size.height
				- strokeSize - inputFieldHeight, inputFieldWidth,
				inputFieldHeight);

		graphics.setColor(TURQUIS);
		input.render(container, graphics);

		graphics.setColor(BLACK);
		graphics.setFont(resources.DEFAULT_FONTS.get("chat"));
		// graphics.setColor(new Color(0f, 0f, 0f));
		int i = 0;
		for (Message m : messages) {
			if (shown[i] == true) {
				
				graphics.drawString(m.print(), origin.x
						* GameEnvironment.BLOCK_SIZE + 10, origin.y
						* GameEnvironment.BLOCK_SIZE + zeile * 14 + 5);
				zeile++;
				graphics.fillRect(scrollBarX, origin.y
						* GameEnvironment.BLOCK_SIZE + strokeSize + i
						* scrollBarHeight, scrollBarWidth, scrollBarHeight);
			}
			i++;
		}
		zeile = 0;

		graphics.setColor(BLUE);
		graphics.fillRect(strokeSize, positionY + size.height
				- inputFieldHeight - strokeSize, timeSpace, inputFieldHeight);
		
		((TrueTypeFont) resources.DEFAULT_FONTS.get("description")).drawString( strokeSize + timeSpace/2, positionY + size.height - inputFieldHeight - strokeSize + 2,
				"<" + (hour>9?Integer.toString(hour):"0"+hour) + ":"
						+ (minute > 9 ? minute : ("0" + minute)) + ">", WHITE, TrueTypeFont.ALIGN_CENTER);
		
		/*graphics.drawString("<" + (hour>9?Integer.toString(hour):"0"+hour) + ":"
				+ (minute > 9 ? minute : ("0" + minute)) + ">", strokeSize + 2, positionY + size.height - inputFieldHeight - strokeSize);*/
		
		/*graphics.setColor(TURQUIS);
		graphics.fillRect(strokeSize * 2 + timeSpace, positionY + size.height
				- inputFieldHeight - strokeSize, inputFieldWidth, inputFieldHeight);*/
	}

	@Override
	public void update() {
		Calendar cal = Calendar.getInstance();
		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);
	}

	/**
	 * Adds a new message to the list of message, delete oldest message if more
	 * than 7 messages are present.<br>
	 * 
	 * Only the first 4 messages will be shown and rendered on the screen.<br>
	 * Synchronzied to avoid concurrent modification exception caused by 
	 * modifiying list form two threads at same time.
	 * 
	 * @param message
	 */
	public synchronized void newMessage(Message message) {
		
		message.setTime(hour, minute); //set time to chat time - useful when adding message from other computer to avoid time sync
		
		String string = message.print();
		List<String> words = new LinkedList<String>();
		Channels channel = message.getChannel();
								
		/* split string if too long */
		if (resources.DEFAULT_FONTS.get("chat").getWidth(string) > 340) {
			
			String tempString = "";
			String[] stringSplit = new String[2];
			
			stringSplit = string.split("-", 2);
			stringSplit[1] = stringSplit[1].substring(1);
			
			for (String word: stringSplit[1].split(" ")){
		         words.add(word);
		    }
						
			String testLength = null;
			int followUpCtr = 0;
			
			while (words.size() > 0) {
				
				testLength = "";
				tempString = "";
				
				do {
					tempString = tempString.concat(words.get(0)).concat(" ");
					words.remove(0);
					if (words.size() > 0) {
						testLength = tempString.concat(words.get(0));
					}
				} while (resources.DEFAULT_FONTS.get("chat").getWidth(testLength) < 265 && words.size() > 0);
				
				if (followUpCtr == 0) {
					processMessage(new Message(tempString, hour, minute, channel));
				} else {
					processMessage(new Message(tempString, hour, minute, channel, true));
				}
				
				followUpCtr++;
			}
			
		} else {
			processMessage(message);
		}
	}

	/**
	 * Scrolls through messages in Chat if more than 4 messages are stored.<br>
	 * 
	 * @param scroll
	 */
	public void scroll(int scroll) { // +120 rauf, -120 runter scrollen
		scroll /= 120;

		/* only scroll chat if there are more message not currently displayed */
		if (messages.size() < 5) {
			return;
		}

		for (int i = 0; i < 4; i++) {
			if (shown[i] == true) {
				if (i == 0 && scroll < 0) { // undisplay first message
					shown[i] = false;
					shown[i + 4] = true;
					break;
				} else if (i == 3 && scroll > 0) { // undisplay last message
					shown[6] = false;
					shown[i - 1] = true;
				} else {
					if (scroll > 0 && i != 0) { // raufscrollen
						shown[i + 3] = false;
						shown[i - 1] = true;
					} else if (scroll < 0 && i != 3) { // runterscrollen

						shown[i] = false;
						shown[i + 4] = true;
					}
				}
			}
		}
	}

	/**Set focus on input field.
	 * @param b
	 */
	public void setFocus(boolean b) {
		input.setFocus(b);
	}

	/**Check if input field has focus.
	 * @return
	 */
	public boolean hasFocus() {
		return input.hasFocus();
	}
	
	/**If a message is too long, split into seperate lines.
	 * @param print
	 */
	private void processMessage(Message message) {
		
		if (message.getChannel() == Channels.PUBLIC) {
			networkManager.sendMessage(new NetworkMessage(message.getMessage()));
		}
		
		messages.add(message);
		if (messages.size() > 7) {
			messages.removeFirst();
		}

		if (messages.size() > 4) {
			for (int i = 0; i < messages.size(); i++) {
				if (i < messages.size() - 4) {
					shown[i] = false;
				} else {
					shown[i] = true;
				}
			}
		}
	}
}