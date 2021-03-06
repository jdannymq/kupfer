package org.domain.sck.entity;

// Generated 29-ago-2012 11:06:15 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * LcredMotivoRechazo generated by hbm2java
 */
@Entity
@Table(name = "lcred_motivo_rechazo")
public class LcredMotivoRechazo implements java.io.Serializable {

	private String codRechazo;
	private String desRechazo;
	private String indBorrado;

	public LcredMotivoRechazo() {
	}

	public LcredMotivoRechazo(String codRechazo) {
		this.codRechazo = codRechazo;
	}

	public LcredMotivoRechazo(String codRechazo, String desRechazo,
			String indBorrado) {
		this.codRechazo = codRechazo;
		this.desRechazo = desRechazo;
		this.indBorrado = indBorrado;
	}

	@Id
	@Column(name = "cod_rechazo", unique = true, nullable = false, length = 3)
	@NotNull
	@Length(max = 3)
	public String getCodRechazo() {
		return this.codRechazo;
	}

	public void setCodRechazo(String codRechazo) {
		this.codRechazo = codRechazo;
	}

	@Column(name = "des_rechazo", length = 200)
	@Length(max = 200)
	public String getDesRechazo() {
		return this.desRechazo;
	}

	public void setDesRechazo(String desRechazo) {
		this.desRechazo = desRechazo;
	}

	@Column(name = "ind_borrado", length = 1)
	@Length(max = 1)
	public String getIndBorrado() {
		return this.indBorrado;
	}

	public void setIndBorrado(String indBorrado) {
		this.indBorrado = indBorrado;
	}

}
