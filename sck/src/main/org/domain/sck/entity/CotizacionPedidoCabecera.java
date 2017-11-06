package org.domain.sck.entity;

// Generated 29-05-2012 04:33:41 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotNull;

/**
 * CotizacionPedidoCabecera generated by hbm2java
 */
@Entity
@Table(name = "cotizacion_pedido_cabecera")
public class CotizacionPedidoCabecera  {

	private long systemId;
	private LcredSolicitud solicitud;
	private Long neto;
	private Long total;
	private Double porcentaje;

	
	public CotizacionPedidoCabecera() {
	}

	public CotizacionPedidoCabecera(long systemId) {
		this.systemId = systemId;
	}

	public CotizacionPedidoCabecera(long systemId, LcredSolicitud solicitud,
			FormaPago formaPago, Long NCotiPed, Long neto, Long total,Double porcentaje) {
		this.systemId = systemId;
		this.solicitud = solicitud;
		this.total = total;
		this.neto = neto;
		this.porcentaje = porcentaje;
	}

	@Id
	@GenericGenerator(name="cotPedCabeceraSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator",
	    parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_COTIZACION_PEDIDO_CABECERA"), 
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	    }
	)
	@GeneratedValue(generator = "cotPedCabeceraSequenceGenerator")
	@Column(name = "system_id", unique = true, nullable = false, precision = 18, scale = 0,columnDefinition = "numeric(18,0)")
	public long getSystemId() {
		return this.systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = {@JoinColumn(name = "solicitud_id", referencedColumnName="num_solicitud", columnDefinition = "numeric(18,0)"),
			              @JoinColumn(name = "fecha_solicitud", referencedColumnName = "fec_solicitud")})
	@NotNull
	public LcredSolicitud getSolicitud() {
		return this.solicitud;
	}

	public void setSolicitud(LcredSolicitud solicitud) {
		this.solicitud = solicitud;
	}

	
	@Column(name = "neto", precision = 18, scale = 0)
	public Long getNeto() {
		return neto;
	}
	
	public void setNeto(Long neto) {
		this.neto = neto;
	}

	@Column(name = "total", precision = 18, scale = 0)
	public Long getTotal() {
		return total;
	}
	
	public void setTotal(Long total) {
		this.total = total;
	}
	@Column(name = "porcentaje", precision = 18, scale = 0,columnDefinition = "numeric(18,3)",nullable = true)
	public Double getPorcentaje() {
		return porcentaje;
	}
	
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
	
}