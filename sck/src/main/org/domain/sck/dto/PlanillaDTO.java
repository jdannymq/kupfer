package org.domain.sck.dto;

import org.domain.sck.entity.enums.TipoSolicitudCodigoType;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PlanillaDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idSolictud;
    private Long numSolicitud;
	private String fechaEmision;
    private Date fechaEmisor;
    private String emisor = ""; // Ejecutivo
    private String nombreEmisor = ""; // Ejecutivo
    private String horaEmision;
	private String rut;
	private String razonSocial;
	private String monto;
	private String tipoSolicitud;
	private String despTipoSolictud;
	private String aprobadores;
	private String conceptosInvolucrados;
	private long totalDeMinutosestado;
	private String descripcionEstado;
	private String tiempo;
	private Set<String> tipoHitos = new HashSet<String>();
	

	private List<PlanillaHitosDTO> listaHitos;
    private TipoSolicitudCodigoType tipoSolicitudCodigoType;
    
    private Long tiempoHitoInicial;
    private Long tiempoEmisor;
    private Long tiempoAprobadores;
    private Long tiempoAnalista;
    private Long tiempoInicial;
    private Long totalTime;
    private String tiempoTotal = "";
    private String hitoEmisor = "";
    private String hitoEjecutivo = "";
    private String hitoAnalista = "";
    private String hitoAprobadores = "";
    private String tipoHito = "";
    private String cumplio = "";
    private EstadoNivelServicioSolicitud cumplida = EstadoNivelServicioSolicitud.CUMPLIDA;
    private EstadoNivelServicioSolicitud cumplidoInicial = EstadoNivelServicioSolicitud.NO_APLICA;
    private EstadoNivelServicioSolicitud cumplidoAnalista = EstadoNivelServicioSolicitud.NO_APLICA;
    private EstadoNivelServicioSolicitud cumplidoEmisor = EstadoNivelServicioSolicitud.NO_APLICA;
    private EstadoNivelServicioSolicitud cumplidoAprobadores = EstadoNivelServicioSolicitud.NO_APLICA;
	private String fechaModificadaAsString = "";
	private String fechaRealAsString = "";
	
	

	public String getIdSolictud() {
		return idSolictud;
	}
	public void setIdSolictud(String idSolictud) {
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
	
	public String getCumplio() {
		return cumplio;
	}
	
	public void setCumplio(String cumplio) {
		this.cumplio = cumplio;
	}
	
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	public String getAprobadores() {
		return aprobadores;
	}
	public void setAprobadores(String aprobadores) {
		this.aprobadores = aprobadores;
	}
	public String getConceptosInvolucrados() {
		return conceptosInvolucrados;
	}
	public void setConceptosInvolucrados(String conceptosInvolucrados) {
		this.conceptosInvolucrados = conceptosInvolucrados;
	}
	public long getTotalDeMinutosestado() {
		return totalDeMinutosestado;
	}
	public void setTotalDeMinutosestado(long totalDeMinutosestado) {
		this.totalDeMinutosestado = totalDeMinutosestado;
	}
	public String getDescripcionEstado() {
		return descripcionEstado;
	}
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}
	public String getTiempoTotal() {
		return tiempoTotal;
	}
	public void setTiempoTotal(String tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}
	public List<PlanillaHitosDTO> getListaHitos() {
		return listaHitos;
	}
	public void setListaHitos(List<PlanillaHitosDTO> listaHitos) {
		this.listaHitos = listaHitos;
	}
	public String getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getHoraEmision() {
		return horaEmision;
	}
	public void setHoraEmision(String horaEmision) {
		this.horaEmision = horaEmision;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    public Long getNumSolicitud() {
        return numSolicitud;
    }

    public void setNumSolicitud(Long numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    public Date getFechaEmisor() {
        return fechaEmisor;
    }

    public void setFechaEmisor(Date fechaEmisor) {
        this.fechaEmisor = fechaEmisor;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public TipoSolicitudCodigoType getTipoSolicitudCodigoType() {
        return tipoSolicitudCodigoType;
    }

    public void setTipoSolicitudCodigoType(TipoSolicitudCodigoType tipoSolicitudCodigoType) {
        this.tipoSolicitudCodigoType = tipoSolicitudCodigoType;
    }

    public Long getTiempoHitoInicial() {
        return tiempoHitoInicial;
    }

    public void setTiempoHitoInicial(Long tiempoHitoInicial) {
        this.tiempoHitoInicial = tiempoHitoInicial;
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

    public String getNombreEmisor() {
		return nombreEmisor;
	}
	public void setNombreEmisor(String nombreEmisor) {
		this.nombreEmisor = nombreEmisor;
	}
	public Long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
	
	public EstadoNivelServicioSolicitud getCumplida() {
		return cumplida;
	}
	
	public void setCumplida(EstadoNivelServicioSolicitud cumplida) {
		this.cumplida = cumplida;
	}
	
	public EstadoNivelServicioSolicitud getCumplidoInicial() {
		return cumplidoInicial;
	}
	public void setCumplidoInicial(EstadoNivelServicioSolicitud cumplidoInicial) {
		this.cumplidoInicial = cumplidoInicial;
	}
	public EstadoNivelServicioSolicitud getCumplidoAnalista() {
		return cumplidoAnalista;
	}
	public void setCumplidoAnalista(EstadoNivelServicioSolicitud cumplidoAnalista) {
		this.cumplidoAnalista = cumplidoAnalista;
	}
	public EstadoNivelServicioSolicitud getCumplidoEmisor() {
		return cumplidoEmisor;
	}
	public void setCumplidoEmisor(EstadoNivelServicioSolicitud cumplidoEmisor) {
		this.cumplidoEmisor = cumplidoEmisor;
	}
	public EstadoNivelServicioSolicitud getCumplidoAprobadores() {
		return cumplidoAprobadores;
	}
	public void setCumplidoAprobadores(
			EstadoNivelServicioSolicitud cumplidoAprobadores) {
		this.cumplidoAprobadores = cumplidoAprobadores;
	}

    public String getHitoEmisor() {
        return hitoEmisor;
    }

    public void setHitoEmisor(String hitoEmisor) {
        this.hitoEmisor = hitoEmisor;
    }

    public String getHitoEjecutivo() {
        return hitoEjecutivo;
    }

    public void setHitoEjecutivo(String hitoEjecutivo) {
        this.hitoEjecutivo = hitoEjecutivo;
    }

    public String getHitoAnalista() {
        return hitoAnalista;
    }

    public void setHitoAnalista(String hitoAnalista) {
        this.hitoAnalista = hitoAnalista;
    }

    public String getHitoAprobadores() {
        return hitoAprobadores;
    }

    public void setHitoAprobadores(String hitoAprobadores) {
        this.hitoAprobadores = hitoAprobadores;
    }

    public String getTipoHito() {
        return tipoHito;
    }

    public void setTipoHito(String tipoHito) {
        this.tipoHito = tipoHito;
    }
    
    public String getFechaModificadaAsString() {
		return fechaModificadaAsString;
	}
    
    public void setFechaModificadaAsString(String fechaModificadaAsString) {
		this.fechaModificadaAsString = fechaModificadaAsString;
	}
    
    public String getFechaRealAsString() {
		return fechaRealAsString;
	}
    
    public void setFechaRealAsString(String fechaRealAsString) {
		this.fechaRealAsString = fechaRealAsString;
	}

    public Set<String> getTipoHitos() {
		return tipoHitos;
	}
    
    public void setTipoHitos(Set<String> tipoHitos) {
		this.tipoHitos = tipoHitos;
	}
    
    
    private String getTimeAsHHMMSS(Long time) {
        long millis = time;
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return (hours < 10? "0"+hours : hours ) + ":" +
                (minutes <10 ? "0"+minutes : minutes) + ":" + (seconds<10 ? "0"+seconds : seconds);
    }



    public String getTotalTimeAsHHMMSS() {
        return getTimeAsHHMMSS(totalTime == null ? 0 : totalTime);
    }

    public String getAnalystTimeAsHHMMSS() {
        return getTimeAsHHMMSS(tiempoAnalista == null ? 0 : tiempoAnalista);
    }

    public String getEmisorTimeAsHHMMSS() {
        return getTimeAsHHMMSS(tiempoEmisor == null ? 0 : tiempoEmisor);
    }

    public String getInitialTimeAsHHMMSS() {
        return getTimeAsHHMMSS(tiempoInicial == null ? 0 : tiempoInicial);

    }

    public String getApproverTimeAsHHMMSS() {
        return getTimeAsHHMMSS(tiempoAprobadores);
    }


}
