package org.domain.sck.dto;

import java.io.Serializable;

public class DMDTO implements Serializable{

	private String codigoStr; 
	private String descripcion;
	private String vwer;
	
	public String getCodigoStr() {
		return codigoStr;
	}
	public void setCodigoStr(String codigoStr) {
		this.codigoStr = codigoStr;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getVwer() {
		return vwer;
	}
	public void setVwer(String vwer) {
		this.vwer = vwer;
	} 

	
	
}
