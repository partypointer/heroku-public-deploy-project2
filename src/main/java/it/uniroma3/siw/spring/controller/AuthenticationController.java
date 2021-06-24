package it.uniroma3.siw.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelExtensionsKt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import it.uniroma3.siw.spring.controller.validator.AccountValidator;
import it.uniroma3.siw.spring.controller.validator.ConcertoValidator;
import it.uniroma3.siw.spring.controller.validator.UserValidator;
import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.model.Biglietto;
import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.model.TipologiaPosto;
import it.uniroma3.siw.spring.model.User;
import it.uniroma3.siw.spring.repository.AccountRepository;
import it.uniroma3.siw.spring.service.AccountService;
import it.uniroma3.siw.spring.service.BigliettoService;
import it.uniroma3.siw.spring.service.ConcertoService;
import it.uniroma3.siw.spring.service.TipologiaPostoService;
import it.uniroma3.siw.spring.service.UserService;

@Controller
@SessionAttributes("accountCorrente")
public class AuthenticationController{
	
	@Autowired
	private ConcertoService concertoService;

	@Autowired
	private BigliettoService bigliettoService;
	
	@Autowired
	private TipologiaPostoService tipologiaPostoService;

	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountValidator accountValidator;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountValidator.class);

	@RequestMapping(value = "/logout", method = RequestMethod.GET) 
	public String logout(Model model) {
		return "index";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET) 
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("account", new Account());
		return "registrationForm";
	}
	
	/** Invocata quando un utente che ha visualizzato la pagina di registrazione
	 * ha digitato i propri dati personali nel form e preme conferma.
	 * A questo punto, se i dati sono "corretti" verrà restituita una pagina di
	 * conferma della registrazione, altrimenti **/
    @RequestMapping(value = { "/register" }, method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user,
                 				BindingResult userBindingResult,
                 	@ModelAttribute("account") Account account,
                 	BindingResult accountBindingResult,
                 Model model) {

        // validate user and credentials fields
        this.userValidator.validate(user, userBindingResult);
        this.accountValidator.validate(account, accountBindingResult);

        // if neither of them had invalid contents, store the User and the Credentials into the DB
        if(!userBindingResult.hasErrors() && !accountBindingResult.hasErrors()) {
        	account.setUser(user);
        	user.setAccount(account);
        	
            accountService.saveAccount(account);
            return "registrationSuccessful";
        }
        return "registrationForm";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET) 
	public String showLoginForm(Model model) {
    	Account accountCorrente = (Account) model.getAttribute("accountCorrente");

    	if(accountCorrente != null) {
			if(checkIfIsAccountIsAdministrator(accountCorrente)) return "admin/home";
			if(checkIfIsAccountIsDefault(accountCorrente)) return "default/home";
    	}
    	
		return "loginForm";
	}

	/* Se ha successo il login dell'utente, verrà controllato il suo ruolo */
	@RequestMapping(value = "/default", method = RequestMethod.GET)
    public String defaultAfterLogin(Model model) {
        
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Account accountPersistente = accountService.getAccount(userDetails.getUsername());

    	if(accountPersistente != null) {
        	model.addAttribute("accountCorrente", accountPersistente);
    		if(this.checkIfIsAccountIsAdministrator(accountPersistente)) {
    			logger.debug("MYINFO) ADMIN accountCorrente.username: " + accountPersistente.getUsername() + ", " + accountPersistente.getId());
    			return "admin/home";
        	}
        	else {
    			logger.debug("MYINFO) DEFAULT accountCorrente.username: " + accountPersistente.getUsername() + ", " + accountPersistente.getId());
    			return "default/home";
        	}
    	}
    	
        return "loginFail";
    }
	
	/** Controlla se l'account ha il ruolo di ADMIN **/
	public boolean checkIfIsAccountIsAdministrator(Account account) {
		if(account.getRuolo().equals(Account.ADMIN_RUOLO)) return true;
		return false;
	}
	
	/** Controlla se l'account ha il ruolo di DEFAULT USER **/
	public boolean checkIfIsAccountIsDefault(Account account) {
		if(account.getRuolo().equals(Account.DEFAULT_RUOLO)) return true;
		return false;
	}
	
	
	//-----------------------------------------------------------------------------------------
	
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
		Account accountCorrente = (Account) model.getAttribute("accountCorrente");
		logger.debug("MYINFO) TEST 4 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId());
		Concerto concertoScelto = concertoService.getConcerto(idConcerto);
		model.addAttribute("concertoScelto", concertoScelto);
		model.addAttribute("tipologiePosti", tipologiaPostoService.getTipologiaPostoByConcerto(concertoScelto));
		logger.debug("MYINFO) TEST 3 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId());
		return "addConcerto";
	}

	@RequestMapping(value = {"/addBiglietto/{id}"}, method = RequestMethod.GET)
	public String addBiglietto(@PathVariable("id") Long idTipologiaPosto, Model model) {
		Account accountCorrente = (Account) model.getAttribute("accountCorrente");
		logger.debug("MYINFO) TEST 2 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId());
		
		TipologiaPosto tipologiaPosto = tipologiaPostoService.getTipologiaPosto(idTipologiaPosto);
		
		/* Per il momento vi è un hardcoded limit pari a 5 prenotazioni alla volta */
		int postiAttualmentePrenotabili = tipologiaPosto.getPostiAttualmentePrenotabili().intValue();
		int maximumQuantity = 5;
		if(postiAttualmentePrenotabili <= 5) maximumQuantity = postiAttualmentePrenotabili;

		logger.debug("MYINFO) tipologiaPosto inside addBiglietto: " + tipologiaPosto.getNome());
		logger.debug("MYINFO) getPostiAttualmentePrenotabili: " + tipologiaPosto.getPostiAttualmentePrenotabili().intValue());
		
		model.addAttribute("tipologiaPosto", tipologiaPosto);
		model.addAttribute("maximumQuantity", maximumQuantity);
		logger.debug("MYINFO) TEST 1 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId());
		return "addBiglietto";
	}
	
	@RequestMapping(value = {"/concludiOrdine/{id}"}, method = RequestMethod.POST)
	public String concludiOrdine(@RequestParam("secretValue") Long sliderValue,
												@PathVariable("id") Long idTipologiaPosto,
																			Model model) {
		Account accountCorrente = (Account) model.getAttribute("accountCorrente");
		logger.debug("MYINFO) HELLO 1 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId());
		TipologiaPosto tipologiaPosto = tipologiaPostoService.getTipologiaPosto(idTipologiaPosto);
		
		logger.debug("MYINFO) sliderValue: " + sliderValue);
		int ticketQuantity = (sliderValue).intValue();
		
		logger.debug("MYINFO) HELLO 2 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
		
		/* E' possibile effettuare la prenotazione*/
		if(tipologiaPosto.checkDisponibilitaPrenotazione(ticketQuantity)) {
			/* Vengono ridotte le quantità di posti disponibili da prenotare 
			 * per la determinata tipologia di posto */
			tipologiaPosto.riduciPosti(ticketQuantity);
			logger.debug("MYINFO) HELLO 3 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());

			/* Viene presa la lista di biglietti dell'account corrente per poterci
			 * aggiungere i nuovi biglietti */
			List<Biglietto> bigliettiAccount = accountCorrente.getBiglietti();
			logger.debug("MYINFO) HELLO 4 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
			
			/* Generazione e aggiunta dei nuovi biglietti in una lista dedicata e
			 * nella lista dei biglietti dell'account corrente  */
			List<Biglietto> nuoviBiglietti = new ArrayList<Biglietto>();
			for(int i = 0; i < ticketQuantity ;i++) {
				logger.debug("MYINFO) HELLO 5 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
				/* Impostazione del nuovo biglietto */
				Biglietto nuovoBiglietto = new Biglietto();
				logger.debug("MYINFO) HELLO 5.A accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
				nuovoBiglietto.setDataAcquisizione(LocalDateTime.now());
				logger.debug("MYINFO) HELLO 5.B accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
				nuovoBiglietto.setProprietario(accountCorrente);
				logger.debug("MYINFO) HELLO 5.C accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
				nuovoBiglietto.setTipologiaPosto(tipologiaPosto);
				logger.debug("MYINFO) HELLO 5.D accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
				nuovoBiglietto.generateCode(i);
				logger.debug("MYINFO) HELLO 5.E accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
				
				logger.debug("MYINFO) new Ticket: code " + nuovoBiglietto.getCode());
				
				/* Aggiornamento lista biglietti dell'account corrente */
				bigliettiAccount.add(nuovoBiglietto);
				/* Aggiornamento lista dei nuovi biglietti da passare al
				 * prossimo mapping */
				nuoviBiglietti.add(nuovoBiglietto);
				/* Salvataggio persistente del nuovo biglietto corrente */
				bigliettoService.saveBiglietto(nuovoBiglietto);
			}
			logger.debug("MYINFO) HELLO 6 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
			
			/* Si aggiorna la lista dei biglietti dell'account corrente */
			accountCorrente.setBiglietti(bigliettiAccount);
			/* Si aggiorna l'account con i nuovi biglietti */
			accountService.updateAccount(accountCorrente);
			
			logger.debug("MYINFO) HELLO 7 accountCorrente: " + accountCorrente.getUsername() + ", " + accountCorrente.getId() + ", pass: " + accountCorrente.getPassword());
			model.addAttribute("nuoviBiglietti", nuoviBiglietti);
			return "concludiOrdine"; 
		}
		
		return "prenotazioneFail";
	}
	
	@RequestMapping(value = {"/eliminaBiglietto/{id}"}, method = RequestMethod.GET)
	public String eliminaBiglietto(@PathVariable("id") Long idBiglietto, Model model) {
		/* Trova il biglietto da rimuovere */
		Biglietto bigliettoDaRimuovere = this.bigliettoService.getBiglietto(idBiglietto);
		
		/* Trova l'utente corrente */
		Account accountCorrente = (Account) model.getAttribute("accountCorrente");
		/* Trova la lista di biglietti dell'account corrente */
		List<Biglietto> bigliettiAccount = accountCorrente.getBiglietti();

		boolean isRimossoDaAccount = false;
		/* Rimuove il biglietto dalla lista di biglietti dell'account */
		for(int i = 0; i < bigliettiAccount.size() ;i++) {
			Biglietto bigliettoCorrente = bigliettiAccount.get(i);
			if(bigliettoDaRimuovere.getCode().equals(bigliettoCorrente.getCode())) {
				logger.debug("MYINFO) rimozione da accountCorrente posizione i: " + i);
				bigliettiAccount.remove(i);
				isRimossoDaAccount = true;
			}
		}
		

		logger.debug("MYINFO) isRimossoDaAccount=" + isRimossoDaAccount);
		/* E' stato correttamente rimosso dalla lista dell'account corrente! */
		if(isRimossoDaAccount) {
			logger.debug("MYINFO) isRimossoDaAccount");
			/* Trova la tipologia di posto del biglietto da rimuovere */
			TipologiaPosto tipologiaPosto = bigliettoDaRimuovere.getTipologiaPosto();
			/* Trova la lista di biglietti della tipologia di posto */
			List<Biglietto> bigliettiTipologiaPosto = tipologiaPosto.getBiglietti();

			/* Rimuove il biglietto dalla tipologia di posto */
			boolean isRimossoDaTipologiaPosto = false;
			/* Rimuove il biglietto dalla lista di biglietti dell'account */
			for(int i = 0; i < bigliettiTipologiaPosto.size() ;i++) {
				Biglietto bigliettoCorrente = bigliettiTipologiaPosto.get(i);
				if(bigliettoDaRimuovere.getCode().equals(bigliettoCorrente.getCode())) {
					logger.debug("MYINFO) rimozione da tipologiaPosto posizione i: " + i);
					bigliettiTipologiaPosto.remove(i);
					isRimossoDaTipologiaPosto = true;
				}
			}
			
			if(isRimossoDaTipologiaPosto) {
				logger.debug("MYINFO) isRimossoDaTipologiaPosto");
				/* Aggiorna la lista di biglietti della tipologia di posto */
				tipologiaPosto.setBiglietti(bigliettiTipologiaPosto);
				logger.debug("MYINFO) ftest 1");

				/* Trova la quantità di posti attualmente prenotabili */
				int postiAttualmentePrenotabili = tipologiaPosto.getPostiAttualmentePrenotabili().intValue();
				logger.debug("MYINFO) ftest 2");
				/* Aggiunge un posto attualmente prenotabile! */
				tipologiaPosto.setPostiAttualmentePrenotabili(postiAttualmentePrenotabili + 1);
				logger.debug("MYINFO) ftest 3");

				/* Aggiornamenti e salvataggi */
				this.accountService.updateAccount(accountCorrente);
				this.tipologiaPostoService.saveTipologiaPosto(tipologiaPosto);
				model.addAttribute("accountCorrente", accountCorrente);
				
				/* Rimuove il biglietto dal database */
				//boolean isRimossoDalDb = this.bigliettoService.removeBiglietto(bigliettoDaRimuovere);
				this.bigliettoService.deleteBiglietto(bigliettoDaRimuovere);
				logger.debug("MYINFO) ftest 4");
				
				return "eliminaBiglietto";
			}
		}
		
		return "default/home";
	}

}
