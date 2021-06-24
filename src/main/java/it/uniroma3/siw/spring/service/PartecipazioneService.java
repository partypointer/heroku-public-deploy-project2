package it.uniroma3.siw.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.model.Band;
import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.model.Partecipazione;
import it.uniroma3.siw.spring.repository.PartecipazioneRepository;

@Service
public class PartecipazioneService {

    @Autowired
    protected PartecipazioneRepository partecipazioneRepository;

    @Transactional
    public Partecipazione getPartecipazione(Long id) {
        Optional<Partecipazione> result = this.partecipazioneRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Partecipazione getPartecipazione(Concerto concerto, Band band) {
        Optional<Partecipazione> result = this.partecipazioneRepository.findByConcertoAndBand(concerto, band);
        return result.orElse(null);
    }

    @Transactional
    public Partecipazione savePartecipazione(Partecipazione partecipazione) {
        return this.partecipazioneRepository.save(partecipazione);
    }

    @Transactional
    public List<Partecipazione> getAllPartecipazioni() {
        List<Partecipazione> result = new ArrayList<>();
        Iterable<Partecipazione> iterable = this.partecipazioneRepository.findAll();
        for(Partecipazione partecipazione : iterable)
            result.add(partecipazione);
        return result;
    }
    
    /** Non è @Transactional in quanto lo è la funzione che invoca **/
	public boolean alreadyExists(Partecipazione partecipazione) {
		if(this.getPartecipazione(partecipazione.getId()) != null) return true;
		return false;
	}
}