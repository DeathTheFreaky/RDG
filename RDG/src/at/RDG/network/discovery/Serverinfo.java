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
	private String lobbyName;
	private String UID;
	
	Serverinfo(InetAddress address, String lobbyName){
		this.setAddress(address);
		this.lobbyName = lobbyName;
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
		return address;
	}

	void setAddress(InetAddress address) {
		this.address = address;
	}
	
	public String getLobbyName() {
		return lobbyName;
	}

	public String getUID() {
		return UID;
	}
}
