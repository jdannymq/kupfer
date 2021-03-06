package org.domain.sck.entity;

// Generated 02-10-2012 09:54:39 PM by Hibernate Tools 3.4.0.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.domain.sck.entity.enums.ConceptosType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotNull;

/**
 * UsuarioCargo generated by hbm2java
 */
@Entity
@Table(name = "usuario_cargo")
public class UsuarioCargo {

	private long idUsuarioCargo;
	private Usuariosegur usuario;
	private LcredCargos cargo;
	private ConceptosType zona;
	private ConceptosType sucursal;
	private ConceptosType negocio;
	private ConceptosType concepto;
	private Boolean administrador;
	private Boolean envioAutomatico;
	private Boolean noNotificaInicial;
	
	
	public UsuarioCargo() {
		
	}

	public UsuarioCargo(long idUsuarioCargo, Usuariosegur usuario,LcredCargos cargo, 
			ConceptosType zona,ConceptosType sucursal,ConceptosType negocio,ConceptosType concepto,Boolean administrador,Boolean envioAutomatico, Boolean noNotificaInicial) {
		this.idUsuarioCargo = idUsuarioCargo;
		this.cargo = cargo;
		this.zona = zona;
		this.sucursal = sucursal;
		this.negocio = negocio;
		this.concepto = concepto;
		this.administrador = administrador;
		this.envioAutomatico = envioAutomatico;
		this.noNotificaInicial = noNotificaInicial;
		

	}

	@Id
	@GenericGenerator(name = "usuarioCargoSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator", parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_USUARIO_CARGO"),
			@Parameter(name = "initial_value", value = "1"),
			@Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "usuarioCargoSequenceGenerator")
	@Column(name = "id_usuario_cargo", unique = true, nullable = false, precision = 18, scale = 0)
	public Long getIdUsuarioCargo() {
		return this.idUsuarioCargo;
	}

	public void setIdUsuarioCargo(long idUsuarioCargo) {
		this.idUsuarioCargo = idUsuarioCargo;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_username", nullable = false)
	@NotNull
	public Usuariosegur getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuariosegur usuario) {
		this.usuario = usuario;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cargo_id", nullable = false)
	@NotNull
	public LcredCargos getCargo() {
		return cargo;
	}

	public void setCargo(LcredCargos cargo) {
		this.cargo = cargo;
	}

	@Column(name = "zona", nullable=true)
	public ConceptosType getZona() {
		return zona;
	}

	public void setZona(ConceptosType zona) {
		this.zona = zona;
	}
	@Column(name = "sucursal", nullable=true)
	public ConceptosType getSucursal() {
		return sucursal;
	}

	public void setSucursal(ConceptosType sucursal) {
		this.sucursal = sucursal;
	}

	@Column(name = "negocio", nullable=true)
	public ConceptosType getNegocio() {
		return negocio;
	}

	public void setNegocio(ConceptosType negocio) {
		this.negocio = negocio;
	}
	
	@Column(name = "concepto", nullable=true)
	public ConceptosType getConcepto() {
		return concepto;
	}

	public void setConcepto(ConceptosType concepto) {
		this.concepto = concepto;
	}	

	@Column(name = "administrador", nullable = true)
	public Boolean getAdministrador() {
		return this.administrador;
	}

	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	@Column(name = "envioAutomatico", nullable = true)
	public Boolean getEnvioAutomatico() {
		return this.envioAutomatico;
	}

	public void setEnvioAutomatico(Boolean envioAutomatico) {
		this.envioAutomatico = envioAutomatico;
	}
	
	@Column(name = "noNotificaInicial", nullable = true)
	public Boolean getnoNotificaInicial() {
		return this.noNotificaInicial;
	}
	
	public void setnoNotificaInicial(Boolean noNotificaInicial) {
		this.noNotificaInicial = noNotificaInicial;
	}

	
}
