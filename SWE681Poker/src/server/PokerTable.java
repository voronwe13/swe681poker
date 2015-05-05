package server;

public class PokerTable implements Runnable {
	public static final int MAX_PLAYERS_TABLE = 9;
	public static final int MIN_PLAYERS_TABLE = 3;
	Player[] players; // players at the table, each slot in array is a seat
	PokerGame game; // current game
	int dealer; // current dealer
	boolean tableactive;
	int tableId; //The client needs to be able to address to which table he wants to join

	public PokerTable(int id) {
		players = new Player[MAX_PLAYERS_TABLE];
		tableId = id;
	}

	public void addPlayer(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = player;
				break;
			}
		}
		player.tablenumber = tableId;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		tableactive = true;
		dealer = 0;
		while (tableactive) {
			// TODO: run games, keep track of players in table.
			waitForPlayers(60);
			dealer = findNextDealer();
			game = new PokerGame(players, dealer);
		}

	}

	private int findNextDealer() {
		int i=dealer+1;
		while(i!=dealer){
			i = i%players.length;
			if(players[i] != null){
				return i;
			}
		}
		return -1;
	}

	public void dummyPrint() {
		System.out.println("Dummy output");
	}

	private void waitForPlayers(int secondstowait) {
		// TODO Auto-generated method stub
		while (activePlayers() < MIN_PLAYERS_TABLE) {
			try {
				Thread.sleep(secondstowait * 1000);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	public void closeTable() {
		tableactive = false;
	}

	public int currentlyRegisteredPlayers() {
		int count = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				count++;
			}
		}
		return count;
	}
	
	public int activePlayers() {
		int count = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].active) {
				count++;
			}
		}
		return count;
	}

	public boolean isThereAvailableSeat() {
		if (currentlyRegisteredPlayers() < MAX_PLAYERS_TABLE) {
			return true;
		}
		return false;
	}

}
