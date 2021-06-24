package it.uniroma3.siw.spring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

	public Optional<Account> findByUsername(String username);

}