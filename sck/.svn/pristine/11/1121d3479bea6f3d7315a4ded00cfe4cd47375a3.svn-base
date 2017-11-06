package org.domain.sck.entity;

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
import org.hibernate.validator.Length;

@Entity
@Table(name = "DeudaActual")
public class DeudaActual {
	
	private Long systemId;
	private String numDcto;
	private String numFactura;
	private String dctoContable;
	private Date fechaVcto;
	private Date fechaCont;
	private Boolean protesto;
	private Long monto;
	private String claseDoc;
	private Integer diasMora;
	private String texto;
	private String indCME;
	private String referencia;
	private String codCliente;
	private Date fechaIngreso;
	private String rut;
	
	
	
	public DeudaActual() {}
	
	@Id
	@GenericGenerator(name="deudaActualSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator",
	    parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_DEUDA_ACTUAL"), 
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	    }
	)
	@GeneratedValue(generator = "deudaActualSequenceGenerator")
	@Column(name = "system_id", unique = true, nullable = false, precision = 18, scale = 0,columnDefinition = "numeric(18,0)")
	public Long getSystemId() {
		return systemId;
	}
	
	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}
	
	@Column(name = "numDcto")
	public String getNumDcto() {
		return numDcto;
	}
	
	public void setNumDcto(String numDcto) {
		this.numDcto = numDcto;
	}
	
	@Column(name = "numFactura")
	public String getNumFactura() {
		return numFactura;
	}
	
	public void setNumFactura(String numFactura) {
		this.numFactura = numFactura;
	}
	
	@Column(name = "dctoContable", length = 50)
	@Length(max = 50)
	public String getDctoContable() {
		return dctoContable;
	}
	
	public void setDctoContable(String dctoContable) {
		this.dctoContable = dctoContable;
	}
	
	@Column(name = "fechaVcto", length = 50)
	public Date getFechaVcto() {
		return fechaVcto;
	}
	
	public void setFechaVcto(Date fechaVcto) {
		this.fechaVcto = fechaVcto;
	}
	
	@Column(name = "protesto")
	public Boolean getProtesto() {
		return protesto;
	}
	
	public void setProtesto(Boolean protesto) {
		this.protesto = protesto;
	}
	
	@Column(name = "claseDoc")
	public String getClaseDoc() {
		return claseDoc;
	}
	
	public void setClaseDoc(String claseDoc) {
		this.claseDoc = claseDoc;
	}
	
	@Column(name = "diasMora")
	public Integer getDiasMora() {
		return diasMora;
	}
	
	public void setDiasMora(Integer diasMora) {
		this.diasMora = diasMora;
	}
	
	@Column(name = "monto")
	public Long getMonto() {
		return monto;
	}
	
	public void setMonto(Long monto) {
		this.monto = monto;
	}
	
	@Column(name = "texto", length = 800)
	@Length(max = 800)
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
	@Column(name = "fechaCont", length = 50)
	public Date getFechaCont() {
		return fechaCont;
	}
	
	public void setFechaCont(Date fechaCont) {
		this.fechaCont = fechaCont;
	}

	@Column(name = "indice_cme", length = 15)
	public String getIndCME() {
		return indCME;
	}

	public void setIndCME(String indCME) {
		this.indCME = indCME;
	}
	
	@Column(name = "referencia", length = 50)
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_ingreso", length = 10)
	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	@Column(name = "rut", length = 10, nullable = true)
	@Length(max = 10)
	public String getRut() {
		return rut;
	}
	
	public void setRut(String rut) {
		this.rut = rut;
	}	
	
	
	
}
