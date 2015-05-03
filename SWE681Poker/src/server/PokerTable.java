package server;

import java.util.List;

import swe681poker.CardDeck;

public class PokerTable implements Runnable {
	public static final int MAX_PLAYERS_TABLE = 9;
	Player[] players;	//players at the table, each slot in array is a seat
	PokerGame game;			//current game
	Player dealer;			//current dealer
	boolean tableactive;
	
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
		tableactive = true;
		while(tableactive){
			//TODO: run games, keep track of players in table.
		}
		
	}
	
	public void closeTable(){
		tableactive = false;
	}
	
	
}
