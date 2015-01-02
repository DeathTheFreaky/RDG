package lobby;

import general.Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;

import at.RDG.network.ArgumentOutOfRangeException;
import at.RDG.network.NetworkManager;
import at.RDG.network.UnableToStartConnectionException;


public class LobbyThread implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		/* set to true once a tcp connection between both parties has been established */
		boolean startGame = false;
		
		/* store if this is the lobbyHost in networkManager */
		NetworkManager networkManager = null;
		try {
			networkManager = NetworkManager.getInstance();
			
			/* TEST: lobbyHost */
			networkManager.startLobby("Testlobby");
			
			while (startGame == false) {
				
				/* wait for client to establish connection */
				if (networkManager.isConnected()) {
					startGame = true;
					LobbyListener.createLobby();
					/* stop lobby once connection has been established */
					//networkManager.stopLobby();
				}
				Thread.sleep(100);
			}
			
		} catch (IOException | IllegalThreadStateException | ArgumentOutOfRangeException | UnableToStartConnectionException | InterruptedException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"Failed to establish network connection.", e);
			System.exit(1);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
