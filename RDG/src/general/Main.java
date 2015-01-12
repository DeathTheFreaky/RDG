package general;

import java.awt.Dimension;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import lobby.Lobby;

import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import configLoader.Configloader;
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
		
		Lobby l =  new Lobby();
		l.setVisible(true);
		l.setResizable(false);
		l.setSize(new Dimension(400, 200));
	}
}
