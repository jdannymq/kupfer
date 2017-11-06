package org.domain.sck.entity;

// Generated 29-ene-2013 13:38:00 by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.Length;

/**
 * LcredSolicitudCondiciones generated by hbm2java
 */
@Entity
@Table(name = "lcred_solicitud_condiciones")
public class LcredSolicitudCondiciones implements Serializable {

	private long numSolicitud;
	private String codTipoSolicitud;
	private String statusSolicitud;
	private Character condPagoInicial;
	private String codCondPagoInicial;
	private BigDecimal montoCondPagoInicial;
	private Character condRiesgoInicial;
	private String codCondRiesgoInicial;
	private BigDecimal montoCondRiesgoInicial;
	private Character condPagoFinal;
	private String codCondPagoFinal;
	private BigDecimal montoCondPagoFinal;
	private Character condRiesgoFinal;
	private String codCondRiesgoFinal;
	private BigDecimal montoCondRiesgoFinal;
	private String observacionesInicial;
	private String observacionesFinal;
	private String usuarioAnalisis;
	private Date fechaAnalisis;
	private Date horaAnalisis;
	private String motivoRechazo;
	private String observacionesRechazo;
	private String motivoCambio;

	public LcredSolicitudCondiciones() {
	}

	public LcredSolicitudCondiciones(long numSolicitud) {
		this.numSolicitud = numSolicitud;
	}

	public LcredSolicitudCondiciones(long numSolicitud,
			String codTipoSolicitud, String statusSolicitud,
			Character condPagoInicial, String codCondPagoInicial,
			BigDecimal montoCondPagoInicial, Character condRiesgoInicial,
			String codCondRiesgoInicial, BigDecimal montoCondRiesgoInicial,
			Character condPagoFinal, String codCondPagoFinal,
			BigDecimal montoCondPagoFinal, Character condRiesgoFinal,
			String codCondRiesgoFinal, BigDecimal montoCondRiesgoFinal,
			String observacionesInicial, String observacionesFinal,
			String usuarioAnalisis, Date fechaAnalisis, Date horaAnalisis,
			String motivoRechazo, String observacionesRechazo,
			String motivoCambio) {
		this.numSolicitud = numSolicitud;
		this.codTipoSolicitud = codTipoSolicitud;
		this.statusSolicitud = statusSolicitud;
		this.condPagoInicial = condPagoInicial;
		this.codCondPagoInicial = codCondPagoInicial;
		this.montoCondPagoInicial = montoCondPagoInicial;
		this.condRiesgoInicial = condRiesgoInicial;
		this.codCondRiesgoInicial = codCondRiesgoInicial;
		this.montoCondRiesgoInicial = montoCondRiesgoInicial;
		this.condPagoFinal = condPagoFinal;
		this.codCondPagoFinal = codCondPagoFinal;
		this.montoCondPagoFinal = montoCondPagoFinal;
		this.condRiesgoFinal = condRiesgoFinal;
		this.codCondRiesgoFinal = codCondRiesgoFinal;
		this.montoCondRiesgoFinal = montoCondRiesgoFinal;
		this.observacionesInicial = observacionesInicial;
		this.observacionesFinal = observacionesFinal;
		this.usuarioAnalisis = usuarioAnalisis;
		this.fechaAnalisis = fechaAnalisis;
		this.horaAnalisis = horaAnalisis;
		this.motivoRechazo = motivoRechazo;
		this.observacionesRechazo = observacionesRechazo;
		this.motivoCambio = motivoCambio;
	}

	@Id
	@Column(name = "num_solicitud", unique = true, nullable = false, precision = 18, scale = 0)
	public long getNumSolicitud() {
		return this.numSolicitud;
	}

	public void setNumSolicitud(long numSolicitud) {
		this.numSolicitud = numSolicitud;
	}

	@Column(name = "cod_tipo_solicitud", length = 3)
	@Length(max = 3)
	public String getCodTipoSolicitud() {
		return this.codTipoSolicitud;
	}

	public void setCodTipoSolicitud(String codTipoSolicitud) {
		this.codTipoSolicitud = codTipoSolicitud;
	}

	@Column(name = "status_solicitud", length = 2)
	@Length(max = 2)
	public String getStatusSolicitud() {
		return this.statusSolicitud;
	}

	public void setStatusSolicitud(String statusSolicitud) {
		this.statusSolicitud = statusSolicitud;
	}

	@Column(name = "cond_pago_inicial", length = 1)
	public Character getCondPagoInicial() {
		return this.condPagoInicial;
	}

	public void setCondPagoInicial(Character condPagoInicial) {
		this.condPagoInicial = condPagoInicial;
	}

	@Column(name = "cod_cond_pago_inicial", length = 4)
	@Length(max = 4)
	public String getCodCondPagoInicial() {
		return this.codCondPagoInicial;
	}

	public void setCodCondPagoInicial(String codCondPagoInicial) {
		this.codCondPagoInicial = codCondPagoInicial;
	}

	@Column(name = "monto_cond_pago_inicial", scale = 4)
	public BigDecimal getMontoCondPagoInicial() {
		return this.montoCondPagoInicial;
	}

