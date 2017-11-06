package org.domain.sck.dto;

import java.io.Serializable;


public class SolicitudEstadoDTO implements Serializable{

	private long idSolictud; 
	private String rut;
	private String razonSocial;
	private String emisor;
	private String despTipoSolictud;
	private String fechaRecepcion;
	private String fechaSalida;
	private String estado;
	private String usuarioEnProceso;
	
	
	public long getIdSolictud() {
		return idSolictud;
	}
	public void setIdSolictud(long idSolictud) {
		this.idSolictud = idSolictud;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getEmisor() {
		return emisor;
	}
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
	public String getDespTipoSolictud() {
		return despTipoSolictud;
	}
	public void setDespTipoSolictud(String despTipoSolictud) {
		this.despTipoSolictud = despTipoSolictud;
	}
	public String getFechaRecepcion() {
		return fechaRecepcion;
	}
	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	public String getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(String fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getUsuarioEnProceso() {
		return usuarioEnProceso;
	}
	public void setUsuarioEnProceso(String usuarioEnProceso) {
		this.usuarioEnProceso = usuarioEnProceso;
	}
	

	
	
	
	
}
