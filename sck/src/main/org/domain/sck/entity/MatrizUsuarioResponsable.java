package org.domain.sck.entity;

// Generated 29-05-2012 04:33:41 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotNull;


/**
 * MatrizUsuarioResponsable generated by hbm2java
 */
@Entity
@Table(name = "matriz_usuario_responsable")
public class MatrizUsuarioResponsable  {

	private long systemId;
	private Usuariosegur usuariosegur;
	private Date fechaIngreso;
	private Usuariosegur usuarioIngreso; 
	private boolean disabled;


	public MatrizUsuarioResponsable() {
		disabled = false;
	}

	public MatrizUsuarioResponsable(long systemId) {
		this.systemId = systemId;
	}

	@Id
	@GenericGenerator(name="matrizUsuarioResponsableSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator",
	    parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_MATRIZ_USUARIO_RESPONSABLE"), 
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	    }
	)
	@GeneratedValue(generator = "matrizUsuarioResponsableSequenceGenerator")
	@Column(name = "system_id", unique = true, nullable = false, precision = 18, scale = 0,columnDefinition = "numeric(18,0)")
	public long getSystemId() {
		return this.systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = true)
	@NotNull	
	public Usuariosegur getUsuariosegur() {
		return usuariosegur;
	}

	public void setUsuariosegur(Usuariosegur usuariosegur) {
		this.usuariosegur = usuariosegur;
	}
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_ingreso", nullable = false, length = 23)
	@NotNull
	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_ingreso_id", nullable = true)
	@NotNull	
	public Usuariosegur getUsuarioIngreso() {
		return usuarioIngreso;
	}

	public void setUsuarioIngreso(Usuariosegur usuarioIngreso) {
		this.usuarioIngreso = usuarioIngreso;
	}
	
	
	@Column(name = "disabled", nullable = false)
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}