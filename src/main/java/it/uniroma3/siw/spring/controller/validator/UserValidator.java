package it.uniroma3.siw.spring.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.User;
import it.uniroma3.siw.spring.service.UserService;

@Component
public class UserValidator implements Validator {
	
	@Autowired
	UserService userService;

    final Integer MAX_NAME_LENGTH = 100;
    final Integer MIN_NAME_LENGTH = 4;

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        String nome = user.getNome().trim();
        String cognome = user.getCognome().trim();

        if(nome.isEmpty())
            errors.rejectValue("nome", "error.registration.nome.empty");
        if(nome.length() < MIN_NAME_LENGTH)
        	errors.rejectValue("nome", "error.registration.nome.underMinLength");
        if(nome.length() > MAX_NAME_LENGTH)
        	errors.rejectValue("nome", "error.registration.nome.overMaxLength");
        
        if(cognome.isEmpty())
            errors.rejectValue("cognome", "error.registration.cognome.empty");
        if(cognome.length() < MIN_NAME_LENGTH)
        	errors.rejectValue("cognome", "error.registration.cognome.underMinLength");
        if(cognome.length() > MAX_NAME_LENGTH)
        	errors.rejectValue("cognome", "error.registration.cognome.overMaxLength");
        
        if(userService.getUser(nome, cognome) != null)
        	errors.rejectValue("cognome", "error.registration.duplicateNameAndSurname");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

}

