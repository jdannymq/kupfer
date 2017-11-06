package org.domain.sck.session.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.domain.sck.dto.IndicadoresDTO;
import org.domain.sck.dto.ProrrogaDTO;
import org.domain.sck.dto.SolicitudDTO;
import org.domain.sck.dto.SolicitudEstadoDTO;
import org.domain.sck.dto.UsuarioCorreoDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.entity.ArchivoAdjunto;
import org.domain.sck.entity.Attachment;
import org.domain.sck.entity.CanalUsuarioCargo;
import org.domain.sck.entity.ClienteSck;
import org.domain.sck.entity.Comuna;
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
import org.domain.sck.entity.LcredSolicitudId;
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
import org.domain.sck.entity.Provincia;
import org.domain.sck.entity.Region;
import org.domain.sck.entity.Sociedad;
import org.domain.sck.entity.SolicitudConceptosNegocioLC;
import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudLogs;
import org.domain.sck.entity.SolicitudTipoFormaPago;
import org.domain.sck.entity.SolicitudUsuarioCorreo;
import org.domain.sck.entity.SolicitudUsuarioDerivada;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.TipoFormaPago;
import org.domain.sck.entity.UfSolicitudTipo;
import org.domain.sck.entity.Ufs;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.Usuarios;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.entity.ZonaSucursalNegocioConcepto;
import org.domain.sck.entity.enums.ArchivoAdjuntoType;
import org.domain.sck.entity.enums.ConceptosType;
import org.domain.sck.entity.enums.EstadoEntityType;
import org.domain.sck.entity.enums.FuncionesType;
import org.domain.sck.entity.enums.TipoCuentasKupferType;
import org.domain.sck.entity.enums.TipoSolicitudType;
import org.domain.sck.seam.GlobalParameters;
import org.domain.sck.session.home.ContenidoEmailSolicitudDTO;
import org.domain.sck.session.home.EmailAlertaContactoMensajeHelper;
import org.domain.sck.session.service.intranetsap.IntranetSapService;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.aspects.asynchronous.aspects.jboss.Asynchronous;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.richfaces.model.UploadItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

import conexion.Connect;


@Name("generarSolicitudAction")
@Scope(ScopeType.CONVERSATION)
public class GenerarSolicitudAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5441094655893598910L;

	@Logger
	Log log;

	@In(value = "#{entityManager}")
	EntityManager entityManager;

	@In(value = "#{emailAlertaContactoMensajeHelper}")
	EmailAlertaContactoMensajeHelper emailAlerta;

	@In
	IntranetSapService intranetSapService;

	@In
	GlobalService globalService;

	@In
	ScoringService scoringService;

	@In
	GlobalHitosLogdService globalHitosLogdService;

	@In
	GlobalParameters globalParameters;

	@In(value = "#{identity.usuarioLogueado}", scope = ScopeType.SESSION)
	private Usuariosegur usuarioLogueado;

	@In(value = "#{identity.lcredUsuarioNivelEnc}", scope = ScopeType.SESSION)
	private LcredUsuarioNivelEnc lcredUsuarioNivelEnc;

	@In(value = "#{identity.usuarioSegur}", scope = ScopeType.SESSION)
	private UsuarioSegurDTO usuarioSegur;

	@In(value = "#{identity.usuarioCargo}", scope = ScopeType.SESSION)
	private UsuarioCargo usuarioCargoAux;

	/* variables de pagina */
	private LcredSolicitud solicitud;
	private Sucursal sucursal;
	public Map<String, Boolean> menu;
	private String selectedMenuItem;
	private int paginaIngreso;
	private int tipoSolicitudCodigo;
	private String opcionBloqueoDesbloqueo;
	private String tituloGeneral;
	private String tituloIngreso;
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
	private Long pedidoCotizacionLiberar;
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
	private String mensajeCreacionDm;
	private String motivoProrroga;
	private String tiempoEjecucionProceso;
	private String tiempoEjecucionMenu;
	private String tiempoEjecucionModal;
	private String tiempoEjecucionValidar;
	private Calendar cal1;
	private Calendar cal2;
	private boolean habilitar;
	private List<SolicitudUsuarioDerivada> listUsuariosDerivados;
	private SolicitudDTO solicitudAux;
	private SolicitudHitos solicitudHitos;
	private Long montoActualCuentaCorriente;
	private String negociosCambioSucursal;
	private String conceptoCambioSucursal;
	private Sociedad sociedadAux;
	private boolean habilitarCboSociedad;

	/* lista de paginas */
	private List<Sucursal> listaSucursales;
	private List<Sociedad> listaSociedadesPestTodas;
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
	private Long riesgoKupferCalculado;
	private List<ConceptosNegocio> conceptosNegociosSessions;
	private List<Sociedad> listaSociedades;
	
	private List<SolicitudDTO> listaSolicitudesPendientesAnalizarDto;
	private List<SolicitudDTO> listaMisSolicitudesDto;
	private List<SolicitudDTO> listaTodasSolicitudesDto;
	private List<LcredEstado> listaEstados;
	private List<SolicitudDTO> listaSolicitudesDerivadasForOtros;

	private List<String> listaUsuarios;
	private List<String> listaUsuariosPendientes;
	private List<String> listaUsuariosDerivadas;
	private String usuarioFilter;

	private List<String> listaEstadoString;
	private List<String> listaEstadoPendienteString;
	private List<String> listaEstadoMisSolicitudesString;
	private List<String> listaEstadoDerivadas;
	private String estadoFilter;

	private List<String> listaCanalString;
	private List<String> listaCanalPendienteString;
	private List<String> listaCanalMisSolicitudesString;
	private List<String> listaCanalDerivadas;
	private String canalFilter;

	private List<String> listaProcesoString;
	private List<String> listaProcesoPendienteString;
	private List<String> listaProcesoDerivadas;
	private String procesoFilter;

	private List<String> listaTiposSolicitudes;
	private List<String> listaTiposSolicitudesPendientes;
	private List<String> listaTiposMisSolicitudesString;
	private List<String> listaTiposDerivadas;
	private String tipoFilter;

	private List<String> listaSucursalSolicitudes;
	private List<String> listaSucursalSolicitudesPendientes;
	private List<String> listaSucursalMisSolicitudesString;
	private List<String> listaSucursalDerivadas;
	private String sucursalFilter;

	private List<CanalUsuarioCargo> listaCanalUsuarioCargos;
	private List<PerfilFuncionCanal> listaPerfilFuncionCanals;
	private List<ZonaSucursalNegocioConcepto> listaZonaSucursalNegocioConceptos;

	private String[] arrayCanales;

	private List<ConceptoNegocioDTO> listaProductosagregados = new ArrayList<ConceptoNegocioDTO>(0);
	private List<UsuarioCorreoDTO> listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
	private List<FileUploadedDTO> listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
	private List<DocumentoProrrogaDTO> listaDoctoProrrogaAgregados = new ArrayList<DocumentoProrrogaDTO>(0);
	private List<CotizacionPedido> cotizacionPedidosList = new ArrayList<CotizacionPedido>(0);

	/* Funciones Sap */
	private JCoFunction functionDatosClientes;
	private JCoFunction functionCtaCte;

	/* DTO que recibe datos de cliente desde Sap */
	ClienteDTO clienteTarget;

	public ClienteDTO getClienteTarget() {
		return clienteTarget;
	}

	CompComercialDTO clienteCompComrcial;

	public CompComercialDTO getClienteCompComrcial() {
		return clienteCompComrcial;
	}

	ExpoRiesgoKhDTO clienteExpoRiesgoKh;

	public ExpoRiesgoKhDTO getClienteExpoRiesgoKh() {
		return clienteExpoRiesgoKh;
	}

	public void setClienteExpoRiesgoKh(ExpoRiesgoKhDTO obj) {
		clienteExpoRiesgoKh = obj;
	}

	private String rutAux;
	private String nombreCliente;
	private boolean cuadradoMarcado;
	private String tituloConceptoMonto;
	private boolean habilitarBotonAgregarSolicitud = true;
	private String rutAuxSinGuion;

	/* variables de Ventas */
	private long montoGeneralConceptoMonto;

	/* variables de Crédito */
	private String descripcionProyecto;
	private Long montoCredito;
	private String plazoEjecucion;
	private Long potencialCompra;
	private String conceptoNegocios;
	private String rutNombresSocios;
	private String observacionesCredito;

	/* variables condiciones */
	private String motivoRiesgoPago;

	/* variables otras solicitudes */
	private LcredSolicitudDm clienteDM;
	private String motivoBloDesbloqueo;
	private String nombrePestana;

	/* varibales de pestaña */
	private boolean pestanaGenerarSolicitudes;
	private boolean pestanaPendientes;
	private boolean pestanaMisSolicitudes;
	private boolean pestanaTodas;
	private boolean pestanaDerivadas;

	/* variables de creaar cliente */
	private List<Region> listaRegiones;
	private List<Provincia> listaProvincias;
	private List<Comuna> listaComunas;

	private Region region;
	private Provincia provincia;
	private Comuna comuna;
	private ClienteSck clienteSck;

	/* variables por pasar y metodos por cublir la pestaña de todas */
	private Long idSolicitudPestTodas;
	private String rutAuxPestTodas;
	private LcredEstado estadoPestTodas;
	private Sucursal sucursalPestTodas;
	private Date fechaInicalPestTodas;
	private Date fechaFinalPestTodas;
	private Sociedad sociedadPestTodas;

	/* Variable para enviar correo si es administrador */
	private Boolean wvarEnviaCorreo = false;
	private String observacionesAutirzador;
	private Boolean habilitaBotonApruebaToRechazarAdmin;

	@RequestParameter("tabGrilla")
	String tabGrilla;

	@SuppressWarnings("unchecked")
	@Create
	public void init() {
		List<Sucursal> listaSucur = null;
		try {
			setTituloIngreso("Titulo Segun Selección");
			setTituloGeneral("Titulo Segun Selección");
			listaSucur = (List<Sucursal>) entityManager
					.createQuery(
							"select suc from Sucursal suc "
									+ " where suc.codigo in ('C212','C246','N103','N206','S133','S148','S224','C115',"
									+ "                      'C227','N101','N142','N209','S118','S145','S221','S243','C138') order by suc.descripcion asc ")
					.getResultList();

			if (listaSucur != null) {
				setListaSucursales(listaSucur);
				setListaSociedades(scoringService.getSociedades(usuarioLogueado));
			}
			setHabilitarCtaCte(false);
			setSucursal(null);
			sacarEstadosSolicitudes();
			listaCondicionPagos = intranetSapService.getCondicionPago();
			if (usuarioSegur != null) {
				usuarioCargoAux = scoringService.getUsuarioCargo(usuarioSegur.getIdPersonal());
				if (usuarioCargoAux != null) {
					listaCanalUsuarioCargos = scoringService.getListaCanalUsuarioCargo(usuarioCargoAux.getIdUsuarioCargo());
					if (listaCanalUsuarioCargos != null) {
						StringBuffer cadenaCanales = new StringBuffer();
						long codigo = 0;
						for (CanalUsuarioCargo cuc : listaCanalUsuarioCargos) {
							cadenaCanales.append(cuc.getTipoCuenta().getNombre());
							cadenaCanales.append(",");
							listaPerfilFuncionCanals = scoringService.getListaPerfilFuncionCanal(cuc.getIdCanalUsuarioCargo(),listaPerfilFuncionCanals);
							if (listaPerfilFuncionCanals != null) {
								for (PerfilFuncionCanal pfc : listaPerfilFuncionCanals) {
									if (codigo != pfc.getIdPerfilFuncionCanal()) {
										listaZonaSucursalNegocioConceptos = scoringService.getListaZonaSucursalNegocioConcepto(pfc.getIdPerfilFuncionCanal(),
																																listaZonaSucursalNegocioConceptos);
										codigo = pfc.getIdPerfilFuncionCanal();
									}
								}

							}
						}
						arrayCanales = (cadenaCanales.toString()).split(",");

					}
					
				}
			}

			if (usuarioSegur != null && (usuarioSegur.getAlias().toLowerCase()).equals("administrador")) {
				this.pestanaTodas = true;
				tabGrilla = "todas";
			} else {
				if (listaCanalUsuarioCargos != null	&& listaCanalUsuarioCargos.size() > 0) {
					this.pestanaGenerarSolicitudes = true;
					this.pestanaPendientes = true;
					this.pestanaMisSolicitudes = true;
					this.pestanaTodas = true;
					this.pestanaDerivadas = true;
				} else {
					this.pestanaGenerarSolicitudes = true;
					this.pestanaPendientes = false;
					this.pestanaMisSolicitudes = true;
					this.pestanaTodas = false;
					this.pestanaDerivadas = false;
				}
			}
		} catch (Exception e) {
			log.error("Error, obtener la lista de sucursales. #0", e.getMessage());
		}

		/* genera el menu de la cracion de las solicitudes. */
		if (usuarioSegur != null && (usuarioSegur.getAlias().toLowerCase()).equals("administrador")) {
			log.debug("Administrador no tiene que generar codigo del menu");
		} else {
			generarMenu();
		}

		/* fecha Actual */
		setFechaActual(new Date());

		if (usuarioSegur != null) {
			Sucursal suc = scoringService.obtenerSucursalForCodigo(usuarioSegur.getCodigosucursal());
			if (suc != null) {
				this.setSucursal(suc);
			}
		}

		/* sacar concepto y negocio */
		setListaCboConceptoNegocio(scoringService.obtenerConceptosNegocios());
		conceptosNegociosSessions = scoringService
				.obtenerListaConceptoNegocio();

		listaSolicitudesPendientesAnalizarDto = new ArrayList<SolicitudDTO>(0);
		listaMisSolicitudesDto = new ArrayList<SolicitudDTO>(0);
		//sacarLasSolicitudesAprobarAnalizarMisSolicitudesTodas();

		limpiarTiempoEjecuciones();
		this.setHabilitar(false);

		if (usuarioSegur != null && (usuarioSegur.getAlias().toLowerCase()).equals("administrador")) {
			if (tabGrilla == null || "".equals(tabGrilla)) {
				this.setNombrePestana("crecion");
			} else {
				if ("pendiente".equals(tabGrilla)) {
					this.setNombrePestana(tabGrilla);
					sacarSolicitudAnalizarAprobar();

				} else if ("misSolicitudes".equals(tabGrilla)) {
					this.setNombrePestana(tabGrilla);
					sacarMisSolicitudes();

				} else if ("todas".equals(tabGrilla)) {
					this.setNombrePestana(tabGrilla);
					sacarListasTodasSolicitudes("INICIAL");
				}
			}
		} else {
			if (tabGrilla == null || "".equals(tabGrilla)) {
				this.setNombrePestana("crecion");
			} else {
				if ("pendiente".equals(tabGrilla)) {
					this.setNombrePestana(tabGrilla);
					sacarSolicitudAnalizarAprobar();

				} else if ("misSolicitudes".equals(tabGrilla)) {
					this.setNombrePestana(tabGrilla);
					sacarMisSolicitudes();

				} else if ("todas".equals(tabGrilla)) {
					this.setNombrePestana(tabGrilla);
					sacarListasTodasSolicitudes("INICIAL");
				}
			}
		}

		/* sacar los conceptos de general */
		obtenerListaConceptoNegocioMarca();
		
		if(this.getListaSociedades() != null && this.getListaSociedades().size() > 0){
			this.getSetarSociedadAsignada(this.usuarioSegur.getIdSociedad());
		}
		this.setListaSociedadesPestTodas(this.getListaSociedades());
	}

	
	
	
	@RequestParameter("tabGrillaRetorno")
	String tabGrillaRetorno;
	@RequestParameter("solicitudSeleccionada")
	Long solicitudSeleccionada;
	@RequestParameter("pagina")
	String pagina;

	public void retorno() {
		try {
			if (pagina != null && pagina.equals("Resp")) {
				if (solicitudSeleccionada != null) {
					solicitud = (LcredSolicitud) entityManager.createQuery("select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud")
												.setParameter("numSolicitud", solicitudSeleccionada)
												.getSingleResult();

					if (solicitud.getEstado().equals("I")) {
						insertarLogs(solicitud, 3);
						entityManager.createQuery("update LcredSolicitud set evaluar=0, usuarioActual=null  where id.numSolicitud=:sol").setParameter("sol",solicitud.getId().getNumSolicitud())
												.executeUpdate();
						entityManager.flush();
					} else {
						insertarLogs(solicitud, 4);
						entityManager.createQuery("update LcredSolicitud set evaluar=0 where id.numSolicitud=:sol").setParameter("sol",	solicitud.getId().getNumSolicitud())
								.executeUpdate();
						entityManager.flush();
					}
				}

			} else if (pagina != null && pagina.equals("Ver")) {
				if (solicitudSeleccionada != null) {
					solicitud = (LcredSolicitud) entityManager.createQuery("select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud")
							.setParameter("numSolicitud", solicitudSeleccionada).getSingleResult();

					if (solicitud.getEstado().equals("I")) {
						insertarLogs(solicitud, 3);
						entityManager.createQuery("update LcredSolicitud set evaluar=0, usuarioActual=null  where id.numSolicitud=:sol").setParameter("sol",
										solicitud.getId().getNumSolicitud()).executeUpdate();
						entityManager.flush();
					} else if (solicitud.getEstado().equals("E") || solicitud.getEstado().equals("N")) {
						insertarLogs(solicitud, 3);
						entityManager.createQuery("update LcredSolicitud set evaluar=1 where id.numSolicitud=:sol").setParameter("sol",solicitud.getId().getNumSolicitud())
								.executeUpdate();
						entityManager.flush();
					} else {
						insertarLogs(solicitud, 4);
						entityManager.createQuery("update LcredSolicitud set evaluar=0 where id.numSolicitud=:sol")	.setParameter("sol",solicitud.getId().getNumSolicitud())
								.executeUpdate();
						entityManager.flush();
					}
				}
			}
			log.debug("parametro :#0 ", tabGrilla);

		} catch (Exception e) {
			log.error("error al devolverse y cambiar el estado de la evalucion. #0",e.getMessage());
		}

		if (tabGrillaRetorno == null || "".equals(tabGrillaRetorno)) {
			this.setNombrePestana("crecion");
		} else {
			if ("pendiente".equals(tabGrillaRetorno)) {
				this.setNombrePestana(tabGrillaRetorno);
				// sacarLasSolicitudesAprobarAnalizarMisSolicitudesTodas();
			} else if ("misSolicitudes".equals(tabGrillaRetorno)) {
				this.setNombrePestana(tabGrillaRetorno);
				// sacarLasSolicitudesAprobarAnalizarMisSolicitudesTodas();
			} else if ("todas".equals(tabGrillaRetorno)) {
				this.setNombrePestana(tabGrillaRetorno);
				// sacarLasSolicitudesAprobarAnalizarMisSolicitudesTodas();
			} else if ("derivadas".equals(tabGrillaRetorno)) {
				this.setNombrePestana(tabGrillaRetorno);
				// sacarLasSolicitudesAprobarAnalizarMisSolicitudesTodas();
			}
		}
	}

	/* metodos trabajados */
	public void limpiarTiempoEjecuciones() {
		this.setTiempoEjecucionMenu(null);
		this.setTiempoEjecucionModal(null);
		this.setTiempoEjecucionProceso(null);
		this.setTiempoEjecucionValidar(null);
	}

	public void sacarLasSolicitudesAprobarAnalizarMisSolicitudesTodas() {
		if (usuarioSegur != null && (usuarioSegur.getAlias().toLowerCase()).equals("administrador")) {
			// sacarListasTodasSolicitudes();
		} else {
			if (listaCanalUsuarioCargos != null	&& listaCanalUsuarioCargos.size() > 0) {
				sacarSolicitudAnalizarAprobar();
				// sacarMisSolicitudes();
				sacarSolicitudDerivadasForOtros();
				// sacarListasTodasSolicitudes();
			} else {
				sacarMisSolicitudes();
			}
		}

	}

	public void obtenerDMCliente() {
		if (this.dm != null) {
			log.error(" el dm del cliente es #0", this.dm);
		}
	}

	public void obtenerFechaDeuda() {
		if (this.fechaDeauda != null) {
			log.error(" la fechas seleccionada es  #0", this.fechaDeauda);
		}
	}

	public void obtenerListaConceptoNegocioMarca() {
		try {
			List<ConceptosNegocio> listaConceptoNegocio = scoringService
					.obtenerListaConceptoNegocio();
			if (listaConceptoNegocio != null) {
				listaConceptosMontos = new ArrayList<ConceptoMontoDTO>();
				ConceptoMontoDTO conceptoMonto = null;
				for (ConceptosNegocio cn : listaConceptoNegocio) {
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
		} catch (Exception e) {
			log.error("Error, al sacar lista de los conceptos #0",
					e.getMessage());
		}
	}

	public void obtenerCondicionRiesgo() {
		if (this.clsRiesgo != null) {
			log.error("El clasificacion de riesgo es #0", this.clsRiesgo);
			if (this.paginaIngreso == 31) {

				if (tipoSolicitud != null && (listaUsuarioCorreoagregados == null || listaUsuarioCorreoagregados.size() == 0)) {
					if (listaDestinatarios != null) {
						listaDestinatarios.clear();
					}

					/* va a buscar los destinatarios de las solictudes */
					try {
						listaDestinatarios = scoringService.obtenerListaDestinatario(String.valueOf(this.tipoSolicitudCodigo),
																					 String.valueOf(clienteTarget.getCodigoCanal().ordinal()),
																					 sucursal.getZona(),
																					 sucursal.getCodigo(), 
																					 "", 
																					 "", 
																					 "1");
						if (listaDestinatarios != null) {
							listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
							listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
							listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
							// for(DestinatarioDTO dto : listaDestinatarios){
							// if(dto != null){
							// log.debug("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
							// ,dto.getTipoSolicitud(), dto.getUsername(),
							// dto.getZona(),
							// dto.getSucursal(), dto.getConcepto(),
							// dto.getNegocio() );
							//
							// }
							// }
							agregarAlaListaCorreo();
						}
					} catch (Exception e) {
						log.error("Error, sacar los destinatarios : #0",
								e.getMessage());
					}

				}

			}
		}
	}

	public void obtenerCondicionPago() {
		if (this.cPago != null) {
			log.error("El condicion de pago es #0", this.cPago);
			if (this.paginaIngreso == 31) {
				if (tipoSolicitud != null) {

					if (tipoSolicitud != null
							&& (listaUsuarioCorreoagregados == null || listaUsuarioCorreoagregados
									.size() == 0)) {
						if (listaDestinatarios != null) {
							listaDestinatarios.clear();
						}

						/* va a buscar los destinatarios de las solictudes */
						try {
							listaDestinatarios = scoringService
									.obtenerListaDestinatario(
											String.valueOf(this.tipoSolicitudCodigo),
											String.valueOf(clienteTarget
													.getCodigoCanal().ordinal()),
											sucursal.getZona(), sucursal
													.getCodigo(), "", "","1");
							if (listaDestinatarios != null) {
								listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(
										0);
								listaDescripcionUsuariosCorreos = new ArrayList<String>(
										0);
								listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(
										0);
								for (DestinatarioDTO dto : listaDestinatarios) {
									if (dto != null) {
										// log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
										// ,dto.getTipoSolicitud(),
										// dto.getUsername(), dto.getZona(),
										// dto.getSucursal(), dto.getConcepto(),
										// dto.getNegocio() );

									}
								}
								agregarAlaListaCorreo();
							}
						} catch (Exception e) {
							log.error("Error, sacar los destinatarios : #0",
									e.getMessage());
						}

					}

				}

			}
		}
	}

	public void obtenerMontoProyecto() {
		if (this.montoCredito != null) {
			log.error("monto credito #0", this.montoCredito);
		}
		if (this.potencialCompra != null) {
			log.error("potencial compra  #0", this.potencialCompra);

		}
	}

	public void obtenerMontoCredito() {
		try {
			log.debug("Solo para presentación");
			obtenerMontoCredito("", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void obtenerMontoCredito(String negocios, String concepto) {
		if (this.monto != null) {
			log.error("monto  #0", this.monto);

			if (this.paginaIngreso == 21) {
				UfSolicitudTipo ust = scoringService.getUfSolicitudTipo(this.paginaIngreso);
				if (ust != null) {
					listaDestinatarios = null;
					Ufs uf = scoringService.sacarUFDelDia(new Date());
					if (uf != null) {
						double montoCalculado = (this.monto / uf.getValor());
						if (montoCalculado <= ust.getValorUf().doubleValue()) {
							this.setConceptoCambioSucursal(concepto);
							this.setNegociosCambioSucursal(negocios);

							if (this.paginaIngreso == 23) {
								listaDestinatarios = scoringService.obtenerListaDestinatario(String.valueOf(this.tipoSolicitudCodigo),
																							 String.valueOf(clienteTarget.getCodigoCanal().ordinal()),
																							 sucursal.getZona(), 
																							 sucursal.getCodigo(), 
																							 negocios,
																							 concepto,
																							 "1");
							} else {
								listaDestinatarios = scoringService.obtenerListaDestinatario(String.valueOf(this.tipoSolicitudCodigo),
																							 String.valueOf(clienteTarget.getCodigoCanal().ordinal()), 
																									sucursal.getZona(), 
																									sucursal.getCodigo(), 
																									negocios,
																									concepto,
																									"1");
							}

							if (listaDestinatarios != null) {
								listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
								listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
								listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
								// for(DestinatarioDTO dto :
								// listaDestinatarios){
								// if(dto != null){
								// log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
								// ,dto.getTipoSolicitud(), dto.getUsername(),
								// dto.getZona(),
								// dto.getSucursal(), dto.getConcepto(),
								// dto.getNegocio() );
								//
								// }
								// }
								agregarAlaListaCorreo();
							}
							this.mensajeGlogal = null;
						} else {
							Locale locale = new Locale("es", "CL");
							NumberFormat numberFormatter = NumberFormat
									.getNumberInstance(locale);
							double montoCalculadoPesos = (ust.getValorUf()
									.doubleValue() * uf.getValor());
							FacesMessages
									.instance()
									.add(Severity.WARN,
											"El monto de la solicitud es mayor a "
													+ numberFormatter
															.format(ust
																	.getValorUf()
																	.doubleValue())
													+ " Uf y valor en pesos es #0.",
											numberFormatter
													.format(montoCalculadoPesos));
							this.mensajeGlogal = "El monto de la solicitud es mayor a "
									+ numberFormatter.format(ust.getValorUf()
											.doubleValue())
									+ " Uf y valor en pesos es "
									+ numberFormatter
											.format(montoCalculadoPesos);
							setMonto(null);
							return;
						}
					}else{
						FacesMessages.instance().add(Severity.WARN,	"No tiene asignación de uf para la evaluación de la solicitud");
						this.mensajeGlogal = "No tiene asignación de uf para la evaluación de la solicitud";
						setMonto(null);
						return;						
					}
				} else {
					FacesMessages.instance().add(Severity.WARN,	"No tiene asignación de uf para la evaluación de la solicitud");
					this.mensajeGlogal = "No tiene asignación de uf para la evaluación de la solicitud";
					setMonto(null);
					return;
				}
			} else {
				if (this.sucursal == null) {
					this.mensajeGlogal = "Señor usuario debe seleccionar una sucursal.";
					setMonto(null);
					return;
				}

				if (this.paginaIngreso == 23) {
					Locale locale = new Locale("es", "CL");
					NumberFormat numberFormatter = NumberFormat
							.getNumberInstance(locale);
					long montoSeguro = (long) 0;
					if (clienteTarget.getSeguro() != null
							&& clienteTarget.getSeguro().equals("VIG")) {
						montoSeguro = clienteTarget.getMontoSeguro()
								.longValue();
					}
					log.debug("monto de seguro #0", montoSeguro);
					log.debug("monto linea Riesgo Küpfer #0", this.monto);
					long totalLinea = montoSeguro + this.monto.longValue();
					log.debug("monto de Seguro + monto solicitado #0",
							totalLinea);
					this.riesgoKupferCalculado = totalLinea - montoSeguro;
					log.debug("total de linea - monto de seguro #0",
							this.riesgoKupferCalculado);
					FacesMessages.instance().add(Severity.WARN,
							"Riesgo Küpfer Calculado es: #0.",
							numberFormatter.format(this.riesgoKupferCalculado));

					listaDestinatarios = scoringService
							.obtenerListaDestinatario(String
									.valueOf(this.tipoSolicitudCodigo), String
									.valueOf(clienteTarget.getCodigoCanal()
											.ordinal()), sucursal.getZona(),
									sucursal.getCodigo(), negocios, concepto,"1");

					this.setConceptoCambioSucursal(concepto);
					this.setNegociosCambioSucursal(negocios);

				} else {
					Ufs uf = scoringService.sacarUFDelDia(new Date());
					if (uf != null) {
						if (uf != null) {
							Double montoCalculado = (this.monto / uf.getValor());
							if (montoCalculado != null) {
								Locale locale = new Locale("es", "CL");
								NumberFormat numberFormatter = NumberFormat
										.getNumberInstance(locale);
								double montoCalculadoPesos = (500 * uf
										.getValor());
								// FacesMessages.instance().add(Severity.WARN,"El monto calculado en UF es #0.",
								// numberFormatter.format(montoCalculadoPesos));
								this.mensajeGlogal = null;
								// setMonto(null);
								// return;
							}
						} else {
							FacesMessages
									.instance()
									.add(Severity.WARN,
											"No se puede calcular, no existe la uf del dia. ");
							this.mensajeGlogal = "No se puede calcular el monto, no existe la uf del dia. ";
							setMonto(null);
							return;

						}
					}
					listaDestinatarios = scoringService
							.obtenerListaDestinatario(String
									.valueOf(this.tipoSolicitudCodigo), String
									.valueOf(clienteTarget.getCodigoCanal()
											.ordinal()), sucursal.getZona(),
									sucursal.getCodigo(), negocios, concepto,"1");

					this.setConceptoCambioSucursal(concepto);
					this.setNegociosCambioSucursal(negocios);
				}

				if (listaDestinatarios != null) {
					listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(
							0);
					listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
					listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
					// for(DestinatarioDTO dto : listaDestinatarios){
					// if(dto != null){
					// log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
					// ,dto.getTipoSolicitud(), dto.getUsername(),
					// dto.getZona(),
					// dto.getSucursal(), dto.getConcepto(), dto.getNegocio() );
					//
					// }
					// }
					agregarAlaListaCorreo();
				}
				this.mensajeGlogal = null;
			}
		}
	}

	public void obtenerMontoConcept() {
		if (this.montoConcepto != null) {
			log.error("monto concepto :#0 ", this.montoConcepto);
		}
	}

	public void obtenerMensajeGrillaProrroga(DocumentoProrrogaDTO objeto) {
		if (objeto != null) {
			log.debug("numero del documento #0 mensaje :#1 ",
					objeto.getNumeroDocto());
		}
	}

	public void obtenerCalculosMontosExposicion() {
		String codigoEvaluacion = null;
		long seguroAux = 0;
		this.mensajeGlogal = null;
		if (this.tipoFormaPago == null && this.encabezado == null) {
			this.mensajeGlogal = "Debe seleccionar la forma de pago de la solicitud.";
		} else {
			if (this.tipoFormaPago != null && this.encabezado == null) {
				this.mensajeGlogal = "Debe seleccionar la forma de pago de la solicitud.";
			}
		}

		if (this.monto == null && this.pie == null) {
			FacesMessages.instance().add(Severity.WARN,
					"El monto tiene que mayor 0.");
			this.mensajeGlogal = "El monto tiene que mayor 0.";
			clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
			return;
		} else if (this.monto == null && this.pie != null) {
			FacesMessages.instance().add(Severity.WARN,
					"El monto tiene que mayor 0.");
			this.mensajeGlogal = "El monto tiene que mayor 0.";
			clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
			return;
		}

		if (this.monto == null) {
			this.monto = (long) 0;
		}
		if (this.pie == null) {
			this.pie = (long) 0;
		}

		if (this.monto == 0 && this.pie != 0) {
			FacesMessages.instance().add(Severity.WARN,
					"Debe ingresar el monto y despues pie.");
			this.mensajeGlogal = "Debe ingresar el monto y despues pie.";
			clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
			return;
		}

		if (this.paginaIngreso == 11) {
			codigoEvaluacion = "1";
			if (this.monto == null) {
				this.monto = (long) 0;
			}
			if (this.pie == null) {
				this.pie = (long) 0;
			}

			if (this.pie != 0 && this.monto != 0) {
				if (this.tipoFormaPago != null
						&& this.tipoFormaPago.getDescripcion().equals(
								"Efectivo")) {
					if (this.pie.longValue() != this.monto.longValue()) {
						FacesMessages
								.instance()
								.add(Severity.ERROR,
										"El monto de compra y el pie deben ser iguales.");
						this.mensajeGlogal = "El monto de compra y el pie deben ser iguales.";
						clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
						this.pie = null;
						return;
					}
				} else if (this.tipoFormaPago != null
						&& !this.tipoFormaPago.getDescripcion().equals(
								"Efectivo")) {
					if (this.pie >= this.monto) {
						FacesMessages
								.instance()
								.add(Severity.WARN,
										"El pie debe ser menor que el monto de compra.");
						this.mensajeGlogal = "El pie debe ser menor que el monto de compra.";
						clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
						this.pie = null;
						return;
					} else {
						activarConsulta = true;
					}
				} else {
					activarConsulta = true;
				}
			}

		} else if (this.paginaIngreso == 12) {
			codigoEvaluacion = "1";
			if (this.monto == null) {
				this.monto = (long) 0;
			}
			if (this.pie == null) {
				this.pie = (long) 0;
			}

			if (this.pie.longValue() != 0 && this.monto.longValue() != 0) {
				if (this.evaluadorCompraVC) {
					if (this.pie.longValue() > this.monto.longValue()) {
						FacesMessages
								.instance()
								.add(Severity.WARN,
										"El pie no debe ser mayor que el monto de venta.");
						this.mensajeGlogal = "El pie no debe ser mayor que el monto de venta.";
						clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
						this.pie = null;
						return;
					} else if (this.pie.longValue() == this.monto.longValue()) {
						FacesMessages
								.instance()
								.add(Severity.WARN,
										"El pie no debe ser igual que el monto de venta.");
						this.mensajeGlogal = "El pie no debe ser igual que el monto de venta.";
						clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
						this.pie = null;
						return;
					} else {
						activarConsulta = true;
					}
				} else {
					this.pie = null;
					activarConsulta = true;
				}
			}
		}

		if (this.getClienteTarget() != null) {
			ExpoRiesgoKhDTO objeto = new ExpoRiesgoKhDTO();
			Long monto = null;
			Long pie = null;
			if (this.monto != null) {
				monto = this.monto.longValue();
			} else {
				monto = new Long(0);
			}
			if (this.pie != null) {
				pie = this.pie.longValue();
			} else {
				pie = new Long(0);
			}

			long peakCredito = clienteTarget.getLineaCreditoKHUtilizado()
					.longValue() + monto - pie;
			objeto.setPeakCredito(peakCredito);

			if ("VIG".equals(clienteTarget.getVigenciaSeguro().trim())) {
				seguroAux = clienteTarget.getMontoSeguro().longValue();
			}

			if (peakCredito <= seguroAux) {
				objeto.setMontoAsegurado(peakCredito);
			} else {
				objeto.setMontoAsegurado(seguroAux);
				long diferencia = (peakCredito - seguroAux);
				objeto.setMontoRiesgoKupfer(diferencia);
			}

			// Pedido o Proceso
			objeto.setMontoPedidoProceso(clienteTarget.getPedidoEnProceso());
			// Peak Crédito mas Pedido o Proceso
			objeto.setMontoPeakCreditoPedidoProceso(objeto.getPeakCredito()
					+ objeto.getMontoPedidoProceso());
			// Riesgo Küpfer + Pedido o Proceso
			objeto.setMontoRiesgoKupferPedidoProceso(objeto
					.getMontoRiesgoKupfer() + objeto.getMontoPedidoProceso());

			this.clienteExpoRiesgoKh = objeto;

		}

		if (tipoSolicitud != null && this.clienteExpoRiesgoKh != null) {
			if (this.activarConsulta) {
				if (listaDestinatarios == null) {
					listaDestinatarios = new ArrayList<DestinatarioDTO>(0);
				}

				/* va a buscar los destinatarios de las solictudes */
				try {

					if (listaDestinatarios != null
							&& listaDestinatarios.size() == 0) {
						if (this.paginaIngreso == 11) {

							listaDestinatarios = scoringService
									.obtenerListaDestinatario(
											String.valueOf(this.tipoSolicitudCodigo),
											String.valueOf(clienteTarget
													.getCodigoCanal().ordinal()),
											sucursal.getZona(), sucursal
													.getCodigo(),
											sacarNegociosDeGrillaEspecialVP(),
											sacarConceptoDeGrillaEspecialVP(),"1");

						} else if (this.paginaIngreso == 12) {
							listaDestinatarios = scoringService
									.obtenerListaDestinatario(
											String.valueOf(this.tipoSolicitudCodigo),
											String.valueOf(clienteTarget
													.getCodigoCanal().ordinal()),
											sucursal.getZona(), sucursal
													.getCodigo(),
											sacarNegociosDeGrillaEspecialVP(),
											sacarConceptoDeGrillaEspecialVP(),"1");

						}

						if (listaDestinatarios != null) {
							listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(
									0);
							listaDescripcionUsuariosCorreos = new ArrayList<String>(
									0);
							listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(
									0);
							// for(DestinatarioDTO dto : listaDestinatarios){
							// if(dto != null){
							// log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
							// ,dto.getTipoSolicitud(), dto.getUsername(),
							// dto.getZona(),
							// dto.getSucursal(), dto.getConcepto(),
							// dto.getNegocio() );
							//
							// }
							// }
							agregarAlaListaCorreo();
						}
					} else {
						if (listaDestinatarios != null) {
							listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(
									0);
							listaDescripcionUsuariosCorreos = new ArrayList<String>(
									0);
							listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(
									0);
							// for(DestinatarioDTO dto : listaDestinatarios){
							// if(dto != null){
							// log.error("tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 "
							// ,dto.getTipoSolicitud(), dto.getUsername(),
							// dto.getZona(),
							// dto.getSucursal(), dto.getConcepto(),
							// dto.getNegocio() );
							//
							// }
							// }
							agregarAlaListaCorreo();
						}
					}
				} catch (Exception e) {
					log.error("Error, sacar los destinatarios : #0",
							e.getMessage());
				}
			}
		}
	}

	/* metodos trabajados */
	public void obtenerPedidoCotizacion() {
		try {
			if (pedidoCotizacion != null && pedidoCotizacion != 0) {
				log.debug(" pedido o cotizacion :" + pedidoCotizacion);
				return;
			}
		} catch (Exception e) {
			log.error("error al sacar las cotizaciones o pedidos #0 ",
					e.getMessage());
		}

	}

	public void buscarInformacionCotizacionPedido() {
		this.tiempoEjecucionProceso = "";
		String pedidoCotizacionAux = null;
		StringBuilder pedCotBuild = null;
		StringBuilder rutBuild = null;
		Double montoTotalAux = (double) 0;
		Double montoNetoAux = (double) 0;
		String rutCabecera = null;
		try {
			if (pedidoCotizacion == null || pedidoCotizacion == 0) {
				FacesMessages.instance().add(Severity.WARN,
						"Debe ingresar uno codigo de pedido o cotización.");
				this.mensajeGlogal = "Debe ingresar uno codigo de pedido o cotización.";
				return;
			} else {
				this.mensajeGlogal = null;
				pedidoCotizacionAux = String.valueOf(pedidoCotizacion);
			}

			pedCotBuild = new StringBuilder(pedidoCotizacionAux);
			while (pedCotBuild.toString().length() < 10) {
				pedCotBuild.insert(0, '0');
			}

			log.debug(pedCotBuild.toString());

			if (listaCotPedDTOs == null) {
				listaCotPedDTOs = new ArrayList<CotPedDTO>(0);
			} else {
				if (verificarCotizacionOPedido(pedCotBuild.toString()) == true) {
					FacesMessages.instance().add(Severity.WARN,
							"El pedido o cotización ya fue ingresada.");
					return;
				}
			}

			/* rescatar y calcular por concento y neegocio */
			SapSystem system = new SapSystem(globalParameters.getNameSap(),
					globalParameters.getIpSap(),
					globalParameters.getClientSap(),
					globalParameters.getSystemNumberSap(),
					globalParameters.getUserSap(),
					globalParameters.getPasswordSap());
			Connect connect = new Connect(system);
			JCoFunction function = connect.getFunction("ZSDFN_PEDIDO_1"); // Nombre RFC
			function.getImportParameterList().setValue("NUMPEDIDO",	pedCotBuild.toString()); 
			connect.execute(function);
			log.debug(function);
			log.debug(function.getTableParameterList());
			log.debug(function.getExportParameterList());
			/* Creacion de la tabla que contendra los datos provenientes de sap */
			JCoTable datosCotizacionPedido = function.getTableParameterList().getTable("CABECERA"); // tabla de salida
			if (datosCotizacionPedido != null) {
				rutCabecera = (String) datosCotizacionPedido.getString("KUNNR");

				rutBuild = new StringBuilder(clienteTarget.getCleanRut().toUpperCase());
				while (rutBuild.toString().length() < 10) {
					rutBuild.insert(0, '0');
				}

				log.debug("rut del cliente:" + rutBuild.toString());
				log.debug("rut del pedido:" + rutCabecera);
				if ((!(rutBuild.toString()).equals(rutCabecera))) {
					if (!(clienteTarget.getCleanRut().toUpperCase()).equals(rutCabecera)) {
						FacesMessages.instance().add(Severity.WARN,	"Rut de cotización o pedido debe coincidir con Rut ingresado en solicitud.");
						this.mensajeGlogal = "Rut de cotización o pedido debe coincidir con Rut ingresado en solicitud.";
						return;
					}
				}
			} else {
				FacesMessages.instance().add(Severity.WARN,"No existe pedido o cotización.");
				this.mensajeGlogal = "No existe pedido o cotización.";
				return;
			}

			if (function != null) {
				log.debug(datosCotizacionPedido);
				log.debug("monto antes multiplicado por 100:"+ datosCotizacionPedido.getBigDecimal("NETWR").doubleValue() * 100);
				montoTotalAux = ((datosCotizacionPedido.getBigDecimal("NETWR").doubleValue() * 100) * 1.19);
				montoNetoAux = (datosCotizacionPedido.getBigDecimal("NETWR").doubleValue() * 100);
				montoTotal += (datosCotizacionPedido.getBigDecimal("NETWR").doubleValue() * 100) * 1.19;
				montoTotalNeto += (datosCotizacionPedido.getBigDecimal("NETWR").doubleValue() * 100);

				log.debug("montoTotal :" + montoTotal);
				log.debug("montoTotalNeto :" + montoTotalNeto);
			}

			JCoTable tablaDatos_Clie = function.getTableParameterList().getTable("DETALLE"); // Tabla de Salida
			String codigoBase = "";
			ConceptosNegocio datosNegocio = null;
			/* Creacion de la tabla que contendra los datos provenientes de sap */
			if (tablaDatos_Clie != null) {
				log.debug(tablaDatos_Clie);
			}

			/* para sacar solo el negocio */
			if (tablaDatos_Clie != null && tablaDatos_Clie.getNumRows() > 0) {
				String negocio = null;
				CabeceraCotPedDTO negocioaux = null;
				listaCabeceraCotPeds = new ArrayList<CabeceraCotPedDTO>(0);
				for (int i = 0; i < tablaDatos_Clie.getNumRows(); i++) {
					tablaDatos_Clie.setRow(i);
					String jerarquiaTotal = (String) tablaDatos_Clie.getString("PRODH");
					String jerarquia = tablaDatos_Clie.getString("PRODH").substring(0, 10);
					String codigo = tablaDatos_Clie.getString("PRODH")
							.substring(jerarquiaTotal.length() - 2,jerarquiaTotal.length());
					if (datosNegocio == null) {
						datosNegocio = intranetSapService.buscarNegocio(codigo,jerarquia);
						codigoBase = codigo;
					} else if (!codigo.equals(codigoBase)) {
						datosNegocio = intranetSapService.buscarNegocio(codigo,	jerarquia);
						codigoBase = codigo;
					}

					if (negocio == null && datosNegocio != null) {
						if (verificarNegocio(datosNegocio) == false) {
							negocioaux = new CabeceraCotPedDTO();
							negocioaux.setNegocio(datosNegocio);
							listaCabeceraCotPeds.add(negocioaux);
							negocio = datosNegocio.getDesNegocio();
							log.error("El primer negocio :"
									+ datosNegocio.getDesNegocio());
						} else {
							log.error("El negocio ya existe :"
									+ datosNegocio.getDesNegocio());
						}
					} else {
						if (!negocio.equals(datosNegocio.getDesNegocio())) {
							if (verificarNegocio(datosNegocio) == false) {
								negocioaux = new CabeceraCotPedDTO();
								negocioaux.setNegocio(datosNegocio);
								listaCabeceraCotPeds.add(negocioaux);
								negocio = datosNegocio.getDesNegocio();
								log.error("El otro negocio :"
										+ datosNegocio.getDesNegocio());
							} else {
								log.error("El negocio ya existe :"
										+ datosNegocio.getDesNegocio());
							}
						} else {
							log.error("El negocio ya existe :"
									+ datosNegocio.getDesNegocio());
						}
					}
				}
				codigoBase = "";
				datosNegocio = null;
			}

			/* para saracar el monto totol por el concepto */
			if (tablaDatos_Clie != null && tablaDatos_Clie.getNumRows() > 0) {
				Double porcentaje = (double) 0;
				String concepto = null;
				String negocio = null;
				DetalleCotPedDTO ncm = null;
				int cantidad = 0;
				int contador = 0;
				cantidad = tablaDatos_Clie.getNumRows();
				for (int i = 0; i < tablaDatos_Clie.getNumRows(); i++) {
					tablaDatos_Clie.setRow(i);
					String jerarquiaTotal = (String) tablaDatos_Clie
							.getString("PRODH");
					String jerarquia = tablaDatos_Clie.getString("PRODH")
							.substring(0, 10);
					String codigo = tablaDatos_Clie.getString("PRODH")
							.substring(jerarquiaTotal.length() - 2,
									jerarquiaTotal.length());
					if ("SEG00PAS00".equals(jerarquia)) {
						log.debug("jergaquia " + jerarquia);
					}
					if (datosNegocio == null) {
						datosNegocio = intranetSapService.buscarNegocio(codigo,
								jerarquia);
						codigoBase = codigo;
					} else if (!codigo.equals(codigoBase)) {
						datosNegocio = intranetSapService.buscarNegocio(codigo,
								jerarquia);
						codigoBase = codigo;
					}
					log.debug(" negacio :" + datosNegocio.getDesNegocio());
					log.debug(" concepto :" + datosNegocio.getDescripcion());
					if (concepto == null && negocio == null
							&& datosNegocio != null) {
						ncm = new DetalleCotPedDTO();
						ncm.setNegocio(datosNegocio);
						Double venta = tablaDatos_Clie.getBigDecimal("NETWR")
								.doubleValue() * 100;
						Double monto = venta * 1.19;
						log.debug(" monto :" + monto);
						ncm.setMonto(monto.longValue());
						concepto = datosNegocio.getDescripcion();
						negocio = datosNegocio.getDesNegocio();
						if (cantidad == (contador + 1)) {
							porcentaje = ((ncm.getMonto().doubleValue() * 100) / montoTotal);
							ncm.setPorceDelTotal(porcentaje);
							insertarDetalleToListaCabecera(ncm);
						} else {
							contador++;
						}
					} else {
						if (concepto.equals(datosNegocio.getDescripcion())
								&& negocio.equals(datosNegocio.getDesNegocio())) {
							Double venta = tablaDatos_Clie.getBigDecimal(
									"NETWR").doubleValue() * 100;
							Double monto = venta * 1.19;
							log.debug(" monto :" + monto);
							Double suma = ncm.getMonto() + monto;
							ncm.setMonto(suma.longValue());
							if (cantidad == (contador + 1)) {
								porcentaje = ((ncm.getMonto().doubleValue() * 100) / montoTotal);
								ncm.setPorceDelTotal(porcentaje);
								insertarDetalleToListaCabecera(ncm);
							} else {
								contador++;
							}
						} else {
							porcentaje = ((ncm.getMonto().doubleValue() * 100) / montoTotal);
							ncm.setPorceDelTotal(porcentaje);
							insertarDetalleToListaCabecera(ncm);
							porcentaje = (double) 0;
							ncm = new DetalleCotPedDTO();
							ncm.setNegocio(datosNegocio);
							Double venta = tablaDatos_Clie.getBigDecimal(
									"NETWR").doubleValue() * 100;
							Double monto = venta * 1.19;
							log.debug(" monto :" + monto);
							ncm.setMonto(monto.longValue());
							concepto = datosNegocio.getDescripcion();
							negocio = datosNegocio.getDesNegocio();
							if (cantidad == (contador + 1)) {
								porcentaje = ((ncm.getMonto().doubleValue() * 100) / montoTotal);
								ncm.setPorceDelTotal(porcentaje);
								insertarDetalleToListaCabecera(ncm);
							} else {
								contador++;
							}
						}
					}
				}
				codigoBase = "";
				datosNegocio = null;
			}

			/* el detalle de la los productos */
			for (int i = 0; i < tablaDatos_Clie.getNumRows(); i++) {
				tablaDatos_Clie.setRow(i);
				DetalleCp detalleCP = new DetalleCp();
				String jerarquiaTotal = (String) tablaDatos_Clie
						.getString("PRODH");
				String jerarquia = tablaDatos_Clie.getString("PRODH")
						.substring(0, 10);
				String codigo = tablaDatos_Clie.getString("PRODH").substring(
						jerarquiaTotal.length() - 2, jerarquiaTotal.length());
				String nombre = tablaDatos_Clie.getString("ARKTX");
				String codigoArticulo = tablaDatos_Clie.getString("MATNR");
				if (datosNegocio == null) {
					datosNegocio = intranetSapService.buscarNegocio(codigo,
							jerarquia);
					codigoBase = codigo;
				} else if (!codigo.equals(codigoBase)) {
					datosNegocio = intranetSapService.buscarNegocio(codigo,
							jerarquia);
					detalleCP.setConceptosNegocio(datosNegocio);
					codigoBase = codigo;
				}
				detalleCP.setConceptosNegocio(datosNegocio);
				Double costo = tablaDatos_Clie.getBigDecimal("WAVWR")
						.doubleValue() * 100;
				Double venta = tablaDatos_Clie.getBigDecimal("NETWR")
						.doubleValue() * 100;
				Double margen = ((venta - costo) / venta) * 100;
				Double monto = venta * 1.19;
				Double ventaMas = costo * 1.19;
				log.debug(
						"nombre de articulo :#0 --> venta:#1 --> costo:#2 --> margen:#3 --> (monto + iva):#4 --> (costo + iva):#5 ",
						nombre, venta, costo, margen, monto, ventaMas);
				detalleCP.setMargen(margen);
				detalleCP.setMonto(monto.longValue());
				Double porcentaje = calcularMergenPorConceptoAndNegocio(
						detalleCP.getConceptosNegocio(), monto);
				detalleCP.setMargProdProc(porcentaje);
				detalleCP.setMargenesMultiplicado(multiplicarMargen(margen,
						porcentaje));
				detalleCP.setNombre(nombre);
				detalleCP.setCodigoArticulo(codigoArticulo);
				insertarDetalleCPToListaDetalle(detalleCP);
			}
			log.debug("Total Cotizaciones Pedidos #0", montoTotal);
			codigoBase = "";
			datosNegocio = null;
		} catch (Exception e) {
			log.debug("error al cargar cotizacion #0", e.toString());
			FacesMessages.instance().add(Severity.WARN,
					"No existe pedido y/o cotización.");
			this.mensajeGlogal = "No existe pedido o cotización.";
			return;
		}

		sumarMxMToListaDetalle();

		if (listaCabeceraCotPeds != null && listaCabeceraCotPeds.size() > 0) {
			habilitarVerProductos = true;
			/* agregar lista con su cotizacion o pedido */
			CotPedDTO objeto = new CotPedDTO();
			objeto.setNumeroCotizacion(pedCotBuild.toString());
			objeto.setListaCabeceraCotPeds(listaCabeceraCotPeds);
			objeto.setMontoNeto(montoNetoAux.longValue());
			objeto.setMontoTotal(montoTotalAux.longValue());
			if (listaCotPedDTOs != null) {
				listaCotPedDTOs.add(objeto);
			} else {
				listaCotPedDTOs = new ArrayList<CotPedDTO>(0);
				listaCotPedDTOs.add(objeto);
			}
			setPedidoCotizacion(null);
		} else {
			habilitarVerProductos = false;
			setPedidoCotizacion(null);
		}

		sacarPorcetajeGlobalConceptoNegocio();
		sacarPorcetajeGlobal();
		sacarPorcetajeCotizacionToPedido();
		sacarPorcetajePedidoGlobal();
		insertMontosEnListaConceptosMontos();

	}

	public boolean verificarNegocio(ConceptosNegocio negocio) {
		boolean exito = false;
		try {
			if (listaCabeceraCotPeds == null
					|| listaCabeceraCotPeds.size() == 0) {
				return exito;
			} else {
				for (CabeceraCotPedDTO obj : listaCabeceraCotPeds) {
					if (obj.getNegocio() != null) {
						if (obj.getNegocio().getNegocio()
								.equals(negocio.getNegocio())) {
							exito = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Al revisar si existe el negocio en la lista #0",
					e.getMessage());
		}

		return exito;
	}

	public void insertarDetalleToListaCabecera(DetalleCotPedDTO obj) {
		Double suma = (double) 0;
		List<CabeceraCotPedDTO> lista = null;
		DetalleCotPedDTO objetoEliminar = null;
		try {
			if ("SEG00PAS00".equals(obj.getNegocio().getJerarquia().trim())) {
				log.debug("llenado aca");
			}

			if (listaCabeceraCotPeds != null && obj != null) {
				lista = new ArrayList<CabeceraCotPedDTO>(0);
				for (CabeceraCotPedDTO o : listaCabeceraCotPeds) {
					if ((o.getNegocio().getNegocio().trim()).equals(obj
							.getNegocio().getNegocio().trim())) {
						if (o.getListaDetalle() == null) {
							o.setListaDetalle(new ArrayList<DetalleCotPedDTO>(0));
							o.getListaDetalle().add(obj);
							lista.add(o);
						} else {
							for (DetalleCotPedDTO nuevo : o.getListaDetalle()) {
								if ((nuevo.getNegocio().getDescripcion().trim())
										.equals(obj.getNegocio()
												.getDescripcion().trim())) {
									suma = nuevo.getMonto().doubleValue()
											+ obj.getMonto();
									obj.setMonto(suma.longValue());
									suma = (double) 0;
									objetoEliminar = nuevo;
								}
							}
							if (objetoEliminar != null) {
								o.getListaDetalle().remove(objetoEliminar);
							}
							o.getListaDetalle().add(obj);
							lista.add(o);
						}
					} else {
						lista.add(o);
					}
				}
				if (listaCabeceraCotPeds != null) {
					listaCabeceraCotPeds.clear();
					setListaCabeceraCotPeds(lista);
				} else {
					setListaCabeceraCotPeds(lista);
				}
			}
		} catch (Exception e) {
			log.error(
					"error al insertar la lista de detalle a la lista de cabecera #0",
					e.getMessage());
		}
	}

	public void insertarDetalleCPToListaDetalle(DetalleCp detalleCP) {
		List<CabeceraCotPedDTO> lista = null;
		List<DetalleCotPedDTO> listaDetalle = null;
		try {
			if (listaCabeceraCotPeds != null && detalleCP != null) {
				lista = new ArrayList<CabeceraCotPedDTO>(0);
				for (CabeceraCotPedDTO o : listaCabeceraCotPeds) {
					if ((o.getNegocio().getDesNegocio().trim())
							.equals(detalleCP.getConceptosNegocio()
									.getDesNegocio().trim())) {
						listaDetalle = new ArrayList<DetalleCotPedDTO>(0);
						if (o.getListaDetalle() != null) {
							for (DetalleCotPedDTO obj : o.getListaDetalle()) {
								if ((obj.getNegocio().getDescripcion().trim())
										.equals(detalleCP.getConceptosNegocio()
												.getDescripcion().trim())) {
									if (obj.getListaDetalleCp() == null) {
										obj.setListaDetalleCp(new ArrayList<DetalleCp>(
												0));
										obj.getListaDetalleCp().add(detalleCP);
									} else {
										obj.getListaDetalleCp().add(detalleCP);
									}
									listaDetalle.add(obj);
								} else {
									listaDetalle.add(obj);
								}
							}
						}
						o.setListaDetalle(listaDetalle);
						lista.add(o);
					} else {
						lista.add(o);
					}
				}
				if (listaCabeceraCotPeds != null) {
					listaCabeceraCotPeds.clear();
					setListaCabeceraCotPeds(lista);
				} else {
					setListaCabeceraCotPeds(lista);
				}
			}
		} catch (Exception e) {
			log.error(
					"error al insertar la lista de detalle a la lista de cabecera #0",
					e.getMessage());
		}
	}

	public Double calcularMergenPorConceptoAndNegocio(
			ConceptosNegocio datoNegocio, Double monto) {
		Double porcentaje = (double) 0;
		DetalleCotPedDTO objetoFinal = null;
		try {
			if (listaCabeceraCotPeds != null && datoNegocio != null) {
				for (CabeceraCotPedDTO o : listaCabeceraCotPeds) {
					if ((o.getNegocio().getDesNegocio().trim())
							.equals(datoNegocio.getDesNegocio().trim())) {
						if (o.getListaDetalle() != null) {
							for (DetalleCotPedDTO obj : o.getListaDetalle()) {
								if ((obj.getNegocio().getDescripcion().trim())
										.equals(datoNegocio.getDescripcion()
												.trim())) {
									objetoFinal = obj;
									break;
								}
							}
						}
					}
				}
				if (objetoFinal != null) {
					porcentaje = ((monto * 100) / objetoFinal.getMonto());
				}
			}
		} catch (Exception e) {
			log.error(
					"error al insertar la lista de detalle a la lista de cabecera #0",
					e.getMessage());
		}
		return porcentaje;
	}

	public Double multiplicarMargen(Double margen, Double margProdProc) {
		Double multiplicado = (double) 0;
		try {
			multiplicado = ((margen / 100) * (margProdProc / 100));
		} catch (Exception e) {
			log.error(
					"Error, al sacar el los parcentaje y se  multiplican por lineas #0",
					e.getMessage());
		}
		return multiplicado;
	}

	public void sumarMxMToListaDetalle() {
		List<CabeceraCotPedDTO> lista = null;
		List<DetalleCotPedDTO> listaDetalle = null;
		Double suma = (double) 0;
		Double porcentajeFinal = (double) 0;
		try {
			if (listaCabeceraCotPeds != null) {
				lista = new ArrayList<CabeceraCotPedDTO>(0);
				for (CabeceraCotPedDTO o : listaCabeceraCotPeds) {
					listaDetalle = new ArrayList<DetalleCotPedDTO>(0);
					if (o.getListaDetalle() != null) {
						for (DetalleCotPedDTO obj : o.getListaDetalle()) {
							if (obj.getListaDetalleCp() != null) {
								for (DetalleCp dcp : obj.getListaDetalleCp()) {
									suma += dcp.getMargenesMultiplicado();
								}
								porcentajeFinal = (suma * 100);
								obj.setPorcePorConcepto(porcentajeFinal);
								suma = (double) 0;
							}
							listaDetalle.add(obj);
						}
					}
					o.setListaDetalle(listaDetalle);
					lista.add(o);
				}
				if (listaCabeceraCotPeds != null) {
					listaCabeceraCotPeds.clear();
					setListaCabeceraCotPeds(lista);
				} else {
					setListaCabeceraCotPeds(lista);
				}
			}
		} catch (Exception e) {
			log.error(
					"error al insertar la lista de detalle a la lista de cabecera #0",
					e.getMessage());
		}
	}

	public void sacarPorcetajeGlobalConceptoNegocio() {
		List<CotPedDTO> listaPrincipal = null; // listaCotPedDTOs
		List<CabeceraCotPedDTO> lista = null;
		List<DetalleCotPedDTO> listaDetalle = null;
		Double sumaTotal = (double) 0;
		Double suma = (double) 0;
		Double sumaConcepto = (double) 0;
		Double porcentajeNegocio = (double) 0;
		Double porcentajeMonto = (double) 0;
		Double margenConcepto = (double) 0;
		try {
			if (listaCotPedDTOs != null) {
				listaPrincipal = new ArrayList<CotPedDTO>(0);
				for (CotPedDTO pc : listaCotPedDTOs) {
					if (pc != null) {
						lista = new ArrayList<CabeceraCotPedDTO>(0);
						for (CabeceraCotPedDTO o : pc.getListaCabeceraCotPeds()) {
							listaDetalle = new ArrayList<DetalleCotPedDTO>(0);
							if (o.getListaDetalle() != null) {
								for (DetalleCotPedDTO obj : o.getListaDetalle()) {
									if (obj.getListaDetalleCp() != null) {
										log.debug("monto general pedido  :"
												+ pc.getMontoTotal());
										log.debug("getPorcePorConcepto  :"
												+ obj.getPorcePorConcepto());
										log.debug("getMonto  :"
												+ obj.getMonto());
										/* calculo de porcentaje de articulos */
										Double sumaDetalle = (double) 0;
										for (DetalleCp dcp : obj
												.getListaDetalleCp()) {
											double calculoNew = ((dcp
													.getMargen() / 100) * (dcp
													.getMargProdProc() / 100));
											log.debug("calculoNew : #0",
													calculoNew);
											sumaDetalle += calculoNew;
										}

										sumaDetalle = sumaDetalle * 100;
										log.debug("sumaDetalle : #0",
												sumaDetalle);

										porcentajeMonto = ((obj.getMonto()
												.doubleValue() * 100) / obj
												.getMonto());
										obj.setPorceDelTotal(porcentajeMonto);
										sumaTotal += obj.getMonto()
												.doubleValue();
										log.debug("porcentaje Monto : "
												+ porcentajeMonto);
										log.debug("(obj.getPorcePorConcepto() / 100):"
												+ (obj.getPorcePorConcepto() / 100));
										log.debug("(porcentajeMonto / 100):"
												+ (porcentajeMonto / 100));
										suma += ((obj.getPorcePorConcepto() / 100) * (porcentajeMonto / 100));
										sumaConcepto = ((obj
												.getPorcePorConcepto() / 100) * (porcentajeMonto / 100));
										log.debug("suma negocio:" + suma);
										margenConcepto = (sumaConcepto * 100);
										log.debug("margen de concepto : "
												+ margenConcepto);
										obj.setMargenConcepto(margenConcepto);
										margenConcepto = (double) 0;
									}
									listaDetalle.add(obj);
								}
								porcentajeNegocio = (suma * 100);
								log.debug("margen de suma de negocio : "
										+ porcentajeNegocio);
								o.setPorcentaje(porcentajeNegocio);
								o.setMontoGeneral(sumaTotal.longValue());
								suma = (double) 0;
								sumaTotal = (double) 0;
							}
							o.setListaDetalle(listaDetalle);
							lista.add(o);
						}
					}
					listaPrincipal.add(pc);
				}

				if (listaCotPedDTOs != null) {
					listaCotPedDTOs.clear();
					setListaCotPedDTOs(listaPrincipal);
				} else {
					setListaCotPedDTOs(listaPrincipal);
				}
			}
		} catch (Exception e) {
			log.error(
					"error al insertar la lista de detalle a la lista de cabecera #0",
					e.getMessage());
		}
	}

	public void sacarPorcetajeGlobal() {
		List<CotPedDTO> listaPrincipal = null; // listaCotPedDTOs
		List<CabeceraCotPedDTO> lista = null;
		List<DetalleCotPedDTO> listaDetalle = null;
		Double suma = (double) 0;
		Double porcentajeNegocio = (double) 0;
		Double porcentajeMonto = (double) 0;
		porcentajeGlobal = (double) 0;
		Double sumaPorcentajeGeneralNegocio = (double) 0;
		try {
			if (listaCotPedDTOs != null) {
				listaPrincipal = new ArrayList<CotPedDTO>(0);
				for (CotPedDTO pc : listaCotPedDTOs) {
					if (pc != null) {
						lista = new ArrayList<CabeceraCotPedDTO>(0);
						for (CabeceraCotPedDTO o : pc.getListaCabeceraCotPeds()) {
							listaDetalle = new ArrayList<DetalleCotPedDTO>(0);
							if (o.getListaDetalle() != null
									&& o.getListaDetalle().size() == 1) {
								for (DetalleCotPedDTO obj : o.getListaDetalle()) {
									if (obj.getListaDetalleCp() != null) {
										log.debug("monto general negocio  :"
												+ o.getMontoGeneral());
										log.debug("monto general del concepto  :"
												+ obj.getMonto());
										log.debug("getMargenConcepto  :"
												+ obj.getMargenConcepto());
										porcentajeMonto = ((obj.getMonto()
												.doubleValue() * 100) / o
												.getMontoGeneral());
										log.debug("porcentaje Monto : "
												+ porcentajeMonto);
										log.debug("(obj.getMargenConcepto() / 100):"
												+ (obj.getMargenConcepto() / 100));
										log.debug("(porcentajeMonto / 100):"
												+ (porcentajeMonto / 100));
										log.debug(" ((obj.getMargenConcepto() / 100) * (porcentajeMonto / 100)):"
												+ ((obj.getMargenConcepto() / 100) * (porcentajeMonto / 100)));
										suma += ((obj.getMargenConcepto() / 100) * (porcentajeMonto / 100));

									}
									listaDetalle.add(obj);
								}
								porcentajeNegocio = (suma * 100);
								log.debug("margen de global (porcentajeNegocio): "
										+ porcentajeNegocio);
								Double representacion = (o.getMontoGeneral()
										.doubleValue() * 100)
										/ pc.getMontoTotal();
								log.debug(" representacion :" + representacion);
								o.setRepresentacion(representacion);
								o.setPorcentaje(porcentajeNegocio);
								sumaPorcentajeGeneralNegocio += porcentajeNegocio;
								suma = (double) 0;
							} else {
								Double sumaMontodeConceptosGenerales = (double) 0;
								for (DetalleCotPedDTO obj : o.getListaDetalle()) {
									if (obj.getListaDetalleCp() != null) {
										log.debug("monto general negocio  :"
												+ o.getMontoGeneral());
										log.debug("monto general del concepto  :"
												+ obj.getMonto());
										log.debug("getMargenConcepto  :"
												+ obj.getMargenConcepto());
										porcentajeMonto = ((obj.getMonto()
												.doubleValue() * 100) / o
												.getMontoGeneral());
										log.debug("porcentaje Monto : "
												+ porcentajeMonto);
										log.debug("(obj.getMargenConcepto() / 100):"
												+ (obj.getMargenConcepto() / 100));
										log.debug("(porcentajeMonto / 100):"
												+ (porcentajeMonto / 100));
										suma += ((obj.getMargenConcepto() / 100) * (porcentajeMonto / 100));
										sumaMontodeConceptosGenerales += obj
												.getMonto();
									}
									listaDetalle.add(obj);
								}
								porcentajeNegocio = (suma * 100);
								log.debug("margen de global (porcentajeNegocio): "
										+ porcentajeNegocio);
								Double representacion = (sumaMontodeConceptosGenerales * 100)
										/ pc.getMontoTotal();
								log.debug(" representacion :" + representacion);
								o.setRepresentacion(representacion);
								o.setPorcentaje(porcentajeNegocio);
								sumaPorcentajeGeneralNegocio += porcentajeNegocio;
								suma = (double) 0;
							}

							o.setListaDetalle(listaDetalle);
							lista.add(o);
						}

						pc.setMargenGlobal(sumaPorcentajeGeneralNegocio);
						sumaPorcentajeGeneralNegocio = (double) 0;

					}
					listaPrincipal.add(pc);
				}

				if (listaCotPedDTOs != null) {
					listaCotPedDTOs.clear();
					setListaCotPedDTOs(listaPrincipal);
				} else {
					setListaCotPedDTOs(listaPrincipal);
				}
			}
		} catch (Exception e) {
			log.error(
					"error al insertar la lista de detalle a la lista de cabecera #0",
					e.getMessage());
		}
	}

	public void sacarPorcetajeCotizacionToPedido() {
		List<CotPedDTO> listaPrincipal = null; // listaCotPedDTOs
		Double porcentajeCotizacion = (double) 0;
		Double resultadoFinal = (double) 0;
		Double multiplicacion = (double) 0;
		try {
			if (listaCotPedDTOs != null) {
				listaPrincipal = new ArrayList<CotPedDTO>(0);
				for (CotPedDTO pc : listaCotPedDTOs) {
					if (pc != null) {
						if (pc.getListaCabeceraCotPeds() != null
								&& pc.getListaCabeceraCotPeds().size() == 1) {
							CabeceraCotPedDTO negocio = null;
							porcentajeCotizacion = ((pc.getMontoTotal()
									.doubleValue() * 100) / pc.getMontoTotal());
							log.debug("porcentaje Cotizacion 1 :"
									+ porcentajeCotizacion);

							negocio = pc.getListaCabeceraCotPeds().get(0);
							if (negocio != null) {
								multiplicacion = (porcentajeCotizacion * (negocio
										.getPorcentaje() / 100));
								log.debug("multiplicacion de porcentaje del pedido * el porcentaje del negocio :"
										+ multiplicacion);
								pc.setMargenGlobal(multiplicacion);
								pc.setPonderacion(multiplicacion);
							} else {
								pc.setMargenGlobal(porcentajeCotizacion);
								pc.setPonderacion(porcentajeCotizacion);
							}
						} else {
							for (CabeceraCotPedDTO obj : pc
									.getListaCabeceraCotPeds()) {
								porcentajeCotizacion = ((obj.getMontoGeneral()
										.doubleValue() * 100) / pc
										.getMontoTotal());
								log.debug("porcentaje Cotizacion 2 :"
										+ porcentajeCotizacion);
								log.debug("negocio.getPorcentaje() : "
										+ obj.getPorcentaje());
								resultadoFinal += (porcentajeCotizacion / 100)
										* (obj.getPorcentaje() / 100);
							}
							if (resultadoFinal != 0) {
								multiplicacion = (resultadoFinal * 100);
								log.debug("multiplicacion de porcentaje del pedido * el porcentaje del negocio :"
										+ multiplicacion);
								pc.setMargenGlobal(multiplicacion);

							} else {
								pc.setMargenGlobal(porcentajeCotizacion);
								pc.setPonderacion(multiplicacion);
							}
						}
					}
					listaPrincipal.add(pc);
					porcentajeCotizacion = (double) 0;
					resultadoFinal = (double) 0;
					multiplicacion = (double) 0;
				}

				if (listaCotPedDTOs != null) {
					listaCotPedDTOs.clear();
					setListaCotPedDTOs(listaPrincipal);
				} else {
					setListaCotPedDTOs(listaPrincipal);
				}
			}
		} catch (Exception e) {
			log.error(
					"error al insertar la lista de detalle a la lista de cabecera #0",
					e.getMessage());
		}
	}

	public void sacarPorcetajePedidoGlobal() {
		List<CotPedDTO> listaPrincipal = null; // listaCotPedDTOs
		Double porcentajeCotizacion = (double) 0;
		Double multiplicacion = (double) 0;
		porcentajeGlobal = (double) 0;
		try {
			if (listaCotPedDTOs != null && montoTotal != null
					&& montoTotal.longValue() > 0) {
				listaPrincipal = new ArrayList<CotPedDTO>(0);
				for (CotPedDTO pc : listaCotPedDTOs) {
					if (pc != null) {
						porcentajeCotizacion = ((pc.getMontoTotal()
								.doubleValue() * 100) / montoTotal);
						log.debug("porcentaje pedido o cotizacion :"
								+ porcentajeCotizacion);
						multiplicacion = (porcentajeCotizacion * (pc
								.getMargenGlobal() / 100));
						log.debug("multiplicacion :" + multiplicacion);
						porcentajeGlobal += multiplicacion;
						pc.setPonderacion(multiplicacion);
					}

					listaPrincipal.add(pc);
					porcentajeCotizacion = (double) 0;
					multiplicacion = (double) 0;
				}
				if (listaCotPedDTOs != null) {
					listaCotPedDTOs.clear();
					setListaCotPedDTOs(listaPrincipal);
				} else {
					setListaCotPedDTOs(listaPrincipal);
				}
			}
		} catch (Exception e) {
			log.error(
					"error al insertar la lista de detalle a la lista de cabecera #0",
					e.getMessage());
		}
	}

	public void insertMontosEnListaConceptosMontos() {
		List<ConceptoMontoDTO> listaFinal = new ArrayList<ConceptoMontoDTO>(0);
		Long monto = new Long(0);
		this.montoGeneralConceptoMonto = 0;
		try {
			if (listaConceptosMontos != null) {
				for (ConceptoMontoDTO cmd : listaConceptosMontos) {
					cmd.setMonto(0);
					if (listaCotPedDTOs != null) {
						for (CotPedDTO pc : listaCotPedDTOs) {
							if (pc != null) {
								for (CabeceraCotPedDTO o : pc
										.getListaCabeceraCotPeds()) {
									if (o != null
											&& o.getListaDetalle() != null) {
										for (DetalleCotPedDTO dp : o
												.getListaDetalle()) {
											if ((dp.getNegocio().getConcepto()
													.trim()).equals(cmd
													.getConcepto().trim())) {
												monto += dp.getMonto();
											}
										}
									}
								}
							}
						}
					}
					if (monto != null && monto.longValue() > 0) {
						cmd.setMarca("X");
						cmd.setMonto(monto);
						this.montoGeneralConceptoMonto += monto;
						monto = new Long(0);
					}
					listaFinal.add(cmd);
				}
				this.setListaConceptosMontos(listaFinal);
			}
		} catch (Exception e) {
			log.error("Error al colcar lo montos dentro de la grilla ",
					e.getMessage());
		}
	}

	public void eliminarPedidocotizacion(CotPedDTO objeto) {
		try {
			if (objeto != null) {
				montoTotal = (double) (montoTotal.longValue() - objeto
						.getMontoTotal());
				montoTotalNeto = (double) (montoTotalNeto.longValue() - objeto
						.getMontoNeto());
				listaCotPedDTOs.remove(objeto);
			}
			sacarPorcetajeGlobalConceptoNegocio();
			sacarPorcetajeGlobal();
			sacarPorcetajeCotizacionToPedido();
			sacarPorcetajePedidoGlobal();
			insertMontosEnListaConceptosMontos();
		} catch (Exception e) {
			log.error("error al eliminar pedido o cotizacion de la grilla.");
		}
	}

	public void limpiarCotizacion() {
		limpiarTiempoEjecuciones();
		try {
			setMargenGlobal(this.porcentajeGlobal);
			setMonto(montoTotal.longValue());
			obtenerCalculosMontosExposicion();
		} catch (Exception e) {
			log.error(
					"error al insertar la lista de detalle a la lista de cabecera #0",
					e.getMessage());
		}
	}

	public void limpiaMensajeGlobal() {
		setMensajeGlogal(null);
		return;
	}

	public boolean verificarCotizacionOPedido(String codigoAux) {
		boolean existe = false;
		try {
			for (CotPedDTO o : listaCotPedDTOs) {
				if (o.getNumeroCotizacion().equals(codigoAux)) {
					existe = true;
					break;
				}
			}
		} catch (Exception e) {
			log.error("Error, al verificar si existe...", e.getMessage());
		}
		return existe;
	}

	/* metodos trabajados */
	public void obtenerSucursalSeleccionada() {
		if (sucursal != null) {
			log.debug("sucursal: " + this.sucursal.getDescripcion());
			if (this.listaDestinatarios != null) {
				if (this.tipoSolicitudCodigo != 0) {
					if (clienteTarget != null) {
						if (clienteTarget.getCodigoCanal() != null) {
							this.listaDestinatarios = new ArrayList<DestinatarioDTO>(0);
							String conceptosVarios = "";
							String negociosVarios = "";
							if (this.getConceptoCambioSucursal() != null) {
								conceptosVarios = this.getConceptoCambioSucursal();
							}
							if (this.getNegociosCambioSucursal() != null) {
								negociosVarios = this.getNegociosCambioSucursal();
							}

							listaDestinatarios = scoringService.obtenerListaDestinatario(
											String.valueOf(this.tipoSolicitudCodigo),
											String.valueOf(clienteTarget
													.getCodigoCanal().ordinal()),
											sucursal.getZona(), sucursal
													.getCodigo(),
											negociosVarios, conceptosVarios,"1");
							if (this.listaDestinatarios != null	&& this.listaDestinatarios.size() > 0) {
								agregarAlaListaCorreo();
							}
						}
					}
				}
			}
		}
	}
	
	public void sociedadSeleccionada() {
		if (sociedadAux != null) {
			log.debug("SOCIEDAD: " + this.sociedadAux.getRazonSocial());
			if(this.rutAux != null && !"".equals(this.rutAux)){
				obtenerInformacionCliente();
			}
			if(listaUsuarioCorreoagregados != null && listaUsuarioCorreoagregados.size() > 0)
				listaUsuarioCorreoagregados.clear();
			
			obtenerSucursalSeleccionada();
		}
	}

	/* metodos saca las forma de pagos */
	@SuppressWarnings("unchecked")
	public void obtenerFormaPagos() {
		if (tipoFormaPago != null) {
			setEncabezado(null);
			log.debug("tipo forma de pago : "
					+ this.tipoFormaPago.getDescripcion());
			try {

				if (this.tipoFormaPago.getDescripcion().equals("Efectivo")) {
					FormaPagoEncabezado formaPagoAux = entityManager.find(
							FormaPagoEncabezado.class, new Long(1));
					if (formaPagoAux != null) {
						listaEncabezados = new ArrayList<FormaPagoEncabezado>(0);
						listaEncabezados.add(formaPagoAux);
						setEncabezado(formaPagoAux);
						setPie(this.monto);
						this.activarConsulta = true;
						this.habilitar = true;
						obtenerCalculosMontosExposicion();
						// listaUsuarioCorreoagregados = new
						// ArrayList<UsuarioCorreoDTO>(0);
					}
				} else {
					this.pie = null;
					this.habilitar = false;
					// this.listaUsuarioCorreoagregados = new
					// ArrayList<UsuarioCorreoDTO>(0);
					List<FormaPagoEncabezado> lista = (List<FormaPagoEncabezado>) entityManager
							.createQuery(
									"select fpe "
											+ "from FormaPagoEncabezado fpe "
											+ "where fpe.tipoFormaPago.idTipoFormaPago=:idTipoFormaPagoAux "
											+ "and fpe.tipo.codTipoSolicitud=:tipoSolicitud "
											+ "and fpe.estado=:estadoAux "
											+ "order by fpe.descripcion asc")
							.setParameter("idTipoFormaPagoAux",
									tipoFormaPago.getIdTipoFormaPago())
							.setParameter("tipoSolicitud",
									String.valueOf(this.paginaIngreso))
							.setParameter("estadoAux", EstadoEntityType.ACTIVO)
							.getResultList();

					if (lista != null && this.paginaIngreso == 11) {
						listaEncabezados = new ArrayList<FormaPagoEncabezado>(0);
						for (FormaPagoEncabezado fpe : lista) {
							if (!fpe.getDescripcion().equals("Efectivo")) {
								listaEncabezados.add(fpe);
							}
						}
					} else if (lista != null && this.paginaIngreso == 12) {
						setListaEncabezados(lista);
					}
				}

				if ((this.monto != null && this.monto != 0)
						&& (this.pie != null && this.pie != 0)) {
					obtenerCalculosMontosExposicion();
				}
			} catch (Exception e) {
				log.error(
						"Error, al sacar la lista de forma de pago formada por el usuario. #0",
						e.getMessage());
			}
		}
	}

	public void obtenerFormaPagosEncabezado() {
		String[] array = null;
		this.mensajeGlogal = null;
		if (encabezado != null) {
			log.debug("encabezado : " + this.encabezado.getDescripcion());
			try {
				array = encabezado.getDescripcion().split(",");
			} catch (Exception e) {
				log.error("error, al sacar la cantidad de registro #0",
						e.getMessage());
			}

			try {
				if (this.paginaIngreso == 11 || this.paginaIngreso == 12) {
					if (this.paginaIngreso == 11 && array != null) {
						boolean valido = false;
						for (String cadena : array) {
							if (cadena.equals("Efectivo")) {
								valido = true;
								break;
							}
						}
						if (valido) {
							FacesMessages.instance().add(Severity.WARN,
									"Debe ingresar un pie en la venta normal.");
							this.mensajeGlogal = "Debe ingresar un pie en la venta normal.";
							this.pie = null;
							this.habilitar = false;
							return;
						} else {
							this.pie = null;
							this.activarConsulta = true;
							this.habilitar = true;
							obtenerCalculosMontosExposicion();
						}
					} else if (this.paginaIngreso == 12) {
						this.evaluadorCompraVC = false;
						if (array != null) {
							if (array.length >= 2) {
								for (String nombre : array) {
									if (nombre != null
											&& nombre.equals("Efectivo")) {
										this.evaluadorCompraVC = true;
										break;
									}
								}
								if (this.evaluadorCompraVC) {
									FacesMessages
											.instance()
											.add(Severity.WARN,
													"Debe ingresar un pie en la venta en cuotas.");
									this.mensajeGlogal = "Debe ingresar un pie en la venta en cuotas.";
									this.habilitar = false;
									if (listaUsuarioCorreoagregados != null) {
										listaUsuarioCorreoagregados.clear();
									}
									return;
								} else {
									this.activarConsulta = true;
									this.habilitar = true;
									obtenerCalculosMontosExposicion();
								}
							} else {
								FacesMessages
										.instance()
										.add(Severity.WARN,
												"Debe seleccionar el tipo de pago con mas cuotas");
								setEncabezado(null);
								return;
							}
						} else {
							FacesMessages
									.instance()
									.add(Severity.ERROR,
											"error al seleccionar el tipo de pago de la solicitud.");
							setEncabezado(null);
						}
					}
				}
			} catch (Exception e) {
				FacesMessages
						.instance()
						.add(Severity.INFO,
								"error, al calcular los montos del Riesgo Küpfer  #0...",
								e.getMessage());
			}
		}
	}

	/* metodo generador de menu de la solicitud */
	@SuppressWarnings("unchecked")
	public void generarMenu() {
		List<LcredTipoSolicitud> listaMenu = null;
		try {
			listaMenu = (List<LcredTipoSolicitud>) entityManager
					.createQuery(
							"select ts "
									+ "from LcredTipoSolicitud ts where ts.claveProceso=:claveAux")
					.setParameter("claveAux", "0").getResultList();
		} catch (Exception e) {
			log.error(
					"Error, cuando obtiene los registros del menu principal #0",
					e.getMessage());
		}
		try {
			if (listaMenu != null) {
				boolean status = true;
				menu = new HashMap<String, Boolean>();
				for (LcredTipoSolicitud menuAux : listaMenu) {
					List<LcredTipoSolicitud> listaMenuSub = (List<LcredTipoSolicitud>) entityManager
							.createQuery(
									"select ts from LcredTipoSolicitud ts where ts.claveProceso=:claveAux")
							.setParameter("claveAux",
									menuAux.getCodTipoSolicitud())
							.getResultList();

					/* creacion de objeto de menu de vista */
					StringBuffer cadena = new StringBuffer();
					cadena.append("id");
					cadena.append((menuAux.getDesTipoSolicitud()).replace(" ",
							""));
					cadena.append(menuAux.getCodTipoSolicitud());
					if (status) {
						menu.put(cadena.toString(), status);
						dataTipoSolicitud.put(cadena.toString(), menuAux);
						status = false;
					} else {
						menu.put(cadena.toString(), status);
						dataTipoSolicitud.put(cadena.toString(), menuAux);
					}

					// log.debug("menu Cabecera #0", cadena.toString().trim());
					for (LcredTipoSolicitud submenu : listaMenuSub) {
						StringBuffer cadenaSub = new StringBuffer();
						cadenaSub.append("id");
						cadenaSub.append((submenu.getDesTipoSolicitud())
								.replace(" ", ""));
						cadenaSub.append(submenu.getCodTipoSolicitud());
						menu.put(cadenaSub.toString(), false);
						dataTipoSolicitud.put(cadenaSub.toString(), submenu);
						// log.debug("subMenu detalle #0",
						// cadenaSub.toString().trim());
					}

				}

			}

		} catch (Exception e) {
			log.error("Error, cuando obtiene los registros del sub menu",
					e.getMessage());
		}

		// log.debug("solo para parar el programa #0");

	}

	/* metodo para obtener la informacion del cliente a traves del rut */

	/* boolean que identifica si es rut extranjero o no */
	private boolean rutExtranjero = false;

	public boolean isRutExtranjero() {
		return rutExtranjero;
	}

	public void setRutExtranjero(boolean rutExtranjero) {
		this.rutExtranjero = rutExtranjero;
	}

	/* Inicio metodo */
	public void obtenerInformacionCliente() {
		limpiarTiempoEjecuciones();
		log.debug("Rut: #0", rutAux);
		cal1 = Calendar.getInstance();
		cal1.setTime(new Date());

		clienteTarget = new ClienteDTO();
		clienteTarget.setRut(rutAux); // mantuve el rut en la variable rutAux
										// (no se si la ocuparias para algo)
										// solo traspase su valor al objeto
										// clienteTarget

		// verificar que el rut no es vacio ni nulo
		if (clienteTarget.getRut() == null || clienteTarget.getRut().trim().isEmpty()) {
			clienteTarget.setRut(null);
			this.mensajeGlogal = "No ha ingresado ningun rut a consultar.";
			return;
		}

		// verificar que el rut no tenga menos de 8 caracteres
		if (clienteTarget.getRut().length() < 8) {
			FacesMessages.instance().add(Severity.ERROR,"Rut invalido, debe tener al menos 8 caracteres");
			this.mensajeGlogal = "Rut invalido, debe tener al menos 8 caracteres";
			return;
		}

		// agregar guin en caso de ser necesario
		String rut = globalService.agregarGuionRut(clienteTarget.getRut());
		log.debug("rut #0 #1", rut, globalService.validarRut(rut));

		// verificar si es rut extranjero, si lo es, se omite la validacion del
		// mismo
		if (!rutExtranjero) {
			if (globalService.validarRut(rut))
				clienteTarget.setRut(rut);
			else {
				clienteTarget.setRut(null);
				if(this.listaSociedades != null && this.listaSociedades.size() > 1){
					this.setSociedadAux(null);
				}
				this.rutAux = "";
				FacesMessages.instance().add(Severity.ERROR, "Rut invalido.!!!");
				this.mensajeGlogal = "Rut invalido.!!!";
				return;
			}
		} else {
			clienteTarget.setRut(rut);
		}
		
		if(this.sociedadAux == null){
			FacesMessages.instance().add(Severity.ERROR,"Se debe seleccionar la sociedad para consultar");
			this.mensajeGlogal = "Se debe seleccionar la sociedad para consultar.";
			return;
		}
		
		try {

			/* Comienzo de la extraccion de datos desde Sap */
			/*
			 * declaracion de parametros sap desde archivo global parameters y
			 * creacion de la conexion
			 */
			SapSystem system = new SapSystem(globalParameters.getNameSap(),
					globalParameters.getIpSap(),
					globalParameters.getClientSap(),
					globalParameters.getSystemNumberSap(),
					globalParameters.getUserSap(),
					globalParameters.getPasswordSap());

			Connect connect = new Connect(system);

			/* Paso de los parametros necesarios a la rfc */
			functionDatosClientes = connect	.getFunction("ZSDFN_DATOS_CLIENTE_11"); // Nombre RFC
			functionDatosClientes.getImportParameterList().setValue("RUTCLIENTE", clienteTarget.getCleanRut().toUpperCase()); // Paso de parametros
			functionDatosClientes.getImportParameterList().setValue("SOCIEDAD", this.getSociedadAux().getCodigoSociedad());
			connect.execute(functionDatosClientes);

			/* Creacion de la tabla que contendra los datos provenientes de sap */
			JCoTable datosCliente = functionDatosClientes.getTableParameterList().getTable("DATOS_CLIE"); // tabla de salida

			if (datosCliente != null) {
				log.info(functionDatosClientes);
				log.info(datosCliente);
				String swExiste = (String) functionDatosClientes.getExportParameterList().getValue("SW_EXISTE");
				if ("S".equals(swExiste)) {
					setHabilitarCtaCte(true);
					/*
					 * traspaso de los datos necesarios desde la tabla hacia el
					 * objeto Cliente Target
					 */
					clienteTarget.setRazonSocial((String) datosCliente.getValue("NOMCLIENTE"));
					clienteTarget.setGiro((String) functionDatosClientes.getExportParameterList().getValue("GIRO"));
					clienteTarget.setDireccion((String) datosCliente.getValue("DIRCLIENTE"));
					clienteTarget.setFono((String) datosCliente.getValue("TELCLIENTE"));
					clienteTarget.setFax((String) datosCliente.getValue("FAXCLIENTE"));
					clienteTarget.setComuna((String) datosCliente.getValue("COMCLIENTE"));
					clienteTarget.setCiudad((String) datosCliente.getValue("CIUCLIENTE"));
					clienteTarget.setSucursalCliente((String) datosCliente.getValue("SUCCLIENTE"));

					String codigoCanal = (String) functionDatosClientes.getExportParameterList().getValue("CANAL");
					if (codigoCanal != null) {
						if ("X".equals(codigoCanal)) {
							canalVenta = TipoCuentasKupferType.KX;
							clienteTarget.setCanalVenta(canalVenta.getNombre());
							clienteTarget.setCodigoCanal(canalVenta);
						} else if ("G".equals(codigoCanal)) {
							canalVenta = TipoCuentasKupferType.GC;
							clienteTarget.setCanalVenta(canalVenta.getNombre());
							clienteTarget.setCodigoCanal(canalVenta);
						} else if ("M".equals(codigoCanal)) {
							canalVenta = TipoCuentasKupferType.MX;
							clienteTarget.setCanalVenta(canalVenta.getNombre());
							clienteTarget.setCodigoCanal(canalVenta);
						}

					}

					clienteTarget.setTipoCliente((String) datosCliente.getValue("TIPSEGURO"));
					BigDecimal linea = (BigDecimal) datosCliente.getValue("LINCREDITO");
					Long monto = (linea.longValue() * 100);
					clienteTarget.setLineaCreditoKH(new BigDecimal(monto));
					clienteTarget.setLineaCreditoKHDisponible((BigDecimal) functionDatosClientes.getExportParameterList().getValue("LINDISPONIBLE"));

					// LCredUtilizado.Caption = Format((LbLCreditoKH.Caption -
					// LCredDisp.Caption), Formato)
					Long lineaCreditoUtilizada = (clienteTarget.getLineaCreditoKH().longValue() - clienteTarget.getLineaCreditoKHDisponible().longValue());
					clienteTarget.setLineaCreditoKHUtilizado(new BigDecimal(lineaCreditoUtilizada));

					clienteTarget.setEstadoLineaCreditoKH((String) functionDatosClientes.getExportParameterList().getValue("ESTADO_LC_KH"));
					clienteTarget.setNumeroRV((String) functionDatosClientes.getExportParameterList().getValue("NRORV"));
					clienteTarget.setEstadoLineaEnCuotas((String) functionDatosClientes.getExportParameterList().getValue("ESTADO_LC_CUO"));
					String descondpago = (String) functionDatosClientes.getExportParameterList().getValue("DESCONDPAGO");
					if (descondpago != null) {
						clienteTarget.setCondicionPago((String) datosCliente.getValue("CONPAGO") + "-" + descondpago);
						clienteTarget.setCodigoCondicionPago((String) datosCliente.getValue("CONPAGO"));
					}

					String clasifOPB = (String) functionDatosClientes.getExportParameterList().getValue("CLASIF_OPB");
					try {
						if (clasifOPB != null) {
							Object obj = intranetSapService.descripcionTipoClienteCav(clasifOPB);
							if (obj != null) {
								clienteTarget.setTipoCliente(obj.toString());
							} else {
								clienteTarget.setTipoCliente("No Clasificado");
							}
						} else {
							clienteTarget.setTipoCliente("No Clasificado");
						}

					} catch (Exception e) {
						clienteTarget.setTipoCliente("No Clasificado");
					}

					String clsRiesgo = (String) datosCliente.getValue("CLSRIESGO");
					clienteTarget.setCodigoclasificacionRiesgo(clsRiesgo);
					try {
						Object clasRiesgo = intranetSapService.sacarDescripClasificacionRiesgo(clsRiesgo);
						if (clasRiesgo != null) {
							clienteTarget.setClasificacionRiesgo(clsRiesgo + "-" + clasRiesgo);
						} else {
							clienteTarget.setClasificacionRiesgo(clsRiesgo);
						}

					} catch (Exception e) {
						log.error("Error al sacar la descripcion del la clasificacion de riesgo #0",e.getMessage());
						clienteTarget.setClasificacionRiesgo(clsRiesgo);
					}

					log.info("No Sabemos si hay error");
					
					BigDecimal ventasTotal12Meses = (BigDecimal) functionDatosClientes.getExportParameterList().getValue("VTATOTAL12");
					clienteTarget.setVentasTotal12Meses(new BigDecimal(ventasTotal12Meses.longValue() * 100));
					clienteTarget.setCreacionSap((Date) functionDatosClientes.getExportParameterList().getValue("FECCREACION"));
					clienteTarget.setSeguro((String) datosCliente.getValue("TIPSEGURO"));
					clienteTarget.setVigenciaSeguro((String) datosCliente.getValue("VIGSEGURO"));
					clienteTarget.setMontoSeguro((BigDecimal) functionDatosClientes.getExportParameterList().getValue("MTOSEGURO_P"));
					clienteTarget.setMontoSeguroUf(((BigDecimal) functionDatosClientes.getExportParameterList().getValue("MTOSEGURO")).longValue());

					BigDecimal ventasProm12Meses = (BigDecimal) datosCliente.getValue("VTAPROM12");
					clienteTarget.setVentasProm12Meses(new BigDecimal(ventasProm12Meses.longValue() * 100));
					String facprom12 = (String) datosCliente.getValue("FACPROM12");
					if (facprom12 != null) {
						clienteTarget.setFactProm12Meses(Long.parseLong(facprom12));
					}
					String cantMesesVentas = (String) functionDatosClientes.getExportParameterList().getValue("NUMMESES");
					if (cantMesesVentas != null) {
						clienteTarget.setCantMesesVentas(Long.parseLong(cantMesesVentas));
					}

					ObtenerEstadoSolictud("I");
					if (estadoInicial != null) {
						clienteTarget.setEstadoSolicitud(estadoInicial.getDesEstado());
					}

					/* Se setean lo valores de comportamiento comercial */
					log.info("Veamos si hay error");
					clienteCompComrcial = new CompComercialDTO();
					log.info("Valor Monto Moroso: " + String.valueOf(functionDatosClientes.getExportParameterList().getValue("MTOMOROSIDAD")));
					clienteCompComrcial.setMontoTotalMoro(((BigDecimal) functionDatosClientes.getExportParameterList().getValue("MTOMOROSIDAD")).longValue());
					
					String numeroMoro = (String) functionDatosClientes.getExportParameterList().getValue("CANMOROSIDAD");
					if (numeroMoro != null) {
						clienteCompComrcial.setNumeroMoro(Long.parseLong(numeroMoro));
					}
					String diasMoras = (String) functionDatosClientes.getExportParameterList().getValue("DIASMORA");
					if (diasMoras != null) {
						clienteCompComrcial.setDiasMoras(Long.parseLong(diasMoras));
					}

					clienteCompComrcial.setMontoTotalProt(((BigDecimal) functionDatosClientes.getExportParameterList().getValue("MTOPROTESTO")).longValue());
					String numeroProt = (String) functionDatosClientes.getExportParameterList().getValue("CANPROTESTO");
					if (numeroProt != null) {
						clienteCompComrcial.setNumeroProt(Long.parseLong(numeroProt));
					}
					String dmPort = (String) functionDatosClientes.getExportParameterList().getValue("CNTDM");
					if (dmPort != null) {
						clienteCompComrcial.setDmPort("0"); // dmPort
					}

					//JCoFunction funcPedidos = connect.getFunction("ZSDFN_FRONT_PEDCOT_R");
					JCoFunction funcPedidos = connect.getFunction("ZSDFN_FRONT_PEDCOT_RR");
					log.info("RUT: "+ clienteTarget.getCleanRut());
					log.info("CODIGO_SOCIEDAD:  "+ this.getSociedadAux().getCodigoSociedad());
					funcPedidos.getImportParameterList().setValue("RUTCLIENTE",clienteTarget.getCleanRut());
					funcPedidos.getImportParameterList().setValue("SOCIEDAD",this.getSociedadAux().getCodigoSociedad());
					connect.execute(funcPedidos);

					if (funcPedidos.getTableParameterList().getTable("DETALLE").getNumRows() == 0) {
						funcPedidos.getImportParameterList().setValue("RUTCLIENTE",clienteTarget.getCleanRut().toUpperCase());
						funcPedidos.getImportParameterList().setValue("SOCIEDAD",this.getSociedadAux().getCodigoSociedad());
						connect.execute(funcPedidos);
					}

					long flujoPedidoProceso = 0;
					JCoTable pedidosEnProceo = funcPedidos.getTableParameterList().getTable("DETALLE");
					log.debug(pedidosEnProceo);
					for (int i = 0; i < pedidosEnProceo.getNumRows(); i++) {
						pedidosEnProceo.setRow(i);
						String tipo = pedidosEnProceo.getString(1);
						if (tipo.toLowerCase().equals("p")) {
							flujoPedidoProceso += pedidosEnProceo.getBigDecimal("MONTO").longValue();
							log.debug(flujoPedidoProceso);
						}
					}

					log.info("monto : " + flujoPedidoProceso);
					clienteTarget.setPedidoEnProceso(flujoPedidoProceso);

					/* se crean el obejto de exposicion */
					clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();

					/* se recorre la tabla para sacar los DM */
					JCoTable DMCliente = functionDatosClientes.getTableParameterList().getTable("DM_CLIENTE"); /* indicas
																				// la
																				// tabla
																				// de
																				// la
																				// cual
																				// necesitas
																				// extraer
																				// datos */
					if (DMCliente != null && DMCliente.getNumRows() > 0) {
						int contador = 0;
						listaDescripcionDM = new ArrayList<String>(0);
						listaDmdtos = new ArrayList<DMDTO>(0);
						for (int i = 0; i < DMCliente.getNumRows(); i++) {
							DMDTO nuevo = new DMDTO();
							DMCliente.setRow(i);
							String codigoStr = (String) DMCliente.getValue("CODCLIENTE");
							String descripcion = (String) DMCliente.getValue("NAME1");
							String vwer = (String) DMCliente.getValue("VWERK");
							if (codigoStr != null && descripcion != null && vwer != null) {
								listaDescripcionDM.add(codigoStr + "-" + descripcion);
								nuevo.setCodigoStr(codigoStr);
								nuevo.setDescripcion(descripcion);
								nuevo.setVwer(vwer);
								listaDmdtos.add(nuevo);
								if (contador == 0)
									dm = codigoStr + "-" + descripcion;

								nuevo = null;
								contador++;

							}
							log.error("codigo de cliente #0, descripcion de #1, vwer #2 ",codigoStr, descripcion, vwer);
						}
					} else {
						listaDescripcionDM = new ArrayList<String>(0);
						listaDmdtos = new ArrayList<DMDTO>(0);
						DMDTO nuevo = new DMDTO();
						nuevo.setCodigoStr(this.rutAux);
						nuevo.setDescripcion(clienteTarget.getRazonSocial());
						nuevo.setVwer("");
						listaDescripcionDM.add(nuevo.getCodigoStr() + "-" + nuevo.getDescripcion());
						dm = nuevo.getCodigoStr() + "-"	+ nuevo.getDescripcion();
					}
					setMuestraSeleccionSolicitud(true);
				} else {
					if ("N".equals(swExiste)) {
						FacesMessages.instance().add(Severity.INFO, "Cliente No Existe, Verifique...");
						this.mensajeGlogal = "Cliente No Existe, Verifique...";
					} else if ("A".equals(swExiste)) {
						FacesMessages.instance().add(Severity.INFO,"Cliente No Es Solicitante, Verifique...");
						this.mensajeGlogal = "Cliente No Es Solicitante, Verifique...";
					} else if ("B".equals(swExiste)) {
						FacesMessages.instance().add(Severity.INFO,"Cliente No Es Kupfer Express Verifique...");
						this.mensajeGlogal = "Cliente No Es Kupfer Express Verifique...";
					}
				}
			} else {
				FacesMessages.instance().add(Severity.INFO,"No se pudo traer los datos de SAP para el cliente #0...",this.rutAux);
				this.mensajeGlogal = "No se pudo traer los datos de SAP para el cliente "+ this.rutAux;
			}
		} catch (Exception e) {
			log.debug("error al cargar datos cliente desde sap#0", e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public void cambiarSolicitud(String modulo) {
		log.debug("modulo #0", modulo);
		LcredTipoSolicitud tipo = dataTipoSolicitud.get(modulo);
		if (tipo != null) {
			this.setTituloIngreso(tipo.getDesTipoSolicitud());
			this.setPaginaIngreso(Integer.parseInt(tipo.getCodTipoSolicitud()));
			tipoSolicitud = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(this.paginaIngreso));

			if (this.paginaIngreso == 11 || this.paginaIngreso == 12) {
				setTituloGeneral("Ventas");
				if (this.paginaIngreso == 11) {
					try {
						listaTipoFormaPagos = (List<TipoFormaPago>) entityManager.createQuery("select tfp from TipoFormaPago tfp ").getResultList();

					} catch (Exception e) {
						log.error("Error, de tipo de forma de pagos #0",e.getMessage());
					}
					this.tipoSolicitudCodigo = 1;
				} else if (this.paginaIngreso == 12) {
					try {
						listaTipoFormaPagos = (List<TipoFormaPago>) entityManager.createQuery(
										"select tfp from TipoFormaPago tfp where tfp.idTipoFormaPago not in (2) ").getResultList();
					} catch (Exception e) {
						log.error("Error, de tipo de forma de pagos #0",e.getMessage());
					}
					this.tipoSolicitudCodigo = 1;
				}

				habilitarVerProductos = true;
			} else if (this.paginaIngreso == 21 || this.paginaIngreso == 22 || this.paginaIngreso == 23 || this.paginaIngreso == 24) {
				setTituloGeneral("Línea de Crédito");

				if (listaClsRiesgos == null || listaClsRiesgos.size() == 0) {
					listaClsRiesgos = intranetSapService.getClasificacionRiesgo();
				}
				if (listaCondicionPagos == null || listaCondicionPagos.size() == 0) {
					listaCondicionPagos = intranetSapService.getCondicionPago();
				}
				evaluarInnominalNominalRKupferAseguradora();
				this.tipoSolicitudCodigo = 2;

			} else if (this.paginaIngreso == 31 || this.paginaIngreso == 32) {

				setTituloGeneral("Cambio de Condiciones");

				if (listaClsRiesgos == null || listaClsRiesgos.size() == 0) {
					listaClsRiesgos = intranetSapService.getClasificacionRiesgo();
				}
				if (listaCondicionPagos == null	|| listaCondicionPagos.size() == 0) {
					listaCondicionPagos = intranetSapService.getCondicionPago();
				}

				if (this.paginaIngreso == 31) {
					setCondicionRiesgo(true);
					setComboCRiesgo(false);
					setCondicionPago(false);
					setComboCPago(true);
				} else if (this.paginaIngreso == 32) {
					setCondicionRiesgo(false);
					setComboCRiesgo(true);
					setCondicionPago(true);
					setComboCPago(false);
				}
				evaluarRiesgoPago();
				this.tipoSolicitudCodigo = 3;
			} else if (this.paginaIngreso == 41 || this.paginaIngreso == 42 || this.paginaIngreso == 43 || this.paginaIngreso == 44) {
				if (this.paginaIngreso == 41 || this.paginaIngreso == 42) {
					setTituloGeneral("Bloqueo y Desbloqueo.");
					setMotivoBloqueoDesbloqueo(false);
				} else if (this.paginaIngreso == 43) {
					setTituloGeneral("Creación de DM.");
					setMotivoBloqueoDesbloqueo(true);

				} else if (this.paginaIngreso == 44) {
					setTituloGeneral("Prorroga de Cheques.");
				}
				this.tipoSolicitudCodigo = 4;
			}
		}
		/* limpiar variables por cvada cambio de pestaña */
		limpiarSegunTipoSolictud(this.paginaIngreso);
	}

	public void evaluarInnominalNominalRKupferAseguradora() {
		setClsRiesgo(null);
		setcPago(null);
		String codigoClsRiesgo = null;
		String codigoCdnPago = null;
		listaStringClsRiesgos = new ArrayList<String>(0);
		listaStringCdnPagos = new ArrayList<String>(0);
		try {
			if (this.paginaIngreso == 21) {
				codigoClsRiesgo = "K34";
				activarCboClsRiesgo = false;
				codigoCdnPago = "CC14";
				activarCboCdnPago = false;
			} else if (this.paginaIngreso == 22) {
				codigoClsRiesgo = clienteTarget.getCodigoclasificacionRiesgo();
				activarCboClsRiesgo = false;
				codigoCdnPago = clienteTarget.getCodigoCondicionPago();
				activarCboCdnPago = false;
			} else if (this.paginaIngreso == 23) {
				codigoClsRiesgo = "004";
				activarCboClsRiesgo = false;
				codigoCdnPago = "CC14";
				activarCboCdnPago = false;
			} else if (this.paginaIngreso == 24) {
				// 008 = cliente antiguo --- 005 = cliente nuevo
				codigoClsRiesgo = "005";
				activarCboClsRiesgo = false;
				codigoCdnPago = "CC01";
				activarCboCdnPago = false;
			}

			/* recorrer la lista para encontrar los codigo */
			if (this.listaClsRiesgos != null) {
				for (ClsRiesgoDTO cls : this.listaClsRiesgos) {
					// log.error("codigo de la clasificacion de riesgo #0",
					// cls.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((cls.getCodigo().trim()));
					cadena.append("-");
					cadena.append((cls.getDescripcion().trim()));
					if (this.paginaIngreso != 24) {
						if (cls != null
								&& (cls.getCodigo().trim())
										.equals(codigoClsRiesgo)) {
							// log.error("clasificacion de riesgo #0",
							// cadena.toString());
							setClsRiesgo(cadena.toString());
						}
						listaStringClsRiesgos.add(cadena.toString());
						cadena = null;
					} else {
						if (cls != null
								&& ((cls.getCodigo().trim()).equals("005") || (cls
										.getCodigo().trim()).equals("008"))) {
							// log.error("clasificacion de riesgo #0",
							// cadena.toString());
							if (this.clsRiesgo == null) {
								setClsRiesgo(cadena.toString());
								listaStringClsRiesgos.add(cadena.toString());
							} else {
								listaStringClsRiesgos.add(cadena.toString());
							}
							cadena = null;
						}
					}
				}
			}

			if (this.listaCondicionPagos != null) {
				for (CPagoDTO obj : this.listaCondicionPagos) {
					// log.error("codigo de la condicion de riesgo #0",
					// obj.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((obj.getCodigo().trim()));
					cadena.append("-");
					cadena.append((obj.getDescripcion().trim()));
					// log.debug("|#0| |#1|", obj.getCodigo().trim(),
					// obj.getDescripcion().trim());
					// for(int i = 0; i < obj.getDescripcion().length(); i++ ) {
					// char c = obj.getDescripcion().charAt(i);
					// log.debug("valor #0 is whitespace #1", c,
					// Character.isWhitespace(c));
					// }
					if (this.paginaIngreso != 24) {
						if (obj != null
								&& obj.getCodigo().equals(codigoCdnPago)) {
							// log.error("condicion de riesgo #0",
							// cadena.toString());
							setcPago(cadena.toString());
						}
						listaStringCdnPagos.add(cadena.toString());
						cadena = null;
					} else if (this.paginaIngreso == 24) {
						if (obj != null
								&& obj.getCodigo().equals(codigoCdnPago)) {
							// log.error("condicion de riesgo #0",
							// cadena.toString());
							setcPago(cadena.toString());
							listaStringCdnPagos.add(cadena.toString());
							cadena = null;
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Error, a evaluar los combo #0", e.getMessage());
		}

	}

	public void evaluarRiesgoPago() {
		setClsRiesgo(null);
		setcPago(null);
		String codigoClsRiesgo = null;
		String codigoCdnPago = null;
		listaStringClsRiesgos = new ArrayList<String>(0);
		listaStringCdnPagos = new ArrayList<String>(0);
		try {
			if (this.paginaIngreso == 31) {
				codigoClsRiesgo = "";
				activarCboClsRiesgo = false;
				codigoCdnPago = "";
				activarCboCdnPago = false;
			} else if (this.paginaIngreso == 32) {
				codigoClsRiesgo = "";
				activarCboClsRiesgo = false;
				codigoCdnPago = "";
				activarCboCdnPago = false;
			}

			/* recorrer la lista para encontrar los codigo */
			if (this.listaClsRiesgos != null) {
				for (ClsRiesgoDTO cls : this.listaClsRiesgos) {
					// log.error("codigo de la clasificacion de riesgo #0",
					// cls.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((cls.getCodigo().trim()));
					cadena.append("-");
					cadena.append((cls.getDescripcion().trim()));
					if (cls != null
							&& (cls.getCodigo().trim()).equals(codigoClsRiesgo)) {
						// log.error("clasificacion de riesgo #0",
						// cadena.toString());
						setClsRiesgo(cadena.toString());
					}
					listaStringClsRiesgos.add(cadena.toString());
					cadena = null;

				}
			}

			if (this.listaCondicionPagos != null) {
				for (CPagoDTO obj : this.listaCondicionPagos) {
					// log.error("codigo de la condicion de riesgo #0",
					// obj.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((obj.getCodigo().trim()));
					cadena.append("-");
					cadena.append((obj.getDescripcion().trim()));
					if (obj != null && obj.getCodigo().equals(codigoCdnPago)) {
						// log.error("condicion de riesgo #0",
						// cadena.toString());
						setcPago(cadena.toString());
					}
					listaStringCdnPagos.add(cadena.toString());
					cadena = null;
				}
			}

		} catch (Exception e) {
			log.error("Error, a evaluar los combo #0", e.getMessage());
		}

	}

	/* agregar usuario correo */
	public void agregarUsuarioCorreoListaUsuarioCorreoReserva() {
		if (this.usuarioCorreo != null) {
			String array[] = this.usuarioCorreo.split(".-");
			UsuarioCorreoDTO usuCorreoAux = buscarUsuarioCorreo(array[0]);
			if (usuCorreoAux != null) {
				if (listaUsuarioCorreoagregados.contains(usuCorreoAux)) {
					setMensajeExplicativo("El usuario " + this.usuarioCorreo
							+ " ya fue agregado a la lista.");
					this.usuarioCorreo = null;
				} else {
					usuCorreoAux.setStatus(true);
					listaUsuarioCorreoagregados.add(usuCorreoAux);
					this.usuarioCorreo = null;
					this.mensajeExplicativo = null;
				}
			}
		} else {
			setMensajeExplicativo("Debe seleccionar el Usuario.");
		}
	}

	public void eliminarUsuarioCorreoListaAgregado(
			UsuarioCorreoDTO usuarioCorreoAux) {
		if (usuarioCorreoAux != null) {
			listaUsuarioCorreoagregados.remove(usuarioCorreoAux);
		}
	}

	public UsuarioCorreoDTO buscarUsuarioCorreo(String codigo) {
		UsuarioCorreoDTO usuarioCorreo = null;
		if (listaUsuarioCorreos != null && codigo != null) {
			for (UsuarioCorreoDTO u : listaUsuarioCorreos) {
				if (u.getCorrelativo() == Integer.parseInt(codigo)) {
					usuarioCorreo = u;
					break;
				}
			}
		}
		return usuarioCorreo;
	}

	public void agregarAlaListaCorreo() {
		List<String> listaCodigoNegocio = new ArrayList<String>(0);
		List<String> listaCodigoConcepto = new ArrayList<String>(0);

		if (paginaIngreso == 11 || paginaIngreso == 12) {

			if (listaCotPedDTOs != null) {
				for (CotPedDTO cpd : listaCotPedDTOs) {
					if (cpd.getListaCabeceraCotPeds() != null) {
						for (CabeceraCotPedDTO ccpd : cpd.getListaCabeceraCotPeds()) {
							if (ccpd.getNegocio() != null) {
								log.debug("negocio #0", ccpd.getNegocio().getNegocio()+ "-"+ ccpd.getNegocio().getDesNegocio());
								if (!listaCodigoNegocio.contains(ccpd.getNegocio().getNegocio())) {
									listaCodigoNegocio.add((ccpd.getNegocio().getNegocio()));
								}
							}
						}
					}
				}
				for (CotPedDTO cpd : listaCotPedDTOs) {
					if (cpd.getListaCabeceraCotPeds() != null) {
						for (CabeceraCotPedDTO ccpd : cpd.getListaCabeceraCotPeds()) {
							if (ccpd.getNegocio() != null) {
								for (DetalleCotPedDTO dcpd : ccpd.getListaDetalle()) {
									log.debug("concepto #0", dcpd.getNegocio().getConcepto()+ "-"+ dcpd.getNegocio().getDescripcion());
									if (!listaCodigoConcepto.contains(dcpd.getNegocio().getConcepto())) {
										listaCodigoConcepto.add((dcpd.getNegocio().getConcepto()));
									}
								}
							}
						}
					}
				}
			}
		} else if (paginaIngreso == 21 || paginaIngreso == 22 || paginaIngreso == 23) {
			if (listaSelConceptoNegocio != null) {
				for (ConceptoNegocioDTO cnd : listaSelConceptoNegocio) {
					if (cnd.getConceptoNegocio() != null) {
						log.debug("negocio #0", cnd.getConceptoNegocio().getNegocio()+ "-" + cnd.getConceptoNegocio().getDesNegocio());
						if (!listaCodigoNegocio.contains(cnd.getConceptoNegocio().getNegocio())) {
							listaCodigoNegocio.add((cnd.getConceptoNegocio().getNegocio().trim()));
						}
					}
				}
			}
		}

		int contador = 1;
		UsuarioCorreoDTO obj1 = null;
		boolean asociadoAux = false; 
		
		try {
			if (listaDestinatarios != null) {
				for (DestinatarioDTO dto : listaDestinatarios) {
					if(this.sociedadAux != null){
						asociadoAux = scoringService.verificarUsuarioSociedades(dto, this.sociedadAux);
						log.info("asociadoAux : ", asociadoAux);
					}
					if (dto != null) {
						UsuarioSegurDTO usuarioSegur = scoringService.sacarDatosSessionUsuario(dto.getUsername());
						if (usuarioSegur != null) {
							if (usuarioSegur.getIdPersonal() == 3 || usuarioSegur.getIdPersonal() == 62	|| usuarioSegur.getIdPersonal() == 179 || usuarioSegur.getIdPersonal() == 273
							 || usuarioSegur.getIdPersonal() == 377 || usuarioSegur.getIdPersonal() == 518	|| usuarioSegur.getIdPersonal() == 568 || usuarioSegur.getIdPersonal() == 610
							 || usuarioSegur.getIdPersonal() == 611	|| usuarioSegur.getIdPersonal() == 1069	|| usuarioSegur.getIdPersonal() == 1171
							 || usuarioSegur.getIdPersonal() == 1237	|| usuarioSegur.getIdPersonal() == 1384	|| usuarioSegur.getIdPersonal() == 1395) {

								log.debug("usuario #0",	usuarioSegur.getNombre());

							}
						}
						if (dto.getTipoSolicitud().equals("1")	|| dto.getTipoSolicitud().equals("2") || dto.getTipoSolicitud().equals("3")	|| dto.getTipoSolicitud().equals("5")) {
							if (usuarioSegur != null) {
								if (scoringService.obtenerHabilitacionPorSucursal(usuarioSegur.getIdPersonal(),sucursal.getCodigo(),clienteTarget.getCodigoCanal().ordinal())) {
									if (paginaIngreso == 11 || paginaIngreso == 12 || paginaIngreso == 21 || paginaIngreso == 22 || paginaIngreso == 23) {
										boolean validoNegocio = false;
										boolean validoConcepto = false;
										// List<Integer> listaPerfiles =
										// scoringService.obtenerPerfilesDelUsuario(usuarioSegur.getIdPersonal());
										List<FuncionesType> listafunciones = scoringService.obtenerFuncionesUsuario(usuarioSegur.getIdPersonal());
										if (listaCodigoNegocio != null	&& listaCodigoNegocio.size() != 0) {
											boolean quebreFuera = false;
											for (String codigo : listaCodigoNegocio) {
												if (listafunciones != null) {
													for (FuncionesType ft : listafunciones) {
														if (ft != null && ft.ordinal() != 0) {
															log.debug("codigo de usuario: #0 y nombre  #1",	usuarioSegur.getIdPersonal(),usuarioSegur.getNombre());
															log.debug("negocio #0",	codigo);
															log.debug("canal #0  y codigo canal #1",clienteTarget.getCodigoCanal(),clienteTarget.getCodigoCanal().ordinal());
															log.debug("funcion  #0 y codigo de funcion #1",ft,ft.ordinal());

															validoNegocio = scoringService.obtenerHabilitacionUsuario(usuarioSegur.getIdPersonal(),
																													  codigo.trim(),
																													  clienteTarget.getCodigoCanal().ordinal(),
																													  ft.ordinal());

															log.debug("resultado #0",validoNegocio);
															if (validoNegocio) {
																quebreFuera = true;
																break;
															}
														}
													}
													if (quebreFuera) {
														break;
													}
												}
											}
										}
										if (listaCodigoConcepto != null	&& listaCodigoConcepto.size() != 0) {
											boolean quebreFuera = false;
											for (String codigo : listaCodigoConcepto) {
												if (listafunciones != null) {
													for (FuncionesType ft : listafunciones) {
														if (ft != null && ft.ordinal() != 0) {
															log.debug("codigo de usuario: #0 y nombre  #1",usuarioSegur.getIdPersonal(),usuarioSegur.getNombre());
															log.debug("negocio #0",	codigo);
															log.debug("canal #0  y codigo canal #1",clienteTarget.getCodigoCanal(),clienteTarget.getCodigoCanal().ordinal());
															log.debug("funcion  #0 y codigo de funcion #1",ft,ft.ordinal());

															validoConcepto = scoringService.obtenerHabilitacionUsuario(usuarioSegur.getIdPersonal(),codigo.trim(),
																			clienteTarget.getCodigoCanal().ordinal(),ft.ordinal());

															log.debug("resultado #0",validoConcepto);
															if (validoConcepto) {
																quebreFuera = true;
																break;
															}
														}
													}

													if (quebreFuera) {
														break;
													}

												}
											}
										}

										if (paginaIngreso == 11	|| paginaIngreso == 12) {
												if (validoNegocio == true && validoConcepto == true) {
													obj1 = new UsuarioCorreoDTO();
													obj1.setStatus(false);
													obj1.setCorrelativo(contador);
													obj1.setNombreUsuario(usuarioSegur.getNombre());
													obj1.setAlias(usuarioSegur.getAlias());
													obj1.setCorreoElectronico(usuarioSegur.getCorreo().trim()); // UsuarioCorreoDTO
													Usuarios usuario = entityManager.find(Usuarios.class,(usuarioSegur.getAlias()).trim());
													obj1.setUsuario(usuario);
													if(asociadoAux){
														listaUsuarioCorreoagregados.add(obj1);
														contador++;
														obj1 = null;
													}
												} else {
													if (usuarioSegur.isObligatorio()) {
														obj1 = new UsuarioCorreoDTO();
														obj1.setStatus(false);
														obj1.setCorrelativo(contador);
														obj1.setNombreUsuario(usuarioSegur.getNombre());
														obj1.setAlias(usuarioSegur.getAlias());
														obj1.setCorreoElectronico(usuarioSegur.getCorreo().trim()); // UsuarioCorreoDTO
														Usuarios usuario = entityManager.find(Usuarios.class,(usuarioSegur.getAlias()).trim());
														obj1.setUsuario(usuario);
														listaUsuarioCorreoagregados.add(obj1);
														contador++;
														obj1 = null;
													}
												}
											 
										} else if (paginaIngreso == 21 || paginaIngreso == 22 || paginaIngreso == 23) {
											if (validoNegocio == true) {
												obj1 = new UsuarioCorreoDTO();
												obj1.setStatus(false);
												obj1.setCorrelativo(contador);
												obj1.setNombreUsuario(usuarioSegur.getNombre());
												obj1.setAlias(usuarioSegur.getAlias());
												obj1.setCorreoElectronico(usuarioSegur.getCorreo().trim()); // UsuarioCorreoDTO
												Usuarios usuario = entityManager.find(Usuarios.class,(usuarioSegur.getAlias()).trim());
												obj1.setUsuario(usuario);
												if(asociadoAux){
													listaUsuarioCorreoagregados.add(obj1);
													contador++;
													obj1 = null;
												}
											} else {
												if (usuarioSegur.isObligatorio()) {
													obj1 = new UsuarioCorreoDTO();
													obj1.setStatus(false);
													obj1.setCorrelativo(contador);
													obj1.setNombreUsuario(usuarioSegur.getNombre());
													obj1.setAlias(usuarioSegur.getAlias());
													obj1.setCorreoElectronico(usuarioSegur.getCorreo().trim()); // UsuarioCorreoDTO
													Usuarios usuario = entityManager.find(Usuarios.class,(usuarioSegur.getAlias()).trim());
													obj1.setUsuario(usuario);
													listaUsuarioCorreoagregados.add(obj1);
													contador++;
													obj1 = null;
												} else {
													log.debug("usuario que no cumple con los negocios #0",usuarioSegur.getNombre());
												}

											}
										} else {
											UsuarioCargo user = scoringService.getUsuarioCargo(usuarioSegur.getIdPersonal());
											if (user != null && user.getEnvioAutomatico() != null && user.getEnvioAutomatico().booleanValue() == true) {
												obj1 = new UsuarioCorreoDTO();
												obj1.setStatus(false);
												obj1.setCorrelativo(contador);
												obj1.setNombreUsuario(usuarioSegur.getNombre());
												obj1.setAlias(usuarioSegur.getAlias());
												obj1.setCorreoElectronico(usuarioSegur.getCorreo().trim()); // UsuarioCorreoDTO
												Usuarios usuario = entityManager.find(Usuarios.class,(usuarioSegur.getAlias()).trim());
												obj1.setUsuario(usuario);
												if(asociadoAux){
													listaUsuarioCorreoagregados.add(obj1);
													contador++;
													obj1 = null;
												}
											} else {
												log.debug("usuario que no cumple con los negocios #0",usuarioSegur.getNombre());
											}
										}
									} else {
										boolean validoCanalVenta = true; // scoringService.obtenerAsignacionCanalVentaDelUsuario(usuarioSegur.getIdPersonal(),
																			// clienteTarget.getCodigoCanal().ordinal());
										if (validoCanalVenta) {
											UsuarioCargo usuarioCargoPrueba = scoringService.getUsuarioCargo(usuarioSegur.getIdPersonal());
											if (usuarioCargoPrueba != null) {
												if (usuarioCargoPrueba.getEnvioAutomatico().booleanValue() == true) {
													obj1 = new UsuarioCorreoDTO();
													obj1.setStatus(false);
													obj1.setCorrelativo(contador);
													obj1.setNombreUsuario(usuarioSegur.getNombre());
													obj1.setAlias(usuarioSegur.getAlias());
													obj1.setCorreoElectronico(usuarioSegur.getCorreo().trim()); // UsuarioCorreoDTO
													Usuarios usuario = entityManager.find(Usuarios.class,(usuarioSegur.getAlias()).trim());
													obj1.setUsuario(usuario);
													if(asociadoAux){
														listaUsuarioCorreoagregados.add(obj1);
														contador++;
														obj1 = null;
													}
												} else {
													log.debug("usuario que no cumple con los negocios #0",usuarioSegur.getNombre());
												}
											}
										} else {
											if (usuarioSegur.isObligatorio()) {
												obj1 = new UsuarioCorreoDTO();
												obj1.setStatus(false);
												obj1.setCorrelativo(contador);
												obj1.setNombreUsuario(usuarioSegur.getNombre());
												obj1.setAlias(usuarioSegur.getAlias());
												obj1.setCorreoElectronico(usuarioSegur.getCorreo().trim()); // UsuarioCorreoDTO
												Usuarios usuario = entityManager.find(Usuarios.class,(usuarioSegur.getAlias()).trim());
												obj1.setUsuario(usuario);
												listaUsuarioCorreoagregados.add(obj1);
												contador++;
												obj1 = null;
											} else {
												UsuarioCargo usuarioCargoPrueba = scoringService.getUsuarioCargo(usuarioSegur.getIdPersonal());
												if (usuarioCargoPrueba != null) {
													if (usuarioCargoPrueba.getEnvioAutomatico().booleanValue() == true) {
														obj1 = new UsuarioCorreoDTO();
														obj1.setStatus(false);
														obj1.setCorrelativo(contador);
														obj1.setNombreUsuario(usuarioSegur.getNombre());
														obj1.setAlias(usuarioSegur.getAlias());
														obj1.setCorreoElectronico(usuarioSegur.getCorreo().trim()); // UsuarioCorreoDTO
														Usuarios usuario = entityManager.find(Usuarios.class,(usuarioSegur.getAlias()).trim());
														obj1.setUsuario(usuario);
														if(asociadoAux){
															listaUsuarioCorreoagregados.add(obj1);
															contador++;
															obj1 = null;
														}
													}
												} else {
													log.debug("usuario que no cumple con los negocios #0",usuarioSegur.getNombre());
												}
											}
										}
									}
								}
							}
						} else {
							if (usuarioSegur != null) {
								obj1 = new UsuarioCorreoDTO();
								obj1.setStatus(false);
								obj1.setCorrelativo(contador);
								obj1.setNombreUsuario(usuarioSegur.getNombre());
								obj1.setAlias(usuarioSegur.getAlias());
								obj1.setCorreoElectronico(usuarioSegur.getCorreo().trim()); // usuarioSegur.getCorreo()
								Usuarios usuario = entityManager.find(Usuarios.class,(usuarioSegur.getAlias()).trim());
								obj1.setUsuario(usuario);
								if(asociadoAux){
									contador++;
									listaUsuarioCorreos.add(obj1);
									listaDescripcionUsuariosCorreos.add(String.valueOf(obj1.getCorrelativo())+ ".-"	+ obj1.getNombreUsuario());
									obj1 = null;
								}
							} else {
								log.debug("usuario que no cumple con los negocios #0",usuarioSegur.getNombre());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error al sacar los datos del usaurio #0", e.getMessage());
		}
		agregarCorreosObligatorios();
	}

	public void agregarCorreosObligatorios() {
		List<UsuarioCorreoDTO> listaUsuarios = scoringService.getListaUsuariosSegur();
		boolean exiete = false;
		try {
			if (listaUsuarios != null) {
				for (UsuarioCorreoDTO uc : listaUsuarios) {
					for (UsuarioCorreoDTO uc2 : listaUsuarioCorreoagregados) {
						if (uc.getAlias().trim().equals(uc2.getAlias().trim())) {
							exiete = true;
						}
					}
					if (exiete == false) {
						listaUsuarioCorreoagregados.add(uc);
					} else {
						exiete = false;
					}
				}
			}
			log.debug(listaUsuarioCorreoagregados.size());
		} catch (Exception e) {
			log.error("Error al agrehar los acorreod obligatorios #1",e.getMessage());
		}
	}

	/* Archivos Adjunto */
	/*
	 * private List<UploadItem> uploadFile = new ArrayList<UploadItem>();
	 * 
	 * @BypassInterceptors public List<UploadItem> getUploadFile() {return
	 * uploadFile; }
	 * 
	 * @BypassInterceptors public void setUploadFile(List<UploadItem>
	 * uploadFile) { this.uploadFile = uploadFile; }
	 */

	/*
	 * public void asignarArchivosParaListas() { log.debug("Tama?o lista #0",
	 * uploadFile.size()); if(uploadFile.size() > 0){ guardarArchivo();
	 * agregarArchivos(); } }
	 */
	/************************** Adjuntar archivos ***********************/

	private List<UploadItem> uploadFile = new ArrayList<UploadItem>();
	private String nombreArchivoTarget = new String();

	public List<UploadItem> getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(List<UploadItem> uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getNombreArchivoTarget() {
		return nombreArchivoTarget;
	}

	public void setNombreArchivoTarget(String nombreArchivoTarget) {
		this.nombreArchivoTarget = nombreArchivoTarget;
	}

	public void guardarArchivo(String carpeta) {

		if (uploadFile.size() > 0) {
			if (uploadFile == null || uploadFile.isEmpty()) {
				FacesMessages.instance().add(Severity.ERROR,"El archivo no ha sido cargado");
				return;
			}
			if (uploadFile.get(0) == null) {
				FacesMessages.instance().add(Severity.ERROR,"El archivo no ha sido cargado");
				return;
			}
			if (listaFileUploadedDTOs != null && listaFileUploadedDTOs.size() > 0) {
				for (FileUploadedDTO file : listaFileUploadedDTOs) {
					eliminarArchivo(file);
				}
				listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
			} else {
				listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
			}

			FileUploadedDTO file = null;
			for (UploadItem archivo : uploadFile) {
				file = new FileUploadedDTO();
				file.setTipo(this.tipoSolicitud);
				file.setUploadItem(archivo);
				file.setNombreArchivo(archivo.getFileName());
				try {
					if (archivo != null) {
						try {
							FileInputStream fin = new FileInputStream(archivo.getFile());
							byte fileContent[] = new byte[(int) archivo.getFileSize()];
							fin.read(fileContent);
							Attachment archivoAbjunto = new Attachment();
							archivoAbjunto.setContentType(archivo.getContentType());
							archivoAbjunto.setData(fileContent);
							archivoAbjunto.setName(archivo.getFileName());
							archivoAbjunto.setSize(archivo.getFileSize());
							entityManager.persist(archivoAbjunto);
							entityManager.flush();
							file.setArchivo(archivoAbjunto);
						} catch (Exception e) {
							FacesMessages.instance().add(Severity.WARN,"A ocurrido un error al momento de cargar el archivo para la solicitud.");
						}
					}
				} catch (Exception e) {
					log.error("Error, al gurdar los archivo temporalmente #0",e.getMessage());
				}
				listaFileUploadedDTOs.add(file);
				file = null;
			}
			uploadFile.clear();
			log.debug("size #0", nombreArchivoTarget);
		}
	}

	/******************************************************************/

	public void eliminarArchivo(FileUploadedDTO archivoAux) {
		if (archivoAux != null) {
			entityManager.remove(archivoAux.getArchivo());
			entityManager.flush();
			listaFileUploadedDTOs.remove(archivoAux);
		}
	}

	public void habilitarComboCondicion(String opcion) {
		if ("CondicionRiesgo".equals(opcion)) {
			if (comboCRiesgo) {
				comboCRiesgo = false;
			} else {
				comboCRiesgo = true;
			}
			setClsRiesgo(null);
		} else if ("CondicionPago".equals(opcion)) {
			if (comboCPago) {
				comboCPago = false;
			} else {
				comboCPago = true;
			}
			setcPago(null);
		}

		if (comboCRiesgo == false && this.clsRiesgo == null) {
			if (listaDestinatarios != null) {
				listaDestinatarios.clear();
			}
		} else if (comboCPago == false && this.cPago == null) {
			if (listaDestinatarios != null) {
				listaDestinatarios.clear();
			}
		}
	}

	public void crearVariableClienteDM() {
		LcredSolicitudDmId id = new LcredSolicitudDmId();
		clienteDM = new LcredSolicitudDm();
		id.setRut(this.clienteTarget.getRut());
		id.setNombre(this.clienteTarget.getRazonSocial());
		clienteDM.setId(id);

	}

	public void eliminarClienteDM(LcredSolicitudDm clienteDMDTO) {
		if (clienteDMDTO != null) {
			listaClienteDMs.remove(clienteDMDTO);
		}
	}

	public void limpiarClienteDM() {

		clienteDM = null;

	}

	public void crearVariableClienteNuevo() {
		try {
			clienteSck = new ClienteSck();
			listaProvincias = new ArrayList<Provincia>(0);
			listaComunas = new ArrayList<Comuna>(0);
			listaRegiones = scoringService.getRegiones();
			if (listaRegiones != null) {
				log.debug("cantidad de registro #0", listaRegiones.size());
			} else {
				listaRegiones = new ArrayList<Region>(0);

			}
			this.setRegion(null);
			this.setProvincia(null);
			this.setComuna(null);

		} catch (Exception e) {
			log.error(
					"Error, al sacar los datos de la creacion de cliente nuevo #0",
					e.getMessage());
		}
	}

	public void obtenerProvincias() {
		try {
			if (this.region != null) {
				this.setProvincia(null);
				listaProvincias = scoringService.getProvincias(this.region
						.getCodigo());
				if (listaProvincias != null) {
					log.debug("cantidad de registro #0", listaProvincias.size());
				} else {
					listaProvincias = new ArrayList<Provincia>(0);
				}
				this.setComuna(null);
				listaComunas = new ArrayList<Comuna>(0);
			}
		} catch (Exception e) {
			log.error("Error, al sacar las provincias segun la region #0",
					e.getMessage());
		}
	}

	public void obtenerComunas() {
		try {
			if (this.provincia != null) {
				this.setComuna(null);
				listaComunas = scoringService.getComunas(this.provincia
						.getCodigo());
				if (listaComunas != null) {
					log.debug("cantidad de registro #0", listaComunas.size());
				} else {
					listaComunas = new ArrayList<Comuna>(0);
					this.setComuna(null);
				}
			}
		} catch (Exception e) {
			log.error("Error, al sacar las comunas segun la provincia #0",
					e.getMessage());
		}
	}

	public void seleccionarComuna() {
		try {
			if (this.comuna != null) {
				log.debug("Comuna seleccioanda #0",
						this.comuna.getDescripcion());
			}
		} catch (Exception e) {
			log.error("Error, al seleccionar la comuna #0", e.getMessage());
		}
	}

	public void limpiarClienteNuevo() {
		clienteSck = null;
	}

	public void guardarClienteDMToListaClienteDM() {
		this.mensajeCreacionDm = null;
		if (clienteDM == null) {
			FacesMessages.instance().add(Severity.ERROR, "");
			this.mensajeCreacionDm = "El objeto de cliente DM no esta instanciado.";
			return;
		}
		if (clienteDM.getId().getRut() == null) {
			FacesMessages.instance().add(Severity.ERROR, "");
			this.mensajeCreacionDm = "El rut del cliente no se ingreso antes de guardar.";
			return;
		}
		if (clienteDM.getId().getNombre() == null) {
			FacesMessages.instance().add(Severity.ERROR, "");
			this.mensajeCreacionDm = "El nombre del cliente no se ingreso antes de guardar.";
			return;
		}
		if (clienteDM.getId().getOficinaVentas() == null) {
			FacesMessages.instance().add(Severity.ERROR, "");
			this.mensajeCreacionDm = "El oficina del cliente no se ingreso antes de guardar.";
			return;
		}
		if (clienteDM.getId().getZona() == null) {
			FacesMessages.instance().add(Severity.ERROR, "");
			this.mensajeCreacionDm = "El zona del cliente no se ingreso antes de guardar.";
			return;
		}
		if (clienteDM.getId().getListaPrecio() == null) {
			FacesMessages.instance().add(Severity.ERROR, "");
			this.mensajeCreacionDm = "Debe ingresar la lista de precio del cliente al guardar.";
			return;
		}
		if (clienteDM.getId().getSector() == null) {
			FacesMessages.instance().add(Severity.ERROR, "");
			this.mensajeCreacionDm = "Debe ingresar el sector del cliente del cliente al guardar.";
			return;
		}
		// if(clienteDM.getId().getVendTelefono() == null){
		// FacesMessages.instance().add(Severity.ERROR,"" );
		// this.mensajeCreacionDm =
		// "Debe ingresar rut o el telefono del cliente al guardar.";
		// return;
		// }
		// if(clienteDM.getId().getVendTerreno() == null){
		// FacesMessages.instance().add(Severity.ERROR,"" );
		// this.mensajeCreacionDm =
		// "Debe ingresar rut o el terreno del cliente al guardar.";
		// return;
		// }

		if (listaClienteDMs == null) {
			listaClienteDMs = new ArrayList<LcredSolicitudDm>(0);
			listaClienteDMs.add(clienteDM);
		} else {
			listaClienteDMs.add(clienteDM);
		}
		limpiarClienteDM();

		if (listaUsuarioCorreoagregados == null
				|| listaUsuarioCorreoagregados.size() == 0) {
			obtenerCorreoBloqueDesbloqueo();
		}
	}

	public void validarRuts(int opcion) {
		String rut = null;
		if (opcion == 1) {
			if (this.clienteDM.getId().getVendTelefono() != null) {
				rut = this.clienteDM.getId().getVendTelefono();
			}
		} else {
			if (this.clienteDM.getId().getVendTerreno() != null) {
				rut = this.clienteDM.getId().getVendTerreno();
			}
		}

		boolean exito = validarRut(rut, opcion);
		if (opcion == 1) {
			if (exito == false) {
				this.clienteDM.getId().setVendTelefono(null);
			}
		} else {
			if (exito == false) {
				this.clienteDM.getId().setVendTerreno(null);
			}
		}
	}

	public boolean validarRut(String rut, int opcion) {
		String nombre = null;
		if (opcion == 1) {
			nombre = "rut del vendedor ";
		} else {
			nombre = "rut del vendedor en terreno ";
		}

		if (rut != null) {
			int posicionGuion = rut.indexOf("-");
			if (posicionGuion < 0) {
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"El rut del "
										+ nombre
										+ " ingrsado no tienen el formato establecido (xxxxxxxx-x).");
				return false;
			}
			String largoDespuesGuion = rut.substring(posicionGuion + 1,
					rut.length());
			if (largoDespuesGuion != null && largoDespuesGuion.length() > 1) {
				FacesMessages.instance().add(
						Severity.ERROR,
						"El digito verificador del  " + nombre
								+ " no debe ser mas de dos caracteres.");
				return false;
			}
			if (globalService.validarRut(rut) == false) {
				FacesMessages.instance().add(Severity.ERROR,
						"El rut del " + nombre + " ingresado no es valido.");
				return false;
			}
		} else {
			FacesMessages.instance().add(Severity.ERROR,
					"No ha ingresado el rut del " + nombre + ".");
			return false;

		}
		return true;
	}

	public void cambioEstadoDoctoProrroga(DocumentoProrrogaDTO objeto) {
		if (objeto != null) {
			log.debug("numero documento #0", objeto.getNumeroDocto());
			log.debug("estado de  #0", objeto.isStatus());

		}

	}

	public void guardarEnLaListaDoctoProrrogaAgregados() {
		List<DocumentoProrrogaDTO> listaFinal = new ArrayList<DocumentoProrrogaDTO>(
				0);
		if (listaDoctoProrroga != null) {
			for (DocumentoProrrogaDTO dto : listaDoctoProrroga) {
				if (dto != null && dto.isStatus() == true) {
					DocumentoProrrogaDTO obj = revisarListaAgregadoDctoConFechaVencimientoNueva(dto
							.getNumeroDocto());
					if (obj != null) {
						listaFinal.add(obj);
					} else {
						dto.setFechaVencNuevo(null);
						listaFinal.add(dto);
					}
				}
			}
		}
		listaDoctoProrrogaAgregados.clear();
		setListaDoctoProrrogaAgregados(listaFinal);
	}

	public DocumentoProrrogaDTO revisarListaAgregadoDctoConFechaVencimientoNueva(
			String numero) {
		DocumentoProrrogaDTO obj = null;
		for (DocumentoProrrogaDTO dto : listaDoctoProrrogaAgregados) {
			if (dto != null && dto.getNumeroDocto().equals(numero)) {
				if (dto.getFechaVencNuevo() != null) {
					obj = dto;
					break;
				}
			}
		}
		return obj;
	}

	public void eliminarDocumentoProrroga(DocumentoProrrogaDTO docto) {
		List<DocumentoProrrogaDTO> listaNuevaDocto = new ArrayList<DocumentoProrrogaDTO>(
				0);
		if (docto != null) {
			listaDoctoProrrogaAgregados.remove(docto);
			for (DocumentoProrrogaDTO dto : listaDoctoProrroga) {
				if (dto != null
						&& dto.getNumeroDocto().equals(docto.getNumeroDocto())) {
					dto.setStatus(false);
					dto.setFechaVencNuevo(null);
				}
				listaNuevaDocto.add(dto);
			}
			listaDoctoProrroga.clear();
			setListaDoctoProrroga(listaNuevaDocto);
		}
	}

	public void verificarFechaVencimientoNuevoMayorFechaActual(
			DocumentoProrrogaDTO objeto) {
		boolean valido = false;
		List<DocumentoProrrogaDTO> listaNuevaDctoAgregadosNuevos = new ArrayList<DocumentoProrrogaDTO>(
				0);
		Calendar calActual = Calendar.getInstance();
		Calendar calNueva = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		if (objeto != null) {
			log.debug("numero documento #0", objeto.getNumeroDocto());
			log.debug("fecha vencimiento actual  de  #0",
					format.format(objeto.getFechaVencActual()));
			log.debug("fecha vencimiento nuevo  de  #0",
					format.format(objeto.getFechaVencNuevo()));

			calActual.setTime(objeto.getFechaVencActual());
			long actual = calActual.getTimeInMillis();

			calNueva.setTime(objeto.getFechaVencNuevo());
			long nuevo = calNueva.getTimeInMillis();

			if (actual > nuevo) {
				valido = true;
			}

			for (DocumentoProrrogaDTO dto : listaDoctoProrrogaAgregados) {
				if (dto != null
						&& dto.getNumeroDocto().equals(objeto.getNumeroDocto())
						&& valido == true) {
					objeto.setFechaVencNuevo(null);
					listaNuevaDctoAgregadosNuevos.add(objeto);
					this.mensajeGlogal = "la fecha nueva  de prorroga tiene que ser mayor que la fecha actual.";
				} else {
					listaNuevaDctoAgregadosNuevos.add(dto);
				}
			}
			listaDoctoProrrogaAgregados.clear();
			setListaDoctoProrrogaAgregados(listaNuevaDctoAgregadosNuevos);

		}
		calActual = null;
		calNueva = null;

		if (listaUsuarioCorreoagregados == null
				|| listaUsuarioCorreoagregados.size() == 0) {
			obtenerCorreoBloqueDesbloqueo();
		}

	}

	public void validarSolicitud() {
		limpiarTiempoEjecuciones();
		try {
			log.debug("se abre el validarSolicitud");
			// vamos a validar todo lo qie se ingreso al pagina

			if (this.sucursal == null) {
				FacesMessages.instance().add(Severity.ERROR, "Debe seleccionar la sucursal por obligación.");
				return;
			}
			if (this.sociedadAux == null) {
				FacesMessages.instance().add(Severity.ERROR, "Debe seleccionar la sociedad de la solicitud por obligación.");
				return;
			}
			
			if (this.paginaIngreso == 11 || this.paginaIngreso == 12) {
				habilitarBotonAgregarSolicitud = true;
				if (this.monto == null || this.monto == 0) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el valor de la compra.");
					return;
				}
				// if(this.paginaIngreso == 11){
				// if(this.pie == null || this.pie == 0){
				// FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el valor de la pie."
				// );
				// return;
				// }
				// }
				if (this.listaCotPedDTOs == null) {
					FacesMessages.instance().add(Severity.ERROR,
									"Debe consultar una Cotización o Pedido para agregar los productos relacionado.");
					return;
				} else if (this.listaCotPedDTOs.size() == 0) {
					FacesMessages.instance().add(Severity.ERROR,
									"Debe consultar una Cotización o Pedido para agregar los productos relacionado.");
					return;
				}
				if (this.tipoFormaPago == null) {
					FacesMessages.instance().add(Severity.ERROR,
									"Debe seleccionar el tipo de forma de pago.");
					return;
				} else {
					if (this.tipoFormaPago.getDescripcion().equals("Efectivo")) {
						this.pie = this.monto;
					}
				}

				if (this.paginaIngreso == 11) {
					if (this.monto == null) {
						this.monto = (long) 0;
					}
					if (this.pie == null) {
						this.pie = (long) 0;
					}
					if (this.encabezado == null) {
						FacesMessages.instance().add(Severity.ERROR,
										"Debe seleccionar el tipo de pago.");
						return;
					} else {
						String[] array = this.encabezado.getDescripcion().split(",");
						if (array != null) {
							int cantidad = array.length;
							boolean exito = false;
							for (String cadena : array) {
								if ((cadena.trim()).equals("Efectivo")) {
									if (cantidad > 1) {
										exito = true;
										break;
									}
								}
							}
							if (exito) {
								if (this.monto.longValue() < this.pie.longValue()) {
									FacesMessages.instance().add(Severity.ERROR,
													"El monto del pie debe ser menor al monto de venta.");
									return;
								}
								if (this.monto.longValue() == this.pie.longValue()) {
									FacesMessages.instance().add(Severity.ERROR,
													"El monto del pie debe ser menor al monto de venta.");
									return;
								}
								if (this.pie.longValue() == 0) {
									FacesMessages.instance().add(Severity.ERROR,
													"Debe ingresar el pie de la venta.");
									return;
								}

							}
						}
					}

				} else if (this.paginaIngreso == 12) {
					if (this.monto == null) {
						this.monto = (long) 0;
					}
					if (this.pie == null) {
						this.pie = (long) 0;
					}
					if (this.encabezado == null) {
						FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar el tipo de pago.");
						return;
					} else {
						String[] array = this.encabezado.getDescripcion().split(",");
						if (array != null) {
							boolean exito = false;
							for (String cadena : array) {
								if ((cadena.trim()).equals("Efectivo")) {
									exito = true;
									this.evaluadorCompraVC = true;
									break;
								}
							}

							if (exito) {
								if (this.evaluadorCompraVC) {
									if (this.pie == 0) {
										FacesMessages.instance().add(Severity.WARN,"Debe ingresar un pie en la venta en cuotas.");
										this.mensajeGlogal = "Debe ingresar un pie en la venta en cuotas.";
										if (listaUsuarioCorreoagregados != null) {
											listaUsuarioCorreoagregados.clear();
										}
										return;
									} else if (this.pie == this.monto) {
										FacesMessages
												.instance()
												.add(Severity.WARN,
														"Debe ingresar un pie y debe ser menor al monto total.");
										this.mensajeGlogal = "Debe ingresar un pie y debe ser menor al monto total.";
										if (listaUsuarioCorreoagregados != null) {
											listaUsuarioCorreoagregados.clear();
										}
										return;
									} else {
										this.mensajeGlogal = null;
									}
								}
							}
						} else {
							FacesMessages
									.instance()
									.add(Severity.ERROR,
											"error al seleccionar el tipo de pago de la solicitud.");
							setEncabezado(null);
						}
					}
				}

				if (this.margenGlobal == null || this.margenGlobal == 0) {
					FacesMessages.instance().add(Severity.ERROR,
							"El Margen Global  no puede ser 0 ni vacio.");
					return;
				}

				if (this.motivoCompra == null) {
					FacesMessages.instance().add(Severity.ERROR,
							"Debe ingresar el motivo de la compra.");
					return;
				} else {
					if ("".equals(this.motivoCompra)) {
						FacesMessages.instance().add(Severity.ERROR,
								"Debe ingresar el motivo de la compra.");
						return;
					}
				}

				if (this.listaUsuarioCorreoagregados == null) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"No existen usuarios en la lista de destinatarios.");
					return;
				} else {
					if (this.listaUsuarioCorreoagregados.size() == 0) {
						FacesMessages
								.instance()
								.add(Severity.ERROR,
										"No existen usuarios en la lista de destinatarios.");
						return;
					}
				}

				habilitarBotonAgregarSolicitud = false;
			} else if (this.paginaIngreso == 21 || this.paginaIngreso == 22	|| this.paginaIngreso == 23 || this.paginaIngreso == 24) {

				if (this.monto == null || this.monto == 0) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el valor de la compra.");
					return;
				}
				if (this.clsRiesgo == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe selecionar una condición de riesgo.");
					return;
				}
				if (this.cPago == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe selecionar una condición de pago.");
					return;
				}
				if (this.listaUsuarioCorreoagregados == null) {
					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios.");
					return;
				} else {
					if (this.listaUsuarioCorreoagregados.size() == 0) {
						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios.");
						return;
					}
				}

				if (this.descripcionProyecto == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la descripción del proyecto.");
					return;
				} else {
					if ("".equals(this.descripcionProyecto)) {
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la descripción del proyecto.");
						return;
					}
				}

				if (this.montoCredito == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto del proyecto.");
					return;
				}

				if (this.plazoEjecucion == null) {
					this.plazoEjecucion = "";
				}

				if (this.potencialCompra == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el potencial de compra del proyecto.");
					return;
				} else {
					if (this.potencialCompra.longValue() == 0) {
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el potencial de compra del proyecto.");
						return;
					}
				}

				if (this.conceptoNegocios == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el o los concepto(s) relacionado del proyecto.");
					return;
				} else {
					if ("".equals(this.conceptoNegocios)) {
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el o los concepto(s) relacionado del proyecto.");
						return;
					}
				}
				if (this.rutNombresSocios == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar rut y nombres de los socios.");
					return;
				} else {
					if ("".equals(this.rutNombresSocios)) {
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar rut y nombres de los socios.");
						return;
					}
				}
				// if(this.observacionesCredito == null){
				// FacesMessages.instance().add(Severity.ERROR,"Debe ingresar alguna observación del proyecto."
				// );
				// return;
				// }else{
				// if("".equals(this.observacionesCredito)){
				// FacesMessages.instance().add(Severity.ERROR,"Debe ingresar alguna observación del proyecto."
				// );
				// return;
				// }
				// }

				if (this.listaSelConceptoNegocio == null) {
					FacesMessages.instance().add(Severity.ERROR,"No existen conceptos en la lista asociada en la solicitud.");
					return;
				} else {
					if (this.listaSelConceptoNegocio.size() == 0) {
						FacesMessages.instance().add(Severity.ERROR,"No existen conceptos en la lista asociada en la solicitud.");
						return;
					}
				}
				if (this.listaSocios == null) {
					FacesMessages.instance().add(Severity.ERROR,"No existen socios en la lista asociada en la solicitud.");
					return;
				} else {
					if (this.listaSocios.size() == 0) {
						FacesMessages.instance().add(Severity.ERROR,"No existen socios en la lista asociada en la solicitud.");
						habilitarBotonAgregarSolicitud = true;
						return;
					}
				}
				if (this.paginaIngreso == 23) {
					if (this.listaFileUploadedDTOs == null) {
						FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud.");
						habilitarBotonAgregarSolicitud = true;
						return;
					} else {
						if (this.listaFileUploadedDTOs.size() == 0) {
							FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud.");
							habilitarBotonAgregarSolicitud = true;
							return;
						}
					}

				}
				habilitarBotonAgregarSolicitud = false;
			} else if (this.paginaIngreso == 31) {
				if (this.condicionRiesgo == false && this.condicionPago == false) {
					FacesMessages.instance().add(Severity.ERROR,"Debe selecionar condición de riesgo o condición de pago.");
					return;
				}

				if (this.condicionRiesgo == true) {
					if (this.clsRiesgo == null) {
						FacesMessages.instance().add(Severity.ERROR,"Debe selecionar una condición de riesgo.");
						return;
					}
				}

				if (this.condicionPago == true) {
					if (this.cPago == null) {
						FacesMessages.instance().add(Severity.ERROR,"Debe selecionar una condición de pago.");
						return;
					}
				}

				if (this.listaUsuarioCorreoagregados == null) {
					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios.");
					return;
				} else {
					if (this.listaUsuarioCorreoagregados.size() == 0) {
						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios.");
						return;
					}
				}
				if (this.motivoRiesgoPago == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la motivo de la solicitud.");
					return;
				} else {
					if ("".equals(this.motivoRiesgoPago)) {
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la motivo de la solicitud.");
						return;
					}
				}

				if (this.listaFileUploadedDTOs == null) {
					FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud.");
					return;
				} else {
					if (this.listaFileUploadedDTOs.size() == 0) {
						FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud.");
						return;
					}
				}

				habilitarBotonAgregarSolicitud = false;

			} else if (this.paginaIngreso == 41) {

				if (this.sucursal == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar la sucursal del emisor.");
					return;
				}

				if (this.opcionBloqueoDesbloqueo == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar tipo de solicitud (bloqueo o desbloqueo).");
					return;
				} else {
					if ("".equals(this.opcionBloqueoDesbloqueo)) {
						FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar tipo de solicitud (bloqueo o desbloqueo).");
						return;
					}
				}

				// if(this.motivoBloDesbloqueo == null){
				// FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la motivo de bloqueo o desbloqueo de la solicitud."
				// );
				// return;
				// }else{
				// if("".equals(this.motivoBloDesbloqueo)){
				// FacesMessages.instance().add(Severity.ERROR,"Debe ingresar la motivo de bloqueo o desbloqueo de la solicitud."
				// );
				// return;
				// }
				// }

				if (this.observaciones == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar una observacion de bloqueo o desbloqueo de la solicitud.");
					return;
				} else {
					if ("".equals(this.observaciones)) {
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar una observacion de bloqueo o desbloqueo de la solicitud.");
						return;
					}
				}

				if (this.listaUsuarioCorreoagregados == null) {
					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios.");
					return;
				} else {
					if (this.listaUsuarioCorreoagregados.size() == 0) {
						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios.");
						return;
					}
				}

				// if(this.listaFileUploadedDTOs == null){
				// FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud."
				// );
				// return;
				// }else {
				// if(this.listaFileUploadedDTOs.size() == 0){
				// FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud."
				// );
				// return;
				// }
				// }

				habilitarBotonAgregarSolicitud = false;

			} else if (this.paginaIngreso == 43) {

				if (listaClienteDMs == null) {
					FacesMessages.instance().add(Severity.ERROR,"No existen ningun DM  agregado en la lista.");
					return;
				} else {
					if (listaClienteDMs.size() == 0) {
						FacesMessages.instance().add(Severity.ERROR,"No existen ningun DM  agregado en la lista.");
						return;
					}
				}

				if (this.listaUsuarioCorreoagregados == null) {
					FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios.");
					return;
				} else {
					if (this.listaUsuarioCorreoagregados.size() == 0) {
						FacesMessages.instance().add(Severity.ERROR,"No existen usuarios en la lista de destinatarios.");
						return;
					}
				}

				// if(this.listaFileUploadedDTOs == null){
				// FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud."
				// );
				// return;
				// }else {
				// if(this.listaFileUploadedDTOs.size() == 0){
				// FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud."
				// );
				// return;
				// }
				// }

				habilitarBotonAgregarSolicitud = false;

			} else if (this.paginaIngreso == 44) {

				if (this.motivoProrroga == null) {
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar un motivo de prorroga de la solicitud.");
					return;
				} else {
					if ("".equals(this.motivoProrroga)) {
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar un motivo de prorroga de la solicitud.");
						return;
					}
				}

				if (listaDoctoProrrogaAgregados == null) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"No existen archivo en la lista de archivos asociado en la solicitud.");
					return;
				} else {
					if (listaDoctoProrrogaAgregados.size() == 0) {
						FacesMessages
								.instance()
								.add(Severity.ERROR,
										"No existen archivo en la lista de archivos asociado en la solicitud.");
						return;

					}
				}

				for (DocumentoProrrogaDTO docto : listaDoctoProrrogaAgregados) {
					if (docto != null) {
						if (docto.isStatus() == true
								&& docto.getFechaVencNuevo() != null) {
							this.contadorStatus++;
						}
					}
				}

				if (contadorStatus == 0) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"Se asigna la nueva fecha de vencimientos a los registro seleccionado.");
					return;
				}

				if (this.listaUsuarioCorreoagregados == null) {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"No existen usuarios en la lista de destinatarios.");
					return;
				} else {
					if (this.listaUsuarioCorreoagregados.size() == 0) {
						FacesMessages
								.instance()
								.add(Severity.ERROR,
										"No existen usuarios en la lista de destinatarios.");
						return;
					}
				}

				// if(this.listaFileUploadedDTOs == null){
				// FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud."
				// );
				// return;
				// }else {
				// if(this.listaFileUploadedDTOs.size() == 0){
				// FacesMessages.instance().add(Severity.ERROR,"No existen archivo en la lista de archivos asociado en la solicitud."
				// );
				// return;
				// }
				// }

				habilitarBotonAgregarSolicitud = false;

			}

		} catch (Exception e) {
			log.error("error al abrir la solictud.", e.getMessage());
		}
	}

	public void ingresarSolictud(String enviar) {
		limpiarTiempoEjecuciones();

		List<ConceptoDTO> listaArchivoGuardado = null;
		try {
			/* saca el estado inical de la solicitud */
			LcredEstado estadoAux = entityManager.find(LcredEstado.class, "I");

			/* creacion de objetos id de la solicitud nivel general */
			LcredSolicitudId id = new LcredSolicitudId();
			id.setFecSolicitud(new Date());
			BigDecimal numeroNuevo = scoringService.getUltimaSolicitudMasUno();
			id.setNumSolicitud(numeroNuevo.longValue());
			/* creacion el objeto id de la solictitud */
			LcredSolicitud solAux = new LcredSolicitud();
			solAux.setId(id);
			solAux.setHraSolicitud(new Date());
			solAux.setCodEmisor(usuarioLogueado.getAlias());
			solAux.setSucursalEmisor(sucursal.getCodigo());
			solAux.setRutCliente(clienteTarget.getRut());
			if (clienteTarget.getRazonSocial() != null	&& !"".equals(clienteTarget.getRazonSocial())) {
				solAux.setNomCliente(clienteTarget.getRazonSocial());
			} else {
				solAux.setNomCliente("No existe NOmbre");
			}

			solAux.setCodSucursal(clienteTarget.getSucursalCliente());
			solAux.setDirCliente(clienteTarget.getDireccion());
			if (clienteTarget.getComuna() != null && !"".equals(clienteTarget.getComuna())) {
				solAux.setComCliente(clienteTarget.getComuna());
			} else {
				solAux.setComCliente("No existe comuna.");
			}

			if (clienteTarget.getCiudad() != null && !"".equals(clienteTarget.getCiudad())) {
				solAux.setCiuCliente(clienteTarget.getCiudad());
			} else {
				solAux.setCiuCliente("No existe ciudad.");
			}

			if (clienteTarget.getFono() != null && !"".equals(clienteTarget.getFono())) {
				solAux.setTelCliente(clienteTarget.getFono());
			} else {
				solAux.setTelCliente("0");
			}
			if (clienteTarget.getFax() != null	&& !"".equals(clienteTarget.getFax())) {
				solAux.setFaxCliente(clienteTarget.getFax());
			} else {
				solAux.setFaxCliente("0");
			}

			solAux.setFechaCreacionCliente(clienteTarget.getCreacionSap());
			solAux.setLinCredito(clienteTarget.getLineaCreditoKH());
			solAux.setLinCreditoF(clienteTarget.getLineaCreditoFinanciada());
			solAux.setLinCreditoS(new BigDecimal(0));
			solAux.setClsRiesgo(clienteTarget.getCodigoclasificacionRiesgo());
			solAux.setConPago(clienteTarget.getCodigoCondicionPago());
			solAux.setTipSeguro(clienteTarget.getSeguro());
			solAux.setVigSeguro(clienteTarget.getVigenciaSeguro());
			solAux.setPrmFacturas(clienteTarget.getFactProm12Meses().intValue());
			solAux.setPrmVentas(clienteTarget.getVentasProm12Meses());
			solAux.setVtaTotal12Meses(clienteTarget.getVentasTotal12Meses());
			solAux.setMesesVentas(clienteTarget.getCantMesesVentas());
			solAux.setFecSalida(new Date());
			solAux.setEstado("I");
			solAux.setUsuarioActual("");
			solAux.setTipoSolicitudIncluidas("1/");
			solAux.setCantidadSolicitudesIncluidas(new Long(1));
			solAux.setCantidadSolicitudesTratadas(new Long(1));
			solAux.setLinCreditoDisp(clienteTarget.getLineaCreditoKHDisponible());
			solAux.setLinCreditoFDisp(clienteTarget.getLineaCreditoFinanciada());
			if (clienteTarget.getNumeroRV() == null	|| "".equals(clienteTarget.getNumeroRV())) {
				solAux.setNroRv((long) 0);
			} else {
				try {
					long numerRv = Long.parseLong(clienteTarget.getNumeroRV());
					solAux.setNroRv(numerRv);
				} catch (Exception e) {
					solAux.setNroRv((long) 0);
				}
			}
			solAux.setMontoCuotas(new BigDecimal(0));
			solAux.setMontoNormal(clienteTarget.getVentasTotal12Meses());
			solAux.setLinCreditoUtiliz(clienteTarget.getLineaCreditoKHUtilizado());
			solAux.setLinCreditoFUtiliz(new BigDecimal(0));
			if (clienteTarget.getGiro() != null) {
				solAux.setGiroCliente(clienteTarget.getGiro());
			} else {
				solAux.setGiroCliente("");
			}
			solAux.setSeguroPesos(clienteTarget.getMontoSeguro());
			solAux.setSeguroUf(new BigDecimal(clienteTarget.getMontoSeguroUf()));
			solAux.setEstadoLcKh(clienteTarget.getEstadoLineaCreditoKH());
			solAux.setEstadoLcCuotas(clienteTarget.getEstadoLineaEnCuotas());
			solAux.setSecuenciaEjecutivo(new Long(1));
			solAux.setCanal(clienteTarget.getCanalVenta());
			solAux.setTipoCliente(clienteTarget.getTipoCliente());

			solAux.setMorosidadMonto(new BigDecimal(clienteCompComrcial.getMontoTotalMoro()));
			solAux.setMorosidadNro(clienteCompComrcial.getNumeroMoro());
			solAux.setProtestosMonto(new BigDecimal(clienteCompComrcial.getMontoTotalProt()));
			solAux.setProtestosNro(clienteCompComrcial.getNumeroProt());
			solAux.setDiasMora(clienteCompComrcial.getDiasMoras());
			solAux.setDm(clienteCompComrcial.getDmPort());
			if (this.dm != null) {
				solAux.setDmVenta(this.dm);
			} else {
				solAux.setDmVenta("");
			}

			/* opcion para venta normal */
			if (this.paginaIngreso == 11 || this.paginaIngreso == 12) {
				if (this.monto == null) {
					this.monto = (long) 0;
				}
				if (this.pie == null) {
					this.pie = (long) 0;
				}

				if (numeroNuevo != null) {
					solAux.setPeackCredito(new BigDecimal(clienteExpoRiesgoKh.getPeakCredito()));
					solAux.setMontoAsegurado(new BigDecimal(clienteExpoRiesgoKh.getMontoAsegurado()));
					solAux.setRiesgoKupfer(new BigDecimal(clienteExpoRiesgoKh.getMontoRiesgoKupfer()));
					solAux.setMontoCuotas(new BigDecimal(clienteExpoRiesgoKh.getMontoPedidoProceso()));

					solAux.setMonto(new BigDecimal(this.monto));
					solAux.setDesTiposol(this.tipoSolicitud.getDesTipoSolicitud());
					if (this.paginaIngreso == 11) {
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						cadena.append("N");
						solAux.setTipTiposol(cadena.toString());
						solAux.setSubTiposol(this.tipoSolicitud.getDesTipoSolicitud() + " Normal");
					} else if (this.paginaIngreso == 12) {
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						cadena.append("C");
						solAux.setTipTiposol(cadena.toString());
						solAux.setSubTiposol(this.tipoSolicitud.getDesTipoSolicitud() + " En Cuotas");
					}
					/* ingreso de la forma de pago y encabezado */
					SolicitudTipoFormaPago stfp = new SolicitudTipoFormaPago();
					stfp.setIdSolicitud(solAux.getId().getNumSolicitud());
					stfp.setTipoFormaPago(this.tipoFormaPago);
					stfp.setEncabezado(this.encabezado);
					boolean exitoStfp = scoringService.persistSolicitudTipoFormaPago(stfp);
					StringBuilder cadAux = null;
					FormaPagoDetalle obj = null;
					if (exitoStfp) {
						cadAux = new StringBuilder();
						cadAux.append(this.tipoFormaPago.getDescripcion() + " ");
						cadAux.append(this.encabezado.getDescripcion());
						obj = scoringService.sacarUltimaFormaDePago(this.encabezado);
						solAux.setFormaPago(this.encabezado.getTipoFormaPago().getDescripcion()	+ "/" + this.encabezado.getDescripcion());
					}
					solAux.setGlsObservaciones(this.observaciones);

					cal2 = Calendar.getInstance();
					cal2.setTime(new Date());
					String horaMinutosSegundos = calcularTiempoDelProceso(cal1,cal2, solAux);
					this.setTiempoEjecucionProceso(horaMinutosSegundos);
					this.setTiempoEjecucionMenu(horaMinutosSegundos);
					solAux.setHoraMinutosSegundos(horaMinutosSegundos);
					solAux.setSociedad(this.sociedadAux);
					solAux = scoringService.persistSolicitud(solAux);
					if (solAux != null) {
						LcredSolicitudVtapuntual solPun = new LcredSolicitudVtapuntual();
						solPun.setNumSolicitud(solAux.getId().getNumSolicitud());
						solPun.setCodTipoSolicitud(this.tipoSolicitud.getClaveProceso());
						solPun.setStatusSolicitud("I");
						if (this.paginaIngreso == 11) {
							String character = "N";
							solPun.setTipoVentaInicial(character.charAt(0));
							solPun.setTipoVentaFinal(character.charAt(0));
						} else {
							String character = "C";
							solPun.setTipoVentaInicial(character.charAt(0));
							solPun.setTipoVentaFinal(character.charAt(0));

						}
						solPun.setMontoInicial(new BigDecimal(this.monto));
						if (cadAux != null) {
							solPun.setObservacionesInicial(cadAux.toString());
							solPun.setDcProducto(generarDescripcionProducto());
						}
						if (obj != null) {
							solPun.setDcPlazo(this.tipoFormaPago.getDescripcion() + " "	+ String.valueOf(obj.getFormaPago().getDias()));
						} else {
							solPun.setDcPlazo("0");
						}

						DecimalFormat formateador = new DecimalFormat("###0.00");
						solPun.setDcMargen(String.valueOf(formateador.format(this.margenGlobal)));
						solPun.setDcMotivo(this.motivoCompra);
						solPun.setMontoPieInicial(new BigDecimal(this.pie));
						boolean valido = scoringService.persistSolicitudVentaPuntual(solPun);
						if (valido == true) {
							listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							FacesMessages.instance().add(Severity.INFO,"Se ha generado solicitud N° #0",solAux.getId().getNumSolicitud());
							this.mensajeGlogal = "Se ha generado solicitud N° "	+ String.valueOf(solAux.getId().getNumSolicitud());

							if (listaFileUploadedDTOs != null) {
								for (FileUploadedDTO archivo : listaFileUploadedDTOs) {
									try {
										OutputStream ostream = null;
										String nombreArchivoAux = archivo.getNombreArchivo().replace(" ", "_");
										nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
										String url = "archivos"	+ "/" + solAux.getId().getNumSolicitud() + "_" + tipoSolicitud.getCodTipoSolicitud() + "_" + nombreArchivoAux;
										String nombreArchivo = solAux.getId().getNumSolicitud()	+ "_" + tipoSolicitud.getCodTipoSolicitud() + "_" + nombreArchivoAux;
										InputStream istream = new FileInputStream(archivo.getUploadItem().getFile());

										// la direccion donde se rescata el
										// archivo esta dado por el path del
										// global parameter + la url formada
										// arriba
										ostream = new FileOutputStream(globalParameters.getAttachedFilesAbsolutePath()+ url);
										log.debug("url #0", archivo.getUploadItem().getContentType());
										IOUtils.copyStream(ostream, istream);
										ostream.flush();
										ostream.close();
										istream.close();
										// guardar datos en la BD
										ArchivoAdjunto archivoAux = new ArchivoAdjunto();
										archivoAux.setSolicitud(solAux);
										archivoAux.setUrl(url);
										archivoAux.setUbicacion("");
										archivoAux.setNombreAdjunto(archivo.getNombreArchivo());
										archivoAux.setTipo(ArchivoAdjuntoType.INGRESO);
										archivoAux.setUsuario(usuarioLogueado);
										archivoAux.setFechaCreacion(new Date());
										entityManager.persist(archivoAux);
										entityManager.flush();

										entityManager.remove(archivo.getArchivo());
										entityManager.flush();

										ConceptoDTO objetoAux = new ConceptoDTO();
										objetoAux.setNombreArchivo(nombreArchivo);
										objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+ "" + url);
										listaArchivoGuardado.add(objetoAux);
										objetoAux = null;

									} catch (FileNotFoundException e) {
										log.error("error al guardar archivo #0",e.toString());
										FacesMessages.instance().add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (IOException e) {
										log.error(
												"error al guardar archivo #0",
												e.toString());
										FacesMessages
												.instance()
												.add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							boolean ingresoCalculo = scoringService
									.insertCotizacionPedidoCalculo(
											listaCotPedDTOs, solAux);
							if (ingresoCalculo == false) {
								log.error("Error en el proceso de insertar los datos los calculos de cotizacion y pedido");
							}

							List<ConceptoDTO> lista = scoringService
									.getConceptoMonto(solAux.getId()
											.getNumSolicitud(), this.monto);
							List<ConceptoDTO> listaProducto = scoringService
									.getProductos(solAux.getId()
											.getNumSolicitud());

							if ("Enviar".equals(enviar)
									&& listaUsuarioCorreoagregados != null) {
								Locale locale = new Locale("es", "CL");
								// EmailAlertaContactoMensajeHelper emailAlerta
								// =
								// (EmailAlertaContactoMensajeHelper)Component.getInstance("emailAlertaContactoMensajeHelper");
								/* setando los varlores del dto para el correo */
								ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
								contenidoEmail
										.setTituloRespuesta("Generación de solicitud");
								contenidoEmail.setCorreoEmisor(usuarioLogueado
										.getAlias());
								contenidoEmail.setSolicitud(solAux);
								NumberFormat numberFormatter;
								StringBuffer rutNombre = new StringBuffer();
								rutNombre.append(clienteTarget.getRut());
								rutNombre.append(" / ");
								rutNombre.append(modificarTexto(clienteTarget
										.getRazonSocial()));
								contenidoEmail.setRutNombre(rutNombre
										.toString());
								contenidoEmail.setSoloNombre(clienteTarget
										.getRazonSocial());
								contenidoEmail.setCanalVenta(clienteTarget
										.getCanalVenta());
								contenidoEmail.setSucursal(sucursal
										.getDescripcion());
								contenidoEmail
										.setEmisor(modificarTexto(usuarioLogueado
												.getNombre()));
								contenidoEmail
										.setTipoSolicitud("Venta Puntual");
								contenidoEmail
										.setTipoVentas(modificarTexto(tipoSolicitud
												.getDesTipoSolicitud()));

								numberFormatter = NumberFormat
										.getNumberInstance(locale);
								log.debug(numberFormatter.format(this.monto));

								contenidoEmail.setMontoMasIva(numberFormatter
										.format(this.monto));
								contenidoEmail.setMontoPie(numberFormatter
										.format(this.pie));
								contenidoEmail.setMargenNegocio(String
										.valueOf(formateador
												.format(this.margenGlobal)));
								contenidoEmail.setFormaPago(encabezado
										.getDescripcion());
								contenidoEmail
										.setMotivoCompra(this.motivoCompra);

								contenidoEmail.setPeakCredito(numberFormatter
										.format(clienteExpoRiesgoKh
												.getPeakCredito()));

								if ("VIG".equals(clienteTarget
										.getVigenciaSeguro().trim())) {
									contenidoEmail
											.setLineaSeguro(numberFormatter
													.format(clienteTarget
															.getMontoSeguro()));
								} else {
									contenidoEmail
											.setLineaSeguro(numberFormatter
													.format(0));
								}

								if (clienteExpoRiesgoKh.getMontoAsegurado() != 0) {
									contenidoEmail
											.setCoberturaSeguro(numberFormatter.format(clienteExpoRiesgoKh
													.getMontoAsegurado()));
								} else {
									contenidoEmail
											.setCoberturaSeguro(numberFormatter
													.format(0));
								}

								// contenidoEmail.setLineaSeguro(numberFormatter.format(clienteTarget.getMontoSeguro()));
								contenidoEmail.setRisgoKupfer(numberFormatter
										.format(clienteExpoRiesgoKh
												.getMontoRiesgoKupfer()));
								contenidoEmail.setListaProductos(listaProducto);
								contenidoEmail.setListaConceptoMontos(lista);
								contenidoEmail
										.setListaArchivos(listaArchivoGuardado);

								List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
										0);
								if (listaUsuarioCorreoagregados != null) {
									for (UsuarioCorreoDTO ucdto : listaUsuarioCorreoagregados) {
										if (ucdto != null
												&& ucdto.getAlias() != null
												&& ucdto.getCorreoElectronico() != null) {
											log.debug(
													"ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1",
													ucdto.getAlias().trim(),
													ucdto.getCorreoElectronico()
															.trim());
											Usuariosegur usuarioAux = scoringService
													.getUsuarioUsuarioSegurForUsername(
															ucdto.getAlias()
																	.trim(),
															ucdto.getCorreoElectronico()
																	.trim());
											if (usuarioAux != null
													&& !"".equals(usuarioAux
															.getCorreo())
													&& !"".equals(usuarioAux
															.getCorreo())) {
												if (usuarioAux != null
														&& (usuarioAux
																.getEliminado() != null && usuarioAux
																.getEliminado()
																.booleanValue() == false)) {
													if (!ultimaListaCorreoPrueba
															.contains(usuarioAux)) {
														ultimaListaCorreoPrueba
																.add(usuarioAux);
														usuarioAux = new Usuariosegur();
													}
												}
											}
										}
									}
								}

								Usuariosegur correoAdministrador = scoringService
										.getUsuarioUsuarioSegurForUsername(
												"ADMINISTRADOR",
												"sck@kupfer.cl");
								if (correoAdministrador != null) {
									ultimaListaCorreoPrueba
											.add(correoAdministrador);
									correoAdministrador = null;
								}

								Usuariosegur correoRicardoN = scoringService
										.getUsuarioUsuarioSegurForUsername(
												"RNEUMANN",
												"rneumann@kupfer.cl");
								if (correoRicardoN != null) {
									ultimaListaCorreoPrueba.add(correoRicardoN);
									correoRicardoN = null;
								}

								// insertar el emisor de la solicitud.
								Usuariosegur usuarioEmisor = scoringService
										.getUsuarioUsuarioSegurForUsername(
												usuarioLogueado.getAlias(),
												usuarioLogueado.getCorreo());

								if (usuarioEmisor != null) {
									if (ultimaListaCorreoPrueba != null
											&& ultimaListaCorreoPrueba.size() > 0) {
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									} else {
										ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
												0);
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									}
								}

								contenidoEmail
										.setListaNuevosCorreos(ultimaListaCorreoPrueba);

								try {
									boolean guardar = true;

									if (emailAlerta != null
											&& (wvarEnviaCorreo == true || !usuarioCargoAux
													.getAdministrador())) {
										emailAlerta
												.enviarSolicitud(contenidoEmail);
									}

									/*
									 * se hace el proceso guardar los datos de
									 * la cuenta corriente cuando se ingresa la
									 * solicitud.
									 */
									if (ctaCteList == null
											|| ctaCteList.size() == 0) {
										List<String> listaFechasIngresadas = scoringService
												.getFechasCuentaCorriente(clienteTarget
														.getCleanRut()
														.toUpperCase());
										if (listaFechasIngresadas != null) {
											SimpleDateFormat format = new SimpleDateFormat(
													"dd/MM/yyyy");
											String fechaActual = format
													.format(new Date());
											for (String fechList : listaFechasIngresadas) {
												if (fechaActual
														.equals(fechList)) {
													guardar = false;
													break;
												}
											}
										}
									}
									if (guardar) {
										informacionCtaCte();
										log.debug("antes de ingresar los datos de la cuenta corriente.");
										if (ctaCteList != null
												&& ctaCteList.size() > 0) {
											for (DeudaActual ctaCte : ctaCteList) {
												entityManager.persist(ctaCte);
												entityManager.flush();
											}

										}
										log.debug("fin de ingresar los datos de la cuenta corriente.");
									}

								} catch (Exception e) {
									log.debug("error por que email de alerta esta nulo #0"
											+ e.getMessage());
								}
							}
							/* se setea el numero de solicitud creada */
							insertarSolicitudDTO(solAux);
							setSolicitud(solAux);
						}
					}
				}

				/* venta a credito */
			} else if (this.paginaIngreso == 21 || this.paginaIngreso == 22 || this.paginaIngreso == 23 || this.paginaIngreso == 24) {
				String codigo = null;
				if (numeroNuevo != null) {
					solAux.setPeackCredito(new BigDecimal(0));
					solAux.setMontoAsegurado(new BigDecimal(0));
					solAux.setRiesgoKupfer(new BigDecimal(0));

					solAux.setMonto(new BigDecimal(this.monto));
					solAux.setDesTiposol(this.tipoSolicitud
							.getDesTipoSolicitud());

					if (this.paginaIngreso == 21) {
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						cadena.append("I");
						solAux.setTipTiposol(cadena.toString());
						solAux.setSubTiposol("Linea Credito " + this.tipoSolicitud.getDesTipoSolicitud());
						codigo = "I";
					} else if (this.paginaIngreso == 22) {
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						cadena.append("N");
						solAux.setTipTiposol(cadena.toString());
						solAux.setSubTiposol("Linea Credito " + this.tipoSolicitud.getDesTipoSolicitud());
						codigo = "N";
					} else if (this.paginaIngreso == 23) {
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						cadena.append("R");
						solAux.setTipTiposol(cadena.toString());
						solAux.setSubTiposol("Linea Credito " + this.tipoSolicitud.getDesTipoSolicitud());
						codigo = "R";
						solAux.setMontoRiesgoKupferCalculado(new BigDecimal(this.riesgoKupferCalculado));
					} else if (this.paginaIngreso == 24) {
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						cadena.append("A");
						solAux.setTipTiposol(cadena.toString());
						solAux.setSubTiposol("Linea Credito " + this.tipoSolicitud.getDesTipoSolicitud());
						codigo = "A";
					}

					solAux.setGlsObservaciones(this.observacionesCredito);
					cal2 = Calendar.getInstance();
					cal2.setTime(new Date());
					String horaMinutosSegundos = calcularTiempoDelProceso(cal1, cal2, solAux);
					this.setTiempoEjecucionProceso(horaMinutosSegundos);
					this.setTiempoEjecucionMenu(horaMinutosSegundos);
					solAux.setHoraMinutosSegundos(horaMinutosSegundos);
					solAux.setSociedad(this.sociedadAux);
					solAux = scoringService.persistSolicitud(solAux);

					if (solAux != null) {
						Locale locale = new Locale("es", "CL");
						NumberFormat numberFormatter;
						numberFormatter = NumberFormat.getNumberInstance(locale);

						LcredSolicitudLcredito solCredito = new LcredSolicitudLcredito();
						solCredito.setNumSolicitud(solAux.getId().getNumSolicitud());
						solCredito.setCodTipoSolicitud(this.tipoSolicitud.getClaveProceso());
						solCredito.setOpcionNormalInicial(codigo.charAt(0));
						solCredito.setStatusSolicitud("I");
						solCredito.setMontoNormalInicial(new BigDecimal(this.monto));
						String[] arrayRiesgo = this.clsRiesgo.split("-");
						if (arrayRiesgo != null) {
							solCredito.setCondRiesgoInicial(arrayRiesgo[0].toString());
						}

						String[] arrayPago = this.cPago.split("-");
						if (arrayPago != null) {
							solCredito.setCondPagoInicial(arrayPago[0].toString());
						}
						if (this.observacionesCredito != null) {
							solCredito.setObservacionesInicial(this.observacionesCredito);
						} else {
							solCredito.setObservacionesInicial("");
						}

						solCredito.setDpDescripcionProyecto(this.descripcionProyecto);
						solCredito.setDpMonto(numberFormatter.format(this.montoCredito));
						solCredito.setDpPlazoEjecucion(this.plazoEjecucion);
						solCredito.setDpPotencialCompra(numberFormatter.format(this.potencialCompra));
						solCredito.setDpConceptosInvoluc(this.conceptoNegocios);
						solCredito.setDpSocios(this.rutNombresSocios);

						boolean valido = scoringService.persistSolicitudLineaCredito(solCredito);
						if (valido == true) {
							listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							FacesMessages.instance().add(Severity.INFO,"Se ha generado solicitud N° #0",solAux.getId().getNumSolicitud());
							this.mensajeGlogal = "Se ha generado solicitud N° "	+ String.valueOf(solAux.getId().getNumSolicitud());
							if (listaFileUploadedDTOs != null) {
								for (FileUploadedDTO archivo : listaFileUploadedDTOs) {
									try {
										OutputStream ostream = null;
										String nombreArchivoAux = archivo.getNombreArchivo().replace(" ", "_");
										nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);

										String url = "archivosLC"+ "/"+ solAux.getId().getNumSolicitud()+ "_"+ tipoSolicitud.getCodTipoSolicitud()+ "_" + nombreArchivoAux;
										String nombreArchivo = solAux.getId().getNumSolicitud()	+ "_"+ tipoSolicitud.getCodTipoSolicitud()+ "_" + nombreArchivoAux;
										InputStream istream = new FileInputStream(archivo.getUploadItem().getFile());

										// la direccion donde se rescata el
										// archivo esta dado por el path del
										// global parameter + la url formada
										// arriba
										log.error("primera url : #0", globalParameters.getAttachedFilesAbsolutePath() + url);
										ostream = new FileOutputStream(globalParameters.getAttachedFilesAbsolutePath() + url);
										log.error("url #0", archivo.getUploadItem().getContentType());
										IOUtils.copyStream(ostream, istream);
										ostream.flush();
										ostream.close();
										istream.close();
										// guardar datos en la BD
										ArchivoAdjunto archivoAux = new ArchivoAdjunto();
										archivoAux.setSolicitud(solAux);
										archivoAux.setUrl(url);
										archivoAux.setUbicacion("");
										archivoAux.setNombreAdjunto(archivo.getNombreArchivo());
										archivoAux.setTipo(ArchivoAdjuntoType.INGRESO);
										archivoAux.setUsuario(usuarioLogueado);
										archivoAux.setFechaCreacion(new Date());
										entityManager.persist(archivoAux);
										entityManager.flush();

										entityManager.remove(archivo.getArchivo());
										entityManager.flush();

										ConceptoDTO objetoAux = new ConceptoDTO();
										objetoAux.setNombreArchivo(nombreArchivo);
										objetoAux.setRutaCompleta(globalParameters.getAttachedFilesWebPath()+ "" + url);
										log.error("url #0", objetoAux.getRutaCompleta());
										listaArchivoGuardado.add(objetoAux);
										objetoAux = null;
										

									} catch (FileNotFoundException e) {
										log.error("error al guardar archivo #0", e.toString());
										FacesMessages.instance().add(Severity.ERROR, "Error al guardar el archivo "	+ archivo.getNombreArchivo());
									} catch (IOException e) {
										log.error("error al guardar archivo #0",e.toString());
										FacesMessages.instance().add(Severity.ERROR,"Error al guardar el archivo "	+ archivo.getNombreArchivo());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							SolicitudConceptosNegocioLC scn = null;
							/* crear lista de concepto de involucrados */
							List<ConceptoDTO> conceptosInvolucrado = new ArrayList<ConceptoDTO>(0);
							if (this.listaSelConceptoNegocio != null) {
								for (ConceptoNegocioDTO cn : listaSelConceptoNegocio) {
									scn = new SolicitudConceptosNegocioLC();
									scn.setSolicitud(solAux);
									scn.setConceptosNegocio(cn.getConceptoNegocio());
									scn.setMonto(cn.getMonto());
									boolean guardado = false;
									try {
										guardado = scoringService.persistSolicitudConceptosNegocioLC(scn);
									} catch (Exception e) {
										log.error("error al guardar los datos Solicitud Conceptos Negocio LC #0",e.getMessage());
										guardado = false;
									}
									if (guardado) {
										ConceptoDTO ci = new ConceptoDTO();
										ci.setDescripcion(cn.getConceptoNegocio().getDescripcion());
										ci.setMontoFormateado(numberFormatter.format(cn.getMonto()));
										conceptosInvolucrado.add(ci);
										ci = null;
										scn = null;
									}
								}
							}

							if ("Enviar".equals(enviar)	&& listaUsuarioCorreoagregados != null) {

								// EmailAlertaContactoMensajeHelper emailAlerta
								// = null;
								/* setando los varlores del dto para el correo */
								ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
								contenidoEmail.setTituloRespuesta("Generación de solicitud");
								contenidoEmail.setSolicitud(solAux);
								contenidoEmail.setCorreoEmisor(usuarioLogueado.getAlias());
								StringBuffer rutNombre = new StringBuffer();
								rutNombre.append(clienteTarget.getRut());
								rutNombre.append(" / ");
								rutNombre.append(modificarTexto(clienteTarget
										.getRazonSocial()));
								contenidoEmail.setRutNombre(rutNombre
										.toString());
								contenidoEmail.setSoloNombre(clienteTarget
										.getRazonSocial());
								contenidoEmail
										.setCanalVenta(modificarTexto(clienteTarget
												.getCanalVenta()));
								contenidoEmail
										.setSucursal(modificarTexto(sucursal
												.getDescripcion()));
								contenidoEmail
										.setEmisor(modificarTexto(usuarioLogueado
												.getNombre()));
								contenidoEmail
										.setTipoSolicitud("Linea de Crédito.");
								contenidoEmail
										.setTipoLineaCredito(tipoSolicitud
												.getDesTipoSolicitud());
								log.debug(numberFormatter.format(this.monto));

								if ("VIG".equals(clienteTarget
										.getVigenciaSeguro().trim())) {
									contenidoEmail
											.setLineaSeguroActual(numberFormatter.format(clienteExpoRiesgoKh
													.getMontoAsegurado()));
								} else {
									contenidoEmail
											.setLineaSeguroActual(numberFormatter
													.format(0));
								}

								// contenidoEmail.setLineaSeguroActual(numberFormatter.format(clienteTarget.getMontoSeguro()));
								contenidoEmail
										.setMontoSolicitado(numberFormatter
												.format(this.monto));
								contenidoEmail
										.setCondicionRiesgo(this.clsRiesgo);
								contenidoEmail.setCondicionPago(this.cPago);

								contenidoEmail.setProyecto(solCredito
										.getDpDescripcionProyecto());
								contenidoEmail.setMontoProyecto(solCredito
										.getDpMonto());
								contenidoEmail
										.setPlazoEjecucion(modificarTexto(solCredito
												.getDpPlazoEjecucion()));
								contenidoEmail.setPotencialCompra(solCredito
										.getDpPotencialCompra());

								/* datos de lineas credito solCredito */
								contenidoEmail.setListaSocios(listaSocios);
								contenidoEmail
										.setListaConceptoMontos(conceptosInvolucrado);
								contenidoEmail
										.setListaArchivos(listaArchivoGuardado);

								List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
										0);
								if (listaUsuarioCorreoagregados != null) {
									for (UsuarioCorreoDTO ucdto : listaUsuarioCorreoagregados) {
										if (ucdto != null
												&& ucdto.getAlias() != null
												&& ucdto.getCorreoElectronico() != null) {
											log.debug(
													"ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1",
													ucdto.getAlias().trim(),
													ucdto.getCorreoElectronico()
															.trim());
											Usuariosegur usuarioAux = scoringService
													.getUsuarioUsuarioSegurForUsername(
															ucdto.getAlias()
																	.trim(),
															ucdto.getCorreoElectronico()
																	.trim());
											if (usuarioAux != null
													&& !"".equals(usuarioAux
															.getCorreo())
													&& !"".equals(usuarioAux
															.getCorreo())) {
												if (usuarioAux != null
														&& (usuarioAux
																.getEliminado() != null && usuarioAux
																.getEliminado()
																.booleanValue() == false)) {
													if (!ultimaListaCorreoPrueba
															.contains(usuarioAux)) {
														ultimaListaCorreoPrueba
																.add(usuarioAux);
														usuarioAux = new Usuariosegur();
													}
												}
											}
										}
									}
								}

								Usuariosegur correoAdministrador = scoringService
										.getUsuarioUsuarioSegurForUsername(
												"ADMINISTRADOR",
												"sck@kupfer.cl");
								if (correoAdministrador != null) {
									ultimaListaCorreoPrueba
											.add(correoAdministrador);
									correoAdministrador = null;
								}

								// insertar el emisor de la solicitud.
								Usuariosegur usuarioEmisor = scoringService
										.getUsuarioUsuarioSegurForUsername(
												usuarioLogueado.getAlias(),
												usuarioLogueado.getCorreo());
								if (usuarioEmisor != null) {
									if (ultimaListaCorreoPrueba != null
											&& ultimaListaCorreoPrueba.size() > 0) {
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									} else {
										ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
												0);
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									}
								}

								contenidoEmail
										.setListaNuevosCorreos(ultimaListaCorreoPrueba);
								try {
									boolean guardar = true;
									if (emailAlerta != null
											&& (wvarEnviaCorreo == true || !usuarioCargoAux
													.getAdministrador())) {
										emailAlerta
												.enviarSolicitudLineaCredito(contenidoEmail);
									}

									/*
									 * se hace el proceso guardar los datos de
									 * la cuenta corriente cuando se ingresa la
									 * solicitud.
									 */
									if (ctaCteList == null
											|| ctaCteList.size() == 0) {
										List<String> listaFechasIngresadas = scoringService
												.getFechasCuentaCorriente(clienteTarget
														.getCleanRut()
														.toUpperCase());
										if (listaFechasIngresadas != null) {
											SimpleDateFormat format = new SimpleDateFormat(
													"dd/MM/yyyy");
											String fechaActual = format
													.format(new Date());
											for (String fechList : listaFechasIngresadas) {
												if (fechaActual
														.equals(fechList)) {
													guardar = false;
													break;
												}
											}
										}
									}
									if (guardar) {
										informacionCtaCte();
										log.debug("antes de ingresar los datos de la cuenta corriente.");
										if (ctaCteList != null
												&& ctaCteList.size() > 0) {
											for (DeudaActual ctaCte : ctaCteList) {
												entityManager.persist(ctaCte);
												entityManager.flush();
											}

										}
										log.debug("fin de ingresar los datos de la cuenta corriente.");
									}

								} catch (Exception e) {
									log.debug("error por que email de alerta esta nulo #0"
											+ e.getMessage());
								}

							}
							/* se setea el numero de solicitud creada */
							insertarSolicitudDTO(solAux);
							setSolicitud(solAux);
						}
					}
				}
				/* condiciones */
			} else if (this.paginaIngreso == 31) {
				if (numeroNuevo != null) {
					solAux.setPeackCredito(new BigDecimal(0));
					solAux.setMontoAsegurado(new BigDecimal(0));
					solAux.setRiesgoKupfer(new BigDecimal(0));
					solAux.setMonto(new BigDecimal(0));
					solAux.setDesTiposol("Condiciones "
							+ this.tipoSolicitud.getDesTipoSolicitud());

					if (this.paginaIngreso == 31) {
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						if (this.condicionRiesgo == true
								&& this.condicionPago == false) {
							solAux.setSubTiposol("Condiciones de Riesgo");
							cadena.append("R");

						} else if (this.condicionRiesgo == false
								&& this.condicionPago == true) {
							solAux.setSubTiposol("Condiciones de Pago");
							cadena.append("P");
						} else if (this.condicionRiesgo == true
								&& this.condicionPago == true) {
							solAux.setSubTiposol("Condiciones "
									+ this.tipoSolicitud.getDesTipoSolicitud());
							cadena.append("R");
							cadena.append("P");
						}
						solAux.setTipTiposol(cadena.toString());
					}

					solAux.setGlsObservaciones(this.motivoRiesgoPago);
					cal2 = Calendar.getInstance();
					cal2.setTime(new Date());
					String horaMinutosSegundos = calcularTiempoDelProceso(cal1,
							cal2, solAux);
					this.setTiempoEjecucionProceso(horaMinutosSegundos);
					this.setTiempoEjecucionMenu(horaMinutosSegundos);
					solAux.setHoraMinutosSegundos(horaMinutosSegundos);
					solAux.setSociedad(this.sociedadAux);
					solAux = scoringService.persistSolicitud(solAux);
					if (solAux != null) {
						String var = "S";
						LcredSolicitudCondiciones solCondiciones = new LcredSolicitudCondiciones();
						solCondiciones.setNumSolicitud(solAux.getId()
								.getNumSolicitud());
						solCondiciones.setCodTipoSolicitud(this.tipoSolicitud
								.getClaveProceso());
						solCondiciones.setStatusSolicitud("I");

						if (this.condicionRiesgo == true
								&& this.clsRiesgo != null) {
							String[] arrayRiesgo = this.clsRiesgo.split("-");
							if (arrayRiesgo != null) {
								solCondiciones
										.setCodCondRiesgoInicial(arrayRiesgo[0]
												.toString());
								solCondiciones.setCondRiesgoInicial(var
										.charAt(0));
							}
						}

						if (this.condicionPago == true && this.cPago != null) {
							String[] arrayPago = this.cPago.split("-");
							if (arrayPago != null) {
								solCondiciones
										.setCodCondPagoInicial(arrayPago[0]
												.toString());
								solCondiciones
										.setCondPagoInicial(var.charAt(0));
							}
						}
						solCondiciones.setMotivoCambio(this.motivoRiesgoPago);

						boolean valido = scoringService
								.persistSolicitudCondiciones(solCondiciones);
						if (valido == true) {
							listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							FacesMessages.instance().add(Severity.INFO,
									"Se ha generado solicitud N° #0",
									solAux.getId().getNumSolicitud());
							this.mensajeGlogal = "Se ha generado solicitud N° "
									+ String.valueOf(solAux.getId()
											.getNumSolicitud());

							if (listaFileUploadedDTOs != null) {
								for (FileUploadedDTO archivo : listaFileUploadedDTOs) {
									try {
										OutputStream ostream = null;
										String nombreArchivoAux = archivo
												.getNombreArchivo().replace(
														" ", "_");
										nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
										String url = "archivosCC"
												+ "/"
												+ solAux.getId()
														.getNumSolicitud()
												+ "_"
												+ tipoSolicitud
														.getCodTipoSolicitud()
												+ "_" + nombreArchivoAux;
										String nombreArchivo = solAux.getId()
												.getNumSolicitud()
												+ "_"
												+ tipoSolicitud
														.getCodTipoSolicitud()
												+ "_" + nombreArchivoAux;
										InputStream istream = new FileInputStream(
												archivo.getUploadItem()
														.getFile());

										// la direccion donde se rescata el
										// archivo esta dado por el path del
										// global parameter + la url formada
										// arriba
										ostream = new FileOutputStream(
												globalParameters
														.getAttachedFilesAbsolutePath()
														+ url);
										log.debug("url #0", archivo
												.getUploadItem()
												.getContentType());
										IOUtils.copyStream(ostream, istream);
										ostream.flush();
										ostream.close();
										istream.close();
										// guardar datos en la BD
										ArchivoAdjunto archivoAux = new ArchivoAdjunto();
										archivoAux.setSolicitud(solAux);
										archivoAux.setUrl(url);
										archivoAux.setUbicacion("");
										archivoAux.setNombreAdjunto(archivo
												.getNombreArchivo());
										archivoAux
												.setTipo(ArchivoAdjuntoType.INGRESO);
										archivoAux.setUsuario(usuarioLogueado);
										archivoAux.setFechaCreacion(new Date());
										entityManager.persist(archivoAux);
										entityManager.flush();

										entityManager.remove(archivo
												.getArchivo());
										entityManager.flush();

										ConceptoDTO objetoAux = new ConceptoDTO();
										objetoAux
												.setNombreArchivo(nombreArchivo);
										objetoAux
												.setRutaCompleta(globalParameters
														.getAttachedFilesWebPath()
														+ "" + url);
										listaArchivoGuardado.add(objetoAux);
										objetoAux = null;

									} catch (FileNotFoundException e) {
										log.error(
												"error al guardar archivo #0",
												e.toString());
										FacesMessages
												.instance()
												.add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (IOException e) {
										log.error(
												"error al guardar archivo #0",
												e.toString());
										FacesMessages
												.instance()
												.add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							if ("Enviar".equals(enviar)
									&& listaUsuarioCorreoagregados != null) {

								// EmailAlertaContactoMensajeHelper emailAlerta
								// = null;
								/* setando los varlores del dto para el correo */
								ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
								contenidoEmail
										.setTituloRespuesta("Generación de solicitud");
								contenidoEmail.setSolicitud(solAux);
								contenidoEmail.setCorreoEmisor(usuarioLogueado
										.getAlias());
								StringBuffer rutNombre = new StringBuffer();
								rutNombre.append(clienteTarget.getRut());
								rutNombre.append(" / ");
								rutNombre.append(modificarTexto(clienteTarget
										.getRazonSocial()));
								contenidoEmail.setRutNombre(rutNombre
										.toString());

								contenidoEmail.setSoloNombre(clienteTarget
										.getRazonSocial());
								contenidoEmail
										.setCanalVenta(modificarTexto(clienteTarget
												.getCanalVenta()));
								contenidoEmail
										.setSucursal(modificarTexto(sucursal
												.getDescripcion()));
								contenidoEmail
										.setEmisor(modificarTexto(usuarioLogueado
												.getNombre()));

								contenidoEmail
										.setTipoSolicitud("Cambio de Condiciones.");

								if (this.condicionRiesgo == true
										&& this.condicionPago == false) {
									contenidoEmail
											.setTipoLineaCredito("Condiciones de Riesgo.");
									contenidoEmail
											.setCondicionRiesgo(clienteTarget
													.getClasificacionRiesgo());
									contenidoEmail.setCondicionPago("");
									contenidoEmail
											.setCondicionRiesgoNuevo(modificarTexto(this.clsRiesgo));
									contenidoEmail.setCondicionPagoNuevo("");

								} else if (this.condicionRiesgo == false
										&& this.condicionPago == true) {
									contenidoEmail
											.setTipoLineaCredito("Condiciones de Pago.");
									contenidoEmail.setCondicionRiesgo("");
									contenidoEmail
											.setCondicionPago(clienteTarget
													.getCondicionPago());
									contenidoEmail.setCondicionRiesgoNuevo("");
									contenidoEmail
											.setCondicionPagoNuevo(this.cPago);

								} else if (this.condicionRiesgo == true
										&& this.condicionPago == true) {
									contenidoEmail
											.setTipoLineaCredito("Condiciones "
													+ this.tipoSolicitud
															.getDesTipoSolicitud());
									contenidoEmail
											.setCondicionRiesgo(clienteTarget
													.getClasificacionRiesgo());
									contenidoEmail
											.setCondicionPago(clienteTarget
													.getCondicionPago());
									contenidoEmail
											.setCondicionRiesgoNuevo(this.clsRiesgo);
									contenidoEmail
											.setCondicionPagoNuevo(this.cPago);
								}
								contenidoEmail
										.setMotivoCambio(this.motivoRiesgoPago);

								/* datos de lineas credito solCredito */
								contenidoEmail
										.setListaArchivos(listaArchivoGuardado);

								List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
										0);
								if (listaUsuarioCorreoagregados != null) {
									for (UsuarioCorreoDTO ucdto : listaUsuarioCorreoagregados) {
										if (ucdto != null
												&& ucdto.getAlias() != null
												&& ucdto.getCorreoElectronico() != null) {
											log.debug(
													"ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1",
													ucdto.getAlias().trim(),
													ucdto.getCorreoElectronico()
															.trim());
											Usuariosegur usuarioAux = scoringService
													.getUsuarioUsuarioSegurForUsername(
															ucdto.getAlias()
																	.trim(),
															ucdto.getCorreoElectronico()
																	.trim());
											if (usuarioAux != null
													&& !"".equals(usuarioAux
															.getCorreo())
													&& !"".equals(usuarioAux
															.getCorreo())) {
												if (usuarioAux != null
														&& (usuarioAux
																.getEliminado() != null && usuarioAux
																.getEliminado()
																.booleanValue() == false)) {
													if (usuarioAux
															.getObligatorio() != null
															&& usuarioAux
																	.getObligatorio()
																	.booleanValue() == true) {
														if (!ultimaListaCorreoPrueba
																.contains(usuarioAux)) {
															ultimaListaCorreoPrueba
																	.add(usuarioAux);
															usuarioAux = new Usuariosegur();
														}
													} else {
														if (scoringService
																.getUsuarioCargoEnvioAutomatico(usuarioAux
																		.getIdPersonal())) {
															if (!ultimaListaCorreoPrueba
																	.contains(usuarioAux)) {
																ultimaListaCorreoPrueba
																		.add(usuarioAux);
																usuarioAux = new Usuariosegur();
															}
														}
													}
												}
											}
										}
									}
								}
								Usuariosegur correoAdministrador = scoringService
										.getUsuarioUsuarioSegurForUsername(
												"ADMINISTRADOR",
												"sck@kupfer.cl");
								if (correoAdministrador != null) {
									ultimaListaCorreoPrueba
											.add(correoAdministrador);
									correoAdministrador = null;
								}

								// insertar el emisor de la solicitud.
								Usuariosegur usuarioEmisor = scoringService
										.getUsuarioUsuarioSegurForUsername(
												usuarioLogueado.getAlias(),
												usuarioLogueado.getCorreo());
								if (usuarioEmisor != null) {
									if (ultimaListaCorreoPrueba != null
											&& ultimaListaCorreoPrueba.size() > 0) {
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									} else {
										ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
												0);
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									}
								}

								contenidoEmail
										.setListaNuevosCorreos(ultimaListaCorreoPrueba);

								try {
									boolean guardar = true;
									if (emailAlerta != null
											&& (wvarEnviaCorreo == true || !usuarioCargoAux
													.getAdministrador())) {
										emailAlerta
												.enviarSolicitudCondiciones(contenidoEmail);
									}
									/*
									 * se hace el proceso guardar los datos de
									 * la cuenta corriente cuando se ingresa la
									 * solicitud.
									 */
									if (ctaCteList == null
											|| ctaCteList.size() == 0) {
										List<String> listaFechasIngresadas = scoringService
												.getFechasCuentaCorriente(clienteTarget
														.getCleanRut()
														.toUpperCase());
										if (listaFechasIngresadas != null) {
											SimpleDateFormat format = new SimpleDateFormat(
													"dd/MM/yyyy");
											String fechaActual = format
													.format(new Date());
											for (String fechList : listaFechasIngresadas) {
												if (fechaActual
														.equals(fechList)) {
													guardar = false;
													break;
												}
											}
										}
									}
									if (guardar) {
										informacionCtaCte();
										log.debug("antes de ingresar los datos de la cuenta corriente.");
										if (ctaCteList != null
												&& ctaCteList.size() > 0) {
											for (DeudaActual ctaCte : ctaCteList) {
												entityManager.persist(ctaCte);
												entityManager.flush();
											}

										}
										log.debug("fin de ingresar los datos de la cuenta corriente.");
									}

								} catch (Exception e) {
									log.debug("error por que email de alerta esta nulo #0"
											+ e.getMessage());
								}

							}
							/* se setea el numero de solicitud creada */
							insertarSolicitudDTO(solAux);
							setSolicitud(solAux);
						}
					}
				}

			} else if (this.paginaIngreso == 41) {/* bloqueo y desbloqueo */

				if (numeroNuevo != null) {
					solAux.setPeackCredito(new BigDecimal(0));
					solAux.setMontoAsegurado(new BigDecimal(0));
					solAux.setRiesgoKupfer(new BigDecimal(0));
					solAux.setMonto(new BigDecimal(0));
					solAux.setDesTiposol(this.tipoSolicitud
							.getDesTipoSolicitud());

					if ("41".equals(this.opcionBloqueoDesbloqueo)) {
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						cadena.append("B");
						solAux.setTipTiposol(cadena.toString());
						solAux.setSubTiposol(this.tipoSolicitud
								.getDesTipoSolicitud());
					} else if ("42".equals(this.opcionBloqueoDesbloqueo)) {
						StringBuffer cadena = new StringBuffer();
						cadena.append(this.tipoSolicitud.getClaveProceso());
						cadena.append("D");
						solAux.setTipTiposol(cadena.toString());
						solAux.setSubTiposol(this.tipoSolicitud
								.getDesTipoSolicitud());
					}

					solAux.setGlsObservaciones(this.observaciones);
					cal2 = Calendar.getInstance();
					cal2.setTime(new Date());
					String horaMinutosSegundos = calcularTiempoDelProceso(cal1,
							cal2, solAux);
					this.setTiempoEjecucionProceso(horaMinutosSegundos);
					this.setTiempoEjecucionMenu(horaMinutosSegundos);
					solAux.setHoraMinutosSegundos(horaMinutosSegundos);
					solAux.setSociedad(this.sociedadAux);
					solAux = scoringService.persistSolicitud(solAux);
					if (solAux != null) {
						LcredSolicitudBloqueos solBloqueos = new LcredSolicitudBloqueos();
						solBloqueos.setNumSolicitud(solAux.getId()
								.getNumSolicitud());
						solBloqueos.setMotivoBloqDesbloq(this.observaciones);
						boolean valido = scoringService
								.persistSolicitudBloqueoDesbloqueo(solBloqueos);

						LcredSolicitudOtras otrasId = new LcredSolicitudOtras();
						otrasId.setNumSolicitud(solAux.getId()
								.getNumSolicitud());
						otrasId.setCodTipoSolicitud("4");
						otrasId.setStatusSolicitud("I");
						if ("41".equals(this.opcionBloqueoDesbloqueo)) {
							otrasId.setOpcionInicial("B");

						} else if ("42".equals(this.opcionBloqueoDesbloqueo)) {
							otrasId.setOpcionInicial("D");
						}

						otrasId.setObservacionesInicial(this.observaciones);
						boolean validoOtra = scoringService
								.persistSolicitudOtras(otrasId);

						if (valido == true && validoOtra == true) {
							listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							FacesMessages.instance().add(Severity.INFO,
									"Se ha generado solicitud N°e #0",
									solAux.getId().getNumSolicitud());
							this.mensajeGlogal = "Se ha generado solicitud N° "
									+ String.valueOf(solAux.getId()
											.getNumSolicitud());
							if (listaFileUploadedDTOs != null) {
								for (FileUploadedDTO archivo : listaFileUploadedDTOs) {
									try {
										OutputStream ostream = null;
										String nombreArchivoAux = archivo
												.getNombreArchivo().replace(
														" ", "_");
										nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);

										String url = "archivosOS"
												+ "/"
												+ solAux.getId()
														.getNumSolicitud()
												+ "_"
												+ tipoSolicitud
														.getCodTipoSolicitud()
												+ "_" + nombreArchivoAux;
										String nombreArchivo = solAux.getId()
												.getNumSolicitud()
												+ "_"
												+ tipoSolicitud
														.getCodTipoSolicitud()
												+ "_" + nombreArchivoAux;
										InputStream istream = new FileInputStream(
												archivo.getUploadItem()
														.getFile());

										// la direccion donde se rescata el
										// archivo esta dado por el path del
										// global parameter + la url formada
										// arriba
										ostream = new FileOutputStream(
												globalParameters
														.getAttachedFilesAbsolutePath()
														+ url);
										log.debug("url #0", archivo
												.getUploadItem()
												.getContentType());
										IOUtils.copyStream(ostream, istream);
										ostream.flush();
										ostream.close();
										istream.close();
										// guardar datos en la BD
										ArchivoAdjunto archivoAux = new ArchivoAdjunto();
										archivoAux.setSolicitud(solAux);
										archivoAux.setUrl(url);
										archivoAux.setUbicacion("");
										archivoAux.setNombreAdjunto(archivo
												.getNombreArchivo());
										archivoAux
												.setTipo(ArchivoAdjuntoType.INGRESO);
										archivoAux.setUsuario(usuarioLogueado);
										archivoAux.setFechaCreacion(new Date());
										entityManager.persist(archivoAux);
										entityManager.flush();

										entityManager.remove(archivo
												.getArchivo());
										entityManager.flush();

										ConceptoDTO objetoAux = new ConceptoDTO();
										objetoAux
												.setNombreArchivo(nombreArchivo);
										objetoAux
												.setRutaCompleta(globalParameters
														.getAttachedFilesWebPath()
														+ "" + url);
										listaArchivoGuardado.add(objetoAux);
										objetoAux = null;

									} catch (FileNotFoundException e) {
										log.error(
												"error al guardar archivo #0",
												e.toString());
										FacesMessages
												.instance()
												.add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (IOException e) {
										log.error(
												"error al guardar archivo #0",
												e.toString());
										FacesMessages
												.instance()
												.add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							if ("Enviar".equals(enviar)
									&& listaUsuarioCorreoagregados != null) {

								// EmailAlertaContactoMensajeHelper emailAlerta
								// = null;
								/* setando los varlores del dto para el correo */
								ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
								contenidoEmail
										.setTituloRespuesta("Generación de solicitud");
								contenidoEmail.setSolicitud(solAux);
								contenidoEmail.setCorreoEmisor(usuarioLogueado
										.getAlias());
								StringBuffer rutNombre = new StringBuffer();
								rutNombre.append(clienteTarget.getRut());
								rutNombre.append(" / ");
								rutNombre.append(modificarTexto(clienteTarget
										.getRazonSocial()));
								contenidoEmail.setRutNombre(rutNombre
										.toString());
								contenidoEmail.setSoloNombre(clienteTarget
										.getRazonSocial());
								contenidoEmail
										.setCanalVenta(modificarTexto(clienteTarget
												.getCanalVenta()));
								contenidoEmail
										.setSucursal(modificarTexto(sucursal
												.getDescripcion()));
								contenidoEmail
										.setEmisor(modificarTexto(usuarioLogueado
												.getNombre()));

								contenidoEmail
										.setTipoSolicitud("Bloqueo / Desbloqueo.");

								if ("41".equals(this.opcionBloqueoDesbloqueo)) {
									contenidoEmail
											.setTipoBloqueoDesbloqueo("Bloqueo.");

								} else if ("42"
										.equals(this.opcionBloqueoDesbloqueo)) {
									contenidoEmail
											.setTipoBloqueoDesbloqueo("Desbloqueo.");
								}

								contenidoEmail
										.setMotivoBloqueoDesbloqueo(this.observaciones);
								contenidoEmail
										.setObservacionesBloqueoDesblorqueo(this.observaciones);

								/* datos de lineas credito solCredito */
								contenidoEmail
										.setListaArchivos(listaArchivoGuardado);
								List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
										0);
								if (listaUsuarioCorreoagregados != null) {
									for (UsuarioCorreoDTO ucdto : listaUsuarioCorreoagregados) {
										if (ucdto != null
												&& ucdto.getAlias() != null
												&& ucdto.getCorreoElectronico() != null) {
											log.debug(
													"ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1",
													ucdto.getAlias().trim(),
													ucdto.getCorreoElectronico()
															.trim());
											Usuariosegur usuarioAux = scoringService
													.getUsuarioUsuarioSegurForUsername(
															ucdto.getAlias()
																	.trim(),
															ucdto.getCorreoElectronico()
																	.trim());
											if (usuarioAux != null
													&& !"".equals(usuarioAux
															.getCorreo())
													&& !"".equals(usuarioAux
															.getCorreo())) {
												if (usuarioAux != null
														&& (usuarioAux
																.getEliminado() != null && usuarioAux
																.getEliminado()
																.booleanValue() == false)) {

													if (usuarioAux
															.getObligatorio() != null
															&& usuarioAux
																	.getObligatorio()
																	.booleanValue() == true) {
														if (!ultimaListaCorreoPrueba
																.contains(usuarioAux)) {
															ultimaListaCorreoPrueba
																	.add(usuarioAux);
															usuarioAux = new Usuariosegur();
														}
													} else {
														if (scoringService
																.getUsuarioCargoEnvioAutomatico(usuarioAux
																		.getIdPersonal())) {
															if (!ultimaListaCorreoPrueba
																	.contains(usuarioAux)) {
																ultimaListaCorreoPrueba
																		.add(usuarioAux);
																usuarioAux = new Usuariosegur();
															}
														}
													}
												}
											}
										}
									}
								}

								Usuariosegur correoAdministrador = scoringService
										.getUsuarioUsuarioSegurForUsername(
												"ADMINISTRADOR",
												"sck@kupfer.cl");
								if (correoAdministrador != null) {
									ultimaListaCorreoPrueba
											.add(correoAdministrador);
									correoAdministrador = null;
								}

								// insertar el emisor de la solicitud.
								Usuariosegur usuarioEmisor = scoringService
										.getUsuarioUsuarioSegurForUsername(
												usuarioLogueado.getAlias(),
												usuarioLogueado.getCorreo());
								if (usuarioEmisor != null) {
									if (ultimaListaCorreoPrueba != null
											&& ultimaListaCorreoPrueba.size() > 0) {
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									} else {
										ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
												0);
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									}
								}

								contenidoEmail
										.setListaNuevosCorreos(ultimaListaCorreoPrueba);

								try {
									boolean guardar = true;

									if (emailAlerta != null
											&& (wvarEnviaCorreo == true || !usuarioCargoAux
													.getAdministrador())) {
										emailAlerta
												.enviarSolicitudBloqueoDesbloqueo(contenidoEmail);
									}

									/*
									 * se hace el proceso guardar los datos de
									 * la cuenta corriente cuando se ingresa la
									 * solicitud.
									 */
									if (ctaCteList == null
											|| ctaCteList.size() == 0) {
										List<String> listaFechasIngresadas = scoringService
												.getFechasCuentaCorriente(clienteTarget
														.getCleanRut()
														.toUpperCase());
										if (listaFechasIngresadas != null) {
											SimpleDateFormat format = new SimpleDateFormat(
													"dd/MM/yyyy");
											String fechaActual = format
													.format(new Date());
											for (String fechList : listaFechasIngresadas) {
												if (fechaActual
														.equals(fechList)) {
													guardar = false;
													break;
												}
											}
										}
									}
									if (guardar) {
										informacionCtaCte();
										log.debug("antes de ingresar los datos de la cuenta corriente.");
										if (ctaCteList != null
												&& ctaCteList.size() > 0) {
											for (DeudaActual ctaCte : ctaCteList) {
												entityManager.persist(ctaCte);
												entityManager.flush();
											}

										}
										log.debug("fin de ingresar los datos de la cuenta corriente.");
									}
								} catch (Exception e) {
									log.debug("error por que email de alerta esta nulo #0"
											+ e.getMessage());
								}

							}
							/* se setea el numero de solicitud creada */
							insertarSolicitudDTO(solAux);
							setSolicitud(solAux);
						}
					}
				}
			} else if (this.paginaIngreso == 43) {

				if (numeroNuevo != null) {
					solAux.setPeackCredito(new BigDecimal(0));
					solAux.setMontoAsegurado(new BigDecimal(0));
					solAux.setRiesgoKupfer(new BigDecimal(0));
					solAux.setMonto(new BigDecimal(0));
					solAux.setDesTiposol(this.tipoSolicitud
							.getDesTipoSolicitud());

					StringBuffer cadena = new StringBuffer();
					cadena.append(this.tipoSolicitud.getClaveProceso());
					cadena.append("DM");
					solAux.setTipTiposol(cadena.toString());
					solAux.setSubTiposol(this.tipoSolicitud
							.getDesTipoSolicitud());
					cal2 = Calendar.getInstance();
					cal2.setTime(new Date());
					String horaMinutosSegundos = calcularTiempoDelProceso(cal1,
							cal2, solAux);
					this.setTiempoEjecucionProceso(horaMinutosSegundos);
					this.setTiempoEjecucionMenu(horaMinutosSegundos);
					solAux.setHoraMinutosSegundos(horaMinutosSegundos);

					solAux.setGlsObservaciones(modificarTexto("Creacón de DM"));
					solAux.setSociedad(this.sociedadAux);
					solAux = scoringService.persistSolicitud(solAux);
					if (solAux != null) {
						StringBuffer cadenaNombre = new StringBuffer();
						List<CreacionDmDTO> listaDms = new ArrayList<CreacionDmDTO>(
								0);
						CreacionDmDTO obejtoDm = null;
						boolean valido = false;
						for (LcredSolicitudDm docto : listaClienteDMs) {

							if (docto != null
									&& docto.getId().getNumSolicitud() == 0) {
								docto.getId().setNumSolicitud(
										numeroNuevo.longValue());
							}

							if (docto != null) {
								valido = scoringService
										.persistSolicitudDM(docto);
								obejtoDm = new CreacionDmDTO();
								obejtoDm.setRut(docto.getId().getRut());
								cadenaNombre.append(docto.getId().getNombre()
										+ ", ");
								obejtoDm.setNombre(docto.getId().getNombre());
								obejtoDm.setOficinaVentas(docto.getId()
										.getOficinaVentas());
								obejtoDm.setZona(docto.getId().getZona());
								obejtoDm.setListaPrecio(docto.getId()
										.getListaPrecio());
								obejtoDm.setSector(docto.getId().getSector());
								obejtoDm.setVendTelefono(docto.getId()
										.getVendTelefono());
								obejtoDm.setVendTerreno(docto.getId()
										.getVendTerreno());
								obejtoDm.setCobrador(docto.getId()
										.getCobrador());
								obejtoDm.setCondExpedicion(docto.getId()
										.getCondExpedicion());
								obejtoDm.setCondPago(docto.getId()
										.getCondPago());
								listaDms.add(obejtoDm);
							}
						}

						LcredSolicitudOtras otrasId = new LcredSolicitudOtras();
						otrasId.setNumSolicitud(solAux.getId()
								.getNumSolicitud());
						otrasId.setCodTipoSolicitud("4");
						otrasId.setStatusSolicitud("I");
						otrasId.setOpcionInicial("DM");
						otrasId.setObservacionesInicial(cadenaNombre.toString());
						boolean validoOtra = scoringService
								.persistSolicitudOtras(otrasId);
						if (valido == true && validoOtra == true) {
							listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							FacesMessages.instance().add(Severity.INFO,
									"Se ha generado solicitud N° #0",
									solAux.getId().getNumSolicitud());
							this.mensajeGlogal = "Se ha generado solicitud N° "
									+ String.valueOf(solAux.getId()
											.getNumSolicitud());

							if (listaFileUploadedDTOs != null) {
								for (FileUploadedDTO archivo : listaFileUploadedDTOs) {
									try {
										OutputStream ostream = null;
										String nombreArchivoAux = archivo
												.getNombreArchivo().replace(
														" ", "_");
										nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
										String url = "archivosOS"
												+ "/"
												+ solAux.getId()
														.getNumSolicitud()
												+ "_"
												+ tipoSolicitud
														.getCodTipoSolicitud()
												+ "_" + nombreArchivoAux;
										String nombreArchivo = solAux.getId()
												.getNumSolicitud()
												+ "_"
												+ tipoSolicitud
														.getCodTipoSolicitud()
												+ "_" + nombreArchivoAux;
										InputStream istream = new FileInputStream(
												archivo.getUploadItem()
														.getFile());

										// la direccion donde se rescata el
										// archivo esta dado por el path del
										// global parameter + la url formada
										// arriba
										ostream = new FileOutputStream(
												globalParameters
														.getAttachedFilesAbsolutePath()
														+ url);
										log.debug("url #0", archivo
												.getUploadItem()
												.getContentType());
										IOUtils.copyStream(ostream, istream);
										ostream.flush();
										ostream.close();
										istream.close();
										// guardar datos en la BD
										ArchivoAdjunto archivoAux = new ArchivoAdjunto();
										archivoAux.setSolicitud(solAux);
										archivoAux.setUrl(url);
										archivoAux.setUbicacion("");
										archivoAux.setNombreAdjunto(archivo
												.getNombreArchivo());
										archivoAux
												.setTipo(ArchivoAdjuntoType.INGRESO);
										archivoAux.setUsuario(usuarioLogueado);
										archivoAux.setFechaCreacion(new Date());
										entityManager.persist(archivoAux);
										entityManager.flush();

										entityManager.remove(archivo
												.getArchivo());
										entityManager.flush();

										ConceptoDTO objetoAux = new ConceptoDTO();
										objetoAux
												.setNombreArchivo(nombreArchivo);
										objetoAux
												.setRutaCompleta(globalParameters
														.getAttachedFilesWebPath()
														+ "" + url);
										listaArchivoGuardado.add(objetoAux);
										objetoAux = null;

									} catch (FileNotFoundException e) {
										log.error(
												"error al guardar archivo #0",
												e.toString());
										FacesMessages
												.instance()
												.add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (IOException e) {
										log.error(
												"error al guardar archivo #0",
												e.toString());
										FacesMessages
												.instance()
												.add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							if ("Enviar".equals(enviar)
									&& listaUsuarioCorreoagregados != null) {

								// EmailAlertaContactoMensajeHelper emailAlerta
								// = null;
								/* setando los varlores del dto para el correo */
								ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
								contenidoEmail
										.setTituloRespuesta("Generación de solicitud");
								contenidoEmail.setSolicitud(solAux);
								contenidoEmail.setCorreoEmisor(usuarioLogueado
										.getAlias());
								StringBuffer rutNombre = new StringBuffer();
								rutNombre.append(clienteTarget.getRut());
								rutNombre.append(" / ");
								rutNombre.append(modificarTexto(clienteTarget
										.getRazonSocial()));
								contenidoEmail.setRutNombre(rutNombre
										.toString());

								contenidoEmail.setSoloNombre(clienteTarget
										.getRazonSocial());
								contenidoEmail
										.setCanalVenta(modificarTexto(clienteTarget
												.getCanalVenta()));
								contenidoEmail
										.setSucursal(modificarTexto(sucursal
												.getDescripcion()));
								contenidoEmail
										.setEmisor(modificarTexto(usuarioLogueado
												.getNombre()));

								contenidoEmail
										.setTipoSolicitud("Creacion de DM");
								contenidoEmail.setTipoDM("DM");

								/* datos de archivos */
								contenidoEmail
										.setListaArchivos(listaArchivoGuardado);

								/* datos de prorrogas */
								contenidoEmail.setListaCreacionDM(listaDms);

								List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
										0);
								if (listaUsuarioCorreoagregados != null) {
									for (UsuarioCorreoDTO ucdto : listaUsuarioCorreoagregados) {
										if (ucdto != null
												&& ucdto.getAlias() != null
												&& ucdto.getCorreoElectronico() != null) {
											log.debug(
													"ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1",
													ucdto.getAlias().trim(),
													ucdto.getCorreoElectronico()
															.trim());
											Usuariosegur usuarioAux = scoringService
													.getUsuarioUsuarioSegurForUsername(
															ucdto.getAlias()
																	.trim(),
															ucdto.getCorreoElectronico()
																	.trim());
											if (usuarioAux != null
													&& !"".equals(usuarioAux
															.getCorreo())) {
												if (usuarioAux.getObligatorio() != null
														&& usuarioAux
																.getObligatorio()
																.booleanValue() == true) {
													if (!ultimaListaCorreoPrueba
															.contains(usuarioAux)) {
														ultimaListaCorreoPrueba
																.add(usuarioAux);
														usuarioAux = new Usuariosegur();
													}
												} else {
													if (scoringService
															.getUsuarioCargoEnvioAutomatico(usuarioAux
																	.getIdPersonal())) {
														if (!ultimaListaCorreoPrueba
																.contains(usuarioAux)) {
															ultimaListaCorreoPrueba
																	.add(usuarioAux);
															usuarioAux = new Usuariosegur();
														}
													}
												}
											}
										}
									}
								}

								Usuariosegur correoAdministrador = scoringService
										.getUsuarioUsuarioSegurForUsername(
												"ADMINISTRADOR",
												"sck@kupfer.cl");
								if (correoAdministrador != null) {
									ultimaListaCorreoPrueba
											.add(correoAdministrador);
									correoAdministrador = null;
								}

								// insertar el emisor de la solicitud.
								Usuariosegur usuarioEmisor = scoringService
										.getUsuarioUsuarioSegurForUsername(
												usuarioLogueado.getAlias(),
												usuarioLogueado.getCorreo());
								if (usuarioEmisor != null) {
									if (ultimaListaCorreoPrueba != null
											&& ultimaListaCorreoPrueba.size() > 0) {
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									} else {
										ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
												0);
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									}
								}

								contenidoEmail
										.setListaNuevosCorreos(ultimaListaCorreoPrueba);

								try {
									boolean guardar = true;

									if (emailAlerta != null
											&& (wvarEnviaCorreo == true || !usuarioCargoAux
													.getAdministrador())) {
										emailAlerta
												.enviarSolicitudDm(contenidoEmail);
									}
									/*
									 * se hace el proceso guardar los datos de
									 * la cuenta corriente cuando se ingresa la
									 * solicitud.
									 */
									if (ctaCteList == null
											|| ctaCteList.size() == 0) {
										List<String> listaFechasIngresadas = scoringService
												.getFechasCuentaCorriente(clienteTarget
														.getCleanRut()
														.toUpperCase());
										if (listaFechasIngresadas != null) {
											SimpleDateFormat format = new SimpleDateFormat(
													"dd/MM/yyyy");
											String fechaActual = format
													.format(new Date());
											for (String fechList : listaFechasIngresadas) {
												if (fechaActual
														.equals(fechList)) {
													guardar = false;
													break;
												}
											}
										}
									}
									if (guardar) {
										informacionCtaCte();
										log.debug("antes de ingresar los datos de la cuenta corriente.");
										if (ctaCteList != null
												&& ctaCteList.size() > 0) {
											for (DeudaActual ctaCte : ctaCteList) {
												entityManager.persist(ctaCte);
												entityManager.flush();
											}

										}
										log.debug("fin de ingresar los datos de la cuenta corriente.");
									}

								} catch (Exception e) {
									log.debug("error por que email de alerta esta nulo #0"
											+ e.getMessage());
								}
							}
							/* se setea el numero de solicitud creada */
							insertarSolicitudDTO(solAux);
							setSolicitud(solAux);
						}
					}
				}
			} else if (this.paginaIngreso == 44) {
				if (numeroNuevo != null) {
					solAux.setPeackCredito(new BigDecimal(0));
					solAux.setMontoAsegurado(new BigDecimal(0));
					solAux.setRiesgoKupfer(new BigDecimal(0));
					solAux.setMonto(new BigDecimal(0));
					solAux.setDesTiposol(this.tipoSolicitud
							.getDesTipoSolicitud());
					solAux.setObservSolicitud(this.motivoProrroga);

					StringBuffer cadena = new StringBuffer();
					cadena.append(this.tipoSolicitud.getClaveProceso());
					cadena.append("PR");
					solAux.setTipTiposol(cadena.toString());
					solAux.setSubTiposol(this.tipoSolicitud
							.getDesTipoSolicitud());

					solAux.setGlsObservaciones(modificarTexto(this.motivoProrroga));
					cal2 = Calendar.getInstance();
					cal2.setTime(new Date());
					String horaMinutosSegundos = calcularTiempoDelProceso(cal1,
							cal2, solAux);
					this.setTiempoEjecucionProceso(horaMinutosSegundos);
					this.setTiempoEjecucionMenu(horaMinutosSegundos);
					solAux.setHoraMinutosSegundos(horaMinutosSegundos);
					solAux.setSociedad(this.sociedadAux);
					solAux = scoringService.persistSolicitud(solAux);
					if (solAux != null) {
						Locale locale = new Locale("es", "CL");
						NumberFormat numberFormatter;
						numberFormatter = NumberFormat.getNumberInstance(locale);
						List<ProrrogaDTO> listaProrrogas = new ArrayList<ProrrogaDTO>(
								0);
						ProrrogaDTO obejtoProrroga = null;
						boolean valido = false;
						for (DocumentoProrrogaDTO docto : listaDoctoProrrogaAgregados) {
							LcredSolicitudProrrogaId solProrroga = null;
							if (docto != null && docto.isStatus() == true) {
								solProrroga = new LcredSolicitudProrrogaId();
								SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
								String fechaAux1 = format.format(docto.getFechaVencActual());
								String fechaAux2 = format.format(docto.getFechaVencNuevo());

								solProrroga.setNumSolicitud(numeroNuevo.longValue());
								solProrroga.setNroCheque(docto.getNumeroDocto());
								solProrroga.setMonto(new BigDecimal(docto.getMonto()));
								solProrroga.setVencActual(fechaAux1);
								solProrroga.setVencNuevo(fechaAux2);
							}

							if (solProrroga != null && docto != null) {
								LcredSolicitudProrroga lsp = new LcredSolicitudProrroga();
								lsp.setId(solProrroga);
								valido = scoringService.persistSolicitudProrroga(lsp);
								obejtoProrroga = new ProrrogaDTO();
								obejtoProrroga.setNumeroDocto(solProrroga.getNroCheque());
								obejtoProrroga.setMontoFormateado(numberFormatter.format(solProrroga.getMonto()));
								obejtoProrroga.setMotivoProrroga(this.motivoProrroga);
								obejtoProrroga.setFechaVencicmiento(solProrroga.getVencActual());
								obejtoProrroga.setFechaSolicitado(solProrroga.getVencNuevo());
								listaProrrogas.add(obejtoProrroga);
							}
						}

						LcredSolicitudOtras otrasId = new LcredSolicitudOtras();
						otrasId.setNumSolicitud(solAux.getId().getNumSolicitud());
						otrasId.setCodTipoSolicitud("4");
						otrasId.setStatusSolicitud("I");
						otrasId.setOpcionInicial("PR");
						otrasId.setObservacionesInicial(this.motivoProrroga);
						boolean validoOtra = scoringService
								.persistSolicitudOtras(otrasId);

						if (valido == true && validoOtra == true) {
							listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
							FacesMessages.instance().add(Severity.INFO,
									"Se ha generado solicitud N° #0",
									solAux.getId().getNumSolicitud());
							this.mensajeGlogal = "Se ha generado solicitud N° "
									+ String.valueOf(solAux.getId()
											.getNumSolicitud());

							if (listaFileUploadedDTOs != null) {
								for (FileUploadedDTO archivo : listaFileUploadedDTOs) {
									try {
										OutputStream ostream = null;
										String nombreArchivoAux = archivo
												.getNombreArchivo().replace(
														" ", "_");
										nombreArchivoAux = sacarCaracteresInvalidoParaWeb(nombreArchivoAux);
										String url = "archivosOS"
												+ "/"
												+ solAux.getId()
														.getNumSolicitud()
												+ "_"
												+ tipoSolicitud
														.getCodTipoSolicitud()
												+ "_" + nombreArchivoAux;
										String nombreArchivo = solAux.getId()
												.getNumSolicitud()
												+ "_"
												+ tipoSolicitud
														.getCodTipoSolicitud()
												+ "_" + nombreArchivoAux;
										InputStream istream = new FileInputStream(
												archivo.getUploadItem()
														.getFile());

										// la direccion donde se rescata el
										// archivo esta dado por el path del
										// global parameter + la url formada
										// arriba
										ostream = new FileOutputStream(
												globalParameters
														.getAttachedFilesAbsolutePath()
														+ url);
										log.debug("url #0", archivo
												.getUploadItem()
												.getContentType());
										IOUtils.copyStream(ostream, istream);
										ostream.flush();
										ostream.close();
										istream.close();
										// guardar datos en la BD
										ArchivoAdjunto archivoAux = new ArchivoAdjunto();
										archivoAux.setSolicitud(solAux);
										archivoAux.setUrl(url);
										archivoAux.setUbicacion("");
										archivoAux.setNombreAdjunto(archivo
												.getNombreArchivo());
										archivoAux
												.setTipo(ArchivoAdjuntoType.INGRESO);
										archivoAux.setUsuario(usuarioLogueado);
										archivoAux.setFechaCreacion(new Date());
										entityManager.persist(archivoAux);
										entityManager.flush();

										entityManager.remove(archivo
												.getArchivo());
										entityManager.flush();

										ConceptoDTO objetoAux = new ConceptoDTO();
										objetoAux
												.setNombreArchivo(nombreArchivo);
										objetoAux
												.setRutaCompleta(globalParameters
														.getAttachedFilesWebPath()
														+ "" + url);
										listaArchivoGuardado.add(objetoAux);
										objetoAux = null;

									} catch (FileNotFoundException e) {
										log.error(
												"error al guardar archivo #0",
												e.toString());
										FacesMessages
												.instance()
												.add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (IOException e) {
										log.error(
												"error al guardar archivo #0",
												e.toString());
										FacesMessages
												.instance()
												.add(Severity.ERROR,
														"Error al guardar el archivo "
																+ archivo
																		.getNombreArchivo());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							if ("Enviar".equals(enviar)
									&& listaUsuarioCorreoagregados != null) {

								// EmailAlertaContactoMensajeHelper emailAlerta
								// = null;
								/* setando los varlores del dto para el correo */
								ContenidoEmailSolicitudDTO contenidoEmail = new ContenidoEmailSolicitudDTO();
								contenidoEmail
										.setTituloRespuesta("Generación de solicitud");
								contenidoEmail.setSolicitud(solAux);
								contenidoEmail.setCorreoEmisor(usuarioLogueado
										.getAlias());
								StringBuffer rutNombre = new StringBuffer();
								rutNombre.append(clienteTarget.getRut());
								rutNombre.append(" / ");
								rutNombre.append(modificarTexto(clienteTarget
										.getRazonSocial()));
								contenidoEmail.setRutNombre(rutNombre
										.toString());

								contenidoEmail.setSoloNombre(clienteTarget
										.getRazonSocial());
								contenidoEmail
										.setCanalVenta(modificarTexto(clienteTarget
												.getCanalVenta()));
								contenidoEmail.setSucursal(sucursal
										.getDescripcion());
								contenidoEmail
										.setEmisor(modificarTexto(usuarioLogueado
												.getNombre()));

								contenidoEmail
										.setTipoSolicitud("Prorroga de Cheque");
								contenidoEmail.setTipoProrroga("Prorroga.");
								contenidoEmail
										.setMotivoProrroga(this.motivoProrroga);

								/* datos de archivos */
								contenidoEmail
										.setListaArchivos(listaArchivoGuardado);

								/* datos de prorrogas */
								contenidoEmail
										.setListaProrrogas(listaProrrogas);

								List<Usuariosegur> ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
										0);
								if (listaUsuarioCorreoagregados != null) {
									for (UsuarioCorreoDTO ucdto : listaUsuarioCorreoagregados) {
										if (ucdto != null
												&& ucdto.getAlias() != null
												&& ucdto.getCorreoElectronico() != null) {
											log.debug(
													"ucdto.getAlias().trim() : #0, ucdto.getCorreoElectronico().trim() #1",
													ucdto.getAlias().trim(),
													ucdto.getCorreoElectronico()
															.trim());
											Usuariosegur usuarioAux = scoringService
													.getUsuarioUsuarioSegurForUsername(
															ucdto.getAlias()
																	.trim(),
															ucdto.getCorreoElectronico()
																	.trim());
											if (usuarioAux != null
													&& !"".equals(usuarioAux
															.getCorreo())
													&& !"".equals(usuarioAux
															.getCorreo())) {
												if (usuarioAux != null
														&& (usuarioAux
																.getEliminado() != null && usuarioAux
																.getEliminado()
																.booleanValue() == false)) {
													if (usuarioAux
															.getObligatorio() != null
															&& usuarioAux
																	.getObligatorio()
																	.booleanValue() == true) {
														if (!ultimaListaCorreoPrueba
																.contains(usuarioAux)) {
															ultimaListaCorreoPrueba
																	.add(usuarioAux);
															usuarioAux = new Usuariosegur();
														}
													} else {
														if (scoringService
																.getUsuarioCargoEnvioAutomatico(usuarioAux
																		.getIdPersonal())) {
															if (!ultimaListaCorreoPrueba
																	.contains(usuarioAux)) {
																ultimaListaCorreoPrueba
																		.add(usuarioAux);
																usuarioAux = new Usuariosegur();
															}
														}
													}
												}
											}
										}
									}
								}
								Usuariosegur correoAdministrador = scoringService
										.getUsuarioUsuarioSegurForUsername(
												"ADMINISTRADOR",
												"sck@kupfer.cl");
								if (correoAdministrador != null) {
									ultimaListaCorreoPrueba
											.add(correoAdministrador);
									correoAdministrador = null;
								}

								// insertar el emisor de la solicitud.
								Usuariosegur usuarioEmisor = scoringService
										.getUsuarioUsuarioSegurForUsername(
												usuarioLogueado.getAlias(),
												usuarioLogueado.getCorreo());
								if (usuarioEmisor != null) {
									if (ultimaListaCorreoPrueba != null
											&& ultimaListaCorreoPrueba.size() > 0) {
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									} else {
										ultimaListaCorreoPrueba = new ArrayList<Usuariosegur>(
												0);
										ultimaListaCorreoPrueba
												.add(usuarioEmisor);
									}
								}

								contenidoEmail
										.setListaNuevosCorreos(ultimaListaCorreoPrueba);

								try {
									boolean guardar = true;
									if (emailAlerta != null
											&& (wvarEnviaCorreo == true || !usuarioCargoAux
													.getAdministrador())) {
										emailAlerta
												.enviarSolicitudProrroga(contenidoEmail);
									}
									/*
									 * se hace el proceso guardar los datos de
									 * la cuenta corriente cuando se ingresa la
									 * solicitud.
									 */
									if (ctaCteList == null
											|| ctaCteList.size() == 0) {
										List<String> listaFechasIngresadas = scoringService
												.getFechasCuentaCorriente(clienteTarget
														.getCleanRut()
														.toUpperCase());
										if (listaFechasIngresadas != null) {
											SimpleDateFormat format = new SimpleDateFormat(
													"dd/MM/yyyy");
											String fechaActual = format
													.format(new Date());
											for (String fechList : listaFechasIngresadas) {
												if (fechaActual
														.equals(fechList)) {
													guardar = false;
													break;
												}
											}
										}
									}
									if (guardar) {
										informacionCtaCte();
										log.debug("antes de ingresar los datos de la cuenta corriente.");
										if (ctaCteList != null
												&& ctaCteList.size() > 0) {
											for (DeudaActual ctaCte : ctaCteList) {
												entityManager.persist(ctaCte);
												entityManager.flush();
											}

										}
										log.debug("fin de ingresar los datos de la cuenta corriente.");
									}
								} catch (Exception e) {
									log.debug("error por que email de alerta esta nulo #0"
											+ e.getMessage());
								}
							}
							/* se setea el numero de solicitud creada */
							insertarSolicitudDTO(solAux);
							setSolicitud(solAux);
						}
					}
				}
			}

			/* hitos de solicitud */
			SolicitudHitos hitos = new SolicitudHitos();
			hitos.setEmisor(usuarioLogueado.getAlias());
			hitos.setIdSolicitud(solAux.getId().getNumSolicitud());
			hitos.setUsuarioActual(null);
			hitos.setFechaHora(new Date());
			hitos.setCodigoEstado(estadoAux.getCodEstado());
			hitos.setDescripcionEstado(estadoAux.getDesEstado());
			try {
				long numero = scoringService.obtenerNumeroHitoToLog(1);
				hitos.setSystemId(numero);
				boolean exito = scoringService.persistSolicitudHitos(hitos);
				log.debug("verificacion si inserto registro hitos #0", exito);

			} catch (Exception e) {
				log.error("Error, al insertar el hitos de la solicitud #0",
						e.getMessage());
			}

			/* ingreso de logs */
			try {
				String codigo = "I";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Ingreso inical de la solicitud.");
					exito = scoringService.persistSolicitudLogs(solAux.getId()
							.getNumSolicitud(), usuarioLogueado.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Ingreso inical de la solicitud, no se encontro el estado.");
					exito = scoringService.persistSolicitudLogs(solAux.getId()
							.getNumSolicitud(), usuarioLogueado.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);

			} catch (Exception e) {
				log.error("Error, al insertar el hitos de la solicitud #0",
						e.getMessage());
			}

			/* limpiar datos */
			limpiarSegunTipoSolictud(this.paginaIngreso);
			limpiarDespuesGenerar();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al ingresar los datos de la solictud #0",
					e.getMessage());
		}
		log.debug("Mensaje #0", this.mensajeGlogal);
	}

	/*
	 * es una cadena o frase de palabras donde todoas las palabras que contengan
	 * mas 1 caracter la primera la de en maytusculas y las demas en minusculas.
	 */
	public String modificarTexto(String cadenaAux) {
		StringBuffer cadenaBuffer = new StringBuffer();
		String[] splitRazonSocial = cadenaAux.split(" ");
		for (String cadena : splitRazonSocial) {
			if (cadena.length() > 1 && !cadena.equals("S.A.")) {
				cadenaBuffer.append(capitalizarTexto(cadena));
				cadenaBuffer.append(" ");
			} else {
				cadenaBuffer.append(cadena);
				cadenaBuffer.append(" ");
			}
		}
		return cadenaBuffer.toString();
	}

	public void limpiarSegunTipoSolictud(long tipoSolicitud) {
		try {

			if (tipoSolicitud == 11 || tipoSolicitud == 12) {
				this.monto = (long) 0;
				this.pie = null;
				this.listaCotPedDTOs = null;
				this.tipoFormaPago = null;
				this.encabezado = null;
				this.margenGlobal = (double) 0;
				this.motivoCompra = null;
				this.listaUsuarioCorreoagregados = null;
				this.listaFileUploadedDTOs = null;
				this.observaciones = null;
				this.porcentajeGlobal = (double) 0;
				this.montoTotalNeto = (double) 0;
				this.montoTotal = (double) 0;
				this.habilitar = false;
				

			} else if (tipoSolicitud == 21 || tipoSolicitud == 22 || tipoSolicitud == 23 || tipoSolicitud == 24) {
				this.descripcionProyecto = null;
				this.montoCredito = null;
				this.plazoEjecucion = null;
				this.potencialCompra = null;
				this.conceptoNegocios = null;
				this.rutNombresSocios = null;
				this.observacionesCredito = null;
				this.observaciones = null;
				this.monto = (long) 0;
				this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
				this.listaSocios = new ArrayList<String>(0);
				this.listaSelConceptoNegocio = new ArrayList<ConceptoNegocioDTO>(0);
				evaluarInnominalNominalRKupferAseguradora();
			} else if (tipoSolicitud == 31 || tipoSolicitud == 32) {
				setClsRiesgo(null);
				setcPago(null);
				this.motivoRiesgoPago = null;
				this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
				if (this.paginaIngreso == 31) {
					setCondicionRiesgo(true);
					setComboCRiesgo(false);
					setCondicionPago(false);
					setComboCPago(true);
				} else if (this.paginaIngreso == 32) {
					setCondicionRiesgo(false);
					setComboCRiesgo(true);
					setCondicionPago(true);
					setComboCPago(false);
				}
				evaluarRiesgoPago();
			} else if (tipoSolicitud == 41) {
				this.opcionBloqueoDesbloqueo = null;
				this.motivoBloDesbloqueo = null;
				this.observaciones = null;
				this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);

			} else if (tipoSolicitud == 43) {
				this.listaClienteDMs = new ArrayList<LcredSolicitudDm>(0);
				this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);

			} else if (tipoSolicitud == 44) {
				this.motivoProrroga = null;
				this.listaDoctoProrrogaAgregados = new ArrayList<DocumentoProrrogaDTO>(0);
				this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
			}
		} catch (Exception e) {
			log.error("error al limpiar las variables ...");
		}
	}

	public void limpiarDespuesGenerar() {
		clienteTarget = new ClienteDTO();
		this.setRutAux(null);
		if(this.listaSociedades.size() > 1){
			this.sociedadAux = null;
		}else{
			log.error("sigue la sociedad : #0", this.sociedadAux.getRazonSocial());
		}
		clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
		clienteCompComrcial = new CompComercialDTO();
		this.setDm(null);
	}

	public void limpiarTotal() {
		try {
			this.sociedadAux = null;
			this.setSolicitud(null);
			this.monto = (long) 0;
			this.pie = null;
			this.listaCotPedDTOs = null;
			this.tipoFormaPago = null;
			this.encabezado = null;
			this.margenGlobal = (double) 0;
			this.motivoCompra = null;
			this.listaUsuarioCorreoagregados = null;
			this.listaFileUploadedDTOs = null;
			this.observaciones = null;
			this.porcentajeGlobal = (double) 0;
			this.montoTotalNeto = (double) 0;
			this.montoTotal = (double) 0;
			this.habilitar = false;
			this.descripcionProyecto = null;
			this.montoCredito = null;
			this.plazoEjecucion = null;
			this.potencialCompra = null;
			this.conceptoNegocios = null;
			this.rutNombresSocios = null;
			this.observacionesCredito = null;
			this.observaciones = null;
			this.monto = (long) 0;
			this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(	0);
			this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
			this.listaSocios = new ArrayList<String>(0);
			this.listaSelConceptoNegocio = new ArrayList<ConceptoNegocioDTO>(0);
			setClsRiesgo(null);
			setcPago(null);
			this.motivoRiesgoPago = null;
			this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
			this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
			if (this.paginaIngreso == 31) {
				setCondicionRiesgo(true);
				setComboCRiesgo(false);
				setCondicionPago(false);
				setComboCPago(true);
			} else if (this.paginaIngreso == 32) {
				setCondicionRiesgo(false);
				setComboCRiesgo(true);
				setCondicionPago(true);
				setComboCPago(false);
			}
			this.opcionBloqueoDesbloqueo = null;
			this.motivoBloDesbloqueo = null;
			this.observaciones = null;
			this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
			this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);

			this.listaClienteDMs = new ArrayList<LcredSolicitudDm>(0);
			this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
			this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);

			this.motivoProrroga = null;
			this.listaDoctoProrrogaAgregados = new ArrayList<DocumentoProrrogaDTO>(0);
			this.listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(0);
			this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);
			clienteTarget = new ClienteDTO();
			this.setRutAux(null);
			clienteExpoRiesgoKh = new ExpoRiesgoKhDTO();
			clienteCompComrcial = new CompComercialDTO();
			this.setDm(null);
			setMuestraSeleccionSolicitud(false);
			setPedidoCotizacion(null);

			/*limpiar si exite mas de una sociedad*/
			if(this.listaSociedades.size() > 1){
				this.sociedadAux = null;
			}else{
				log.error("sigue la sociedad : #0", this.sociedadAux.getRazonSocial());
			}
			
		} catch (Exception e) {
			log.error("error al limpiar las variables ...");
		}
	}

	public String generarDescripcionProducto() {
		StringBuilder cadena = new StringBuilder();
		int contador = 1;
		try {
			if (listaCotPedDTOs != null)
				for (CotPedDTO encabezado : listaCotPedDTOs) {
					if (encabezado.getListaCabeceraCotPeds() != null) {
						for (CabeceraCotPedDTO o : encabezado
								.getListaCabeceraCotPeds()) {
							if (o.getListaDetalle() != null) {
								for (DetalleCotPedDTO obj : o.getListaDetalle()) {
									contador = 1;
									if (obj.getListaDetalleCp() != null) {
										cadena.append("Cotización y/o pedido: "
												+ encabezado
														.getNumeroCotizacion());
										cadena.append(" - Negocio: "
												+ obj.getNegocio()
														.getDesNegocio().trim());
										cadena.append(" - Concepto: "
												+ obj.getNegocio()
														.getDescripcion()
														.trim());
										cadena.append("\n");
										for (DetalleCp dcp : obj
												.getListaDetalleCp()) {
											cadena.append(contador + ").-");
											cadena.append(dcp.getNombre());
											cadena.append("\n");
											contador++;
										}
									}
								}
							}
						}
					}
				}

		} catch (Exception e) {
			log.error("Error, al sacar la descripcion de los productos.",
					e.getMessage());
		}
		return cadena.toString();
	}

	public void salirDelIngresoSolicitud() {
		log.debug("salir del pop.");
		if (this.salirDelMensaje == false) {
			this.salirDelMensaje = true;
		}

	}

	/*
	 * creacion de los metodos, para poder subir logo de colegio
	 */
	public List<UploadItem> archivosSubidos = new ArrayList<UploadItem>();

	public List<UploadItem> getArchivosSubidos() {
		if (archivosSubidos != null && archivosSubidos.size() > 0) {
			UploadItem obj = (UploadItem) archivosSubidos.get(0);
			log.debug(obj.getData());
		}
		return archivosSubidos;
	}

	public void setArchivosSubidos(List<UploadItem> archivosSubidos) {
		this.archivosSubidos = archivosSubidos;
	}

	public void limpiarArchivosSubidos() {
		archivosSubidos.clear();
	}

	/*** Obtencion datos cuenta corriente ***/
	private List<DeudaActual> ctaCteList = new ArrayList<DeudaActual>();

	public List<DeudaActual> getCtaCteList() {
		return ctaCteList;
	}

	public void informacionCtaCte() {
		
		if(this.sociedadAux == null){
			FacesMessages.instance().add(Severity.ERROR,"Se debe seleccionar la sociedad para consultar");
			this.mensajeGlogal = "Se debe seleccionar la sociedad para consultar.";
			return;
		}
	
		/* limpiar la lista */
		ctaCteList = new ArrayList<DeudaActual>(0);
		this.setFechaDeauda(null);
		this.setMontoActualCuentaCorriente(new Long(0));

		/* fecha Actual */
		setFechaActualCtaCte(new Date());

		SapSystem system = new SapSystem(globalParameters.getNameSap(),
				globalParameters.getIpSap(), globalParameters.getClientSap(),
				globalParameters.getSystemNumberSap(),
				globalParameters.getUserSap(),
				globalParameters.getPasswordSap());

		Connect connect = new Connect(system);
		//functionCtaCte = connect.getFunction("ZFIFN_CTACTE"); // Nombre RFC
		functionCtaCte = connect.getFunction("ZFIFN_CTACTE_R");
		functionCtaCte.getImportParameterList().setValue("CODCLIENTE",clienteTarget.getCleanRut().toUpperCase()); // Paso de
		functionCtaCte.getImportParameterList().setValue("SOCIEDAD",this.getSociedadAux().getCodigoSociedad());															// parametros
		connect.execute(functionCtaCte);
		JCoTable datosCtaCte = functionCtaCte.getTableParameterList().getTable("DETALLE"); // tabla de salida
		log.debug(clienteTarget.getCleanRut().toUpperCase());
		log.debug(datosCtaCte);

		for (int i = 0; i < datosCtaCte.getNumRows(); i++) {
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
			ctaCte.setMonto(((BigDecimal) datosCtaCte.getValue("MTO_DOCTO")).longValue());
			ctaCte.setCodCliente(datosCtaCte.getValue("CODCLIENTE").toString());
			ctaCte.setTexto(datosCtaCte.getValue("TEXTO").toString());
			ctaCte.setDiasMora(((BigDecimal) datosCtaCte.getValue("DIASMORA")).intValue());
			ctaCte.setFechaIngreso(new Date());
			this.montoActualCuentaCorriente += ctaCte.getMonto();
			ctaCteList.add(ctaCte);

		}

		if (listaFechas == null) {
			listaFechas = scoringService.getFechasCuentaCorriente(clienteTarget	.getCleanRut().toUpperCase());
		}

	}

	public void guardarInformacionCtaCte() {
		try {
			boolean ingresar = scoringService.getVerificarHistorialCuentaCorriente(clienteTarget.getCleanRut().toUpperCase(), new Date());
			if (ingresar == false) {
				if (ctaCteList != null && ctaCteList.size() > 0) {
					for (DeudaActual ctaCte : ctaCteList) {
						entityManager.persist(ctaCte);
					}
					entityManager.flush();
				}
				FacesMessages
						.instance()
						.add(Severity.INFO,
								"Se guardo la información actual de la cuenta corriente con fecha actual.");
			} else {
				FacesMessages.instance().add(Severity.WARN,
						"Ya existe registros con la fecha actual.");
			}

		} catch (Exception e) {
			log.error(
					"Error al guardar registro de la cuenta corriente del cliente #0",
					e.getMessage());
			FacesMessages.instance().add(Severity.ERROR,
					"Error al ingresar los registro de la cuenta corriente.");
		}
	}

	public void consultarInformacionCtaCteFechaDeterminada() {
		/* fecha Actual */
		if (this.fechaDeauda == null) {
			FacesMessages.instance().add(Severity.WARN,
					"Debe seleccionar una fecha menos o igual a la actual.");
			return;
		} else {
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

		List<DeudaActual> listaDeudas = scoringService
				.getConsultarHistorialCuentaCorriente(clienteTarget
						.getCleanRut().toUpperCase(), fecha);
		if (listaDeudas != null && listaDeudas.size() > 0) {
			for (DeudaActual da : listaDeudas) {
				this.montoActualCuentaCorriente += da.getMonto();
			}
			ctaCteList = listaDeudas;
		} else {
			if (ctaCteList != null) {
				ctaCteList = null;
			}
		}

	}

	public void limpiarInformacionCtaCteFechaDeterminada() {
		try {
			this.setFechaDeauda(null);
			informacionCtaCte();

		} catch (Exception e) {
			log.error("error al limpiar la grilla y actualizar nuevamente ",
					e.getMessage());
		}

	}

	public void agregarConceptoListaReserva() {
		boolean exito = false;

		if (this.listaCboConceptoNegocioSeleccion == null) {
			FacesMessages.instance().add(Severity.WARN,
					"Debe seleccionar un concepto para ingresar a la lista.");
			mensajeExplicativo = "Error";
			return;
		} else {
			if (this.listaCboConceptoNegocioSeleccion.size() == 0) {
				FacesMessages
						.instance()
						.add(Severity.WARN,
								"Debe seleccionar un concepto para ingresar a la lista.");
				mensajeExplicativo = "Error";
				return;
			}
		}

		if (listaSelConceptoNegocio != null) {
			for (ConceptosNegocio cn : this.listaCboConceptoNegocioSeleccion) {
				ConceptoNegocioDTO nuevo = new ConceptoNegocioDTO();
				nuevo.setConceptoNegocio(cn);
				nuevo.setMonto((long) 0);
				for (ConceptoNegocioDTO obj : listaSelConceptoNegocio) {
					if (obj.getConceptoNegocio().getConcepto()
							.equals(nuevo.getConceptoNegocio().getConcepto())) {
						exito = true;
					}
				}
				if (exito == false) {
					listaSelConceptoNegocio.add(nuevo);
					setConceptoNegocioCbo(null);
					setMontoConcepto((long) 0);
				} else {
					log.debug(" el concepto que ya existe en la grilla es #0",
							nuevo.getConceptoNegocio().getConcepto());
				}
				nuevo = null;
			}

		} else {
			listaSelConceptoNegocio = new ArrayList<ConceptoNegocioDTO>(0);
			for (ConceptosNegocio cn : this.listaCboConceptoNegocioSeleccion) {
				ConceptoNegocioDTO nuevo = new ConceptoNegocioDTO();
				nuevo.setConceptoNegocio(cn);
				nuevo.setMonto((long) 0);
				listaSelConceptoNegocio.add(nuevo);
				nuevo = null;
			}
		}

		this.listaCboConceptoNegocioSeleccion.clear();
		this.listaCboConceptoNegocio = null;
		if (this.listaCboConceptoNegocio == null) {
			/* sacar concepto y negocio */
			setListaCboConceptoNegocio(scoringService
					.obtenerConceptosNegocios());
		}
	}

	/* eliminar Concepto de Negocio */
	public void eliminarConceptoNegocioListaAgregado(
			ConceptoNegocioDTO conceptoNegocio) {
		if (conceptoNegocio != null) {
			listaSelConceptoNegocio.remove(conceptoNegocio);
		}
		this.listaCboConceptoNegocio = null;
		if (this.listaCboConceptoNegocio == null) {
			/* sacar concepto y negocio */
			setListaCboConceptoNegocio(scoringService
					.obtenerConceptosNegocios());
		}
	}

	/* seteo stringe de Concepto de Negocio */
	public void generarStringConceptoAsociado() {
		List<String> listaNegociosParticulares = new ArrayList<String>(0);
		List<String> listaConceptoParticulares = new ArrayList<String>(0);
		StringBuffer negocios = new StringBuffer();
		StringBuffer conceptos = new StringBuffer();
		long montofinal = 0;
		if (this.potencialCompra == null) {
			montofinal = 0;
		} else {
			montofinal = this.potencialCompra;
		}

		try {
			if (listaSelConceptoNegocio != null) {
				int cantidad = listaSelConceptoNegocio.size();
				int contador = 1;
				String cadena = "";
				for (ConceptoNegocioDTO cn : listaSelConceptoNegocio) {
					if (contador < cantidad) {
						cadena += (cn.getConceptoNegocio().getDescripcion())
								.trim() + "-";
					} else if (contador == cantidad) {
						cadena += (cn.getConceptoNegocio().getDescripcion())
								.trim() + "";
					}
					montofinal += cn.getMonto();
					contador++;

					if (!listaNegociosParticulares.contains(cn
							.getConceptoNegocio().getNegocio())) {
						listaNegociosParticulares.add(cn.getConceptoNegocio()
								.getNegocio());
						if (contador < cantidad) {
							negocios.append(cn.getConceptoNegocio()
									.getNegocio() + ",");
						} else if (contador == cantidad) {
							negocios.append(cn.getConceptoNegocio()
									.getNegocio());
						}
					}
					if (!listaConceptoParticulares.contains(cn
							.getConceptoNegocio().getConcepto())) {
						listaConceptoParticulares.add(cn.getConceptoNegocio()
								.getConcepto());
						if (contador < cantidad) {
							conceptos.append(cn.getConceptoNegocio()
									.getConcepto() + ",");
						} else if (contador == cantidad) {
							conceptos.append(cn.getConceptoNegocio()
									.getConcepto());
						}
					}
				}
				setConceptoNegocios(cadena);
				if (montofinal == 0) {
					setPotencialCompra(null);
				} else {
					setPotencialCompra(montofinal);
				}
			}

			obtenerMontoCredito(negocios.toString(), conceptos.toString());

		} catch (Exception e) {
			log.error("Error al setear el string de concepto ", e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	public void agregarSociosListaReserva() {

		if (this.rutSocio == null && this.socio == null) {
			FacesMessages.instance().add(Severity.ERROR,
					"Debe ingresar el rut del socio  o el nombre del socio.");
			mensajeExplicativo = "Error";
			return;
		} else {
			if ("".equals(this.rutSocio) && "".equals(this.socio)) {
				FacesMessages
						.instance()
						.add(Severity.ERROR,
								"Debe ingresar el rut del socio  o el nombre del socio.");
				mensajeExplicativo = "Error";
				return;
			}
		}

		String cadena = "";

		if (this.rutSocio != null && !"".equals(this.rutSocio)) {
			cadena += this.rutSocio;
		}
		if (this.socio != null && !"".equals(this.socio)) {
			if (this.rutSocio != null && !"".equals(this.rutSocio)) {
				cadena += "-->";
				cadena += this.socio;
			} else {
				cadena += this.socio;
			}
		}

		if (cadena != null || !"".equals(cadena)) {
			if (listaSocios != null) {
				if (!listaSocios.contains(cadena)) {
					listaSocios.add(cadena);
					setRutSocio(null);
					setSocio(null);
				} else {
					FacesMessages
							.instance()
							.add(Severity.ERROR,
									"El concepto seleccionado ya esta agregado en la grilla.");
					mensajeExplicativo = "Error";
				}
			} else {
				listaSocios = new ArrayList<String>(0);
				listaSocios.add(cadena);
				setRutSocio(null);
				setSocio(null);
			}
		} else {
			FacesMessages.instance().add(Severity.WARN,
					"Debe seleccionar un socio para ingresar a la lista.");
			mensajeExplicativo = "Error";
			return;
		}
	}

	/* seteo string de socios */
	public void generarStringSocios() {
		try {
			if (listaSocios != null) {
				int cantidad = listaSocios.size();
				int contador = 1;
				String cadena = "";
				for (String cn : listaSocios) {

					if (contador < cantidad) {
						cadena += cn + ",";
					} else if (contador == cantidad) {
						cadena += cn + ".";
					}
					contador++;
				}
				setRutNombresSocios(cadena);
			}
		} catch (Exception e) {
			log.error("Error al setear el string de rut y nombre del socio ",
					e.getMessage());
		}
	}

	/* eliminar Concepto de Negocio */
	public void eliminarSociosListaAgregado(String socio) {
		if (socio != null) {
			listaSocios.remove(socio);
		}
	}

	public void ObtenerEstadoSolictud(String codigo) {
		try {
			estadoInicial = (LcredEstado) entityManager.find(LcredEstado.class,
					codigo);
		} catch (Exception e) {
			log.error("Error, al sacar el objeto de estado para la solicitud #0",e.getMessage());
			estadoInicial = null;
		}
	}

	public void limpiarDatos() {
		if (ctaCteList != null) {
			ctaCteList.clear();
		}

	}

	public void seleccionOpcionesBloqueoDesbloqueo() {
		if (this.opcionBloqueoDesbloqueo != null) {
			log.debug("la opcion seleccionada es #0",
					this.opcionBloqueoDesbloqueo);
			if (this.sucursal != null) {
				obtenerCorreoBloqueDesbloqueo();
			} else {
				this.opcionBloqueoDesbloqueo = null;
				this.mensajeGlogal = "Debe seleccionar la sucursal del emisor antes de seleccionar (Bloqueo y/o Desbloquedo).";
			}

		} else {
			log.error("error de seleccion viene vacio");
		}
	}

	public void obtenerCorreoBloqueDesbloqueo() {
		try {
			if (listaDestinatarios != null) {
				listaDestinatarios.clear();
			}
			/* va a buscar los destinatarios de las solictudes */
			try {
				listaDestinatarios = scoringService.obtenerListaDestinatario(
						String.valueOf(this.tipoSolicitudCodigo), String
								.valueOf(clienteTarget.getCodigoCanal()
										.ordinal()), sucursal.getZona(),
						sucursal.getCodigo(), "", "","1");

				if (listaDestinatarios != null) {
					listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>();
					listaDescripcionUsuariosCorreos = new ArrayList<String>(0);
					listaUsuarioCorreos = new ArrayList<UsuarioCorreoDTO>(0);
					for (DestinatarioDTO dto : listaDestinatarios) {
						if (dto != null) {
							log.error(
									"tipo:#0 ,username:#1 ,zona :#2 , sucursal:#3 , concepto:#4, negocio:#5 ",
									dto.getTipoSolicitud(), dto.getUsername(),
									dto.getZona(), dto.getSucursal(),
									dto.getConcepto(), dto.getNegocio());

						}
					}
					agregarAlaListaCorreo();
				}
			} catch (Exception e) {
				log.error("Error, sacar los destinatarios : #0", e.getMessage());
			}
		} catch (Exception e) {
			log.error("Error, al sacar los correo de bloque y desbloqueo #0",
					e.getMessage());
		}
	}

	public void obtenerChequeProrroga() {
		/* limpiar la lista */
		if (this.clienteTarget != null && this.clienteTarget.getRut() != null) {

			StringBuilder pedCotBuild = null;
			listaDoctoProrroga = new ArrayList<DocumentoProrrogaDTO>(0);

			SapSystem system = new SapSystem(globalParameters.getNameSap(),
					globalParameters.getIpSap(),
					globalParameters.getClientSap(),
					globalParameters.getSystemNumberSap(),
					globalParameters.getUserSap(),
					globalParameters.getPasswordSap());

			Connect connect = new Connect(system);
			//functionCtaCte = connect.getFunction("ZFIFN_CTACTE"); // Nombre RFC
			functionCtaCte = connect.getFunction("ZFIFN_CTACTE_R");
			functionCtaCte.getImportParameterList().setValue("CODCLIENTE",clienteTarget.getCleanRut().toUpperCase()); // Paso de
			functionCtaCte.getImportParameterList().setValue("SOCIEDAD",this.getSociedadAux().getCodigoSociedad());	
			
			connect.execute(functionCtaCte);
			JCoTable datosCtaCte = functionCtaCte.getTableParameterList().getTable("DETALLE"); // tabla de salida
			log.debug(clienteTarget.getCleanRut().toUpperCase());
			log.debug(datosCtaCte);

			for (int i = 0; i < datosCtaCte.getNumRows(); i++) {
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
				ctaCte.setMonto(((BigDecimal) datosCtaCte.getValue("MTO_DOCTO")).longValue());
				ctaCte.setCodigoCliente(datosCtaCte.getValue("CODCLIENTE").toString());
				ctaCte.setTexto(datosCtaCte.getValue("TEXTO").toString());
				ctaCte.setDiasMoras(((BigDecimal) datosCtaCte.getValue("DIASMORA")).intValue());
				if ("DC".equals(ctaCte.getCl()) || "DA".equals(ctaCte.getCl()) || "SC".equals(ctaCte.getCl()) 
				 || "IC".equals(ctaCte.getCl())	|| "CD".equals(ctaCte.getCl()) || "AB".equals(ctaCte.getCl())
				 || "PA".equals(ctaCte.getCl())) {
					
					pedCotBuild = new StringBuilder(ctaCte.getNumeroDocto());
					while (pedCotBuild.toString().length() < 10) {
						pedCotBuild.insert(0, '0');
					}
					ctaCte.setNumeroDocto(pedCotBuild.toString());
					if (ctaCte.getMonto() > 0) {
						listaDoctoProrroga.add(ctaCte);
						log.error(pedCotBuild.toString());

					}

				}
			}
		} else {
			this.mensajeGlogal = "Debe consultar por un cliente antes de seleccionar documentos para prorrogas.";
		}
	}

	@Asynchronous
	@SuppressWarnings("unchecked")
	public void sacarSolicitudAnalizarAprobar() {
		Sucursal suc = null;
		this.setEstadoFilter("");
		this.setProcesoFilter("");
		this.setCanalFilter("");
		this.setSucursalFilter("");
		try {
			LcredPerfiles perfil1 = scoringService.obtenerPerfil("1");
			LcredPerfiles perfil2 = scoringService.obtenerPerfil("6");
			Date fechaBusqueda = null;
			DateFormat formatoFechaBusqueda = new SimpleDateFormat("yyyy-MM-dd");
			try {
				fechaBusqueda = formatoFechaBusqueda.parse("2013-06-01");
				log.debug(fechaBusqueda);
			} catch (Exception e) {
				throw new Exception(e);
			}
			if (usuarioLogueado != null) {
				sacarEstadosSolicitudes();
				List<Integer> array = scoringService.obtenerPerfilesDelUsuario(usuarioLogueado.getIdPersonal());

				/************ Solicitudes pendientes para Analizar o Aprobar **********************/
				if (array != null && array.size() > 0) {
					// List<LcredSolicitud> listaSolicitudesAnalizar =
					// (List<LcredSolicitud>) entityManager
					// //Cambiar query para obtener las solicitudes pendientes
					// para aprobar 'I','E','N','DE','DR','DC'
					// .createQuery(
					// "Select s from LcredSolicitud s "
					// +
					// " Where s.estado in ('I','E','N','DE','DR','DC','B','SA')"
					// + " and s.id.numSolicitud > 29347 "
					// + " and s.id.fecSolicitud >:fecha "
					// + " order by s.id.numSolicitud desc")
					// .setMaxResults(400)
					// .setParameter("fecha", fechaBusqueda)
					// .getResultList();

					List<Object[]> listaTodasSolicitudesNueva = (List<Object[]>) entityManager
							.createNativeQuery(
									" SELECT sol.[num_solicitud] as numero"
											+ // 0
											" ,sol.[id_canal] as codigo_canal"
											+ // 1
											" ,sol.[fec_solicitud]  as fecha_sol"
											+ // 2
											" ,sol.[hra_solicitud] as hora_sol"
											+ // 3
											" ,cast(sol.[cod_emisor]  as varchar) as codigo_emisor"
											+ // 4
											" ,cast(sol.[sucursal_emisor]  as varchar) as suc_emisor"
											+ // 5
											" ,cast(sol.[rut_cliente] as varchar)  as rut_cliente"
											+ // 6
											" ,cast(sol.[nom_cliente] as varchar)  as nombre "
											+ // 7
											" ,cast(sol.[cod_sucursal]  as varchar) as cod_sucursal"
											+ // 8
											" ,cast(sol.[des_tiposol]  as varchar) as des_tiposol"
											+ // 9
											" ,cast(sol.[tip_tiposol]  as varchar) as tip_tiposol"
											+ // 10
											" ,sol.[fec_salida] as fecha_salida"
											+ // 11
											" ,cast(sol.[usuario_actual]  as varchar) as usuario_actual"
											+ // 12
											" ,sol.[peack_credito] as peak_credito"
											+ // 13
											" ,sol.[monto_asegurado] as montoAsegudado"
											+ // 14
											" ,sol.[riesgo_kupfer] as riesgo_kupfer"
											+ // 15
											" ,cast(sol.[forma_pago]  as varchar) as forma_pago"
											+ // 16
											" ,sol.[fec_salida] as fecha_salida"
											+ // 17
											" ,cast(sol.[estado]  as varchar) as estado"
											+ // 18
											" ,cast(sol.[canal]  as varchar) as canal "
											+ // 19
											" ,sol.[monto] as monto "
											+ // 20
											" ,sol.[evaluar] as evaluar "
											+ // 21
											" ,sol.[analizar] as analizar "
											+ // 22
											" ,cast(sol.[usuario_varios]  as varchar) as usuario_varios"
											+ // 23
											" ,cast(sol.[sub_tiposol]  as varchar) as sub_tiposol"
											+ // 24
											" ,sol.[cod_usuario_devuelve] as cod_usuario_devuelve"
											+ // 25
											" ,cast(sol.[usuario_actual]  as varchar) as usuario_actual_aux"
											+ // 26
											" ,sol.[peack_credito] as peak_credito "
											+ // 27
											" FROM [SCORING_PRODUCION].[dbo].[vw_lcred_solicitudes] sol "
											+ " where sol.estado not in ('A','R','NU','NC','P')"
											+ " and sol.num_solicitud > 29347  "
											+ " and sol.fec_solicitud > convert(datetime, '2013-06-01', 111) "
											+ " and sol.sucursal_emisor + convert(varchar,sol.id_canal) in "
											+ " ( select zsnc.codigo + CONVERT(varchar,cuc.tipo_cuenta) "
											+ "   from [SCORING_PRODUCION].[dbo].[zona_sucursal_negocio_concepto] zsnc  "
											+ "    left join [SCORING_PRODUCION].[dbo].[perfil_funcion_canal] pfc on zsnc.perfil_funcion_canal = pfc.id_canal_usuario_cargo "
											+ "    left join [SCORING_PRODUCION].[dbo].[canal_usuario_cargo] cuc on pfc.canal_usuario_cargo = cuc.id_canal_usuario_cargo "
											+ "    left join [SCORING_PRODUCION].[dbo].[usuario_cargo] uc on cuc.usuario_cargo = uc.id_usuario_cargo"
											+ "   where   uc.usuario_username=:id_personal "
											+ "   and pfc.funcion in (1,3,4,5)"
											+ "  ) " + // 1,3,4
											" order by sol.num_solicitud desc; ")
							.setParameter("id_personal",usuarioLogueado.getIdPersonal())
							.setMaxResults(500).getResultList();

					if (listaTodasSolicitudesNueva != null) {
						log.debug("cantidad de registro #0", listaTodasSolicitudesNueva.size());
					}

					if (listaTodasSolicitudesNueva != null) {
						listaSolicitudesPendientesAnalizarDto = new ArrayList<SolicitudDTO>(0);
						listaUsuariosPendientes = new ArrayList<String>(0);
						listaTiposSolicitudesPendientes = new ArrayList<String>(0);
						listaEstadoPendienteString = new ArrayList<String>(0);
						listaCanalPendienteString = new ArrayList<String>(0);
						listaProcesoPendienteString = new ArrayList<String>(0);
						listaSucursalSolicitudesPendientes = new ArrayList<String>();

						for (Object[] sol : listaTodasSolicitudesNueva) {

							int tipoCuenta = 0;
							boolean autorizacion = false;
							if (sol[19] != null) {
								if ("Mixto".equals((sol[19].toString()))) {
									autorizacion = verificaCanal(TipoCuentasKupferType.MX);
									tipoCuenta = TipoCuentasKupferType.MX.ordinal();
								} else if ("Kupfer Express".equals((sol[19].toString()))) {
									autorizacion = verificaCanal(TipoCuentasKupferType.KX);
									tipoCuenta = TipoCuentasKupferType.KX.ordinal();
								} else if ("Grandes Cuentas".equals((sol[19].toString()))) {
									autorizacion = verificaCanal(TipoCuentasKupferType.GC);
									tipoCuenta = TipoCuentasKupferType.GC.ordinal();
								}
							}

							boolean exito = false;
							// log.debug("solicitud #0  y sucursal : #1 ",
							// sol.getId().getNumSolicitud(),sol.getCodSucursal());
							boolean aprobar = false;
							boolean ejecutivo = false;
							boolean financiero = false;
							boolean comenta = false;
							if (sol[5] != null) {
								for (Integer obj : array) {
									if (obj != null && obj.intValue() != 0) {
										exito = scoringService.obtenerHabilitacionUsuario(usuarioLogueado.getIdPersonal(),sol[5].toString(),tipoCuenta, obj.intValue());
										// log.debug(" sucursal #0 -- canal #1 -- exito #2",
										// sol[8].toString(),obj.intValue(),exito);
										if (obj.intValue() == 1	&& exito == true) {
											aprobar = true;
										} else if (obj.intValue() == 3	&& exito == true) {
											ejecutivo = true;
										} else if (obj.intValue() == 4 	&& exito == true) {
											financiero = true;
										} else if (obj.intValue() == 5 	&& exito == true) {
											comenta = true;
										}
									}
								}
								if (aprobar == true || ejecutivo == true || financiero == true) {
									boolean aprobarNegocio = false;
									boolean ejecutivoNegocio = false;
									boolean financieroNegocio = false;
									boolean comentaNegocio = false;
									// evaluar negocio autorizado para este
									// perfil
									if (sol[10] != null && sol[0] != null) {
										if ((sol[10].toString()).equals("1N") || (sol[10].toString()).equals("1C")) {
											List<String> listaNegocios = scoringService.obtenerNegociosAsociadoToSolicitud(Long.parseLong(sol[0].toString()));
											if (listaNegocios != null && listaNegocios.size() > 0) {
												boolean validaNegocio = false;
												for (String codigoNegocio : listaNegocios) {
													for (Integer obj : array) {
														if (obj != null && obj.intValue() != 0) {
															validaNegocio = scoringService.obtenerHabilitacionUsuarioNegocio(usuarioLogueado.getIdPersonal(),
																			codigoNegocio,
																			tipoCuenta,
																			obj.intValue());
															if (obj.intValue() == 1	&& validaNegocio == true) {
																aprobarNegocio = true;
															} else if (obj.intValue() == 3 && validaNegocio == true) {
																ejecutivoNegocio = true;
															} else if (obj.intValue() == 4	&& validaNegocio == true) {
																financieroNegocio = true;
															} else if (obj.intValue() == 5 	&& validaNegocio == true) {
																comentaNegocio = true;
															}
														}
													}
												}
											}
										} else if ((sol[10].toString()).equals("2I") || (sol[10].toString()).equals("2N") || (sol[10].toString()).equals("2R")) {
											List<String> listaNegocios = scoringService.obtenerNegociosAsociadoToSolicitudLineaCredito(Long.parseLong(sol[0].toString()),conceptosNegociosSessions);
											if (listaNegocios != null && listaNegocios.size() > 0) {
												for (Integer obj : array) {
													if (obj != null	&& obj.intValue() != 0) {
														boolean validaNegocio = false;
														for (String codigoNegocio : listaNegocios) {
															validaNegocio = scoringService.obtenerHabilitacionUsuarioNegocio(usuarioLogueado.getIdPersonal(),
																															codigoNegocio,
																															tipoCuenta,
																															obj.intValue());
															if (obj.intValue() == 1 && validaNegocio == true) {
																aprobarNegocio = true;
															} else if (obj.intValue() == 3 	&& validaNegocio == true) {
																ejecutivoNegocio = true;
															} else if (obj.intValue() == 4 	&& validaNegocio == true) {
																financieroNegocio = true;
															} else if (obj.intValue() == 5 	&& validaNegocio == true) {
																comentaNegocio = true;
															}
														}
													}
												}
											}
										} else if ((sol[10].toString())	.equals("4B") || (sol[10].toString()).equals("4PR")	|| (sol[10].toString()).equals("4BD")
												|| (sol[10].toString()).equals("4DM")) {
											if (sol[0] != null) {
												// log.debug("solicitud #0",
												// sol[0].toString());
											}
											aprobarNegocio = true;
											ejecutivoNegocio = true;
											financieroNegocio = true;
										} else {

											/*
											 * si la solictud no es ni venta
											 * puntual y lineas de credito.
											 */
											aprobarNegocio = true;
											ejecutivoNegocio = true;
											financieroNegocio = true;
										}
									}

									/* evaluacion de negocios */
									if (aprobarNegocio == true || ejecutivoNegocio == true	|| financieroNegocio == true) {
										exito = true;
									} else if (comentaNegocio == true) {
										exito = false;
									}

								} else if (comenta == true) {
									exito = false;
								}
							}

							// log.debug("validaciones: #0", exito);
							if (autorizacion == true && exito == true) {
								SolicitudDTO nuevo = new SolicitudDTO();
								nuevo.setIdSolictud(Long.parseLong(sol[0].toString()));
								if (nuevo.getIdSolictud() == 29638) {
									log.debug("solicitud #0",nuevo.getIdSolictud());
								}

								if (sol[6] != null) {
									nuevo.setRut(sol[6].toString().replace("-",""));
								}
								if (sol[7] != null) {
									nuevo.setRazonSocial(sol[7].toString());
									nuevo.setRazonSocialString(sol[7].toString());
								}
								if (sol[4] != null) {
									nuevo.setEmisor(sol[4].toString().toUpperCase().toUpperCase());
								}
								if (sol[24] != null) {
									nuevo.setDespTipoSolictud(sol[24].toString());
								}
								if (sol[11] != null) {
									nuevo.setFechaSalida((Date) sol[11]);
								}

								if (sol[10] != null) {
									nuevo.setTipoSolicitud(sol[10].toString());
								}

								if (sol[19] != null) {
									nuevo.setCanal(sol[19].toString());
								}

								if (sol[2] != null && sol[3] != null) {

									/* rescatar la fecha */
									SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
									String fecha = sdf.format((Date) sol[2]);

									/* rescatar la hora */
									SimpleDateFormat sdh = new SimpleDateFormat("HH:mm");
									String hora = sdh.format((Date) sol[3]);

									/* fecha y hora */
									// log.debug(" Fecha String:" + fecha);
									// log.debug(" hora String :" + hora);

									DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yy HH:mm");
									try {
										Date d = formatoFecha.parse(fecha + " " + hora);
										nuevo.setFechaString(fecha + " " + hora);
										nuevo.setFechaEmision(d);
										nuevo.setHoraEmision((Date) sol[3]);
									} catch (Exception e) {
										throw new Exception(e);
									}
								}

								if (sol[18] != null) {
									LcredEstado estado = obtenerObjetoLcredEstado(sol[18].toString());
									if (estado != null) {
										nuevo.setCodigoEstado(estado.getCodEstado());
										nuevo.setEstado(estado.getDesEstado());
									}

									if (array != null) {
										for (Integer obj : array) {
											if (obj != null) {
												if (obj.intValue() == 1	|| obj.intValue() == 3 || obj.intValue() == 4) {
													nuevo.setProceso(perfil1.getDesPerfil());
													break;
												} else {
													nuevo.setProceso(perfil2.getDesPerfil());
													break;
												}
											}
										}
									}
								}

								if (sol[5] != null) {
									// log.debug(" codigo de sucursal  :" +
									// sol.getSucursalEmisor());
									suc = obtenerObjetoSucursal(sol[5].toString());
									if (suc != null) {
										nuevo.setSucursal(suc.getDescripcion());
									} else {
										nuevo.setSucursal(sol[5].toString());
									}

								}
								if (sol[14] != null) {
									nuevo.setMontoAsegurado(Double.parseDouble(sol[14].toString()));
								} else {
									nuevo.setMontoAsegurado((double) 0);
								}

								if (sol[20] != null) {
									nuevo.setMonto(Double.parseDouble(sol[20].toString()));
								} else {
									nuevo.setMonto((double) 0);
								}

								if (sol[15] != null) {
									nuevo.setMontoRiegoKupfer(Double.parseDouble(sol[15].toString()));
								} else {
									nuevo.setMontoRiegoKupfer((double) 0);
								}

								if (sol[21] != null) {
									nuevo.setEvaluar(Boolean.valueOf(sol[21].toString()));
								} else {
									nuevo.setEvaluar(false);
								}
								if (sol[22] != null) {
									if (sol[22].toString().equals("1")) {
										nuevo.setAnalizar(true);
									} else {
										nuevo.setAnalizar(false);
									}

								} else {
									nuevo.setAnalizar(false);
								}
								if (sol[16] != null) {
									nuevo.setCondicionPago(sol[16].toString());
								} else {
									nuevo.setCondicionPago("");
								}

								if (sol[25] != null) {
									nuevo.setUsuarioTomada(sol[25].toString());
								} else {
									nuevo.setUsuarioTomada("");
								}
								if (sol[12] != null) {
									nuevo.setUsuarioProceso(sol[12].toString());
								} else {
									nuevo.setUsuarioProceso("");
								}

								if (sol[27] != null) {
									nuevo.setPeakCredito(Double
											.parseDouble(sol[27].toString()));
								} else {
									nuevo.setPeakCredito((double) 0);
								}

								LcredSolicitud solicitudAux = null;
								try {
									solicitudAux = (LcredSolicitud) entityManager
											.createQuery("select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud")
											.setParameter("numSolicitud",nuevo.getIdSolictud())
											.getSingleResult();
									
									if(solicitudAux != null){
										nuevo.setSociedad(solicitudAux.getSociedad().getRazonSocial());
									}
								} catch (Exception e) {
									log.error("no encontro la solicitud #0",e.getMessage());
								}

								if (exito == true && solicitudAux != null) {// (sol.getUsuarioActual()
																			// ==
																			// null
																			// ||
																			// "".equals(sol.getUsuarioActual()))
									if (usuarioCargoAux != null	&& usuarioCargoAux
													.getAdministrador()
													.booleanValue()) {
										if (nuevo.getCodigoEstado() != null) {
											if (nuevo.getCodigoEstado().equals(
													"A")
													|| nuevo.getCodigoEstado()
															.equals("NU")
													|| nuevo.getCodigoEstado()
															.equals("R")) {
												nuevo.setControlador(2);
											} else if (nuevo.getCodigoEstado()
													.equals("DE")) {
												boolean evaluacion = evaluarFechaSolicitud(
														nuevo.getIdSolictud(),
														nuevo.getCodigoEstado());
												if (evaluacion) {
													solicitudAux
															.setEstado("NC");
													if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.V1N
																	.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.V1C
																			.getNombre())) {
														try {
															LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																	.createQuery(
																			"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																	.setParameter(
																			"solAux",
																			solicitudAux
																					.getId()
																					.getNumSolicitud())
																	.getSingleResult();

															if (venta != null) {
																venta.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudVentaPuntual(venta);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(venta);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de venta puntual");
														}
													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.LC1
																	.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.LC2
																			.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.LC3
																			.getNombre())) {
														try {
															LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																	.createQuery(
																			"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																	.setParameter(
																			"solAux",
																			solicitudAux
																					.getId()
																					.getNumSolicitud())
																	.getSingleResult();

															if (credito != null) {
																credito.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudLineaCredito(credito);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(credito);
																entityManager
																		.flush();
															}

														} catch (Exception e) {
															log.debug("no existe datos de linea credito.");
														}

													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.CR1
																	.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CR2
																			.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CRP3
																			.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CRP4
																			.getNombre())) {

														try {
															LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																	.createQuery(
																			"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																	.setParameter(
																			"solAux",
																			solicitudAux
																					.getId()
																					.getNumSolicitud())
																	.getSingleResult();

															if (condicion != null) {
																condicion
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudCondiciones(condicion);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(condicion);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de condiciones.");
														}
													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.OS1
																	.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS2
																			.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS
																			.getNombre())) {
														/***************** Bloqueo y Desbloqueo ***********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService
																	.getSolicitudOtrasId(solicitudAux
																			.getId()
																			.getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(solicitudOtra);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de bloqueo o desbloqueo.");
														}
													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.OS3
																	.getNombre())) {
														/***************** Creacion DM ***********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService
																	.getSolicitudOtrasId(solicitudAux
																			.getId()
																			.getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(solicitudOtra);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de creacion de dm.");
														}

													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.OS4
																	.getNombre())) {
														/********************* Prorroga ********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService
																	.getSolicitudOtrasId(solicitudAux
																			.getId()
																			.getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(solicitudOtra);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de prorroga.");
														}
													}
												} else {
													nuevo.setControlador(3);
												}
											} else {
												nuevo.setControlador(1);
											}
										}
									} else {
										if (usuarioCargoAux != null
												&& !usuarioCargoAux.getCargo()
														.getCodCargo()
														.equals("015")) {
											if (nuevo.getCodigoEstado() != null) {
												if (nuevo.getCodigoEstado()
														.equals("I")
														|| nuevo.getCodigoEstado()
																.equals("DC")
														|| nuevo.getCodigoEstado()
																.equals("DR")) {
													nuevo.setControlador(1);
												} else if (nuevo
														.getCodigoEstado()
														.equals("E")) {
													if (usuarioLogueado
															.getAlias()
															.equals(nuevo
																	.getUsuarioProceso())) {
														nuevo.setControlador(1);
													} else {
														if (usuarioCargoAux != null
																&& usuarioCargoAux
																		.getCargo()
																		.getNivelCargo() == 10) {
															nuevo.setControlador(1);
														} else {
															nuevo.setControlador(1);
														}
													}
												} else if (nuevo
														.getCodigoEstado()
														.equals("DE")) {
													boolean evaluacion = evaluarFechaSolicitud(
															nuevo.getIdSolictud(),
															nuevo.getCodigoEstado());
													if (evaluacion) {
														solicitudAux
																.setEstado("NC");
														if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.V1N
																		.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.V1C
																				.getNombre())) {
															try {
																LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																		.createQuery(
																				"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				solicitudAux
																						.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (venta != null) {
																	venta.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudVentaPuntual(venta);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(venta);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de venta puntual");
															}
														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.LC1
																		.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.LC2
																				.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.LC3
																				.getNombre())) {
															try {
																LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																		.createQuery(
																				"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				solicitudAux
																						.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (credito != null) {
																	credito.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudLineaCredito(credito);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(credito);
																	entityManager
																			.flush();
																}

															} catch (Exception e) {
																log.debug("no existe datos de linea credito.");
															}

														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.CR1
																		.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CR2
																				.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CRP3
																				.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CRP4
																				.getNombre())) {

															try {
																LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																		.createQuery(
																				"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				solicitudAux
																						.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (condicion != null) {
																	condicion
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudCondiciones(condicion);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(condicion);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de condiciones.");
															}
														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS1
																		.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.OS2
																				.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.OS
																				.getNombre())) {
															/*****************
															 * Bloqueo y
															 * Desbloqueo
															 ***********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(solicitudAux
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de bloqueo o desbloqueo.");
															}
														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS3
																		.getNombre())) {
															/***************** Creacion DM ***********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(solicitudAux
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de creacion de dm.");
															}

														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS4
																		.getNombre())) {
															/********************* Prorroga ********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(solicitudAux
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de prorroga.");
															}
														}
													} else {
														nuevo.setControlador(3);
													}
												} else if (nuevo
														.getCodigoEstado()
														.equals("B")
														|| nuevo.getCodigoEstado()
																.equals("SA")) {
													nuevo.setControlador(1);
												} else if (nuevo
														.getCodigoEstado()
														.equals("N")
														|| nuevo.getCodigoEstado()
																.equals("NC")) {
													nuevo.setControlador(1);
												} else if (nuevo
														.getCodigoEstado()
														.equals("ES")) {
													if (nuevo
															.getUsuarioProceso() != null
															&& nuevo.getUsuarioProceso()
																	.equals(usuarioLogueado
																			.getAlias())) {
														nuevo.setControlador(1);
													} else {
														nuevo.setProceso(perfil2
																.getDesPerfil());
														nuevo.setControlador(1);
													}
												} else {
													nuevo.setProceso(perfil2
															.getDesPerfil());
													nuevo.setControlador(2);
												}
											}
										} else {
											nuevo.setControlador(1);
										}
									}
								} else {
									nuevo.setControlador(2);
								}

								/*
								 * verificar si tiene mas un derivado o
								 * aprbadores
								 */
								if (solicitudAux != null
										&& solicitudAux.getEstadoEvaluacion() != null) {
									Long numero = scoringService
											.getCantidadSolicitudUsuarioDerivacion(solicitudAux
													.getId().getNumSolicitud());
									if (numero != null
											&& numero.longValue() > 1) {
										nuevo.setEstadoEvaluacion(solicitudAux
												.getEstadoEvaluacion());
										nuevo.setHabilitaLista(true);
									} else {
										List<SolicitudUsuarioDerivada> lista = scoringService
												.getSolicitudUsuarioDerivacionForSolicitud(solicitudAux
														.getId()
														.getNumSolicitud());
										if (lista != null && lista.size() > 0) {
											SolicitudUsuarioDerivada obj = lista
													.get(0);
											if (obj != null) {
												nuevo.setUsuarioProceso(obj
														.getUsuario()
														.getAlias());
											}
										}
										nuevo.setHabilitaLista(false);
									}
								}

								if (listaUsuariosPendientes != null
										&& listaUsuariosPendientes.size() > 0) {
									if (!listaUsuariosPendientes
											.contains((nuevo.getEmisor()
													.toUpperCase()).trim())) {
										listaUsuariosPendientes.add((nuevo
												.getEmisor().toUpperCase())
												.trim());
									}
								} else {
									listaUsuariosPendientes.add((nuevo
											.getEmisor().toUpperCase()).trim());
								}

								if (listaTiposSolicitudesPendientes != null
										&& listaTiposSolicitudesPendientes
												.size() > 0) {
									if (!listaTiposSolicitudesPendientes
											.contains((nuevo
													.getDespTipoSolictud()
													.toUpperCase()).trim())) {
										listaTiposSolicitudesPendientes
												.add((nuevo
														.getDespTipoSolictud()
														.toUpperCase()).trim());
									}
								} else {
									listaTiposSolicitudesPendientes.add((nuevo
											.getDespTipoSolictud()
											.toUpperCase()).trim());
								}

								if (listaEstadoPendienteString != null
										&& listaEstadoPendienteString.size() > 0) {
									if (!listaEstadoPendienteString
											.contains((nuevo.getEstado()
													.toUpperCase()).trim())) {
										listaEstadoPendienteString.add((nuevo
												.getEstado().toUpperCase())
												.trim());
									}
								} else {
									listaEstadoPendienteString.add((nuevo
											.getEstado().toUpperCase()).trim());
								}

								if (listaCanalPendienteString != null
										&& listaCanalPendienteString.size() > 0) {
									if (!listaCanalPendienteString
											.contains((nuevo.getCanal()
													.toUpperCase()).trim())) {
										listaCanalPendienteString.add((nuevo
												.getCanal().toUpperCase())
												.trim());
									}
								} else {
									listaCanalPendienteString.add((nuevo
											.getCanal().toUpperCase()).trim());
								}

								if (listaProcesoPendienteString != null
										&& listaProcesoPendienteString.size() > 0) {
									if (!listaProcesoPendienteString
											.contains((nuevo.getProceso()
													.toUpperCase()).trim())) {
										listaProcesoPendienteString.add((nuevo
												.getProceso().toUpperCase())
												.trim());
									}
								} else {
									listaProcesoPendienteString
											.add((nuevo.getProceso()
													.toUpperCase()).trim());
								}

								if (listaSucursalSolicitudesPendientes != null
										&& listaSucursalSolicitudesPendientes
												.size() > 0) {
									if (!listaSucursalSolicitudesPendientes
											.contains((nuevo.getSucursal()
													.toUpperCase()).trim())) {
										listaSucursalSolicitudesPendientes
												.add((nuevo.getSucursal()
														.toUpperCase()).trim());
									}
								} else {
									listaSucursalSolicitudesPendientes
											.add((nuevo.getSucursal()
													.toUpperCase()).trim());
								}

								listaSolicitudesPendientesAnalizarDto
										.add(nuevo);
								nuevo = null;
							}
						}
						if (listaSolicitudesPendientesAnalizarDto != null
								&& listaSolicitudesPendientesAnalizarDto.size() == 0) {
							listaSolicitudesPendientesAnalizarDto = null;
						}
					}

					if (listaUsuariosPendientes != null) {
						Collections.sort(listaUsuariosPendientes);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al sacar los solicitudes analizar #0",
					e.getMessage());
		}
		return;
	}

	@Asynchronous
	@SuppressWarnings("unchecked")
	public void sacarMisSolicitudes() {
		Sucursal suc = null;
		this.setEstadoFilter("");
		this.setProcesoFilter("");
		this.setCanalFilter("");
		this.setSucursalFilter("");
		try {
			LcredPerfiles perfil1 = scoringService.obtenerPerfil("1");
			LcredPerfiles perfil2 = scoringService.obtenerPerfil("6");
			Date fechaBusqueda = null;
			DateFormat formatoFechaBusqueda = new SimpleDateFormat("yyyy-MM-dd");
			try {
				fechaBusqueda = formatoFechaBusqueda.parse("2013-06-01");
				log.debug(fechaBusqueda);
			} catch (Exception e) {
				throw new Exception(e);
			}
			if (usuarioLogueado != null) {

				List<Integer> array = scoringService
						.obtenerPerfilesDelUsuario(usuarioLogueado
								.getIdPersonal());
				sacarEstadosSolicitudes();
				/************ Mis solicitudes **********************/
				if (lcredUsuarioNivelEnc != null) {
					List<LcredSolicitud> listaMisSolicitudes = null;
					if (this.pestanaPendientes == true
							&& this.pestanaMisSolicitudes == true
							&& this.pestanaTodas == true
							&& this.pestanaDerivadas == true) {
						listaMisSolicitudes = (List<LcredSolicitud>) entityManager
								.createQuery(
										"Select s from LcredSolicitud s "
												+ " Where  s.codEmisor=:emisor  "
												+ " and s.id.fecSolicitud >:fecha "
												+ " order by s.id.numSolicitud desc")
								.setParameter("emisor",
										usuarioLogueado.getAlias())
								.setParameter("fecha", fechaBusqueda)
								.setMaxResults(100).getResultList(); // and
																		// s.estado
																		// in
																		// ('I','DE',
																		// 'DC')
					} else {
						listaMisSolicitudes = (List<LcredSolicitud>) entityManager
								.createQuery(
										"Select s from LcredSolicitud s "
												+ " Where  s.codEmisor=:emisor "
												+ " and s.id.fecSolicitud >:fecha "
												+ " order by s.id.numSolicitud desc")
								.setParameter("emisor",
										usuarioLogueado.getAlias())
								.setParameter("fecha", fechaBusqueda)
								.setMaxResults(100).getResultList();
					}

					if (listaMisSolicitudes != null) {
						listaMisSolicitudesDto = new ArrayList<SolicitudDTO>(0);
						listaTiposMisSolicitudesString = new ArrayList<String>(0);
						listaEstadoMisSolicitudesString = new ArrayList<String>(0);
						listaCanalMisSolicitudesString = new ArrayList<String>(0);
						listaSucursalMisSolicitudesString = new ArrayList<String>(0);

						for (LcredSolicitud sol : listaMisSolicitudes) {

							int tipoCuenta = 0;
							boolean autorizacion = false;
							boolean exito = false;

							if (this.pestanaPendientes == true 	&& this.pestanaMisSolicitudes == true && this.pestanaTodas == true 	&& this.pestanaDerivadas == true) {

								if ("Mixto".equals(sol.getCanal())) {
									autorizacion = verificaCanal(TipoCuentasKupferType.MX);
									tipoCuenta = TipoCuentasKupferType.MX
											.ordinal();
								} else if ("Kupfer Express".equals(sol
										.getCanal())) {
									autorizacion = verificaCanal(TipoCuentasKupferType.KX);
									tipoCuenta = TipoCuentasKupferType.KX
											.ordinal();
								} else if ("Grandes Cuentas".equals(sol
										.getCanal())) {
									autorizacion = verificaCanal(TipoCuentasKupferType.GC);
									tipoCuenta = TipoCuentasKupferType.GC
											.ordinal();
								}
								// log.debug("solicitud #0  y sucursal : #1 ",
								// sol.getId().getNumSolicitud(),sol.getCodSucursal());
								exito = true;
							} else {
								// log.debug("Usuario #0 sin perfil se cambia solo sus solicitud ");
								autorizacion = true;
								exito = true;
							}

							if (autorizacion == true && exito == true) {
								SolicitudDTO nuevo = new SolicitudDTO();
								nuevo.setIdSolictud(sol.getId().getNumSolicitud());
								nuevo.setRut(sol.getRutCliente().replace("-",""));
								nuevo.setRazonSocial(sol.getNomCliente());
								nuevo.setRazonSocialString(sol.getNomCliente());
								nuevo.setEmisor(sol.getCodEmisor().toUpperCase().toUpperCase());
								nuevo.setDespTipoSolictud(sol.getSubTiposol());
								nuevo.setFechaSalida(sol.getFecSalida());
								nuevo.setUsuarioProceso(sol.getUsuarioActual());
								nuevo.setTipoSolicitud(sol.getTipTiposol());
								nuevo.setCanal(sol.getCanal());
								nuevo.setCondicionPago(sol.getFormaPago());
								nuevo.setPeakCredito(sol.getPeackCredito().doubleValue());
								if(sol.getSociedad() != null){
									nuevo.setSociedad(sol.getSociedad().getRazonSocial());
								}
								
								if (sol.getId().getFecSolicitud() != null && sol.getHraSolicitud() != null) {

									/* rescatar la fecha */
									SimpleDateFormat sdf = new SimpleDateFormat(
											"dd-MM-yy");
									String fecha = sdf.format(sol.getId()
											.getFecSolicitud());

									/* rescatar la hora */
									SimpleDateFormat sdh = new SimpleDateFormat(
											"HH:mm");
									String hora = sdh.format(sol
											.getHraSolicitud());

									/* fecha y hora */
									// log.debug(" Fecha String:" + fecha);
									// log.debug(" hora String :" + hora);

									DateFormat formatoFecha = new SimpleDateFormat(
											"dd-MM-yy HH:mm");
									try {
										Date d = formatoFecha.parse(fecha + " "
												+ hora);
										nuevo.setFechaString(fecha + " " + hora);
										nuevo.setFechaEmision(d);
										nuevo.setHoraEmision(sol
												.getHraSolicitud());
									} catch (Exception e) {
										throw new Exception(e);
									}
								}

								if (sol.getEstado() != null) {
									LcredEstado estado = obtenerObjetoLcredEstado(sol
											.getEstado());
									if (estado != null) {
										nuevo.setCodigoEstado(estado
												.getCodEstado());
										nuevo.setEstado(estado.getDesEstado());
									}

									if (array != null) {
										for (Integer obj : array) {
											if (obj != null) {
												if (obj.intValue() == 1
														|| obj.intValue() == 3
														|| obj.intValue() == 4) {
													nuevo.setProceso(perfil1
															.getDesPerfil());
													break;
												} else {
													nuevo.setProceso(perfil2
															.getDesPerfil());
													break;
												}
											}
										}
									}
								}

								if (sol.getSucursalEmisor() != null) {
									// log.debug(" codigo de sucursal  :" +
									// sol.getSucursalEmisor());
									suc = obtenerObjetoSucursal(sol
											.getSucursalEmisor());
									if (suc != null) {
										nuevo.setSucursal(suc.getDescripcion());
									} else {
										nuevo.setSucursal(sol.getCodSucursal());
									}

								}
								if (sol.getMontoAsegurado() != null) {
									nuevo.setMontoAsegurado(sol
											.getMontoAsegurado().doubleValue());
								} else {
									nuevo.setMontoAsegurado((double) 0);
								}

								if (sol.getMonto() != null) {
									nuevo.setMonto(sol.getMonto().doubleValue());
								} else {
									nuevo.setMonto((double) 0);
								}

								if (sol.getRiesgoKupfer() != null) {
									nuevo.setMontoRiegoKupfer(sol
											.getRiesgoKupfer().doubleValue());
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

								if (sol.getId().getNumSolicitud().longValue() == 26318) {
									// log.debug("solicitud #0",
									// sol.getId().getNumSolicitud());
								}

								if (this.pestanaPendientes == true
										&& this.pestanaMisSolicitudes == true
										&& this.pestanaTodas == true
										&& this.pestanaDerivadas == true) {
									if (exito) {// (sol.getUsuarioActual() ==
												// null ||
												// "".equals(sol.getUsuarioActual()))
										if (usuarioCargoAux != null
												&& usuarioCargoAux
														.getAdministrador()
														.booleanValue()) {
											if (nuevo.getCodigoEstado() != null) {
												if (nuevo.getCodigoEstado()
														.equals("A")
														|| nuevo.getCodigoEstado()
																.equals("NU")
														|| nuevo.getCodigoEstado()
																.equals("R")) {
													nuevo.setControlador(2);
												} else if (nuevo
														.getCodigoEstado()
														.equals("DE")) {
													boolean evaluacion = evaluarFechaSolicitud(
															nuevo.getIdSolictud(),
															nuevo.getCodigoEstado());
													if (evaluacion) {
														sol.setEstado("NC");
														if ((sol.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.V1N
																		.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.V1C
																				.getNombre())) {
															try {
																LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																		.createQuery(
																				"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				sol.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (venta != null) {
																	venta.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudVentaPuntual(venta);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(venta);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de venta puntual");
															}
														} else if ((sol
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.LC1
																		.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.LC2
																				.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.LC3
																				.getNombre())) {
															try {
																LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																		.createQuery(
																				"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				sol.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (credito != null) {
																	credito.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudLineaCredito(credito);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(credito);
																	entityManager
																			.flush();
																}

															} catch (Exception e) {
																log.debug("no existe datos de linea credito.");
															}

														} else if ((sol
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.CR1
																		.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CR2
																				.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CRP3
																				.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CRP4
																				.getNombre())) {

															try {
																LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																		.createQuery(
																				"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				sol.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (condicion != null) {
																	condicion
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudCondiciones(condicion);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(condicion);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de condiciones.");
															}
														} else if ((sol
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS1
																		.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.OS2
																				.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.OS
																				.getNombre())) {
															/*****************
															 * Bloqueo y
															 * Desbloqueo
															 ***********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(sol
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de bloqueo o desbloqueo.");
															}
														} else if ((solicitud
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS3
																		.getNombre())) {
															/***************** Creacion DM ***********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(sol
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de creacion de dm.");
															}

														} else if ((solicitud
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS4
																		.getNombre())) {
															/********************* Prorroga ********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(sol
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de prorroga.");
															}
														}
													} else {
														nuevo.setControlador(3);
													}
												} else {
													nuevo.setControlador(1);
												}
											}
										} else {
											if (usuarioCargoAux != null
													&& !usuarioCargoAux
															.getCargo()
															.getCodCargo()
															.equals("015")) {
												if (nuevo.getCodigoEstado() != null) {
													if (nuevo.getCodigoEstado()
															.equals("I")
															|| nuevo.getCodigoEstado()
																	.equals("DC")
															|| nuevo.getCodigoEstado()
																	.equals("DR")) {
														nuevo.setControlador(1);
													} else if (nuevo
															.getCodigoEstado()
															.equals("E")) {
														if (lcredUsuarioNivelEnc
																.getId()
																.getIdUsuario()
																.equals(sol
																		.getUsuarioActual())) {
															nuevo.setControlador(1);
														} else {
															if (usuarioCargoAux != null
																	&& usuarioCargoAux
																			.getCargo()
																			.getNivelCargo() == 10) {
																nuevo.setControlador(1);
															} else {
																nuevo.setControlador(2);
															}
														}
													} else if (nuevo
															.getCodigoEstado()
															.equals("DE")) {
														boolean evaluacion = evaluarFechaSolicitud(
																nuevo.getIdSolictud(),
																nuevo.getCodigoEstado());
														if (evaluacion) {
															sol.setEstado("NC");
															if ((sol.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.V1N
																			.getNombre())
																	|| (sol.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.V1C
																					.getNombre())) {
																try {
																	LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																			.createQuery(
																					"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																			.setParameter(
																					"solAux",
																					sol.getId()
																							.getNumSolicitud())
																			.getSingleResult();

																	if (venta != null) {
																		venta.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(sol);
																		scoringService
																				.mergerSolicitudVentaPuntual(venta);
																		LcredEstado estado = obtenerObjetoLcredEstado(sol
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																		}
																		entityManager
																				.refresh(sol);
																		entityManager
																				.refresh(venta);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de venta puntual");
																}
															} else if ((sol
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.LC1
																			.getNombre())
																	|| (sol.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.LC2
																					.getNombre())
																	|| (sol.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.LC3
																					.getNombre())) {
																try {
																	LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																			.createQuery(
																					"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																			.setParameter(
																					"solAux",
																					sol.getId()
																							.getNumSolicitud())
																			.getSingleResult();

																	if (credito != null) {
																		credito.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(sol);
																		scoringService
																				.mergerSolicitudLineaCredito(credito);
																		LcredEstado estado = obtenerObjetoLcredEstado(sol
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																		}
																		entityManager
																				.refresh(sol);
																		entityManager
																				.refresh(credito);
																		entityManager
																				.flush();
																	}

																} catch (Exception e) {
																	log.debug("no existe datos de linea credito.");
																}

															} else if ((sol
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CR1
																			.getNombre())
																	|| (sol.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.CR2
																					.getNombre())
																	|| (sol.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.CRP3
																					.getNombre())
																	|| (sol.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.CRP4
																					.getNombre())) {

																try {
																	LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																			.createQuery(
																					"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																			.setParameter(
																					"solAux",
																					sol.getId()
																							.getNumSolicitud())
																			.getSingleResult();

																	if (condicion != null) {
																		condicion
																				.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(sol);
																		scoringService
																				.mergerSolicitudCondiciones(condicion);
																		LcredEstado estado = obtenerObjetoLcredEstado(sol
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																		}
																		entityManager
																				.refresh(sol);
																		entityManager
																				.refresh(condicion);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de condiciones.");
																}
															} else if ((sol
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS1
																			.getNombre())
																	|| (sol.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.OS2
																					.getNombre())
																	|| (sol.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.OS
																					.getNombre())) {
																/*****************
																 * Bloqueo y
																 * Desbloqueo
																 ***********************/
																try {
																	LcredSolicitudOtras solicitudOtra = scoringService
																			.getSolicitudOtrasId(sol
																					.getId()
																					.getNumSolicitud());
																	if (solicitudOtra != null) {
																		solicitudOtra
																				.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(sol);
																		scoringService
																				.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																		LcredEstado estado = obtenerObjetoLcredEstado(sol
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																		}
																		entityManager
																				.refresh(sol);
																		entityManager
																				.refresh(solicitudOtra);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de bloqueo o desbloqueo.");
																}
															} else if ((solicitud
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS3
																			.getNombre())) {
																/***************** Creacion DM ***********************/
																try {
																	LcredSolicitudOtras solicitudOtra = scoringService
																			.getSolicitudOtrasId(sol
																					.getId()
																					.getNumSolicitud());
																	if (solicitudOtra != null) {
																		solicitudOtra
																				.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(sol);
																		scoringService
																				.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																		LcredEstado estado = obtenerObjetoLcredEstado(sol
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																		}
																		entityManager
																				.refresh(sol);
																		entityManager
																				.refresh(solicitudOtra);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de creacion de dm.");
																}

															} else if ((solicitud
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS4
																			.getNombre())) {
																/********************* Prorroga ********************/
																try {
																	LcredSolicitudOtras solicitudOtra = scoringService
																			.getSolicitudOtrasId(sol
																					.getId()
																					.getNumSolicitud());
																	if (solicitudOtra != null) {
																		solicitudOtra
																				.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(sol);
																		scoringService
																				.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																		LcredEstado estado = obtenerObjetoLcredEstado(sol
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																		}
																		entityManager
																				.refresh(sol);
																		entityManager
																				.refresh(solicitudOtra);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de prorroga.");
																}
															}
														} else {
															nuevo.setControlador(3);
														}
													} else if (nuevo
															.getCodigoEstado()
															.equals("B")
															|| nuevo.getCodigoEstado()
																	.equals("SA")) {
														nuevo.setControlador(2);
													} else if (nuevo
															.getCodigoEstado()
															.equals("N")
															|| nuevo.getCodigoEstado()
																	.equals("NC")) {
														nuevo.setControlador(2);
													} else {
														nuevo.setControlador(2);
													}
												}
											} else {
												nuevo.setControlador(2);
											}
										}
									} else {
										nuevo.setControlador(2);
									}
								} else {
									// log.debug("usuario logeado #0",
									// usuarioLogueado.getAlias());
									// log.debug("emisor de solicitud #0",
									// sol.getCodEmisor());
									// log.debug("estado de la solcicitud #0",
									// sol.getEstado());
									if (usuarioLogueado.getAlias().equals(
											sol.getCodEmisor())) {
										if (sol.getEstado().equals("DE")) {
											nuevo.setControlador(3);
										} else {
											nuevo.setControlador(2);
										}
									} else {
										nuevo.setControlador(2);
									}
								}

								/*
								 * verificar si tiene mas un derivado o
								 * aprbadores
								 */
								if (sol.getEstadoEvaluacion() != null) {
									Long numero = scoringService
											.getCantidadSolicitudUsuarioDerivacion(sol
													.getId().getNumSolicitud());
									if (numero != null
											&& numero.longValue() > 1) {
										nuevo.setEstadoEvaluacion(sol
												.getEstadoEvaluacion());
										nuevo.setHabilitaLista(true);
									} else {
										List<SolicitudUsuarioDerivada> lista = scoringService
												.getSolicitudUsuarioDerivacionForSolicitud(sol
														.getId()
														.getNumSolicitud());
										if (lista != null && lista.size() > 0) {
											SolicitudUsuarioDerivada obj = lista
													.get(0);
											if (obj != null) {
												nuevo.setUsuarioProceso(obj
														.getUsuario()
														.getAlias());
											}
										}
										nuevo.setHabilitaLista(false);
									}
								}

								if (nuevo.getDespTipoSolictud() != null) {
									if (listaTiposMisSolicitudesString != null
											&& listaTiposMisSolicitudesString
													.size() > 0) {
										if (!listaTiposMisSolicitudesString
												.contains((nuevo
														.getDespTipoSolictud()
														.toUpperCase()).trim())) {
											listaTiposMisSolicitudesString
													.add((nuevo
															.getDespTipoSolictud()
															.toUpperCase())
															.trim());
										}
									} else {
										listaTiposMisSolicitudesString
												.add((nuevo
														.getDespTipoSolictud()
														.toUpperCase()).trim());
									}
								}

								if (listaEstadoMisSolicitudesString != null
										&& listaEstadoMisSolicitudesString
												.size() > 0) {
									if (!listaEstadoMisSolicitudesString
											.contains((nuevo.getEstado()
													.toUpperCase()).trim())) {
										listaEstadoMisSolicitudesString
												.add((nuevo.getEstado()
														.toUpperCase()).trim());
									}
								} else {
									listaEstadoMisSolicitudesString.add((nuevo
											.getEstado().toUpperCase()).trim());
								}

								if (listaCanalMisSolicitudesString != null
										&& listaCanalMisSolicitudesString
												.size() > 0) {
									if (!listaCanalMisSolicitudesString
											.contains((nuevo.getCanal()
													.toUpperCase()).trim())) {
										listaCanalMisSolicitudesString
												.add((nuevo.getCanal()
														.toUpperCase()).trim());
									}
								} else {
									listaCanalMisSolicitudesString.add((nuevo
											.getCanal().toUpperCase()).trim());
								}

								if (listaSucursalMisSolicitudesString != null
										&& listaSucursalMisSolicitudesString
												.size() > 0) {
									if (!listaSucursalMisSolicitudesString
											.contains((nuevo.getSucursal()
													.toUpperCase()).trim())) {
										listaSucursalMisSolicitudesString
												.add((nuevo.getSucursal()
														.toUpperCase()).trim());
									}
								} else {
									listaSucursalMisSolicitudesString
											.add((nuevo.getSucursal()
													.toUpperCase()).trim());
								}

								listaMisSolicitudesDto.add(nuevo);
								nuevo = null;
							}
						}
						if (listaMisSolicitudesDto != null
								&& listaMisSolicitudesDto.size() == 0) {
							listaMisSolicitudesDto = null;
						}

						if (listaSucursalMisSolicitudesString != null) {
							Collections.sort(listaSucursalMisSolicitudesString);
						}

					} else {
						listaMisSolicitudesDto = new ArrayList<SolicitudDTO>(0);
					}
				}
			} else {
				listaMisSolicitudesDto = new ArrayList<SolicitudDTO>(0);
			}
		} catch (Exception e) {
			log.error("Error, al sacar todas mis solicitudes #0",
					e.getMessage());
		}
	}

	/* metodos de para sacar las lista solicitudes */

	@Asynchronous
	@SuppressWarnings("unchecked")
	public void sacarListasTodasSolicitudes(String evalucion) {
		Sucursal suc = null;
		this.setEstadoFilter("");
		this.setProcesoFilter("");
		this.setCanalFilter("");
		this.setSucursalFilter("");

		try {
			LcredPerfiles perfil1 = scoringService.obtenerPerfil("1");
			LcredPerfiles perfil2 = scoringService.obtenerPerfil("6");

			Date fechaBusqueda = null;
			DateFormat formatoFechaBusqueda = new SimpleDateFormat("yyyy-MM-dd");
			try {
				fechaBusqueda = formatoFechaBusqueda.parse("2013-06-01");
				log.debug(fechaBusqueda);
			} catch (Exception e) {
				throw new Exception(e);
			}

			if (usuarioLogueado != null) {
				List<Integer> array = scoringService
						.obtenerPerfilesDelUsuario(usuarioLogueado
								.getIdPersonal());
				sacarEstadosSolicitudes();
				/************ Todas las solicitudes **********************/
				if (usuarioLogueado != null) {
					List<Object[]> listaTodasSolicitudesNueva = null;
					if ("INICIAL".equals(evalucion)	|| "ACTUALIZAR".equals(evalucion)) {
						listaTodasSolicitudesNueva = (List<Object[]>) entityManager
								.createNativeQuery(
										" SELECT sol.[num_solicitud] as numero"
												+ // 0
												" ,cast(sol.[id_canal]  as varchar) as codigo_canal"
												+ // 1
												" ,sol.[fec_solicitud]  as fecha_sol"
												+ // 2
												" ,sol.[hra_solicitud] as hora_sol"
												+ // 3
												" ,cast(sol.[cod_emisor]  as varchar) as codigo_emisor"
												+ // 4
												" ,cast(sol.[sucursal_emisor]  as varchar) as suc_emisor"
												+ // 5
												" ,cast(sol.[rut_cliente] as varchar)  as rut_cliente"
												+ // 6
												" ,cast(sol.[nom_cliente] as varchar)  as nombre "
												+ // 7
												" ,cast(sol.[cod_sucursal]  as varchar) as cod_sucursal"
												+ // 8
												" ,cast(sol.[des_tiposol]  as varchar) as des_tiposol"
												+ // 9
												" ,cast(sol.[tip_tiposol]  as varchar) as tip_tiposol"
												+ // 10
												" ,sol.[fec_salida] as fecha_salida"
												+ // 11
												" ,cast(sol.[usuario_actual]  as varchar) as usuario_actual"
												+ // 12
												" ,sol.[peack_credito] as peak_credito"
												+ // 13
												" ,sol.[monto_asegurado] as montoAsegudado"
												+ // 14
												" ,sol.[riesgo_kupfer] as riesgo_kupfer"
												+ // 15
												" ,cast(sol.[forma_pago]  as varchar) as forma_pago"
												+ // 16
												" ,sol.[fec_salida] as fecha_salida"
												+ // 17
												" ,cast(sol.[estado]  as varchar) as estado"
												+ // 18
												" ,cast(sol.[canal]  as varchar) as canal "
												+ // 19
												" ,sol.[monto] as monto "
												+ // 20
												" ,sol.[evaluar] as evaluar "
												+ // 21
												" ,sol.[analizar] as analizar "
												+ // 22
												" ,cast(sol.[usuario_varios]  as varchar) as usuario_varios"
												+ // 23
												" ,cast(sol.[sub_tiposol]  as varchar) as sub_tiposol"
												+ // 24
												" ,sol.[cod_usuario_devuelve] as cod_usuario_devuelve"
												+ // 25
												" ,cast(sol.[usuario_actual]  as varchar) as usuario_actual_aux "
												+ // 26
												" ,sol.[peack_credito] as peak_credito "
												+ // 27
												" FROM [SCORING_PRODUCION].[dbo].[vw_lcred_solicitudes] sol "
												+ " where sol.num_solicitud > 29347 and sol.estado not in ('') and sol.sucursal_emisor + convert(varchar,sol.id_canal) in "
												+ " ( select zsnc.codigo + CONVERT(varchar,cuc.tipo_cuenta) "
												+ "   from [SCORING_PRODUCION].[dbo].[zona_sucursal_negocio_concepto] zsnc  "
												+ "    left join [SCORING_PRODUCION].[dbo].[perfil_funcion_canal] pfc on zsnc.perfil_funcion_canal = pfc.id_canal_usuario_cargo "
												+ "    left join [SCORING_PRODUCION].[dbo].[canal_usuario_cargo] cuc on pfc.canal_usuario_cargo = cuc.id_canal_usuario_cargo "
												+ "    left join [SCORING_PRODUCION].[dbo].[usuario_cargo] uc on cuc.usuario_cargo = uc.id_usuario_cargo"
												+ "   where   uc.usuario_username=:id_personal "
												+ "   and pfc.funcion in (1,3,4,5)"
												+ "  ) " + // 1,3,4
												" order by sol.num_solicitud desc; ")
								.setParameter("id_personal",
										usuarioLogueado.getIdPersonal())
								.setMaxResults(500).getResultList();
					} else if ("DESDEPESTANA".equals(evalucion)) {
						String query = " SELECT sol.[num_solicitud] as numero"
								+ // 0
								" ,cast(sol.[id_canal]  as varchar) as codigo_canal"
								+ // 1
								" ,sol.[fec_solicitud]  as fecha_sol"
								+ // 2
								" ,sol.[hra_solicitud] as hora_sol"
								+ // 3
								" ,cast(sol.[cod_emisor]  as varchar) as codigo_emisor"
								+ // 4
								" ,cast(sol.[sucursal_emisor]  as varchar) as suc_emisor"
								+ // 5
								" ,cast(sol.[rut_cliente] as varchar)  as rut_cliente"
								+ // 6
								" ,cast(sol.[nom_cliente] as varchar)  as nombre "
								+ // 7
								" ,cast(sol.[cod_sucursal]  as varchar) as cod_sucursal"
								+ // 8
								" ,cast(sol.[des_tiposol]  as varchar) as des_tiposol"
								+ // 9
								" ,cast(sol.[tip_tiposol]  as varchar) as tip_tiposol"
								+ // 10
								" ,sol.[fec_salida] as fecha_salida"
								+ // 11
								" ,cast(sol.[usuario_actual]  as varchar) as usuario_actual"
								+ // 12
								" ,sol.[peack_credito] as peak_credito"
								+ // 13
								" ,sol.[monto_asegurado] as montoAsegudado"
								+ // 14
								" ,sol.[riesgo_kupfer] as riesgo_kupfer"
								+ // 15
								" ,cast(sol.[forma_pago]  as varchar) as forma_pago"
								+ // 16
								" ,sol.[fec_salida] as fecha_salida"
								+ // 17
								" ,cast(sol.[estado]  as varchar) as estado"
								+ // 18
								" ,cast(sol.[canal]  as varchar) as canal "
								+ // 19
								" ,sol.[monto] as monto "
								+ // 20
								" ,sol.[evaluar] as evaluar "
								+ // 21
								" ,sol.[analizar] as analizar "
								+ // 22
								" ,cast(sol.[usuario_varios]  as varchar) as usuario_varios"
								+ // 23
								" ,cast(sol.[sub_tiposol]  as varchar) as sub_tiposol"
								+ // 24
								" ,sol.[cod_usuario_devuelve] as cod_usuario_devuelve"
								+ // 25
								" ,cast(sol.[usuario_actual]  as varchar) as usuario_actual_aux "
								+ // 26
								" ,sol.[peack_credito] as peak_credito " 
								+ // 27
								" FROM [SCORING_PRODUCION].[dbo].[vw_lcred_solicitudes] sol where ";

						if (this.idSolicitudPestTodas != null) {
							query += " sol.num_solicitud=:idSolicitudAux ";
						}

						if (this.idSolicitudPestTodas != null && this.rutAuxPestTodas != null) {
							query += " and ( sol.rut_cliente=:rutAux or sol.rut_cliente=:rutAuxSinGuion )";
						} else if (this.idSolicitudPestTodas == null && this.rutAuxPestTodas != null) {
							query += "  sol.rut_cliente=:rutAux or sol.rut_cliente=:rutAuxSinGuion ";
						}

						/* validar la consulta de fechas inicial y fecha final */
						if ((this.idSolicitudPestTodas != null || this.rutAuxPestTodas != null)	&& this.fechaInicalPestTodas != null && this.fechaFinalPestTodas == null) {

							query += " and convert(datetime,sol.fec_solicitud, 111) >= convert(datetime,:fechaInicial, 111) ";

						} else if ((this.idSolicitudPestTodas == null && this.rutAuxPestTodas == null)	&& this.fechaInicalPestTodas != null && this.fechaFinalPestTodas != null) {

							query += " convert(datetime,sol.fec_solicitud, 111) BETWEEN  convert(datetime, :fechaInicio, 111) and convert(datetime, :fechaFinal, 111) ";

						} else if ((this.idSolicitudPestTodas == null && this.rutAuxPestTodas == null) && this.fechaInicalPestTodas != null	&& this.fechaFinalPestTodas == null) {

							query += "  convert(datetime,sol.fec_solicitud, 111) >= convert(datetime,:fechaInicial, 111) ";

						} else if ((this.idSolicitudPestTodas == null && this.rutAuxPestTodas != null)	&& this.fechaInicalPestTodas != null && this.fechaFinalPestTodas == null) {

							query += " and convert(datetime,sol.fec_solicitud, 111) >= convert(datetime,:fechaInicial, 111) ";
						}

						/* validar la consulta de estado */
						if ((this.idSolicitudPestTodas != null || this.rutAuxPestTodas != null || this.fechaInicalPestTodas != null) && this.estadoPestTodas != null) {
							if (!"TO".equals(this.estadoPestTodas.getCodEstado())) {
								query += " and sol.estado=:codEstado ";
							} else {
								query += " and sol.estado not in ('')";
							}

						} else if ((this.idSolicitudPestTodas == null && this.rutAuxPestTodas == null && this.fechaInicalPestTodas == null)	&& this.estadoPestTodas != null) {
							if (!"TO".equals(this.estadoPestTodas.getCodEstado())) {
								query += "  sol.estado=:codEstado ";
							} else {
								query += "  sol.estado not in ('')";
							}

						}

						/* validar la consulta de sucursal */
						if ((this.idSolicitudPestTodas != null || this.rutAuxPestTodas != null	|| this.fechaInicalPestTodas != null || this.estadoPestTodas != null)	&& this.sucursalPestTodas != null) {
							if (!"TDS1".equals(this.sucursalPestTodas.getCodigo())) {
								query += " and sol.sucursal_emisor=:codSucursal ";
							} else {
								query += " and sol.sucursal_emisor not in ('') ";
							}
						} else if ((this.idSolicitudPestTodas == null && this.rutAuxPestTodas == null && this.fechaInicalPestTodas == null && this.estadoPestTodas == null)	&& this.sucursalPestTodas != null) {

							if (!"TDS1".equals(this.sucursalPestTodas.getCodigo())) {
								query += "  sol.cod_sucursal=:codSucursal ";
							} else {
								query += " sol.cod_sucursal not in ('') ";
							}
						}

						/* validar la consulta de sociedad */
						if ((this.idSolicitudPestTodas != null || this.rutAuxPestTodas != null	|| this.fechaInicalPestTodas != null || this.estadoPestTodas != null && this.sucursalPestTodas != null) && this.sociedadPestTodas != null) {
								query += " and sol.sociedad=:codSociedad ";
						} else if ((this.idSolicitudPestTodas == null && this.rutAuxPestTodas == null && this.fechaInicalPestTodas == null && this.estadoPestTodas == null	&& this.sucursalPestTodas == null) && this.sociedadPestTodas != null) {
								query += "  sol.sociedad=:codSociedad  ";
						}						
						
						
						
						query += " and sol.sucursal_emisor + convert(varchar,sol.id_canal) in "
								+ " ( select zsnc.codigo + CONVERT(varchar,cuc.tipo_cuenta) "
								+ "   from [SCORING_PRODUCION].[dbo].[zona_sucursal_negocio_concepto] zsnc  "
								+ "    left join [SCORING_PRODUCION].[dbo].[perfil_funcion_canal] pfc on zsnc.perfil_funcion_canal = pfc.id_canal_usuario_cargo "
								+ "    left join [SCORING_PRODUCION].[dbo].[canal_usuario_cargo] cuc on pfc.canal_usuario_cargo = cuc.id_canal_usuario_cargo "
								+ "    left join [SCORING_PRODUCION].[dbo].[usuario_cargo] uc on cuc.usuario_cargo = uc.id_usuario_cargo"
								+ "   where   uc.usuario_username=:id_personal "
								+ "   and pfc.funcion in (1,3,4,5)" + "  ) " + // 1,3,4
								" order by sol.num_solicitud desc; ";

						Query consulta = entityManager.createNativeQuery(query);

						if (this.idSolicitudPestTodas != null) {
							consulta.setParameter("idSolicitudAux",
									this.idSolicitudPestTodas);
						}
						if (this.rutAuxPestTodas != null) {
							consulta.setParameter("rutAux",
									this.rutAuxPestTodas);
							consulta.setParameter("rutAuxSinGuion",
									this.rutAuxSinGuion);
						}

						/* validar la consulta de fechas inicial y fecha final */
						if (this.fechaInicalPestTodas != null
								&& this.fechaFinalPestTodas == null) {
							Calendar calDesde = Calendar.getInstance();
							calDesde.setTime(this.fechaInicalPestTodas);
							calDesde.set(Calendar.HOUR_OF_DAY, 0);
							calDesde.set(Calendar.MINUTE, 0);
							calDesde.set(Calendar.SECOND, 0);
							this.fechaInicalPestTodas = calDesde.getTime();
							consulta.setParameter("fechaInicial",
									this.fechaInicalPestTodas);

						} else if (this.fechaInicalPestTodas != null
								&& this.fechaFinalPestTodas != null) {
							Calendar calDesde = Calendar.getInstance();
							calDesde.setTime(this.fechaInicalPestTodas);
							calDesde.set(Calendar.HOUR_OF_DAY, 0);
							calDesde.set(Calendar.MINUTE, 0);
							calDesde.set(Calendar.SECOND, 0);
							this.fechaInicalPestTodas = calDesde.getTime();

							Calendar calHasta = Calendar.getInstance();
							calHasta.setTime(this.fechaFinalPestTodas);
							calHasta.set(Calendar.HOUR_OF_DAY, 23);
							calHasta.set(Calendar.MINUTE, 59);
							calHasta.set(Calendar.SECOND, 59);
							this.fechaFinalPestTodas = calHasta.getTime();

							consulta.setParameter("fechaInicio",
									this.fechaInicalPestTodas);
							consulta.setParameter("fechaFinal",
									this.fechaFinalPestTodas);
						}

						/* validar la consulta de estado */
						if (this.estadoPestTodas != null) {
							if (!"TO".equals(this.estadoPestTodas
									.getCodEstado())) {
								consulta.setParameter("codEstado",
										this.estadoPestTodas.getCodEstado());
							}
						}

						/* validar la consulta de sucursal */
						if (this.sucursalPestTodas != null) {
							if (!"TDS1".equals(this.sucursalPestTodas
									.getCodigo())) {
								consulta.setParameter("codSucursal",
										this.sucursalPestTodas.getCodigo());
							}
						}
						/* validar la consulta de sucursal */
						if (this.sociedadPestTodas != null) {
								consulta.setParameter("codSociedad",this.sociedadPestTodas.getIdSociedad());
						}
						try {
							listaTodasSolicitudesNueva = (List<Object[]>) consulta.setParameter("id_personal",usuarioLogueado.getIdPersonal()).setMaxResults(2000).getResultList();

						} catch (Exception e) {
							log.error(
									"Error al armar la query por favor revisar. #0",
									e.getMessage());
							e.printStackTrace();
							FacesMessages
									.instance()
									.add(Severity.INFO,
											"La consulta genero demasiada información, favor aplicar otro fitro mas.");
							return;
						}

					}

					if (listaTodasSolicitudesNueva != null) {
						// log.debug("cantidad de registro #0",
						// listaTodasSolicitudesNueva.size());
					}

					if (listaTodasSolicitudesNueva != null) {
						listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>(0);
						listaUsuarios = new ArrayList<String>(0);
						listaTiposSolicitudes = new ArrayList<String>(0);
						listaEstadoString = new ArrayList<String>(0);
						listaCanalString = new ArrayList<String>(0);
						listaProcesoString = new ArrayList<String>(0);
						listaSucursalSolicitudes = new ArrayList<String>(0);

						for (Object[] sol : listaTodasSolicitudesNueva) {
							int tipoCuenta = 0;
							boolean autorizacion = false;
							if (sol[19] != null) {
								if ("Mixto".equals((sol[19].toString()))) {
									autorizacion = verificaCanal(TipoCuentasKupferType.MX);
									tipoCuenta = TipoCuentasKupferType.MX.ordinal();
								} else if ("Kupfer Express".equals((sol[19].toString()))) {
									autorizacion = verificaCanal(TipoCuentasKupferType.KX);
									tipoCuenta = TipoCuentasKupferType.KX.ordinal();
								} else if ("Grandes Cuentas".equals((sol[19].toString()))) {
									autorizacion = verificaCanal(TipoCuentasKupferType.GC);
									tipoCuenta = TipoCuentasKupferType.GC.ordinal();
								}
							}

							if (autorizacion == true) {
								SolicitudDTO nuevo = new SolicitudDTO();
								nuevo.setIdSolictud(Long.parseLong(sol[0].toString()));
								if (nuevo.getIdSolictud() == 36812) {
									log.debug("solicitud #0",nuevo.getIdSolictud());
								}

								if (sol[6] != null) {
									nuevo.setRut(sol[6].toString().replace("-",""));
								}
								if (sol[7] != null) {
									nuevo.setRazonSocial(sol[7].toString());
									nuevo.setRazonSocialString(sol[7].toString());
								}
								if (sol[4] != null) {
									nuevo.setEmisor(sol[4].toString().toUpperCase().toUpperCase());
								}
								if (sol[24] != null) {
									nuevo.setDespTipoSolictud(sol[24].toString());
								}
								if (sol[11] != null) {
									nuevo.setFechaSalida((Date) sol[11]);
								}

								if (sol[10] != null) {
									nuevo.setTipoSolicitud(sol[10].toString());
								}

								if (sol[19] != null) {
									nuevo.setCanal(sol[19].toString());
								}
								if (sol[2] != null && sol[3] != null) {

									/* rescatar la fecha */
									SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
									String fecha = sdf.format((Date) sol[2]);

									/* rescatar la hora */
									SimpleDateFormat sdh = new SimpleDateFormat("HH:mm");
									String hora = sdh.format((Date) sol[3]);

									/* fecha y hora */
									// log.debug(" Fecha String:" + fecha);
									// log.debug(" hora String :" + hora);

									DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yy HH:mm");
									try {
										Date d = formatoFecha.parse(fecha + " " + hora);
										nuevo.setFechaString(fecha + " " + hora);
										nuevo.setFechaEmision(d);
										nuevo.setHoraEmision((Date) sol[3]);
									} catch (Exception e) {
										throw new Exception(e);
									}
								}

								if (sol[18] != null) {
									LcredEstado estado = obtenerObjetoLcredEstado(sol[18].toString());
									if (estado != null) {
										nuevo.setCodigoEstado(estado.getCodEstado());
										nuevo.setEstado(estado.getDesEstado());
									}

									if (array != null) {
										for (Integer obj : array) {
											if (obj != null) {
												if (obj.intValue() == 1	|| obj.intValue() == 3 || obj.intValue() == 4) {
													nuevo.setProceso(perfil1.getDesPerfil());
													break;
												} else {
													nuevo.setProceso(perfil2.getDesPerfil());
													break;
												}
											}
										}
									}
								}

								if (sol[5] != null) {
									// log.debug(" codigo de sucursal  :" +
									// sol.getSucursalEmisor());
									suc = obtenerObjetoSucursal(sol[5].toString());
									if (suc != null) {
										nuevo.setSucursal(suc.getDescripcion());
									} else {
										nuevo.setSucursal(sol[5].toString());
									}

								}
								if (sol[14] != null) {
									nuevo.setMontoAsegurado(Double.parseDouble(sol[14].toString()));
								} else {
									nuevo.setMontoAsegurado((double) 0);
								}

								if (sol[20] != null) {
									nuevo.setMonto(Double.parseDouble(sol[20].toString()));
								} else {
									nuevo.setMonto((double) 0);
								}

								if (sol[15] != null) {
									nuevo.setMontoRiegoKupfer(Double.parseDouble(sol[15].toString()));
								} else {
									nuevo.setMontoRiegoKupfer((double) 0);
								}

								if (sol[21] != null) {
									nuevo.setEvaluar(Boolean.valueOf(sol[21].toString()));
								} else {
									nuevo.setEvaluar(false);
								}
								if (sol[22] != null) {
									if (sol[22].toString().equals("1")) {
										nuevo.setAnalizar(true);
									} else {
										nuevo.setAnalizar(false);
									}

								} else {
									nuevo.setAnalizar(false);
								}
								if (sol[16] != null) {
									nuevo.setCondicionPago(sol[16].toString());
								} else {
									nuevo.setCondicionPago("");
								}

								if (sol[25] != null) {
									nuevo.setUsuarioTomada(sol[25].toString());
								} else {
									nuevo.setUsuarioTomada("");
								}
								if (sol[12] != null) {
									nuevo.setUsuarioProceso(sol[12].toString());
								} else {
									nuevo.setUsuarioProceso("");
								}
								if (sol[27] != null) {
									nuevo.setPeakCredito(Double.parseDouble(sol[27].toString()));
								} else {
									nuevo.setPeakCredito((double) 0);
								}
								LcredSolicitud solicitudAux = null;
								try {
									solicitudAux = (LcredSolicitud) entityManager
											.createQuery("select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud").setParameter("numSolicitud",nuevo.getIdSolictud())
											.getSingleResult();
									if(solicitudAux != null && solicitudAux.getSociedad() != null){
										nuevo.setSociedad(solicitudAux.getSociedad().getRazonSocial());
									}
								} catch (Exception e) {
									log.error("no encontro la solicitud #0", e.getMessage());
								}

								boolean exito = false;
								// log.debug("solicitud #0  y sucursal : #1 ",
								// sol.getId().getNumSolicitud(),sol.getCodSucursal());
								boolean aprobar = false;
								boolean ejecutivo = false;
								boolean financiero = false;
								boolean comenta = false;
								if (sol[5] != null) {
									for (Integer obj : array) {
										if (obj != null && obj.intValue() != 0) {
											exito = scoringService.obtenerHabilitacionUsuario(usuarioLogueado.getIdPersonal(),sol[5].toString(),tipoCuenta, obj.intValue());
											// log.debug(" sucursal #0 -- canal #1 -- exito #2",
											// sol[8].toString(),obj.intValue(),exito);
											if (obj.intValue() == 1 && exito == true) {
												aprobar = true;
											} else if (obj.intValue() == 3 && exito == true) {
												ejecutivo = true;
											} else if (obj.intValue() == 4 && exito == true) {
												financiero = true;
											} else if (obj.intValue() == 5 && exito == true) {
												comenta = true;
											}
										}
									}
									if (aprobar == true || ejecutivo == true || financiero == true) {
										boolean aprobarNegocio = false;
										boolean ejecutivoNegocio = false;
										boolean financieroNegocio = false;
										boolean comentaNegocio = false;
										// evaluar negocio autorizado para este
										// perfil
										if (sol[10] != null && sol[0] != null) {
											if ((sol[10].toString()).equals("1N") || (sol[10].toString()).equals("1C")) {
												List<String> listaNegocios = scoringService.obtenerNegociosAsociadoToSolicitud(Long.parseLong(sol[0].toString()));
												if (listaNegocios != null && listaNegocios.size() > 0) {
													boolean validaNegocio = false;
													for (String codigoNegocio : listaNegocios) {
														for (Integer obj : array) {
															if (obj != null	&& obj.intValue() != 0) {
																validaNegocio = scoringService.obtenerHabilitacionUsuarioNegocio(
																				usuarioLogueado.getIdPersonal(),
																				codigoNegocio,
																				tipoCuenta,
																				obj.intValue());
																if (obj.intValue() == 1 && validaNegocio == true) {
																	aprobarNegocio = true;
																} else if (obj.intValue() == 3 && validaNegocio == true) {
																	ejecutivoNegocio = true;
																} else if (obj.intValue() == 4 && validaNegocio == true) {
																	financieroNegocio = true;
																} else if (obj.intValue() == 5 && validaNegocio == true) {
																	comentaNegocio = true;
																}
															}
														}
													}
												}
											} else if ((sol[10].toString()).equals("2I") || (sol[10].toString()).equals("2N") || (sol[10].toString()).equals("2R")) {
												List<String> listaNegocios = scoringService.obtenerNegociosAsociadoToSolicitudLineaCredito(Long.parseLong(sol[0].toString()),
																																		   conceptosNegociosSessions);
												if (listaNegocios != null && listaNegocios.size() > 0) {
													for (Integer obj : array) {
														if (obj != null	&& obj.intValue() != 0) {
															boolean validaNegocio = false;
															for (String codigoNegocio : listaNegocios) {
																validaNegocio = scoringService.obtenerHabilitacionUsuarioNegocio(usuarioLogueado.getIdPersonal(),
																																codigoNegocio,
																																tipoCuenta,
																																obj.intValue());
																if (obj.intValue() == 1	&& validaNegocio == true) {
																	aprobarNegocio = true;
																} else if (obj.intValue() == 3 && validaNegocio == true) {
																	ejecutivoNegocio = true;
																} else if (obj.intValue() == 4 && validaNegocio == true) {
																	financieroNegocio = true;
																} else if (obj.intValue() == 5 && validaNegocio == true) {
																	comentaNegocio = true;
																}
															}
														}
													}
												}
											} else if ((sol[10].toString()).equals("4B") || (sol[10].toString()).equals("4PR")
													|| (sol[10].toString()).equals("4BD") || (sol[10].toString()).equals("4DM")) {
												if (sol[0] != null) {
													// log.debug("solicitud #0",
													// sol[0].toString());
												}
												aprobarNegocio = true;
												ejecutivoNegocio = true;
												financieroNegocio = true;
											} else {

												/*
												 * si la solictud no es ni venta
												 * puntual y lineas de credito.
												 */
												aprobarNegocio = true;
												ejecutivoNegocio = true;
												financieroNegocio = true;
											}

										}

										/* evaluacion de negocios */
										if (aprobarNegocio == true || ejecutivoNegocio == true || financieroNegocio == true) {
											exito = true;
										} else if (comentaNegocio == true) {
											exito = false;
										} else if (aprobarNegocio == false && ejecutivoNegocio == false && financieroNegocio == false) {
											exito = false;
										}
									} else if (comenta == true) {
										exito = false;
									}
								}

								if (exito) {// (sol.getUsuarioActual() == null
											// ||
											// "".equals(sol.getUsuarioActual()))

									if (usuarioSegur != null && (usuarioSegur.getAlias().toLowerCase()).equals("administrador")) {
										if (nuevo.getCodigoEstado() != null) {
											if (nuevo.getCodigoEstado().equals("A")	|| nuevo.getCodigoEstado().equals("NU")
											 || nuevo.getCodigoEstado().equals("R")	|| nuevo.getCodigoEstado().equals("E")) {
												nuevo.setControlador(1);
											} else if (nuevo.getCodigoEstado().equals("SA")	|| nuevo.getCodigoEstado().equals("B")) {
												if (nuevo.getUsuarioTomada().equals(usuarioLogueado.getAlias())) {
													nuevo.setControlador(1);
												} else {
													nuevo.setControlador(1);
												}
											} else if (nuevo.getCodigoEstado().equals("DE")) {
												boolean evaluacion = evaluarFechaSolicitud(nuevo.getIdSolictud(),nuevo.getCodigoEstado());
												if (evaluacion) {
													solicitudAux.setEstado("NC");
													if ((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.V1N.getNombre())
													 || (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.V1C.getNombre())) {
														try {
															LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																	.createQuery("Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																	.setParameter("solAux",solicitudAux.getId().getNumSolicitud())
																	.getSingleResult();

															if (venta != null) {
																venta.setStatusSolicitud("NC");
																scoringService.mergerSolicitud(solicitudAux);
																scoringService.mergerSolicitudVentaPuntual(venta);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado.getCodEstado());
																	nuevo.setEstado(estado.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager.refresh(solicitudAux);
																entityManager.refresh(venta);
																entityManager.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de venta puntual");
														}
													} else if ((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.LC1.getNombre())
															|| (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.LC2.getNombre())
															|| (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.LC3.getNombre())) {
														try {
															LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																	.createQuery("Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																	.setParameter("solAux",solicitudAux.getId().getNumSolicitud())
																	.getSingleResult();

															if (credito != null) {
																credito.setStatusSolicitud("NC");
																scoringService.mergerSolicitud(solicitudAux);
																scoringService.mergerSolicitudLineaCredito(credito);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado.getCodEstado());
																	nuevo.setEstado(estado.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager.refresh(solicitudAux);
																entityManager.refresh(credito);
																entityManager.flush();
															}

														} catch (Exception e) {
															log.debug("no existe datos de linea credito.");
														}

													} else if ((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.CR1.getNombre())
															|| (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.CR2.getNombre())
															|| (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.CRP3.getNombre())
															|| (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.CRP4.getNombre())) {

														try {
															LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																	.createQuery("Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																	.setParameter("solAux",solicitudAux.getId().getNumSolicitud())
																	.getSingleResult();

															if (condicion != null) {
																condicion.setStatusSolicitud("NC");
																scoringService.mergerSolicitud(solicitudAux);
																scoringService.mergerSolicitudCondiciones(condicion);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado.getCodEstado());
																	nuevo.setEstado(estado.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager.refresh(solicitudAux);
																entityManager.refresh(condicion);
																entityManager.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de condiciones.");
														}
													} else if ((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS1.getNombre())
															|| (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS2.getNombre())
															|| (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS.getNombre())) {
														/***************** Bloqueo y Desbloqueo ***********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService.getSolicitudOtrasId(solicitudAux.getId().getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra.setStatusSolicitud("NC");
																scoringService.mergerSolicitud(solicitudAux);
																scoringService.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado.getCodEstado());
																	nuevo.setEstado(estado.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager.refresh(solicitudAux);
																entityManager.refresh(solicitudOtra);
																entityManager.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de bloqueo o desbloqueo.");
														}
													} else if ((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS3.getNombre())) {
														/***************** Creacion DM ***********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService.getSolicitudOtrasId(solicitudAux.getId().getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra.setStatusSolicitud("NC");
																scoringService.mergerSolicitud(solicitudAux);
																scoringService.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado.getCodEstado());
																	nuevo.setEstado(estado.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager.refresh(solicitudAux);
																entityManager.refresh(solicitudOtra);
																entityManager.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de creacion de dm.");
														}

													} else if ((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS4.getNombre())) {
														/********************* Prorroga ********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService.getSolicitudOtrasId(solicitudAux.getId().getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra.setStatusSolicitud("NC");
																scoringService.mergerSolicitud(solicitudAux);
																scoringService.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado.getCodEstado());
																	nuevo.setEstado(estado.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager.refresh(solicitudAux);
																entityManager.refresh(solicitudOtra);
																entityManager.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de prorroga.");
														}
													}
												} else {
													nuevo.setControlador(3);
												}
											} else {
												nuevo.setControlador(1);
											}
										}
									} else {
										if (usuarioCargoAux != null	&& usuarioCargoAux.getAdministrador().booleanValue()) {
											if (nuevo.getCodigoEstado() != null) {
												if (nuevo.getCodigoEstado().equals("A")	|| nuevo.getCodigoEstado().equals("NU") || nuevo.getCodigoEstado().equals("R")) {
													nuevo.setControlador(1);
												} else if (nuevo.getCodigoEstado().equals("DE")) {
													boolean evaluacion = evaluarFechaSolicitud(nuevo.getIdSolictud(),nuevo.getCodigoEstado());
													if (evaluacion) {
														solicitudAux.setEstado("NC");
														if ((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.V1N.getNombre())
														 || (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.V1C.getNombre())) {
															try {
																LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																		.createQuery("Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																		.setParameter("solAux",solicitudAux.getId().getNumSolicitud())
																		.getSingleResult();

																if (venta != null) {
																	venta.setStatusSolicitud("NC");
																	scoringService.mergerSolicitud(solicitudAux);
																	scoringService.mergerSolicitudVentaPuntual(venta);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado.getCodEstado());
																		nuevo.setEstado(estado.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager.refresh(solicitudAux);
																	entityManager.refresh(venta);
																	entityManager.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de venta puntual");
															}
														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.LC1
																		.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.LC2
																				.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.LC3
																				.getNombre())) {
															try {
																LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																		.createQuery(
																				"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				solicitudAux
																						.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (credito != null) {
																	credito.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudLineaCredito(credito);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(credito);
																	entityManager
																			.flush();
																}

															} catch (Exception e) {
																log.debug("no existe datos de linea credito.");
															}

														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.CR1
																		.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CR2
																				.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CRP3
																				.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CRP4
																				.getNombre())) {

															try {
																LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																		.createQuery(
																				"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				solicitudAux
																						.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (condicion != null) {
																	condicion
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudCondiciones(condicion);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(condicion);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de condiciones.");
															}
														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS1
																		.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.OS2
																				.getNombre())
																|| (solicitudAux
																		.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.OS
																				.getNombre())) {
															/*****************
															 * Bloqueo y
															 * Desbloqueo
															 ***********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(solicitudAux
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de bloqueo o desbloqueo.");
															}
														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS3
																		.getNombre())) {
															/***************** Creacion DM ***********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(solicitudAux
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de creacion de dm.");
															}

														} else if ((solicitudAux
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS4
																		.getNombre())) {
															/********************* Prorroga ********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(solicitudAux
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(solicitudAux);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(solicitudAux);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de prorroga.");
															}
														}
													} else {
														nuevo.setControlador(3);
													}
												} else {
													nuevo.setControlador(1);
												}
											}
										} else {
											if (usuarioCargoAux != null
													&& !usuarioCargoAux
															.getCargo()
															.getCodCargo()
															.equals("015")) {
												if (nuevo.getCodigoEstado() != null) {
													if (nuevo.getCodigoEstado()
															.equals("I")
															|| nuevo.getCodigoEstado()
																	.equals("DC")
															|| nuevo.getCodigoEstado()
																	.equals("DR")) {
														nuevo.setControlador(1);
													} else if (nuevo
															.getCodigoEstado()
															.equals("E")) {
														if (lcredUsuarioNivelEnc
																.getId()
																.getIdUsuario()
																.equals(solicitudAux
																		.getUsuarioActual())) {
															nuevo.setControlador(1);
														} else {
															if (usuarioCargoAux != null
																	&& usuarioCargoAux
																			.getCargo()
																			.getNivelCargo() == 10) {
																nuevo.setControlador(1);
															} else {
																nuevo.setControlador(1);
															}
														}
													} else if (nuevo
															.getCodigoEstado()
															.equals("DE")) {
														boolean evaluacion = evaluarFechaSolicitud(
																nuevo.getIdSolictud(),
																nuevo.getCodigoEstado());
														if (evaluacion) {
															solicitudAux
																	.setEstado("NC");
															if ((solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.V1N
																			.getNombre())
																	|| (solicitudAux
																			.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.V1C
																					.getNombre())) {
																try {
																	LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																			.createQuery(
																					"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																			.setParameter(
																					"solAux",
																					solicitudAux
																							.getId()
																							.getNumSolicitud())
																			.getSingleResult();

																	if (venta != null) {
																		venta.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(solicitudAux);
																		scoringService
																				.mergerSolicitudVentaPuntual(venta);
																		LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																			nuevo.setProceso(perfil2
																					.getDesPerfil());
																		}
																		entityManager
																				.refresh(solicitudAux);
																		entityManager
																				.refresh(venta);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de venta puntual");
																}
															} else if ((solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.LC1
																			.getNombre())
																	|| (solicitudAux
																			.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.LC2
																					.getNombre())
																	|| (solicitudAux
																			.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.LC3
																					.getNombre())) {
																try {
																	LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																			.createQuery(
																					"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																			.setParameter(
																					"solAux",
																					solicitudAux
																							.getId()
																							.getNumSolicitud())
																			.getSingleResult();

																	if (credito != null) {
																		credito.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(solicitudAux);
																		scoringService
																				.mergerSolicitudLineaCredito(credito);
																		LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																			nuevo.setProceso(perfil2
																					.getDesPerfil());
																		}
																		entityManager
																				.refresh(solicitudAux);
																		entityManager
																				.refresh(credito);
																		entityManager
																				.flush();
																	}

																} catch (Exception e) {
																	log.debug("no existe datos de linea credito.");
																}

															} else if ((solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CR1
																			.getNombre())
																	|| (solicitudAux
																			.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.CR2
																					.getNombre())
																	|| (solicitudAux
																			.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.CRP3
																					.getNombre())
																	|| (solicitudAux
																			.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.CRP4
																					.getNombre())) {

																try {
																	LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																			.createQuery(
																					"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																			.setParameter(
																					"solAux",
																					solicitudAux
																							.getId()
																							.getNumSolicitud())
																			.getSingleResult();

																	if (condicion != null) {
																		condicion
																				.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(solicitudAux);
																		scoringService
																				.mergerSolicitudCondiciones(condicion);
																		LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																			nuevo.setProceso(perfil2
																					.getDesPerfil());
																		}
																		entityManager
																				.refresh(sol);
																		entityManager
																				.refresh(condicion);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de condiciones.");
																}
															} else if ((solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS1
																			.getNombre())
																	|| (solicitudAux
																			.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.OS2
																					.getNombre())
																	|| (solicitudAux
																			.getTipTiposol()
																			.trim())
																			.equals(TipoSolicitudType.OS
																					.getNombre())) {
																/*****************
																 * Bloqueo y
																 * Desbloqueo
																 ***********************/
																try {
																	LcredSolicitudOtras solicitudOtra = scoringService
																			.getSolicitudOtrasId(solicitudAux
																					.getId()
																					.getNumSolicitud());
																	if (solicitudOtra != null) {
																		solicitudOtra
																				.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(solicitudAux);
																		scoringService
																				.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																		LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																			nuevo.setProceso(perfil2
																					.getDesPerfil());
																		}
																		entityManager
																				.refresh(sol);
																		entityManager
																				.refresh(solicitudOtra);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de bloqueo o desbloqueo.");
																}
															} else if ((solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS3
																			.getNombre())) {
																/***************** Creacion DM ***********************/
																try {
																	LcredSolicitudOtras solicitudOtra = scoringService
																			.getSolicitudOtrasId(solicitudAux
																					.getId()
																					.getNumSolicitud());
																	if (solicitudOtra != null) {
																		solicitudOtra
																				.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(solicitudAux);
																		scoringService
																				.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																		LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																			nuevo.setProceso(perfil2
																					.getDesPerfil());
																		}
																		entityManager
																				.refresh(solicitudAux);
																		entityManager
																				.refresh(solicitudOtra);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de creacion de dm.");
																}

															} else if ((solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS4
																			.getNombre())) {
																/********************* Prorroga ********************/
																try {
																	LcredSolicitudOtras solicitudOtra = scoringService
																			.getSolicitudOtrasId(solicitudAux
																					.getId()
																					.getNumSolicitud());
																	if (solicitudOtra != null) {
																		solicitudOtra
																				.setStatusSolicitud("NC");
																		scoringService
																				.mergerSolicitud(solicitudAux);
																		scoringService
																				.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																		LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																				.getEstado());
																		if (estado != null) {
																			nuevo.setCodigoEstado(estado
																					.getCodEstado());
																			nuevo.setEstado(estado
																					.getDesEstado());
																			nuevo.setControlador(2);
																			nuevo.setProceso(perfil2
																					.getDesPerfil());
																		}
																		entityManager
																				.refresh(solicitudAux);
																		entityManager
																				.refresh(solicitudOtra);
																		entityManager
																				.flush();
																	}
																} catch (Exception e) {
																	log.debug("no existe datos de prorroga.");
																}
															}
														} else {
															nuevo.setControlador(3);
														}
													} else if (nuevo
															.getCodigoEstado()
															.equals("SA")
															|| nuevo.getCodigoEstado()
																	.equals("B")) {
														if (nuevo
																.getUsuarioTomada()
																.equals(usuarioLogueado
																		.getAlias())) {
															nuevo.setProceso(perfil1
																	.getDesPerfil());
															nuevo.setControlador(1);
														} else {
															nuevo.setProceso(perfil2
																	.getDesPerfil());
															nuevo.setControlador(2);
														}
													} else if (nuevo
															.getCodigoEstado()
															.equals("N")
															|| nuevo.getCodigoEstado()
																	.equals("NC")) {
														nuevo.setProceso(perfil2
																.getDesPerfil());
														nuevo.setControlador(2);
													} else if (nuevo
															.getCodigoEstado()
															.equals("ES")) {
														if (nuevo
																.getUsuarioProceso() != null
																&& nuevo.getUsuarioProceso()
																		.equals(usuarioLogueado
																				.getAlias())) {
															nuevo.setControlador(1);
														} else {
															nuevo.setProceso(perfil2
																	.getDesPerfil());
															nuevo.setControlador(1);
														}
													} else {
														nuevo.setProceso(perfil2
																.getDesPerfil());
														nuevo.setControlador(1);
													}
												}
											} else {
												nuevo.setProceso(perfil2
														.getDesPerfil());
												nuevo.setControlador(1);
											}
										}
									}
								} else {
									if (usuarioSegur != null
											&& (usuarioSegur.getAlias()
													.toLowerCase())
													.equals("administrador")) {
										if (nuevo.getCodigoEstado() != null) {
											if (nuevo.getCodigoEstado().equals(
													"A")
													|| nuevo.getCodigoEstado()
															.equals("NU")
													|| nuevo.getCodigoEstado()
															.equals("R")
													|| nuevo.getCodigoEstado()
															.equals("E")) {
												nuevo.setControlador(1);
											} else if (nuevo.getCodigoEstado()
													.equals("SA")
													|| nuevo.getCodigoEstado()
															.equals("B")) {
												if (nuevo.getUsuarioTomada()
														.equals(usuarioLogueado
																.getAlias())) {
													nuevo.setControlador(1);
												} else {
													nuevo.setControlador(2);
												}
											} else if (nuevo.getCodigoEstado()
													.equals("DE")) {
												boolean evaluacion = evaluarFechaSolicitud(
														nuevo.getIdSolictud(),
														nuevo.getCodigoEstado());
												if (evaluacion) {
													solicitudAux
															.setEstado("NC");
													if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.V1N
																	.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.V1C
																			.getNombre())) {
														try {
															LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																	.createQuery(
																			"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																	.setParameter(
																			"solAux",
																			solicitudAux
																					.getId()
																					.getNumSolicitud())
																	.getSingleResult();

															if (venta != null) {
																venta.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudVentaPuntual(venta);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(venta);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de venta puntual");
														}
													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.LC1
																	.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.LC2
																			.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.LC3
																			.getNombre())) {
														try {
															LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																	.createQuery(
																			"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																	.setParameter(
																			"solAux",
																			solicitudAux
																					.getId()
																					.getNumSolicitud())
																	.getSingleResult();

															if (credito != null) {
																credito.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudLineaCredito(credito);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(credito);
																entityManager
																		.flush();
															}

														} catch (Exception e) {
															log.debug("no existe datos de linea credito.");
														}

													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.CR1
																	.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CR2
																			.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CRP3
																			.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CRP4
																			.getNombre())) {

														try {
															LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																	.createQuery(
																			"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																	.setParameter(
																			"solAux",
																			solicitudAux
																					.getId()
																					.getNumSolicitud())
																	.getSingleResult();

															if (condicion != null) {
																condicion
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudCondiciones(condicion);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(condicion);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de condiciones.");
														}
													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.OS1
																	.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS2
																			.getNombre())
															|| (solicitudAux
																	.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS
																			.getNombre())) {
														/***************** Bloqueo y Desbloqueo ***********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService
																	.getSolicitudOtrasId(solicitudAux
																			.getId()
																			.getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(solicitudOtra);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de bloqueo o desbloqueo.");
														}
													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.OS3
																	.getNombre())) {
														/***************** Creacion DM ***********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService
																	.getSolicitudOtrasId(solicitudAux
																			.getId()
																			.getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(solicitudOtra);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de creacion de dm.");
														}

													} else if ((solicitudAux
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.OS4
																	.getNombre())) {
														/********************* Prorroga ********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService
																	.getSolicitudOtrasId(solicitudAux
																			.getId()
																			.getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(solicitudAux);
																scoringService
																		.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(solicitudAux);
																entityManager
																		.refresh(solicitudOtra);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de prorroga.");
														}
													}
												} else {
													nuevo.setControlador(3);
												}
											} else {
												nuevo.setControlador(1);
											}
										}
									} else {
										nuevo.setProceso(perfil2.getDesPerfil());
										nuevo.setControlador(2);
									}
								}

								/*
								 * verificar si tiene mas un derivado o
								 * aprbadores
								 */

								if (solicitudAux != null && solicitudAux.getEstadoEvaluacion() != null) {
									Long numero = scoringService
											.getCantidadSolicitudUsuarioDerivacion(solicitudAux
													.getId().getNumSolicitud());
									if (numero != null
											&& numero.longValue() > 1) {
										nuevo.setEstadoEvaluacion(solicitudAux
												.getEstadoEvaluacion());
										nuevo.setHabilitaLista(true);
									} else {
										List<SolicitudUsuarioDerivada> lista = scoringService
												.getSolicitudUsuarioDerivacionForSolicitud(solicitudAux
														.getId()
														.getNumSolicitud());
										if (lista != null && lista.size() > 0) {
											SolicitudUsuarioDerivada obj = lista
													.get(0);
											if (obj != null) {
												nuevo.setUsuarioProceso(obj
														.getUsuario()
														.getAlias());
											}
										}
										nuevo.setHabilitaLista(false);
									}
								}

								if (nuevo.getEmisor() != null) {
									if (listaUsuarios != null
											&& listaUsuarios.size() > 0) {
										if (!listaUsuarios.contains((nuevo
												.getEmisor().toUpperCase())
												.trim())) {
											listaUsuarios.add((nuevo
													.getEmisor().toUpperCase())
													.trim());
										}
									} else {
										listaUsuarios.add(nuevo.getEmisor()
												.toUpperCase().trim());
									}
								}
								if (nuevo.getDespTipoSolictud() != null) {
									if (listaTiposSolicitudes != null
											&& listaTiposSolicitudes.size() > 0) {
										if (!listaTiposSolicitudes
												.contains((nuevo
														.getDespTipoSolictud()
														.toUpperCase()).trim())) {
											listaTiposSolicitudes.add((nuevo
													.getDespTipoSolictud()
													.toUpperCase()).trim());
										}
									} else {
										listaTiposSolicitudes.add((nuevo
												.getDespTipoSolictud()
												.toUpperCase()).trim());
									}
								}

								if (listaEstadoString != null
										&& listaEstadoString.size() > 0) {
									if (!listaEstadoString.contains((nuevo
											.getEstado().toUpperCase()).trim())) {
										listaEstadoString.add((nuevo
												.getEstado().toUpperCase())
												.trim());
									}
								} else {
									listaEstadoString.add((nuevo.getEstado()
											.toUpperCase()).trim());
								}

								if (listaCanalString != null
										&& listaCanalString.size() > 0) {
									if (!listaCanalString.contains((nuevo
											.getCanal().toUpperCase()).trim())) {
										listaCanalString.add((nuevo.getCanal()
												.toUpperCase()).trim());
									}
								} else {
									listaCanalString.add((nuevo.getCanal()
											.toUpperCase()).trim());
								}

								if (listaProcesoString != null
										&& listaProcesoString.size() > 0) {
									if (!listaProcesoString
											.contains((nuevo.getProceso()
													.toUpperCase()).trim())) {
										listaProcesoString.add((nuevo
												.getProceso().toUpperCase())
												.trim());
									}
								} else {
									listaProcesoString.add((nuevo.getProceso()
											.toUpperCase()).trim());
								}

								if (listaSucursalSolicitudes != null
										&& listaSucursalSolicitudes.size() > 0) {
									if (!listaSucursalSolicitudes
											.contains((nuevo.getSucursal()
													.toUpperCase()).trim())) {
										listaSucursalSolicitudes.add((nuevo
												.getSucursal().toUpperCase())
												.trim());
									}
								} else {
									listaSucursalSolicitudes.add((nuevo
											.getSucursal().toUpperCase())
											.trim());
								}

								listaTodasSolicitudesDto.add(nuevo);
								nuevo = null;
							}
						}
						if (listaTodasSolicitudesDto != null
								&& listaTodasSolicitudesDto.size() == 0) {
							listaTodasSolicitudesDto = null;
						}

						if (listaUsuarios != null) {
							Collections.sort(listaUsuarios);
						}
						if (listaSucursalSolicitudes != null) {
							Collections.sort(listaSucursalSolicitudes);
						}

					}

				}

			} else {

				listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>(0);
			}
			log.debug("cantidad de ususarios de todas #0", listaUsuarios.size());
			log.debug("lista de solicitudes #0",
					listaTodasSolicitudesDto.size());
		} catch (Exception e) {
			if (listaUsuarios != null) {
				Collections.sort(listaUsuarios);
			}
			log.error("error al sacar todas las solictudes en generar #0",
					e.getMessage());
			e.printStackTrace();
		}
	}

	@Asynchronous
	@SuppressWarnings("unchecked")
	public void sacarSolicitudDerivadasForOtros() {
		Sucursal suc = null;
		this.setEstadoFilter("");
		this.setProcesoFilter("");
		this.setCanalFilter("");
		this.setSucursalFilter("");
		try {
			LcredPerfiles perfil1 = scoringService.obtenerPerfil("1");
			LcredPerfiles perfil2 = scoringService.obtenerPerfil("6");

			if (usuarioLogueado != null) {
				List<Integer> array = scoringService.obtenerPerfilesDelUsuario(usuarioLogueado.getIdPersonal());
				sacarEstadosSolicitudes();

				/************ Solicitudes pendientes para Analizar y Aprobar o rechazar **********************/
				if (usuarioLogueado != null && usuarioLogueado != null) {
					List<LcredSolicitud> listaSolicitudesDerivadas = (List<LcredSolicitud>) entityManager
							// Cambiar query para obtener las solicitudes
							// pendientes para aprobar
							.createQuery(
									"Select s from LcredSolicitud s "
											+ " Where s.estado in ('B','SA', 'DC', 'DR') and s.usuarioDevuelve=:usuarioActual "
											+ " or s.id.numSolicitud in ("
											+ "  Select sud.idSolicitud  from SolicitudUsuarioDerivada sud "
											+ "  where sud.usuario.idPersonal=:idPersonal "
											+ "	and sud.idSolicitud > 29347  "
											+ "   and sud.estado.codEstado in ('B','SA')"
											+ "   and sud.confirmacion is null ) "
											+ " order by s.id.numSolicitud desc ")
							.setParameter("idPersonal",	usuarioLogueado.getIdPersonal())
							.setParameter("usuarioActual",usuarioLogueado.getAlias())
							.setMaxResults(100).getResultList();

					// "  and sud.estado.codEstado in ('B','SA') and sud.confirmacion is null ) "
					// +
					// "  and s.estado not in ('NU', 'A', 'R', 'P', 'RP', 'AP', 'I' ) "
					// +

					if (listaSolicitudesDerivadas != null) {
						listaSolicitudesDerivadasForOtros = new ArrayList<SolicitudDTO>(0);
						listaUsuariosDerivadas = new ArrayList<String>(0);
						listaTiposDerivadas = new ArrayList<String>(0);
						listaEstadoDerivadas = new ArrayList<String>(0);
						listaCanalDerivadas = new ArrayList<String>(0);
						listaProcesoDerivadas = new ArrayList<String>(0);
						listaSucursalDerivadas = new ArrayList<String>(0);

						for (LcredSolicitud sol : listaSolicitudesDerivadas) {

							int tipoCuenta = 0;
							boolean autorizacion = false;
							if ("Mixto".equals(sol.getCanal())) {
								autorizacion = verificaCanal(TipoCuentasKupferType.MX);
								tipoCuenta = TipoCuentasKupferType.MX.ordinal();
							} else if ("Kupfer Express".equals(sol.getCanal())) {
								autorizacion = verificaCanal(TipoCuentasKupferType.KX);
								tipoCuenta = TipoCuentasKupferType.KX.ordinal();
							} else if ("Grandes Cuentas".equals(sol.getCanal())) {
								autorizacion = verificaCanal(TipoCuentasKupferType.GC);
								tipoCuenta = TipoCuentasKupferType.GC.ordinal();
							}
							boolean exito = true;
							// log.debug("solicitud #0  y sucursal : #1 ",
							// sol.getId().getNumSolicitud(),sol.getCodSucursal());
							// for(Integer obj :array){
							// if(obj!= null && obj.intValue() != 0){
							// exito =
							// scoringService.obtenerHabilitacionUsuario(usuarioSegur.getIdPersonal(),
							// sol.getCodSucursal(), tipoCuenta,
							// obj.intValue());
							// }
							// }

							if (autorizacion == true && exito == true) {
								SolicitudDTO nuevo = new SolicitudDTO();
								nuevo.setIdSolictud(sol.getId().getNumSolicitud());
								nuevo.setRut(sol.getRutCliente().replace("-",""));
								nuevo.setRazonSocial(sol.getNomCliente());
								nuevo.setRazonSocialString(sol.getNomCliente());
								nuevo.setEmisor(sol.getCodEmisor());
								nuevo.setDespTipoSolictud(sol.getSubTiposol());
								nuevo.setFechaSalida(sol.getFecSalida());
								nuevo.setUsuarioProceso(sol.getUsuarioActual());
								nuevo.setTipoSolicitud(sol.getTipTiposol());
								nuevo.setCanal(sol.getCanal());
								nuevo.setCondicionPago(sol.getFormaPago());
								nuevo.setUsuarioTomada(sol.getUsuarioDevuelve());
								nuevo.setPeakCredito(sol.getPeackCredito().doubleValue());
								
								if(sol.getSociedad() != null){
									nuevo.setSociedad(sol.getSociedad().getRazonSocial());
								}
								if (sol.getId().getFecSolicitud() != null && sol.getHraSolicitud() != null) {

									/* rescatar la fecha */
									SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
									String fecha = sdf.format(sol.getId().getFecSolicitud());

									/* rescatar la hora */
									SimpleDateFormat sdh = new SimpleDateFormat("HH:mm");
									String hora = sdh.format(sol.getHraSolicitud());

									/* fecha y hora */
									// log.debug(" Fecha String:" + fecha);
									// log.debug(" hora String :" + hora);

									DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yy HH:mm");
									try {
										Date d = formatoFecha.parse(fecha + " " + hora);
										nuevo.setFechaString(fecha + " " + hora);
										nuevo.setFechaEmision(d);
										nuevo.setHoraEmision(sol.getHraSolicitud());
									} catch (Exception e) {
										throw new Exception(e);
									}
								}

								if (sol.getEstado() != null) {
									LcredEstado estado = obtenerObjetoLcredEstado(sol.getEstado());
									if (estado != null) {
										nuevo.setCodigoEstado(estado
												.getCodEstado());
										nuevo.setEstado(estado.getDesEstado());
									}

									if (array != null) {
										for (Integer obj : array) {
											if (obj != null) {
												if (obj.intValue() == 1
														|| obj.intValue() == 3
														|| obj.intValue() == 4) {
													nuevo.setProceso(perfil1
															.getDesPerfil());
													break;
												} else {
													nuevo.setProceso(perfil2
															.getDesPerfil());
													break;
												}
											}
										}
									}
								}

								if (sol.getSucursalEmisor() != null) {
									// log.debug(" codigo de sucursal  :" +
									// sol.getSucursalEmisor());
									suc = obtenerObjetoSucursal(sol
											.getSucursalEmisor());
									if (suc != null) {
										nuevo.setSucursal(suc.getDescripcion());
									} else {
										nuevo.setSucursal(sol.getCodSucursal());
									}

								}
								if (sol.getMontoAsegurado() != null) {
									nuevo.setMontoAsegurado(sol
											.getMontoAsegurado().doubleValue());
								} else {
									nuevo.setMontoAsegurado((double) 0);
								}

								if (sol.getMonto() != null) {
									nuevo.setMonto(sol.getMonto().doubleValue());
								} else {
									nuevo.setMonto((double) 0);
								}

								if (sol.getRiesgoKupfer() != null) {
									nuevo.setMontoRiegoKupfer(sol
											.getRiesgoKupfer().doubleValue());
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

								if (exito) {
									nuevo.setControlador(1);
								}

								/*
								 * verificar si tiene mas un derivado o
								 * aprbadores
								 */
								if (exito == true && sol != null) {// (sol.getUsuarioActual()
																	// == null
																	// ||
																	// "".equals(sol.getUsuarioActual()))
									if (usuarioCargoAux != null
											&& usuarioCargoAux
													.getAdministrador()
													.booleanValue()) {
										if (nuevo.getCodigoEstado() != null) {
											if (nuevo.getCodigoEstado().equals(
													"A")
													|| nuevo.getCodigoEstado()
															.equals("NU")
													|| nuevo.getCodigoEstado()
															.equals("R")) {
												nuevo.setControlador(0);
											} else if (nuevo.getCodigoEstado()
													.equals("DE")) {
												boolean evaluacion = evaluarFechaSolicitud(
														nuevo.getIdSolictud(),
														nuevo.getCodigoEstado());
												if (evaluacion) {
													sol.setEstado("NC");
													if ((sol.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.V1N
																	.getNombre())
															|| (sol.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.V1C
																			.getNombre())) {
														try {
															LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																	.createQuery(
																			"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																	.setParameter(
																			"solAux",
																			sol.getId()
																					.getNumSolicitud())
																	.getSingleResult();

															if (venta != null) {
																venta.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(sol);
																scoringService
																		.mergerSolicitudVentaPuntual(venta);
																LcredEstado estado = obtenerObjetoLcredEstado(sol
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(sol);
																entityManager
																		.refresh(venta);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de venta puntual");
														}
													} else if ((sol
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.LC1
																	.getNombre())
															|| (sol.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.LC2
																			.getNombre())
															|| (sol.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.LC3
																			.getNombre())) {
														try {
															LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																	.createQuery(
																			"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																	.setParameter(
																			"solAux",
																			sol.getId()
																					.getNumSolicitud())
																	.getSingleResult();

															if (credito != null) {
																credito.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(sol);
																scoringService
																		.mergerSolicitudLineaCredito(credito);
																LcredEstado estado = obtenerObjetoLcredEstado(sol
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(sol);
																entityManager
																		.refresh(credito);
																entityManager
																		.flush();
															}

														} catch (Exception e) {
															log.debug("no existe datos de linea credito.");
														}

													} else if ((sol
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.CR1
																	.getNombre())
															|| (sol.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CR2
																			.getNombre())
															|| (sol.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CRP3
																			.getNombre())
															|| (sol.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.CRP4
																			.getNombre())) {

														try {
															LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																	.createQuery(
																			"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																	.setParameter(
																			"solAux",
																			sol.getId()
																					.getNumSolicitud())
																	.getSingleResult();

															if (condicion != null) {
																condicion
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(sol);
																scoringService
																		.mergerSolicitudCondiciones(condicion);
																LcredEstado estado = obtenerObjetoLcredEstado(sol
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(sol);
																entityManager
																		.refresh(condicion);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de condiciones.");
														}
													} else if ((sol
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.OS1
																	.getNombre())
															|| (sol.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS2
																			.getNombre())
															|| (sol.getTipTiposol()
																	.trim())
																	.equals(TipoSolicitudType.OS
																			.getNombre())) {
														/***************** Bloqueo y Desbloqueo ***********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService
																	.getSolicitudOtrasId(sol
																			.getId()
																			.getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(sol);
																scoringService
																		.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(sol
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(sol);
																entityManager
																		.refresh(solicitudOtra);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de bloqueo o desbloqueo.");
														}
													} else if ((sol
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.OS3
																	.getNombre())) {
														/***************** Creacion DM ***********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService
																	.getSolicitudOtrasId(sol
																			.getId()
																			.getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(sol);
																scoringService
																		.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(sol
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(sol);
																entityManager
																		.refresh(solicitudOtra);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de creacion de dm.");
														}

													} else if ((sol
															.getTipTiposol()
															.trim())
															.equals(TipoSolicitudType.OS4
																	.getNombre())) {
														/********************* Prorroga ********************/
														try {
															LcredSolicitudOtras solicitudOtra = scoringService
																	.getSolicitudOtrasId(sol
																			.getId()
																			.getNumSolicitud());
															if (solicitudOtra != null) {
																solicitudOtra
																		.setStatusSolicitud("NC");
																scoringService
																		.mergerSolicitud(sol);
																scoringService
																		.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																LcredEstado estado = obtenerObjetoLcredEstado(sol
																		.getEstado());
																if (estado != null) {
																	nuevo.setCodigoEstado(estado
																			.getCodEstado());
																	nuevo.setEstado(estado
																			.getDesEstado());
																	nuevo.setControlador(2);
																}
																entityManager
																		.refresh(sol);
																entityManager
																		.refresh(solicitudOtra);
																entityManager
																		.flush();
															}
														} catch (Exception e) {
															log.debug("no existe datos de prorroga.");
														}
													}
												} else {
													nuevo.setControlador(3);
												}
											} else if (nuevo.getCodigoEstado()
													.equals("DR")
													|| nuevo.getCodigoEstado()
															.equals("DC")) {
												nuevo.setControlador(2);
											} else if (nuevo.getCodigoEstado()
													.equals("SA")
													|| nuevo.getCodigoEstado()
															.equals("B")) {
												if (nuevo.getUsuarioTomada()
														.equals(usuarioLogueado
																.getAlias())) {
													nuevo.setControlador(2);
												} else {
													nuevo.setControlador(1);
												}
											} else {
												nuevo.setControlador(1);
											}
										}
									} else {
										if (usuarioCargoAux != null
												&& !usuarioCargoAux.getCargo()
														.getCodCargo()
														.equals("015")) {
											if (nuevo.getCodigoEstado() != null) {
												if (nuevo.getCodigoEstado()
														.equals("DC")
														|| nuevo.getCodigoEstado()
																.equals("DR")) {
													nuevo.setControlador(2);
												} else if (nuevo
														.getCodigoEstado()
														.equals("E")) {
													if (usuarioLogueado
															.getAlias()
															.equals(nuevo
																	.getUsuarioProceso())) {
														nuevo.setControlador(2);
													} else {
														if (usuarioCargoAux != null
																&& usuarioCargoAux
																		.getCargo()
																		.getNivelCargo() == 10) {
															nuevo.setControlador(2);
														} else {
															nuevo.setControlador(2);
														}
													}
												} else if (nuevo
														.getCodigoEstado()
														.equals("DE")) {
													boolean evaluacion = evaluarFechaSolicitud(
															nuevo.getIdSolictud(),
															nuevo.getCodigoEstado());
													if (evaluacion) {
														sol.setEstado("NC");
														if ((sol.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.V1N
																		.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.V1C
																				.getNombre())) {
															try {
																LcredSolicitudVtapuntual venta = (LcredSolicitudVtapuntual) entityManager
																		.createQuery(
																				"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				sol.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (venta != null) {
																	venta.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudVentaPuntual(venta);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(venta);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de venta puntual");
															}
														} else if ((sol
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.LC1
																		.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.LC2
																				.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.LC3
																				.getNombre())) {
															try {
																LcredSolicitudLcredito credito = (LcredSolicitudLcredito) entityManager
																		.createQuery(
																				"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				sol.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (credito != null) {
																	credito.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudLineaCredito(credito);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(credito);
																	entityManager
																			.flush();
																}

															} catch (Exception e) {
																log.debug("no existe datos de linea credito.");
															}

														} else if ((sol
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.CR1
																		.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CR2
																				.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CRP3
																				.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.CRP4
																				.getNombre())) {

															try {
																LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones) entityManager
																		.createQuery(
																				"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																		.setParameter(
																				"solAux",
																				sol.getId()
																						.getNumSolicitud())
																		.getSingleResult();

																if (condicion != null) {
																	condicion
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudCondiciones(condicion);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(condicion);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de condiciones.");
															}
														} else if ((sol
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS1
																		.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.OS2
																				.getNombre())
																|| (sol.getTipTiposol()
																		.trim())
																		.equals(TipoSolicitudType.OS
																				.getNombre())) {
															/*****************
															 * Bloqueo y
															 * Desbloqueo
															 ***********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(sol
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de bloqueo o desbloqueo.");
															}
														} else if ((sol
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS3
																		.getNombre())) {
															/***************** Creacion DM ***********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(sol
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de creacion de dm.");
															}

														} else if ((sol
																.getTipTiposol()
																.trim())
																.equals(TipoSolicitudType.OS4
																		.getNombre())) {
															/********************* Prorroga ********************/
															try {
																LcredSolicitudOtras solicitudOtra = scoringService
																		.getSolicitudOtrasId(sol
																				.getId()
																				.getNumSolicitud());
																if (solicitudOtra != null) {
																	solicitudOtra
																			.setStatusSolicitud("NC");
																	scoringService
																			.mergerSolicitud(sol);
																	scoringService
																			.mergerSolicitudOtrasSolicitudes(solicitudOtra);
																	LcredEstado estado = obtenerObjetoLcredEstado(sol
																			.getEstado());
																	if (estado != null) {
																		nuevo.setCodigoEstado(estado
																				.getCodEstado());
																		nuevo.setEstado(estado
																				.getDesEstado());
																		nuevo.setControlador(2);
																	}
																	entityManager
																			.refresh(sol);
																	entityManager
																			.refresh(solicitudOtra);
																	entityManager
																			.flush();
																}
															} catch (Exception e) {
																log.debug("no existe datos de prorroga.");
															}
														}
													} else {
														nuevo.setControlador(3);
													}
												} else if (nuevo
														.getCodigoEstado()
														.equals("SA")
														|| nuevo.getCodigoEstado()
																.equals("B")) {
													if (nuevo
															.getUsuarioTomada()
															.equals(usuarioLogueado
																	.getAlias())) {
														nuevo.setControlador(2);
													} else {
														nuevo.setControlador(1);
													}
												} else if (nuevo
														.getCodigoEstado()
														.equals("N")
														|| nuevo.getCodigoEstado()
																.equals("NC")) {
													nuevo.setControlador(3);
												} else {
													nuevo.setControlador(3);
												}
											}
										} else {
											nuevo.setControlador(0);
										}
									}
								} else {
									nuevo.setControlador(0);
								}

								if (sol.getEstadoEvaluacion() != null) {
									Long numero = scoringService
											.getCantidadSolicitudUsuarioDerivacion(sol
													.getId().getNumSolicitud());
									if (numero != null
											&& numero.longValue() > 1) {
										nuevo.setEstadoEvaluacion(sol
												.getEstadoEvaluacion());
										nuevo.setHabilitaLista(true);
									} else {
										List<SolicitudUsuarioDerivada> lista = scoringService
												.getSolicitudUsuarioDerivacionForSolicitud(sol
														.getId()
														.getNumSolicitud());
										if (lista != null && lista.size() > 0) {
											SolicitudUsuarioDerivada obj = lista
													.get(0);
											if (obj != null) {
												nuevo.setUsuarioProceso(obj
														.getUsuario()
														.getAlias());
											}
										}
										nuevo.setHabilitaLista(false);
									}
								}

								if (listaUsuariosDerivadas != null
										&& listaUsuariosDerivadas.size() > 0) {
									if (!listaUsuariosDerivadas.contains((nuevo
											.getEmisor().toUpperCase()).trim())) {
										listaUsuariosDerivadas.add((nuevo
												.getEmisor().toUpperCase())
												.trim());
									}
								} else {
									listaUsuariosDerivadas.add((nuevo
											.getEmisor().toUpperCase()).trim());
								}

								if (listaTiposDerivadas != null
										&& listaTiposDerivadas.size() > 0) {
									if (!listaTiposDerivadas.contains((nuevo
											.getDespTipoSolictud()
											.toUpperCase()).trim())) {
										listaTiposDerivadas.add((nuevo
												.getDespTipoSolictud()
												.toUpperCase()).trim());
									}
								} else {
									listaTiposDerivadas.add((nuevo
											.getDespTipoSolictud()
											.toUpperCase()).trim());
								}

								if (listaEstadoDerivadas != null
										&& listaEstadoDerivadas.size() > 0) {
									if (!listaEstadoDerivadas.contains((nuevo
											.getEstado().toUpperCase()).trim())) {
										listaEstadoDerivadas.add((nuevo
												.getEstado().toUpperCase())
												.trim());
									}
								} else {
									listaEstadoDerivadas.add((nuevo.getEstado()
											.toUpperCase()).trim());
								}

								if (listaCanalDerivadas != null
										&& listaCanalDerivadas.size() > 0) {
									if (!listaCanalDerivadas.contains((nuevo
											.getCanal().toUpperCase()).trim())) {
										listaCanalDerivadas.add((nuevo
												.getCanal().toUpperCase())
												.trim());
									}
								} else {
									listaCanalDerivadas.add((nuevo.getCanal()
											.toUpperCase()).trim());
								}

								if (listaProcesoDerivadas != null
										&& listaProcesoDerivadas.size() > 0) {
									if (!listaProcesoDerivadas
											.contains((nuevo.getProceso()
													.toUpperCase()).trim())) {
										listaProcesoDerivadas.add((nuevo
												.getProceso().toUpperCase())
												.trim());
									}
								} else {
									listaProcesoDerivadas
											.add((nuevo.getProceso()
													.toUpperCase()).trim());
								}

								if (listaSucursalDerivadas != null
										&& listaSucursalDerivadas.size() > 0) {
									if (!listaSucursalDerivadas.contains((nuevo
											.getSucursal().toUpperCase())
											.trim())) {
										listaSucursalDerivadas.add((nuevo
												.getSucursal().toUpperCase())
												.trim());
									}
								} else {
									listaSucursalDerivadas.add((nuevo
											.getSucursal().toUpperCase())
											.trim());
								}

								listaSolicitudesDerivadasForOtros.add(nuevo);
								nuevo = null;
							}
						}
						if (listaSolicitudesDerivadasForOtros != null
								&& listaSolicitudesDerivadasForOtros.size() == 0) {
							listaSolicitudesDerivadasForOtros = null;
						}

						if (listaUsuariosDerivadas != null) {
							Collections.sort(listaUsuariosDerivadas);
						}
						if (listaSucursalDerivadas != null) {
							Collections.sort(listaSucursalDerivadas);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error, al sacar los solicitudes analizar #0",
					e.getMessage());
		}
	}

	public void sacarTodasLasSolicitudesDesdeActualizarComoPestana(
			String pestanaAux, String evalucion) {
		if (pestanaAux != null) {
			this.setNombrePestana(pestanaAux);
			log.debug(pestanaAux);
			sacarListasTodasSolicitudes(evalucion);
		} else {
			log.error("no tiene valor la variable pestanaAux");
		}

	}

	public boolean evaluarFechaSolicitud(Long idSolicitud, String codigoEstado) {
		boolean cambiar = false;
		try {
			Long numeroDias = scoringService.getSolicitudDiaReparoMaximo();
			SolicitudHitos hitoEstado = null;
			List<SolicitudHitos> listaSolicitudesHitos = scoringService
					.getSolicitudHitos(idSolicitud);
			if (listaSolicitudesHitos != null) {
				for (SolicitudHitos sh : listaSolicitudesHitos) {
					if (sh != null && sh.getCodigoEstado().equals(codigoEstado)) { // if(sh
																					// !=
																					// null
																					// &&
																					// sh.getCodigoEstado().equals(codigoEstado)){
						hitoEstado = sh;
						break;
					}
				}
			}

			/*
			 * recordatorio : 1 = Domingo, 2 = Lunes, 3 = Martes, 4 = Miercoles,
			 * 5 = Jueves, 6 = Viernes, 7 = Sabado
			 */
			if (hitoEstado != null && numeroDias != null) {
				// log.debug("fecha hito : #0, dias ha evaluar #1",
				// hitoEstado.getFechaHora(), numeroDias);

				Calendar cal = Calendar.getInstance();
				cal.setTime(hitoEstado.getFechaHora());
				// log.debug("Fecha hitos base de datos : #0",
				// formatoDeFecha.format(cal.getTime()));
				// log.debug("Calendar.DAY_OF_WEEK Hitos : #0",cal.get(Calendar.DAY_OF_WEEK));
				Long valorHito = cal.getTimeInMillis();
				cal.add(Calendar.DATE, numeroDias.intValue());
				// log.debug("Fecha hitos + dias : #0",
				// formatoDeFecha.format(cal.getTime()));
				// log.debug("Calendar.DAY_OF_WEEK Hitos + Dias: #0",cal.get(Calendar.DAY_OF_WEEK));

				Calendar calActual = Calendar.getInstance();
				calActual.setTime(new Date());
				// log.debug("Calendar.DAY_OF_WEEK Actual: #0",calActual.get(Calendar.DAY_OF_WEEK));
				// log.debug("Fecha Fecha Actual : #0",
				// formatoDeFecha.format(calActual.getTime()));

				/* evaluacion final */
				Long valor1 = cal.getTimeInMillis();
				Long valor2 = calActual.getTimeInMillis();
				// log.debug("valorHito: #0",valorHito);
				// log.debug("valor1 + dias : #0",valor1);
				// log.debug("valor2 Actual: #0",valor2);

				if (valor2 > valor1) {
					cambiar = true;
					log.debug("se cambia el estado DE pasarlo a NC");
				} else if ((valorHito <= valor2) && (valor2 <= valor1)) {
					log.debug("se habilitar link para reparar la solicitud");
					cambiar = false;
				} else {

				}
			}
		} catch (Exception e) {
			log.error("Error al evaluar de la fecha de devulucion #0",
					e.getMessage());
		}

		return cambiar;
	}

	SolicitudDTO filtro = new SolicitudDTO();

	public SolicitudDTO getFiltro() {
		return filtro;
	}

	public void insertarSolicitudDTO(LcredSolicitud solicitudAux) {
		Sucursal suc = null;
		try {
			if (solicitudAux != null) {

				SolicitudDTO nuevo = new SolicitudDTO();
				nuevo.setIdSolictud(solicitudAux.getId().getNumSolicitud());
				nuevo.setRut(solicitudAux.getRutCliente().replace("-", ""));
				nuevo.setRazonSocial(solicitudAux.getNomCliente());
				nuevo.setRazonSocialString(solicitudAux.getNomCliente());
				nuevo.setEmisor(solicitudAux.getCodEmisor());
				nuevo.setDespTipoSolictud(solicitudAux.getSubTiposol());
				nuevo.setFechaSalida(solicitudAux.getFecSalida());
				nuevo.setUsuarioProceso(solicitudAux.getUsuarioActual());
				nuevo.setTipoSolicitud(solicitudAux.getTipTiposol());
				nuevo.setCanal(solicitudAux.getCanal());
				nuevo.setCondicionPago(solicitudAux.getFormaPago());

				if (solicitudAux.getId().getFecSolicitud() != null
						&& solicitudAux.getHraSolicitud() != null) {

					/* rescatar la fecha */
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String fecha = sdf.format(solicitudAux.getId()
							.getFecSolicitud());

					/* rescatar la hora */
					SimpleDateFormat sdh = new SimpleDateFormat("HH:mm");
					String hora = sdh.format(solicitudAux.getHraSolicitud());

					/* fecha y hora */
					// log.debug(" Fecha String:" + fecha);
					// log.debug(" hora String :" + hora);

					DateFormat formatoFecha = new SimpleDateFormat(
							"dd/MM/yyyy HH:mm");
					try {
						Date d = formatoFecha.parse(fecha + " " + hora);
						nuevo.setFechaString(fecha + " " + hora);
						nuevo.setFechaEmision(d);
						nuevo.setHoraEmision(solicitudAux.getHraSolicitud());
					} catch (Exception e) {
						throw new Exception(e);
					}
				}

				if (solicitudAux.getEstado() != null) {
					LcredEstado estado = obtenerObjetoLcredEstado(solicitudAux
							.getEstado());
					if (estado != null) {
						nuevo.setCodigoEstado(estado.getCodEstado());
						nuevo.setEstado(estado.getDesEstado());
					}

					if (solicitudAux.getEstado().equals("I")) {
						LcredPerfiles perfil1 = scoringService
								.obtenerPerfil("1");
						nuevo.setProceso(perfil1.getDesPerfil());
					}
				}

				if (solicitudAux.getSucursalEmisor() != null) {
					// log.debug(" codigo de sucursal  :" +
					// sol.getSucursalEmisor());
					suc = obtenerObjetoSucursal(solicitudAux
							.getSucursalEmisor());
					if (suc != null) {
						nuevo.setSucursal(suc.getDescripcion());
					} else {
						nuevo.setSucursal(solicitudAux.getSucursalEmisor());
					}

				}

				if (solicitudAux.getMontoAsegurado() != null) {
					nuevo.setMontoAsegurado(solicitudAux.getMontoAsegurado()
							.doubleValue());
				} else {
					nuevo.setMontoAsegurado((double) 0);
				}

				if (solicitudAux.getMonto() != null) {
					nuevo.setMonto(solicitudAux.getMonto().doubleValue());
				} else {
					nuevo.setMonto((double) 0);
				}

				if (solicitudAux.getRiesgoKupfer() != null) {
					nuevo.setMontoRiegoKupfer(solicitudAux.getRiesgoKupfer()
							.doubleValue());
				} else {
					nuevo.setMontoRiegoKupfer((double) 0);
				}

				if (solicitudAux.getEvaluar() != null) {
					nuevo.setEvaluar(solicitudAux.getEvaluar());
				} else {
					nuevo.setEvaluar(false);
				}
				if (solicitudAux.getAnalizar() != null) {
					nuevo.setAnalizar(solicitudAux.getAnalizar());
				} else {
					nuevo.setAnalizar(false);
				}

				if (usuarioCargoAux != null
						&& !usuarioCargoAux.getCargo().getCodCargo()
								.equals("015")) {
					if (nuevo.getCodigoEstado() != null) {
						if (nuevo.getCodigoEstado().equals("I")) {
							nuevo.setControlador(1);
						} else if (nuevo.getCodigoEstado().equals("E")
								|| nuevo.getCodigoEstado().equals("N")) {
							if (lcredUsuarioNivelEnc.getId().getIdUsuario()
									.equals(solicitudAux.getUsuarioActual())) {
								nuevo.setControlador(1);
							} else {
								if (usuarioCargoAux != null
										&& usuarioCargoAux.getCargo()
												.getNivelCargo() == 10) {
									nuevo.setControlador(1);
								} else {
									nuevo.setControlador(2);
								}
							}
						} else if (!nuevo.getCodigoEstado().equals("I")
								&& !nuevo.getCodigoEstado().equals("E")
								&& !nuevo.getCodigoEstado().equals("N")) {
							nuevo.setControlador(1);
						} else {
							nuevo.setControlador(2);
						}
					}
				} else {
					nuevo.setControlador(2);
				}

				/* verificar si tiene mas un derivado o aprbadores */

				Long numero = scoringService
						.getCantidadSolicitudUsuarioDerivacion(solicitudAux
								.getId().getNumSolicitud());
				if (numero != null && numero.longValue() > 1) {
					nuevo.setHabilitaLista(true);
				} else {
					List<SolicitudUsuarioDerivada> lista = scoringService
							.getSolicitudUsuarioDerivacionForSolicitud(solicitudAux
									.getId().getNumSolicitud());
					if (lista != null && lista.size() > 0) {
						SolicitudUsuarioDerivada obj = lista.get(0);
						if (obj != null) {
							nuevo.setUsuarioProceso(obj.getUsuario().getAlias());
						}
					}
					nuevo.setHabilitaLista(false);
				}

				if (listaTodasSolicitudesDto != null) {
					listaTodasSolicitudesDto.add(0, nuevo);
				}
				if (listaSolicitudesPendientesAnalizarDto != null) {
					listaSolicitudesPendientesAnalizarDto.add(0, nuevo);
				}
				if (listaMisSolicitudesDto != null) {
					listaMisSolicitudesDto.add(0, nuevo);
				}
			}

		} catch (Exception e) {
			log.error("Error, al insertar los datos en la grillas #0",
					e.getMessage());
		}

	}

	public void sacarListaUsuarioDerivado(SolicitudDTO sol) {
		try {
			if (sol != null) {
				this.setSolicitudAux(sol);
				this.listUsuariosDerivados = scoringService
						.getSolicitudUsuarioDerivacionForSolicitudEstado(
								sol.getIdSolictud(), sol.getEstadoEvaluacion());
				if (this.listUsuariosDerivados != null) {
					log.debug("cantidad #0", this.listUsuariosDerivados.size());
				}
			}
		} catch (Exception e) {
			log.error(
					"Error, al sacar los usuarios que fueron derivado la solicitud, #0",
					e.getMessage());
		}
		return;
	}

	public void limpiarVariblesUsuarioDerevida() {
		this.listUsuariosDerivados = null;
		this.solicitudAux = null;

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
				this.setSolicitudAux(sol);
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

	public void sacarLogHitosSolicitudUsuarioDerivada(SolicitudHitos sol) {
		int contadorNoConfirmados = 0;
		List<SolicitudUsuarioDerivada> listaFinal = null;
		try {
			if (sol != null) {
				this.setSolicitudHitos(sol);
				List<SolicitudUsuarioDerivada> listaDerivadas = scoringService
						.getSolicitudUsuarioDerivacionForSolicitud(
								sol.getIdSolicitud(),
								sol.getCodEstadoDerivada());

				if (listaDerivadas != null && listaDerivadas.size() > 0) {
					listaFinal = new ArrayList<SolicitudUsuarioDerivada>();
					for (SolicitudUsuarioDerivada sud : listaDerivadas) {
						if (sud.getEstado() != null) {
							if ("AP".equals(sud.getEstado().getCodEstado())) {
								sud.setApruebaRechazo("Aprobar");
							} else if ("RP".equals(sud.getEstado()
									.getCodEstado())) {
								sud.setApruebaRechazo("Rechazar");
							}
						} else {
							sud.setApruebaRechazo(null);
						}
						listaFinal.add(sud);

						if (sud.getUsernameAutorizacion() == null) {
							contadorNoConfirmados++;
						}
					}
					if (listaFinal != null && listaFinal.size() > 0) {
						globalHitosLogdService
								.setListUsuariosDerivados(listaFinal);
					} else {
						globalHitosLogdService.setListUsuariosDerivados(null);
					}

					/*
					 * habilita el boton si hay algun usuario no confirmado sea
					 * obligatorio y no obligatorio.
					 */
					if (contadorNoConfirmados == 0) {
						this.habilitaBotonApruebaToRechazarAdmin = true;
					} else {
						this.habilitaBotonApruebaToRechazarAdmin = false;
					}
				} else {
					this.habilitaBotonApruebaToRechazarAdmin = true;
				}
			}

		} catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud",
					e.getMessage());
		}

	}

	public void limpiarLogHitosNuevo() {
		try {
			if (globalHitosLogdService != null) {
				globalHitosLogdService.limpiarHitos();
			}

		} catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud",
					e.getMessage());
		}

	}

	public boolean verificaCanal(TipoCuentasKupferType tipoCuenta) {
		boolean permiso = false;
		try {
			if (this.listaCanalUsuarioCargos != null) {
				for (CanalUsuarioCargo cuc : this.listaCanalUsuarioCargos) {
					if (cuc.getTipoCuenta().name().equals(tipoCuenta.name())) {
						permiso = true;
						break;
					}
				}

			}
		} catch (Exception e) {
			log.error("Error, al buscar el canal", e.getMessage());
		}
		return permiso;
	}

	public boolean verificaZonaSucursalNegocioConcepto(ConceptosType concepto,
			FuncionesType funcion, String codigo) {
		boolean permiso = false;
		List<ZonaSucursalNegocioConcepto> listaNueva = new ArrayList<ZonaSucursalNegocioConcepto>(
				0);
		try {

			if (this.listaZonaSucursalNegocioConceptos != null) {
				for (ZonaSucursalNegocioConcepto zsnc : this.listaZonaSucursalNegocioConceptos) {
					if (zsnc.getTipoConcepto().name().equals(concepto.name())
							&& zsnc.getPerfilFuncionCanal().getFuncion().name()
									.equals(funcion.name())) {
						listaNueva.add(zsnc);
					}
				}

				if (listaNueva != null) {
					for (ZonaSucursalNegocioConcepto cod : listaNueva) {
						if (cod.getCodigo().equals(codigo)) {
							permiso = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(
					"Error, al buscar el codigo de zona o sucursal o negocio o concepto.",
					e.getMessage());
		}
		return permiso;
	}

	public String calcularTiempoDelProceso(Calendar cal, Calendar cal2,
			LcredSolicitud solicitudAux) {
		String horaFinal = "";
		try {
			// conseguir la representacion de la fecha en milisegundos
			Long milis1 = cal.getTimeInMillis();
			Long milis2 = cal2.getTimeInMillis();
			// calcular la diferencia en milisengundos
			Long diff = ((milis2 - milis1) / 1000);

			int num, diffHours, diffMinutes, diffSeconds;
			num = diff.intValue();
			diffHours = num / 3600;
			diffMinutes = (num - (3600 * diffHours)) / 60;
			diffSeconds = num - ((diffHours * 3600) + (diffMinutes * 60));
			log.debug(diffHours + " horas ->" + diffMinutes + " minutos -> "
					+ diffSeconds + " segundos ->" + diff + " milissegundos");

			// calcular la diferencia en segundos
			// Double diffSeconds = ((diff.doubleValue() / 1000));
			// colocar hora
			// segundosahoras(diffSeconds.longValue());
			// calcular la diferencia en minutos
			// long diffMinutes = diffSeconds.longValue() / (60 * 1000);

			// calcular la diferencia en horas
			// long diffHours = diff / (60 * 60 * 1000);
			// calcular la diferencia en dias
			// long diffDays = diff / (24 * 60 * 60 * 1000);
			// log.debug("En milisegundos: " + diff + " milisegundos.");
			// log.debug("En segundos: " + diffSeconds + " segundos.");
			// log.debug("En minutos: " + diffMinutes + " minutos.");
			// log.debug("En horas: " + diffHours + " horas.");

			String horaResultado = diffHours + ":" + diffMinutes + ":"
					+ diffSeconds;
			log.debug("hora : minuto : segundo = #0", horaResultado);

			if (diffHours < 10) {
				horaFinal += "0" + diffHours + ":";
			} else {
				horaFinal += String.valueOf(diffHours) + ":";
			}

			if (diffMinutes < 10) {
				horaFinal += "0" + diffMinutes + ":";
			} else {
				horaFinal += String.valueOf(diffMinutes) + ":";
			}

			if (diffSeconds < 10) {
				horaFinal += "0" + diffSeconds;
			} else {
				horaFinal += String.valueOf(diffSeconds);
			}

			/* ingreso de accion final de cuando se segenero la solicitud */
			try {
				// Accion accion = entityManager.find(Accion.class,"5");
				// Long numero = scoringService.getSolicitudAccionMaximo();
				// SolicitudAccion solAccion = new SolicitudAccion();
				// solAccion.setSystemId(numero);
				// solAccion.setAccion(accion);
				// solAccion.setDiffHours(new Long(diffHours));
				// solAccion.setDiffMinutes(new Long(diffMinutes));
				// solAccion.setDiffSeconds(new Long(diffSeconds));
				// solAccion.setFechaInicial(cal.getTime());
				// solAccion.setFechaFinal(cal2.getTime());
				// solAccion.setUsuario(usuarioLogueado.getAlias());
				// solAccion.setIdSolicitud(solicitudAux.getId().getNumSolicitud());

			} catch (Exception e) {
				log.error(
						"error al ingresar la fecha y ahora  la solicitud #0",
						e.getMessage());
			}

			log.debug("HH: MM : SS : MS= #0", horaFinal);
		} catch (Exception e) {
			log.error("Error al calcular el proceso de tiempo : #0",
					e.getMessage());
		}

		return horaFinal;
	}

	public void segundosahoras(Long segundo) {
		int num, hor, min, seg;
		log.debug("ingrese los segundos ");
		num = segundo.intValue();
		hor = num / 3600;
		min = (num - (3600 * hor)) / 60;
		seg = num - ((hor * 3600) + (min * 60));
		log.debug(hor + "h " + min + "m " + seg + "s");
	}

	public String sacarCaracteresInvalidoParaWeb(String nombre) {
		try {
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

		} catch (Exception e) {
			log.error("Error, al sacar los caracteres de la palabra. #0",
					e.getMessage());
		}
		return nombre;

	}

	public void createToUpdateIndicadoresEconomicos() {
		log.debug("inicia proceso automatico de consultar indicadores economicos");
		List<IndicadoresDTO> listaIdicadores = new ArrayList<IndicadoresDTO>(0);
		Ufs uf = null;
		String dolar = null;
		String dolar_clp = null;
		String euro = null;
		String ufs = null;
		String utm = null;
		try {
			URL url = new URL(
					"http://indicadoresdeldia.cl/webservice/indicadores.xml");
			InputStream is = url.openConnection().getInputStream();
			try {
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory
						.newDocumentBuilder();
				Document doc = docBuilder.parse(is);

				doc.getDocumentElement().normalize();
				log.debug("El elemento raíz es "
						+ doc.getDocumentElement().getNodeName());

				/* moneda */
				NodeList listaMonedas = doc.getElementsByTagName("moneda");
				int totalMonedas = listaMonedas.getLength();
				log.debug("Número total de personas : " + totalMonedas);
				for (int i = 0; i < totalMonedas; i++) {
					Node moneda = listaMonedas.item(i);
					if (moneda.getNodeType() == Node.ELEMENT_NODE) {
						log.debug(Node.ELEMENT_NODE);
					}

					Element elemento = (Element) moneda;
					dolar = getTagValue("dolar", elemento);
					dolar = dolar.replace("$", "");
					dolar = dolar.replace(",", ".");
					log.debug("dolar  : " + dolar);

					IndicadoresDTO indDolar = new IndicadoresDTO();
					indDolar.setCodigo("Dolar Observado");
					indDolar.setValor(Double.parseDouble(dolar));
					listaIdicadores.add(indDolar);

					dolar_clp = getTagValue("dolar_clp", elemento);
					dolar_clp = dolar_clp.replace("$", "");
					dolar_clp = dolar_clp.replace(",", ".");
					log.debug("dolar_clp : " + dolar_clp);

					euro = getTagValue("euro", elemento);
					euro = euro.replace("$", "");
					euro = euro.replace(",", ".");
					log.debug("euro  : " + euro);
					IndicadoresDTO indEuro = new IndicadoresDTO();
					indEuro.setCodigo("Euro");
					indEuro.setValor(Double.parseDouble(euro));
					listaIdicadores.add(indEuro);
				}

				NodeList listaIndica = doc.getElementsByTagName("indicador");
				int totalIndica = listaIndica.getLength();
				log.debug("Número total de indicadores : " + totalIndica);
				for (int i = 0; i < totalIndica; i++) {
					Node indicador = listaIndica.item(i);
					if (indicador.getNodeType() == Node.ELEMENT_NODE) {
						log.debug(Node.ELEMENT_NODE);
					}

					Element elemento = (Element) indicador;
					ufs = getTagValue("uf", elemento);
					ufs = ufs.replace("$", "");
					ufs = ufs.replace(".", "");
					ufs = ufs.replace(",", ".");
					log.debug("ufs  : " + ufs);
					IndicadoresDTO indUf = new IndicadoresDTO();
					indUf.setCodigo("UF");
					indUf.setValor(Double.parseDouble(ufs));
					listaIdicadores.add(indUf);

					utm = getTagValue("utm", elemento);
					utm = utm.replace("$", "");
					utm = utm.replace(".", "");
					utm = utm.replace(",", ".");
					log.debug("utm  : " + utm);

					IndicadoresDTO indUtm = new IndicadoresDTO();
					indUtm.setCodigo("UTM");
					indUtm.setValor(Double.parseDouble(utm));
					listaIdicadores.add(indUtm);

				}
			} catch (Exception e) {
				log.error("no se puede parsear: ", e.getMessage());
			}
			if (listaIdicadores != null && listaIdicadores.size() > 0) {
				uf = new Ufs();
				uf.setFecha(new Date());
				for (IndicadoresDTO indicador : listaIdicadores) {
					if (indicador.getCodigo().equals("Euro")) {
						uf.setEuro(indicador.getValor());
					} else if (indicador.getCodigo().equals("Dolar Observado")) {
						uf.setDolar(indicador.getValor());
					} else if (indicador.getCodigo().equals("UF")) {
						uf.setValor(indicador.getValor());
					} else if (indicador.getCodigo().equals("UTM")) {
						uf.setUtm(indicador.getValor());
					}
				}
				boolean exito = scoringService.persistUfs(uf);
				if (exito) {
					log.debug("valor : #0", exito);
					return;
				} else {
					return;
				}
			}

		} catch (Exception e) {
			log.error("error al sacar los endicadores ");
		}

		String date = new Date().toString();

		log.debug("Proceso ejecutado hora indicada: " + date);
		log.debug("finaliza proceso automatico de consultar indicadores economicos");
		return;
	}

	public String getTagValue(String tag, Element elemento) {
		NodeList lista = elemento.getElementsByTagName(tag).item(0)
				.getChildNodes();
		Node valor = (Node) lista.item(0);
		return valor.getNodeValue();
	}

	public void insertarLogs(LcredSolicitud sol, int opcion) {
		/* ingreso de logs */
		try {
			if (opcion == 1) {/* solo esta revisando la solicitud */
				String codigo = "Z";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Revisando la solicitud. ");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Revisando la solicitud. ");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);
			} else if (opcion == 2) {/* aca se esta daando aprobaciones */
				String codigo = "A";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Se da aprobación la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Se da aprobación la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);

			} else if (opcion == 3) {/* aca se cambia elestado salir sin modifica */
				String codigo = "X";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Salir de la solicitud sin modificar.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Se da aprobación la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);

			} else if (opcion == 4) {/* aca se cambia elestado salir con modifica */
				String codigo = "Y";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Salir de la solicitud sin modificar.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Se da aprobación la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);

			} else if (opcion == 5) {
				String codigo = "O";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Ingresa de observaciones de  la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Ingresa de observaciones de  la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);
			} else if (opcion == 6) {
				String codigo = "D";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Devolución de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Devolución de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);
			} else if (opcion == 7) {
				String codigo = "N";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Anular de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Anular de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);
			} else if (opcion == 8) {
				String codigo = "A";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Aprobar de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Aprobar de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);
			} else if (opcion == 9) {
				String codigo = "K";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Análisis Ejecutivo de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Análisis Ejecutivo de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);
			} else if (opcion == 10) {
				String codigo = "Q";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Análisis Analista de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), String.valueOf(estadoLogs
									.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Análisis Analista de la solicitud.");
					exito = scoringService.persistSolicitudLogs(sol.getId()
							.getNumSolicitud(), usuarioSegur.getAlias(),
							new Date(), "", "", cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);
			}

		} catch (Exception e) {
			log.error("Error, al insertar el logs de la solicitud #0",
					e.getMessage());
		}
	}

	public String capitalizarTexto(String textoSinFormato) {
		if (textoSinFormato != null) {
			String[] palabras = textoSinFormato.split("\\s+");
			StringBuilder textoFormateado = new StringBuilder();

			for (String palabra : palabras) {
				textoFormateado.append(palabra
						.substring(0, 1)
						.toUpperCase()
						.concat(palabra.substring(1, palabra.length())
								.toLowerCase()).concat(" "));
			}
			return textoFormateado.toString();
		} else {
			return "";
		}
	}

	public void limpiarPedidoAndMensajeGlobal() {
		this.setPedidoCotizacionLiberar(null);
		this.setMensajeGlogal(null);
	}

	public void libararPedido() {
		try {
			if (this.pedidoCotizacionLiberar == null) {
				this.mensajeGlogal = "Debe ingresar el número de pedido y/o cotización a liberar.";
				return;
			}
			if(this.sociedadAux == null){
				this.mensajeGlogal = "Se debe seleccionar la sociedad para consultar.";
				return;
			}
			
			if (this.pedidoCotizacionLiberar != null && this.pedidoCotizacionLiberar.longValue() != 0) {

				/* liberar pedido en sap */
				try {
					/* cambia condicion de pago */
					JCoFunction functionDatosLiberar;
					SapSystem system = new SapSystem(
							globalParameters.getNameSap(),
							globalParameters.getIpSap(),
							globalParameters.getClientSap(),
							globalParameters.getSystemNumberSap(), "intranet",
							"informat");
					Connect connect = new Connect(system);
					functionDatosLiberar = connect.getFunction("ZSD_LIBERA_PEDIDO"); // Nombre RFC para liberar pedido
					log.debug(this.pedidoCotizacionLiberar);
					functionDatosLiberar.getImportParameterList().setValue("VBELN", this.pedidoCotizacionLiberar.longValue());// numero de  pedido
					connect.execute(functionDatosLiberar);
					String resultado = null;
					if (functionDatosLiberar != null) {
						resultado = (String) functionDatosLiberar.getExportParameterList().getValue("RETURN");
					}
					log.debug("resultado : " + resultado);
					if (resultado != null && "0".equals(resultado)) {
						this.mensajeGlogal = "Se libero el pedido y/o cotización número " + this.pedidoCotizacionLiberar.longValue();
					} else {
						this.mensajeGlogal = "El pedido y/o cotización número "	+ this.pedidoCotizacionLiberar.longValue() + ", nose libero.";
					}
					this.setPedidoCotizacionLiberar(null);
				} catch (Exception e) {
					log.debug("error al liberar el pedido revisar nuevamente #0",e.toString());
					this.mensajeGlogal = "Error, al liberar el pedido ingresado en la ejecución de SAP.";
					return;
				}
			} else {
				this.mensajeGlogal = "Debe ingresar el número de pedido y/o cotización a liberar.";
				return;
			}
		} catch (Exception e) {
			log.error("Error, al liberar el pedido numero "	+ this.pedidoCotizacionLiberar + " el error es #0 ", e.getMessage());
			this.mensajeGlogal = "Error, al liberar el pedido ingresado.";
			return;
		}
	}

	public void sacarEstado() {
		if (this.estadoPestTodas != null) {
			log.error(" el estado seleccionado #0",
					this.estadoPestTodas.getDesEstado());
		}
	}

	public void sacarSucursal() {
		if (this.sucursalPestTodas != null) {
			log.error(" la sucursal  seleccionado #0",
					this.sucursalPestTodas.getDescripcion());
		}
	}

	public void sacarSociedadPest() {
		if (this.sociedadPestTodas != null) {
			log.error(" la sociedad seleccionada #0",	this.sociedadPestTodas.getRazonSocial());
		}
	}
	
	public void limpiarPestanaTodas() {
		this.listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>(0);
		this.listaUsuarios = new ArrayList<String>(0);
		this.listaTiposSolicitudes = new ArrayList<String>(0);
		this.idSolicitudPestTodas = null;
		this.rutAuxPestTodas = null;
		this.fechaInicalPestTodas = null;
		this.fechaFinalPestTodas = null;
		this.sucursalPestTodas = null;
		this.estadoPestTodas = null;

	}

	public void consultarSolicitudes(String pestanaAux, String evalucion) {
		if (validarParametros()) {
			if (pestanaAux != null) {
				this.setNombrePestana(pestanaAux);
				log.debug(pestanaAux);
				sacarListasTodasSolicitudes(evalucion);
			} else {
				log.error("no tiene valor la variable pestanaAux");
			}
		} else {
			this.listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>(0);
		}
	}

	public boolean validarParametros() {
		try {
			if (this.idSolicitudPestTodas == null	&& (this.rutAuxPestTodas == null || "".equals(this.rutAuxPestTodas))
					&& this.fechaInicalPestTodas == null && this.fechaFinalPestTodas == null && this.estadoPestTodas == null 
					&& this.sucursalPestTodas == null && this.sociedadPestTodas == null) {

				FacesMessages.instance().add(Severity.INFO,
						"Debe ingresar por lo menos un criterio de busqueda.");
				return false;
			}

			if ("".equals(this.rutAuxPestTodas)) {
				this.setRutAuxPestTodas(null);
			}

			if (this.idSolicitudPestTodas != null) {
				if (this.idSolicitudPestTodas.longValue() != 0) {
					return true;
				}
			}
			if (this.getRutAuxPestTodas() != null) {
				if (!"".equals(this.getRutAuxPestTodas())) {
					// verificar que el rut no tenga menos de 8 caracteres
					if (this.getRutAuxPestTodas().length() < 8) {
						FacesMessages
								.instance()
								.add(Severity.ERROR,
										"Rut invalido, debe tener al menos 8 caracteres");
						return false;
					}
					// sacar el cero inicial del rut.
					this.setRutAuxPestTodas(globalService.sacarCeroRut(this
							.getRutAuxPestTodas()));

					// agregar guin en caso de ser necesario
					String rut = globalService.agregarGuionRut(this
							.getRutAuxPestTodas());
					log.debug("rut #0 #1", rut, globalService.validarRut(rut));
					this.rutAuxSinGuion = globalService.sacarGuionRut(rut);
					this.setRutAuxPestTodas(rut);
				} else {
					this.setRutAuxPestTodas(null);
				}
			}

			if (this.fechaInicalPestTodas == null
					&& this.fechaFinalPestTodas != null) {
				FacesMessages.instance().add(Severity.ERROR,
						"Debe seleccionar la fecha inicial.");
				return false;
			} else if (this.fechaInicalPestTodas != null
					&& this.fechaFinalPestTodas != null) {
				Calendar calInicial = Calendar.getInstance();
				calInicial.setTime(fechaInicalPestTodas);
				// log.debug("Fecha hitos base de datos : #0",
				// formatoDeFecha.format(cal.getTime()));
				// log.debug("Calendar.DAY_OF_WEEK Hitos : #0",cal.get(Calendar.DAY_OF_WEEK));
				Long valorInicial = calInicial.getTimeInMillis();
				// log.debug("Fecha hitos + dias : #0",
				// formatoDeFecha.format(cal.getTime()));
				// log.debug("Calendar.DAY_OF_WEEK Hitos + Dias: #0",cal.get(Calendar.DAY_OF_WEEK));

				Calendar calFinal = Calendar.getInstance();
				calFinal.setTime(this.fechaFinalPestTodas);
				// log.debug("Calendar.DAY_OF_WEEK Actual: #0",calActual.get(Calendar.DAY_OF_WEEK));
				// log.debug("Fecha Fecha Actual : #0",
				// formatoDeFecha.format(calActual.getTime()));

				/* evaluacion final */
				Long valorFinal = calFinal.getTimeInMillis();
				// log.debug("valorHito: #0",valorHito);
				// log.debug("valor1 + dias : #0",valor1);
				// log.debug("valor2 Actual: #0",valor2);

				if (valorFinal < valorInicial) {
					FacesMessages.instance().add(Severity.ERROR,
							"Fecha Final debe ser mayor que la Fecha Final.");
					return false;
				}

			}

		} catch (Exception e) {
			log.error("Error, al validar los parametros #0 ", e.getMessage());
		}
		return true;
	}

	public void aprobarToRechazarPorAdministrador(Long idSolicitud,
			List<SolicitudUsuarioDerivada> listaDerivados) {
		boolean exitoSud = false;
		boolean mensaje = true;
		int verificadosObligados = 0;
		int cantVeriObligadosAprobado = 0;
		int cantVeriObligadosRechazado = 0;
		boolean cambioEstadosolicitud = false;
		LcredSolicitud solicitudAdmin = null;
		int codigoTipoSolicitud = 0;
		log.debug("el numero de la solicitud es : #0", idSolicitud);
		log.debug("cometarios de autizado #0", this.observacionesAutirzador);
		if (idSolicitud != null) {
			/* se saca la solicitud para actualizar si marca todos */
			try {
				solicitudAdmin = (LcredSolicitud) entityManager
						.createQuery(
								"select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud")
						.setParameter("numSolicitud", idSolicitud)
						.getSingleResult();

				if ((solicitudAdmin.getTipTiposol().trim())
						.equals(TipoSolicitudType.V1N.getNombre())
						|| (solicitudAdmin.getTipTiposol().trim())
								.equals(TipoSolicitudType.V1C.getNombre())) {
					if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.V1N.getNombre())) {
						codigoTipoSolicitud = 11;
					} else if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.V1C.getNombre())) {
						codigoTipoSolicitud = 12;
					}

				} else if ((solicitudAdmin.getTipTiposol().trim())
						.equals(TipoSolicitudType.LC1.getNombre())
						|| (solicitudAdmin.getTipTiposol().trim())
								.equals(TipoSolicitudType.LC2.getNombre())
						|| (solicitudAdmin.getTipTiposol().trim())
								.equals(TipoSolicitudType.LC3.getNombre())) {
					if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC1.getNombre())) {
						codigoTipoSolicitud = 21;
					} else if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC2.getNombre())) {
						codigoTipoSolicitud = 22;
					} else if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC3.getNombre())) {
						codigoTipoSolicitud = 23;
					}
				} else if ((solicitudAdmin.getTipTiposol().trim())
						.equals(TipoSolicitudType.CR1.getNombre())
						|| (solicitudAdmin.getTipTiposol().trim())
								.equals(TipoSolicitudType.CR2.getNombre())
						|| (solicitudAdmin.getTipTiposol().trim())
								.equals(TipoSolicitudType.CRP3.getNombre())
						|| (solicitudAdmin.getTipTiposol().trim())
								.equals(TipoSolicitudType.CRP4.getNombre())) {
					codigoTipoSolicitud = 31;

				} else if ((solicitudAdmin.getTipTiposol().trim())
						.equals(TipoSolicitudType.OS1.getNombre())
						|| (solicitudAdmin.getTipTiposol().trim())
								.equals(TipoSolicitudType.OS2.getNombre())
						|| (solicitudAdmin.getTipTiposol().trim())
								.equals(TipoSolicitudType.OS.getNombre())) {
					/***************** Bloqueo y Desbloqueo ***********************/
					codigoTipoSolicitud = 41;

				} else if ((solicitudAdmin.getTipTiposol().trim())
						.equals(TipoSolicitudType.OS3.getNombre())) {
					/***************** Creacion DM ***********************/
					codigoTipoSolicitud = 43;
				} else if ((solicitudAdmin.getTipTiposol().trim())
						.equals(TipoSolicitudType.OS4.getNombre())) {
					/********************* Prorroga ********************/
					codigoTipoSolicitud = 44;
				}

			} catch (NoResultException e) {
				solicitudAdmin = null;
				mensaje = false;
			}

			if (listaDerivados != null && solicitudAdmin != null) {
				try {
					log.debug("revisar la lista de se setiaron los seleccionado");
					for (SolicitudUsuarioDerivada sud : listaDerivados) {
						if (sud.getConfirmacion() == null) {
							log.debug("muestra #0", sud.getApruebaRechazo());
							if (sud.getApruebaRechazo() != null
									&& !"".equals(sud.getApruebaRechazo())) {

								/*
								 * se debe tomar la la instancia del objeto de
								 * derivado
								 */
								LcredEstado estadoAux = null;
								if ("Aprobar".equals(sud.getApruebaRechazo())) {
									estadoAux = scoringService
											.obtenerEstado("AP");
								} else if ("Rechazar".equals(sud
										.getApruebaRechazo())) {
									estadoAux = scoringService
											.obtenerEstado("RP");
								}

								if (estadoAux != null) {
									if (sud != null) {
										/* actualiza la derivacion del usuario */
										sud.setConfirmacion(true);
										sud.setEstado(estadoAux);
										sud.setUsernameAutorizacion(usuarioLogueado
												.getAlias());
										exitoSud = scoringService
												.mergerSolicitudUsuarioDerivacion(sud);
										log.debug(exitoSud);
										if (exitoSud) {
											/*
											 * insertar un hito desde la
											 * actualizacion de administrador
											 */
											Usuariosegur usuarioHito = sud
													.getUsuario();
											if (usuarioHito != null) {
												insertHitos(solicitudAdmin,
														estadoAux, "NADA", 0,
														usuarioHito);

												LcredSolicitudObservacionesId id = new LcredSolicitudObservacionesId();
												Long correlativo = scoringService
														.obtenerCorrelativoObservaciones(solicitudAdmin
																.getId()
																.getNumSolicitud());
												if (correlativo != null) {
													id.setCorrelativo(correlativo);
													id.setFecha(new Date());
													id.setHora(new Date());
													id.setNumSolicitud(solicitudAdmin
															.getId()
															.getNumSolicitud());
													id.setTipoSol(String
															.valueOf(codigoTipoSolicitud));
													id.setObservacion(this.observacionesAutirzador);
													id.setUsuario(usuarioLogueado
															.getAlias());
													LcredSolicitudObservaciones obser = new LcredSolicitudObservaciones();
													obser.setId(id);
													scoringService
															.persistSolicitudObservaciones(obser);
													insertarLogs(
															solicitudAdmin, 5);
												}
											}
										}
									}
								}
							}

						}
					}

				} catch (Exception e) {
					log.error(
							"error, al actualizar las asiganciones seleccionadas #0",
							e.getMessage());
					mensaje = false;
				}

				/*
				 * verificar si los cambio a todos cambiar el estado de la
				 * solicitud
				 */
				List<SolicitudUsuarioDerivada> listaVerificacion = scoringService
						.getSolicitudUsuarioDerivacionForSolicitudEstado(
								solicitudAdmin.getId().getNumSolicitud(),
								solicitudAdmin.getEstadoEvaluacion());
				if (listaVerificacion != null) {
					/* verificar cantidad de usuarios obligatorios debe aprobar */
					for (SolicitudUsuarioDerivada sudAux : listaVerificacion) {
						if (sudAux.getConfirmacionObligatoria() == true) {
							verificadosObligados++;
						}
					}
					/*
					 * verificar la cantidad de aprobados, rechazados, la
					 * cantidad de usuarios
					 */
					for (SolicitudUsuarioDerivada sudAux2 : listaVerificacion) {
						if (sudAux2.getEstado().getCodEstado().equals("AP")) {
							if (sudAux2.getConfirmacion()) {
								if (sudAux2.getConfirmacionObligatoria()) {
									cantVeriObligadosAprobado++;
								}
							}
						} else if (sudAux2.getEstado().getCodEstado()
								.equals("RP")) {
							if (sudAux2.getConfirmacion()) {
								if (sudAux2.getConfirmacionObligatoria()) {
									cantVeriObligadosRechazado++;
								}
							}
						}
					}
				}

				if (verificadosObligados == (cantVeriObligadosAprobado + cantVeriObligadosRechazado)) {
					cambioEstadosolicitud = true;
				}

				if (cambioEstadosolicitud) {
					if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.V1N.getNombre())
							|| (solicitudAdmin.getTipTiposol().trim())
									.equals(TipoSolicitudType.V1C.getNombre())) {
						try {
							venta = (LcredSolicitudVtapuntual) entityManager
									.createQuery(
											"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
									.setParameter(
											"solAux",
											solicitudAdmin.getId()
													.getNumSolicitud())
									.getSingleResult();
							if (venta != null) {
								solicitudAdmin.setEstado("DR");
								solicitudAdmin.setAnalizar(true);
								venta.setStatusSolicitud("DR");
								boolean exitoUno = scoringService
										.mergerSolicitud(solicitudAdmin);
								boolean exitoDos = scoringService
										.mergerSolicitudVentaPuntual(venta);
								entityManager.refresh(solicitudAdmin);
								entityManager.refresh(venta);
								log.debug("se actualiza solicitud #0", exitoUno);
								log.debug("se actualiza venta #0", exitoDos);
							}
						} catch (Exception e) {
							log.debug("no existe datos de venta puntual");
							mensaje = false;
						}

					} else if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC1.getNombre())
							|| (solicitudAdmin.getTipTiposol().trim())
									.equals(TipoSolicitudType.LC2.getNombre())
							|| (solicitudAdmin.getTipTiposol().trim())
									.equals(TipoSolicitudType.LC3.getNombre())) {
						try {
							credito = (LcredSolicitudLcredito) entityManager
									.createQuery(
											"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
									.setParameter(
											"solAux",
											solicitudAdmin.getId()
													.getNumSolicitud())
									.getSingleResult();
							if (credito != null) {
								solicitudAdmin.setEstado("DR");
								solicitudAdmin.setAnalizar(true);
								credito.setStatusSolicitud("DR");
								boolean exitoUno = scoringService
										.mergerSolicitud(solicitudAdmin);
								boolean exitoDos = scoringService
										.mergerSolicitudLineaCredito(credito);
								entityManager.refresh(solicitudAdmin);
								entityManager.refresh(credito);
								log.debug("se actualiza solicitud #0", exitoUno);
								log.debug("se actualiza credito #0", exitoDos);

							}
						} catch (Exception e) {
							log.debug("no existe datos de linea credito.");
							mensaje = false;
						}
					} else if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.CR1.getNombre())
							|| (solicitudAdmin.getTipTiposol().trim())
									.equals(TipoSolicitudType.CR2.getNombre())
							|| (solicitudAdmin.getTipTiposol().trim())
									.equals(TipoSolicitudType.CRP3.getNombre())
							|| (solicitudAdmin.getTipTiposol().trim())
									.equals(TipoSolicitudType.CRP4.getNombre())) {

						try {
							condicion = (LcredSolicitudCondiciones) entityManager
									.createQuery(
											"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
									.setParameter(
											"solAux",
											solicitudAdmin.getId()
													.getNumSolicitud())
									.getSingleResult();
							if (condicion != null) {
								solicitudAdmin.setEstado("DR");
								solicitudAdmin.setAnalizar(true);
								condicion.setStatusSolicitud("DR");
								boolean exitoUno = scoringService
										.mergerSolicitud(solicitudAdmin);
								boolean exitoDos = scoringService
										.mergerSolicitudCondiciones(condicion);
								entityManager.refresh(solicitudAdmin);
								entityManager.refresh(condicion);
								log.debug("se actualiza solicitud #0", exitoUno);
								log.debug("se actualiza condicion #0", exitoDos);
							}
						} catch (Exception e) {
							log.debug("no existe datos de condiciones.");
						}

					} else if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS1.getNombre())
							|| (solicitudAdmin.getTipTiposol().trim())
									.equals(TipoSolicitudType.OS2.getNombre())
							|| (solicitudAdmin.getTipTiposol().trim())
									.equals(TipoSolicitudType.OS.getNombre())) {
						/***************** Bloqueo y Desbloqueo ***********************/

						try {
							solicitudOtra = scoringService
									.getSolicitudOtrasId(solicitudAdmin.getId()
											.getNumSolicitud());
							if (solicitudOtra != null) {
								solicitudAdmin.setEstado("DR");
								solicitudAdmin.setAnalizar(true);
								solicitudOtra.setStatusSolicitud("DR");
								boolean exitoUno = scoringService
										.mergerSolicitud(solicitudAdmin);
								boolean exitoDos = scoringService
										.mergerSolicitudOtrasSolicitudes(solicitudOtra);
								entityManager.refresh(solicitudAdmin);
								entityManager.refresh(solicitudOtra);
								log.debug("se actualiza solicitud #0", exitoUno);
								log.debug(
										"se actualiza bloque o desbloqueo #0",
										exitoDos);
							}

						} catch (Exception e) {
							log.debug("no existe datos de la tablas de otras solicitudes.");
							mensaje = false;
						}

					} else if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS3.getNombre())) {
						/***************** Creacion DM ***********************/
						try {
							solicitudOtra = scoringService
									.getSolicitudOtrasId(solicitudAdmin.getId()
											.getNumSolicitud());
							if (solicitudOtra != null) {
								solicitudAdmin.setEstado("DR");
								solicitudAdmin.setAnalizar(true);
								solicitudOtra.setStatusSolicitud("DR");
								boolean exitoUno = scoringService
										.mergerSolicitud(solicitudAdmin);
								boolean exitoDos = scoringService
										.mergerSolicitudOtrasSolicitudes(solicitudOtra);
								entityManager.refresh(solicitudAdmin);
								entityManager.refresh(solicitudOtra);
								log.debug("se actualiza solicitud #0", exitoUno);
								log.debug("se actualiza creacion de DM #0",
										exitoDos);
							}

						} catch (Exception e) {
							log.debug("no existe datos de la tablas de otras solicitudes.");
							mensaje = false;
						}
					} else if ((solicitudAdmin.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS4.getNombre())) {
						/********************* Prorroga ********************/
						try {
							solicitudOtra = scoringService
									.getSolicitudOtrasId(solicitudAdmin.getId()
											.getNumSolicitud());
							if (solicitudOtra != null) {
								solicitudAdmin.setEstado("DR");
								solicitudAdmin.setAnalizar(true);
								solicitudOtra.setStatusSolicitud("DR");
								boolean exitoUno = scoringService
										.mergerSolicitud(solicitudAdmin);
								boolean exitoDos = scoringService
										.mergerSolicitudOtrasSolicitudes(solicitudOtra);
								entityManager.refresh(solicitudAdmin);
								entityManager.refresh(solicitudOtra);
								log.debug("se actualiza solicitud #0", exitoUno);
								log.debug("se actualiza prorrogas #0", exitoDos);
							}

						} catch (Exception e) {
							log.debug("no existe datos de la tablas de otras solicitudes.");
							mensaje = false;
						}
					}
				}

				if (this.solicitudAux != null) {
					sacarLogHitos(this.solicitudAux);
				}

				if (mensaje) {
					this.mensajeGlogal = "Se realizaron las actualizaciones de las asignaciones de la solicitud.";
					this.observacionesAutirzador = null;
				} else {
					this.mensajeGlogal = "Se produjo un problema en la actualización en alguna asignación";
				}
			}
		}
	}

	public Date DeStringADate(String fecha) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String strFecha = fecha;
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(strFecha);
			log.debug(fechaDate.toString());
			return fechaDate;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return fechaDate;
		}
	}

	public void insertHitos(LcredSolicitud solAux, LcredEstado estadoAux,
			String accion, int cantidad, Usuariosegur usuarioAux) {
		try {
			SolicitudHitos hitos = new SolicitudHitos();
			if ("NADA".equals(accion)) {
				hitos.setEmisor("");
				hitos.setUsuarioActual(usuarioAux.getAlias());
				hitos.setFechaHora(new Date());
				hitos.setCodigoEstado(estadoAux.getCodEstado());
				hitos.setDescripcionEstado(estadoAux.getDesEstado());
				hitos.setIdSolicitud(solAux.getId().getNumSolicitud());
				hitos.setUsuarioActual(usuarioAux.getAlias());
				hitos.setAccionAdministrador(estadoAux.getDesEstado()
						+ " por el Administrador.");
			}
			try {
				long numero = scoringService.obtenerNumeroHitoToLog(1);
				hitos.setSystemId(numero);
				boolean exito = scoringService.persistSolicitudHitos(hitos);
				log.debug("verificacion si inserto registro hitos #0", exito);

			} catch (Exception e) {
				log.error("Error, al insertar el hitos de la solicitud #0",
						e.getMessage());
			}

		} catch (Exception e) {
			log.error("Error, al insertar los hitos de respuesta.",
					e.getMessage());
		}

	}

	public String sacarNegociosDeGrillaEspecialVP() {
		StringBuffer cadenaFinalAux = new StringBuffer();
		List<String> listaInicial = new ArrayList<String>(0);
		try {
			if (this.listaCotPedDTOs != null && this.listaCotPedDTOs.size() > 0) {
				for (CotPedDTO encabezado : listaCotPedDTOs) {
					for (CabeceraCotPedDTO subCabecera : encabezado
							.getListaCabeceraCotPeds()) {
						if (!listaInicial.contains(subCabecera.getNegocio()
								.getNegocio().trim())) {
							listaInicial.add(subCabecera.getNegocio()
									.getNegocio().trim());
						}
					}
				}
			}

			if (listaInicial.size() > 0) {
				int cantidad = listaInicial.size();
				int contador = 0;
				for (String negocio : listaInicial) {
					if ((contador + 1) == cantidad) {
						cadenaFinalAux.append(negocio + "");
					} else {
						cadenaFinalAux.append(negocio + ",");
					}
					contador++;
				}
			}

		} catch (Exception e) {
			log.error(
					"Error, al sacar los negocios de la grilla generada por los pedidos y cotizaciones. #0",
					e.getMessage());
		}

		return cadenaFinalAux.toString();
	}

	public String sacarConceptoDeGrillaEspecialVP() {
		StringBuffer cadenaFinalAux = new StringBuffer();
		List<String> listaInicial = new ArrayList<String>(0);
		try {
			if (this.listaCotPedDTOs != null && this.listaCotPedDTOs.size() > 0) {
				for (CotPedDTO encabezado : listaCotPedDTOs) {
					for (CabeceraCotPedDTO subCabecera : encabezado
							.getListaCabeceraCotPeds()) {
						for (DetalleCotPedDTO subsubCabecera : subCabecera
								.getListaDetalle()) {
							if (!listaInicial.contains(subsubCabecera
									.getNegocio().getConcepto().trim())) {
								listaInicial.add(subsubCabecera.getNegocio()
										.getConcepto().trim());
							}
						}
					}
				}
			}

			if (listaInicial.size() > 0) {
				int cantidad = listaInicial.size();
				int contador = 0;
				for (String negocio : listaInicial) {
					if ((contador + 1) == cantidad) {
						cadenaFinalAux.append(negocio + "");
					} else {
						cadenaFinalAux.append(negocio + ",");
					}
					contador++;
				}
			}
		} catch (Exception e) {
			log.error(
					"Error, al sacar los negocios de la grilla generada por los pedidos y cotizaciones. #0",
					e.getMessage());
		}

		return cadenaFinalAux.toString();
	}

	/* gets y sets */

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

	public void setListaProductosagregados(
			List<ConceptoNegocioDTO> listaProductosagregados) {
		this.listaProductosagregados = listaProductosagregados;
	}

	public List<String> getListaDescripcionProductos() {
		return listaDescripcionProductos;
	}

	public void setListaDescripcionProductos(
			List<String> listaDescripcionProductos) {
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

	public void setListaUsuarioCorreos(
			List<UsuarioCorreoDTO> listaUsuarioCorreos) {
		this.listaUsuarioCorreos = listaUsuarioCorreos;
	}

	public String getUsuarioCorreo() {
		return usuarioCorreo;
	}

	public void setUsuarioCorreo(String usuarioCorreo) {
		this.usuarioCorreo = usuarioCorreo;
	}

	public List<UsuarioCorreoDTO> getListaUsuarioCorreoagregados() {

		return listaUsuarioCorreoagregados;
	}

	public void setListaUsuarioCorreoagregados(
			List<UsuarioCorreoDTO> listaUsuarioCorreoagregados) {
		this.listaUsuarioCorreoagregados = listaUsuarioCorreoagregados;
	}

	public List<FileUploadedDTO> getListaFileUploadedDTOs() {
		return listaFileUploadedDTOs;
	}

	public void setListaFileUploadedDTOs(
			List<FileUploadedDTO> listaFileUploadedDTOs) {
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

	public void setListaDoctoProrroga(
			List<DocumentoProrrogaDTO> listaDoctoProrroga) {
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

	public void setListaConceptosMontos(
			List<ConceptoMontoDTO> listaConceptosMontos) {
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

	public void setLcredUsuarioNivelEnc(
			LcredUsuarioNivelEnc lcredUsuarioNivelEnc) {
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

	@BypassInterceptors
	public Long getPedidoCotizacion() {
		return pedidoCotizacion;
	}

	@BypassInterceptors
	public void setPedidoCotizacion(Long pedidoCotizacion) {
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

	public void setListaCabeceraCotPeds(
			List<CabeceraCotPedDTO> listaCabeceraCotPeds) {
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

	@BypassInterceptors
	public String getMotivoCompra() {
		return motivoCompra;
	}

	@BypassInterceptors
	public void setMotivoCompra(String motivoCompra) {
		this.motivoCompra = motivoCompra;
	}

	@BypassInterceptors
	public String getObservaciones() {
		return observaciones;
	}

	@BypassInterceptors
	public void setObservaciones(String observaciones) {
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

	@BypassInterceptors
	public Date getFechaActualCtaCte() {
		return fechaActualCtaCte;
	}

	@BypassInterceptors
	public void setFechaActualCtaCte(Date fechaActualCtaCte) {
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

	@BypassInterceptors
	public ConceptosNegocio getConceptoNegocioCbo() {
		return conceptoNegocioCbo;
	}

	@BypassInterceptors
	public void setConceptoNegocioCbo(ConceptosNegocio conceptoNegocioCbo) {
		this.conceptoNegocioCbo = conceptoNegocioCbo;
	}

	@BypassInterceptors
	public String getSocio() {
		return socio;
	}

	@BypassInterceptors
	public void setSocio(String socio) {
		this.socio = socio;
	}

	public List<String> getListaSocios() {
		return listaSocios;
	}

	public void setListaSocios(List<String> listaSocios) {
		this.listaSocios = listaSocios;
	}

	@BypassInterceptors
	public String getRutSocio() {
		return rutSocio;
	}

	@BypassInterceptors
	public void setRutSocio(String rutSocio) {
		this.rutSocio = rutSocio;
	}

	@BypassInterceptors
	public String getMotivoRiesgoPago() {
		return motivoRiesgoPago;
	}

	@BypassInterceptors
	public void setMotivoRiesgoPago(String motivoRiesgoPago) {
		this.motivoRiesgoPago = motivoRiesgoPago;
	}

	public boolean isSalirDelMensaje() {
		return salirDelMensaje;
	}

	public void setSalirDelMensaje(boolean salirDelMensaje) {
		this.salirDelMensaje = salirDelMensaje;
	}

	@BypassInterceptors
	public String getMotivoBloDesbloqueo() {
		return motivoBloDesbloqueo;
	}

	@BypassInterceptors
	public void setMotivoBloDesbloqueo(String motivoBloDesbloqueo) {
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

	@BypassInterceptors
	public Long getMontoConcepto() {
		return montoConcepto;
	}

	@BypassInterceptors
	public void setMontoConcepto(Long montoConcepto) {
		this.montoConcepto = montoConcepto;
	}

	public String getTituloGeneral() {
		return tituloGeneral;
	}

	public void setTituloGeneral(String tituloGeneral) {
		this.tituloGeneral = tituloGeneral;
	}

	@BypassInterceptors
	public String getOpcionBloqueoDesbloqueo() {
		return opcionBloqueoDesbloqueo;
	}

	@BypassInterceptors
	public void setOpcionBloqueoDesbloqueo(String opcionBloqueoDesbloqueo) {
		this.opcionBloqueoDesbloqueo = opcionBloqueoDesbloqueo;
	}

	public String getMensajeCreacionDm() {
		return mensajeCreacionDm;
	}

	public void setMensajeCreacionDm(String mensajeCreacionDm) {
		this.mensajeCreacionDm = mensajeCreacionDm;
	}

	@BypassInterceptors
	public String getMotivoProrroga() {
		return motivoProrroga;
	}

	@BypassInterceptors
	public void setMotivoProrroga(String motivoProrroga) {
		this.motivoProrroga = motivoProrroga;
	}

	public List<SolicitudEstadoDTO> getListaSolicitudesEstados() {
		return listaSolicitudesEstados;
	}

	public void setListaSolicitudesEstados(
			List<SolicitudEstadoDTO> listaSolicitudesEstados) {
		this.listaSolicitudesEstados = listaSolicitudesEstados;
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

	public void setListaMisSolicitudesDto(
			List<SolicitudDTO> listaMisSolicitudesDto) {
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

	public List<ConceptosNegocio> getListaCboConceptoNegocioSeleccion() {
		return listaCboConceptoNegocioSeleccion;
	}

	public void setListaCboConceptoNegocioSeleccion(
			List<ConceptosNegocio> listaCboConceptoNegocioSeleccion) {
		this.listaCboConceptoNegocioSeleccion = listaCboConceptoNegocioSeleccion;
	}

	public Long getRiesgoKupferCalculado() {
		return riesgoKupferCalculado;
	}

	public void setRiesgoKupferCalculado(Long riesgoKupferCalculado) {
		this.riesgoKupferCalculado = riesgoKupferCalculado;
	}

	private String tsListaAnalista;
	private String tsListaAprobacion;
	private String tsListaMisSolicitudes;
	private String tsListaTodasSolicitudes;
	private String tsListaDerivadasSolicitudes;

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

	public boolean isHabilitar() {
		return habilitar;
	}

	public void setHabilitar(boolean habilitar) {
		this.habilitar = habilitar;
	}

	public String getNombrePestana() {
		return nombrePestana;
	}

	public void setNombrePestana(String nombrePestana) {
		this.nombrePestana = nombrePestana;
	}

	public List<SolicitudUsuarioDerivada> getListUsuariosDerivados() {
		return listUsuariosDerivados;
	}

	public void setListUsuariosDerivados(
			List<SolicitudUsuarioDerivada> listUsuariosDerivados) {
		this.listUsuariosDerivados = listUsuariosDerivados;
	}

	public SolicitudDTO getSolicitudAux() {
		return solicitudAux;
	}

	public void setSolicitudAux(SolicitudDTO solicitudAux) {
		this.solicitudAux = solicitudAux;
	}

	public List<SolicitudDTO> getListaSolicitudesDerivadasForOtros() {
		return listaSolicitudesDerivadasForOtros;
	}

	public void setListaSolicitudesDerivadasForOtros(
			List<SolicitudDTO> listaSolicitudesDerivadasForOtros) {
		this.listaSolicitudesDerivadasForOtros = listaSolicitudesDerivadasForOtros;
	}

	public String getTsListaDerivadasSolicitudes() {
		return tsListaDerivadasSolicitudes;
	}

	public void setTsListaDerivadasSolicitudes(
			String tsListaDerivadasSolicitudes) {
		this.tsListaDerivadasSolicitudes = tsListaDerivadasSolicitudes;
	}

	public boolean isPestanaPendientes() {
		return pestanaPendientes;
	}

	public void setPestanaPendientes(boolean pestanaPendientes) {
		this.pestanaPendientes = pestanaPendientes;
	}

	public boolean isPestanaMisSolicitudes() {
		return pestanaMisSolicitudes;
	}

	public void setPestanaMisSolicitudes(boolean pestanaMisSolicitudes) {
		this.pestanaMisSolicitudes = pestanaMisSolicitudes;
	}

	public boolean isPestanaTodas() {
		return pestanaTodas;
	}

	public void setPestanaTodas(boolean pestanaTodas) {
		this.pestanaTodas = pestanaTodas;
	}

	public boolean isPestanaDerivadas() {
		return pestanaDerivadas;
	}

	public void setPestanaDerivadas(boolean pestanaDerivadas) {
		this.pestanaDerivadas = pestanaDerivadas;
	}

	public SolicitudHitos getSolicitudHitos() {
		return solicitudHitos;
	}

	public void setSolicitudHitos(SolicitudHitos solicitudHitos) {
		this.solicitudHitos = solicitudHitos;
	}

	@BypassInterceptors
	public Long getPedidoCotizacionLiberar() {
		return pedidoCotizacionLiberar;
	}

	@BypassInterceptors
	public void setPedidoCotizacionLiberar(Long pedidoCotizacionLiberar) {
		this.pedidoCotizacionLiberar = pedidoCotizacionLiberar;
	}

	/***************************************************** Inicio solicitud en base a modelo ******************************************************************/

	@RequestParameter("rut")
	String rut;

	@RequestParameter("idSolicitud")
	Long idSolicitud;

	@RequestParameter("tipoSolicitud")
	String tipoSolicitudCargada;

	/********** metodo extraido desde reparo ************************/

	private LcredSolicitudVtapuntual venta = null;
	private LcredSolicitudLcredito credito = null;
	private LcredSolicitudCondiciones condicion = null;
	private LcredSolicitudBloqueos bloqueo = null;
	private List<LcredSolicitudDm> listaDms = null;
	private List<LcredSolicitudProrroga> listaProrrogas = null;
	private LcredSolicitudOtras solicitudOtra = null;
	private SolicitudTipoFormaPago stfp;
	private String paginaVolver;
	private Long solicitudSeleccionadaPlantilla;

	public String getPaginaVolver() {
		return paginaVolver;
	}

	public void setPaginaVolver(String paginaVolver) {
		this.paginaVolver = paginaVolver;
	}

	public Long getSolicitudSeleccionadaPlantilla() {
		return solicitudSeleccionadaPlantilla;
	}

	public void setSolicitudSeleccionadaPlantilla(
			Long solicitudSeleccionadaPlantilla) {
		this.solicitudSeleccionadaPlantilla = solicitudSeleccionadaPlantilla;
	}

	/*********************** Desde reparos *******************/
	@SuppressWarnings("unchecked")
	public void ingresarPaginaPrincipal() {
		List<Sucursal> listaSucur = null;
		try {

			listaSucur = (List<Sucursal>) entityManager.createQuery(
					"select suc from Sucursal suc ").getResultList();
			setHabilitarCtaCte(true);
			setSucursal(null);
			sacarEstadosSolicitudes();
			listaCondicionPagos = intranetSapService.getCondicionPago();
			if (usuarioSegur != null) {
				usuarioCargoAux = scoringService.getUsuarioCargo(usuarioSegur
						.getIdPersonal());
				if (usuarioCargoAux != null) {
					listaCanalUsuarioCargos = scoringService
							.getListaCanalUsuarioCargo(usuarioCargoAux
									.getIdUsuarioCargo());
					if (listaCanalUsuarioCargos != null) {
						long codigo = 0;
						for (CanalUsuarioCargo cuc : listaCanalUsuarioCargos) {
							listaPerfilFuncionCanals = scoringService
									.getListaPerfilFuncionCanal(
											cuc.getIdCanalUsuarioCargo(),
											listaPerfilFuncionCanals);
							if (listaPerfilFuncionCanals != null) {
								for (PerfilFuncionCanal pfc : listaPerfilFuncionCanals) {
									if (codigo != pfc.getIdPerfilFuncionCanal()) {
										listaZonaSucursalNegocioConceptos = scoringService
												.getListaZonaSucursalNegocioConcepto(
														pfc.getIdPerfilFuncionCanal(),
														listaZonaSucursalNegocioConceptos);
										codigo = pfc.getIdPerfilFuncionCanal();
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error, obtener la lista de sucursales. #0",
					e.getMessage());
		}

		if (usuarioSegur != null) {
			Sucursal suc = scoringService.obtenerSucursalForCodigo(usuarioSegur
					.getCodigosucursal());
			if (suc != null) {
				this.setSucursal(suc);
			}
		}

		/* sacar concepto y negocio */
		setListaCboConceptoNegocio(scoringService.obtenerConceptosNegocios());

		if (this.monto == null) {
			this.monto = new Long(0);
		}
		if (this.pie == null) {
			this.pie = new Long(0);
		}
		if (this.pedidoCotizacion == null) {
			this.pedidoCotizacion = new Long(0);
		}

		if (listaSucur != null) {
			setListaSucursales(listaSucur);
		}

		limpiarTiempoEjecuciones();

		this.muestraSeleccionSolicitud = true;

		generarMenu();

		try {
			if (idSolicitud != null) {
				solicitud = (LcredSolicitud) entityManager
						.createQuery(
								"select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud")
						.setParameter("numSolicitud", idSolicitud)
						.getSingleResult();

				if (solicitud != null) {

					if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.V1N.getNombre())) {
						this.setPaginaIngreso(11);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.V1C.getNombre())) {
						this.setPaginaIngreso(12);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC1.getNombre())) {
						this.setPaginaIngreso(21);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC2.getNombre())) {
						this.setPaginaIngreso(22);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC3.getNombre())) {
						this.setPaginaIngreso(23);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.CR1.getNombre())
							|| (solicitud.getTipTiposol().trim())
									.equals(TipoSolicitudType.CR2.getNombre())
							|| (solicitud.getTipTiposol().trim())
									.equals(TipoSolicitudType.CRP3.getNombre())
							|| (solicitud.getTipTiposol().trim())
									.equals(TipoSolicitudType.CRP4.getNombre())) {
						this.setPaginaIngreso(23);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS1.getNombre())) {
						this.setPaginaIngreso(41);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS2.getNombre())) {
						this.setPaginaIngreso(42);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS.getNombre())) {
						this.setOpcionBloqueoDesbloqueo("");
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS3.getNombre())) {
						this.setPaginaIngreso(43);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS4.getNombre())) {
						this.setPaginaIngreso(44);
					}

					/* limpiar variables por cvada cambio de pestaña */
					limpiarSegunTipoSolictud(this.paginaIngreso);
					setSolicitudSeleccionadaPlantilla(solicitud.getId()
							.getNumSolicitud());
					evaludar(solicitud);
				}
			}

		} catch (Exception e) {
			log.error("Error, al sacar la solicitud seleccionada #0",
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void evaludar(LcredSolicitud solicitud) {
		try {
			if (solicitud != null) {
				this.setRutAux(solicitud.getRutCliente());
				obtenerInformacionCliente();
				this.listaFileUploadedDTOs = new ArrayList<FileUploadedDTO>(0);

				log.debug("solicitud.getTipTiposol() : #0",
						solicitud.getTipTiposol());
				if ((solicitud.getTipTiposol().trim())
						.equals(TipoSolicitudType.V1N.getNombre())
						|| (solicitud.getTipTiposol().trim())
								.equals(TipoSolicitudType.V1C.getNombre())) {

					try {
						venta = (LcredSolicitudVtapuntual) entityManager
								.createQuery(
										"Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
								.setParameter("solAux",
										solicitud.getId().getNumSolicitud())
								.getSingleResult();
					} catch (Exception e) {
						log.debug("no existe datos de venta puntual");
					}

					this.setTituloIngreso(solicitud.getDesTiposol());

					if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.V1N.getNombre())) {
						this.setPaginaIngreso(11);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.V1C.getNombre())) {
						this.setPaginaIngreso(12);
					}

					if (venta != null) {
						this.tipoSolicitud = scoringService
								.obtenerObjetoLcredTipoSolicitud(String
										.valueOf(this.paginaIngreso));
						if (this.tipoSolicitud != null) {
							setTituloIngreso(this.tipoSolicitud
									.getDesTipoSolicitud());
							setTituloGeneral("Ventas");
						}
						this.setMonto(venta.getMontoInicial().longValue());
						this.setPie(venta.getMontoPieInicial().longValue());

						if (this.paginaIngreso == 11) {
							this.stfp = scoringService
									.getSolicitudTipoFormaPago(venta
											.getNumSolicitud());
							if (this.stfp != null) {
								this.setTipoFormaPago(this.stfp
										.getTipoFormaPago());
								try {
									listaTipoFormaPagos = (List<TipoFormaPago>) entityManager
											.createQuery(
													"select tfp from TipoFormaPago tfp ")
											.getResultList();
									if (this.tipoFormaPago != null
											&& listaTipoFormaPagos != null) {
										obtenerFormaPagos();
									}
								} catch (Exception e) {
									log.error(
											"Error, de tipo de forma de pagos #0",
											e.getMessage());
								}
								this.setEncabezado(this.stfp.getEncabezado());
							}
						} else if (this.paginaIngreso == 12) {
							this.stfp = scoringService
									.getSolicitudTipoFormaPago(venta
											.getNumSolicitud());
							if (this.stfp != null) {
								this.setTipoFormaPago(this.stfp
										.getTipoFormaPago());
								try {
									listaTipoFormaPagos = (List<TipoFormaPago>) entityManager
											.createQuery(
													"select tfp from TipoFormaPago tfp where tfp.idTipoFormaPago not in (2) ")
											.getResultList();
									if (this.tipoFormaPago != null
											&& listaTipoFormaPagos != null) {
										obtenerFormaPagos();
									}
								} catch (Exception e) {
									log.error(
											"Error, de tipo de forma de pagos #0",
											e.getMessage());
								}
								this.setEncabezado(this.stfp.getEncabezado());
							} else {
								try {
									listaTipoFormaPagos = (List<TipoFormaPago>) entityManager
											.createQuery(
													"select tfp from TipoFormaPago tfp where tfp.idTipoFormaPago not in (2) ")
											.getResultList();
								} catch (Exception e) {
									log.error(
											"Error, de tipo de forma de pagos #0",
											e.getMessage());
								}
							}
						}

						this.setMonto(venta.getMontoInicial().longValue());
						this.setPie(venta.getMontoPieInicial().longValue());
						this.setMotivoCompra(venta.getDcMotivo());
						this.setObservaciones(venta.getObservacionesFinal());
						try {
							String margenAux = venta.getDcMargen().replace(",",
									".");
							String margennuevo = margenAux.replace("%", "");
							Double margen = Double.parseDouble(margennuevo);
							this.setMargenGlobal(margen);
						} catch (Exception e) {
							log.error(
									"Error al transformar el margen global #0",
									e.getMessage());
							this.setMargenGlobal((double) 0);
						}

					}
					/* sacar los archivos existente en la generacion */
					List<ArchivoAdjunto> listaArchivos = scoringService
							.obtenerArchivosSolicitud(solicitud.getId()
									.getNumSolicitud(),
									ArchivoAdjuntoType.INGRESO);
					if (listaArchivos != null) {
						FileUploadedDTO obj = null;
						for (ArchivoAdjunto aa : listaArchivos) {
							obj = new FileUploadedDTO();
							obj.setNombreArchivo(aa.getNombreAdjunto());
							obj.setRutaArchivo(globalParameters
									.getAttachedFilesAbsolutePath()
									+ ""
									+ aa.getUrl());
							obj.setTipo(this.tipoSolicitud);
							obj.setEliminar(false);
							this.listaFileUploadedDTOs.add(obj);
						}
					}

					/* sacar los datps de representacion la grilla nuevamente. */
					List<CotizacionPedidoCabecera> listaCpc = scoringService
							.getCotizacionPedidoCobecera(venta
									.getNumSolicitud());
					if (listaCpc != null && listaCpc.size() > 0) {
						this.listaCotPedDTOs = new ArrayList<CotPedDTO>(0);
						List<CabeceraCotPedDTO> listaCabeceraCotPeds = null;
						List<DetalleCotPedDTO> listaDetalle = null;
						CotPedDTO primero = null;
						CabeceraCotPedDTO segundo = null;
						DetalleCotPedDTO tercero = null;
						for (CotizacionPedidoCabecera cpc : listaCpc) {
							primero = new CotPedDTO();
							primero.setMontoNeto(cpc.getNeto());
							primero.setMontoTotal(cpc.getTotal());
							primero.setPonderacion(cpc.getPorcentaje());

							montoTotal += cpc.getTotal();
							montoTotalNeto += cpc.getNeto();
							List<CotizacionPedido> listaCp = scoringService
									.getCotizacionPedido(cpc.getSystemId());
							if (listaCp != null && listaCp.size() > 0) {
								for (CotizacionPedido cp : listaCp) {
									primero.setNumeroCotizacion(String
											.valueOf(cp.getNCotiPed()));
									primero.setMargenGlobal(cp
											.getMargenGlobal());
									List<CotizacionPedidoNegocio> listaCpn = scoringService
											.getCotizacionPedidoNegocio(cp
													.getSystemId());
									if (listaCpn != null && listaCpn.size() > 0) {
										listaCabeceraCotPeds = new ArrayList<CabeceraCotPedDTO>(
												0);
										for (CotizacionPedidoNegocio cpn : listaCpn) {
											segundo = new CabeceraCotPedDTO();
											ConceptosNegocio cn = scoringService
													.obtenerConceptoNegocioLocalForCodigoNegocio(cpn
															.getNegocio());
											segundo.setNegocio(cn);
											segundo.setMontoGeneral((long) 0);
											segundo.setPorcentaje(cpn
													.getMargenNegocio());
											segundo.setRepresentacion(cpn
													.getRepresentacion());
											List<CotizacionPedidoConcepto> listaCpCp = scoringService
													.getCotizacionPedidoConcepto(cpn
															.getSystemId());
											if (listaCpCp != null
													&& listaCpCp.size() > 0) {
												listaDetalle = new ArrayList<DetalleCotPedDTO>(
														0);
												for (CotizacionPedidoConcepto cpcp : listaCpCp) {
													tercero = new DetalleCotPedDTO();
													ConceptosNegocio cn2 = scoringService
															.obtenerConceptoNegocioLocalForNegocioAndConcepto(
																	cpn.getNegocio(),
																	cpcp.getCodigoConcepto());
													tercero.setNegocio(cn2);
													tercero.setMargenConcepto(cpcp
															.getMargenConcepto());
													tercero.setMonto(cpcp
															.getMontoMasIva());
													tercero.setMontoNeto((long) (cpcp
															.getMontoMasIva() / 1.19));
													tercero.setPorceDelTotal(cpcp
															.getMargenPoderado());
													if (cpcp.getMargenPorcePorConcepto() != null) {
														tercero.setPorcePorConcepto(cpcp
																.getMargenPorcePorConcepto());
													} else {
														tercero.setPorcePorConcepto(cpcp
																.getMargenConcepto());
													}
													List<DetalleCp> listaDcp = scoringService
															.getDetalleCp(cpcp
																	.getSystemId());
													if (listaDcp != null
															&& listaDcp.size() > 0) {
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
					/* va a buscar los destinatarios de las solictudes */
					SolicitudUsuarioCorreo nuevo = scoringService
							.getSolicitudUsuarioCorreo(solicitud
									.getUsuarioDevuelve());
					if (nuevo != null) {
						if (listaUsuarioCorreoagregados == null)
							listaUsuarioCorreoagregados = new ArrayList<UsuarioCorreoDTO>(
									0);

						UsuarioCorreoDTO otro = new UsuarioCorreoDTO();
						otro.setCorreoElectronico(nuevo.getCorreo());
						otro.setNombreUsuario(nuevo.getNombre());
						listaUsuarioCorreoagregados.add(otro);
					}

				} else if ((solicitud.getTipTiposol().trim())
						.equals(TipoSolicitudType.LC1.getNombre())
						|| (solicitud.getTipTiposol().trim())
								.equals(TipoSolicitudType.LC2.getNombre())
						|| (solicitud.getTipTiposol().trim())
								.equals(TipoSolicitudType.LC3.getNombre())) {

					this.setTituloIngreso(solicitud.getDesTiposol());
					try {
						credito = (LcredSolicitudLcredito) entityManager
								.createQuery(
										"Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
								.setParameter("solAux",
										solicitud.getId().getNumSolicitud())
								.getSingleResult();
					} catch (Exception e) {
						log.debug("no existe datos de linea credito.");
					}

					this.setTituloIngreso(solicitud.getDesTiposol());
					if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC1.getNombre())) {
						this.setPaginaIngreso(21);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC2.getNombre())) {
						this.setPaginaIngreso(22);
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.LC3.getNombre())) {
						this.setPaginaIngreso(23);
					}

					// this.setInnNomRiesgo(String.valueOf(this.paginaIngreso));

					List<ArchivoAdjunto> listaArchivos = scoringService
							.obtenerArchivosSolicitud(solicitud.getId()
									.getNumSolicitud(),
									ArchivoAdjuntoType.INGRESO);
					if (listaArchivos != null) {
						ConceptoDTO obj = null;
						// listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
						for (ArchivoAdjunto aa : listaArchivos) {
							obj = new ConceptoDTO();
							obj.setNombreArchivo(aa.getNombreAdjunto());
							obj.setRutaCompleta(globalParameters
									.getAttachedFilesAbsolutePath()
									+ ""
									+ aa.getUrl());
							// listaArchivoGuardado.add(obj);
						}
					}

					this.tipoSolicitud = scoringService
							.obtenerObjetoLcredTipoSolicitud(String
									.valueOf(this.paginaIngreso));

					/* sacando condiciones de riesgo y de pago */
					try {
						if (listaClsRiesgos == null
								|| listaClsRiesgos.size() == 0) {
							listaClsRiesgos = intranetSapService
									.getClasificacionRiesgo();
						}
						if (listaCondicionPagos == null
								|| listaCondicionPagos.size() == 0) {
							listaCondicionPagos = intranetSapService
									.getCondicionPago();
						}
					} catch (Exception e) {
						log.error(
								"Error, al obtener los datos de lista risgo y pago #0",
								e.getMessage());
					}

					if (credito != null) {
						if (credito.getCondPagoInicial() != null
								&& credito.getCondRiesgoInicial() != null) {
							evaluarInnominalNominalRKupfer(
									credito.getCondRiesgoInicial(),
									credito.getCondPagoInicial());
						}
						this.setMonto(solicitud.getMonto().longValue());
						this.setDescripcionProyecto(credito
								.getDpDescripcionProyecto());
						String montoAux = credito.getDpMonto().replace(".", "");
						this.setMontoCredito(Long.parseLong(montoAux));
						this.setPlazoEjecucion(credito.getDpPlazoEjecucion());
						this.setConceptoNegocios(credito
								.getDpConceptosInvoluc());
						this.setRutNombresSocios(credito.getDpSocios());
						String montoPotencial = credito.getDpPotencialCompra()
								.replace(".", "");
						this.setPotencialCompra(Long.parseLong(montoPotencial));
						this.setObservacionesCredito(credito
								.getObservacionesInicial());

						StringBuffer cadenaNegocios = new StringBuffer();
						StringBuffer cadenaConceptos = new StringBuffer();
						List<String> listaNegocios = new ArrayList<String>(0);
						List<String> listaConceptos = new ArrayList<String>(0);

						List<SolicitudConceptosNegocioLC> lista = scoringService
								.getSolicitudConceptosNegocioLC(credito
										.getNumSolicitud());
						if (lista != null) {
							listaSelConceptoNegocio = new ArrayList<ConceptoNegocioDTO>();
							ConceptoNegocioDTO conceptoNegocio = null;
							for (SolicitudConceptosNegocioLC scn : lista) {
								conceptoNegocio = new ConceptoNegocioDTO();
								conceptoNegocio.setConceptoNegocio(scn
										.getConceptosNegocio());
								conceptoNegocio.setMonto(scn.getMonto());
								listaSelConceptoNegocio.add(conceptoNegocio);
								conceptoNegocio = null;

								if (!listaNegocios.contains(scn
										.getConceptosNegocio().getNegocio())) {
									listaNegocios.add(scn.getConceptosNegocio()
											.getNegocio());
									cadenaNegocios.append(scn
											.getConceptosNegocio().getNegocio()
											+ ",");
								}

								if (!listaConceptos.contains(scn
										.getConceptosNegocio().getConcepto())) {
									listaConceptos.add(scn
											.getConceptosNegocio()
											.getConcepto());
									cadenaConceptos.append(scn
											.getConceptosNegocio()
											.getConcepto()
											+ ",");
								}
							}
						}
						obtenerMontoCredito(cadenaNegocios.toString(),
								cadenaConceptos.toString());

						listaSocios = new ArrayList<String>(0);
						if (credito.getDpSocios() != null) {
							String[] slitSocios = credito.getDpSocios().split(
									",");
							if (slitSocios != null) {
								for (String socio : slitSocios) {
									listaSocios.add(socio);
								}
							}
						}
					}

				} else if ((solicitud.getTipTiposol().trim())
						.equals(TipoSolicitudType.CR1.getNombre())
						|| (solicitud.getTipTiposol().trim())
								.equals(TipoSolicitudType.CR2.getNombre())
						|| (solicitud.getTipTiposol().trim())
								.equals(TipoSolicitudType.CRP3.getNombre())
						|| (solicitud.getTipTiposol().trim())
								.equals(TipoSolicitudType.CRP4.getNombre())) {

					this.setTituloIngreso(solicitud.getDesTiposol());

					this.comboCPago = true;
					this.comboCRiesgo = true;
					try {
						condicion = (LcredSolicitudCondiciones) entityManager
								.createQuery(
										"Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
								.setParameter("solAux",
										solicitud.getId().getNumSolicitud())
								.getSingleResult();
					} catch (Exception e) {
						log.debug("no existe datos de condiciones.");
					}
					List<ArchivoAdjunto> listaArchivos = scoringService
							.obtenerArchivosSolicitud(solicitud.getId()
									.getNumSolicitud(),
									ArchivoAdjuntoType.INGRESO);
					if (listaArchivos != null) {
						ConceptoDTO obj = null;
						// listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
						for (ArchivoAdjunto aa : listaArchivos) {
							obj = new ConceptoDTO();
							obj.setNombreArchivo(aa.getNombreAdjunto());
							obj.setRutaCompleta(globalParameters
									.getAttachedFilesAbsolutePath()
									+ ""
									+ aa.getUrl());
							// listaArchivoGuardado.add(obj);
						}
					}

					if (condicion != null) {
						setPaginaIngreso(31);
						/* sacando condiciones de riesgo y de pago */
						try {
							if (listaClsRiesgos == null
									|| listaClsRiesgos.size() == 0) {
								listaClsRiesgos = intranetSapService
										.getClasificacionRiesgo();
							}
							if (listaCondicionPagos == null
									|| listaCondicionPagos.size() == 0) {
								listaCondicionPagos = intranetSapService
										.getCondicionPago();
							}
						} catch (Exception e) {
							log.error(
									"Error, al obtener los datos de lista risgo y pago #0",
									e.getMessage());
						}

						if (condicion.getCodCondPagoInicial() != null
								&& condicion.getCodCondRiesgoInicial() != null) {
							evaluarInnominalNominalRKupfer(
									condicion.getCodCondRiesgoInicial(),
									condicion.getCodCondPagoInicial());
						}

						if ((condicion.getCondRiesgoInicial() != null)
								&& (condicion.getCondRiesgoInicial().toString())
										.equals("S")) {
							this.condicionRiesgo = true;
							evaluarInnominalNominalRKupfer(
									condicion.getCodCondRiesgoInicial(),
									condicion.getCodCondPagoInicial());
						}
						if ((condicion.getCondPagoInicial() != null)
								&& (condicion.getCondPagoInicial().toString())
										.equals("S")) {
							this.condicionPago = true;
							evaluarInnominalNominalRKupfer(
									condicion.getCodCondRiesgoInicial(),
									condicion.getCodCondPagoInicial());
						}

						if (condicion.getMotivoCambio() != null) {
							this.setMotivoRiesgoPago(condicion
									.getMotivoCambio());
						}

					}

					this.tipoSolicitud = scoringService
							.obtenerObjetoLcredTipoSolicitud(String
									.valueOf(this.paginaIngreso));

					/* va a buscar los destinatarios de las solictudes */
					// obtenerCorreo(solicitud.getCodEmisor(), "3");

					// this.modificarCondicion = false;

				} else if ((solicitud.getTipTiposol().trim())
						.equals(TipoSolicitudType.OS1.getNombre())
						|| (solicitud.getTipTiposol().trim())
								.equals(TipoSolicitudType.OS2.getNombre())
						|| (solicitud.getTipTiposol().trim())
								.equals(TipoSolicitudType.OS.getNombre())) {
					/***************** Bloqueo y Desbloqueo ***********************/

					try {
						bloqueo = (LcredSolicitudBloqueos) entityManager
								.createQuery(
										"Select sb from LcredSolicitudBloqueos sb where sb.numSolicitud=:solAux")
								.setParameter("solAux",
										solicitud.getId().getNumSolicitud())
								.getSingleResult();
					} catch (Exception e) {
						log.debug("no existe datos de bloqueo o desbloqueo.");
					}

					solicitudOtra = scoringService
							.getSolicitudOtrasId(solicitud.getId()
									.getNumSolicitud());

					this.setTituloIngreso(solicitud.getDesTiposol());

					if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS1.getNombre())) {
						this.setOpcionBloqueoDesbloqueo("41");
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS2.getNombre())) {
						this.setOpcionBloqueoDesbloqueo("42");
					} else if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS.getNombre())) {
						this.setOpcionBloqueoDesbloqueo("");
					}

					if (solicitudOtra != null && bloqueo == null) {
						this.setPaginaIngreso(41);
						this.setMotivoBloDesbloqueo(solicitudOtra
								.getObservacionesInicial());
					} else if (solicitudOtra == null && bloqueo != null) {
						this.setPaginaIngreso(41);
						this.setMotivoBloDesbloqueo(bloqueo
								.getMotivoBloqDesbloq());
					} else if (solicitudOtra != null && bloqueo != null) {
						this.setPaginaIngreso(41);
						this.setMotivoBloDesbloqueo(solicitudOtra
								.getObservacionesInicial());
					}

					List<ArchivoAdjunto> listaArchivos = scoringService
							.obtenerArchivosSolicitud(solicitud.getId()
									.getNumSolicitud(),
									ArchivoAdjuntoType.INGRESO);
					if (listaArchivos != null) {
						ConceptoDTO obj = null;
						// listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
						for (ArchivoAdjunto aa : listaArchivos) {
							obj = new ConceptoDTO();
							obj.setNombreArchivo(aa.getNombreAdjunto());
							obj.setRutaCompleta(globalParameters
									.getAttachedFilesAbsolutePath()
									+ ""
									+ aa.getUrl());
							// listaArchivoGuardado.add(obj);
						}
					}

					this.tipoSolicitud = scoringService
							.obtenerObjetoLcredTipoSolicitud(String
									.valueOf(this.paginaIngreso));

					/* va a buscar los destinatarios de las solictudes */
					// obtenerCorreo(solicitud.getCodEmisor(), "5");

					// this.modificarbloqueoToDesbloqueo = false;

				} else if ((solicitud.getTipTiposol().trim())
						.equals(TipoSolicitudType.OS3.getNombre())) {
					/***************** Creacion DM ***********************/
					try {
						listaDms = scoringService.getSacarListaDms(solicitud
								.getId().getNumSolicitud());
						if (listaDms != null) {
							this.setListaClienteDMs(listaDms);
						}
					} catch (Exception e) {
						log.debug("no existe datos de los dm. #0",
								e.getMessage());
					}

					solicitudOtra = scoringService
							.getSolicitudOtrasId(solicitud.getId()
									.getNumSolicitud());
					if (solicitudOtra != null) {
						log.debug("Existe resgistro en otra solicitud ");
					}

					this.setTituloIngreso(solicitud.getDesTiposol());

					if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS3.getNombre())) {
						this.setPaginaIngreso(43);
						this.setMotivoProrroga(solicitud.getObservSolicitud());
					}
					List<ArchivoAdjunto> listaArchivos = scoringService
							.obtenerArchivosSolicitud(solicitud.getId()
									.getNumSolicitud(),
									ArchivoAdjuntoType.INGRESO);
					if (listaArchivos != null) {
						ConceptoDTO obj = null;
						// listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
						for (ArchivoAdjunto aa : listaArchivos) {
							obj = new ConceptoDTO();
							obj.setNombreArchivo(aa.getNombreAdjunto());
							obj.setRutaCompleta(globalParameters
									.getAttachedFilesAbsolutePath()
									+ ""
									+ aa.getUrl());
							// listaArchivoGuardado.add(obj);
						}
					}

					this.tipoSolicitud = scoringService
							.obtenerObjetoLcredTipoSolicitud(String
									.valueOf(this.paginaIngreso));

					/* va a buscar los destinatarios de las solictudes */
					// obtenerCorreo(solicitud.getCodEmisor(), "5");

					// this.modificarCreacionDM = false;

				} else if ((solicitud.getTipTiposol().trim())
						.equals(TipoSolicitudType.OS4.getNombre())) {
					/********************* Prorroga ********************/
					try {
						listaProrrogas = (List<LcredSolicitudProrroga>) entityManager
								.createQuery(
										"Select lp from LcredSolicitudProrroga lp where lp.id.numSolicitud=:solAux")
								.setParameter("solAux",
										solicitud.getId().getNumSolicitud())
								.getResultList();
					} catch (Exception e) {
						log.debug("no existe datos de las prorrogas dm.");
					}

					solicitudOtra = scoringService
							.getSolicitudOtrasId(solicitud.getId()
									.getNumSolicitud());
					if (solicitudOtra != null) {
						this.setMotivoProrroga(solicitudOtra
								.getObservacionesInicial());
					}

					this.setTituloIngreso(solicitud.getDesTiposol());
					if ((solicitud.getTipTiposol().trim())
							.equals(TipoSolicitudType.OS4.getNombre())) {
						this.setPaginaIngreso(44);
					}
					List<ArchivoAdjunto> listaArchivos = scoringService
							.obtenerArchivosSolicitud(solicitud.getId()
									.getNumSolicitud(),
									ArchivoAdjuntoType.INGRESO);
					if (listaArchivos != null) {
						ConceptoDTO obj = null;
						// listaArchivoGuardado = new ArrayList<ConceptoDTO>(0);
						for (ArchivoAdjunto aa : listaArchivos) {
							obj = new ConceptoDTO();
							obj.setNombreArchivo(aa.getNombreAdjunto());
							obj.setRutaCompleta(globalParameters
									.getAttachedFilesAbsolutePath()
									+ ""
									+ aa.getUrl());
							// listaArchivoGuardado.add(obj);
						}
					}

					this.tipoSolicitud = scoringService
							.obtenerObjetoLcredTipoSolicitud(String
									.valueOf(this.paginaIngreso));

					/* va a buscar los destinatarios de las solictudes */
					// obtenerCorreo(solicitud.getCodEmisor(), "5");

				}
			}

		} catch (Exception e) {
			log.error("Error, al eveluar la solictud #0", e.getMessage());

		}
	}

	public void evaluarInnominalNominalRKupfer(String codigoClsRiesgo,
			String codigoCdnPago) {
		setClsRiesgo(null);
		setcPago(null);
		listaStringClsRiesgos = new ArrayList<String>(0);
		listaStringCdnPagos = new ArrayList<String>(0);
		try {

			/* recorrer la lista para encontrar los codigo */
			if (this.listaClsRiesgos != null) {
				for (ClsRiesgoDTO cls : this.listaClsRiesgos) {
					// log.error("codigo de la clasificacion de riesgo #0",
					// cls.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((cls.getCodigo().trim()));
					cadena.append("-");
					cadena.append((cls.getDescripcion().trim()));
					if (this.paginaIngreso != 24) {
						if (cls != null
								&& (cls.getCodigo().trim())
										.equals(codigoClsRiesgo)) {
							// log.error("clasificacion de riesgo #0",
							// cadena.toString());
							setClsRiesgo(cadena.toString());
						}
						listaStringClsRiesgos.add(cadena.toString());
						cadena = null;
					} else {
						if (cls != null
								&& ((cls.getCodigo().trim()).equals("005") || (cls
										.getCodigo().trim()).equals("008"))) {
							// log.error("clasificacion de riesgo #0",
							// cadena.toString());
							if (this.clsRiesgo == null) {
								setClsRiesgo(cadena.toString());
								listaStringClsRiesgos.add(cadena.toString());
							} else {
								listaStringClsRiesgos.add(cadena.toString());
							}
							cadena = null;
						}
					}
				}
			}

			if (this.listaCondicionPagos != null) {
				for (CPagoDTO obj : this.listaCondicionPagos) {
					// log.error("codigo de la condicion de riesgo #0",
					// obj.getCodigo());
					StringBuffer cadena = new StringBuffer();
					cadena.append((obj.getCodigo().trim()));
					cadena.append("-");
					cadena.append((obj.getDescripcion().trim()));

					if (this.paginaIngreso != 24) {
						if (obj != null
								&& obj.getCodigo().equals(codigoCdnPago)) {
							// log.error("condicion de riesgo #0",
							// cadena.toString());
							setcPago(cadena.toString());
						}
						listaStringCdnPagos.add(cadena.toString());
						cadena = null;
					} else if (this.paginaIngreso == 24) {
						if (obj != null
								&& obj.getCodigo().equals(codigoCdnPago)) {
							// log.error("condicion de riesgo #0",
							// cadena.toString());
							setcPago(cadena.toString());
							listaStringCdnPagos.add(cadena.toString());
							cadena = null;
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Error, a evaluar los combo #0", e.getMessage());
		}

	}
	
	
	public void getSetarSociedadAsignada(int idSociedad){
		try{
			if(listaSociedades.size() > 1){
				this.setHabilitarCboSociedad(false);
				return;
			}else{
				Sociedad soc = listaSociedades.get(0);
				this.setSociedadAux(soc);
			}
			if(this.sociedadAux != null){
				this.setHabilitarCboSociedad(true);
			}
			
		}catch(Exception ex){
			log.error("Error, al buscar la sociedad asocidada al usuario #0", ex.getMessage());
		}
	}
	
	
	/*******************************************************************************************************************************************************************************/
	
//    private ArrayList<UploadedImage> files = new ArrayList<UploadedImage>();
//    
//    public void paint(OutputStream stream, Object object) throws IOException {
//        stream.write(getFiles().get((Integer) object).getData());
//        stream.close();
//    }
// 
//    public void listener(FileUploadEvent event) throws Exception {
//        UploadedFile item = event.getUploadedFile();
//        UploadedImage file = new UploadedImage();
//        file.setLength(item.getData().length);
//        file.setName(item.getName());
//        file.setData(item.getData());
//        files.add(file);
//    }
// 
//    public String clearUploadData() {
//        files.clear();
//        return null;
//    }
// 
//    public int getSize() {
//        if (getFiles().size() > 0) {
//            return getFiles().size();
//        } else {
//            return 0;
//        }
//    }
// 
//    public long getTimeStamp() {
//        return System.currentTimeMillis();
//    }
// 
//    public ArrayList<UploadedImage> getFiles() {
//        return files;
//    }
// 
//    public void setFiles(ArrayList<UploadedImage> files) {
//        this.files = files;
//    }
	
	/******************************************************************************************************************************************************************************/
	
	

	public boolean habilitarCheckEnvio() {
		return usuarioCargoAux.getAdministrador();
	}

	public List<Region> getListaRegiones() {
		return listaRegiones;
	}

	public void setListaRegiones(List<Region> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}

	public List<Provincia> getListaProvincias() {
		return listaProvincias;
	}

	public void setListaProvincias(List<Provincia> listaProvincias) {
		this.listaProvincias = listaProvincias;
	}

	public List<Comuna> getListaComunas() {
		return listaComunas;
	}

	public void setListaComunas(List<Comuna> listaComunas) {
		this.listaComunas = listaComunas;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public Comuna getComuna() {
		return comuna;
	}

	public void setComuna(Comuna comuna) {
		this.comuna = comuna;
	}

	public ClienteSck getClienteSck() {
		return clienteSck;
	}

	public void setClienteSck(ClienteSck clienteSck) {
		this.clienteSck = clienteSck;
	}

	public boolean isPestanaGenerarSolicitudes() {
		return pestanaGenerarSolicitudes;
	}

	public void setPestanaGenerarSolicitudes(boolean pestanaGenerarSolicitudes) {
		this.pestanaGenerarSolicitudes = pestanaGenerarSolicitudes;
	}

	public List<String> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<String> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public String getUsuarioFilter() {
		return usuarioFilter;
	}

	public void setUsuarioFilter(String usuarioFilter) {
		this.usuarioFilter = usuarioFilter;
	}

	public List<String> getListaUsuariosPendientes() {
		return listaUsuariosPendientes;
	}

	public void setListaUsuariosPendientes(List<String> listaUsuariosPendientes) {
		this.listaUsuariosPendientes = listaUsuariosPendientes;
	}

	public List<String> getListaTiposSolicitudes() {
		return listaTiposSolicitudes;
	}

	public void setListaTiposSolicitudes(List<String> listaTiposSolicitudes) {
		this.listaTiposSolicitudes = listaTiposSolicitudes;
	}

	public List<String> getListaTiposSolicitudesPendientes() {
		return listaTiposSolicitudesPendientes;
	}

	public void setListaTiposSolicitudesPendientes(
			List<String> listaTiposSolicitudesPendientes) {
		this.listaTiposSolicitudesPendientes = listaTiposSolicitudesPendientes;
	}

	public String getTipoFilter() {
		return tipoFilter;
	}

	public void setTipoFilter(String tipoFilter) {
		this.tipoFilter = tipoFilter;
	}

	public String[] getArrayCanales() {
		return arrayCanales;
	}

	public void setArrayCanales(String[] arrayCanales) {
		this.arrayCanales = arrayCanales;
	}

	public Long getIdSolicitudPestTodas() {
		return idSolicitudPestTodas;
	}

	public void setIdSolicitudPestTodas(Long idSolicitudPestTodas) {
		this.idSolicitudPestTodas = idSolicitudPestTodas;
	}

	public String getRutAuxPestTodas() {
		return rutAuxPestTodas;
	}

	public void setRutAuxPestTodas(String rutAuxPestTodas) {
		this.rutAuxPestTodas = rutAuxPestTodas;
	}

	public LcredEstado getEstadoPestTodas() {
		return estadoPestTodas;
	}

	public void setEstadoPestTodas(LcredEstado estadoPestTodas) {
		this.estadoPestTodas = estadoPestTodas;
	}

	public Sucursal getSucursalPestTodas() {
		return sucursalPestTodas;
	}

	public void setSucursalPestTodas(Sucursal sucursalPestTodas) {
		this.sucursalPestTodas = sucursalPestTodas;
	}

	public Date getFechaInicalPestTodas() {
		return fechaInicalPestTodas;
	}

	public void setFechaInicalPestTodas(Date fechaInicalPestTodas) {
		this.fechaInicalPestTodas = fechaInicalPestTodas;
	}

	public Date getFechaFinalPestTodas() {
		return fechaFinalPestTodas;
	}

	public void setFechaFinalPestTodas(Date fechaFinalPestTodas) {
		this.fechaFinalPestTodas = fechaFinalPestTodas;
	}

	public Boolean getWvarEnviaCorreo() {
		return wvarEnviaCorreo;
	}

	public void setWvarEnviaCorreo(Boolean wvarEnviaCorreo) {
		this.wvarEnviaCorreo = wvarEnviaCorreo;
	}

	public UsuarioCargo getUsuarioCargoAux() {
		return usuarioCargoAux;
	}

	public void setUsuarioCargoAux(UsuarioCargo usuarioCargoAux) {
		this.usuarioCargoAux = usuarioCargoAux;
	}

	public String getObservacionesAutirzador() {
		return observacionesAutirzador;
	}

	public void setObservacionesAutirzador(String observacionesAutirzador) {
		this.observacionesAutirzador = observacionesAutirzador;
	}

	public Boolean getHabilitaBotonApruebaToRechazarAdmin() {
		return habilitaBotonApruebaToRechazarAdmin;
	}

	public void setHabilitaBotonApruebaToRechazarAdmin(
			Boolean habilitaBotonApruebaToRechazarAdmin) {
		this.habilitaBotonApruebaToRechazarAdmin = habilitaBotonApruebaToRechazarAdmin;
	}

	public String getRutAuxSinGuion() {
		return rutAuxSinGuion;
	}

	public void setRutAuxSinGuion(String rutAuxSinGuion) {
		this.rutAuxSinGuion = rutAuxSinGuion;
	}

	public Long getMontoActualCuentaCorriente() {
		return montoActualCuentaCorriente;
	}

	public void setMontoActualCuentaCorriente(Long montoActualCuentaCorriente) {
		this.montoActualCuentaCorriente = montoActualCuentaCorriente;
	}

	public List<String> getListaEstadoString() {
		return listaEstadoString;
	}

	public void setListaEstadoString(List<String> listaEstadoString) {
		this.listaEstadoString = listaEstadoString;
	}

	public List<String> getListaEstadoPendienteString() {
		return listaEstadoPendienteString;
	}

	public void setListaEstadoPendienteString(
			List<String> listaEstadoPendienteString) {
		this.listaEstadoPendienteString = listaEstadoPendienteString;
	}

	public String getEstadoFilter() {
		return estadoFilter;
	}

	public void setEstadoFilter(String estadoFilter) {
		this.estadoFilter = estadoFilter;
	}

	public List<String> getListaCanalString() {
		return listaCanalString;
	}

	public void setListaCanalString(List<String> listaCanalString) {
		this.listaCanalString = listaCanalString;
	}

	public List<String> getListaCanalPendienteString() {
		return listaCanalPendienteString;
	}

	public void setListaCanalPendienteString(
			List<String> listaCanalPendienteString) {
		this.listaCanalPendienteString = listaCanalPendienteString;
	}

	public String getCanalFilter() {
		return canalFilter;
	}

	public void setCanalFilter(String canalFilter) {
		this.canalFilter = canalFilter;
	}

	public List<String> getListaProcesoString() {
		return listaProcesoString;
	}

	public void setListaProcesoString(List<String> listaProcesoString) {
		this.listaProcesoString = listaProcesoString;
	}

	public List<String> getListaProcesoPendienteString() {
		return listaProcesoPendienteString;
	}

	public void setListaProcesoPendienteString(
			List<String> listaProcesoPendienteString) {
		this.listaProcesoPendienteString = listaProcesoPendienteString;
	}

	public String getProcesoFilter() {
		return procesoFilter;
	}

	public void setProcesoFilter(String procesoFilter) {
		this.procesoFilter = procesoFilter;
	}

	public List<String> getListaUsuariosDerivadas() {
		return listaUsuariosDerivadas;
	}

	public void setListaUsuariosDerivadas(List<String> listaUsuariosDerivadas) {
		this.listaUsuariosDerivadas = listaUsuariosDerivadas;
	}

	public List<String> getListaEstadoMisSolicitudesString() {
		return listaEstadoMisSolicitudesString;
	}

	public void setListaEstadoMisSolicitudesString(
			List<String> listaEstadoMisSolicitudesString) {
		this.listaEstadoMisSolicitudesString = listaEstadoMisSolicitudesString;
	}

	public List<String> getListaEstadoDerivadas() {
		return listaEstadoDerivadas;
	}

	public void setListaEstadoDerivadas(List<String> listaEstadoDerivadas) {
		this.listaEstadoDerivadas = listaEstadoDerivadas;
	}

	public List<String> getListaCanalMisSolicitudesString() {
		return listaCanalMisSolicitudesString;
	}

	public void setListaCanalMisSolicitudesString(
			List<String> listaCanalMisSolicitudesString) {
		this.listaCanalMisSolicitudesString = listaCanalMisSolicitudesString;
	}

	public List<String> getListaCanalDerivadas() {
		return listaCanalDerivadas;
	}

	public void setListaCanalDerivadas(List<String> listaCanalDerivadas) {
		this.listaCanalDerivadas = listaCanalDerivadas;
	}

	public List<String> getListaProcesoDerivadas() {
		return listaProcesoDerivadas;
	}

	public void setListaProcesoDerivadas(List<String> listaProcesoDerivadas) {
		this.listaProcesoDerivadas = listaProcesoDerivadas;
	}

	public List<String> getListaTiposMisSolicitudesString() {
		return listaTiposMisSolicitudesString;
	}

	public void setListaTiposMisSolicitudesString(
			List<String> listaTiposMisSolicitudesString) {
		this.listaTiposMisSolicitudesString = listaTiposMisSolicitudesString;
	}

	public List<String> getListaTiposDerivadas() {
		return listaTiposDerivadas;
	}

	public void setListaTiposDerivadas(List<String> listaTiposDerivadas) {
		this.listaTiposDerivadas = listaTiposDerivadas;
	}

	public List<String> getListaSucursalSolicitudes() {
		return listaSucursalSolicitudes;
	}

	public void setListaSucursalSolicitudes(
			List<String> listaSucursalSolicitudes) {
		this.listaSucursalSolicitudes = listaSucursalSolicitudes;
	}

	public List<String> getListaSucursalSolicitudesPendientes() {
		return listaSucursalSolicitudesPendientes;
	}

	public void setListaSucursalSolicitudesPendientes(
			List<String> listaSucursalSolicitudesPendientes) {
		this.listaSucursalSolicitudesPendientes = listaSucursalSolicitudesPendientes;
	}

	public List<String> getListaSucursalMisSolicitudesString() {
		return listaSucursalMisSolicitudesString;
	}

	public void setListaSucursalMisSolicitudesString(
			List<String> listaSucursalMisSolicitudesString) {
		this.listaSucursalMisSolicitudesString = listaSucursalMisSolicitudesString;
	}

	public List<String> getListaSucursalDerivadas() {
		return listaSucursalDerivadas;
	}

	public void setListaSucursalDerivadas(List<String> listaSucursalDerivadas) {
		this.listaSucursalDerivadas = listaSucursalDerivadas;
	}

	public String getSucursalFilter() {
		return sucursalFilter;
	}

	public void setSucursalFilter(String sucursalFilter) {
		this.sucursalFilter = sucursalFilter;
	}

	public int getTipoSolicitudCodigo() {
		return tipoSolicitudCodigo;
	}

	public void setTipoSolicitudCodigo(int tipoSolicitudCodigo) {
		this.tipoSolicitudCodigo = tipoSolicitudCodigo;
	}

	public String getNegociosCambioSucursal() {
		return negociosCambioSucursal;
	}

	public void setNegociosCambioSucursal(String negociosCambioSucursal) {
		this.negociosCambioSucursal = negociosCambioSucursal;
	}

	public String getConceptoCambioSucursal() {
		return conceptoCambioSucursal;
	}

	public void setConceptoCambioSucursal(String conceptoCambioSucursal) {
		this.conceptoCambioSucursal = conceptoCambioSucursal;
	}

	public Sociedad getSociedadAux() {
		return sociedadAux;
	}

	public void setSociedadAux(Sociedad sociedadAux) {
		this.sociedadAux = sociedadAux;
	}

	public List<Sociedad> getListaSociedades() {
		return listaSociedades;
	}

	public void setListaSociedades(List<Sociedad> listaSociedades) {
		this.listaSociedades = listaSociedades;
	}

	public boolean isHabilitarCboSociedad() {
		return habilitarCboSociedad;
	}

	public void setHabilitarCboSociedad(boolean habilitarCboSociedad) {
		this.habilitarCboSociedad = habilitarCboSociedad;
	}

	public Sociedad getSociedadPestTodas() {
		return sociedadPestTodas;
	}

	public void setSociedadPestTodas(Sociedad sociedadPestTodas) {
		this.sociedadPestTodas = sociedadPestTodas;
	}

	public List<Sociedad> getListaSociedadesPestTodas() {
		return listaSociedadesPestTodas;
	}

	public void setListaSociedadesPestTodas(List<Sociedad> listaSociedadesPestTodas) {
		this.listaSociedadesPestTodas = listaSociedadesPestTodas;
	}
	
	

}
