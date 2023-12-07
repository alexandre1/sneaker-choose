package org.vaadin.example;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

@Entity
public class Brand {


	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public int idBrand;
	
	public String name;

	 @ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.MERGE)
     @JoinTable( name = "Brands_Shops_Associations",
     						joinColumns = @JoinColumn( name = "idBrand" ),
     						inverseJoinColumns = @JoinColumn( name = "idShop" ),
    						uniqueConstraints = @UniqueConstraint(columnNames = {
    					             "idBrand", "idShop" })
    
     )

	public Set<Shop> shopByBrand = new HashSet<Shop>();

	@Override
	public int hashCode() {
	    return Objects.hashCode(idBrand);
	}
	  
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Brand other = (Brand) obj;
	    return Objects.equals(idBrand, other.getIdBrand());
	}
	  
	public Set<Shop> getShopByBrand() {
		return shopByBrand;
	}

	public void setShopByBrand(Set<Shop> shopByBrand) {
		this.shopByBrand = shopByBrand;
	}

	public int getIdBrand() {
		return idBrand;
	}

	public void setIdBrand(int id) {
		this.idBrand = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addShop(Shop shop) {
		this.shopByBrand.add(shop);
		shop.getBrandsByShop().add(this);
	}

}
