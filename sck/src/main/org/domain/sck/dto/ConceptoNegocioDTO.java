package org.domain.sck.dto;

import java.io.Serializable;

import org.domain.sck.entity.ConceptosNegocio;

public class ConceptoNegocioDTO implements Serializable{

	private ConceptosNegocio conceptoNegocio;
	private Long monto;
	public ConceptosNegocio getConceptoNegocio() {
		return conceptoNegocio;
	}
	public void setConceptoNegocio(ConceptosNegocio conceptoNegocio) {
		this.conceptoNegocio = conceptoNegocio;
	}
	public Long getMonto() {
		return monto;
	}
	public void setMonto(Long monto) {
		this.monto = monto;
	}
	

	
}
