package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
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

	public LoginInterface() {
		try {
			System.setProperty("javax.net.ssl.trustStore", "clientTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "123456");
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory
					.getDefault();
			SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(
					"localhost", 9999);

			InputStream inputstream = sslsocket.getInputStream();
			inputstreamreader = new InputStreamReader(inputstream);
			bufferedreader = new BufferedReader(inputstreamreader);

			OutputStream outputstream = sslsocket.getOutputStream();
			outputstreamwriter = new OutputStreamWriter(outputstream);
			bufferedwriter = new BufferedWriter(outputstreamwriter);

			// ObjectOutputStream objectoutputstream = new
			// ObjectOutputStream(outputstream);
			// objectoutputstream.flush();
			// ObjectInputStream objectinputstream = new
			// ObjectInputStream(inputstream);
			ObjectOutputStream objectoutputstream = null;
			ObjectInputStream objectinputstream = null;

			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256);
			SecretKey sessionKey = keyGen.generateKey();

			// send the session key
			byte[] sessionKeyEncoded = sessionKey.getEncoded();
			System.out.println("The AES key is: " + sessionKeyEncoded + "\n");
			System.out.flush();
			bufferedwriter.write(sessionKeyEncoded + "\n");
			bufferedwriter.flush();

			/*
			 * SecureRandom nonce = new SecureRandom(); String nonceString =
			 * nonce.toString().substring(nonce.toString().length()-8);
			 * System.out.println("The nonce is: "+nonceString);
			 * System.out.flush();
			 * 
			 * //Send the nonce
			 * //bufferedwriter.write("Hello server I am your new client\n");
			 * //bufferedwriter.flush();
			 * bufferedwriter.write(nonce.toString()+"\n");
			 * bufferedwriter.flush();
			 */

			// Read the ACK
			String string = null;
			string = bufferedreader.readLine();
			System.out.println(string);
			System.out.flush();

			/*
			 * String string = null; while ((string = bufferedreader.readLine())
			 * != null) { System.out.println(string); System.out.flush(); }
			 */
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

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
