package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class ServerThread implements Runnable {
    public static InputStreamReader inputstreamreader;
    public static BufferedReader bufferedreader;
    public static OutputStreamWriter outputstreamwriter;
    public static BufferedWriter bufferedwriter;
    public ObjectInputStream objectinputstream;
    public ObjectOutputStream objectoutputstream;

    public ServerThread(InputStreamReader inputstreamreaderInput,
	    BufferedReader bufferedreaderInput,
	    OutputStreamWriter outputstreamwriterInput,
	    BufferedWriter bufferedWriterInput,
	    ObjectInputStream objectinputstreamInput,
	    ObjectOutputStream objectoutputstreamInput) {
	inputstreamreader = inputstreamreaderInput;
	bufferedreader = bufferedreaderInput;
	outputstreamwriter = outputstreamwriterInput;
	bufferedwriter = bufferedWriterInput;
	objectinputstream = objectinputstreamInput;
	objectoutputstream = objectoutputstreamInput;
    }

    public void run() {
	try {
	    String string = null;
	    // Read the Session key
	    client.AesMessageStructure aesMessage = (client.AesMessageStructure) objectinputstream
		    .readObject();
	    // Compute the encrypted nonce to client to eliminate the man in the
	    // middle
	    String nonceString = aesMessage.nonceString;
	    SecretKey sessionKey = aesMessage.sessionKey;
	    String randomString = aesMessage.randomString;
	    byte randomStringByte[] = randomString.getBytes();
	    IvParameterSpec ivspec = new IvParameterSpec(randomStringByte);
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, sessionKey,ivspec);
	    byte[] encryptedStringByte = cipher.doFinal(nonceString.getBytes());
	    // Send the encryptedString to client
	    objectoutputstream.writeObject(encryptedStringByte);

	    /*
	     * while ((string = bufferedreader.readLine()) != null) {
	     * System.out.println(string); System.out.flush(); }
	     */
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
