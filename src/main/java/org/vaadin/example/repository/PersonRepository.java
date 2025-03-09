package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.example.entity.Address;
import org.vaadin.example.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long>  {
	
	public Person findById(int id);
	
	public Person findByEmailAndPassword(String email, String password);
	
	public Person findByEmail(String email);
	
	public Person findByStripeId (String stripeId);
	
	public Person findBySessionId(String sessionId);

	@Transactional
	@Modifying
	@Query("update person p set p.firstName = ?1, p.lastName = ?2, p.phoneNumber = ?3, p.password = ?4, p.address = ?5 , p.imageProfil = ?7 where p.id = ?6")
	public void update(String firstname, String lastname,String phoneNumber,String password,Address adress, int userId, byte [] imageProfil);

	@Transactional
	@Modifying
	@Query("update person p set p.active = ?2 where p.id = ?1")
	public void confirmRegistration(int userId, boolean active);

	@Transactional
	@Modifying
	@Query("update person p set p.firstName = ?1, p.lastName = ?2, p.phoneNumber = ?3, p.password = ?4, p.address = ?5 , p.imageProfil = ?7 , p.stripeId = ?8, session_id = ?9, subscription_id  = ?10 where p.id = ?6")
	public void update(String firstname, String lastname,String phoneNumber,String password,Address adress, int userId, byte [] imageProfil, String stripeId, String sessionId, String  subscriptionId);

	@Transactional
	@Modifying
	@Query("update person p set p.firstName = ?1, p.lastName = ?2, p.phoneNumber = ?3, p.password = ?4, p.address = ?5 , p.imageProfil = ?7 , p.stripeId = ?8, session_id = ?9  where p.id = ?6")
	public void update(String firstname, String lastname,String phoneNumber,String password,Address adress, int userId, byte [] imageProfil, String stripeId, String sessionId);

}
