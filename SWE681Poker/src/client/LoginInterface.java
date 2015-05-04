package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

import server.ServerThreadSsl;

public class LoginInterface {

	final int WIDTH = 480, HEIGHT = 360;
	final int CENTERX = WIDTH/2, CENTERY = HEIGHT/2;

    protected Shell shell;
    public static InputStreamReader inputstreamreader;
    public static BufferedReader bufferedreader;
    public static OutputStreamWriter outputstreamwriter;
    public static BufferedWriter bufferedwriter;
    public SecretKey sessionKey;
    public String nonceString;

    public LoginInterface() {
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

    /*
     * public static IvParameterSpec ivspecClient() { return ivspec; }
     * 
     * public static SecretKey sessionKeyClient() { return sessionKey; }
     * 
     * public static String nonceStringClient() { return nonceString; }
     */
    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
    	try {
    		LoginInterface window = new LoginInterface();
    		window.open();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Open the window.
     */
    public void open() {
    	Display display = Display.getDefault();
    	createLoginContents();
    	shell.open();
    	shell.layout();
    	while (!shell.isDisposed()) {
    		if (!display.readAndDispatch()) {
    			display.sleep();
    		}
    	}
    	display.dispose();
    }

    /**
     * Create contents of the window.
     */
    protected void createLoginContents() {
    	shell = new Shell();
    	shell.setSize(WIDTH, HEIGHT);
    	shell.setText("Log in");
    	
    	Label title = new Label(shell, SWT.CENTER);
    	title.setText("Welcome to SecurePoker");
    	title.setBounds(10, 10, WIDTH - 20, 40);
    	
    	Label usernametxt= new Label(shell, SWT.NONE);
    	usernametxt.setText("Username:");
    	usernametxt.setBounds(CENTERX/2, CENTERY/2, 70, 20);
    	Text usernamebox = new Text(shell, SWT.BORDER);
    	usernamebox.setBounds(CENTERX/2 + 75, CENTERY/2, 100, 20);
    	
    	Label passwordtxt= new Label(shell, SWT.NONE);
    	passwordtxt.setText("Password:");
    	passwordtxt.setBounds(CENTERX/2, CENTERY/2+45, 70, 20);
    	Text passwordbox = new Text(shell, SWT.PASSWORD | SWT.BORDER);
    	passwordbox.setBounds(CENTERX/2 + 75, CENTERY/2+45, 100, 20);

    	Button btnsignup = new Button(shell, SWT.NONE);
    	btnsignup.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			//TODO: call server to create account
    			showMainMenu();
    		}
    	});
    	btnsignup.setBounds(140, 239, 95, 28);
    	btnsignup.setText("Sign Up");
    	
    	Button btnsignin = new Button(shell, SWT.NONE);
    	btnsignin.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			//TODO: call server to log in
    			showMainMenu();
    		}
    	});
    	btnsignin.setBounds(240, 239, 95, 28);
    	btnsignin.setText("Log In");

    }
    
    private void clearContents(){
    	for (Control child : shell.getChildren()) {
    		child.dispose();
    	}
    }
    
    Button createButton(Shell shell, int style, Rectangle rect, String text){
    	Button button = new Button(shell, style);
    	button.setBounds(rect);
    	button.setText(text);
    	return button;
    }
    
    private void showMainMenu(){
    	clearContents();
    	Label title = new Label(shell, SWT.CENTER);
    	title.setText("Welcome to SecurePoker");
    	title.setBounds(10, 10, WIDTH - 20, 40);
    	
    	int buttonwidth = 200, buttonheight = 30;
    	int halfbtnw = buttonwidth/2;
    	Rectangle buttonrect = new Rectangle(CENTERX - halfbtnw, 60, buttonwidth, buttonheight);
    	Button jointable = createButton(shell, SWT.NONE, buttonrect, "Join a Table");
    	jointable.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showTableMenu();
    		}
    	});
    	buttonrect.y += buttonheight + 5;
    	Button seescores = createButton(shell, SWT.NONE, buttonrect, "See Leaderboard");
    	seescores.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showLeaderBoard();
    		}
    	});
    	buttonrect.y += buttonheight + 5;
    	Button seegames = createButton(shell, SWT.NONE, buttonrect, "See Completed Games");
    	seegames.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showOldGames();
    		}
    	});    	
    }

	protected void showOldGames() {
		// TODO Auto-generated method stub
		
	}

	protected void showLeaderBoard() {
		// TODO Auto-generated method stub
		
	}

	protected void showTableMenu() {
		// TODO Auto-generated method stub
		
	}
}
