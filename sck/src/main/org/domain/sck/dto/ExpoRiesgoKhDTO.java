package org.domain.sck.dto;


//DTO datos de cormportamiento comercial de la solicitud seleccionada
public class ExpoRiesgoKhDTO {
	
	private long peakCredito;
	private long montoAsegurado;
	private long montoRiesgoKupfer;
	private long montoPedidoProceso;
	private long montoPeakCreditoPedidoProceso;
	private long montoRiesgoKupferPedidoProceso;

	
	public ExpoRiesgoKhDTO() {
		
	}


	public long getPeakCredito() {
		return peakCredito;
	}


	public void setPeakCredito(long peakCredito) {
		this.peakCredito = peakCredito;
	}


	public long getMontoAsegurado() {
		return montoAsegurado;
	}


	public void setMontoAsegurado(long montoAsegurado) {
		this.montoAsegurado = montoAsegurado;
	}


	public long getMontoRiesgoKupfer() {
		return montoRiesgoKupfer;
	}


	public void setMontoRiesgoKupfer(long montoRiesgoKupfer) {
		this.montoRiesgoKupfer = montoRiesgoKupfer;
	}


	public long getMontoPedidoProceso() {
		return montoPedidoProceso;
	}


	public void setMontoPedidoProceso(long montoPedidoProceso) {
		this.montoPedidoProceso = montoPedidoProceso;
	}


	public long getMontoPeakCreditoPedidoProceso() {
		return montoPeakCreditoPedidoProceso;
	}


	public void setMontoPeakCreditoPedidoProceso(long montoPeakCreditoPedidoProceso) {
		this.montoPeakCreditoPedidoProceso = montoPeakCreditoPedidoProceso;
	}


	public long getMontoRiesgoKupferPedidoProceso() {
		return montoRiesgoKupferPedidoProceso;
	}


	public void setMontoRiesgoKupferPedidoProceso(
			long montoRiesgoKupferPedidoProceso) {
		this.montoRiesgoKupferPedidoProceso = montoRiesgoKupferPedidoProceso;
	}




	
	
	
}
