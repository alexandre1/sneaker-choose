package org.vaadin.example.utils;

import org.vaadin.example.utils.AES;

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
