package views;

import gameEssentials.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Minimap extends View {
	
	public final int WIDTH = 160;
	public final int HEIGHT = 130;
	public int positionX = 0;
	public int positionY = 0;

	private int roomWidth = 30;
	private int roomHeight = 24;
	private int border = 3;

	private boolean map[][];
	
	private boolean isMoved = false;

	private Color GREY = new Color(0.5f, 0.5f, 0.5f, 0.4f);
	private Color WHITE = new Color(1f, 1f, 1f, 0.6f);
	private Color BLACK = new Color(0f, 0f, 0f, 0.6f);
	private Color RED = new Color(1f, 0f, 0f, 0.6f);
	private Color GREEN = new Color(0f, 1f, 0f, 0.5f);

	public Minimap(String contextName, int originX, int originY)
			throws SlickException {
		super(contextName, originX, originY);
		
		positionX = originX;
		positionY = originY;
		
		map = new boolean[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				map[i][j] = false;
			}
		}
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		// TODO Auto-generated method stub
		
		graphics.setColor(GREY);
		graphics.fillRect(positionX, positionY, WIDTH, HEIGHT);

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if(i == 2 && j == 2) {
					graphics.setColor(RED);
				}else if (map[i][j]) {
					graphics.setColor(WHITE);
				} else {
					graphics.setColor(BLACK);
				}
				graphics.fillRect(positionX + border + i * (roomWidth+1),
						positionY + border + j * (roomHeight+1), roomWidth,
						roomHeight);
			}
		}
		
		if(isMoved) {
			drawPreview(graphics);
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
	
	public void setMoved(boolean b) {
		this.isMoved = b;
	}
	
	
	
	private void drawPreview(Graphics g) {
		int posX, posY;
		
		if(positionX <= Game.GAME_ENVIRONMENT_WIDTH/2-WIDTH/2) {
			//links
			posX = 20;
		}else {
			//rechts
			posX = Game.GAME_ENVIRONMENT_WIDTH-WIDTH-20;
		}
		
		if(positionY <= Game.GAME_ENVIRONMENT_HEIGHT/2-HEIGHT/2) {
			//oben
			posY = 20;
		}else {
			//unten
			posY = Game.GAME_ENVIRONMENT_HEIGHT-HEIGHT-20;
		}
		
		g.setColor(GREEN);
		g.fillRect(posX-border, posY-border, border, HEIGHT+border*2);
		g.fillRect(posX+WIDTH, posY-border, border, HEIGHT+border*2);
		g.fillRect(posX-border, posY-border, WIDTH+border*2, border);
		g.fillRect(posX-border, posY+HEIGHT, WIDTH+border*2, border);
	}

}
