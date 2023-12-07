package org.vaadin.example.sneaker.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.vaadin.example.Person;
import org.vaadin.example.repository.PersonRepository;

@Service
public class PersonService {
	private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());
	private PersonRepository contactRepository;
	

	public PersonService(PersonRepository contactRepository){ 
		this.contactRepository = contactRepository;		
	}

	public Person findByEmailAndPassword(String email, String password) {
		return this.contactRepository.findByEmailAndPassword(email, password);
	}
	public Person findByEmail(String email) {
		return this.contactRepository.findByEmail(email);
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

	public void update (Person person) {
		if (person == null) {
			LOGGER.log(Level.SEVERE,
					"Contact is null. Are you sure you have connected your form to the application?");
			return;
		}
		contactRepository.update(person.getFirstName(), person.getLastName(), person.getPhoneNumber(), person.getPassword(),person.getAddress(),person.getId(),person.getImageProfil());
	}

	
	public void update (String stripeId,Person person) {
		if (person == null) {
			LOGGER.log(Level.SEVERE,
					"Contact is null. Are you sure you have connected your form to the application?");
			return;
		}
		contactRepository.update(person.getFirstName(), person.getLastName(), person.getPhoneNumber(), person.getPassword(),person.getAddress(),person.getId(),person.getImageProfil(), stripeId, person.getSessionId());
	}

	public void update (String stripeId,Person person, String subscriptionId) {
		if (person == null) {
			LOGGER.log(Level.SEVERE,
					"Contact is null. Are you sure you have connected your form to the application?");
			return;
		}
		contactRepository.update(person.getFirstName(), person.getLastName(), person.getPhoneNumber(), person.getPassword(),person.getAddress(),person.getId(),person.getImageProfil(), stripeId, person.getSessionId(), person.getSubscriptionId());
	}

	public void confirmRegistation (Person person) {
		if (person == null) {
			LOGGER.log(Level.SEVERE,
					"Contact is null. Are you sure you have connected your form to the application?");
			return;
		}
		contactRepository.confirmRegistration(person.getId(), true);
	}
	
	public Person findByStripeId (String stripeId) {
		Person person = contactRepository.findByStripeId(stripeId);
		System.out.println(person.getEmail());
		return person;
	}


	public Person findBySessionId(String sessionId) {
		return this.contactRepository.findBySessionId(sessionId);
	}
}
