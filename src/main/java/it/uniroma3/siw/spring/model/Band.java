package it.uniroma3.siw.spring.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
public @Data class Band {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private	Long id;
	
	@Column(nullable=false)
	private String nome;

	@Column(nullable=false)
	private Integer annoFormazione;

	private Integer annoScioglimento;
	
	private String descrizione;

	@Column(nullable=false)
	private String genere;
	
	@OneToMany(mappedBy="band")
	private List<Partecipazione> partecipazioni;

}
