package server;

import javax.net.ssl.*;

import java.io.*;

public class Server {
    public static void main(String[] arstring) {
	try {
	    System.setProperty("javax.net.ssl.keyStore", "mySrvKeystore");
	    System.setProperty("javax.net.ssl.keyStorePassword", "123456");
	    SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
		    .getDefault();
	    SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory
		    .createServerSocket(9999);
	    while (true) {
		SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

		InputStream inputstream = sslsocket.getInputStream();
		//InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
		InputStreamReader inputstreamreader = null;
		//BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
		BufferedReader bufferedreader = null;
		
		OutputStream outputstream = sslsocket.getOutputStream();
		//OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
		OutputStreamWriter outputstreamwriter = null;
		//BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);
		BufferedWriter bufferedwriter = null;
		
		ObjectOutputStream objectoutputstream = new ObjectOutputStream(outputstream);
		objectoutputstream.flush();
		ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
		//ObjectOutputStream objectoutputstream = null;
		//ObjectInputStream objectinputstream = null;
		new Thread(new ServerThread(inputstreamreader, bufferedreader,
			outputstreamwriter, bufferedwriter, objectinputstream,
			objectoutputstream)).start();
	    }
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
