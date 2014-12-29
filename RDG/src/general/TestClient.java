package general;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import gameEssentials.Game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import at.RDG.network.ArgumentOutOfRangeException;
import at.RDG.network.NetworkManager;
import at.RDG.network.UnableToStartConnectionException;
import at.RDG.network.discovery.LobbySearcher;
import at.RDG.network.discovery.LobbyServer;
import at.RDG.network.discovery.Serverinfo;


public class TestClient {

	/**Starts the game and sets game parameters display mode, frame rate, always render, show fps.
	 * 
	 * @param args
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException {
		
		/* set to true once a tcp connection between both parties has been established */
		boolean startGame = false;
		int tryConnCtr = 0;
		int tryConnMax = 100;

		/* store if this is the lobbyHost in networkManager */
		NetworkManager networkManager = null;
		try {
			networkManager = NetworkManager.getInstance();
			
			/* TEST: lobbyclient -> start server before!!! */
			List<Serverinfo> lobbyList = new LinkedList<Serverinfo>();
			networkManager.searchLobby(lobbyList);
			
			while(lobbyList.size() == 0) {
				lobbyList = networkManager.getLobbyList();
			}
			
			networkManager.stopSearchLobby();
			
			System.out.println(lobbyList.get(0).getLobbyName());
			System.out.println(lobbyList.get(0).getPort());
			
			while (startGame == false && tryConnCtr < tryConnMax) {
				
				networkManager.connect(lobbyList.get(0));
				
				/* wait for client to establish connection */
				if (networkManager.isConnected()) {
					startGame = true;
				}
				Thread.sleep(100);
				
				tryConnCtr++;
			}
			
			if (startGame == false) {
				throw new Exception("Failed to establish connection with lobby Host");
			}
			
			System.out.println("connection established!");
			
		} catch (Exception e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"Failed to establish network connection.", e);
			System.exit(1);
		}
		
		AppGameContainer app1 = null;
		try {
			app1 = new AppGameContainer(Game.getInstance("Battle Dungeon"));
		} catch (IOException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"ServerSocket could not be created.", e);
			System.exit(1);
		} 
		app1.setDisplayMode(Game.WIDTH, Game.HEIGHT, false); // Breite, Höhe, ???
		app1.setTargetFrameRate(30); // 60 Frames pro Sekunde
		app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
		app1.setShowFPS(false);
		app1.start(); // startet die App
	}
}
