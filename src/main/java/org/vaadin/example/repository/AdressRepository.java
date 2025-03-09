package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.example.entity.Address;

public interface AdressRepository extends JpaRepository<Address, Long>  {

	public Address findById (int id);

	@Transactional
	@Modifying
	@Query("update Address a set a.country = ?1, a.adress = ?2, a.adress2 = ?3, a.city = ?4, a.npa = ?5 where a.id = ?6")	
	public void update (String country, String adress, String adress2, String city, String npa, int id);
}
