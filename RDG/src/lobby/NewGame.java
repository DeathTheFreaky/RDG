package lobby;

import gameEssentials.Game;
import general.Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class NewGame implements Runnable {
	
	private String playerName = null;
	private Lobby lobby;
	
	public NewGame(String playerName, Lobby l) {
		this.playerName = playerName;
		this.lobby = l;
	}

	@Override
	public void run() {
					
		// TODO Auto-generated method stub
		AppGameContainer app1 = null;
		try {
			System.out.println("lobby starting new game");
			Lobby.gameRunning = true;
			app1 = new AppGameContainer(Game.getInstance("Room Duelling Game", playerName));
			app1.setDisplayMode(Game.WIDTH, Game.HEIGHT, false); // Breite, Höhe, ???
			app1.setTargetFrameRate(30); // 60 Frames pro Sekunde
			app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
			app1.setShowFPS(false);
			app1.setForceExit(false);
			System.out.println("lobby just before game start");
			app1.start();
			Lobby.gameRunning = false;
		} catch (IOException e) {
			Lobby.gameRunning = false;
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"ServerSocket could not be created.", e);
			System.exit(1);
		} catch (SlickException e) {
			Lobby.gameRunning = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		lobby.setVisible(true);
	}
}
