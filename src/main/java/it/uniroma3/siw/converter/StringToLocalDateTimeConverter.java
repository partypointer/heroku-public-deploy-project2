package it.uniroma3.siw.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import it.uniroma3.siw.spring.controller.validator.ConcertoValidator;
import it.uniroma3.siw.spring.controller.validator.PartecipazioneValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
    private static final Logger logger = LoggerFactory.getLogger(ConcertoValidator.class);
        @Override
        public LocalDateTime convert(String s) {
        	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-dd-MM'T'HH:mm:ss");
        	LocalDateTime localDateTime = LocalDateTime.parse(s + ":00", fmt);
        	logger.debug("/nMYINFO) java.time.LocalDateTime.parse(s, fmt).toString(): (" + localDateTime.toString() + ")/n");
            return localDateTime;
        }
}