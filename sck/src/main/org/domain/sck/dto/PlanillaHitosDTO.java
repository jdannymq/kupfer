package org.domain.sck.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PlanillaHitosDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long idSystem;
    private Date fechaHito;
    private Date fechaRealHito;
    private String fechaRealAsString;
    private String fechaModificadaAsString;
	private String fecha;
	private String hora;
	private String estado;
	private String descripcionEstado;
	private String tiempo;
	private String tiempoTotal;
    private Long tiempoHito = 0l;
    private String usuarioUsername;
    private String usuarioNombre;
	
	
	/* sets y gets*/
	public long getIdSystem() {
		return idSystem;
	}
	public void setIdSystem(long idSystem) {
		this.idSystem = idSystem;
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
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    public Date getFechaHito() {
        return fechaHito;
    }
    public void setFechaHito(Date fechaHito) {
        this.fechaHito = fechaHito;
    }

    public Long getTiempoHito() {
        return tiempoHito;
    }

    public void setTiempoHito(Long tiempoHito) {
        this.tiempoHito = tiempoHito;
    }

    public String getUsuarioUsername() {
        return usuarioUsername;
    }

    public void setUsuarioUsername(String usuarioUsername) {
        this.usuarioUsername = usuarioUsername;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
    
    public Date getFechaRealHito() {
		return fechaRealHito;
	}
    
    public void setFechaRealHito(Date fechaRealHito) {
		this.fechaRealHito = fechaRealHito;
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

    public String getTimeAsHHMMSS() {
        long millis = tiempoHito;
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return (hours < 10? "0"+hours : hours ) + ":" + 
        		(minutes <10 ? "0"+minutes : minutes) + ":" + (seconds<10 ? "0"+seconds : seconds);
    }
}
