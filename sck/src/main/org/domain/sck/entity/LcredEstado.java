package org.domain.sck.entity;

// Generated 04-sep-2012 11:19:31 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * LcredEstado generated by hbm2java
 */
@Entity
@Table(name = "lcred_estado")
public class LcredEstado implements java.io.Serializable {

	private String codEstado;
	private String desEstado;
	private String cierraProceso;
	private Long grupoBarraAnalista;
	private Long grupoBarraEjecutivo;

	public LcredEstado() {
	}

	public LcredEstado(String codEstado) {
		this.codEstado = codEstado;
	}

	public LcredEstado(String codEstado, String desEstado,
			String cierraProceso, Long grupoBarraAnalista,
			Long grupoBarraEjecutivo) {
		this.codEstado = codEstado;
		this.desEstado = desEstado;
		this.cierraProceso = cierraProceso;
		this.grupoBarraAnalista = grupoBarraAnalista;
		this.grupoBarraEjecutivo = grupoBarraEjecutivo;
	}

	@Id
	@Column(name = "cod_estado", unique = true, nullable = false, length = 2)
	@NotNull
	@Length(max = 2)
	public String getCodEstado() {
		return this.codEstado;
	}

	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}

	@Column(name = "des_estado", length = 30)
	@Length(max = 30)
	public String getDesEstado() {
		return this.desEstado;
	}

	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}

	@Column(name = "cierra_proceso", length = 2)
	@Length(max = 2)
	public String getCierraProceso() {
		return this.cierraProceso;
	}

	public void setCierraProceso(String cierraProceso) {
		this.cierraProceso = cierraProceso;
	}

	@Column(name = "grupo_barra_analista", precision = 18, scale = 0)
	public Long getGrupoBarraAnalista() {
		return this.grupoBarraAnalista;
	}

	public void setGrupoBarraAnalista(Long grupoBarraAnalista) {
		this.grupoBarraAnalista = grupoBarraAnalista;
	}

	@Column(name = "grupo_barra_ejecutivo", precision = 18, scale = 0)
	public Long getGrupoBarraEjecutivo() {
		return this.grupoBarraEjecutivo;
	}

	public void setGrupoBarraEjecutivo(Long grupoBarraEjecutivo) {
		this.grupoBarraEjecutivo = grupoBarraEjecutivo;
	}

}
