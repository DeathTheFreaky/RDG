package at.RDG.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public class LobbyServer extends Thread {
	
	private int port;
	private final String lobbyName;
	
	public LobbyServer(int port, String lobbyName) throws ArgumentOutOfRangeException{
		if(lobbyName.length() > NetworkStatics.LOBBYNAMEMAXLENGTH){
			throw new ArgumentOutOfRangeException("lobbyName cannot be more then 256 characters long!");
		}
		this.port = port;
		this.lobbyName = lobbyName;
	}

	@Override
	public void run() {
		//open MulticastSocket (UDP)
		MulticastSocket socket = null;
		InetAddress group = null;
		try {
			//socket = new MulticastSocket(new InetSocketAddress(NetworkStatics.IP, this.port));
			socket = new MulticastSocket(this.port);
			group = InetAddress.getByName(NetworkStatics.GROUPNAME);
			System.out.println(group.getHostAddress());
			socket.joinGroup(group);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(!socket.isBound() || group == null){
				Thread.currentThread().interrupt();
				//TODO error msg and logging
			}
		}
		
		try {
			System.out.println(socket.getInterface());
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(socket.getLocalAddress());
		//receive and answer Requests
		DatagramPacket packet;
		while(!Thread.interrupted()){
			byte[] buf = new byte[1];
		    packet = new DatagramPacket(buf, buf.length);
		    try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    if(packet.getData()[0] == 7){
		    	System.out.println("recved");
		    	DatagramPacket sendPacket = new DatagramPacket(this.lobbyName.getBytes(), this.lobbyName.getBytes().length, packet.getAddress(), packet.getPort());
		    	try {
					socket.send(sendPacket);
					System.out.println("sended");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		
		//close Socket when interrupted
		try {
			socket.leaveGroup(group);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
	}
	
}
