package at.RDG.network.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;

public class NetworkWriter extends Thread{
	
	private ObjectOutputStream oos;
	private Queue<NetworkMessage> writeQueue;
	private Socket socket;
	
	public NetworkWriter(Socket s, Queue<NetworkMessage> writeQueue) throws IOException{
		this.socket = s;
		this.oos = new ObjectOutputStream(this.socket.getOutputStream());
		this.writeQueue = writeQueue;
	}
	
	@Override
	public void run(){
		while(!Thread.interrupted()){
			while(!writeQueue.isEmpty()){
				try {
					this.oos.writeObject(this.writeQueue.poll());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
