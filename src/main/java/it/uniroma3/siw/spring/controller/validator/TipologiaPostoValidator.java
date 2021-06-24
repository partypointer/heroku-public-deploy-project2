package it.uniroma3.siw.spring.controller.validator;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.model.TipologiaPosto;
import it.uniroma3.siw.spring.service.ConcertoService;
import it.uniroma3.siw.spring.service.TipologiaPostoService;

@Component
public class TipologiaPostoValidator implements Validator {
	
	@Autowired
	private TipologiaPostoService tipologiaPostoService;
	
	@Autowired
	private ConcertoService concertoService;
	
    private static final Logger logger = LoggerFactory.getLogger(TipologiaPostoValidator.class);

    /** Non vengono richiesti i campi derivati in quanto se i campi dai quali derivano
     * fossero nulli verrebbe sollevata un'eccezione NullPointerException.
     * Per questo motivo, vengono controllati inizialmente SOLO E SOLTANTO maxPostiRealiDisponibili
     * e percentualeOverbooking fra i campi che descrivono i posti. **/
	@Override
	public void validate(Object o, Errors errors) {
		logger.debug("MYINFO) BASE pre errors: " + errors.hasErrors() + ", allerrors: [@ " + errors.getAllErrors().toString() + " @]");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "error.tipologiaPosto.nome.empty");
		logger.debug("MYINFO) PIER 1");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maxPostiRealiDisponibili", "error.tipologiaPosto.maxPostiRealiDisponibili.empty");
		logger.debug("MYINFO) PIER 2");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "percentualeOverbooking", "error.tipologiaPosto.percentualeOverbooking.empty");
		logger.debug("MYINFO) PIER 3");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "prezzoUnitario", "error.tipologiaPosto.prezzoUnitario.empty");
		TipologiaPosto test = (TipologiaPosto) o;
		logger.debug("MYINFO) PIER 4 nome concerto:" + test.getConcerto().getNome());
		//ValidationUtils.rejectIfEmpty(errors, "concerto", "error.tipologiaPosto.concerto.empty");
		logger.debug("MYINFO) PIER 5");

		logger.debug("MYINFO) BASE post errors: " + errors.hasErrors() + ", allerrors: [@ " + errors.getAllErrors().toString() + " @]");
		
		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			TipologiaPosto thisTipologiaPosto = (TipologiaPosto) o;
			String nome = thisTipologiaPosto.getNome();
			Concerto concerto = thisTipologiaPosto.getConcerto();
			logger.debug("MYINFO: a bit later...");
			
			if(this.tipologiaPostoService.alreadyExists(nome, concerto)){
				logger.debug("e' un duplicato");
				errors.rejectValue("nome", "error.tipologiaPosto.duplicate");
			}
			logger.debug("MYINFO: much later in the day...");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return TipologiaPosto.class.equals(aClass);
	}
}