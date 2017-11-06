package org.domain.sck.session.home;

import java.util.List;

import org.domain.sck.dto.ConceptoDTO;
import org.domain.sck.dto.CreacionDmDTO;
import org.domain.sck.dto.ProrrogaDTO;
import org.domain.sck.dto.UsuarioCorreoDTO;
import org.domain.sck.entity.LcredSolicitud;
import org.domain.sck.entity.Usuariosegur;

public class ContenidoEmailSolicitudDTO {

	private String destinatario;
	private String nombreUsuario;
	private String correoEmisor;
	private String tituloRespuesta;
	private Integer opcionFinal;
	
	/*datos globales*/
	private String rutNombre;
    private String canalVenta;
    private String sucursal;
    private String emisor;
    private String tipoSolicitud;
    private String soloNombre;
    private String observacionEjecutivo;
    
    private String usuarioAprobador;
    private String accionRespuesta;
    private String ObservacionRespuesta;
    private String color;
    private String accion;
    private String desicionEjecutivo;
    private String respuestaFinalSolicitud;
    
    
    /*datos de Venta*/
    private String tipoVentas;
    private String montoMasIva;
    private String montoPie;
    private String montoMasIvaAprobado;
    private String montoPieAprobado;
    private String margenNegocio;
    private String formaPago;
    private String motivoCompra;
    private String peakCredito;
    private String lineaSeguro;
    private String coberturaSeguro;
    private String risgoKupfer;

    /*datos de lineas credito */
    private String tipoLineaCredito;
    private String lineaSeguroActual;
    private String montoSolicitado;
    private String condicionRiesgo;
    private String condicionPago;
    private String proyecto;
    private String montoProyecto;
    private String plazoEjecucion;
    private String potencialCompra;
    
    /*datos  de riesgo y pago*/
    private String condicionRiesgoNuevo;
    private String condicionPagoNuevo;
    private String motivoCambio;
    
    /*datos de bloqueo y desboqueo*/
    private String tipoBloqueoDesbloqueo;
    private String motivoBloqueoDesbloqueo;
    private String observacionesBloqueoDesblorqueo;
    
    /*datos de prorroga*/
    private String tipoProrroga;
    private String motivoProrroga;

    /*datos de creacion de DM*/
    private String tipoDM;
    
    
    
    private List<Usuariosegur> listaNuevosCorreos;
    
    

    
    
    
    
    private List<ConceptoDTO> listaProductos;
    private List<ConceptoDTO> listaConceptoMontos;
    private List<ConceptoDTO> listaArchivos;
    private List<String> listaSocios;
    private List<ProrrogaDTO> listaProrrogas;
    private List<CreacionDmDTO> listaCreacionDM;
    private List<ConceptoDTO> listaArchivosRespuesta;  
    private List<ConceptoDTO> listaMotivosRechazos;
    private List<ConceptoDTO> listaObservaciones;
    

    


	private String htmlArchivos;

	private LcredSolicitud solicitud;


	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public LcredSolicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(LcredSolicitud solicitud) {
		this.solicitud = solicitud;
	}

	public String getHtmlArchivos() {
		return htmlArchivos;
	}

	public void setHtmlArchivos(String htmlArchivos) {
		this.htmlArchivos = htmlArchivos;
	}

	public String getRutNombre() {
		return rutNombre;
	}

	public void setRutNombre(String rutNombre) {
		this.rutNombre = rutNombre;
	}

	public String getCanalVenta() {
		return canalVenta;
	}

