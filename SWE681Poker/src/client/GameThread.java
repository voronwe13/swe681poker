package client;

import org.eclipse.swt.widgets.Display;

public class GameThread implements Runnable {
	LoginInterface clientint;
	boolean active;
	Display display;
	PokerClient client;
	
	public GameThread(LoginInterface clientinterface){
		clientint = clientinterface;
		active = true;
		display = clientint.display;
		client = clientint.client;
	}
	
	@Override
	public void run() {
		
		
		while(active){
			String command = client.getCommand();
			switch(command){
			case "update": clientint.showGame();
						break;
						
			}
		}
	}

	public void quit() {
		active = false;
		
	}

}
