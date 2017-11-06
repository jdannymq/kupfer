package org.domain.sck.dto;

import java.io.Serializable;
import java.util.Date;

public class HitoDTO implements Serializable{

	private long systemId;
	private Long idSolicitud;
	private String emisor;
	private String usuarioActual;
	private Date fechaHora;
	private String codigoEstado;
	private String descripcionEstado;
	private String codEstadoDerivada;
	private String accionAdministrador;
	
	
	public HitoDTO(long systemId, Long idSolicitud, String emisor,
			String usuarioActual, Date fechaHora, String codigoEstado,
			String descripcionEstado, String codEstadoDerivada,
			String accionAdministrador) {
		super();
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
	public long getSystemId() {
		return systemId;
	}
	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}
	public Long getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getEmisor() {
		return emisor;
	}
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
	public String getUsuarioActual() {
		return usuarioActual;
	}
	public void setUsuarioActual(String usuarioActual) {
		this.usuarioActual = usuarioActual;
	}
	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getCodigoEstado() {
		return codigoEstado;
	}
	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	public String getDescripcionEstado() {
		return descripcionEstado;
	}
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}
	public String getCodEstadoDerivada() {
		return codEstadoDerivada;
	}
	public void setCodEstadoDerivada(String codEstadoDerivada) {
		this.codEstadoDerivada = codEstadoDerivada;
	}
	public String getAccionAdministrador() {
		return accionAdministrador;
	}
	public void setAccionAdministrador(String accionAdministrador) {
		this.accionAdministrador = accionAdministrador;
	}

	 
	
	
}
