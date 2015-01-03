package lobby;

import gameEssentials.Game;
import general.Main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class NewGame implements Runnable {

	@Override
	public void run() {
		
		String[] options = { "OK" };
		JPanel panel = new JPanel();
		JLabel lbl = new JLabel("Enter Player Name: ");
		JTextField txt = new JTextField(10);
		panel.add(lbl);
		panel.add(txt);
		JOptionPane.showOptionDialog(null, panel, "Player Name",
				JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		String playerName = txt.getText();
		
		
		// TODO Auto-generated method stub
		AppGameContainer app1 = null;
		try {
			app1 = new AppGameContainer(Game.getInstance("Battle Dungeon", playerName));
			app1.setDisplayMode(Game.WIDTH, Game.HEIGHT, false); // Breite, Höhe, ???
			app1.setTargetFrameRate(30); // 60 Frames pro Sekunde
			app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
			app1.setShowFPS(false);
			app1.setForceExit(false);
			app1.start();
		} catch (IOException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"ServerSocket could not be created.", e);
			System.exit(1);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
