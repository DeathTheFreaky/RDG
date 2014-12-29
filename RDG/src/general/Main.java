package general;

import java.awt.Dimension;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import javax.swing.JFrame;

import lobby.Lobby;

import org.newdawn.slick.SlickException;

import at.RDG.network.ArgumentOutOfRangeException;
import at.RDG.network.NetworkManager;
import at.RDG.network.UnableToStartConnectionException;
import at.RDG.network.discovery.LobbySearcher;
import at.RDG.network.discovery.LobbyServer;
import at.RDG.network.discovery.Serverinfo;


public class Main {

	/**Starts the game and sets game parameters display mode, frame rate, always render, show fps.
	 * 
	 * @param args
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException {

		
	}
	
	private static void server(int count) {
		LobbyServer server = null;
		try {
			server = new LobbyServer("Neue Lobby " + count, 0);	// Stefan. Wusste nicht genau, wofür der int ist
		} catch (ArgumentOutOfRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.start();
	}

	private static void searcher() {
		Thread t = new Thread() {
			@Override
			public void run() {
				LinkedList<Serverinfo> lobbyList = new LinkedList<Serverinfo>();
				LobbySearcher search = new LobbySearcher(lobbyList);
				search.start();
				while (true) {
					try {
						Thread.sleep(1000);
						System.out.println("stopped waiting");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(int i = 0; i < lobbyList.size(); i++){
						Serverinfo info = lobbyList.get(i);
						System.out.println("Lobbyserver: "
								+ info.getLobbyName());
					}
				}
			}
		};

		t.start();
	}
}
