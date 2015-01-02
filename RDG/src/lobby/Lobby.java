package lobby;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import at.RDG.network.discovery.Serverinfo;

public class Lobby extends JFrame {

	public static List<Serverinfo> lobbies = new LinkedList<Serverinfo>();

	public enum Scenes {
		MENU, CREATED_LOBBY, SEARCH_LOBBY, INSTRUCTIONS
	}

	private GridBagConstraints constraints;

	private Container container;
	private LobbyListener listener;

	private JPanel menu;
	private JPanel createdLobby;
	private JPanel lobbyMenu;
	private JPanel searchingLobby;
	private JPanel instructionSite;

	private JButton createLobby;
	private JButton searchLobby;
	private JButton instructions;
	private JButton exit;

	private JLabel wait;
	private JLabel allLobbies;
	private JButton returnToMenu;
	private JButton returnToMenu2;
	private JLabel instr;

	private JScrollPane scroll;
	private JListModel list;
	private JTable table;
	private JScrollPane scrollPane;

	public Lobby() {
		container = getContentPane();
		container.setLayout(new GridLayout(1, 1));

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;

		menu = new JPanel();
		menu.setLayout(new GridLayout(4, 0));
		createdLobby = new JPanel();
		lobbyMenu = new JPanel();
		searchingLobby = new JPanel();
		searchingLobby.setLayout(new GridBagLayout());
		instructionSite = new JPanel();

		listener = new LobbyListener(this);

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

		wait = new JLabel("Created Lobby.\n Wait for Opponent...");
		returnToMenu = new JButton("Quit");
		returnToMenu.setActionCommand("return");
		returnToMenu.addMouseListener(listener);

		createdLobby.add(wait);
		createdLobby.add(returnToMenu);
		createdLobby.setVisible(false);

		allLobbies = new JLabel("Erstellte Lobbies:");
		searchingLobby.add(allLobbies, constraints);
		searchingLobby.setVisible(false);
		scroll = new JScrollPane();
		constraints.gridx = 0;
		constraints.gridy = 1;
		searchingLobby.add(scroll, constraints);
		returnToMenu2 = new JButton("Return");
		returnToMenu2.setActionCommand("return");
		returnToMenu2.addMouseListener(listener);
		constraints.gridx = 0;
		constraints.gridy = 2;
		searchingLobby.add(returnToMenu2, constraints);
		table = new JTable();
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setBounds(30, 30, 240, 150);
		scroll.add(table);

		instructionSite.setVisible(false);
		

		container.add(menu);
		container.add(createdLobby);
		container.add(searchingLobby);
		container.add(instructionSite);
		
		container.revalidate();
		this.pack();
	}

	public void leaveGame() {
		int eingabe = JOptionPane.showConfirmDialog(container,
				"Do you really want to close the game?", "Leave Game?",
				JOptionPane.YES_NO_OPTION);
		if (eingabe == 0) { // Yes
			this.dispose();
		}
	}

	public void switchScreen(Scenes s) {
		if (menu.isShowing()) {
			menu.setVisible(false);
			//container.remove(menu);
		} else if (createdLobby.isShowing()) {
			createdLobby.setVisible(false);
		} else if (searchingLobby.isShowing()) {
			searchingLobby.setVisible(false);
		} else if (instructions.isShowing()) {
			instructions.setVisible(false);
		}

		switch (s) {
		case CREATED_LOBBY:
			createdLobby.setVisible(true);
			//container.add(createdLobby);
			break;
		case MENU:
			menu.setVisible(true);
			break;
		case SEARCH_LOBBY:
			searchingLobby.setVisible(true);
			break;
		case INSTRUCTIONS:
			instructionSite.setVisible(true);
			break;
		}
	}

	public void showAllLobbies() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			model.removeRow(i);
		}
		for (Serverinfo s : lobbies) {
			model.addColumn(s.getLobbyName(), new Object[] { s.getLobbyName() });
		}
		table.revalidate();
	}
}
