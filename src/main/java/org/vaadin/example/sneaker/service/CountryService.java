package org.vaadin.example.sneaker.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.vaadin.example.Address;
import org.vaadin.example.Country;
import org.vaadin.example.repository.AdressRepository;
import org.vaadin.example.repository.CountryRepository;

@Service
public class CountryService {
	private static final Logger LOGGER = Logger.getLogger(CountryService.class.getName());
	private CountryRepository countryRepository;
	

	public CountryService(CountryRepository countryRepository){ 
		this.countryRepository = countryRepository;
		
	}

	public List<Country> findAll() {
		return countryRepository.findAll();
	}

	public List<Country> findByLibelle (String libelle) {
		return countryRepository.findByLibelle(libelle);
	}
}
