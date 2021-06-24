package it.uniroma3.siw.spring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Concerto;

public interface ConcertoRepository extends CrudRepository<Concerto, Long> {

	public Optional<Concerto> findByNome(String name);
	
}

