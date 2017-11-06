package org.domain.sck.entity;

// Generated 12-feb-2013 22:24:06 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * LcredSolicitudObservacionesId generated by hbm2java
 */
@Embeddable
public class LcredSolicitudObservacionesId implements java.io.Serializable {

	private long numSolicitud;
	private String tipoSol;
	private Date fecha;
	private Date hora;
	private String usuario;
	private String observacion;
	private Long correlativo;

	public LcredSolicitudObservacionesId() {
	}

	public LcredSolicitudObservacionesId(long numSolicitud, Date fecha,
			Date hora) {
		this.numSolicitud = numSolicitud;
		this.fecha = fecha;
		this.hora = hora;
	}

	public LcredSolicitudObservacionesId(long numSolicitud, String tipoSol,
			Date fecha, Date hora, String usuario, String observacion,
			Long correlativo, String observacionDos) {
		this.numSolicitud = numSolicitud;
		this.tipoSol = tipoSol;
		this.fecha = fecha;
		this.hora = hora;
		this.usuario = usuario;
		this.observacion = observacion;
		this.correlativo = correlativo;
		
	}

	@Column(name = "num_solicitud", nullable = false, precision = 18, scale = 0)
	public long getNumSolicitud() {
		return this.numSolicitud;
	}

	public void setNumSolicitud(long numSolicitud) {
		this.numSolicitud = numSolicitud;
	}

	@Column(name = "tipo_sol", length = 10)
	@Length(max = 10)
	public String getTipoSol() {
		return this.tipoSol;
	}

	public void setTipoSol(String tipoSol) {
		this.tipoSol = tipoSol;
	}

	@Column(name = "fecha", nullable = false, length = 23)
	@NotNull
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "hora", nullable = false, length = 23)
	@NotNull
	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	@Column(name = "usuario", length = 20)
	@Length(max = 20)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Lob
	@Column(name = "observacion", nullable = false, length = 65535)
	@NotNull
	@Length(max = 65535)
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	

	
	@Column(name = "correlativo", precision = 18, scale = 0)
	public Long getCorrelativo() {
		return this.correlativo;
	}

	public void setCorrelativo(Long correlativo) {
		this.correlativo = correlativo;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LcredSolicitudObservacionesId))
			return false;
		LcredSolicitudObservacionesId castOther = (LcredSolicitudObservacionesId) other;

		return (this.getNumSolicitud() == castOther.getNumSolicitud())
				&& ((this.getTipoSol() == castOther.getTipoSol()) || (this
						.getTipoSol() != null && castOther.getTipoSol() != null && this
						.getTipoSol().equals(castOther.getTipoSol())))
				&& ((this.getFecha() == castOther.getFecha()) || (this
						.getFecha() != null && castOther.getFecha() != null && this
						.getFecha().equals(castOther.getFecha())))
				&& ((this.getHora() == castOther.getHora()) || (this.getHora() != null
						&& castOther.getHora() != null && this.getHora()
						.equals(castOther.getHora())))
				&& ((this.getUsuario() == castOther.getUsuario()) || (this
						.getUsuario() != null && castOther.getUsuario() != null && this
						.getUsuario().equals(castOther.getUsuario())))
				&& ((this.getObservacion() == castOther.getObservacion()) || (this
						.getObservacion() != null
						&& castOther.getObservacion() != null && this
						.getObservacion().equals(castOther.getObservacion())))
				&& ((this.getCorrelativo() == castOther.getCorrelativo()) || (this
						.getCorrelativo() != null
						&& castOther.getCorrelativo() != null && this
						.getCorrelativo().equals(castOther.getCorrelativo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getNumSolicitud();
		result = 37 * result
				+ (getTipoSol() == null ? 0 : this.getTipoSol().hashCode());
		result = 37 * result
				+ (getFecha() == null ? 0 : this.getFecha().hashCode());
		result = 37 * result
				+ (getHora() == null ? 0 : this.getHora().hashCode());
		result = 37 * result
				+ (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		result = 37
				* result
				+ (getObservacion() == null ? 0 : this.getObservacion()
						.hashCode());
		result = 37
				* result
				+ (getCorrelativo() == null ? 0 : this.getCorrelativo()
						.hashCode());
		return result;
	}

}