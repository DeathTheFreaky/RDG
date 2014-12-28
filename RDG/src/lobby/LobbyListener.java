package lobby;

import gameEssentials.Game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

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
		try {
			network = NetworkManager.getInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				frame.switchScreen(Scenes.CREATED_LOBBY);
			}else if(((JButton) e.getComponent()).getActionCommand() == "searchLobby") {
				network.searchLobby(Lobby.lobbies);
			}else if(((JButton) e.getComponent()).getActionCommand() == "instructions") {
			}else if(((JButton) e.getComponent()).getActionCommand() == "exit") {
				frame.leaveGame();
			}else if(((JButton) e.getComponent()).getActionCommand() == "return") {
				frame.switchScreen(Scenes.MENU);
			}
		}
	}

}
