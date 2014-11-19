package fighting;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Creature;
import views.GameEnvironment;
import views.View;

public class Fight extends View {

	/* If a fight currently takes place */
	private boolean activeFight = false;

	/* different Colors */
	Color borderColor = new Color(0.2f, 0.2f, 0.2f);
	Color optionsColor = new Color(1f, 0.5f, 0.8f);

	/* positioning values */
	private final int border = 5;
	private final int optionsWidth = size.width - 2 * border;
	private final int optionsHeight = 100;
	private final int fightWindowWidth = size.width - 2 * border;
	private final int fightWindowHeight = size.height - 2 * border
			- optionsHeight;

	int ende = 1;

	/* Reference to the Game, where this Fight belongs to */
	private GameEnvironment gameEnvironment;

	/* Creature that the player is fighting against */
	private Creature enemy;

	public Fight(Point origin, Dimension size, GameEnvironment ge)
			throws SlickException {
		super("Fight", origin, size);

		this.gameEnvironment = ge;
	}

	/**
	 * Starts a new Fight
	 * 
	 * @param creature
	 *            - handsover the enemy
	 * @return true or false, if a fight already has started
	 */
	public boolean newFight(Creature creature) {
		/* if a fight already is started, return */
		if (activeFight) {
			return false;
		}

		this.enemy = creature;

		return true;
	}

	private void reset() {
		this.activeFight = false;
		this.enemy = null;
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {

		graphics.setColor(borderColor);
		graphics.fillRect(origin.x, origin.y, size.width, size.height);

		graphics.setColor(new Color(1f, 1f, 1f));
		graphics.fillRect(origin.x+border, origin.y + border , fightWindowWidth,
				fightWindowHeight);

		graphics.setColor(optionsColor);
		graphics.fillRect(origin.x + border, size.height - 100 + border,
				size.width - 2 * border, size.height - 100 - 2 * border);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
	
	public boolean isInFight() {
		return this.activeFight;
	}
}
