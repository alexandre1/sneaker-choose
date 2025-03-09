package org.vaadin.example.sneaker.service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Person;
import org.vaadin.example.entity.Shoe;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.repository.ShoeRepository;

@Service
public class ShoeService {
	
	private static final Logger LOGGER = Logger.getLogger(ShoeService.class.getName());
	
	private ShoeRepository shoeRepository;
	
	public ShoeService (ShoeRepository shoeRepository) {
		this.shoeRepository = shoeRepository;
	}
	
	public Shoe findByIdShoe(int idShoe) {
		return this.shoeRepository.findByIdShoe(idShoe);
	}
	
	public List<Shoe> findAllByDemandeur (Person demandeur) {
		return this.shoeRepository.findAllByDemandeur(demandeur);
	}
	@Transactional
	public void save(Shoe shoe) {
		this.shoeRepository.save(shoe);
	}

	public List<Shoe> findAllByVendeur (Shop vendeur) {
		return this.shoeRepository.findAllByVendeur(vendeur);
	}
	
	public Set<Shoe> findAllByShop(int idShop) {
		return this.shoeRepository.findAllByShop(idShop);
	}

	public void updateShoeSeller(String reservation, String prix,Shoe shoes) {
		this.shoeRepository.updateShoeSeller(reservation, prix, shoes.getIdShoe(), "Disponible", shoes.getImageFounded());
	}

	public void updateShoeSellerSold(String reservation, String prix,Shoe shoes) {
		this.shoeRepository.updateShoeSeller(reservation, prix, shoes.getIdShoe(), "Vendue", shoes.getImageFounded());
	}

}
