package org.domain.sck.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "matriz_plazos")
public class MatrizPlazoPromedioPagos {
	
	private Long system_id;
	private Long plazo_inicial;
	private Long plazo_final;
	private String descripcion = "sin descripcion";
	private Boolean disabled = false;

	

	@Id
	@GenericGenerator(name="matrizMontoSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator",
	    parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_MTRZ_MONTO"), 
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	    }
	)
	@GeneratedValue(generator = "matrizMontoSequenceGenerator")
	@Column(name = "system_id", unique = true, nullable = false, precision = 18, scale = 0)	
	public Long getSystem_id() {
		return system_id;
	}
	public void setSystem_id(Long system_id) {
		this.system_id = system_id;
	}
	
	@Column(name = "plazo_inicial")	
	public Long getPlazo_inicial() {
		return plazo_inicial;
	}
	
	public void setPlazo_inicial(Long plazo_inicial) {
		this.plazo_inicial = plazo_inicial;
	}
	
	@Column(name = "plazo_final")
	public Long getPlazo_final() {
		return plazo_final;
	}
	public void setPlazo_final(Long plazo_final) {
		this.plazo_final = plazo_final;
	}
	
	
	@Column(name = "descripcion")	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name = "disabled")	
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

}
