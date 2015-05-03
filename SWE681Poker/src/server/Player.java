package server;

import swe681poker.Card;

public class Player {
	int money;  //money this player has according to the server
	Card[] hand;  //cards in hand
	boolean active;  //whether player is in current game
	int tablenumber;	//which table the player is currently playing at
	//client socket to communicate with this player.
	//other stats for the player
	
	public Player(int startingmoney){
		money = startingmoney;
		active = false;
	}
	
	/**
	 * Get the bid from the client.  Should use secure communication.  Since this is
	 * server side, this should verify that bid amount is <= available money. 
	 * 
	 * @param timeout		Amount of time in ms to wait for the client to respond
	 * @return				Amount of bid.  -1 for check/call, -2 for fold.
	 */
	public int getBid(int timeout){
		//TODO: get bid from the client
		return 0;
	}
	
	/**
	 * set the player's hand (two cards for hold'em, but this could support any game).
	 * @param hand		Hand dealt by table
	 */
	public void setHand(Card[] hand){
		this.hand = hand;
		//TODO: send hand (probably just int value of card) to the client
	}
	
	/**
	 * send the 3 card flop for hold'em to the client
	 * @param flop
	 */
	public void sendFlop(Card[] flop){
		//TODO: send 3 card flop to the client (int values for cards)
	}
	
	/**
	 * send a single card to the client.  This can be used for the turn
	 * and the river in hold'em, or any cards in a different game.
	 * @param card
	 */
	public void sendCard(Card card){
		//TODO: send card to client
	}
	
	
}
