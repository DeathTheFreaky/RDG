package fighting;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Attack;
import elements.Creature;
import elements.Monster;
import elements.Potion;
import gameEssentials.Player;
import general.AttackFactory;
import general.Chances;
import general.Enums.ArmorStatsAttributes;
import general.Enums.ArmorStatsMode;
import general.Enums.ArmorStatsTypes;
import general.Enums.AttackOptions;
import general.Enums.AttackScreens;
import general.Enums.Attacks;
import general.Enums.Attributes;
import general.Enums.Modes;
import general.Enums.Targets;
import views.ArmorView;
import views.GameEnvironment;
import views.View;

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
	
	/* Selected Attack option from Fight menu - used for both player and enemy 
	 * - needs to be set to null in between */
	Attacks activeAttackType = null;
	
	/* actual instance of an attack (ARMS, HEAD, CHEST, LEGS) - used for both player and enemy 
	 * - needs to be set to null in between  */
	Attack activeAttack = null;
	
	/* balancing of parrying */
	float parryMultiplier = 1.0f;

	/* end of a fight */
	int end = 1;

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
	
	/* player vs player host */
	private Boolean humanFightHost = false;
	private Boolean humanFightSlave = false;
	private Boolean humanFight = false; 
	
	/* values manipulated by networking class */
	
		/* used in human fight to determine which player gets first attack */
		int thisPlayerisFirst = 0;
		
		/* a potion drunk by a player and set in armorView.drinkPotion() */
		Potion selectedPotion = null;
		
		/* needed to determine which player starts a round in a fight */
		float enemyArmorSpeedMalusSum = 0;
		float enemyWeaponSpeedMalusMax = 0;
		boolean enemySpeedMalusSumSet = false;
		
		/* the enemy calculates if a parry was successful */
		boolean enemyParrySuccess = false;
		boolean enemyParrySuccessSet = false;
		
		/* speed and armor stats are needed to calculate the attack damages */
		float enemyArmorSum = 0;
		float enemySpeed = 0;
		float enemyAccuracy = 0;
		boolean enemyStatsSet = false;
		
		/* the enemy calculates the health and attribute damage of its attack */
		float enemyAttackHealthDamage = 0;
		float enemyAttackAttributeDamage = 0;
		float enemyAttackHitProbability = 0;
		boolean enemyAttackSet = false;

	/**Constructs a Fight Instance, which will provide an environment for all fights a player engages in.
	 * @param origin
	 * @param size
	 * @param ge
	 * @param player
	 * @param armorView
	 * @throws SlickException
	 */
	public Fight(Point origin, Dimension size, GameEnvironment ge, Player player, ArmorView armorView)
			throws SlickException {
		super("Fight", origin, size);

		this.gameEnvironment = ge;
		
		this.player = player;
	
		attacks = new AttackFactory().getInstance().getAllAttacks();
		
		this.armorView = armorView;
		
	}
	
	@Override
	public void draw(GameContainer container, Graphics graphics) {

		/* BACKGROUND */
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
		/* BACKGROUND */
		
		
		/* FOREGROUND */
		/* Enemy Health Bar */
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
		
		// Waiting Screen
		if (attackScreen == AttackScreens.WAITING) {
			graphics.setColor(black);
			graphics.drawString("Waiting for oponent's action... ", optionsWidth / 4 - 20, fightWindowHeight + optionsHeight / 2);
		}
		
		if (attackScreen != AttackScreens.WAITING) {
			/* Draw lines between the different attack options */
			graphics.setColor(gray);
			graphics.fillRect(origin.x + border, size.height - optionsHeight/2 - 4 + border,
					size.width - 2 * border, 4); // horizontal
			graphics.fillRect(origin.x + size.width/2 - border - 2, size.height - optionsHeight + border,
					4, size.height - optionsHeight); // vertical
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
	}
	
	/**Checks which option was selected in Fight menu.
	 * @param x
	 * @param y
	 */
	public void handleFightOptions(int x, int y) {
		
		AttackOptions attackOption = null;
		
		this.potionTakingActive = false;
		
		if (x > origin.x + border && x < origin.x + border + optionsWidth / 2 - 2
				&& y > size.height - optionsHeight + border && y < size.height - optionsHeight/2 - 4 + border) {
			attackOption = AttackOptions.OPTION1;
		}
		else if (x > origin.x + border && x < origin.x + border + optionsWidth / 2 - 2
				&& y > size.height - optionsHeight/2 && y < size.height) {
			attackOption = AttackOptions.OPTION3;
		}
		else if (x > origin.x + size.width/2 - border + 2 && x < origin.x + size.width - border 
				&& y > size.height - optionsHeight + border && y < size.height - optionsHeight/2 - 4 + border) {
			attackOption = AttackOptions.OPTION2;
		}
		else if (x > origin.x + size.width/2 - border + 2 && x < origin.x + size.width - border 
				&& y > size.height - optionsHeight/2 && y < size.height) {
			attackOption = AttackOptions.OPTION4;
		}
		
		if (attackOption != null) {
			
			if (attackScreen == AttackScreens.MAIN) {
				
				switch (attackOption) {
					case OPTION1: System.out.println("Attack");
						attackScreen = AttackScreens.ATTACK;
						break;
					case OPTION2: System.out.println("Parry");
						this.activeAttackType = Attacks.PARRY;
						this.attackScreen = AttackScreens.WAITING;
						break;
					case OPTION3: System.out.println("Change Set");
						this.activeAttackType = Attacks.SET;
						this.attackScreen = AttackScreens.WAITING;
						break;
					case OPTION4: System.out.println("Potion");
						//activeAttackType is set in Armor View -> drinkPotion()
						this.potionTakingActive = true;
						break;
				}
				
			} else if (attackScreen == AttackScreens.ATTACK) {
				
				switch (attackOption) {
					case OPTION1: System.out.println("Chest");
						this.activeAttackType = Attacks.TORSO;
						this.attackScreen = AttackScreens.WAITING;
						break;
					case OPTION2: System.out.println("Head");
						this.activeAttackType = Attacks.HEAD;
						this.attackScreen = AttackScreens.WAITING;
						break;
					case OPTION3: System.out.println("Arms");
						this.activeAttackType = Attacks.ARMS;
						this.attackScreen = AttackScreens.WAITING;
						break;
					case OPTION4: System.out.println("Legs");
						this.activeAttackType = Attacks.LEGS;
						this.attackScreen = AttackScreens.WAITING;
						break;
				}
			}
		}
	}
	
	/**
	 * Starts a new Fight.
	 * 
	 * @param creature - hands over the enemy
	 * @return the Creature that died or null if fight is already active
	 * @throws InterruptedException 
	 */
	public Creature newFight(Creature creature) throws InterruptedException {
		
		/* if a fight already is started, return */
		if (activeFight) {
			return null;
		}
		else {
			activeFight = true;
		}

		this.enemy = creature;
		this.healthEnemyOr = creature.getOrHp();
		this.healthSelfOr = player.getOrHp();
		this.healthEnemy = creature.getHp();
		this.healthSelf = player.getHp();
		this.humanFight = false; //reset to false before checking if it is a human fight
		
		Creature looser = fight();

		return looser;
	}

	/**Reset fight variables to default values.
	 */
	private void reset() {
		this.activeFight = false;
		this.enemy = null;
		this.attackScreen = AttackScreens.MAIN;
		resetRoundVariables();
	}
	
	/**Reset variables that need to be resetted each round in a fight.
	 */
	private void resetRoundVariables() {
		this.changeTabActive = false;
		this.potionTakingActive = false;
		this.thisPlayerisFirst = 0;
		this.activeAttackType = null;
		this.activeAttack = null;
		this.enemySpeedMalusSumSet = false;
		this.enemyArmorSpeedMalusSum = 0;
		this.selectedPotion = null;
		this.enemyParrySuccess = false;
		this.enemyParrySuccessSet = false;
		this.enemyAttackHealthDamage = 0;
		this.enemyAttackAttributeDamage = 0;
		this.enemyAttackHitProbability = 0;
		this.enemyAttackSet = false;
		this.enemyArmorSum = 0;
		this.enemySpeed = 0;
		this.enemyStatsSet = false;
		this.enemyAccuracy = 0;
	}

	/**
	 * This method manages the process of fighting.
	 * 
	 * @return the looser of a fight
	 * @throws InterruptedException 
	 */
	private Creature fight() throws InterruptedException {
		
		/* Determine if this is a human fight. */
		this.humanFight = humanFightInitialization();
		
		/* Main fight loop */
		while (player.getHp() > 0 && enemy.getHp() > 0) { // as long as nobody died
			
			Creature creature1 = null; //player1 comes first in a round
			Creature creature2 = null;
			
			/* Determine which creature gets the first Attack in a fight. */
			int firstAttackTemp =  determineFirstAttack();
			
			if (firstAttackTemp == 1) {
				creature1 = this.player;
				creature2 = this.enemy;
			}
			else if (firstAttackTemp == 2) {
				creature1 = this.enemy;
				creature2 = this.player;
			}
			
			/* perform Attack of creature first in round */
			attackControl(creature1, creature2);
			
			/* potion effects for a creature are applied after its attack */
			potionEffects(creature1);
			
			/* set to null between attacks of player and enemy to determine if attack was already chosen */
			activeAttack = null;
			activeAttackType = null;
			
			/* perform Attack of creature first in round */
			attackControl(creature2, creature1);
			
			/* potion effects for a creature are applied after its attack */
			potionEffects(creature2);
			
			/* reset variables that need to be changed each round */
			resetRoundVariables();
		}
		
		/* after a fight, reset the fighting instance's variables */
		reset();
		
		/* check which one of the fighting parties died and return the loser */
		if (player.getHp() <= 0) {
			return player;
		} else {
			player.resetOriginals();
			return enemy;
		}
	}
	
	/**Controls which Actions to perform when an attack has been chosen by a creature.
	 * @param creature
	 * @throws InterruptedException 
	 */
	private void attackControl(Creature creature1, Creature creature2) throws InterruptedException {
		
		/* player chooses what to do */
		switch(getCommand(creature1)) {
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
			
			/* only switch sets if this is the player's turn */
			if (creature1 == this.player) {
				armorView.switchSet();
			}
			break;
		case POTION:
			
			/* select potion */	
			selectedPotion = getSelectedPotion(creature1);	
			
			/* manage the handling of a drunk potion */
			usePotion(creature1, creature2, selectedPotion);
			
			/* reset selectedPotion */
			selectedPotion = null;
			
			break;
		case PARRY:
			
			/* when a player decides to parry, if successful, he deals x times the damage of a normal torso attack */
			parryMultiplier = 2.0f;
			if (parrySuccess(creature1, creature2) == true) {
				activeAttack = attacks.get(Attacks.TORSO);	// muss man noch rausfinden was am besten is (ich war ja für head aber flo für torso xD)
				break;
			}
			break;
		default:
			break;
		}
		
		/* actual attack */
		attack(creature1, creature2);
		
		/* parryMultiplier is used on every attack and only temporarily increased when a creature chooses to parry
		 * -> so it needs to be reset */
		parryMultiplier = 1.0f;	
		
	}

	/**Obtain the selectedPotion -> either from armorView or via network from another player.
	 * @param creature1
	 * @return the selected potion
	 * @throws InterruptedException 
	 */
	private Potion getSelectedPotion(Creature creature1) throws InterruptedException {
				
		if ((!humanFight) || (creature1 == this.player)) {
			this.selectedPotion = armorView.getSelectedPotion();
		}
		else {
			
			/* its the other human player's turn -> obtain the potion he selected */ 
			int timeoutctr = 0;
			boolean successfulCommunication  = false;
			
			/* wait for fight host to set needed information */
			while (timeoutctr <= 10) {
				
				/* wait for selectedPotion to be set via network */
				if (this.selectedPotion != null) {
					successfulCommunication  = true;
					break;
				}
				
				timeoutctr++;
				Thread.sleep(100);
			}
			
			if (successfulCommunication == false) {
				System.err.println("Obtaining the opponent's selected Potion timed out...");
				new Exception("Obtaining the opponent's selected Potion failed");
			}			
		}
		
		return this.selectedPotion;
	}

	/**Determine which creature comes first in a round.
	 * @return 1 if thisPlayer comes first, 2 if enemy comes first
	 * @throws InterruptedException 
	 */
	private int determineFirstAttack() throws InterruptedException {
		
		if (humanFightSlave == false) {
			
			/* calculate which player comes first */
			if (speedBasedSuccess(this.player, this.enemy) == true) {
				thisPlayerisFirst = 1;
			} else {
				thisPlayerisFirst = 2;
			}
			
			if (humanFightHost == true) {
				// SEND RESULT OF CALCULATION TO SLAVE -> message(thisPlayerisFirst, value is opposite of local value)
			}
		}
		else {
			
			int timeoutctr = 0;
			boolean successfulCommunication  = false;
			
			/* wait for fight host to set needed information */
			while (timeoutctr <= 10) {
				
				if (this.thisPlayerisFirst != 0) {
					successfulCommunication  = true;
					break;
				}
				
				if (timeoutctr == 10) {
					break;
				}
				
				timeoutctr++;
				Thread.sleep(100);
			}
			
			if (successfulCommunication == false) {
				System.err.println("Calculation of initial attack timed out...");
				new Exception("Calculation of initial attack failed");
			}
		}
		
		/* reset to default values */
		enemyArmorSpeedMalusSum = 0;
		
		return thisPlayerisFirst;	
	}

	/**Waits for and manages humanFightInitialization.
	 * @return true if it is a humenFight
	 * @throws InterruptedException 
	 */
	private boolean humanFightInitialization() throws InterruptedException {
		
		if (this.enemy instanceof Player) {
			
			int timeoutctr = 0;
			boolean successfulCommunication = false;
			
			/* wait for fight host to set needed information */
			while (timeoutctr <= 10) {
				
				if ((humanFightHost == true && humanFightSlave == false) || 
						(humanFightHost == false && humanFightSlave == true)) {
					successfulCommunication = true;
					break;
				}
				
				if (timeoutctr == 10) {
					break;
				}
				
				timeoutctr++;
				Thread.sleep(100);
			}
			
			if (successfulCommunication == false) {
				System.err.println("Distribution of Human Fight Roles timed out...");
				new Exception("Distribution of Human Fight Roles failed");
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Including weapon-set change and potions!
	 * because this method is used by ALL creatures, <br>
	 * NPCs have to use a randomly selected attack or parry (no weapon change or potions). <br>
	 * Not called for human enemies.
	 * 
	 * @return enum of player's choice
	 * @throws InterruptedException 
	 */
	private Attacks getCommand(Creature creature) throws InterruptedException {
				
		/* checks which option was selected, returns enum */
		Attacks chosenAttackType = null;
		
		if (creature instanceof Player) {
			/* wait for player to chose an attack */
			while (this.activeAttackType == null) {
				
				if (this.activeAttackType != null) {
					chosenAttackType = this.activeAttackType;
				}
				
				Thread.sleep(100);
			}
		}
		else {
			chosenAttackType = Chances.randomAttackType();
		}
		
		return chosenAttackType;
	}

	/**
	 * This method is used whenever someone is attacked.<br>
	 * In case of the attack being performed by another human player,
	 * obtain the result value from the other player.
	 * 
	 * @param attacker, defender
	 * @throws InterruptedException 
	 */
	private void attack(Creature attacker, Creature defender) throws InterruptedException {
		
		/* Attacks are designed to be calculated on each host knowing 
		 * the raw healthDamage, attributeDamage and HitProbability of the attacker. 
		 * Strength, accuracy and weapon damage are already included in these values.
		 * Beforehand, the defender must provide its armor and speed values 
		 * for to the attacker to calculate all necessary information. */
		
		/* if this is a human fight, wait for other party to send relevant data */
		if (attacker == this.player && humanFight) {
			
			/* Wait for enemie's stats for attack calculation */
			int timeoutctr = 0;
			boolean successfulCommunication = false;
			
			/* wait for fight host to set needed information */
			while (timeoutctr <= 10) {
				
				if (enemyStatsSet == true) {
					successfulCommunication = true;
					break;
				}
				
				if (timeoutctr == 10) {
					break;
				}
				
				timeoutctr++;
				Thread.sleep(100);
			}
			
			if (successfulCommunication == false) {
				System.err.println("Waiting for enemyStats timed out...");
				new Exception("Waiting for enemyStats failed");
			}
		}
		else if (attacker != this.player && humanFight) {
			
			float armorSum = armorView.getStats(ArmorStatsTypes.ARMAMENT, ArmorStatsMode.SUM, ArmorStatsAttributes.ARMOR);
			float speed = calcCreatureSpeed(this.player);
			
			//TELL THE ATTACKER OUR OWN SPEED AND AMORSUM VALUES -> message(speed, value); message(armorsum, value)
			
			/* Wait for enemie's attack calculation */
			int timeoutctr = 0;
			boolean successfulCommunication = false;
			
			/* wait for fight host to set needed information */
			while (timeoutctr <= 10) {
				
				if (enemyAttackSet == true) {
					successfulCommunication = true;
					break;
				}
				
				if (timeoutctr == 10) {
					break;
				}
				
				timeoutctr++;
				Thread.sleep(100);
			}
			
			if (successfulCommunication == false) {
				System.err.println("Waiting for enemyAttackDamage timed out...");
				new Exception("Waiting for enemyAttackDamage failed");
			}
		}
		
		/* finished retrieving necessary data */
		
		if (activeAttack == null) return;
		
		/* now calculate the actual attacks */
		if (calcHitSuccess(attacker, defender) == true) {
			
			float damage = calcDamage(attacker, defender);	// could be used to display Damage on Screen
			updateHealth(defender, damage);
			updateAttributes(defender);
		} else {
			if (attacker == this.player && humanFight) {
				
				//SEND 0 as ATTACK DAMAGE VALUES TO OTHER PARTIE -> in updateHealth, updateAttributes
			}
		}
		
		/* reset values */
		this.enemyArmorSum = 0;
		this.enemySpeed = 0;
		this.enemyAccuracy = 0;
		this.enemyStatsSet = false;
		this.enemyAttackHealthDamage = 0;
		this.enemyAttackAttributeDamage = 0;
		this.enemyAttackSet = false;
	}
	
	/**Calculate the accuracy of a creature, taking into account the multipliers from a player's weapons.
	 * @param creature
	 * @return
	 * @throws InterruptedException 
	 */
	private float calcCreatureAccuracy(Creature creature) throws InterruptedException {
		
		float accuracy = 0;
		float accuracyMultiplier = 0.5f;
		
		/* get accuracy of a monster */
		if (creature instanceof Monster) {
			return creature.getAccuracy();
		}
		
		/* get accuracy of local player */
		if (creature == this.player) {
			accuracy = creature.getAccuracy() + accuracyMultiplier * armorView.getStats(ArmorStatsTypes.WEAPONS, ArmorStatsMode.AVERAGE, ArmorStatsAttributes.ACCURACY);
		} 
		
		/* get the accuracy of the other player */
		else {
			
			int timeoutctr = 0;
			boolean successfulCommunication = false;
			
			/* wait for fight host to set needed information */
			while (timeoutctr <= 10) {
				
				if (enemyStatsSet == true) {
					accuracy = enemyAccuracy;
					successfulCommunication = true;
					break;
				}
				
				if (timeoutctr == 10) {
					break;
				}
				
				timeoutctr++;
				Thread.sleep(100);
			}
			
			if (successfulCommunication == false) {
				System.err.println("Waiting for enemyAccuracy timed out...");
				new Exception("Waiting for enemyAccuracy failed");
			}
		}
		
		/* do not reset values here - could be used later on */
		
		return accuracy;
	}
	
	/**Calculate the speed of a creature, taking into account mali from armament and weapons of a player.
	 * @param creature
	 * @return
	 * @throws InterruptedException 
	 */
	private float calcCreatureSpeed(Creature creature) throws InterruptedException {
		
		float speed = 0;
		
		/* actual calculated malus from equipped armaments to be substracted from Player's speed */
		float playerArmorSpeedMalus;
		float armorSpeedMalusMultiplier = 0.5f;
		
		/* actual calculated malus multiplier from equipped weapons to be substracted from Player's speed */
		float playerWeaponSpeedMalusMult;
		float weaponSpeedMult = 0.5f; // for balancing
		
		/* calculated from armor view */
		float playerArmorSpeedMalusSum = 0;
		float playerWeaponSpeedMalusMax = 0;
		
		/* get speed of a monster */
		if (creature instanceof Monster) {
			return creature.getSpeed();
		}
		
		/* get speed of local player */
		if (creature == this.player) {
			
			playerArmorSpeedMalusSum = armorView.getStats(ArmorStatsTypes.ARMAMENT, ArmorStatsMode.SUM, ArmorStatsAttributes.SPEED);
			playerWeaponSpeedMalusMax = armorView.getStats(ArmorStatsTypes.WEAPONS, ArmorStatsMode.MIN, ArmorStatsAttributes.SPEED);
		}
		
		/* get speed of other player */
		else {
			
			int timeoutctr = 0;
			boolean successfulCommunication = false;
			
			/* wait for fight host to set needed information */
			while (timeoutctr <= 10) {
				
				if (enemySpeedMalusSumSet == true) {
					playerArmorSpeedMalusSum = enemyArmorSpeedMalusSum;
					playerWeaponSpeedMalusMax = enemyWeaponSpeedMalusMax;
					successfulCommunication = true;
					break;
				}
				
				if (timeoutctr == 10) {
					break;
				}
				
				timeoutctr++;
				Thread.sleep(100);
			}
			
			if (successfulCommunication == false) {
				System.err.println("Waiting for enemyArmorSpeedMalusSum timed out...");
				new Exception("Waiting for enemyArmorSpeedMalusSum failed");
			}
			
			/* do not reset values here - could be used later on */
		}
		
		if (playerArmorSpeedMalusSum >= 1) {
			playerArmorSpeedMalus = creature.getSpeed() / (armorSpeedMalusMultiplier * playerArmorSpeedMalusSum);
		}
		else {
			playerArmorSpeedMalus = 0;
		}
		
		/* playerWeaponSpeedMalusMax means that the lower the speed value of a weapon, the higher the malus */
		if (playerWeaponSpeedMalusMax >= 1) {
			playerWeaponSpeedMalusMult = creature.getSpeed() * playerWeaponSpeedMalusMax/100 * weaponSpeedMult;
		}
		else {
			playerWeaponSpeedMalusMult = creature.getSpeed() * 80/100 * weaponSpeedMult; //80 -> fists: not sure if those are actually equipped
		}
		
		/* the initial speed - momentum  - does not consider armor -> armor is considered for hit probability */
		speed = (creature.getSpeed() - playerArmorSpeedMalus) * playerWeaponSpeedMalusMult;
		
		return speed;
	}
	
	/**Calculates the faster creature for initial attack and parry success.<br>
	 * Creature speed, armor speed malus value and weapon speed are taken into account.<br>
	 * @param attacker
	 * @param defender
	 * @param waitForOtherPlayer
	 * @return true if the attacker succeeds, false if the defender succeeds
	 * @throws InterruptedException
	 */
	private boolean speedBasedSuccess(Creature attacker, Creature defender) throws InterruptedException {
		
		boolean attackerSucceeds = false;
		
		/* balancing values */
		float speedRandLow = 0.0f;
		float speedRandHigh = 1.0f;
		
		/* the initial speed - momentum  - does not consider armor -> armor is considered for hit probability */
		float attackerSpeed = calcCreatureSpeed(attacker) * Chances.randomFloat(speedRandLow, speedRandHigh);
		float defenderSpeed = calcCreatureSpeed(defender) * Chances.randomFloat(speedRandLow, speedRandHigh);
		
		if (attackerSpeed > defenderSpeed) {
			attackerSucceeds = true;
		} else if (attackerSpeed < defenderSpeed) {
			attackerSucceeds = false;
		} else {
			// random decision
			if (Math.random() >= 0.5) {
				attackerSucceeds = true;
			} else {
				attackerSucceeds = false;
			}
		}
		
		return attackerSucceeds;
	}
	
	/**
	 * Calculates if an attack can be parried.
	 * The faster a defender's speed, the higher the chance an attack can be parried.
	 * 
	 * @return true if a parry will be successful
	 * @throws InterruptedException 
	 */
	private boolean parrySuccess(Creature attacker, Creature defender) throws InterruptedException {
		
		boolean parrySuccess = false;
				
		/* enemy computer shall carry out calculation and tell us the result */
		if (humanFight && attacker != this.player) {
			
			float defenderArmorSpeedMalusSum = armorView.getStats(ArmorStatsTypes.ARMAMENT, ArmorStatsMode.SUM, ArmorStatsAttributes.SPEED);
			
			//SEND OUR OWN ARMOR_SPEED_MALUS_SUM -> message(defenderArmorSpeedMalusSum, value);			
		}
		
		/* check if defender succeeded with his parry */
		if ((speedBasedSuccess(attacker, defender)) == false) {
			parrySuccess = true;
		} 
			
		return parrySuccess;
	}
	
	/**
	 * Calculates if an Attack hits the enemy, taking into account <br>
	 * a creature's accuracy, the accuracy of equipped weapons, <br>
	 * and the speed of the defender. <br>
	 * 
	 * @return true if Attack hits the enemy
	 * @throws InterruptedException 
	 */
	private boolean calcHitSuccess(Creature attacker, Creature defender) throws InterruptedException {
		
		float randAccuracyLow = 0.2f;
		float randAccuracyHigh = 0.7f;
		
		/* speed value will probably be quite a lot lower than accuracy, because for accuracy,
		 * the average of equipped weapons accuracy adds to the player's accuracy whereas for speed,
		 * the armor's speed malus is substracted from the player's speed on a proportional base. */
		float randSpeedLow = 0.5f;
		float randSpeedHigh = 1.0f;
		
		float randAttackerAccuracy = calcCreatureAccuracy(attacker) * activeAttack.hitProbability;
		float randDefenderSpeed = calcCreatureSpeed(defender);
		
		float randAccuracy = randAttackerAccuracy * Chances.randomFloat(randAccuracyLow, randAccuracyHigh);
		float randSpeed = randDefenderSpeed * Chances.randomFloat(randSpeedLow, randSpeedHigh);
		
		if (randAccuracy > randSpeed) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Calculates dealt damage
	 * 
	 * @return amount of dealt damage
	 */
	private float calcDamage(Creature attacker, Creature defender) {
		float damage = 0.0f;
		
		damage = attacker.getStrength() * 1 /* weapon damage */ * Chances.randomFloat(activeAttack.statsLowMultiplier, activeAttack.statsHighMultiplier) - 0 /* armor values: defender */ * parryMultiplier;
		
		return damage;
	}
	
	/**Update an opponent's attributes in reaction to an attack.
	 * @param creature
	 */
	private void updateAttributes(Creature creature) {
		switch(activeAttack.effect) {
		case HP:
			creature.setHp(creature.getHp()
					* activeAttack.attributeDamageMultiplier
					* Chances.randomFloat(activeAttack.statsLowMultiplier, activeAttack.statsHighMultiplier));
			break;
		case ACCURACY:
			creature.setAccuracy(creature.getAccuracy()
					* activeAttack.attributeDamageMultiplier
					* Chances.randomFloat(activeAttack.statsLowMultiplier,
							activeAttack.statsHighMultiplier));
			break;
		case STRENGTH:
			creature.setStrength(creature.getStrength()
					* activeAttack.attributeDamageMultiplier
					* Chances.randomFloat(activeAttack.statsLowMultiplier,
							activeAttack.statsHighMultiplier));
			break;
		case SPEED:
			creature.setSpeed(creature.getSpeed()
					* activeAttack.attributeDamageMultiplier
					* Chances.randomFloat(activeAttack.statsLowMultiplier,
							activeAttack.statsHighMultiplier));
			break;
		default:
			break;
		}
		
		if (creature == this.player && humanFight) {
			
			//SEND OUR CALCULATED ATTACK DAMAGE VALUES TO OTHER PARTIE -> in updateHealth, updateAttributes
		}
	}
	
	/**
	 * This method updates Health of attacked Player or Enemy
	 * 
	 * @param player
	 * @param heal
	 * @param damage
	 */
	private void updateHealth(Creature creature, float damage) {
		float hp = creature.getHp() - damage;
		creature.setHp(hp);
		
		if (creature == this.player && humanFight) {
			
			//SEND OUR CALCULATED ATTACK DAMAGE VALUES TO OTHER PARTIE -> in updateHealth, updateAttributes
		}
	}
	
	/**
	 * Is called every time the player uses a potion (directly or indirectly).
	 */
	private void usePotion(Creature potionTaker, Creature opponent, Potion potion) {
			
		/* if player uses antidote / removes the first poison in the list */
		if (potion.MODE == Modes.LIFT) {
			for (Potion _potion : potionTaker.getActivePotions()) {
				if (_potion.EFFECT == Attributes.HP && _potion.MODE == Modes.DECR) {
					potionTaker.removeActivePotions(_potion);
				}
				break;
			}
		} else {		

			/* store potions to the creature that they affect */
			if (potion.TARGET == Targets.SELF) {	// if player uses good potion for himself
				potionTaker.addActivePotions(potion);
				if (potion.MODE == Modes.TINCR) {
					potionIncrease(potionTaker, potion);
				}
			} else {		// if player uses bad potion for enemy
				opponent.addActivePotions(potion);
				if (potion.MODE == Modes.TDECR) {
					potionDecrease(opponent, potion);
				}
			}
		}
	}
	
	/**
	 * Calculates Potions Effects' on a creature every round.<br>
	 * Potions whith mode TINCR or TDECR are not applied in this method but in method usePotion().
	 */
	private void potionEffects(Creature creature) {	
		
		/* apply all non temporary potion effects */
		for (Potion potion : creature.getActivePotions()) {
			potion.DURATION--;
			switch(potion.MODE) {
			case INCR: 
				potionIncrease(creature, potion);
				break;
			case DECR:
				potionDecrease(creature, potion);
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
	 * Reverts effect of temporary Potions.
	 * 
	 * @param creature
	 * @param potion
	 */
	private void revertEffect(Creature creature, Potion potion) {
		if (potion.MODE == Modes.TINCR) {
			potionDecrease(creature, potion);
		} else if (potion.MODE == Modes.TDECR) {
			potionIncrease(creature, potion);
		}
	}
	
	/**
	 * Decreases attributes - caused by potion effects.
	 * 
	 * @param creature
	 * @param potion
	 */
	private void potionDecrease(Creature creature, Potion potion) {
		switch(potion.EFFECT) {
			case HP:
				creature.setHp(creature.getHp() - potion.POWER);
				break;
			case SPEED:
				creature.setSpeed(creature.getSpeed() - potion.POWER);
				break;
			case ACCURACY:
				creature.setAccuracy(creature.getAccuracy() - potion.POWER);
				break;
			case STRENGTH:
				creature.setStrength(creature.getStrength() - potion.POWER);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Increases attributes - caused by potion effects.
	 * 
	 * @param creature
	 * @param potion
	 */
	private void potionIncrease(Creature creature, Potion potion) {
		switch(potion.EFFECT) {
			case HP:
				creature.setHp(creature.getHp() + potion.POWER);
				break;
			case SPEED:
				creature.setSpeed(creature.getSpeed() + potion.POWER);
				break;
			case ACCURACY:
				creature.setAccuracy(creature.getAccuracy() + potion.POWER);
				break;
			case STRENGTH:
				creature.setStrength(creature.getStrength() + potion.POWER);
				break;
			default:
				break;
		}
	}
	
	/**Indicates if a fight is currently active.
	 * @return if fight is active
	 */
	public boolean isInFight() {
		return this.activeFight;
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
	
	/**Allows a player to drink a potion when set to true.
	 * @return if a player may drink a potion
	 */
	public boolean isPotionTakingActive() {
		return this.potionTakingActive;
	}
	
	/**Only the fight's host calculates the "first attack" in a round.
	 * 
	 * @param host
	 * @param slave
	 */
	public synchronized void setHumanFightParties(Creature host, Creature slave) {
		if (host == this.player) {
			this.humanFightHost = true;
		}
		else {
			this.humanFightSlave = true;
		}
	}
	
	/**Called by a networking class to set result of FirstAttacker calculation at fight host.
	 * @param firstAttacker
	 */
	public synchronized void setFirstAttacker(Boolean firstAttacker) {
		
		if (firstAttacker) {
			this.thisPlayerisFirst = 1;
		}
		else {
			this.thisPlayerisFirst = 2;
		}
	}
	
	/**Called by a networking class to set the opponent's selectedPotion.
	 * @param selectedPotion
	 */
	public synchronized void setSelectedPotion(Potion selectedPotion) {
		
		this.selectedPotion = selectedPotion;
	}
	
	/**Called by a networking class to set the opponent's enemyArmorSpeedMalusSum.
	 * @param enemyArmorSpeedMalusSum
	 */
	public synchronized void setEnemyArmorSpeedMalusSum(float enemyArmorSpeedMalusSum, float enemyWeaponSpeedMalusMax) {
		
		this.enemyArmorSpeedMalusSum = enemyArmorSpeedMalusSum;
		this.enemyWeaponSpeedMalusMax = enemyWeaponSpeedMalusMax;
		this.enemySpeedMalusSumSet = true;
	}
	
	/**Called by a networking class to set the opponent's enemyParrySuccess.
	 * @param enemyParrySuccess
	 */
	public synchronized void setEnemyParrySuccess (boolean enemyParrySuccess) {
		
		this.enemyParrySuccess = enemyParrySuccess;
		this.enemyParrySuccessSet = true;
	}
	
	/**Called by a networking class to set the calculated attack damages.
	 * @param healthDamage
	 * @param attributeDamage
	 * @param hitProbability
	 */
	public synchronized void setEnemyAttack (float healthDamage, float attributeDamage, float hitProbability) {
		this.enemyAttackHealthDamage = healthDamage;
		this.enemyAttackAttributeDamage = attributeDamage;
		this.enemyAttackHitProbability = hitProbability;
		this.enemyAttackSet = true;
	}
	
	public synchronized void setEnemyStats (float armorSum, float speed) {
		this.enemyArmorSum = armorSum;
		this.enemySpeed = speed;
		this.enemyStatsSet = true;
	}
	
	/**Sets the acitveAttackType - called by Game.java for Use of Potions.
	 * 
	 */
	public void setActiveAttackType(Attacks attackType) {
		this.activeAttackType = attackType;
	}
}
