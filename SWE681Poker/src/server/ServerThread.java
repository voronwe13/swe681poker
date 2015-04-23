package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ServerThread implements Runnable {
	public static InputStreamReader inputstreamreader;
	public static BufferedReader bufferedreader;
	public static OutputStreamWriter outputstreamwriter;
	public static BufferedWriter bufferedwriter;
	public ObjectInputStream objectinputstream;
	public ObjectOutputStream objectoutputstream;
	
	public ServerThread(InputStreamReader inputstreamreaderInput,BufferedReader bufferedreaderInput,OutputStreamWriter outputstreamwriterInput, 
			BufferedWriter bufferedWriterInput, ObjectInputStream objectinputstreamInput,ObjectOutputStream objectoutputstreamInput)
	{
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
			//Read the Session key
			byte[] sessionKey = bufferedreader.readLine().getBytes();
			//System.out.println("AES string: "+string+"\n");
			//System.out.println("AES bytes: "+string.getBytes()+"\n");
			System.out.println("sessionKey: "+sessionKey+"\n");
			//Send an ACK for the session key
			bufferedwriter.write("Server ACK for the session key\n");
			bufferedwriter.flush();
			/*
			while ((string = bufferedreader.readLine()) != null) {
				System.out.println(string);
				System.out.flush();
			}
			*/
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
