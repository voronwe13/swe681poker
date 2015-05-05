package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean authenticateUser() {
		String command;
		try {
			command = input.readLine();
			boolean newuser = false;
			if("authenticate".equals(command)){
				if("newuser".equals(command)){
					newuser = true;
				}
			} else {
				System.out.println("possible attack, expecting authentication, got: "+command);
				return false;
			}
			pw.println("ready");
			String username = input.readLine();
			String password = input.readLine();
			if(newuser){
				player = Player.createNewPlayer(username, password);
			} else {
				player = Player.authenticatePlayer(username, password);
			}
			if(player == null)
				return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
