package at.RDG.network.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Stack;

public class NetworkWriter extends Thread{
	
	private ObjectOutputStream oos;
	private Stack<NetworkMessage> writeList;
	private Socket socket;
	
	public NetworkWriter(Socket s, Stack<NetworkMessage> writeList) throws IOException{
		this.socket = s;
		this.oos = new ObjectOutputStream(this.socket.getOutputStream());
		this.writeList = writeList;
	}
	
	@Override
	public void run(){
		while(!Thread.interrupted()){
			NetworkMessage msg;
			while((msg = writeList.pop()) != null){
				
			}
		}
	}
	
	public void send(NetworkMessage msg){
		//this.oos.writeObject(msg);
	}
}
