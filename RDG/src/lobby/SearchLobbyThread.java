package lobby;

import general.Main;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.RDG.network.NetworkManager;
import at.RDG.network.discovery.Serverinfo;

public class SearchLobbyThread implements Runnable {
	
	private Lobby frame;
	
	public SearchLobbyThread(Lobby l) {
		this.frame = l;
	}
	
	private boolean quit;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		quit = false;
		
		/* store if this is the lobbyHost in networkManager */
		NetworkManager networkManager = null;
		try {
			networkManager = NetworkManager.getInstance();
			
			networkManager.searchLobby(Lobby.lobbies);
			
			int j = 0;
			while(j < 50) {
				Thread.sleep(100);
				if(this.quit)
					break;
				j++;
			}
			
			networkManager.stopSearchLobby();
			
			frame.showAllLobbies();
			/*for(Serverinfo s : Lobby.lobbies) {
				System.out.println("Lobby: " + s.getLobbyName());
			}*/
			
		} catch (Exception e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"Failed to establish network connection.", e);
			System.exit(1);
		}
		
		System.out.println("Starting Game");
	}

	public void quit() {
		this.quit = true;
	}
	
}
