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
    public table.GameCoordinator gameCoordinator;

    public ServerThread(Socket socketInput, SecretKey sessionKeyInput,
	    IvParameterSpec ivspecInput,
	    table.GameCoordinator gameCoordinatorInput) {
	try {
	    socket = socketInput;
	    sessionKey = sessionKeyInput;
	    ivspec = ivspecInput;
	    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    gameCoordinator = gameCoordinatorInput;
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
		if (clientRequestParts[0].equals("create a new game")) {
		    if (!gameCoordinator.checkClient(clientUsername)) {
			table.Game game = new table.Game();
			gameCoordinator.addClientToANewGame(clientUsername, game);
			System.out.println("Created a new game\n");
		    } else {
			// deny
			System.out.println("Denied to create a game\n");
		    }
		}
		if (clientRequestParts[0].equals("check for games")) {
		    
		}
		if (clientRequestParts[0].equals("join a game")) {
		    if (!gameCoordinator.checkClient(clientUsername)) {
			
		    } else {
			// deny because player is only allowed to play one game
			// at a time
			//
			System.out.println("Denied to join a game\n");
		    }
		}
	    }
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }

}
