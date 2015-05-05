package server;

import javax.net.ssl.*;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] arstring) {
	try {
	    final int MAX_ACTIVE_TABLES = 9;
	    PokerTable[] activePokerTables = new PokerTable[MAX_ACTIVE_TABLES];
	    for (int i = 0; i < activePokerTables.length; i++) {
		activePokerTables[i] = null;
	    }

	    // Assuming that server is being started for the first time so
	    // Lets start the PokerTable thread.
	    PokerTable pokerTableThread = new PokerTable(0);
	    new Thread(pokerTableThread).start();
	    activePokerTables[0] = pokerTableThread;

	    System.setProperty("javax.net.ssl.keyStore", "mySrvKeystore");
	    System.setProperty("javax.net.ssl.keyStorePassword", "123456");
	    SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
		    .getDefault();
	    SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory
		    .createServerSocket(9999);
	    ServerSocket serverSocket = new ServerSocket(9998);
	    table.GameCoordinator gameCoordinator = new table.GameCoordinator();
	    while (true) {
		SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

		InputStream inputstream = sslsocket.getInputStream();
		OutputStream outputstream = sslsocket.getOutputStream();

		ObjectOutputStream objectoutputstream = new ObjectOutputStream(
			outputstream);
		objectoutputstream.flush();
		ObjectInputStream objectinputstream = new ObjectInputStream(
			inputstream);
		new Thread(new ServerThreadSsl(objectinputstream,
			objectoutputstream, serverSocket, pokerTableThread,
			activePokerTables)).start();

	    }
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
