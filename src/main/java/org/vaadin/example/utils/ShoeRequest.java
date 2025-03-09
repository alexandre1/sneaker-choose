package org.vaadin.example.utils;

public class ShoeRequest {

	private int id;
	
	private String marque;
			
	private String emailWisher;

	private String sex;
	
	private String size;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}

	public String getEmailWisher() {
		return emailWisher;
	}

	public void setEmailWisher(String emailWisher) {
		this.emailWisher = emailWisher;
	}
}
