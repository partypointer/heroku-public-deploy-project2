package it.uniroma3.siw.spring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Band;

public interface BandRepository extends CrudRepository<Band, Long> {

	public Optional<Band> findByNome(String name);

}

