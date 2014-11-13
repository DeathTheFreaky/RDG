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
	Color textColor = new Color(0f, 0f, 0f);
	Color backgroundColor = new Color(1f, 1f, 1f);
	Color borderColor = new Color(0.2f, 0.2f, 0.2f);

	/* Chat Window Values */
	private int positionX;
	private int positionY;
	private final int strokeSize = 5;
	private final int inputFieldHeight = 20;
	private final int inputFieldWidth = size.width - 2 * strokeSize;

	/* Scrolling Bar Values */
	private final int scrollBarX = size.width - 20;
	private final int scrollBarWidth = 12;
	private final float scrollBarHeight = (size.height - 3f * strokeSize - inputFieldHeight) / 7f;

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

		positionX = origin.x * GameEnvironment.BLOCK_SIZE;
		positionY = origin.y * GameEnvironment.BLOCK_SIZE;

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

		input = new InputField(container, ttf, strokeSize, positionY
				+ size.height - inputFieldHeight - strokeSize, inputFieldWidth,
				inputFieldHeight) {
			@Override
			public void keyPressed(int key, char c) {
				super.keyPressed(key, c);
				//container.getInput().consumeEvent();
				if (key == 28) {
					Chat.this.newMessage(new Message(this.getText(), Calendar
							.getInstance(), Channels.PUBLIC));
					this.setText("");
					this.deactivate();
				}
			}
		};
		input.setBackgroundColor(new Color(1f, 1f, 1f));
		input.setTextColor(new Color(0f, 0f, 0f));
		input.setMaxLength(MAXIMUM_LENGTH);
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		graphics.setColor(borderColor);
		graphics.fillRect(origin.x * GameEnvironment.BLOCK_SIZE, origin.y
				* GameEnvironment.BLOCK_SIZE, size.width, size.height);

		graphics.setColor(backgroundColor);
		graphics.fillRect(positionX + strokeSize, positionY + strokeSize,
				size.width - 2 * strokeSize, size.height - 3 * strokeSize
						- inputFieldHeight);
		graphics.fillRect(positionX + strokeSize, positionY + size.height
				- strokeSize - inputFieldHeight, inputFieldWidth,
				inputFieldHeight);

		graphics.setColor(new Color(0.2f, 0.5f, 0.9f));
		input.render(container, graphics);

		graphics.setColor(new Color(0f, 0f, 0f));
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

		if (messages.size() < 5) {
			return;
		}

		for (int i = 0; i < 4; i++) {
			if (shown[i] == true) {
				if (i == 0 && scroll < 0) { //
					shown[i] = false;
					shown[i + 4] = true;
					break;
				} else if (i == 3 && scroll > 0) {
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
	
	public void focus() {
		input.setFocus(true);
	}

}
