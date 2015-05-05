package client;

import org.eclipse.swt.widgets.Display;

import swe681poker.Card;

public class GameThread implements Runnable {
	LoginInterface clientint;
	boolean active;
	Display display;
	PokerClient client;
	private int bid;
	
	public GameThread(LoginInterface clientinterface){
		clientint = clientinterface;
		active = true;
		display = clientint.display;
		client = clientint.client;
		bid = 0;
	}
	
	@Override
	public void run() {
		
		
		while(active){
			String command = client.getCommand();
			switch(command){
			case "update": clientint.showGame();
						break;
			case "getbid": client.sendBid(bid);
						clientint.setBid(0);
						bid = 0;
			case "sethand": Card[] cards = client.getHand();
							clientint.setHand(cards);
			}
		}
	}

	public void quit() {
		active = false;
	}
	
	public void setBid(int bid){
		this.bid = bid;
	}

}
