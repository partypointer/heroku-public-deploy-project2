package it.uniroma3.siw.spring.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
/*
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

https://mkyong.com/java/java-aes-encryption-and-decryption/#:~:text=The%20Advanced%20Encryption%20Standard%20(AES,%2C%20192%2C%20or%20256%20bits.
https://www.baeldung.com/java-aes-encryption-decryption
*/
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.Data;

@Entity
public @Data class Account {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private	Long id;
	
	public static final String DEFAULT_RUOLO = "DEFAULT";
	public static final String ADMIN_RUOLO = "ADMIN";

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;
	
	@Transient
	private String confermaPassword;
    
	@Column(nullable = false)
	private String ruolo;
    
    @Column(nullable = false)
	private	LocalDateTime dataCreazione;

    /* L'Account è posseduto da uno User */
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private User user;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy="proprietario", cascade = CascadeType.ALL)
    private List<Biglietto> biglietti;

	public Account(){
		this.dataCreazione = LocalDateTime.now();
		this.ruolo = this.getDefaultRuolo();
		this.biglietti = new ArrayList<Biglietto>();
	}

	/** Sfortunatamente Lombok non genera i getter di variabili static final... **/
	
	public static String getDefaultRuolo() {
		return DEFAULT_RUOLO;
	}

	public static String getAdminRuolo() {
		return ADMIN_RUOLO;
	}

}
