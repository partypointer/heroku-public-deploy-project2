package it.uniroma3.siw.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.model.Biglietto;

public interface BigliettoRepository extends CrudRepository<Biglietto, Long> {
	//public List<Biglietto> findByProprietario(Account proprietario);
	public List<Biglietto> findByProprietario(Account proprietario);

	@Modifying
	public int removeById(Long id);

	@Modifying
	public void deleteById(Long id);
}
