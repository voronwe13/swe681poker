package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

import swe681poker.Card;

public class Player {
    String username;
    int money; // money this player has according to the server
    Card[] hand; // cards in hand
    boolean active; // whether player is in current game
    int tablenumber; // which table the player is currently playing at
    int seatnumber; // which seat at the table the player is at
    PrintWriter printwriter; // bound to socket output
    BufferedReader bufferedreader; // bound to socket input

    // client socket to communicate with this player.
    // other stats for the player

    public Player(int startingmoney, String usernameInput) {
	money = startingmoney;
	active = false;
	username = usernameInput;
	tablenumber = -1;
	seatnumber = -1;
    }

    /**
     * Get the bid from the client. Should use secure communication. Since this
     * is server side, this should verify that bid amount is <= available money.
     * 
     * @param timeout
     *            Amount of time in ms to wait for the client to respond
     * @return Amount of bid. -1 for check/call, -2 for fold.
     */
    public int getBid(int timeout) {
	try {
	    printwriter.println("getbid");
	    String bidstr = bufferedreader.readLine();
	    int bid = 0;
	    if (Pattern.matches("^[0-9]+$", bidstr)) // only allow positive
						     // numbers
		bid = Integer.parseInt(bidstr);
	    else {
		System.out.println("attack detected, invalid bid string: "
			+ bidstr);
		return -2;
	    }
	    if (bid > money) {
		System.out
			.println("attack detected: bid larger than available money");
		return -2;
	    }

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return -2;
    }

    /**
     * set the player's hand (two cards for hold'em, but this could support any
     * game).
     * 
     * @param hand
     *            Hand dealt by table
     */
    public void setHand(Card[] hand) {
	this.hand = hand;
	printwriter.println("sethand");
	printwriter.println(hand[0].intvalue);
	printwriter.println(hand[1].intvalue);
    }

    /**
     * send the 3 card flop for hold'em to the client
     * 
     * @param flop
     */
    public void sendFlop(Card[] flop) {
	// TODO: send 3 card flop to the client (int values for cards)
    }

    /**
     * send a single card to the client. This can be used for the turn and the
     * river in hold'em, or any cards in a different game.
     * 
     * @param card
     */
    public void sendCard(Card card) {
	// TODO: send card to client
    }

    /**
     * sends dealer number to client so it can show dealer, big blind and small
     * blind
     * 
     * @param dealer
     *            array position of dealer in players.
     */
    public void setDealer(int dealer) {
	// TODO send dealer number to client so it can show which seat is the
	// dealer

    }

    /**
     * sends player information on the rest of the players. Specifically, the
     * seat number (array position), the player's name, and the player's chip
     * total.
     * 
     * @param players
     *            array of players in the game
     */
    public void setPlayers(Player[] players) {
	// TODO: send player information for each player to client.
    }

    /**
     * if new player joins the table, this notifies this player of the new
     * player's attributes (see setPlayers).
     * 
     * @param newplayer
     *            the new player in the game.
     */
    public void newPlayerAdded(Player newplayer) {
	// TODO: send player data to client
    }

    /**
     * Creates a new user account, and returns a new player object.
     * 
     * @param username
     *            the requested username for the account
     * @param password
     *            the requested password for the account
     * @return new Player object, or null if username or password are not valid
     */
    public static Player createNewPlayer(String username, String password) {
    	// TODO check username/password, then create account
    	boolean valid = false;
    	try {
    		// TODO check username/password, then create account
    		// This is to check if the request is null
    		// Later we will check for empty but not null strings
    		if (username != null && password != null) {
    			// Check for the validity of the username
    			// No special characters allowed
    			if (Pattern.matches("^[A-Za-z]+$", username)
    					&& username.length() >= 4) {
    				// Check if the username already exists
    				File userFile = new File("AuthenticationFile/" + username);
    				if (!userFile.exists()) {
    					// Check for the password
    					// No special characters are allowed
    					if (Pattern.matches("^[A-Za-z0-9]+$", password)
    							&& password.length() >= 8) {
    						Random random = new SecureRandom();
    						byte[] salt = new byte[16];
    						random.nextBytes(salt);
    						MessageDigest md = MessageDigest.getInstance("MD5");

    						byte combinedPasswordSalt[] = new byte[password
    						                                       .getBytes().length + salt.length];
    						System.arraycopy(salt, 0, combinedPasswordSalt, 0,
    								salt.length);
    						System.arraycopy(password.getBytes(), 0,
    								combinedPasswordSalt, salt.length,
    								password.getBytes().length);
    						byte[] digested = md.digest(combinedPasswordSalt);

    						DataOutputStream stream = new DataOutputStream(
    								new BufferedOutputStream(
    										new FileOutputStream(
    												"AuthenticationFile/"
    														+ username, true)));
    						stream.write(salt);
    						stream.write(digested);
    						stream.close();
    						valid = true;
    					}
    				}
    				else return null;
    			}
    		}
    		if (valid == true) {
    			return new Player(1000, username);
    		} else {
    			return null;
    		}
    	} catch (Exception e) {
    		System.out.println(e.toString());
    		valid = false;
    		return null;
    	}
    }

    /**
     * Authenticates with an existing user account, and returns a new player
     * object with the stored player's data.
     * 
     * @param username
     *            the username for the account
     * @param password
     *            the password for the account
     * @return new Player object, or null if username or password are not valid
     */
    public static Player authenticatePlayer(String username, String password) {
    	boolean valid = false;
    	try {
    		File userFile = new File("AuthenticationFile/" + username);
    		if (userFile.exists()) {
    			DataInputStream stream = new DataInputStream(
    					new BufferedInputStream(new FileInputStream(userFile)));
    			byte[] fileContent = new byte[32];
    			stream.readFully(fileContent);
    			byte[] salt = new byte[16];
    			byte[] digestedPassword = new byte[16];
    			for (int i = 0; i < 16; i++) {
    				salt[i] = fileContent[i];
    				digestedPassword[i] = fileContent[i + 16];
    			}

    			MessageDigest md = MessageDigest.getInstance("MD5");

    			byte combinedPasswordSalt[] = new byte[password.getBytes().length
    			                                       + salt.length];
    			System.arraycopy(salt, 0, combinedPasswordSalt, 0, salt.length);
    			System.arraycopy(password.getBytes(), 0, combinedPasswordSalt,
    					salt.length, password.getBytes().length);
    			byte[] digested = md.digest(combinedPasswordSalt);

    			int count = 0;
    			for (int i = 0; i < 16; i++) {
    				if (digested[i] == digestedPassword[i]) {
    					count++;
    				}
    			}
    			if (count == 16) {
    				System.out.println("The user authenticated");
    				valid = true;
    			}
    		}
    		if (valid == true) {
    			int chips = 1000; // TODO: get from stored player data
    			return new Player(chips, username);
    		} else {
    			return null;
    		}
    	} catch (Exception e) {
    		System.out.println(e.toString());
    		return null;
    	}
    }

}
