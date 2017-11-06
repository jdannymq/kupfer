package org.domain.sck.dto;

import java.io.Serializable;

public class BotonesDTO implements Serializable{

	private boolean observaiones; 
	private boolean devolver; 
	private boolean adjuntar; 
	private boolean derivar; 
	private boolean analisis; 
	private boolean imprimir; 
	private boolean rechazar; 
	private boolean aprobar; 
	private boolean logs; 
	private boolean hitos; 
	private boolean conceptos; 
	private boolean anular; 
	private boolean salir;
	private boolean cerrarAnalisis;
	private int muestraBotonAnalisisToCierraAnalisis;
	private int evaluarAdjuntarArchivo;
	
	public BotonesDTO (){
		this.observaiones = true;
		this.logs = true;
		this.hitos = true;
		this.salir = true;
	}
	
	
	public boolean isObservaiones() {
		return observaiones;
	}
	public void setObservaiones(boolean observaiones) {
		this.observaiones = observaiones;
	}
	public boolean isDevolver() {
		return devolver;
	}
	public void setDevolver(boolean devolver) {
		this.devolver = devolver;
	}
	public boolean isAdjuntar() {
		return adjuntar;
	}
	public void setAdjuntar(boolean adjuntar) {
		this.adjuntar = adjuntar;
	}
	public boolean isDerivar() {
		return derivar;
	}
	public void setDerivar(boolean derivar) {
		this.derivar = derivar;
	}
	public boolean isAnalisis() {
		return analisis;
	}
	public void setAnalisis(boolean analisis) {
		this.analisis = analisis;
	}
	public boolean isImprimir() {
		return imprimir;
	}
	public void setImprimir(boolean imprimir) {
		this.imprimir = imprimir;
	}
	public boolean isRechazar() {
		return rechazar;
	}
	public void setRechazar(boolean rechazar) {
		this.rechazar = rechazar;
	}
	public boolean isAprobar() {
		return aprobar;
	}
	public void setAprobar(boolean aprobar) {
		this.aprobar = aprobar;
	}
	public boolean isLogs() {
		return logs;
	}
	public void setLogs(boolean logs) {
		this.logs = logs;
	}
	public boolean isHitos() {
		return hitos;
	}
	public void setHitos(boolean hitos) {
		this.hitos = hitos;
	}
	public boolean isConceptos() {
		return conceptos;
	}
	public void setConceptos(boolean conceptos) {
		this.conceptos = conceptos;
	}
	public boolean isAnular() {
		return anular;
	}
	public void setAnular(boolean anular) {
		this.anular = anular;
	}
	public boolean isSalir() {
		return salir;
	}
	public void setSalir(boolean salir) {
		this.salir = salir;
	}
	public boolean isCerrarAnalisis() {
		return cerrarAnalisis;
	}
	public void setCerrarAnalisis(boolean cerrarAnalisis) {
		this.cerrarAnalisis = cerrarAnalisis;
	}
	public int getMuestraBotonAnalisisToCierraAnalisis() {
		return muestraBotonAnalisisToCierraAnalisis;
	}
	public void setMuestraBotonAnalisisToCierraAnalisis(
			int muestraBotonAnalisisToCierraAnalisis) {
		this.muestraBotonAnalisisToCierraAnalisis = muestraBotonAnalisisToCierraAnalisis;
	}
	public int getEvaluarAdjuntarArchivo() {
		return evaluarAdjuntarArchivo;
	}
	public void setEvaluarAdjuntarArchivo(int evaluarAdjuntarArchivo) {
		this.evaluarAdjuntarArchivo = evaluarAdjuntarArchivo;
	} 
	
	
}
