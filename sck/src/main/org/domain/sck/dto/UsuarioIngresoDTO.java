package org.domain.sck.dto;

import java.io.Serializable;


public class UsuarioIngresoDTO implements Serializable{

	private String usuario;
	private String nombre;
	private String correo;
	private String cargo;
	private String descripcionCargo;
	private String nivelCargo;
	private String NivelSiguiente;
	private String zona;
	private String sucursal;
	private String negocio;
	private String concepto;
	private String adminstrador;
	private String envio;
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getDescripcionCargo() {
		return descripcionCargo;
	}
	public void setDescripcionCargo(String descripcionCargo) {
		this.descripcionCargo = descripcionCargo;
	}
	public String getNivelCargo() {
		return nivelCargo;
	}
	public void setNivelCargo(String nivelCargo) {
		this.nivelCargo = nivelCargo;
	}
	public String getNivelSiguiente() {
		return NivelSiguiente;
	}
	public void setNivelSiguiente(String nivelSiguiente) {
		NivelSiguiente = nivelSiguiente;
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
	public String getNegocio() {
		return negocio;
	}
	public void setNegocio(String negocio) {
		this.negocio = negocio;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getAdminstrador() {
		return adminstrador;
	}
	public void setAdminstrador(String adminstrador) {
		this.adminstrador = adminstrador;
	}
	public String getEnvio() {
		return envio;
	}
	public void setEnvio(String envio) {
		this.envio = envio;
	}




		
	
}
