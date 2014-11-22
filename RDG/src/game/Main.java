package game;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import at.RDG.network.ArgumentOutOfRangeException;
import at.RDG.network.discovery.LobbySearcher;
import at.RDG.network.discovery.LobbyServer;
import at.RDG.network.discovery.Serverinfo;
import config_templates.Armament_Template;
import config_templates.Attack_Template;
import config_templates.Monster_Template;
import config_templates.Potion_Template;
import config_templates.Room_Template;
import config_templates.Weapon_Template;

public class Main {

	public static void main(String[] args) {
		//server(Integer.parseInt(args[0]));
		searcher();
		
		/*
		//path to config files
		String configpath = "config/Results/";
		
		//create instance of configloader
		Configloader configloader = new Configloader(configpath);
		
		//running configloader must be successful in order for program to continue
		try {
			configloader.run();
		} catch (IllegalArgumentException | ParserConfigurationException | SAXException | IOException e){
			e.printStackTrace();
			System.err.println("\nParsing Configuration Files failed\nExiting program\n");
			System.exit(1);
		}
		
		//Test Printing
		Config_Testprinter configprinter = new Config_Testprinter(configloader);
		configprinter.print();
		*/
	}
	
	private static void server(int count){
		LobbyServer server = null;
		try {
			server = new LobbyServer(1024, "Neue Lobby " + count);
		} catch (ArgumentOutOfRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.start();
	}
	
	private static void searcher(){
		Thread t = new Thread(){
			@Override
			public void run(){
				LinkedList<Serverinfo> lobbyList = new LinkedList<Serverinfo>();
				LobbySearcher search = new LobbySearcher(1025, lobbyList);
				search.start();
				while(true){
					try {
						synchronized(search){
							search.wait();
							System.out.println("stopped waiting");
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(Serverinfo info : lobbyList){
						System.out.println("Lobbyserver: " + info.lobbyName);
					}
				}
			}
		};
		
		t.start();
	}
}
