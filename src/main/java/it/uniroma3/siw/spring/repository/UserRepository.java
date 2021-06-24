package it.uniroma3.siw.spring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	public Optional<User> findByNomeAndCognome(String nome, String cognome);

}