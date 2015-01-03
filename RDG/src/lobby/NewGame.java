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
	
	public NewGame(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public void run() {
					
		// TODO Auto-generated method stub
		AppGameContainer app1 = null;
		try {
			Lobby.gameRunning = true;
			app1 = new AppGameContainer(Game.getInstance("Battle Dungeon", playerName));
			app1.setDisplayMode(Game.WIDTH, Game.HEIGHT, false); // Breite, Höhe, ???
			app1.setTargetFrameRate(30); // 60 Frames pro Sekunde
			app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
			app1.setShowFPS(false);
			app1.setForceExit(false);
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
		
	}
}
