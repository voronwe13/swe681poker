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
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LoginInterface {

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
	    outputstreamwriter = null;
	    bufferedwriter = null;

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
	    System.out.println("Plain text: "+nonceString);
	    // Generate IV
	    SecureRandom random = new SecureRandom();
	    String randomString = random.toString().substring(
		    nonce.toString().length() - 8);
	    randomString += randomString;
	    byte[] iv = randomString.getBytes();
	    IvParameterSpec ivspec = new IvParameterSpec(iv);
	    FileOutputStream fs = new FileOutputStream(new File("paramFile"));
	    BufferedOutputStream bos = new BufferedOutputStream(fs);
	    bos.write(iv);
	    bos.close();
	    // Create the AES message object
	    AesMessageStructure aesMessage = new AesMessageStructure(
		    sessionKey, nonceString, randomString);
	    // Send the AES message object
	    objectoutputstream.writeObject(aesMessage);
	    // Compute the encrypted string
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, sessionKey, ivspec);
	    byte[] encryptedStringByte = cipher.doFinal(nonceString.getBytes());
	    // Listen to server
	    byte[] encryptedStringByteFromServer = (byte[]) objectinputstream
		    .readObject();
	    // Compare with the object returned from Server
	    if (encryptedStringByte.equals(encryptedStringByteFromServer)) {
		System.out.println("Life is good\n");
	    }
	    byte[] fileData = new byte[16];
	    DataInputStream dis = null;

	    dis = new DataInputStream(
		    new FileInputStream(new File("paramFile")));
	    dis.readFully(fileData);
	    if (dis != null) {
		dis.close();
	    }
	    // reinitialize the cipher for decryption
	    cipher.init(Cipher.DECRYPT_MODE, sessionKey, new IvParameterSpec(
		    fileData));

	    // decrypt the message
	    byte[] decrypted = cipher.doFinal(encryptedStringByteFromServer);
	    System.out.println("Plaintext: " + new String(decrypted) + "\n");

	    if(nonceString.equals(new String(decrypted)))
	    {
		System.out.println("Client verified the server\n");
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
	createContents();
	shell.open();
	shell.layout();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) {
		display.sleep();
	    }
	}
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
	shell = new Shell();
	shell.setSize(445, 346);
	shell.setText("Log in");

	Button btnSignUp = new Button(shell, SWT.NONE);
	btnSignUp.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {

	    }
	});
	btnSignUp.setBounds(172, 139, 95, 28);
	btnSignUp.setText("Sign Up");

    }
}
