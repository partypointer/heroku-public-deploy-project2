package it.uniroma3.siw.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.spring.controller.validator.TipologiaPostoValidator;
import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.model.TipologiaPosto;
import it.uniroma3.siw.spring.repository.TipologiaPostoRepository;

@Service
public class TipologiaPostoService {

    @Autowired
    protected TipologiaPostoRepository tipologiaPostoRepository;

    @Transactional
    public TipologiaPosto getTipologiaPosto(Long id) {
        Optional<TipologiaPosto> result = this.tipologiaPostoRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public List<TipologiaPosto> getTipologiaPostoByConcerto(Concerto concerto) {
    	return this.tipologiaPostoRepository.findByConcerto(concerto);
    }

    @Transactional
    public TipologiaPosto saveTipologiaPosto(TipologiaPosto tipologiaPosto) {
        return this.tipologiaPostoRepository.save(tipologiaPosto);
    }

    @Transactional
    public List<TipologiaPosto> getAllTipologiePosti() {
        List<TipologiaPosto> result = new ArrayList<>();
        Iterable<TipologiaPosto> iterable = this.tipologiaPostoRepository.findAll();
        for(TipologiaPosto tipologiaPosto : iterable)
            result.add(tipologiaPosto);
        return result;
    }
    

    /** Non è @Transactional in quanto lo è la funzione che invoca **/
	public boolean alreadyExists(TipologiaPosto tipologiaPosto) {
		if(this.getTipologiaPosto(tipologiaPosto.getId()) != null) return true;
		return false;
	}

	/** Non è @Transactional in quanto lo è la funzione che invoca **/
	public boolean alreadyExists(String nomeTipologiaPosto, Concerto concerto) {
		if(this.getTipologiaPostoByNomeAndConcerto(nomeTipologiaPosto, concerto) != null) return true;
		return false;
	}
	
    @Transactional
    public TipologiaPosto getTipologiaPostoByNomeAndConcerto(String nomeTipologiaPosto, Concerto concerto) {
        Optional<TipologiaPosto> result = this.tipologiaPostoRepository.findByNomeAndConcerto(nomeTipologiaPosto, concerto);
        return result.orElse(null);
    }
	
    /* For testing purposes */
	/*
    private static final Logger logger = LoggerFactory.getLogger(TipologiaPostoValidator.class);
	public TipologiaPostoRepository getTipologiaPostoRepository() {
		return this.tipologiaPostoRepository;
	}
    */
}