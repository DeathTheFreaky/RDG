package lobby;

import gameEssentials.Game;
import general.Main;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import lobby.Lobby.Scenes;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import at.RDG.network.NetworkManager;
import at.RDG.network.discovery.Serverinfo;

public class LobbyListener implements MouseListener {
	
	private NetworkManager network;
	private Lobby frame;
	
	private CreateLobbyThread createLobbyThread;
	private SearchLobbyThread searchLobbyThread;
	
	private JLabel instr;
	private JScrollPane scroll;
	
	public LobbyListener(Lobby frame) {
		// TODO Auto-generated constructor stub
		try {
			network = NetworkManager.getInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.frame = frame;
		
		instr = new JLabel(
				"<html><head></head><body><h2><u>Controls</u></h2><b>W</b> - North <br><b>A</b> - West <br><b>S</b> - South <br><b>D</b> - East <br><b>E</b> - Pick up item/start fight <br><b>Mouse</b> - Fighting/handling equipment <br><br><h2><u>Fight</u></h2><p>Fighting is mainly controlled by mouse. <br>Each round you can select one of these four choices:<br></p><h3>Attack</h3>	<p>Klicking Attack Opens a Submenu where you can choose, <br>	which of the enemy's body parts you want to attack. <br>	Different body parts have different effects on the enemy, <br>	such as slowing them down or lowering their accuracy. <br>	<br>	By pressing the ESC-Button you can return to the Menu. <br></p><h3>Force Parry</h3>	<p>Klicking Force Parry will force the enemy to parry your attack. <br>	Depending on different factors (mainly on your speed) you will either <br>	make bonus damage or forfeit your attack. <br></p><h3>Change Set</h3>	<p>By changing your weapon set you can adapt your equipment to your <br>	current enemy, but have to wait for next round to attack your opponent. <br></p><h3>Use Potion</h3>	<p>Using Potions can cause a massive advantage over your enemy, <br>	but at the cost of your attack. <br></p><br><h2><u>Equipment</u></h2><h3>Armor</h3>	<p>While all pieces of armor are better than your clothes at the beginning, <br>	some offer more protection at the cost of speed. When fighting against <br>	fast enemies, light armor is recommended, <br>	while heavy armor is recommended against big enemies. <br></p><h3>Weapons</h3>	<p>Small weapons like daggers are very fast and precise but do little damage, while <br>	big weapons, such as the greatsword, do heavy damage but are very slow and inaccurate. <br></p><h3>Potions</h3>	<p>Different Potions have different effects on you or your opponent. <br>	You can determine by the color what effect it will cause. <br>	Shiny bottles will give you some kind of bonus <br>	while dull will harm your enemy. <br></p><br><h2><u>Enemies</u></h2><h3>Small</h3>	<p>Small enemies are very fast, though do only little damage. <br></p><h3>Medium</h3>	<p>Medium enemies practice a good mix between speed and strength. <br></p><h3>Big</h3>	<p>Big Enemies can only be found in the treasury. <br>	These enemies are slow but deal heavy damage. <br></p><h3>Player</h3>	<p>Watch out! This guy will try to hunt you down! <br>	Winner of the fight wins the game! <br></p><br><h2><u>Treasury</u></h2><p>In the treasury there are party of heavy armor as well as potions, <br>but watch out, there is a big enemy too. <br><br></p></body></html>");
		scroll = new JScrollPane(instr);
		scroll.setPreferredSize(new Dimension(550, 600));
		
		createLobbyThread = new CreateLobbyThread();
		searchLobbyThread = new SearchLobbyThread(frame);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getComponent() instanceof JButton) {
			if(((JButton) e.getComponent()).getActionCommand() == "createLobby") {
				frame.switchScreen(Scenes.CREATED_LOBBY);
				new Thread(createLobbyThread).start();
			}else if(((JButton) e.getComponent()).getActionCommand() == "searchLobby") {
				new Thread(searchLobbyThread).start();
				//frame.showAllLobbies();
				frame.switchScreen(Scenes.SEARCH_LOBBY);
			}else if(((JButton) e.getComponent()).getActionCommand() == "instructions") {
				//frame.switchScreen(Scenes.INSTRUCTIONS);
				instructions();
			}else if(((JButton) e.getComponent()).getActionCommand() == "exit") {
				frame.leaveGame();
			}else if(((JButton) e.getComponent()).getActionCommand() == "return") {
				createLobbyThread.quit();
				searchLobbyThread.quit();
				frame.switchScreen(Scenes.MENU);
			}
		}
	}
	
	
	private void instructions() {
		JOptionPane.showMessageDialog(frame, scroll);
	}
	
	
	public static void createLobby() throws SlickException {
		System.out.println("Starting Game");
				
		AppGameContainer app1 = null;
		try {
			app1 = new AppGameContainer(Game.getInstance("Battle Dungeon Host"));
		} catch (IOException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"ServerSocket could not be created.", e);
			System.exit(1);
		} 	
		app1.setDisplayMode(Game.WIDTH, Game.HEIGHT, false); // Breite, Höhe, ???
		app1.setTargetFrameRate(30); // 60 Frames pro Sekunde
		app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
		app1.setShowFPS(false);
		app1.start(); // startet die App
	}
	
	private void searchLobbies() throws SlickException {
		/* set to true once a tcp connection between both parties has been established */
		
		System.out.println("Starting Game");
		
		AppGameContainer app1 = null;
		try {
			app1 = new AppGameContainer(Game.getInstance("Battle Dungeon Client"));
		} catch (IOException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"ServerSocket could not be created.", e);
			System.exit(1);
		} 
		app1.setDisplayMode(Game.WIDTH, Game.HEIGHT, false); // Breite, Höhe, ???
		app1.setTargetFrameRate(30); // 60 Frames pro Sekunde
		app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
		app1.setShowFPS(false);
		app1.start(); // startet die App
	}
	

}
