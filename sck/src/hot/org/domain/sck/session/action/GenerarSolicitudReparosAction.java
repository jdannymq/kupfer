package org.domain.sck.session.action;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import modelo.SapSystem;

import org.domain.sck.base.GlobalHitosLogdService;
import org.domain.sck.base.GlobalService;
import org.domain.sck.dto.CPagoDTO;
import org.domain.sck.dto.CabeceraCotPedDTO;
import org.domain.sck.dto.ClienteDTO;
import org.domain.sck.dto.ClsRiesgoDTO;
import org.domain.sck.dto.CompComercialDTO;
import org.domain.sck.dto.ConceptoDTO;
import org.domain.sck.dto.ConceptoMontoDTO;
import org.domain.sck.dto.ConceptoNegocioDTO;
import org.domain.sck.dto.CotPedDTO;
import org.domain.sck.dto.CreacionDmDTO;
import org.domain.sck.dto.DMDTO;
import org.domain.sck.dto.DestinatarioDTO;
import org.domain.sck.dto.DetalleCotPedDTO;
import org.domain.sck.dto.DocumentoProrrogaDTO;
import org.domain.sck.dto.ExpoRiesgoKhDTO;
import org.domain.sck.dto.FileUploadedDTO;
import org.domain.sck.dto.FormaPagoDTO;
import org.domain.sck.dto.ProrrogaDTO;
import org.domain.sck.dto.SolicitudDTO;
import org.domain.sck.dto.SolicitudEstadoDTO;
import org.domain.sck.dto.UsuarioCorreoDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.entity.ArchivoAdjunto;
import org.domain.sck.entity.Attachment;
import org.domain.sck.entity.CanalUsuarioCargo;
import org.domain.sck.entity.ConceptosNegocio;
import org.domain.sck.entity.CotizacionPedido;
import org.domain.sck.entity.CotizacionPedidoCabecera;
import org.domain.sck.entity.CotizacionPedidoConcepto;
import org.domain.sck.entity.CotizacionPedidoNegocio;
import org.domain.sck.entity.DetalleCp;
import org.domain.sck.entity.DeudaActual;
import org.domain.sck.entity.Estados;
import org.domain.sck.entity.FormaPagoDetalle;
import org.domain.sck.entity.FormaPagoEncabezado;
import org.domain.sck.entity.LcredEstado;
import org.domain.sck.entity.LcredPerfiles;
import org.domain.sck.entity.LcredSolicitud;
import org.domain.sck.entity.LcredSolicitudBloqueos;
import org.domain.sck.entity.LcredSolicitudCondiciones;
import org.domain.sck.entity.LcredSolicitudDm;
import org.domain.sck.entity.LcredSolicitudDmId;
import org.domain.sck.entity.LcredSolicitudLcredito;
import org.domain.sck.entity.LcredSolicitudObservaciones;
import org.domain.sck.entity.LcredSolicitudObservacionesId;
import org.domain.sck.entity.LcredSolicitudOtras;
import org.domain.sck.entity.LcredSolicitudProrroga;
import org.domain.sck.entity.LcredSolicitudProrrogaId;
import org.domain.sck.entity.LcredSolicitudVtapuntual;
import org.domain.sck.entity.LcredTipoSolicitud;
import org.domain.sck.entity.LcredUsuarioNivelEnc;
import org.domain.sck.entity.PerfilFuncionCanal;
import org.domain.sck.entity.SolicitudConceptosNegocioLC;
import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudLogs;
import org.domain.sck.entity.SolicitudTipoFormaPago;
import org.domain.sck.entity.SolicitudUsuarioCorreo;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.TipoFormaPago;
import org.domain.sck.entity.Ufs;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.entity.ZonaSucursalNegocioConcepto;
import org.domain.sck.entity.enums.ArchivoAdjuntoType;
import org.domain.sck.entity.enums.ConceptosType;
import org.domain.sck.entity.enums.FuncionesType;
import org.domain.sck.entity.enums.TipoCuentasKupferType;
import org.domain.sck.entity.enums.TipoSolicitudType;
import org.domain.sck.seam.GlobalParameters;
import org.domain.sck.session.home.ContenidoEmailSolicitudDTO;
import org.domain.sck.session.home.EmailAlertaContactoMensajeHelper;
import org.domain.sck.session.service.intranetsap.IntranetSapService;
import org.domain.sck.session.service.scoring.ScoringService;
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



@Name("generarSolicitudReparosAction")
@Scope(ScopeType.CONVERSATION)
public class GenerarSolicitudReparosAction implements Serializable{

	@Logger
	Log log;
	

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	@In IntranetSapService intranetSapService;
	
	@In GlobalService globalService;
	
	@In ScoringService scoringService;
	
	@In GlobalHitosLogdService globalHitosLogdService;
	
	@In GlobalParameters globalParameters;
	
	@In(value="#{identity.usuarioLogueado}", scope = ScopeType.SESSION)
	private Usuariosegur usuarioLogueado;
	
	@In(value="#{identity.lcredUsuarioNivelEnc}", scope = ScopeType.SESSION)
	private LcredUsuarioNivelEnc lcredUsuarioNivelEnc;

	@In(value="#{identity.usuarioSegur}", scope = ScopeType.SESSION)
	private UsuarioSegurDTO usuarioSegur;
	
	@In(value="#{identity.usuarioCargo}", scope = ScopeType.SESSION)
	private UsuarioCargo usuarioCargoAux;
	
	@In(value = "#{emailAlertaContactoMensajeHelper}")
	EmailAlertaContactoMensajeHelper emailAlerta;
	
	
	/*variables de pagina*/
	private LcredSolicitud solicitud;
	private Sucursal sucursal;
	public Map<String, Boolean>  menu;
	private String selectedMenuItem;
	private int paginaIngreso;
	private Long solicitudSeleccionada;
	private String opcionBloqueoDesbloqueo;
	private String tituloGeneral;
	private String tituloIngreso;
	private String paginaVolver;
	private String producto;
	private String usuarioCorreo;
	private FormaPagoDTO formaPago;
	private String mensajeExplicativo;
	private Map<String, LcredTipoSolicitud> dataTipoSolicitud = new HashMap<String, LcredTipoSolicitud>();
	private boolean condicionRiesgo;
	private boolean condicionPago;
	private boolean comboCRiesgo;
	private boolean comboCPago;
	private boolean motivoBloqueoDesbloqueo;
	private String dm;
	private Date fechaActual;
	private boolean habilitarCtaCte;
	private LcredEstado estadoInicial;
	private TipoCuentasKupferType canalVenta;
	private Long monto;
	private Long pie;
	private Long pedidoCotizacion;
	private LcredTipoSolicitud tipoSolicitud;
	private boolean habilitarVerProductos;
	private FormaPagoEncabezado encabezado;
	private TipoFormaPago tipoFormaPago;
	private boolean muestraSeleccionSolicitud;
	private Double montoTotal = (double) 0;
	private Double montoTotalNeto = (double) 0;
	private Double margenGlobal;
	private String motivoCompra;
	private String observaciones;
	private String cPago;
	private String clsRiesgo;
	private Date fechaActualCtaCte;
	private Double porcentajeGlobal;
	private boolean evaluadorCompraVC; 
	private boolean activarConsulta;
	private boolean activarCboClsRiesgo;
	private boolean activarCboCdnPago;
	private ConceptosNegocio conceptoNegocioCbo;
	private String rutSocio;
	private String socio;
	private boolean salirDelMensaje;
	private String fechaDeauda;
	private String mensajeGlogal;
	private Long montoConcepto;
	private int contadorStatus; 
	private int contadorStatusFecha;
	private String mensajeCreacionDm;
	private String motivoProrroga;
	private String tiempoEjecucionProceso;
	private String tiempoEjecucionMenu;
	private String tiempoEjecucionModal;
	private String tiempoEjecucionValidar;
    private LcredSolicitudVtapuntual venta = null;
    private LcredSolicitudLcredito credito = null;
    private LcredSolicitudCondiciones condicion = null;
    private LcredSolicitudBloqueos bloqueo = null;
    private List<LcredSolicitudDm>  listaDms = null;
    private List<LcredSolicitudProrroga> listaProrrogas = null;
    private LcredSolicitudOtras solicitudOtra = null;
    private SolicitudTipoFormaPago stfp;
	private boolean habilitar;
    private List<ConceptoDTO> listaArchivoGuardado = null;
    private List<LcredSolicitudObservaciones> listaObservaciones = null;
	private List<SolicitudUsuarioCorreo> listaSolicitudUsuarioCorresSessions;
	private String tipoSolicitudCodigo;

	
	
	
	
	

	/*lista de paginas*/
	private List<Sucursal> listaSucursales;
	private List<String> listaDescripcionProductos;
	private List<String> listaDescripcionUsuariosCorreos;
	private List<FormaPagoDTO> listaFormaPagoDTOs;
	private List<UsuarioCorreoDTO> listaUsuarioCorreos;
	private List<LcredSolicitudDm> listaClienteDMs;
	private List<DocumentoProrrogaDTO> listaDoctoProrroga;
	private List<DocumentoProrrogaDTO> listaDoctoCuentaCorriente;
	private List<ConceptoMontoDTO> listaConceptosMontos;
	private List<ConceptoMontoDTO> listaConceptosIncluidos;
    private List<String> listaDescripcionDM;
    private List<DMDTO> listaDmdtos;
    private List<DestinatarioDTO> listaDestinatarios; 
    private List<TipoFormaPago> listaTipoFormaPagos;
    private List<FormaPagoEncabezado> listaEncabezados;
    private List<CabeceraCotPedDTO> listaCabeceraCotPeds;
    private List<DetalleCotPedDTO> listaDetalleCotPeds;
    private List<CPagoDTO> listaCondicionPagos;
    private List<ClsRiesgoDTO> listaClsRiesgos;
    private List<CotPedDTO> listaCotPedDTOs;
    private List<String> listaStringClsRiesgos;
    private List<String> listaStringCdnPagos;
    private List<ConceptosNegocio> listaCboConceptoNegocio;
    private List<ConceptosNegocio> listaCboConceptoNegocioSeleccion;
    private List<ConceptoNegocioDTO> listaSelConceptoNegocio;
    private List<String> listaSocios;
    private List<String> listaFechas;
    private List<SolicitudEstadoDTO> listaSolicitudesEstados;
	private List<SolicitudDTO> listaSolicitudesPendientesAprobarDto;
	private List<SolicitudDTO> listaSolicitudesPendientesAnalizarDto;
	private List<SolicitudDTO> listaMisSolicitudesDto;
	private List<SolicitudDTO> listaTodasSolicitudesDto;
	private List<LcredEstado> listaEstados;
	private List<CanalUsuarioCargo> listaCanalUsuarioCargos;
	private List<PerfilFuncionCanal> listaPerfilFuncionCanals;
	private List<ZonaSucursalNegocioConcepto> listaZonaSucursalNegocioConceptos;
	private List<ConceptoNegocioDTO> listaProductosagregados = new ArrayList<ConceptoNegocioDTO>(0);
	private List<SolicitudUsuarioCorreo> listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
	private List<FileUploadedDTO> listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
	private List<DocumentoProrrogaDTO> listaDoctoProrrogaAgregados = new ArrayList<DocumentoProrrogaDTO>(0);
	private List<CotizacionPedido> cotizacionPedidosList = new ArrayList<CotizacionPedido>(0);
	

	/*Funciones Sap*/
	private JCoFunction functionDatosClientes;
	private JCoFunction functionCtaCte;
	
	/*DTO que recibe datos de cliente desde Sap*/
	ClienteDTO clienteTarget;
	public ClienteDTO getClienteTarget() {return clienteTarget;}
	
	CompComercialDTO clienteCompComrcial;
	public CompComercialDTO getClienteCompComrcial() {return clienteCompComrcial;}
	
	ExpoRiesgoKhDTO clienteExpoRiesgoKh;
	public ExpoRiesgoKhDTO getClienteExpoRiesgoKh() {return clienteExpoRiesgoKh;}	
	public void setClienteExpoRiesgoKh(ExpoRiesgoKhDTO obj) {clienteExpoRiesgoKh = obj;}
	
	
	

	private String rutAux;
	private String nombreCliente;
	private boolean cuadradoMarcado;
	private String tituloConceptoMonto;
	private boolean habilitarBotonAgregarSolicitud = true;
	private LcredTipoSolicitud tipoSolicitudActual;


	/*variables de Ventas*/
	private long montoGeneralConceptoMonto;
	
	/*variables de Crédito*/
	private String descripcionProyecto;
	private Long montoCredito;
	private String plazoEjecucion;
	private Long potencialCompra;
	private String conceptoNegocios;
	private String rutNombresSocios;
	private String observacionesCredito;
	
	
	/*variables condiciones */
	private String motivoRiesgoPago;

	/*variables otras solicitudes */
	private LcredSolicitudDm clienteDM;
	private String motivoBloDesbloqueo;
	
	private String usuarioEmisor;
	

	
	
	@RequestParameter("idSolicitud")
	Long idSolicitud;
	@RequestParameter("tipoSolicitudAux")
	String tipoSolicitudAux;
	@RequestParameter("pagina")
	String pagina;
	@Create
	public void init(){
		
	}
	
	@SuppressWarnings("unchecked")
	public void ingresarPaginaPrincipal(){
		List<Sucursal> listaSucur = null;
		try{

			listaSucur = (List<Sucursal>)entityManager.createQuery("select suc from Sucursal suc ").getResultList();
			setHabilitarCtaCte(true);
			setSucursal(null);
			sacarEstadosSolicitudes();
			listaCondicionPagos = intranetSapService.getCondicionPago();
			if(usuarioSegur != null){
				usuarioCargoAux = scoringService.getUsuarioCargo(usuarioSegur.getIdPersonal());
				if(usuarioCargoAux != null){
					listaCanalUsuarioCargos = scoringService.getListaCanalUsuarioCargo(usuarioCargoAux.getIdUsuarioCargo());
					if(listaCanalUsuarioCargos != null){
						long codigo = 0;
						for(CanalUsuarioCargo cuc : listaCanalUsuarioCargos){
							listaPerfilFuncionCanals = scoringService.getListaPerfilFuncionCanal(cuc.getIdCanalUsuarioCargo(), listaPerfilFuncionCanals);
							if(listaPerfilFuncionCanals != null){
								for(PerfilFuncionCanal pfc: listaPerfilFuncionCanals){
									if(codigo != pfc.getIdPerfilFuncionCanal()){
										listaZonaSucursalNegocioConceptos = scoringService.getListaZonaSucursalNegocioConcepto(pfc.getIdPerfilFuncionCanal(),
																															listaZonaSucursalNegocioConceptos);
										codigo = pfc.getIdPerfilFuncionCanal();
									}
								}
							}
						}
					}
				}
			}
			
			
		}catch (Exception e) {
			log.error("Error, obtener la lista de sucursales. #0", e.getMessage());
		}
		

		if(usuarioSegur != null){
			Sucursal suc = scoringService.obtenerSucursalForCodigo(usuarioSegur.getCodigosucursal());
			if(suc != null){
				this.setSucursal(suc);
			}
		}
		
		/*sacar concepto y negocio*/
		 setListaCboConceptoNegocio(scoringService.obtenerConceptosNegocios());
		
		 
		if(this.monto == null){
			this.monto =  new Long(0);
		}
		if(this.pie == null){
			this.pie =  null;
		}
		if(this.pedidoCotizacion == null){
			this.pedidoCotizacion = null;
		}
		
		if(listaSucur != null){
			setListaSucursales(listaSucur);
		}
		
		limpiarTiempoEjecuciones();
		
		this.muestraSeleccionSolicitud = true;
		
		try{
			if(idSolicitud != null){
				solicitud = (LcredSolicitud)entityManager.
						createQuery("select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud")
						.setParameter("numSolicitud", idSolicitud).getSingleResult();

				if(solicitud != null){

					solicitud.setUsuarioActual(usuarioSegur.getAlias());
					boolean exito = scoringService.mergerSolicitud(solicitud);
					if(solicitud != null && exito == true){
						if(pagina != null){
							setPaginaVolver(pagina);
						}
						this.setListaSolicitudUsuarioCorresSessions(scoringService.obtenerAllSolicitudUsuarioCorreo());
						setSolicitudSeleccionada(solicitud.getId().getNumSolicitud());
						insertarLogs(solicitud,1);
						evaludar(solicitud);
					}
					
					/*fecha Actual */
					setFechaActual(solicitud.getId().getFecSolicitud());
					
					this.setSolicitud(solicitud);
				}
			}

			
		}catch (Exception e) {
			log.error("Error, al sacar la solicitud seleccionada #0", e.getMessage());
		}
		
		/*sacar los conceptos  de general*/
		obtenerListaConceptoNegocioMarca();
		
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
	
	
	@SuppressWarnings("unchecked")
	public void evaludar(LcredSolicitud solicitud){
		try{
				if(solicitud != null){
					this.setRutAux(solicitud.getRutCliente());
					clienteTarget = new ClienteDTO();
					log.debug("Sucursal:"+ solicitud.getSucursalEmisor());
					obtenerObjetoSucursal(solicitud.getSucursalEmisor());
					
					log.debug("codigo de emisor #0", solicitud.getCodEmisor());
					UsuarioSegurDTO user = scoringService.sacarDatosSessionUsuario(solicitud.getCodEmisor());
					if(user != null){
						this.setUsuarioEmisor(user.getNombre());
					}else{
						this.setUsuarioEmisor(solicitud.getCodEmisor());
					}
					
					
					/*setear los datos de cliente*/
					clienteTarget.setRut(solicitud.getRutCliente());
					clienteTarget.setRazonSocial(solicitud.getNomCliente());
					clienteTarget.setGiro(solicitud.getGiroCliente());
					clienteTarget.setDireccion(solicitud.getDirCliente());
					clienteTarget.setFono(solicitud.getTelCliente());
					clienteTarget.setFax(solicitud.getFaxCliente());
					clienteTarget.setComuna(solicitud.getComCliente());
					clienteTarget.setCiudad(solicitud.getCiuCliente());
					Sucursal sucusarlcliente = scoringService.obtenerSucursalForCodigo(solicitud.getCodSucursal());
					if(sucusarlcliente != null){
						clienteTarget.setSucursalCliente(sucusarlcliente.getDescripcion());
					}else{
						clienteTarget.setSucursalCliente(solicitud.getCodSucursal());
					}					
					
					clienteTarget.setCanalVenta(solicitud.getCanal());
					clienteTarget.setTipoCliente(solicitud.getTipoCliente());
					clienteTarget.setLineaCreditoKH(solicitud.getLinCredito());
					clienteTarget.setLineaCreditoKHUtilizado(solicitud.getLinCreditoUtiliz());
					clienteTarget.setLineaCreditoKHDisponible(solicitud.getLinCreditoDisp());
					clienteTarget.setEstadoLineaCreditoKH(solicitud.getEstadoLcKh());
					clienteTarget.setNumeroRV(solicitud.getNroRv().toString());
					clienteTarget.setSeguro(solicitud.getTipSeguro());
					clienteTarget.setEstadoLineaEnCuotas(solicitud.getEstadoLcCuotas());
					clienteTarget.setCodigoCondicionPago(solicitud.getConPago());
					clienteTarget.setCodigoclasificacionRiesgo(solicitud.getClsRiesgo());
					clienteTarget.setVentasTotal12Meses(solicitud.getVtaTotal12Meses());
					clienteTarget.setCreacionSap(solicitud.getFechaCreacionCliente());	
					clienteTarget.setDmVentasUno(solicitud.getDmVenta());
					clienteTarget.setPrmFacturas(solicitud.getPrmFacturas());
					clienteTarget.setCantMesesVentas(solicitud.getMesesVentas().longValue());
					clienteTarget.setVentasProm12Meses(solicitud.getPrmVentas());
					clienteTarget.setVigenciaSeguro(solicitud.getVigSeguro());
					clienteTarget.setFactProm12Meses(solicitud.getPrmFacturas().longValue());
					
					LcredEstado lcredEstado = obtenerObjetoLcredEstado(solicitud.getEstado());
					if(lcredEstado != null){
						clienteTarget.setEstadoSolicitud(lcredEstado.getDesEstado());
					}else{
						clienteTarget.setEstadoSolicitud(solicitud.getEstado());
					}
					
					clienteTarget.setMontoSeguro(solicitud.getSeguroPesos());
					clienteTarget.setMontoSeguroUf(solicitud.getSeguroUf().longValue());
					
					try{
						if(clienteTarget.getCodigoCondicionPago()!=null){
							Object obj = intranetSapService.sacarDescripCondicionPago(clienteTarget.getCodigoCondicionPago());
							if(obj != null){
								clienteTarget.setCondicionPago(obj.toString());
							}else{
								clienteTarget.setCondicionPago("No Clasificado");
							}
						}else{clienteTarget.setCondicionPago("No Clasificado");}
						
					}catch (Exception e) {
						clienteTarget.setCondicionPago("No Clasificado");
					}

					try{
						if(clienteTarget.getCodigoclasificacionRiesgo() != null){
							Object obj = intranetSapService.sacarDescripClasificacionRiesgo(clienteTarget.getCodigoclasificacionRiesgo());
							if(obj != null){
								clienteTarget.setClasificacionRiesgo(obj.toString());
							}else{
								clienteTarget.setClasificacionRiesgo("No Clasificado");
							}
						}else{clienteTarget.setClasificacionRiesgo("No Clasificado");}
						
					}catch (Exception e) {
						clienteTarget.setClasificacionRiesgo("No Clasificado");
					}
					
					String codigoCanal = solicitud.getCanal();
					if(codigoCanal != null){
						String kx = TipoCuentasKupferType.KX.getNombre().trim();
						String gc = TipoCuentasKupferType.GC.getNombre().trim();
						String mx = TipoCuentasKupferType.MX.getNombre().trim();
						if(kx.equals(codigoCanal.trim())){
							clienteTarget.setCodigoCanal(TipoCuentasKupferType.KX);
						}
						if(gc.equals(codigoCanal.trim())){
							clienteTarget.setCodigoCanal(TipoCuentasKupferType.GC);
						}
						if(mx.equals(codigoCanal.trim())){
							clienteTarget.setCodigoCanal(TipoCuentasKupferType.MX);
						}
					}
					
					
					/*setar los datos de comportamiento comercial*/
					clienteCompComrcial = new CompComercialDTO();
					clienteCompComrcial.setDiasMoras(solicitud.getDiasMora());
					clienteCompComrcial.setDmPort(solicitud.getDm());
					clienteCompComrcial.setMontoTotalMoro(solicitud.getMorosidadMonto().longValue());
					clienteCompComrcial.setMontoTotalProt(solicitud.getProtestosMonto().longValue());
					clienteCompComrcial.setNumeroMoro(solicitud.getMorosidadNro());
					clienteCompComrcial.setNumeroProt(solicitud.getProtestosNro());
					
					/*setar los datos del exposicion de Riesgo Küpfer*/
					clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
					clienteExpoRiesgoKh.setPeakCredito(solicitud.getPeackCredito().longValue());
					clienteExpoRiesgoKh.setMontoRiesgoKupfer(solicitud.getRiesgoKupfer().longValue());
					long montoRiegoKupferPedidoPorceso = solicitud.getRiesgoKupfer().longValue() + solicitud.getMontoCuotas().longValue();
					clienteExpoRiesgoKh.setMontoRiesgoKupferPedidoProceso(montoRiegoKupferPedidoPorceso);
					clienteExpoRiesgoKh.setMontoPedidoProceso(solicitud.getMontoCuotas().longValue());
					long montoPeakCreditoPedidoProceso = solicitud.getPeackCredito().longValue() + solicitud.getMontoCuotas().longValue();
					clienteExpoRiesgoKh.setMontoPeakCreditoPedidoProceso(montoPeakCreditoPedidoProceso);
					clienteExpoRiesgoKh.setMontoAsegurado(solicitud.getMontoAsegurado().longValue());
					this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
					this.listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
					this.listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);


					
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
								 this.setTituloGeneral("Venta Normal");
								 this.setTipoSolicitudCodigo("1");
							 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.V1C.getNombre())){
								 this.setPaginaIngreso(12);
								 this.setTituloGeneral("Venta en Cuotas.");
								 this.setTipoSolicitudCodigo("1");
							 }
							 
							 if(venta != null){
								 this.tipoSolicitudActual = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
								 if(this.tipoSolicitudActual != null){
									setTituloIngreso(this.tipoSolicitudActual.getDesTipoSolicitud());
									this.tipoSolicitud = tipoSolicitudActual;
								 }	
								 this.setMonto(venta.getMontoInicial().longValue());
								 this.setPie(venta.getMontoPieInicial().longValue());	
								 
								 if(this.paginaIngreso == 11){ 
									 this.stfp = scoringService.getSolicitudTipoFormaPago(venta.getNumSolicitud());
									if(this.stfp != null){
									   this.setTipoFormaPago(this.stfp.getTipoFormaPago());
									   try{
											listaTipoFormaPagos = (List<TipoFormaPago>)entityManager.
													createQuery("select tfp from TipoFormaPago tfp ")
													.getResultList();
											if(this.tipoFormaPago != null && listaTipoFormaPagos != null){
												obtenerFormaPagos();
											}
										}catch (Exception e) {
											log.error("Error, de tipo de forma de pagos #0", e.getMessage());
										}
									   this.setEncabezado(this.stfp.getEncabezado());
									}	
								 }else if(this.paginaIngreso == 12){
									 this.stfp = scoringService.getSolicitudTipoFormaPago(venta.getNumSolicitud());
									if(this.stfp != null){
									   this.setTipoFormaPago(this.stfp.getTipoFormaPago());
									   try{
											listaTipoFormaPagos = (List<TipoFormaPago>)entityManager.
													createQuery("select tfp from TipoFormaPago tfp where tfp.idTipoFormaPago not in (2) ")
													.getResultList();
											if(this.tipoFormaPago != null && listaTipoFormaPagos != null){
												obtenerFormaPagos();
											}
										}catch (Exception e) {
											log.error("Error, de tipo de forma de pagos #0", e.getMessage());
										}
									   this.setEncabezado(this.stfp.getEncabezado());
									}else{
									   try{
											listaTipoFormaPagos = (List<TipoFormaPago>)entityManager.
													createQuery("select tfp from TipoFormaPago tfp where tfp.idTipoFormaPago not in (2) ")
													.getResultList();
										}catch (Exception e) {
											log.error("Error, de tipo de forma de pagos #0", e.getMessage());
										}										
									}
								 }
								 
								 this.setMonto(venta.getMontoInicial().longValue());
								 this.setPie(venta.getMontoPieInicial().longValue());
								 this.setMotivoCompra(venta.getDcMotivo());
								 this.setObservaciones(venta.getObservacionesFinal());
								 try{
										String margenAux = venta.getDcMargen().replace(",", ".");
										String margennuevo = margenAux.replace("%", "");
										Double margen = Double.parseDouble(margennuevo);
										this.setMargenGlobal(margen);
								}catch (Exception e) {
										log.error("Error al transformar el margen global #0", e.getMessage());
										this.setMargenGlobal((double)0);
								}								 

							 }
							 /*sacar los archivos existente en la generacion */
//							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//							 if(listaArchivos != null){
//								 ConceptoDTO obj = null;
//								 for(ArchivoAdjunto aa : listaArchivos ){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
//									 this.listaArchivoGuardado.add(obj);
//								 }
//							 }
							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
							 if(listaArchivos != null){
								 ConceptoDTO obj = null;
								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								 for(ArchivoAdjunto aa : listaArchivos ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 obj.setTipoArchivoType(aa.getTipo());
									 obj.setFechaCreacion(aa.getFechaCreacion());
									 if(aa.getUsuario() != null){
										 obj.setUsuario(aa.getUsuario().getAlias());
									 }else{
										 obj.setUsuario(""); 
									 }
									 listaArchivoGuardado.add(obj);
								 }
							 }
							 
							 
							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
							 if(listaArchivosRespuesta != null){
								 if(listaArchivoGuardado == null){
									 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0); 
								 }else{
									 log.debug("cantiadad registros #0", listaArchivoGuardado.size());
								 }
								 
								 ConceptoDTO obj = null;
								 if(listaArchivosRespuesta.size() > 0){
									 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
										 obj = new ConceptoDTO();
										 obj.setNombreArchivo(aa.getNombreAdjunto());
										 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
										 obj.setTipoArchivoType(aa.getTipo());
										 obj.setFechaCreacion(aa.getFechaCreacion());
										 if(aa.getUsuario() != null){
											 obj.setUsuario(aa.getUsuario().getAlias());
										 }else{
											 obj.setUsuario(""); 
										 }
										 
										 if(obj != null){
											 listaArchivoGuardado.add(obj);
										 }
									 }
								 }
							 }	
							 
							 
							 
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
							 obtenerCorreo(solicitud.getCodEmisor(), solicitud.getUsuarioDevuelve());
							 
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
								 this.setTituloGeneral("Linea Innominda.");
								 this.setTipoSolicitudCodigo("2");
							 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC2.getNombre())){
								 this.setPaginaIngreso(22);
								 this.setTituloGeneral("Linea Nominada.");
								 this.setTipoSolicitudCodigo("2");
							 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.LC3.getNombre())){
								 this.setPaginaIngreso(23);
								 this.setTituloGeneral("Riesgo kupfer.");
								 this.setTipoSolicitudCodigo("2");
							 }

							 //this.setInnNomRiesgo(String.valueOf(this.paginaIngreso));
							 
//							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//							 if(listaArchivos != null){
//								 ConceptoDTO obj = null;
////								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//								 for(ArchivoAdjunto aa : listaArchivos ){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
////									 listaArchivoGuardado.add(obj);
//								 }
//							 }
							 
							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
							 if(listaArchivos != null){
								 ConceptoDTO obj = null;
								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								 for(ArchivoAdjunto aa : listaArchivos ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 obj.setTipoArchivoType(aa.getTipo());
									 obj.setFechaCreacion(aa.getFechaCreacion());
									 if(aa.getUsuario() != null){
										 obj.setUsuario(aa.getUsuario().getAlias());
									 }else{
										 obj.setUsuario(""); 
									 }
									 listaArchivoGuardado.add(obj);
								 }
							 }
							 
							 
							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
							 if(listaArchivosRespuesta != null){
								 if(listaArchivoGuardado == null){
									 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0); 
								 }else{
									 log.debug("cantiadad registros #0", listaArchivoGuardado.size());
								 }
								 
								 ConceptoDTO obj = null;
								 if(listaArchivosRespuesta.size() > 0){
									 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
										 obj = new ConceptoDTO();
										 obj.setNombreArchivo(aa.getNombreAdjunto());
										 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
										 obj.setTipoArchivoType(aa.getTipo());
										 obj.setFechaCreacion(aa.getFechaCreacion());
										 if(aa.getUsuario() != null){
											 obj.setUsuario(aa.getUsuario().getAlias());
										 }else{
											 obj.setUsuario(""); 
										 }
										 
										 if(obj != null){
											 listaArchivoGuardado.add(obj);
										 }
									 }
								 }
							 }	
							 
							 
							 
							 
							 this.tipoSolicitudActual = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
							 if(this.tipoSolicitudActual != null){
								 this.tipoSolicitud = this.tipoSolicitudActual;
							 }
						
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
								
								this.setDescripcionProyecto(credito.getDpDescripcionProyecto());
								String montoAux = credito.getDpMonto().replace(".", "");
								this.setMontoCredito(Long.parseLong(montoAux));
								this.setPlazoEjecucion(credito.getDpPlazoEjecucion());
								this.setConceptoNegocios(credito.getDpConceptosInvoluc());
								this.setRutNombresSocios(credito.getDpSocios());
								String montoPotencial = credito.getDpPotencialCompra().replace(".", "");
								this.setPotencialCompra(Long.parseLong(montoPotencial));
								this.setObservacionesCredito(credito.getObservacionesInicial());
								this.setMonto(credito.getMontoNormalInicial().longValue());
								List<SolicitudConceptosNegocioLC> lista = scoringService.getSolicitudConceptosNegocioLC(credito.getNumSolicitud());
								if(lista != null){
									listaSelConceptoNegocio = new ArrayList<ConceptoNegocioDTO>();
									ConceptoNegocioDTO conceptoNegocio = null;
									for(SolicitudConceptosNegocioLC scn : lista){
										conceptoNegocio = new ConceptoNegocioDTO();
										conceptoNegocio.setConceptoNegocio(scn.getConceptosNegocio());
										conceptoNegocio.setMonto(scn.getMonto());
										listaSelConceptoNegocio.add(conceptoNegocio);
										conceptoNegocio = null;
									}
								}
								
								listaSocios = new ArrayList<String>(0);
								if(credito.getDpSocios() != null){
									String[] slitSocios = credito.getDpSocios().split(",");
									if(slitSocios != null){
										for(String socio : slitSocios){
											listaSocios.add(socio);
										}
									}
								}
								
							}
							
							/*va a buscar los destinatarios de las solictudes*/
							 obtenerCorreo(solicitud.getCodEmisor(), solicitud.getUsuarioDevuelve());

						 
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
//						 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//						  if(listaArchivos != null){
//							 ConceptoDTO obj = null;
////							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//							 for(ArchivoAdjunto aa : listaArchivos ){
//								 obj = new ConceptoDTO();
//								 obj.setNombreArchivo(aa.getNombreAdjunto());
//								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
////								 listaArchivoGuardado.add(obj);
//							 }
//						  }						  
						  
						  
						 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
						 if(listaArchivos != null){
							 ConceptoDTO obj = null;
							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							 for(ArchivoAdjunto aa : listaArchivos ){
								 obj = new ConceptoDTO();
								 obj.setNombreArchivo(aa.getNombreAdjunto());
								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
								 obj.setTipoArchivoType(aa.getTipo());
								 obj.setFechaCreacion(aa.getFechaCreacion());
								 if(aa.getUsuario() != null){
									 obj.setUsuario(aa.getUsuario().getAlias());
								 }else{
									 obj.setUsuario(""); 
								 }
								 listaArchivoGuardado.add(obj);
							 }
						 }
							 
							 
						 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
						 if(listaArchivosRespuesta != null){
							 if(listaArchivoGuardado == null){
								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0); 
							 }else{
								 log.debug("cantiadad registros #0", listaArchivoGuardado.size());
							 }
							 
							 ConceptoDTO obj = null;
							 if(listaArchivosRespuesta.size() > 0){
								 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 obj.setTipoArchivoType(aa.getTipo());
									 obj.setFechaCreacion(aa.getFechaCreacion());
									 if(aa.getUsuario() != null){
										 obj.setUsuario(aa.getUsuario().getAlias());
									 }else{
										 obj.setUsuario(""); 
									 }
									 
									 if(obj != null){
										 listaArchivoGuardado.add(obj);
									 }
								 }
							 }
						 }	

						  
						  if(condicion != null ){
							  setPaginaIngreso(31);
							  this.setTituloGeneral("Condiciones.");
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
									evaluarInnominalNominalRKupfer(condicion.getCodCondRiesgoInicial(),condicion.getCodCondPagoInicial());
								}
								if((condicion.getCondPagoInicial() != null ) && (condicion.getCondPagoInicial().toString()).equals("S")){
									this.condicionPago = true;
									evaluarInnominalNominalRKupfer(condicion.getCodCondRiesgoInicial(),condicion.getCodCondPagoInicial());
								}
								
								if(condicion.getMotivoCambio() != null){
									this.setMotivoRiesgoPago(condicion.getMotivoCambio());
								}
							  
						  }

						  this.tipoSolicitudActual = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
							 if(this.tipoSolicitudActual != null){
								 this.tipoSolicitud = this.tipoSolicitudActual;
							 }
						  /*va a buscar los destinatarios de las solictudes*/
						    obtenerCorreo(solicitud.getCodEmisor(), solicitud.getUsuarioDevuelve());				 
					  
							 
							 
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
						 	
						 	this.setTituloIngreso(solicitud.getDesTiposol());
						 	
							if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS1.getNombre())){
								this.setOpcionBloqueoDesbloqueo("41");
								this.setTituloGeneral("Bloqueo.");
								 this.setTipoSolicitudCodigo("4");
							}else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS2.getNombre())){
								this.setOpcionBloqueoDesbloqueo("42");
								this.setTituloGeneral("Desbloqueo.");
								 this.setTipoSolicitudCodigo("4");
							}else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS.getNombre())){
								this.setOpcionBloqueoDesbloqueo("");
								 this.setTipoSolicitudCodigo("4");
							}						 
						 
							if(solicitudOtra  != null && bloqueo == null){
								 this.setPaginaIngreso(41);
								 this.setMotivoBloDesbloqueo(solicitudOtra.getObservacionesInicial());
								 this.setTipoSolicitudCodigo("4");
							}else if(solicitudOtra  == null && bloqueo != null){
								 this.setPaginaIngreso(41);
								 this.setMotivoBloDesbloqueo(bloqueo.getMotivoBloqDesbloq());
								 this.setTipoSolicitudCodigo("4");
							}else if(solicitudOtra  != null && bloqueo != null){
								 this.setPaginaIngreso(41);
								 this.setMotivoBloDesbloqueo(solicitudOtra.getObservacionesInicial());	
								 this.setTipoSolicitudCodigo("4");
							}
							
