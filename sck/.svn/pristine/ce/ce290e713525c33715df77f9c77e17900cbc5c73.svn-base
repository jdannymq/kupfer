package org.domain.sck.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "usuarios")
public class Usuarios {

	private String login;
	private String pass;
	private String nom;
	private String ape;

	public Usuarios() {
	}

	public Usuarios(String login) {
		this.login = login;
	}

	public Usuarios(String login, String pass, String nom, String ape) {
		this.login = login;
		this.pass = pass;
		this.nom = nom;
		this.ape = ape;
	}

	@Id
	@Column(name = "login", unique = true, nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "pass", length = 50)
	@Length(max = 50)
	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Column(name = "nom", length = 50)
	@Length(max = 50)
	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Column(name = "ape", length = 50)
	@Length(max = 50)
	public String getApe() {
		return this.ape;
	}

	public void setApe(String ape) {
		this.ape = ape;
	}

}
