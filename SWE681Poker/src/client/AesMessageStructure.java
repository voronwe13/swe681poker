package client;

import java.io.Serializable;
import java.security.SecureRandom;

import javax.crypto.SecretKey;

public class AesMessageStructure implements Serializable{
	public SecretKey sessionKey;
	public SecureRandom nonce;
	
	public AesMessageStructure(SecretKey sessionKeyInput, SecureRandom nonceInput)
	{
		sessionKey = sessionKeyInput;
		nonce = nonceInput;
	}
}
