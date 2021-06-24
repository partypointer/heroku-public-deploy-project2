package it.uniroma3.siw.spring.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.converter.StringToLocalDateTimeConverter;
import it.uniroma3.siw.spring.controller.validator.BandValidator;
import it.uniroma3.siw.spring.controller.validator.ConcertoValidator;
import it.uniroma3.siw.spring.controller.validator.PartecipazioneValidator;
import it.uniroma3.siw.spring.controller.validator.TipologiaPostoValidator;
import it.uniroma3.siw.spring.model.Account;
import it.uniroma3.siw.spring.model.Band;
import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.model.Partecipazione;
import it.uniroma3.siw.spring.model.TipologiaPosto;
import it.uniroma3.siw.spring.model.User;
import it.uniroma3.siw.spring.service.BandService;
import it.uniroma3.siw.spring.service.ConcertoService;
import it.uniroma3.siw.spring.service.PartecipazioneService;
import it.uniroma3.siw.spring.service.TipologiaPostoService;

import java.time.format.DateTimeFormatter;

/** Responsabile del mapping delle pagine dedicate alle operazioni
 * che l'amministratore può effettuare nella sua area personale:
 * - crea una Band
 * - crea un Concerto
 * - crea una Partecipazione
 * - crea una Tipologia di Posto **/
@Controller
public class AdministratorOperationsController {

	@Autowired
	private BandValidator bandValidator;
	
	@Autowired
	private BandService bandService;

	@Autowired
	private ConcertoValidator concertoValidator;
	
	@Autowired
	private ConcertoService concertoService;
	
	@Autowired
	private PartecipazioneService partecipazioneService;
	
	@Autowired
	private TipologiaPostoValidator tipologiaPostoValidator;
	
	@Autowired
	private TipologiaPostoService tipologiaPostoService;
	
	@Autowired
	private PartecipazioneValidator partecipazioneValidator;

    private static final Logger logger = LoggerFactory.getLogger(ConcertoValidator.class);

	@RequestMapping(value = {"/admin/addBand"}, method = RequestMethod.GET)
	public String addBand(Model model) {
		model.addAttribute("band", new Band());
		return "admin/addBand";
	}

    @RequestMapping(value = { "/admin/addBand" }, method = RequestMethod.POST)
    public String addBand(@ModelAttribute("band") Band band,
                 				BindingResult bandBindingResult,
                 									Model model) {
    	this.bandValidator.validate(band, bandBindingResult);

        if(!bandBindingResult.hasErrors()) {
            bandService.saveBand(band);
            return "admin/successfulAdminOperation";
        }
        return "admin/addBand";
    }
    
    /** La prima band verrà inserita tramite ID, la prima tipologia
     * di posto dovrà essere generata insieme al concerto, la partecipazione
     * verrà generata di conseguenza se la band esiste **/
	@RequestMapping(value = {"/admin/addConcerto"}, method = RequestMethod.GET)
	public String addConcerto(Model model) {
    	logger.debug("/nMYINFO) Entered addBand GET/n");
		model.addAttribute("concerto", new Concerto());
		return "admin/addConcerto";
	}

	// senza queste direttive, la stringa di datetime-local non viene correttamente trasposto in LocalDateTime @DateTimeFormat(pattern = "yyyy-dd-MM'T'HH:mm")
	// @RequestParam("dataOraInizio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String dataOraInizio,
    @RequestMapping(value = { "/admin/addConcerto" }, method = RequestMethod.POST)
    public String addConcerto(@ModelAttribute("concerto") Concerto concerto,
										BindingResult concertoBindingResult,
		                 									Model model) {
    	/*
    	StringToLocalDateTimeConverter converter = new StringToLocalDateTimeConverter();
    	LocalDateTime convertedDataOraInizio = converter.convert(dataOraInizio);
    	*/
    	/*
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime convertedDataOraInizio = LocalDateTime.parse(dataOraInizio + ":00", formatter); // 
    	concerto.setDataOraInizio(convertedDataOraInizio);
    	*/
    	//DateTimeFormat.ISO.DATE_TIME
    	//DateTimeFormatter.BASIC_ISO_DATE
    	/*
    	logger.debug("/nMYINFO) Entered addBand POST, dataOraInizio: " + dataOraInizio + "/n");
    	logger.debug("/nMYINFO) Entered addBand POST, convertedDataOraInizio: " + convertedDataOraInizio.toString() + "/n");
    	*/
    	//concerto.setDataOraInizio(dataOraInizio);
        this.concertoValidator.validate(concerto, concertoBindingResult);
        
        //Controllo esito validazioni precedenti e verifica di esistenza della band
        if(!concertoBindingResult.hasErrors()){
        	logger.debug("/nMYINFO) Entered addBand POST 5/n");
        	// Salvataggio persistente
        	concertoService.saveConcerto(concerto);
        	logger.debug("/nMYINFO) Entered addBand POST 6/n");
            return "admin/successfulAdminOperation";
        }
    	logger.debug("/nMYINFO) Entered addBand POST BAD END: " + concertoBindingResult.getAllErrors().toString() +"/n");
        return "admin/addConcerto";
    }
    
