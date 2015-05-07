package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

import swe681poker.Card;

public class ServerThread implements Runnable {

	private static final int MAX_TRIES = 5;
	private Socket connection;
	private String filepath;
	BufferedReader input;
	PrintWriter pw;
	boolean active;
	Player player;
	PokerTable currenttable;

	ServerThread(Socket connection, String filepath){
		player = null;
		this.connection = connection;
		this.filepath = filepath;
		try {
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			pw = new PrintWriter(connection.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		active = false;
		for(int i=0; i<MAX_TRIES && !active; i++){
			active = authenticateUser();
			if(!active)
				pw.println("failure");
		}
		try {
			while(active){

				String command = input.readLine();
				if(command == null){
					System.out.println("possible attack, received null (EOF) "
							+ "when expecting a main menu command.  Closing connection.");
					closeConnections();
					return;
				}
				switch(command){
					case "checkintable": checkInTable();
									break;
					case "getgamelist": getOldGames();
									break;
					case "gettablelist": getTableList();
									break;
					case "jointable": joinTable(false);
									break;
					case "rejointable": joinTable(true);
									break;
					case "quit": active = false;
				}
				Thread.sleep(500);
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		closeConnections();
	}

	private void checkInTable() {
		pw.println(player.tablenumber);
	}

	private void joinTable(boolean rejoin) throws IOException {
		System.out.println("Join table requested");
		int tablenum = -1;
		if(rejoin){
			tablenum = player.tablenumber;
		} else {
			String tablenumstr = input.readLine();
			if(Pattern.matches("[0-9]{1,2}", tablenumstr))
				tablenum = Integer.parseInt(tablenumstr);
			if(tablenum < 0 || tablenum > Server.getNumTables()){
				System.out.println("possible attack, requested tablenum: "+tablenumstr);
				pw.println("failure");
				return;
			}
		}
		PokerTable table = Server.getTable(tablenum);
		if(!table.addPlayer(player)){
			pw.println("tablefull");
			return;
		}
		pw.println("success");
		currenttable = table;
		startTableLoop();
	}

	private void startTableLoop() throws IOException {
		try {
			while(player.tablenumber >= 0 && active){

				String command = input.readLine();
				if(command == null){
					System.out.println("possible attack, received null (EOF) "
							+ "when expecting a table command.  Closing connection.");
					closeConnections();
					return;
				}
				if(!"checkupdate".equals(command))
					System.out.println("got command: " + command);
				switch(command){
					case "getplayerlist": sendPlayerList();
									break;
					case "getpot": sendPot();
									break;
					case "getchips": sendChips();
									break;
					case "getactivechips": sendActiveChips();
									break;
					case "getmyactivechips": sendMyActiveChips();
									break;
					case "gethand": sendHand();
									break;
					case "getbids": sendBids();
									break;
					case "getminbid": sendMinBid();
									break;
					case "getdealer": sendDealer();
									break;
					case "getcommunitycards": sendCommunityCards();
									break;
					case "joingame": joinGame();
									break;
					case "leavetable": currenttable.removePlayer(player);
									break;
					case "checkupdate": checkUpdate();
									break;
					case "quit": quit();
				}
				Thread.sleep(500);
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}


	private void sendHand() {
		String[] hand = player.getHand();
		for(int i=0; i < hand.length; i++){
			String cardstr = hand[i];
			System.out.println("sending: "+cardstr);
			pw.println(cardstr);
		}
		pw.println("done");
	}

	private void checkUpdate() {
		String sendstr = player.getUpdate();
		//System.out.println("sending: "+sendstr);
		pw.println(sendstr);
	}

	private void sendMyActiveChips() {
		// TODO Auto-generated method stub
		String sendstr = ""+player.activechips;
		System.out.println("sending: "+sendstr);
		pw.println(sendstr);
	}

	private void quit() {
		active = false;
		currenttable.removePlayer(player);
		Server.removePlayer(player);
	}

	private void sendMinBid() {
		String sendstr = ""+currenttable.getMinBid();
		System.out.println("sending: "+sendstr);
		pw.println(sendstr);
	}

	private void sendBids() {
		Player[] players = currenttable.players;
		for(int i=0; i < players.length; i++){
			String bidstr = "";// = i+". ";
			if(players[i] != null){
				bidstr = "$" + players[i].currentbid;
			}
			System.out.println("sending: "+bidstr);
			pw.println(bidstr);
		}
		pw.println("done");
	}

	private void sendActiveChips() {
		Player[] players = currenttable.players;
		for(int i=0; i < players.length; i++){
			String chipstr = "";// = i+". ";
			if(players[i] != null){
				chipstr = "$" + players[i].activechips;
			}
			System.out.println("sending: "+chipstr);
			pw.println(chipstr);
		}
		pw.println("done");
	}

	private void sendChips() {
		String sendstr = ""+player.money;
		System.out.println("sending: "+sendstr);
		pw.println(sendstr);
		
	}

	private void sendCommunityCards() {
		if(currenttable.game != null){
			Card[] cards = currenttable.game.communitycards;
			for(int i=0; i < cards.length; i++){
				if(cards[i] == null){
					break;
				} else {
					System.out.println("sending: "+cards[i].intvalue);
					pw.println(""+cards[i].intvalue);
				}
			}
		}
		pw.println("done");
	}

	private void joinGame() throws IOException {
		String chipstr = input.readLine();
		if(Pattern.matches("^[0-9]{1,9}", chipstr)){
			int chips = Integer.parseInt(chipstr);
			if(chips > player.money){
				System.out.println("possible attack, user requested more chips for the game than user has");
				chips = player.money;
			}
			player.activechips = chips;
			player.active = true;
		} else {
			System.out.println("possible attack, chips: " + chipstr);
		}
		pw.println("success");
		
	}

	private void sendDealer() {
		String sendstr = ""+currenttable.dealer;
		System.out.println("sending: "+sendstr);
		pw.println(sendstr);
		
	}

	private void sendPot() {
		String sendstr;
		if(currenttable.game != null)
			sendstr = ""+currenttable.game.pot;
		else
			sendstr = "0";
		System.out.println("sending: "+sendstr);
		pw.println(sendstr);
	}

	private void sendPlayerList() {
		Player[] players = currenttable.players;
		for(int i=0; i < players.length; i++){
			String seatstr = "";// = i+". ";
			if(players[i] == null){
				seatstr += "seat empty";
			} else {
				seatstr += players[i].username;
				if(currenttable.game != null && currenttable.game.currentbidder == i)
					seatstr+=" <--bidder";
			}
			System.out.println("sending: "+seatstr);
			pw.println(seatstr);
		}
		pw.println("done");
	}

	private void getTableList() throws IOException {
		System.out.println("gettablelist requested");
		String[] tables = Server.getTableList();
		for(String table:tables){
			pw.println(table);
		}
		pw.println("done");
	}

	private void getOldGames() throws IOException {
		// TODO Auto-generated method stub
		System.out.println("getoldgames requested");
		
	}

	private boolean authenticateUser() {
		String command;
		try {
			command = input.readLine();
			boolean newuser = false;
			newuser = "newuser".equals(command);
			if(!("authenticate".equals(command) || newuser)){
				System.out.println("possible attack, expecting authentication, got: "+command);
				return false;
			}
			pw.println("ready");
			String username = input.readLine();
			String password = input.readLine();
			System.out.println("Attempted login, username: "+username+", password: "+password);
			if(newuser){
				player = Player.createNewPlayer(username, password);
			} else {
				player = Player.authenticatePlayer(username, password);
			}
			if(player == null){
				if(newuser)
					pw.println("usernametaken");
				System.out.println("Authentication failed.");
				return false;
			} 
				
			System.out.println("Authentication succeeded.");
			pw.println("success");
			player.server = this;
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void closeConnections(){
		active = false;
		try {
			pw.close();
			input.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
