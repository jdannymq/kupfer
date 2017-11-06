package org.domain.sck.dto;

import java.io.Serializable;
import java.util.Date;

public class DocumentoProrrogaDTO implements Serializable{

	private boolean status;
	private String codigoCliente;
	private String rut;
	private String doctoContable;
	private Date fechaContable;
	private String cl;
	private String numeroDocto;
	private String nroFactura;
	private Date fechaVencActual; 	
	private String indCme;
	private String referencia;
	private long monto;
	private String texto;
	private int diasMoras;
	private Date fechaVencNuevo;
	
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public Date getFechaContable() {
		return fechaContable;
	}
	public void setFechaContable(Date fechaContable) {
		this.fechaContable = fechaContable;
	}
	public String getCl() {
		return cl;
	}
	public void setCl(String cl) {
		this.cl = cl;
	}
	public String getNroFactura() {
		return nroFactura;
	}
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}
	public String getIndCme() {
		return indCme;
	}
	public void setIndCme(String indCme) {
		this.indCme = indCme;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getNumeroDocto() {
		return numeroDocto;
	}
	public void setNumeroDocto(String numeroDocto) {
		this.numeroDocto = numeroDocto;
	}
	public long getMonto() {
		return monto;
	}
	public void setMonto(long monto) {
		this.monto = monto;
	}
	public Date getFechaVencActual() {
		return fechaVencActual;
	}
	public void setFechaVencActual(Date fechaVencActual) {
		this.fechaVencActual = fechaVencActual;
	}
	public Date getFechaVencNuevo() {
		return fechaVencNuevo;
	}
	public void setFechaVencNuevo(Date fechaVencNuevo) {
		this.fechaVencNuevo = fechaVencNuevo;
	}
	public String getDoctoContable() {
		return doctoContable;
	}
	public void setDoctoContable(String doctoContable) {
		this.doctoContable = doctoContable;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public int getDiasMoras() {
		return diasMoras;
	}
	public void setDiasMoras(int diasMoras) {
		this.diasMoras = diasMoras;
	}
	
	
	
}
