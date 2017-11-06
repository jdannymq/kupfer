package org.domain.sck.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.domain.sck.entity.enums.TipoCuentasKupferType;

//DTO que retiene los datos del cliente provenientes desde sap
public class ClienteDTO {
	private String rut;
	private String razonSocial; 
	private String giro; 
	private String direccion; 
	private String fono; 
	private String fax; 
	private String comuna;
	private String ciudad;
	private String sucursalCliente;
	private String canalVenta;
	private String tipoCliente;
	private List<String> dmVentas;
	private BigDecimal lineaCreditoKH;
	private BigDecimal lineaCreditoKHUtilizado;
	private BigDecimal lineaCreditoKHDisponible;
	private BigDecimal lineaCreditoFinanciada;
	private String estadoLineaCreditoKH;
	private String numeroRV;
	private BigDecimal lineaEnCuotas;
	private BigDecimal lineaEnCuotasUtil;
	private BigDecimal lineaEnCuotasDisp;
	private String estadoLineaEnCuotas;
	private String codigoCondicionPago;
	private String condicionPago; //conP en rfc
	private String codigoclasificacionRiesgo;
	private String clasificacionRiesgo; 
	private String seguro; 
	private String vigenciaSeguro;
	private BigDecimal montoSeguro;
	private Long montoSeguroUf;
	private BigDecimal ventasProm12Meses;
	private Long factProm12Meses;
	private Long CantMesesVentas;
	private BigDecimal ventasTotal12Meses;
	private Date creacionSap;
	private String estadoSolicitud;
	private String dmVentasUno;
	private long prmFacturas;
	private long mesesVentas;
	private long pedidoEnProceso;
	private TipoCuentasKupferType codigoCanal;
	
	
	
	



	public TipoCuentasKupferType getCodigoCanal() {
		return codigoCanal;
	}



	public void setCodigoCanal(TipoCuentasKupferType codigoCanal) {
		this.codigoCanal = codigoCanal;
	}



	public long getMesesVentas() {
		return mesesVentas;
	}



	public void setMesesVentas(long mesesVentas) {
		this.mesesVentas = mesesVentas;
	}



	public long getPrmFacturas() {
		return prmFacturas;
	}



	public void setPrmFacturas(long prmFacturas) {
		this.prmFacturas = prmFacturas;
	}



	public ClienteDTO() {
		
	}
	
	

	public String getRut() {
		return rut;
	}



	public void setRut(String rut) {
		this.rut = rut;
	}



	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getGiro() {
		return giro;
	}

