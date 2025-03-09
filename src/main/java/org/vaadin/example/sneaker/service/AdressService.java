package org.vaadin.example.sneaker.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Address;
import org.vaadin.example.repository.AdressRepository;

@Service
public class AdressService {
	private static final Logger LOGGER = Logger.getLogger(AdressService.class.getName());
	private AdressRepository contactRepository;
	

	public AdressService(AdressRepository contactRepository){ 
		this.contactRepository = contactRepository;
		
	}

	public List<Address> findAll() {
		return contactRepository.findAll();
	}

	public Address findById (int id) {
		return contactRepository.findById(id);
	}
	public long count() {
		return contactRepository.count();
	}

	public void delete(Address address) {
		contactRepository.delete(address);
	}

	public void save(Address contact) {
		if (contact == null) { 
			LOGGER.log(Level.SEVERE,
					"Contact is null. Are you sure you have connected your form to the application?");
			return;
		}
		contactRepository.save(contact);
	}

	public void update (Address adress) {
		if (adress == null) {
		LOGGER.log(Level.SEVERE,			
					"Contact is null. Are you sure you have connected your form to the application?");
		return;
		}
		contactRepository.update(adress.getCountry(), adress.getAdress(), adress.getAdress2(), adress.getCity(), adress.getNpa(), adress.getId());		
	}
}