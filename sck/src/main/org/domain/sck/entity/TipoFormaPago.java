package org.domain.sck.entity;

// Generated 02-10-2012 09:54:39 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotNull;

/**
 * TipoFormaPago generated by hbm2java
 */
@Entity
@Table(name = "tipo_forma_pago")
public class TipoFormaPago {

	private long idTipoFormaPago;
	private String descripcion;
	public TipoFormaPago() {
	}

	public TipoFormaPago(Long idTipoFormaPago, String descripcion) {
		this.idTipoFormaPago = idTipoFormaPago;
		this.descripcion = descripcion;

	}

	@Id
	@GenericGenerator(name = "tipoformaPagoSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator", parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_TIPO_FORMA_PAGO"),
			@Parameter(name = "initial_value", value = "1"),
			@Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "tipoformaPagoSequenceGenerator")
	@Column(name = "id_tipo_forma_pago", unique = true, nullable = false, precision = 18, scale = 0)
	public Long getIdTipoFormaPago() {
		return this.idTipoFormaPago;
	}

	public void setIdTipoFormaPago(long idTipoFormaPago) {
		this.idTipoFormaPago = idTipoFormaPago;
	}
	

	@Column(name = "descripcion", nullable = false)
	@NotNull
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
