package it.uniroma3.siw.spring.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.uniroma3.siw.spring.controller.validator.AccountValidator;
import lombok.Data;

@Entity
public @Data class Biglietto {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private	Long id;
	
	@Column(nullable=false)
	LocalDateTime dataAcquisizione;

	@ManyToOne
	private TipologiaPosto tipologiaPosto;
	
	@ManyToOne
	private Account proprietario;

	private String code;
	
	
	public Biglietto() {
		//this.setCode();
		this.dataAcquisizione = LocalDateTime.now();
	}
	

	private static final Logger logger = LoggerFactory.getLogger(AccountValidator.class);
	/** Genera il codice da utilizzare per pagare il biglietto al ticket office
	 * Vengono utilizzati l'id del concerto, l'id della tipologia del posto,
	 * l'id dell'account proprietario del biglietto **/
	public void generateCode(int i) {
		String code = "";

		logger.debug("MYINFO) CODE 1");
		TipologiaPosto tipologiaPosto = this.getTipologiaPosto();
		logger.debug("MYINFO) CODE 2");
		if(tipologiaPosto != null) {
			logger.debug("MYINFO) CODE 3");
			code = code + "TP" + tipologiaPosto.getId();

			logger.debug("MYINFO) CODE 4");
			Concerto concerto = tipologiaPosto.getConcerto();
			logger.debug("MYINFO) CODE 5");
			if(concerto != null) code = code + "C" + concerto.getId();
			logger.debug("MYINFO) CODE 6");
		} 

		logger.debug("MYINFO) CODE 7");
		logger.debug("MYINFO) CODE 8");
		Account proprietario = this.getProprietario();
		logger.debug("MYINFO) CODE 9");
		if(proprietario != null) {
			logger.debug("MYINFO) CODE 9.SPECIAL.A");
			code = code + "P" + proprietario.getId();
			logger.debug("MYINFO) CODE 9.SPECIAL.B");
		}

		logger.debug("MYINFO) CODE 10");
		LocalDateTime dataAcquisizione = this.getDataAcquisizione();
		if(dataAcquisizione != null) {
			logger.debug("MYINFO) CODE 10.SPECIAL.A");
			code = code + dataAcquisizione.getSecond() + dataAcquisizione.getMinute() + dataAcquisizione.getHour() + dataAcquisizione.getMonthValue() + dataAcquisizione.getYear();
			logger.debug("MYINFO) CODE 10.SPECIAL.B");
		}
		
		code = code + "I" + i;
		
		logger.debug("MYINFO) CODE 11: the code is (" + code + ")");
		this.setCode(code);
		logger.debug("MYINFO) CODE 12: SET!");
	}
	
}
