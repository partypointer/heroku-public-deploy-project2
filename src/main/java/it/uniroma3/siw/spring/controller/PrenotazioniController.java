package it.uniroma3.siw.spring.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.spring.controller.validator.AccountValidator;
import it.uniroma3.siw.spring.controller.validator.ConcertoValidator;
import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.model.Biglietto;
import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.model.TipologiaPosto;
import it.uniroma3.siw.spring.service.AccountService;
import it.uniroma3.siw.spring.service.BigliettoService;
import it.uniroma3.siw.spring.service.ConcertoService;
import it.uniroma3.siw.spring.service.TipologiaPostoService;

//@Controller
public class PrenotazioniController extends AuthenticationController{

	@Autowired
	private ConcertoService concertoService;

	@Autowired
	private BigliettoService bigliettoService;
	
	@Autowired
	private TipologiaPostoService tipologiaPostoService;
	
	@Autowired
	private AccountService accountService;
	
	private static final Logger logger = LoggerFactory.getLogger(ConcertoValidator.class);

	
	/** Restituisce l'account corrente **/
	private Account getAccount() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		
		if(principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		}else username = principal.toString();
		
		return this.accountService.getAccount(username);
	}
	
	/** Controlla se l'account corrente ha lo stesso username dato come parametro
	 * Può evitare che un utente possa vedere i dati di un'altro utente **/
	private boolean isCurrentAccountValid(String username) {
		if(this.getAccount().getUsername().compareTo(username) == 0) return true;
		return false;
	}
	
	@RequestMapping(value = {"/prenotazioneFail"}, method = RequestMethod.GET)
	public String prenotazioneFail(Model model) {
		return "prenotazioneFail";
	}
	
	@RequestMapping(value = {"/prenotazioni"}, method = RequestMethod.GET)
	public String prenotazioni(@ModelAttribute("accountCorrente") Account accountCorrente, Model model) {
		logger.debug("MYINFO) OMG 1 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId());
		model.addAttribute("concerti", this.concertoService.getAllConcertiBeforeToday());
		model.addAttribute("biglietti", this.bigliettoService.getBigliettiFromAccount(accountCorrente));
		return "prenotazioni";
	}

	@RequestMapping(value = {"/addConcerto/{id}"}, method = RequestMethod.GET)
	public String addConcerto(@PathVariable("id") Long idConcerto, Model model) {
		Concerto concertoScelto = concertoService.getConcerto(idConcerto);
		model.addAttribute("concertoScelto", concertoScelto);
		model.addAttribute("tipologiePosti", tipologiaPostoService.getTipologiaPostoByConcerto(concertoScelto));
		return "addConcerto";
	}

	@RequestMapping(value = {"/addBiglietto/{id}"}, method = RequestMethod.GET)
	public String addBiglietto(@PathVariable("id") Long idTipologiaPosto, Model model) {
		
		TipologiaPosto tipologiaPosto = tipologiaPostoService.getTipologiaPosto(idTipologiaPosto);
		
		/* Per il momento vi è un hardcoded limit pari a 5 prenotazioni alla volta */
		int postiAttualmentePrenotabili = tipologiaPosto.getPostiAttualmentePrenotabili().intValue();
		int maximumQuantity = 5;
		if(postiAttualmentePrenotabili <= 5) maximumQuantity = postiAttualmentePrenotabili;

		logger.debug("MYINFO) tipologiaPosto inside addBiglietto: " + tipologiaPosto.getNome());
		logger.debug("MYINFO) getPostiAttualmentePrenotabili: " + tipologiaPosto.getPostiAttualmentePrenotabili().intValue());
		
		model.addAttribute("tipologiaPosto", tipologiaPosto);
		model.addAttribute("maximumQuantity", maximumQuantity);
		return "addBiglietto";
	}
	
	@RequestMapping(value = {"/concludiOrdine/{id}"}, method = RequestMethod.POST)
	public String concludiOrdine(@RequestParam("secretValue") Long sliderValue,
						@PathVariable("id") Long idTipologiaPosto,
					@ModelAttribute("accountCorrente") Account accountCorrente,
																	Model model) {
		TipologiaPosto tipologiaPosto = tipologiaPostoService.getTipologiaPosto(idTipologiaPosto);
		logger.debug("MYINFO) sliderValue: " + sliderValue);
		int ticketQuantity = (sliderValue).intValue();
		//int ticketQuantity = (sliderValue).parseInt();
		
		logger.debug("MYINFO) OMG 2 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId());
		
		/* E' possibile effettuare la prenotazione*/
		if(tipologiaPosto.checkDisponibilitaPrenotazione(ticketQuantity)) {
			/* Vengono ridotte le quantità di posti disponibili da prenotare 
			 * per la determinata tipologia di posto */
			tipologiaPosto.riduciPosti(ticketQuantity);
			
			/* Viene presa la lista di biglietti dell'account corrente per poterci
			 * aggiungere i nuovi biglietti */
			List<Biglietto> bigliettiAccount = accountCorrente.getBiglietti();
			
			/* Generazione e aggiunta dei nuovi biglietti in una lista dedicata e
			 * nella lista dei biglietti dell'account corrente  */
			List<Biglietto> nuoviBiglietti = new ArrayList<Biglietto>();
			for(int i = 0; i < ticketQuantity ;i++) {
				/* Impostazione del nuovo biglietto */
				Biglietto nuovoBiglietto = new Biglietto();
				nuovoBiglietto.setDataAcquisizione(LocalDateTime.now());
				nuovoBiglietto.setProprietario(accountCorrente);
				nuovoBiglietto.setTipologiaPosto(tipologiaPosto);
				nuovoBiglietto.generateCode(i);
				
				/* Aggiornamento lista biglietti dell'account corrente */
				bigliettiAccount.add(nuovoBiglietto);
				/* Aggiornamento lista dei nuovi biglietti da passare al
				 * prossimo mapping */
				nuoviBiglietti.add(nuovoBiglietto);
				/* Salvataggio persistente del nuovo biglietto corrente */
				bigliettoService.saveBiglietto(nuovoBiglietto);
			}
			
			/* Si aggiorna la lista dei biglietti dell'account corrente */
			accountCorrente.setBiglietti(bigliettiAccount);
			/* Si aggiorna l'account con i nuovi biglietti */
			accountService.saveAccount(accountCorrente);
			
			model.addAttribute("nuoviBiglietti", nuoviBiglietti);
			return "concludiOrdine"; 
		}
		
		return "prenotazioneFail";
	}

}