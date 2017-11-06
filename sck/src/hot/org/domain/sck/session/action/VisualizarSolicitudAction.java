package org.domain.sck.session.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;

import modelo.SapSystem;

import org.domain.sck.base.GlobalHitosLogdService;
import org.domain.sck.dto.BotonesDTO;
import org.domain.sck.dto.CPagoDTO;
import org.domain.sck.dto.CabeceraCotPedDTO;
import org.domain.sck.dto.ClienteDTO;
import org.domain.sck.dto.ClsRiesgoDTO;
import org.domain.sck.dto.CompComercialDTO;
import org.domain.sck.dto.ConceptoDTO;
import org.domain.sck.dto.ConceptoMontoDTO;
import org.domain.sck.dto.CotPedDTO;
import org.domain.sck.dto.DestinatarioDTO;
import org.domain.sck.dto.DetalleCotPedDTO;
import org.domain.sck.dto.ExpoRiesgoKhDTO;
import org.domain.sck.dto.FileUploadedDTO;
import org.domain.sck.dto.SolicitudDTO;
import org.domain.sck.dto.UsuarioCorreoDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.entity.ArchivoAdjunto;
import org.domain.sck.entity.CanalUsuarioCargo;
import org.domain.sck.entity.ConceptosNegocio;
import org.domain.sck.entity.CotizacionPedido;
import org.domain.sck.entity.CotizacionPedidoCabecera;
import org.domain.sck.entity.CotizacionPedidoConcepto;
import org.domain.sck.entity.CotizacionPedidoNegocio;
import org.domain.sck.entity.DetalleCp;
import org.domain.sck.entity.DeudaActual;
import org.domain.sck.entity.Estados;
import org.domain.sck.entity.LcredArchivoAdjuntos;
import org.domain.sck.entity.LcredCargos;
import org.domain.sck.entity.LcredEstado;
import org.domain.sck.entity.LcredMotivoRechazo;
import org.domain.sck.entity.LcredSolicitud;
import org.domain.sck.entity.LcredSolicitudBloqueos;
import org.domain.sck.entity.LcredSolicitudCondiciones;
import org.domain.sck.entity.LcredSolicitudDm;
import org.domain.sck.entity.LcredSolicitudLcredito;
import org.domain.sck.entity.LcredSolicitudObservaciones;
import org.domain.sck.entity.LcredSolicitudObservacionesId;
import org.domain.sck.entity.LcredSolicitudOtras;
import org.domain.sck.entity.LcredSolicitudProrroga;
import org.domain.sck.entity.LcredSolicitudVtapuntual;
import org.domain.sck.entity.LcredTipoSolicitud;
import org.domain.sck.entity.LcredUsuarioNivelEnc;
import org.domain.sck.entity.PerfilFuncionCanal;
import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudLogs;
import org.domain.sck.entity.SolicitudUsuarioCorreo;
import org.domain.sck.entity.SolicitudUsuarioDerivada;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.entity.ZonaSucursalNegocioConcepto;
import org.domain.sck.entity.enums.ArchivoAdjuntoType;
import org.domain.sck.entity.enums.TipoCuentasKupferType;
import org.domain.sck.entity.enums.TipoSolicitudType;
import org.domain.sck.seam.GlobalParameters;
import org.domain.sck.session.home.ContenidoEmailSolicitudDTO;
import org.domain.sck.session.home.EmailAlertaContactoMensajeHelper;
import org.domain.sck.session.service.intranetsap.IntranetSapService;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.wsf.common.IOUtils;
import org.richfaces.model.UploadItem;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

import conexion.Connect;


@Name("visualizarSolicitudAction")
@Scope(ScopeType.CONVERSATION)
public class VisualizarSolicitudAction implements Serializable{
	

	@Logger
	Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	@In(value="#{identity.usuarioLogueado}", scope = ScopeType.SESSION)
	private Usuariosegur usuarioLogueado;
	
	@In(value="#{identity.lcredUsuarioNivelEnc}", scope = ScopeType.SESSION)
	private LcredUsuarioNivelEnc lcredUsuarioNivelEnc;
	
	@In(value="#{identity.usuarioSegur}", scope = ScopeType.SESSION)
	private UsuarioSegurDTO usuarioSegur;
	
	@In ScoringService scoringService;
	@In GlobalParameters globalParameters;
	@In IntranetSapService intranetSapService;
	@In GlobalHitosLogdService globalHitosLogdService;
	
	private LcredSolicitud solicitud;
	private Date fechaActual;
	private List<Sucursal> listaSucursales;
	private Sucursal sucursal;
	private ClienteDTO cliente;
	private CompComercialDTO comportamiento;
	private ExpoRiesgoKhDTO expoRiesgoKh;
	private LcredEstado lcredEstado;
	private Long solicitudSeleccionada;
	private String fechaDeauda;
	private Date fechaActualCtaCte;
	private LcredTipoSolicitud tipoSolicitudAux;
	private int paginaIngreso;
	private String tituloIngreso;
	private String paginaVolver;
    private List<String> listaFechas;
    private Long montoVtaPuntual;
    private Long pieVentaPuntual;
    private String  motivoCompra;
    private String observacionesCompra;
    private Double margenGlogal;
    private String productos;
    private String formaPago;
    private String observacionesFinales;
    private String normalCuota;
    private String innNomRiesgo; 
    private String innNomRiesgoNueva;
    private String observacionesModel;
	private String mensajeGlogal;
	private boolean salirDelMensaje;
	private String cPago;
	private String clsRiesgo;
	private String cPagoNuevo;
	private String clsRiesgoNuevo;
	private Long montoNuevo;
	private Long pieNuevo;
	private Long montoLineaCredito;
	private Long montoLineaCreditoNuevo;
	private String analisisAprobacion;
	private String mensajeAnalisisAprobacion;
	private String mensajeRechazoSolicitud;
	
	/*variables de Crédito*/
	private String descripcionProyecto;
	private Long montoCredito;
	private String plazoEjecucion;
	private Long potencialCompra;
	private String conceptoNegocios;
	private String rutNombresSocios;
	private String observacionesCredito;
	private boolean condicionRiesgo;
	private boolean condicionPago;
	private boolean comboCRiesgo;
	private boolean comboCPago;
	private String motivoRiesgoPago;
	private boolean condicionRiesgoNew;
	private boolean condicionPagoNew;	
	private String opcionBloqueoDesbloqueo;
	private String motivoBloDesbloqueo;
	private String opcionBloqueoDesbloqueoNew;	
	private SolicitudUsuarioCorreo usuarioCorreo;
	private String mensajeExplicativo;
	private String motivoProrroga;
	private LcredMotivoRechazo motivoRechazo;
	private boolean modificarVenta;
	private String modalAnalista;
    
	
    private LcredSolicitudVtapuntual venta = null;
    private LcredSolicitudLcredito credito = null;
    private LcredSolicitudCondiciones condicion = null;
    private LcredSolicitudBloqueos bloqueo = null;
    private List<LcredSolicitudDm>  listaDms = null;
    private List<LcredSolicitudProrroga> listaProrrogas = null;
    private LcredSolicitudOtras solicitudOtra = null;
    
    private List<ConceptoDTO> listaArchivoGuardado = null;
    private List<ConceptoDTO> listaArchivoGuardadoRespuesta = null;
    
    private List<LcredSolicitudObservaciones> listaObservaciones = null;
	private List<FileUploadedDTO> listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
    private List<CPagoDTO> listaCondicionPagos;
    private List<ClsRiesgoDTO> listaClsRiesgos;
    private List<String> listaStringClsRiesgos;
    private List<String> listaStringCdnPagos;
    private List<String> listaStringClsRiesgosNuevos;
    private List<String> listaStringCdnPagosNuevos;	
    private List<DestinatarioDTO> listaDestinatarios; 
	private List<SolicitudUsuarioCorreo> listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
	private List<SolicitudUsuarioCorreo> listaDescripcionUsuariosCorreos;
	private List<SolicitudUsuarioCorreo> listaUsuarioCorreos;
	private List<LcredSolicitudDm> listaClienteDMs;
	private List<SolicitudUsuarioCorreo> listaCorreoAnaDer = new ArrayList<SolicitudUsuarioCorreo>(0);
	
	private List<LcredMotivoRechazo> listaMotivosRechazos;
	private List<LcredMotivoRechazo> listaMotivosRechazosAgregados = new ArrayList<LcredMotivoRechazo>(0);	
	private List<LcredCargos> listaCargos;
	private BotonesDTO botonera;
	private boolean habilitarBotonAprobadoresInvolucrados;
	private LcredCargos cargoUsuario;
	
	private UsuarioCargo usuarioCargo;
	private List<CanalUsuarioCargo> listaCanalUsuarioCargos;
	private List<PerfilFuncionCanal> listaPerfilFuncionCanals;
	private List<ZonaSucursalNegocioConcepto> listaZonaSucursalNegocioConceptos;
    private List<CotPedDTO> listaCotPedDTOs;
	private Double montoTotal = (double) 0;
	private Double montoTotalNeto = (double) 0;
	private Double porcentajeGlobal;
	
	/*Funciones Sap*/
	private JCoFunction functionCtaCte;
	private List<ConceptoMontoDTO> listaConceptosMontos;
	/*variables de Ventas*/
    private long montoGeneralConceptoMonto;
	
	
	private String tabGrilla;
	private String usuarioEmisor;
	
	/*variable de log y hitos*/
	private List<SolicitudHitos> listaHitos;
	private List<SolicitudLogs> listaLogs;
	private List<SolicitudUsuarioDerivada> listUsuariosDerivados;
	private LcredEstado estadoLogHitos;
	private LcredSolicitud solicitudLogHitos;
	private SolicitudHitos solicitudHitos;
	
	
	
	@RequestParameter("idSolicitud")
	Long idSolicitud;
	@RequestParameter("tipoSolicitud")
	String tipoSolicitud;
	@RequestParameter("pagina")
	String pagina;
	@RequestParameter("tab")
	String tab;
	
	@Create
	public void init(){
		log.error("Inicia el componente de respuestaSolicitudAction");
		log.error("finaliza el componente de respuestaSolicitudAction");		
	}
	
	public void ingresarPaginaPrincipal(){

		log.error("Inicia el componente de respuestaSolicitudAction por metodo ingresarPaginaPrincipal");
		boolean exito = false;
		try{
			if(idSolicitud != null){
				if(lcredUsuarioNivelEnc != null){
					cargoUsuario = scoringService.getCargo(lcredUsuarioNivelEnc.getId().getCargo());
					listaCargos = scoringService.getListaCargos();
					if(usuarioSegur != null){
						usuarioCargo = scoringService.getUsuarioCargo(usuarioSegur.getIdPersonal());
						if(usuarioCargo != null){
							listaCanalUsuarioCargos = scoringService.getListaCanalUsuarioCargo(usuarioCargo.getIdUsuarioCargo());
							if(listaCanalUsuarioCargos != null){
								for(CanalUsuarioCargo cuc : listaCanalUsuarioCargos){
									listaPerfilFuncionCanals = scoringService.getListaPerfilFuncionCanal(cuc.getIdCanalUsuarioCargo(), listaPerfilFuncionCanals);
								}
							}
						}	
					}	
				}
				
				solicitud = (LcredSolicitud)entityManager.
						createQuery("select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud")
						.setParameter("numSolicitud", idSolicitud).getSingleResult();

				if(solicitud != null){
					this.botonera = new BotonesDTO();
					
					if("Mixto".equals(solicitud.getCanal().trim())){
						validarBotonera(TipoCuentasKupferType.MX, solicitud);
					}else if("Kupfer Express".equals(solicitud.getCanal().trim())){
						validarBotonera(TipoCuentasKupferType.KX, solicitud);
					}else if("Grandes Cuentas".equals(solicitud.getCanal().trim())){
						validarBotonera(TipoCuentasKupferType.GC, solicitud);
					}
					
					//limpiar variaobles
					this.porcentajeGlobal = (double)0;
					this.montoTotalNeto = (double)0;
					this.montoTotal = (double)0;
					
					//solicitud.setUsuarioActual(usuarioSegur.getAlias());
					exito = true; //scoringService.mergerSolicitud(solicitud);
					if(solicitud != null && exito == true){
						if(pagina != null){
							setPaginaVolver(pagina);
						}
						setSolicitudSeleccionada(solicitud.getId().getNumSolicitud());
						cambioEvaluacionSolicitud(solicitud);
						insertarLogs(solicitud,1);
						//evaludar(solicitud, pagina);
						evaludarNuevo(solicitud, pagina);
						setSolicitudLogHitos(solicitud);
					}
				}
			}

			log.debug("parametro de tab :#0 ", tab);
			if(tab != null && !"".equals(tab)){
				this.setTabGrilla(tab);
			}
			
			/*sacar los conceptos  de general*/
			obtenerListaConceptoNegocioMarca();
			
			
			
		}catch (Exception e) {
			log.error("Error, al sacar la solicitud seleccionada #0", e.getMessage());
		}
		log.error("finaliza el componente de respuestaSolicitudAction por el metodo ingresarPaginaPrincipal");		
			
		
	}
	
	
	public void obtenerListaConceptoNegocioMarca(){
		try{
			List<ConceptosNegocio> listaConceptoNegocio = scoringService.obtenerListaConceptoNegocio();
			if(listaConceptoNegocio != null){
				listaConceptosMontos = new ArrayList<ConceptoMontoDTO>();
				ConceptoMontoDTO conceptoMonto = null;
				for(ConceptosNegocio cn : listaConceptoNegocio){
					conceptoMonto = new ConceptoMontoDTO();
					conceptoMonto.setNegocio(cn.getNegocio());
					conceptoMonto.setConcepto(cn.getConcepto());
					conceptoMonto.setDescripcion(cn.getDescripcion());
					conceptoMonto.setMarca("");
					conceptoMonto.setMonto(0);
					conceptoMonto.setJerarquia(cn.getJerarquia());
					listaConceptosMontos.add(conceptoMonto);
				}
				
				this.montoGeneralConceptoMonto = 0;
				
			}
		}catch (Exception e) {
			log.error("Error, al sacar lista de los conceptos #0", e.getMessage());
		}
	}
	
	
	public void validarBotonera(TipoCuentasKupferType tipo,LcredSolicitud sol){
		try{
			if(tipo  != null && usuarioCargo != null && sol != null){
				botonera.setObservaiones(true);
				botonera.setSalir(true);
				botonera.setHitos(true);
				botonera.setLogs(true);
			}
		}catch (Exception e) {
			log.error("Error, al setear al botenera", e.getMessage());
		}
	}	
	
	
	
