package server;

import java.util.List;

import swe681poker.CardDeck;

public class PokerTable implements Runnable {
	public static final int MAX_PLAYERS_TABLE = 9;
	Player[] players;	//players at the table, each slot in array is a seat
	PokerGame game;			//current game
	Player dealer;			//current dealer
	
	public PokerTable(){
		players = new Player[MAX_PLAYERS_TABLE];
	}
	
	
	public void addPlayer(Player player){
		for(int i = 0; i < players.length; i++){
			if(players[i] == null){
				players[i] = player;
				break;
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
}
