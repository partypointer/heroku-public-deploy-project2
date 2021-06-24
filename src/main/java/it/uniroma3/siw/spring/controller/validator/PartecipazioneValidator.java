package it.uniroma3.siw.spring.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.Partecipazione;
import it.uniroma3.siw.spring.service.PartecipazioneService;

@Component
public class PartecipazioneValidator implements Validator {
	
	@Autowired
	private PartecipazioneService partecipazioneService;
	
    private static final Logger logger = LoggerFactory.getLogger(PartecipazioneValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "partecipazione", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "band", "required");
		
		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			if (this.partecipazioneService.alreadyExists((Partecipazione)o)) {
				logger.debug("e' un duplicato");
				errors.reject("duplicato");
			}
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Partecipazione.class.equals(aClass);
	}
}