	@SuppressWarnings("unchecked")
	public void evaludarNuevo(LcredSolicitud solicitud, String pagina){
		
		try{
				if(solicitud != null){

					
					if(pagina != null){
						setPaginaVolver(pagina);
					}
					
					setSolicitudSeleccionada(solicitud.getId().getNumSolicitud());
					cambioEvaluacionSolicitud(solicitud);
					insertarLogs(solicitud,1);
					
					cliente = new ClienteDTO();
					sacarListas();
					log.debug("Sucursal:"+ solicitud.getSucursalEmisor());
					obtenerSucursal(solicitud.getSucursalEmisor());
					
					log.debug("codigo de emisor #0", solicitud.getCodEmisor());
					UsuarioSegurDTO user = scoringService.sacarDatosSessionUsuario(solicitud.getCodEmisor());
					if(user != null){
						this.setUsuarioEmisor(user.getNombre());
					}else{
						this.setUsuarioEmisor(solicitud.getCodEmisor());
					}					
					
					/*setear los datos de cliente*/
					cliente.setRut(solicitud.getRutCliente());
					cliente.setRazonSocial(solicitud.getNomCliente());
					cliente.setGiro(solicitud.getGiroCliente());
					cliente.setDireccion(solicitud.getDirCliente());
					cliente.setFono(solicitud.getTelCliente());
					cliente.setFax(solicitud.getFaxCliente());
					cliente.setComuna(solicitud.getComCliente());
					cliente.setCiudad(solicitud.getCiuCliente());
					Sucursal sucusarlcliente = scoringService.obtenerSucursalForCodigo(solicitud.getCodSucursal());
					if(sucusarlcliente != null){
						cliente.setSucursalCliente(sucusarlcliente.getDescripcion());
					}else{
						cliente.setSucursalCliente(solicitud.getCodSucursal());
					}					
					
					cliente.setCanalVenta(solicitud.getCanal());
					cliente.setTipoCliente(solicitud.getTipoCliente());
					cliente.setLineaCreditoKH(solicitud.getLinCredito());
					cliente.setLineaCreditoKHUtilizado(solicitud.getLinCreditoUtiliz());
					cliente.setLineaCreditoKHDisponible(solicitud.getLinCreditoDisp());
					cliente.setEstadoLineaCreditoKH(solicitud.getEstadoLcKh());
					cliente.setNumeroRV(solicitud.getNroRv().toString());
					cliente.setSeguro(solicitud.getTipSeguro());
					cliente.setEstadoLineaEnCuotas(solicitud.getEstadoLcCuotas());
					cliente.setCodigoCondicionPago(solicitud.getConPago());
					cliente.setCodigoclasificacionRiesgo(solicitud.getClsRiesgo());
					cliente.setVentasTotal12Meses(solicitud.getVtaTotal12Meses());
					cliente.setCreacionSap(solicitud.getFechaCreacionCliente());	
					cliente.setDmVentasUno(solicitud.getDmVenta());
					cliente.setPrmFacturas(solicitud.getPrmFacturas());
					cliente.setCantMesesVentas(solicitud.getMesesVentas().longValue());
					cliente.setVentasProm12Meses(solicitud.getPrmVentas());
					cliente.setVigenciaSeguro(solicitud.getVigSeguro());
					cliente.setFactProm12Meses(solicitud.getPrmFacturas().longValue());
					
					lcredEstado = ObtenerEstadoSolictud(solicitud.getEstado());
					if(lcredEstado != null){
						cliente.setEstadoSolicitud(lcredEstado.getDesEstado());
						this.setModalAnalista(solicitud.getEstado());
					}else{
						cliente.setEstadoSolicitud(solicitud.getEstado());
					}
					
					cliente.setMontoSeguro(solicitud.getSeguroPesos());
					cliente.setMontoSeguroUf(solicitud.getSeguroUf().longValue());
					
					try{
						if(cliente.getCodigoCondicionPago()!=null){
							Object obj = intranetSapService.sacarDescripCondicionPago(cliente.getCodigoCondicionPago());
							if(obj != null){
								cliente.setCondicionPago(obj.toString());
							}else{
								cliente.setCondicionPago("No Clasificado");
							}
						}else{cliente.setCondicionPago("No Clasificado");}
						
					}catch (Exception e) {
						cliente.setCondicionPago("No Clasificado");
					}

					try{
						if(cliente.getCodigoclasificacionRiesgo() != null){
							Object obj = intranetSapService.sacarDescripClasificacionRiesgo(cliente.getCodigoclasificacionRiesgo());
							if(obj != null){
								cliente.setClasificacionRiesgo(obj.toString());
							}else{
								cliente.setClasificacionRiesgo("No Clasificado");
							}
						}else{cliente.setClasificacionRiesgo("No Clasificado");}
						
					}catch (Exception e) {
						cliente.setClasificacionRiesgo("No Clasificado");
					}
					
					String codigoCanal = solicitud.getCanal();
					if(codigoCanal != null){
						String kx = TipoCuentasKupferType.KX.getNombre().trim();
						String gc = TipoCuentasKupferType.GC.getNombre().trim();
						String mx = TipoCuentasKupferType.MX.getNombre().trim();
						if(kx.equals(codigoCanal.trim())){
							cliente.setCodigoCanal(TipoCuentasKupferType.KX);
						}
						if(gc.equals(codigoCanal.trim())){
							cliente.setCodigoCanal(TipoCuentasKupferType.GC);
						}
						if(mx.equals(codigoCanal.trim())){
							cliente.setCodigoCanal(TipoCuentasKupferType.MX);
						}
					}
					
					
					/*setar los datos de comportamiento comercial*/
					comportamiento = new CompComercialDTO();
					comportamiento.setDiasMoras(solicitud.getDiasMora());
					comportamiento.setDmPort(solicitud.getDm());
					comportamiento.setMontoTotalMoro(solicitud.getMorosidadMonto().longValue());
					comportamiento.setMontoTotalProt(solicitud.getProtestosMonto().longValue());
					comportamiento.setNumeroMoro(solicitud.getMorosidadNro());
					comportamiento.setNumeroProt(solicitud.getProtestosNro());
					
					/*setar los datos del exposicion de riesgo kupfer*/
					expoRiesgoKh = new ExpoRiesgoKhDTO();
					expoRiesgoKh.setPeakCredito(solicitud.getPeackCredito().longValue());
					expoRiesgoKh.setMontoRiesgoKupfer(solicitud.getRiesgoKupfer().longValue());
					long montoRiegoKupferPedidoPorceso = solicitud.getRiesgoKupfer().longValue() + solicitud.getMontoCuotas().longValue();
					expoRiesgoKh.setMontoRiesgoKupferPedidoProceso(montoRiegoKupferPedidoPorceso);
					expoRiesgoKh.setMontoPedidoProceso(solicitud.getMontoCuotas().longValue());
					long montoPeakCreditoPedidoProceso = solicitud.getPeackCredito().longValue() + solicitud.getMontoCuotas().longValue();
					expoRiesgoKh.setMontoPeakCreditoPedidoProceso(montoPeakCreditoPedidoProceso);
					expoRiesgoKh.setMontoAsegurado(solicitud.getMontoAsegurado().longValue());
					this.setObservacionesFinales(null);
					this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
					this.listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);


					
					log.debug("solicitud.getTipTiposol() : #0", solicitud.getTipTiposol());
					 if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.V1N.getNombre()) || 
					    (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.V1C.getNombre())){
							try{ 
								 venta = (LcredSolicitudVtapuntual)entityManager
										 .createQuery("Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
										 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getSingleResult();
							}catch (Exception e) {
								log.debug("no existe datos de venta puntual");
							}
							 
							 this.setTituloIngreso(solicitud.getDesTiposol());
							 
							 if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.V1N.getNombre())){
								 this.setPaginaIngreso(11);
							 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.V1C.getNombre())){
								 this.setPaginaIngreso(12);
							 }
							 
							 if(venta != null){
								this.setMontoVtaPuntual(venta.getMontoInicial().longValue());
								this.setPieVentaPuntual(venta.getMontoPieInicial().longValue());
								this.setProductos(venta.getDcProducto());
								this.setObservacionesCompra("");
								this.setMotivoCompra(venta.getDcMotivo());
								this.setFormaPago(venta.getObservacionesInicial());
								try{
									String margenAux = venta.getDcMargen().replace(",", ".");
									String margennuevo = margenAux.replace("%", "");
									Double margen = Double.parseDouble(margennuevo);
									this.setMargenGlogal(margen);
								}catch (Exception e) {
									log.error("Error al transformar el margen global #0", e.getMessage());
									this.setMargenGlogal((double)0);
								}
								
								/*setar los valores al los datos*/
								this.setMontoNuevo(venta.getMontoInicial().longValue());
								this.setPieNuevo(venta.getMontoPieInicial().longValue());
								if(this.paginaIngreso != 0){
									this.normalCuota = String.valueOf(this.paginaIngreso);
								}
								this.setObservacionesCompra(solicitud.getGlsObservaciones());
								this.setObservacionesFinales(venta.getObservacionesFinal());
								
							 }
							 
							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
							 if(listaArchivos != null){
								 ConceptoDTO obj = null;
								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								 for(ArchivoAdjunto aa : listaArchivos ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 listaArchivoGuardado.add(obj);
								 }
							 }
							 
							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
							 if(listaArchivosRespuesta != null){
								 ConceptoDTO obj = null;
								 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
								 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 listaArchivoGuardadoRespuesta.add(obj);
								 }
							 }							 
							 
							 
							 List<LcredArchivoAdjuntos> listaArchivosRespuestaAntiguos = scoringService.obtenerArchivosSolicitudAntiguas(solicitud.getId().getNumSolicitud(),String.valueOf(this.paginaIngreso));
							 if(listaArchivosRespuestaAntiguos != null){
								 ConceptoDTO obj = null;
								 for(LcredArchivoAdjuntos laa : listaArchivosRespuestaAntiguos ){
									 obj = new ConceptoDTO();
									 String nombreAux = laa.getId().getArchivoAdjunto().replace("\\",";");
									 String[] arreglo = nombreAux.split(";");
									 int cant = arreglo.length;
									 if(cant > 0){
										 obj.setNombreArchivo(arreglo[cant-1]);
										 String urlAux = globalParameters.getAttachedFilesWebPath()+""+arreglo[cant-3]+"/"+arreglo[cant-2]+"/"+arreglo[cant-1];
										 log.debug("url transformada  #0", urlAux);
										 obj.setRutaCompleta(urlAux);
										 if(listaArchivoGuardado == null || listaArchivoGuardado.size() == 0){
											 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
											 listaArchivoGuardado.add(obj);
										 }else{
											 listaArchivoGuardado.add(obj);
										 }
									 }
								 }
							 }	
							 
							 
							 this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
							 
							 /*sacar los datps de representacion la grilla nuevamente.*/
							 List<CotizacionPedidoCabecera> listaCpc = scoringService.getCotizacionPedidoCobecera(venta.getNumSolicitud());
							 if(listaCpc != null && listaCpc.size() > 0){
								 this.listaCotPedDTOs = new ArrayList<CotPedDTO>(0);
								 List<CabeceraCotPedDTO> listaCabeceraCotPeds = null;
								 List<DetalleCotPedDTO> listaDetalle = null;
								 CotPedDTO primero = null;CabeceraCotPedDTO segundo = null;  DetalleCotPedDTO tercero = null; 
								 for(CotizacionPedidoCabecera cpc : listaCpc){
									 primero = new CotPedDTO();
									 primero.setMontoNeto(cpc.getNeto());
									 primero.setMontoTotal(cpc.getTotal());
									 primero.setPonderacion(cpc.getPorcentaje());
									 
									 montoTotal+=cpc.getTotal();
									 montoTotalNeto+= cpc.getNeto();
									 List<CotizacionPedido> listaCp = scoringService.getCotizacionPedido(cpc.getSystemId());
									 if(listaCp != null && listaCp.size() > 0){
										 for(CotizacionPedido cp: listaCp){
											 primero.setNumeroCotizacion(String.valueOf(cp.getNCotiPed()));
											 primero.setMargenGlobal(cp.getMargenGlobal());
											 List<CotizacionPedidoNegocio> listaCpn = scoringService.getCotizacionPedidoNegocio(cp.getSystemId());
											 if(listaCpn != null && listaCpn.size() > 0 ){
												 listaCabeceraCotPeds = new ArrayList<CabeceraCotPedDTO>(0);
												 for(CotizacionPedidoNegocio cpn : listaCpn){
													 segundo = new CabeceraCotPedDTO(); 
													 ConceptosNegocio cn = scoringService.obtenerConceptoNegocioLocalForCodigoNegocio(cpn.getNegocio());
													 segundo.setNegocio(cn);
													 segundo.setMontoGeneral((long) 0);
													 segundo.setPorcentaje(cpn.getMargenNegocio());
													 segundo.setRepresentacion(cpn.getRepresentacion());
													 List<CotizacionPedidoConcepto> listaCpCp = scoringService.getCotizacionPedidoConcepto(cpn.getSystemId());
													 if(listaCpCp != null && listaCpCp.size() > 0){
														 listaDetalle = new ArrayList<DetalleCotPedDTO>(0);
														 for(CotizacionPedidoConcepto cpcp : listaCpCp){
															 tercero = new DetalleCotPedDTO();
															 ConceptosNegocio cn2 = scoringService.obtenerConceptoNegocioLocalForNegocioAndConcepto(cpn.getNegocio(), cpcp.getCodigoConcepto());
															 tercero.setNegocio(cn2);
															 tercero.setMargenConcepto(cpcp.getMargenConcepto());
															 tercero.setMonto(cpcp.getMontoMasIva());
															 tercero.setMontoNeto((long)(cpcp.getMontoMasIva()/1.19));
															 tercero.setPorceDelTotal(cpcp.getMargenPoderado());
															 if(cpcp.getMargenPorcePorConcepto() != null){ tercero.setPorcePorConcepto(cpcp.getMargenPorcePorConcepto());}
															 else{ tercero.setPorcePorConcepto(cpcp.getMargenConcepto());}
															 List<DetalleCp> listaDcp = scoringService.getDetalleCp(cpcp.getSystemId());
															  if(listaDcp != null && listaDcp.size() > 0){
																  tercero.setListaDetalleCp(listaDcp);
															  }
															  listaDetalle.add(tercero);
														 }
													 }
													segundo.setListaDetalle(listaDetalle); 
													listaCabeceraCotPeds.add(segundo);
												 }
											 }
											 primero.setListaCabeceraCotPeds(listaCabeceraCotPeds);
											 listaCotPedDTOs.add(primero); 
										 }
									 }
								 }
									sacarPorcetajeGlobalConceptoNegocio();
							        sacarPorcetajeGlobal();
							        sacarPorcetajeCotizacionToPedido();
							        sacarPorcetajePedidoGlobal();	
							        insertMontosEnListaConceptosMontos();
						        
							 }							 
							 
							 
							 
							/*va a buscar los destinatarios de las solictudes*/
							 
							 if(listaUsuarioCorreoagregados == null || listaUsuarioCorreoagregados.size() == 0){
								 //obtenerCorreo(solicitud.getCodEmisor(), "1");
							  }
							 this.modificarVenta = false;
							 

					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC1.getNombre()) || (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC2.getNombre()) ||
							 (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC3.getNombre())){
							
						 	this.setTituloIngreso(solicitud.getDesTiposol());
						    try{ 
								 credito = (LcredSolicitudLcredito)entityManager
										 .createQuery("Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
										 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getSingleResult();
							 }catch (Exception e) {
								log.debug("no existe datos de linea credito.");
							 }
							 
							 this.setTituloIngreso(solicitud.getDesTiposol());
							 if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC1.getNombre())){
								 this.setPaginaIngreso(21);
							 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC2.getNombre())){
								 this.setPaginaIngreso(22);
							 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC3.getNombre())){
								 this.setPaginaIngreso(23);
							 }

							 this.setInnNomRiesgo(String.valueOf(this.paginaIngreso));
							 this.setInnNomRiesgoNueva(String.valueOf(this.paginaIngreso));
							 
							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
							 if(listaArchivos != null){
								 ConceptoDTO obj = null;
								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								 for(ArchivoAdjunto aa : listaArchivos ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 listaArchivoGuardado.add(obj);
								 }
							 }
							 
							 
							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
							 if(listaArchivosRespuesta != null){
								 ConceptoDTO obj = null;
								 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
								 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 listaArchivoGuardadoRespuesta.add(obj);
								 }
							 }		
							 
							 
							 List<LcredArchivoAdjuntos> listaArchivosRespuestaAntiguos = scoringService.obtenerArchivosSolicitudAntiguas(solicitud.getId().getNumSolicitud(),String.valueOf(this.paginaIngreso));
							 if(listaArchivosRespuestaAntiguos != null){
								 ConceptoDTO obj = null;
								 for(LcredArchivoAdjuntos laa : listaArchivosRespuestaAntiguos ){
									 obj = new ConceptoDTO();
									 String nombreAux = laa.getId().getArchivoAdjunto().replace("\\",";");
									 String[] arreglo = nombreAux.split(";");
									 int cant = arreglo.length;
									 if(cant > 0){
										 obj.setNombreArchivo(arreglo[cant-1]);
										 String urlAux = globalParameters.getAttachedFilesWebPath()+""+arreglo[cant-3]+"/"+arreglo[cant-2]+"/"+arreglo[cant-1];
										 log.debug("url transformada  #0", urlAux);
										 obj.setRutaCompleta(urlAux);
										 if(listaArchivoGuardado == null || listaArchivoGuardado.size() == 0){
											 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
											 listaArchivoGuardado.add(obj);
										 }else{
											 listaArchivoGuardado.add(obj);
										 }
									 }
								 }
							 }	
							 
							 
							 this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
						
						
							/*sacando condiciones de riesgo y de pago */
							try{
								if(listaClsRiesgos == null || listaClsRiesgos.size() == 0){
									listaClsRiesgos = intranetSapService.getClasificacionRiesgo();
								}
								if(listaCondicionPagos == null || listaCondicionPagos.size() == 0){
									listaCondicionPagos = intranetSapService.getCondicionPago();
								}							
							}catch (Exception e) {
								log.error("Error, al obtener los datos de lista risgo y pago #0", e.getMessage());
							}
							
							if(credito != null){
								if(credito.getCondPagoInicial() != null && credito.getCondRiesgoInicial() != null){
									evaluarInnominalNominalRKupfer( credito.getCondRiesgoInicial(),credito.getCondPagoInicial());
								}
								if(credito.getCondPagoFinal() != null && credito.getCondRiesgoFinal() != null){
									evaluarInnominalNominalRKupferFinales( credito.getCondRiesgoFinal(),credito.getCondPagoFinal());
								}
								
								if(credito.getCondPagoFinal() == null && credito.getCondRiesgoFinal() == null){
									evaluarInnominalNominalRKupferFinales( credito.getCondRiesgoInicial(),credito.getCondPagoInicial());
								}
								
								this.setDescripcionProyecto(credito.getDpDescripcionProyecto());
								String montoAux = credito.getDpMonto().replace(".", "");
								this.setMontoCredito(Long.parseLong(montoAux));
								this.setPlazoEjecucion(credito.getDpPlazoEjecucion());
								this.setConceptoNegocios(credito.getDpConceptosInvoluc());
								this.setRutNombresSocios(credito.getDpSocios());
								String montoPotencial = credito.getDpPotencialCompra().replace(".", "");
								this.setPotencialCompra(Long.parseLong(montoPotencial));
								
								if(credito.getObservacionesInicial() != null && !"".equals(credito.getObservacionesInicial())){
									this.setObservacionesCredito(credito.getObservacionesInicial());
								}else if(solicitud.getGlsObservaciones() != null){
									this.setObservacionesCredito(solicitud.getGlsObservaciones());
								}	
								
								if(credito.getObservacionesFinal() != null && !"".equals(credito.getObservacionesFinal())){
									this.setObservacionesFinales(credito.getObservacionesFinal());
								}
								
								this.setMontoLineaCredito(credito.getMontoNormalInicial().longValue());
								if(credito.getMontoNormalFinal() != null){
									this.setMontoLineaCreditoNuevo(credito.getMontoNormalFinal().longValue());
								}else{
									this.setMontoLineaCreditoNuevo(credito.getMontoNormalInicial().longValue());
								}
							}
							
							/*va a buscar los destinatarios de las solictudes*/
							 //obtenerCorreo(solicitud.getCodEmisor(), "2");
							
						 
					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.CR1.getNombre()) || (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.CR2.getNombre()) ||
							 (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.CRP3.getNombre()) ||  (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.CRP4.getNombre())){
						 	
						 	this.setTituloIngreso(solicitud.getDesTiposol());
						 	
						 	this.comboCPago = true;
						 	this.comboCRiesgo = true;
						  try{ 
							  condicion = (LcredSolicitudCondiciones)entityManager
									 .createQuery("Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
									 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getSingleResult();
						  }catch (Exception e) {
							log.debug("no existe datos de condiciones.");
						  }		
						  List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
						  if(listaArchivos != null){
							 ConceptoDTO obj = null;
							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							 for(ArchivoAdjunto aa : listaArchivos ){
								 obj = new ConceptoDTO();
								 obj.setNombreArchivo(aa.getNombreAdjunto());
								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
								 listaArchivoGuardado.add(obj);
							 }
						  }						  
						  
						 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
						 if(listaArchivosRespuesta != null){
							 ConceptoDTO obj = null;
							 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
							 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
								 obj = new ConceptoDTO();
								 obj.setNombreArchivo(aa.getNombreAdjunto());
								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
								 listaArchivoGuardadoRespuesta.add(obj);
							 }
						 }		
						 
						  
						  if(condicion != null ){
							  setPaginaIngreso(31);
								/*sacando condiciones de riesgo y de pago */
								try{
									if(listaClsRiesgos == null || listaClsRiesgos.size() == 0){
										listaClsRiesgos = intranetSapService.getClasificacionRiesgo();
									}
									if(listaCondicionPagos == null || listaCondicionPagos.size() == 0){
										listaCondicionPagos = intranetSapService.getCondicionPago();
									}							
								}catch (Exception e) {
									log.error("Error, al obtener los datos de lista risgo y pago #0", e.getMessage());
								}
							 
								if(condicion.getCodCondPagoInicial() != null && condicion.getCodCondRiesgoInicial() != null){
									evaluarInnominalNominalRKupfer(condicion.getCodCondRiesgoInicial(),condicion.getCodCondPagoInicial());
								}

								
								if((condicion.getCondRiesgoInicial() != null ) &&  (condicion.getCondRiesgoInicial().toString()).equals("S")){
									this.condicionRiesgo = true;
									this.condicionRiesgoNew = true;
									evaluarInnominalNominalRKupfer(condicion.getCodCondRiesgoInicial(),condicion.getCodCondPagoInicial());
									this.comboCRiesgo = true; 
								}
								if((condicion.getCondPagoInicial() != null ) && (condicion.getCondPagoInicial().toString()).equals("S")){
									this.condicionPago = true;
									this.condicionPagoNew = true;
									evaluarInnominalNominalRKupfer(condicion.getCodCondRiesgoInicial(),condicion.getCodCondPagoInicial());
									this.comboCPago = true;
								}
								if(condicion.getMotivoCambio() != null){
									this.setMotivoRiesgoPago(condicion.getMotivoCambio());
									this.setObservacionesFinales(condicion.getMotivoCambio());
								}else if(solicitud.getGlsObservaciones() != null){
									this.setMotivoRiesgoPago(solicitud.getGlsObservaciones());
									this.setObservacionesFinales(solicitud.getGlsObservaciones());
								}

								evaluarInnominalNominalRKupferFinales(condicion.getCodCondRiesgoFinal(),condicion.getCodCondPagoFinal());
								log.debug(this.cPagoNuevo);
								log.debug(this.clsRiesgoNuevo);
								
						  }

						  List<LcredArchivoAdjuntos> listaArchivosRespuestaAntiguos = scoringService.obtenerArchivosSolicitudAntiguas(solicitud.getId().getNumSolicitud(),String.valueOf(this.paginaIngreso));
						  if(listaArchivosRespuestaAntiguos != null){
								 ConceptoDTO obj = null;
								 for(LcredArchivoAdjuntos laa : listaArchivosRespuestaAntiguos ){
									 obj = new ConceptoDTO();
									 String nombreAux = laa.getId().getArchivoAdjunto().replace("\\",";");
									 String[] arreglo = nombreAux.split(";");
									 int cant = arreglo.length;
									 if(cant > 0){
										 obj.setNombreArchivo(arreglo[cant-1]);
										 String urlAux = globalParameters.getAttachedFilesWebPath()+""+arreglo[cant-3]+"/"+arreglo[cant-2]+"/"+arreglo[cant-1];
										 log.debug("url transformada  #0", urlAux);
										 obj.setRutaCompleta(urlAux);
										 if(listaArchivoGuardado == null || listaArchivoGuardado.size() == 0){
											 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
											 listaArchivoGuardado.add(obj);
										 }else{
											 listaArchivoGuardado.add(obj);
										 }
									 }
								 }
							 }	
						  
						  this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
						  
						  /*va a buscar los destinatarios de las solictudes*/
						  //obtenerCorreo(solicitud.getCodEmisor(), "3");						 
					  
							 
							 
					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS1.getNombre()) || (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS2.getNombre()) || 
							 (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS.getNombre())){/***************** Bloqueo y Desbloqueo ***********************/
						 	
						 	try{
								 bloqueo = (LcredSolicitudBloqueos)entityManager
										 .createQuery("Select sb from LcredSolicitudBloqueos sb where sb.numSolicitud=:solAux")
										 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getSingleResult();
							}catch (Exception e) {
								log.debug("no existe datos de bloqueo o desbloqueo.");
							}
						 	
						 	solicitudOtra = scoringService.getSolicitudOtrasId(solicitud.getId().getNumSolicitud());
						 	
							if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS1.getNombre())){
								this.setOpcionBloqueoDesbloqueo("41");
								this.setOpcionBloqueoDesbloqueoNew("41");
							}else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS2.getNombre())){
								this.setOpcionBloqueoDesbloqueo("42");
								this.setOpcionBloqueoDesbloqueoNew("42");
							}else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS.getNombre())){
								this.setOpcionBloqueoDesbloqueo("");
							}							 
						 
							if(solicitudOtra  != null && bloqueo == null){
								 this.setPaginaIngreso(41);
								 this.setMotivoBloDesbloqueo(solicitudOtra.getObservacionesInicial());
							}else if(solicitudOtra  == null && bloqueo != null){
								 this.setPaginaIngreso(41);
								 this.setMotivoBloDesbloqueo(bloqueo.getMotivoBloqDesbloq());
							}else if(solicitudOtra  != null && bloqueo != null){
								 this.setPaginaIngreso(41);
								 this.setMotivoBloDesbloqueo(solicitudOtra.getObservacionesInicial());								
							}
							
							if(solicitudOtra.getOpcionInicial() != null){
								if(solicitudOtra.getOpcionInicial().equals("B")){
									this.setOpcionBloqueoDesbloqueo("41");
								}else{
									this.setOpcionBloqueoDesbloqueo("42");
								}
								
							}
							
							if(solicitudOtra.getOpcionFinal() != null){
								if(solicitudOtra.getOpcionFinal().equals("B")){
									this.setOpcionBloqueoDesbloqueoNew("41");
								}else{
									this.setOpcionBloqueoDesbloqueoNew("42");
								}								
								
							}
							
							this.setObservacionesFinales(solicitudOtra.getObservacionesFinal());
							
							
							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
							if(listaArchivos != null){
								 ConceptoDTO obj = null;
								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								 for(ArchivoAdjunto aa : listaArchivos ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 listaArchivoGuardado.add(obj);
								 }
							 }	
							
							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
							 if(listaArchivosRespuesta != null){
								 ConceptoDTO obj = null;
								 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
								 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 listaArchivoGuardadoRespuesta.add(obj);
								 }
							 }	
							 
							 List<LcredArchivoAdjuntos> listaArchivosRespuestaAntiguos = scoringService.obtenerArchivosSolicitudAntiguas(solicitud.getId().getNumSolicitud(),String.valueOf(this.paginaIngreso));
							  if(listaArchivosRespuestaAntiguos != null){
									 ConceptoDTO obj = null;
									 for(LcredArchivoAdjuntos laa : listaArchivosRespuestaAntiguos ){
										 obj = new ConceptoDTO();
										 String nombreAux = laa.getId().getArchivoAdjunto().replace("\\",";");
										 String[] arreglo = nombreAux.split(";");
										 int cant = arreglo.length;
										 if(cant > 0){
											 obj.setNombreArchivo(arreglo[cant-1]);
											 String urlAux = globalParameters.getAttachedFilesWebPath()+""+arreglo[cant-3]+"/"+arreglo[cant-2]+"/"+arreglo[cant-1];
											 log.debug("url transformada  #0", urlAux);
											 obj.setRutaCompleta(urlAux);
											 if(listaArchivoGuardado == null || listaArchivoGuardado.size() == 0){
												 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
												 listaArchivoGuardado.add(obj);
											 }else{
												 listaArchivoGuardado.add(obj);
											 }
										 }
									 }
								 }	
						 
						   this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
						 
						
						   
						 
					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS3.getNombre())){/***************** Creacion DM ***********************/
						 try{
							   listaDms = scoringService.getSacarListaDms(solicitud.getId().getNumSolicitud());
							   if(listaDms != null){
								   this.setListaClienteDMs(listaDms);
							   }
						}catch (Exception e) {
							log.debug("no existe datos de los dm. #0",e.getMessage() );
						}		
						 	
						 solicitudOtra = scoringService.getSolicitudOtrasId(solicitud.getId().getNumSolicitud());
						 if(solicitudOtra != null){
							 log.debug("Existe resgistro en otra solicitud ");
							 this.setObservacionesFinales(solicitudOtra.getObservacionesFinal());
						 }
						 
						 
					 	this.setTituloIngreso(solicitud.getDesTiposol());
					 	
						if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS3.getNombre())){
							this.setPaginaIngreso(43);
							this.setMotivoProrroga(solicitud.getObservSolicitud());
						}	
						 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
						if(listaArchivos != null){
							 ConceptoDTO obj = null;
							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							 for(ArchivoAdjunto aa : listaArchivos ){
								 obj = new ConceptoDTO();
								 obj.setNombreArchivo(aa.getNombreAdjunto());
								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
								 listaArchivoGuardado.add(obj);
							 }
						 }						  
					 
						 List<LcredArchivoAdjuntos> listaArchivosRespuestaAntiguos = scoringService.obtenerArchivosSolicitudAntiguas(solicitud.getId().getNumSolicitud(),String.valueOf(this.paginaIngreso));
						  if(listaArchivosRespuestaAntiguos != null){
								 ConceptoDTO obj = null;
								 for(LcredArchivoAdjuntos laa : listaArchivosRespuestaAntiguos ){
									 obj = new ConceptoDTO();
									 String nombreAux = laa.getId().getArchivoAdjunto().replace("\\",";");
									 String[] arreglo = nombreAux.split(";");
									 int cant = arreglo.length;
									 if(cant > 0){
										 obj.setNombreArchivo(arreglo[cant-1]);
										 String urlAux = globalParameters.getAttachedFilesWebPath()+""+arreglo[cant-3]+"/"+arreglo[cant-2]+"/"+arreglo[cant-1];
										 log.debug("url transformada  #0", urlAux);
										 obj.setRutaCompleta(urlAux);
										 if(listaArchivoGuardado == null || listaArchivoGuardado.size() == 0){
											 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
											 listaArchivoGuardado.add(obj);
										 }else{
											 listaArchivoGuardado.add(obj);
										 }
									 }
								 }
							 }						
						
						
					   this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));						
					 
					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS4.getNombre())){/********************* Prorroga ********************/
						 try{
							 listaProrrogas = (List<LcredSolicitudProrroga>)entityManager
									 .createQuery("Select lp from LcredSolicitudProrroga lp where lp.id.numSolicitud=:solAux")
									 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getResultList();						 
						}catch (Exception e) {
							log.debug("no existe datos de las prorrogas dm.");
						}	
						 
						 
						 solicitudOtra = scoringService.getSolicitudOtrasId(solicitud.getId().getNumSolicitud());
					 	if(solicitudOtra != null){
					 		this.setMotivoProrroga(solicitudOtra.getObservacionesInicial());
					 	}

					 	this.setObservacionesFinales(solicitudOtra.getObservacionesFinal());
						 
					 	this.setTituloIngreso(solicitud.getDesTiposol());
						if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS4.getNombre())){
							this.setPaginaIngreso(44);
						}	
						 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
						if(listaArchivos != null){
							 ConceptoDTO obj = null;
							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							 for(ArchivoAdjunto aa : listaArchivos ){
								 obj = new ConceptoDTO();
								 obj.setNombreArchivo(aa.getNombreAdjunto());
								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
								 listaArchivoGuardado.add(obj);
							 }
						 }						  
					 
						 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
						 if(listaArchivosRespuesta != null){
							 ConceptoDTO obj = null;
							 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
							 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
								 obj = new ConceptoDTO();
								 obj.setNombreArchivo(aa.getNombreAdjunto());
								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
								 listaArchivoGuardadoRespuesta.add(obj);
							 }
						 }	
						 
						 
						 List<LcredArchivoAdjuntos> listaArchivosRespuestaAntiguos = scoringService.obtenerArchivosSolicitudAntiguas(solicitud.getId().getNumSolicitud(),String.valueOf(this.paginaIngreso));
						  if(listaArchivosRespuestaAntiguos != null){
								 ConceptoDTO obj = null;
								 for(LcredArchivoAdjuntos laa : listaArchivosRespuestaAntiguos ){
									 obj = new ConceptoDTO();
									 String nombreAux = laa.getId().getArchivoAdjunto().replace("\\",";");
									 String[] arreglo = nombreAux.split(";");
									 int cant = arreglo.length;
									 if(cant > 0){
										 obj.setNombreArchivo(arreglo[cant-1]);
										 String urlAux = globalParameters.getAttachedFilesWebPath()+""+arreglo[cant-3]+"/"+arreglo[cant-2]+"/"+arreglo[cant-1];
										 log.debug("url transformada  #0", urlAux);
										 obj.setRutaCompleta(urlAux);
										 if(listaArchivoGuardado == null || listaArchivoGuardado.size() == 0){
											 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
											 listaArchivoGuardado.add(obj);
										 }else{
											 listaArchivoGuardado.add(obj);
										 }
									 }
								 }
							 }	
						
					   this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));								 
						 
					 }
				}
		}catch (Exception e) {
			log.error("Error, al eveluar la solicitud #0", e.getMessage());
			
		}
	}
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
//	public void evaludar(LcredSolicitud solicitud , String pagina){
//		try{
//				if(solicitud != null){
//					if(pagina != null){
//						setPaginaVolver(pagina);
//					}
//					
//					setSolicitudSeleccionada(solicitud.getId().getNumSolicitud());
//					cambioEvaluacionSolicitud(solicitud);
//					insertarLogs(solicitud,1);
//					
//					cliente = new ClienteDTO();
//					sacarListas();
//					log.debug("Sucursal:"+ solicitud.getSucursalEmisor());
//					obtenerSucursal(solicitud.getSucursalEmisor());
//					
//					log.debug("codigo de emisor #0", solicitud.getCodEmisor());
//					UsuarioSegurDTO user = scoringService.sacarDatosSessionUsuario(solicitud.getCodEmisor());
//					if(user != null){
//						this.setUsuarioEmisor(user.getNombre());
//					}else{
//						this.setUsuarioEmisor(solicitud.getCodEmisor());
//					}					
//					
//					/*setear los datos de cliente*/
//					cliente.setRut(solicitud.getRutCliente());
//					cliente.setRazonSocial(solicitud.getNomCliente());
//					cliente.setGiro(solicitud.getGiroCliente());
//					cliente.setDireccion(solicitud.getDirCliente());
//					cliente.setFono(solicitud.getTelCliente());
//					cliente.setFax(solicitud.getFaxCliente());
//					cliente.setComuna(solicitud.getComCliente());
//					cliente.setCiudad(solicitud.getCiuCliente());
//					cliente.setSucursalCliente(sucursal.getDescripcion());
//					cliente.setCanalVenta(solicitud.getCanal());
//					cliente.setTipoCliente(solicitud.getTipoCliente());
//					cliente.setLineaCreditoKH(solicitud.getLinCredito());
//					cliente.setLineaCreditoKHUtilizado(solicitud.getLinCreditoUtiliz());
//					cliente.setLineaCreditoKHDisponible(solicitud.getLinCreditoDisp());
//					cliente.setEstadoLineaCreditoKH(solicitud.getEstadoLcKh());
//					cliente.setNumeroRV(solicitud.getNroRv().toString());
//					cliente.setSeguro(solicitud.getTipSeguro());
//					cliente.setEstadoLineaEnCuotas(solicitud.getEstadoLcCuotas());
//					cliente.setCodigoCondicionPago(solicitud.getConPago());
//					cliente.setCodigoclasificacionRiesgo(solicitud.getClsRiesgo());
//					cliente.setVentasTotal12Meses(solicitud.getVtaTotal12Meses());
//					cliente.setCreacionSap(solicitud.getId().getFecSolicitud());	
//					cliente.setDmVentasUno(solicitud.getDmVenta());
//					cliente.setPrmFacturas(solicitud.getPrmFacturas());
//					cliente.setCantMesesVentas(solicitud.getMesesVentas().longValue());
//					cliente.setVentasProm12Meses(solicitud.getPrmVentas());
//					cliente.setVigenciaSeguro(solicitud.getVigSeguro());
//					cliente.setFactProm12Meses(solicitud.getPrmFacturas().longValue());
//					
//					lcredEstado = ObtenerEstadoSolictud(solicitud.getEstado());
//					if(lcredEstado != null){
//						cliente.setEstadoSolicitud(lcredEstado.getDesEstado());
//						this.setModalAnalista(solicitud.getEstado());
//					}else{
//						cliente.setEstadoSolicitud(solicitud.getEstado());
//					}
//					
//					cliente.setMontoSeguro(solicitud.getSeguroPesos());
//					cliente.setMontoSeguroUf(solicitud.getSeguroUf().longValue());
//					
//					try{
//						if(cliente.getCodigoCondicionPago()!=null){
//							Object obj = intranetSapService.sacarDescripCondicionPago(cliente.getCodigoCondicionPago());
//							if(obj != null){
//								cliente.setCondicionPago(obj.toString());
//							}else{
//								cliente.setCondicionPago("No Clasificado");
//							}
//						}else{cliente.setCondicionPago("No Clasificado");}
//						
//					}catch (Exception e) {
//						cliente.setCondicionPago("No Clasificado");
//					}
//
//					try{
//						if(cliente.getCodigoclasificacionRiesgo() != null){
//							Object obj = intranetSapService.sacarDescripClasificacionRiesgo(cliente.getCodigoclasificacionRiesgo());
//							if(obj != null){
//								cliente.setClasificacionRiesgo(obj.toString());
//							}else{
//								cliente.setClasificacionRiesgo("No Clasificado");
//							}
//						}else{cliente.setClasificacionRiesgo("No Clasificado");}
//						
//					}catch (Exception e) {
//						cliente.setClasificacionRiesgo("No Clasificado");
//					}
//					
//					String codigoCanal = solicitud.getCanal();
//					if(codigoCanal != null){
//						String kx = TipoCuentasKupferType.KX.getNombre().trim();
//						String gc = TipoCuentasKupferType.GC.getNombre().trim();
//						String mx = TipoCuentasKupferType.MX.getNombre().trim();
//						if(kx.equals(codigoCanal.trim())){
//							cliente.setCodigoCanal(TipoCuentasKupferType.KX);
//						}
//						if(gc.equals(codigoCanal.trim())){
//							cliente.setCodigoCanal(TipoCuentasKupferType.GC);
//						}
//						if(mx.equals(codigoCanal.trim())){
//							cliente.setCodigoCanal(TipoCuentasKupferType.MX);
//						}
//					}
//					
//					
//					/*setar los datos de comportamiento comercial*/
//					comportamiento = new CompComercialDTO();
//					comportamiento.setDiasMoras(solicitud.getDiasMora());
//					comportamiento.setDmPort(solicitud.getDm().trim());
//					comportamiento.setMontoTotalMoro(solicitud.getMorosidadMonto().longValue());
//					comportamiento.setMontoTotalProt(solicitud.getProtestosMonto().longValue());
//					comportamiento.setNumeroMoro(solicitud.getMorosidadNro());
//					comportamiento.setNumeroProt(solicitud.getProtestosNro());
//					
//					/*setar los datos del exposicion de riesgo kupfer*/
//					expoRiesgoKh = new ExpoRiesgoKhDTO();
//					expoRiesgoKh.setPeakCredito(solicitud.getPeackCredito().longValue());
//					expoRiesgoKh.setMontoRiesgoKupfer(solicitud.getRiesgoKupfer().longValue());
//					long montoRiegoKupferPedidoPorceso = solicitud.getRiesgoKupfer().longValue() + solicitud.getMontoCuotas().longValue();
//					expoRiesgoKh.setMontoRiesgoKupferPedidoProceso(montoRiegoKupferPedidoPorceso);
//					expoRiesgoKh.setMontoPedidoProceso(solicitud.getMontoCuotas().longValue());
//					long montoPeakCreditoPedidoProceso = solicitud.getPeackCredito().longValue() + solicitud.getMontoCuotas().longValue();
//					expoRiesgoKh.setMontoPeakCreditoPedidoProceso(montoPeakCreditoPedidoProceso);
//					expoRiesgoKh.setMontoAsegurado(solicitud.getMontoAsegurado().longValue());
//					this.setObservacionesFinales(null);
//					this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
//					this.listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
//
//
//					
//					log.debug("solicitud.getTipTiposol() : #0", solicitud.getTipTiposol());
//					 if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.V1N.getNombre()) || 
//					    (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.V1C.getNombre())){
//							try{ 
//								 venta = (LcredSolicitudVtapuntual)entityManager
//										 .createQuery("Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
//										 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getSingleResult();
//							}catch (Exception e) {
//								log.debug("no existe datos de venta puntual");
//							}
//							 
//							 this.setTituloIngreso(solicitud.getDesTiposol());
//							 
//							 if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.V1N.getNombre())){
//								 this.setPaginaIngreso(11);
//							 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.V1C.getNombre())){
//								 this.setPaginaIngreso(12);
//							 }
//							 
//							 if(venta != null){
//									this.setMontoVtaPuntual(venta.getMontoInicial().longValue());
//									this.setPieVentaPuntual(venta.getMontoPieInicial().longValue());
//									this.setProductos(venta.getDcProducto());
//									this.setObservacionesCompra("");
//									this.setMotivoCompra(venta.getDcMotivo());
//									this.setFormaPago(venta.getObservacionesInicial());
//									try{
//										String margenAux = venta.getDcMargen().replace(",", ".");
//										String margennuevo = margenAux.replace("%", "");
//										Double margen = Double.parseDouble(margennuevo);
//										this.setMargenGlogal(margen);
//									}catch (Exception e) {
//										log.error("Error al transformar el margen global #0", e.getMessage());
//										this.setMargenGlogal((double)0);
//									}
//									
//									/*setar los valores al los datos*/
//									if(venta.getMontoFinal() != null){
//										this.setMontoNuevo(venta.getMontoFinal().longValue());
//									}else{
//										this.setMontoNuevo(venta.getMontoInicial().longValue());
//									}
//									if(venta.getMontoPieFinal() != null){
//										this.setPieNuevo(venta.getMontoPieFinal().longValue());
//									}else{
//										this.setPieNuevo(venta.getMontoPieInicial().longValue());
//									}
//									
//										
//									if(this.paginaIngreso != 0){
//										this.normalCuota = String.valueOf(this.paginaIngreso);
//									}
//									this.setObservacionesCompra(solicitud.getGlsObservaciones());
//									this.setObservacionesFinales(venta.getObservacionesFinal());
//									
//								 }
//							 
//							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//							 if(listaArchivos != null){
//								 ConceptoDTO obj = null;
//								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//								 for(ArchivoAdjunto aa : listaArchivos ){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//									 listaArchivoGuardado.add(obj);
//								 }
//							 }
//							 
//							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
//							 if(listaArchivosRespuesta != null){
//								 ConceptoDTO obj = null;
//								 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
//								 for(ArchivoAdjunto aa : listaArchivosRespuesta){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//									 listaArchivoGuardadoRespuesta.add(obj);
//								 }
//							 }
//							 
//							 
//							 
//							 
//							 
//							 this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
//							 
//							/*va a buscar los destinatarios de las solictudes*/
//							 obtenerCorreo(solicitud.getCodEmisor());
//
//							 this.modificarVenta = false;
//							 
//
//					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC1.getNombre()) || (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC2.getNombre()) ||
//							 (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC3.getNombre())){
//							
//						 	this.setTituloIngreso(solicitud.getDesTiposol());
//						    try{ 
//								 credito = (LcredSolicitudLcredito)entityManager
//										 .createQuery("Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
//										 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getSingleResult();
//							 }catch (Exception e) {
//								log.debug("no existe datos de linea credito.");
//							 }
//							 
//							 this.setTituloIngreso(solicitud.getDesTiposol());
//							 if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC1.getNombre())){
//								 this.setPaginaIngreso(21);
//							 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC2.getNombre())){
//								 this.setPaginaIngreso(22);
//							 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC3.getNombre())){
//								 this.setPaginaIngreso(23);
//							 }
//
//							 this.setInnNomRiesgo(String.valueOf(this.paginaIngreso));
//							 
//							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//							 if(listaArchivos != null){
//								 ConceptoDTO obj = null;
//								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//								 for(ArchivoAdjunto aa : listaArchivos ){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//									 listaArchivoGuardado.add(obj);
//								 }
//							 }
//
//							 
//							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
//							 if(listaArchivosRespuesta != null){
//								 ConceptoDTO obj = null;
//								 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
//								 for(ArchivoAdjunto aa : listaArchivosRespuesta){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//									 listaArchivoGuardadoRespuesta.add(obj);
//								 }
//							 }							
//							
//							 
//							 this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
//						
//						
//							/*sacando condiciones de riesgo y de pago */
//							try{
//								if(listaClsRiesgos == null || listaClsRiesgos.size() == 0){
//									listaClsRiesgos = intranetSapService.getClasificacionRiesgo();
//								}
//								if(listaCondicionPagos == null || listaCondicionPagos.size() == 0){
//									listaCondicionPagos = intranetSapService.getCondicionPago();
//								}							
//							}catch (Exception e) {
//								log.error("Error, al obtener los datos de lista risgo y pago #0", e.getMessage());
//							}
//							
//							if(credito != null){
//								if(credito.getCondPagoInicial() != null && credito.getCondRiesgoInicial() != null){
//									evaluarInnominalNominalRKupfer( credito.getCondRiesgoInicial(),credito.getCondPagoInicial());
//								}
//								if(credito.getCondPagoFinal() != null && credito.getCondRiesgoFinal() != null){
//									evaluarInnominalNominalRKupferFinales( credito.getCondRiesgoFinal(),credito.getCondPagoFinal());
//								}								
//								
//								this.setDescripcionProyecto(credito.getDpDescripcionProyecto());
//								String montoAux = credito.getDpMonto().replace(".", "");
//								this.setMontoCredito(Long.parseLong(montoAux));
//								this.setPlazoEjecucion(credito.getDpPlazoEjecucion());
//								this.setConceptoNegocios(credito.getDpConceptosInvoluc());
//								this.setRutNombresSocios(credito.getDpSocios());
//								String montoPotencial = credito.getDpPotencialCompra().replace(".", "");
//								this.setPotencialCompra(Long.parseLong(montoPotencial));
//								this.setObservacionesCredito(credito.getObservacionesInicial());
//								this.setMontoLineaCreditoNuevo(credito.getMontoNormalFinal().longValue());
//								this.setObservacionesFinales(credito.getObservacionesFinal());
//								
//							}
//							this.setMontoLineaCredito(solicitud.getMonto().longValue());
//							/*va a buscar los destinatarios de las solictudes*/
//							 obtenerCorreo(solicitud.getCodEmisor());
//						 
//					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.CR1.getNombre()) || (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.CR2.getNombre()) ||
//							 (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.CRP3.getNombre()) ||  (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.CRP4.getNombre())){
//						 	
//						 	this.setTituloIngreso(solicitud.getDesTiposol());
//						 	
//						 	this.comboCPago = true;
//						 	this.comboCRiesgo = true;
//						  try{ 
//							  condicion = (LcredSolicitudCondiciones)entityManager
//									 .createQuery("Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
//									 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getSingleResult();
//						  }catch (Exception e) {
//							log.debug("no existe datos de condiciones.");
//						  }		
//						  List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//						  if(listaArchivos != null){
//							 ConceptoDTO obj = null;
//							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//							 for(ArchivoAdjunto aa : listaArchivos ){
//								 obj = new ConceptoDTO();
//								 obj.setNombreArchivo(aa.getNombreAdjunto());
//								 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//								 listaArchivoGuardado.add(obj);
//							 }
//						  }						  
//							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
//							 if(listaArchivosRespuesta != null){
//								 ConceptoDTO obj = null;
//								 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
//								 for(ArchivoAdjunto aa : listaArchivosRespuesta){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//									 listaArchivoGuardadoRespuesta.add(obj);
//								 }
//							 }								  
//						  
//						  if(condicion != null ){
//							  setPaginaIngreso(31);
//								/*sacando condiciones de riesgo y de pago */
//								try{
//									if(listaClsRiesgos == null || listaClsRiesgos.size() == 0){
//										listaClsRiesgos = intranetSapService.getClasificacionRiesgo();
//									}
//									if(listaCondicionPagos == null || listaCondicionPagos.size() == 0){
//										listaCondicionPagos = intranetSapService.getCondicionPago();
//									}							
//								}catch (Exception e) {
//									log.error("Error, al obtener los datos de lista risgo y pago #0", e.getMessage());
//								}
//							 
//								if(condicion.getCodCondPagoInicial() != null && condicion.getCodCondRiesgoInicial() != null){
//									evaluarInnominalNominalRKupfer(condicion.getCodCondRiesgoInicial(),condicion.getCodCondPagoInicial());
//								}
//
//								if((condicion.getCondRiesgoInicial().toString()).equals("S")){
//									this.condicionRiesgo = true;
//								}
//								if((condicion.getCondPagoInicial().toString()).equals("S")){
//									this.condicionPago = true;
//								}
//								
//								if(condicion.getMotivoCambio() != null){
//									this.setMotivoRiesgoPago(condicion.getMotivoCambio());
//								}
//								this.setObservacionesFinales(condicion.getObservacionesFinal());
//							  
//						  }
//
//						  this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
//						  
//							/*va a buscar los destinatarios de las solictudes*/
//							 obtenerCorreo(solicitud.getCodEmisor());						 
//						  
//					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS1.getNombre()) || (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS2.getNombre()) || 
//							 (solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS.getNombre())){
//						 	
//						 	try{
//								 bloqueo = (LcredSolicitudBloqueos)entityManager
//										 .createQuery("Select sb from LcredSolicitudBloqueos sb where sb.numSolicitud=:solAux")
//										 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getSingleResult();
//							}catch (Exception e) {
//								log.debug("no existe datos de bloqueo o desbloqueo.");
//							}							 
//						 
//						 	
//						 	solicitudOtra = scoringService.getSolicitudOtrasId(solicitud.getId().getNumSolicitud());
//						 	
//						 	this.setTituloIngreso(solicitud.getDesTiposol());
//						 	
//							if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS1.getNombre())){
//								this.setOpcionBloqueoDesbloqueo("41");
//							}else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS2.getNombre())){
//								this.setOpcionBloqueoDesbloqueo("42");
//							}else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS.getNombre())){
//								this.setOpcionBloqueoDesbloqueo("");
//							}						 
//						 
//							if(solicitudOtra  != null && bloqueo == null){
//								 this.setPaginaIngreso(41);
//								 this.setMotivoBloDesbloqueo(solicitudOtra.getObservacionesInicial());
//							}else if(solicitudOtra  == null && bloqueo != null){
//								 this.setPaginaIngreso(41);
//								 this.setMotivoBloDesbloqueo(bloqueo.getMotivoBloqDesbloq());
//							}else if(solicitudOtra  != null && bloqueo != null){
//								 this.setPaginaIngreso(41);
//								 this.setMotivoBloDesbloqueo(solicitudOtra.getObservacionesInicial());								
//							}
//							
//							
//							List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//							if(listaArchivos != null){
//								 ConceptoDTO obj = null;
//								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//								 for(ArchivoAdjunto aa : listaArchivos ){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//									 listaArchivoGuardado.add(obj);
//								 }
//							 }						  
//						 
//							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
//							 if(listaArchivosRespuesta != null){
//								 ConceptoDTO obj = null;
//								 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
//								 for(ArchivoAdjunto aa : listaArchivosRespuesta){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//									 listaArchivoGuardadoRespuesta.add(obj);
//								 }
//							 }									
//						   this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
//						 
//							/*va a buscar los destinatarios de las solictudes*/
//							 obtenerCorreo(solicitud.getCodEmisor());
//						   
//						 
//					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS3.getNombre())){
//						 try{
//							   listaDms = (List<LcredSolicitudDm>)entityManager
//									 .createQuery("Select sdm from LcredSolicitudDm sdm where sdm.numSolicitud=:solAux")
//									 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getResultList();
//							   if(listaDms != null){
//								   
//								  this.setListaClienteDMs(listaDms);
//							   }
//								   
//								   
//						}catch (Exception e) {
//							log.debug("no existe datos de los dm.");
//						}		
//						 
//						 solicitudOtra = scoringService.getSolicitudOtrasId(solicitud.getId().getNumSolicitud());
//						 if(solicitudOtra != null){
//							 log.debug("Existe resgistro en otra solicitud ");
//							 this.setObservacionesFinales(solicitudOtra.getObservacionesFinal());
//						 }
//						 
//						 
//					 	this.setTituloIngreso(solicitud.getDesTiposol());
//					 	
//						if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS3.getNombre())){
//							this.setPaginaIngreso(43);
//							this.setMotivoProrroga(solicitud.getObservSolicitud());
//						}	
//						List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//						if(listaArchivos != null){
//							 ConceptoDTO obj = null;
//							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//							 for(ArchivoAdjunto aa : listaArchivos ){
//								 obj = new ConceptoDTO();
//								 obj.setNombreArchivo(aa.getNombreAdjunto());
//								 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//								 listaArchivoGuardado.add(obj);
//							 }
//						 }						  
//					 
//						 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
//						 if(listaArchivosRespuesta != null){
//							 ConceptoDTO obj = null;
//							 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
//							 for(ArchivoAdjunto aa : listaArchivosRespuesta){
//								 obj = new ConceptoDTO();
//								 obj.setNombreArchivo(aa.getNombreAdjunto());
//								 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//								 listaArchivoGuardadoRespuesta.add(obj);
//							 }
//						 }								
//					   this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));						
//						 
//							/*va a buscar los destinatarios de las solictudes*/
//					   obtenerCorreo(solicitud.getCodEmisor());
//						 
//					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS4.getNombre())){
//						 try{
//							 listaProrrogas = (List<LcredSolicitudProrroga>)entityManager
//									 .createQuery("Select lp from LcredSolicitudProrroga lp where lp.id.numSolicitud=:solAux")
//									 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getResultList();						 
//						}catch (Exception e) {
//							log.debug("no existe datos de las prorrogas dm.");
//						}	
//						 
//						 solicitudOtra = scoringService.getSolicitudOtrasId(solicitud.getId().getNumSolicitud());
//						 if(solicitudOtra != null){
//							 log.debug("Existe resgistro en otra solicitud ");
//							 this.setObservacionesFinales(solicitudOtra.getObservacionesFinal());
//						 }						 
//						 
//					 	this.setTituloIngreso(solicitud.getDesTiposol());
//					 	
//						if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS4.getNombre())){
//							this.setPaginaIngreso(44);
//						}	
//						List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//						if(listaArchivos != null){
//							 ConceptoDTO obj = null;
//							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//							 for(ArchivoAdjunto aa : listaArchivos ){
//								 obj = new ConceptoDTO();
//								 obj.setNombreArchivo(aa.getNombreAdjunto());
//								 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//								 listaArchivoGuardado.add(obj);
//							 }
//						 }			
//						 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
//						 if(listaArchivosRespuesta != null){
//							 ConceptoDTO obj = null;
//							 listaArchivoGuardadoRespuesta = new ArrayList<ConceptoDTO>(0);
//							 for(ArchivoAdjunto aa : listaArchivosRespuesta){
//								 obj = new ConceptoDTO();
//								 obj.setNombreArchivo(aa.getNombreAdjunto());
//								 obj.setRutaCompleta(globalParameters.getAttachedFilesAbsolutePath()+""+aa.getUrl());
//								 listaArchivoGuardadoRespuesta.add(obj);
//							 }
//						 }								
//					 
//					   this.tipoSolicitudAux = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));								 
//						 
//						 
//						 
//						 /*va a buscar los destinatarios de las solictudes*/
//						 obtenerCorreo(solicitud.getCodEmisor());
//					 }
//				}
//		}catch (Exception e) {
//			log.error("Error, al eveluar la solictud #0", e.getMessage());
//		}
//	}

	public void solicitudSeleccionada(){
		try{
			try{
				if(idSolicitud != null){
					solicitud = entityManager.find(LcredSolicitud.class, idSolicitud);
				}

			}catch (Exception e) {
				log.error("Error, al sacar la solicitud seleccionada", e.getMessage());
			}

		}catch (Exception e) {
			log.error("Error, al sacar la solicitud seleccionada", e.getMessage());
		}
	}
	public void obtenerSucursal(String codigo){
		if(listaSucursales != null){
			for(Sucursal s : listaSucursales){
				if(s.getCodigo().equals(codigo)){
					setSucursal(s);
					sucursal = s;
					break;
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void sacarListas(){
		try{
			listaSucursales = (List<Sucursal>)entityManager.createQuery("select suc from Sucursal suc ").getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de sucursales #0", e.getMessage());
		}
	}
	public LcredEstado ObtenerEstadoSolictud(String codigo){
		LcredEstado estado = null;
		try{
			estado = (LcredEstado)entityManager.find(LcredEstado.class, codigo);
		}catch (Exception e) {
			log.error("Error, al sacar la lista de sucursales #0", e.getMessage());
			estado = null;
		}
		return estado;
	}
	public void insertHitos(LcredSolicitud solAux, LcredEstado estadoAux){
		try{
			SolicitudHitos hitos = new SolicitudHitos();
			hitos.setEmisor(usuarioSegur.getAlias());
			hitos.setIdSolicitud(solAux.getId().getNumSolicitud());
			hitos.setUsuarioActual(usuarioSegur.getAlias());
			hitos.setFechaHora(new Date());
			hitos.setCodigoEstado(estadoAux.getCodEstado());
			hitos.setDescripcionEstado(estadoAux.getDesEstado());
			try{
				long numero = scoringService.obtenerNumeroHitoToLog(1);
				hitos.setSystemId(numero);
				boolean exito = scoringService.persistSolicitudHitos(hitos);
				log.debug("verificacion si inserto registro hitos #0", exito);
				
			}catch (Exception e) {
				log.error("Error, al insertar el hitos de la solicitud #0", e.getMessage());
			}				
			
		}catch (Exception e) {
			log.error("Error, al insertar los hitos de respuesta.", e.getMessage());
		}
		
	}
	
	
	public void insertarLogs(LcredSolicitud sol, int opcion){
		/*ingreso de logs*/
		try{
			if(opcion == 1){/*solo esta revisando la solicitud*/
				String codigo = "Z";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Revisando la solicitud. ");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Revisando la solicitud. ");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);
			}else if(opcion == 2){/*aca se esta daando aprobaciones */
				String codigo = "A";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Se da aprobación la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Se da aprobación la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);
				
				
			}else if(opcion == 3){/*aca se cambia elestado salir sin modifica */
				String codigo = "X";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Salir de la solicitud sin modificar.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Se da aprobación la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);				
				
			}else if(opcion == 4){/*aca se cambia el estado salir con modifica */
				String codigo = "Y";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Salir de la solicitud con modificar.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Salir de la solicitud con modificar.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);				
				
			}else if(opcion == 5){
				String codigo = "O";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Ingresa de observaciones de  la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Ingresa de observaciones de  la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);					
			}else if(opcion == 6){
				String codigo = "D";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Devolución de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Devolución de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);					
			}else if(opcion == 7){
				String codigo = "N";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Anular de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Anular de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);					
			}else if(opcion == 8){
				String codigo = "A";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Aprobar de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Aprobar de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);					
			}else if(opcion == 9){
				String codigo = "K";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Analisis Ejecutivo de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Analisis Ejecutivo de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);					
			}else if(opcion == 10){
				String codigo = "Q";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Analisis Analista de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Analisis Analista de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);					
			}
			
			
			
		}catch (Exception e) {
			log.error("Error, al insertar el logs de la solicitud #0", e.getMessage());
		}
	}
	
	public void salir(){
	  try{
		if(solicitudSeleccionada != null){
			solicitud = (LcredSolicitud)entityManager.
					createQuery("select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud")
					.setParameter("numSolicitud", solicitudSeleccionada).getSingleResult();
			
			if(solicitud.getEstado().equals("I")){
				insertarLogs(solicitud,3);
				entityManager.createQuery("update LcredSolicitud set evaluar=0, usuarioActual=null  where id.numSolicitud=:sol")
				             .setParameter("sol", solicitud.getId().getNumSolicitud()).executeUpdate();
				entityManager.flush();
			}else if(solicitud.getEstado().equals("E")  || solicitud.getEstado().equals("N")) {
				insertarLogs(solicitud,3);
				entityManager.createQuery("update LcredSolicitud set evaluar=1 where id.numSolicitud=:sol")
							 .setParameter("sol", solicitud.getId().getNumSolicitud()).executeUpdate();
				entityManager.flush();
			}else{
				insertarLogs(solicitud,4);
				entityManager.createQuery("update LcredSolicitud set evaluar=0 where id.numSolicitud=:sol")
							 .setParameter("sol", solicitud.getId().getNumSolicitud()).executeUpdate();
				entityManager.flush();				
			}
		}
	  }catch (Exception e) {
		log.error("error al devolverse y cambiar el estado de la evalucion. #0", e.getMessage());
	}	
		
	}
	
	public void cambioEvaluacionSolicitud(LcredSolicitud sol){
		try{
			if(sol != null){
				entityManager.createQuery("update LcredSolicitud set evaluar=1 where id.numSolicitud=:sol")
				             .setParameter("sol",  sol.getId()
				             .getNumSolicitud())
				             .executeUpdate();
				entityManager.flush();
			}
			
		}catch (Exception e) {
			log.error("Error al cambiar esl estado de la evaluacion.");
		}
		
		
	}
	
	/*** Obtencion datos cuenta corriente***/
	private List <DeudaActual> ctaCteList = new ArrayList<DeudaActual>();
	public List<DeudaActual> getCtaCteList() {	return ctaCteList;	}
	
	public void informacionCtaCte(){
		/*limpiar la lista */
		ctaCteList = new ArrayList<DeudaActual>(0);
		this.setFechaDeauda(null);
		
		/*fecha Actual*/
		setFechaActualCtaCte(new Date());
		
		SapSystem system = new SapSystem(globalParameters.getNameSap(),
				globalParameters.getIpSap(), globalParameters.getClientSap(), 
				globalParameters.getSystemNumberSap(),globalParameters.getUserSap(),
				globalParameters.getPasswordSap()); 

		Connect connect = new Connect(system);
		functionCtaCte = connect.getFunction("ZFIFN_CTACTE"); // Nombre RFC
		functionCtaCte.getImportParameterList().setValue("CODCLIENTE", cliente.getCleanRut().toUpperCase()); // Paso de parametros
		connect.execute(functionCtaCte);
		JCoTable datosCtaCte = functionCtaCte.getTableParameterList().getTable("DETALLE"); //tabla de salida
		log.debug(cliente.getCleanRut().toUpperCase());
		log.debug(datosCtaCte);
		
		for (int i = 0; i < datosCtaCte.getNumRows(); i++){
			DeudaActual ctaCte = new DeudaActual();
			datosCtaCte.setRow(i);
			ctaCte.setRut(cliente.getCleanRut().toUpperCase());
			ctaCte.setDctoContable(datosCtaCte.getValue("DOCTO_CONTABLE").toString());
			ctaCte.setFechaCont(datosCtaCte.getDate("FECHA_CONTABLE"));
			ctaCte.setClaseDoc(datosCtaCte.getValue("CLASE_DOCTO").toString());
			ctaCte.setNumDcto(datosCtaCte.getValue("NUM_DOCTO").toString());
			ctaCte.setNumFactura(datosCtaCte.getValue("NUM_FACTURA").toString());
			ctaCte.setFechaVcto(datosCtaCte.getDate("FEC_VCTO"));
			ctaCte.setIndCME(datosCtaCte.getValue("IND_CME").toString());
			ctaCte.setReferencia(datosCtaCte.getValue("REFERENCIA").toString());
			ctaCte.setMonto(((BigDecimal)datosCtaCte.getValue("MTO_DOCTO")).longValue());
			ctaCte.setCodCliente(datosCtaCte.getValue("CODCLIENTE").toString());
			ctaCte.setTexto(datosCtaCte.getValue("TEXTO").toString());
			ctaCte.setDiasMora(((BigDecimal)datosCtaCte.getValue("DIASMORA")).intValue());
			ctaCte.setFechaIngreso(new Date());
			ctaCteList.add(ctaCte);	
		}
		
		if(listaFechas == null){
			listaFechas = scoringService.getFechasCuentaCorriente(cliente.getCleanRut().toUpperCase());
		}
		
		
	}
	
	public void limpiarDatos(){
		if(ctaCteList != null){
			ctaCteList.clear();

		}
	}

	public void consultarInformacionCtaCteFechaDeterminada(){
		/*fecha Actual*/
		if(this.fechaDeauda == null){
			FacesMessages.instance().add(Severity.WARN,"Debe seleccionar una fecha menos o igual a la actual.");
			return;
		}else{
			this.setFechaActualCtaCte(null);
			log.error("la fecha seleccionada es #0", this.fechaDeauda);
		}
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
		Date fecha = null;
		try {
			 fecha = formatoDelTexto.parse(this.fechaDeauda);
		} catch (ParseException ex) {
			ex.printStackTrace();
			return;
		}		
		
		
		List<DeudaActual> listaDeudas = scoringService.getConsultarHistorialCuentaCorriente(cliente.getCleanRut().toUpperCase(), fecha);
		if(listaDeudas != null && listaDeudas.size() >0){
			ctaCteList = listaDeudas;
		}else{
			if(ctaCteList != null){
				ctaCteList = null;
			}
		}
		
	}	
	public void guardarInformacionCtaCte(){
		try{
			boolean ingresar = scoringService.getVerificarHistorialCuentaCorriente(cliente.getCleanRut().toUpperCase(), new Date());
			if(ingresar ==  false){
				if(ctaCteList != null && ctaCteList.size() > 0){
					for(DeudaActual ctaCte :ctaCteList){
						entityManager.persist(ctaCte);
					}
					entityManager.flush();
				}
				FacesMessages.instance().add(Severity.INFO,"Se guardo la información actual de la cuenta corriente con fecha actual.");
			}else{
				FacesMessages.instance().add(Severity.WARN,"Ya existe registros con la fecha actual.");
			}
			
		}catch (Exception e) {
			log.error("Error al guardar registro de la cuenta corriente del cliente #0", e.getMessage());
			FacesMessages.instance().add(Severity.ERROR,"Error al ingresar los registro de la cuenta corriente.");
		}
	}
	public void limpiarInformacionCtaCteFechaDeterminada(){
		try{
			this.setFechaDeauda(null);
			informacionCtaCte();
			
		}catch (Exception e) {
			log.error("error al limpiar la grilla y actualizar nuevamente ", e.getMessage());
		}
		
	}
	public void ingresoObservaciones(){
		if(this.solicitud != null && this.tipoSolicitudAux != null){
			log.debug("solicitud #0 tipo de solicitud #1", solicitud.getId().getNumSolicitud(), tipoSolicitudAux.getCodTipoSolicitud());
			this.observacionesModel = null;
			consultarObservacion();
		}
		
		
	}
	public void gnuardarObservaciones(){
		try{
			if(this.observacionesModel == null){
				FacesMessages.instance().add(Severity.ERROR,"Debe ingresar una observación." );
				return;					
			}else{
				if("".equals(this.observacionesModel)){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar una observación." );
					return;					
				}
			}

			LcredSolicitudObservacionesId id = new LcredSolicitudObservacionesId();
			Long correlativo = scoringService.obtenerCorrelativoObservaciones(solicitud.getId().getNumSolicitud());
			if(correlativo != null){
				id.setCorrelativo(correlativo);
				id.setFecha(new Date());
				id.setHora(new Date());
				id.setNumSolicitud(solicitud.getId().getNumSolicitud());
				id.setTipoSol(tipoSolicitudAux.getCodTipoSolicitud());
				id.setObservacion(this.observacionesModel);
				id.setUsuario(usuarioSegur.getAlias());
				LcredSolicitudObservaciones obser = new LcredSolicitudObservaciones();
				obser.setId(id);
				scoringService.persistSolicitudObservaciones(obser);
				insertarLogs(solicitud, 5);
			}
			this.observacionesModel = null;
		}catch (Exception e) {
			log.error("error, al guardar la observaciones #0", e.getMessage());
		}
		
		consultarObservacion();
	}
	public void consultarObservacion(){
		try{
			if(this.solicitud != null){
				List<LcredSolicitudObservaciones> lista = scoringService.obtenerListaSolicitudObservaciones(this.solicitud.getId().getNumSolicitud());
				if(lista != null){
					this.setListaObservaciones(lista);
//					this.listaObservaciones = new ArrayList<LcredSolicitudObservaciones>();
//					for(LcredSolicitudObservaciones obs : lista){
//						String cadena = obs.getId().getObservacion();
//						if(cadena != null && cadena.length() < 170){
//							String[] vsplit = cadena.split(" ");
//							if(vsplit != null && vsplit.length > 1){
//								this.listaObservaciones.add(obs);
//							}else if(vsplit != null && vsplit.length == 1){
//								StringBuffer cadenaNueva = new StringBuffer();
//								Double cant = Double.parseDouble(String.valueOf(cadena.length()))/30;
//								int parteEntera = (int)cant.doubleValue();
//								Double decimal = cant - parteEntera;
//								if(decimal >= 0.5){
//									parteEntera++;
//								}
//								int comienzo = 0;
//								int limite = 30;
//								for(int i=0; i<parteEntera;i++){
//									
//									if((i+1)<parteEntera){
//										cadenaNueva.append(cadena.substring(comienzo, limite)).append(" ");
//									}
//									if(i == (parteEntera -1)){
//										cadenaNueva.append(cadena.substring(comienzo, cadena.length())).append(" ");
//									}
//									comienzo = (limite + 1);
//									limite = (comienzo - 1) + 30;
//								}
//								
//								if(!(cadenaNueva.toString()).equals("")){
//									obs.getId().setObservacion(cadenaNueva.toString());
//								}else{
//									obs.getId().setObservacion(cadena.toString());
//								}
//								
//								
//								this.listaObservaciones.add(obs);
//							}
//						}else{
//							StringBuffer cadenaNueva = new StringBuffer();
//							String[] cadenaSplit = obs.getId().getObservacion().split(" ");
//							for(String frase : cadenaSplit){
//								if(frase != null && frase.length() < 30){
//									cadenaNueva.append(frase).append(" ");
//								}else{
//									Double cant = Double.parseDouble(String.valueOf(frase.length()))/30;
//									int parteEntera = (int)cant.doubleValue();
//									Double decimal = cant - parteEntera;
//									if(decimal >= 0.5){
//										parteEntera++;
//									}
//									int comienzo = 0;
//									int limite = 30;
//									for(int i=0; i<parteEntera;i++){
//										
//										if((i+1)<parteEntera){
//											cadenaNueva.append(frase.substring(comienzo, limite)).append(" ");
//										}
//										if(i == (parteEntera -1)){
//											cadenaNueva.append(frase.substring(comienzo, frase.length())).append(" ");
//										}
//										comienzo = (limite + 1);
//										limite = (comienzo - 1) + 30;
//									}
//									obs.getId().setObservacion(cadenaNueva.toString());
//								}
//							}
//						  this.listaObservaciones.add(obs);
//						}
//					}
				}
			}
		}catch (Exception e) {
			log.error("Error al sacar los aobservacioes de la solicitud selecionadas #0",e.getMessage());
		}
		
	}
	public void limpiarObservaciones(){
		this.listaObservaciones = null;
		this.observacionesModel = null;
		
	}
	public void ingresoDevolucion(){
		if(this.solicitud != null && this.tipoSolicitudAux != null){
			log.debug("solicitud #0 tipo de solicitud #1", solicitud.getId().getNumSolicitud(), tipoSolicitudAux.getCodTipoSolicitud());
			if(solicitud != null){
				if(solicitud.getEstado().equals("DR") || solicitud.getEstado().equals("DC")){
					this.mensajeGlogal = "La solicitud fue devuelta al remitente para correción.";
					return;
				}else if(!solicitud.getEstado().equals("I")){
					this.mensajeGlogal = "La solicitud no esta en estado inicial para devolver al remitente.";
					return;
				}else{
					this.mensajeGlogal = null;
				}
			}
			
			if(this.normalCuota != null || this.montoNuevo != null || this.pieNuevo != null){
				if(this.normalCuota == null){
					this.mensajeGlogal = "Debe ingresar el tipo de venta de la solicitud.";
					return;				
				}else{
					if("".equals(this.normalCuota)){
						this.mensajeGlogal = "Debe ingresar el tipo de venta de la solicitud.";
						return;							
					}
				}
				if(this.montoNuevo == null){
					this.mensajeGlogal = "Debe ingresar el monto de la solictud.";
					return;				
				}else{
					if(this.montoNuevo == 0){
						this.mensajeGlogal = "Debe ingresar el monto de la solictud.";
						return;							
					}
				}
				if(this.pieNuevo == null){
					this.mensajeGlogal = "Debe ingresar el pie de la solictud.";
					return;				
				}else{
					if(this.pieNuevo == 0){
						this.mensajeGlogal = "Debe ingresar el pie de la solictud.";
						return;							
					}
				}				

				if(this.pieNuevo > this.montoNuevo){
					this.mensajeGlogal = "El pie ingresado debe ser menor o igual al monto.";
					return;				
				}	
				
				this.modificarVenta = true;
			}
			
			if(this.listaUsuarioCorreoagregados ==null){
				this.mensajeGlogal = "Debe ingresar los usuarios para el envio de correo.";
				return;
			}else{
				if(this.listaUsuarioCorreoagregados.size() == 0 ){
					this.mensajeGlogal = "Debe ingresar los usuarios para el envio de correo.";
					return ;
				}
			}
			
			
			if(this.observacionesFinales ==null){
				this.mensajeGlogal = "Debe ingresar una observación final.";
				return;
			}else{
				if("".equals(this.observacionesFinales)){
					this.mensajeGlogal = "Debe ingresar una observación final.";
					return ;
				}
			}
			
			this.mensajeGlogal = null;
		}else{
			this.mensajeGlogal = "La solicitud no tiene asociada el tipo de solicitud...";
		}
	}
	public String devorlverSolcicitud(){
		String devolver = null;
		try{
			if(solicitud != null && venta != null){
			
				if(this.paginaIngreso == 11 || this.paginaIngreso == 12){
					if(this.modificarVenta){
						solicitud.setEstado("DR");
						solicitud.setUsuarioActual(solicitud.getCodEmisor());
						solicitud.setDevolver(true);
						
						venta.setMontoFinal( new BigDecimal(this.montoNuevo));
						venta.setMontoPieInicial(new BigDecimal(this.pieNuevo));
						venta.setObservacionesFinal(this.observacionesFinales);
						venta.setStatusSolicitud(solicitud.getEstado());
						String tipo = null;
						if(this.normalCuota.equals("11")){
							tipo = "N";
							venta.setTipoVentaFinal(tipo.charAt(0));
							
						}else if(this.normalCuota.equals("12")){
							tipo = "C";
							venta.setTipoVentaFinal(tipo.charAt(0));
						}
					}else{
						solicitud.setEstado("DR");
						solicitud.setUsuarioActual(solicitud.getCodEmisor());
						solicitud.setDevolver(true);
	
						venta.setMontoFinal( venta.getMontoInicial());
						venta.setMontoPieInicial(venta.getMontoPieInicial());
						venta.setObservacionesFinal(this.observacionesFinales);
						venta.setTipoVentaFinal(venta.getTipoVentaInicial());
						venta.setStatusSolicitud(solicitud.getEstado());
						
					}
					
					
					
					
					boolean exito = scoringService.mergerSolicitud(solicitud);
					boolean exitoVenta = scoringService.mergerSolicitudVentaPuntual(venta);
					
					if(exito == true && exitoVenta == true){
						this.mensajeGlogal = "La solicitud "+ solicitud.getId().getNumSolicitud() +" fue devuelta alremitente.";
						List<ConceptoDTO> listaArchivoGuardadoResp = new ArrayList<ConceptoDTO>(0); 
						if(listaFileUploadedDTOs != null && listaFileUploadedDTOs.size() != 0){
							
							for(FileUploadedDTO archivo :listaFileUploadedDTOs){
							   try {							
									OutputStream ostream = null;
									String url = "archivosRespuesta"+"/"+ solicitud.getId().getNumSolicitud()+"_"+tipoSolicitudAux.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
									String nombreArchivo = solicitud.getId().getNumSolicitud()+"_"+tipoSolicitudAux.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
									InputStream istream = new FileInputStream(archivo.getUploadItem().getFile());
									
									//la direccion donde se rescata el archivo esta dado por el path del global parameter + la url formada arriba
									ostream = new FileOutputStream(globalParameters.getAttachedFilesAbsolutePath()+url);
									log.debug("url #0",archivo.getUploadItem().getContentType());
									IOUtils.copyStream(ostream, istream);
									ostream.flush();
									ostream.close();
									istream.close();
									//guardar datos en la BD
									ArchivoAdjunto archivoAux = new ArchivoAdjunto();
									archivoAux.setSolicitud(solicitud);
									archivoAux.setUrl(url);
									archivoAux.setNombreAdjunto(archivo.getNombreArchivo());
									entityManager.persist(archivoAux);
									entityManager.flush();
									ConceptoDTO objetoAux = new ConceptoDTO();
									objetoAux.setNombreArchivo(nombreArchivo);
									objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+url);
									listaArchivoGuardadoResp.add(objetoAux);
									objetoAux = null;
									
								} catch (FileNotFoundException e) {
									log.error("error al guardar archivo #0", e.toString());
									FacesMessages.instance().add(Severity.ERROR,"Error al guardar el archivo "+ archivo.getNombreArchivo());
								}
								catch (IOException e) {
									log.error("error al guardar archivo #0", e.toString());
									FacesMessages.instance().add(Severity.ERROR,"Error al guardar el archivo "+ archivo.getNombreArchivo());
								}catch (Exception e) {
									e.printStackTrace();
								}							
							}	
						}
						
						List<ConceptoDTO> lista = scoringService.getConceptoMonto(solicitud.getId().getNumSolicitud(), solicitud.getMonto().longValue());
						List<ConceptoDTO> listaProducto = scoringService.getProductos(solicitud.getId().getNumSolicitud());
						
						if(listaUsuarioCorreoagregados != null){
							Locale locale = new Locale("es","CL");
							EmailAlertaContactoMensajeHelper emailAlerta = null;
							/*setando los varlores del dto para el correo*/
							ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
							contenidoEmail.setAccionRespuesta("Devolver la Solicitud");
							contenidoEmail.setUsuarioAprobador(lcredUsuarioNivelEnc.getId().getNombreUsuario());
							contenidoEmail.setObservacionRespuesta(this.observacionesFinales);
							contenidoEmail.setSolicitud(solicitud);
							NumberFormat numberFormatter;
							StringBuffer rutNombre = new StringBuffer();
							rutNombre.append(cliente.getRut());
							rutNombre.append(" / ");
							rutNombre.append(cliente.getRazonSocial());
							contenidoEmail.setRutNombre(rutNombre.toString());
							contenidoEmail.setSoloNombre(cliente.getRazonSocial());
							contenidoEmail.setCanalVenta(cliente.getCanalVenta());
							contenidoEmail.setSucursal(sucursal.getDescripcion());
							contenidoEmail.setEmisor(usuarioLogueado.getNombre());
							contenidoEmail.setTipoSolicitud("Venta Puntual");
							contenidoEmail.setTipoVentas(tipoSolicitudAux.getDesTipoSolicitud());
							
							numberFormatter = NumberFormat.getNumberInstance(locale);
							log.debug(numberFormatter.format(venta.getMontoInicial()));
							
							contenidoEmail.setMontoMasIva(numberFormatter.format(venta.getMontoInicial()));
							contenidoEmail.setMontoPie(numberFormatter.format(venta.getMontoPieInicial()));
							contenidoEmail.setMargenNegocio(String.valueOf(numberFormatter.format(this.margenGlogal)));
							contenidoEmail.setFormaPago(this.formaPago);
							contenidoEmail.setMotivoCompra(this.motivoCompra);
							
							contenidoEmail.setPeakCredito(numberFormatter.format(expoRiesgoKh.getPeakCredito()));
							if("VIG".equals(cliente.getVigenciaSeguro().trim())){
								contenidoEmail.setLineaSeguro(numberFormatter.format(expoRiesgoKh.getMontoAsegurado()));
							}else{
								contenidoEmail.setLineaSeguro(numberFormatter.format(0));
							}								
							//contenidoEmail.setLineaSeguro(numberFormatter.format(clienteTarget.getMontoSeguro()));
							contenidoEmail.setRisgoKupfer(numberFormatter.format(expoRiesgoKh.getMontoRiesgoKupfer()));
							contenidoEmail.setListaProductos(listaProducto);
							contenidoEmail.setListaConceptoMontos(lista);
							contenidoEmail.setListaArchivos(listaArchivoGuardado);
							contenidoEmail.setListaArchivosRespuesta(listaArchivoGuardadoResp);
							
							for( SolicitudUsuarioCorreo usuarioCorreo: listaUsuarioCorreoagregados){
								try{
									if(emailAlerta == null){
										emailAlerta = (EmailAlertaContactoMensajeHelper) Component.getInstance("emailAlertaContactoMensajeHelper");
										contenidoEmail.setDestinatario(usuarioCorreo.getCorreo());
										contenidoEmail.setNombreUsuario(usuarioCorreo.getNombre());
										emailAlerta.enviarRespuestaSolicitudVenta(contenidoEmail);	
										log.debug("enviado respuesta de correo al usuario: #0",usuarioCorreo.getNombre());
										emailAlerta=null; 
									}	
								}catch (Exception e) {
									log.debug("paso por el error #0"+ e.getMessage());
								}
							}
						}
						
						insertarLogs(solicitud, 6);
						LcredEstado estado = ObtenerEstadoSolictud("DR");
						insertHitos(solicitud, estado);
						entityManager.createQuery("update LcredSolicitud set evaluar=0 where id.numSolicitud=:sol")
						             .setParameter("sol", solicitud.getId().getNumSolicitud()).executeUpdate();
						entityManager.flush();
						if(paginaVolver.equals("Listado")){
							devolver="devolverListaAprobar";
						}else if(paginaVolver.equals("ListaEstado")){
							devolver="devolverListaEstados";
						}
					}
			}	
		}
			
		}catch (Exception e) {
			log.error("Error al devolver la solicitud #0", e.getMessage());
		}
		return devolver;		
	}

	public  void limpiaMensajeGlobal(){
		setMensajeGlogal(null);
	}
	
	public void salirDevolucionSolicitud(){
	  log.debug("salir del pop.");	
	  if(this.salirDelMensaje == false){
		  this.salirDelMensaje = true;
	  }
	}

	public String salirRespuestaIrListado(){
		String devolver = null;
		try{
			if(paginaVolver.equals("Listado")){
				devolver="devolverListaAprobar";
			}else if(paginaVolver.equals("ListaEstado")){
				devolver="devolverListaEstados";
			}
		}catch (Exception e) {
			log.error("Error, al salir de la solicitud", e.getMessage());
		}
		return devolver;
	}
	
	/**************************Adjuntar archivos***********************/
	
	private List<UploadItem> uploadFile = new ArrayList<UploadItem>();
	private String nombreArchivoTarget = new String();
		
	public List<UploadItem> getUploadFile() { return uploadFile; }
	public void setUploadFile(List<UploadItem> uploadFile) { this.uploadFile = uploadFile; }
	public String getNombreArchivoTarget() { return nombreArchivoTarget; }
	public void setNombreArchivoTarget(String nombreArchivoTarget) { this.nombreArchivoTarget = nombreArchivoTarget; }
		
	public void guardarArchivo(String carpeta) {
		
		if(uploadFile.size() > 0){
			if(uploadFile == null || uploadFile.isEmpty()) {
				FacesMessages.instance().add(Severity.ERROR, "El archivo no ha sido cargado" );
				return;
			}
			if(uploadFile.get(0) == null) {
				FacesMessages.instance().add(Severity.ERROR, "El archivo no ha sido cargado" );
				return;
			}
			listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
			FileUploadedDTO file = null;
			for(UploadItem archivo :uploadFile){
				file = new FileUploadedDTO();
				file.setTipo(this.tipoSolicitudAux);				
				file.setUploadItem(archivo);
				file.setNombreArchivo(archivo.getFileName());
				listaFileUploadedDTOs.add(file);
				file = null;
			}
			uploadFile.clear();
			log.debug("size #0",nombreArchivoTarget);
		}
		
	}
	
	
	
	
	/******************************************************************/
	
	public void agregarArchivos(String nombrearchivo){
		if(uploadFile != null){
			LcredTipoSolicitud tipo = entityManager.find(LcredTipoSolicitud.class, String.valueOf(paginaIngreso));
			for(UploadItem uploadItem: uploadFile){
				FileUploadedDTO archivoDto = new FileUploadedDTO();
				archivoDto.setNombreArchivo(nombrearchivo);
				archivoDto.setUploadItem(uploadItem);
				archivoDto.setTipo(tipo);
				listaFileUploadedDTOs.add(archivoDto);
			}
		}
	}
	
	public void eliminarArchivo(FileUploadedDTO archivoAux){
		if(archivoAux != null){
			listaFileUploadedDTOs.remove(archivoAux);
		}
	}	
	
	public void obtenerOpcionNormalCuota(){
		if(this.normalCuota != null){
			log.debug("selccionado : #0", this.normalCuota);
		}
		
	}
	
	
	public void obtenerMontoNuevo(){
		try{
			if(this.montoNuevo != null){
				log.debug("monto nuevo  #0", this.montoNuevo);
			}
			if(this.pieNuevo != null){
				log.debug("monto nuevo  #0", this.pieNuevo);
			}
			if(this.montoLineaCreditoNuevo != null){
				log.debug("monto nuevo de la linea de Crédito #0", this.montoLineaCreditoNuevo);
			}			
		}catch (Exception e) {
			log.error("Error, al formatear los montos #0", e.getMessage());
		}
	}
	
	public void obtenerOpcionInnNomRiesgo(){
		if(this.innNomRiesgo != null){
			log.debug("selccionado innNomRiesgo : #0", this.innNomRiesgo);
		}
		
		if(this.innNomRiesgoNueva != null){
			log.debug("selccionado innNomRiesgoNueva : #0", this.innNomRiesgoNueva);
		}
		
	}
	
	public void obtenerCondicionRiesgo(){
		if(this.clsRiesgo != null){
			log.error("El clasificacion de riesgo es #0", this.clsRiesgo);
		}
		if(this.clsRiesgoNuevo != null){
			log.error("El nuevo clasificacion de riesgo es #0", this.clsRiesgoNuevo);
		}
		
		
	}	

	public void obtenerCondicionPago(){
		if(this.cPago != null){
			log.error("El condicion de pago es #0", this.cPago);
		}
		if(this.cPagoNuevo != null){
			log.error("El nuevo condicion de pago es #0", this.cPagoNuevo);
		}
		
	}
	public void obtenerCondicionRiesgoNuevo(){
		if(this.clsRiesgoNuevo != null){
			log.error("El clasificacion de riesgo nuevo es #0", this.clsRiesgoNuevo);
		}
	}	

	public void obtenerCondicionPagoNuevo(){
		if(this.cPagoNuevo != null){
			log.error("El condicion de pago nuevo es #0", this.cPagoNuevo);
		}
	}	
	
	public void evaluarInnominalNominalRKupfer(String codigoClsRiesgo, String codigoCdnPago ){
		setClsRiesgo(null); 
		setcPago(null);
		listaStringClsRiesgos = new ArrayList<String>(0); 
		listaStringCdnPagos = new ArrayList<String>(0);		
		try{
			
			/*recorrer la lista para encontrar los codigo*/
			if(this.listaClsRiesgos != null){
				for(ClsRiesgoDTO cls : this.listaClsRiesgos){
					//log.error("codigo de la clasificacion de riesgo #0", cls.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((cls.getCodigo().trim())); cadena.append("-"); cadena.append((cls.getDescripcion().trim()));
					if(this.paginaIngreso != 24){
						if(cls != null  && (cls.getCodigo().trim()).equals(codigoClsRiesgo)){
							//log.error("clasificacion de riesgo #0", cadena.toString());
							setClsRiesgo(cadena.toString());
						}
						listaStringClsRiesgos.add(cadena.toString());
						cadena = null;
					}else{
						if(cls != null  && ((cls.getCodigo().trim()).equals("005") || (cls.getCodigo().trim()).equals("008"))){
							//log.error("clasificacion de riesgo #0", cadena.toString());
							if(this.clsRiesgo == null){
								setClsRiesgo(cadena.toString());
								listaStringClsRiesgos.add(cadena.toString());
							}else{
								listaStringClsRiesgos.add(cadena.toString());
							}
							cadena = null;
						}
					}
				}
			}
			
			if(this.listaCondicionPagos != null){
				for(CPagoDTO obj : this.listaCondicionPagos){
					//log.error("codigo de la condicion de riesgo #0", obj.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((obj.getCodigo().trim())); cadena.append("-"); cadena.append((obj.getDescripcion().trim()));
					
					if(this.paginaIngreso != 24){
						if(obj != null  && obj.getCodigo().equals(codigoCdnPago) ){
							//log.error("condicion de riesgo #0", cadena.toString());
							setcPago(cadena.toString());
						}
						listaStringCdnPagos.add(cadena.toString());
						cadena = null;
					}else if(this.paginaIngreso == 24){
						if(obj != null  && obj.getCodigo().equals(codigoCdnPago) ){
							//log.error("condicion de riesgo #0", cadena.toString());
							setcPago(cadena.toString());
							listaStringCdnPagos.add(cadena.toString());
							cadena = null;	
							break;
						}
					}
				}					
			}			
			
		}catch (Exception e) {
			log.error("Error, a evaluar los combo #0", e.getMessage());
		}
		
		
	}

	public void evaluarInnominalNominalRKupferFinales(String codigoClsRiesgo, String codigoCdnPago ){
		setClsRiesgoNuevo(null); 
		setcPagoNuevo(null);
		try{
			
			/*recorrer la lista para encontrar los codigo*/
			if(this.listaClsRiesgos != null && codigoClsRiesgo != null){
				for(ClsRiesgoDTO cls : this.listaClsRiesgos){
					//log.error("codigo de la clasificacion de riesgo #0", cls.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((cls.getCodigo().trim())); cadena.append("-"); cadena.append((cls.getDescripcion().trim()));
					if(this.paginaIngreso != 24){
						if(cls != null  && (cls.getCodigo().trim()).equals(codigoClsRiesgo)){
							setClsRiesgoNuevo(cadena.toString());
						}
					}else{
						if(cls != null  && ((cls.getCodigo().trim()).equals("005") || (cls.getCodigo().trim()).equals("008"))){
							//log.error("clasificacion de riesgo #0", cadena.toString());
							if(this.clsRiesgo == null){
								setClsRiesgoNuevo(cadena.toString());
							}
							cadena = null;
						}
					}
				}
			}
			if(this.listaCondicionPagos != null && codigoCdnPago != null){
				for(CPagoDTO obj : this.listaCondicionPagos){
					//log.error("codigo de la condicion de riesgo #0", obj.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((obj.getCodigo().trim())); cadena.append("-"); cadena.append((obj.getDescripcion().trim()));
					
					if(this.paginaIngreso != 24){
						if(obj != null  && obj.getCodigo().equals(codigoCdnPago) ){
							//log.error("condicion de riesgo #0", cadena.toString());
							setcPagoNuevo(cadena.toString());
						}
						cadena = null;
					}else if(this.paginaIngreso == 24){
						if(obj != null  && obj.getCodigo().equals(codigoCdnPago) ){
							//log.error("condicion de riesgo #0", cadena.toString());
							setcPagoNuevo(cadena.toString());
							listaStringCdnPagos.add(cadena.toString());
							cadena = null;	
							break;
						}
					}
				}					
			}			
			
		}catch (Exception e) {
			log.error("Error, a evaluar los combo #0", e.getMessage());
		}
	}
	
	
	public void habilitarComboCondicion(String opcion){
		if("CondicionRiesgo".equals(opcion)){
			if(comboCRiesgo){comboCRiesgo = false;}else{comboCRiesgo=true;}
			setClsRiesgoNuevo(null);
		}else if("CondicionPago".equals(opcion)){
			if(comboCPago){ comboCPago = false;}else{comboCPago=true;}
			setcPagoNuevo(null);
		}
	}
	
	public void seleccionOpcionesBloqueoDesbloqueo(){
		if(this.opcionBloqueoDesbloqueo != null){
			log.debug("la opcion seleccionada es #0", this.opcionBloqueoDesbloqueo);
		}else{
			log.error("error de seleccion viene vacio");
		}
	}

	public void seleccionOpcionesBloqueoDesbloqueoNew(){
		if(this.opcionBloqueoDesbloqueoNew != null){
			log.debug("la nueva opcion seleccionada es #0", this.opcionBloqueoDesbloqueoNew);
		}else{
			log.error("error de seleccion viene vacio");
		}
	}
	
	public void obtenerCorreo(String usarioAux){
		/*va a buscar los destinatarios de las solictudes*/
		try{
			UsuarioSegurDTO user = scoringService.sacarDatosSessionUsuario(usarioAux);
			if(user != null){
				SolicitudUsuarioCorreo nuevo = scoringService.getSolicitudUsuarioCorreo(user.getAlias());
				if(nuevo == null){
					nuevo = new SolicitudUsuarioCorreo();
					nuevo.setUsername(user.getAlias());
					nuevo.setNombre(user.getNombre());
					nuevo.setCorreo(user.getCorreo());
					nuevo.setEvaluar(false);
					nuevo = scoringService.persistSolicitudCorreo(nuevo);
					listaUsuarioCorreoagregados.add(nuevo);
				}else{
					if(nuevo.getEvaluar() == false){
						listaUsuarioCorreoagregados.add(nuevo);
					}else{
						nuevo.setEvaluar(false);
						nuevo = scoringService.mergerSolicitudCorreo(nuevo);
						listaUsuarioCorreoagregados.add(nuevo); 
					}
				}
			}
		}catch (Exception e) {
			log.error("Error, sacar los destinatarios : #0", e.getMessage());
		}
	}
	
	
	/* agregar usuario correo*/
	public void agregarUsuarioCorreoListaUsuarioCorreoReserva(){
		if(this.usuarioCorreo != null){
			SolicitudUsuarioCorreo usuCorreoAux =  buscarUsuarioCorreo(usuarioCorreo.getNombre());
			if(usuCorreoAux != null){
				if(listaUsuarioCorreoagregados.contains(usuCorreoAux)){
					setMensajeExplicativo("El usuario "+ this.usuarioCorreo + " ya fue agregado a la lista.");
					this.usuarioCorreo = null;
				}else{
					listaUsuarioCorreoagregados.add(usuCorreoAux);
					this.usuarioCorreo = null;
					this.mensajeExplicativo = null;
				}
			}
		}else{setMensajeExplicativo("Debe seleccionar el Usuario.");}
	}	
	public void eliminarUsuarioCorreoListaAgregado(UsuarioCorreoDTO usuarioCorreoAux){
		if(usuarioCorreoAux != null){
			listaUsuarioCorreoagregados.remove(usuarioCorreoAux);
		}
	}	

	public void eliminarUsuarioCorreoListaAgregado(SolicitudUsuarioCorreo usuarioCorreoAux){
		if(usuarioCorreoAux != null){
			listaUsuarioCorreoagregados.remove(usuarioCorreoAux);
		}
		ingresarModelCorreo();
	}	
	public SolicitudUsuarioCorreo buscarUsuarioCorreo(String codigo){
		SolicitudUsuarioCorreo usuarioCorreo = null;
		if(listaUsuarioCorreos != null && codigo != null){
			for(SolicitudUsuarioCorreo u: listaUsuarioCorreos){
				if(u.getNombre().equals(codigo)){
					usuarioCorreo = u;
					break;
				}
			}
		}
		return usuarioCorreo;
	}
	
	public void ingresarModelCorreo(){
		this.setUsuarioCorreo(null);
		this.setMensajeExplicativo(null);
		
	}
	
	
	public void obtenerUsuarioCorreo(){
		if(this.usuarioCorreo != null){
			log.error("El usaurio seleccionado es el siguiente #0", this.usuarioCorreo.getNombre());
		}
	}
	
	
	public void sacarLogSolicitud(){
		SolicitudDTO sol = null;
		try{
			if(solicitud != null && cliente != null){
				sol = new SolicitudDTO();
				sol.setIdSolictud(solicitud.getId().getNumSolicitud());
				sol.setRazonSocial(cliente.getRazonSocial());
				sol.setEstado(cliente.getEstadoSolicitud());
				
				globalHitosLogdService.setSolicitud(sol);
				List<SolicitudLogs> listaSolicitudesLogs = scoringService.getSolicitudLogs(sol.getIdSolictud());
				if(listaSolicitudesLogs != null){
					globalHitosLogdService.setListaLogs(listaSolicitudesLogs);
				}
			}
		}catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud", e.getMessage());
		}
		
		
	}
	public void sacarLogHitos(){
		SolicitudDTO sol = null;
		try{
			if(solicitud != null && cliente != null){
				sol = new SolicitudDTO();
				sol.setIdSolictud(solicitud.getId().getNumSolicitud());
				sol.setRazonSocial(cliente.getRazonSocial());
				sol.setEstado(cliente.getEstadoSolicitud());				
				globalHitosLogdService.setSolicitud(sol);
				List<SolicitudHitos> listaSolicitudesHitos = scoringService.getSolicitudHitos(sol.getIdSolictud());
				if(listaSolicitudesHitos != null){
					globalHitosLogdService.setListaHitos(listaSolicitudesHitos);
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud", e.getMessage());
		}
		
	}	
	
	
	
	public void obtenerAnalisisAprobacion(){
		if(this.analisisAprobacion != null){
			log.debug("selccionado  : #0", this.analisisAprobacion);
		}
	}
	
	
	/* agregar usuario correo*/
	public void agregarUsuarioCorreoListaAnalisisDerivacion(){
		if(this.usuarioCorreo != null){
			SolicitudUsuarioCorreo usuCorreoAux =  buscarUsuarioCorreo(usuarioCorreo.getNombre());
			if(usuCorreoAux != null){
				if(listaCorreoAnaDer.contains(usuCorreoAux)){
					setMensajeExplicativo("existe");
					FacesMessages.instance().add(Severity.ERROR,"El usuario "+ this.usuarioCorreo.getNombre().toLowerCase() + " ya fue agregado a la lista.");
					this.usuarioCorreo = null;
				}else{
					listaCorreoAnaDer.add(usuCorreoAux);
					this.usuarioCorreo = null;
					this.mensajeExplicativo = null;
				}
			}
		}else{
			setMensajeExplicativo("Debe seleccionar el Usuario.");
			FacesMessages.instance().add(Severity.WARN,"Debe seleccionar el Usuario que quiere agregar a la lista.");
		}
	}
	public void agregarTodosUsuarioCorreoListaAnalisisDerivacion(){
		for(SolicitudUsuarioCorreo u: listaUsuarioCorreos){
			if(!listaCorreoAnaDer.contains(u)){
				listaCorreoAnaDer.add(u);
			}
		}	
	}
	public void eliminarTodosUsuarioCorreoListaAnalisisDerivacion(){
		this.setUsuarioCorreo(null);
		this.listaCorreoAnaDer = new ArrayList<SolicitudUsuarioCorreo>(0);
	}	
	public void eliminarCorreoAnalisisDerivacion(SolicitudUsuarioCorreo usuarioCorreoAux){
		if(usuarioCorreoAux != null){
			listaCorreoAnaDer.remove(usuarioCorreoAux);
		}
	}
	
	public void ingresoDerivarSolicitud(){
		this.setUsuarioCorreo(null);
		this.listaCorreoAnaDer = new ArrayList<SolicitudUsuarioCorreo>(0);
		
	}
	
	public void analizarToDerivar(){
		
		
	}

	public void cerrarAnalizarToDerivar(){
		this.analisisAprobacion= null;
		this.setUsuarioCorreo(null);
		this.listaCorreoAnaDer = new ArrayList<SolicitudUsuarioCorreo>(0);
		this.mensajeAnalisisAprobacion = null;
		
	}
	
	public void obtenerMotivoRechazo(){
		if(this.motivoRechazo != null){
			log.debug("El motivo de rechazo  seleccionado es el siguiente #0", this.motivoRechazo.getDesRechazo());
		}
	}

	/* agregar usuario correo*/
	public void agregarMotivoRechazoListaRechazo(){
		if(this.motivoRechazo != null){
			if(listaMotivosRechazosAgregados.contains(this.motivoRechazo)){
				setMensajeExplicativo("existe");
				FacesMessages.instance().add(Severity.ERROR,"El motivo de rechazo  "+ this.motivoRechazo.getDesRechazo().toLowerCase() + " ya fue agregado a la lista.");
				this.motivoRechazo = null;
			}else{
				listaMotivosRechazosAgregados.add(motivoRechazo);
				this.motivoRechazo = null;
				this.mensajeExplicativo = null;
			}
		}else{
			setMensajeExplicativo("Debe seleccionar el motivo de rechazo.");
			FacesMessages.instance().add(Severity.WARN,"Debe seleccionar el motivo de rechazo que quiere agregar a la lista.");
		}
	}
	
	public void agregarTodosMotivoRechazosListaAnalisisDerivacion(){
		for(LcredMotivoRechazo u: listaMotivosRechazos){
			if(!listaMotivosRechazosAgregados.contains(u)){
				listaMotivosRechazosAgregados.add(u);
			}
		}	
	}
	public void cerrarMotivoRechazo(){
		this.setMotivoRechazo(null);
		this.listaMotivosRechazosAgregados = new ArrayList<LcredMotivoRechazo>(0);
		this.mensajeAnalisisAprobacion = null;
		
	}
	
	public void eliminarTodosMotivoRechazosLista(){
		this.setMotivoRechazo(null);
		this.listaMotivosRechazosAgregados = new ArrayList<LcredMotivoRechazo>(0);
	}	
	public void eliminarMotivoRechazo(LcredMotivoRechazo motivoRechazoAux){
		if(motivoRechazoAux != null){
			listaMotivosRechazosAgregados.remove(motivoRechazoAux);
		}
	}	
	public void ingresoRechazoSolicitud(){
		this.setMotivoRechazo(null);
		this.listaMotivosRechazosAgregados = new ArrayList<LcredMotivoRechazo>(0);
		if(this.listaMotivosRechazos == null){
			this.listaMotivosRechazos = scoringService.getMotivosRechazos();
		}
		
	}
	
	public void rechazarSolicitudes(){
		
		
	}

	public void ingresoAnular(){
		if(this.solicitud != null && this.tipoSolicitudAux != null){
			log.debug("solicitud #0 tipo de solicitud #1", solicitud.getId().getNumSolicitud(), tipoSolicitudAux.getCodTipoSolicitud());
			if(solicitud != null){
				if(solicitud.getEstado().equals("NU")){
					this.mensajeGlogal = "La solicitud ya fue anulada.";
					return;
				}else if(!solicitud.getEstado().equals("I")){
					this.mensajeGlogal = "La solicitud no esta en estado inicial para anular al remitente.";
					return;
				}else{
					this.mensajeGlogal = null;
				}
			}
			
			
			if(this.listaUsuarioCorreoagregados ==null){
				this.mensajeGlogal = "Debe ingresar los usuarios para el envio de correo.";
				return;
			}else{
				if(this.listaUsuarioCorreoagregados.size() == 0 ){
					this.mensajeGlogal = "Debe ingresar los usuarios para el envio de correo.";
					return ;
				}
			}
			
			
			if(this.observacionesFinales ==null){
				this.mensajeGlogal = "Debe ingresar una observación final.";
				return;
			}else{
				if("".equals(this.observacionesFinales)){
					this.mensajeGlogal = "Debe ingresar una observación final.";
					return ;
				}
			}
			
			this.mensajeGlogal = null;
		}else{
			this.mensajeGlogal = "La solicitud no tiene asociada el tipo de solicitud...";
		}
	}	
	
	
	public void anularSolicitudes(){
		
		try{
			if(solicitud != null && venta != null){
				if(this.paginaIngreso == 11 || this.paginaIngreso == 12){
					if(this.modificarVenta){
						solicitud.setEstado("NU");
						solicitud.setUsuarioActual(solicitud.getCodEmisor());
						solicitud.setDevolver(true);
						
						venta.setMontoFinal( new BigDecimal(this.montoNuevo));
						venta.setMontoPieInicial(new BigDecimal(this.pieNuevo));
						venta.setObservacionesFinal(this.observacionesFinales);
						venta.setStatusSolicitud(solicitud.getEstado());
						String tipo = null;
						if(this.normalCuota.equals("11")){
							tipo = "N";
							venta.setTipoVentaFinal(tipo.charAt(0));
							
						}else if(this.normalCuota.equals("12")){
							tipo = "C";
							venta.setTipoVentaFinal(tipo.charAt(0));
						}
					}else{
						solicitud.setEstado("NU");
						solicitud.setUsuarioActual(solicitud.getCodEmisor());
						solicitud.setDevolver(true);
	
						venta.setMontoFinal( venta.getMontoInicial());
						venta.setMontoPieInicial(venta.getMontoPieInicial());
						venta.setObservacionesFinal(this.observacionesFinales);
						venta.setTipoVentaFinal(venta.getTipoVentaInicial());
						venta.setStatusSolicitud(solicitud.getEstado());
						
					}
					
					boolean exito = scoringService.mergerSolicitud(solicitud);
					boolean exitoVenta = scoringService.mergerSolicitudVentaPuntual(venta);
					
					if(exito == true && exitoVenta == true){
						this.mensajeGlogal = "La solicitud "+ solicitud.getId().getNumSolicitud() +" fue anulada definitivamente.";
						List<ConceptoDTO> listaArchivoGuardadoResp = new ArrayList<ConceptoDTO>(0); 
						if(listaFileUploadedDTOs != null && listaFileUploadedDTOs.size() != 0){
							
							for(FileUploadedDTO archivo :listaFileUploadedDTOs){
							   try {							
									OutputStream ostream = null;
									String url = "archivosRespuesta"+"/"+ solicitud.getId().getNumSolicitud()+"_"+tipoSolicitudAux.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
									String nombreArchivo = solicitud.getId().getNumSolicitud()+"_"+tipoSolicitudAux.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
									InputStream istream = new FileInputStream(archivo.getUploadItem().getFile());
									
									//la direccion donde se rescata el archivo esta dado por el path del global parameter + la url formada arriba
									ostream = new FileOutputStream(globalParameters.getAttachedFilesAbsolutePath()+url);
									log.debug("url #0",archivo.getUploadItem().getContentType());
									IOUtils.copyStream(ostream, istream);
									ostream.flush();
									ostream.close();
									istream.close();
									//guardar datos en la BD
									ArchivoAdjunto archivoAux = new ArchivoAdjunto();
									archivoAux.setSolicitud(solicitud);
									archivoAux.setUrl(url);
									archivoAux.setNombreAdjunto(archivo.getNombreArchivo());
									entityManager.persist(archivoAux);
									entityManager.flush();
									ConceptoDTO objetoAux = new ConceptoDTO();
									objetoAux.setNombreArchivo(nombreArchivo);
									objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+url);
									listaArchivoGuardadoResp.add(objetoAux);
									objetoAux = null;
									
								} catch (FileNotFoundException e) {
									log.error("error al guardar archivo #0", e.toString());
									FacesMessages.instance().add(Severity.ERROR,"Error al guardar el archivo "+ archivo.getNombreArchivo());
								}
								catch (IOException e) {
									log.error("error al guardar archivo #0", e.toString());
									FacesMessages.instance().add(Severity.ERROR,"Error al guardar el archivo "+ archivo.getNombreArchivo());
								}catch (Exception e) {
									e.printStackTrace();
								}							
							}	
						}
						
						List<ConceptoDTO> lista = scoringService.getConceptoMonto(solicitud.getId().getNumSolicitud(), solicitud.getMonto().longValue());
						List<ConceptoDTO> listaProducto = scoringService.getProductos(solicitud.getId().getNumSolicitud());
						
						if(listaUsuarioCorreoagregados != null){
							Locale locale = new Locale("es","CL");
							EmailAlertaContactoMensajeHelper emailAlerta = null;
							/*setando los varlores del dto para el correo*/
							ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
							contenidoEmail.setAccionRespuesta("Anular la Solicitud");
							contenidoEmail.setUsuarioAprobador(lcredUsuarioNivelEnc.getId().getNombreUsuario());
							contenidoEmail.setObservacionRespuesta(this.observacionesFinales);
							contenidoEmail.setSolicitud(solicitud);
							NumberFormat numberFormatter;
							StringBuffer rutNombre = new StringBuffer();
							rutNombre.append(cliente.getRut());
							rutNombre.append(" / ");
							rutNombre.append(cliente.getRazonSocial());
							contenidoEmail.setRutNombre(rutNombre.toString());
							contenidoEmail.setSoloNombre(cliente.getRazonSocial());
							contenidoEmail.setCanalVenta(cliente.getCanalVenta());
							contenidoEmail.setSucursal(sucursal.getDescripcion());
							contenidoEmail.setEmisor(usuarioLogueado.getNombre());
							contenidoEmail.setTipoSolicitud("Venta Puntual");
							contenidoEmail.setTipoVentas(tipoSolicitudAux.getDesTipoSolicitud());
							
							numberFormatter = NumberFormat.getNumberInstance(locale);
							log.debug(numberFormatter.format(venta.getMontoInicial()));
							
							contenidoEmail.setMontoMasIva(numberFormatter.format(venta.getMontoInicial()));
							contenidoEmail.setMontoPie(numberFormatter.format(venta.getMontoPieInicial()));
							contenidoEmail.setMargenNegocio(String.valueOf(numberFormatter.format(this.margenGlogal)));
							contenidoEmail.setFormaPago(this.formaPago);
							contenidoEmail.setMotivoCompra(this.motivoCompra);
							
							contenidoEmail.setPeakCredito(numberFormatter.format(expoRiesgoKh.getPeakCredito()));
							if("VIG".equals(cliente.getVigenciaSeguro().trim())){
								contenidoEmail.setLineaSeguro(numberFormatter.format(expoRiesgoKh.getMontoAsegurado()));
							}else{
								contenidoEmail.setLineaSeguro(numberFormatter.format(0));
							}								
							//contenidoEmail.setLineaSeguro(numberFormatter.format(clienteTarget.getMontoSeguro()));
							contenidoEmail.setRisgoKupfer(numberFormatter.format(expoRiesgoKh.getMontoRiesgoKupfer()));
							contenidoEmail.setListaProductos(listaProducto);
							contenidoEmail.setListaConceptoMontos(lista);
							contenidoEmail.setListaArchivos(listaArchivoGuardado);
							contenidoEmail.setListaArchivosRespuesta(listaArchivoGuardadoResp);
							
							for( SolicitudUsuarioCorreo usuarioCorreo: listaUsuarioCorreoagregados){
								try{
									if(emailAlerta == null){
										emailAlerta = (EmailAlertaContactoMensajeHelper) Component.getInstance("emailAlertaContactoMensajeHelper");
										contenidoEmail.setDestinatario(usuarioCorreo.getCorreo());
										contenidoEmail.setNombreUsuario(usuarioCorreo.getNombre());
										emailAlerta.enviarRespuestaSolicitudVenta(contenidoEmail);	
										log.debug("enviado respuesta de correo al usuario: #0",usuarioCorreo.getNombre());
										emailAlerta=null; 
									}	
								}catch (Exception e) {
									log.debug("paso por el error #0"+ e.getMessage());
								}
							}
						}
						
						insertarLogs(solicitud, 7);
						LcredEstado estado = ObtenerEstadoSolictud("NU");
						insertHitos(solicitud, estado);
						entityManager.createQuery("update LcredSolicitud set evaluar=0 where id.numSolicitud=:sol")
						             .setParameter("sol", solicitud.getId().getNumSolicitud()).executeUpdate();
						entityManager.flush();

					}
			}	
		}
			
		}catch (Exception e) {
			log.error("Error al devolver la solicitud #0", e.getMessage());
		}
		return ;		
	}
	public void salirAnulacionSolicitud(){
		  log.debug("salir del pop.");	
		  if(this.salirDelMensaje == false){
			  this.salirDelMensaje = true;
		  }
	}
	
	
	public void cerrarMotivoRechazar(){
		
		this.setMotivoRechazo(null);
		this.listaMotivosRechazosAgregados = new ArrayList<LcredMotivoRechazo>(0);
		this.mensajeRechazoSolicitud = null;
		
	}	
	
	public void  validarDatosRespuesta(int tipoSolicitud){
		try{
			if(tipoSolicitud  == 11){
				
			}
		}catch (Exception e) {
			log.error("Error, al los datos tipo de  solicitud.", e.getMessage());
		}
	}
	
	public void ingresoAprobar(){
		if(this.solicitud != null && this.tipoSolicitudAux != null){
			log.debug("solicitud #0 tipo de solicitud #1", solicitud.getId().getNumSolicitud(), tipoSolicitudAux.getCodTipoSolicitud());
			if(solicitud != null){
				if(solicitud.getEstado().equals("P")){
					this.mensajeGlogal = "La solicitud esta procesada y/o aprobada.";
					return;
				}else if(!solicitud.getEstado().equals("I")){
					this.mensajeGlogal = "La solicitud no esta en estado inicial para aprobarla.";
					return;
				}else{
					this.mensajeGlogal = null;
				}
			}
			
			
			if(this.listaUsuarioCorreoagregados ==null){
				this.mensajeGlogal = "Debe ingresar los usuarios para el envio de correo.";
				return;
			}else{
				if(this.listaUsuarioCorreoagregados.size() == 0 ){
					this.mensajeGlogal = "Debe ingresar los usuarios para el envio de correo.";
					return ;
				}
			}
			
			
			if(this.observacionesFinales ==null){
				this.mensajeGlogal = "Debe ingresar una observación final.";
				return;
			}else{
				if("".equals(this.observacionesFinales)){
					this.mensajeGlogal = "Debe ingresar una observación final.";
					return ;
				}
			}
			
			this.mensajeGlogal = null;
		}else{
			this.mensajeGlogal = "La solicitud no tiene asociada el tipo de solicitud...";
		}
	}	
	
	
	public void aprobarSolicitudes(){
		
		try{
			if(solicitud != null && venta != null){
				if(this.paginaIngreso == 11 || this.paginaIngreso == 12){
					if(this.modificarVenta){
						solicitud.setEstado("A");
						solicitud.setUsuarioActual(solicitud.getCodEmisor());
						solicitud.setDevolver(true);
						
						venta.setMontoFinal( new BigDecimal(this.montoNuevo));
						venta.setMontoPieInicial(new BigDecimal(this.pieNuevo));
						venta.setObservacionesFinal(this.observacionesFinales);
						venta.setStatusSolicitud(solicitud.getEstado());
						String tipo = null;
						if(this.normalCuota.equals("11")){
							tipo = "N";
							venta.setTipoVentaFinal(tipo.charAt(0));
							
						}else if(this.normalCuota.equals("12")){
							tipo = "C";
							venta.setTipoVentaFinal(tipo.charAt(0));
						}
					}else{
						solicitud.setEstado("A");
						solicitud.setUsuarioActual(solicitud.getCodEmisor());
						solicitud.setDevolver(true);
	
						venta.setMontoFinal( venta.getMontoInicial());
						venta.setMontoPieInicial(venta.getMontoPieInicial());
						venta.setObservacionesFinal(this.observacionesFinales);
						venta.setTipoVentaFinal(venta.getTipoVentaInicial());
						venta.setStatusSolicitud(solicitud.getEstado());
						
					}
					
					boolean exito = scoringService.mergerSolicitud(solicitud);
					boolean exitoVenta = scoringService.mergerSolicitudVentaPuntual(venta);
					
					if(exito == true && exitoVenta == true){
						this.mensajeGlogal = "La solicitud "+ solicitud.getId().getNumSolicitud() +" fue aprobada.";
						List<ConceptoDTO> listaArchivoGuardadoResp = new ArrayList<ConceptoDTO>(0); 
						if(listaFileUploadedDTOs != null && listaFileUploadedDTOs.size() != 0){
							
							for(FileUploadedDTO archivo :listaFileUploadedDTOs){
							   try {							
									OutputStream ostream = null;
									String url = "archivosRespuesta"+"/"+ solicitud.getId().getNumSolicitud()+"_"+tipoSolicitudAux.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
									String nombreArchivo = solicitud.getId().getNumSolicitud()+"_"+tipoSolicitudAux.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
									InputStream istream = new FileInputStream(archivo.getUploadItem().getFile());
									
									//la direccion donde se rescata el archivo esta dado por el path del global parameter + la url formada arriba
									ostream = new FileOutputStream(globalParameters.getAttachedFilesAbsolutePath()+url);
									log.debug("url #0",archivo.getUploadItem().getContentType());
									IOUtils.copyStream(ostream, istream);
									ostream.flush();
									ostream.close();
									istream.close();
									//guardar datos en la BD
									ArchivoAdjunto archivoAux = new ArchivoAdjunto();
									archivoAux.setSolicitud(solicitud);
									archivoAux.setUrl(url);
									archivoAux.setNombreAdjunto(archivo.getNombreArchivo());
									entityManager.persist(archivoAux);
									entityManager.flush();
									ConceptoDTO objetoAux = new ConceptoDTO();
									objetoAux.setNombreArchivo(nombreArchivo);
									objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+url);
									listaArchivoGuardadoResp.add(objetoAux);
									objetoAux = null;
									
								} catch (FileNotFoundException e) {
									log.error("error al guardar archivo #0", e.toString());
									FacesMessages.instance().add(Severity.ERROR,"Error al guardar el archivo "+ archivo.getNombreArchivo());
								}
								catch (IOException e) {
									log.error("error al guardar archivo #0", e.toString());
									FacesMessages.instance().add(Severity.ERROR,"Error al guardar el archivo "+ archivo.getNombreArchivo());
								}catch (Exception e) {
									e.printStackTrace();
								}							
							}	
						}
						
						List<ConceptoDTO> lista = scoringService.getConceptoMonto(solicitud.getId().getNumSolicitud(), solicitud.getMonto().longValue());
						List<ConceptoDTO> listaProducto = scoringService.getProductos(solicitud.getId().getNumSolicitud());
						
						if(listaUsuarioCorreoagregados != null){
							Locale locale = new Locale("es","CL");
							EmailAlertaContactoMensajeHelper emailAlerta = null;
							/*setando los varlores del dto para el correo*/
							ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
							contenidoEmail.setAccionRespuesta("Aprobada la Solicitud "+ solicitud.getId().getNumSolicitud());
							contenidoEmail.setUsuarioAprobador(lcredUsuarioNivelEnc.getId().getNombreUsuario());
							contenidoEmail.setObservacionRespuesta(this.observacionesFinales);
							contenidoEmail.setSolicitud(solicitud);
							NumberFormat numberFormatter;
							StringBuffer rutNombre = new StringBuffer();
							rutNombre.append(cliente.getRut());
							rutNombre.append(" / ");
							rutNombre.append(cliente.getRazonSocial());
							contenidoEmail.setRutNombre(rutNombre.toString());
							contenidoEmail.setSoloNombre(cliente.getRazonSocial());
							contenidoEmail.setCanalVenta(cliente.getCanalVenta());
							contenidoEmail.setSucursal(sucursal.getDescripcion());
							contenidoEmail.setEmisor(usuarioLogueado.getNombre());
							contenidoEmail.setTipoSolicitud("Venta Puntual");
							contenidoEmail.setTipoVentas(tipoSolicitudAux.getDesTipoSolicitud());
							
							numberFormatter = NumberFormat.getNumberInstance(locale);
							log.debug(numberFormatter.format(venta.getMontoInicial()));
							
							contenidoEmail.setMontoMasIva(numberFormatter.format(venta.getMontoInicial()));
							contenidoEmail.setMontoPie(numberFormatter.format(venta.getMontoPieInicial()));
							contenidoEmail.setMargenNegocio(String.valueOf(numberFormatter.format(this.margenGlogal)));
							contenidoEmail.setFormaPago(this.formaPago);
							contenidoEmail.setMotivoCompra(this.motivoCompra);
							
							contenidoEmail.setPeakCredito(numberFormatter.format(expoRiesgoKh.getPeakCredito()));
							if("VIG".equals(cliente.getVigenciaSeguro().trim())){
								contenidoEmail.setLineaSeguro(numberFormatter.format(expoRiesgoKh.getMontoAsegurado()));
							}else{
								contenidoEmail.setLineaSeguro(numberFormatter.format(0));
							}								
							contenidoEmail.setRisgoKupfer(numberFormatter.format(expoRiesgoKh.getMontoRiesgoKupfer()));
							contenidoEmail.setListaProductos(listaProducto);
							contenidoEmail.setListaConceptoMontos(lista);
							contenidoEmail.setListaArchivos(listaArchivoGuardado);
							contenidoEmail.setListaArchivosRespuesta(listaArchivoGuardadoResp);
							
							for( SolicitudUsuarioCorreo usuarioCorreo: listaUsuarioCorreoagregados){
								try{
									if(emailAlerta == null){
										emailAlerta = (EmailAlertaContactoMensajeHelper) Component.getInstance("emailAlertaContactoMensajeHelper");
										contenidoEmail.setDestinatario(usuarioCorreo.getCorreo());
										contenidoEmail.setNombreUsuario(usuarioCorreo.getNombre());
										emailAlerta.enviarRespuestaSolicitudVenta(contenidoEmail);	
										log.debug("enviado respuesta de correo al usuario: #0",usuarioCorreo.getNombre());
										emailAlerta=null; 
									}	
								}catch (Exception e) {
									log.debug("paso por el error #0"+ e.getMessage());
								}
							}
						}
						
						insertarLogs(solicitud, 8);
						LcredEstado estado = ObtenerEstadoSolictud("A");
						insertHitos(solicitud, estado);
						entityManager.createQuery("update LcredSolicitud set evaluar=0 where id.numSolicitud=:sol")
						             .setParameter("sol", solicitud.getId().getNumSolicitud()).executeUpdate();
						entityManager.flush();

					}
			}	
		}
		

		}catch (Exception e) {
			log.error("Error al devolver la solicitud #0", e.getMessage());
		}
		return ;		
	}
	
	public void salirAprobarSolicitud(){
		  log.debug("salir del pop.");	
		  if(this.salirDelMensaje == false){
			  this.salirDelMensaje = true;
		  }
	}
	
	
	public void analizarSolicitud(){
		try{
			if(this.paginaIngreso == 11 || this.paginaIngreso == 12){	
				if(solicitud != null && venta != null){
					if(this.modalAnalista.equals("I")){
						solicitud.setEstado("E");
						venta.setStatusSolicitud("E");
						
						insertarLogs(solicitud, 9);
						LcredEstado estado = ObtenerEstadoSolictud("E");
						insertHitos(solicitud, estado);
						entityManager.createQuery("update LcredSolicitud set analizar=1 where id.numSolicitud=:sol")
						             .setParameter("sol", solicitud.getId().getNumSolicitud()).executeUpdate();
						entityManager.flush();	
					}else if(this.modalAnalista.equals("E")){
						solicitud.setEstado("N");
						venta.setStatusSolicitud("N");	
						
						insertarLogs(solicitud, 10);
						LcredEstado estado = ObtenerEstadoSolictud("N");
						insertHitos(solicitud, estado);
						entityManager.createQuery("update LcredSolicitud set analizar=1 where id.numSolicitud=:sol")
						             .setParameter("sol", solicitud.getId().getNumSolicitud()).executeUpdate();
						entityManager.flush();
					}
				}
			}else if(this.paginaIngreso == 21 || this.paginaIngreso == 22 || this.paginaIngreso == 23){
				
				
			}

		}catch (Exception e) {
			log.error("Error, al cambiar el estado de la solicitud", e.getMessage());
		}
	}
	
	public void salirAnalisisSolicitud(){
		  log.debug("salir del pop.");	
		  if(this.salirDelMensaje == false){
			  this.salirDelMensaje = true;
		  }
	}
	
	public void sacarAprobadores(){
		try{
			if(solicitud != null ){
				estadoLogHitos = scoringService.obtenerEstado(solicitud.getEstado());
//				List<SolicitudUsuarioDerivada> listaSolicitudesDerivada = scoringService
//						.getSolicitudUsuarioDerivacionForSolicitud(solicitud.getId().getNumSolicitud(), "SA");
				
				List<SolicitudUsuarioDerivada> listaSolicitudesDerivada = scoringService
						.getSolicitudUsuarioDerivacionForSolicitudMasEjecutivo(solicitud.getId().getNumSolicitud());				
				if(listaSolicitudesDerivada != null){
					this.setListUsuariosDerivados(listaSolicitudesDerivada);
				}
			}
		}catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud", e.getMessage());
		}
	}
	
	
	public void sacarLogSolicitudPagina() {
		try {
			if (this.solicitudLogHitos != null) {
				this.setEstadoLogHitos(scoringService.obtenerEstado(this.solicitudLogHitos.getEstado()));
				List<SolicitudLogs> listaSolicitudesLogs = scoringService.getSolicitudLogs(solicitudLogHitos.getId().getNumSolicitud());
				if (listaSolicitudesLogs != null) {
					this.setListaLogs(listaSolicitudesLogs);
				}
			}
		} catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud",
					e.getMessage());
		}

	}

	public void sacarLogHitosPagina() {
		try {
			if (this.solicitudLogHitos != null) {
				this.setEstadoLogHitos(scoringService.obtenerEstado(this.solicitudLogHitos.getEstado()));
				List<SolicitudHitos> listaSolicitudesHitos = scoringService
						.getSolicitudHitos(solicitudLogHitos.getId().getNumSolicitud());
				if (listaSolicitudesHitos != null) {
					this.setListaHitos(listaSolicitudesHitos);
					
				}
				List<SolicitudUsuarioDerivada> listaDerivadas = scoringService.getSolicitudUsuarioDerivacionForSolicitud(solicitudLogHitos.getId().getNumSolicitud());
				if(listaDerivadas != null && listaDerivadas.size() > 0){
					this.setListUsuariosDerivados(listaDerivadas);
				}
			}

		} catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud",
					e.getMessage());
		}
	}
	
	public void limpiarHitos(){
		this.listaHitos = null;
		this.listaLogs = null;
		this.listUsuariosDerivados = null;
	}
	
	
	public void sacarLogHitosSolicitudUsuarioDerivada(SolicitudHitos sol) {
		try {
			if (sol != null) {
				this.setSolicitudHitos(sol);
				List<SolicitudUsuarioDerivada> listaDerivadas = scoringService
						.getSolicitudUsuarioDerivacionForSolicitudEstado(sol.getIdSolicitud(), sol.getCodEstadoDerivada());
				if(listaDerivadas != null && listaDerivadas.size() > 0){
					globalHitosLogdService.setListUsuariosDerivados(listaDerivadas);
					this.setListUsuariosDerivados(listaDerivadas);
				}
			}

		} catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud",
					e.getMessage());
		}

	}
	public void limpiarVariblesUsuarioDerevida(){
		this.listUsuariosDerivados = null;
		this.solicitudHitos = null;
		
	}
	
	public void sacarPorcetajeGlobalConceptoNegocio(){
		  List<CotPedDTO>  listaPrincipal = null; //listaCotPedDTOs 
		  List<CabeceraCotPedDTO> lista = null;
		  List<DetalleCotPedDTO> listaDetalle = null;
		  Double sumaTotal = (double) 0;
		  Double suma = (double)0;
		  Double sumaConcepto = (double)0;
		  Double porcentajeNegocio = (double)0;
		  Double porcentajeMonto = (double)0;
		  Double margenConcepto = (double)0;
		 try{
			 if(listaCotPedDTOs != null){
				 listaPrincipal = new ArrayList<CotPedDTO>(0);
				for(CotPedDTO pc : listaCotPedDTOs){
					if(pc != null){
						 lista = new ArrayList<CabeceraCotPedDTO>(0);
						 for(CabeceraCotPedDTO o :pc.getListaCabeceraCotPeds()){
								 listaDetalle = new ArrayList<DetalleCotPedDTO>(0);
								 if(o.getListaDetalle() != null){
									for(DetalleCotPedDTO obj : o.getListaDetalle()){
										if(obj.getListaDetalleCp() != null){
											log.debug("monto general pedido  :"+ pc.getMontoTotal());
											log.debug("getPorcePorConcepto  :"+ obj.getPorcePorConcepto());
											log.debug("getMonto  :"+ obj.getMonto());
											/*calculo de porcentaje de articulos*/
											Double sumaDetalle = (double)0;
											for(DetalleCp dcp :  obj.getListaDetalleCp()){
												double calculoNew = ((dcp.getMargen() / 100) * ( dcp.getMargProdProc() / 100));
												log.debug("calculoNew : #0", calculoNew);
												sumaDetalle += calculoNew;
											}
											
											sumaDetalle = sumaDetalle * 100;
											log.debug("sumaDetalle : #0", sumaDetalle);
											
											porcentajeMonto = ((obj.getMonto().doubleValue() * 100)/obj.getMonto());
											obj.setPorceDelTotal(porcentajeMonto);
											sumaTotal += obj.getMonto().doubleValue(); 
											log.debug("porcentaje Monto : "+ porcentajeMonto);
											log.debug("(obj.getPorcePorConcepto() / 100):"+ (obj.getPorcePorConcepto() / 100));
											log.debug("(porcentajeMonto / 100):"+ (porcentajeMonto / 100));
											suma += ((obj.getPorcePorConcepto() / 100) * (porcentajeMonto / 100));
											sumaConcepto = ((obj.getPorcePorConcepto() / 100) * (porcentajeMonto / 100));
											log.debug("suma negocio:"+ suma);
											margenConcepto = (sumaConcepto * 100);
											log.debug("margen de concepto : "+ margenConcepto);
											obj.setMargenConcepto(margenConcepto);
											margenConcepto = (double)0;
										}
										listaDetalle.add(obj);
									}
									porcentajeNegocio = (suma * 100);
									log.debug("margen de suma de negocio : "+ porcentajeNegocio);
									o.setPorcentaje(porcentajeNegocio);
									o.setMontoGeneral(sumaTotal.longValue());
									suma = (double)0;
									sumaTotal  = (double)0;
								 }
								 o.setListaDetalle(listaDetalle);
								 lista.add(o);
						  }
					}
					listaPrincipal.add(pc);
				} 

				if(listaCotPedDTOs != null){
					 listaCotPedDTOs.clear();
					 setListaCotPedDTOs(listaPrincipal);
				 }else{
					 setListaCotPedDTOs(listaPrincipal);
				 }
			 }
		 }catch (Exception e) {
			log.error("error al insertar la lista de detalle a la lista de cabecera #0", e.getMessage());
		}
	  }
	public void sacarPorcetajeGlobal(){
	  List<CotPedDTO>  listaPrincipal = null; //listaCotPedDTOs 
		  List<CabeceraCotPedDTO> lista = null;
		  List<DetalleCotPedDTO> listaDetalle = null;
		  Double suma = (double)0;
		  Double porcentajeNegocio = (double)0;
		  Double porcentajeMonto = (double)0;
		  porcentajeGlobal = (double)0;
		  Double sumaPorcentajeGeneralNegocio = (double)0;
		 try{
			 if(listaCotPedDTOs != null){
				 listaPrincipal = new ArrayList<CotPedDTO>(0);
				for(CotPedDTO pc : listaCotPedDTOs){
					if(pc != null){
						 lista = new ArrayList<CabeceraCotPedDTO>(0);
						 for(CabeceraCotPedDTO o :pc.getListaCabeceraCotPeds()){
								 listaDetalle = new ArrayList<DetalleCotPedDTO>(0);
								 if(o.getListaDetalle() != null && o.getListaDetalle().size() == 1){
									for(DetalleCotPedDTO obj : o.getListaDetalle()){
										if(obj.getListaDetalleCp() != null){
											log.debug("monto general negocio  :"+ o.getMontoGeneral());
											log.debug("monto general del concepto  :"+ obj.getMonto());
											log.debug("getMargenConcepto  :"+ obj.getMargenConcepto());
											porcentajeMonto = ((obj.getMonto().doubleValue() * 100)/o.getMontoGeneral());
											log.debug("porcentaje Monto : "+ porcentajeMonto);
											log.debug("(obj.getMargenConcepto() / 100):"+ (obj.getMargenConcepto() / 100));
											log.debug("(porcentajeMonto / 100):"+ (porcentajeMonto / 100));
											log.debug(" ((obj.getMargenConcepto() / 100) * (porcentajeMonto / 100)):"+  ((obj.getMargenConcepto() / 100) * (porcentajeMonto / 100)));
											suma += ((obj.getMargenConcepto() / 100) * (porcentajeMonto / 100));
											
										}
										listaDetalle.add(obj);
									}
									porcentajeNegocio = (suma * 100);
									log.debug("margen de global (porcentajeNegocio): "+ porcentajeNegocio);
									Double representacion = (o.getMontoGeneral().doubleValue() *100)/pc.getMontoTotal();
									log.debug(" representacion :"+ representacion);
									o.setRepresentacion(representacion);
									o.setPorcentaje(porcentajeNegocio);
									sumaPorcentajeGeneralNegocio += porcentajeNegocio;
									suma = (double)0;
								 }else{
									Double sumaMontodeConceptosGenerales = (double)0;
									for(DetalleCotPedDTO obj : o.getListaDetalle()){
										if(obj.getListaDetalleCp() != null){
											log.debug("monto general negocio  :"+ o.getMontoGeneral());
											log.debug("monto general del concepto  :"+ obj.getMonto());
											log.debug("getMargenConcepto  :"+ obj.getMargenConcepto());
											porcentajeMonto = ((obj.getMonto().doubleValue() * 100)/o.getMontoGeneral());
											log.debug("porcentaje Monto : "+ porcentajeMonto);
											log.debug("(obj.getMargenConcepto() / 100):"+ (obj.getMargenConcepto() / 100));
											log.debug("(porcentajeMonto / 100):"+ (porcentajeMonto / 100));
											suma += ((obj.getMargenConcepto() / 100) * (porcentajeMonto / 100));
											sumaMontodeConceptosGenerales +=obj.getMonto();
										}
										listaDetalle.add(obj);
									}
									porcentajeNegocio = (suma * 100);
									log.debug("margen de global (porcentajeNegocio): "+ porcentajeNegocio);
									Double representacion = (sumaMontodeConceptosGenerales *100)/pc.getMontoTotal();
									log.debug(" representacion :"+ representacion);
									o.setRepresentacion(representacion);
									o.setPorcentaje(porcentajeNegocio);
									sumaPorcentajeGeneralNegocio += porcentajeNegocio;
									suma = (double)0;								 
								 }
								 
								 o.setListaDetalle(listaDetalle);
								 lista.add(o);
						  }
						 
						 
						 pc.setMargenGlobal(sumaPorcentajeGeneralNegocio);
						 sumaPorcentajeGeneralNegocio = (double)0;
						 
					}
					listaPrincipal.add(pc);
				} 

				if(listaCotPedDTOs != null){
					 listaCotPedDTOs.clear();
					 setListaCotPedDTOs(listaPrincipal);
				 }else{
					 setListaCotPedDTOs(listaPrincipal);
				 }
			 }
		 }catch (Exception e) {
			log.error("error al insertar la lista de detalle a la lista de cabecera #0", e.getMessage());
		}
	  }
	public void sacarPorcetajeCotizacionToPedido(){
		  List<CotPedDTO>  listaPrincipal = null; //listaCotPedDTOs 
		  Double porcentajeCotizacion = (double)0;
		  Double resultadoFinal = (double)0;
		  Double multiplicacion = (double)0;
		  try{
			 if(listaCotPedDTOs != null){
				 listaPrincipal = new ArrayList<CotPedDTO>(0);
				for(CotPedDTO pc : listaCotPedDTOs){
					if(pc != null){
						if(pc.getListaCabeceraCotPeds() != null && pc.getListaCabeceraCotPeds().size() == 1){
							CabeceraCotPedDTO negocio = null;
							porcentajeCotizacion =((pc.getMontoTotal().doubleValue() * 100)/pc.getMontoTotal());
							log.debug("porcentaje Cotizacion 1 :" + porcentajeCotizacion);
							
							negocio = pc.getListaCabeceraCotPeds().get(0);
							if(negocio != null){
								multiplicacion = (porcentajeCotizacion * (negocio.getPorcentaje()/100)); 
								log.debug("multiplicacion de porcentaje del pedido * el porcentaje del negocio :"+ multiplicacion);
								pc.setMargenGlobal(multiplicacion);
								pc.setPonderacion(multiplicacion);
							}else{
								pc.setMargenGlobal(porcentajeCotizacion);
								pc.setPonderacion(porcentajeCotizacion);
							}
						}else{
							for(CabeceraCotPedDTO obj : pc.getListaCabeceraCotPeds()){
								porcentajeCotizacion =((obj.getMontoGeneral().doubleValue() * 100)/pc.getMontoTotal());
								log.debug("porcentaje Cotizacion 2 :" + porcentajeCotizacion);
								log.debug("negocio.getPorcentaje() : "+ obj.getPorcentaje());
								resultadoFinal += (porcentajeCotizacion / 100) * (obj.getPorcentaje()/ 100);
							}
							if(resultadoFinal != 0){
								multiplicacion = (resultadoFinal*100); 
								log.debug("multiplicacion de porcentaje del pedido * el porcentaje del negocio :"+ multiplicacion);
								pc.setMargenGlobal(multiplicacion);	
								
								
								
							}else{
								pc.setMargenGlobal(porcentajeCotizacion);
								pc.setPonderacion(multiplicacion);
							}
						}
					}
					listaPrincipal.add(pc);
					porcentajeCotizacion = (double)0;
					resultadoFinal = (double)0;
					multiplicacion = (double)0;
				} 
				
				
				
				
				if(listaCotPedDTOs != null){
					 listaCotPedDTOs.clear();
					 setListaCotPedDTOs(listaPrincipal);
				 }else{
					 setListaCotPedDTOs(listaPrincipal);
				 }
			 }
		 }catch (Exception e) {
			log.error("error al insertar la lista de detalle a la lista de cabecera #0", e.getMessage());
		}
	  }
	public void sacarPorcetajePedidoGlobal(){
		  List<CotPedDTO>  listaPrincipal = null; //listaCotPedDTOs 
		  Double porcentajeCotizacion = (double)0;
		  Double multiplicacion = (double)0;
		  porcentajeGlobal = (double)0;
		  try{
			 if(listaCotPedDTOs != null && montoTotal != null && montoTotal.longValue() > 0){
				 listaPrincipal = new ArrayList<CotPedDTO>(0);
				for(CotPedDTO pc : listaCotPedDTOs){
					if(pc != null){
						 porcentajeCotizacion =((pc.getMontoTotal().doubleValue() * 100)/montoTotal);
						 log.debug("porcentaje pedido o cotizacion :" + porcentajeCotizacion);
						 multiplicacion = (porcentajeCotizacion * (pc.getMargenGlobal()/100));
						 log.debug("multiplicacion :" + multiplicacion);
						 porcentajeGlobal += multiplicacion;
						 pc.setPonderacion(multiplicacion);
					}
					
					listaPrincipal.add(pc);
					porcentajeCotizacion = (double)0;
					multiplicacion = (double)0;
				} 
				if(listaCotPedDTOs != null){
					 listaCotPedDTOs.clear();
					 setListaCotPedDTOs(listaPrincipal);
				 }else{
					 setListaCotPedDTOs(listaPrincipal);
				 }
			 }
		 }catch (Exception e) {
			log.error("error al insertar la lista de detalle a la lista de cabecera #0", e.getMessage());
		}
	  }	
	
	public void insertMontosEnListaConceptosMontos(){
		List<ConceptoMontoDTO> listaFinal = new ArrayList<ConceptoMontoDTO>(0);
		Long monto = new Long(0);
		this.montoGeneralConceptoMonto = 0;
		try{
			if(listaConceptosMontos != null){
				for(ConceptoMontoDTO cmd : listaConceptosMontos){
					cmd.setMonto(0);
					if(listaCotPedDTOs != null){
						for(CotPedDTO pc : listaCotPedDTOs){
							if(pc != null){
								 for(CabeceraCotPedDTO o :pc.getListaCabeceraCotPeds()){
									 if(o != null && o.getListaDetalle() != null){
										 for(DetalleCotPedDTO  dp :o.getListaDetalle()){
											 if((dp.getNegocio().getConcepto().trim()).equals(cmd.getConcepto().trim())){
												 monto+=dp.getMonto(); 
											 }											 
										 }
									 }
								 }
							}	 
						}
					 }
					if(monto != null && monto.longValue() > 0){
						cmd.setMarca("X");
						cmd.setMonto(monto);
						this.montoGeneralConceptoMonto +=monto;
						monto = new Long(0);
					}
					listaFinal.add(cmd);
				}
				this.setListaConceptosMontos(listaFinal);
			}
		}catch (Exception e) {
			log.error("Error al colcar lo montos dentro de la grilla ", e.getMessage());
		}
	}	
	
	
	/*gets y Sets*/
	public List<CotPedDTO> getListaCotPedDTOs() {
		return listaCotPedDTOs;
	}
	public void setListaCotPedDTOs(List<CotPedDTO> listaCotPedDTOs) {
		this.listaCotPedDTOs = listaCotPedDTOs;
	}
	
	public LcredSolicitud getSolicitud() {
		return solicitud;
	}
	public void setSolicitud(LcredSolicitud solicitud) {
		this.solicitud = solicitud;
	}
	public Usuariosegur getUsuarioLogueado() {
		return usuarioLogueado;
	}
	public void setUsuarioLogueado(Usuariosegur usuarioLogueado) {
		this.usuarioLogueado = usuarioLogueado;
	}
	public Date getFechaActual() {
		return fechaActual;
	}
	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}
	public LcredUsuarioNivelEnc getLcredUsuarioNivelEnc() {
		return lcredUsuarioNivelEnc;
	}
	public List<Sucursal> getListaSucursales() {
		return listaSucursales;
	}
	public void setListaSucursales(List<Sucursal> listaSucursales) {
		this.listaSucursales = listaSucursales;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public ClienteDTO getCliente() {
		return cliente;
	}
	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}
	public CompComercialDTO getComportamiento() {
		return comportamiento;
	}
	public void setComportamiento(CompComercialDTO comportamiento) {
		this.comportamiento = comportamiento;
	}
	public ExpoRiesgoKhDTO getExpoRiesgoKh() {
		return expoRiesgoKh;
	}
	public void setExpoRiesgoKh(ExpoRiesgoKhDTO expoRiesgoKh) {
		this.expoRiesgoKh = expoRiesgoKh;
	}
	public LcredEstado getLcredEstado() {
		return lcredEstado;
	}

	public void setLcredEstado(LcredEstado lcredEstado) {
		this.lcredEstado = lcredEstado;
	}

	public String getPagina() {
		return pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}

	public Long getIdSolicitud() {
		return idSolicitud;
	}


	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public Long getSolicitudSeleccionada() {
		return solicitudSeleccionada;
	}

	public void setSolicitudSeleccionada(Long solicitudSeleccionada) {
		this.solicitudSeleccionada = solicitudSeleccionada;
	}

	@BypassInterceptors public String getFechaDeauda() {
		return fechaDeauda;
	}

	@BypassInterceptors public void setFechaDeauda(String fechaDeauda) {
		this.fechaDeauda = fechaDeauda;
	}

	@BypassInterceptors public Date getFechaActualCtaCte() {
		return fechaActualCtaCte;
	}

	@BypassInterceptors public void setFechaActualCtaCte(Date fechaActualCtaCte) {
		this.fechaActualCtaCte = fechaActualCtaCte;
	}

	public void setCtaCteList(List<DeudaActual> ctaCteList) {
		this.ctaCteList = ctaCteList;
	}

	public List<String> getListaFechas() {
		return listaFechas;
	}

	public void setListaFechas(List<String> listaFechas) {
		this.listaFechas = listaFechas;
	}
	public int getPaginaIngreso() {
		return paginaIngreso;
	}
	public void setPaginaIngreso(int paginaIngreso) {
		this.paginaIngreso = paginaIngreso;
	}
	public String getTituloIngreso() {
		return tituloIngreso;
	}
	public void setTituloIngreso(String tituloIngreso) {
		this.tituloIngreso = tituloIngreso;
	}
	public Long getMontoVtaPuntual() {
		return montoVtaPuntual;
	}
	public void setMontoVtaPuntual(Long montoVtaPuntual) {
		this.montoVtaPuntual = montoVtaPuntual;
	}
	public Long getPieVentaPuntual() {
		return pieVentaPuntual;
	}
	public void setPieVentaPuntual(Long pieVentaPuntual) {
		this.pieVentaPuntual = pieVentaPuntual;
	}
	public LcredSolicitudLcredito getCredito() {
		return credito;
	}
	public void setCredito(LcredSolicitudLcredito credito) {
		this.credito = credito;
	}
	public LcredSolicitudCondiciones getCondicion() {
		return condicion;
	}
	public void setCondicion(LcredSolicitudCondiciones condicion) {
		this.condicion = condicion;
	}
	public LcredSolicitudBloqueos getBloqueo() {
		return bloqueo;
	}
	public void setBloqueo(LcredSolicitudBloqueos bloqueo) {
		this.bloqueo = bloqueo;
	}
	public List<LcredSolicitudDm> getListaDms() {
		return listaDms;
	}
	public void setListaDms(List<LcredSolicitudDm> listaDms) {
		this.listaDms = listaDms;
	}
	public List<LcredSolicitudProrroga> getListaProrrogas() {
		return listaProrrogas;
	}
	public void setListaProrrogas(List<LcredSolicitudProrroga> listaProrrogas) {
		this.listaProrrogas = listaProrrogas;
	}
	public void setLcredUsuarioNivelEnc(LcredUsuarioNivelEnc lcredUsuarioNivelEnc) {
		this.lcredUsuarioNivelEnc = lcredUsuarioNivelEnc;
	}
	public String getMotivoCompra() {
		return motivoCompra;
	}
	public void setMotivoCompra(String motivoCompra) {
		this.motivoCompra = motivoCompra;
	}
	public String getObservacionesCompra() {
		return observacionesCompra;
	}
	public void setObservacionesCompra(String observacionesCompra) {
		this.observacionesCompra = observacionesCompra;
	}
	public Double getMargenGlogal() {
		return margenGlogal;
	}
	public void setMargenGlogal(Double margenGlogal) {
		this.margenGlogal = margenGlogal;
	}
	public String getProductos() {
		return productos;
	}
	public void setProductos(String productos) {
		this.productos = productos;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public List<ConceptoDTO> getListaArchivoGuardado() {
		return listaArchivoGuardado;
	}
	public void setListaArchivoGuardado(List<ConceptoDTO> listaArchivoGuardado) {
		this.listaArchivoGuardado = listaArchivoGuardado;
	}
	public String getObservacionesFinales() {
		return observacionesFinales;
	}
	public void setObservacionesFinales(String observacionesFinales) {
		this.observacionesFinales = observacionesFinales;
	}
	public String getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	public LcredTipoSolicitud getTipoSolicitudAux() {
		return tipoSolicitudAux;
	}
	public void setTipoSolicitudAux(LcredTipoSolicitud tipoSolicitudAux) {
		this.tipoSolicitudAux = tipoSolicitudAux;
	}
	@BypassInterceptors public String getObservacionesModel() {
		return observacionesModel;
	}
	@BypassInterceptors public void setObservacionesModel(String observacionesModel) {
		this.observacionesModel = observacionesModel;
	}
	public List<LcredSolicitudObservaciones> getListaObservaciones() {
		return listaObservaciones;
	}
	public void setListaObservaciones(
			List<LcredSolicitudObservaciones> listaObservaciones) {
		this.listaObservaciones = listaObservaciones;
	}
	public String getMensajeGlogal() {
		return mensajeGlogal;
	}
	public void setMensajeGlogal(String mensajeGlogal) {
		this.mensajeGlogal = mensajeGlogal;
	}
	public String getPaginaVolver() {
		return paginaVolver;
	}
	public void setPaginaVolver(String paginaVolver) {
		this.paginaVolver = paginaVolver;
	}
	public boolean isSalirDelMensaje() {
		return salirDelMensaje;
	}
	public void setSalirDelMensaje(boolean salirDelMensaje) {
		this.salirDelMensaje = salirDelMensaje;
	}
	public List<FileUploadedDTO> getListaFileUploadedDTOs() {
		return listaFileUploadedDTOs;
	}
	public void setListaFileUploadedDTOs(List<FileUploadedDTO> listaFileUploadedDTOs) {
		this.listaFileUploadedDTOs = listaFileUploadedDTOs;
	}
	public String getNormalCuota() {
		return normalCuota;
	}
	public void setNormalCuota(String normalCuota) {
		this.normalCuota = normalCuota;
	}
	public Long getMontoNuevo() {
		return montoNuevo;
	}
	public void setMontoNuevo(Long montoNuevo) {
		this.montoNuevo = montoNuevo;
	}
	public Long getPieNuevo() {
		return pieNuevo;
	}
	public void setPieNuevo(Long pieNuevo) {
		this.pieNuevo = pieNuevo;
	}

	public String getInnNomRiesgo() {
		return innNomRiesgo;
	}

	public void setInnNomRiesgo(String innNomRiesgo) {
		this.innNomRiesgo = innNomRiesgo;
	}
	public Long getMontoLineaCredito() {
		return montoLineaCredito;
	}
	public void setMontoLineaCredito(Long montoLineaCredito) {
		this.montoLineaCredito = montoLineaCredito;
	}
	public String getcPago() {
		return cPago;
	}
	public void setcPago(String cPago) {
		this.cPago = cPago;
	}
	public String getClsRiesgo() {
		return clsRiesgo;
	}
	public void setClsRiesgo(String clsRiesgo) {
		this.clsRiesgo = clsRiesgo;
	}
	public String getcPagoNuevo() {
		return cPagoNuevo;
	}
	public void setcPagoNuevo(String cPagoNuevo) {
		this.cPagoNuevo = cPagoNuevo;
	}
	public String getClsRiesgoNuevo() {
		return clsRiesgoNuevo;
	}
	public void setClsRiesgoNuevo(String clsRiesgoNuevo) {
		this.clsRiesgoNuevo = clsRiesgoNuevo;
	}
	public List<CPagoDTO> getListaCondicionPagos() {
		return listaCondicionPagos;
	}
	public void setListaCondicionPagos(List<CPagoDTO> listaCondicionPagos) {
		this.listaCondicionPagos = listaCondicionPagos;
	}
	public List<ClsRiesgoDTO> getListaClsRiesgos() {
		return listaClsRiesgos;
	}
	public void setListaClsRiesgos(List<ClsRiesgoDTO> listaClsRiesgos) {
		this.listaClsRiesgos = listaClsRiesgos;
	}
	public List<String> getListaStringClsRiesgos() {
		return listaStringClsRiesgos;
	}
	public void setListaStringClsRiesgos(List<String> listaStringClsRiesgos) {
		this.listaStringClsRiesgos = listaStringClsRiesgos;
	}
	public List<String> getListaStringCdnPagos() {
		return listaStringCdnPagos;
	}
	public void setListaStringCdnPagos(List<String> listaStringCdnPagos) {
		this.listaStringCdnPagos = listaStringCdnPagos;
	}
	public List<String> getListaStringClsRiesgosNuevos() {
		return listaStringClsRiesgosNuevos;
	}
	public void setListaStringClsRiesgosNuevos(
			List<String> listaStringClsRiesgosNuevos) {
		this.listaStringClsRiesgosNuevos = listaStringClsRiesgosNuevos;
	}
	public List<String> getListaStringCdnPagosNuevos() {
		return listaStringCdnPagosNuevos;
	}
	public void setListaStringCdnPagosNuevos(List<String> listaStringCdnPagosNuevos) {
		this.listaStringCdnPagosNuevos = listaStringCdnPagosNuevos;
	}
	public String getInnNomRiesgoNueva() {
		return innNomRiesgoNueva;
	}
	public void setInnNomRiesgoNueva(String innNomRiesgoNueva) {
		this.innNomRiesgoNueva = innNomRiesgoNueva;
	}
	public Long getMontoLineaCreditoNuevo() {
		return montoLineaCreditoNuevo;
	}
	public void setMontoLineaCreditoNuevo(Long montoLineaCreditoNuevo) {
		this.montoLineaCreditoNuevo = montoLineaCreditoNuevo;
	}

	public String getDescripcionProyecto() {
		return descripcionProyecto;
	}
	public void setDescripcionProyecto(String descripcionProyecto) {
		this.descripcionProyecto = descripcionProyecto;
	}
	public Long getMontoCredito() {
		return montoCredito;
	}
	public void setMontoCredito(Long montoCredito) {
		this.montoCredito = montoCredito;
	}
	public String getPlazoEjecucion() {
		return plazoEjecucion;
	}
	public void setPlazoEjecucion(String plazoEjecucion) {
		this.plazoEjecucion = plazoEjecucion;
	}
	public Long getPotencialCompra() {
		return potencialCompra;
	}
	public void setPotencialCompra(Long potencialCompra) {
		this.potencialCompra = potencialCompra;
	}
	public String getConceptoNegocios() {
		return conceptoNegocios;
	}
	public void setConceptoNegocios(String conceptoNegocios) {
		this.conceptoNegocios = conceptoNegocios;
	}
	public String getRutNombresSocios() {
		return rutNombresSocios;
	}
	public void setRutNombresSocios(String rutNombresSocios) {
		this.rutNombresSocios = rutNombresSocios;
	}
	public String getObservacionesCredito() {
		return observacionesCredito;
	}
	public void setObservacionesCredito(String observacionesCredito) {
		this.observacionesCredito = observacionesCredito;
	}
	public boolean isCondicionRiesgo() {
		return condicionRiesgo;
	}
	public void setCondicionRiesgo(boolean condicionRiesgo) {
		this.condicionRiesgo = condicionRiesgo;
	}
	public boolean isCondicionPago() {
		return condicionPago;
	}
	public void setCondicionPago(boolean condicionPago) {
		this.condicionPago = condicionPago;
	}
	public boolean isComboCRiesgo() {
		return comboCRiesgo;
	}
	public void setComboCRiesgo(boolean comboCRiesgo) {
		this.comboCRiesgo = comboCRiesgo;
	}
	public boolean isComboCPago() {
		return comboCPago;
	}
	public void setComboCPago(boolean comboCPago) {
		this.comboCPago = comboCPago;
	}
	public String getMotivoRiesgoPago() {
		return motivoRiesgoPago;
	}
	public void setMotivoRiesgoPago(String motivoRiesgoPago) {
		this.motivoRiesgoPago = motivoRiesgoPago;
	}
	public boolean isCondicionRiesgoNew() {
		return condicionRiesgoNew;
	}
	public void setCondicionRiesgoNew(boolean condicionRiesgoNew) {
		this.condicionRiesgoNew = condicionRiesgoNew;
	}
	public boolean isCondicionPagoNew() {
		return condicionPagoNew;
	}
	public void setCondicionPagoNew(boolean condicionPagoNew) {
		this.condicionPagoNew = condicionPagoNew;
	}
	public String getOpcionBloqueoDesbloqueo() {
		return opcionBloqueoDesbloqueo;
	}
	public void setOpcionBloqueoDesbloqueo(String opcionBloqueoDesbloqueo) {
		this.opcionBloqueoDesbloqueo = opcionBloqueoDesbloqueo;
	}
	public String getMotivoBloDesbloqueo() {
		return motivoBloDesbloqueo;
	}
	public void setMotivoBloDesbloqueo(String motivoBloDesbloqueo) {
		this.motivoBloDesbloqueo = motivoBloDesbloqueo;
	}
	public String getOpcionBloqueoDesbloqueoNew() {
		return opcionBloqueoDesbloqueoNew;
	}
	public void setOpcionBloqueoDesbloqueoNew(String opcionBloqueoDesbloqueoNew) {
		this.opcionBloqueoDesbloqueoNew = opcionBloqueoDesbloqueoNew;
	}
	@BypassInterceptors public SolicitudUsuarioCorreo getUsuarioCorreo() {
		return usuarioCorreo;
	}
	@BypassInterceptors public void setUsuarioCorreo(SolicitudUsuarioCorreo usuarioCorreo) {
		this.usuarioCorreo = usuarioCorreo;
	}
	public String getMensajeExplicativo() {
		return mensajeExplicativo;
	}
	public void setMensajeExplicativo(String mensajeExplicativo) {
		this.mensajeExplicativo = mensajeExplicativo;
	}
	public List<DestinatarioDTO> getListaDestinatarios() {
		return listaDestinatarios;
	}
	public void setListaDestinatarios(List<DestinatarioDTO> listaDestinatarios) {
		this.listaDestinatarios = listaDestinatarios;
	}
	public List<SolicitudUsuarioCorreo> getListaUsuarioCorreoagregados() {
		return listaUsuarioCorreoagregados;
	}
	public void setListaUsuarioCorreoagregados(
			List<SolicitudUsuarioCorreo> listaUsuarioCorreoagregados) {
		this.listaUsuarioCorreoagregados = listaUsuarioCorreoagregados;
	}
	public List<SolicitudUsuarioCorreo> getListaDescripcionUsuariosCorreos() {
		return listaDescripcionUsuariosCorreos;
	}
	public void setListaDescripcionUsuariosCorreos(
			List<SolicitudUsuarioCorreo> listaDescripcionUsuariosCorreos) {
		this.listaDescripcionUsuariosCorreos = listaDescripcionUsuariosCorreos;
	}
	public List<SolicitudUsuarioCorreo> getListaUsuarioCorreos() {
		return listaUsuarioCorreos;
	}
	public void setListaUsuarioCorreos(List<SolicitudUsuarioCorreo> listaUsuarioCorreos) {
		this.listaUsuarioCorreos = listaUsuarioCorreos;
	}
	public List<LcredSolicitudDm> getListaClienteDMs() {
		return listaClienteDMs;
	}
	public void setListaClienteDMs(List<LcredSolicitudDm> listaClienteDMs) {
		this.listaClienteDMs = listaClienteDMs;
	}
	public String getMotivoProrroga() {
		return motivoProrroga;
	}
	public void setMotivoProrroga(String motivoProrroga) {
		this.motivoProrroga = motivoProrroga;
	}
	public String getAnalisisAprobacion() {
		return analisisAprobacion;
	}
	public void setAnalisisAprobacion(String analisisAprobacion) {
		this.analisisAprobacion = analisisAprobacion;
	}
	@BypassInterceptors public String getMensajeAnalisisAprobacion() {
		return mensajeAnalisisAprobacion;
	}
	@BypassInterceptors public void setMensajeAnalisisAprobacion(String mensajeAnalisisAprobacion) {
		this.mensajeAnalisisAprobacion = mensajeAnalisisAprobacion;
	}
	public List<SolicitudUsuarioCorreo> getListaCorreoAnaDer() {
		return listaCorreoAnaDer;
	}
	public void setListaCorreoAnaDer(List<SolicitudUsuarioCorreo> listaCorreoAnaDer) {
		this.listaCorreoAnaDer = listaCorreoAnaDer;
	}



	public LcredMotivoRechazo getMotivoRechazo() {
		return motivoRechazo;
	}



	public void setMotivoRechazo(LcredMotivoRechazo motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}



	public List<LcredMotivoRechazo> getListaMotivosRechazos() {
		return listaMotivosRechazos;
	}



	public void setListaMotivosRechazos(
			List<LcredMotivoRechazo> listaMotivosRechazos) {
		this.listaMotivosRechazos = listaMotivosRechazos;
	}



	public List<LcredMotivoRechazo> getListaMotivosRechazosAgregados() {
		return listaMotivosRechazosAgregados;
	}



	public void setListaMotivosRechazosAgregados(
			List<LcredMotivoRechazo> listaMotivosRechazosAgregados) {
		this.listaMotivosRechazosAgregados = listaMotivosRechazosAgregados;
	}



	public String getMensajeRechazoSolicitud() {
		return mensajeRechazoSolicitud;
	}



	public void setMensajeRechazoSolicitud(String mensajeRechazoSolicitud) {
		this.mensajeRechazoSolicitud = mensajeRechazoSolicitud;
	}



	public boolean isModificarVenta() {
		return modificarVenta;
	}



	public void setModificarVenta(boolean modificarVenta) {
		this.modificarVenta = modificarVenta;
	}



	public String getModalAnalista() {
		return modalAnalista;
	}



	public void setModalAnalista(String modalAnalista) {
		this.modalAnalista = modalAnalista;
	}
	public String getTabGrilla() {
		return tabGrilla;
	}
	public void setTabGrilla(String tabGrilla) {
		this.tabGrilla = tabGrilla;
	}

	public BotonesDTO getBotonera() {
		return botonera;
	}

	public void setBotonera(BotonesDTO botonera) {
		this.botonera = botonera;
	}

	public List<ConceptoDTO> getListaArchivoGuardadoRespuesta() {
		return listaArchivoGuardadoRespuesta;
	}

	public void setListaArchivoGuardadoRespuesta(
			List<ConceptoDTO> listaArchivoGuardadoRespuesta) {
		this.listaArchivoGuardadoRespuesta = listaArchivoGuardadoRespuesta;
	}

	public List<ConceptoMontoDTO> getListaConceptosMontos() {
		return listaConceptosMontos;
	}

	public void setListaConceptosMontos(List<ConceptoMontoDTO> listaConceptosMontos) {
		this.listaConceptosMontos = listaConceptosMontos;
	}

	public long getMontoGeneralConceptoMonto() {
		return montoGeneralConceptoMonto;
	}

	public void setMontoGeneralConceptoMonto(long montoGeneralConceptoMonto) {
		this.montoGeneralConceptoMonto = montoGeneralConceptoMonto;
	}

	public String getUsuarioEmisor() {
		return usuarioEmisor;
	}

	public void setUsuarioEmisor(String usuarioEmisor) {
		this.usuarioEmisor = usuarioEmisor;
	}

	public List<SolicitudHitos> getListaHitos() {
		return listaHitos;
	}

	public void setListaHitos(List<SolicitudHitos> listaHitos) {
		this.listaHitos = listaHitos;
	}

	public List<SolicitudLogs> getListaLogs() {
		return listaLogs;
	}

	public void setListaLogs(List<SolicitudLogs> listaLogs) {
		this.listaLogs = listaLogs;
	}

	public List<SolicitudUsuarioDerivada> getListUsuariosDerivados() {
		return listUsuariosDerivados;
	}

	public void setListUsuariosDerivados(
			List<SolicitudUsuarioDerivada> listUsuariosDerivados) {
		this.listUsuariosDerivados = listUsuariosDerivados;
	}

	public LcredEstado getEstadoLogHitos() {
		return estadoLogHitos;
	}

	public void setEstadoLogHitos(LcredEstado estadoLogHitos) {
		this.estadoLogHitos = estadoLogHitos;
	}

	public LcredSolicitud getSolicitudLogHitos() {
		return solicitudLogHitos;
	}

	public void setSolicitudLogHitos(LcredSolicitud solicitudLogHitos) {
		this.solicitudLogHitos = solicitudLogHitos;
	}

	public SolicitudHitos getSolicitudHitos() {
		return solicitudHitos;
	}

	public void setSolicitudHitos(SolicitudHitos solicitudHitos) {
		this.solicitudHitos = solicitudHitos;
	}

	public Double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public Double getMontoTotalNeto() {
		return montoTotalNeto;
	}

	public void setMontoTotalNeto(Double montoTotalNeto) {
		this.montoTotalNeto = montoTotalNeto;
	}

	public Double getPorcentajeGlobal() {
		return porcentajeGlobal;
	}

	public void setPorcentajeGlobal(Double porcentajeGlobal) {
		this.porcentajeGlobal = porcentajeGlobal;
	}	
	
	
	
}