//							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//							if(listaArchivos != null){
//								 ConceptoDTO obj = null;
////								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//								 for(ArchivoAdjunto aa : listaArchivos ){
//									 obj = new ConceptoDTO();
//									 obj.setNombreArchivo(aa.getNombreAdjunto());
//									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
////									 listaArchivoGuardado.add(obj);
//								 }
//							 }	
							
							 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
							 if(listaArchivos != null){
								 ConceptoDTO obj = null;
								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								 for(ArchivoAdjunto aa : listaArchivos ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 obj.setTipoArchivoType(aa.getTipo());
									 obj.setFechaCreacion(aa.getFechaCreacion());
									 if(aa.getUsuario() != null){
										 obj.setUsuario(aa.getUsuario().getAlias());
									 }else{
										 obj.setUsuario(""); 
									 }
									 listaArchivoGuardado.add(obj);
								 }
							 }
							 
							 
							 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
							 if(listaArchivosRespuesta != null){
								 if(listaArchivoGuardado == null){
									 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0); 
								 }else{
									 log.debug("cantiadad registros #0", listaArchivoGuardado.size());
								 }
								 
								 ConceptoDTO obj = null;
								 if(listaArchivosRespuesta.size() > 0){
									 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
										 obj = new ConceptoDTO();
										 obj.setNombreArchivo(aa.getNombreAdjunto());
										 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
										 obj.setTipoArchivoType(aa.getTipo());
										 obj.setFechaCreacion(aa.getFechaCreacion());
										 if(aa.getUsuario() != null){
											 obj.setUsuario(aa.getUsuario().getAlias());
										 }else{
											 obj.setUsuario(""); 
										 }
										 
										 if(obj != null){
											 listaArchivoGuardado.add(obj);
										 }
									 }
								 }
							 }	
							
							
						 
						   this.tipoSolicitudActual = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
							 if(this.tipoSolicitudActual != null){
								 this.tipoSolicitud = this.tipoSolicitudActual;
							 }
						   /*va a buscar los destinatarios de las solictudes*/
							 obtenerCorreo(solicitud.getCodEmisor(), solicitud.getUsuarioDevuelve());
						
//						   this.modificarbloqueoToDesbloqueo = false; 
						   
						 
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
						 }
						 
						 
					 	this.setTituloIngreso(solicitud.getDesTiposol());
					 	this.setTituloGeneral("Creación de DM.");
					 	
						if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS3.getNombre())){
							this.setPaginaIngreso(43);
							this.setMotivoProrroga(solicitud.getObservSolicitud());
							this.setTipoSolicitudCodigo("4");
						}	
//						 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//						if(listaArchivos != null){
//							 ConceptoDTO obj = null;
////							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//							 for(ArchivoAdjunto aa : listaArchivos ){
//								 obj = new ConceptoDTO();
//								 obj.setNombreArchivo(aa.getNombreAdjunto());
//								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
////								 listaArchivoGuardado.add(obj);
//							 }
//						 }						  
					 
						 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
						 if(listaArchivos != null){
							 ConceptoDTO obj = null;
							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							 for(ArchivoAdjunto aa : listaArchivos ){
								 obj = new ConceptoDTO();
								 obj.setNombreArchivo(aa.getNombreAdjunto());
								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
								 obj.setTipoArchivoType(aa.getTipo());
								 obj.setFechaCreacion(aa.getFechaCreacion());
								 if(aa.getUsuario() != null){
									 obj.setUsuario(aa.getUsuario().getAlias());
								 }else{
									 obj.setUsuario(""); 
								 }
								 listaArchivoGuardado.add(obj);
							 }
						 }
						 
						 
						 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
						 if(listaArchivosRespuesta != null){
							 if(listaArchivoGuardado == null){
								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0); 
							 }else{
								 log.debug("cantiadad registros #0", listaArchivoGuardado.size());
							 }
							 
							 ConceptoDTO obj = null;
							 if(listaArchivosRespuesta.size() > 0){
								 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 obj.setTipoArchivoType(aa.getTipo());
									 obj.setFechaCreacion(aa.getFechaCreacion());
									 if(aa.getUsuario() != null){
										 obj.setUsuario(aa.getUsuario().getAlias());
									 }else{
										 obj.setUsuario(""); 
									 }
									 
									 if(obj != null){
										 listaArchivoGuardado.add(obj);
									 }
								 }
							 }
						 }	
						
						
						
					   this.tipoSolicitudActual = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));						
						 if(this.tipoSolicitudActual != null){
							 this.tipoSolicitud = this.tipoSolicitudActual;
						 } 
							/*va a buscar los destinatarios de las solictudes*/
						 obtenerCorreo(solicitud.getCodEmisor(), solicitud.getUsuarioDevuelve());
					   
 
					 
					 }else if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS4.getNombre())){/********************* Prorroga ********************/
						 try{
							 listaProrrogas = (List<LcredSolicitudProrroga>)entityManager
									 .createQuery("Select lp from LcredSolicitudProrroga lp where lp.id.numSolicitud=:solAux")
									 .setParameter("solAux", solicitud.getId().getNumSolicitud()).getResultList();	
							 if(listaProrrogas != null){
								this.listaDoctoProrrogaAgregados = new ArrayList<DocumentoProrrogaDTO>(0);
								for(LcredSolicitudProrroga sp : listaProrrogas){
									DocumentoProrrogaDTO obj = new DocumentoProrrogaDTO();
									obj.setNumeroDocto(sp.getId().getNroCheque());
									obj.setMonto(sp.getId().getMonto().longValue());
									DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
									Date d = formatoFecha.parse(sp.getId().getVencActual());
									obj.setFechaVencActual(d);
									DateFormat formatoFecha2 = new SimpleDateFormat("dd/MM/yyyy");
									Date dn = formatoFecha2.parse(sp.getId().getVencNuevo());									
									obj.setFechaVencNuevo(dn);
									obj.setStatus(true);
									listaDoctoProrrogaAgregados.add(obj);
								}
								 
							 }
							 
						}catch (Exception e) {
							log.debug("no existe datos de las prorrogas dm.");
						}	
						 
						 
						solicitudOtra = scoringService.getSolicitudOtrasId(solicitud.getId().getNumSolicitud());
					 	if(solicitudOtra != null){
					 		this.setMotivoProrroga(solicitudOtra.getObservacionesInicial());
					 	}

						 
						 
					 	this.setTituloIngreso(solicitud.getDesTiposol());
						if((solicitud.getTipTiposol().trim()).equals(TipoSolicitudType.OS4.getNombre())){
							this.setPaginaIngreso(44);
							this.setTituloGeneral("Prorroga de Cheques.");
						}	
//						 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
//						if(listaArchivos != null){
//							 ConceptoDTO obj = null;
////							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
//							 for(ArchivoAdjunto aa : listaArchivos ){
//								 obj = new ConceptoDTO();
//								 obj.setNombreArchivo(aa.getNombreAdjunto());
//								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
////								 listaArchivoGuardado.add(obj);
//							 }
//						 }						  
					 
						 List<ArchivoAdjunto> listaArchivos = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.INGRESO);
						 if(listaArchivos != null){
							 ConceptoDTO obj = null;
							 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							 for(ArchivoAdjunto aa : listaArchivos ){
								 obj = new ConceptoDTO();
								 obj.setNombreArchivo(aa.getNombreAdjunto());
								 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
								 obj.setTipoArchivoType(aa.getTipo());
								 obj.setFechaCreacion(aa.getFechaCreacion());
								 if(aa.getUsuario() != null){
									 obj.setUsuario(aa.getUsuario().getAlias());
								 }else{
									 obj.setUsuario(""); 
								 }
								 listaArchivoGuardado.add(obj);
							 }
						 }
						 
						 
						 List<ArchivoAdjunto> listaArchivosRespuesta = scoringService.obtenerArchivosSolicitud(solicitud.getId().getNumSolicitud(),ArchivoAdjuntoType.RESPUESTA);
						 if(listaArchivosRespuesta != null){
							 if(listaArchivoGuardado == null){
								 listaArchivoGuardado = new ArrayList<ConceptoDTO>(0); 
							 }else{
								 log.debug("cantiadad registros #0", listaArchivoGuardado.size());
							 }
							 
							 ConceptoDTO obj = null;
							 if(listaArchivosRespuesta.size() > 0){
								 for(ArchivoAdjunto aa : listaArchivosRespuesta ){
									 obj = new ConceptoDTO();
									 obj.setNombreArchivo(aa.getNombreAdjunto());
									 obj.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+aa.getUrl());
									 obj.setTipoArchivoType(aa.getTipo());
									 obj.setFechaCreacion(aa.getFechaCreacion());
									 if(aa.getUsuario() != null){
										 obj.setUsuario(aa.getUsuario().getAlias());
									 }else{
										 obj.setUsuario(""); 
									 }
									 
									 if(obj != null){
										 listaArchivoGuardado.add(obj);
									 }
								 }
							 }
						 }	
						
						
					   this.tipoSolicitudActual = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));								 
						 if(this.tipoSolicitudActual != null){
							 this.tipoSolicitud = this.tipoSolicitudActual;
						 } 
						 /*va a buscar los destinatarios de las solictudes*/
						 obtenerCorreo(solicitud.getCodEmisor(), solicitud.getUsuarioDevuelve());
	
