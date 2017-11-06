package org.domain.sck.dto;

        import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.domain.sck.entity.Sociedad;
        import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudUsuarioDerivada;
import org.domain.sck.entity.Usuariosegur;

public class SolicitudDTO implements Serializable{

    private long idSolictud;
    private String rut;
    private String razonSocial;
    private String emisor;
    private String despTipoSolictud;
    private Date fechaEmision;
    private Date horaEmision;
    private Date fechaSalida;
    private String codigoEstado;
    private String estado;
    private String proceso;
    private String usuarioProceso;
    private double montoAsegurado;
    private double monto;
    private double montoRiegoKupfer;
    private String tipoSolicitud;
    private String canal;
    private String sucursal;
    private String condicionPago;
    private Boolean evaluar;
    private Boolean analizar;
    private int controlador;
    private double peakCredito;
    private List<SolicitudUsuarioDerivada> listaUsuarioDerivada;
    private boolean habilitaLista;
    private String estadoEvaluacion;
    private List<SolicitudUsuarioDerivada> listaDerivados;
    private SolicitudUsuarioDerivada derivado;
    private Usuariosegur usuarioAsignar;
    private String usuarioTomada;
    private String codSucursalEmisor;
    private String fechaString;
    private String razonSocialString;
    private List<SolicitudHitos> hitos;
    private Long tiempoEmisor;
    private Long tiempoAprobadores;
    private Long tiempoAnalista;
    private Long tiempoInicial;
    private String sociedad;



    public String getSociedad() {
		return sociedad;
	}
	public void setSociedad(String sociedad) {
		this.sociedad = sociedad;
	}
	public List<SolicitudUsuarioDerivada> getListaUsuarioDerivada() {
        return listaUsuarioDerivada;
    }
    public void setListaUsuarioDerivada(
            List<SolicitudUsuarioDerivada> listaUsuarioDerivada) {
        this.listaUsuarioDerivada = listaUsuarioDerivada;
    }
    public boolean isHabilitaLista() {
        return habilitaLista;
    }
    public void setHabilitaLista(boolean habilitaLista) {
        this.habilitaLista = habilitaLista;
    }
    public long getIdSolictud() {
        return idSolictud;
    }
    public void setIdSolictud(long idSolictud) {
        this.idSolictud = idSolictud;
    }
    public String getRut() {
        return rut;
    }
    public void setRut(String rut) {
        this.rut = rut;
    }
    public String getRazonSocial() {
        return razonSocial;
    }
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    public String getDespTipoSolictud() {
        return despTipoSolictud;
    }
    public void setDespTipoSolictud(String despTipoSolictud) {
        this.despTipoSolictud = despTipoSolictud;
    }
    public Date getFechaEmision() {
        return fechaEmision;
    }
    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    public Date getHoraEmision() {
        return horaEmision;
    }
    public void setHoraEmision(Date horaEmision) {
        this.horaEmision = horaEmision;
    }
    public Date getFechaSalida() {
        return fechaSalida;
    }
    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getProceso() {
        return proceso;
    }
    public void setProceso(String proceso) {
        this.proceso = proceso;
    }
    public String getUsuarioProceso() {
        return usuarioProceso;
    }
    public void setUsuarioProceso(String usuarioProceso) {
        this.usuarioProceso = usuarioProceso;
    }
    public double getMontoAsegurado() {
        return montoAsegurado;
    }
    public void setMontoAsegurado(double montoAsegurado) {
        this.montoAsegurado = montoAsegurado;
    }
    public double getMonto() {
        return monto;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
    public double getMontoRiegoKupfer() {
        return montoRiegoKupfer;
    }
    public void setMontoRiegoKupfer(double montoRiegoKupfer) {
        this.montoRiegoKupfer = montoRiegoKupfer;
    }
    public String getEmisor() {
        return emisor;
    }
    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }
    public String getTipoSolicitud() {
        return tipoSolicitud;
    }
    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }
    public String getCanal() {
        return canal;
    }
    public void setCanal(String canal) {
        this.canal = canal;
    }
    public String getSucursal() {
        return sucursal;
    }
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }
    public String getCondicionPago() {
        return condicionPago;
    }
    public void setCondicionPago(String condicionPago) {
        this.condicionPago = condicionPago;
    }
    public Boolean getEvaluar() {
        return evaluar;
    }
    public void setEvaluar(Boolean evaluar) {
        this.evaluar = evaluar;
    }
    public Boolean getAnalizar() {
        return analizar;
    }
    public void setAnalizar(Boolean analizar) {
        this.analizar = analizar;
    }
    public int getControlador() {
        return controlador;
    }
    public void setControlador(int controlador) {
        this.controlador = controlador;
    }
    public String getCodigoEstado() {
        return codigoEstado;
    }
    public void setCodigoEstado(String codigoEstado) {
        this.codigoEstado = codigoEstado;
    }
    public String getEstadoEvaluacion() {
        return estadoEvaluacion;
    }
    public void setEstadoEvaluacion(String estadoEvaluacion) {
        this.estadoEvaluacion = estadoEvaluacion;
    }
    public List<SolicitudUsuarioDerivada> getListaDerivados() {
        return listaDerivados;
    }
    public void setListaDerivados(List<SolicitudUsuarioDerivada> listaDerivados) {
        this.listaDerivados = listaDerivados;
    }
    public SolicitudUsuarioDerivada getDerivado() {
        return derivado;
    }
    public void setDerivado(SolicitudUsuarioDerivada derivado) {
        this.derivado = derivado;
    }
    public Usuariosegur getUsuarioAsignar() {
        return usuarioAsignar;
    }
    public void setUsuarioAsignar(Usuariosegur usuarioAsignar) {
        this.usuarioAsignar = usuarioAsignar;
    }
    public String getUsuarioTomada() {
        return usuarioTomada;
    }
    public void setUsuarioTomada(String usuarioTomada) {
        this.usuarioTomada = usuarioTomada;
    }
    public String getCodSucursalEmisor() {
        return codSucursalEmisor;
    }
    public void setCodSucursalEmisor(String codSucursalEmisor) {
        this.codSucursalEmisor = codSucursalEmisor;
    }

    public String getFechaString() {
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }
    public String getRazonSocialString() {
        return razonSocialString;
    }
    public void setRazonSocialString(String razonSocialString) {
        this.razonSocialString = razonSocialString;
    }
    public double getPeakCredito() {
        return peakCredito;
    }
    public void setPeakCredito(double peakCredito) {
        this.peakCredito = peakCredito;
    }

    public List<SolicitudHitos> getHitos() {
        return hitos;
    }

    public void setHitos(List<SolicitudHitos> hitos) {
        this.hitos = hitos;
    }

    public Long getTiempoEmisor() {
        return tiempoEmisor;
    }

    public void setTiempoEmisor(Long tiempoEmisor) {
        this.tiempoEmisor = tiempoEmisor;
    }

    public Long getTiempoAprobadores() {
        return tiempoAprobadores;
    }

    public void setTiempoAprobadores(Long tiempoAprobadores) {
        this.tiempoAprobadores = tiempoAprobadores;
    }

    public Long getTiempoAnalista() {
        return tiempoAnalista;
    }

    public void setTiempoAnalista(Long tiempoAnalista) {
        this.tiempoAnalista = tiempoAnalista;
    }

    public Long getTiempoInicial() {
        return tiempoInicial;
    }

    public void setTiempoInicial(Long tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }
    
    
}