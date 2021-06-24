package it.uniroma3.siw.configuration.language;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import it.uniroma3.siw.configuration.language.LanguageResolver;

@Configuration
public class LanguageConfiguration implements WebMvcConfigurer{
	@Autowired
	private LanguageResolver languageResolver;
	
	//@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(this.languageResolver.localeChangeInterceptor());
	}
}