	public void setGiro(String giro) {
		this.giro = giro;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	
	public String getFono() {
		return fono;
	}



	public void setFono(String fono) {
		this.fono = fono;
	}

	

	public String getFax() {
		return fax;
	}



	public void setFax(String fax) {
		this.fax = fax;
	}



	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getSucursalCliente() {
		return sucursalCliente;
	}

	public void setSucursalCliente(String sucursalCliente) {
		this.sucursalCliente = sucursalCliente;
	}

	public String getCanalVenta() {
		return canalVenta;
	}

	public void setCanalVenta(String canalVenta) {
		this.canalVenta = canalVenta;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public List<String> getDmVentas() {
		return dmVentas;
	}

	public void setDmVentas(List<String> dmVentas) {
		this.dmVentas = dmVentas;
	}

	public BigDecimal getLineaCreditoKH() {
		return lineaCreditoKH;
	}

	public void setLineaCreditoKH(BigDecimal lineaCreditoKH) {
		this.lineaCreditoKH = lineaCreditoKH;
	}

	public BigDecimal getLineaCreditoKHUtilizado() {
		return lineaCreditoKHUtilizado;
	}

	public void setLineaCreditoKHUtilizado(BigDecimal lineaCreditoKHUtilizado) {
		this.lineaCreditoKHUtilizado = lineaCreditoKHUtilizado;
	}

	public BigDecimal getLineaCreditoKHDisponible() {
		return lineaCreditoKHDisponible;
	}

	public void setLineaCreditoKHDisponible(BigDecimal lineaCreditoKHDisponible) {
		this.lineaCreditoKHDisponible = lineaCreditoKHDisponible;
	}

	public String getEstadoLineaCreditoKH() {
		return estadoLineaCreditoKH;
	}

	public void setEstadoLineaCreditoKH(String estadoLineaCreditoKH) {
		this.estadoLineaCreditoKH = estadoLineaCreditoKH;
	}

	public String getNumeroRV() {
		return numeroRV;
	}

	public void setNumeroRV(String numeroRV) {
		this.numeroRV = numeroRV;
	}

	public BigDecimal getLineaEnCuotas() {
		return lineaEnCuotas;
	}

	public void setLineaEnCuotas(BigDecimal lineaEnCuotas) {
		this.lineaEnCuotas = lineaEnCuotas;
	}

	public BigDecimal getLineaEnCuotasUtil() {
		return lineaEnCuotasUtil;
	}

	public void setLineaEnCuotasUtil(BigDecimal lineaEnCuotasUtil) {
		this.lineaEnCuotasUtil = lineaEnCuotasUtil;
	}

	public BigDecimal getLineaEnCuotasDisp() {
		return lineaEnCuotasDisp;
	}

	public void setLineaEnCuotasDisp(BigDecimal lineaEnCuotasDisp) {
		this.lineaEnCuotasDisp = lineaEnCuotasDisp;
	}

	

	public String getEstadoLineaEnCuotas() {
		return estadoLineaEnCuotas;
	}



	public void setEstadoLineaEnCuotas(String estadoLineaEnCuotas) {
		this.estadoLineaEnCuotas = estadoLineaEnCuotas;
	}



	public String getCondicionPago() {
		return condicionPago;
	}

	public void setCondicionPago(String condicionPago) {
		this.condicionPago = condicionPago;
	}

	public String getClasificacionRiesgo() {
		return clasificacionRiesgo;
	}

	public void setClasificacionRiesgo(String clasificacionRiesgo) {
		this.clasificacionRiesgo = clasificacionRiesgo;
	}

	public String getSeguro() {
		return seguro;
	}

	public void setSeguro(String seguro) {
		this.seguro = seguro;
	}

	public String getVigenciaSeguro() {
		return vigenciaSeguro;
	}

	public void setVigenciaSeguro(String vigenciaSeguro) {
		this.vigenciaSeguro = vigenciaSeguro;
	}

	public BigDecimal getMontoSeguro() {
		return montoSeguro;
	}

	public void setMontoSeguro(BigDecimal montoSeguro) {
		this.montoSeguro = montoSeguro;
	}

	public Long getMontoSeguroUf() {
		return montoSeguroUf;
	}

	public void setMontoSeguroUf(Long montoSeguroUf) {
		this.montoSeguroUf = montoSeguroUf;
	}

	public BigDecimal getVentasProm12Meses() {
		return ventasProm12Meses;
	}

	public void setVentasProm12Meses(BigDecimal ventasProm12Meses) {
		this.ventasProm12Meses = ventasProm12Meses;
	}

	public Long getFactProm12Meses() {
		return factProm12Meses;
	}

	public void setFactProm12Meses(Long factProm12Meses) {
		this.factProm12Meses = factProm12Meses;
	}

	public Long getCantMesesVentas() {
		return CantMesesVentas;
	}

	public void setCantMesesVentas(Long cantMesesVentas) {
		CantMesesVentas = cantMesesVentas;
	}

	public BigDecimal getVentasTotal12Meses() {
		return ventasTotal12Meses;
	}

	public void setVentasTotal12Meses(BigDecimal ventasTotal12Meses) {
		this.ventasTotal12Meses = ventasTotal12Meses;
	}

	public Date getCreacionSap() {
		return creacionSap;
	}

	public void setCreacionSap(Date creacionSap) {
		this.creacionSap = creacionSap;
	}

	public String getEstadoSolicitud() {
		return estadoSolicitud;
	}

	public void setEstadoSolicitud(String estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}
	
	@Transient
	public String getCleanRut() {
		return this.rut.replaceAll("[^0-9/(k|K)/]", "").toLowerCase();
	}

	public String getDmVentasUno() {
		return dmVentasUno;
	}

	public void setDmVentasUno(String dmVentasUno) {
		this.dmVentasUno = dmVentasUno;
	}



	public BigDecimal getLineaCreditoFinanciada() {
		return lineaCreditoFinanciada;
	}



	public void setLineaCreditoFinanciada(BigDecimal lineaCreditoFinanciada) {
		this.lineaCreditoFinanciada = lineaCreditoFinanciada;
	}



	public long getPedidoEnProceso() {
		return pedidoEnProceso;
	}



	public void setPedidoEnProceso(long pedidoEnProceso) {
		this.pedidoEnProceso = pedidoEnProceso;
	}



	public String getCodigoCondicionPago() {
		return codigoCondicionPago;
	}



	public void setCodigoCondicionPago(String codigoCondicionPago) {
		this.codigoCondicionPago = codigoCondicionPago;
	}



	public String getCodigoclasificacionRiesgo() {
		return codigoclasificacionRiesgo;
	}



	public void setCodigoclasificacionRiesgo(String codigoclasificacionRiesgo) {
		this.codigoclasificacionRiesgo = codigoclasificacionRiesgo;
	}
	
	
	
	
	
}
