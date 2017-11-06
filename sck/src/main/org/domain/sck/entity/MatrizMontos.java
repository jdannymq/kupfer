package org.domain.sck.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "matriz_montos")
public class MatrizMontos {
	
	private Long system_id;
	private Long monto_inicial;
	private Long monto_final;
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
	
	@Column(name = "monto_inicial")	
	public Long getMonto_inicial() {
		return monto_inicial;
	}
	public void setMonto_inicial(Long monto_inicial) {
		this.monto_inicial = monto_inicial;
	}
	
	@Column(name = "monto_final")
	public Long getMonto_final() {
		return monto_final;
	}
	public void setMonto_final(Long monto_final) {
		this.monto_final = monto_final;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	
	
	
	
	
	
	
	

}
