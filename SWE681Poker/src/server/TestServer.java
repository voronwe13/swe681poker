package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class TestServer {
	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final int PORT = 9753;
	private static final String FILEPATH = "\\server";
	private static SSLServerSocket mysocket;
	private static boolean serveractive = true;
	
	private List<PokerTable> tables;
	private List<Player> players;	
	
	private static void startServer() {
		//Code adapted from http://blog.trifork.com/2009/11/10/securing-connections-with-tls/
		try{
			// Key store for your own private key and signing certificates
			InputStream keyStoreResource = new FileInputStream("mySrvKeystore");
			char[] keyStorePassphrase = "secret".toCharArray();
			KeyStore ksKeys = KeyStore.getInstance("JKS");
			ksKeys.load(keyStoreResource, keyStorePassphrase);
			
			// KeyManager decides which key material to use.
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ksKeys, keyStorePassphrase);
			
			// Trust store contains certificates of trusted certificate authorities.
			// Needed for client certificate validation.
			InputStream trustStoreIS = new FileInputStream("clientTruststore");
			char[] trustStorePassphrase = "secret".toCharArray();
			KeyStore ksTrust = KeyStore.getInstance("JKS");
			ksTrust.load(trustStoreIS, trustStorePassphrase);
			
			// TrustManager decides which certificate authorities to use.
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ksTrust);
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			
			SSLServerSocketFactory sslserversocketfactory = sslContext.getServerSocketFactory();
			mysocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(PORT);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Server ready, now listening for connections...");
		while(serveractive){
			try{
				SSLSocket socket = (SSLSocket) mysocket.accept();
				socket.setUseClientMode(false);
				
				socket.setEnabledProtocols(StrongTls.intersection(socket.getEnabledProtocols(), StrongTls.ENABLED_PROTOCOLS));
				socket.setEnabledCipherSuites(StrongTls.intersection(socket.getEnabledCipherSuites(), StrongTls.ENABLED_CIPHER_SUITES));
				
				System.out.println("client connected from "
						+socket.getInetAddress().toString()+":"+socket.getPort());
				TestServerThread server = new TestServerThread(socket, FILEPATH+PORT+"\\");
				Thread thread = new Thread(server);
				thread.start();
			} catch(IOException e){
				
			}
		}
	}

	
	@Override
	protected void finalize() {
		try {
			if(mysocket != null)
				mysocket.close();
			super.finalize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	



	
	public static void main(String [] args){
		startServer();
	}
}
