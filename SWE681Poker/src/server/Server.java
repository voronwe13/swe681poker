package server;

import javax.net.ssl.*;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] arstring) {
	try {
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
		
		ObjectOutputStream objectoutputstream = new ObjectOutputStream(outputstream);
		objectoutputstream.flush();
		ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
		new Thread(new ServerThreadSsl(objectinputstream,
			objectoutputstream,serverSocket,gameCoordinator)).start();
	    }
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