	@RequestMapping(value = {"/admin/addPartecipazione"}, method = RequestMethod.GET)
	public String addPartecipazione(Model model) {
		model.addAttribute("concerti", concertoService.getAllConcerti());
		model.addAttribute("bands", bandService.getAllBands());
		return "admin/addPartecipazione";
	}

	//@ModelAttribute("partecipazione") Partecipazione partecipazione,
	//BindingResult partecipazioneBindingResult,
    @RequestMapping(value = { "/admin/addPartecipazione" }, method = RequestMethod.POST)
    public String addPartecipazione(@RequestParam("band") Long band_id,
    						@RequestParam("concerto") Long concerto_id,
                 									Model model) {
    	/* Trova il concerto e la band di riferimento (dai radiobutton nel form) */
    	Concerto concerto = this.concertoService.getConcerto(concerto_id);
    	Band band = this.bandService.getBand(band_id);

    	/* Se non esiste già una partecipazione con quel concerto e quella band... */
    	if(partecipazioneService.getPartecipazione(concerto, band) == null) {

    		/* Genera la partecipazione con la band e il concerto */
        	Partecipazione partecipazione = new Partecipazione();
        	partecipazione.setBand(band);
        	partecipazione.setConcerto(concerto);
        	
        	/* Aggiunta partecipazione nel concerto */
        	List<Partecipazione> partecipazioniConcerto = concerto.getPartecipazioni();
        	partecipazioniConcerto.add(partecipazione);
        	concerto.setPartecipazioni(partecipazioniConcerto);

        	/* Aggiunta partecipazione nella band */
        	List<Partecipazione> partecipazioniBand = band.getPartecipazioni();
        	partecipazioniBand.add(partecipazione);
        	band.setPartecipazioni(partecipazioniBand);

        	/* Salvataggio della partecipazione e aggiornamento Concerto e Band */
            partecipazioneService.savePartecipazione(partecipazione);
            concertoService.saveConcerto(concerto);
            bandService.saveBand(band);
            return "admin/successfulAdminOperation";
        }
        return "admin/addPartecipazione";
    }

	@RequestMapping(value = {"/admin/addTipologiaPosto"}, method = RequestMethod.GET)
	public String addTipologiaPosto(Model model) {
		model.addAttribute("tipologiaPosto", new TipologiaPosto());
		model.addAttribute("concerti", concertoService.getAllConcerti());
		return "admin/addTipologiaPosto";
	}

	@RequestMapping(value = {"/admin/addTipologiaPosto"}, method = RequestMethod.POST)
	public String addTipologiaPosto(@ModelAttribute("tipologiaPosto") TipologiaPosto tipologiaPosto,
														BindingResult tipologiaPostoBindingResult,
														@RequestParam("concerto") Long concerto_id,
																					Model model) {
		logger.debug("MYINFO) BASE 1");
		Concerto concerto = this.concertoService.getConcerto(concerto_id);
		tipologiaPosto.setConcerto(concerto);
		logger.debug("MYINFO) BASE 2");
		
		this.tipologiaPostoValidator.validate(tipologiaPosto, tipologiaPostoBindingResult); //!= da concerto5 (almeno con una tipologia posti)
		logger.debug("MYINFO) BASE 3 errors: " + tipologiaPostoBindingResult.hasErrors() + ", allerrors: [@ " + tipologiaPostoBindingResult.getAllErrors().toString() + " @]");
		
		if(!tipologiaPostoBindingResult.hasErrors()) { //concerto 5 (unico senza tipologie posti)
			/* Ora che sappiamo che non vi sono errori ulteriori, calcoliamo i posti derivati */
			logger.debug("MYINFO) BASE 4");
			tipologiaPosto.setPosti();
			logger.debug("MYINFO) BASE 5");
			
			/* Aggiungiamo la nuova tipologia di posto al concerto */
			List<TipologiaPosto> tipologiePosti = concerto.getTipologiaPosti();
			tipologiePosti.add(tipologiaPosto);
			concerto.setTipologiaPosti(tipologiePosti);
			logger.debug("MYINFO) BASE 6");
			
			/* Salviamo in maniera persistente la nuova tipologia di posto e aggiorniamo il concerto */
			this.tipologiaPostoService.saveTipologiaPosto(tipologiaPosto);
			this.concertoService.saveConcerto(concerto);
			logger.debug("MYINFO) BASE 7");
			return "admin/successfulAdminOperation";
		}
		return "admin/addTipologiaPosto";
	}

