package server;

import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

public class ServerThread implements Runnable {
    public Socket socket;
    public SecretKey sessionKey;
    public IvParameterSpec ivspec;
    public Cipher cipher;
    public PokerTable pokerTableThread;
    public PokerTable[] activePokerTables;

    public ServerThread(Socket socketInput, SecretKey sessionKeyInput,
	    IvParameterSpec ivspecInput, PokerTable pokerTableThreadInput,
	    PokerTable[] activePokerTablesInput) {
	try {
	    socket = socketInput;
	    sessionKey = sessionKeyInput;
	    ivspec = ivspecInput;
	    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    pokerTableThread = pokerTableThreadInput;
	    activePokerTables = activePokerTablesInput;
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }

    public void run() {
	try {
	    ObjectOutputStream objectoutputstream = new ObjectOutputStream(
		    socket.getOutputStream());
	    objectoutputstream.flush();
	    ObjectInputStream objectinputstream = new ObjectInputStream(
		    socket.getInputStream());

	    byte[] input = null;
	    String clientRequest;
	    while ((input = (byte[]) objectinputstream.readObject()) != null) {
		cipher.init(Cipher.DECRYPT_MODE, sessionKey, ivspec);
		byte[] decrypted = cipher.doFinal(input);
		System.out.println(new String(decrypted));
		clientRequest = new String(decrypted);
		String[] clientRequestParts = clientRequest.split("\\+");
		String clientUsername = clientRequestParts[1];

		// We need to decide what information has be to returned and
		// agree on the message protocol for each of these cases

		if (clientRequestParts[0].equals("start a new table")) {
		    for (int i = 0; i < activePokerTables.length; i++) {
			if (activePokerTables[i] == null) {
			    PokerTable clientTableThread = new PokerTable();
			    new Thread(pokerTableThread).start();
			    Player player = new Player(1000, clientUsername);
			    clientTableThread.addPlayer(player);
			    activePokerTables[i] = clientTableThread;
			}
		    }
		}
		if (clientRequestParts[0].equals("check for tables")) {
		    String clientReturn = "availableTables";
		    for (int i = 0; i < activePokerTables.length; i++) {
			if (activePokerTables[i] != null
				&& activePokerTables[i].isThereAvailableSeat()) {
			    clientReturn += "+"
				    + activePokerTables[i]
					    .currentlyRegisteredPlayers();
			}
		    }
		}
		
		if (clientRequestParts[0].equals("join a table")) {
		    Player player = new Player(1000, clientUsername);
		    //Here we need to identify which table he wants to join
		}
	    }
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }

}
