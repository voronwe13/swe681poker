package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.LinkedList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import swe681poker.Card;

public class PokerClient {
	private static final int PORT = 9753;
	private static final String SERVER = "localhost";
	
    private BufferedReader bufferedreader;
    private PrintWriter printwriter;
    private SSLSocket connection;
    
    public PokerClient(){
    	startConnection();
    }
    
    public void startConnection(){
    	try {
    		System.setProperty("javax.net.ssl.trustStore", "clientTrustStore");
    		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
    		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    		connection = (SSLSocket) sslsocketfactory.createSocket(SERVER, PORT);
    		connection.setUseClientMode(true);

    		bufferedreader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    		printwriter = new PrintWriter(connection.getOutputStream(), true);


    	} catch (Exception exception) {
    		exception.printStackTrace();
    	}
    }
    
    String sendCredentials(String username, String password, boolean newuser) {
    	try {
    		if(newuser){
    			printwriter.println("newuser");
    		} else {
    			printwriter.println("authenticate");
    		}
    		String response = bufferedreader.readLine();
    		if("ready".equals(response)){
    			printwriter.println(username);
    			printwriter.println(password);
    			response = bufferedreader.readLine();
    		}
    		System.out.println("Login response: "+response);
    		return response;
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return "failure";
    }
	
	String[] getGameList() {
		LinkedList<String> gamelist = new LinkedList<String>();
		// TODO request game list from server
		try {
			printwriter.println("getgamelist");
			String gamestr = "";
			while(!"done".equals(gamestr)){
				gamestr = bufferedreader.readLine();
				gamelist.add(gamestr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String[]) gamelist.toArray();
	}
	
	String[] getTableList() {
		LinkedList<String> tablelist = new LinkedList<String>();
		// TODO request game list from server
		try {
			printwriter.println("gettablelist");
			String tablestr = "";
			tablestr = bufferedreader.readLine();
			do{
				tablelist.add(tablestr);
				tablestr = bufferedreader.readLine();
			} while(!"done".equals(tablestr));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String[]) tablelist.toArray(new String[tablelist.size()]);
	}
	
	boolean selectTable(int selected) {
		try {
			printwriter.println("jointable");
			printwriter.println(selected);
			String response = bufferedreader.readLine();
			System.out.println("select table response: "+response);
			return "success".equals(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	String[] getPlayerList() {
		LinkedList<String> playerlist = new LinkedList<String>();
		// TODO request game list from server
		try {
			printwriter.println("getplayerlist");
			String playerstr = "";
			while(!"done".equals(playerstr)){
				playerstr = bufferedreader.readLine();
				playerlist.add(playerstr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String[]) playerlist.toArray(new String[playerlist.size()]);
	}

	public int getDealer() {
		try {
			printwriter.println("getdealer");
			String dealerstr = bufferedreader.readLine();
			return Integer.parseInt(dealerstr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public Card[] getCommunityCards() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPot() {
		try {
			printwriter.println("getpot");
			String potstr = bufferedreader.readLine();
			return potstr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	public void leaveTable() {
		printwriter.println("leavetable");
	}

	public boolean joinGame(int chips) {
		try {
			printwriter.println("joingame");
			printwriter.println(""+chips);
			String response = bufferedreader.readLine();
			return "success".equals(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;		
		
	}

	public int getChips() {
		try {
			printwriter.println("getchips");
			String chipstr = bufferedreader.readLine();
			return Integer.parseInt(chipstr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public String getCommand() {
		try {
			String command = bufferedreader.readLine();
			return command;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}

	public void sendBid(int bid) {
		printwriter.println(""+bid);
	}

	public Card[] getHand() {
		Card[] hand = new Card[2];
		try {
			String card = bufferedreader.readLine();
			hand[0] = new Card(Integer.parseInt(card));
			card = bufferedreader.readLine();
			hand[1] = new Card(Integer.parseInt(card));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hand;
	}
}
