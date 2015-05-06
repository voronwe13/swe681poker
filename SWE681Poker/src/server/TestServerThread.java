package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class TestServerThread implements Runnable {

	private static final int MAX_TRIES = 5;
	private Socket connection;
	private String filepath;
	BufferedReader input;
	OutputStream output;
	PrintWriter pw;
	boolean active;
	Player player;

	TestServerThread(Socket connection, String filepath){
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
		while(active){
			try {
				String command = input.readLine();
				switch(command){
					case "getgamelist": getOldGames();
									break;
					case "gettablelist": getTableList();
									break;
					case "jointable": joinTable();
									break;
				}
				Thread.sleep(500);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void joinTable() throws IOException {
		System.out.println("Join table requested");
		String tablenumstr = input.readLine();
		int tablenum = -1;
		if(Pattern.matches("[0-9]{1,2}", tablenumstr))
			tablenum = Integer.parseInt(tablenumstr);
		if(tablenum < 0 || tablenum > TestServer.getNumTables()){
			System.out.println("possible attack, requested tablenum: "+tablenumstr);
			pw.println("failure");
			return;
		}
			
		PokerTable table = TestServer.getTable(tablenum);
		table.addPlayer(player);
		String command = input.readLine();
		switch(command){
			case "joingame": break;
			case "leavetable": return;
		}		
		while(player.tablenumber >= 0){

		}
	}

	private void getTableList() throws IOException {
		System.out.println("gettablelist requested");
		String[] tables = TestServer.getTableList();
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
			TestServer.addPlayer(player);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
