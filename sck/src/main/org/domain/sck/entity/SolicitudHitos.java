package org.domain.sck.entity;

// Generated 17-ene-2013 12:02:26 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.*;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * ConceptosNegocio generated by hbm2java
 */
@Entity
@Table(name = "solicitud_hitos")
public class SolicitudHitos {

	private long systemId;
	private Long idSolicitud;
	private String emisor;
	private String usuarioActual;
	private Date fechaHora;
	private String codigoEstado;
	private String descripcionEstado;
	private String codEstadoDerivada;
	private String accionAdministrador;


	public SolicitudHitos() {

	}

	public SolicitudHitos(long systemId, Long idSolicitud, String emisor,
			String usuarioActual, Date fechaHora, String codigoEstado,
			String descripcionEstado, String codEstadoDerivada,
			String accionAdministrador) {
		this.systemId = systemId;
		this.idSolicitud = idSolicitud;
		this.emisor = emisor;
		this.usuarioActual = usuarioActual;
		this.fechaHora = fechaHora;
		this.codigoEstado = codigoEstado;
		this.descripcionEstado = descripcionEstado;
		this.codEstadoDerivada = codEstadoDerivada;
		this.accionAdministrador = accionAdministrador;
	}

	@Id
	@Column(name = "system_id", unique = true, nullable = false, precision = 18, scale = 0)
	public long getSystemId() {
		return this.systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	@Column(name = "solicitud_id", nullable = false, precision = 18, scale = 0)
	public Long getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	@Column(name = "emisor", nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	@Column(name = "usuario_actual", nullable = true, length = 50)
	@Length(max = 50)
	public String getUsuarioActual() {
		return usuarioActual;
	}

	public void setUsuarioActual(String usuarioActual) {
		this.usuarioActual = usuarioActual;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_hora", nullable = false)
	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}

	@Column(name = "codigo_estado", nullable = false)
	public String getCodigoEstado() {
		return codigoEstado;
	}

	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}

	@Column(name = "descripcion_estado", nullable = false, length = 30)
	@NotNull
	@Length(max = 30)
	public String getDescripcionEstado() {
		return descripcionEstado;
	}

	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}

	@Column(name = "cod_estado_derivada", nullable = true, length = 2)
	@Length(max = 2)
	public String getCodEstadoDerivada() {
		return codEstadoDerivada;
	}

	public void setCodEstadoDerivada(String codEstadoDerivada) {
		this.codEstadoDerivada = codEstadoDerivada;
	}

	@Column(name = "accion_administrador", nullable = true, length = 150)
	@Length(max = 150)
	public String getAccionAdministrador() {
		return accionAdministrador;
	}

	public void setAccionAdministrador(String accionAdministrador) {
		this.accionAdministrador = accionAdministrador;
	}


}
