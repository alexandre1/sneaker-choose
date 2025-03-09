package org.vaadin.example.sneaker.service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Brand;
import org.vaadin.example.repository.BrandRepository;

@Service
public class BrandService {

	private static final Logger LOGGER = Logger.getLogger(BrandService.class.getName());
	
	private BrandRepository brandRepository;
	
	public BrandService (BrandRepository brandRepository) {
		this.brandRepository = brandRepository;
	}

	public List<Brand> findAll() {
		return this.brandRepository.findAll();
	}
	
	public Set<Brand> findByName(String name) {
		return this.brandRepository.findByName(name);
	}


	public void save(Brand brand) {
		this.brandRepository.save(brand);				
	}
	
	public void update (Brand brand) {
		this.brandRepository.update (brand.getIdBrand(), brand.getName());
	}
	
	public List<Brand> findAllByUsername(String email) {
		return this.brandRepository.findAllByUsername(email);
	}
	
	public Brand findByIdBrand (int idBrand) {
		return this.brandRepository.findByIdBrand(idBrand);
	}
}

