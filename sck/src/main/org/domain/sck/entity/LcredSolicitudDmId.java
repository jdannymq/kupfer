package org.domain.sck.entity;

// Generated 21-02-2013 02:50:14 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.Length;

/**
 * LcredSolicitudDmId generated by hbm2java
 */
@Embeddable
public class LcredSolicitudDmId implements java.io.Serializable {

	private long numSolicitud;
	private String rut;
	private String nombre;
	private String oficinaVentas;
	private String zona;
	private String listaPrecio;
	private String sector;
	private String vendTelefono;
	private String vendTerreno;
	private String cobrador;
	private String condExpedicion;
	private String condPago;

	public LcredSolicitudDmId() {
	}

	public LcredSolicitudDmId(long numSolicitud) {
		this.numSolicitud = numSolicitud;
	}

	public LcredSolicitudDmId(long numSolicitud, String rut, String nombre,
			String oficinaVentas, String zona, String listaPrecio,
			String sector, String vendTelefono, String vendTerreno,
			String cobrador, String condExpedicion, String condPago) {
		this.numSolicitud = numSolicitud;
		this.rut = rut;
		this.nombre = nombre;
		this.oficinaVentas = oficinaVentas;
		this.zona = zona;
		this.listaPrecio = listaPrecio;
		this.sector = sector;
		this.vendTelefono = vendTelefono;
		this.vendTerreno = vendTerreno;
		this.cobrador = cobrador;
		this.condExpedicion = condExpedicion;
		this.condPago = condPago;
	}

	@Column(name = "num_solicitud", nullable = false, precision = 18, scale = 0)
	public long getNumSolicitud() {
		return this.numSolicitud;
	}

	public void setNumSolicitud(long numSolicitud) {
		this.numSolicitud = numSolicitud;
	}

	@Column(name = "rut", length = 12)
	@Length(max = 12)
	public String getRut() {
		return this.rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	@Column(name = "nombre", length = 120)
	@Length(max = 120)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "oficina_ventas", length = 10)
	@Length(max = 10)
	public String getOficinaVentas() {
		return this.oficinaVentas;
	}

	public void setOficinaVentas(String oficinaVentas) {
		this.oficinaVentas = oficinaVentas;
	}

	@Column(name = "zona", length = 10)
	@Length(max = 10)
	public String getZona() {
		return this.zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	@Column(name = "lista_precio", length = 10)
	@Length(max = 10)
	public String getListaPrecio() {
		return this.listaPrecio;
	}

	public void setListaPrecio(String listaPrecio) {
		this.listaPrecio = listaPrecio;
	}

	@Column(name = "sector", length = 30)
	@Length(max = 30)
	public String getSector() {
		return this.sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	@Column(name = "vend_telefono", length = 12)
	@Length(max = 12)
	public String getVendTelefono() {
		return this.vendTelefono;
	}

	public void setVendTelefono(String vendTelefono) {
		this.vendTelefono = vendTelefono;
	}

	@Column(name = "vend_terreno", length = 12)
	@Length(max = 12)
	public String getVendTerreno() {
		return this.vendTerreno;
	}

	public void setVendTerreno(String vendTerreno) {
		this.vendTerreno = vendTerreno;
	}

	@Column(name = "cobrador", length = 12)
	@Length(max = 12)
	public String getCobrador() {
		return this.cobrador;
	}

	public void setCobrador(String cobrador) {
		this.cobrador = cobrador;
	}

	@Column(name = "cond_expedicion", length = 5)
	@Length(max = 5)
	public String getCondExpedicion() {
		return this.condExpedicion;
	}

	public void setCondExpedicion(String condExpedicion) {
		this.condExpedicion = condExpedicion;
	}

	@Column(name = "cond_pago", length = 5)
	@Length(max = 5)
	public String getCondPago() {
		return this.condPago;
	}

	public void setCondPago(String condPago) {
		this.condPago = condPago;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LcredSolicitudDmId))
			return false;
		LcredSolicitudDmId castOther = (LcredSolicitudDmId) other;

		return (this.getNumSolicitud() == castOther.getNumSolicitud())
				&& ((this.getRut() == castOther.getRut()) || (this.getRut() != null
						&& castOther.getRut() != null && this.getRut().equals(
						castOther.getRut())))
				&& ((this.getNombre() == castOther.getNombre()) || (this
						.getNombre() != null && castOther.getNombre() != null && this
						.getNombre().equals(castOther.getNombre())))
				&& ((this.getOficinaVentas() == castOther.getOficinaVentas()) || (this
						.getOficinaVentas() != null
						&& castOther.getOficinaVentas() != null && this
						.getOficinaVentas()
						.equals(castOther.getOficinaVentas())))
				&& ((this.getZona() == castOther.getZona()) || (this.getZona() != null
						&& castOther.getZona() != null && this.getZona()
						.equals(castOther.getZona())))
				&& ((this.getListaPrecio() == castOther.getListaPrecio()) || (this
						.getListaPrecio() != null
						&& castOther.getListaPrecio() != null && this
						.getListaPrecio().equals(castOther.getListaPrecio())))
				&& ((this.getSector() == castOther.getSector()) || (this
						.getSector() != null && castOther.getSector() != null && this
						.getSector().equals(castOther.getSector())))
				&& ((this.getVendTelefono() == castOther.getVendTelefono()) || (this
						.getVendTelefono() != null
						&& castOther.getVendTelefono() != null && this
						.getVendTelefono().equals(castOther.getVendTelefono())))
				&& ((this.getVendTerreno() == castOther.getVendTerreno()) || (this
						.getVendTerreno() != null
						&& castOther.getVendTerreno() != null && this
						.getVendTerreno().equals(castOther.getVendTerreno())))
				&& ((this.getCobrador() == castOther.getCobrador()) || (this
						.getCobrador() != null
						&& castOther.getCobrador() != null && this
						.getCobrador().equals(castOther.getCobrador())))
				&& ((this.getCondExpedicion() == castOther.getCondExpedicion()) || (this
						.getCondExpedicion() != null
						&& castOther.getCondExpedicion() != null && this
						.getCondExpedicion().equals(
								castOther.getCondExpedicion())))
				&& ((this.getCondPago() == castOther.getCondPago()) || (this
						.getCondPago() != null
						&& castOther.getCondPago() != null && this
						.getCondPago().equals(castOther.getCondPago())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getNumSolicitud();
		result = 37 * result
				+ (getRut() == null ? 0 : this.getRut().hashCode());
		result = 37 * result
				+ (getNombre() == null ? 0 : this.getNombre().hashCode());
		result = 37
				* result
				+ (getOficinaVentas() == null ? 0 : this.getOficinaVentas()
						.hashCode());
		result = 37 * result
				+ (getZona() == null ? 0 : this.getZona().hashCode());
		result = 37
				* result
				+ (getListaPrecio() == null ? 0 : this.getListaPrecio()
						.hashCode());
		result = 37 * result
				+ (getSector() == null ? 0 : this.getSector().hashCode());
		result = 37
				* result
				+ (getVendTelefono() == null ? 0 : this.getVendTelefono()
						.hashCode());
		result = 37
				* result
				+ (getVendTerreno() == null ? 0 : this.getVendTerreno()
						.hashCode());
		result = 37 * result
				+ (getCobrador() == null ? 0 : this.getCobrador().hashCode());
		result = 37
				* result
				+ (getCondExpedicion() == null ? 0 : this.getCondExpedicion()
						.hashCode());
		result = 37 * result
				+ (getCondPago() == null ? 0 : this.getCondPago().hashCode());
		return result;
	}

}
