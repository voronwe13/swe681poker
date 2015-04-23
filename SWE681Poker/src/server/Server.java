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
				InputStreamReader inputstreamreader = new InputStreamReader(
						inputstream);
				BufferedReader bufferedreader = new BufferedReader(
						inputstreamreader);
				OutputStream outputstream = sslsocket.getOutputStream();
				OutputStreamWriter outputstreamwriter = new OutputStreamWriter(
						outputstream);
				BufferedWriter bufferedwriter = new BufferedWriter(
						outputstreamwriter);
				new Thread(new ServerThread(inputstreamreader, bufferedreader,outputstreamwriter,bufferedwriter))
						.start();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
