package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import swe681poker.Card;

public class Player {
	String username;
	int money;  //money this player has according to the server
	Card[] hand;  //cards in hand
	boolean active;  //whether player is in current game
	int tablenumber;	//which table the player is currently playing at
	int seatnumber;		//which seat at the table the player is at
	PrintWriter printwriter;   //bound to socket output
	BufferedReader bufferedreader;	//bound to socket input
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
		try {
			printwriter.println("getbid");
			String bidstr = bufferedreader.readLine();
			int bid = 0;
			if(Pattern.matches("^[0-9]+$", bidstr)) //only allow positive numbers
				bid = Integer.parseInt(bidstr);
			else {
				System.out.println("attack detected, invalid bid string: "+bidstr);
				return -2;
			}
			if(bid > money){
				System.out.println("attack detected: bid larger than available money");
				return -2;
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -2;
	}
	
	/**
	 * set the player's hand (two cards for hold'em, but this could support any game).
	 * @param hand		Hand dealt by table
	 */
	public void setHand(Card[] hand){
		this.hand = hand;
		printwriter.println("sethand");
		printwriter.println(hand[0].intvalue);
		printwriter.println(hand[1].intvalue);
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

	/**
	 * sends dealer number to client so it can show dealer, big blind and small blind
	 * @param dealer	array position of dealer in players.
	 */
	public void setDealer(int dealer) {
		// TODO send dealer number to client so it can show which seat is the dealer
		
	}
	
	/**
	 * sends player information on the rest of the players. Specifically, the seat 
	 * number (array position), the player's name, and the player's chip total.
	 * @param players		array of players in the game
	 */
	public void setPlayers(Player[] players){
		//TODO: send player information for each player to client.  
	}
	
	/**
	 * if new player joins the table, this notifies this player of the new player's attributes
	 * (see setPlayers).
	 * @param newplayer		the new player in the game.
	 */
	public void newPlayerAdded(Player newplayer){
		//TODO: send player data to client
	}
	
}
