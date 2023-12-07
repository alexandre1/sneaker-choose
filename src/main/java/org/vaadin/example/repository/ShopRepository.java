package org.vaadin.example.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.example.Address;
import org.vaadin.example.Person;
import org.vaadin.example.Shoe;
import org.vaadin.example.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long>{

	
	public Shop findByEmailAndAdresse(String email, Address adresse);
	
	public Shop findByEmail(String email);
	
	public List<Shop> findAll();
	
	@Query("select s from Shoe s where s.vendeur.idShop = :idShop")
	public Set<Shoe> findAllByShop(int idShop);

	@Query("select s from Shop s join Brand u where u.name = :name")
	public List<Shop> findAllByBrand (@Param("name") String name);
	
	@Query("select s from Shoe s where s.vendeur.idShop = :idShop")
	public Set<Shoe> findAllByShoes(int idShop);
	
	@Transactional
	@Modifying
	@Query("update Shop s set s.name = ?1, s.email = ?2, s.telephone = ?3, s.gerant = ?4, s.adresse = ?5, s.image = ?7 where s.idShop = ?6")
	public void update(String name, String email,String telephone,Person gerant,Address adress, int idShop, byte [] image);

	@Transactional
	@Modifying
	@Query("update Shop p set p.active = ?2 where p.id = ?1")
	public void confirmRegistration(Long shopId, boolean active);

}
