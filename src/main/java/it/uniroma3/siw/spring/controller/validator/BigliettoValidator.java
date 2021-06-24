package it.uniroma3.siw.spring.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.Biglietto;
import it.uniroma3.siw.spring.service.BigliettoService;

@Component
public class BigliettoValidator implements Validator{
	
	@Autowired
	private BigliettoService bigliettoService;
	
    private static final Logger logger = LoggerFactory.getLogger(BigliettoValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dataAcquisizione", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tipologiaPosto", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "proprietario", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "required");
		
		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			if (this.bigliettoService.alreadyExists((Biglietto)o)) {
				logger.debug("e' un duplicato");
				errors.reject("duplicato");
			}
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Biglietto.class.equals(aClass);
	}
}