package server;


public class PokerTable implements Runnable {
	public static final int MAX_PLAYERS_TABLE = 9;
	public static final int MIN_PLAYERS_TABLE = 3;
	Player[] players;	//players at the table, each slot in array is a seat
	PokerGame game;			//current game
	int dealer;			//current dealer
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
		dealer = 0;
		while(tableactive){
			//TODO: run games, keep track of players in table.
			waitForPlayers(60);
			game = new PokerGame(players, dealer);
			dealer++;
		}
		
	}


	private void waitForPlayers(int secondstowait) {
		// TODO Auto-generated method stub
		
	}


	public void closeTable(){
		tableactive = false;
	}
	
	
}
