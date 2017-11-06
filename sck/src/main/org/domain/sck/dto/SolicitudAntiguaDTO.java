package org.domain.sck.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.domain.sck.entity.LcredMotivoRechazo;
import org.domain.sck.entity.LcredSolicitud;
import org.domain.sck.entity.SolicitudHitos;


public class SolicitudAntiguaDTO implements Serializable{
	
	private LcredSolicitud solicitud;
	private Date fechaAnalisis;
	private Date horaAnalisis;
	private String usuarioAnalisis;
	private String observacionesRechazo;
	private SolicitudHitos hitosAprobacion;
	private SolicitudHitos hitosRechazo;
	private List<String> listaAprobadores;
	private List<String> listaRechazos;
	private List<LcredMotivoRechazo> listaMotivosRechazos;

	public LcredSolicitud getSolicitud() {
		return solicitud;
	}
	public void setSolicitud(LcredSolicitud solicitud) {
		this.solicitud = solicitud;
	}
	public SolicitudHitos getHitosAprobacion() {
		return hitosAprobacion;
	}
	public void setHitosAprobacion(SolicitudHitos hitosAprobacion) {
		this.hitosAprobacion = hitosAprobacion;
	}
	public SolicitudHitos getHitosRechazo() {
		return hitosRechazo;
	}
	public void setHitosRechazo(SolicitudHitos hitosRechazo) {
		this.hitosRechazo = hitosRechazo;
	}
	public List<String> getListaAprobadores() {
		return listaAprobadores;
	}
	public void setListaAprobadores(List<String> listaAprobadores) {
		this.listaAprobadores = listaAprobadores;
	}
	public List<String> getListaRechazos() {
		return listaRechazos;
	}
	public void setListaRechazos(List<String> listaRechazos) {
		this.listaRechazos = listaRechazos;
	}
	public List<LcredMotivoRechazo> getListaMotivosRechazos() {
		return listaMotivosRechazos;
	}
	public void setListaMotivosRechazos(
			List<LcredMotivoRechazo> listaMotivosRechazos) {
		this.listaMotivosRechazos = listaMotivosRechazos;
	}
	public Date getFechaAnalisis() {
		return fechaAnalisis;
	}
	public void setFechaAnalisis(Date fechaAnalisis) {
		this.fechaAnalisis = fechaAnalisis;
	}
	public Date getHoraAnalisis() {
		return horaAnalisis;
	}
	public void setHoraAnalisis(Date horaAnalisis) {
		this.horaAnalisis = horaAnalisis;
	}
	public String getUsuarioAnalisis() {
		return usuarioAnalisis;
	}
	public void setUsuarioAnalisis(String usuarioAnalisis) {
		this.usuarioAnalisis = usuarioAnalisis;
	}
	public String getObservacionesRechazo() {
		return observacionesRechazo;
	}
	public void setObservacionesRechazo(String observacionesRechazo) {
		this.observacionesRechazo = observacionesRechazo;
	}
	
	
	
	
	
}
