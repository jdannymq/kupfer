package org.domain.sck.entity;

// Generated 20-feb-2013 17:41:40 by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.Length;

/**
 * LcredSolicitudOtras generated by hbm2java
 */
@Entity
@Table(name = "lcred_solicitud_otras")
public class LcredSolicitudOtras implements java.io.Serializable {

	private long numSolicitud;
	private String codTipoSolicitud;
	private String statusSolicitud;
	private String opcionInicial;
	private String opcionFinal;
	private String observacionesInicial;
	private String observacionesFinal;
	private String usuarioAnalisis;
	private Date fechaAnalisis;
	private Date horaAnalisis;
	private String motivoRechazo;
	private String observacionesRechazo;

	public LcredSolicitudOtras() {
	}

	public LcredSolicitudOtras(long numSolicitud) {
		this.numSolicitud = numSolicitud;
	}

	public LcredSolicitudOtras(long numSolicitud, String codTipoSolicitud,
			String statusSolicitud, String opcionInicial, String opcionFinal,
			String observacionesInicial, String observacionesFinal,
			String usuarioAnalisis, Date fechaAnalisis, Date horaAnalisis,
			String motivoRechazo, String observacionesRechazo) {
		this.numSolicitud = numSolicitud;
		this.codTipoSolicitud = codTipoSolicitud;
		this.statusSolicitud = statusSolicitud;
		this.opcionInicial = opcionInicial;
		this.opcionFinal = opcionFinal;
		this.observacionesInicial = observacionesInicial;
		this.observacionesFinal = observacionesFinal;
		this.usuarioAnalisis = usuarioAnalisis;
		this.fechaAnalisis = fechaAnalisis;
		this.horaAnalisis = horaAnalisis;
		this.motivoRechazo = motivoRechazo;
		this.observacionesRechazo = observacionesRechazo;
	}

	@Id
	@Column(name = "num_solicitud", unique = true, nullable = false, precision = 18, scale = 0)
	public long getNumSolicitud() {
		return this.numSolicitud;
	}

	public void setNumSolicitud(long numSolicitud) {
		this.numSolicitud = numSolicitud;
	}

	@Column(name = "cod_tipo_solicitud", length = 3)
	@Length(max = 3)
	public String getCodTipoSolicitud() {
		return this.codTipoSolicitud;
	}

	public void setCodTipoSolicitud(String codTipoSolicitud) {
		this.codTipoSolicitud = codTipoSolicitud;
	}

	@Column(name = "status_solicitud", length = 2)
	@Length(max = 2)
	public String getStatusSolicitud() {
		return this.statusSolicitud;
	}

	public void setStatusSolicitud(String statusSolicitud) {
		this.statusSolicitud = statusSolicitud;
	}

	@Column(name = "opcion_inicial", length = 3)
	@Length(max = 3)
	public String getOpcionInicial() {
		return this.opcionInicial;
	}

	public void setOpcionInicial(String opcionInicial) {
		this.opcionInicial = opcionInicial;
	}

	@Column(name = "opcion_final", length = 3)
	@Length(max = 3)
	public String getOpcionFinal() {
		return this.opcionFinal;
	}

	public void setOpcionFinal(String opcionFinal) {
		this.opcionFinal = opcionFinal;
	}

	@Column(name = "observaciones_inicial")
	public String getObservacionesInicial() {
		return this.observacionesInicial;
	}

	public void setObservacionesInicial(String observacionesInicial) {
		this.observacionesInicial = observacionesInicial;
	}

	@Column(name = "observaciones_final")
	public String getObservacionesFinal() {
		return this.observacionesFinal;
	}

	public void setObservacionesFinal(String observacionesFinal) {
		this.observacionesFinal = observacionesFinal;
	}

	@Column(name = "usuario_analisis", length = 15)
	@Length(max = 15)
	public String getUsuarioAnalisis() {
		return this.usuarioAnalisis;
	}

	public void setUsuarioAnalisis(String usuarioAnalisis) {
		this.usuarioAnalisis = usuarioAnalisis;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_analisis", length = 23)
	public Date getFechaAnalisis() {
		return this.fechaAnalisis;
	}

	public void setFechaAnalisis(Date fechaAnalisis) {
		this.fechaAnalisis = fechaAnalisis;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hora_analisis", length = 23)
	public Date getHoraAnalisis() {
		return this.horaAnalisis;
	}

	public void setHoraAnalisis(Date horaAnalisis) {
		this.horaAnalisis = horaAnalisis;
	}

	@Column(name = "motivo_rechazo", length = 100)
	@Length(max = 100)
	public String getMotivoRechazo() {
		return this.motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	@Column(name = "observaciones_rechazo")
	public String getObservacionesRechazo() {
		return this.observacionesRechazo;
	}

	public void setObservacionesRechazo(String observacionesRechazo) {
		this.observacionesRechazo = observacionesRechazo;
	}

}