//						 this.modificarProrroga = false;
					   informacionCtaCte();
					 }
				}

				
		}catch (Exception e) {
			log.error("Error, al eveluar la solictud #0", e.getMessage());
			
		}
	}

	public void obtenerCorreo(String usarioAux, String ejecutivo){
		try{
			listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
				/*ingreso del emisor y del ejecutivo*/
				if(usarioAux != null){
					UsuarioSegurDTO user = scoringService.sacarDatosSessionUsuario(usarioAux);
					if(user != null){
						SolicitudUsuarioCorreo nuevo = getObtenerSolicitudUsuarioCorreo(user.getAlias());
						if(nuevo == null){
							nuevo = new SolicitudUsuarioCorreo();
							nuevo.setUsername(user.getAlias());
							nuevo.setNombre(user.getNombre());
							nuevo.setCorreo(user.getCorreo());
							nuevo.setEvaluar(false);
							nuevo.setConfirmar(false);
							nuevo = scoringService.persistSolicitudCorreo(nuevo);
							//nuevo.setCorreo(usuarioCorreo.getCorreo());
							listaUsuarioCorreoagregados.add(nuevo);
						}else{
							if(nuevo.getEvaluar() == false){
								nuevo.setConfirmar(false);
								//nuevo.setCorreo(usuarioCorreo.getCorreo());
								listaUsuarioCorreoagregados.add(nuevo);
							}else{
								nuevo.setEvaluar(false);
								nuevo.setConfirmar(false);
								nuevo = scoringService.mergerSolicitudCorreo(nuevo);
								//nuevo.setCorreo(usuarioCorreo.getCorreo());
								listaUsuarioCorreoagregados.add(nuevo);
							}
						}
					}
				}
				if(ejecutivo != null){
					UsuarioSegurDTO ejecutivoAux = scoringService.sacarDatosSessionUsuario(ejecutivo);
					if(ejecutivoAux != null){
						SolicitudUsuarioCorreo nuevoEjecutivo = getObtenerSolicitudUsuarioCorreo(ejecutivoAux.getAlias());
						if(nuevoEjecutivo == null){
							nuevoEjecutivo = new SolicitudUsuarioCorreo();
							nuevoEjecutivo.setUsername(ejecutivoAux.getAlias());
							nuevoEjecutivo.setNombre(ejecutivoAux.getNombre());
							nuevoEjecutivo.setCorreo(ejecutivoAux.getCorreo());
							nuevoEjecutivo.setEvaluar(false);
							nuevoEjecutivo.setConfirmar(false);
							nuevoEjecutivo = scoringService.persistSolicitudCorreo(nuevoEjecutivo);
							//nuevo.setCorreo(usuarioCorreo.getCorreo());
							listaUsuarioCorreoagregados.add(nuevoEjecutivo);
						}else{
							if(nuevoEjecutivo.getEvaluar() == false){
								nuevoEjecutivo.setConfirmar(false);
								//nuevo.setCorreo(usuarioCorreo.getCorreo());
								listaUsuarioCorreoagregados.add(nuevoEjecutivo);
							}else{
								nuevoEjecutivo.setEvaluar(false);
								nuevoEjecutivo.setConfirmar(false);
								nuevoEjecutivo = scoringService.mergerSolicitudCorreo(nuevoEjecutivo);
								//nuevo.setCorreo(usuarioCorreo.getCorreo());
								listaUsuarioCorreoagregados.add(nuevoEjecutivo);
							}
						}
					}
				}
			log.debug(listaUsuarioCorreoagregados.size());
			
		}catch (Exception e) {
			log.error("Error, sacar los destinatarios : #0", e.getMessage());
		}
	}

	public SolicitudUsuarioCorreo getObtenerSolicitudUsuarioCorreo(String ususario){
		SolicitudUsuarioCorreo codigo= null;
		if(this.listaSolicitudUsuarioCorresSessions != null){
			for(SolicitudUsuarioCorreo cn : listaSolicitudUsuarioCorresSessions){
				if(((cn.getUsername().toLowerCase()).trim()).equals((ususario.toLowerCase()).trim())){
					codigo = cn;
					break;
				}
			}
		}
		return codigo;	
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
	
	public void insertarLogs(LcredSolicitud sol, int opcion){
		/*ingreso de logs*/
		try{
			if(opcion == 1){/*solo esta revisando la solicitud*/
				String codigo = "E";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("En Proceso de reparo de la solicitud. ");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("En Proceso de reparo de la solicitud. ");
					exito = scoringService.persistSolicitudLogs(sol.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);
			}
			
			
		}catch (Exception e) {
			log.error("Error, al insertar el logs de la solicitud #0", e.getMessage());
		}
	}
	
    /*metodos trabajados*/
	public void limpiarTiempoEjecuciones(){
		this.setTiempoEjecucionMenu(null);
		this.setTiempoEjecucionModal(null);
		this.setTiempoEjecucionProceso(null);
		this.setTiempoEjecucionValidar(null);		
	}
	
	public void obtenerDMCliente(){
		if(this.dm != null){
			log.error(" el dm del cliente es #0", this.dm);
		}
	}
	public void obtenerFechaDeuda(){
		if(this.fechaDeauda != null){
			log.error(" la fechas seleccionada es  #0", this.fechaDeauda);
		}
	}
	
	public void obtenerCondicionRiesgo(){
		if(this.clsRiesgo != null){
			log.error("El clasificacion de riesgo es #0", this.clsRiesgo);
			if(this.paginaIngreso == 31){
				
				if(tipoSolicitud != null && (listaUsuarioCorreoagregados == null || listaUsuarioCorreoagregados.size() == 0)){
//					if(listaDestinatarios != null){
//						listaDestinatarios.clear();
//					}
					
					/*va a buscar los destinatarios de las solictudes*/
					try{
						listaDestinatarios = null;
//										scoringService.obtenerListaDestinatario(usuarioLogueado.getAlias(), "01", 
//				                        clienteTarget.getCodigoCanal().name(), 
//				                        sucursal.getZona(), 
//				                        sucursal.getCodigo(), 
//				                        clienteExpoRiesgoKh.getMontoRiesgoKupfer(), 
//				                        "3");	
						if(listaDestinatarios != null){
							listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
							listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
							listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
							for(DestinatarioDTO dto : listaDestinatarios){
								if(dto != null){
									log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
											,dto.getTipoSolicitud(), dto.getUsername(), dto.getZona(),
											 dto.getSucursal(), dto.getConcepto(), dto.getNegocio() );

								}
							}	
						}
					}catch (Exception e) {
						log.error("Error, sacar los destinatarios : #0", e.getMessage());
					}
				
				}
			
			}
		}
	}
	public void obtenerCondicionPago(){
		if(this.cPago != null){
			log.error("El condicion de pago es #0", this.cPago);
			if(this.paginaIngreso == 31){
				if(tipoSolicitud != null){
					
					if(tipoSolicitud != null && (listaUsuarioCorreoagregados == null || listaUsuarioCorreoagregados.size() == 0)){
						if(listaDestinatarios != null){
							listaDestinatarios.clear();
						}
						
						/*va a buscar los destinatarios de las solictudes*/
						try{
							listaDestinatarios = null;//
//									        scoringService.obtenerListaDestinatario(usuarioLogueado.getAlias(), "01", 
//					                        clienteTarget.getCodigoCanal().name(), 
//					                        sucursal.getZona(), 
//					                        sucursal.getCodigo(), 
//					                        clienteExpoRiesgoKh.getMontoRiesgoKupfer(), 
//					                        "3");	
							if(listaDestinatarios != null){
								listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
								listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
								listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
								for(DestinatarioDTO dto : listaDestinatarios){
									if(dto != null){
										log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
												,dto.getTipoSolicitud(), dto.getUsername(), dto.getZona(),
												 dto.getSucursal(), dto.getConcepto(), dto.getNegocio() );

									}
								}	
								
							}
						}catch (Exception e) {
							log.error("Error, sacar los destinatarios : #0", e.getMessage());
						}
					
					}
				
				}
				
			}
		}
	}
	public void obtenerMontoProyecto(){
		if(this.montoCredito != null){
			log.error("monto credito #0", this.montoCredito);
		}
		if(this.potencialCompra != null){
			log.error("potencial compra  #0", this.potencialCompra);
			
		}		
	}
	public void obtenerMontoCredito(){
		if(this.monto != null){
			log.error("monto  #0", this.monto);
			
			if(this.paginaIngreso == 21){
				listaDestinatarios = null;
				Ufs uf =scoringService.sacarUFDelDia(new Date());
				if(uf != null){
					double montoCalculado = (this.monto / uf.getValor());
					if(montoCalculado <= 500){
						if(this.paginaIngreso == 23){
							listaDestinatarios = null;
						}else{
							listaDestinatarios = null;
						}
						
						if(listaDestinatarios != null){
							listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
							listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
							listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
							for(DestinatarioDTO dto : listaDestinatarios){
								if(dto != null){
									log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
											,dto.getTipoSolicitud(), dto.getUsername(), dto.getZona(),
											 dto.getSucursal(), dto.getConcepto(), dto.getNegocio() );
			
								}
							}	
							
						}
						this.mensajeGlogal = null;
					}else{
						Locale locale = new Locale("es","CL");
						NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
						double montoCalculadoPesos = (500 * uf.getValor());
						FacesMessages.instance().add(Severity.WARN,"El monto de la solicitud es mayor a 500 Uf y valor en pesos es #0.", numberFormatter.format(montoCalculadoPesos));
						this.mensajeGlogal = "El monto de la solicitud es mayor a 500 Uf y valor en pesos es "+numberFormatter.format(montoCalculadoPesos);
						setMonto(null);
						return;
					}
				}	
			
			}else{
				if(this.paginaIngreso == 23){
					listaDestinatarios = null; 
					      /*scoringService.obtenerListaDestinatario(usuarioLogueado.getAlias(), "01", 
		                    clienteTarget.getCodigoCanal().name(), 
		                    sucursal.getZona(), 
		                    sucursal.getCodigo(), 
		                    this.monto, 
		                    "2");	*/			
				}else{
					listaDestinatarios = null; /*scoringService.obtenerListaDestinatario(usuarioLogueado.getAlias(), "01", 
		                    clienteTarget.getCodigoCanal().name(), 
		                    sucursal.getZona(), 
		                    sucursal.getCodigo(), 
		                    0, 
		                    "2");*/
				}
				if(listaDestinatarios != null){
					listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
					listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
					listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
					for(DestinatarioDTO dto : listaDestinatarios){
						if(dto != null){
							log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
									,dto.getTipoSolicitud(), dto.getUsername(), dto.getZona(),
									 dto.getSucursal(), dto.getConcepto(), dto.getNegocio() );
	
						}
					}	
					
				}
				this.mensajeGlogal = null;
			}
		}
	}
	public void obtenerMontoConcept(){
		if(this.montoConcepto != null){
			log.error("monto concepto :#0 ", this.montoConcepto);
		}
	}
	
	public void obtenerMensajeGrillaProrroga(DocumentoProrrogaDTO objeto){
		if(objeto != null){
			log.debug("numero del documento #0 mensaje :#1 ", objeto.getNumeroDocto());
		}
	}
	
	public void obtenerCalculosMontosExposicion(){
		String codigoEvaluacion=null;
		long seguroAux = 0;
		this.mensajeGlogal = null;
		if(this.tipoFormaPago == null && this.encabezado == null){
			this.mensajeGlogal = "Debe seleccionar la forma de pago de la solicitud.";
		}else{
			if(this.tipoFormaPago != null && this.encabezado == null){
				this.mensajeGlogal = "Debe seleccionar la forma de pago de la solicitud.";
			}
		}
		
		if((this.monto == null && this.pie == null) || (this.monto == 0 && this.pie == 0)){
			FacesMessages.instance().add(Severity.WARN,"El monto tiene que mayor 0.");
			this.mensajeGlogal ="El monto tiene que mayor 0.";
			clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
			return;
		}
		
	
		
		if(this.monto == 0 && this.pie != 0){
			FacesMessages.instance().add(Severity.WARN,"Debe ingresar el monto y despues pie.");
			this.mensajeGlogal = "Debe ingresar el monto y despues pie.";
			clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
			return;			
		}
		
		if(this.paginaIngreso == 11){
			codigoEvaluacion = "1";
			if(this.monto == null){this.monto = (long)0;}
			if(this.pie == null){this.pie = (long)0;}
			
			if(this.pie != 0 && this.monto != 0){
				if(this.tipoFormaPago != null && this.tipoFormaPago.getDescripcion().equals("Efectivo")){
					if(this.pie.longValue() != this.monto.longValue()){
						FacesMessages.instance().add(Severity.ERROR,"El monto de compra y el pie deben ser iguales.");
						this.mensajeGlogal = "El monto de compra y el pie deben ser iguales.";
						clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
						this.pie = null;
						return;			
					}
				}else if(this.tipoFormaPago != null && !this.tipoFormaPago.getDescripcion().equals("Efectivo")){
					if(this.pie >= this.monto){
						FacesMessages.instance().add(Severity.WARN,"El pie debe ser menor que el monto de compra.");
						this.mensajeGlogal = "El pie debe ser menor que el monto de compra.";
						clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
						this.pie = null;
						return;			
					}else{
						activarConsulta = true;
					}
				}else{
					activarConsulta = true;
				}
			}
			
		}else if (this.paginaIngreso == 12){
			codigoEvaluacion = "1";
			if(this.monto == null){this.monto = (long)0;}
			if(this.pie == null){this.pie = (long)0;}
			
			if(this.pie.longValue() != 0 && this.monto.longValue() != 0){
				if(this.evaluadorCompraVC){
					if(this.pie.longValue() > this.monto.longValue()){
						FacesMessages.instance().add(Severity.WARN,"El pie no debe ser mayor que el monto de venta.");
						this.mensajeGlogal = "El pie no debe ser mayor que el monto de venta.";
						clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
						this.pie = null;
						return;			
					}else if(this.pie.longValue() == this.monto.longValue()){
						FacesMessages.instance().add(Severity.WARN,"El pie no debe ser igual que el monto de venta.");
						this.mensajeGlogal = "El pie no debe ser igual que el monto de venta.";
						clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
						this.pie = null;
						return;								
					}else{
						activarConsulta = true;
					}
				}else{
					this.pie = null;
					activarConsulta = true;
				}
			}
		}

		
		if(this.getClienteTarget() != null){
			ExpoRiesgoKhDTO objeto = new  ExpoRiesgoKhDTO();
			Long monto = null; Long pie = null;
			if(this.monto != null){ monto = this.monto.longValue();}else{monto = new Long(0);}
			if(this.pie != null){ pie = this.pie.longValue();}else{pie = new Long(0);}
			long peakCredito  = clienteTarget.getLineaCreditoKHUtilizado().longValue() + monto - pie;		
			objeto.setPeakCredito(peakCredito);
			
			if("VIG".equals(clienteTarget.getVigenciaSeguro().trim())){
				seguroAux = clienteTarget.getMontoSeguro().longValue();
			}
			
			
			if(peakCredito <= seguroAux){
				objeto.setMontoAsegurado(peakCredito);
			}else{
				objeto.setMontoAsegurado(seguroAux);
				long diferencia = (peakCredito - seguroAux);
				objeto.setMontoRiesgoKupfer(diferencia);
			}

			//Pedido o Proceso
			objeto.setMontoPedidoProceso(clienteTarget.getPedidoEnProceso());
			//Peak Crédito mas Pedido o Proceso
			objeto.setMontoPeakCreditoPedidoProceso(objeto.getPeakCredito() + objeto.getMontoPedidoProceso());
			//Riesgo Küpfer + Pedido o Proceso
			objeto.setMontoRiesgoKupferPedidoProceso(objeto.getMontoRiesgoKupfer() + objeto.getMontoPedidoProceso());
			
			this.clienteExpoRiesgoKh = objeto;

		}
		
		if(tipoSolicitud != null && this.clienteExpoRiesgoKh != null){
			if(this.activarConsulta){
				if(listaDestinatarios == null){
					listaDestinatarios = new ArrayList<DestinatarioDTO>(0);
				}
				
				/*va a buscar los destinatarios de las solictudes*/
				try{

					if(listaDestinatarios != null && listaDestinatarios.size() == 0){
						if(this.paginaIngreso == 11){
							listaDestinatarios = null;
//											scoringService.obtenerListaDestinatario(usuarioLogueado.getAlias(), "01", 
//					                        clienteTarget.getCodigoCanal().name(), 
//					                        sucursal.getZona(), 
//					                        sucursal.getCodigo(), 
//					                        clienteExpoRiesgoKh.getMontoRiesgoKupfer(), 
//					                        codigoEvaluacion);	
							
						}else if(this.paginaIngreso == 12){
							listaDestinatarios = null;
							
//									scoringService.obtenerListaDestinatario(usuarioLogueado.getAlias(), "01", 
//			                        clienteTarget.getCodigoCanal().name(), 
//			                        sucursal.getZona(), 
//			                        sucursal.getCodigo(), 
//			                        clienteExpoRiesgoKh.getMontoRiesgoKupfer(), 
//			                        codigoEvaluacion);						
							
						}
						
						if(listaDestinatarios != null){
							listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
							listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
							listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
							for(DestinatarioDTO dto : listaDestinatarios){
								if(dto != null){
									log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
											,dto.getTipoSolicitud(), dto.getUsername(), dto.getZona(),
											 dto.getSucursal(), dto.getConcepto(), dto.getNegocio() );
	
								}
							}	
							
						}
					}else{
						if(listaDestinatarios != null){
							listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
							listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
							listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
							for(DestinatarioDTO dto : listaDestinatarios){
								if(dto != null){
									log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
											,dto.getTipoSolicitud(), dto.getUsername(), dto.getZona(),
											 dto.getSucursal(), dto.getConcepto(), dto.getNegocio() );
	
								}
							}	
						}
					}
				}catch (Exception e) {
					log.error("Error, sacar los destinatarios : #0", e.getMessage());
				}
			}
		}
	}
	
    /*metodos trabajados*/
	public void obtenerPedidoCotizacion(){
		try{
			if(pedidoCotizacion != null && pedidoCotizacion != 0){
				log.debug(" pedido o cotizacion :" +  pedidoCotizacion);	
				return;
			}
		}catch (Exception e) {
			log.error("error al sacar las cotizaciones o pedidos #0 ", e.getMessage());
		}
	
	}

	public void buscarInformacionCotizacionPedido() {
		this.tiempoEjecucionProceso = "";
		Calendar cal = Calendar.getInstance();
		String pedidoCotizacionAux = null;
		StringBuilder pedCotBuild = null;
		Double montoTotalAux = (double)0;
		Double montoNetoAux = (double)0;
        try {
				if(pedidoCotizacion == null || pedidoCotizacion == 0){
					FacesMessages.instance().add(Severity.WARN,"Debe ingresar uno codigo de pedido o cotización.");	
					this.mensajeGlogal = "Debe ingresar uno codigo de pedido o cotización.";
					return;
				}else{
					this.mensajeGlogal = null;
					pedidoCotizacionAux = String.valueOf(pedidoCotizacion); 
				}
				
				pedCotBuild = new StringBuilder(pedidoCotizacionAux);
				while(pedCotBuild.toString().length() < 10) {
						pedCotBuild.insert(0, '0');
				}
				
				log.debug(pedCotBuild.toString());
				
				if(listaCotPedDTOs == null){
					listaCotPedDTOs = new ArrayList<CotPedDTO>(0);
				}else{
					if(verificarCotizacionOPedido(pedCotBuild.toString())==true){
						FacesMessages.instance().add(Severity.WARN,"El pedido o cotización ya fue ingresada.");	
						return;
					}
				}				
				
				
			    /*rescatar y calcular  por concento y neegocio*/	
              SapSystem system = new SapSystem(globalParameters.getNameSap(),
                         globalParameters.getIpSap(), globalParameters.getClientSap(), 
                          globalParameters.getSystemNumberSap(),globalParameters.getUserSap(),
                         globalParameters.getPasswordSap());
              Connect connect = new Connect(system);
              JCoFunction function = connect.getFunction("ZSDFN_PEDIDO_1"); // Nombre RFC
              function.getImportParameterList().setValue("NUMPEDIDO",pedCotBuild.toString()); // Paso
              connect.execute(function);
              log.debug(function);
              log.debug(function.getTableParameterList());
              log.debug(function.getExportParameterList());
  			  /*Creacion de la tabla que contendra los datos provenientes de sap*/
  			  JCoTable datosCotizacionPedido = function.getTableParameterList().getTable("CABECERA"); //tabla de salida
  			
  			  if(function != null){
  				log.debug(datosCotizacionPedido);
  				log.debug("monto antes multiplicado por 100:"+ datosCotizacionPedido.getBigDecimal("NETWR").doubleValue()* 100);
  				montoTotalAux =  ((datosCotizacionPedido.getBigDecimal("NETWR").doubleValue() * 100)*1.19);
  				montoNetoAux =   (datosCotizacionPedido.getBigDecimal("NETWR").doubleValue() * 100);
  				montoTotal += (datosCotizacionPedido.getBigDecimal("NETWR").doubleValue() * 100)*1.19;
  				montoTotalNeto += (datosCotizacionPedido.getBigDecimal("NETWR").doubleValue() * 100);
  				
  				log.debug("montoTotal :"+ montoTotal);
  				log.debug("montoTotalNeto :"+ montoTotalNeto);
  			  }
              
              JCoTable tablaDatos_Clie = function.getTableParameterList().getTable("DETALLE"); // Tabla de Salida
              String codigoBase = "";
              ConceptosNegocio datosNegocio = null;
  			  /*Creacion de la tabla que contendra los datos provenientes de sap*/
  			  if(tablaDatos_Clie != null){
  				log.debug(tablaDatos_Clie);
  			  }   
  			  

  			  /*para sacar solo el negocio*/
  			  if(tablaDatos_Clie != null && tablaDatos_Clie.getNumRows() > 0){
  				 String negocio = null;
  				 CabeceraCotPedDTO negocioaux = null;
  				 listaCabeceraCotPeds = new ArrayList<CabeceraCotPedDTO>(0);
	              for (int i = 0; i < tablaDatos_Clie.getNumRows(); i++) {
	                  tablaDatos_Clie.setRow(i);
	                  String jerarquiaTotal = (String) tablaDatos_Clie.getString("PRODH");
	                  String jerarquia = tablaDatos_Clie.getString("PRODH").substring(0, 10);
	                  String codigo = tablaDatos_Clie.getString("PRODH").substring(jerarquiaTotal.length()-2, jerarquiaTotal.length());
	                  if(datosNegocio == null) {
	                       datosNegocio = intranetSapService.buscarNegocio(codigo, jerarquia);
	                       codigoBase = codigo;
	                  } else if(!codigo.equals(codigoBase)) { 
	                       datosNegocio = intranetSapService.buscarNegocio(codigo, jerarquia);
	                       codigoBase = codigo;
	                  }
	                
	                  if(negocio == null && datosNegocio != null){
	                	  if(verificarNegocio(datosNegocio) == false){
		                	  negocioaux = new CabeceraCotPedDTO();
		                	  negocioaux.setNegocio(datosNegocio);
		                	  listaCabeceraCotPeds.add(negocioaux);
		                	  negocio=datosNegocio.getDesNegocio();
		                	  log.error("El primer negocio :" + datosNegocio.getDesNegocio());
	                	  }else{
	                		  log.error("El negocio ya existe :" + datosNegocio.getDesNegocio());
	                	  }
	                  }else{
	                	  if(!negocio.equals(datosNegocio.getDesNegocio())){
	                		  if(verificarNegocio(datosNegocio) == false){
			                	  negocioaux = new CabeceraCotPedDTO();
			                	  negocioaux.setNegocio(datosNegocio);
			                	  listaCabeceraCotPeds.add(negocioaux);
			                	  negocio=datosNegocio.getDesNegocio();
			                	  log.error("El otro negocio :" + datosNegocio.getDesNegocio());
	                		  }else{
	                			  log.error("El negocio ya existe :" + datosNegocio.getDesNegocio());
	                		  }
	                	  }else{
	                		  log.error("El negocio ya existe :" + datosNegocio.getDesNegocio());
	                	  }
	                  }
	             }  
	             codigoBase = "";
	             datosNegocio = null;	
  			  }
  			  
  			  
 			  
  			  /*para saracar el monto totol por el concepto*/
  			  if(tablaDatos_Clie != null && tablaDatos_Clie.getNumRows() > 0){
  				Double porcentaje = (double) 0;
  				String concepto=null;
  	  			String negocio = null;
  	  			DetalleCotPedDTO ncm=null;
  	  			int cantidad = 0; int contador = 0;  				  
  				cantidad = tablaDatos_Clie.getNumRows();
	            for (int i = 0; i < tablaDatos_Clie.getNumRows(); i++) {
	                  tablaDatos_Clie.setRow(i);
	                  String jerarquiaTotal = (String) tablaDatos_Clie.getString("PRODH");
	                  String jerarquia = tablaDatos_Clie.getString("PRODH").substring(0, 10);
	                  String codigo = tablaDatos_Clie.getString("PRODH").substring(jerarquiaTotal.length()-2, jerarquiaTotal.length());
	                  if(datosNegocio == null) {
	                       datosNegocio = intranetSapService.buscarNegocio(codigo, jerarquia);
	                       codigoBase = codigo;
	                  } else if(!codigo.equals(codigoBase)) { 
	                       datosNegocio = intranetSapService.buscarNegocio(codigo, jerarquia);
	                       codigoBase = codigo;
	                  }
	                  log.debug(" negacio :"+ datosNegocio.getDesNegocio());
	                  log.debug(" concepto :"+ datosNegocio.getDescripcion());
	                  if(concepto == null && negocio == null && datosNegocio != null){
	                	  ncm = new DetalleCotPedDTO();
	                	  ncm.setNegocio(datosNegocio);
	                	  Double venta = tablaDatos_Clie.getBigDecimal("NETWR").doubleValue() * 100;
	                	  Double monto = venta * 1.19;
	                	  log.debug(" monto :"+ monto);
	                	  ncm.setMonto(monto.longValue());
	                      concepto = datosNegocio.getDescripcion();
	                      negocio = datosNegocio.getDesNegocio();
	                      if(cantidad == (contador + 1)){
	            			  porcentaje = ((ncm.getMonto().doubleValue() * 100)/montoTotal);
	            			  ncm.setPorceDelTotal(porcentaje);  	                    	  
	                    	  insertarDetalleToListaCabecera(ncm); 
	                      }else{
	                    	  contador++;
	                      }
	                  }else{
	                	  if(concepto.equals(datosNegocio.getDescripcion()) && negocio.equals(datosNegocio.getDesNegocio())){
		                	  Double venta = tablaDatos_Clie.getBigDecimal("NETWR").doubleValue() * 100;
		                	  Double monto = venta * 1.19;
		                	  log.debug(" monto :"+ monto);
		                	  Double suma = ncm.getMonto() + monto; 
		                	  ncm.setMonto(suma.longValue());
		                      if(cantidad == (contador + 1)){
		            			  porcentaje = ((ncm.getMonto().doubleValue() * 100)/montoTotal);
		            			  ncm.setPorceDelTotal(porcentaje);  
		                    	  insertarDetalleToListaCabecera(ncm); 
		                      }else{
		                    	  contador++;
		                      }		                	  
	                	  }else{
	            			  porcentaje = ((ncm.getMonto().doubleValue() * 100)/montoTotal);
	            			  ncm.setPorceDelTotal(porcentaje);  
                			  insertarDetalleToListaCabecera(ncm);
                			  porcentaje = (double)0;
		                	  ncm = new DetalleCotPedDTO();
		                	  ncm.setNegocio(datosNegocio);
		                	  Double venta = tablaDatos_Clie.getBigDecimal("NETWR").doubleValue() * 100;
		                	  Double monto = venta * 1.19;
		                	  log.debug(" monto :"+ monto);
		                	  ncm.setMonto(monto.longValue());
		                      concepto = datosNegocio.getDescripcion();
		                      negocio = datosNegocio.getDesNegocio();
		                      if(cantidad == (contador + 1)){
		            			  porcentaje = ((ncm.getMonto().doubleValue() * 100)/montoTotal);
		            			  ncm.setPorceDelTotal(porcentaje);  
		                    	  insertarDetalleToListaCabecera(ncm); 
		                      }else{
		                    	  contador++;
		                      }
	                	  }
	                  }
	             }
	             codigoBase = "";
	             datosNegocio = null;	              
  			  } 
  			  
  			  
  			  
  			  /*el detalle de la los productos*/
              for (int i = 0; i < tablaDatos_Clie.getNumRows(); i++) {
                    tablaDatos_Clie.setRow(i);
                    DetalleCp detalleCP = new DetalleCp();
                    String jerarquiaTotal = (String) tablaDatos_Clie.getString("PRODH");
                    String jerarquia = tablaDatos_Clie.getString("PRODH").substring(0, 10);
                    String codigo = tablaDatos_Clie.getString("PRODH").substring(jerarquiaTotal.length()-2, jerarquiaTotal.length());
                    String nombre = tablaDatos_Clie.getString("ARKTX");
                    String codigoArticulo = tablaDatos_Clie.getString("MATNR");
                    if(datosNegocio == null) {
                         datosNegocio = intranetSapService.buscarNegocio(codigo, jerarquia);
                         codigoBase = codigo;
                    } else if(!codigo.equals(codigoBase)) { 
                         datosNegocio = intranetSapService.buscarNegocio(codigo, jerarquia);
                         detalleCP.setConceptosNegocio(datosNegocio);
                         codigoBase = codigo;
                    }
                    detalleCP.setConceptosNegocio(datosNegocio);
                    Double costo = tablaDatos_Clie.getBigDecimal("WAVWR").doubleValue() * 100;  
                    Double venta = tablaDatos_Clie.getBigDecimal("NETWR").doubleValue() * 100; 
                    Double margen = ((venta - costo) / venta) * 100;
                    Double monto = venta * 1.19;
                    detalleCP.setMargen(margen);
                    detalleCP.setMonto(monto.longValue());
                    Double porcentaje = calcularMergenPorConceptoAndNegocio(detalleCP.getConceptosNegocio(), monto);
                    detalleCP.setMargProdProc(porcentaje);
                    detalleCP.setMargenesMultiplicado(multiplicarMargen(margen,porcentaje));
                    detalleCP.setNombre(nombre);
                    detalleCP.setCodigoArticulo(codigoArticulo);
                    insertarDetalleCPToListaDetalle(detalleCP);
               }
              log.debug("Total Cotizaciones Pedidos #0", montoTotal);
	             codigoBase = "";
	             datosNegocio = null;	
        } catch (Exception e) {
              log.debug("error al cargar cotizacion #0", e.toString());
        }
        
        sumarMxMToListaDetalle();
        
        if(listaCabeceraCotPeds != null && listaCabeceraCotPeds.size() > 0){
        	 habilitarVerProductos = true;
             /*agregar lista con su cotizacion o pedido */
        	 CotPedDTO objeto = new CotPedDTO();
        	 objeto.setNumeroCotizacion(pedCotBuild.toString());
        	 objeto.setListaCabeceraCotPeds(listaCabeceraCotPeds);
        	 objeto.setMontoNeto(montoNetoAux.longValue());
        	 objeto.setMontoTotal(montoTotalAux.longValue());
        	 if(listaCotPedDTOs != null){
        		 listaCotPedDTOs.add(objeto);
        	 }else{
        		 listaCotPedDTOs = new ArrayList<CotPedDTO>(0);
        		 listaCotPedDTOs.add(objeto);        		 
        	 }
        	 setPedidoCotizacion(new Long(0));
        }else{
        	habilitarVerProductos = false;
        	 setPedidoCotizacion(new Long(0));
        }
        
        
		sacarPorcetajeGlobalConceptoNegocio();
        sacarPorcetajeGlobal();
        sacarPorcetajeCotizacionToPedido();
        sacarPorcetajePedidoGlobal();
        insertMontosEnListaConceptosMontos();        
        
        Calendar cal2 = Calendar.getInstance();
        this.setTiempoEjecucionModal(calcularTiempoDelProceso(cal, cal2));        
  }
	
	public boolean verificarNegocio(ConceptosNegocio negocio){
		boolean exito =  false;
		try{
			if(listaCabeceraCotPeds == null || listaCabeceraCotPeds.size() == 0){
				return exito;
			}else{
				for(CabeceraCotPedDTO obj :listaCabeceraCotPeds){
					if(obj.getNegocio() != null){
						if(obj.getNegocio().getNegocio().equals(negocio.getNegocio())){
							exito = true;
							break;
						}
					}
				}
			}
		}catch (Exception e) {
			log.error("Al revisar si existe el negocio en la lista #0", e.getMessage() );
		}
		
		return exito;
	}
	public void insertarDetalleToListaCabecera(DetalleCotPedDTO obj){
		  Double suma  = (double)0;
		  List<CabeceraCotPedDTO> lista = null;
		  DetalleCotPedDTO objetoEliminar = null;
		 try{
			 if(listaCabeceraCotPeds != null && obj != null){
				 lista = new ArrayList<CabeceraCotPedDTO>(0);
				 for(CabeceraCotPedDTO o :listaCabeceraCotPeds){
					 if(o.getNegocio().getNegocio().equals(obj.getNegocio().getNegocio())){
						 if(o.getListaDetalle() == null){
							 o.setListaDetalle(new ArrayList<DetalleCotPedDTO>(0));
							 o.getListaDetalle().add(obj);
							 lista.add(o);
						 }else{
							 for(DetalleCotPedDTO nuevo : o.getListaDetalle()){
								 if(nuevo.getNegocio().getDescripcion().equals(obj.getNegocio().getDescripcion())){
									 suma = nuevo.getMonto().doubleValue()+obj.getMonto();
									 obj.setMonto(suma.longValue());
									 suma=(double) 0;
									 objetoEliminar = nuevo;
								 }
							 }
							 if(objetoEliminar != null){
								 o.getListaDetalle().remove(objetoEliminar);
							 }
							 o.getListaDetalle().add(obj);
							 lista.add(o);						 
						 }
					 }else{
						 lista.add(o);
					 }
				 }
				 if(listaCabeceraCotPeds != null){
					 listaCabeceraCotPeds.clear();
					 setListaCabeceraCotPeds(lista);
				 }else{
					 setListaCabeceraCotPeds(lista);
				 }
			 }
		 }catch (Exception e) {
			log.error("error al insertar la lista de detalle a la lista de cabecera #0", e.getMessage());
		}
	  }
    public void insertarDetalleCPToListaDetalle(DetalleCp detalleCP){
    	List<CabeceraCotPedDTO> lista = null;
		  List<DetalleCotPedDTO> listaDetalle = null;
		 try{
			 if(listaCabeceraCotPeds != null && detalleCP != null){
				 lista = new ArrayList<CabeceraCotPedDTO>(0);
				 for(CabeceraCotPedDTO o :listaCabeceraCotPeds){
					 if(o.getNegocio().getDesNegocio().equals(detalleCP.getConceptosNegocio().getDesNegocio())){
						 listaDetalle = new ArrayList<DetalleCotPedDTO>(0);
						 if(o.getListaDetalle() != null){
							for(DetalleCotPedDTO obj : o.getListaDetalle()){
								if(obj.getNegocio().getDescripcion().equals(detalleCP.getConceptosNegocio().getDescripcion())){
									if(obj.getListaDetalleCp() == null){
										obj.setListaDetalleCp(new ArrayList<DetalleCp>(0));
										obj.getListaDetalleCp().add(detalleCP);
									}else{
										obj.getListaDetalleCp().add(detalleCP);
									}
									listaDetalle.add(obj);
								}else{
									listaDetalle.add(obj);
								}
							}
						 }
						 o.setListaDetalle(listaDetalle);
						 lista.add(o);
					 }else{
						 lista.add(o);
					 }
				 }
				 if(listaCabeceraCotPeds != null){
					 listaCabeceraCotPeds.clear();
					 setListaCabeceraCotPeds(lista);
				 }else{
					 setListaCabeceraCotPeds(lista);
				 }
			 }
		 }catch (Exception e) {
			log.error("error al insertar la lista de detalle a la lista de cabecera #0", e.getMessage());
		}
	  }
    public Double calcularMergenPorConceptoAndNegocio(ConceptosNegocio datoNegocio, Double monto){
		  Double porcentaje = (double) 0;
		  DetalleCotPedDTO objetoFinal = null;
		  try{
			 if(listaCabeceraCotPeds != null && datoNegocio != null){
				  for(CabeceraCotPedDTO o :listaCabeceraCotPeds){
					 if(o.getNegocio().getDesNegocio().equals(datoNegocio.getDesNegocio())){
						 if(o.getListaDetalle() != null){
							for(DetalleCotPedDTO obj : o.getListaDetalle()){
								if(obj.getNegocio().getDescripcion().equals(datoNegocio.getDescripcion())){
									  objetoFinal = obj;
									  break;
								}
							}
						 }
					 }
				 }
			    if(objetoFinal != null){
				  porcentaje = ((monto * 100) / objetoFinal.getMonto()); 
			    }
			 }
		 }catch (Exception e) {
			log.error("error al insertar la lista de detalle a la lista de cabecera #0", e.getMessage());
		}
		return porcentaje;	  
	  }
	public Double multiplicarMargen(Double margen, Double margProdProc){
		  Double multiplicado = (double)0;
		  try{
			  multiplicado = ((margen/100) * (margProdProc / 100));
		  }catch (Exception e) {
			log.error("Error, al sacar el los parcentaje y se  multiplican por lineas #0", e.getMessage());
		}
		  return multiplicado;
	  }
	public void sumarMxMToListaDetalle(){
		  List<CabeceraCotPedDTO> lista = null;
		  List<DetalleCotPedDTO> listaDetalle = null;
		  Double suma = (double)0;
		  Double porcentajeFinal = (double)0;
		 try{
			 if(listaCabeceraCotPeds != null){
				 lista = new ArrayList<CabeceraCotPedDTO>(0);
				 for(CabeceraCotPedDTO o :listaCabeceraCotPeds){
						 listaDetalle = new ArrayList<DetalleCotPedDTO>(0);
						 if(o.getListaDetalle() != null){
							for(DetalleCotPedDTO obj : o.getListaDetalle()){
								if(obj.getListaDetalleCp() != null){
									for(DetalleCp dcp : obj.getListaDetalleCp()){
										suma += dcp.getMargenesMultiplicado();
									}
									porcentajeFinal = (suma * 100);
									obj.setPorcePorConcepto(porcentajeFinal);
									suma=(double)0;
								}
								listaDetalle.add(obj);
							}
						 }
						 o.setListaDetalle(listaDetalle);
						 lista.add(o);
				  }
				 if(listaCabeceraCotPeds != null){
					 listaCabeceraCotPeds.clear();
					 setListaCabeceraCotPeds(lista);
				 }else{
					 setListaCabeceraCotPeds(lista);
				 }
			 }
		 }catch (Exception e) {
			log.error("error al insertar la lista de detalle a la lista de cabecera #0", e.getMessage());
		}
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
	
	
	public void eliminarPedidocotizacion(CotPedDTO objeto ){
    	try{
    		if(objeto != null){
    			montoTotal = (double)(montoTotal.longValue() - objeto.getMontoTotal()); 
    			montoTotalNeto = (double)(montoTotalNeto.longValue() - objeto.getMontoNeto());
    			listaCotPedDTOs.remove(objeto);
    		}
    		sacarPorcetajeGlobalConceptoNegocio();
            sacarPorcetajeGlobal();
            sacarPorcetajeCotizacionToPedido();
            sacarPorcetajePedidoGlobal();
            insertMontosEnListaConceptosMontos();
    		
    	}catch (Exception e) {
			log.error("error al eliminar pedido o cotizacion de la grilla.");
		}
    }
	public void limpiarCotizacion(){
		limpiarTiempoEjecuciones();
		Calendar cal = Calendar.getInstance();
		try{
			 setMargenGlobal(this.porcentajeGlobal);
			 setMonto(montoTotal.longValue());
			 //obtenerCalculosMontosExposicion();
		 }catch (Exception e) {
			log.error("error al insertar la lista de detalle a la lista de cabecera #0", e.getMessage());
		}	
		Calendar cal2 = Calendar.getInstance();
		this.setTiempoEjecucionMenu(calcularTiempoDelProceso(cal, cal2));
 	}
	
	public  void limpiaMensajeGlobal(){
		setMensajeGlogal(null);
	}
	
	public boolean verificarCotizacionOPedido(String codigoAux){
		boolean existe = false;
		try{
			for(CotPedDTO o : listaCotPedDTOs){
				if(o.getNumeroCotizacion().equals(codigoAux)){
					existe = true;
					break;
				}
			}
		}catch (Exception e) {
			log.error("Error, al verificar si existe...", e.getMessage());
		}
		return existe;
	}

    /*metodos trabajados*/
	public void obtenerSucursalSeleccionada(){
		if(sucursal != null){
			log.debug("sucursal: "+ this.sucursal.getDescripcion());
		}
	}
    /*metodos saca las forma de pagos */
	@SuppressWarnings("unchecked")
	public void obtenerFormaPagos(){
		if(tipoFormaPago != null){
			setEncabezado(null);
			log.debug("tipo forma de pago : "+ this.tipoFormaPago.getDescripcion());
			try{
				
				if(this.tipoFormaPago.getDescripcion().equals("Efectivo")){
					FormaPagoEncabezado formaPagoAux = entityManager.find(FormaPagoEncabezado.class, new Long(1));
					if(formaPagoAux != null ){
						listaEncabezados = new ArrayList<FormaPagoEncabezado>(0);
						listaEncabezados.add(formaPagoAux);
						setEncabezado(formaPagoAux);
						setPie(this.monto);
						this.activarConsulta = true;
						this.habilitar = true;
						obtenerCalculosMontosExposicion();
						//listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
					}
				}else{
					this.pie = null;
					this.habilitar = false;
					//this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
					List<FormaPagoEncabezado> lista = (List<FormaPagoEncabezado>)
							 entityManager.createQuery("select fpe " +
			     					   "from FormaPagoEncabezado fpe " +
			     					   "where fpe.tipoFormaPago.idTipoFormaPago=:idTipoFormaPagoAux " +
			     					   "and fpe.tipo.codTipoSolicitud=:tipoSolicitud " +
			     					   "order by fpe.descripcion asc")
			 						   .setParameter("idTipoFormaPagoAux", tipoFormaPago.getIdTipoFormaPago())
			 						   .setParameter("tipoSolicitud", String.valueOf(this.paginaIngreso))
			 						   .getResultList();
					
					if(lista != null && this.paginaIngreso == 11){
						listaEncabezados = new ArrayList<FormaPagoEncabezado>(0);
						for(FormaPagoEncabezado fpe : lista){
							if(!fpe.getDescripcion().equals("Efectivo")){
								listaEncabezados.add(fpe);
							}
						}
					}else if(lista != null && this.paginaIngreso == 12){
						setListaEncabezados(lista);
					}
				}
				
				if((this.monto  != null && this.monto != 0) && (this.pie != null && this.pie != 0)){
					obtenerCalculosMontosExposicion();
				}
			}catch (Exception e) {
				log.error("Error, al sacar la lista de forma de pago formada por el usuario. #0", e.getMessage());
			}
		}
	}
		
	public void obtenerFormaPagosEncabezado(){
	      String[] array = null;
	      this.mensajeGlogal = null;
			if(encabezado != null){
				log.debug("encabezado : "+ this.encabezado.getDescripcion());
				try{
					array = encabezado.getDescripcion().split(",");
				}catch (Exception e) {
					log.error("error, al sacar la cantidad de registro #0", e.getMessage());
				}
				
				try{
					if(this.paginaIngreso == 11 || this.paginaIngreso == 12){
						if(this.paginaIngreso == 11 && array != null){
							boolean valido = false;
							for(String cadena : array){
								if(cadena.equals("Efectivo")){
									valido = true;
									break;
								}
							}
							if(valido){
								FacesMessages.instance().add(Severity.WARN,"Debe ingresar un pie en la venta normal.");
								this.mensajeGlogal = "Debe ingresar un pie en la venta normal.";
								this.pie = null;
								this.habilitar = false;
								return;									
							}else{
								this.pie = null;
								this.activarConsulta = true;
								this.habilitar = true;
								obtenerCalculosMontosExposicion();
							}
						}else if (this.paginaIngreso == 12){
							this.evaluadorCompraVC = false;
							if(array != null){
								if(array.length >= 2){
									for(String nombre: array){
										if(nombre !=null && nombre.equals("Efectivo")){
											this.evaluadorCompraVC =  true;
											break;
										}
									}
									if(this.evaluadorCompraVC){
										FacesMessages.instance().add(Severity.WARN,"Debe ingresar un pie en la venta en cuotas.");
										this.mensajeGlogal = "Debe ingresar un pie en la venta en cuotas.";
										this.habilitar = false;
										if(listaUsuarioCorreoagregados != null){
											listaUsuarioCorreoagregados.clear();
										  }
										return;			
									}else{
										this.activarConsulta = true;
										this.habilitar = true;
										obtenerCalculosMontosExposicion();
									}
								}else{
									FacesMessages.instance().add(Severity.WARN,"Debe seleccionar el tipo de pago con mas cuotas");
									setEncabezado(null);
									return;									
								}
							}else{
								FacesMessages.instance().add(Severity.ERROR,"error al seleccionar el tipo de pago de la solicitud.");
								setEncabezado(null);						
							}
						}
					}
				}catch (Exception e) {
					FacesMessages.instance().add(Severity.INFO,"error, al calcular los montos del Riesgo Küpfer  #0...", e.getMessage());
				}
			}
		}

	
	/*metodo para obtener la informacion del cliente a traves del rut*/
	
	/*boolean que identifica si es rut extranjero o no*/ 
	private boolean rutExtranjero = false;
	public boolean isRutExtranjero() { return rutExtranjero; }
	public void setRutExtranjero(boolean rutExtranjero) { this.rutExtranjero = rutExtranjero; }
	
	/*Inicio metodo*/
	public void obtenerInformacionCliente(){
		limpiarTiempoEjecuciones();
		log.debug("Rut: #0", rutAux);
		Calendar cal = Calendar.getInstance();
		clienteTarget = new ClienteDTO();
		clienteTarget.setRut(rutAux); //mantuve el rut en la variable rutAux (no se si la ocuparias para algo) solo traspase su valor al objeto clienteTarget
		
		//verificar que el rut no es vacio ni nulo
		if(clienteTarget.getRut() == null || clienteTarget.getRut().trim().isEmpty()) {
			clienteTarget.setRut(null);
			return;
		}
		
		//verificar que el rut no tenga menos de 8 caracteres
		if(clienteTarget.getRut().length() < 8) {
			FacesMessages.instance().add(Severity.ERROR,"Rut invalido, debe tener al menos 8 caracteres");
			return;
		}
		
		//agregar guin en caso de ser necesario
		String rut = globalService.agregarGuionRut(clienteTarget.getRut());
		log.debug("rut #0 #1", rut,globalService.validarRut(rut));
		
		
		//verificar si es rut extranjero, si lo es, se omite la validacion del mismo
		if(!rutExtranjero) {
			if(globalService.validarRut(rut))
				clienteTarget.setRut(rut);
			else {
				clienteTarget.setRut(null);
				FacesMessages.instance().add(Severity.ERROR,"Rut invalido");
				return;
			}
		} else {
			clienteTarget.setRut(rut);
		}
		try {

			 /*Comienzo de la extraccion de datos desde Sap*/
			 /* declaracion de parametros sap desde archivo global parameters y creacion de la conexion*/	
			SapSystem system = new SapSystem(globalParameters.getNameSap(),
					globalParameters.getIpSap(), globalParameters.getClientSap(), 
					globalParameters.getSystemNumberSap(),globalParameters.getUserSap(),
					globalParameters.getPasswordSap()); 

			Connect connect = new Connect(system);
			
			 /* Paso de los parametros necesarios a la rfc*/
			functionDatosClientes = connect.getFunction("ZSDFN_DATOS_CLIENTE_11"); // Nombre RFC
			functionDatosClientes.getImportParameterList().setValue("RUTCLIENTE", clienteTarget.getCleanRut().toUpperCase()); // Paso de parametros

			connect.execute(functionDatosClientes);
			
			/*Creacion de la tabla que contendra los datos provenientes de sap*/
			JCoTable datosCliente = functionDatosClientes.getTableParameterList().getTable("DATOS_CLIE"); //tabla de salida
			
			if(datosCliente != null){
				
				String swExiste  = (String)functionDatosClientes.getExportParameterList().getValue("SW_EXISTE");
				if("S".equals(swExiste)){
					setHabilitarCtaCte(true);
					/*traspaso de los datos necesarios desde la tabla hacia el objeto Cliente Target*/
					clienteTarget.setRazonSocial((String) datosCliente.getValue("NOMCLIENTE"));
					clienteTarget.setGiro((String)functionDatosClientes.getExportParameterList().getValue("GIRO"));
					clienteTarget.setDireccion((String) datosCliente.getValue("DIRCLIENTE"));
					clienteTarget.setFono((String) datosCliente.getValue("TELCLIENTE"));
					clienteTarget.setFax((String) datosCliente.getValue("FAXCLIENTE"));
					clienteTarget.setComuna((String) datosCliente.getValue("COMCLIENTE"));
					clienteTarget.setCiudad((String) datosCliente.getValue("CIUCLIENTE"));
					clienteTarget.setSucursalCliente((String) datosCliente.getValue("SUCCLIENTE"));
					
					String codigoCanal = (String)functionDatosClientes.getExportParameterList().getValue("CANAL");
					if(codigoCanal != null){
						if("X".equals(codigoCanal)){
							canalVenta = TipoCuentasKupferType.KX;
							clienteTarget.setCanalVenta	(canalVenta.getNombre());
							clienteTarget.setCodigoCanal(canalVenta);
						}else if("G".equals(codigoCanal)){
							canalVenta = TipoCuentasKupferType.GC;
							clienteTarget.setCanalVenta	(canalVenta.getNombre());
							clienteTarget.setCodigoCanal(canalVenta);
						}else if("M".equals(codigoCanal)){
							canalVenta = TipoCuentasKupferType.MX;
							clienteTarget.setCanalVenta	(canalVenta.getNombre());
							clienteTarget.setCodigoCanal(canalVenta);
						}
						
					}
					
					clienteTarget.setTipoCliente((String) datosCliente.getValue("TIPSEGURO"));
					BigDecimal linea = (BigDecimal) datosCliente.getValue("LINCREDITO");
					Long monto = (linea.longValue() * 100);
					clienteTarget.setLineaCreditoKH(new BigDecimal(monto));
					clienteTarget.setLineaCreditoKHDisponible((BigDecimal) functionDatosClientes.getExportParameterList().getValue("LINDISPONIBLE"));
					
					//        LCredUtilizado.Caption = Format((LbLCreditoKH.Caption - LCredDisp.Caption), Formato)
					Long lineaCreditoUtilizada = (clienteTarget.getLineaCreditoKH().longValue() - clienteTarget.getLineaCreditoKHDisponible().longValue());
					clienteTarget.setLineaCreditoKHUtilizado(new BigDecimal(lineaCreditoUtilizada));
					
					clienteTarget.setEstadoLineaCreditoKH((String) functionDatosClientes.getExportParameterList().getValue("ESTADO_LC_KH"));
					clienteTarget.setNumeroRV((String) functionDatosClientes.getExportParameterList().getValue("NRORV"));
					clienteTarget.setEstadoLineaEnCuotas((String) functionDatosClientes.getExportParameterList().getValue("ESTADO_LC_CUO"));
					String descondpago = (String) functionDatosClientes.getExportParameterList().getValue("DESCONDPAGO");
					if(descondpago != null){
						clienteTarget.setCondicionPago((String)datosCliente.getValue("CONPAGO") +"-" +descondpago);
						clienteTarget.setCodigoCondicionPago((String)datosCliente.getValue("CONPAGO"));
					}
					
					
					String clasifOPB =(String) functionDatosClientes.getExportParameterList().getValue("CLASIF_OPB");
					try{
						if(clasifOPB!=null){
							Object obj = intranetSapService.descripcionTipoClienteCav(clasifOPB);
							if(obj != null){
								clienteTarget.setTipoCliente(obj.toString());
							}else{
								clienteTarget.setTipoCliente("No Clasificado");
							}
						}else{clienteTarget.setTipoCliente("No Clasificado");}
						
					}catch (Exception e) {
						clienteTarget.setTipoCliente("No Clasificado");
					}

					
					String clsRiesgo = (String) datosCliente.getValue("CLSRIESGO");
					clienteTarget.setCodigoclasificacionRiesgo(clsRiesgo);
					try{
						Object clasRiesgo = intranetSapService.sacarDescripClasificacionRiesgo(clsRiesgo);
						if(clasRiesgo != null){
							clienteTarget.setClasificacionRiesgo(clsRiesgo +"-"+ clasRiesgo );
						}else{
							clienteTarget.setClasificacionRiesgo(clsRiesgo);
						}
						
					}catch (Exception e) {
						log.error("Error al sacar la descripcion del la clasificacion de riesgo #0", e.getMessage());
						clienteTarget.setClasificacionRiesgo(clsRiesgo);
					}
					
					
					BigDecimal ventasTotal12Meses = (BigDecimal) functionDatosClientes.getExportParameterList().getValue("VTATOTAL12");
					clienteTarget.setVentasTotal12Meses(new BigDecimal(ventasTotal12Meses.longValue() * 100));
					clienteTarget.setCreacionSap((Date) functionDatosClientes.getExportParameterList().getValue("FECCREACION"));
					clienteTarget.setSeguro((String) datosCliente.getValue("TIPSEGURO"));
					clienteTarget.setVigenciaSeguro((String)datosCliente.getValue("VIGSEGURO"));
					clienteTarget.setMontoSeguro((BigDecimal) functionDatosClientes.getExportParameterList().getValue("MTOSEGURO_P"));
					clienteTarget.setMontoSeguroUf(((BigDecimal) functionDatosClientes.getExportParameterList().getValue("MTOSEGURO")).longValue());
					
					BigDecimal ventasProm12Meses = (BigDecimal) datosCliente.getValue("VTAPROM12");
					clienteTarget.setVentasProm12Meses(new BigDecimal(ventasProm12Meses.longValue() * 100));
					
					String facprom12 = (String) datosCliente.getValue("FACPROM12");
					if(facprom12 != null){
						clienteTarget.setFactProm12Meses(Long.parseLong(facprom12));
					}
					String cantMesesVentas = (String)functionDatosClientes.getExportParameterList().getValue("NUMMESES"); 
					if(cantMesesVentas != null){
						clienteTarget.setCantMesesVentas(Long.parseLong(cantMesesVentas));
					}
				
					ObtenerEstadoSolictud("I");
					if(estadoInicial != null){
						clienteTarget.setEstadoSolicitud(estadoInicial.getDesEstado());
					}
					
					
					/*Se setean lo valores de comportamiento comercial*/
					clienteCompComrcial = new CompComercialDTO();
					clienteCompComrcial.setMontoTotalMoro(((BigDecimal)functionDatosClientes.getExportParameterList().getValue("MTOMOROSIDAD")).longValue());
					String numeroMoro = (String)functionDatosClientes.getExportParameterList().getValue("CANMOROSIDAD");
					if(numeroMoro != null){
						clienteCompComrcial.setNumeroMoro(Long.parseLong(numeroMoro));
					}
					String diasMoras = (String)functionDatosClientes.getExportParameterList().getValue("DIASMORA");
					if(diasMoras != null){
						clienteCompComrcial.setDiasMoras(Long.parseLong(diasMoras));
					}				
					
					
					clienteCompComrcial.setMontoTotalProt(((BigDecimal)functionDatosClientes.getExportParameterList().getValue("MTOPROTESTO")).longValue());
					String numeroProt = (String)functionDatosClientes.getExportParameterList().getValue("CANPROTESTO");
					if(numeroProt != null){
						clienteCompComrcial.setNumeroProt(Long.parseLong(numeroProt));
					}
					String dmPort = (String)functionDatosClientes.getExportParameterList().getValue("CNTDM");
					if(dmPort != null){
						clienteCompComrcial.setDmPort(dmPort);
					}	

					
					JCoFunction funcPedidos = connect.getFunction("ZSDFN_FRONT_PEDCOT_R");
					log.debug(clienteTarget.getCleanRut());
					funcPedidos.getImportParameterList().setValue("RUTCLIENTE", clienteTarget.getCleanRut());
					connect.execute(funcPedidos);
					
					if(funcPedidos.getTableParameterList().getTable("DETALLE").getNumRows() == 0) {
						funcPedidos.getImportParameterList().setValue("RUTCLIENTE", clienteTarget.getCleanRut().toUpperCase());
						connect.execute(funcPedidos);
					}
					

					long flujoPedidoProceso=0;
					JCoTable pedidosEnProceo = funcPedidos.getTableParameterList().getTable("DETALLE"); 
					log.debug(pedidosEnProceo);
					for (int i = 0; i < pedidosEnProceo.getNumRows(); i++) {
						pedidosEnProceo.setRow(i);
						String tipo = pedidosEnProceo.getString(0);
						if(tipo.toLowerCase().equals("p")) {
							flujoPedidoProceso += pedidosEnProceo.getBigDecimal("MONTO").longValue();
							log.debug(flujoPedidoProceso);
						}
					}
					
					log.debug("monto : "+ flujoPedidoProceso);
					clienteTarget.setPedidoEnProceso(flujoPedidoProceso);
					
					
					
					/*se crean el obejto de exposicion*/
					clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
					
					/*se recorre la tabla para sacar los DM*/
					JCoTable DMCliente = functionDatosClientes.getTableParameterList().getTable("DM_CLIENTE"); //indicas la tabla de la cual necesitas extraer datos
					if(DMCliente !=null && DMCliente.getNumRows() > 0){
						int contador = 0;
						listaDescripcionDM = new ArrayList<String>(0);
						listaDmdtos = new ArrayList<DMDTO>(0);
						for (int i = 0; i < DMCliente.getNumRows(); i++) {
							DMDTO nuevo = new DMDTO();
							DMCliente.setRow(i);
				            String codigoStr = (String) DMCliente.getValue("CODCLIENTE");
				            String descripcion = (String) DMCliente.getValue("NAME1");
				            String vwer = (String) DMCliente.getValue("VWERK");
				            if(codigoStr != null && descripcion != null && vwer != null){
				            	listaDescripcionDM.add(codigoStr+"-"+descripcion);
				            	nuevo.setCodigoStr(codigoStr);
				            	nuevo.setDescripcion(descripcion);
				            	nuevo.setVwer(vwer);
				            	listaDmdtos.add(nuevo);
				            	if(contador == 0) dm = descripcion;
				            	
				            	nuevo = null; 
				            	contador++;
				            	
				            }
				            log.error("codigo de cliente #0, descripcion de #1, vwer #2 ", codigoStr, descripcion, vwer);
						}				
					}else{
						listaDescripcionDM = new ArrayList<String>(0);
						listaDmdtos = new ArrayList<DMDTO>(0);
						DMDTO nuevo = new DMDTO();
						nuevo.setCodigoStr(this.rutAux);
						nuevo.setDescripcion(clienteTarget.getRazonSocial());
						nuevo.setVwer("");
						listaDescripcionDM.add(nuevo.getCodigoStr()+"-"+nuevo.getDescripcion());
						dm = nuevo.getCodigoStr()+"-"+nuevo.getDescripcion();						
					}
					setMuestraSeleccionSolicitud(true);
				}else{
					if("N".equals(swExiste)){
						FacesMessages.instance().add(Severity.INFO,"Cliente No Existe, Verifique..."  );
					}else if("A".equals(swExiste)){
						FacesMessages.instance().add(Severity.INFO,"Cliente No Es Solicitante, Verifique..."  );
					}else if("B".equals(swExiste)){
						FacesMessages.instance().add(Severity.INFO,"Cliente No Es Kupfer Express Verifique..."  );
					}
				}
			}else{
				FacesMessages.instance().add(Severity.INFO,"No se pudo traer los datos de SAP para el cliente #0...", this.rutAux);
			}
		
			Calendar cal2 = Calendar.getInstance();
			this.setTiempoEjecucionProceso(calcularTiempoDelProceso(cal, cal2));		
		} catch (Exception e) {
			log.debug("error al cargar datos cliente desde sap#0",e.toString());
		}
	}

	
	
	
	
	@SuppressWarnings("unchecked")
	public void cambiarSolicitud(String modulo){
		log.debug("modulo #0", modulo);
		LcredTipoSolicitud tipo =   dataTipoSolicitud.get(modulo);
		if(tipo != null){
			this.setTituloIngreso(tipo.getDesTipoSolicitud());
			this.setPaginaIngreso(Integer.parseInt(tipo.getCodTipoSolicitud()));
			tipoSolicitud = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));
			
			if(this.paginaIngreso == 11 || this.paginaIngreso == 12){
				setTituloGeneral("Ventas");
				if(this.paginaIngreso == 11){
					try{
						listaTipoFormaPagos = (List<TipoFormaPago>)entityManager.
								createQuery("select tfp from TipoFormaPago tfp ")
								.getResultList();
						
					}catch (Exception e) {
						log.error("Error, de tipo de forma de pagos #0", e.getMessage());
					}
				}else if( this.paginaIngreso == 12){
					try{
						listaTipoFormaPagos = (List<TipoFormaPago>)entityManager.
								createQuery("select tfp from TipoFormaPago tfp where tfp.idTipoFormaPago not in (2) ")
								.getResultList();
					}catch (Exception e) {
						log.error("Error, de tipo de forma de pagos #0", e.getMessage());
					}					
					
				}

				habilitarVerProductos = true;
			}else if(this.paginaIngreso == 21 || this.paginaIngreso == 22 || this.paginaIngreso == 23 || this.paginaIngreso == 24){
				setTituloGeneral("Línea de Crédito");
				
				if(listaClsRiesgos == null || listaClsRiesgos.size() == 0){
					listaClsRiesgos = intranetSapService.getClasificacionRiesgo();
				}
				if(listaCondicionPagos == null || listaCondicionPagos.size() == 0){
					listaCondicionPagos = intranetSapService.getCondicionPago();
				}
				evaluarInnominalNominalRKupferAseguradora();
		
			}else if(this.paginaIngreso == 31 || this.paginaIngreso == 32){
				setTituloGeneral("Cambio de Condiciones");
				
				if(listaClsRiesgos == null || listaClsRiesgos.size() == 0){
					listaClsRiesgos = intranetSapService.getClasificacionRiesgo();
				}
				if(listaCondicionPagos == null || listaCondicionPagos.size() == 0){
					listaCondicionPagos = intranetSapService.getCondicionPago();
				}
				
				if(this.paginaIngreso == 31){
					setCondicionRiesgo(true);
					setComboCRiesgo(false);
					setCondicionPago(false);
					setComboCPago(true);
				}else if(this.paginaIngreso == 32){
					setCondicionRiesgo(false);
					setComboCRiesgo(true);
					setCondicionPago(true);
					setComboCPago(false);	
				}
				evaluarRiesgoPago();
			}else if(this.paginaIngreso == 41 || this.paginaIngreso == 42 || this.paginaIngreso == 43 || this.paginaIngreso == 44){
				
				if(this.paginaIngreso == 41 || this.paginaIngreso == 42){
					setTituloGeneral("Bloqueo y Desbloqueo.");
					setMotivoBloqueoDesbloqueo(false);
				}else if(this.paginaIngreso == 43){	
					setTituloGeneral("Creación de DM.");
					setMotivoBloqueoDesbloqueo(true);
					
				}else if(this.paginaIngreso == 44){
					setTituloGeneral("Prorroga de Cheques.");
					
				}
			}
			
		}
		/*limpiar variables por cvada cambio de pestaña*/
		limpiarSegunTipoSolictud(this.paginaIngreso);
	}

	public void evaluarInnominalNominalRKupferAseguradora(){
		setClsRiesgo(null); setcPago(null);
		String codigoClsRiesgo = null; String codigoCdnPago = null;
		listaStringClsRiesgos = new ArrayList<String>(0); listaStringCdnPagos = new ArrayList<String>(0);		
		try{
			if(this.paginaIngreso == 21){
				codigoClsRiesgo ="K34";
				activarCboClsRiesgo = false;
				codigoCdnPago = "CC14";
				activarCboCdnPago = false;
			}else if(this.paginaIngreso == 22){
				codigoClsRiesgo = clienteTarget.getCodigoclasificacionRiesgo();
				activarCboClsRiesgo = false;
				codigoCdnPago = clienteTarget.getCodigoCondicionPago();
				activarCboCdnPago = false;					
			}else if(this.paginaIngreso == 23){
				codigoClsRiesgo ="004";
				activarCboClsRiesgo = false;
				codigoCdnPago = "CC14";
				activarCboCdnPago = false;					
			}else if(this.paginaIngreso == 24){
				//008 = cliente antiguo --- 005 = cliente nuevo 
				codigoClsRiesgo ="005";
				activarCboClsRiesgo = false;
				codigoCdnPago = "CC01";
				activarCboCdnPago = false;	
			}
			
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
	
	public void evaluarRiesgoPago(){
		setClsRiesgo(null); setcPago(null);
		String codigoClsRiesgo = null; String codigoCdnPago = null;
		listaStringClsRiesgos = new ArrayList<String>(0); listaStringCdnPagos = new ArrayList<String>(0);		
		try{
			if(this.paginaIngreso == 31){
				codigoClsRiesgo ="";
				activarCboClsRiesgo = false;
				codigoCdnPago = "";
				activarCboCdnPago = false;
			}else if(this.paginaIngreso == 32){
				codigoClsRiesgo = "";
				activarCboClsRiesgo = false;
				codigoCdnPago = "";
				activarCboCdnPago = false;					
			}
			
			/*recorrer la lista para encontrar los codigo*/
			if(this.listaClsRiesgos != null){
				for(ClsRiesgoDTO cls : this.listaClsRiesgos){
					//log.error("codigo de la clasificacion de riesgo #0", cls.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((cls.getCodigo().trim())); cadena.append("-"); cadena.append((cls.getDescripcion().trim()));
					if(cls != null  && (cls.getCodigo().trim()).equals(codigoClsRiesgo)){
						//log.error("clasificacion de riesgo #0", cadena.toString());
						setClsRiesgo(cadena.toString());
					}
					listaStringClsRiesgos.add(cadena.toString());
					cadena = null;
					
				}
			}
			
			if(this.listaCondicionPagos != null){
				for(CPagoDTO obj : this.listaCondicionPagos){
					//log.error("codigo de la condicion de riesgo #0", obj.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((obj.getCodigo().trim())); cadena.append("-"); cadena.append((obj.getDescripcion().trim()));
					if(obj != null  && obj.getCodigo().equals(codigoCdnPago) ){
						//log.error("condicion de riesgo #0", cadena.toString());
						setcPago(cadena.toString());
					}
					listaStringCdnPagos.add(cadena.toString());
					cadena = null;
				}					
			}			
			
		}catch (Exception e) {
			log.error("Error, a evaluar los combo #0", e.getMessage());
		}
		
		
	}
	
	
	
	/* agregar usuario correo*/
	public void agregarUsuarioCorreoListaUsuarioCorreoReserva(){
		if(this.usuarioCorreo != null){
			String array[] = this.usuarioCorreo.split(".-");
			UsuarioCorreoDTO usuCorreoAux =  buscarUsuarioCorreo(array[0]);
			if(usuCorreoAux != null){
				if(listaUsuarioCorreoagregados.contains(usuCorreoAux)){
					setMensajeExplicativo("El usuario "+ this.usuarioCorreo + " ya fue agregado a la lista.");
					this.usuarioCorreo = null;
				}else{
					usuCorreoAux.setStatus(true);
					//listaUsuarioCorreoagregados.add(usuCorreoAux);
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
	public UsuarioCorreoDTO buscarUsuarioCorreo(String codigo){
		UsuarioCorreoDTO usuarioCorreo = null;
		if(listaUsuarioCorreos != null && codigo != null){
			for(UsuarioCorreoDTO u: listaUsuarioCorreos){
				if(u.getCorrelativo() ==  Integer.parseInt(codigo)){
					usuarioCorreo = u;
					break;
				}
			}
		}
		return usuarioCorreo;
	}

	
//	public void obtenerCorreo(String usarioAux, String tipoSolicitud,List<String> listaConceptos, List<String> listaNegocio, String sucSolicitud, int canal){
//		/*va a buscar los destinatarios de las solictudes*/
//		Sucursal sucursalAux = null;
//		try{
//			if(listaDestinatarios != null){
//				listaDestinatarios.clear();
//			}
//			if(sucSolicitud != null){
//				sucursalAux = scoringService.obtenerSucursalForCodigo(sucSolicitud);
//			}
//			if(sucursalAux != null){
//				listaDestinatarios = scoringService.obtenerListaDestinatario(usuarioLogueado.getAlias(), "01", 
//		                        cliente.getCodigoCanal().name(), 
//		                        sucursalAux.getZona(), 
//		                        sucursalAux.getCodigo(), 
//		                        solicitud.getRiesgoKupfer().longValue(), 
//		                        tipoSolicitud);	
//			}else{
//				listaDestinatarios = scoringService.obtenerListaDestinatario(usuarioLogueado.getAlias(), "01", 
//                        cliente.getCodigoCanal().name(), 
//                        sucursal.getZona(), 
//                        sucursal.getCodigo(), 
//                        solicitud.getRiesgoKupfer().longValue(), 
//                        tipoSolicitud);					
//			}
//			
//			if(listaDestinatarios != null){
//				listaUsuarioSegurDTO = new ArrayList<UsuarioSegurDTO>(0);
//				listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
//				UsuarioSegurDTO user = scoringService.sacarDatosSessionUsuario(usarioAux);
//				if(user != null){
//					SolicitudUsuarioCorreo nuevo = getObtenerSolicitudUsuarioCorreo(user.getAlias());
//					if(nuevo == null){
//						nuevo = new SolicitudUsuarioCorreo();
//						nuevo.setUsername(user.getAlias());
//						nuevo.setNombre(user.getNombre());
//						nuevo.setCorreo(user.getCorreo());
//						nuevo.setEvaluar(false);
//						nuevo.setConfirmar(false);
//						nuevo = scoringService.persistSolicitudCorreo(nuevo);
//						//nuevo.setCorreo(usuarioCorreo.getCorreo());
//						listaUsuarioCorreoagregados.add(nuevo);
//					}else{
//						if(nuevo.getEvaluar() == false){
//							nuevo.setConfirmar(false);
//							//nuevo.setCorreo(usuarioCorreo.getCorreo());
//							listaUsuarioCorreoagregados.add(nuevo);
//						}else{
//							nuevo.setEvaluar(false);
//							nuevo.setConfirmar(false);
//							nuevo = scoringService.mergerSolicitudCorreo(nuevo);
//							//nuevo.setCorreo(usuarioCorreo.getCorreo());
//							listaUsuarioCorreoagregados.add(nuevo);
//						}
//					}
//				}
//				
//				/*evaluacion de la solicitud y el usuario */
//				try{
//					if(listaDestinatarios != null){
//						for(DestinatarioDTO dto : listaDestinatarios){
//							if(dto != null){
//								UsuarioSegurDTO usuarioSegur =  scoringService.sacarDatosSessionUsuario(dto.getUsername());	
//								if(dto.getTipoSolicitud().equals("1") || dto.getTipoSolicitud().equals("2") || dto.getTipoSolicitud().equals("3")|| dto.getTipoSolicitud().equals("5")){
//									if(usuarioSegur != null){
//										if(paginaIngreso == 11 || paginaIngreso == 12 || paginaIngreso == 21 || paginaIngreso == 22 || paginaIngreso == 23){
//											boolean validoNegocio = false;
//											boolean validoConcepto = false;
//											//List<Integer> listaPerfiles =  scoringService.obtenerPerfilesDelUsuario(usuarioSegur.getIdPersonal());
//											List<FuncionesType> listafunciones = scoringService.obtenerFuncionesUsuario(usuarioSegur.getIdPersonal());
//											if(listaNegocio != null && listaNegocio.size() != 0){
//												for(String codigo : listaNegocio){
//													if(listafunciones != null){
//														for(FuncionesType ft : listafunciones){
//															if(ft != null && ft.ordinal() != 0){
//																log.debug("codigo de usuario: #0 y nombre  #1", usuarioSegur.getIdPersonal(), usuarioSegur.getNombre());
//																log.debug("negocio #0", codigo);
//																log.debug("canal #0  y codigo canal #1",cliente.getCodigoCanal(), canal);
//																log.debug("funcion  #0 y codigo de funcion #1", ft, ft.ordinal());
//																
//																validoNegocio = scoringService.obtenerHabilitacionUsuario(usuarioSegur.getIdPersonal(),
//																													codigo.trim(), 
//																													canal, 
//																													ft.ordinal());
//																
//																log.debug("resultado #0", validoNegocio);
//																if(validoNegocio){
//																	break;
//																}
//														    }
//														}
//													}
//												}
//											}
//											if(listaConceptos != null && listaConceptos.size() != 0){
//												for(String codigo : listaConceptos){
//													if(listafunciones != null){
//														for(FuncionesType ft : listafunciones){
//															if(ft != null && ft.ordinal() != 0){
//																log.debug("codigo de usuario: #0 y nombre  #1", usuarioSegur.getIdPersonal(), usuarioSegur.getNombre());
//																log.debug("negocio #0", codigo);
//																log.debug("canal #0  y codigo canal #1", cliente.getCodigoCanal(), canal);
//																log.debug("funcion  #0 y codigo de funcion #1", ft, ft.ordinal());
//																
//																validoConcepto = scoringService.obtenerHabilitacionUsuario(usuarioSegur.getIdPersonal(),
//																													codigo.trim(), 
//																													canal, 
//																													ft.ordinal());
//																
//																log.debug("resultado #0", validoConcepto);
//																if(validoConcepto){
//																	break;
//																}
//														    }
//														}
//													}
//												}
//											}								
//											
//											if(paginaIngreso == 11 || paginaIngreso == 12){
//												if(validoNegocio == true && validoConcepto == true){
//													SolicitudUsuarioCorreo suc =  getObtenerSolicitudUsuarioCorreo(usuarioSegur.getAlias());
//													if(suc == null){
//														suc = new SolicitudUsuarioCorreo();
//														suc.setConfirmar(false);
//														suc.setCorreo(usuarioSegur.getCorreo());
//														suc.setEvaluar(false);
//														suc.setNombre(usuarioSegur.getNombre());
//														suc.setUsername(usuarioSegur.getAlias());
//														suc = scoringService.persistSolicitudCorreo(suc);
//														if(listaDescripcionUsuariosCorreos != null){
//															if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}else{
//															listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//															listaDescripcionUsuariosCorreos.add(suc);
//														}
//													}else{
//														if(suc.getEvaluar() == false){
//															suc.setConfirmar(false);
//															suc = scoringService.persistSolicitudCorreo(suc);
//															
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}															
//														
//														}else{
//															suc.setEvaluar(false);
//															suc.setConfirmar(false);
//															suc = scoringService.mergerSolicitudCorreo(suc);
//															
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}
//													}
//												}else{
//													if(usuarioSegur.isObligatorio()){
//														SolicitudUsuarioCorreo suc =  getObtenerSolicitudUsuarioCorreo(usuarioSegur.getAlias());
//														if(suc == null){
//															suc = new SolicitudUsuarioCorreo();
//															suc.setConfirmar(false);
//															suc.setCorreo(usuarioSegur.getCorreo());
//															suc.setEvaluar(false);
//															suc.setNombre(usuarioSegur.getNombre());
//															suc.setUsername(usuarioSegur.getAlias());
//															
//															suc = scoringService.persistSolicitudCorreo(suc);
//															
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//															
//														}else{
//															if(suc.getEvaluar() == false){
//																suc.setConfirmar(false);
//																if(listaDescripcionUsuariosCorreos != null){
//																	if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																		listaDescripcionUsuariosCorreos.add(suc);
//																	}
//																}else{
//																	listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//																
//															}else{
//																suc.setEvaluar(false);
//																suc.setConfirmar(false);
//																suc = scoringService.mergerSolicitudCorreo(suc);
//																if(listaDescripcionUsuariosCorreos != null){
//																	if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																		listaDescripcionUsuariosCorreos.add(suc);
//																	}
//																}else{
//																	listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}
//														}
//													}else{
//														SolicitudUsuarioCorreo suc =  getObtenerSolicitudUsuarioCorreo(usuarioSegur.getAlias());
//														if(suc == null){
//															suc = new SolicitudUsuarioCorreo();
//															suc.setConfirmar(true);
//															suc.setCorreo(usuarioSegur.getCorreo());
//															suc.setEvaluar(true);
//															suc.setNombre(usuarioSegur.getNombre());
//															suc.setUsername(usuarioSegur.getAlias());
//															
//															suc = scoringService.persistSolicitudCorreo(suc);
//															if(listaUsuarioCorreos != null){
//																if(!listaUsuarioCorreos.contains(suc)){
//																	listaUsuarioCorreos.add(suc);
//																}
//															}else{
//																listaUsuarioCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaUsuarioCorreos.add(suc);
//															}
//															
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//															
//														}else{
//															if(suc.getEvaluar() == false){
//																suc.setEvaluar(true);
//																suc.setConfirmar(true);
//																if(listaUsuarioCorreos != null){
//																	if(!listaUsuarioCorreos.contains(suc)){
//																		listaUsuarioCorreos.add(suc);
//																	}
//																}else{
//																	listaUsuarioCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaUsuarioCorreos.add(suc);
//																}
//																
//																if(listaDescripcionUsuariosCorreos != null){
//																	if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																		listaDescripcionUsuariosCorreos.add(suc);
//																	}
//																}else{
//																	listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																suc.setEvaluar(true);
//																suc.setConfirmar(true);
//																suc = scoringService.mergerSolicitudCorreo(suc);
//																if(listaUsuarioCorreos != null){
//																	if(!listaUsuarioCorreos.contains(suc)){
//																		listaUsuarioCorreos.add(suc);
//																	}
//																}else{
//																	listaUsuarioCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaUsuarioCorreos.add(suc);
//																}
//																
//																if(listaDescripcionUsuariosCorreos != null){
//																	if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																		listaDescripcionUsuariosCorreos.add(suc);
//																	}
//																}else{
//																	listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}
//														}														
//													}
//												}
//											}else if(paginaIngreso == 21 || paginaIngreso == 22 || paginaIngreso ==23){
//												if(validoNegocio == true){
//													SolicitudUsuarioCorreo suc =  getObtenerSolicitudUsuarioCorreo(usuarioSegur.getAlias());
//													if(suc == null){
//														suc = new SolicitudUsuarioCorreo();
//														suc.setConfirmar(false);
//														suc.setCorreo(usuarioSegur.getCorreo());
//														suc.setEvaluar(false);
//														suc.setNombre(usuarioSegur.getNombre());
//														suc.setUsername(usuarioSegur.getAlias());
//														suc = scoringService.persistSolicitudCorreo(suc);
//														if(listaDescripcionUsuariosCorreos != null){
//															if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}else{
//															listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//															listaDescripcionUsuariosCorreos.add(suc);
//														}
//														
//													}else{
//														if(suc.getEvaluar() == false){
//															suc.setConfirmar(false);
//															suc = scoringService.persistSolicitudCorreo(suc);
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}															
//														}else{
//															suc.setEvaluar(false);
//															suc.setConfirmar(false);
//															suc = scoringService.mergerSolicitudCorreo(suc);
//															
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														
//														}
//													}
//												}else{
//													if(usuarioSegur.isObligatorio()){
//														SolicitudUsuarioCorreo suc =  getObtenerSolicitudUsuarioCorreo(usuarioSegur.getAlias());
//														if(suc == null){
//															suc = new SolicitudUsuarioCorreo();
//															suc.setConfirmar(false);
//															suc.setCorreo(usuarioSegur.getCorreo());
//															suc.setEvaluar(false);
//															suc.setNombre(usuarioSegur.getNombre());
//															suc.setUsername(usuarioSegur.getAlias());
//															
//															suc = scoringService.persistSolicitudCorreo(suc);
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//															
//														}else{
//															if(suc.getEvaluar() == false){
//																suc.setConfirmar(false);
//																if(listaDescripcionUsuariosCorreos != null){
//																	if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																		listaDescripcionUsuariosCorreos.add(suc);
//																	}
//																}else{
//																	listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																suc.setEvaluar(false);
//																suc.setConfirmar(false);
//																suc = scoringService.mergerSolicitudCorreo(suc);
//																if(listaDescripcionUsuariosCorreos != null){
//																	if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																		listaDescripcionUsuariosCorreos.add(suc);
//																	}
//																}else{
//																	listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}
//														}
//													}else{
//														SolicitudUsuarioCorreo suc =  getObtenerSolicitudUsuarioCorreo(usuarioSegur.getAlias());
//														if(suc == null){
//															suc = new SolicitudUsuarioCorreo();
//															suc.setConfirmar(true);
//															suc.setCorreo(usuarioSegur.getCorreo());
//															suc.setEvaluar(true);
//															suc.setNombre(usuarioSegur.getNombre());
//															suc.setUsername(usuarioSegur.getAlias());
//															
//															suc = scoringService.persistSolicitudCorreo(suc);
//															if(listaUsuarioCorreos != null){
//																if(!listaUsuarioCorreos.contains(suc)){
//																	listaUsuarioCorreos.add(suc);
//																}
//															}else{
//																listaUsuarioCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaUsuarioCorreos.add(suc);
//															}
//															
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//															
//														}else{
//															if(suc.getEvaluar() == false){
//																suc.setConfirmar(true);
//																if(listaUsuarioCorreos != null){
//																	if(!listaUsuarioCorreos.contains(suc)){
//																		listaUsuarioCorreos.add(suc);
//																	}
//																}else{
//																	listaUsuarioCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaUsuarioCorreos.add(suc);
//																}
//																
//																if(listaDescripcionUsuariosCorreos != null){
//																	if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																		listaDescripcionUsuariosCorreos.add(suc);
//																	}
//																}else{
//																	listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																suc.setEvaluar(true);
//																suc.setConfirmar(true);
//																suc = scoringService.mergerSolicitudCorreo(suc);
//																if(listaUsuarioCorreos != null){
//																	if(!listaUsuarioCorreos.contains(suc)){
//																		listaUsuarioCorreos.add(suc);
//																	}
//																}else{
//																	listaUsuarioCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaUsuarioCorreos.add(suc);
//																}
//																
//																if(listaDescripcionUsuariosCorreos != null){
//																	if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																		listaDescripcionUsuariosCorreos.add(suc);
//																	}
//																}else{
//																	listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}
//														}														
//													}
//												}
//											}
//										}else{/*es para otras solicitudes */
//											boolean validoCanalVenta = scoringService.obtenerAsignacionCanalVentaDelUsuario(usuarioSegur.getIdPersonal(), canal);
//											if(validoCanalVenta){
//												SolicitudUsuarioCorreo suc =  getObtenerSolicitudUsuarioCorreo(usuarioSegur.getAlias());
//												if(suc == null){
//													suc = new SolicitudUsuarioCorreo();
//													suc.setConfirmar(false);
//													suc.setCorreo(usuarioSegur.getCorreo());
//													suc.setEvaluar(false);
//													suc.setNombre(usuarioSegur.getNombre());
//													suc.setUsername(usuarioSegur.getAlias());
//													suc = scoringService.persistSolicitudCorreo(suc);
//													if(listaDescripcionUsuariosCorreos != null){
//														if(!listaDescripcionUsuariosCorreos.contains(suc)){
//															listaDescripcionUsuariosCorreos.add(suc);
//														}
//													}else{
//														listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//														listaDescripcionUsuariosCorreos.add(suc);
//													}
//												}else{
//													if(suc.getEvaluar() == false){
//														suc.setConfirmar(false);
//														suc = scoringService.persistSolicitudCorreo(suc);
//														if(listaDescripcionUsuariosCorreos != null){
//															if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}else{
//															listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//															listaDescripcionUsuariosCorreos.add(suc);
//														}															
//													}else{
//														suc.setEvaluar(false);
//														suc.setConfirmar(false);
//														suc = scoringService.mergerSolicitudCorreo(suc);
//														
//													
//														if(listaDescripcionUsuariosCorreos != null){
//															if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}else{
//															listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//															listaDescripcionUsuariosCorreos.add(suc);
//														}
//														
//													}
//												}
//											}else{
//												if(usuarioSegur.isObligatorio()){
//													SolicitudUsuarioCorreo suc =  getObtenerSolicitudUsuarioCorreo(usuarioSegur.getAlias());
//													if(suc == null){
//														suc = new SolicitudUsuarioCorreo();
//														suc.setConfirmar(false);
//														suc.setCorreo(usuarioSegur.getCorreo());
//														suc.setEvaluar(false);
//														suc.setNombre(usuarioSegur.getNombre());
//														suc.setUsername(usuarioSegur.getAlias());
//														
//														suc = scoringService.persistSolicitudCorreo(suc);
//														if(listaDescripcionUsuariosCorreos != null){
//															if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}else{
//															listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//															listaDescripcionUsuariosCorreos.add(suc);
//														}
//														
//													}else{
//														if(suc.getEvaluar() == false){
//															suc.setConfirmar(false);
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}else{
//															suc.setEvaluar(false);
//															suc.setConfirmar(false);
//															suc = scoringService.mergerSolicitudCorreo(suc);
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}
//													}
//												}else{
//													SolicitudUsuarioCorreo suc =  getObtenerSolicitudUsuarioCorreo(usuarioSegur.getAlias());
//													if(suc == null){
//														suc = new SolicitudUsuarioCorreo();
//														suc.setConfirmar(true);
//														suc.setCorreo(usuarioSegur.getCorreo());
//														suc.setEvaluar(true);
//														suc.setNombre(usuarioSegur.getNombre());
//														suc.setUsername(usuarioSegur.getAlias());
//														
//														suc = scoringService.persistSolicitudCorreo(suc);
//														if(listaUsuarioCorreos != null){
//															if(!listaUsuarioCorreos.contains(suc)){
//																listaUsuarioCorreos.add(suc);
//															}
//														}else{
//															listaUsuarioCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//															listaUsuarioCorreos.add(suc);
//														}
//														
//														if(listaDescripcionUsuariosCorreos != null){
//															if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}else{
//															listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//															listaDescripcionUsuariosCorreos.add(suc);
//														}
//														
//													}else{
//														if(suc.getEvaluar() == false){
//															suc.setEvaluar(true);
//															suc.setConfirmar(true);
//															if(listaUsuarioCorreos != null){
//																if(!listaUsuarioCorreos.contains(suc)){
//																	listaUsuarioCorreos.add(suc);
//																}
//															}else{
//																listaUsuarioCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaUsuarioCorreos.add(suc);
//															}
//															
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}else{
//															suc.setEvaluar(true);
//															suc.setConfirmar(true);
//															suc = scoringService.mergerSolicitudCorreo(suc);
//															if(listaUsuarioCorreos != null){
//																if(!listaUsuarioCorreos.contains(suc)){
//																	listaUsuarioCorreos.add(suc);
//																}
//															}else{
//																listaUsuarioCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaUsuarioCorreos.add(suc);
//															}
//															
//															if(listaDescripcionUsuariosCorreos != null){
//																if(!listaDescripcionUsuariosCorreos.contains(suc)){
//																	listaDescripcionUsuariosCorreos.add(suc);
//																}
//															}else{
//																listaDescripcionUsuariosCorreos = new ArrayList<SolicitudUsuarioCorreo>(0);
//																listaDescripcionUsuariosCorreos.add(suc);
//															}
//														}
//													}														
//												}
//											}
//										}
//									}
//								}else{
//									if(listaUsuarioSegurDTO != null){
//										if(!listaUsuarioSegurDTO.contains(usuarioSegur)){
//											listaUsuarioSegurDTO.add(usuarioSegur);
//										}
//									}else{
//										listaUsuarioSegurDTO = new ArrayList<UsuarioSegurDTO>(0);
//										listaUsuarioSegurDTO.add(usuarioSegur);
//									}
//								}
//							}
//						}
//					}
//				}catch (Exception e) {
//					log.error("Error al sacar los datos del usaurio #0", e.getMessage());
//				}				
//			}
//			log.debug(listaUsuarioCorreoagregados.size());
//			log.debug(listaDescripcionUsuariosCorreos.size());	
//			agregarCorreosObligatorios();
//		}catch (Exception e) {
//			log.error("Error, sacar los destinatarios : #0", e.getMessage());
//		}
//	}	
	
    private String observacionesModel;
	public void ingresoObservaciones(){
		if(this.solicitud != null && this.tipoSolicitud != null){
			log.debug("solicitud #0 tipo de solicitud #1", solicitud.getId().getNumSolicitud(), tipoSolicitud.getCodTipoSolicitud());
			this.observacionesModel = null;
			consultarObservacion();
		}
	}
	
	public void gnuardarObservaciones(){
		try{
			if(this.observacionesModel == null){
				FacesMessages.instance().add(Severity.ERROR,"Debe ingresar una observación." );
				return;					
			}

			LcredSolicitudObservacionesId id = new LcredSolicitudObservacionesId();
			Long correlativo = scoringService.obtenerCorrelativoObservaciones(solicitud.getId().getNumSolicitud());
			if(correlativo != null){
				id.setCorrelativo(correlativo);
				id.setFecha(new Date());
				id.setHora(new Date());
				id.setNumSolicitud(solicitud.getId().getNumSolicitud());
				id.setTipoSol(tipoSolicitud.getCodTipoSolicitud());
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
					this.listaObservaciones = new ArrayList<LcredSolicitudObservaciones>();
					for(LcredSolicitudObservaciones obs : lista){
						String cadena = obs.getId().getObservacion();
						if(cadena != null && cadena.length() < 170){
							String[] vsplit = cadena.split(" ");
							if(vsplit != null && vsplit.length > 1){
								this.listaObservaciones.add(obs);
							}else if(vsplit != null && vsplit.length == 1){
								StringBuffer cadenaNueva = new StringBuffer();
								Double cant = Double.parseDouble(String.valueOf(cadena.length()))/30;
								int parteEntera = (int)cant.doubleValue();
								Double decimal = cant - parteEntera;
								if(decimal >= 0.5){
									parteEntera++;
								}
								int comienzo = 0;
								int limite = 30;
								for(int i=0; i<parteEntera;i++){
									
									if((i+1)<parteEntera){
										cadenaNueva.append(cadena.substring(comienzo, limite)).append(" ");
									}
									if(i == (parteEntera -1)){
										cadenaNueva.append(cadena.substring(comienzo, cadena.length())).append(" ");
									}
									comienzo = (limite + 1);
									limite = (comienzo - 1) + 30;
								}
								obs.getId().setObservacion(cadenaNueva.toString());
								this.listaObservaciones.add(obs);
							}
						}else{
							StringBuffer cadenaNueva = new StringBuffer();
							String[] cadenaSplit = obs.getId().getObservacion().split(" ");
							for(String frase : cadenaSplit){
								if(frase != null && frase.length() < 30){
									cadenaNueva.append(frase).append(" ");
								}else{
									Double cant = Double.parseDouble(String.valueOf(frase.length()))/30;
									int parteEntera = (int)cant.doubleValue();
									Double decimal = cant - parteEntera;
									if(decimal >= 0.5){
										parteEntera++;
									}
									int comienzo = 0;
									int limite = 30;
									for(int i=0; i<parteEntera;i++){
										
										if((i+1)<parteEntera){
											cadenaNueva.append(frase.substring(comienzo, limite)).append(" ");
										}
										if(i == (parteEntera -1)){
											cadenaNueva.append(frase.substring(comienzo, frase.length())).append(" ");
										}
										comienzo = (limite + 1);
										limite = (comienzo - 1) + 30;
									}
									obs.getId().setObservacion(cadenaNueva.toString());
								}
							}
						  this.listaObservaciones.add(obs);
						}
					}
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
	
	

	/*Archivos Adjunto*/
	/*
	private List<UploadItem> uploadFile = new ArrayList<UploadItem>();
	@BypassInterceptors public List<UploadItem> getUploadFile() {return uploadFile; }
	@BypassInterceptors public void setUploadFile(List<UploadItem> uploadFile) { this.uploadFile = uploadFile; }
	*/
	
	/*
	public void asignarArchivosParaListas() {
		log.debug("Tama�o lista #0", uploadFile.size());
		if(uploadFile.size() > 0){
			guardarArchivo();
			agregarArchivos();
		}
	}
	*/
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
			if(listaFileUploadedDTOs == null ){
				listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
			}else{
				log.debug("cantidad de registro en la lista #0", listaFileUploadedDTOs.size());
			}
			
			FileUploadedDTO file = null;
			for(UploadItem archivo :uploadFile){
				file = new FileUploadedDTO();
				file.setTipo(this.tipoSolicitud);				
				file.setUploadItem(archivo);
				file.setNombreArchivo(archivo.getFileName());
				try{
					if(archivo != null){
						try{
							FileInputStream fin = new FileInputStream(archivo.getFile());
							byte fileContent[] = new byte[(int)archivo.getFileSize()];
							fin.read(fileContent);
							Attachment archivoAbjunto = new Attachment();
							archivoAbjunto.setContentType(archivo.getContentType());
							archivoAbjunto.setData(fileContent);
							archivoAbjunto.setName(archivo.getFileName());
							archivoAbjunto.setSize(archivo.getFileSize());
							entityManager.persist(archivoAbjunto);
							entityManager.flush();
							file.setArchivo(archivoAbjunto);
							file.setEliminar(true);
						} catch (Exception e) {
							FacesMessages.instance().add(Severity.WARN,"A ocurrido un error al momento de cargar el archivo para la solicitud.");
						}
					}
				}catch (Exception e) {
					log.error("Error, al gurdar los archivo temporalmente #0", e.getMessage());
				}
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
			entityManager.remove(archivoAux.getArchivo());
			entityManager.flush();
			listaFileUploadedDTOs.remove(archivoAux);
		}
	}	
	
	public void habilitarComboCondicion(String opcion){
		if("CondicionRiesgo".equals(opcion)){
			if(comboCRiesgo){comboCRiesgo = false;}else{comboCRiesgo=true;}
			setClsRiesgo(null);
		}else if("CondicionPago".equals(opcion)){
			if(comboCPago){ comboCPago = false;}else{comboCPago=true;}
			setcPago(null);
		}
		
		if(comboCRiesgo == false && this.clsRiesgo == null){
			if(listaDestinatarios!= null){
				listaDestinatarios.clear();
			}
		}else if(comboCPago == false && this.cPago == null){
			if(listaDestinatarios!= null){
				listaDestinatarios.clear();
			}			
		}
	}
	
	public void crearVariableClienteDM(){
		LcredSolicitudDmId id = new LcredSolicitudDmId();
		clienteDM = new LcredSolicitudDm();
		id.setRut(this.clienteTarget.getRut());
		id.setNombre(this.clienteTarget.getRazonSocial());
		clienteDM.setId(id);

	}

	public void eliminarClienteDM(LcredSolicitudDm clienteDMDTO){
		if(clienteDMDTO != null){
			listaClienteDMs.remove(clienteDMDTO);
		}
	}
	
	public void limpiarClienteDM(){
		
		clienteDM = null;

	}
	
	public void guardarClienteDMToListaClienteDM(){
		this.mensajeCreacionDm = null;
		if(clienteDM == null){
			FacesMessages.instance().add(Severity.ERROR,"" );
			this.mensajeCreacionDm = "El objeto de cliente DM no esta instanciado.";
			return;
		}
		if(clienteDM.getId().getRut() == null){
			FacesMessages.instance().add(Severity.ERROR,"" );
			this.mensajeCreacionDm = "El rut del cliente no se ingreso antes de guardar.";
			return;
		}		
		if(clienteDM.getId().getNombre() == null){
			FacesMessages.instance().add(Severity.ERROR,"" );
			this.mensajeCreacionDm = "El nombre del cliente no se ingreso antes de guardar.";
			return;
		}		
		if(clienteDM.getId().getOficinaVentas() == null){
			FacesMessages.instance().add(Severity.ERROR,"" );
			this.mensajeCreacionDm = "El oficina del cliente no se ingreso antes de guardar.";
			return;
		}			
		if(clienteDM.getId().getZona() == null){
			FacesMessages.instance().add(Severity.ERROR,"" );
			this.mensajeCreacionDm = "El zona del cliente no se ingreso antes de guardar.";
			return;
		}			
		if(clienteDM.getId().getListaPrecio() == null){
			FacesMessages.instance().add(Severity.ERROR,"" );
			this.mensajeCreacionDm = "Debe ingresar la lista de precio del cliente al guardar.";
			return;
		}
		if(clienteDM.getId().getSector() == null){
			FacesMessages.instance().add(Severity.ERROR,"" );
			this.mensajeCreacionDm = "Debe ingresar el sector del cliente del cliente al guardar.";
			return;
		}
		if(clienteDM.getId().getVendTelefono() == null){
			FacesMessages.instance().add(Severity.ERROR,"" );
			this.mensajeCreacionDm = "Debe ingresar rut o el telefono del cliente al guardar.";
			return;
		}		
		if(clienteDM.getId().getVendTerreno() == null){
			FacesMessages.instance().add(Severity.ERROR,"" );
			this.mensajeCreacionDm = "Debe ingresar rut o el terreno del cliente al guardar.";
			return;
		}		

		if(listaClienteDMs == null){
			listaClienteDMs = new ArrayList<LcredSolicitudDm>(0);
			listaClienteDMs.add(clienteDM);
		}else{
			listaClienteDMs.add(clienteDM);
		}
		limpiarClienteDM();
		
		if(listaUsuarioCorreoagregados == null || listaUsuarioCorreoagregados.size() == 0){
			obtenerCorreoBloqueDesbloqueo();
		}
	}
	
	public void validarRuts(int opcion){
		String rut= null;
		if(opcion == 1){
			if(this.clienteDM.getId().getVendTelefono() != null){
				rut = this.clienteDM.getId().getVendTelefono();
			}
		}else{
			if(this.clienteDM.getId().getVendTerreno() != null){
				rut = this.clienteDM.getId().getVendTerreno();
			}
		}
		
		boolean exito = validarRut(rut, opcion);
		if(opcion ==1){
			if(exito == false){
				this.clienteDM.getId().setVendTelefono(null);
			}
		}else{
			if(exito == false){
				this.clienteDM.getId().setVendTerreno(null);
			}
		}
	}
	
	public boolean validarRut(String rut, int opcion){
		String nombre = null;
		if(opcion == 1){
			nombre = "rut del vendedor ";
		}else {
			nombre = "rut del vendedor en terreno ";
		}
		
		if(rut != null){
			int posicionGuion = rut.indexOf("-");
			if(posicionGuion < 0){
				FacesMessages.instance().add(Severity.ERROR,"El rut del "+ nombre + " ingrsado no tienen el formato establecido (xxxxxxxx-x)." );
				return false;					
			}
			String largoDespuesGuion = rut.substring(posicionGuion+1, rut.length());
			if(largoDespuesGuion != null && largoDespuesGuion.length() > 1){
				FacesMessages.instance().add(Severity.ERROR,"El digito verificador del  " + nombre +" no debe ser mas de dos caracteres." );
				return false;					
			}
			if(globalService.validarRut(rut) == false){
				FacesMessages.instance().add(Severity.ERROR,"El rut del "+ nombre +" ingresado no es valido." );
				return false;					
			}
		}else{
			FacesMessages.instance().add(Severity.ERROR,"No ha ingresado el rut del " + nombre+ "." );
			return false;
			
		}
		return true;
	}
	
	public void cambioEstadoDoctoProrroga(DocumentoProrrogaDTO objeto){
		if(objeto != null){
			log.debug("numero documento #0", objeto.getNumeroDocto());
			log.debug("estado de  #0", objeto.isStatus());
			
		}
		
	}
	
	public void guardarEnLaListaDoctoProrrogaAgregados(){
		List<DocumentoProrrogaDTO> listaFinal = new ArrayList<DocumentoProrrogaDTO>(0);
		if(listaDoctoProrroga != null){
			for(DocumentoProrrogaDTO dto: listaDoctoProrroga){
				if(dto != null && dto.isStatus() == true){
					DocumentoProrrogaDTO obj = revisarListaAgregadoDctoConFechaVencimientoNueva(dto.getNumeroDocto());
					if(obj != null){
						listaFinal.add(obj);
					}else{
						dto.setFechaVencNuevo(null);
						listaFinal.add(dto);
					}
				}
			}
		}
		listaDoctoProrrogaAgregados.clear();
		setListaDoctoProrrogaAgregados(listaFinal);
	}	
	
	public DocumentoProrrogaDTO revisarListaAgregadoDctoConFechaVencimientoNueva(String numero){
		DocumentoProrrogaDTO obj = null;
		for(DocumentoProrrogaDTO dto: listaDoctoProrrogaAgregados){
			if(dto != null && dto.getNumeroDocto().equals(numero)){
				if(dto.getFechaVencNuevo() != null){
					obj = dto;
					break;
				}
			}
		}
		return obj;
	}
	
	public void eliminarDocumentoProrroga(DocumentoProrrogaDTO docto){
		List<DocumentoProrrogaDTO> listaNuevaDocto = new ArrayList<DocumentoProrrogaDTO>(0);
		if(docto != null && solicitud != null){
			LcredSolicitudProrroga lsp = scoringService.getDoctoProrrogaForSolicitud(solicitud.getId().getNumSolicitud() , docto.getNumeroDocto());
			if(lsp != null){
				boolean exito = scoringService.removeLcredSolicitudProrroga(lsp);
				log.debug(exito);
				if(exito){
					listaDoctoProrrogaAgregados.remove(docto);
					for(DocumentoProrrogaDTO dto: listaDoctoProrroga){
						if(dto != null && dto.getNumeroDocto().equals(docto.getNumeroDocto())){
							dto.setStatus(false);
							dto.setFechaVencNuevo(null);
						}
						listaNuevaDocto.add(dto);
					}
					listaDoctoProrroga.clear();
					setListaDoctoProrroga(listaNuevaDocto);					
				}
			
			}

		}
	}
	
	public void verificarFechaVencimientoNuevoMayorFechaActual(DocumentoProrrogaDTO objeto){
		boolean valido = false;
		List<DocumentoProrrogaDTO> listaNuevaDctoAgregadosNuevos = new ArrayList<DocumentoProrrogaDTO>(0);
		Calendar calActual = Calendar.getInstance();
		Calendar calNueva = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		if(objeto != null){
			log.debug("numero documento #0", objeto.getNumeroDocto());
			log.debug("fecha vencimiento actual  de  #0", format.format(objeto.getFechaVencActual()));
			log.debug("fecha vencimiento nuevo  de  #0",format.format(objeto.getFechaVencNuevo()));
			
			calActual.setTime(objeto.getFechaVencActual());
			long actual = calActual.getTimeInMillis();
			
			calNueva.setTime(objeto.getFechaVencNuevo());
			long nuevo = calNueva.getTimeInMillis();
			
			if(actual > nuevo){
				valido = true;
			}
			
			for(DocumentoProrrogaDTO dto: listaDoctoProrrogaAgregados){
				if(dto != null && dto.getNumeroDocto().equals(objeto.getNumeroDocto()) && valido==true){
					objeto.setFechaVencNuevo(null);
					listaNuevaDctoAgregadosNuevos.add(objeto);
					this.mensajeGlogal ="la fecha nueva  de prorroga tiene que ser mayor que la fecha actual.";
				}else{
					listaNuevaDctoAgregadosNuevos.add(dto);
				}
			}
			listaDoctoProrrogaAgregados.clear();
			setListaDoctoProrrogaAgregados(listaNuevaDctoAgregadosNuevos);

		}
		calActual=null;
		calNueva=null;
		
		if(listaUsuarioCorreoagregados == null || listaUsuarioCorreoagregados.size() == 0){
			obtenerCorreoBloqueDesbloqueo();
		}
		
	}
	
	public void validarSolicitud(){
		limpiarTiempoEjecuciones();
		Calendar cal = Calendar.getInstance();
		try{
			log.debug("se abre el puppo");
			// vamos a validar todo lo qie se ingreso al pagina
			
			if(this.paginaIngreso == 11 || this.paginaIngreso == 12){
				if(this.monto == null || this.monto == 0){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el valor de la compra." );
					return;		
				}
//				if(this.paginaIngreso == 11){
//					if(this.pie == null || this.pie == 0){
//						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el valor de la pie." );
//						return;		
//					}	
//				}
				if(this.listaCotPedDTOs == null ){
					FacesMessages.instance().add(Severity.ERROR,"Debe consultar una Cotización o Pedido para agregar los productos relacionado." );
					return;						
				}else if(this.listaCotPedDTOs.size() == 0){
					FacesMessages.instance().add(Severity.ERROR,"Debe consultar una Cotización o Pedido para agregar los productos relacionado." );
					return;						
				}
				if(this.tipoFormaPago == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar el tipo de forma de pago." );
					return;						
				}else{
					if(this.tipoFormaPago.getDescripcion().equals("Efectivo")){
						this.pie = this.monto;
					}
				}
				
				
				if(this.paginaIngreso == 11){
					if(this.encabezado == null){
						FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar el tipo de pago." );
						return;						
					}else{
						String[] array = this.encabezado.getDescripcion().split(",");
						if(array != null){
							int cantidad = array.length;
							boolean exito = false;
							for(String cadena : array){
								if((cadena.trim()).equals("Efectivo")){
									if(cantidad > 1){
										exito = true;
										break;
									}
								}
							}
							if(exito){
								if(this.monto.longValue() < this.pie.longValue() ){
									FacesMessages.instance().add(Severity.ERROR,"El monto del pie debe ser menor al monto de venta." );
									return;									
								}
								if(this.monto.longValue() == this.pie.longValue()){
									FacesMessages.instance().add(Severity.ERROR,"El monto del pie debe ser menor al monto de venta." );
									return;									
								}
								if(this.pie.longValue() == 0){
									FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el pie de la venta." );
									return;									
								}
							
							}
						}
					}
				
				}else if(this.paginaIngreso == 12){
					if(this.encabezado == null){
						FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar el tipo de pago." );
						return;						
					}else{
						String[] array = this.encabezado.getDescripcion().split(",");
						if(array != null){
							boolean exito = false;
							for(String cadena : array){
								if((cadena.trim()).equals("Efectivo")){
									exito = true;
									this.evaluadorCompraVC =  true;
									break;
								}
							}
							
							if(exito){
								if(this.evaluadorCompraVC){
									if(this.pie == 0){
										FacesMessages.instance().add(Severity.WARN,"Debe ingresar un pie en la venta en cuotas.");
										this.mensajeGlogal = "Debe ingresar un pie en la venta en cuotas.";
										if(listaUsuarioCorreoagregados != null){
											listaUsuarioCorreoagregados.clear();
										}
										return;	
									}else if(this.pie == this.monto){
										FacesMessages.instance().add(Severity.WARN,"Debe ingresar un pie y debe ser menor al monto total.");
										this.mensajeGlogal = "Debe ingresar un pie y debe ser menor al monto total.";
										if(listaUsuarioCorreoagregados != null){
											listaUsuarioCorreoagregados.clear();
										}
										return;											
									}else{
										this.mensajeGlogal = null;
									}
								}
							}
						}else{
							FacesMessages.instance().add(Severity.ERROR,"error al seleccionar el tipo de pago de la solicitud.");
							setEncabezado(null);						
						}						
					}
				}

				
				
				
				if(this.margenGlobal == null || this.margenGlobal == 0){
					FacesMessages.instance().add(Severity.ERROR,"El Margen Global  no puede ser 0 ni vacio." );
					return;		
				}	
				
				if(this.motivoCompra == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el motivo de la compra." );
					return;		
				}else{
					if("".equals(this.motivoCompra)){
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el motivo de la compra." );
						return;							
					}
				}
				
//				if(this.listaUsuarioCorreoagregados == null){
//					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
//					return;						
//				}else {
//					if(this.listaUsuarioCorreoagregados.size() == 0){
//						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
//						return;							
//					}
//				}

//				if(this.listaFileUploadedDTOs == null){
//					FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
//					return;						
//				}else {
//					if(this.listaFileUploadedDTOs.size() == 0){
//						FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
//						return;							
//					}
//				}				
				habilitarBotonAgregarSolicitud = false;
			}else if(this.paginaIngreso == 21 || this.paginaIngreso == 22 || this.paginaIngreso == 23 || this.paginaIngreso == 24  ){
				
				if(this.monto == null || this.monto == 0){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el valor de la compra." );
					return;		
				}
				if(this.clsRiesgo == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe selecionar una condición de riesgo." );
					return;		
				}				
				if(this.cPago == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe selecionar una condición de pago." );
					return;		
				}					
				if(this.listaUsuarioCorreoagregados == null){
					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
					return;						
				}else {
					if(this.listaUsuarioCorreoagregados.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
						return;							
					}
				}
				
				if(this.descripcionProyecto == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la descripción del proyecto." );
					return;		
				}else{
					if("".equals(this.descripcionProyecto)){
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la descripción del proyecto." );
						return;							
					}
				}
				
				if(this.montoCredito == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto del proyecto." );
					return;		
				}
				
				if(this.plazoEjecucion == null){
					this.plazoEjecucion =""; 
				}			
				
				if(this.potencialCompra == null || this.potencialCompra == 0){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el potencial de compra del proyecto." );
					return;		
				}

				if(this.conceptoNegocios == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el o los concepto(s) relacionado del proyecto." );
					return;		
				}else{
					if("".equals(this.conceptoNegocios)){
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el o los concepto(s) relacionado del proyecto." );
						return;							
					}
				}
				if(this.rutNombresSocios == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar rut y nombres de los socios." );
					return;		
				}else{
					if("".equals(this.rutNombresSocios)){
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar rut y nombres de los socios." );
						return;							
					}
				}
//				if(this.observacionesCredito == null){
//					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar alguna observación del proyecto." );
//					return;		
//				}else{
//					if("".equals(this.observacionesCredito)){
//						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar alguna observación del proyecto." );
//						return;							
//					}
//				}
				
				if(this.listaFileUploadedDTOs == null){
					FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
					return;						
				}else {
					if(this.listaFileUploadedDTOs.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
						return;							
					}
				}				
				if(this.listaSelConceptoNegocio == null){
					FacesMessages.instance().add(Severity.ERROR,"No existen conceptos en la lista asociada en la solicitud." );
					return;						
				}else {
					if(this.listaSelConceptoNegocio.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen conceptos en la lista asociada en la solicitud." );
						return;							
					}
				}
				if(this.listaSocios == null){
					FacesMessages.instance().add(Severity.ERROR,"No existen socios en la lista asociada en la solicitud." );
					return;						
				}else {
					if(this.listaSocios.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen socios en la lista asociada en la solicitud." );
						return;							
					}
				}				
				
				habilitarBotonAgregarSolicitud = false;		
				
			}else if(this.paginaIngreso == 31){
				
				if(this.condicionRiesgo == false && this.condicionPago == false ){
					FacesMessages.instance().add(Severity.ERROR,"Debe selecionar condición de riesgo o condición de pago." );
					return;		
				}
				
				
				if(this.condicionRiesgo == true){
					if(this.clsRiesgo == null){
						FacesMessages.instance().add(Severity.ERROR,"Debe selecionar una condición de riesgo." );
						return;		
					}				
				}

				if(this.condicionPago == true){
					if(this.cPago == null){
						FacesMessages.instance().add(Severity.ERROR,"Debe selecionar una condición de pago." );
						return;		
					}				
				}
				
				
				if(this.listaUsuarioCorreoagregados == null){
					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
					return;						
				}else {
					if(this.listaUsuarioCorreoagregados.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
						return;							
					}
				}
				if(this.motivoRiesgoPago == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la motivo de la solicitud." );
					return;		
				}else{
					if("".equals(this.motivoRiesgoPago)){
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la motivo de la solicitud." );
						return;							
					}
				}

				if(this.listaFileUploadedDTOs == null){
					FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
					return;						
				}else {
					if(this.listaFileUploadedDTOs.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
						return;							
					}
				}
				
				
				habilitarBotonAgregarSolicitud = false;
				
			}else if(this.paginaIngreso == 41){
				
				if(this.opcionBloqueoDesbloqueo == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar tipo de solicitud (bloqueo o desbloqueo)." );
					return;		
				}else{
					if("".equals(this.opcionBloqueoDesbloqueo)){
						FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar tipo de solicitud (bloqueo o desbloqueo)." );
						return;							
					}
				}
				
//				if(this.motivoBloDesbloqueo == null){
//					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la motivo de bloqueo o desbloqueo de la solicitud." );
//					return;		
//				}else{
//					if("".equals(this.motivoBloDesbloqueo)){
//						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la motivo de bloqueo o desbloqueo de la solicitud." );
//						return;							
//					}
//				}				
				
				if(this.observaciones == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar una observacion de bloqueo o desbloqueo de la solicitud." );
					return;		
				}else{
					if("".equals(this.observaciones)){
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar una observacion de bloqueo o desbloqueo de la solicitud." );
						return;							
					}
				}				
				
				if(this.listaUsuarioCorreoagregados == null){
					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
					return;						
				}else {
					if(this.listaUsuarioCorreoagregados.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
						return;							
					}
				}				

//				if(this.listaFileUploadedDTOs == null){
//					FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
//					return;						
//				}else {
//					if(this.listaFileUploadedDTOs.size() == 0){
//						FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
//						return;							
//					}
//				}	
				
				
				habilitarBotonAgregarSolicitud = false;
				
			}else if(this.paginaIngreso == 43){

				
				if(listaClienteDMs == null ){
					FacesMessages.instance().add(Severity.ERROR,"No existen ningun DM  agregado en la lista." );
					return;						
				}else{
					if(listaClienteDMs.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen ningun DM  agregado en la lista." );
						return;						
						
					}
				}
				
				
				if(this.listaUsuarioCorreoagregados == null){
					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
					return;						
				}else {
					if(this.listaUsuarioCorreoagregados.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
						return;							
					}
				}	


//				if(this.listaFileUploadedDTOs == null){
//					FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
//					return;						
//				}else {
//					if(this.listaFileUploadedDTOs.size() == 0){
//						FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
//						return;							
//					}
//				}	
				
				
				
				
				habilitarBotonAgregarSolicitud = false;
				
			}else if(this.paginaIngreso == 44){
				
				if(this.motivoProrroga == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar un motivo de prorroga de la solicitud." );
					return;		
				}else{
					if("".equals(this.motivoProrroga)){
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar un motivo de prorroga de la solicitud." );
						return;							
					}
				}				
				
				
				if(listaDoctoProrrogaAgregados == null ){
					FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
					return;						
				}else{
					if(listaDoctoProrrogaAgregados.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
						return;						
						
					}
				}
				
				for(DocumentoProrrogaDTO docto : listaDoctoProrrogaAgregados){
					if(docto != null){
						if(docto.isStatus() == true && docto.getFechaVencNuevo() != null){
							this.contadorStatus++;
						}
					}
				}
				
				
				if(contadorStatus == 0){
					FacesMessages.instance().add(Severity.ERROR,"Se asignar la nueva fecha de vencimientos a los registro seleccionado." );
					return;						
				}
				
				if(this.listaUsuarioCorreoagregados == null){
					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
					return;						
				}else {
					if(this.listaUsuarioCorreoagregados.size() == 0){
						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios." );
						return;							
					}
				}				

//				if(this.listaFileUploadedDTOs == null){
//					FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
//					return;						
//				}else {
//					if(this.listaFileUploadedDTOs.size() == 0){
//						FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud." );
//						return;							
//					}
//				}					
				
				habilitarBotonAgregarSolicitud = false;
				
				
			}
			Calendar cal2 = Calendar.getInstance();
			this.setTiempoEjecucionValidar(calcularTiempoDelProceso(cal, cal2));
		}catch (Exception e) {
			log.error("error al abrir la solictud.", e.getMessage());
		}
	}
	
	public void ingresarSolictud(String enviar){
		limpiarTiempoEjecuciones();
		Calendar cal = Calendar.getInstance();	
		List<ConceptoDTO> listaArchivoGuardado = null;
		try{
			  if(solicitud != null){	  
				  /*saca el estado inical de la solicitud*/
				  LcredEstado estadoAux = entityManager.find(LcredEstado.class, "DC");
				  /*opcion para venta normal*/	
				 if(this.paginaIngreso == 11 || this.paginaIngreso == 12){
					if(solicitud != null){
						solicitud.setEstado("DC");
						solicitud.setPeackCredito(new BigDecimal(clienteExpoRiesgoKh.getPeakCredito()));
						solicitud.setMontoAsegurado(new BigDecimal(clienteExpoRiesgoKh.getMontoAsegurado()));
						solicitud.setRiesgoKupfer(new BigDecimal(clienteExpoRiesgoKh.getMontoRiesgoKupfer()));
						solicitud.setMontoCuotas(new BigDecimal(clienteExpoRiesgoKh.getMontoPedidoProceso()));
						solicitud.setMonto(new BigDecimal(this.monto));
						solicitud.setMotivoCompra(this.motivoCompra);

						
						boolean mergerSolicitud = scoringService.mergerSolicitud(solicitud);
						
						if(solicitud != null && mergerSolicitud == true && venta != null){
							venta.setNumSolicitud(solicitud.getId().getNumSolicitud());
							venta.setCodTipoSolicitud(this.tipoSolicitud.getClaveProceso());
							venta.setStatusSolicitud("DC");
							venta.setMontoInicial(solicitud.getMonto());
							DecimalFormat formateador = new DecimalFormat("###0.00");
							venta.setDcMargen(String.valueOf(formateador.format (this.margenGlobal)));
							venta.setDcMotivo(this.motivoCompra);
							venta.setMontoPieInicial(new BigDecimal(this.pie));							
						
							if(this.stfp != null){
								this.stfp.setTipoFormaPago(tipoFormaPago);
								this.stfp.setEncabezado(encabezado);
								boolean exitoStfp = scoringService.mergerSolicitudTipoFormaPago(stfp);
								if(exitoStfp){
									StringBuilder cadAux = new StringBuilder();
									//cadAux.append(this.observaciones);
									//cadAux.append(" ");
									cadAux.append(this.encabezado.getDescripcion());
									venta.setObservacionesInicial(cadAux.toString());
									venta.setDcProducto(generarDescripcionProducto());
									FormaPagoDetalle obj = scoringService.sacarUltimaFormaDePago(this.encabezado);
									if(obj != null){
										venta.setDcPlazo(String.valueOf(obj.getFormaPago().getDias()));
									}else {venta.setDcPlazo("0");}
								}						
							}else{
								this.stfp = new SolicitudTipoFormaPago();
								this.stfp.setIdSolicitud(solicitud.getId().getNumSolicitud());
								this.stfp.setTipoFormaPago(tipoFormaPago);
								this.stfp.setEncabezado(encabezado);
								boolean exitoStfp = scoringService.persistSolicitudTipoFormaPago(this.stfp);
								if(exitoStfp){
									StringBuilder cadAux = new StringBuilder();
									//cadAux.append(this.observaciones);
									//cadAux.append(" ");
									cadAux.append(this.encabezado.getDescripcion());
									venta.setObservacionesInicial(cadAux.toString());
									venta.setDcProducto(generarDescripcionProducto());
									FormaPagoDetalle obj = scoringService.sacarUltimaFormaDePago(this.encabezado);
									if(obj != null){
										venta.setDcPlazo(String.valueOf(obj.getFormaPago().getDias()));
									}else {venta.setDcPlazo("0");}
								}						
							}

							boolean valido = scoringService.persistSolicitudVentaPuntual(venta);
							if(valido == true ){
								listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								FacesMessages.instance().add(Severity.INFO,"Se Actualizo la siguiente Solicitud  N° #0", solicitud.getId().getNumSolicitud());
								this.mensajeGlogal = "Se Actualizo la siguiente Solicitud  N° " + String.valueOf(solicitud.getId().getNumSolicitud());
								if(listaFileUploadedDTOs != null){
									for(FileUploadedDTO archivo :listaFileUploadedDTOs){
									   try{		
										   if(archivo.isEliminar()){
												String nombreArchivoAux = archivo.getNombreArchivo().replace(" ", "_");
												nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
												
												OutputStream ostream = null;
												String url = "archivos"+"/"+ solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+nombreArchivoAux;
												String nombreArchivo = solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+nombreArchivoAux;
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
												archivoAux.setTipo(ArchivoAdjuntoType.INGRESO);
												archivoAux.setUbicacion("");
												archivoAux.setUsuario(usuarioLogueado);
												archivoAux.setFechaCreacion(new Date());
												entityManager.persist(archivoAux);
												entityManager.flush();
												
												entityManager.remove(archivo.getArchivo());
												entityManager.flush();
												
												ConceptoDTO objetoAux = new ConceptoDTO();
												objetoAux.setNombreArchivo(nombreArchivo);
												objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+url);
												listaArchivoGuardado.add(objetoAux);
												objetoAux = null;
											}else{
												ConceptoDTO objetoAux = new ConceptoDTO();
												objetoAux.setNombreArchivo(archivo.getNombreArchivo());
												objetoAux.setRutaCompleta(archivo.getNombreArchivo());
												listaArchivoGuardado.add(objetoAux);
												objetoAux = null;												
											}
											
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
					
								
									
								List<ConceptoDTO> lista = scoringService.getConceptoMonto(solicitud.getId().getNumSolicitud(), this.monto);
								List<ConceptoDTO> listaProducto = scoringService.getProductos(solicitud.getId().getNumSolicitud());
								
								if("Enviar".equals(enviar) && listaUsuarioCorreoagregados != null){
									Locale locale = new Locale("es","CL");
									//EmailAlertaContactoMensajeHelper emailAlerta = null;
									/*setando los varlores del dto para el correo*/
									ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
									contenidoEmail.setTituloRespuesta("Solicitud corregida ");
									contenidoEmail.setSolicitud(solicitud);
									NumberFormat numberFormatter;
									StringBuffer rutNombre = new StringBuffer();
									rutNombre.append(clienteTarget.getRut());
									rutNombre.append(" / ");
									rutNombre.append(modificarTexto(clienteTarget.getRazonSocial()));
									contenidoEmail.setRutNombre(rutNombre.toString());
									contenidoEmail.setSoloNombre(clienteTarget.getRazonSocial());
									contenidoEmail.setCanalVenta(clienteTarget.getCanalVenta());
									contenidoEmail.setSucursal(sucursal.getDescripcion());
									contenidoEmail.setEmisor(modificarTexto(usuarioLogueado.getNombre()));
									contenidoEmail.setTipoSolicitud("Venta Puntual");
									contenidoEmail.setTipoVentas(modificarTexto(tipoSolicitud.getDesTipoSolicitud()));
									
									numberFormatter = NumberFormat.getNumberInstance(locale);
									log.debug(numberFormatter.format(this.monto));
									
									contenidoEmail.setMontoMasIva(numberFormatter.format(this.monto));
									contenidoEmail.setMontoPie(numberFormatter.format(this.pie));
									contenidoEmail.setMargenNegocio(String.valueOf(formateador.format (this.margenGlobal)));
									contenidoEmail.setFormaPago(encabezado.getDescripcion());
									contenidoEmail.setMotivoCompra(modificarTexto(this.motivoCompra));
									
									contenidoEmail.setPeakCredito(numberFormatter.format(clienteExpoRiesgoKh.getPeakCredito()));
									if("VIG".equals(clienteTarget.getVigenciaSeguro().trim())){
										contenidoEmail.setLineaSeguro(numberFormatter.format(clienteExpoRiesgoKh.getMontoAsegurado()));
									}else{
										contenidoEmail.setLineaSeguro(numberFormatter.format(0));
									}								
									//contenidoEmail.setLineaSeguro(numberFormatter.format(clienteTarget.getMontoSeguro()));
									contenidoEmail.setRisgoKupfer(numberFormatter.format(clienteExpoRiesgoKh.getMontoRiesgoKupfer()));
									contenidoEmail.setListaProductos(listaProducto);
									contenidoEmail.setListaConceptoMontos(lista);
									contenidoEmail.setListaArchivos(listaArchivoGuardado);
									
									List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
									if(listaUsuarioCorreoagregados != null){
										for(SolicitudUsuarioCorreo ucdto : listaUsuarioCorreoagregados){
											if(ucdto != null && ucdto.getUsername() != null && ucdto.getCorreo() != null && !ucdto.getUsername().equals(usuarioLogueado.getAlias().trim())){
												log.debug("ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1", ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												Usuariosegur  usuarioAux =  scoringService.getUsuarioUsuarioSegurForUsername(ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												if(usuarioAux != null){
													if(!ultimaListaCorreoPrueba.contains(usuarioAux)){
														ultimaListaCorreoPrueba.add(usuarioAux);	
														usuarioAux = new Usuariosegur();
													}													
												}
											}
										}
									}

									Usuariosegur administradorCorreo =scoringService.getUsuarioUsuarioSegurForUsername("ADMINISTRADOR", "sck@kupfer.cl");
									if(administradorCorreo != null){
										ultimaListaCorreoPrueba.add(administradorCorreo);
										administradorCorreo = null;
									}
									
									//insertar el emisor de la solicitud. 
									Usuariosegur usuarioEmisor = scoringService.getUsuarioUsuarioSegurForUsername(usuarioLogueado.getAlias(), usuarioLogueado.getCorreo());
									if(usuarioEmisor != null){
										if(ultimaListaCorreoPrueba != null && ultimaListaCorreoPrueba.size() >  0){
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}else{
											ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}
										contenidoEmail.setCorreoEmisor(usuarioEmisor.getAlias());
									}
									
									contenidoEmail.setListaNuevosCorreos(ultimaListaCorreoPrueba);
									try{
										if(emailAlerta != null){
											emailAlerta.enviarSolicitud(contenidoEmail);
										}	
									}catch (Exception e) {
										log.debug("error por que email de alerta esta nulo #0"+ e.getMessage());
									}
								
								}
								/*se setea el numero de solicitud creada*/
								setSolicitud(solicitud);
							}
						}
					}
				/*venta a credito*/	
				}else if(this.paginaIngreso == 21 || this.paginaIngreso == 22 || this.paginaIngreso == 23 || this.paginaIngreso == 24  ){
					if(solicitud != null){
						solicitud.setPeackCredito(new BigDecimal(0));
						solicitud.setMontoAsegurado(new BigDecimal(0));
						solicitud.setRiesgoKupfer(new BigDecimal(0));
						solicitud.setEstado("DC");
	
						solicitud.setMonto(new BigDecimal(this.monto));
						solicitud.setDesTiposol(this.tipoSolicitud.getDesTipoSolicitud());
	
						if(this.paginaIngreso == 21){
							StringBuffer cadena = new StringBuffer();
							cadena.append(this.tipoSolicitud.getClaveProceso());
							cadena.append("I");
							solicitud.setTipTiposol(cadena.toString());
							solicitud.setSubTiposol("Linea Credito " +this.tipoSolicitud.getDesTipoSolicitud());	
							
						}else if(this.paginaIngreso == 22){
							StringBuffer cadena = new StringBuffer();
							cadena.append(this.tipoSolicitud.getClaveProceso());
							cadena.append("N");
							solicitud.setTipTiposol(cadena.toString());
							solicitud.setSubTiposol("Linea Credito "+this.tipoSolicitud.getDesTipoSolicitud());	
						}else if(this.paginaIngreso == 23){
							StringBuffer cadena = new StringBuffer();
							cadena.append(this.tipoSolicitud.getClaveProceso());
							cadena.append("R");
							solicitud.setTipTiposol(cadena.toString());
							solicitud.setSubTiposol("Linea Credito "+this.tipoSolicitud.getDesTipoSolicitud());	
						}else if(this.paginaIngreso == 24){
							StringBuffer cadena = new StringBuffer();
							cadena.append(this.tipoSolicitud.getClaveProceso());
							cadena.append("A");
							solicitud.setTipTiposol(cadena.toString());
							solicitud.setSubTiposol("Linea Credito " + this.tipoSolicitud.getDesTipoSolicitud());
						}
						boolean exito = scoringService.mergerSolicitud(solicitud);
						
						if(exito == true && credito != null){
							Locale locale = new Locale("es","CL");
							NumberFormat numberFormatter;
							numberFormatter = NumberFormat.getNumberInstance(locale);
							

							credito.setStatusSolicitud("DC");
							credito.setMontoNormalInicial(new BigDecimal(this.monto));
							String[] arrayRiesgo = this.clsRiesgo.split("-");
							if(arrayRiesgo != null){
								credito.setCondRiesgoInicial(arrayRiesgo[0].toString());
							}
							
							String[] arrayPago = this.cPago.split("-");
							if(arrayPago != null){
								credito.setCondPagoInicial(arrayPago[0].toString());
							}
							if(this.observacionesCredito != null){
								credito.setObservacionesInicial(this.observacionesCredito);
							}else{
								credito.setObservacionesInicial("");
							}
							
							credito.setDpDescripcionProyecto(this.descripcionProyecto);
							credito.setDpMonto(numberFormatter.format(this.montoCredito));
							credito.setDpPlazoEjecucion(this.plazoEjecucion);
							credito.setDpPotencialCompra(numberFormatter.format(this.potencialCompra));
							credito.setDpConceptosInvoluc(this.conceptoNegocios);
							credito.setDpSocios(this.rutNombresSocios);
							credito.setMontoNormalFinal(null);
							
	
							boolean valido = scoringService.mergerSolicitudLineaCredito(credito);
							
							if(valido == true ){
								listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								FacesMessages.instance().add(Severity.INFO,"Se Actualizo la siguiente Solicitud  N° #0", solicitud.getId().getNumSolicitud());
								this.mensajeGlogal = "Se Actualizo la siguiente Solicitud  N° " + String.valueOf(solicitud.getId().getNumSolicitud());
								
								if(listaFileUploadedDTOs != null){
									for(FileUploadedDTO archivo :listaFileUploadedDTOs){
									   try {	
										   if(archivo.isEliminar()){
												String nombreArchivoAux = archivo.getNombreArchivo().replace(" ", "_");
												nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
												
												OutputStream ostream = null;
												String url = "archivosLC"+"/"+ solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
												String nombreArchivo = solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
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
												archivoAux.setTipo(ArchivoAdjuntoType.INGRESO);
												archivoAux.setUbicacion("");
												archivoAux.setUsuario(usuarioLogueado);
												archivoAux.setFechaCreacion(new Date());
												entityManager.persist(archivoAux);
												entityManager.flush();
												
												entityManager.remove(archivo.getArchivo());
												entityManager.flush();
												
												ConceptoDTO objetoAux = new ConceptoDTO();
												objetoAux.setNombreArchivo(nombreArchivo);
												objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+url);
												objetoAux.setTipoArchivoType(ArchivoAdjuntoType.INGRESO);
												objetoAux.setFechaCreacion(archivoAux.getFechaCreacion());
												listaArchivoGuardado.add(objetoAux);
												objetoAux = null;
												
											}									   
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
							
								SolicitudConceptosNegocioLC scn = null;
								/*crear lista de concepto de involucrados*/
								List<ConceptoDTO> conceptosInvolucrado = new ArrayList<ConceptoDTO>(0);
								if(this.listaSelConceptoNegocio != null){
									for(ConceptoNegocioDTO cn : listaSelConceptoNegocio){
										scn = new SolicitudConceptosNegocioLC();
										scn.setSolicitud(solicitud);
										scn.setConceptosNegocio(cn.getConceptoNegocio());
										scn.setMonto(cn.getMonto());
										boolean guardado = false;
										try{
											if(scoringService.getSolicitudConceptosNegocioLCEvaluar(solicitud.getId().getNumSolicitud(),scn.getConceptosNegocio().getJerarquia()) == false){
												guardado = scoringService.persistSolicitudConceptosNegocioLC(scn);
											}else{
												guardado = true;
											}
										}catch (Exception e) {
											log.error("error al guardar los datos Solicitud Conceptos Negocio LC #0", e.getMessage());
											guardado = false;
										}
										if(guardado){
											ConceptoDTO ci = new ConceptoDTO();
											ci.setDescripcion(cn.getConceptoNegocio().getDescripcion());
											ci.setMontoFormateado(numberFormatter.format(cn.getMonto()));
											conceptosInvolucrado.add(ci);
											ci=null;
											scn = null;
										}
									}
								}
								
								if("Enviar".equals(enviar) && listaUsuarioCorreoagregados != null){
									
									//EmailAlertaContactoMensajeHelper emailAlerta = null;
									/*setando los varlores del dto para el correo*/
									ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
									contenidoEmail.setTituloRespuesta("Solicitud corregida ");
									contenidoEmail.setSolicitud(solicitud);
									
									StringBuffer rutNombre = new StringBuffer();
									rutNombre.append(clienteTarget.getRut());
									rutNombre.append(" / ");
									rutNombre.append(modificarTexto(clienteTarget.getRazonSocial()));
									contenidoEmail.setRutNombre(rutNombre.toString());
									contenidoEmail.setSoloNombre(clienteTarget.getRazonSocial());
									contenidoEmail.setCanalVenta(clienteTarget.getCanalVenta());
									contenidoEmail.setSucursal(sucursal.getDescripcion());
									contenidoEmail.setEmisor(modificarTexto(usuarioLogueado.getNombre()));
									contenidoEmail.setTipoSolicitud("Linea de Crédito.");
									contenidoEmail.setTipoLineaCredito(tipoSolicitud.getDesTipoSolicitud());
									log.debug(numberFormatter.format(this.monto));
	
									if("VIG".equals(clienteTarget.getVigenciaSeguro().trim())){
										contenidoEmail.setLineaSeguroActual(numberFormatter.format(clienteExpoRiesgoKh.getMontoAsegurado()));
									}else{
										contenidoEmail.setLineaSeguroActual(numberFormatter.format(0));
									}
									
									//contenidoEmail.setLineaSeguroActual(numberFormatter.format(clienteTarget.getMontoSeguro()));
									contenidoEmail.setMontoSolicitado(numberFormatter.format(this.monto));
									contenidoEmail.setCondicionRiesgo(this.clsRiesgo);
									contenidoEmail.setCondicionPago(this.cPago);
									
									contenidoEmail.setProyecto(modificarTexto(credito.getDpDescripcionProyecto()));
									contenidoEmail.setMontoProyecto(credito.getDpMonto());
									contenidoEmail.setPlazoEjecucion(modificarTexto(credito.getDpPlazoEjecucion()));
									contenidoEmail.setPotencialCompra(credito.getDpPotencialCompra());
									
								    /*datos de lineas credito solCredito */
									contenidoEmail.setListaSocios(listaSocios);
									contenidoEmail.setListaConceptoMontos(conceptosInvolucrado);
									contenidoEmail.setListaArchivos(listaArchivoGuardado);
									
									
									List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
									if(listaUsuarioCorreoagregados != null){
										for(SolicitudUsuarioCorreo ucdto : listaUsuarioCorreoagregados){
											if(ucdto != null && ucdto.getUsername() != null && ucdto.getCorreo() != null && !ucdto.getUsername().equals(usuarioLogueado.getAlias().trim())){
												log.debug("ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1", ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												Usuariosegur  usuarioAux =  scoringService.getUsuarioUsuarioSegurForUsername(ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												if(usuarioAux != null){
													if(!ultimaListaCorreoPrueba.contains(usuarioAux)){
														ultimaListaCorreoPrueba.add(usuarioAux);	
														usuarioAux = new Usuariosegur();
													}													
												}
											}
										}
									}
									
									Usuariosegur administradorCorreo =scoringService.getUsuarioUsuarioSegurForUsername("ADMINISTRADOR", "sck@kupfer.cl");
									if(administradorCorreo != null){
										ultimaListaCorreoPrueba.add(administradorCorreo);
										administradorCorreo = null;
									}
									
									//insertar el emisor de la solicitud. 
									Usuariosegur usuarioEmisor = scoringService.getUsuarioUsuarioSegurForUsername(usuarioLogueado.getAlias(), usuarioLogueado.getCorreo());
									if(usuarioEmisor != null){
										if(ultimaListaCorreoPrueba != null && ultimaListaCorreoPrueba.size() >  0){
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}else{
											ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}
										contenidoEmail.setCorreoEmisor(usuarioEmisor.getAlias());
									}

									contenidoEmail.setListaNuevosCorreos(ultimaListaCorreoPrueba);
									
									try{
										if(emailAlerta != null){
											emailAlerta.enviarSolicitudLineaCredito(contenidoEmail);
										}	
									}catch (Exception e) {
										log.debug("error por que email de alerta esta nulo #0"+ e.getMessage());
									}
	
								}
								/*se setea el numero de solicitud creada*/
								setSolicitud(solicitud);
							}
						}
					}
				 /*condiciones*/	
				}else if(this.paginaIngreso == 31){
					if(solicitud != null){
						solicitud.setPeackCredito(new BigDecimal(0));
						solicitud.setMontoAsegurado(new BigDecimal(0));
						solicitud.setRiesgoKupfer(new BigDecimal(0));
						solicitud.setMonto(new BigDecimal(0));
						solicitud.setDesTiposol("Condiciones "+ this.tipoSolicitud.getDesTipoSolicitud());
						solicitud.setEstado("DC");
						
						if(this.paginaIngreso == 31){
							StringBuffer cadena = new StringBuffer();
							cadena.append(this.tipoSolicitud.getClaveProceso());
							if(this.condicionRiesgo == true && this.condicionPago == false){
								solicitud.setSubTiposol("Condiciones de Riesgo");	
								cadena.append("R");
								
							}else if(this.condicionRiesgo == false && this.condicionPago == true){
								solicitud.setSubTiposol("Condiciones de Pago");
								cadena.append("P");
							}else if(this.condicionRiesgo == true && this.condicionPago == true){
								solicitud.setSubTiposol("Condiciones "+ this.tipoSolicitud.getDesTipoSolicitud());
								cadena.append("R");
								cadena.append("P");
							}
							solicitud.setTipTiposol(cadena.toString());	
						}
						
						
						boolean exito = scoringService.mergerSolicitud(solicitud);
						if(exito == true && condicion != null){
							condicion.setStatusSolicitud("DC");	
						
							if(this.condicionRiesgo == true && this.clsRiesgo != null){
								String[] arrayRiesgo = this.clsRiesgo.split("-");
								if(arrayRiesgo != null){
									condicion.setCodCondRiesgoInicial(arrayRiesgo[0].toString());

								}
							}
							
							if(this.condicionPago == true && this.cPago != null){
								String[] arrayPago = this.cPago.split("-");
								if(arrayPago != null){
									condicion.setCodCondPagoInicial(arrayPago[0].toString());
								}
							}
							condicion.setMotivoCambio(this.motivoRiesgoPago);
	
	
							boolean valido = scoringService.mergerSolicitudCondiciones(condicion);
							if(valido == true ){
								listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								FacesMessages.instance().add(Severity.INFO,"Se Actualizo la siguiente Solicitud  N° #0", solicitud.getId().getNumSolicitud());
								this.mensajeGlogal = "Se Actualizo la siguiente Solicitud  N° " + String.valueOf(solicitud.getId().getNumSolicitud());								if(listaFileUploadedDTOs != null){
									for(FileUploadedDTO archivo :listaFileUploadedDTOs){
									   try {	
										   
										   if(archivo.isEliminar()){
												String nombreArchivoAux = archivo.getNombreArchivo().replace(" ", "_");
												nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
												
												OutputStream ostream = null;
												String url = "archivosCC"+"/"+ solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
												String nombreArchivo = solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
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
												archivoAux.setTipo(ArchivoAdjuntoType.INGRESO);
												archivoAux.setUbicacion("");
												archivoAux.setUsuario(usuarioLogueado);
												archivoAux.setFechaCreacion(new Date());
												entityManager.persist(archivoAux);
												entityManager.flush();
												
												entityManager.remove(archivo.getArchivo());
												entityManager.flush();
												
												ConceptoDTO objetoAux = new ConceptoDTO();
												objetoAux.setNombreArchivo(nombreArchivo);
												objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+url);
												objetoAux.setTipoArchivoType(ArchivoAdjuntoType.INGRESO);
												objetoAux.setFechaCreacion(archivoAux.getFechaCreacion());
												listaArchivoGuardado.add(objetoAux);
												objetoAux = null;
												
											}	
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
								
								if("Enviar".equals(enviar) && listaUsuarioCorreoagregados != null){
									
									//EmailAlertaContactoMensajeHelper emailAlerta = null;
									/*setando los varlores del dto para el correo*/
									ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
									contenidoEmail.setTituloRespuesta("Solicitud corregida ");
									contenidoEmail.setSolicitud(solicitud);
									
									StringBuffer rutNombre = new StringBuffer();
									rutNombre.append(clienteTarget.getRut());
									rutNombre.append(" / ");
									rutNombre.append(modificarTexto(clienteTarget.getRazonSocial()));
									contenidoEmail.setRutNombre(rutNombre.toString());
									contenidoEmail.setSoloNombre(clienteTarget.getRazonSocial());
									contenidoEmail.setCanalVenta(clienteTarget.getCanalVenta());
									contenidoEmail.setSucursal(sucursal.getDescripcion());
									contenidoEmail.setEmisor(modificarTexto(usuarioLogueado.getNombre()));
									
									contenidoEmail.setTipoSolicitud("Cambio de Condiciones.");
									
									
									
									if(this.condicionRiesgo == true && this.condicionPago == false){
										contenidoEmail.setTipoLineaCredito("Condiciones de Riesgo.");	
										contenidoEmail.setCondicionRiesgo(clienteTarget.getClasificacionRiesgo());
										contenidoEmail.setCondicionPago("");
										contenidoEmail.setCondicionRiesgoNuevo(this.clsRiesgo);								
										contenidoEmail.setCondicionPagoNuevo("");
									
									}else if(this.condicionRiesgo == false && this.condicionPago == true){
										contenidoEmail.setTipoLineaCredito("Condiciones de Pago.");
										contenidoEmail.setCondicionRiesgo("");
										contenidoEmail.setCondicionPago(clienteTarget.getCondicionPago());
										contenidoEmail.setCondicionRiesgoNuevo("");								
										contenidoEmail.setCondicionPagoNuevo(this.cPago);									
									
									}else if(this.condicionRiesgo == true && this.condicionPago == true){
										contenidoEmail.setTipoLineaCredito("Condiciones "+ this.tipoSolicitud.getDesTipoSolicitud());
										contenidoEmail.setCondicionRiesgo(clienteTarget.getClasificacionRiesgo());
										contenidoEmail.setCondicionPago(clienteTarget.getCondicionPago());
										contenidoEmail.setCondicionRiesgoNuevo(this.clsRiesgo);								
										contenidoEmail.setCondicionPagoNuevo(this.cPago);
									}
									
									contenidoEmail.setMotivoCambio(modificarTexto(this.motivoRiesgoPago));
	
									
									
								    /*datos de lineas credito solCredito */
									contenidoEmail.setListaArchivos(listaArchivoGuardado);
									
									List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
									if(listaUsuarioCorreoagregados != null){
										for(SolicitudUsuarioCorreo ucdto : listaUsuarioCorreoagregados){
											if(ucdto != null && ucdto.getUsername() != null && ucdto.getCorreo() != null && !ucdto.getUsername().equals(usuarioLogueado.getAlias().trim())){
												log.debug("ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1", ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												Usuariosegur  usuarioAux =  scoringService.getUsuarioUsuarioSegurForUsername(ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												if(usuarioAux != null){
													if(!ultimaListaCorreoPrueba.contains(usuarioAux)){
														ultimaListaCorreoPrueba.add(usuarioAux);	
														usuarioAux = new Usuariosegur();
													}													
												}
											}
										}
									}

									Usuariosegur administradorCorreo =scoringService.getUsuarioUsuarioSegurForUsername("ADMINISTRADOR", "sck@kupfer.cl");
									if(administradorCorreo != null){
										ultimaListaCorreoPrueba.add(administradorCorreo);
										administradorCorreo = null;
									}
//									
									//insertar el emisor de la solicitud. 
									Usuariosegur usuarioEmisor = scoringService.getUsuarioUsuarioSegurForUsername(usuarioLogueado.getAlias(), usuarioLogueado.getCorreo());
									if(usuarioEmisor != null){
										if(ultimaListaCorreoPrueba != null && ultimaListaCorreoPrueba.size() >  0){
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}else{
											ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}
										contenidoEmail.setCorreoEmisor(usuarioEmisor.getAlias());
									}

									contenidoEmail.setListaNuevosCorreos(ultimaListaCorreoPrueba);									
									try{
										if(emailAlerta != null){
											emailAlerta.enviarSolicitudCondiciones(contenidoEmail);
										}	
									}catch (Exception e) {
										log.debug("error por que email de alerta esta nulo #0"+ e.getMessage());
									}	
								}
								/*se setea el numero de solicitud creada*/
								setSolicitud(solicitud);
							}
						}
					}				
					
				}else if(this.paginaIngreso == 41){
					
					if(solicitud != null){
						solicitud.setPeackCredito(new BigDecimal(0));
						solicitud.setMontoAsegurado(new BigDecimal(0));
						solicitud.setRiesgoKupfer(new BigDecimal(0));
						solicitud.setMonto(new BigDecimal(0));
						solicitud.setDesTiposol(this.tipoSolicitud.getDesTipoSolicitud());
						solicitud.setEstado("DC");
						if("41".equals(this.opcionBloqueoDesbloqueo)){
							StringBuffer cadena = new StringBuffer();
							cadena.append(this.tipoSolicitud.getClaveProceso());
							cadena.append("B");
							solicitud.setTipTiposol(cadena.toString());
							solicitud.setSubTiposol(this.tipoSolicitud.getDesTipoSolicitud());					
						}else if("42".equals(this.opcionBloqueoDesbloqueo)){
							StringBuffer cadena = new StringBuffer();
							cadena.append(this.tipoSolicitud.getClaveProceso());
							cadena.append("D");
							solicitud.setTipTiposol(cadena.toString());
							solicitud.setSubTiposol(this.tipoSolicitud.getDesTipoSolicitud());						
						}
						
						
						boolean exito  = scoringService.mergerSolicitud(solicitud);
						if(exito == true && solicitudOtra != null && bloqueo != null){
							

							bloqueo.setMotivoBloqDesbloq(this.observaciones);
							boolean valido = scoringService.mergerSolicitudBloqueoToDesbloqueo(bloqueo);
	
							solicitudOtra.setStatusSolicitud("DC");
							if("41".equals(this.opcionBloqueoDesbloqueo)){
								solicitudOtra.setOpcionInicial("B");
					
							}else if("42".equals(this.opcionBloqueoDesbloqueo)){
								solicitudOtra.setOpcionInicial("D");
							}						
							
							solicitudOtra.setObservacionesInicial(this.observaciones);
							boolean validoOtra = scoringService.mergerSolicitudOtrasSolicitudes(solicitudOtra);
							
							if(valido == true && validoOtra == true){
								listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								FacesMessages.instance().add(Severity.INFO,"Se Actualizo la siguiente Solicitud  N° #0", solicitud.getId().getNumSolicitud());
								this.mensajeGlogal = "Se Actualizo la siguiente Solicitud  N° " + String.valueOf(solicitud.getId().getNumSolicitud());								if(listaFileUploadedDTOs != null){
								if(listaFileUploadedDTOs != null){
									for(FileUploadedDTO archivo :listaFileUploadedDTOs){
									   try {	

										   if(archivo.isEliminar()){
												String nombreArchivoAux = archivo.getNombreArchivo().replace(" ", "_");
												nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
												
												OutputStream ostream = null;
												String url = "archivosOS"+"/"+ solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
												String nombreArchivo = solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
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
												archivoAux.setTipo(ArchivoAdjuntoType.INGRESO);
												archivoAux.setUbicacion("");
												archivoAux.setUsuario(usuarioLogueado);
												archivoAux.setFechaCreacion(new Date());
												entityManager.persist(archivoAux);
												entityManager.flush();
												
												entityManager.remove(archivo.getArchivo());
												entityManager.flush();
												
												ConceptoDTO objetoAux = new ConceptoDTO();
												objetoAux.setNombreArchivo(nombreArchivo);
												objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+url);
												objetoAux.setTipoArchivoType(ArchivoAdjuntoType.INGRESO);
												objetoAux.setFechaCreacion(archivoAux.getFechaCreacion());
												listaArchivoGuardado.add(objetoAux);
												objetoAux = null;
												
											}											   
											
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
								
								if("Enviar".equals(enviar) && listaUsuarioCorreoagregados != null){
									
									//EmailAlertaContactoMensajeHelper emailAlerta = null;
									/*setando los varlores del dto para el correo*/
									ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
									contenidoEmail.setTituloRespuesta("Solicitud corregida ");
									contenidoEmail.setSolicitud(solicitud);
									
									StringBuffer rutNombre = new StringBuffer();
									rutNombre.append(clienteTarget.getRut());
									rutNombre.append(" / ");
									rutNombre.append(modificarTexto(clienteTarget.getRazonSocial()));
									contenidoEmail.setRutNombre(rutNombre.toString());
									contenidoEmail.setSoloNombre(clienteTarget.getRazonSocial());
									contenidoEmail.setCanalVenta(clienteTarget.getCanalVenta());
									contenidoEmail.setSucursal(sucursal.getDescripcion());
									contenidoEmail.setEmisor(modificarTexto(usuarioLogueado.getNombre()));
									
									contenidoEmail.setTipoSolicitud("Bloqueo / Desbloqueo.");
									
									if("41".equals(this.opcionBloqueoDesbloqueo)){
										contenidoEmail.setTipoBloqueoDesbloqueo("Bloqueo.");
				
									}else if("42".equals(this.opcionBloqueoDesbloqueo)){
										contenidoEmail.setTipoBloqueoDesbloqueo("Desbloqueo.");
									}
									
									contenidoEmail.setMotivoBloqueoDesbloqueo(modificarTexto(this.motivoBloDesbloqueo));
									contenidoEmail.setObservacionesBloqueoDesblorqueo(modificarTexto(this.motivoBloDesbloqueo));
									
								    /*datos de lineas credito solCredito */
									contenidoEmail.setListaArchivos(listaArchivoGuardado);

									List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
									if(listaUsuarioCorreoagregados != null){
										for(SolicitudUsuarioCorreo ucdto : listaUsuarioCorreoagregados){
											if(ucdto != null && ucdto.getUsername() != null && ucdto.getCorreo() != null && !ucdto.getUsername().equals(usuarioLogueado.getAlias().trim())){
												log.debug("ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1", ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												Usuariosegur  usuarioAux =  scoringService.getUsuarioUsuarioSegurForUsername(ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												if(usuarioAux != null){
													if(!ultimaListaCorreoPrueba.contains(usuarioAux)){
														ultimaListaCorreoPrueba.add(usuarioAux);	
														usuarioAux = new Usuariosegur();
													}													
												}
											}
										}
									}

									Usuariosegur administradorCorreo =scoringService.getUsuarioUsuarioSegurForUsername("ADMINISTRADOR", "sck@kupfer.cl");
									if(administradorCorreo != null){
										ultimaListaCorreoPrueba.add(administradorCorreo);
										administradorCorreo = null;
									}
									
									//insertar el emisor de la solicitud. 
									Usuariosegur usuarioEmisor = scoringService.getUsuarioUsuarioSegurForUsername(usuarioLogueado.getAlias(), usuarioLogueado.getCorreo());
									if(usuarioEmisor != null){
										if(ultimaListaCorreoPrueba != null && ultimaListaCorreoPrueba.size() >  0){
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}else{
											ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}
										contenidoEmail.setCorreoEmisor(usuarioEmisor.getAlias());
									}

									contenidoEmail.setListaNuevosCorreos(ultimaListaCorreoPrueba);									
									try{
										if(emailAlerta != null){
											emailAlerta.enviarSolicitudBloqueoDesbloqueo(contenidoEmail);
										}	
									}catch (Exception e) {
										log.debug("error por que email de alerta esta nulo #0"+ e.getMessage());
									}	
	
								}
								/*se setea el numero de solicitud creada*/
								setSolicitud(solicitud);
							}
						}
					}	
				}		
			}else if(this.paginaIngreso == 43){
					
					if(solicitud != null){
						solicitud.setPeackCredito(new BigDecimal(0));
						solicitud.setMontoAsegurado(new BigDecimal(0));
						solicitud.setRiesgoKupfer(new BigDecimal(0));
						solicitud.setMonto(new BigDecimal(0));
						solicitud.setDesTiposol(this.tipoSolicitud.getDesTipoSolicitud());
						solicitud.setEstado("DC");
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						cadena.append("DM");
						solicitud.setTipTiposol(cadena.toString());
						solicitud.setSubTiposol(this.tipoSolicitud.getDesTipoSolicitud());					
						
						
						
						boolean exito = scoringService.mergerSolicitud(solicitud);
						if(exito == true  && solicitudOtra != null){
							try{
								boolean  eliminado = scoringService.getEliminarListaDms(solicitud.getId().getNumSolicitud());
								log.debug(eliminado);
							}catch (Exception e) {
								log.debug("Error al eliminar los dms", e.getMessage());
							}
							StringBuffer cadenaNombre = new StringBuffer();
							List<CreacionDmDTO> listaDms = new ArrayList<CreacionDmDTO>(0);
							CreacionDmDTO obejtoDm =  null;
							boolean valido = false;
							for(LcredSolicitudDm docto : listaClienteDMs){
								if(docto != null && docto.getId().getNumSolicitud() == 0){
									docto.getId().setNumSolicitud(solicitud.getId().getNumSolicitud());
								}
								if(docto != null){
								   valido = scoringService.persistSolicitudDM(docto);
								   obejtoDm = new CreacionDmDTO();
								   obejtoDm.setRut(docto.getId().getRut());
								   cadenaNombre.append(docto.getId().getNombre() +", ");
								   obejtoDm.setNombre(docto.getId().getNombre());
								   obejtoDm.setOficinaVentas(docto.getId().getOficinaVentas());
								   obejtoDm.setZona(docto.getId().getZona());
								   obejtoDm.setListaPrecio(docto.getId().getListaPrecio());
								   obejtoDm.setSector(docto.getId().getSector());
								   obejtoDm.setVendTelefono(docto.getId().getVendTelefono());
								   obejtoDm.setVendTerreno(docto.getId().getVendTerreno());
								   obejtoDm.setCobrador(docto.getId().getCobrador());
								   obejtoDm.setCondExpedicion(docto.getId().getCondExpedicion());
								   obejtoDm.setCondPago(docto.getId().getCondPago());
								   listaDms.add(obejtoDm);
								}
							}
							
							solicitudOtra.setStatusSolicitud("DC");
							solicitudOtra.setObservacionesInicial(cadenaNombre.toString());
							
							boolean validoOtra = scoringService.mergerSolicitudOtrasSolicitudes(solicitudOtra);
							if(valido == true && validoOtra == true){
								listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								FacesMessages.instance().add(Severity.INFO,"Se Actualizo la siguiente Solicitud  N° #0", solicitud.getId().getNumSolicitud());
								this.mensajeGlogal = "Se Actualizo la siguiente Solicitud  N° " + String.valueOf(solicitud.getId().getNumSolicitud());								if(listaFileUploadedDTOs != null){
								if(listaFileUploadedDTOs != null){
									for(FileUploadedDTO archivo :listaFileUploadedDTOs){
									   try {
										   if(archivo.isEliminar()){
												String nombreArchivoAux = archivo.getNombreArchivo().replace(" ", "_");
												nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
												
												OutputStream ostream = null;
												String url = "archivosOS"+"/"+ solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
												String nombreArchivo = solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
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
												archivoAux.setTipo(ArchivoAdjuntoType.INGRESO);
												archivoAux.setUbicacion("");
												archivoAux.setUsuario(usuarioLogueado);
												archivoAux.setFechaCreacion(new Date());
												entityManager.persist(archivoAux);
												entityManager.flush();
												
												entityManager.remove(archivo.getArchivo());
												entityManager.flush();
												
												ConceptoDTO objetoAux = new ConceptoDTO();
												objetoAux.setNombreArchivo(nombreArchivo);
												objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+url);
												objetoAux.setTipoArchivoType(ArchivoAdjuntoType.INGRESO);
												objetoAux.setFechaCreacion(archivoAux.getFechaCreacion());
												listaArchivoGuardado.add(objetoAux);
												objetoAux = null;
												
											}												   
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
								
								if("Enviar".equals(enviar) && listaUsuarioCorreoagregados != null){
									
									//EmailAlertaContactoMensajeHelper emailAlerta = null;
									/*setando los varlores del dto para el correo*/
									ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
									contenidoEmail.setTituloRespuesta("Solicitud corregida ");
									contenidoEmail.setSolicitud(solicitud);
									
									StringBuffer rutNombre = new StringBuffer();
									rutNombre.append(clienteTarget.getRut());
									rutNombre.append(" / ");
									rutNombre.append(modificarTexto(clienteTarget.getRazonSocial()));
									contenidoEmail.setRutNombre(rutNombre.toString());
									contenidoEmail.setSoloNombre(clienteTarget.getRazonSocial());
									contenidoEmail.setCanalVenta(clienteTarget.getCanalVenta());
									contenidoEmail.setSucursal(sucursal.getDescripcion());
									contenidoEmail.setEmisor(modificarTexto(usuarioLogueado.getNombre()));
									
									contenidoEmail.setTipoSolicitud("Creacion de DM");
									contenidoEmail.setTipoDM("DM");
									
									
								    /*datos de archivos */
									contenidoEmail.setListaArchivos(listaArchivoGuardado);
									
									/*datos de prorrogas*/
									contenidoEmail.setListaCreacionDM(listaDms);
									List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
									if(listaUsuarioCorreoagregados != null){
										for(SolicitudUsuarioCorreo ucdto : listaUsuarioCorreoagregados){
											if(ucdto != null && ucdto.getUsername() != null && ucdto.getCorreo() != null && !ucdto.getUsername().equals(usuarioLogueado.getAlias().trim())){
												log.debug("ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1", ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												Usuariosegur  usuarioAux =  scoringService.getUsuarioUsuarioSegurForUsername(ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												if(usuarioAux != null){
													if(!ultimaListaCorreoPrueba.contains(usuarioAux)){
														ultimaListaCorreoPrueba.add(usuarioAux);	
														usuarioAux = new Usuariosegur();
													}													
												}
											}
										}
									}

									Usuariosegur administradorCorreo =scoringService.getUsuarioUsuarioSegurForUsername("ADMINISTRADOR", "sck@kupfer.cl");
									if(administradorCorreo != null){
										ultimaListaCorreoPrueba.add(administradorCorreo);
										administradorCorreo = null;
									}
									
									//insertar el emisor de la solicitud. 
									Usuariosegur usuarioEmisor = scoringService.getUsuarioUsuarioSegurForUsername(usuarioLogueado.getAlias(), usuarioLogueado.getCorreo());
									if(usuarioEmisor != null){
										if(ultimaListaCorreoPrueba != null && ultimaListaCorreoPrueba.size() >  0){
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}else{
											ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}
									}

									contenidoEmail.setListaNuevosCorreos(ultimaListaCorreoPrueba);									
									try{
										if(emailAlerta != null){
											emailAlerta.enviarSolicitudDm(contenidoEmail);
										}	
									}catch (Exception e) {
										log.debug("error por que email de alerta esta nulo #0"+ e.getMessage());
									}	
	
								}
								/*se setea el numero de solicitud creada*/
								setSolicitud(solicitud);
							}
						}
					}	
				  }		
				}else if(this.paginaIngreso == 44){
					if(solicitud != null){
						solicitud.setEstado("DC");
						solicitud.setObservSolicitud(this.motivoProrroga);
						boolean mergerSolicitud = scoringService.mergerSolicitud(solicitud);
						if(solicitud != null && mergerSolicitud == true && solicitudOtra != null){
							Locale locale = new Locale("es","CL");
							NumberFormat numberFormatter;
							numberFormatter = NumberFormat.getNumberInstance(locale);
							
							List<ProrrogaDTO> listaProrrogas = new ArrayList<ProrrogaDTO>(0);
							ProrrogaDTO obejtoProrroga =  null;
							LcredSolicitudProrroga objeto = null;
							boolean valido = false;
							for(DocumentoProrrogaDTO docto : listaDoctoProrrogaAgregados){
								LcredSolicitudProrrogaId solProrroga = null;
								if(docto != null && docto.isStatus() == true){
									 objeto = scoringService.getDoctoProrrogaForSolicitud(solicitud.getId().getNumSolicitud(), docto.getNumeroDocto());
									if(objeto == null){
										solProrroga = new LcredSolicitudProrrogaId();
										SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
										String fechaAux1 = format.format(docto.getFechaVencActual());
										String fechaAux2 = format.format(docto.getFechaVencNuevo());
										
										solProrroga.setNumSolicitud(solicitud.getId().getNumSolicitud());
										solProrroga.setNroCheque(docto.getNumeroDocto());
										solProrroga.setMonto(new BigDecimal(docto.getMonto()));
										solProrroga.setVencActual(fechaAux1);
										solProrroga.setVencNuevo(fechaAux2);
									}else{
										SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
										String fechaAux1 = format.format(docto.getFechaVencActual());
										String fechaAux2 = format.format(docto.getFechaVencNuevo());
										objeto.getId().setMonto(new BigDecimal(docto.getMonto()));
										objeto.getId().setVencActual(fechaAux1);
										objeto.getId().setVencNuevo(fechaAux2);										
									}
										
								}
	
								if(solProrroga != null && docto != null && objeto == null){
								   LcredSolicitudProrroga lsp = new LcredSolicitudProrroga();
								   lsp.setId(solProrroga);
								   valido = scoringService.persistSolicitudProrroga(lsp);
								   if(valido){
									   obejtoProrroga = new ProrrogaDTO();
									   obejtoProrroga.setNumeroDocto(solProrroga.getNroCheque());
									   obejtoProrroga.setMontoFormateado(numberFormatter.format(solProrroga.getMonto()));
									   obejtoProrroga.setMotivoProrroga(this.motivoProrroga);
									   obejtoProrroga.setFechaVencicmiento(solProrroga.getVencActual());
									   obejtoProrroga.setFechaSolicitado(solProrroga.getVencNuevo());
									   listaProrrogas.add(obejtoProrroga);
								   }
								}else if(solProrroga == null && docto != null && objeto != null){
									   valido = scoringService.mergeSolicitudProrroga(objeto);
									   obejtoProrroga = new ProrrogaDTO();
									   obejtoProrroga.setNumeroDocto(objeto.getId().getNroCheque());
									   obejtoProrroga.setMontoFormateado(numberFormatter.format(objeto.getId().getMonto()));
									   obejtoProrroga.setMotivoProrroga(this.motivoProrroga);
									   obejtoProrroga.setFechaVencicmiento(objeto.getId().getVencActual());
									   obejtoProrroga.setFechaSolicitado(objeto.getId().getVencNuevo());
									   listaProrrogas.add(obejtoProrroga);									
								}
							}							
							
							solicitudOtra.setStatusSolicitud("DC");
							boolean exitoOtra = scoringService.mergerSolicitudOtrasSolicitudes(solicitudOtra);
							if(exitoOtra){
								listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
								FacesMessages.instance().add(Severity.INFO,"Se Actualizo la siguiente Solicitud  N° #0", solicitud.getId().getNumSolicitud());
								this.mensajeGlogal = "Se Actualizo la siguiente Solicitud  N° " + String.valueOf(solicitud.getId().getNumSolicitud());
								if(listaFileUploadedDTOs != null){
									for(FileUploadedDTO archivo :listaFileUploadedDTOs){
									   try {
										   if(archivo.isEliminar()){
												String nombreArchivoAux = archivo.getNombreArchivo().replace(" ", "_");
												nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
												
												OutputStream ostream = null;
												String url = "archivosOS"+"/"+ solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
												String nombreArchivo = solicitud.getId().getNumSolicitud()+"_"+tipoSolicitud.getCodTipoSolicitud()+"_"+archivo.getNombreArchivo();
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
												archivoAux.setTipo(ArchivoAdjuntoType.INGRESO);
												archivoAux.setUbicacion("");
												archivoAux.setUsuario(usuarioLogueado);
												archivoAux.setFechaCreacion(new Date());
												entityManager.persist(archivoAux);
												entityManager.flush();
												
												entityManager.remove(archivo.getArchivo());
												entityManager.flush();
												
												ConceptoDTO objetoAux = new ConceptoDTO();
												objetoAux.setNombreArchivo(nombreArchivo);
												objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+""+url);
												objetoAux.setTipoArchivoType(ArchivoAdjuntoType.INGRESO);
												objetoAux.setFechaCreacion(archivoAux.getFechaCreacion());
												listaArchivoGuardado.add(objetoAux);
												objetoAux = null;
												
											}										   
											
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
								
								if("Enviar".equals(enviar) && listaUsuarioCorreoagregados != null){
									
									//EmailAlertaContactoMensajeHelper emailAlerta = null;
									/*setando los varlores del dto para el correo*/
									ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
									contenidoEmail.setTituloRespuesta("Solicitud corregida ");
									contenidoEmail.setSolicitud(solicitud);
									
									StringBuffer rutNombre = new StringBuffer();
									rutNombre.append(clienteTarget.getRut());
									rutNombre.append(" / ");
									rutNombre.append(modificarTexto(clienteTarget.getRazonSocial()));
									contenidoEmail.setRutNombre(rutNombre.toString());
								
									contenidoEmail.setSoloNombre(clienteTarget.getRazonSocial());
									contenidoEmail.setCanalVenta(modificarTexto(clienteTarget.getCanalVenta()));
									contenidoEmail.setSucursal(modificarTexto(sucursal.getDescripcion()));
									contenidoEmail.setEmisor(modificarTexto(usuarioLogueado.getNombre()));
									
									contenidoEmail.setTipoSolicitud("Prorroga de Cheque");
									contenidoEmail.setTipoProrroga("Prorroga.");
									contenidoEmail.setMotivoProrroga(modificarTexto(this.motivoProrroga));
									
									
								    /*datos de archivos */
									contenidoEmail.setListaArchivos(listaArchivoGuardado);
									
									/*datos de prorrogas*/
									contenidoEmail.setListaProrrogas(listaProrrogas);
									
									List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
									if(listaUsuarioCorreoagregados != null){
										for(SolicitudUsuarioCorreo ucdto : listaUsuarioCorreoagregados){
											if(ucdto != null && ucdto.getUsername() != null && ucdto.getCorreo() != null && !ucdto.getUsername().equals(usuarioLogueado.getAlias().trim())){
												log.debug("ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1", ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												Usuariosegur  usuarioAux =  scoringService.getUsuarioUsuarioSegurForUsername(ucdto.getUsername().trim(), ucdto.getCorreo().trim());
												if(usuarioAux != null){
													if(!ultimaListaCorreoPrueba.contains(usuarioAux)){
														ultimaListaCorreoPrueba.add(usuarioAux);	
														usuarioAux = new Usuariosegur();
													}													
												}
											}
										}
									}

									Usuariosegur administradorCorreo =scoringService.getUsuarioUsuarioSegurForUsername("ADMINISTRADOR", "sck@kupfer.cl");
									if(administradorCorreo != null){
										ultimaListaCorreoPrueba.add(administradorCorreo);
										administradorCorreo = null;
									}
									
									//insertar el emisor de la solicitud. 
									Usuariosegur usuarioEmisor = scoringService.getUsuarioUsuarioSegurForUsername(usuarioLogueado.getAlias(), usuarioLogueado.getCorreo());
									if(usuarioEmisor != null){
										if(ultimaListaCorreoPrueba != null && ultimaListaCorreoPrueba.size() >  0){
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}else{
											ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(0);
											ultimaListaCorreoPrueba.add(usuarioEmisor);
										}
										contenidoEmail.setCorreoEmisor(usuarioEmisor.getAlias());
									}

									contenidoEmail.setListaNuevosCorreos(ultimaListaCorreoPrueba);									
									try{
										if(emailAlerta != null){
											emailAlerta.enviarSolicitudProrroga(contenidoEmail);
										}	
									}catch (Exception e) {
										log.debug("error por que email de alerta esta nulo #0"+ e.getMessage());
									}	
								}
								/*se setea el numero de solicitud creada*/
								setSolicitud(solicitud);
								
							}
						}
					}	
				}
			   
			/*hitos de solicitud*/
			SolicitudHitos hitos = new SolicitudHitos();
			hitos.setEmisor(usuarioLogueado.getAlias());
			hitos.setIdSolicitud(solicitud.getId().getNumSolicitud());
			hitos.setUsuarioActual(null);
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
				
			/*ingreso de logs*/
			try{
				String codigo = "P";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Se vuelve ha enviar la solicitud actualizada.");
					exito = scoringService.persistSolicitudLogs(solicitud.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Se vuelve ha enviar la solicitud actualizada., no se encontro el estado.");
					exito = scoringService.persistSolicitudLogs(solicitud.getId().getNumSolicitud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
				log.debug("verificacion si inserto registro logs #0", exito);
				
			}catch (Exception e) {
				log.error("Error, al insertar el hitos de la solicitud #0", e.getMessage());
			}		
			
			
				/*limpiar datos */
				limpiarSegunTipoSolictud(this.paginaIngreso);	
				limpiarDespuesGenerar();
			
				Calendar cal2 = Calendar.getInstance();
				
				this.setTiempoEjecucionProceso(calcularTiempoDelProceso(cal, cal2));
				this.setTiempoEjecucionMenu(calcularTiempoDelProceso(cal, cal2));
		}	
		
	}catch (Exception e) {
		log.error("Error, al ingresar los datos de la solictud #0", e.getMessage());
	}
	log.debug("Mensaje #0", this.mensajeGlogal);
}
	
	public void limpiarSegunTipoSolictud(long tipoSolicitud){
		try{

			if(tipoSolicitud == 11 || tipoSolicitud == 12){
				this.monto = (long)0;
				this.pie = (long)0;
				this.listaCotPedDTOs = null;
				this.tipoFormaPago = null;
				this.encabezado = null;
				this.margenGlobal = (double)0;
				this.motivoCompra = null;
				this.listaUsuarioCorreoagregados = null;
				this.listaFileUploadedDTOs = null;
				this.observaciones = null;
				this.porcentajeGlobal = (double)0;
				this.montoTotalNeto = (double)0;
				this.montoTotal = (double)0;
				
			}else if(tipoSolicitud == 21 || tipoSolicitud == 22 || tipoSolicitud == 23 || tipoSolicitud == 24){
				this.descripcionProyecto=null;
				this.montoCredito =  null;
				this.plazoEjecucion = null;
				this.potencialCompra = null;
				this.conceptoNegocios = null;
				this.rutNombresSocios = null;
				this.observacionesCredito = null;				
				this.observaciones = null;
				this.monto = (long)0;
				this.listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
				evaluarInnominalNominalRKupferAseguradora();
			}else if(tipoSolicitud == 31 || tipoSolicitud == 32){
				setClsRiesgo(null);
				setcPago(null);
				this.motivoRiesgoPago = null;
				this.listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);				
				if(this.paginaIngreso == 31){
					setCondicionRiesgo(true);
					setComboCRiesgo(false);
					setCondicionPago(false);
					setComboCPago(true);
				}else if(this.paginaIngreso == 32){
					setCondicionRiesgo(false);
					setComboCRiesgo(true);
					setCondicionPago(true);
					setComboCPago(false);	
				}
				evaluarRiesgoPago();				
			}else if(tipoSolicitud == 41){
				this.opcionBloqueoDesbloqueo = null;
				this.motivoBloDesbloqueo = null;
				this.observaciones = null;
				this.listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
				
			}else if(tipoSolicitud == 43){
				this.listaClienteDMs = new ArrayList<LcredSolicitudDm>(0);
				this.listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);		
				
			}else if(tipoSolicitud == 44){
				
				this.motivoProrroga = null;
				this.listaDoctoProrrogaAgregados = new ArrayList<DocumentoProrrogaDTO>(0);
				this.listaUsuarioCorreoagregados = new ArrayList<SolicitudUsuarioCorreo>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);				
			}
		}catch (Exception e) {
			log.error("error al limpiar las variables ...");
		}
	}
	
	public void limpiarDespuesGenerar(){
		clienteTarget = new ClienteDTO();
		this.setRutAux(null);
		clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
		clienteCompComrcial = new CompComercialDTO();
		this.setDm(null);
	}
	
	public String generarDescripcionProducto(){
		StringBuilder cadena = new StringBuilder();
		int contador = 1;
		try{
			if(listaCotPedDTOs != null){
				for(CotPedDTO cotped : listaCotPedDTOs){
					 if(cotped.getListaCabeceraCotPeds() != null){
						 for(CabeceraCotPedDTO o :cotped.getListaCabeceraCotPeds()){
							 if(o.getListaDetalle() != null){
								for(DetalleCotPedDTO obj : o.getListaDetalle()){
									if(obj.getListaDetalleCp() != null){
										for(DetalleCp dcp : obj.getListaDetalleCp()){
											cadena.append("Producto : "+ contador);
											cadena.append(dcp.getNombre());
											cadena.append(" ");
											contador++;
										}
									}
								}
							 }
						 }
					 }
				}	 
			}
		}catch (Exception e) {
			log.error("Error, al sacar la descripcion de los productos.", e.getMessage());
		}
		return cadena.toString();
	}
	
	
	public void salirDelIngresoSolicitud(){
	  log.debug("salir del pop.");	
	  if(this.salirDelMensaje == false){
		  this.salirDelMensaje = true;
	  }
	  
	}
	

	
	
	/*
	 * creacion de los metodos, para poder  subir logo de colegio
	 * */
	public List<UploadItem> archivosSubidos = new ArrayList<UploadItem>(); 
	public List<UploadItem> getArchivosSubidos() {
		if(archivosSubidos != null && archivosSubidos.size() > 0){
			UploadItem obj = (UploadItem)archivosSubidos.get(0);
			log.debug(obj.getData());
		}
		return archivosSubidos;	
	}
	public void setArchivosSubidos(List<UploadItem> archivosSubidos) { 
		this.archivosSubidos = archivosSubidos;
		}
	public void limpiarArchivosSubidos(){
		archivosSubidos.clear();
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
		functionCtaCte.getImportParameterList().setValue("CODCLIENTE", clienteTarget.getCleanRut().toUpperCase()); // Paso de parametros
		connect.execute(functionCtaCte);
		JCoTable datosCtaCte = functionCtaCte.getTableParameterList().getTable("DETALLE"); //tabla de salida
		log.debug(clienteTarget.getCleanRut().toUpperCase());
		log.debug(datosCtaCte);
		
		for (int i = 0; i < datosCtaCte.getNumRows(); i++){
			DeudaActual ctaCte = new DeudaActual();
			datosCtaCte.setRow(i);
			ctaCte.setRut(clienteTarget.getCleanRut().toUpperCase());
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
			listaFechas = scoringService.getFechasCuentaCorriente(clienteTarget.getCleanRut().toUpperCase());
		}
		
		
	}
	
	public void guardarInformacionCtaCte(){
		try{
			boolean ingresar = scoringService.getVerificarHistorialCuentaCorriente(clienteTarget.getCleanRut().toUpperCase(), new Date());
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
		
		
		List<DeudaActual> listaDeudas = scoringService.getConsultarHistorialCuentaCorriente(clienteTarget.getCleanRut().toUpperCase(), fecha);
		if(listaDeudas != null && listaDeudas.size() >0){
			ctaCteList = listaDeudas;
		}else{
			if(ctaCteList != null){
				ctaCteList = null;
			}
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
	
	public void agregarConceptoListaReserva(){
		boolean exito = false;
			
		if(this.listaCboConceptoNegocioSeleccion == null){
			FacesMessages.instance().add(Severity.WARN,"Debe seleccionar un concepto para ingresar a la lista.");
			mensajeExplicativo="Error";	
			return;
		}else{
			if(this.listaCboConceptoNegocioSeleccion.size() == 0){
				FacesMessages.instance().add(Severity.WARN,"Debe seleccionar un concepto para ingresar a la lista.");
				mensajeExplicativo="Error";	
				return;				
			}
		}
		
		
		if(listaSelConceptoNegocio != null){
			for(ConceptosNegocio cn : this.listaCboConceptoNegocioSeleccion){
				ConceptoNegocioDTO nuevo = new ConceptoNegocioDTO();
				nuevo.setConceptoNegocio(cn);
				nuevo.setMonto((long)0);				
				for(ConceptoNegocioDTO obj : listaSelConceptoNegocio){
					if(obj.getConceptoNegocio().getConcepto().equals(nuevo.getConceptoNegocio().getConcepto())){
						exito = true;
					}
				}
				if(exito ==  false){
					listaSelConceptoNegocio.add(nuevo);
					setConceptoNegocioCbo(null);
					setMontoConcepto((long)0);
				}else{
					log.debug(" el concepto que ya existe en la grilla es #0", nuevo.getConceptoNegocio().getConcepto());
				}
				nuevo =  null;				
			}
			
		}else{
			listaSelConceptoNegocio = new ArrayList<ConceptoNegocioDTO>(0);
			for(ConceptosNegocio cn : this.listaCboConceptoNegocioSeleccion){
				ConceptoNegocioDTO nuevo = new ConceptoNegocioDTO();
				nuevo.setConceptoNegocio(cn);
				nuevo.setMonto((long)0);
				listaSelConceptoNegocio.add(nuevo);
				nuevo = null;				
			}
		}
		
		this.listaCboConceptoNegocioSeleccion.clear();
		this.listaCboConceptoNegocio = null;
		if(this.listaCboConceptoNegocio == null){
			/*sacar concepto y negocio*/
			 setListaCboConceptoNegocio(scoringService.obtenerConceptosNegocios());			
		}
	}
    /* eliminar  Concepto de Negocio*/
	public void eliminarConceptoNegocioListaAgregado(ConceptoNegocioDTO conceptoNegocio){
		if(conceptoNegocio != null){
			listaSelConceptoNegocio.remove(conceptoNegocio);
		}
	}

    /* seteo stringe de  Concepto de Negocio*/
	public void generarStringConceptoAsociado(){
		long montofinal = 0;
		if(this.potencialCompra == null){
			 montofinal = 0;
		}else{
			 montofinal = this.potencialCompra;
		}
	  	
		try{
			if(listaSelConceptoNegocio != null){
				int cantidad = listaSelConceptoNegocio.size();
				int contador = 1;
				String cadena ="";
				for(ConceptoNegocioDTO cn : listaSelConceptoNegocio){
					if(contador < cantidad){
						cadena+=(cn.getConceptoNegocio().getDescripcion()).trim()+"-";
					}else if(contador == cantidad){
						cadena+=(cn.getConceptoNegocio().getDescripcion()).trim()+"";
					}
					montofinal+=cn.getMonto();
					contador++;
				}
				setConceptoNegocios(cadena);
				if(montofinal == 0){
					setPotencialCompra(null);
				}else{
					setPotencialCompra(montofinal);
				}
			}
			
		}catch (Exception e) {
			log.error("Error al setear el string de concepto ", e.getMessage());
		}
	}
	

	@SuppressWarnings("unused")
	public void agregarSociosListaReserva(){
		
		if(this.rutSocio == null && this.socio == null ){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el rut del socio  o el nombre del socio.");
			mensajeExplicativo="Error";
			return;			
		}else{
			if("".equals(this.rutSocio) &&  "".equals(this.socio)){
				FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el rut del socio  o el nombre del socio.");
				mensajeExplicativo="Error";
				return;			
			}
		}

		String cadena = "";
		
		if(this.rutSocio != null && !"".equals(this.rutSocio)){
			cadena += this.rutSocio;
		}
		if(this.socio != null && !"".equals(this.socio) ){
			if(this.rutSocio != null && !"".equals(this.rutSocio)){
				cadena += "-->";
				cadena += this.socio;
			}else{
				cadena += this.socio;
			}
		}
		
		if(cadena != null || !"".equals(cadena)){
			if(listaSocios != null){
				if(!listaSocios.contains(cadena)){
					listaSocios.add(cadena);
					setRutSocio(null);
					setSocio(null);
				}else{
					FacesMessages.instance().add(Severity.ERROR,"El concepto seleccionado ya esta agregado en la grilla.");
					mensajeExplicativo="Error";
				}
			}else{
				listaSocios = new ArrayList<String>(0);
				listaSocios.add(cadena);
				setRutSocio(null);
				setSocio(null);
			}
		}else{
			FacesMessages.instance().add(Severity.WARN,"Debe seleccionar un socio para ingresar a la lista.");
			mensajeExplicativo="Error";
			return;
		}
	}

    /* seteo string de  socios */
	public void generarStringSocios(){
		try{
			if(listaSocios != null){
				int cantidad = listaSocios.size();
				int contador = 1;
				String cadena ="";
				for(String cn : listaSocios){
					
					if(contador < cantidad){
						cadena+=cn+",";
					}else if(contador == cantidad){
						cadena+=cn+".";
					}
					contador++;
				}
				setRutNombresSocios(cadena);
			}
		}catch (Exception e) {
			log.error("Error al setear el string de rut y nombre del socio ", e.getMessage());
		}
	}
    /* eliminar  Concepto de Negocio*/
	public void eliminarSociosListaAgregado(String socio){
		if(socio != null){
			listaSocios.remove(socio);
		}
	}	
	
	public void ObtenerEstadoSolictud(String codigo){
		try{
			estadoInicial = (LcredEstado)entityManager.find(LcredEstado.class, codigo);
		}catch (Exception e) {
			log.error("Error, al sacar el objeto de estado para la solicitud #0", e.getMessage());
			estadoInicial = null;
		}
	}
	
	public void limpiarDatos(){
		if(ctaCteList != null){
			ctaCteList.clear();

		}
		
	}

	public void seleccionOpcionesBloqueoDesbloqueo(){
		if(this.opcionBloqueoDesbloqueo != null){
			log.debug("la opcion seleccionada es #0", this.opcionBloqueoDesbloqueo);
		}else{
			log.error("error de seleccion viene vacio");
		}
	}
	
	public void obtenerCorreoBloqueDesbloqueo(){
		try{
			if(listaDestinatarios != null){
				listaDestinatarios.clear();
			}
			
			/*va a buscar los destinatarios de las solictudes*/
			try{
				listaDestinatarios = scoringService.obtenerListaDestinatario(
						String.valueOf(this.tipoSolicitudCodigo), 
                        String.valueOf(clienteTarget.getCodigoCanal().ordinal()), 
                        sucursal.getZona(), 
                        sucursal.getCodigo(), 
                        "", 
                        "","1");	
				
				
//				listaDestinatarios = scoringService.obtenerListaDestinatario(usuarioLogueado.getAlias(), "01", 
//		                        clienteTarget.getCodigoCanal().name(), 
//		                        sucursal.getZona(), 
//		                        sucursal.getCodigo(), 
//		                        0, 
//		                        "5");	
					
					
				
				if(listaDestinatarios != null){
					listaUsuarioCorreoagregados = new  ArrayList<SolicitudUsuarioCorreo>();
					listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
					listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
					for(DestinatarioDTO dto : listaDestinatarios){
						if(dto != null){
							log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
									,dto.getTipoSolicitud(), dto.getUsername(), dto.getZona(),
									 dto.getSucursal(), dto.getConcepto(), dto.getNegocio() );

						}
					}	

				}
			}catch (Exception e) {
				log.error("Error, sacar los destinatarios : #0", e.getMessage());
			}
		}catch (Exception e) {
			log.error("Error, al sacar los correo de bloque y desbloqueo #0", e.getMessage()); 
		}
	}
	
	public void obtenerChequeProrroga(){
		/*limpiar la lista */
		StringBuilder pedCotBuild = null;
		listaDoctoProrroga = new ArrayList<DocumentoProrrogaDTO>(0);
				
		SapSystem system = new SapSystem(globalParameters.getNameSap(),
				globalParameters.getIpSap(), globalParameters.getClientSap(), 
				globalParameters.getSystemNumberSap(),globalParameters.getUserSap(),
				globalParameters.getPasswordSap()); 

		Connect connect = new Connect(system);
		functionCtaCte = connect.getFunction("ZFIFN_CTACTE"); // Nombre RFC
		functionCtaCte.getImportParameterList().setValue("CODCLIENTE", clienteTarget.getCleanRut().toUpperCase()); // Paso de parametros
		connect.execute(functionCtaCte);
		JCoTable datosCtaCte = functionCtaCte.getTableParameterList().getTable("DETALLE"); //tabla de salida
		log.debug(clienteTarget.getCleanRut().toUpperCase());
		log.debug(datosCtaCte);
		
		for (int i = 0; i < datosCtaCte.getNumRows(); i++){
			DocumentoProrrogaDTO ctaCte = new DocumentoProrrogaDTO();
			datosCtaCte.setRow(i);
			ctaCte.setRut(clienteTarget.getCleanRut().toUpperCase());
			ctaCte.setDoctoContable(datosCtaCte.getValue("DOCTO_CONTABLE").toString());
			ctaCte.setFechaContable(datosCtaCte.getDate("FECHA_CONTABLE"));
			ctaCte.setCl(datosCtaCte.getValue("CLASE_DOCTO").toString());
			ctaCte.setNumeroDocto(datosCtaCte.getValue("NUM_DOCTO").toString());
			ctaCte.setNroFactura(datosCtaCte.getValue("NUM_FACTURA").toString());
			ctaCte.setFechaVencActual(datosCtaCte.getDate("FEC_VCTO"));
			ctaCte.setIndCme(datosCtaCte.getValue("IND_CME").toString());
			ctaCte.setReferencia(datosCtaCte.getValue("REFERENCIA").toString());
			ctaCte.setMonto(((BigDecimal)datosCtaCte.getValue("MTO_DOCTO")).longValue());
			ctaCte.setCodigoCliente(datosCtaCte.getValue("CODCLIENTE").toString());
			ctaCte.setTexto(datosCtaCte.getValue("TEXTO").toString());
			ctaCte.setDiasMoras(((BigDecimal)datosCtaCte.getValue("DIASMORA")).intValue());
			if("DC".equals(ctaCte.getCl()) || "DA".equals(ctaCte.getCl()) || "SC".equals(ctaCte.getCl()) || "IC".equals(ctaCte.getCl()) || "CD".equals(ctaCte.getCl()) || "AB".equals(ctaCte.getCl())){
				pedCotBuild = new StringBuilder(ctaCte.getNumeroDocto());
				while(pedCotBuild.toString().length() < 10) {
						pedCotBuild.insert(0, '0');
				}
				ctaCte.setNumeroDocto(pedCotBuild.toString());
				if(ctaCte.getMonto() > 0){
					listaDoctoProrroga.add(ctaCte);
					log.debug(pedCotBuild.toString());

				}
				
			}
		}
	}
	
	public void getSolicitudesEstados(int opcion){
		try{
			if(opcion == 1){
				if(this.listaSolicitudesEstados == null && usuarioSegur.getAlias() != null){
					this.listaSolicitudesEstados = scoringService.obtenerSolicitudesEstados(usuarioSegur.getAlias(), "I", "05/10/2012");
				}
			}			
		}catch (Exception e) {
			log.error("Error, al sacar las solicitudes de estados #0 ", e.getMessage());
		}
		
	}
	
	/*metodos de para sacar las lista solicitudes*/
	@SuppressWarnings("unchecked")
	public void sacarListasTodasSolicitudes(){
		Sucursal suc = null;
		try {

			if (lcredUsuarioNivelEnc != null) {
				sacarEstadosSolicitudes();
				/************Todas las solicitudes **********************/
				if (lcredUsuarioNivelEnc != null) {
					List<LcredSolicitud> listaTodasSolicitudes = (List<LcredSolicitud>) entityManager
							.createQuery("Select s from LcredSolicitud s  order by s.id.numSolicitud desc")
							.setMaxResults(1000)
							.getResultList();

					if (listaTodasSolicitudes != null) {
						listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>(0);

						for (LcredSolicitud sol : listaTodasSolicitudes) {
							boolean autorizacion = false; 
							if("Mixto".equals(sol.getCanal())){
								autorizacion = verificaCanal(TipoCuentasKupferType.MX);
							}else if("Kupfer Express".equals(sol.getCanal())){
								autorizacion = verificaCanal(TipoCuentasKupferType.KX);
							}else if("Grandes Cuentas".equals(sol.getCanal())){
								autorizacion = verificaCanal(TipoCuentasKupferType.GC);
							}
							
							if(autorizacion){
								SolicitudDTO nuevo = new SolicitudDTO();
								nuevo.setIdSolictud(sol.getId().getNumSolicitud());
								nuevo.setRut(sol.getRutCliente());
								nuevo.setRazonSocial(sol.getNomCliente());
								nuevo.setEmisor(sol.getCodEmisor());
								nuevo.setDespTipoSolictud(sol.getSubTiposol());
								nuevo.setFechaSalida(sol.getFecSalida());
								nuevo.setUsuarioProceso(sol.getUsuarioActual());
								nuevo.setTipoSolicitud(sol.getTipTiposol());
								nuevo.setCanal(sol.getCanal());
	
								if (sol.getId().getFecSolicitud() != null
										&& sol.getHraSolicitud() != null) {
	
									/* rescatar la fecha */
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									String fecha = sdf.format(sol.getId().getFecSolicitud());
	
									/* rescatar la hora */
									SimpleDateFormat sdh = new SimpleDateFormat("HH:mm");
									String hora = sdh.format(sol.getHraSolicitud());
	
									/* fecha y hora */
									//log.debug(" Fecha String:" + fecha);
									//log.debug(" hora String :" + hora);
	
									DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
									try {
										Date d = formatoFecha.parse(fecha + " "	+ hora);
										nuevo.setFechaEmision(d);
										nuevo.setHoraEmision(sol.getHraSolicitud());
									} catch (Exception e) {
										throw new Exception(e);
									}
								}
	
								if (sol.getEstado() != null) {
									LcredEstado estado = obtenerObjetoLcredEstado(sol
											.getEstado());
									if (estado != null) {
										nuevo.setCodigoEstado(estado.getCodEstado());
										nuevo.setEstado(estado.getDesEstado());
									}
	
									if (sol.getEstado().equals("I")) {
										LcredPerfiles perfil = scoringService
												.obtenerPerfil("3");
										if (perfil != null) {
											nuevo.setProceso(perfil.getDesPerfil());
										}
									}
								}
	
								if (sol.getSucursalEmisor() != null) {
									//log.debug(" codigo de sucursal  :"	+ sol.getSucursalEmisor());
									suc = obtenerObjetoSucursal(sol
											.getSucursalEmisor());
									if (suc != null) {
										nuevo.setSucursal(suc.getDescripcion());
									} else {
										nuevo.setSucursal(sol.getSucursalEmisor());
									}
	
								}
	
								if (sol.getConPago() != null) {
									//log.debug("condicion de pago  :"+ sol.getConPago());
									CPagoDTO obj = obtenerObjetoCPago(sol
											.getConPago());
									if (obj != null) {
										nuevo.setCondicionPago(obj.getDescripcion());
									} else {
										nuevo.setCondicionPago(sol.getConPago());
									}
	
								}
	
								if (sol.getMontoAsegurado() != null) {
									nuevo.setMontoAsegurado(sol.getMontoAsegurado()
											.doubleValue());
								} else {
									nuevo.setMontoAsegurado((double) 0);
								}
	
								if (sol.getMonto() != null) {
									nuevo.setMonto(sol.getMonto().doubleValue());
								} else {
									nuevo.setMonto((double) 0);
								}
	
								if (sol.getRiesgoKupfer() != null) {
									nuevo.setMontoRiegoKupfer(sol.getRiesgoKupfer()
											.doubleValue());
								} else {
									nuevo.setMontoRiegoKupfer((double) 0);
								}
	
								if (sol.getEvaluar() != null) {
									nuevo.setEvaluar(sol.getEvaluar());
								} else {
									nuevo.setEvaluar(false);
								}
								if (sol.getAnalizar() != null) {
									nuevo.setAnalizar(sol.getAnalizar());
								} else {
									nuevo.setAnalizar(false);
								}
	
								
								if(usuarioCargoAux != null && !usuarioCargoAux.getCargo().getCodCargo().equals("015")){
									nuevo.setControlador(2);
								}else{
									nuevo.setControlador(2);
								}
								listaTodasSolicitudesDto.add(nuevo);
								nuevo = null;
							}	
						}

					}

				}
				
			}
		} catch (Exception e) {
			log.error("error al sacar todas las solictudes en generar #0",e.getMessage());
		}
	}

	public Sucursal obtenerObjetoSucursal(String codigo) {
		Sucursal suc = null;
		if (listaSucursales != null && codigo != null) {
			for (Sucursal s : listaSucursales) {
				if (s.getCodigo().equals(codigo)) {
					suc = s;
					break;
				}
			}
		}
		return suc;
	}
	
	public CPagoDTO obtenerObjetoCPago(String codigo) {
		CPagoDTO obj = null;
		if (listaCondicionPagos != null && codigo != null) {
			for (CPagoDTO s : listaCondicionPagos) {
				if (s.getCodigo().equals(codigo)) {
					obj = s;
					break;
				}
			}
		}
		return obj;
	}
	public void sacarEstadosSolicitudes() {
		try {
			listaEstados = scoringService.obtenerEstados();
		} catch (Exception e) {
			log.error("Error, al sacar la lista de estados de pagos #0",
					e.getMessage());
		}
	}	
	public LcredEstado obtenerObjetoLcredEstado(String codigo) {
		LcredEstado obj = null;
		if (listaEstados != null && codigo != null) {
			for (LcredEstado s : listaEstados) {
				if (s.getCodEstado().equals(codigo)) {
					obj = s;
					break;
				}
			}
		}
		return obj;
	}
	public void sacarLogSolicitud(SolicitudDTO sol) {
		try {
			if (sol != null) {
				globalHitosLogdService.setSolicitud(sol);
				List<SolicitudLogs> listaSolicitudesLogs = scoringService
						.getSolicitudLogs(sol.getIdSolictud());
				if (listaSolicitudesLogs != null) {
					globalHitosLogdService.setListaLogs(listaSolicitudesLogs);
				}
			}
		} catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud",
					e.getMessage());
		}

	}
	public void sacarLogHitos(SolicitudDTO sol) {
		try {
			if (sol != null) {
				globalHitosLogdService.setSolicitud(sol);

				List<SolicitudHitos> listaSolicitudesHitos = scoringService
						.getSolicitudHitos(sol.getIdSolictud());
				if (listaSolicitudesHitos != null) {
					globalHitosLogdService.setListaHitos(listaSolicitudesHitos);
				}
			}

		} catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud",
					e.getMessage());
		}

	}
	public boolean verificaCanal(TipoCuentasKupferType tipoCuenta){
		boolean permiso = false;
		try{
			if(this.listaCanalUsuarioCargos != null){
				for(CanalUsuarioCargo cuc : this.listaCanalUsuarioCargos){
					if(cuc.getTipoCuenta().name().equals(tipoCuenta.name())){
						permiso=true;
						break;
					}
				}
				
			}
		}catch (Exception e) {
			log.error("Error, al buscar el canal", e.getMessage());
		}
		return permiso;
	}
	public boolean verificaZonaSucursalNegocioConcepto(ConceptosType concepto, FuncionesType funcion, String codigo){
		boolean permiso = false;
		List<ZonaSucursalNegocioConcepto> listaNueva = new ArrayList<ZonaSucursalNegocioConcepto>(0);
		try{
			
			if(this.listaZonaSucursalNegocioConceptos != null){
				for(ZonaSucursalNegocioConcepto zsnc : this.listaZonaSucursalNegocioConceptos){
					if(zsnc.getTipoConcepto().name().equals(concepto.name()) && zsnc.getPerfilFuncionCanal().getFuncion().name().equals(funcion.name())){
						listaNueva.add(zsnc);
					}
				}
				
				if(listaNueva != null){
					for(ZonaSucursalNegocioConcepto cod : listaNueva){
						if(cod.getCodigo().equals(codigo)){
							permiso = true;
							break;
						}
					}
				}
			}
		}catch (Exception e) {
			log.error("Error, al buscar el codigo de zona o sucursal o negocio o concepto.", e.getMessage());
		}
		return permiso;
	}
	public String calcularTiempoDelProceso(Calendar cal, Calendar cal2){
		String horaFinal = "";
		try{
	        // conseguir la representacion de la fecha en milisegundos
	        long milis1 = cal.getTimeInMillis();
	        long milis2 = cal2.getTimeInMillis();
	        // calcular la diferencia en milisengundos
	        long diff = (milis2 - milis1);
	        // calcular la diferencia en segundos
	        long diffSeconds = ((diff / 1000)/60);
	        // calcular la diferencia en minutos
	        long diffMinutes = diff / (60 * 1000);
	        // calcular la diferencia en horas
	        long diffHours = diff / (60 * 60 * 1000);
	        // calcular la diferencia en dias
	        long diffDays = diff / (24 * 60 * 60 * 1000);
	        log.debug("En milisegundos: " + diff + " milisegundos.");
	        log.debug("En segundos: " + diffSeconds + " segundos.");
	        log.debug("En minutos: " + diffMinutes + " minutos.");
	        log.debug("En horas: " + diffHours + " horas.");
	        log.debug("En dias: " + diffDays + " dias.");
			
			String horaResultado = diffHours +":"+diffMinutes+":"+ diffSeconds;
			log.debug("hora : minuto : segundo = #0", horaResultado);
			
			
			if(diffHours <10){
				horaFinal+="0"+diffHours+":";
			}else{
				horaFinal+=String.valueOf(diffHours)+":";
			}
			
			if(diffMinutes < 10){
				horaFinal+="0"+diffMinutes+":";
			}else{
				horaFinal+=String.valueOf(diffMinutes)+":";
			}
			
			if(diffSeconds < 10){
				horaFinal+="0"+diffSeconds;
			}else{
				horaFinal+=String.valueOf(diffSeconds);
			}
			
			log.debug("HH: MM : SS : MS= #0", horaFinal);
		}catch (Exception e) {
			log.error("Error al calcular el proceso de tiempo : #0", e.getMessage());
		}
		
		return horaFinal;
	}
	public String sacarCaracteresInvalidoParaWeb(String nombre){
		try{
			nombre = nombre.replaceAll("á", "a");
			nombre = nombre.replaceAll("é", "e");
			nombre = nombre.replaceAll("í", "i");
			nombre = nombre.replaceAll("ó", "o");
			nombre = nombre.replaceAll("ú", "u");
			nombre = nombre.replaceAll("Á", "A");
			nombre = nombre.replaceAll("É", "E");
			nombre = nombre.replaceAll("Í", "I");
			nombre = nombre.replaceAll("Ó", "O");
			nombre = nombre.replaceAll("Ú", "U"); 
			
		}catch (Exception e) {
			log.error("Error, al sacar los caracteres de la palabra. #0", e.getMessage());
		}
		return nombre;
		
	}
	public  String limpiarVolver(){
		setMensajeGlogal(null);
		limpiaMensajeGlobal();
		limpiarDatos();
		limpiarDespuesGenerar();
		return "generarSolicitud";
	}
	/*
 	* es una cadena o frase de palabras donde todoas las palabras que contengan mas 1 caracter la primera 
 	* la de en maytusculas y las demas en minusculas.
    */
	public String modificarTexto(String cadenaAux){
		StringBuffer cadenaBuffer = new StringBuffer();
		String[] splitRazonSocial = cadenaAux.split(" ");
		for(String cadena : splitRazonSocial){
			if(cadena.length() > 1 && !cadena.equals("S.A.")){
				cadenaBuffer.append(capitalizarTexto(cadena));
				cadenaBuffer.append(" ");
			}else{
				cadenaBuffer.append(cadena);
				cadenaBuffer.append(" ");
			}
		}
		return cadenaBuffer.toString();
	}
	
	/*
	 * se sacan os caracteres especiales antes de transformar la cadena de string
	 * **/
    public String capitalizarTexto(String textoSinFormato){
        String []palabras = textoSinFormato.split("\\s+");
        StringBuilder textoFormateado = new StringBuilder();
        
        for(String palabra : palabras){
            textoFormateado.append(palabra.substring(0,1).toUpperCase()
        	    	.concat( palabra.substring(1,palabra.length())
        		.toLowerCase()).concat(" "));
        }
        
        return textoFormateado.toString();        
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
												 log.debug(monto);
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
	
	
	/*gets y sets*/
    
	public void select(ActionEvent event) {
		this.setSelectedMenuItem(event.getComponent().getId());
	}
	public String getSelectedMenuItem() {
		return selectedMenuItem;
	}
	public void setSelectedMenuItem(String selectedMenuItem) {
		this.selectedMenuItem = selectedMenuItem;
	}
	public Map<String, Boolean> getMenu() {
		return menu;
	}
	public void setMenu(Map<String, Boolean> menu) {
		this.menu = menu;
	}
	public String getRutAux() {
		return rutAux;
	}
	public void setRutAux(String rutAux) {
		this.rutAux = rutAux;
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
	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public FormaPagoDTO getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPagoDTO formaPago) {
		this.formaPago = formaPago;
	}

	public List<FormaPagoDTO> getListaFormaPagoDTOs() {
		return listaFormaPagoDTOs;
	}

	public void setListaFormaPagoDTOs(List<FormaPagoDTO> listaFormaPagoDTOs) {
		this.listaFormaPagoDTOs = listaFormaPagoDTOs;
	}
	public List<ConceptoNegocioDTO> getListaProductosagregados() {
		return listaProductosagregados;
	}

	public void setListaProductosagregados(List<ConceptoNegocioDTO> listaProductosagregados) {
		this.listaProductosagregados = listaProductosagregados;
	}
	public List<String> getListaDescripcionProductos() {
		return listaDescripcionProductos;
	}

	public void setListaDescripcionProductos(List<String> listaDescripcionProductos) {
		this.listaDescripcionProductos = listaDescripcionProductos;
	}
	public String getMensajeExplicativo() {
		return mensajeExplicativo;
	}

	public void setMensajeExplicativo(String mensajeExplicativo) {
		this.mensajeExplicativo = mensajeExplicativo;
	}
	public List<String> getListaDescripcionUsuariosCorreos() {
		return listaDescripcionUsuariosCorreos;
	}

	public void setListaDescripcionUsuariosCorreos(
			List<String> listaDescripcionUsuariosCorreos) {
		this.listaDescripcionUsuariosCorreos = listaDescripcionUsuariosCorreos;
	}

	public List<UsuarioCorreoDTO> getListaUsuarioCorreos() {
		return listaUsuarioCorreos;
	}

	public void setListaUsuarioCorreos(List<UsuarioCorreoDTO> listaUsuarioCorreos) {
		this.listaUsuarioCorreos = listaUsuarioCorreos;
	}
	public String getUsuarioCorreo() {
		return usuarioCorreo;
	}

	public void setUsuarioCorreo(String usuarioCorreo) {
		this.usuarioCorreo = usuarioCorreo;
	}
	public List<SolicitudUsuarioCorreo> getListaUsuarioCorreoagregados() {
		
		return listaUsuarioCorreoagregados;
	}

	public void setListaUsuarioCorreoagregados(
			List<SolicitudUsuarioCorreo> listaUsuarioCorreoagregados) {
		this.listaUsuarioCorreoagregados = listaUsuarioCorreoagregados;
	}	
	public List<FileUploadedDTO> getListaFileUploadedDTOs() {
		return listaFileUploadedDTOs;
	}

	public void setListaFileUploadedDTOs(List<FileUploadedDTO> listaFileUploadedDTOs) {
		this.listaFileUploadedDTOs = listaFileUploadedDTOs;
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
	public boolean isMotivoBloqueoDesbloqueo() {
		return motivoBloqueoDesbloqueo;
	}

	public void setMotivoBloqueoDesbloqueo(boolean motivoBloqueoDesbloqueo) {
		this.motivoBloqueoDesbloqueo = motivoBloqueoDesbloqueo;
	}
	public LcredSolicitudDm getClienteDM() {
		return clienteDM;
	}

	public void setClienteDM(LcredSolicitudDm clienteDM) {
		this.clienteDM = clienteDM;
	}
	
	public List<LcredSolicitudDm> getListaClienteDMs() {
		return listaClienteDMs;
	}

	public void setListaClienteDMs(List<LcredSolicitudDm> listaClienteDMs) {
		this.listaClienteDMs = listaClienteDMs;
	}	
	public List<DocumentoProrrogaDTO> getListaDoctoProrroga() {
		return listaDoctoProrroga;
	}

	public void setListaDoctoProrroga(List<DocumentoProrrogaDTO> listaDoctoProrroga) {
		this.listaDoctoProrroga = listaDoctoProrroga;
	}

	public List<DocumentoProrrogaDTO> getListaDoctoProrrogaAgregados() {
		return listaDoctoProrrogaAgregados;
	}

	public void setListaDoctoProrrogaAgregados(
			List<DocumentoProrrogaDTO> listaDoctoProrrogaAgregados) {
		this.listaDoctoProrrogaAgregados = listaDoctoProrrogaAgregados;
	}
	public List<DocumentoProrrogaDTO> getListaDoctoCuentaCorriente() {
		return listaDoctoCuentaCorriente;
	}

	public void setListaDoctoCuentaCorriente(
			List<DocumentoProrrogaDTO> listaDoctoCuentaCorriente) {
		this.listaDoctoCuentaCorriente = listaDoctoCuentaCorriente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public List<ConceptoMontoDTO> getListaConceptosMontos() {
		return listaConceptosMontos;
	}

	public void setListaConceptosMontos(List<ConceptoMontoDTO> listaConceptosMontos) {
		this.listaConceptosMontos = listaConceptosMontos;
	}
	public boolean isCuadradoMarcado() {
		return cuadradoMarcado;
	}

	public void setCuadradoMarcado(boolean cuadradoMarcado) {
		this.cuadradoMarcado = cuadradoMarcado;
	}
	
	public long getMontoGeneralConceptoMonto() {
		return montoGeneralConceptoMonto;
	}

	public void setMontoGeneralConceptoMonto(long montoGeneralConceptoMonto) {
		this.montoGeneralConceptoMonto = montoGeneralConceptoMonto;
	}
	
	

	public String getDm() {
		return dm;
	}

	public void setDm(String dm) {
		this.dm = dm;
	}

	public List<String> getListaDescripcionDM() {
		return listaDescripcionDM;
	}

	public void setListaDescripcionDM(List<String> listaDescripcionDM) {
		this.listaDescripcionDM = listaDescripcionDM;
	}

	public List<DMDTO> getListaDmdtos() {
		return listaDmdtos;
	}

	public void setListaDmdtos(List<DMDTO> listaDmdtos) {
		this.listaDmdtos = listaDmdtos;
	}

	public List<ConceptoMontoDTO> getListaConceptosIncluidos() {
		return listaConceptosIncluidos;
	}

	public void setListaConceptosIncluidos(
			List<ConceptoMontoDTO> listaConceptosIncluidos) {
		this.listaConceptosIncluidos = listaConceptosIncluidos;
	}

	public String getTituloConceptoMonto() {
		return tituloConceptoMonto;
	}

	public void setTituloConceptoMonto(String tituloConceptoMonto) {
		this.tituloConceptoMonto = tituloConceptoMonto;
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

	public void setLcredUsuarioNivelEnc(LcredUsuarioNivelEnc lcredUsuarioNivelEnc) {
		this.lcredUsuarioNivelEnc = lcredUsuarioNivelEnc;
	}

	public boolean isHabilitarCtaCte() {
		return habilitarCtaCte;
	}

	public void setHabilitarCtaCte(boolean habilitarCtaCte) {
		this.habilitarCtaCte = habilitarCtaCte;
	}

	public LcredEstado getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoInicial(LcredEstado estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	public TipoCuentasKupferType getCanalVenta() {
		return canalVenta;
	}

	public void setCanalVenta(TipoCuentasKupferType canalVenta) {
		this.canalVenta = canalVenta;
	}

	public Long getMonto() {
		return monto;
	}

	public void setMonto(Long monto) {
		this.monto = monto;
	}

	public Long getPie() {
		return pie;
	}

	public void setPie(Long pie) {
		this.pie = pie;
	}
	
	
	@BypassInterceptors public Long getPedidoCotizacion() {
		return pedidoCotizacion;
	}
	@BypassInterceptors public void setPedidoCotizacion(Long pedidoCotizacion) {
		this.pedidoCotizacion = pedidoCotizacion;
	}
	public boolean isHabilitarVerProductos() {
		return habilitarVerProductos;
	}
	public void setHabilitarVerProductos(boolean habilitarVerProductos) {
		this.habilitarVerProductos = habilitarVerProductos;
	}
	public FormaPagoEncabezado getEncabezado() {
		return encabezado;
	}
	public void setEncabezado(FormaPagoEncabezado encabezado) {
		this.encabezado = encabezado;
	}
	public TipoFormaPago getTipoFormaPago() {
		return tipoFormaPago;
	}
	public void setTipoFormaPago(TipoFormaPago tipoFormaPago) {
		this.tipoFormaPago = tipoFormaPago;
	}
	public List<TipoFormaPago> getListaTipoFormaPagos() {
		return listaTipoFormaPagos;
	}
	public void setListaTipoFormaPagos(List<TipoFormaPago> listaTipoFormaPagos) {
		this.listaTipoFormaPagos = listaTipoFormaPagos;
	}
	public List<FormaPagoEncabezado> getListaEncabezados() {
		return listaEncabezados;
	}
	public void setListaEncabezados(List<FormaPagoEncabezado> listaEncabezados) {
		this.listaEncabezados = listaEncabezados;
	}
	public boolean isMuestraSeleccionSolicitud() {
		return muestraSeleccionSolicitud;
	}
	public void setMuestraSeleccionSolicitud(boolean muestraSeleccionSolicitud) {
		this.muestraSeleccionSolicitud = muestraSeleccionSolicitud;
	}

	public List<CotizacionPedido> getCotizacionPedidosList() {
		return cotizacionPedidosList;
	}
	public void setCotizacionPedidosList(
			List<CotizacionPedido> cotizacionPedidosList) {
		this.cotizacionPedidosList = cotizacionPedidosList;
	}
	public List<CabeceraCotPedDTO> getListaCabeceraCotPeds() {
		return listaCabeceraCotPeds;
	}
	public void setListaCabeceraCotPeds(List<CabeceraCotPedDTO> listaCabeceraCotPeds) {
		this.listaCabeceraCotPeds = listaCabeceraCotPeds;
	}
	public Double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}
	public boolean isHabilitarBotonAgregarSolicitud() {
		return habilitarBotonAgregarSolicitud;
	}
	public void setHabilitarBotonAgregarSolicitud(
			boolean habilitarBotonAgregarSolicitud) {
		this.habilitarBotonAgregarSolicitud = habilitarBotonAgregarSolicitud;
	}
	public Double getMargenGlobal() {
		return margenGlobal;
	}
	public void setMargenGlobal(Double margenGlobal) {
		this.margenGlobal = margenGlobal;
	}
	@BypassInterceptors public String getMotivoCompra() {
		return motivoCompra;
	}
	@BypassInterceptors public void setMotivoCompra(String motivoCompra) {
		this.motivoCompra = motivoCompra;
	}
	@BypassInterceptors public String getObservaciones() {
		return observaciones;
	}
	@BypassInterceptors public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public LcredSolicitud getSolicitud() {
		return solicitud;
	}
	public void setSolicitud(LcredSolicitud solicitud) {
		this.solicitud = solicitud;
	}
	public Double getMontoTotalNeto() {
		return montoTotalNeto;
	}
	public void setMontoTotalNeto(Double montoTotalNeto) {
		this.montoTotalNeto = montoTotalNeto;
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
	public List<CotPedDTO> getListaCotPedDTOs() {
		return listaCotPedDTOs;
	}
	public void setListaCotPedDTOs(List<CotPedDTO> listaCotPedDTOs) {
		this.listaCotPedDTOs = listaCotPedDTOs;
	}
	@BypassInterceptors public Date getFechaActualCtaCte() {
		return fechaActualCtaCte;
	}
	@BypassInterceptors public void setFechaActualCtaCte(Date fechaActualCtaCte) {
		this.fechaActualCtaCte = fechaActualCtaCte;
	}
	public Double getPorcentajeGlobal() {
		return porcentajeGlobal;
	}
	public void setPorcentajeGlobal(Double porcentajeGlobal) {
		this.porcentajeGlobal = porcentajeGlobal;
	}
	public boolean isEvaluadorCompraVC() {
		return evaluadorCompraVC;
	}
	public void setEvaluadorCompraVC(boolean evaluadorCompraVC) {
		this.evaluadorCompraVC = evaluadorCompraVC;
	}
	public boolean isActivarCboClsRiesgo() {
		return activarCboClsRiesgo;
	}
	public void setActivarCboClsRiesgo(boolean activarCboClsRiesgo) {
		this.activarCboClsRiesgo = activarCboClsRiesgo;
	}
	public boolean isActivarCboCdnPago() {
		return activarCboCdnPago;
	}
	public void setActivarCboCdnPago(boolean activarCboCdnPago) {
		this.activarCboCdnPago = activarCboCdnPago;
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
	public String getConceptoNegocios() {
		return conceptoNegocios;
	}
	public void setConceptoNegocios(String conceptoNegocios) {
		this.conceptoNegocios = conceptoNegocios;
	}
	public Long getPotencialCompra() {
		return potencialCompra;
	}
	public void setPotencialCompra(Long potencialCompra) {
		this.potencialCompra = potencialCompra;
	}
	public List<ConceptosNegocio> getListaCboConceptoNegocio() {
		return listaCboConceptoNegocio;
	}
	public void setListaCboConceptoNegocio(
			List<ConceptosNegocio> listaCboConceptoNegocio) {
		this.listaCboConceptoNegocio = listaCboConceptoNegocio;
	}
	public List<ConceptoNegocioDTO> getListaSelConceptoNegocio() {
		return listaSelConceptoNegocio;
	}
	public void setListaSelConceptoNegocio(
			List<ConceptoNegocioDTO> listaSelConceptoNegocio) {
		this.listaSelConceptoNegocio = listaSelConceptoNegocio;
	}
	@BypassInterceptors public ConceptosNegocio getConceptoNegocioCbo() {
		return conceptoNegocioCbo;
	}
	@BypassInterceptors public void setConceptoNegocioCbo(ConceptosNegocio conceptoNegocioCbo) {
		this.conceptoNegocioCbo = conceptoNegocioCbo;
	}
	@BypassInterceptors public String getSocio() {
		return socio;
	}
	@BypassInterceptors public void setSocio(String socio) {
		this.socio = socio;
	}
	public List<String> getListaSocios() {
		return listaSocios;
	}
	public void setListaSocios(List<String> listaSocios) {
		this.listaSocios = listaSocios;
	}
	@BypassInterceptors public String getRutSocio() {
		return rutSocio;
	}
	@BypassInterceptors public void setRutSocio(String rutSocio) {
		this.rutSocio = rutSocio;
	}
	@BypassInterceptors public String getMotivoRiesgoPago() {
		return motivoRiesgoPago;
	}
	@BypassInterceptors public void setMotivoRiesgoPago(String motivoRiesgoPago) {
		this.motivoRiesgoPago = motivoRiesgoPago;
	}
	public boolean isSalirDelMensaje() {
		return salirDelMensaje;
	}
	public void setSalirDelMensaje(boolean salirDelMensaje) {
		this.salirDelMensaje = salirDelMensaje;
	}
	@BypassInterceptors public String getMotivoBloDesbloqueo() {
		return motivoBloDesbloqueo;
	}
	@BypassInterceptors public void setMotivoBloDesbloqueo(String motivoBloDesbloqueo) {
		this.motivoBloDesbloqueo = motivoBloDesbloqueo;
	}
	public List<String> getListaFechas() {
		return listaFechas;
	}
	public void setListaFechas(List<String> listaFechas) {
		this.listaFechas = listaFechas;
	}
	public String getFechaDeauda() {
		return fechaDeauda;
	}
	public void setFechaDeauda(String fechaDeauda) {
		this.fechaDeauda = fechaDeauda;
	}
	public String getMensajeGlogal() {
		return mensajeGlogal;
	}
	public void setMensajeGlogal(String mensajeGlogal) {
		this.mensajeGlogal = mensajeGlogal;
	}
	@BypassInterceptors public Long getMontoConcepto() {
		return montoConcepto;
	}
	@BypassInterceptors public void setMontoConcepto(Long montoConcepto) {
		this.montoConcepto = montoConcepto;
	}
	public String getTituloGeneral() {
		return tituloGeneral;
	}
	public void setTituloGeneral(String tituloGeneral) {
		this.tituloGeneral = tituloGeneral;
	}
	@BypassInterceptors public String getOpcionBloqueoDesbloqueo() {
		return opcionBloqueoDesbloqueo;
	}
	@BypassInterceptors public void setOpcionBloqueoDesbloqueo(String opcionBloqueoDesbloqueo) {
		this.opcionBloqueoDesbloqueo = opcionBloqueoDesbloqueo;
	}
	public String getMensajeCreacionDm() {
		return mensajeCreacionDm;
	}
	public void setMensajeCreacionDm(String mensajeCreacionDm) {
		this.mensajeCreacionDm = mensajeCreacionDm;
	}
	@BypassInterceptors public String getMotivoProrroga() {
		return motivoProrroga;
	}
	@BypassInterceptors public void setMotivoProrroga(String motivoProrroga) {
		this.motivoProrroga = motivoProrroga;
	}
	public List<SolicitudEstadoDTO> getListaSolicitudesEstados() {
		return listaSolicitudesEstados;
	}
	public void setListaSolicitudesEstados(
			List<SolicitudEstadoDTO> listaSolicitudesEstados) {
		this.listaSolicitudesEstados = listaSolicitudesEstados;
	}
	public List<SolicitudDTO> getListaSolicitudesPendientesAprobarDto() {
		return listaSolicitudesPendientesAprobarDto;
	}
	public void setListaSolicitudesPendientesAprobarDto(
			List<SolicitudDTO> listaSolicitudesPendientesAprobarDto) {
		this.listaSolicitudesPendientesAprobarDto = listaSolicitudesPendientesAprobarDto;
	}
	public List<SolicitudDTO> getListaSolicitudesPendientesAnalizarDto() {
		return listaSolicitudesPendientesAnalizarDto;
	}
	public void setListaSolicitudesPendientesAnalizarDto(
			List<SolicitudDTO> listaSolicitudesPendientesAnalizarDto) {
		this.listaSolicitudesPendientesAnalizarDto = listaSolicitudesPendientesAnalizarDto;
	}
	public List<SolicitudDTO> getListaMisSolicitudesDto() {
		return listaMisSolicitudesDto;
	}
	public void setListaMisSolicitudesDto(List<SolicitudDTO> listaMisSolicitudesDto) {
		this.listaMisSolicitudesDto = listaMisSolicitudesDto;
	}
	public List<SolicitudDTO> getListaTodasSolicitudesDto() {
		return listaTodasSolicitudesDto;
	}
	public void setListaTodasSolicitudesDto(
			List<SolicitudDTO> listaTodasSolicitudesDto) {
		this.listaTodasSolicitudesDto = listaTodasSolicitudesDto;
	}
	public List<LcredEstado> getListaEstados() {
		return listaEstados;
	}
	public void setListaEstados(List<LcredEstado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	private String tsListaAnalista;
	private String tsListaAprobacion;
	private String tsListaMisSolicitudes;
	private String tsListaTodasSolicitudes;
	
	public String getTsListaAnalista() {
		return tsListaAnalista;
	}
	
	public void setTsListaAnalista(String tsListaAnalista) {
		this.tsListaAnalista = tsListaAnalista;
	}

	public String getTsListaAprobacion() {
		return tsListaAprobacion;
	}

	public void setTsListaAprobacion(String tsListaAprobacion) {
		this.tsListaAprobacion = tsListaAprobacion;
	}

	public String getTsListaMisSolicitudes() {
		return tsListaMisSolicitudes;
	}

	public void setTsListaMisSolicitudes(String tsListaMisSolicitudes) {
		this.tsListaMisSolicitudes = tsListaMisSolicitudes;
	}

	public String getTsListaTodasSolicitudes() {
		return tsListaTodasSolicitudes;
	}

	public void setTsListaTodasSolicitudes(String tsListaTodasSolicitudes) {
		this.tsListaTodasSolicitudes = tsListaTodasSolicitudes;
	}
	public String getTiempoEjecucionProceso() {
		return tiempoEjecucionProceso;
	}
	public void setTiempoEjecucionProceso(String tiempoEjecucionProceso) {
		this.tiempoEjecucionProceso = tiempoEjecucionProceso;
	}
	public String getTiempoEjecucionMenu() {
		return tiempoEjecucionMenu;
	}
	public void setTiempoEjecucionMenu(String tiempoEjecucionMenu) {
		this.tiempoEjecucionMenu = tiempoEjecucionMenu;
	}
	public String getTiempoEjecucionModal() {
		return tiempoEjecucionModal;
	}
	public void setTiempoEjecucionModal(String tiempoEjecucionModal) {
		this.tiempoEjecucionModal = tiempoEjecucionModal;
	}
	public String getTiempoEjecucionValidar() {
		return tiempoEjecucionValidar;
	}
	public void setTiempoEjecucionValidar(String tiempoEjecucionValidar) {
		this.tiempoEjecucionValidar = tiempoEjecucionValidar;
	}
	public String getPaginaVolver() {
		return paginaVolver;
	}
	public void setPaginaVolver(String paginaVolver) {
		this.paginaVolver = paginaVolver;
	}
	public Long getSolicitudSeleccionada() {
		return solicitudSeleccionada;
	}
	public void setSolicitudSeleccionada(Long solicitudSeleccionada) {
		this.solicitudSeleccionada = solicitudSeleccionada;
	}
	public SolicitudTipoFormaPago getStfp() {
		return stfp;
	}
	public void setStfp(SolicitudTipoFormaPago stfp) {
		this.stfp = stfp;
	}
	public boolean isHabilitar() {
		return habilitar;
	}
	public void setHabilitar(boolean habilitar) {
		this.habilitar = habilitar;
	}
	public List<ConceptoDTO> getListaArchivoGuardado() {
		return listaArchivoGuardado;
	}
	public void setListaArchivoGuardado(List<ConceptoDTO> listaArchivoGuardado) {
		this.listaArchivoGuardado = listaArchivoGuardado;
	}
	public String getUsuarioEmisor() {
		return usuarioEmisor;
	}
	public void setUsuarioEmisor(String usuarioEmisor) {
		this.usuarioEmisor = usuarioEmisor;
	}
	public LcredTipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(LcredTipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	public String getObservacionesModel() {
		return observacionesModel;
	}
	public void setObservacionesModel(String observacionesModel) {
		this.observacionesModel = observacionesModel;
	}
	public List<LcredSolicitudObservaciones> getListaObservaciones() {
		return listaObservaciones;
	}
	public void setListaObservaciones(
			List<LcredSolicitudObservaciones> listaObservaciones) {
		this.listaObservaciones = listaObservaciones;
	}
	public List<SolicitudUsuarioCorreo> getListaSolicitudUsuarioCorresSessions() {
		return listaSolicitudUsuarioCorresSessions;
	}
	public void setListaSolicitudUsuarioCorresSessions(
			List<SolicitudUsuarioCorreo> listaSolicitudUsuarioCorresSessions) {
		this.listaSolicitudUsuarioCorresSessions = listaSolicitudUsuarioCorresSessions;
	}
	public List<ConceptosNegocio> getListaCboConceptoNegocioSeleccion() {
		return listaCboConceptoNegocioSeleccion;
	}
	public void setListaCboConceptoNegocioSeleccion(
			List<ConceptosNegocio> listaCboConceptoNegocioSeleccion) {
		this.listaCboConceptoNegocioSeleccion = listaCboConceptoNegocioSeleccion;
	}
	public String getTipoSolicitudCodigo() {
		return tipoSolicitudCodigo;
	}
	public void setTipoSolicitudCodigo(String tipoSolicitudCodigo) {
		this.tipoSolicitudCodigo = tipoSolicitudCodigo;
	}
	

	
}





