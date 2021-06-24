package it.uniroma3.siw.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.model.TipologiaPosto;

public interface TipologiaPostoRepository extends CrudRepository<TipologiaPosto, Long> {
	
	public List<TipologiaPosto> findByConcerto(Concerto concerto);
	
	public Optional<TipologiaPosto> findByNomeAndConcerto(String nome, Concerto concerto);
	
}