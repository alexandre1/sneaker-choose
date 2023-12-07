package org.vaadin.example;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.context.annotation.Bean;

@Entity(name="country")
public class Country {

	@Id
	public int id;
	
	public String libelle;
	
	public String indicatif;
	
	public Country () {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getIndicatif() {
		return indicatif;
	}

	public void setIndicatif(String indedicatif) {
		this.indicatif = indedicatif;
	}
}
