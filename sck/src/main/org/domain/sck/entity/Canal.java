package org.domain.sck.entity;

// Generated 29-05-2012 04:33:41 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Canal generated by hbm2java
 */
@Entity
@Table(name = "canal")
public class Canal  {

	private long systemId;
	private String codigo;
	private String  descripcion;
	private boolean disabled;
	
	

	public Canal() {
		disabled = false;
	}

	public Canal(long systemId) {
		this.systemId = systemId;
	}

	@Id
	@GenericGenerator(name="canalSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator",
	    parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_CANAL"), 
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	    }
	)
	@GeneratedValue(generator = "canalSequenceGenerator")
	@Column(name = "system_id", unique = true, nullable = false, precision = 18, scale = 0,columnDefinition = "numeric(18,0)")
	public long getSystemId() {
		return this.systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	
	@Column(name = "codigo", nullable = false, length = 5)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	
	@Column(name = "descripcion", nullable = false, length = 100)
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	
	
	@Column(name = "disabled", nullable = false)
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}
