package gameEssentials;

import general.Main;
import general.ResourceManager;
import general.Enums.CreatureType;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import views.ArmorView;
import views.Chat;
import views.GameEnvironment;
import views.InventoryView;
import views.Minimap;
import configLoader.Configloader;

/**Used for loading the game while a loading screen is displayed.
 * @author Flo
 *
 */
public class GameLoad implements Runnable {

	@Override
	public void run() {
		
		try {
		
			Game myGame = Game.getInstance();
	
			/* load config - must be successful in order to continue */
			try {
				myGame.configloader = new Configloader().getInstance();
			} catch (IllegalArgumentException | ParserConfigurationException
					| SAXException | IOException e) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
						"Parsing Configuration Files failed.", e);
				System.exit(1);
			}
	
			// Test Printing
			/*
			 * ConfigTestprinter configprinter = new
			 * ConfigTestprinter(configloader); configprinter.print();
			 */
			
			/* determined by network lobby  - TESTING only */
			myGame.lobbyHost = myGame.networkManager.isLobbyHost();
	
			/* Points in tile numbers */
			myGame.gameEnvironmentOrigin = new Point(0, 0);
			myGame.chatOrigin = new Point(0, 12);
			myGame.armorViewOrigin = new Point(15, 0);
			myGame.inventoryViewOrigin = new Point(15, 12);
			myGame.minimapOrigin = new Point(20, 20);
	
			/* Initialize Factory and Manager classes! */
			myGame.resourceManager = new ResourceManager().getInstance();
	
			/* network lobby must be called before this to detect player type */
			CreatureType playerType;
			if (myGame.lobbyHost) {
				playerType = CreatureType.PLAYER1;
			}
			else {
				playerType = CreatureType.PLAYER2;
			}
			if (playerType == CreatureType.PLAYER1) {
				myGame.player = new Player(myGame.playerName,
						new ResourceManager().getInstance().IMAGES.get("Player1"),
						myGame.gameEnvironmentOrigin, CreatureType.PLAYER1, true);
				myGame.opponent = new Player("Testenemy",
						new ResourceManager().getInstance().IMAGES.get("Player2"),
						myGame.gameEnvironmentOrigin, CreatureType.PLAYER2, false);
			} else if (playerType == CreatureType.PLAYER2) {
				myGame.player = new Player(myGame.playerName,
						new ResourceManager().getInstance().IMAGES.get("Player2"),
						myGame.gameEnvironmentOrigin, CreatureType.PLAYER2, true);
				myGame.opponent = new Player("Testenemy",
						new ResourceManager().getInstance().IMAGES.get("Player1"),
						myGame.gameEnvironmentOrigin, CreatureType.PLAYER1, false);
			}
			
			/* Load the chat */
			myGame.chat = new Chat("Chat", myGame.chatOrigin, new Dimension(Game.CHAT_WIDTH,
					Game.CHAT_HEIGHT), myGame.container);
			
			/* Load Views - Dimension is specified in pixels */
			myGame.armorView = new ArmorView("ArmorInventory", myGame.armorViewOrigin,
					new Dimension(Game.ARMOR_WIDTH, Game.ARMOR_HEIGHT));
			
			myGame.gameEnvironment = new GameEnvironment("GameEnvironment",
					myGame.gameEnvironmentOrigin, new Dimension(Game.GAME_ENVIRONMENT_WIDTH,
							Game.GAME_ENVIRONMENT_HEIGHT), myGame.player, myGame.opponent, myGame.armorView, myGame, myGame.chat);
	
			myGame.minimap = new Minimap("Minimap", myGame.gameEnvironmentOrigin.x
					+ myGame.minimapOrigin.x, myGame.gameEnvironmentOrigin.y + myGame.minimapOrigin.y);
	
			myGame.inventoryView = InventoryView.getInstance("Inventory", myGame.inventoryViewOrigin,
					new Dimension(Game.INVENTORY_WIDTH, Game.INVENTORY_HEIGHT));
			
			/* Load Map and place the player */
			myGame.map = new Map().getInstance();
			myGame.map.setPlayer(myGame.player);
			myGame.map.setGameEnvironment(myGame.gameEnvironment);
			
			/* needs to be changed - only for testing purposes */
			myGame.map.setOpponent(myGame.opponent);
			// map.fillMap();
					
			myGame.fightInstance = myGame.gameEnvironment.getFightInstance();
						
			myGame.setLoading(false);
			
		} catch (SlickException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"Failed to load game.", e);
			System.exit(1);
		}
	}
}
