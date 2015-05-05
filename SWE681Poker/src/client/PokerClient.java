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

public class PokerClient {
    public static InputStreamReader inputstreamreader;
    public static BufferedReader bufferedreader;
    public static OutputStreamWriter outputstreamwriter;
    public static BufferedWriter bufferedwriter;
    public static PrintWriter printwriter;
    public SecretKey sessionKey;
    public String nonceString;
    
    public PokerClient(){
    	try {
    		System.setProperty("javax.net.ssl.trustStore", "clientTrustStore");
    		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
    		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory
    				.getDefault();
    		SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(
    				"localhost", 9999);

    		InputStream inputstream = sslsocket.getInputStream();
    		OutputStream outputstream = sslsocket.getOutputStream();

    		ObjectOutputStream objectoutputstream = new ObjectOutputStream(
    				outputstream);
    		objectoutputstream.flush();
    		ObjectInputStream objectinputstream = new ObjectInputStream(
    				inputstream);
    		// Generate the session key
    		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    		keyGen.init(128);
    		sessionKey = keyGen.generateKey();
    		// Generate the nonce
    		SecureRandom nonce = new SecureRandom();
    		nonceString = nonce.toString().substring(
    				nonce.toString().length() - 8);
    		System.out.println("Plain text: " + nonceString);
    		// Generate IV
    		SecureRandom random = new SecureRandom();
    		String randomString = random.toString().substring(
    				nonce.toString().length() - 8);
    		randomString += randomString;
    		byte[] iv = randomString.getBytes();
    		while (iv.length != 16) {
    			random = new SecureRandom();
    			randomString = random.toString().substring(
    					nonce.toString().length() - 8);
    			randomString += randomString;
    			iv = randomString.getBytes();
    		}
    		// Create the AES message object
    		AesMessageStructure aesMessage = new AesMessageStructure(
    				sessionKey, nonceString, randomString);
    		// Send the AES message object
    		objectoutputstream.writeObject(aesMessage);
    		// Listen to server
    		byte[] encryptedStringByteFromServer = (byte[]) objectinputstream
    				.readObject();
    		// reinitialize the cipher for decryption
    		IvParameterSpec ivspec = new IvParameterSpec(iv);
    		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    		cipher.init(Cipher.DECRYPT_MODE, sessionKey, ivspec);

    		// decrypt the message
    		byte[] decrypted = cipher.doFinal(encryptedStringByteFromServer);
    		System.out.println("Plaintext: " + new String(decrypted) + "\n");

    		if (nonceString.equals(new String(decrypted))) {
    			System.out.println("Client verified the server\n");
    			Socket socket = new Socket("localhost", 9998); 
    			// new Thread(new ClientThread(socket, sessionKey,
    			// new IvParameterSpec(iv))).start();
    			cipher.init(Cipher.ENCRYPT_MODE, sessionKey, ivspec);
    			byte[] encryptedStringByte = cipher.doFinal(new String("Bu bir deneme kaydidir\n")
    			.getBytes());
    			ObjectOutputStream objectoutputstreamTemp = new ObjectOutputStream(
    					socket.getOutputStream());
    			objectoutputstreamTemp.writeObject(encryptedStringByte);
    		}

    	} catch (Exception exception) {
    		exception.printStackTrace();
    	}
    }
    
	String sendCredentials(String username, String password, boolean newuser) {
		// TODO Auto-generated method stub
		return "success";
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
			while(!"done".equals(tablestr)){
				tablestr = bufferedreader.readLine();
				tablelist.add(tablestr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String[]) tablelist.toArray();
	}
	
	boolean selectTable(int selected) {
		// TODO Auto-generated method stub
		try {
			printwriter.println("jointable");
			String response = bufferedreader.readLine();
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
		return (String[]) playerlist.toArray();
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
}
