package it.uniroma3.siw.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.repository.ConcertoRepository;

@Service
public class ConcertoService {

    @Autowired
    protected ConcertoRepository concertoRepository;

    @Transactional
    public Concerto getConcerto(Long id) {
        Optional<Concerto> result = this.concertoRepository.findById(id);
        return result.orElse(null);
    }
    
    @Transactional
	public Concerto getConcerto(String name) {
    	Optional<Concerto> result = this.concertoRepository.findByNome(name);
		return result.orElse(null);
	}

    @Transactional
    public Concerto saveConcerto(Concerto concerto) {
        return this.concertoRepository.save(concerto);
    }

    @Transactional
    public List<Concerto> getAllConcerti() {
        List<Concerto> result = new ArrayList<>();
        Iterable<Concerto> iterable = this.concertoRepository.findAll();
        for(Concerto concerto : iterable)
            result.add(concerto);
        return result;
    }

    @Transactional
    public List<Concerto> getAllConcertiBeforeToday() {
        List<Concerto> result = new ArrayList<>();
        Iterable<Concerto> iterable = this.concertoRepository.findAll();
        LocalDate now = LocalDate.now();
        for(Concerto concerto : iterable)
            if(concerto.getDataInizio().compareTo(now) > 0) result.add(concerto);
        return result;
    }

    /** Non è @Transactional in quanto lo è la funzione che invoca **/
	public boolean alreadyExists(Concerto concerto) {
		if(this.getConcerto(concerto.getId()) != null) return true;
		return false;
	}
	/** Non è @Transactional in quanto lo è la funzione che invoca **/
	public boolean alreadyExists(String nome) {
		if(this.getConcerto(nome) != null) return true;
		return false;
	}
}