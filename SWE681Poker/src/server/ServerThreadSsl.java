package server;

import java.io.*;
import java.net.*;
import java.security.SecureRandom;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

public class ServerThreadSsl implements Runnable {
    public ObjectInputStream objectinputstream;
    public ObjectOutputStream objectoutputstream;
    public ServerSocket serverSocket;
    public table.GameCoordinator gameCoordinator;

    public ServerThreadSsl(ObjectInputStream objectinputstreamInput,
	    ObjectOutputStream objectoutputstreamInput,ServerSocket socketInput, table.GameCoordinator gameCoordinatorInput) {
	objectinputstream = objectinputstreamInput;
	objectoutputstream = objectoutputstreamInput;
	serverSocket=socketInput;
	gameCoordinator=gameCoordinatorInput;
    }

    public void run() {
	try {
	    String string = null;
	    // Read the Session key
	    client.AesMessageStructure aesMessage = (client.AesMessageStructure) objectinputstream
		    .readObject();
	    // Compute the encrypted nonce to client to eliminate the man in the
	    // middle
	    String nonceString = aesMessage.nonceString;
	    SecretKey sessionKey = aesMessage.sessionKey;
	    String randomString = aesMessage.randomString;
	    byte randomStringByte[] = randomString.getBytes();
	    IvParameterSpec ivspec = new IvParameterSpec(randomStringByte);
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, sessionKey, ivspec);
	    byte[] encryptedStringByte = cipher.doFinal(nonceString.getBytes());
	    // Send the encryptedString to client
	    objectoutputstream.writeObject(encryptedStringByte);

	    Socket socket = serverSocket.accept();
	    
	    System.out.println("Ben server olarak baglantiyi kabul ettim\n");
	    
	    new Thread(new ServerThread(socket, sessionKey, ivspec,gameCoordinator)).start();

	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
