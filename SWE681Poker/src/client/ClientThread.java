package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class ClientThread implements Runnable {
    public Socket socket;
    public SecretKey sessionKey;
    public IvParameterSpec ivspec;
    public ObjectOutputStream objectoutputstream;
    public ObjectInputStream objectinputstream;

    public ClientThread(Socket socketInput, SecretKey sessionKeyInput,
	    IvParameterSpec ivspecInput) {
	try {
	    socket = socketInput;
	    sessionKey = sessionKeyInput;
	    ivspec = ivspecInput;
	    objectoutputstream = new ObjectOutputStream(
		    socket.getOutputStream());
	    objectoutputstream.flush();
	    objectinputstream = new ObjectInputStream(socket.getInputStream());
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }

    public void send(String stringData) {
	try {
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, sessionKey, ivspec);
	    byte[] encryptedStringByte = cipher.doFinal(stringData.getBytes());
	    objectoutputstream.writeObject(encryptedStringByte);
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }

    public void run() {

    }

}
