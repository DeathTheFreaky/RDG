package lobby;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import at.RDG.network.discovery.Serverinfo;

public class Lobby extends JFrame {
	
	public static List<Serverinfo> lobbies = new LinkedList<Serverinfo>();
	
	public enum Scenes {
		MENU, CREATED_LOBBY
	}
	
	private Container container;
	private LobbyListener listener;
	
	private JPanel menu;
	private JPanel createdLobby;
	private JPanel lobbyMenu;
	
	private JButton createLobby;
	private JButton searchLobby;
	private JButton instructions;
	private JButton exit;
	
	private JLabel wait;
	private JButton returnToMenu;

	public Lobby() {
		container = getContentPane();
		container.setLayout(new GridLayout(3, 6));
		
		menu = new JPanel();
		createdLobby = new JPanel();
		lobbyMenu = new JPanel();
		
		listener = new LobbyListener(this);
		
		createLobby = new JButton("Create Lobby");
		createLobby.setActionCommand("createLobby");
		createLobby.addMouseListener(listener);
		
		searchLobby = new JButton("Search Lobby");
		searchLobby.setActionCommand("searchLobby");
		searchLobby.addMouseListener(listener);
		
		instructions = new JButton("Instructions");
		instructions.setActionCommand("instructions");
		instructions.addMouseListener(listener);
		
		exit = new JButton("Leave Game");
		exit.setActionCommand("exit");
		exit.addMouseListener(listener);
		
		menu.add(createLobby);
		menu.add(searchLobby);
		menu.add(instructions);
		menu.add(exit);
		
		wait = new JLabel("Created Lobby.\n Wait for Opponent...");
		returnToMenu = new JButton("Quit");
		returnToMenu.setActionCommand("return");
		returnToMenu.addMouseListener(listener);
		
		createdLobby.add(wait);
		createdLobby.add(returnToMenu);
		createdLobby.setVisible(false);
		
		container.add(menu);
		container.add(createdLobby);
	}
	
	public void leaveGame() {
		int eingabe = JOptionPane.showConfirmDialog(container,
                "Do you really want to close the game?",
                "Leave Game?",
                JOptionPane.YES_NO_OPTION);
		System.out.println(eingabe);
		if(eingabe == 0) {	// Yes
			this.dispose();
		}
	}
	
	public void switchScreen(Scenes s) {
		if(menu.isShowing()) {
			menu.setVisible(false);
		}else if(createdLobby.isShowing()) {
			createdLobby.setVisible(false);
		}
		
		switch(s) {
		case CREATED_LOBBY:
			createdLobby.setVisible(true);
			break;
		case MENU:
			menu.setVisible(true);
			break;
		}
	}
}
