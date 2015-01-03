package lobby;

import gameEssentials.Game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.newdawn.slick.SlickException;

import at.RDG.network.NetworkManager;
import at.RDG.network.UnableToStartConnectionException;
import at.RDG.network.discovery.Serverinfo;

public class Lobby extends JFrame {

	public static List<Serverinfo> lobbies = new LinkedList<Serverinfo>();
	public static String lobbyName = "DefaultLobbyName";
	
	/* prevent user from clicking create lobby or searching lobby while game is running */
	public static boolean gameRunning = false;
	
	/* only ask for playerName when double click is on a lobby */
	public static boolean lobbiesShown = false;
	
	private static NetworkManager network;

	public enum Scenes {
		MENU, CREATED_LOBBY, SEARCH_LOBBY, INSTRUCTIONS
	}

	private Container container;
	private LobbyListener listener;

	private JPanel menu;
	private JPanel createdLobby;
	private JPanel searchingLobby;

	private JButton createLobby;
	private JButton searchLobby;
	private JButton instructions;
	private JButton exit;

	private JLabel wait;
	private JLabel allLobbies;
	private JButton returnToMenu;
	private JButton returnToMenu2;
	private JScrollPane scroll;
	private static JTable table;
	public Lobby() {
		try {
			network = NetworkManager.getInstance();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() { 
            public void windowClosing(WindowEvent event1) { 
            	System.out.println("S");
            } 
        }); 
		
		container = getContentPane();
		container.setLayout(new CardLayout());

		listener = new LobbyListener(this);

		menu = new JPanel();
		menu.setLayout(new GridLayout(4, 0));

		createLobby = new JButton("Create Lobby");
		createLobby.setActionCommand("createLobby");
		createLobby.addMouseListener(listener);
		createLobby.setPreferredSize(new Dimension(150, 30));

		searchLobby = new JButton("Search Lobby");
		searchLobby.setActionCommand("searchLobby");
		searchLobby.addMouseListener(listener);
		searchLobby.setPreferredSize(new Dimension(150, 30));

		instructions = new JButton("Instructions");
		instructions.setActionCommand("instructions");
		instructions.addMouseListener(listener);
		instructions.setPreferredSize(new Dimension(150, 30));

		exit = new JButton("Leave Game");
		exit.setActionCommand("exit");
		exit.addMouseListener(listener);
		exit.setPreferredSize(new Dimension(150, 30));

		menu.add(createLobby);
		menu.add(searchLobby);
		menu.add(instructions);
		menu.add(exit);

		createdLobby = new JPanel();

		wait = new JLabel("Created Lobby.\n Wait for Opponent...");
		returnToMenu = new JButton("Quit");
		returnToMenu.setActionCommand("return");
		returnToMenu.addMouseListener(listener);

		createdLobby.add(wait);
		createdLobby.add(returnToMenu);

		searchingLobby = new JPanel();
		searchingLobby.setLayout(new BorderLayout());
		allLobbies = new JLabel("Erstellte Lobbies:");
		returnToMenu2 = new JButton("Return");
		returnToMenu2.setActionCommand("return");
		returnToMenu2.addMouseListener(listener);
		table = new JTable();
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setBounds(30, 30, 240, 150);
		DefaultTableModel tableModel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};
		table.setModel(tableModel);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						try {
							
							if (lobbiesShown) {
								String[] options = { "OK" };
								JPanel panel = new JPanel();
								JLabel lbl = new JLabel("Enter Player Name: ");
								JTextField txt = new JTextField(10);
								panel.add(lbl);
								panel.add(txt);
								int returnVal = JOptionPane.showOptionDialog(null, panel, "Player Name",
										JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
										options, options[0]);
								String playerName;
								
								if (returnVal != JOptionPane.CLOSED_OPTION) {
									
									playerName = txt.getText();
									
									if (playerName.equals("")) {
										playerName = "Default Player";
									}
									
									network.connect(lobbies.get(table.getSelectedRow()));
									
									startClient(playerName);
								}
							}
							
						} catch (UnableToStartConnectionException e1) {
							e1.printStackTrace();
						}
					} catch (SlickException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		resetTable();
		scroll = new JScrollPane(table);
		searchingLobby.add(allLobbies, BorderLayout.PAGE_START);
		searchingLobby.add(scroll, BorderLayout.CENTER);
		searchingLobby.add(returnToMenu2, BorderLayout.PAGE_END);

		container.add(menu);
		container.add(createdLobby);
		container.add(searchingLobby);

		container.revalidate();
		this.pack();
	}

	public void leaveGame() {
		int eingabe = JOptionPane.showConfirmDialog(container,
				"Do you really want to close the game?", "Leave Game?",
				JOptionPane.YES_NO_OPTION);
		if (eingabe == 0) { // Yes
			try {
				Game.getInstance().quitGame();
			} catch (NullPointerException e) {
				//game was not running
			}
			this.dispose();
			System.exit(0); //actually close all open windows by exiting jvm
		}
	}

	public String switchScreen(Scenes s) {
		CardLayout cl = (CardLayout) container.getLayout();
		String retString = null;

		switch (s) {
		case CREATED_LOBBY:
			String[] options = { "OK" };
			JPanel panel = new JPanel();
			JLabel lbl = new JLabel("Enter Lobby Name: ");
			JTextField txt = new JTextField(10);
			panel.add(lbl);
			panel.add(txt);

		    int returnVal = JOptionPane.showOptionDialog(null, panel, "Lobby Name",
					JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
		    
		    if (returnVal != JOptionPane.CLOSED_OPTION) {
		    	lobbyName = txt.getText();
				if (lobbyName.equals("")) {
					lobbyName = "Default Lobby";
				}
				
				String[] optionsP = { "OK" };
				JPanel panelP = new JPanel();
				JLabel lblP = new JLabel("Enter Player Name: ");
				JTextField txtP = new JTextField(10);
				panelP.add(lblP);
				panelP.add(txtP);
				int returnValP = JOptionPane.showOptionDialog(null, panelP, "Player Name",
						JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						optionsP, optionsP[0]);
				String playerName;
				
				if (returnValP != JOptionPane.CLOSED_OPTION) {
					
					playerName = txtP.getText();
					
					if (playerName.equals("")) {
						playerName = "Default Player";
					}
					
					retString = playerName;
										
					cl.next(container);
				}
    	    }
		    
			break;
		case MENU:
			cl.first(container);
			break;
		case SEARCH_LOBBY:
			cl.last(container);
			break;
		}
		
		return retString;
	}

	public void showAllLobbies() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			model.removeRow(i);
		}
		
		for (Serverinfo s : lobbies) {
			model.addRow(new Object[] { s.getLobbyName(), s.getAddress().toString().replace("/", "") });
		}
		if (model.getRowCount() == 0) {
			model.addRow(new Object[] { "There are no Lobbies!" });
			lobbiesShown = false;
		} else {
			lobbiesShown = true;
		}

		this.revalidate();
	}
	
	public void showSearchingLobbies() {
		lobbiesShown = false;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			model.removeRow(i);
		}
		model.addRow(new Object[] { "Searching Lobbies..." });

		this.revalidate();
	}
	
	public static void showLobbyClosed() {
		lobbiesShown = false;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			model.removeRow(i);
		}
		model.addRow(new Object[] { "Lobby closed in the meantime..." });
	}

	private void resetTable() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			model.removeRow(i);
		}
		model.addColumn("", new Object[] { "Searching for Lobbies..." });
		model.addColumn("", new Object[] { "" });
	}
	
	public void startClient(String playerName) throws SlickException {
		this.switchScreen(Scenes.MENU);
		//Lobby.this.setVisible(false);
		
		new Thread(new NewGame(playerName)).start();
	}

	/**At the end of game, quit the connection and set gameRunning to false.
	 * 
	 */
	public static void quitConnection() {
		System.out.println("quitting connection");
		Lobby.gameRunning = false;
		network.stopConnection();
		System.out.println("Am i connected after quitting connection? " + network.isConnected());
	}
}
