package fighting;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Creature;
import gameEssentials.Player;
import views.GameEnvironment;
import views.View;

public class Fight extends View {

	/* If a fight currently takes place */
	private boolean activeFight = false;

	/* different Colors */
	Color borderColor = new Color(0.2f, 0.2f, 0.2f);
	Color optionsColor = new Color(1f, 0.5f, 0.8f);
	Color healthColor = new Color(1f, 0.0f, 0.0f);
	Color black = new Color(0.0f, 0.0f, 0.0f);

	/* positioning values */
	private final int border = 5;
	private final int optionsWidth = size.width - 2 * border;
	private final int optionsHeight = 100;
	private final int fightWindowWidth = size.width - 2 * border;
	private final int fightWindowHeight = size.height - 2 * border
			- optionsHeight;

	// Used for health Bars
	private final int barWidth = fightWindowWidth / 3;
	private final int barHeight = 25;
	private final int barGap = 30;
	private final int barBorder = 3;
	private float healthEnemy = 1.0f;
	private float healthSelf = 1.0f;

	int ende = 1;

	/* Reference to the Game, where this Fight belongs to */
	private GameEnvironment gameEnvironment;

	/* Creature that the player is fighting against */
	private Creature enemy;
	
	/* The Player Himself */
	private Player player;

	public Fight(Point origin, Dimension size, GameEnvironment ge)
			throws SlickException {
		super("Fight", origin, size);

		this.gameEnvironment = ge;
		
		this.healthEnemy = 100 * (enemy.getHp() / enemy.getOrHp());
		this.healthSelf = 100 * (player.getHp() / player.getOrHp());
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
		graphics.fillRect(origin.x + border, origin.y + border,
				fightWindowWidth, fightWindowHeight);

		graphics.setColor(optionsColor);
		graphics.fillRect(origin.x + border, size.height - 100 + border,
				size.width - 2 * border, size.height - 100 - 2 * border);

		
		// Enemy Health Bar
		// Black border around Health bar
		graphics.setColor(black);
		graphics.drawRect(origin.x + border + barGap - barBorder, origin.y
				+ border + barGap - barBorder, barWidth + 2 * barBorder,
				barHeight + 2 * barHeight);
		// Actual Bar
		graphics.setColor(healthColor);
		graphics.fillRect(origin.x + border + barGap, origin.y + border
				+ barGap, barWidth * healthEnemy, barHeight);

		// Own Health Bar
		// Black border around health bar
		graphics.setColor(black);
		graphics.drawRect(fightWindowWidth - border - barGap - barWidth - barBorder,
				fightWindowHeight - border - barGap - barHeight - barBorder,
				barWidth + 2 * barWidth, barHeight + 2 * barBorder);
		// Actual bar
		graphics.setColor(healthColor);
		graphics.fillRect(fightWindowWidth - border - barGap - barWidth,
				fightWindowHeight - border - barGap - barHeight,
				barWidth * healthSelf, barHeight);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	public boolean isInFight() {
		return this.activeFight;
	}

	public void fight(Player player, Creature creature) {
		boolean faster = false;
		if (player.getSpeed() > creature.getSpeed()) {
			faster = true;
		} else if (player.getSpeed() < creature.getSpeed()) {
			faster = false;
		} else {	// random decision?
			
		}
		
		while (player.getHp() > 0 && creature.getHp() > 0) { // as long as nobody died
			if (faster) {
				
			}
		}
		
		if (player.getHp() == 0) {	// player died
			
		} else {					// enemy died, Player gets his attributes resetted
			player.setHp(player.getOrHp());
			player.setAccuracy(player.getOrAccuracy());
			player.setSpeed(player.getOrSpeed());
			player.setStrength(player.getOrStrength());
		}
	}
	
	/**
	 * This method is used whenever someone is attacked
	 * 
	 * @param creature
	 */
	public void attack(Creature creature) {
		if (calcHitSuccess() == true) {
			float heal = calcHeal();		// could be used to display Heal on Screen
			float damage = calcDamage();	// could be used to display Damage on Screen
			updateHealth(creature, heal, damage);
		}
	}
	
	/**
	 * Calculates if Attack is successful
	 * 
	 * @return true if Attack is successful
	 */
	public boolean calcHitSuccess() {

		return true;
	}

	/**
	 * Calculates dealt damage
	 * 
	 * @return amount of dealt damage
	 */
	public float calcDamage() {
		float damage = 0.0f;
		
		return damage;
	}
	
	/**
	 * Calculates healing
	 * 
	 * @return amount of healed HP
	 */
	public float calcHeal() {
		float heal = 0.0f;
		
		return heal;
	}
	
	/**
	 * This method updates Health of attacked Player or Enemy
	 * 
	 * @param player
	 * @param heal
	 * @param damage
	 */
	public void updateHealth(Creature player, float heal, float damage) {
		float hp = player.getHp() - damage + heal;
		player.setHp(hp);
	}
}
