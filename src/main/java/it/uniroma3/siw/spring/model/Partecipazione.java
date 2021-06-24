package it.uniroma3.siw.spring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
public @Data class Partecipazione {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private	Long id;
	
	@ManyToOne
	private Concerto concerto;
	
	@ManyToOne
	private Band band;

}
