package org.domain.sck.entity;

// Generated 04-sep-2012 11:19:31 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * LcredTipoSolicitud generated by hbm2java
 */
@Entity
@Table(name = "lcred_tipo_solicitud")
public class LcredTipoSolicitud implements java.io.Serializable {

	private String codTipoSolicitud;
	private String desTipoSolicitud;
	private Character indBorrado;
	private String claveProceso;
	private Integer correlativo;

	public LcredTipoSolicitud() {
	}

	public LcredTipoSolicitud(String codTipoSolicitud) {
		this.codTipoSolicitud = codTipoSolicitud;
	}

	public LcredTipoSolicitud(String codTipoSolicitud, String desTipoSolicitud,
			Character indBorrado, String claveProceso, Integer correlativo) {
		this.codTipoSolicitud = codTipoSolicitud;
		this.desTipoSolicitud = desTipoSolicitud;
		this.indBorrado = indBorrado;
		this.claveProceso = claveProceso;
		this.correlativo = correlativo;
	}

	@Id
	@Column(name = "cod_tipo_solicitud", unique = true, nullable = false, length = 3)
	@NotNull
	@Length(max = 3)
	public String getCodTipoSolicitud() {
		return this.codTipoSolicitud;
	}

	public void setCodTipoSolicitud(String codTipoSolicitud) {
		this.codTipoSolicitud = codTipoSolicitud;
	}

	@Column(name = "des_tipo_solicitud", length = 30)
	@Length(max = 30)
	public String getDesTipoSolicitud() {
		return this.desTipoSolicitud;
	}

	public void setDesTipoSolicitud(String desTipoSolicitud) {
		this.desTipoSolicitud = desTipoSolicitud;
	}

	@Column(name = "ind_borrado", length = 1)
	public Character getIndBorrado() {
		return this.indBorrado;
	}

	public void setIndBorrado(Character indBorrado) {
		this.indBorrado = indBorrado;
	}

	@Column(name = "clave_proceso", length = 2)
	@Length(max = 2)
	public String getClaveProceso() {
		return this.claveProceso;
	}

	public void setClaveProceso(String claveProceso) {
		this.claveProceso = claveProceso;
	}

	
	@Column(name = "correlativo", precision = 3, scale = 0, nullable = true)
	public Integer getCorrelativo() {
		return this.correlativo;
	}

	public void setCorrelativo(Integer correlativo) {
		this.correlativo = correlativo;
	}
	

}
