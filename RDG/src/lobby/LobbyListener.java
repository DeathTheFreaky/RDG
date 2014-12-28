package lobby;

import gameEssentials.Game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import lobby.Lobby.Scenes;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import at.RDG.network.ArgumentOutOfRangeException;
import at.RDG.network.NetworkManager;

public class LobbyListener implements MouseListener {
	
	private NetworkManager network;
	private Lobby frame;
	
	public LobbyListener(Lobby frame) {
		// TODO Auto-generated constructor stub
		network = NetworkManager.getInstance();
		this.frame = frame;
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
				System.out.println("createLobby");
				frame.switchScreen(Scenes.CREATED_LOBBY);
				/*try {
					network.startLobby("MyLobby");
				} catch (ArgumentOutOfRangeException e1) {
					// TODO Auto-generated catch block
					System.out.println("Couldn't create Lobby");
					e1.printStackTrace();
				}*/
			}else if(((JButton) e.getComponent()).getActionCommand() == "searchLobby") {
				System.out.println("searchLobby");
				network.searchLobby(Lobby.lobbies);
			}else if(((JButton) e.getComponent()).getActionCommand() == "instructions") {
				System.out.println("instructions");
			}else if(((JButton) e.getComponent()).getActionCommand() == "exit") {
				System.out.println("exit");
				frame.leaveGame();
			}else if(((JButton) e.getComponent()).getActionCommand() == "return") {
				System.out.println("return");
				frame.switchScreen(Scenes.MENU);
			}
		}
	}

}
