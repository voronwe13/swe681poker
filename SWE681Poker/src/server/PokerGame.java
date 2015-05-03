package server;

import swe681poker.CardDeck;

public class PokerGame {
	Player[] players;
	CardDeck deck;
	int dealer;
	int playersremaining;
	public static final int firstroundmin = 10;
	public static final int secondroundmin = 20;

	public PokerGame(Player[] players, int dealer) {
		this.players = players;
		this.dealer = dealer;
		deck = new CardDeck();
	}
	
	public void startGame(){
		deck.shuffleDeck();
		playersremaining = 0;
		//deal out cards
		for(Player player:players){
			if(player != null){
				player.setHand(deck.dealX(2));
				player.setDealer(dealer);
				playersremaining++;
			}
		}
		biddingRound(firstroundmin);
		if(checkWin()) return;
		playFlop();
		biddingRound(firstroundmin);
		if(checkWin()) return;
		playTurn();
		biddingRound(secondroundmin);
		if(checkWin()) return;
		playRiver();
		biddingRound(secondroundmin);
		checkWin();
	}

	private void playRiver() {
		// TODO Auto-generated method stub
		
	}

	private void playTurn() {
		// TODO Auto-generated method stub
		
	}

	private void playFlop() {
		// TODO Auto-generated method stub
		
	}

	private boolean checkWin() {
		// TODO Auto-generated method stub
		return false;
	}

	private void biddingRound(int firstroundmin2) {
		// TODO Auto-generated method stub
		
	}

}
