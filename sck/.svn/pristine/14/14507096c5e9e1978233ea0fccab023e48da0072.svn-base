package org.domain.sck.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotNull;


@Entity
@Table(name = "matriz_nivel_servicio")
public class MatrizNivelServicio {
	
	private Long systemId;
	private LcredTipoSolicitud tipoSolicitud;
	private Long tiempoEmisor = 0l;
	private Long tiempoAnalista = 0l;
	private Long tiempoAprobador = 0l;
	private Long tiempoInicial = 0l;
	private boolean disabled = false;
	
	
	@Id
	@GenericGenerator(name="matrizNivelServicioSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator",
	    parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_MATRIZNIVELSERVICIO"), 
	        @Parameter(name = "initial_value", value = "10"),
	        @Parameter(name = "increment_size", value = "1")
	    }
	)
	@GeneratedValue(generator = "matrizNivelServicioSequenceGenerator")
	@Column(name = "system_id", unique = true, nullable = false, precision = 18, scale = 0)	
	public Long getSystemId() {
		return systemId;
	}
	public void setSystemId(Long system_id) {
		this.systemId = system_id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_solicitud")		
	public LcredTipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}
	
	public void setTipoSolicitud(LcredTipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	
	@Column(name = "tiempo_analista", nullable = false)
	@NotNull
	public Long getTiempoAnalista() {
		return tiempoAnalista;
	}
	
	public void setTiempoAnalista(Long tiempoAnalista) {
		this.tiempoAnalista = tiempoAnalista;
	}
	
	@Column(name = "tiempo_aprobador", nullable = false)
	@NotNull
	public Long getTiempoAprobador() {
		return tiempoAprobador;
	}
	
	public void setTiempoAprobador(Long tiempoAprobador) {
		this.tiempoAprobador = tiempoAprobador;
	}
	
	@Column(name = "tiempo_emisor", nullable = false)
	@NotNull
	public Long getTiempoEmisor() {
		return tiempoEmisor;
	}
	
	public void setTiempoEmisor(Long tiempoEmisor) {
		this.tiempoEmisor = tiempoEmisor;
	}
	
	@Column(name = "tiempo_inicial", nullable = false)
	@NotNull
	public Long getTiempoInicial() {
		return tiempoInicial;
	}
	
	public void setTiempoInicial(Long tiempoInicial) {
		this.tiempoInicial = tiempoInicial;
	}
	
	@Column(name = "disabled")	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	
	
	

}
