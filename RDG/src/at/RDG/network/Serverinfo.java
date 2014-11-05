package at.RDG.network;

import java.net.InetAddress;

public class Serverinfo {
	
	public final InetAddress address;
	public final String lobbyName;
	
	public Serverinfo(InetAddress address, String lobbyName){
		this.address = address;
		this.lobbyName = lobbyName;
	}
}
