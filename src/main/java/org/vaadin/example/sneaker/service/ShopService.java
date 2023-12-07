package org.vaadin.example.sneaker.service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.example.Address;
import org.vaadin.example.Person;
import org.vaadin.example.Shoe;
import org.vaadin.example.Shop;
import org.vaadin.example.repository.ShopRepository;

@Service
public class ShopService {
	private static final Logger LOGGER = Logger.getLogger(ShopService.class.getName());
	private ShopRepository shopRepository;
	

	public ShopService(ShopRepository shopRepository){ 
		this.shopRepository = shopRepository;		
	}

	public void save (Shop shop) {
		this.shopRepository.save(shop);
	}
	
	public Shop findByEmailAndAdress(String email, Address adresse) {
		return this.shopRepository.findByEmailAndAdresse(email, adresse);
	}
	
	public Shop findByEmail(String email ) {
		LOGGER.info("EMAIL TO SEARCH :  " + email);
		return this.shopRepository.findByEmail(email);
	}
	
	public void update(String name, String email,String telephone,Person gerant,Address adress, int id, byte [] image) {
		this.shopRepository.update(name, email, telephone, gerant, adress, id, image);

	}
	
	public void confirmRegistration(Shop shop) {
		this.shopRepository.confirmRegistration(new Long (shop.getIdShop()), true);
	}

	public List<Shop> findAllByBrand (String name) {
		return this.shopRepository.findAllByBrand(name);
	}
	
	public List<Shop> findAll() {
		return this.shopRepository.findAll();
	}
	
	public Set<Shoe>  findAllByShop(String email) {
		return this.findAllByShop(email);
	}
	
	public Set<Shoe> findAllByShop(int idShop) {
		return this.shopRepository.findAllByShop(idShop);
	}
}