	public void setMontoCondPagoInicial(BigDecimal montoCondPagoInicial) {
		this.montoCondPagoInicial = montoCondPagoInicial;
	}

	@Column(name = "cond_riesgo_inicial", length = 1)
	public Character getCondRiesgoInicial() {
		return this.condRiesgoInicial;
	}

	public void setCondRiesgoInicial(Character condRiesgoInicial) {
		this.condRiesgoInicial = condRiesgoInicial;
	}

	@Column(name = "cod_cond_riesgo_inicial", length = 5)
	@Length(max = 5)
	public String getCodCondRiesgoInicial() {
		return this.codCondRiesgoInicial;
	}

	public void setCodCondRiesgoInicial(String codCondRiesgoInicial) {
		this.codCondRiesgoInicial = codCondRiesgoInicial;
	}

	@Column(name = "monto_cond_riesgo_inicial", scale = 4)
	public BigDecimal getMontoCondRiesgoInicial() {
		return this.montoCondRiesgoInicial;
	}

	public void setMontoCondRiesgoInicial(BigDecimal montoCondRiesgoInicial) {
		this.montoCondRiesgoInicial = montoCondRiesgoInicial;
	}

	@Column(name = "cond_pago_final", length = 1)
	public Character getCondPagoFinal() {
		return this.condPagoFinal;
	}

	public void setCondPagoFinal(Character condPagoFinal) {
		this.condPagoFinal = condPagoFinal;
	}

	@Column(name = "cod_cond_pago_final", length = 4)
	@Length(max = 4)
	public String getCodCondPagoFinal() {
		return this.codCondPagoFinal;
	}

	public void setCodCondPagoFinal(String codCondPagoFinal) {
		this.codCondPagoFinal = codCondPagoFinal;
	}

	@Column(name = "monto_cond_pago_final", scale = 4)
	public BigDecimal getMontoCondPagoFinal() {
		return this.montoCondPagoFinal;
	}

	public void setMontoCondPagoFinal(BigDecimal montoCondPagoFinal) {
		this.montoCondPagoFinal = montoCondPagoFinal;
	}

	@Column(name = "cond_riesgo_final", length = 1)
	public Character getCondRiesgoFinal() {
		return this.condRiesgoFinal;
	}

	public void setCondRiesgoFinal(Character condRiesgoFinal) {
		this.condRiesgoFinal = condRiesgoFinal;
	}

	@Column(name = "cod_cond_riesgo_final", length = 5)
	@Length(max = 5)
	public String getCodCondRiesgoFinal() {
		return this.codCondRiesgoFinal;
	}

	public void setCodCondRiesgoFinal(String codCondRiesgoFinal) {
		this.codCondRiesgoFinal = codCondRiesgoFinal;
	}

	@Column(name = "monto_cond_riesgo_final", scale = 4)
	public BigDecimal getMontoCondRiesgoFinal() {
		return this.montoCondRiesgoFinal;
	}

	public void setMontoCondRiesgoFinal(BigDecimal montoCondRiesgoFinal) {
		this.montoCondRiesgoFinal = montoCondRiesgoFinal;
	}

	@Column(name = "observaciones_inicial")
	public String getObservacionesInicial() {
		return this.observacionesInicial;
	}

	public void setObservacionesInicial(String observacionesInicial) {
		this.observacionesInicial = observacionesInicial;
	}

	@Column(name = "observaciones_final")
	public String getObservacionesFinal() {
		return this.observacionesFinal;
	}

	public void setObservacionesFinal(String observacionesFinal) {
		this.observacionesFinal = observacionesFinal;
	}

	@Column(name = "usuario_analisis", length = 15)
	@Length(max = 15)
	public String getUsuarioAnalisis() {
		return this.usuarioAnalisis;
	}

	public void setUsuarioAnalisis(String usuarioAnalisis) {
		this.usuarioAnalisis = usuarioAnalisis;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_analisis", length = 23)
	public Date getFechaAnalisis() {
		return this.fechaAnalisis;
	}

	public void setFechaAnalisis(Date fechaAnalisis) {
		this.fechaAnalisis = fechaAnalisis;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hora_analisis", length = 23)
	public Date getHoraAnalisis() {
		return this.horaAnalisis;
	}

	public void setHoraAnalisis(Date horaAnalisis) {
		this.horaAnalisis = horaAnalisis;
	}

	@Column(name = "motivo_rechazo", length = 3)
	@Length(max = 3)
	public String getMotivoRechazo() {
		return this.motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	@Column(name = "observaciones_rechazo")
	public String getObservacionesRechazo() {
		return this.observacionesRechazo;
	}

	public void setObservacionesRechazo(String observacionesRechazo) {
		this.observacionesRechazo = observacionesRechazo;
	}

	@Column(name = "motivo_cambio")
	public String getMotivoCambio() {
		return this.motivoCambio;
	}

	public void setMotivoCambio(String motivoCambio) {
		this.motivoCambio = motivoCambio;
	}

}
