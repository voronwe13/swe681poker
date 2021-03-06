package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import swe681poker.Card;

public class Player {
    String username;
    int money; // money this player has according to the server
    int activechips; //money player has in this game.  This is visible to other players.
    int currentbid; //current bid in active game
    Card[] hand; // cards in hand
    boolean active; // whether player is in current game
    boolean update; // game has updated
    int tablenumber; // which table the player is currently playing at
    int seatnumber; // which seat at the table the player is at
    PrintWriter printwriter; // bound to socket output
    BufferedReader bufferedreader; // bound to socket input
    int wins, losses, folds;
    String passwordForUserInformation = "dummyPassword";
    ServerThread server;

    // client socket to communicate with this player.
    // other stats for the player

    public Player(int startingmoney, String usernameInput) {
		money = startingmoney;
		active = false;
		username = usernameInput;
		tablenumber = -1;
		seatnumber = -1;
		wins = 0;
		losses = 0;
		folds = 0;
		activechips = 0;
		currentbid = 0;
		server = null;
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
    	update = true;
    }
    
    public String[] getHand(){
    	if(hand == null)
    		return new String[0];
    	String[] cards = new String[hand.length];
    	for(int i=0; i<cards.length; i++){
    		cards[i] = "" + hand[i].intvalue;
    	}
    	return cards;
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
//    					if (Pattern.matches("^[A-Za-z0-9]+$", password)
//    							&& password.length() >= 8) {
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
//    					}
    				}
    				else return null;
    			}
    		}
    		if (valid == true) {
    			Player player = new Player(1000, username);
    			player.addUserInformation();
    			return player;
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
    		if (Pattern.matches("^[A-Za-z]+$", username) && username.length() >= 4){
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
	    			stream.close();
	    		}
	    		if (valid == true) {
	    			Player player = Server.checkPlayer(username);
	    			if(player == null){
	    				player = new Player(0, username);
	    				player.readUserInformation();
	    				Server.addPlayer(player);
	    			} else {
	    				System.out.println("user was already logged in, disconnecting old port");
	    				player.server.closeConnections();
	    			}
	    			return player;
	    		} else {
	    			return null;
	    		} 
    		} 
    	} catch (Exception e) {
    		System.out.println(e.toString());
    		return null;
    	}
    	return null;
    }
    
    public boolean addUserInformation() {
    	boolean valid = false;
    	String userInformation;
    	try {
    		// Validate the user
    		File userFile = new File("AuthenticationFile/" + username);
    		if (userFile.exists()) {
    			File file = new File("UserInformationFile/" + username);
    			BufferedWriter bufferedWriter = new BufferedWriter(
    					new FileWriter(file));
    			userInformation = Integer.toString(money) + "\n";
    			userInformation += Integer.toString(wins) + "+\n";
    			userInformation += Integer.toString(losses) + "\n";
    			userInformation += Integer.toString(folds) + "\n";

    			bufferedWriter.write(userInformation);
    			bufferedWriter.close();
    			valid = true;
    			FileInputStream fis = new FileInputStream(file);
    			FileOutputStream fos = new FileOutputStream(
    					"UserInformationFile/" + username + "_encrypted");
    			encrypt(passwordForUserInformation, fis, fos);
    			file.delete();
    		}
    		return valid;
    	} catch (Exception e) {
    		System.out.println(e.toString());
    		return false;
    	}
    }

    public void readUserInformation(){
    	try {
    		File userFile = new File("AuthenticationFile/" + username);
    		if (userFile.exists()) {
    			FileInputStream fis2 = new FileInputStream(
    					"UserInformationFile/" + username + "_encrypted");
    			FileOutputStream fos2 = new FileOutputStream(
    					"UserInformationFile/" + username);
    			decrypt(passwordForUserInformation, fis2, fos2);
    			File file = new File("UserInformationFile/" + username);
    			BufferedReader bufferedReader = new BufferedReader(
    					new FileReader(file));
    			String moneystr = bufferedReader.readLine();
    			String winstr = bufferedReader.readLine();
    			String lossestr = bufferedReader.readLine();
    			String foldsstr = bufferedReader.readLine();
    			bufferedReader.close();
    			money = Integer.parseInt(moneystr);
    			wins = Integer.parseInt(winstr);
    			losses = Integer.parseInt(lossestr);
    			folds = Integer.parseInt(foldsstr);
    			System.out.println("user information: " + moneystr + " " + winstr + " " + lossestr + " " + foldsstr);
    			file.delete();
    		}
    	} catch (Exception e) {
    		System.out.println(e.toString());
    	}
    }

    public static void encrypt(String key, InputStream inputStream, OutputStream outputStream){
    	try{
	    	DESKeySpec dks = new DESKeySpec(key.getBytes());
	    	SecretKey encryptionKey = SecretKeyFactory.getInstance("DES").generateSecret(dks);
	    	Cipher cipher = Cipher.getInstance("DES");
	    	cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
	    	CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
	    	byte[] bytes = new byte[64];
	    	int temp;
	    	while ((temp = cipherInputStream.read(bytes)) != -1) {
	    		outputStream.write(bytes, 0, temp);
	    	}
	    	outputStream.flush();
	    	outputStream.close();
	    	cipherInputStream.close();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    }

    public static void decrypt(String key, InputStream inputsStream, OutputStream outputStream){
    	try{
	    	DESKeySpec dks = new DESKeySpec(key.getBytes());
	    	SecretKey encryptionKey = SecretKeyFactory.getInstance("DES").generateSecret(dks);
	    	Cipher cipher = Cipher.getInstance("DES");
	    	cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
	    	CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
	    	byte[] bytes = new byte[64];
	    	int temp;
	    	while ((temp = inputsStream.read(bytes)) != -1) {
	    		cipherOutputStream.write(bytes, 0, temp);
	    	}
	    	cipherOutputStream.flush();
	    	cipherOutputStream.close();
	    	inputsStream.close();
	    	
		} catch (Exception e){
			e.printStackTrace();
		}
    }

    public void update(){
    	update = true;
    }
    
    public String getUpdate(){
    	String updatestr = Boolean.toString(update);
    	update = false;
    	return updatestr;
    }
    
}
