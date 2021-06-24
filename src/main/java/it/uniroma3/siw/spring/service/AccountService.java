package it.uniroma3.siw.spring.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.model.Biglietto;
import it.uniroma3.siw.spring.repository.AccountRepository;

@Service
public class AccountService {
	
    @Autowired
    protected PasswordEncoder passwordEncoder;

	@Autowired
	protected AccountRepository accountRepository;
	
	@Transactional
	public Account getAccount(Long id) {
		Optional<Account> result = this.accountRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Account getAccount(String username) {
		Optional<Account> result = this.accountRepository.findByUsername(username);
		return result.orElse(null);
	}

	/** Attenzione! Ricodifica la password! **/
    @Transactional
    public Account saveAccount(Account Account) {
        Account.setRuolo(Account.DEFAULT_RUOLO);
        Account.setPassword(this.passwordEncoder.encode(Account.getPassword()));
        return this.accountRepository.save(Account);
    }

    /** Rispetto a save account non ricodifica la password! **/
    @Transactional
    public Account updateAccount(Account Account) {
        return this.accountRepository.save(Account);
    }
    
    /** Non è @Transactional in quanto lo è la funzione che invoca **/
	public boolean alreadyExists(Account account) {
		if(this.getAccount(account.getId()) != null) return true;
		return false;
	}
}