	public void setCanalVenta(String canalVenta) {
		this.canalVenta = canalVenta;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	public String getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public String getTipoVentas() {
		return tipoVentas;
	}

	public void setTipoVentas(String tipoVentas) {
		this.tipoVentas = tipoVentas;
	}

	public String getMontoMasIva() {
		return montoMasIva;
	}

	public void setMontoMasIva(String montoMasIva) {
		this.montoMasIva = montoMasIva;
	}

	public String getMontoPie() {
		return montoPie;
	}

	public void setMontoPie(String montoPie) {
		this.montoPie = montoPie;
	}

	public String getMargenNegocio() {
		return margenNegocio;
	}

	public void setMargenNegocio(String margenNegocio) {
		this.margenNegocio = margenNegocio;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getMotivoCompra() {
		return motivoCompra;
	}

	public void setMotivoCompra(String motivoCompra) {
		this.motivoCompra = motivoCompra;
	}

	public String getPeakCredito() {
		return peakCredito;
	}

	public void setPeakCredito(String peakCredito) {
		this.peakCredito = peakCredito;
	}

	public String getLineaSeguro() {
		return lineaSeguro;
	}

	public void setLineaSeguro(String lineaSeguro) {
		this.lineaSeguro = lineaSeguro;
	}

	public String getRisgoKupfer() {
		return risgoKupfer;
	}

	public void setRisgoKupfer(String risgoKupfer) {
		this.risgoKupfer = risgoKupfer;
	}

	public List<ConceptoDTO> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(List<ConceptoDTO> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public List<ConceptoDTO> getListaConceptoMontos() {
		return listaConceptoMontos;
	}

	public void setListaConceptoMontos(List<ConceptoDTO> listaConceptoMontos) {
		this.listaConceptoMontos = listaConceptoMontos;
	}

	public List<ConceptoDTO> getListaArchivos() {
		return listaArchivos;
	}

	public void setListaArchivos(List<ConceptoDTO> listaArchivos) {
		this.listaArchivos = listaArchivos;
	}

	public String getTipoLineaCredito() {
		return tipoLineaCredito;
	}

	public void setTipoLineaCredito(String tipoLineaCredito) {
		this.tipoLineaCredito = tipoLineaCredito;
	}

	public String getLineaSeguroActual() {
		return lineaSeguroActual;
	}

	public void setLineaSeguroActual(String lineaSeguroActual) {
		this.lineaSeguroActual = lineaSeguroActual;
	}

	public String getMontoSolicitado() {
		return montoSolicitado;
	}

	public void setMontoSolicitado(String montoSolicitado) {
		this.montoSolicitado = montoSolicitado;
	}

	public String getCondicionRiesgo() {
		return condicionRiesgo;
	}

	public void setCondicionRiesgo(String condicionRiesgo) {
		this.condicionRiesgo = condicionRiesgo;
	}

	public String getCondicionPago() {
		return condicionPago;
	}

	public void setCondicionPago(String condicionPago) {
		this.condicionPago = condicionPago;
	}

	public String getProyecto() {
		return proyecto;
	}

	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}

	public String getMontoProyecto() {
		return montoProyecto;
	}

	public void setMontoProyecto(String montoProyecto) {
		this.montoProyecto = montoProyecto;
	}

	public String getPlazoEjecucion() {
		return plazoEjecucion;
	}

	public void setPlazoEjecucion(String plazoEjecucion) {
		this.plazoEjecucion = plazoEjecucion;
	}

	public String getPotencialCompra() {
		return potencialCompra;
	}

	public void setPotencialCompra(String potencialCompra) {
		this.potencialCompra = potencialCompra;
	}

	public List<String> getListaSocios() {
		return listaSocios;
	}

	public void setListaSocios(List<String> listaSocios) {
		this.listaSocios = listaSocios;
	}

	public String getCondicionRiesgoNuevo() {
		return condicionRiesgoNuevo;
	}

	public void setCondicionRiesgoNuevo(String condicionRiesgoNuevo) {
		this.condicionRiesgoNuevo = condicionRiesgoNuevo;
	}

	public String getCondicionPagoNuevo() {
		return condicionPagoNuevo;
	}

	public void setCondicionPagoNuevo(String condicionPagoNuevo) {
		this.condicionPagoNuevo = condicionPagoNuevo;
	}

	public String getMotivoCambio() {
		return motivoCambio;
	}

	public void setMotivoCambio(String motivoCambio) {
		this.motivoCambio = motivoCambio;
	}

	public String getMotivoBloqueoDesbloqueo() {
		return motivoBloqueoDesbloqueo;
	}

	public void setMotivoBloqueoDesbloqueo(String motivoBloqueoDesbloqueo) {
		this.motivoBloqueoDesbloqueo = motivoBloqueoDesbloqueo;
	}

	public String getObservacionesBloqueoDesblorqueo() {
		return observacionesBloqueoDesblorqueo;
	}

	public void setObservacionesBloqueoDesblorqueo(
			String observacionesBloqueoDesblorqueo) {
		this.observacionesBloqueoDesblorqueo = observacionesBloqueoDesblorqueo;
	}

	public String getTipoBloqueoDesbloqueo() {
		return tipoBloqueoDesbloqueo;
	}

	public void setTipoBloqueoDesbloqueo(String tipoBloqueoDesbloqueo) {
		this.tipoBloqueoDesbloqueo = tipoBloqueoDesbloqueo;
	}

	public String getTipoProrroga() {
		return tipoProrroga;
	}

	public void setTipoProrroga(String tipoProrroga) {
		this.tipoProrroga = tipoProrroga;
	}

	public List<ProrrogaDTO> getListaProrrogas() {
		return listaProrrogas;
	}

	public void setListaProrrogas(List<ProrrogaDTO> listaProrrogas) {
		this.listaProrrogas = listaProrrogas;
	}

	public String getSoloNombre() {
		return soloNombre;
	}

	public void setSoloNombre(String soloNombre) {
		this.soloNombre = soloNombre;
	}

	public List<CreacionDmDTO> getListaCreacionDM() {
		return listaCreacionDM;
	}

	public void setListaCreacionDM(List<CreacionDmDTO> listaCreacionDM) {
		this.listaCreacionDM = listaCreacionDM;
	}

	public String getTipoDM() {
		return tipoDM;
	}

	public void setTipoDM(String tipoDM) {
		this.tipoDM = tipoDM;
	}

	public String getMotivoProrroga() {
		return motivoProrroga;
	}

	public void setMotivoProrroga(String motivoProrroga) {
		this.motivoProrroga = motivoProrroga;
	}

	public String getUsuarioAprobador() {
		return usuarioAprobador;
	}

	public void setUsuarioAprobador(String usuarioAprobador) {
		this.usuarioAprobador = usuarioAprobador;
	}

	public String getAccionRespuesta() {
		return accionRespuesta;
	}

	public void setAccionRespuesta(String accionRespuesta) {
		this.accionRespuesta = accionRespuesta;
	}

	public String getObservacionRespuesta() {
		return ObservacionRespuesta;
	}

	public void setObservacionRespuesta(String observacionRespuesta) {
		ObservacionRespuesta = observacionRespuesta;
	}

	public List<ConceptoDTO> getListaArchivosRespuesta() {
		return listaArchivosRespuesta;
	}

	public void setListaArchivosRespuesta(List<ConceptoDTO> listaArchivosRespuesta) {
		this.listaArchivosRespuesta = listaArchivosRespuesta;
	}

	public String getMontoMasIvaAprobado() {
		return montoMasIvaAprobado;
	}

	public void setMontoMasIvaAprobado(String montoMasIvaAprobado) {
		this.montoMasIvaAprobado = montoMasIvaAprobado;
	}

	public String getMontoPieAprobado() {
		return montoPieAprobado;
	}

	public void setMontoPieAprobado(String montoPieAprobado) {
		this.montoPieAprobado = montoPieAprobado;
	}

	public String getCoberturaSeguro() {
		return coberturaSeguro;
	}

	public void setCoberturaSeguro(String coberturaSeguro) {
		this.coberturaSeguro = coberturaSeguro;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public List<ConceptoDTO> getListaMotivosRechazos() {
		return listaMotivosRechazos;
	}

	public void setListaMotivosRechazos(List<ConceptoDTO> listaMotivosRechazos) {
		this.listaMotivosRechazos = listaMotivosRechazos;
	}

	public String getObservacionEjecutivo() {
		return observacionEjecutivo;
	}

	public void setObservacionEjecutivo(String observacionEjecutivo) {
		this.observacionEjecutivo = observacionEjecutivo;
	}
	
	public List<ConceptoDTO> getListaObservaciones() {
		return listaObservaciones;
	}

	public void setListaObservaciones(List<ConceptoDTO> listaObservaciones) {
		this.listaObservaciones = listaObservaciones;
	}

	public String getCorreoEmisor() {
		return correoEmisor;
	}

	public void setCorreoEmisor(String correoEmisor) {
		this.correoEmisor = correoEmisor;
	}

	public String getTituloRespuesta() {
		return tituloRespuesta;
	}

	public void setTituloRespuesta(String tituloRespuesta) {
		this.tituloRespuesta = tituloRespuesta;
	}

	public List<Usuariosegur> getListaNuevosCorreos() {
		return listaNuevosCorreos;
	}

	public void setListaNuevosCorreos(List<Usuariosegur> listaNuevosCorreos) {
		this.listaNuevosCorreos = listaNuevosCorreos;
	}

	public Integer getOpcionFinal() {
		return opcionFinal;
	}

	public void setOpcionFinal(Integer opcionFinal) {
		this.opcionFinal = opcionFinal;
	}

	public String getDesicionEjecutivo() {
		return desicionEjecutivo;
	}

	public void setDesicionEjecutivo(String desicionEjecutivo) {
		this.desicionEjecutivo = desicionEjecutivo;
	}

	public String getRespuestaFinalSolicitud() {
		return respuestaFinalSolicitud;
	}

	public void setRespuestaFinalSolicitud(String respuestaFinalSolicitud) {
		this.respuestaFinalSolicitud = respuestaFinalSolicitud;
	}


	
	

}