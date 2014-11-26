package at.RDG.network.discovery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedList;

import at.RDG.network.NetworkStatics;

public class LobbySearcher extends Thread {

	private int port;
	private LinkedList<Serverinfo> lobbyList;

	public LobbySearcher(int port, LinkedList<Serverinfo> lobbyList) {
		this.port = port;
		this.lobbyList = lobbyList;
	}

	@Override
	public void run() {
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket(this.port);
			socket.setBroadcast(true);
			socket.setTimeToLive(10);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (!socket.isBound()/* || group == null */) {
				Thread.currentThread().interrupt();
				// TODO error msg and logging
			}
		}

		DatagramPacket sendPacket = null;
		try {
			sendPacket = new DatagramPacket(new byte[] { (byte) 7 }, 1,
					InetAddress.getByName("255.255.255.255"), 1024);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			socket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Broadcast the message over all the network interfaces
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			try {
				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue; // Don't want to broadcast to the loopback
								// interface
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (InterfaceAddress interfaceAddress : networkInterface
					.getInterfaceAddresses()) {
				InetAddress broadcast = interfaceAddress.getBroadcast();
				if (broadcast == null) {
					continue;
				}

				// Send the broadcast package!
				try {
					sendPacket = new DatagramPacket(new byte[] { (byte) 7 }, 1,
							broadcast, 1024);
					socket.send(sendPacket);
				} catch (Exception e) {
				}

				System.out.println(getClass().getName()
						+ ">>> Request packet sent to: "
						+ broadcast.getHostAddress() + "; Interface: "
						+ networkInterface.getDisplayName());
			}
		}

		DatagramPacket packet;
		while (!Thread.interrupted()) {
			byte[] buf = new byte[NetworkStatics.LOBBYNAMEMAXLENGTH*2];
			packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
			ObjectInputStream ios = null;
			Object obj = null;
			try {
				ios = new ObjectInputStream(bis);
				obj = ios.readObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!(obj instanceof Serverinfo)){
				continue;
			}
			Serverinfo info = (Serverinfo)obj;
			info.setAddress(packet.getAddress());
			
			this.lobbyList.add(info);
		}

		socket.close();
	}

}
