package server;

import java.io.*;
import java.net.*;
import java.security.SecureRandom;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

public class ServerThread implements Runnable {
    public Socket socket;
    public SecretKey sessionKey;
    public IvParameterSpec ivspec;
    public Cipher cipher;

    public ServerThread(Socket socketInput, SecretKey sessionKeyInput,
	    IvParameterSpec ivspecInput) {
	try {
	    socket = socketInput;
	    sessionKey = sessionKeyInput;
	    ivspec = ivspecInput;
	    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
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
	    while ((input = (byte[]) objectinputstream.readObject()) != null) {
		cipher.init(Cipher.DECRYPT_MODE, sessionKey, ivspec);
		byte[] decrypted = cipher
			.doFinal(input);
		System.out.println(new String(decrypted));
		// System.out.flush();
	    }
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
