package game;

import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import worthy.Game;

import configTemplates.ArmamentTemplate;
import configTemplates.AttackTemplate;
import configTemplates.MonsterTemplate;
import configTemplates.PotionTemplate;
import configTemplates.RoomTemplate;
import configTemplates.WeaponTemplate;

public class Main {

	/*public static void main(String[] args) {
		
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
		ConfigTestprinter configprinter = new ConfigTestprinter(configloader);
		configprinter.print();
		
	}*/
	
	/**Start the game and set game parameters display mode, frame rate, always render, show fps.
	 * 
	 * @param args
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException {

		AppGameContainer app1 = new AppGameContainer(new Game("Battle Dungeon"));
		app1.setDisplayMode(Game.WIDTH, Game.HEIGHT, false); // Breite, Höhe, ???
		app1.setTargetFrameRate(60); // 60 Frames pro Sekunde
		app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
		app1.setShowFPS(false);
		app1.start(); // startet die App
	}
}
