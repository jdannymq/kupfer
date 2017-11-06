package org.domain.sck.dto;

import java.util.List;

import org.domain.sck.entity.ConceptosNegocio;

public class CabeceraCotPedDTO {
	
	public CabeceraCotPedDTO(){
		
	}
	
	private ConceptosNegocio negocio;
	private Double porcentaje;
	private Long montoGeneral;
	private Double representacion;
	private List<DetalleCotPedDTO> listaDetalle;
	
	public ConceptosNegocio getNegocio() {
		return negocio;
	}
	public void setNegocio(ConceptosNegocio negocio) {
		this.negocio = negocio;
	}
	public List<DetalleCotPedDTO> getListaDetalle() {
		return listaDetalle;
	}
	public void setListaDetalle(List<DetalleCotPedDTO> listaDetalle) {
		this.listaDetalle = listaDetalle;
	}
	public Double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
	public Long getMontoGeneral() {
		return montoGeneral;
	}
	public void setMontoGeneral(Long montoGeneral) {
		this.montoGeneral = montoGeneral;
	}
	public Double getRepresentacion() {
		return representacion;
	}
	public void setRepresentacion(Double representacion) {
		this.representacion = representacion;
	}
	
	
	
	
	
	
}
