package server;

import swe681poker.Card;
import swe681poker.CardDeck;

public class PokerGame {
	Player[] players;
	CardDeck deck;
	int dealer;
	int playersremaining;
	int pot;
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
		checkWinFinal();
	}

	private void playRiver() {
		// TODO Auto-generated method stub
		
	}

	private void playTurn() {
		// TODO Auto-generated method stub
		
	}

	private void playFlop() {
		Card burn = deck.dealOne(); //probably not necessary, but following poker rules
		Card[] flop = deck.dealX(3);
		for(Player player:players){
			if(player != null)
				player.sendFlop(flop);
		}
	}

	/**
	 * check if only one player remains; if so, notify players of the win, adjust pots, etc.
	 * @return	true if a player won by being the last player.
	 */
	private boolean checkWin() {
		if(playersremaining == 1){
			for(int i=0; i<players.length; i++){
				if(players[i].active){
					notifyWin(i);
					break;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * give pot to winning player, notify all players of winner;
	 * @param position
	 */
	private void notifyWin(int position) {
		// TODO Auto-generated method stub
		
	}

	private void checkWinFinal(){
		if(!checkWin()){
			//TODO: check hands for each player, find best hand, notify all players of win.
			int winposition = compareHands();
			notifyWin(winposition);
		}
	}
	
	/**
	 * find best hand, notify players of shown hands, return position of winning hand.
	 * @return	position of winning hand.
	 */
	private int compareHands() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Conduct a round of bidding.  Get bids from each player, mark folded players as
	 * inactive, until bidding is finished
	 * @param minbet	the minimum bet for this round.
	 */
	private void biddingRound(int minbet) {
		// TODO Auto-generated method stub
		
	}

}
