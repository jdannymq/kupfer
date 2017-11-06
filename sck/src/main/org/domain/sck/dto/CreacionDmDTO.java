package org.domain.sck.dto;

import java.io.Serializable;

import javax.persistence.Transient;

public class CreacionDmDTO implements Serializable{

	private String rut;
	private String nombre;
	private String oficinaVentas;
	private String zona;
	private String listaPrecio;
	private String sector;
	private String vendTelefono;
	private String vendTerreno;
	private String cobrador;
	private String condExpedicion;
	private String condPago;
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getOficinaVentas() {
		return oficinaVentas;
	}
	public void setOficinaVentas(String oficinaVentas) {
		this.oficinaVentas = oficinaVentas;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getListaPrecio() {
		return listaPrecio;
	}
	public void setListaPrecio(String listaPrecio) {
		this.listaPrecio = listaPrecio;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getVendTelefono() {
		return vendTelefono;
	}
	public void setVendTelefono(String vendTelefono) {
		this.vendTelefono = vendTelefono;
	}
	public String getVendTerreno() {
		return vendTerreno;
	}
	public void setVendTerreno(String vendTerreno) {
		this.vendTerreno = vendTerreno;
	}
	public String getCobrador() {
		return cobrador;
	}
	public void setCobrador(String cobrador) {
		this.cobrador = cobrador;
	}
	public String getCondExpedicion() {
		return condExpedicion;
	}
	public void setCondExpedicion(String condExpedicion) {
		this.condExpedicion = condExpedicion;
	}
	public String getCondPago() {
		return condPago;
	}
	public void setCondPago(String condPago) {
		this.condPago = condPago;
	}
	

	

    
    
    
}
