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

    public ServerThread(Socket socketInput, SecretKey sessionKeyInput,
	    IvParameterSpec ivspecInput) {
	socket = socketInput;
	sessionKey = sessionKeyInput;
	ivspec = ivspecInput;
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
		// System.out.println(string);
		// System.out.flush();
	    }
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
