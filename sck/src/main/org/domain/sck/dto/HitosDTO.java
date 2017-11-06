package org.domain.sck.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.domain.sck.entity.enums.TipoCuentasKupferType;

public class HitosDTO implements Serializable{

	private long idSystem; 
	private String codEstadoDeriva;
	private String estado;
	private String descripcionEstado;
	private String emisor;
	private String fechaCreacion;
	private String horaCreacion;
	private String fechaHitos;
	private String horaHitos;
	private long idSolicitud;
	private String usuarioActualHitos;
	private String accionAdmiistrador;
	private BigDecimal lineaCreditoDisponible;
	private TipoCuentasKupferType canal;
	private String tipoSolicitud;
	
	/*solo datos de emergencia de busqueda de data de planilla*/
	private String rut;
	private String nombre;
	private Long monto;
	private String aprobadores;
	private String conceptos;
	private String tiempoInicial;
	private String descripcionTipoSolicitud;
	

	 
	
	/* sets y gets*/
	public long getIdSystem() {
		return idSystem;
	}
	public void setIdSystem(long idSystem) {
		this.idSystem = idSystem;
	}
	public String getCodEstadoDeriva() {
		return codEstadoDeriva;
	}
	public void setCodEstadoDeriva(String codEstadoDeriva) {
		this.codEstadoDeriva = codEstadoDeriva;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getDescripcionEstado() {
		return descripcionEstado;
	}
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}
	public String getEmisor() {
		return emisor;
	}
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getHoraCreacion() {
		return horaCreacion;
	}
	public void setHoraCreacion(String horaCreacion) {
		this.horaCreacion = horaCreacion;
	}
	public String getFechaHitos() {
		return fechaHitos;
	}
	public void setFechaHitos(String fechaHitos) {
		this.fechaHitos = fechaHitos;
	}
	public String getHoraHitos() {
		return horaHitos;
	}
	public void setHoraHitos(String horaHitos) {
		this.horaHitos = horaHitos;
	}
	public long getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getUsuarioActualHitos() {
		return usuarioActualHitos;
	}
	public void setUsuarioActualHitos(String usuarioActualHitos) {
		this.usuarioActualHitos = usuarioActualHitos;
	}
	public String getAccionAdmiistrador() {
		return accionAdmiistrador;
	}
	public void setAccionAdmiistrador(String accionAdmiistrador) {
		this.accionAdmiistrador = accionAdmiistrador;
	}
	public BigDecimal getLineaCreditoDisponible() {
		return lineaCreditoDisponible;
	}
	public void setLineaCreditoDisponible(BigDecimal lineaCreditoDisponible) {
		this.lineaCreditoDisponible = lineaCreditoDisponible;
	}
	public TipoCuentasKupferType getCanal() {
		return canal;
	}
	public void setCanal(TipoCuentasKupferType canal) {
		this.canal = canal;
	}
	public String getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Long getMonto() {
		return monto;
	}
	public void setMonto(Long monto) {
		this.monto = monto;
	}
	public String getAprobadores() {
		return aprobadores;
	}
	public void setAprobadores(String aprobadores) {
		this.aprobadores = aprobadores;
	}
	public String getConceptos() {
		return conceptos;
	}
	public void setConceptos(String conceptos) {
		this.conceptos = conceptos;
	}
	public String getTiempoInicial() {
		return tiempoInicial;
	}
	public void setTiempoInicial(String tiempoInicial) {
		this.tiempoInicial = tiempoInicial;
	}
	public String getDescripcionTipoSolicitud() {
		return descripcionTipoSolicitud;
	}
	public void setDescripcionTipoSolicitud(String descripcionTipoSolicitud) {
		this.descripcionTipoSolicitud = descripcionTipoSolicitud;
	}
	

	
	
}