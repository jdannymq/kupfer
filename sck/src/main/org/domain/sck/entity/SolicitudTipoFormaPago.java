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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotNull;

/**
 * SolicitudTipoFormaPago generated by hbm2java
 */
@Entity
@Table(name = "solicitud_tipo_forma_pago")
public class SolicitudTipoFormaPago {

	private long idSystem;
	private Long idSolicitud;
	private TipoFormaPago tipoFormaPago;
	private FormaPagoEncabezado encabezado;
	
	public SolicitudTipoFormaPago() {
		
	}

	public SolicitudTipoFormaPago(long idSystem, Long solicitud,TipoFormaPago tipoFormaPago,FormaPagoEncabezado encabezado) {
		this.idSystem = idSystem;
		this.idSolicitud = solicitud;
		this.tipoFormaPago = tipoFormaPago;
		this.encabezado = encabezado;
	}

	@Id
	@GenericGenerator(name = "solicitudTipoFormaPagoSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator", parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_SOLICITUD_TIPO_FORMA_PAGO"),
			@Parameter(name = "initial_value", value = "1"),
			@Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "solicitudTipoFormaPagoSequenceGenerator")
	@Column(name = "idSystem", unique = true, nullable = false, precision = 18, scale = 0)
	public Long getIdSystem() {
		return this.idSystem;
	}

	public void setIdSystem(long idSystem) {
		this.idSystem = idSystem;
	}
	
	@Column(name = "solicitud_id", nullable = false, precision = 18, scale = 0)
	public Long getIdSolicitud() {
		return idSolicitud;
	}


	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encabezado_id", nullable = false)
	@NotNull
	public FormaPagoEncabezado getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(FormaPagoEncabezado encabezado) {
		this.encabezado = encabezado;
	}	
	
}
