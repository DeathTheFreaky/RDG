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
import general.Enums.Channels;
import general.Enums.Levels;
import general.Enums.Modes;
import general.Enums.Targets;
import views.ArmorView;
import views.Chat;
import views.GameEnvironment;
import views.View;
import views.chat.Message;

public class Fight extends View implements Runnable {

	/* If a fight currently takes place */
	private boolean activeFight = false;

	/* different Colors */
	private final Color BLACK = new Color(0.0f, 0.0f, 0.0f);
	private final Color GRAY = new Color(0.2f, 0.2f, 0.2f);
	private final Color LIGHTGRAY = new Color(0.65f, 0.65f, 0.65f);
	private final Color WHITE = new Color(1.0f, 1.0f, 1.0f);
	private final Color PINK = new Color(1f, 0.5f, 0.8f);
	private final Color RED = new Color(1f, 0.0f, 0.0f);

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
	
	// Instances of all available attacks
	private Map<Attacks, Attack> attacks = null;
	
	/* actual instance of an attack (ARMS, HEAD, CHEST, LEGS) - used for both player and enemy 
	 * - needs to be set to null in between  */
	private Attack activeAttack = null;
	
	/* balancing of parrying */
	private float parryMultiplier = 1.0f;

	/* end of a fight */
	private int end = 1;

	/* Reference to the Game, where this Fight belongs to */
	private GameEnvironment gameEnvironment;
	
	/* Reference to Chat class for displaying fight messages */
	private Chat chat;

	/* Creature that the player is fighting against */
	private Creature enemy = null;
	
	/* The Player Himself */
	private Player player;
	
	/* Variables to check which options were selected */
	private boolean changeTabActive = false;
	private boolean potionTakingActive = false;
	
	/* ArmorView is needed to interact with armory items */
	private ArmorView armorView = null;
	
	/* Determine type of attack screen */
	private AttackScreens attackScreen = AttackScreens.MAIN;
	
	/* player vs player host */
	private Boolean humanFightHost = false;
	private Boolean humanFightSlave = false;
	private Boolean humanFight = false; 
	
	/* fightCtr for increasing damage of monster's attack damage over time */
	private float finishedFights = 0; //only increase when fight is won
	
	/* since return type is not allowed */
	
	/* values manipulated by networking class */
	
		/* Selected Attack option from Fight menu - used for both player and enemy 
		 * - needs to be set to null in between */
		private Attacks activeAttackType = null;
	
		/* used in human fight to determine which player gets first attack - set in determineFirstAttack() for each round */
		private int thisPlayerisFirst = 0;
		
		/* a potion drunk by a player and set in armorView.drinkPotion() - set when drinking a potion -> both by player and enemy */
		private Potion selectedPotion = null;
		
		/* the enemy calculates if a parry was successful - set on each Parry */
		private boolean enemyParrySuccess = false;
		private boolean enemyParrySuccessSet = false;
		
		/* needed to determine which player starts a round in a fight - 
		   set on humanFightInitialization() and when enemyStats changes by SetSwitch */
		private float enemyArmorSpeedMalusSum = 0;
		private float enemyWeaponSpeedMalusMax = 0;
		private boolean enemySpeedMalusSumSet = false;
		
		/* speed and armor stats are needed to calculate the attack damages - 
		 * set on humanFightInitialization() and when enemyStats changes by SetSwitch */
		private float enemyArmorSum = 0;
		private float enemySpeed = 0;
		private boolean enemyStatsSet = false;
		
		/* the enemy calculates the health and attribute damage of its attack - set on each attack */
		private float enemyAttackHealthDamage = 0;
		private float enemyAttackAttributeDamage = 0;
		private boolean enemyAttackSet = false;
		
		/* enemie's finishedFightsMultiplier needed for potions */
		private float enemyFightsMultiplier = 0;

	/**Constructs a Fight Instance, which will provide an environment for all fights a player engages in.
	 * @param origin
	 * @param size
	 * @param ge
	 * @param player
	 * @param armorView
	 * @throws SlickException
	 */
	public Fight(Point origin, Dimension size, GameEnvironment ge, Player player, ArmorView armorView, Chat chat)
			throws SlickException {
		super("Fight", origin, size);

		this.gameEnvironment = ge;
		
		this.player = player;
	
		attacks = new AttackFactory().getInstance().getAllAttacks();
		
		this.armorView = armorView;
		
		this.chat = chat;
		
	}
	
