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

import worthy.Message.Channels;

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

	public Chat(String contextName, int originX, int originY,
			GameContainer container) throws SlickException {
		this(contextName, new Point(originX, originY), container);
	}

	public Chat(String contextName, Point origin, GameContainer container)
			throws SlickException {
		this(contextName, origin.x, origin.y, 640, 400, container);
	}

	public Chat(String contextName, int originX, int originY, int width,
			int height, GameContainer container) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height), container);
	}

	public Chat(String contextName, Point origin, Dimension size,
			GameContainer container) throws SlickException {
		super(contextName, origin, size);

		messages = new LinkedList<Message>();
		messages.add(new Message("New Game Started! Player vs. Opponent",
				Calendar.getInstance()));
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

		for (int i = 0; i < MAXIMUM_MESSAGES; i++) {
			if (i < 4) {
				shown[i] = true;
			} else {
				shown[i] = false;
			}
		}

		Font font = new Font("Verdana", Font.BOLD, 12);
		TrueTypeFont ttf = new TrueTypeFont(font, true);

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

		input.render(container, graphics);
	}

	@Override
	public void update() {

	}

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

	public void scroll(int scroll) { // +120 rauf, -120 runter scrollen
		scroll /= 120;
		System.out.println("Scroll" + scroll);
		
		if(messages.size() < 5) {
			return;
		}

		for(int i = 0; i < 4; i++) {
			if(shown[i] == true) {
				if(i == 0 && scroll < 0) {	// 
					shown[i] = false;
					shown[i+4] = true;
					break;
				}else if(i == 3 && scroll > 0) {
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
