package org.vaadin.example.sneaker.service;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.vaadin.example.utils.AES;
import org.vaadin.example.entity.Person;
import org.vaadin.example.repository.PersonRepository;

@Service
public class LoginService {

	private static final Logger LOGGER = Logger.getLogger(RegisterService.class.getName());

	private PersonRepository contactRepository;
	

	public LoginService(PersonRepository contactRepository){ 
		this.contactRepository = contactRepository;		
	}

	public String login (String email, String password) {
		Person p = this.contactRepository.findByEmail(email);
		String result = "";
			 
		if (p != null && p.getGroupOfUser() != null && p.getGroupOfUser().equals("SHOPER") && p.isActive() && password.equals(p.getPassword())) {
			result = "mainShopperView";
		} else if (p != null && p.getGroupOfUser() != null && p.getGroupOfUser().equals("SHOP") && p.isActive() != null && p.isActive() && password != null && password.equals(p.getPassword())) {
			result = "mainShopView";
		} else if (p == null || p.getGroupOfUser() == null) {
			result = "login";
		}
		return result;
	}

	public String loginNew (String email, String password) {
		Person p = this.contactRepository.findByEmail(email);
		String result = "";
			 
		if (p != null && p.getGroupOfUser().equals("SHOPER") && getPasswordEncrypted( password).equals(getPassword(p.getPassword()))) {
			result = "mainShopperView";
		} else if (p != null && p.getGroupOfUser().equals("SHOP") && password.equals(p.getPassword())) {
			result = "mainShopView";
		} else if (p == null || p.getGroupOfUser() == null) {
			result = "login";
		}				
		return result;
	}
	

	public String loginOld (String email, String password) {
		Person p = this.contactRepository.findByEmail(email);
		String result = "";
			 
		if (p != null && p.getGroupOfUser().equals("SHOPER")) {
			result = "mainShopperView";
		} else if (p != null && p.getGroupOfUser().equals("SHOP")) {
			result = "mainShopView";
		}				
		return result;
	}

	private String getPassword (String password) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	     
	    String originalString = password;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(encryptedString, secretKey) ;	  			  
		
	    
		return decryptedString;
	}
	
	private String getPasswordEncrypted (String password) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	     
	    String originalString = password;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(encryptedString, secretKey) ;	  			  
		
	    
		return encryptedString;
	}

}
