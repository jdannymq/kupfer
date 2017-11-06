package org.domain.sck.entity;

// Generated 08-ene-2013 11:52:16 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.NotNull;

/**
 * LcredSolicitudId generated by hbm2java
 */
@Embeddable
public class LcredSolicitudId implements java.io.Serializable {

	private Long numSolicitud;
	private Date fecSolicitud;

	public LcredSolicitudId() {
	}

	public LcredSolicitudId(Long numSolicitud, Date fecSolicitud) {
		this.numSolicitud = numSolicitud;
		this.fecSolicitud = fecSolicitud;
	}

	@Column(name = "num_solicitud", nullable = false, precision = 18, scale = 0)
	public Long getNumSolicitud() {
		return this.numSolicitud;
	}

	public void setNumSolicitud(Long numSolicitud) {
		this.numSolicitud = numSolicitud;
	}

	@Column(name = "fec_solicitud", nullable = false, length = 23)
	@NotNull
	public Date getFecSolicitud() {
		return this.fecSolicitud;
	}

	public void setFecSolicitud(Date fecSolicitud) {
		this.fecSolicitud = fecSolicitud;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LcredSolicitudId))
			return false;
		LcredSolicitudId castOther = (LcredSolicitudId) other;

		return (this.getNumSolicitud() == castOther.getNumSolicitud())
				&& ((this.getFecSolicitud() == castOther.getFecSolicitud()) || (this
						.getFecSolicitud() != null
						&& castOther.getFecSolicitud() != null && this
						.getFecSolicitud().equals(castOther.getFecSolicitud())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getNumSolicitud().intValue();
		result = 37
				* result
				+ (getFecSolicitud() == null ? 0 : this.getFecSolicitud()
						.hashCode());
		return result;
	}

}