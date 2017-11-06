package org.domain.sck.dto;

import java.io.Serializable;

public class DestinatarioDTO implements Serializable{

	private String tipoSolicitud; 
	private String username;
	private String zona;
	private String sucursal;
	private String concepto;
	private String negocio;
	private String nombre;
	private String correo;
	private long idPersonal;
	
	public String getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getNegocio() {
		return negocio;
	}
	public void setNegocio(String negocio) {
		this.negocio = negocio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public long getIdPersonal() {
		return idPersonal;
	}
	public void setIdPersonal(long idPersonal) {
		this.idPersonal = idPersonal;
	}
	
	
}
