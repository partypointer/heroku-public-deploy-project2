package it.uniroma3.siw.spring.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.Concerto;
import it.uniroma3.siw.spring.service.ConcertoService;

@Component
public class ConcertoValidator implements Validator {
	
	@Autowired
	private ConcertoService concertoService;
	
    private static final Logger logger = LoggerFactory.getLogger(ConcertoValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "error.addConcerto.nome.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dataInizio", "error.addConcerto.dataInizio.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "indirizzoLocation", "error.addConcerto.indirizzoLocation.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "iconaLink", "error.addConcerto.iconaLink.empty");
		
		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			Concerto concerto = (Concerto) o;
			if (this.concertoService.alreadyExists(concerto.getNome())) {
				logger.debug("e' un duplicato");
				errors.reject("duplicato");
			}
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Concerto.class.equals(aClass);
	}
}