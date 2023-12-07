package org.vaadin.example;

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
public class Shop {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int idShop;
	
	@OneToOne
	private Address adresse;
	
	private String telephone;
	
	private String name;
	
	private String email;

	@OneToOne
	private Person gerant;
	
	private String photo;
	
	private boolean active;
	
	 @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
     @JoinTable( name = "Brands_Shops_Associations",
     						joinColumns = @JoinColumn( name = "idShop" ),
     						inverseJoinColumns = @JoinColumn( name = "idBrand" ),
     						uniqueConstraints = @UniqueConstraint(columnNames = {
     					             "idShop", "idBrand" }))
     
     
	public Set<Brand> brandsByShop = new HashSet<Brand>();
	
	 
	 @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
     @JoinTable( name = "Shoes_Asked_By_Shops_Associations",
     						joinColumns = @JoinColumn( name = "idShop" ),
     						inverseJoinColumns = @JoinColumn( name = "idShoe" ),
     						uniqueConstraints = @UniqueConstraint(columnNames = {
     					             "idShop", "idShoe" }))
     
     

	public Set<Shoe> shoesByShopAsked = new HashSet<Shoe>();
	 
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte  [] image;
	
	private String web;
	
	
	public Shop () {
		
	}
	@Override
	public int hashCode() {
	    return Objects.hashCode(idShop);
	}
	  
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Shop other = (Shop) obj;
	    return Objects.equals(idShop, other.getIdShop());
	}

	public int getIdShop() {
		return idShop;
	}

	public String getNom() {
		return name;
	}

	public void setNom(String name) {
		this.name = name;
	}

	public void setIdShop(int id) {
		this.idShop = id;
	}

	public Address getAdresse() {
		return adresse;
	}

	public void setAdresse(Address adress) {
		this.adresse = adress;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String phoneNumber) {
		this.telephone = phoneNumber;
	}


	public void setGerant(Person gerant) {
		this.gerant = gerant;
	}

	public Person getGerant() {
		return this.gerant;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<Brand> getBrandsByShop() {
		return brandsByShop;
	}

	public void setBrandsByShop(Set<Brand> brandsByShop) {
		this.brandsByShop = brandsByShop;
	}

	public void addBrand(Brand brand) {
		this.brandsByShop.add(brand);
		brand.getShopByBrand().add(this);
	}
	public Set<Shoe> getShoesByShopAsked() {
		return shoesByShopAsked;
	}
	public void setShoesByShopAsked(Set<Shoe> shoesByShopAsked) {
		this.shoesByShopAsked = shoesByShopAsked;
		
	}
	
	public void addShoe(Shoe shoe) {
		this.shoesByShopAsked.add(shoe);
		shoe.getShoesByShopAsked().add(this);
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}

}