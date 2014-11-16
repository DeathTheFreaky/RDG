package general;

import gameEssentials.Game;

import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import configLoader.ArmamentTemplate;
import configLoader.AttackTemplate;
import configLoader.ConfigTestprinter;
import configLoader.Configloader;
import configLoader.MonsterTemplate;
import configLoader.PotionTemplate;
import configLoader.RoomTemplate;
import configLoader.WeaponTemplate;

public class Main {

	/**Starts the game and sets game parameters display mode, frame rate, always render, show fps.
	 * 
	 * @param args
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException {

		AppGameContainer app1 = new AppGameContainer(new Game("Battle Dungeon"));
		app1.setDisplayMode(Game.WIDTH, Game.HEIGHT, false); // Breite, H�he, ???
		app1.setTargetFrameRate(60); // 60 Frames pro Sekunde
		app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
		app1.setShowFPS(false);
		app1.start(); // startet die App
	}
}
