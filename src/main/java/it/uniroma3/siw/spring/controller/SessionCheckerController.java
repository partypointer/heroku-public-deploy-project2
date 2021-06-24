package it.uniroma3.siw.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.service.AccountService;

@Controller
public class SessionCheckerController {

	@Autowired
	private AccountService accountService;
	
	protected boolean checkIfCurrentAccountExists(Model model) {
		Account accountCorrente = (Account) model.getAttribute("accountCorrente");
		if(accountService.getAccount(accountCorrente.getId()) != null) return true;
		return false;
	}
}
