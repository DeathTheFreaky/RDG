package at.RDG.network.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;

public class NetworkWriter extends Thread{
	
	private ObjectOutputStream oos;
	private Queue<NetworkMessage> writeQueue;
	private Socket s;
	
	public NetworkWriter(Socket s, Queue<NetworkMessage> writeQueue) throws IOException{
		this.s = s;
		this.oos = new ObjectOutputStream(s.getOutputStream());
		this.writeQueue = writeQueue;
	}
	
	@Override
	public void run(){
		while(!Thread.interrupted()){
			while(!writeQueue.isEmpty()){
				try {
					this.oos.writeObject(this.writeQueue.poll());
					this.oos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			this.s.close();
		} catch (IOException e) {}
	}
}
