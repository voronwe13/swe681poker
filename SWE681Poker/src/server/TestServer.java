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
import java.util.Collection;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class TestServer {
	private static final int PORT = 9753;
	private static final String FILEPATH = "\\server";
	public static final int MAX_TABLES = 9;
	public static final int MAX_PLAYERS = 60;
	private static SSLServerSocket mysocket;
	private static boolean serveractive = true;
	
	private static List<PokerTable> tables;
	private static List<Player> players;	
	
	private static void startServer() {
		//Code adapted from http://blog.trifork.com/2009/11/10/securing-connections-with-tls/
		try{
			// Key store for your own private key and signing certificates
			InputStream keyStoreResource = new FileInputStream("mySrvKeystore");
			char[] keyStorePassphrase = "123456".toCharArray();
			KeyStore ksKeys = KeyStore.getInstance("JKS");
			ksKeys.load(keyStoreResource, keyStorePassphrase);
			
			// KeyManager decides which key material to use.
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ksKeys, keyStorePassphrase);
			
			// Trust store contains certificates of trusted certificate authorities.
			// Needed for client certificate validation.
			InputStream trustStoreIS = new FileInputStream("clientTruststore");
			char[] trustStorePassphrase = "123456".toCharArray();
			KeyStore ksTrust = KeyStore.getInstance("JKS");
			ksTrust.load(trustStoreIS, trustStorePassphrase);
			
			// TrustManager decides which certificate authorities to use.
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ksTrust);
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			
			SSLServerSocketFactory sslserversocketfactory = sslContext.getServerSocketFactory();
			mysocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(PORT);
			mysocket.setNeedClientAuth(false);
			
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
		createTable();
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
	
	static void createTable(){
		System.out.println("Creating table...");
		if(tables == null)
			tables = new ArrayList<PokerTable>(MAX_TABLES);
		if(tables.size() >= MAX_TABLES)
			return;
		PokerTable table = new PokerTable(tables.size());
		tables.add(table);
		Thread thread = new Thread(table);
		thread.start();
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
	

	public static int getNumTables(){
		return tables.size();
	}

	
	public static void main(String [] args){
		startServer();
	}

	public static void addPlayer(Player player) {
		if(players == null)
			players = new ArrayList<Player>(MAX_PLAYERS);
		players.add(player);
		if(players.size()/6 > tables.size()){
			createTable();
		}
	}

	public static PokerTable getTable(int tablenum) {
		// TODO Auto-generated method stub
		return tables.get(tablenum);
	}
	
	public static String[] getTableList(){
		String[] tablelist = new String[tables.size()];
		for(int i=1; i<=tablelist.length; i++)
			tablelist[i-1] = "Table "+i+" - "+tables.get(i-1).currentlyRegisteredPlayers()+" players";
		return tablelist;
	}
}
