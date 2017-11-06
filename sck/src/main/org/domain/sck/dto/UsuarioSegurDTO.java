package org.domain.sck.dto;

import java.io.Serializable;

public class UsuarioSegurDTO implements Serializable{

	private String alias; 
	private String correo;
	private String codigosucursal;
	private String nombre;
	private long idPersonal;
	private boolean obligatorio;
	private String cargo;
	private String rut;
	private String anexo;
	private String celular;
	private String marca;
	private String sucursal;
	private String negocios;
	private boolean eliminado;
	private int idSociedad;
	
	
	public int getIdSociedad() {
		return idSociedad;
	}
	public void setIdSociedad(int idSociedad) {
		this.idSociedad = idSociedad;
	}
	public long getIdPersonal() {
		return idPersonal;
	}
	public void setIdPersonal(long idPersonal) {
		this.idPersonal = idPersonal;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getCodigosucursal() {
		return codigosucursal;
	}
	public void setCodigosucursal(String codigosucursal) {
		this.codigosucursal = codigosucursal;
	}
	public boolean isObligatorio() {
		return obligatorio;
	}
	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getAnexo() {
		return anexo;
	}
	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public String getNegocios() {
		return negocios;
	}
	public void setNegocios(String negocios) {
		this.negocios = negocios;
	}
	public boolean isEliminado() {
		return eliminado;
	}
	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}
	
	

	
	
}
