package fighting;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Attack;
import elements.Creature;
import elements.Potion;
import gameEssentials.Player;
import general.AttackFactory;
import general.Chances;
import general.Enums.Attacks;
import general.Enums.Attributes;
import general.Enums.Modes;
import general.Enums.Targets;
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
	
	// Instances of all available attacks
	Map<Attacks, Attack> attacks = null;
	
	Attack activeAttack = null;
	float parryMultiplier = 1.0f;

	int ende = 1;

	/* Reference to the Game, where this Fight belongs to */
	private GameEnvironment gameEnvironment;

	/* Creature that the player is fighting against */
	private Creature enemy;
	
	/* The Player Himself */
	private Player player;

	public Fight(Point origin, Dimension size, GameEnvironment ge, Player player, Creature enemy)
			throws SlickException {
		super("Fight", origin, size);

		this.gameEnvironment = ge;
		
		this.player = player;
		this.enemy = enemy;
	
		this.healthEnemy = 100 * (enemy.getHp() / enemy.getOrHp());
		this.healthSelf = 100 * (player.getHp() / player.getOrHp());
		
		attacks = new AttackFactory().getInstance().getAllAttacks();
		
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
			activeAttack = null;
			parryMultiplier = 1.0f;
			if (faster) {
				/* player chooses what to do */
				switch(getCommand()) {
				case TORSO:
					activeAttack = attacks.get(Attacks.TORSO);
					break;
				case HEAD:
					activeAttack = attacks.get(Attacks.HEAD);
					break;
				case ARMS:
					activeAttack = attacks.get(Attacks.ARMS);
					break;
				case LEGS:
					activeAttack = attacks.get(Attacks.LEGS);
					break;
				case SET:
					/* change weapon-set */
					break;
				case POTION:
					/* bla bla bla, select potion, bla bla bla */
					Potion selected = null;
					usePotion(player, enemy, selected);
					break;
				case PARRY:
					/*  */
					parryMultiplier = 2.0f;
					if (parrySuccess() == true) {
						activeAttack = attacks.get(Attacks.TORSO);	// muss man noch rausfinden was am besten is (ich war ja für head aber flo für torso xD)
						break;
					}
					break;
				default:
					break;
				}
				
				/* potion effects */
				potionEffects(player);
				
			} else {
				/* potion effects */
				potionEffects(enemy);
				
			}
		}
		
		
		if (player.getHp() == 0) {	// player died
			
		} else {					// enemy died, Player gets his attributes resetted
			player.resetOriginals();
		}
	}
	
	/**
	 * This method is used whenever someone is attacked
	 * 
	 * @param creature
	 */
	public void attack(Player player, Creature creature) {
		if (activeAttack == null) return;
		
		if (calcHitSuccess() == true) {
			
			
			
			float damage = calcDamage();	// could be used to display Damage on Screen
			updateHealth(creature, damage);
			updateAttributes(creature);
		}
	}
	
	/**
	 * Calculates if Attack is successful
	 * 
	 * @return true if Attack is successful
	 */
	public boolean calcHitSuccess() {
		if ((player.getAccuracy() * 1/*weapon accuracy*/ * activeAttack.hitProbability) - (enemy.getOrSpeed() * 1) >= 0.5) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * calculates if attack can be parried
	 * 
	 * @return true if a parry will be successful
	 */
	public boolean parrySuccess() {
		
		return false;
	}

	/**
	 * Calculates dealt damage
	 * 
	 * @return amount of dealt damage
	 */
	public float calcDamage() {
		float damage = 0.0f;
		
		damage = player.getStrength() * 1 /* weapon damage */ * Chances.randomFloat(activeAttack.statsLowMultiplier, activeAttack.statsHighMultiplier) - 0 /* armor values */ * parryMultiplier;
		
		return damage;
	}
	
	public void updateAttributes(Creature enemy) {
		switch(activeAttack.effect) {
		case HP:
			enemy.setHp(enemy.getHp()
					* activeAttack.attributeDamageMultiplier
					* Chances.randomFloat(activeAttack.statsLowMultiplier, activeAttack.statsHighMultiplier));
			break;
		case ACCURACY:
			enemy.setAccuracy(enemy.getAccuracy()
					* activeAttack.attributeDamageMultiplier
					* Chances.randomFloat(activeAttack.statsLowMultiplier,
							activeAttack.statsHighMultiplier));
			break;
		case STRENGTH:
			enemy.setStrength(enemy.getStrength()
					* activeAttack.attributeDamageMultiplier
					* Chances.randomFloat(activeAttack.statsLowMultiplier,
							activeAttack.statsHighMultiplier));
			break;
		case SPEED:
			enemy.setSpeed(enemy.getSpeed()
					* activeAttack.attributeDamageMultiplier
					* Chances.randomFloat(activeAttack.statsLowMultiplier,
							activeAttack.statsHighMultiplier));
			break;
		default:
			break;
		}
	}
	
	/**
	 * This method updates Health of attacked Player or Enemy
	 * 
	 * @param player
	 * @param heal
	 * @param damage
	 */
	public void updateHealth(Creature player, float damage) {
		float hp = player.getHp() - damage;
		player.setHp(hp);
	}
	
	/**
	 * called every time the player uses a potion (directly or indirectly)
	 */
	public void usePotion(Creature player, Creature creature, Potion potion) {
		// if player uses antidote / removes the first poison in the list
		if (potion.MODE == Modes.LIFT) {
			for (Potion _potion : creature.getActivePotions()) {
				if (_potion.EFFECT == Attributes.HP && _potion.MODE == Modes.DECR) {
					creature.removeActivePotions(_potion);
				}
				break;
			}
		} else {		// if player uses other potion
			if (potion.TARGET == Targets.SELF) {	// if player uses good potion for himself
				player.addActivePotions(potion);
				if (potion.MODE == Modes.TINCR) {
					increase(player, potion);
				}
			} else {		// if player uses bad potion for enemy
				creature.addActivePotions(potion);
				if (potion.MODE == Modes.TDECR) {
					decrease(creature, potion);
				}
			}
		}
	}
	
	/**
	 * calculates Potions Effects' on a specific every round
	 * Potions whith mode TINCR or TDECR are not applied in this method but in method usePotion()
	 */
	public void potionEffects(Creature creature) {		
		// apply all non temporary potion effects
		for (Potion potion : creature.getActivePotions()) {
			potion.DURATION--;
			switch(potion.MODE) {
			case INCR: 
				increase(creature, potion);
				break;
			case DECR:
				decrease(creature, potion);
				break;
			default:
				break;
			}
			if (potion.DURATION <= 0) {
				revertEffect(creature, potion);
				creature.removeActivePotions(potion);
			}
		}
		for (Potion potion : creature.getActivePotions()) {
			potion.DURATION--;
			if (potion.DURATION <= 0) {
				creature.removeActivePotions(potion);
			}
		}
	}
	
	/**
	 * reverts effect of temporary potions
	 * 
	 * @param creature
	 * @param potion
	 */
	public void revertEffect(Creature creature, Potion potion) {
		if (potion.MODE == Modes.TINCR) {
			decrease(creature, potion);
		} else if (potion.MODE == Modes.TDECR) {
			increase(creature, potion);
		}
	}
	
	/**
	 * decreases attributes
	 * 
	 * @param creature
	 * @param potion
	 */
	public void decrease(Creature creature, Potion potion) {
		switch(potion.EFFECT) {
		case HP:
			player.setHp(player.getHp() - potion.POWER);
			break;
		case SPEED:
			player.setSpeed(player.getSpeed() - potion.POWER);
			break;
		case ACCURACY:
			player.setAccuracy(player.getAccuracy() - potion.POWER);
			break;
		case STRENGTH:
			player.setStrength(player.getStrength() - potion.POWER);
			break;
		default:
			break;
		}
	}
	
	/**
	 * increases attributes
	 * 
	 * @param creature
	 * @param potion
	 */
	public void increase(Creature creature, Potion potion) {
		switch(potion.EFFECT) {
		case HP:
			player.setHp(player.getHp() + potion.POWER);
			break;
		case SPEED:
			player.setSpeed(player.getSpeed() + potion.POWER);
			break;
		case ACCURACY:
			player.setAccuracy(player.getAccuracy() + potion.POWER);
			break;
		case STRENGTH:
			player.setStrength(player.getStrength() + potion.POWER);
			break;
		default:
			break;
		}
	}
	
	/**
	 * including weapon-set change and potions!
	 * because this method is used by ALL creatures, <br>
	 * NPCs have to use a randomly selected attack or parry (no weapon change or potions)
	 * 
	 * @return enum of player's choice
	 */
	public Attacks getCommand() {
		/* checks which option was selected, returns enum */
		
		
		return null;
	}
}
