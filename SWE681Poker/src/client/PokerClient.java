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
		return getListFromServer("getgamelist");
	}
	
	String[] getTableList() {
		return getListFromServer("gettablelist");
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
		return getListFromServer("getplayerlist");
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
		String[] cardlist = getListFromServer("getcommunitycards");
		Card[] cards = new Card[cardlist.length];
		for(int i=0; i<cards.length; i++){
			cards[i] = new Card(Integer.parseInt(cardlist[i]));
		}
		return cards;
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
	
	public int getMinBid() {
		try {
			printwriter.println("getminbid");
			String chipstr = bufferedreader.readLine();
			return Integer.parseInt(chipstr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 10;
	}

//	public String getCommand() {
//		try {
//			String command = bufferedreader.readLine();
//			return command;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "error";
//	}

	public void sendBid(int bid) {
		printwriter.println("sendbid");
		printwriter.println(""+bid);
	}

	public Card[] getHand() {
		String[] cardstr = getListFromServer("gethand");
		Card[] hand = new Card[cardstr.length];
		for(int i=0; i<hand.length; i++){
			hand[i] = new Card(Integer.parseInt(cardstr[i]));
		}
		return hand;
	}
	
	public void closeConnections(){
		try {
			printwriter.println("quit");
			bufferedreader.close();
			printwriter.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String[] getActiveChips() {
		return getListFromServer("getactivechips");
	}

	public String[] getBids() {
		return getListFromServer("getbids");
	}
	
	public String[] getListFromServer(String command){
		LinkedList<String> list = new LinkedList<String>();
		// TODO request game list from server
		try {
			printwriter.println(command);
			String string = bufferedreader.readLine();
			while(!"done".equals(string)){
				list.add(string);
				string = bufferedreader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public int getMyActiveChips() {
		try {
			printwriter.println("getmyactivechips");
			String chipstr = bufferedreader.readLine();
			return Integer.parseInt(chipstr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public boolean checkUpdate() {
		try {
			printwriter.println("checkupdate");
			String updatestr = bufferedreader.readLine();
			//System.out.println("checking for update: "+updatestr);
			return Boolean.valueOf(updatestr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public int checkTable() {
		try {
			printwriter.println("checkintable");
			String position = bufferedreader.readLine();
			return Integer.parseInt(position);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}
