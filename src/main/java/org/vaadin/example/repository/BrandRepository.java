package org.vaadin.example.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.example.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	public Brand findByIdBrand (int idBrand);
	
	public Set<Brand> findByName(String name);

	
	@Query("select b from Brand b join b.shopByBrand s where s.email = :email")
	public List<Brand> findAllByUsername(@Param("email") String email);

	@Transactional
	@Modifying
	@Query("update Brand b set b.name = ?2 where b.idBrand = ?1")
	public void update (int id, String brand);
}
