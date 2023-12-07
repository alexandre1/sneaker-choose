package org.vaadin.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.Country;

public interface CountryRepository  extends JpaRepository<Country, Long> {

	public List<Country> findByLibelle (String libelle);
}
