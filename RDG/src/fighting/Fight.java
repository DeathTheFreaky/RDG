package fighting;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tests.GraphicsTest;

import elements.Attack;
import elements.Creature;
import elements.Potion;
import gameEssentials.Player;
import general.AttackFactory;
import general.Chances;
import general.Enums.AttackOptions;
import general.Enums.AttackScreens;
import general.Enums.Attacks;
import general.Enums.Attributes;
import general.Enums.Modes;
import general.Enums.Targets;
import views.ArmorView;
import views.GameEnvironment;
import views.View;
import views.chat.Message;

public class Fight extends View {

	/* If a fight currently takes place */
	private boolean activeFight = false;

	/* different Colors */
	Color black = new Color(0.0f, 0.0f, 0.0f);
	Color gray = new Color(0.2f, 0.2f, 0.2f);
	Color lightGray = new Color(0.65f, 0.65f, 0.65f);
	Color white = new Color(1.0f, 1.0f, 1.0f);
	Color pink = new Color(1f, 0.5f, 0.8f);
	Color red = new Color(1f, 0.0f, 0.0f);

	/* positioning values */
	private final int border = 5;
	private final int optionsWidth = size.width - 2 * border;
	private final int optionsHeight = 100;
	private final int fightWindowWidth = size.width - 2 * border;
	private final int fightWindowHeight = size.height - border
			- optionsHeight;

	// Used for health Bars
	private final int barWidth = fightWindowWidth / 3;
	private final int barHeight = 13;
	private final int barGap = 30;
	private float healthEnemy = 1.0f;
	private float healthSelf = 1.0f;
	private float healthEnemyOr;
	private float healthSelfOr;
	
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
	
	/* Variables to check which options were selected */
	private boolean changeTabActive = false;
	private boolean potionTakingActive = false;
	
	/* ArmorView is needed to interact with armory items */
	ArmorView armorView = null;
	
	/* Determine type of attack screen */
	private AttackScreens attackScreen = AttackScreens.MAIN;

	public Fight(Point origin, Dimension size, GameEnvironment ge, Player player, ArmorView armorView)
			throws SlickException {
		super("Fight", origin, size);

		this.gameEnvironment = ge;
		
		this.player = player;
	
		attacks = new AttackFactory().getInstance().getAllAttacks();
		
		this.armorView = armorView;
		
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
		else {
			activeFight = true;
			System.out.println("fight is now active");
		}

		this.enemy = creature;
		this.healthEnemyOr = creature.getOrHp();
		this.healthSelfOr = player.getOrHp();
		this.healthEnemy = creature.getHp();
		this.healthSelf = player.getHp();

		return true;
	}

