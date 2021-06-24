package it.uniroma3.siw.spring.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.Band;
import it.uniroma3.siw.spring.service.BandService;

@Component
public class BandValidator implements Validator {
	
	@Autowired
	private BandService bandService;
	
    private static final Logger logger = LoggerFactory.getLogger(BandValidator.class);

	@Override
	/** Si accettano bande omonime **/
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "annoFormazione", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "genere", "required");
		
		Band b = (Band)o;
		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			if (this.bandService.alreadyExists(b.getNome())) {
				logger.debug("e' un duplicato");
				errors.reject("duplicato");
			}
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Band.class.equals(aClass);
	}
}