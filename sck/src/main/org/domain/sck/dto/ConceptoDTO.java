package org.domain.sck.dto;

import java.io.Serializable;
import java.util.Date;

import org.domain.sck.entity.enums.ArchivoAdjuntoType;

public class ConceptoDTO implements Serializable{

	private String codigo;
	private String descripcion;
	private long monto;
	private String montoFormateado;
	private String nombreArchivo;
	private String rutaCompleta;
	private String margen;
	private String usuario;
	private Date fechaCreacion;
	private Double margenParaSuma;
	private ArchivoAdjuntoType tipoArchivoType;
	private String margenOriginal;
	

	public String getMargen() {
		return margen;
	}
	public void setMargen(String margen) {
		this.margen = margen;
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
	public long getMonto() {
		return monto;
	}
	public void setMonto(long monto) {
		this.monto = monto;
	}
	public String getMontoFormateado() {
		return montoFormateado;
	}
	public void setMontoFormateado(String montoFormateado) {
		this.montoFormateado = montoFormateado;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getRutaCompleta() {
		return rutaCompleta;
	}

	public void setRutaCompleta(String rutaCompleta) {
		this.rutaCompleta = rutaCompleta;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public ArchivoAdjuntoType getTipoArchivoType() {
		return tipoArchivoType;
	}
	public void setTipoArchivoType(ArchivoAdjuntoType tipoArchivoType) {
		this.tipoArchivoType = tipoArchivoType;
	}
	public Double getMargenParaSuma() {
		return margenParaSuma;
	}
	public void setMargenParaSuma(Double margenParaSuma) {
		this.margenParaSuma = margenParaSuma;
	}
	public String getMargenOriginal() {
		return margenOriginal;
	}
	public void setMargenOriginal(String margenOriginal) {
		this.margenOriginal = margenOriginal;
	}
	
	
	
	
}
