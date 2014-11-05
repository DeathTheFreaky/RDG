package at.RDG.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class LobbySearcher extends Thread {

	private int port;
	private LinkedList<Serverinfo> lobbyList;
	
	public LobbySearcher(int port, LinkedList<Serverinfo> lobbyList){
		this.port = port;
		this.lobbyList = lobbyList;
	}
	
	@Override
	public void run(){
		DatagramSocket socket = null;
		InetAddress group = null;
		try {
			//socket = new DatagramSocket(new InetSocketAddress(NetworkStatics.IP, this.port));
			socket = new DatagramSocket(this.port);
			group = InetAddress.getByName(NetworkStatics.GROUPNAME);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(!socket.isBound() || group == null){
				Thread.currentThread().interrupt();
				//TODO error msg and logging
			}
		}
		
		System.out.println(socket.getLocalAddress());
		System.out.println(socket.getInetAddress());
		System.out.println(socket.getLocalSocketAddress());
		DatagramPacket sendPacket = new DatagramPacket(new byte[] {(byte) 7}, 1, group, 1024);
    	try {
			socket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	DatagramPacket packet;
    	while(!Thread.interrupted()){
    		byte[] buf = new byte[NetworkStatics.LOBBYNAMEMAXLENGTH];
		    packet = new DatagramPacket(buf, buf.length);
		    try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    this.lobbyList.add(new Serverinfo(packet.getAddress(), new String(packet.getData())));
		    System.out.println("recived");
		    synchronized(this){
		    	this.notify();
		    }
    	}
    	
    	socket.close();
	}
	
}
