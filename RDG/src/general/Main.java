package general;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import gameEssentials.Game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import at.RDG.network.ArgumentOutOfRangeException;
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

		AppGameContainer app1 = new AppGameContainer(new Game("Battle Dungeon"));
		app1.setDisplayMode(Game.WIDTH, Game.HEIGHT, false); // Breite, Höhe, ???
		app1.setTargetFrameRate(30); // 60 Frames pro Sekunde
		app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
		app1.setShowFPS(false);
		app1.start(); // startet die App
	}
	
	private static void server(int count) {
		LobbyServer server = null;
		try {
			server = new LobbyServer("Neue Lobby " + count);
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
