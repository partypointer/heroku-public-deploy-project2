package it.uniroma3.siw.spring.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;

import lombok.Data;

@Entity
public @Data class Concerto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private	Long id;

	@Column(nullable=false)
	private String nome;
	
	@Column(length=500)
	private String descrizione;
	
	@Column(nullable=false)
	// @JsonSerialize(using = LocalDateTimeSerializer.class)
    // @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
	private LocalDate dataInizio;

	@Column(nullable=false)
	private String indirizzoLocation;

	@Column(nullable=false)
	private String iconaLink;
	
	@OneToMany(mappedBy="concerto")
	private List<TipologiaPosto> tipologiaPosti;
	
	@OneToMany(mappedBy="concerto")
	private List<Partecipazione> partecipazioni;

}