	private void reset() {
		this.activeFight = false;
		this.enemy = null;
		this.attackScreen = AttackScreens.MAIN;
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {

		//BACKGROUND
		graphics.setColor(gray);
		graphics.fillRect(origin.x, origin.y, size.width, size.height);
		
		// Fight screen
		graphics.setColor(new Color(1f, 1f, 1f));
		graphics.fillRect(origin.x + border, origin.y + border,
				fightWindowWidth, fightWindowHeight);
		
		// Fight Option Selection
		graphics.setColor(pink);
		graphics.fillRect(origin.x + border, size.height - 100 + border,
				size.width - 2 * border, size.height - 100 - 2 * border);
		
		// Enemy Health Bar
		// Black border around Health bar
		graphics.setColor(black);
		graphics.drawRect(origin.x + border + barGap - 1, origin.y
				+ border + barGap - 1, barWidth + 1,
				barHeight + 1);
		// Gray background bar
		graphics.setColor(lightGray);
		graphics.fillRect(origin.x + border + barGap, origin.y + border
				+ barGap, barWidth, barHeight);
		// Actual Bar
		graphics.setColor(red);
		graphics.fillRect(origin.x + border + barGap, origin.y + border
				+ barGap, barWidth * healthEnemy / healthEnemyOr, barHeight);
		
		// Own Health Bar
		// Black border around health bar
		graphics.setColor(black);
		graphics.drawRect(fightWindowWidth - border - barGap - barWidth - 1,
				fightWindowHeight - barGap - barHeight - 1,
				barWidth + 1, barHeight + 1);
		// gray background bar
		graphics.setColor(lightGray);
		graphics.fillRect(fightWindowWidth - border - barGap - barWidth,
				fightWindowHeight - barGap - barHeight,
				barWidth, barHeight);
		// Actual bar
		graphics.setColor(red);
		graphics.fillRect(fightWindowWidth - border - barGap - barWidth,
				fightWindowHeight - barGap - barHeight,
				barWidth * healthSelf / healthSelfOr, barHeight);
		
		/* Print Names of Enemy and Player */
		/* Enemy name */
		graphics.setColor(black);
		graphics.drawString(this.enemy.NAME, origin.x + border + barGap + 1 , origin.y
				+ border + barGap - 25);
		graphics.drawString(this.player.NAME, fightWindowWidth - border - barGap - barWidth , 
				fightWindowHeight - barGap + 10);
		
		// Fight Options
		if (attackScreen == AttackScreens.MAIN) {
			graphics.setColor(black);
			graphics.drawString("Attack", optionsWidth / 4 - 25, fightWindowHeight + optionsHeight / 4);
			graphics.drawString("Parry", optionsWidth / 4 * 3 - 20, fightWindowHeight + optionsHeight / 4);
			graphics.drawString("Change Set", optionsWidth / 4 - 45, fightWindowHeight + optionsHeight / 4 * 3);
			graphics.drawString("Use Potion", optionsWidth / 4 * 3 - 42, fightWindowHeight + optionsHeight / 4 * 3);
		}
			
		// Attack Options
		if (attackScreen == AttackScreens.ATTACK) {
			graphics.setColor(black);
			graphics.drawString("Chest", optionsWidth / 4 - 20, fightWindowHeight + optionsHeight / 4);
			graphics.drawString("Head", optionsWidth / 4 * 3 - 15, fightWindowHeight + optionsHeight / 4);
			graphics.drawString("Arms", optionsWidth / 4 - 15, fightWindowHeight + optionsHeight / 4 * 3);
			graphics.drawString("Legs", optionsWidth / 4 * 3 - 15, fightWindowHeight + optionsHeight / 4 * 3);
		}
		
		/* Draw lines between the different attack options */
		graphics.setColor(gray);
		graphics.fillRect(origin.x + border, size.height - optionsHeight/2 - 2 + border,
				size.width - 2 * border, 4); // horizontal
		graphics.fillRect(origin.x + size.width/2 - border - 2, size.height - optionsHeight + border,
				4, size.height - optionsHeight); // vertical
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	/**Indicates if a fight is currently active.
	 * @return if fight is active
	 */
	public boolean isInFight() {
		return this.activeFight;
	}
	
	/**
	 * 
	 */
	public void resetStatusVariables() {
		this.changeTabActive = false;
		this.potionTakingActive = false;
	}
	
	/**Sets the current attackScreen in a fight (main menu, sub menu).
	 * @param attackScreen
	 */
	public void setAttackScreen(AttackScreens attackScreen) {
		this.attackScreen = attackScreen;
	}
	
	/**
	 * @return the current attackScreen in a fight (main menu, sub menu)
	 */
	public AttackScreens getAttackScreens() {
		return this.attackScreen;
	}
	
	/**Checks which option was selected in Fight menu.
	 * @param x
	 * @param y
	 */
	public void handleFightOptions(int x, int y) {
		
		AttackOptions attackOption = null;
		
		this.potionTakingActive = false;
		
		if (x > origin.x + border && x < origin.x + border + optionsWidth / 2 - 2
				&& y > size.height - optionsHeight + border && y < size.height - optionsHeight/2 - 2 + border) {
			attackOption = AttackOptions.OPTION1;
		}
		else if (x > origin.x + border && x < origin.x + border + optionsWidth / 2 - 2
				&& y > size.height - optionsHeight/2 + 2 && y < size.height) {
			attackOption = AttackOptions.OPTION3;
		}
		else if (x > origin.x + size.width/2 - border + 2 && x < origin.x + size.width - border 
				&& y > size.height - optionsHeight + border && y < size.height - optionsHeight/2 - 2 + border) {
			attackOption = AttackOptions.OPTION2;
		}
		else if (x > origin.x + size.width/2 - border + 2 && x < origin.x + size.width - border 
				&& y > size.height - optionsHeight/2 + 2 && y < size.height) {
			attackOption = AttackOptions.OPTION4;
		}
		
		if (attackOption != null) {
			
			if (attackScreen == AttackScreens.MAIN) {
				
				switch (attackOption) {
					case OPTION1: System.out.println("Attack");
						attackScreen = AttackScreens.ATTACK;
						break;
					case OPTION2: System.out.println("Parry");
						break;
					case OPTION3: System.out.println("Change Set");
						armorView.switchSet();
						break;
					case OPTION4: System.out.println("Potion");
							this.potionTakingActive = true;
						break;
				}
				
			} else if (attackScreen == AttackScreens.ATTACK) {
				
				switch (attackOption) {
					case OPTION1: System.out.println("Chest");
						break;
					case OPTION2: System.out.println("Head");
						break;
					case OPTION3: System.out.println("Arms");
						break;
					case OPTION4: System.out.println("Legs");
						break;
				}
			}
		}
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
		reset();
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
	 * Including weapon-set change and potions!
	 * because this method is used by ALL creatures, <br>
	 * NPCs have to use a randomly selected attack or parry (no weapon change or potions)
	 * 
	 * @return enum of player's choice
	 */
	public Attacks getCommand() {
		/* checks which option was selected, returns enum */
		
		
		return null;
	}
	
	/**Sets the active status of ChangeTab to allow or disallow changing Weapon Sets.
	 * @param activeStatus
	 */
	public void setChangeTabActive(Boolean activeStatus) {
		this.changeTabActive = activeStatus;
	}

	/**Only allow changing set when option was chosen from fight menu.
	 * @return if set changing is active
	 */
	public boolean isChangeTabActive() {
		return this.changeTabActive;
	}

	/**Sets the active status of PotionTaking to allow or disallow taking a potion.
	 * @param activeStatus
	 */
	public void setPotionTakingActive(boolean activeStatus) {
		this.potionTakingActive = activeStatus;
	}
	
	public boolean isPotionTakingActive() {
		return this.potionTakingActive;
	}
}
