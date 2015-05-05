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

public class PokerClientDummy extends PokerClient {
    public static InputStreamReader inputstreamreader;
    public static BufferedReader bufferedreader;
    public static OutputStreamWriter outputstreamwriter;
    public static BufferedWriter bufferedwriter;
    public static PrintWriter printwriter;
    public SecretKey sessionKey;
    public String nonceString;
    
    public PokerClientDummy() {

    }
    
	String sendCredentials(String username, String password, boolean newuser) {
		// TODO Auto-generated method stub
		return "success";
	}
	
	String[] getGameList() {
		String[] gamelist = {"game1", "game2", "game3"};
		return gamelist;
	}
	
	String[] getTableList() {
		String[] tablelist = {"table1", "table2", "table3"};
		return tablelist;
	}
	
	boolean selectTable(int selected) {
		return true;
	}
	
	String[] getPlayerList() {
		String[] playerlist = {"player1", "player2", "player3"};
		return playerlist;
	}

	public int getDealer() {
		return 1;
	}
	
	public Card[] getCommunityCards() {
		Card[] cardlist = {new Card(1), new Card(13), new Card(20)};
		return cardlist;
	}
}
