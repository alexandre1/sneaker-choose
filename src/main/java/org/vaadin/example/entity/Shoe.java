package org.vaadin.example.entity;

import java.util.HashSet;
import java.util.Objects;
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

@Entity 
public class Shoe {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int idShoe;
	
	private String marque;
	
	@OneToOne
	private Person demandeur;
	
	@OneToOne
	private Shop vendeur;
	
	private String category;
	
	private String size;
	
	private String sizeUE;
	
	private String sizeUS;
	
	private String sizeCM;

	private String reservation;
	
	private String prix;
	
	private String state;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte  [] image;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte  [] imageFounded;	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinTable( name = "Shoes_Asked_By_Shops_Associations",
    						joinColumns = @JoinColumn( name = "idShop" ),
    						inverseJoinColumns = @JoinColumn( name = "idShoe" ),
    						uniqueConstraints = @UniqueConstraint(columnNames = {
    					             "idShop", "idShoe" }))
    
    

	 public Set<Shop> shoesByShopAsked = new HashSet<Shop>();
	 
	 public int getIdShoe() {
		return idShoe;
	 }

	public void setIdShoe(int id) {
		this.idShoe = id;
	}

	public String getReservation() {
		return reservation;
	}

	public void setReservation(String reservation) {
		this.reservation = reservation;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Person getDemandeur() {
		return demandeur;
	}

	public void setDemandeur(Person demandeur) {
		this.demandeur = demandeur;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}

	public Shop getVendeur() {
		return vendeur;
	}

	public void setVendeur(Shop vendeur) {
		this.vendeur = vendeur;
	}

	public String getPrix() {
		return prix;
	}

	public void setPrix(String prix) {
		this.prix = prix;
	}

	public String getSizeUE() {
		return sizeUE;
	}

	public void setSizeUE(String sizeUE) {
		this.sizeUE = sizeUE;
	}

	public String getSizeUS() {
		return sizeUS;
	}

	public void setSizeUS(String sizeUS) {
		this.sizeUS = sizeUS;
	}

	public String getSizeCM() {
		return sizeCM;
	}

	public void setSizeCM(String sizeCM) {
		this.sizeCM = sizeCM;
	}

	public Set<Shop> getShoesByShopAsked() {
		return shoesByShopAsked;
	}

	public void setShoesByShopAsked(Set<Shop> shoesByShopAsked) {
		this.shoesByShopAsked = shoesByShopAsked;
	}
	
	public void addShop (Shop shop) {
		this.shoesByShopAsked.add(shop);
		shop.getShoesByShopAsked().add(this);
	}
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getState() {
		return state;
	}

	public byte[] getImageFounded() {
		return imageFounded;
	}

	public void setImageFounded(byte[] imageFounded) {
		this.imageFounded = imageFounded;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
	    return Objects.hashCode(idShoe);
	}
	  
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Shoe other = (Shoe) obj;
	    return Objects.equals(idShoe, other.getIdShoe());
	}

}
