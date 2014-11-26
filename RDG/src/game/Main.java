package game;

import java.util.LinkedList;

import at.RDG.network.ArgumentOutOfRangeException;
import at.RDG.network.discovery.LobbySearcher;
import at.RDG.network.discovery.LobbyServer;
import at.RDG.network.discovery.Serverinfo;

public class Main {

	public static void main(String[] args) {
		//server(Integer.parseInt(args[0]));
		searcher();

		/*
		 * //path to config files String configpath = "config/Results/";
		 * 
		 * //create instance of configloader Configloader configloader = new
		 * Configloader(configpath);
		 * 
		 * //running configloader must be successful in order for program to
		 * continue try { configloader.run(); } catch (IllegalArgumentException
		 * | ParserConfigurationException | SAXException | IOException e){
		 * e.printStackTrace(); System.err.println(
		 * "\nParsing Configuration Files failed\nExiting program\n");
		 * System.exit(1); }
		 * 
		 * //Test Printing Config_Testprinter configprinter = new
		 * Config_Testprinter(configloader); configprinter.print();
		 */
	}

	private static void server(int count) {
		LobbyServer server = null;
		try {
			server = new LobbyServer("Neue Lobby " + count);
		} catch (ArgumentOutOfRangeException e) {
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
