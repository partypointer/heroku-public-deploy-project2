package it.uniroma3.siw.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.model.Band;
import it.uniroma3.siw.spring.repository.BandRepository;

@Service
public class BandService {

    @Autowired
    protected BandRepository bandRepository;

    @Transactional
    public Band getBand(Long id) {
        Optional<Band> result = this.bandRepository.findById(id);
        return result.orElse(null);
    }
    
    @Transactional
	public Band getBand(String nome) {
    	Optional<Band> result = this.bandRepository.findByNome(nome);
		return result.orElse(null);
	}

    @Transactional
    public Band saveBand(Band band) {
        return this.bandRepository.save(band);
    }

    @Transactional
    public List<Band> getAllBands() {
        List<Band> result = new ArrayList<>();
        Iterable<Band> iterable = this.bandRepository.findAll();
        for(Band band : iterable)
            result.add(band);
        return result;
    }

    /** Non è @Transactional in quanto lo è la funzione che invoca **/
	public boolean alreadyExists(Band band) {
		if(this.getBand(band.getId()) != null) return true;
		return false;
	}
	public boolean alreadyExists(Long id) {
		if(this.getBand(id) != null) return true;
		return false;
	}
	public boolean alreadyExists(String name) {
		if(this.getBand(name) != null) return true;
		return false;
	}
}