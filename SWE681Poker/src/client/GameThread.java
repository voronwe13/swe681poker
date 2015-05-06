package client;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.widgets.Display;

import swe681poker.Card;

public class GameThread implements Runnable {
	LoginInterface clientint;
	boolean active;
	PokerClient client;
	private int bid;
	public int minbid;
	Object monitor;
	boolean ready;
	Queue<String> commandqueue;
	
	public GameThread(LoginInterface clientinterface){
		clientint = clientinterface;
		active = true;
		client = clientint.client;
		bid = 0;
		commandqueue = new LinkedList<String>();
		monitor = new Object();
	}
	
	@Override
	public void run() {
		
		
		while(active){
			while(!commandqueue.isEmpty()){
				String command = commandqueue.remove();
				switch(command){
				case "setbid": client.sendBid(bid);
							clientint.setBid(0);
							bid = 0;
				}
			}
			if(client.checkUpdate()){
				System.out.println("about to tell interface to update...");
				ready = false;
				clientint.update();
				while(!ready){
					synchronized(monitor){
						try {
							monitor.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				System.out.println("(in gamethread: finished update");
			}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void quit() {
		active = false;
	}
	
	public void setBid(int bid){
		this.bid = bid;
		commandqueue.add("setbid");
	}
	
	public void updateFinished(){
		System.out.println("notifying thread that update is finished");
		synchronized(monitor){
			ready = true;
			monitor.notifyAll();
		}
	}

}
