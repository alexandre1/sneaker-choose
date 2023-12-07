package org.vaadin.example.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.example.Person;
import org.vaadin.example.Shoe;
import org.vaadin.example.Shop;

public interface ShoeRepository  extends JpaRepository<Shoe, Long>  {
	
	public Shoe findByIdShoe(int idShoe);
	
	public List<Shoe> findAllByVendeur (Shop vendeur);
	
	public List<Shoe> findAllByDemandeur(Person demandeur);	

	@Query("select s from Shoe s where s.vendeur.idShop = :idShop")
	public Set<Shoe> findAllByShop(int idShop);

	@Transactional
	@Modifying
	@Query("update Shoe s set s.reservation = ?1, s.prix = ?2, s.state= ?4, s.imageFounded = ?5 where s.id = ?3")
	public void updateShoeSeller(String reservation, String prix, int id, String state, byte  [] imageFounded);


}
