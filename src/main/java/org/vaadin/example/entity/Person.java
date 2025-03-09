package org.vaadin.example.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;
@Entity(name="person")

public class Person {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String title;
	
	private String firstName;
	
	private String lastName;
	
	private Date dateOfBirth;
	
	@OneToOne
	private Address address;
	
	@OneToOne
	private Address addressDelivery;
	
	private String phoneNumber;
	
	private String userName;

	private String password;
	
	private String email;
	
	private String groupOfUser;
	
	private String stripeId;
	
	private String sessionId;
	
	private String subscriptionId;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
	@JoinTable( name = "shoes_requested_by_user",
	    						joinColumns = @JoinColumn( name = "id" ),
	    						inverseJoinColumns = @JoinColumn( name = "idShoe" ),
	    						uniqueConstraints = @UniqueConstraint(columnNames = {
	    					             "id", "idShoe" }))
	    
	    


	private Set<Shoe> shoesRequestedByUser;
	
	private Boolean active;
	

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte  [] imageProfil;
	
	public Person (int id, String firstName, String lastName, Address address, String phoneNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;		
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public Person () {
		
	}
	public Person (String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public Set<Shoe> getShoesRequestedByUser() {
		return shoesRequestedByUser;
	}

	public void setShoesRequestedByUser(Set<Shoe> shoesRequestedByUser) {
		this.shoesRequestedByUser = shoesRequestedByUser;
	}

	public Address getAddressDelivery() {
		return addressDelivery;
	}

	public String getEmail() {
		return email;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAddressDelivery(Address addressDelivery) {
		this.addressDelivery = addressDelivery;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getGroupOfUser() {
		return groupOfUser;
	}

	public void setGroupOfUser(String group) {
		this.groupOfUser = group;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public byte[] getImageProfil() {
		return imageProfil;
	}

	public void setImageProfil(byte[] imageProfil) {
		this.imageProfil = imageProfil;
	}

	public String getStripeId() {
		return stripeId;
	}

	public void setStripeId(String stripeId) {
		this.stripeId = stripeId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
}
