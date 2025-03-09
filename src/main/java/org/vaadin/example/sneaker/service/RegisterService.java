package org.vaadin.example.sneaker.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Person;
import org.vaadin.example.repository.PersonRepository;

@Service
public class RegisterService {

	private static final Logger LOGGER = Logger.getLogger(RegisterService.class.getName());
	private PersonRepository contactRepository;
	

	public RegisterService(PersonRepository contactRepository){ 
		this.contactRepository = contactRepository;
		
	}

	public List<Person> findAll() {
		return contactRepository.findAll();
	}

	public long count() {
		return contactRepository.count();
	}

	public void delete(Person contact) {
		contactRepository.delete(contact);
	}

	public void save(Person contact) {
		if (contact == null) { 
			LOGGER.log(Level.SEVERE,
					"Contact is null. Are you sure you have connected your form to the application?");
			return;
		}
		contactRepository.save(contact);
	}

}
