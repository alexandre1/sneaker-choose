package org.vaadin.example;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class TestPassword {


	
	public static void main (String [] args) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";

	     
	    String originalString = "wKPv2pXsqHqM4JIJN6mOAg==";
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(originalString, secretKey) ;
	     
	    System.out.println(originalString);
	    System.out.println(encryptedString);
	    System.out.println(decryptedString);
	}
}
