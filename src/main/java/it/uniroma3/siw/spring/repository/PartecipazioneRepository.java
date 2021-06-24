package it.uniroma3.siw.spring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Band;
import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.model.Partecipazione;

public interface PartecipazioneRepository extends CrudRepository<Partecipazione, Long> {
	public Optional<Partecipazione> findByConcertoAndBand(Concerto concerto, Band band);
}