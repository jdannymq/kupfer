package org.domain.sck.dto;

import java.io.Serializable;


public class NegocioUsuarioDTO implements Serializable{

	private boolean status;
	private String codigo;
	private String descripcion;
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	


		
	
}
