package client;

import java.io.Serializable;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AesMessageStructure implements Serializable{
	public SecretKey sessionKey;
	public String nonceString;
	public String randomString;
	
	public AesMessageStructure(SecretKey sessionKeyInput, String nonceInput, String randomStringInput)
	{
		sessionKey = sessionKeyInput;
		nonceString = nonceInput;
		randomString = randomStringInput;
	}
}
