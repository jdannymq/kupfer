package org.domain.sck.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.domain.sck.entity.enums.TiempoType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "matriz_medicion")
public class MatrizMedicion {
	
	private Long 			system_id;
	private LcredTipoSolicitud link_solicitud;
	private Medicion 		link_condicion;
	private MatrizMontos 	link_montos;
	private Canal 			link_canal;
	private MatrizPlazoPromedioPagos link_plazoPromedioPago;
	private Long 			tiempo;
	private TiempoType		unidadTiempo;
	private boolean			disabled = false;
	
	
	@Id
	@GenericGenerator(name="matrizMedicionSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator",
	    parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_MATRIZMEDICION"), 
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	    }
	)
	@GeneratedValue(generator = "matrizMedicionSequenceGenerator")
	@Column(name = "system_id", unique = true, nullable = false, precision = 18, scale = 0)	
	public Long getSystem_id() {
		return system_id;
	}
	public void setSystem_id(Long system_id) {
		this.system_id = system_id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_solicitud")		
	public LcredTipoSolicitud getLink_solicitud() {
		return link_solicitud;
	}
	public void setLink_solicitud(LcredTipoSolicitud link_solicitud) {
		this.link_solicitud = link_solicitud;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_montos")	
	public MatrizMontos getLink_montos() {
		return link_montos;
	}
	public void setLink_montos(MatrizMontos link_montos) {
		this.link_montos = link_montos;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_plazoPromedioPago")
	public MatrizPlazoPromedioPagos getLink_plazoPromedioPago() {
		return link_plazoPromedioPago;
	}
	public void setLink_plazoPromedioPago(
			MatrizPlazoPromedioPagos link_plazoPromedioPago) {
		this.link_plazoPromedioPago = link_plazoPromedioPago;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_canal")	
	public Canal getLink_canal() {
		return link_canal;
	}
	public void setLink_canal(Canal link_canal) {
		this.link_canal = link_canal;
	}
	
	@Column(name = "tiempo")	
	public Long getTiempo() {
		return tiempo;
	}
	public void setTiempo(Long tiempo) {
		this.tiempo = tiempo;
	}
	
	@Column(name = "unidad_tiempo")	
	public TiempoType getUnidadTiempo() {
		return unidadTiempo;
	}
	public void setUnidadTiempo(TiempoType unidadTiempo) {
		this.unidadTiempo = unidadTiempo;
	}
	
	@Column(name = "disabled")	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_condicion")
	public Medicion getLink_condicion() {
		return link_condicion;
	}
	
	public void setLink_condicion(Medicion link_condicion) {
		this.link_condicion = link_condicion;
	}
	
	

}