	/* Motivazione della deprecazione: Il ValidatorTipologiaPosto non ammette inserimenti nel caso
	 * il concerto non esista quindi:
	 * - o si aggiunge un caso eccezionale nel validator dove se un amministratore stà creando
	 * un concerto non controlla se l'id del concerto è vuoto (rendendolo insicuro o invalido)
	 * - o si elimina la possibilità di generare un Concerto e la sua prima TipologiaPosto insieme
	 * Ho preferito la seconda soluzione perchè mi sembra quella più pulita da un punto di vista
	 * di progettazione della sicurezza della struttura del codice. */
	/*
	@Deprecated
    @RequestMapping(value = { "/admin/addConcerto" }, method = RequestMethod.POST)
    public String addConcerto(@ModelAttribute("concerto") Concerto concerto,
									BindingResult concertoBindingResult,
								@ModelAttribute("id_primaBand") Long id_primaBand,
					 					BindingResult bandBindingResult,
					 		@ModelAttribute("primaTipologiaPosto") TipologiaPosto primaTipologiaPosto,
										BindingResult tipologiaPostoBindingResult,
		                 									Model model) {
    	
    	// Ricerca della band tramite ID: se verrà trovata, non occorre svolgere una validazione
    	Band primaBand = this.bandService.getBand(id_primaBand);

    	//Validazione del nuovo concerto e della nuova tipologia di posto
        this.concertoValidator.validate(concerto, concertoBindingResult);
        this.tipologiaPostoValidator.validate(primaTipologiaPosto, tipologiaPostoBindingResult);
        
        //Controllo esito validazioni precedenti e verifica di esistenza della band
        if(!concertoBindingResult.hasErrors() && !tipologiaPostoBindingResult.hasErrors() && primaBand != null) {
        	//Generazione della prima partecipazione del concerto
        	Partecipazione primaPartecipazione = new Partecipazione();
        	primaPartecipazione.setConcerto(concerto);
        	primaPartecipazione.setBand(primaBand);
        	
        	//Aggiunta della prima partecipazione nella lista di partecipazioni del concerto
        	List<Partecipazione> partecipazioniConcerto = concerto.getPartecipazioni();
        	partecipazioniConcerto.add(primaPartecipazione);
        	concerto.setPartecipazioni(partecipazioniConcerto); // update list
        	
        	//Aggiunta della prima tipologia di posto nella lista di tipologie di posti del concerto
        	List<TipologiaPosto> tipologiePostiConcerto = concerto.getTipologiaPosti();
        	tipologiePostiConcerto.add(primaTipologiaPosto);
        	concerto.setTipologiaPosti(tipologiePostiConcerto); // update list
        	
        	//Impostazione del concerto nella tipologia di posto
        	primaTipologiaPosto.setConcerto(concerto);
        	
        	// Aggiunta della prima partecipazione nella lista di partecipazioni della band
        	List<Partecipazione> partecipazioniBand = primaBand.getPartecipazioni();
        	partecipazioniBand.add(primaPartecipazione);
        	primaBand.setPartecipazioni(partecipazioniBand); // update list
        	
        	// Salvataggio persistente
        	partecipazioneService.savePartecipazione(primaPartecipazione);
        	concertoService.saveConcerto(concerto);
        	tipologiaPostoService.saveTipologiaPosto(primaTipologiaPosto);
            bandService.saveBand(primaBand);
            return "admin/successfulAdminOperation";
        }
        return "admin/addConcerto";
    }*/
}
