package org.domain.sck.entity;

// Generated 02-10-2012 09:54:39 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.domain.sck.entity.enums.EstadoEntityType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotNull;

/**
 * AsignaFormaPago generated by hbm2java
 */
@Entity
@Table(name = "forma_pago_encabeza")
public class FormaPagoEncabezado {

	private Long idEncabezado;
	private String descripcion;
	private TipoFormaPago tipoFormaPago;
	private LcredTipoSolicitud tipo;
	private EstadoEntityType estado;
	
	
	public FormaPagoEncabezado() {

	}

	public FormaPagoEncabezado(long idEncabezado, TipoFormaPago tipoFormaPago,String descripcion, EstadoEntityType estado) {
		this.idEncabezado = idEncabezado;
		this.tipoFormaPago = tipoFormaPago;
		this.descripcion = descripcion;
		this.estado = estado;
	}

	@Id
	@GenericGenerator(name = "formaPagoEncSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator", parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_FORMA_PAGO_ENC"),
			@Parameter(name = "initial_value", value = "1"),
			@Parameter(name = "increment_size", value = "1") })
			
			
	@GeneratedValue(generator = "formaPagoEncSequenceGenerator")
	@Column(name = "id_encabezado", unique = true, nullable = false, precision = 18, scale = 0)
	public Long getIdEncabezado() {
		return idEncabezado;
	}

	public void setIdEncabezado(Long idEncabezado) {
		this.idEncabezado = idEncabezado;
	}
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_forma_pago_id", nullable = false)
	@NotNull
	public TipoFormaPago getTipoFormaPago() {
		return tipoFormaPago;
	}

	public void setTipoFormaPago(TipoFormaPago tipoFormaPago) {
		this.tipoFormaPago = tipoFormaPago;
	}
	

	@Column(name = "descripcion", nullable = false)
	@NotNull
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_solicitud_id", nullable = true)
	@NotNull	
	public LcredTipoSolicitud getTipo() {
		return tipo;
	}

	public void setTipo(LcredTipoSolicitud tipo) {
		this.tipo = tipo;
	}

	
	@Column(name = "estado", nullable = false)
	public EstadoEntityType getEstado() {
		return this.estado;
	}

	public void setEstado(EstadoEntityType estado) {
		this.estado = estado;
	}
	
	
}