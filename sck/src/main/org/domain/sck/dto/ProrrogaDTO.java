package org.domain.sck.dto;

import java.io.Serializable;

import javax.persistence.Transient;

public class ProrrogaDTO implements Serializable{

	private String montoFormateado;
    private String numeroDocto;
    private String fechaVencicmiento;
    private String fechaSolicitado;
    private String motivoProrroga;
    
	public String getMontoFormateado() {
		return montoFormateado;
	}
	public void setMontoFormateado(String montoFormateado) {
		this.montoFormateado = montoFormateado;
	}
	public String getNumeroDocto() {
		return numeroDocto;
	}
	public void setNumeroDocto(String numeroDocto) {
		this.numeroDocto = numeroDocto;
	}
	public String getFechaVencicmiento() {
		return fechaVencicmiento;
	}
	public void setFechaVencicmiento(String fechaVencicmiento) {
		this.fechaVencicmiento = fechaVencicmiento;
	}
	public String getFechaSolicitado() {
		return fechaSolicitado;
	}
	public void setFechaSolicitado(String fechaSolicitado) {
		this.fechaSolicitado = fechaSolicitado;
	}
	public String getMotivoProrroga() {
		return motivoProrroga;
	}
	public void setMotivoProrroga(String motivoProrroga) {
		this.motivoProrroga = motivoProrroga;
	}
	

    
    
    
}