	@Override
	public void draw(GameContainer container, Graphics graphics) {

		/* BACKGROUND */
		graphics.setColor(GRAY);
		graphics.fillRect(origin.x, origin.y, size.width, size.height);
		
		// Fight screen
		graphics.setColor(WHITE);
		graphics.fillRect(origin.x + border, origin.y + border,
				fightWindowWidth, fightWindowHeight);
		
		// Fight Option Selection
		graphics.setColor(PINK);
		graphics.fillRect(origin.x + border, size.height - 100 + border,
				size.width - 2 * border, size.height - 100 - 2 * border);
		/* BACKGROUND */
		
		
		/* FOREGROUND */
		/* Enemy Health Bar */
		// Black border around Health bar
		graphics.setColor(BLACK);
		graphics.drawRect(origin.x + border + barGap - 1, origin.y
				+ border + barGap - 1, barWidth + 1,
				barHeight + 1);
		// Gray background bar
		graphics.setColor(LIGHTGRAY);
		graphics.fillRect(origin.x + border + barGap, origin.y + border
				+ barGap, barWidth, barHeight);
		// Actual Bar
		graphics.setColor(RED);
		graphics.fillRect(origin.x + border + barGap, origin.y + border
				+ barGap, barWidth * enemy.getHp()/enemy.getOrHp(), barHeight);
		
		// Own Health Bar
		// Black border around health bar
		graphics.setColor(BLACK);
		graphics.drawRect(fightWindowWidth - border - barGap - barWidth - 1,
				fightWindowHeight - barGap - barHeight - 1,
				barWidth + 1, barHeight + 1);
		// gray background bar
		graphics.setColor(LIGHTGRAY);
		graphics.fillRect(fightWindowWidth - border - barGap - barWidth,
				fightWindowHeight - barGap - barHeight,
				barWidth, barHeight);
		// Actual bar
		graphics.setColor(RED);
		graphics.fillRect(fightWindowWidth - border - barGap - barWidth,
				fightWindowHeight - barGap - barHeight,
				barWidth * player.getHp()/player.getOrHp(), barHeight);
		
		/* Print Names of Enemy and Player */
		/* Enemy name */
		graphics.setColor(BLACK);
		graphics.drawString(this.enemy.NAME, origin.x + border + barGap + 1 , origin.y
				+ border + barGap - 25);
		graphics.drawString(this.player.NAME, fightWindowWidth - border - barGap - barWidth , 
				fightWindowHeight - barGap + 10);

		// Fight Options
		if (attackScreen == AttackScreens.MAIN) {
			graphics.setColor(BLACK);
			graphics.drawString("Attack", optionsWidth / 4 - 25, fightWindowHeight + optionsHeight / 4);
			graphics.drawString("Force Parry", optionsWidth / 4 * 3 - 45, fightWindowHeight + optionsHeight / 4);
			graphics.drawString("Change Set", optionsWidth / 4 - 45, fightWindowHeight + optionsHeight / 4 * 3);
			graphics.drawString("Use Potion", optionsWidth / 4 * 3 - 42, fightWindowHeight + optionsHeight / 4 * 3);
		}
			
		// Attack Options
		if (attackScreen == AttackScreens.ATTACK) {
			graphics.setColor(BLACK);
			graphics.drawString("Chest", optionsWidth / 4 - 20, fightWindowHeight + optionsHeight / 4);
			graphics.drawString("Head", optionsWidth / 4 * 3 - 15, fightWindowHeight + optionsHeight / 4);
			graphics.drawString("Arms", optionsWidth / 4 - 15, fightWindowHeight + optionsHeight / 4 * 3);
			graphics.drawString("Legs", optionsWidth / 4 * 3 - 15, fightWindowHeight + optionsHeight / 4 * 3);
		}
		
		// Waiting Screen
		if (attackScreen == AttackScreens.WAITING) {
			graphics.setColor(BLACK);
			graphics.drawString("Waiting for opponent's action... ", optionsWidth / 4 - 20, fightWindowHeight + optionsHeight / 2);
		}
		
		if (attackScreen != AttackScreens.WAITING) {
			/* Draw lines between the different attack options */
			graphics.setColor(GRAY);
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
						break;
					case OPTION3: System.out.println("Change Set");
						this.activeAttackType = Attacks.SET;
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
						break;
					case OPTION2: System.out.println("Head");
						this.activeAttackType = Attacks.HEAD;
						break;
					case OPTION3: System.out.println("Arms");
						this.activeAttackType = Attacks.ARMS;
						break;
					case OPTION4: System.out.println("Legs");
						this.activeAttackType = Attacks.LEGS;
						break;
				}
			}
		}
	}
	
	/**
	 * Starts a new Fight.
	 * 
	 * @param creature - hands over the enemy
	 */
	@Override
	public void run() {
				
		this.activeFight = true;
		Creature myLoser = null;

		while (this.enemy == null) {
			//wait for enemy to be set
		}
		
		chatMessage("Started fight: " + this.player.NAME + " vs " + this.enemy.NAME);

		this.humanFight = false; //reset to false before checking if it is a human fight
		
		try {
			myLoser = fight();
		} catch (InterruptedException e) {
			System.err.println("Fight was interrupted");
			e.printStackTrace();
		}
		
		/* increase finishedFights Ctr only when player won the fight */
		if (this.player != myLoser) {
			finishedFights++;
			chatMessage("VICTORY!");
			chatMessage("You won a fight against " + this.enemy.NAME);
		} else {
			chatMessage("DEFEAT!");
			chatMessage("You lost a fight against " + this.enemy.NAME);
		}
		
		/* after a fight, reset the fighting instance's variables */
		reset();
		
		gameEnvironment.fightEnds(myLoser);
	}
	
	/**Sets the enemy creature for a fight.
	 * @param enemy
	 */
	public synchronized void setEnemy(Creature enemy) {
		this.enemy = enemy;
	}

	/**Reset fight variables to default values.
	 */
	private void reset() {
				
		this.activeFight = false;
		this.enemy = null;
		this.attackScreen = AttackScreens.MAIN;
		/* only send these values at begin of a fight and when necessary */
		this.enemySpeedMalusSumSet = false;
		this.enemyArmorSpeedMalusSum = 0;
		
		this.enemyStatsSet = false;
		this.enemySpeed = 0;
		this.enemyArmorSum = 0;
		resetRoundVariables();
	}
	
	/**Reset variables that need to be resetted each round in a fight.
	 */
	private void resetRoundVariables() {
				
		this.changeTabActive = false;
		this.potionTakingActive = false;
		this.thisPlayerisFirst = 0;
		this.enemyAttackSet = false;
		this.activeAttackType = null;
		this.activeAttack = null;
		this.selectedPotion = null;
		this.enemyParrySuccessSet = false;
		this.enemyParrySuccess = false;
		this.enemyAttackSet = false;
		this.enemyAttackHealthDamage = 0;
		this.enemyAttackAttributeDamage = 0;
	}
	
	/**
	 * This method manages the process of fighting.
	 * 
	 * @return the looser of a fight
	 * @throws InterruptedException 
	 */
	private Creature fight() throws InterruptedException {
		
		System.out.println("\n\n\n");
		
		System.out.println("Player's health: " + this.player.getHp());
		
		System.out.println(this.player + " has " + this.player.getHp() + " health,\n " + this.player.getAccuracy() + " accuracy,\n " 
				+ this.player.getSpeed() + " speed,\n " + this.player.getStrength() + " strength" );
		
		System.out.println("Opponent's health: " + this.enemy.getHp());
		
		System.out.println(this.enemy + " has " + this.enemy.getHp() + " health,\n " + this.enemy.getAccuracy() + " accuracy,\n " 
				+ this.enemy.getSpeed() + " speed,\n " + this.enemy.getStrength() + " strength" );
		
		/* Determine if this is a human fight. */
		this.humanFight = humanFightInitialization();
		
		System.out.println("humanFight: " + this.humanFight);
		
		/* Main fight loop */
		while (player.getHp() > 0 && enemy.getHp() > 0) { // as long as nobody died
			
			Creature creature1 = null; //player1 comes first in a round
			Creature creature2 = null;
			
			/* Determine which creature gets the first Attack in a fight. */
			int firstAttackTemp =  determineFirstAttack();
			
			if (firstAttackTemp == 1) {
				creature1 = this.player;
				creature2 = this.enemy;
				this.attackScreen = AttackScreens.MAIN;
			}
			else if (firstAttackTemp == 2) {
				creature1 = this.enemy;
				creature2 = this.player;
				this.attackScreen = AttackScreens.WAITING;
			}
			
			System.out.println("firstAttack by " + creature1);
			
			Thread.sleep(1000);
			
			/* perform Attack of creature first in round */
			attackControl(creature1, creature2);
			
			/* potion effects for a creature are applied after its attack */
			potionEffects(creature1);
			
			if (player.getHp() <= 0) {
				break;
			} else if (enemy.getHp() <= 0){
				break;
			}
			
			/* set to null between attacks of player and enemy to determine if attack was already chosen */
			activeAttack = null;
			activeAttackType = null;
			
			/* change attack screen */
			if (firstAttackTemp == 1) {
				this.attackScreen = AttackScreens.WAITING;
			}
			else if (firstAttackTemp == 2) {
				this.attackScreen = AttackScreens.MAIN;
			}
			
			Thread.sleep(1000);
			
			/* perform Attack of creature first in round */
			attackControl(creature2, creature1);
			
			Thread.sleep(1000);
			
			/* potion effects for a creature are applied after its attack */
			potionEffects(creature2);
			
			/* reset variables that need to be changed each round */
			resetRoundVariables();
		}
		
		/* determine who lost the fight */
		Creature fightLoser = null;
		
		if (player.getHp() <= 0) {
			player.resetOriginals();
			enemy.resetOriginals();
			fightLoser = player;
		} else {
			fightLoser = enemy;
			//give attribute bonus to winner of the fight
			attributeBonusForWinner((Monster) fightLoser);
			//set bonussed original values as new normal player values
			player.resetOriginals(); 
		}
		
		/* empty active potion lists */
		player.emptyActivePotions();
		enemy.emptyActivePotions();
		
		/* return the loser of a fight */
		return fightLoser;
	}
	
	/**Waits for and manages humanFightInitialization.
	 * @return true if it is a humenFight
	 * @throws InterruptedException 
	 */
	private boolean humanFightInitialization() throws InterruptedException {
		
		System.out.println(("human Fight initialization"));
		
		if (this.enemy instanceof Player) {
			
			int timeoutctr = 0;
			boolean successfulCommunication = false;
			
			/* wait for fight host to set needed information */
			while (timeoutctr <= 10) {
				
				if (((humanFightHost == true && humanFightSlave == false) 
						|| (humanFightHost == false && humanFightSlave == true))
						&& enemySpeedMalusSumSet == true 
						&& enemyStatsSet == true 
						&& enemyFightsMultiplier > 0) {
					
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
				System.err.println("Initialization of human fight timed out...");
				new Exception("Initialization of human fight failed");
			}
			
			return true;
		}
		
		return false;
	}
	
	/**Send changed Stats when player's armor set changes.
	 * @throws InterruptedException 
	 * 
	 */
	private void sendStats() throws InterruptedException {
		
		System.out.println("Sending Stats");

		/* needed to determine which player starts a round in a fight - 
		   set on humanFightInitialization() and when enemyStats changes by SetSwitch */
		float playerArmorSpeedMalusSum = armorView.getStats(ArmorStatsTypes.ARMAMENT, ArmorStatsMode.SUM, ArmorStatsAttributes.SPEED);
		float playerWeaponSpeedMalusMax = armorView.getStats(ArmorStatsTypes.WEAPONS, ArmorStatsMode.MIN, ArmorStatsAttributes.SPEED);
		
		//send message(playerArmorSpeedMalusSum, value)
		//send message(playerWeaponSpeedMalusMax, value)
		
		/* speed and armor stats are needed to calculate the attack damages - 
		 * set on humanFightInitialization() and when enemyStats changes by SetSwitch */
		
		float playerArmorSum = armorView.getStats(ArmorStatsTypes.ARMAMENT, ArmorStatsMode.SUM, ArmorStatsAttributes.ARMOR);
		float playerSpeed = calcCreatureSpeed(this.player);
		float playerAccuracy = calcCreatureAccuracy(this.player);
		
		//send message(playerArmorSum, value)
		//send message(playerSpeed, value)
		//send message(playerAccuracy, value)
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
				Thread.sleep(100);
			}
			
			chosenAttackType = this.activeAttackType;
		}
		else {
						
			chosenAttackType = Chances.randomAttackType();
		}
				
		return chosenAttackType;
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
				if (creature1 == this.player || (creature1 instanceof Monster)) {
					chatMessage(creature1.NAME + " chose TORSO Attack", true);
				}
				break;
			case HEAD:
				activeAttack = attacks.get(Attacks.HEAD);
				if (creature1 == this.player || (creature1 instanceof Monster)) {
					chatMessage(creature1.NAME + " chose HEAD Attack", true);
				}
				break;
			case ARMS:
				activeAttack = attacks.get(Attacks.ARMS);
				if (creature1 == this.player || (creature1 instanceof Monster)) {
					chatMessage(creature1.NAME + " chose ARMS Attack", true);
				}
				break;
			case LEGS:
				activeAttack = attacks.get(Attacks.LEGS);
				if (creature1 == this.player || (creature1 instanceof Monster)) {
					chatMessage(creature1.NAME + " chose LEGS Attack", true);
				}
				break;
			case SET:
				
				/* only switch sets if this is the player's turn */
				if (creature1 == this.player) {
					System.out.println("Switching sets");
					armorView.switchSet();
					chatMessage(creature1.NAME + " chose SWITCH SET", true);
					sendStats();
				}
				break;
			case POTION:
				
				if (creature1 == this.player) {
					chatMessage(creature1.NAME + " chose TAKE POTION", true);
				}
				
				/* select potion */	
				selectedPotion = getSelectedPotion(creature1);	
								
				if (selectedPotion != null) {
					/* manage the handling of a drunk potion */
					usePotion(creature1, creature2, selectedPotion);
				}
				
				/* reset selectedPotion */
				selectedPotion = null;
				
				break;
			case PARRY:
				
				if (creature1 == this.player) {
					chatMessage(creature1.NAME + " chose PARRY", true);
				}
				
				/* will also be calculated for other human player, but has no effect -> message and health damage come from attacker computer */
				if (parrySuccess(creature1, creature2) == true) {
					
					if (creature1 == this.player) {
						chatMessage(creature1.NAME + "'s PARRY was successful", true);
					}
					
					/* when a player decides to parry, if successful, he deals x times the damage of a normal torso attack */
					parryMultiplier = 2.0f;
					activeAttack = attacks.get(Attacks.TORSO);	// muss man noch rausfinden was am besten is (ich war ja für head aber flo für torso xD)
				} else {
					if (creature1 == this.player) {
						chatMessage(creature1.NAME + "'s PARRY was unsuccessful", true);
					}
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
			
			//enemyStats are set when starting a fight and also sent when enemyStats change
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
		
		float healthDamage = 0;
		float attributeDamage = 0;
		
		if ((attacker instanceof Monster) || attacker == this.player) {
			
			/* now calculate the actual attacks */
			if (calcHitSuccess(attacker, defender) == true) {
				
				healthDamage = calcHealthDamage(attacker, defender);	// could be used to display Damage on Screen
				attributeDamage = calcAttributeDamage(defender);
			} 
			if (humanFight) {
				
				//Send healthDamage, attributeDamage to enemy 
				//message(healthDamage, value)
				//message(attributeDamage, value)
			}
		} else {
			
			healthDamage = enemyAttackHealthDamage;
			attributeDamage = enemyAttackAttributeDamage;
		}
			
		/* update attributes with calculated Damages */
		if (healthDamage > 0) {
			chatMessage(defender.NAME + " suffers " + round(healthDamage, 1) + " HEALTH damage");
			updateHealth(defender, healthDamage);
		} else {
			chatMessage(attacker.NAME + "'s Attack missed its target");
		}
		if (attributeDamage > 0) {
			chatMessage(defender.NAME + " suffers " +  round(attributeDamage, 1) + " " + activeAttack.effect + " damage");
			updateAttributes(defender, attributeDamage);
		}
		
		/* reset values */
		this.enemyArmorSum = 0;
		this.enemySpeed = 0;
		this.enemyStatsSet = false;
		this.enemyAttackHealthDamage = 0;
		this.enemyAttackAttributeDamage = 0;
		this.enemyAttackSet = false;
	}
	
	/**Calculate the speed of a creature, taking into account mali from armament and weapons of a player.
	 * 
	 * @param creature
	 * @return
	 * @throws InterruptedException 
	 */
	private float calcCreatureSpeed(Creature creature) throws InterruptedException {
		
		/* constants */
		float armorSpeedMalusMult = 0.3f;
		float weaponSpeedMult = 0.3f; 
		
		/* variables */
		float playerArmorSpeedMalusSum; //sum of all equipped armament's armor and shield's defense values
		float playerWeaponSpeedMalusMax; //100 - min. Speed of equipped Weapons
		float playerArmorSpeedMalus; 
		float playerWeaponSpeedMalus; 
		
		/* results */
		float speed = 0;
		
		/* get speed of a monster */
		if (creature instanceof Monster) {
			return creature.getSpeed();
		}
		
		/* get speed of local player */
		if (creature == this.player) {
			playerArmorSpeedMalusSum = armorView.getStats(ArmorStatsTypes.ARMAMENT, ArmorStatsMode.SUM, ArmorStatsAttributes.SPEED);
			playerWeaponSpeedMalusMax = armorView.getStats(ArmorStatsTypes.WEAPONS, ArmorStatsMode.MAX, ArmorStatsAttributes.SPEED);
		}
		
		/* get speed of other player */
		else {
			//enemyStats are set on humanFightInitialization and sent when enemyStats change
			playerArmorSpeedMalusSum = enemyArmorSpeedMalusSum;
			playerWeaponSpeedMalusMax = enemyWeaponSpeedMalusMax;
		}
		
		/* perform calculations */
		playerArmorSpeedMalus = creature.getSpeed() / 100 * playerArmorSpeedMalusSum * armorSpeedMalusMult;
		playerWeaponSpeedMalus = creature.getSpeed() / 100 * playerWeaponSpeedMalusMax * weaponSpeedMult;
		speed = creature.getSpeed() - playerArmorSpeedMalus - playerWeaponSpeedMalus;
		
		return speed;
	}
	
	/**Calculates the faster creature for initial attack and parry success.<br>
	 * Creature speed, armor speed malus value and weapon speed are taken into account.<br>
	 * 
	 * @param attacker
	 * @param defender
	 * @param waitForOtherPlayer
	 * @return true if the attacker succeeds, false if the defender succeeds
	 * @throws InterruptedException
	 */
	private AttDefMinAvgMax speedBasedSuccess(Creature attacker, Creature defender) throws InterruptedException {
		
		/* constants */
		float speedRandLow = 0.5f;
		float speedRandHigh = 1.5f;
		float speedBase = 50f;
		float finishedFightsDivisor = 40f;
		
		/* variables */
		float attackerSpeedTemp;
		float defenderSpeedTemp;
		float attackerSpeedBase;
		float defenderSpeedBase;
		
		/* results */
		MinAvgMax attackerSpeed;
		MinAvgMax defenderSpeed;
		
		/* determine creatures' speed */
		attackerSpeedTemp = attacker.getSpeed();
		defenderSpeedTemp = defender.getSpeed();
		
		/* include a finishedFights bonus for monsters */
		if (defender instanceof Monster) {
			defenderSpeedTemp = defenderSpeedTemp * (1 + finishedFights/finishedFightsDivisor);
		}
		if (attacker instanceof Monster) {
			attackerSpeedTemp = attackerSpeedTemp * (1 + finishedFights/finishedFightsDivisor);
		}
		
		/* perform calculations */
		attackerSpeedBase = attackerSpeedTemp + (calcCreatureSpeed(attacker) / attackerSpeedTemp) * speedBase;
		defenderSpeedBase = defenderSpeedTemp + (calcCreatureSpeed(defender) / defenderSpeedTemp) * speedBase;
		attackerSpeed = new MinAvgMax(speedRandLow, speedRandHigh, attackerSpeedBase);
		defenderSpeed = new MinAvgMax(speedRandLow, speedRandHigh, defenderSpeedBase);
				
		return new AttDefMinAvgMax(attackerSpeed, defenderSpeed);
	}

	/**Determine which creature comes first in a round.
	 * @return 1 if thisPlayer comes first, 2 if enemy comes first
	 * @throws InterruptedException 
	 */
	private int determineFirstAttack() throws InterruptedException {
		
		System.out.println("determineFirstAttack"); 
		
		/* this computer is not the lobby hoster */
		if (humanFightSlave == false) {
			
			/* variables */
			AttDefMinAvgMax speedBasedSuccessRetVal;
			float randAttackerSpeed;
			float randDefenderSpeed;
			
			/* perform randomizing calculations */
			speedBasedSuccessRetVal = speedBasedSuccess(this.player, this.enemy);
			randAttackerSpeed = Chances.randomFloat(speedBasedSuccessRetVal.attacker.min, speedBasedSuccessRetVal.attacker.max);
			randDefenderSpeed = Chances.randomFloat(speedBasedSuccessRetVal.defender.min, speedBasedSuccessRetVal.defender.max);
			
			System.out.println("randAttackerSpeed: " + randAttackerSpeed + ", randDefenderSpeed: " + randDefenderSpeed);
			
			/* determine which player comes first */
			if (randAttackerSpeed >= randDefenderSpeed) {
				thisPlayerisFirst = 1;
			} else {
				thisPlayerisFirst = 2;
			}
			
			if (humanFightHost == true) {
				// SEND RESULT OF CALCULATION TO SLAVE -> message(thisPlayerisFirst, value is opposite of local value)
			}
		}
		
		/* this computer is the lobby hoster */
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
		
		return thisPlayerisFirst;	
	}
	
	/**
	 * Calculates if an attack can be parried.<br>
	 * Unfortunately, the name "parry" is a little ambiguous.<br>
	 * The concept of "parrying" does not really work because under some circumstances
	 * a player can perform two attacks in a row (slower player on first attack, faster player on second attack).<br>
	 * "Parry" could be seen as an especially powerful attack which forces the opponent to try a parry.<br>
	 * If the enemy succeeds with his parry, he will not suffer any damage.<br>
	 * However, if the attacker is faster than the enemy, he performs a torso attack with twice as much damage.
	 * 
	 * @return true if a parry will be successful
	 * @throws InterruptedException 
	 */
	private boolean parrySuccess(Creature attacker, Creature defender) throws InterruptedException {

		System.out.println("parrySuccess");

		/* variables */
		AttDefMinAvgMax speedBasedSuccessRetVal;
		float randAttackerSpeed;
		float randDefenderSpeed;
		boolean parrySuccess = false;
		
		/* perform randomizing calculations */
		speedBasedSuccessRetVal = speedBasedSuccess(this.player, this.enemy);
		randAttackerSpeed = Chances.randomFloat(speedBasedSuccessRetVal.attacker.min, speedBasedSuccessRetVal.attacker.max);
		randDefenderSpeed = Chances.randomFloat(speedBasedSuccessRetVal.defender.min, speedBasedSuccessRetVal.defender.max);
		
		System.out.println("randAttackerSpeed: " + randAttackerSpeed + ", randDefenderSpeed: " + randDefenderSpeed);
		
		/* determine which player comes first */
		if (randAttackerSpeed >= randDefenderSpeed) {
			parrySuccess = true;
		} else {
			parrySuccess = false;
		}
		
		return parrySuccess;
	}

	/**Calculate the accuracy of a creature, taking into account the multipliers from a player's weapons.
	 * @param creature
	 * @return
	 * @throws InterruptedException 
	 */
	private float calcCreatureAccuracy(Creature creature) throws InterruptedException {
	
		/* constants */
		float accuracyMultiplier = 2.65f;

		/* results */
		float accuracy;
		
		/* get accuracy of a monster */
		if (creature instanceof Monster) {
			return creature.getAccuracy();
		}
		
		/* perform calculations */
		accuracy = creature.getAccuracy() / 100 * armorView.getStats(ArmorStatsTypes.WEAPONS, ArmorStatsMode.AVERAGE, ArmorStatsAttributes.ACCURACY) * accuracyMultiplier;
		
		return accuracy;
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
		
		System.out.println("calcHitSuccess");
		
		/* constants */
		float randAccuracyLow = 0.5f;
		float randAccuracyHigh = 1.5f;
		float accuracyBase = 50f;
		float finishedFightsDivisor = 40f;
		
		/* variables */
		float attackerAccuracyBase;
		MinAvgMax attackerAccuracy;
		MinAvgMax defenderSpeed;
		float randAttackerAccuracy;
		float randDefenderSpeed;
		float finishedFightsMult;
		
		/* results */
		boolean hitSuccess;
		
		/* set finishedFightsMult Bonus for Monsters */
		if (attacker instanceof Monster) {
			finishedFightsMult = 1 + (finishedFights/finishedFightsDivisor);
		} else {
			finishedFightsMult = 1;
		}
			
		/* perform calculations */
		attackerAccuracyBase = activeAttack.hitProbability * (attacker.getAccuracy() + ((calcCreatureAccuracy(attacker) / attacker.getAccuracy()) * accuracyBase));
		attackerAccuracy = new MinAvgMax(randAccuracyLow, randAccuracyHigh, attackerAccuracyBase);
		defenderSpeed = speedBasedSuccess(attacker, defender).defender;
		
		/* calculate randomized values */
		randAttackerAccuracy = Chances.randomFloat(attackerAccuracy.min, attackerAccuracy.max);
		randDefenderSpeed = Chances.randomFloat(defenderSpeed.min, defenderSpeed.max);
		
		System.out.println("randAttackerAccuracy: " + randAttackerAccuracy + ", randDefenderSpeed: " + randDefenderSpeed);
		
		/* determine hitSuccess - always hit after successful parry */
		if (randAttackerAccuracy >= randDefenderSpeed || parryMultiplier > 1.0f) {
			hitSuccess = true;
		} else {
			hitSuccess = false;
		}
		
		return hitSuccess;
	}

	/**
	 * Calculates dealt health damage.
	 * 
	 * @return amount of dealt health damage
	 */
	private float calcHealthDamage(Creature attacker, Creature defender) {
		
		/* constants */
		float baseDefensePlayer = 100f;
		float baseDefenseEasy = 100f;
		float baseDefenseNormal = 150f;
		float baseDefenseHard = 200f;
		float finishedFightsDivisor = 100f;
		float damageMult = 45f;
		float weaponMult = 1f;
		float strengthMult = 1.5f;
		float armorMult = 0.5f;
		
		/* parameters */
		float attackerWeaponDamage;
		float defenderArmor;
		float baseDefense;
		
		/* variables */
		MinAvgMax attackDamage;
		MinAvgMax attackerRawDamage;
		float defenderDefense;
		MinAvgMax healthDamage;
		float finishedFightsMult;
		
		/* results */
		float randHealthDamage;
		
		/* set parameters */
		if (attacker instanceof Monster) {
			attackerWeaponDamage = 0;
			finishedFightsMult = 1 + (finishedFights / finishedFightsDivisor);
		} else {
			attackerWeaponDamage = armorView.getStats(ArmorStatsTypes.WEAPONS, ArmorStatsMode.SUM, ArmorStatsAttributes.ATTACK);
			finishedFightsMult = 1;
		}
		if (defender instanceof Monster) {
			defenderArmor = 0;
			finishedFightsMult = 1 + (finishedFights / finishedFightsDivisor);
			if (((Monster) defender).level == Levels.EASY) {
				baseDefense = baseDefenseEasy;
			} else if (((Monster) defender).level == Levels.NORMAL) {
				baseDefense = baseDefenseNormal;
			} else {
				baseDefense = baseDefenseHard;
			}
		} else {
			defenderArmor = armorView.getStats(ArmorStatsTypes.ARMAMENT, ArmorStatsMode.SUM, ArmorStatsAttributes.ARMOR);
			finishedFightsMult = 1;
			baseDefense = baseDefensePlayer;
		}
		
		/* perform calculations */
		attackDamage = new MinAvgMax(activeAttack.statsLowMultiplier, activeAttack.statsHighMultiplier, activeAttack.hpDamageMultiplier);
		attackerRawDamage = new MinAvgMax (
			attackDamage.min * (strengthMult * attacker.getStrength() + weaponMult * attackerWeaponDamage),
			attackDamage.max * (strengthMult * attacker.getStrength() + weaponMult * attackerWeaponDamage)		
		);
		defenderDefense = finishedFightsMult * baseDefense + armorMult * defenderArmor;
		healthDamage = new MinAvgMax(
			parryMultiplier * attackerRawDamage.min / defenderDefense * damageMult,
			parryMultiplier * attackerRawDamage.max / defenderDefense * damageMult
		);
		
		/* calculate a randomized result */
		randHealthDamage = Chances.randomFloat(healthDamage.min, healthDamage.max);
		
		return randHealthDamage;
	}
	
	/**Calculates the damage on the affected attribute of an attack.
	 * 
	 * @param defender
	 * @return
	 */
	private float calcAttributeDamage(Creature defender) {
		
		/* constants */
		float playerAttributeDamageMult = 1f;
		float easyAttributeDamageMult = 1.05f;
		float normalAttributeDamageMult = 1f;
		float hardAttributeDamageMult = 0.95f;
		
		/* variables */
		float attributeDamageMult;
		float defenderDamageMult;
		float defenderAttribute = 0;
		
		/* results */
		float attributeDamage;

		/* get enemies current attribute value */
		switch(activeAttack.effect) {
			case HP:
				defenderAttribute = defender.getHp();		
				break;
			case ACCURACY:
				defenderAttribute = defender.getAccuracy();
				break;
			case STRENGTH:
				defenderAttribute = defender.getStrength();
				break;
			case SPEED:
				defenderAttribute = defender.getSpeed();
				break;
			default:
				break;
		}
		
		/* set attributeDamageMult */
		if (defender instanceof Monster) {
			if (((Monster) defender).level == Levels.EASY) {
				attributeDamageMult = easyAttributeDamageMult;
			} else if (((Monster) defender).level == Levels.NORMAL) {
				attributeDamageMult = normalAttributeDamageMult;
			} else {
				attributeDamageMult = hardAttributeDamageMult;
			} 
		} else {
			attributeDamageMult = playerAttributeDamageMult;
		}
		
		/* perform calculations */
		defenderDamageMult = attributeDamageMult * activeAttack.attributeDamageMultiplier;
		attributeDamage = defenderAttribute - defenderAttribute * defenderDamageMult;		
		
		return attributeDamage;
	}
	
	/**This method updates the health of attacked opponent.
	 * @param creature
	 * @param damage
	 */
	private void updateHealth(Creature defender, float healthDamage) {

		float hp  = defender.getHp() - healthDamage;
		if (hp < 0) {
			hp = 0;
		}
		defender.setHp(hp);
		
		System.out.println(defender + " now has " + defender.getHp() + " health");
	}
	
	/**Update an opponent's attributes in reaction to an attack.
	 * @param creature
	 * @param attributeDamageMult
	 */
	private void updateAttributes(Creature defender, float attributeDamage) {

		System.out.println(defender + " now has " + defender.getHp() + " health,\n " + defender.getAccuracy() + " accuracy,\n " 
				+ defender.getSpeed() + " speed,\n " + defender.getStrength() + " strength" );
		
		switch(activeAttack.effect) {
			case HP:
				/*float hp = defender.getHp() - attributeDamage;
				if (hp < 0) {
					hp = 0;
				}
				defender.setHp(hp);*/
				break;
			case ACCURACY:
				float accuracy = defender.getAccuracy() - attributeDamage;
				if (accuracy < 0) {
					accuracy = 0;
				}
				defender.setAccuracy(accuracy);
				break;
			case STRENGTH:
				float strength = defender.getStrength() - attributeDamage;
				if (strength < 0) {
					strength = 0;
				}
				defender.setStrength(strength);
				break;
			case SPEED:
				float speed = defender.getSpeed() - attributeDamage;
				if (speed < 0) {
					speed = 0;
				}
				defender.setSpeed(speed);
				break;
			default:
				break;
		}
		
		if (defender == this.player && humanFight) {
			
			//SEND OUR CALCULATED ATTACK DAMAGE VALUES TO OTHER PARTIE -> message(attributeDamage, value)
		}
		
		System.out.println(defender + " now has " + defender.getHp() + " health,\n " + defender.getAccuracy() + " accuracy,\n " 
				+ defender.getSpeed() + " speed,\n " + defender.getStrength() + " strength" );
	}
	
	/**
	 * Is called every time the player uses a potion (directly or indirectly).
	 */
	private void usePotion(Creature potionTaker, Creature opponent, Potion potion) {

		/* if player uses antidote / removes the first poison in the list */
		if (potion.MODE == Modes.LIFT) {
			chatMessage(potionTaker.NAME + " uses " + potion.NAME + " to cure poisoning");
			for (Potion _potion : potionTaker.getActivePotions()) {
				if (_potion.EFFECT == Attributes.HP && _potion.MODE == Modes.DECR) {
					potionTaker.removeActivePotions(_potion);
				}
				break;
			}
		} else {		

			/* store potions to the creature that they affect */
			if (potion.TARGET == Targets.SELF) {	// if player uses good potion for himself
				chatMessage(potionTaker.NAME + " uses " + potion.NAME + " on him/herself");
				potionTaker.addActivePotions(potion);
				if (potion.MODE == Modes.TINCR) {
					potionIncrease(potionTaker, potion);
				}
			} else {		// if player uses bad potion for enemy
				chatMessage(potionTaker.NAME + " uses " + potion.NAME + " against " + opponent.NAME);
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
	}
	
	/**
	 * Reverts effect of temporary Potions.
	 * 
	 * @param creature
	 * @param potion
	 */
	private void revertEffect(Creature creature, Potion potion) {
				
		chatMessage("Reverting effect of " + potion.NAME + " for " + creature.NAME);
		
		System.out.println(creature + " before reverting has " + creature.getHp() + " health,\n " + creature.getAccuracy() + " accuracy,\n " 
				+ creature.getSpeed() + " speed,\n " + creature.getStrength() + " strength" );

		if (potion.MODE == Modes.TINCR) {
			potionDecrease(creature, potion);
		} else if (potion.MODE == Modes.TDECR) {
			potionIncrease(creature, potion);
		}
		
		System.out.println(creature + " after reverting has " + creature.getHp() + " health,\n " + creature.getAccuracy() + " accuracy,\n " 
				+ creature.getSpeed() + " speed,\n " + creature.getStrength() + " strength" );
	}
	
	/**
	 * Decreases attributes - caused by potion effects.
	 * 
	 * @param creature
	 * @param potion
	 */
	private void potionDecrease(Creature creature, Potion potion) {
				
		/* constants */
		float finishedFightsDivisor = 50f;
		
		/* variables */
		float finishedFightsMult = 1 + (finishedFights / finishedFightsDivisor);
		if (creature == this.enemy && (creature instanceof Player)) {
			finishedFightsMult = enemyFightsMultiplier;
		}
		
		if (potion.DURATION > 0) {
			chatMessage(potion.NAME + " decreasing " + creature.NAME + "'s " + potion.EFFECT + " by " + round(potion.POWER, 1) * finishedFightsMult);
		}
			
		switch(potion.EFFECT) {
			case HP:
				float hp = creature.getHp() - potion.POWER * finishedFightsMult;
				if (hp < 0) {
					hp = 0;
				}
				creature.setHp(hp);
				break;
			case SPEED:
				float speed = creature.getSpeed() - potion.POWER * finishedFightsMult;
				if (speed < 0) {
					speed = 0;
				}
				creature.setSpeed(speed);
				break;
			case ACCURACY:
				float accuracy = creature.getAccuracy() - potion.POWER * finishedFightsMult;
				if (accuracy < 0) {
					accuracy = 0;
				}
				creature.setAccuracy(accuracy);
				break;
			case STRENGTH:
				float strength = creature.getStrength() - potion.POWER * finishedFightsMult;
				if (strength < 0) {
					strength = 0;
				}
				creature.setStrength(strength);
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
				
		/* constants */
		float finishedFightsDivisor = 50f;
		
		/* variables */
		float finishedFightsMult = 1 + (finishedFights / finishedFightsDivisor);
		if (creature == this.enemy && (creature instanceof Player)) {
			finishedFightsMult = enemyFightsMultiplier;
		}
		
		if (potion.DURATION > 0) {
			chatMessage(potion.NAME + " increasing " + creature.NAME + "'s " + potion.EFFECT + " by " + round(potion.POWER, 1) * finishedFightsMult);
		}
		
		switch(potion.EFFECT) {
			case HP:
				float hp = creature.getHp() + potion.POWER * finishedFightsMult;
				if (hp > creature.getOrHp()) {
					hp = creature.getOrHp();
				}
				creature.setHp(hp);
				break;
			case SPEED:
				creature.setSpeed(creature.getSpeed() + potion.POWER * finishedFightsMult);
				break;
			case ACCURACY:
				creature.setAccuracy(creature.getAccuracy() + potion.POWER * finishedFightsMult);
				break;
			case STRENGTH:
				creature.setStrength(creature.getStrength() + potion.POWER * finishedFightsMult);
				break;
			default:
				break;
		}
	}
	
	/**When a player defeats a monster, he gains a bonus on one if his attributes.
	 * @param fightLoser
	 */
	private void attributeBonusForWinner(Monster fightLoser) {
				
		chatMessage(this.player.NAME + " receives " + round(fightLoser.killBonus, 1) + " " + fightLoser.killBonusType + " bonus");

		switch(fightLoser.killBonusType) {
			case HP: 
					this.player.setOrHp(this.player.getOrHp() + fightLoser.killBonus);
				break;
			case ACCURACY: 
					this.player.setOrAccuracy(this.player.getOrAccuracy() + fightLoser.killBonus);
				break;
			case SPEED: 
					this.player.setOrSpeed(this.player.getOrSpeed() + fightLoser.killBonus);
				break;
			case STRENGTH: 
					this.player.setOrStrength(this.player.getOrStrength() + fightLoser.killBonus);
				break;
		}
		
		System.out.println(this.player + " now has " + this.player.getOrHp() + " health,\n " + this.player.getOrAccuracy() + " accuracy,\n " 
				+ this.player.getOrSpeed() + " speed,\n " + this.player.getOrStrength() + " strength" );
		
		return;
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
		this.enemyAttackSet = true;
	}
	
	/**Called by a networking class to set the enemies attributes.
	 * @param armorSum
	 * @param speed
	 */
	public synchronized void setEnemyStats (float armorSum, float speed) {
		
		this.enemyArmorSum = armorSum;
		this.enemySpeed = speed;
		this.enemyStatsSet = true;
	}
	
	/**Sets the activeAttackType - called by Game.java and opponent host.
	 * 
	 */
	public void setActiveAttackType(Attacks attackType) {
		
		this.activeAttackType = attackType;
	}
	
	/**Displays messages in Chat and checks which channel to use.
	 * 
	 */
	private void chatMessage(String text) {
		
		Channels channel = Channels.PRIVATE;
		
		chat.newMessage(new Message(text, channel));
	}
	
	/**Displays messages in Chat and checks which channel to use.
	 * 
	 */
	private void chatMessage(String text, Boolean checkPublic) {
		
		Channels channel = Channels.PRIVATE;
		
		if (checkPublic && (this.enemy instanceof Player)) {
			channel = Channels.PUBLIC;
		}
		
		chat.newMessage(new Message(text, channel));
	}
	
	/**Used for rounding in chat messages.
	 * @param value
	 * @param places
	 * @return rounded value for n places
	 */
	private float round(float value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (float) tmp / factor;
	}
}
