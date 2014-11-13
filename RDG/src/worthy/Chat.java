package worthy;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Calendar;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import enums.Enums.Channels;

/**Chat is used to display game status messages and exchange text messages between players.<br>
 * Chat extends a View in the Game context.
 * 
 * @see View
 */
public class Chat extends View {

	/* Maximum for saved Messages and Message Length */
	private final int MAXIMUM_MESSAGES = 7;
	private final int MAXIMUM_LENGTH = 40;

	/* Input Field for typing messages */
	private InputField input;

	/* List for all Messages, that are currently saved */
	private LinkedList<Message> messages;

	/* tracks the 4 Messages, that are currently displayed */
	private boolean shown[] = new boolean[MAXIMUM_MESSAGES];

	/* For displaying row by row */
	private int zeile = 0;

	/**Constructs a Chat passing its origin's position as single x and y coordinates in tile numbers.<br>
	 * The Chat's View dimensions will be set automatically to default values in pixels.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param container
	 * @throws SlickException
	 * @see Chat
	 */
	public Chat(String contextName, int originX, int originY,
			GameContainer container) throws SlickException {
		this(contextName, new Point(originX, originY), container);
	}

	/**Constructs a Chat passing its origin's position as a point in tile numbers.<br>
	 * The Chat's View dimensions will be set automatically to default values in pixels.
	 * 
	 * @param contextName
	 * @param origin
	 * @param container
	 * @throws SlickException
	 * @see Chat
	 */
	public Chat(String contextName, Point origin, GameContainer container)
			throws SlickException {
		this(contextName, origin.x, origin.y, 640, 400, container);
	}

	/**Constructs a Chat passing its origin's position as single x and y coordinates in tile numbers 
	 * and the dimension of its superclass View as single x and y coordinates in pixels.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @param container
	 * @throws SlickException
	 * @see Chat
	 */
	public Chat(String contextName, int originX, int originY, int width,
			int height, GameContainer container) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height), container);
	}

	/**Constructs a Chat passing its origin's position as a Point in tile numbers 
	 * and the dimension of its superclass View as a Dimension in pixels.
	 * 
	 * @param contextName
	 * @param origin
	 * @param size
	 * @param container
	 * @throws SlickException
	 * @see Chat
	 */
	public Chat(String contextName, Point origin, Dimension size,
			GameContainer container) throws SlickException {
		super(contextName, origin, size);
		
		messages = new LinkedList<Message>();
		
		/* print a welcoming message and use an instance of Calendar class to get current time */
		messages.add(new Message("New Game Started! Player vs. Opponent",
				Calendar.getInstance()));
		/* print end of this game session */
		/* if game session overlaps a full hour, react accordingly ->
		 * use up minutes until full hour is reached,
		 * increase hour,
		 * increase remaining minutes starting form 0
		 */
		if (Calendar.getInstance().get(Calendar.MINUTE) >= 45) {
			messages.add(new Message(
					"Instance ends at "
							+ ((Calendar.getInstance()
									.get(Calendar.HOUR_OF_DAY) + 1) > 23 ? "00"
									: (Calendar.getInstance().get(
											Calendar.HOUR_OF_DAY) + 1))
							+ ":"
							+ ((Calendar.getInstance().get(Calendar.MINUTE) - 45) > 9 ? (Calendar
									.getInstance().get(Calendar.MINUTE) - 45)
									: "0"
											+ (Calendar.getInstance().get(
													Calendar.MINUTE) - 45)),
					Calendar.getInstance()));
		} else {
			messages.add(new Message("Instance ends at "
					+ Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":"
					+ (Calendar.getInstance().get(Calendar.MINUTE) + 15),
					Calendar.getInstance()));
		}
		
		/* set first 4 messages to be shown */
		for (int i = 0; i < MAXIMUM_MESSAGES; i++) {
			if (i < 4) {
				shown[i] = true;
			} else {
				shown[i] = false;
			}
		}

		/* set font type */
		Font font = new Font("Verdana", Font.BOLD, 12);
		TrueTypeFont ttf = new TrueTypeFont(font, true);

		/* create an inputfield and clear it when message is sent */
		input = new InputField(container, ttf, 15, origin.y
				* GameEnvironment.BLOCK_SIZE + size.height - 25, 450, 20) {
			@Override
			public void keyPressed(int key, char c) {
				super.keyPressed(key, c);
				if (key == 28) {
					System.out.println("Enter pressed!");
					Chat.this.newMessage(new Message(this.getText(), Calendar
							.getInstance(), Channels.PUBLIC));
					this.setText("");
				}
			}
		};
		input.setBackgroundColor(new Color(255, 0, 0));
		input.setMaxLength(MAXIMUM_LENGTH); 
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		int i = 0;
		/* draw all messages to a Graphics object */
		for (Message m : messages) {
			if (shown[i] == true) {
				graphics.drawString(m.print(), origin.x
						* GameEnvironment.BLOCK_SIZE + 10, origin.y
						* GameEnvironment.BLOCK_SIZE + zeile * 14 + 5);
				zeile++;
			}
			i++;
		}
		zeile = 0;
		
		/* render the Graphics object on the screen */
		input.render(container, graphics);
	}

	@Override
	public void update() {

	}

	/**Adds a new message to the list of message, 
	 * delete oldest message if more than 7 messages are present.<br>
	 * 
	 * Only the first 4 messages will be shown and rendered on the screen.
	 * 
	 * @param message 
	 */
	private void newMessage(Message message) {
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

	/**Scrolls through messages in Chat if more than 4 messages are stored.<br>
	 * 
	 * @param scroll
	 */
	public void scroll(int scroll) { // +120 rauf, -120 runter scrollen
		scroll /= 120;
		System.out.println("Scroll" + scroll);
		
		/* only scroll chat if there are more message not currently displayed */
		if(messages.size() < 5) {
			return;
		}

		for(int i = 0; i < 4; i++) {
			if(shown[i] == true) {
				if(i == 0 && scroll < 0) {	//undisplay first message 
					shown[i] = false;
					shown[i+4] = true;
					break;
				}else if(i == 3 && scroll > 0) { //undisplay last message
					shown[6] = false;
					shown[i-1] = true;
				}else {
					if(scroll > 0 && i != 0) {	// raufscrollen
						shown[i+3] = false;
						shown[i-1] = true;
					}else if(scroll < 0 && i != 3) {	// runterscrollen
						
						shown[i] = false;
						shown[i+4] = true;
					}
				}
			}
		}
	}

}
