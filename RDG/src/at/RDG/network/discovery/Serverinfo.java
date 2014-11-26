package at.RDG.network.discovery;

import java.io.Serializable;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Serverinfo implements Serializable{
	
	private static final long serialVersionUID = 2153567303370864667L;
	private InetAddress address;
	private int port;
	private String lobbyName;
	private String UID;
	
	Serverinfo(InetAddress address,int port, String lobbyName){
		this.address = address;
		this.lobbyName = lobbyName;
		this.port = port;
		MessageDigest md = null;
		byte[] thedigest = null;
		try {
			md = MessageDigest.getInstance("MD5");
			thedigest = md.digest((this.lobbyName + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.UID = new String(thedigest);
	}

	public InetAddress getAddress() {
		return this.address;
	}

	void setAddress(InetAddress address) {
		this.address = address;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public String getLobbyName() {
		return this.lobbyName;
	}

	public String getUID() {
		return this.UID;
	}
}
