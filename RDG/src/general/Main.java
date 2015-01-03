package general;

import java.awt.Dimension;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import javax.swing.JFrame;

import lobby.Lobby;

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

		Lobby l =  new Lobby();
		l.setVisible(true);
		l.setResizable(false);
		l.setSize(new Dimension(400, 200));
	}
}
