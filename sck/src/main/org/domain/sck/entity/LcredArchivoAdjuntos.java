package org.domain.sck.entity;

// Generated 07-05-2013 05:44:56 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.validator.NotNull;

/**
 * LcredArchivoAdjuntos generated by hbm2java
 */
@Entity
@Table(name = "lcred_archivo_adjuntos")
public class LcredArchivoAdjuntos implements java.io.Serializable {

	private LcredArchivoAdjuntosId id;

	public LcredArchivoAdjuntos() {
	}

	public LcredArchivoAdjuntos(LcredArchivoAdjuntosId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "numSolicitud", column = @Column(name = "num_solicitud", nullable = false, precision = 18, scale = 0)),
			@AttributeOverride(name = "codTipoSolicitud", column = @Column(name = "cod_tipo_solicitud", nullable = false, length = 3)),
			@AttributeOverride(name = "archivoAdjunto", column = @Column(name = "archivo_adjunto", nullable = false, length = 300)) })
	@NotNull
	public LcredArchivoAdjuntosId getId() {
		return this.id;
	}

	public void setId(LcredArchivoAdjuntosId id) {
		this.id = id;
	}

}