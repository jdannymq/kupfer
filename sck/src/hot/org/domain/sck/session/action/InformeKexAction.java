package org.domain.sck.session.action;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;

import org.domain.sck.base.GlobalHitosLogdService;
import org.domain.sck.base.GlobalService;
import org.domain.sck.dto.CPagoDTO;
import org.domain.sck.dto.HitosDTO;
import org.domain.sck.dto.PorcentajeDTO;
import org.domain.sck.dto.SolicitudDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.entity.Canal;
import org.domain.sck.entity.CanalUsuarioCargo;
import org.domain.sck.entity.ConceptosNegocio;
import org.domain.sck.entity.CondicionTipoSolicitudRango;
import org.domain.sck.entity.Estados;
import org.domain.sck.entity.LcredEstado;
import org.domain.sck.entity.LcredSolicitudObservaciones;
import org.domain.sck.entity.LcredSolicitudObservacionesId;
import org.domain.sck.entity.LcredTipoSolicitud;
import org.domain.sck.entity.LcredUsuarioNivelEnc;
import org.domain.sck.entity.Medicion;
import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudLogs;
import org.domain.sck.entity.SolicitudUsuarioDerivada;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.entity.enums.TiempoMontoType;
import org.domain.sck.entity.enums.TipoCuentasKupferType;
import org.domain.sck.session.service.intranetsap.IntranetSapService;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

@Name("informeKexAction")
@Scope(ScopeType.CONVERSATION)
public class InformeKexAction {

	@Logger
	Log log;

	@In(value = "#{entityManager}")
	EntityManager entityManager;

	@In
	IntranetSapService intranetSapService;

	@In
	ScoringService scoringService;

	@In
	GlobalService globalService;

	@In
	GlobalHitosLogdService globalHitosLogdService;

	@In(value = "#{identity.usuarioCargo}", scope = ScopeType.SESSION)
	private UsuarioCargo usuarioCargoAux;

	@In(value = "#{identity.usuarioLogueado}", scope = ScopeType.SESSION)
	private Usuariosegur usuarioLogueado;

	@In(value = "#{identity.lcredUsuarioNivelEnc}", scope = ScopeType.SESSION)
	private LcredUsuarioNivelEnc lcredUsuarioNivelEnc;

	@In(value = "#{identity.usuarioSegur}", scope = ScopeType.SESSION)
	private UsuarioSegurDTO usuarioSegur;

	private LcredTipoSolicitud tipoSolicitud;
	private Usuariosegur usuarioSegurCbo;
	private LcredEstado estado;
	private Sucursal sucursal;
	private Date fechaInical;
	private Date fechaFinal;

	private List<Canal> listaCanales;
	private List<Canal> listaCanalesSeleccionada;

	private List<ConceptosNegocio> listaNegocios;
	private List<ConceptosNegocio> listaNegociosSeleccionados;

	private List<LcredTipoSolicitud> listaLcredTipoSolicituds;
	private List<LcredTipoSolicitud> listaLcredTipoSolicitudsSeleccionados;

	private List<Sucursal> listaSucursals;
	private List<Sucursal> listaSucursalsSeleccionados;

	private List<Usuariosegur> listaUsuariosegurs;
	private List<Usuariosegur> listaUsuariosegursSeleccionados;

	private List<SolicitudDTO> listaTodasSolicitudesDto;
	private List<CPagoDTO> listaCPagoDTOs;
	private List<LcredEstado> listaEstados;
	private List<String> listaUsuarios;
	private List<String> listaTiposSolicitudes;
	private List<String> listaFormaPago;
	private String tsListaTodasSolicitudes;
	private String usuarioFilter;
	private String tipoFilter;
	private String fpagos;
	private List<SolicitudUsuarioDerivada> listUsuariosDerivados;
	private SolicitudDTO solicitudAux;
	private SolicitudHitos solicitudHitos;
	private Long idSolicitud;
	private String rutAux;
	private String rutAuxSinGuion;
	
	private Long totalSolicitudes=(long)0;
	private Long cumplidas=(long)0;
	private Long nocumplidas=(long)0;
	private Double porcentajeCumplidas=(double)0;
	private Double porcentajeNoCumplidas=(double)0;
	
	private List<PorcentajeDTO> listaPorcentajes;
	private  List<PorcentajeDTO> listaPorcentajeTipoSolicitudes;
	
	private boolean ventasNormal;
	private boolean ventaEnCuotas;
	private boolean nominadas;
	private boolean innomindas;
	private boolean riesgoKupfer;
	private boolean riesgoPago;
	private boolean bloqueoDesbloqueo;
	private boolean creacionDm;
	private boolean prorogas;
	
	
	

	private List<CanalUsuarioCargo> listaCanalUsuarioCargos;

	@Create
	public void init() {
		try {

			if (lcredUsuarioNivelEnc != null) {
				sacarNegocios();
				sacarCanales();
				sacarUsuarios();
				sacarTipoSolicitudes();
				sacarSucursales();
				sacarCPago();
				sacarEstadosSolicitudes();

			}
		} catch (Exception e) {
			log.error("error al sacar las solictudes de usuario logeado #0",
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void sacarUsuarios() {
		try {
			List<Usuariosegur> lista = (List<Usuariosegur>) entityManager
					.createQuery(
							" select u from Usuariosegur u " +
							" where u.alias in ( " +
							"  select sh.usuarioActual from SolicitudHitos sh " +
							"  where sh.codigoEstado in ('E') " +
							"  and sh.usuarioActual not in ('VARIOS') "+
							" ) " +
							" order by u.nombre")
					.getResultList();
			
			         

			if (lista != null) {
				listaUsuariosegurs = new ArrayList<Usuariosegur>(0);
				for (Usuariosegur usuario : lista) {
					if (usuario != null && !usuario.getAlias().equals("")
							&& !usuario.getAlias().equals("xxxx")
							&& !usuario.getAlias().equals("xxxxxx")) {
						if (usuario.getNombre() != null
								&& !usuario.getNombre().equals("")) {
							if (!listaUsuariosegurs.contains(usuario)) {
								listaUsuariosegurs.add(usuario);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error, al sacar los datos de usuarios #0",
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void sacarCanales() {
		try {
			listaCanales = entityManager
					.createQuery(
							"select c from Canal c where c.disabled=0  order by c.systemId")
					.getResultList();
			if (listaCanales == null || listaCanales.size() == 0) {
				listaCanales = new ArrayList<Canal>(0);
			}
		} catch (Exception e) {
			log.error("Error, al sacar los datos del canal #0", e.getMessage());
		}
	}

	public void sacarNegocios() {
		try {
			listaNegocios = scoringService.obtenerNegocios();
			if (listaNegocios == null || listaNegocios.size() == 0) {
				listaNegocios = new ArrayList<ConceptosNegocio>(0);
			}
		} catch (Exception e) {
			log.error("Error, al sacar los datos del canal #0", e.getMessage());
		}
	}

	public void sacarTipoSolicitudes() {
		try {
			listaLcredTipoSolicituds = scoringService
					.getListadoLcredTipoSolicitudOrdenadoPorCorrelativo();
			if (listaLcredTipoSolicituds == null
					&& listaLcredTipoSolicituds.size() == 0) {
				listaLcredTipoSolicituds = new ArrayList<LcredTipoSolicitud>(0);
			}
		} catch (Exception e) {
			log.error("Error, al sacar la lista de tipo de solicitudes #0",
					e.getMessage());
		}
	}

	public void seleccionarTipoSolicitud() {
		if (this.tipoSolicitud != null) {
			log.error(" el tipo de solicitud es: #0",
					this.tipoSolicitud.getDesTipoSolicitud());
		}
	}

	public void seleccionarUsuario() {
		if (this.tipoSolicitud != null) {
			log.error(" el tipo de solicitud es: #0",
					this.tipoSolicitud.getDesTipoSolicitud());
		}
	}

	public void sacarEstado() {
		if (this.estado != null) {
			log.error(" el estado seleccionado #0", this.estado.getDesEstado());
		}
	}

	public void sacarSucursal() {
		if (this.sucursal != null) {
			log.error(" la sucursal  seleccionado #0",
					this.sucursal.getDescripcion());
		}
	}

	public boolean validarParametros() {
		try {

			if (this.idSolicitud == null
					&& (this.rutAux == null || "".equals(this.rutAux))
					&& this.fechaInical == null && this.fechaFinal == null
					&& this.estado == null && this.sucursal == null) {

				FacesMessages.instance().add(Severity.INFO,
						"Debe ingresar por lo menos un criterio de busqueda.");
				return false;
			}

			if ("".equals(this.rutAux)) {
				this.setRutAux(null);
			}

			if (this.idSolicitud != null) {
				if (this.idSolicitud.longValue() != 0) {
					return true;
				}
			}
			if (this.getRutAux() != null) {
				if (!"".equals(this.getRutAux())) {
					// verificar que el rut no tenga menos de 8 caracteres
					if (this.getRutAux().length() < 8) {
						FacesMessages
								.instance()
								.add(Severity.ERROR,
										"Rut invalido, debe tener al menos 8 caracteres");
						return false;
					}

					// agregar guin en caso de ser necesario
					String rut = globalService
							.agregarGuionRut(this.getRutAux());
					log.debug("rut #0 #1", rut, globalService.validarRut(rut));
					this.rutAuxSinGuion = globalService.sacarGuionRut(rut);

					this.setRutAux(rut);
				} else {
					this.setRutAux(null);
				}

			}

			if (this.fechaInical == null && this.fechaFinal != null) {
				FacesMessages.instance().add(Severity.ERROR,
						"Debe seleccionar la fecha inicial.");
				return false;
			} else if (this.fechaInical != null && this.fechaFinal != null) {
				Calendar calInicial = Calendar.getInstance();
				calInicial.setTime(fechaInical);
				Long valorInicial = calInicial.getTimeInMillis();
				Calendar calFinal = Calendar.getInstance();
				calFinal.setTime(this.fechaFinal);
				/* evaluacion final */
				Long valorFinal = calFinal.getTimeInMillis();
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

	public void consultarSolicitudes() {
		String fechaInicio = null;
		String fechasFinal = null;
		String negocios = null;
		String canales = null;
		String tipoSolicitudes = null;
		String sucursales = null;
		String usuarios = null;
		if (validarParametros()) {
			// fechas inicial
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			fechaInicio = sdf.format(fechaInical);

			// Fechas Final
			SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMdd");
			fechasFinal = sdft.format(fechaFinal);

			// canales
			if (listaCanalesSeleccionada != null
					&& listaCanalesSeleccionada.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaCanalesSeleccionada.size();
				int contador = 1;
				for (Canal can : listaCanalesSeleccionada) {
					cadena.append("'");
					cadena.append(can.getDescripcion().trim());
					cadena.append("'");
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				canales = cadena.toString();
			}
			// negocios
			if (listaNegociosSeleccionados != null
					&& listaNegociosSeleccionados.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaNegociosSeleccionados.size();
				int contador = 1;
				for (ConceptosNegocio cn : listaNegociosSeleccionados) {
					cadena.append("'");
					cadena.append(cn.getNegocio().trim());
					cadena.append("'");
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				negocios = cadena.toString();
			}
			// tipo de solicitudes
			if (listaLcredTipoSolicitudsSeleccionados != null
					&& listaLcredTipoSolicitudsSeleccionados.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaLcredTipoSolicitudsSeleccionados.size();
				int contador = 1;
				String tipoSol = "";
				for (LcredTipoSolicitud tipo : listaLcredTipoSolicitudsSeleccionados) {

					switch (Integer.parseInt(tipo.getCodTipoSolicitud().trim())) {
					case 11:
						tipoSol = "'1N'";
						ventasNormal = true;
						break;
					case 12:
						tipoSol = "'1C'";
						ventaEnCuotas = true;
						break;
					case 21:
						tipoSol = "'2I'";
						innomindas = true;
						break;
					case 22:
						tipoSol = "'2N'";
						nominadas = true;
						break;
					case 23:
						tipoSol = "'2R'";
						riesgoKupfer=true;
						break;
					case 31:
						tipoSol = "'3RP','3P','3R'";
						riesgoPago = true;
						break;
					case 41:
						tipoSol = "'4B','4D'";
						bloqueoDesbloqueo = true;
						break;
					case 43:
						tipoSol = "'4DM'";
						creacionDm = true;
						break;
					case 44:
						tipoSol = "'4PR'";
						prorogas=true;
						break;
					default:
						break;
					}

					cadena.append(tipoSol);
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				tipoSolicitudes = cadena.toString();
			}

			// sucursales
			if (listaSucursalsSeleccionados != null
					&& listaSucursalsSeleccionados.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaSucursalsSeleccionados.size();
				int contador = 1;
				for (Sucursal suc : listaSucursalsSeleccionados) {
					cadena.append("'");
					cadena.append(suc.getCodigo().trim());
					cadena.append("'");
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				sucursales = cadena.toString();
			}

			// usuarios
			if (listaUsuariosegursSeleccionados != null
					&& listaUsuariosegursSeleccionados.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaUsuariosegursSeleccionados.size();
				int contador = 1;
				for (Usuariosegur usu : listaUsuariosegursSeleccionados) {
					cadena.append("'");
					cadena.append(usu.getAlias().trim());
					cadena.append("'");
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				usuarios = cadena.toString();
			}

			// setar los valores obtenidos
			getObtenerDatosParaTrabajoMatriz(usuarios, fechaInicio,
					fechasFinal, tipoSolicitudes, canales, negocios, sucursales);
			
			
			if(listaUsuariosegursSeleccionados != null && listaUsuariosegursSeleccionados.size() > 0){
				listaPorcentajes =  new ArrayList<PorcentajeDTO>(0);
				for(Usuariosegur user : listaUsuariosegursSeleccionados){
					StringBuffer cadena = new StringBuffer();
					cadena.append("'");cadena.append(user.getAlias());cadena.append("'");
					PorcentajeDTO objPorcentaje = getObtenerDatosParaTrabajoUsuario(cadena.toString(), fechaInicio,
							fechasFinal, tipoSolicitudes, canales, negocios, sucursales);
					if(objPorcentaje != null){
						listaPorcentajes.add(objPorcentaje);
					}
				}
				
				if(listaLcredTipoSolicitudsSeleccionados != null && listaLcredTipoSolicitudsSeleccionados.size() > 0){
					listaPorcentajeTipoSolicitudes = new ArrayList<PorcentajeDTO>(0);
					if(listaPorcentajes != null){
						for(PorcentajeDTO obj : listaPorcentajes){
							if(obj != null){
								for(LcredTipoSolicitud ti: listaLcredTipoSolicitudsSeleccionados){
									PorcentajeDTO objPorTipo = getObtenerDatosParaTrabajoUsuarioAndTipoSolicitud(obj.getUsuario(), fechaInicio,
											fechasFinal, ti, canales, negocios, sucursales);
									if(obj.getListasProcentajeTipoSolicitudes() == null){
									   obj.setListasProcentajeTipoSolicitudes(new ArrayList<PorcentajeDTO>(0));
									   obj.getListasProcentajeTipoSolicitudes().add(objPorTipo);
									}else{
									   obj.getListasProcentajeTipoSolicitudes().add(objPorTipo);
									}
								}
							}
							listaPorcentajeTipoSolicitudes.add(obj); 	
						}
					}
				}
			}
		}
	}
	public void generarExcelSolicitudes() {
		String fechaInicio = null;
		String fechasFinal = null;
		String negocios = null;
		String canales = null;
		String tipoSolicitudes = null;
		String sucursales = null;
		String usuarios = null;
		if (validarParametros()) {
			// fechas inicial
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			fechaInicio = sdf.format(fechaInical);

			// Fechas Final
			SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMdd");
			fechasFinal = sdft.format(fechaFinal);

			// canales
			if (listaCanalesSeleccionada != null
					&& listaCanalesSeleccionada.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaCanalesSeleccionada.size();
				int contador = 1;
				for (Canal can : listaCanalesSeleccionada) {
					cadena.append("'");
					cadena.append(can.getDescripcion().trim());
					cadena.append("'");
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				canales = cadena.toString();
			}
			// negocios
			if (listaNegociosSeleccionados != null
					&& listaNegociosSeleccionados.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaNegociosSeleccionados.size();
				int contador = 1;
				for (ConceptosNegocio cn : listaNegociosSeleccionados) {
					cadena.append("'");
					cadena.append(cn.getNegocio().trim());
					cadena.append("'");
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				negocios = cadena.toString();
			}
			// tipo de solicitudes
			if (listaLcredTipoSolicitudsSeleccionados != null
					&& listaLcredTipoSolicitudsSeleccionados.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaLcredTipoSolicitudsSeleccionados.size();
				int contador = 1;
				String tipoSol = "";
				for (LcredTipoSolicitud tipo : listaLcredTipoSolicitudsSeleccionados) {

					switch (Integer.parseInt(tipo.getCodTipoSolicitud().trim())) {
					case 11:
						tipoSol = "'1N'";
						ventasNormal = true;
						break;
					case 12:
						tipoSol = "'1C'";
						ventaEnCuotas = true;
						break;
					case 21:
						tipoSol = "'2I'";
						innomindas = true;
						break;
					case 22:
						tipoSol = "'2N'";
						nominadas = true;
						break;
					case 23:
						tipoSol = "'2R'";
						riesgoKupfer=true;
						break;
					case 31:
						tipoSol = "'3RP','3P','3R'";
						riesgoPago = true;
						break;
					case 41:
						tipoSol = "'4B','4D'";
						bloqueoDesbloqueo = true;
						break;
					case 43:
						tipoSol = "'4DM'";
						creacionDm = true;
						break;
					case 44:
						tipoSol = "'4PR'";
						prorogas=true;
						break;
					default:
						break;
					}

					cadena.append(tipoSol);
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				tipoSolicitudes = cadena.toString();
			}

			// sucursales
			if (listaSucursalsSeleccionados != null
					&& listaSucursalsSeleccionados.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaSucursalsSeleccionados.size();
				int contador = 1;
				for (Sucursal suc : listaSucursalsSeleccionados) {
					cadena.append("'");
					cadena.append(suc.getCodigo().trim());
					cadena.append("'");
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				sucursales = cadena.toString();
			}

			// usuarios
			if (listaUsuariosegursSeleccionados != null
					&& listaUsuariosegursSeleccionados.size() > 0) {
				StringBuffer cadena = new StringBuffer();
				int cantidad = listaUsuariosegursSeleccionados.size();
				int contador = 1;
				for (Usuariosegur usu : listaUsuariosegursSeleccionados) {
					cadena.append("'");
					cadena.append(usu.getAlias().trim());
					cadena.append("'");
					if (contador < cantidad) {
						cadena.append(",");
						contador++;
					} else {
						contador++;
					}
				}
				usuarios = cadena.toString();
			}

			// setar los valores obtenidos
			getObtenerDatosParaTrabajoMatriz(usuarios, fechaInicio,
					fechasFinal, tipoSolicitudes, canales, negocios, sucursales);
			
			
			if(listaUsuariosegursSeleccionados != null && listaUsuariosegursSeleccionados.size() > 0){
				listaPorcentajes =  new ArrayList<PorcentajeDTO>(0);
				for(Usuariosegur user : listaUsuariosegursSeleccionados){
					StringBuffer cadena = new StringBuffer();
					cadena.append("'");cadena.append(user.getAlias());cadena.append("'");
					PorcentajeDTO objPorcentaje = getObtenerDatosParaTrabajoUsuario(cadena.toString(), fechaInicio,
							fechasFinal, tipoSolicitudes, canales, negocios, sucursales);
					if(objPorcentaje != null){
						listaPorcentajes.add(objPorcentaje);
					}
				}
				
				if(listaLcredTipoSolicitudsSeleccionados != null && listaLcredTipoSolicitudsSeleccionados.size() > 0){
					listaPorcentajeTipoSolicitudes = new ArrayList<PorcentajeDTO>(0);
					if(listaPorcentajes != null){
						for(PorcentajeDTO obj : listaPorcentajes){
							if(obj != null){
								for(LcredTipoSolicitud ti: listaLcredTipoSolicitudsSeleccionados){
									PorcentajeDTO objPorTipo = getObtenerDatosParaTrabajoUsuarioAndTipoSolicitud(obj.getUsuario(), fechaInicio,
											fechasFinal, ti, canales, negocios, sucursales);
									if(obj.getListasProcentajeTipoSolicitudes() == null){
									   obj.setListasProcentajeTipoSolicitudes(new ArrayList<PorcentajeDTO>(0));
									   obj.getListasProcentajeTipoSolicitudes().add(objPorTipo);
									}else{
									   obj.getListasProcentajeTipoSolicitudes().add(objPorTipo);
									}
								}
							}
							listaPorcentajeTipoSolicitudes.add(obj); 	
						}
					}
				}
			}
		}
	}
	
	
	public void limpiarParametos() {
		this.setIdSolicitud(null);
		this.setRutAux(null);
		this.setFechaFinal(null);
		this.setFechaInical(null);
		this.setEstado(null);
		this.setSucursal(null);
		this.setListaTodasSolicitudesDto(new ArrayList<SolicitudDTO>());
		this.setTsListaTodasSolicitudes(null);
		listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>();
	}

	@SuppressWarnings("unchecked")
	public void sacarSucursales() {
		try {
			listaSucursals = (List<Sucursal>) entityManager
					.createQuery(
							"select suc from Sucursal suc "
									+ " where suc.codigo in ('C212','C246','N103','N206','S133','S148','S224','C115',"
									+ "                      'C227','N101','N142','N209','S118','S145','S221','S243','C138','TDS1') order by suc.descripcion asc ")
					.getResultList();
		} catch (Exception e) {
			log.error("Error, al sacar la lista de sucursales #0",
					e.getMessage());
		}
	}

	public Sucursal obtenerObjetoSucursal(String codigo) {
		Sucursal suc = null;
		if (listaSucursals != null && codigo != null) {
			for (Sucursal s : listaSucursals) {
				if (s.getCodigo().equals(codigo)) {
					suc = s;
					break;
				}
			}
		}
		return suc;
	}

	public void sacarCPago() {
		try {
			listaCPagoDTOs = intranetSapService.getCondicionPago();
		} catch (Exception e) {
			log.error("Error, al sacar la lista de condiciones de pagos #0",
					e.getMessage());
		}
	}

	public CPagoDTO obtenerObjetoCPago(String codigo) {
		CPagoDTO obj = null;
		if (listaCPagoDTOs != null && codigo != null) {
			for (CPagoDTO s : listaCPagoDTOs) {
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

	@SuppressWarnings("unchecked")
	public List<SolicitudUsuarioDerivada> sacarSolicitudUsuarioDerivada(
			Long idSolicitud) {
		List<SolicitudUsuarioDerivada> lista = null;
		try {
			lista = (List<SolicitudUsuarioDerivada>) entityManager
					.createQuery(
							"select sud "
									+ "	from SolicitudUsuarioDerivada sud"
									+ " 	where sud.codEstadoDerivada=:codEstado "
									+ "	and sud.confirmacionObligatoria=1 "
									+ "   and sud.idSolicitud=:solAux"
									+ "   and sud.confirmacion is null ")
					.setParameter("codEstado", "SA")
					.setParameter("solAux", idSolicitud).getResultList();
			if (lista != null && lista.size() > 0) {
				log.debug("cantidad #0", lista.size());
			}
		} catch (Exception e) {
			log.error("Error, al sacar la lista de sucursales #0",
					e.getMessage());
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<SolicitudUsuarioDerivada> sacarSolicitudUsuarioDerivadaAnalisis(
			Long idSolicitud) {
		List<SolicitudUsuarioDerivada> lista = null;
		try {
			lista = (List<SolicitudUsuarioDerivada>) entityManager
					.createQuery(
							"select sud "
									+ "	from SolicitudUsuarioDerivada sud"
									+ " 	where sud.codEstadoDerivada=:codEstado "
									+ "	and sud.confirmacionObligatoria=1 "
									+ "   and sud.idSolicitud=:solAux"
									+ "   and sud.confirmacion is null ")
					.setParameter("codEstado", "B")
					.setParameter("solAux", idSolicitud).getResultList();
			if (lista != null && lista.size() > 0) {
				log.debug("cantidad #0", lista.size());
			}
		} catch (Exception e) {
			log.error("Error, al sacar la lista de sucursales #0",
					e.getMessage());
		}
		return lista;
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

	public void liberarSolicitud(Long idSolicitud) {
		try {
			if (idSolicitud != null) {
				entityManager
						.createQuery(
								"update LcredSolicitud set evaluar=0 where id.numSolicitud=:sol")
						.setParameter("sol", idSolicitud).executeUpdate();
				entityManager.flush();

				String codigo = "L";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Se libera la solicitud que quedo tomada.");
					exito = scoringService.persistSolicitudLogs(idSolicitud,
							usuarioSegur.getAlias(), new Date(),
							String.valueOf(estadoLogs.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Se da aprobación la solicitud.");
					exito = scoringService.persistSolicitudLogs(idSolicitud,
							usuarioSegur.getAlias(), new Date(), "", "",
							cadena.toString());
				}

				log.debug("verificacion si inserto registro logs #0", exito);

			}

		} catch (Exception e) {
			log.error("Error al libar la solicitud sleccionada #0",
					e.getMessage());
		}

	}

	public void reAsignarSolicitudDerivadaUsuario(SolicitudDTO solicitudDTO) {
		try {
			if (solicitudDTO != null) {

				if (solicitudDTO.getDerivado() == null) {
					FacesMessages
							.instance()
							.add(Severity.WARN,
									"Debe seleccionar al usuario aprobador a reemplazar.");
					return;
				}
				if (solicitudDTO.getUsuarioAsignar() == null) {
					FacesMessages.instance().add(Severity.WARN,
							"Debe seleccionar el usuario a reasignar.");
					return;
				}

				SolicitudUsuarioDerivada sud = solicitudDTO.getDerivado();
				if (sud != null) {
					SolicitudUsuarioDerivada sudNueva = new SolicitudUsuarioDerivada();
					Long numero = scoringService
							.getSolicitudUsuarioDerivacionMaximo();
					sudNueva.setSystemId(numero);
					sudNueva.setIdSolicitud(sud.getIdSolicitud());
					sudNueva.setEstado(sud.getEstado());
					sudNueva.setFechaHora(new Date());
					sudNueva.setUsuario(solicitudDTO.getUsuarioAsignar());
					sudNueva.setConfirmacionObligatoria(true);
					sudNueva.setConfirmacion(null);
					sudNueva.setCodEstadoDerivada(sud.getCodEstadoDerivada());
					boolean exitoingreso = scoringService
							.persitSolicitudUsuarioDerivacion(sudNueva);
					log.debug(exitoingreso);

					sud.setConfirmacionObligatoria(false);
					sud.setConfirmacion(true);
					entityManager.merge(sud);
					entityManager.flush();
					entityManager.refresh(sud);

					/* se agrega la observaciones final de una aprobacion final */
					LcredSolicitudObservacionesId id = new LcredSolicitudObservacionesId();
					Long correlativo = scoringService
							.obtenerCorrelativoObservaciones(solicitudDTO
									.getIdSolictud());
					if (correlativo != null) {
						id.setCorrelativo(correlativo);
						id.setFecha(new Date());
						id.setHora(new Date());
						id.setNumSolicitud(solicitudDTO.getIdSolictud());
						id.setTipoSol(solicitudDTO.getTipoSolicitud());
						id.setObservacion("Se libera la solicitud asignada al usuario "
								+ solicitudDTO.getDerivado().getUsuario()
										.getNombre()
								+ " y se reasigna al usuario "
								+ solicitudDTO.getUsuarioAsignar().getNombre());
						id.setUsuario(usuarioSegur.getAlias());
						LcredSolicitudObservaciones obser = new LcredSolicitudObservaciones();
						obser.setId(id);
						scoringService.persistSolicitudObservaciones(obser);
					}
				}

				String codigo = "L";
				Estados estadoLogs = scoringService.obtenerEstados(codigo
						.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if (estadoLogs != null) {
					cadena.append("Se libera la solicitud y se reasigna a otro usuario.");
					exito = scoringService.persistSolicitudLogs(
							solicitudDTO.getIdSolictud(),
							usuarioSegur.getAlias(), new Date(),
							String.valueOf(estadoLogs.getCodEstado()),
							estadoLogs.getDesEstado(), cadena.toString());
				} else {
					cadena.append("Se libera la solicitud y se reasigna a otro usuario.");
					exito = scoringService.persistSolicitudLogs(
							solicitudDTO.getIdSolictud(),
							usuarioSegur.getAlias(), new Date(), "", "",
							cadena.toString());
				}
				log.debug("verificacion si inserto registro logs #0", exito);
			}

		} catch (Exception e) {
			log.error("Error al libar la solicitud sleccionada #0",
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

	public void limpiarVariblesUsuarioDerevida() {
		this.listUsuariosDerivados = null;
		this.solicitudAux = null;

	}

	public void sacarLogHitosSolicitudUsuarioDerivada(SolicitudHitos sol) {
		try {
			if (sol != null) {
				this.setSolicitudHitos(sol);
				List<SolicitudUsuarioDerivada> listaDerivadas = scoringService
						.getSolicitudUsuarioDerivacionForSolicitudEstado(
								sol.getIdSolicitud(),
								sol.getCodEstadoDerivada());
				if (listaDerivadas != null && listaDerivadas.size() > 0) {
					globalHitosLogdService
							.setListUsuariosDerivados(listaDerivadas);
					this.setListUsuariosDerivados(listaDerivadas);
				}
			}

		} catch (Exception e) {
			log.error("Error, al desplegar los datos de la solicitud",
					e.getMessage());
		}

	}

	public void sacarUsuariosSegunSucursalesSeleccionadas() {
		int contador = 0;
		StringBuffer cadena = new StringBuffer();
		try {
			if (listaSucursalsSeleccionados != null
					&& listaSucursalsSeleccionados.size() != 0) {
				int cantidad = listaSucursalsSeleccionados.size();
				for (Sucursal s : listaSucursalsSeleccionados) {
					if ((contador + 1) == cantidad) {
						cadena.append("'");
						cadena.append(s.getCodigo());
						cadena.append("'");
					} else {
						cadena.append("'");
						cadena.append(s.getCodigo());
						cadena.append("'");
						cadena.append(",");
						contador++;
					}
				}
				log.debug("cadena #0", cadena.toString());

				if (cadena != null && !"".equals(cadena.toString())) {
					listaUsuariosegurs = scoringService
							.getListaUsuariosSegunMatrizResponsabilidades(cadena
									.toString());
				}
			} else {
				listaUsuariosegurs = new ArrayList<Usuariosegur>();
			}
		} catch (Exception e) {
			log.error(
					"error, al sacar los usuarios de segun responsabilidad #0",
					e.getMessage());
		}

	}

	public void getObtenerDatosParaTrabajoMatriz(String usuarios, String fechaInicio, String fechaTermino, String tipoSolicitudes, 
			String canales, String negocios, String sucursales){
		long idSolicitud = 0;
		String hora1 = ""; //03:15:20
		String hora2 = ""; //04:20:45
		String fechaInicial = "";
		String fechaFinal = "";
		BigDecimal lineaCredito = new BigDecimal(0);
		List<String> listaFeriados;
		HitosDTO enSession = null;
		LcredTipoSolicitud tipo = null;
		Canal canal = null;
		TiempoMontoType tiempoMontoType = null; 
		Medicion medicion = null;
		int cont = 0;
		int hora = 0;
		long horaTotales = 0;
		totalSolicitudes = (long)0;
		cumplidas = (long)0;
		nocumplidas = (long)0;
		porcentajeCumplidas = (double)0;
		porcentajeNoCumplidas = (double)0;
		boolean contada = false;
		try{
			/*listado de feriados*/
			listaFeriados = scoringService.getConsultaFeriadosSegunRangoFechas(fechaInicio, fechaTermino);
			/*listado de tiempos de la solicitudes*/
			List<HitosDTO> listaHitos = scoringService.getConsultaMatrizResponsabilidades(usuarios, fechaInicio, fechaTermino, tipoSolicitudes, canales, negocios, sucursales);

			/*Realizar el trabajo de evaluacion */
			if(listaHitos != null){
				for(HitosDTO obj :listaHitos){
					if(idSolicitud ==0){
						idSolicitud = obj.getIdSolicitud();
						hora1 = obj.getHoraCreacion();
						lineaCredito = obj.getLineaCreditoDisponible();
						fechaInicial = obj.getFechaCreacion();
						enSession = obj;
					}else{
						if(obj.getIdSolicitud()==idSolicitud){
							hora2 = obj.getHoraHitos();
							lineaCredito = obj.getLineaCreditoDisponible();
							fechaFinal = obj.getFechaHitos();
							enSession = obj;
						}else{
							totalSolicitudes++;
							if(idSolicitud == 32265 || idSolicitud ==32272 || 
									idSolicitud ==32514 || idSolicitud == 32664){
								log.debug("idSolicitud : #0", idSolicitud);
							}
							if(fechaInicial.equals(fechaFinal)){
								String[] h1 = hora1.split(":");
								String[] h2 = hora2.split(":");
								int resto = 0;
								int segundo =  Integer.parseInt(h2[2])-Integer.parseInt(h1[2]);
								if (segundo < 0){
								   resto = -1;
								   segundo = 60 - segundo;
								}
								int minuto = (Integer.parseInt(h2[1]) - Integer.parseInt(h1[1])) - resto;
								resto = 0;
								if (minuto < 0){
								   minuto = 60 - minuto;
								   resto = -1;
								}
								hora = (Integer.parseInt(h2[0]) - Integer.parseInt(h1[0])) - resto;

								System.out.println("Diferencia o Respuesta= "+hora+":"+minuto+":"+segundo);										
								
							}else{
							      StringTokenizer stM = new StringTokenizer(fechaFinal,"-");
							      //int re = FM - fm;
							      int diaM = Integer.parseInt(stM.nextToken());     
							      int mesM = Integer.parseInt(stM.nextToken());     
							      int anoM = Integer.parseInt(stM.nextToken());     
							      
							      StringTokenizer st = new StringTokenizer(fechaInicial,"-");
							      //int re = FM - fm;
							      int diam = Integer.parseInt(st.nextToken());     
							      int mesm = Integer.parseInt(st.nextToken());     
							      int anom = Integer.parseInt(st.nextToken());     
							      
							      
							      int FMAYOR = Integer.parseInt(anoM+""+mesM+""+diaM);
							      int FMENOR = Integer.parseInt(anom+""+mesm+""+diam);
							      
							      
							      if(FMAYOR >= FMENOR){
							         int mesMaux = mesM;
							         int diaMaux = diaM;
							         
							      
							         while(anom<=anoM){
								          if(anom<anoM){
								            mesM = 12;
								             diaM = 31;
								          }else{
								             mesM = mesMaux;
								             diaM = diaMaux;
								          }
								          while(mesm <= mesM){
									           int DiasDelMes = 0;
									           if(mesm == mesM){
									              DiasDelMes = diaM;
									           }else{
									             DiasDelMes = DiasDelMes(anom,mesm);
									           }  
								               while(diam <= DiasDelMes){
								                 cont++;
								                 diam++;
								               }
								               diam = 1;
								               mesm ++;
								           }   
								           mesm = 1;
								           anom++;
							         }
								     System.out.println ("Numero de dias contados -> "+(cont-1));
								     System.out.println ("Con el dia de inicio incluido serian -> "+cont);
								     /*calcular las hora entre los dias.*/
								     
								     
								     
							      }else{
							         System.out.println ("Error....!!La fecha Menor supera a la fecha Mayor");
							      }								
							  }
							

							/*evaluar el tipo de solicitud */
							if("1C".equals(enSession.getTipoSolicitud()) ||"1N".equals(enSession.getTipoSolicitud()) ){
								horaTotales = 0;
								if(lineaCredito.longValue() < 0 ){
									System.out.println("Con Sobre Giro");
									tiempoMontoType = TiempoMontoType.MONTOS;
									
									if("1C".equals(enSession.getTipoSolicitud())){
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("11");
									}else{
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("12"); 
									}
									medicion = scoringService.obtenerObjetoMedicion(2);
									
									if(enSession.getCanal().KX.equals(TipoCuentasKupferType.KX)){
										canal = scoringService.obtenerCanalForCodigo(1);
									}else if(enSession.getCanal().MX.equals(TipoCuentasKupferType.MX)){
										canal = scoringService.obtenerCanalForCodigo(3);
									}else if(enSession.getCanal().GC.equals(TipoCuentasKupferType.GC)){
										canal = scoringService.obtenerCanalForCodigo(2);
									}
									
									List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, canal, tiempoMontoType, medicion);
									if(fechaInicial.equals(fechaFinal)){
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												contada = true;
												nocumplidas++;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito.longValue() < 0){/*con sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}else{/*Sin sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}
											if(hora<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												contada = true;
												nocumplidas++;
											}											
										}
									}else{
										
										Calendar cal = Calendar.getInstance();
										SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
										Date fecha = formatoDelTexto.parse(fechaInicial);
										cal.setTime(fecha);
										String convertido = "";
										if(cont > 1){
											for(int i = 0; i< cont; i++){
												cal.add(Calendar.DATE, i);
												DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
												convertido = fechaAux.format(cal.getTime());
												System.out.println(convertido);
												if(listaFeriados.contains(convertido)== false){
													horaTotales = horaTotales+9;
												}
											}
											
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}											
										}else{
											horaTotales = 9;
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}												
										}
									}
								}else{
									System.out.println("Sin Sobre Giro");
									tiempoMontoType = TiempoMontoType.DIAS;
									if("1C".equals(enSession.getTipoSolicitud())){
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("11");
									}else{
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("12"); 
									}
									medicion = scoringService.obtenerObjetoMedicion(1);
									
									List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, null, tiempoMontoType, medicion);	
									if(fechaInicial.equals(fechaFinal)){
										
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito.longValue() < 0){/*con sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}else{/*Sin sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}
											if(hora<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}
									}else{
										
										Calendar cal = Calendar.getInstance();
										SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
										Date fecha = formatoDelTexto.parse(fechaInicial);
										cal.setTime(fecha);
										String convertido = "";
										if(cont > 1){
											for(int i = 0; i< cont; i++){
												cal.add(Calendar.DATE, i);
												DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
												convertido = fechaAux.format(cal.getTime());
												System.out.println(convertido);
												if(listaFeriados.contains(convertido)== false){
													horaTotales = horaTotales+9;
												}
											}
											
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}	
										}else{
											horaTotales = 9;
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}												
										}
									}									
								}
								
							}else if("2R".equals(enSession.getTipoSolicitud())){
								horaTotales = 0;
								System.out.println("Con Sobre Giro");
								tiempoMontoType = TiempoMontoType.MONTOS;

								tipo = scoringService.obtenerObjetoLcredTipoSolicitud("23");
								
								medicion = scoringService.obtenerObjetoMedicion(2);
								
								if(enSession.getCanal().KX.equals(TipoCuentasKupferType.KX)){
									canal = scoringService.obtenerCanalForCodigo(1);
								}else if(enSession.getCanal().MX.equals(TipoCuentasKupferType.MX)){
									canal = scoringService.obtenerCanalForCodigo(3);
								}else if(enSession.getCanal().GC.equals(TipoCuentasKupferType.GC)){
									canal = scoringService.obtenerCanalForCodigo(2);
								}
								
								List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, canal, tiempoMontoType, medicion);
								if(fechaInicial.equals(fechaFinal)){
									if(lista != null && lista.size() == 1){
										CondicionTipoSolicitudRango nuevo = lista.get(0);
										if(hora<=nuevo.getHoras().longValue()){
											cumplidas++;
											contada = true;
										}else{
											nocumplidas++;
											contada = true;
										}											
									}else if(lista != null && lista.size() > 1){
										CondicionTipoSolicitudRango paraCaclulo = null;
										if(lineaCredito != null){
											if(lineaCredito.longValue() < 0){
												lineaCredito = new BigDecimal(lineaCredito.longValue() * -1);
											}											
											 long valorReal = lineaCredito.longValue();
											
											for(CondicionTipoSolicitudRango nuevo : lista){
												if(nuevo.getMedicionCanalRango().getValorInicial().longValue() <= valorReal &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() >= valorReal){
													    paraCaclulo = nuevo;
													    break;
												}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() <= lineaCredito.longValue()){
													    paraCaclulo = nuevo;
													    break;													
												}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() >= lineaCredito.longValue()){
												        paraCaclulo = nuevo;
												        break;														
												}
											}
										}
										if(hora<=paraCaclulo.getHoras().longValue()){
											cumplidas++;
											contada = true;
										}else{
											nocumplidas++;
											contada = true;
										}											
									}
								}else{
									Calendar cal = Calendar.getInstance();
									SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
									Date fecha = formatoDelTexto.parse(fechaInicial);
									cal.setTime(fecha);
									String convertido = "";
									if(cont > 1){
										for(int i = 0; i< cont; i++){
											cal.add(Calendar.DATE, i);
											DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
											convertido = fechaAux.format(cal.getTime());
											System.out.println(convertido);
											if(listaFeriados.contains(convertido)== false){
												horaTotales = horaTotales+9;
											}
										}
										
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito != null){
												
												if(lineaCredito.longValue() < 0){
													lineaCredito = new BigDecimal(lineaCredito.longValue() * -1);
												}											
												 long valorReal = lineaCredito.longValue();												
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() <= valorReal &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= valorReal){
														    paraCaclulo = nuevo;
														    break;
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= lineaCredito.longValue()){
														    paraCaclulo = nuevo;
														    break;													
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= lineaCredito.longValue()){
													        paraCaclulo = nuevo;
													        break;														
													}
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}	
									}else{
										horaTotales = 9;
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito != null){
												if(lineaCredito.longValue() < 0){
													lineaCredito = new BigDecimal(lineaCredito.longValue() * -1);
												}											
												 long valorReal = lineaCredito.longValue();												
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() <= valorReal &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= valorReal){
														    paraCaclulo = nuevo;
														    break;
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= lineaCredito.longValue()){
														    paraCaclulo = nuevo;
														    break;													
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= lineaCredito.longValue()){
													        paraCaclulo = nuevo;
													        break;														
													}
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}											
									}
								}
							}else{
								tiempoMontoType = TiempoMontoType.DIAS;
								if("2I".equals(enSession.getTipoSolicitud())){
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("21");
								}else if("2N".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("22");
								}else if("3R".equals(enSession.getTipoSolicitud()) || "3P".equals(enSession.getTipoSolicitud()) || "3RP".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("31");									
								}else if("4B".equals(enSession.getTipoSolicitud()) || "4D".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("41");
								}else if("4DM".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("43");
								}else if("4PR".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("44");
								}
								medicion = scoringService.obtenerObjetoMedicion(3);
								
								List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, null, tiempoMontoType, medicion);
								if(fechaInicial.equals(fechaFinal)){

									if(lista != null && lista.size() == 1){
										CondicionTipoSolicitudRango nuevo = lista.get(0);
										if(hora<=nuevo.getHoras().longValue()){
											cumplidas++;
											contada = true;
										}else{
											nocumplidas++;
											contada = true;
										}											
									}else if(lista != null && lista.size() > 1){
										CondicionTipoSolicitudRango paraCaclulo = null;
										lineaCredito = new BigDecimal(lineaCredito.longValue()*-1);
										for(CondicionTipoSolicitudRango nuevo : lista){
											if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
													nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 63){
												    paraCaclulo = nuevo;
												    break;
											}
										}
										if(paraCaclulo != null){
											if(hora<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}	
										}else{
											nocumplidas++;
											contada = true;
										}
									}									
								
								}else{
									
									Calendar cal = Calendar.getInstance();
									SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
									Date fecha = formatoDelTexto.parse(fechaInicial);
									cal.setTime(fecha);
									String convertido = "";
									if(cont > 1){
										for(int i = 0; i< cont; i++){
											cal.add(Calendar.DATE, i);
											DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
											convertido = fechaAux.format(cal.getTime());
											System.out.println(convertido);
											if(listaFeriados.contains(convertido)== false){
												horaTotales = horaTotales+9;
											}
										}
										
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											lineaCredito = new BigDecimal(lineaCredito.longValue()*-1);
											for(CondicionTipoSolicitudRango nuevo : lista){
												if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 63){
													    paraCaclulo = nuevo;
													    break;
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}
										}											
										
										

									}else{
										horaTotales = 9;
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											lineaCredito = new BigDecimal(lineaCredito.longValue()*-1);
											for(CondicionTipoSolicitudRango nuevo : lista){
												if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 63){
													    paraCaclulo = nuevo;
													    break;
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}
										}											
									}
								}
							}
							
							if(contada==false){
								log.debug("idSolicitud : #0", idSolicitud);
								log.debug("totales : #0", totalSolicitudes);
								log.debug("cumplidas : #0", cumplidas);
								log.debug("No cumplidas : #0", nocumplidas);
								
							}else{
								contada = false;
							}

							/*variables seteadas para el objeto sigueinte*/
							idSolicitud = obj.getIdSolicitud();
							hora1 = obj.getHoraCreacion();
							lineaCredito = obj.getLineaCreditoDisponible();
							fechaInicial = obj.getFechaCreacion();
							fechaFinal = obj.getFechaHitos();
							enSession = obj;
							hora = 0;
							cont = 0;
							horaTotales = 0;
							

						}
					}
				}
				log.debug("Si trae si problema los datos.");
			}
			
			porcentajeCumplidas = (cumplidas * 100.00)/totalSolicitudes;
			porcentajeNoCumplidas = (nocumplidas * 100.00)/totalSolicitudes;
			
			
		}catch (Exception e) {
			log.error("Error, no se puede realizar la la verificacion de existecia de negocio ", e.getMessage());
		}
		
	}

	public PorcentajeDTO getObtenerDatosParaTrabajoUsuario(String usuario, String fechaInicio, String fechaTermino, String tipoSolicitudes, 
			String canales, String negocios, String sucursales){
		PorcentajeDTO porcentajeFinal = new PorcentajeDTO();
		
		long idSolicitud = 0;
		String hora1 = ""; //03:15:20
		String hora2 = ""; //04:20:45
		String fechaInicial = "";
		String fechaFinal = "";
		BigDecimal lineaCredito = new BigDecimal(0);
		List<String> listaFeriados;
		HitosDTO enSession = null;
		LcredTipoSolicitud tipo = null;
		Canal canal = null;
		TiempoMontoType tiempoMontoType = null; 
		Medicion medicion = null;
		int cont = 0;
		int hora = 0;
		long horaTotales = 0;
		Long totalSolicitudes=(long)0;
		Long cumplidas=(long)0;
		Long nocumplidas=(long)0;
		boolean contada = false;
		try{
			/*listado de feriados*/
			listaFeriados = scoringService.getConsultaFeriadosSegunRangoFechas(fechaInicio, fechaTermino);
			/*listado de tiempos de la solicitudes*/
			List<HitosDTO> listaHitos = scoringService.getConsultaMatrizResponsabilidades(usuario, fechaInicio, fechaTermino, tipoSolicitudes, canales, negocios, sucursales);

			/*Realizar el trabajo de evaluacion */
			if(listaHitos != null){
				for(HitosDTO obj :listaHitos){
					if(idSolicitud ==0){
						idSolicitud = obj.getIdSolicitud();
						hora1 = obj.getHoraCreacion();
						lineaCredito = obj.getLineaCreditoDisponible();
						fechaInicial = obj.getFechaCreacion();
						enSession = obj;
					}else{
						if(obj.getIdSolicitud()==idSolicitud){
							hora2 = obj.getHoraHitos();
							lineaCredito = obj.getLineaCreditoDisponible();
							fechaFinal = obj.getFechaHitos();
							enSession = obj;
						}else{
							totalSolicitudes++;
							if(idSolicitud == 32265 || idSolicitud ==32272 || 
									idSolicitud ==32514 || idSolicitud == 32664){
								log.debug("idSolicitud : #0", idSolicitud);
							}
							if(fechaInicial.equals(fechaFinal)){
								String[] h1 = hora1.split(":");
								String[] h2 = hora2.split(":");
								int resto = 0;
								int segundo =  Integer.parseInt(h2[2])-Integer.parseInt(h1[2]);
								if (segundo < 0){
								   resto = -1;
								   segundo = 60 - segundo;
								}
								int minuto = (Integer.parseInt(h2[1]) - Integer.parseInt(h1[1])) - resto;
								resto = 0;
								if (minuto < 0){
								   minuto = 60 - minuto;
								   resto = -1;
								}
								hora = (Integer.parseInt(h2[0]) - Integer.parseInt(h1[0])) - resto;

								System.out.println("Diferencia o Respuesta= "+hora+":"+minuto+":"+segundo);										
								
							}else{
							      StringTokenizer stM = new StringTokenizer(fechaFinal,"-");
							      //int re = FM - fm;
							      int diaM = Integer.parseInt(stM.nextToken());     
							      int mesM = Integer.parseInt(stM.nextToken());     
							      int anoM = Integer.parseInt(stM.nextToken());     
							      
							      StringTokenizer st = new StringTokenizer(fechaInicial,"-");
							      //int re = FM - fm;
							      int diam = Integer.parseInt(st.nextToken());     
							      int mesm = Integer.parseInt(st.nextToken());     
							      int anom = Integer.parseInt(st.nextToken());     
							      
							      
							      int FMAYOR = Integer.parseInt(anoM+""+mesM+""+diaM);
							      int FMENOR = Integer.parseInt(anom+""+mesm+""+diam);
							      
							      
							      if(FMAYOR >= FMENOR){
							         int mesMaux = mesM;
							         int diaMaux = diaM;
							         
							      
							         while(anom<=anoM){
								          if(anom<anoM){
								            mesM = 12;
								             diaM = 31;
								          }else{
								             mesM = mesMaux;
								             diaM = diaMaux;
								          }
								          while(mesm <= mesM){
									           int DiasDelMes = 0;
									           if(mesm == mesM){
									              DiasDelMes = diaM;
									           }else{
									             DiasDelMes = DiasDelMes(anom,mesm);
									           }  
								               while(diam <= DiasDelMes){
								                 cont++;
								                 diam++;
								               }
								               diam = 1;
								               mesm ++;
								           }   
								           mesm = 1;
								           anom++;
							         }
								     System.out.println ("Numero de dias contados -> "+(cont-1));
								     System.out.println ("Con el dia de inicio incluido serian -> "+cont);
								     /*calcular las hora entre los dias.*/
								     
								     
								     
							      }else{
							         System.out.println ("Error....!!La fecha Menor supera a la fecha Mayor");
							      }								
							  }
							

							/*evaluar el tipo de solicitud */
							if("1C".equals(enSession.getTipoSolicitud()) ||"1N".equals(enSession.getTipoSolicitud()) ){
								horaTotales = 0;
								if(lineaCredito.longValue() < 0 ){
									System.out.println("Con Sobre Giro");
									tiempoMontoType = TiempoMontoType.MONTOS;
									
									if("1C".equals(enSession.getTipoSolicitud())){
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("11");
									}else{
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("12"); 
									}
									medicion = scoringService.obtenerObjetoMedicion(2);
									
									if(enSession.getCanal().KX.equals(TipoCuentasKupferType.KX)){
										canal = scoringService.obtenerCanalForCodigo(1);
									}else if(enSession.getCanal().MX.equals(TipoCuentasKupferType.MX)){
										canal = scoringService.obtenerCanalForCodigo(3);
									}else if(enSession.getCanal().GC.equals(TipoCuentasKupferType.GC)){
										canal = scoringService.obtenerCanalForCodigo(2);
									}
									
									List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, canal, tiempoMontoType, medicion);
									if(fechaInicial.equals(fechaFinal)){
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												contada = true;
												nocumplidas++;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito.longValue() < 0){/*con sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}else{/*Sin sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}
											if(hora<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												contada = true;
												nocumplidas++;
											}											
										}
									}else{
										
										Calendar cal = Calendar.getInstance();
										SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
										Date fecha = formatoDelTexto.parse(fechaInicial);
										cal.setTime(fecha);
										String convertido = "";
										if(cont > 1){
											for(int i = 0; i< cont; i++){
												cal.add(Calendar.DATE, i);
												DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
												convertido = fechaAux.format(cal.getTime());
												System.out.println(convertido);
												if(listaFeriados.contains(convertido)== false){
													horaTotales = horaTotales+9;
												}
											}
											
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}											
										}else{
											horaTotales = 9;
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}												
										}
									}
								}else{
									System.out.println("Sin Sobre Giro");
									tiempoMontoType = TiempoMontoType.DIAS;
									if("1C".equals(enSession.getTipoSolicitud())){
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("11");
									}else{
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("12"); 
									}
									medicion = scoringService.obtenerObjetoMedicion(1);
									
									List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, null, tiempoMontoType, medicion);	
									if(fechaInicial.equals(fechaFinal)){
										
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito.longValue() < 0){/*con sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}else{/*Sin sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}
											if(hora<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}
									}else{
										
										Calendar cal = Calendar.getInstance();
										SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
										Date fecha = formatoDelTexto.parse(fechaInicial);
										cal.setTime(fecha);
										String convertido = "";
										if(cont > 1){
											for(int i = 0; i< cont; i++){
												cal.add(Calendar.DATE, i);
												DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
												convertido = fechaAux.format(cal.getTime());
												System.out.println(convertido);
												if(listaFeriados.contains(convertido)== false){
													horaTotales = horaTotales+9;
												}
											}
											
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}	
										}else{
											horaTotales = 9;
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}												
										}
									}									
								}
								
							}else if("2R".equals(enSession.getTipoSolicitud())){
								horaTotales = 0;
								System.out.println("Con Sobre Giro");
								tiempoMontoType = TiempoMontoType.MONTOS;

								tipo = scoringService.obtenerObjetoLcredTipoSolicitud("23");
								
								medicion = scoringService.obtenerObjetoMedicion(2);
								
								if(enSession.getCanal().KX.equals(TipoCuentasKupferType.KX)){
									canal = scoringService.obtenerCanalForCodigo(1);
								}else if(enSession.getCanal().MX.equals(TipoCuentasKupferType.MX)){
									canal = scoringService.obtenerCanalForCodigo(3);
								}else if(enSession.getCanal().GC.equals(TipoCuentasKupferType.GC)){
									canal = scoringService.obtenerCanalForCodigo(2);
								}
								
								List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, canal, tiempoMontoType, medicion);
								if(fechaInicial.equals(fechaFinal)){
									if(lista != null && lista.size() == 1){
										CondicionTipoSolicitudRango nuevo = lista.get(0);
										if(hora<=nuevo.getHoras().longValue()){
											cumplidas++;
											contada = true;
										}else{
											nocumplidas++;
											contada = true;
										}											
									}else if(lista != null && lista.size() > 1){
										CondicionTipoSolicitudRango paraCaclulo = null;
										if(lineaCredito != null){
											if(lineaCredito.longValue() < 0){
												lineaCredito = new BigDecimal(lineaCredito.longValue() * -1);
											}											
											 long valorReal = lineaCredito.longValue();
											
											for(CondicionTipoSolicitudRango nuevo : lista){
												if(nuevo.getMedicionCanalRango().getValorInicial().longValue() <= valorReal &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() >= valorReal){
													    paraCaclulo = nuevo;
													    break;
												}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() <= lineaCredito.longValue()){
													    paraCaclulo = nuevo;
													    break;													
												}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() >= lineaCredito.longValue()){
												        paraCaclulo = nuevo;
												        break;														
												}
											}
										}
										if(hora<=paraCaclulo.getHoras().longValue()){
											cumplidas++;
											contada = true;
										}else{
											nocumplidas++;
											contada = true;
										}											
									}
								}else{
									Calendar cal = Calendar.getInstance();
									SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
									Date fecha = formatoDelTexto.parse(fechaInicial);
									cal.setTime(fecha);
									String convertido = "";
									if(cont > 1){
										for(int i = 0; i< cont; i++){
											cal.add(Calendar.DATE, i);
											DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
											convertido = fechaAux.format(cal.getTime());
											System.out.println(convertido);
											if(listaFeriados.contains(convertido)== false){
												horaTotales = horaTotales+9;
											}
										}
										
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito != null){
												
												if(lineaCredito.longValue() < 0){
													lineaCredito = new BigDecimal(lineaCredito.longValue() * -1);
												}											
												 long valorReal = lineaCredito.longValue();												
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() <= valorReal &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= valorReal){
														    paraCaclulo = nuevo;
														    break;
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= lineaCredito.longValue()){
														    paraCaclulo = nuevo;
														    break;													
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= lineaCredito.longValue()){
													        paraCaclulo = nuevo;
													        break;														
													}
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}	
									}else{
										horaTotales = 9;
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito != null){
												if(lineaCredito.longValue() < 0){
													lineaCredito = new BigDecimal(lineaCredito.longValue() * -1);
												}											
												 long valorReal = lineaCredito.longValue();												
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() <= valorReal &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= valorReal){
														    paraCaclulo = nuevo;
														    break;
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= lineaCredito.longValue()){
														    paraCaclulo = nuevo;
														    break;													
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= lineaCredito.longValue()){
													        paraCaclulo = nuevo;
													        break;														
													}
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}											
									}
								}
							}else{
								tiempoMontoType = TiempoMontoType.DIAS;
								if("2I".equals(enSession.getTipoSolicitud())){
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("21");
								}else if("2N".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("22");
								}else if("3R".equals(enSession.getTipoSolicitud()) || "3P".equals(enSession.getTipoSolicitud()) || "3RP".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("31");									
								}else if("4B".equals(enSession.getTipoSolicitud()) || "4D".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("41");
								}else if("4DM".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("43");
								}else if("4PR".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("44");
								}
								medicion = scoringService.obtenerObjetoMedicion(3);
								
								List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, null, tiempoMontoType, medicion);
								if(fechaInicial.equals(fechaFinal)){

									if(lista != null && lista.size() == 1){
										CondicionTipoSolicitudRango nuevo = lista.get(0);
										if(hora<=nuevo.getHoras().longValue()){
											cumplidas++;
											contada = true;
										}else{
											nocumplidas++;
											contada = true;
										}											
									}else if(lista != null && lista.size() > 1){
										CondicionTipoSolicitudRango paraCaclulo = null;
										lineaCredito = new BigDecimal(lineaCredito.longValue()*-1);
										for(CondicionTipoSolicitudRango nuevo : lista){
											if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
													nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 63){
												    paraCaclulo = nuevo;
												    break;
											}
										}
										if(paraCaclulo != null){
											if(hora<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}	
										}else{
											nocumplidas++;
											contada = true;
										}
									}									
								
								}else{
									
									Calendar cal = Calendar.getInstance();
									SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
									Date fecha = formatoDelTexto.parse(fechaInicial);
									cal.setTime(fecha);
									String convertido = "";
									if(cont > 1){
										for(int i = 0; i< cont; i++){
											cal.add(Calendar.DATE, i);
											DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
											convertido = fechaAux.format(cal.getTime());
											System.out.println(convertido);
											if(listaFeriados.contains(convertido)== false){
												horaTotales = horaTotales+9;
											}
										}
										
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											lineaCredito = new BigDecimal(lineaCredito.longValue()*-1);
											for(CondicionTipoSolicitudRango nuevo : lista){
												if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 63){
													    paraCaclulo = nuevo;
													    break;
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}
										}											
										
										

									}else{
										horaTotales = 9;
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											lineaCredito = new BigDecimal(lineaCredito.longValue()*-1);
											for(CondicionTipoSolicitudRango nuevo : lista){
												if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 63){
													    paraCaclulo = nuevo;
													    break;
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}
										}											
									}
								}
							}
							
							if(contada==false){
								log.debug("idSolicitud : #0", idSolicitud);
								log.debug("totales : #0", totalSolicitudes);
								log.debug("cumplidas : #0", cumplidas);
								log.debug("No cumplidas : #0", nocumplidas);
								
							}else{
								contada = false;
							}

							/*variables seteadas para el objeto sigueinte*/
							idSolicitud = obj.getIdSolicitud();
							hora1 = obj.getHoraCreacion();
							lineaCredito = obj.getLineaCreditoDisponible();
							fechaInicial = obj.getFechaCreacion();
							fechaFinal = obj.getFechaHitos();
							enSession = obj;
							hora = 0;
							cont = 0;
							horaTotales = 0;
							

						}
					}
				}
				log.debug("Si trae si problema los datos.");
			}
			
		
			/*setear valors al objeto procentaje*/
			if(porcentajeFinal != null){
				porcentajeFinal.setUsuario(usuario);
				porcentajeFinal.setCumplidas(cumplidas);
				porcentajeFinal.setNoCumplidas(nocumplidas);
				porcentajeFinal.setTotalSolicitudesIndividual(totalSolicitudes);
				double dividir = (porcentajeFinal.getCumplidas()*100);
				if(dividir != 0){
					double dividir2 = (dividir/porcentajeFinal.getTotalSolicitudesIndividual());
					porcentajeFinal.setPorcentajes(dividir2);					
				}else{
					porcentajeFinal.setPorcentajes(0);	
				}

			}
		}catch (Exception e) {
			log.error("Error, se cae al verificar lka informacion de trabajo del usuario #0 y el error es el siguiente #1",usuario , e.getMessage());
		}
	
		return porcentajeFinal;
	}

	public PorcentajeDTO getObtenerDatosParaTrabajoUsuarioAndTipoSolicitud(String usuario, String fechaInicio, String fechaTermino, LcredTipoSolicitud tipoSolicitudes, 
			String canales, String negocios, String sucursales){
		PorcentajeDTO porcentajeFinal = new PorcentajeDTO();
		long idSolicitud = 0;
		String hora1 = ""; //03:15:20
		String hora2 = ""; //04:20:45
		String fechaInicial = "";
		String fechaFinal = "";
		BigDecimal lineaCredito = new BigDecimal(0);
		List<String> listaFeriados;
		HitosDTO enSession = null;
		LcredTipoSolicitud tipo = null;
		Canal canal = null;
		TiempoMontoType tiempoMontoType = null; 
		Medicion medicion = null;
		int cont = 0;
		int hora = 0;
		long horaTotales = 0;
		Long totalSolicitudes=(long)0;
		Long cumplidas=(long)0;
		Long nocumplidas=(long)0;
		boolean contada = false;
		String tipoSol = "";
		try{
			/*listado de feriados*/
			listaFeriados = scoringService.getConsultaFeriadosSegunRangoFechas(fechaInicio, fechaTermino);
			
			switch (Integer.parseInt(tipoSolicitudes.getCodTipoSolicitud().trim())) {
			case 11:
				tipoSol = "'1N'";
				break;
			case 12:
				tipoSol = "'1C'";
				break;
			case 21:
				tipoSol = "'2I'";
				break;
			case 22:
				tipoSol = "'2N'";
				break;
			case 23:
				tipoSol = "'2R'";
				break;
			case 31:
				tipoSol = "'3RP','3P','3R'";
				break;
			case 41:
				tipoSol = "'4B','4D'";
				break;
			case 43:
				tipoSol = "'4DM'";
				break;
			case 44:
				tipoSol = "'4PR'";
				break;
			default:
				break;
			}
			
			
			/*listado de tiempos de la solicitudes*/
			List<HitosDTO> listaHitos = scoringService.getConsultaMatrizResponsabilidades(usuario, fechaInicio, fechaTermino, tipoSol, canales, negocios, sucursales);

			/*Realizar el trabajo de evaluacion */
			if(listaHitos != null){
				for(HitosDTO obj :listaHitos){
					if(idSolicitud ==0){
						idSolicitud = obj.getIdSolicitud();
						hora1 = obj.getHoraCreacion();
						lineaCredito = obj.getLineaCreditoDisponible();
						fechaInicial = obj.getFechaCreacion();
						enSession = obj;
					}else{
						if(obj.getIdSolicitud()==idSolicitud){
							hora2 = obj.getHoraHitos();
							lineaCredito = obj.getLineaCreditoDisponible();
							fechaFinal = obj.getFechaHitos();
							enSession = obj;
						}else{
							totalSolicitudes++;
							if(idSolicitud == 32265 || idSolicitud ==32272 || 
									idSolicitud ==32514 || idSolicitud == 32664){
								log.debug("idSolicitud : #0", idSolicitud);
							}
							if(fechaInicial.equals(fechaFinal)){
								String[] h1 = hora1.split(":");
								String[] h2 = hora2.split(":");
								int resto = 0;
								int segundo =  Integer.parseInt(h2[2])-Integer.parseInt(h1[2]);
								if (segundo < 0){
								   resto = -1;
								   segundo = 60 - segundo;
								}
								int minuto = (Integer.parseInt(h2[1]) - Integer.parseInt(h1[1])) - resto;
								resto = 0;
								if (minuto < 0){
								   minuto = 60 - minuto;
								   resto = -1;
								}
								hora = (Integer.parseInt(h2[0]) - Integer.parseInt(h1[0])) - resto;

								System.out.println("Diferencia o Respuesta= "+hora+":"+minuto+":"+segundo);										
								
							}else{
							      StringTokenizer stM = new StringTokenizer(fechaFinal,"-");
							      //int re = FM - fm;
							      int diaM = Integer.parseInt(stM.nextToken());     
							      int mesM = Integer.parseInt(stM.nextToken());     
							      int anoM = Integer.parseInt(stM.nextToken());     
							      
							      StringTokenizer st = new StringTokenizer(fechaInicial,"-");
							      //int re = FM - fm;
							      int diam = Integer.parseInt(st.nextToken());     
							      int mesm = Integer.parseInt(st.nextToken());     
							      int anom = Integer.parseInt(st.nextToken());     
							      
							      
							      int FMAYOR = Integer.parseInt(anoM+""+mesM+""+diaM);
							      int FMENOR = Integer.parseInt(anom+""+mesm+""+diam);
							      
							      
							      if(FMAYOR >= FMENOR){
							         int mesMaux = mesM;
							         int diaMaux = diaM;
							         
							      
							         while(anom<=anoM){
								          if(anom<anoM){
								            mesM = 12;
								             diaM = 31;
								          }else{
								             mesM = mesMaux;
								             diaM = diaMaux;
								          }
								          while(mesm <= mesM){
									           int DiasDelMes = 0;
									           if(mesm == mesM){
									              DiasDelMes = diaM;
									           }else{
									             DiasDelMes = DiasDelMes(anom,mesm);
									           }  
								               while(diam <= DiasDelMes){
								                 cont++;
								                 diam++;
								               }
								               diam = 1;
								               mesm ++;
								           }   
								           mesm = 1;
								           anom++;
							         }
								     System.out.println ("Numero de dias contados -> "+(cont-1));
								     System.out.println ("Con el dia de inicio incluido serian -> "+cont);
								     /*calcular las hora entre los dias.*/
								     
								     
								     
							      }else{
							         System.out.println ("Error....!!La fecha Menor supera a la fecha Mayor");
							      }								
							  }
							

							/*evaluar el tipo de solicitud */
							if("1C".equals(enSession.getTipoSolicitud()) ||"1N".equals(enSession.getTipoSolicitud()) ){
								horaTotales = 0;
								if(lineaCredito.longValue() < 0 ){
									System.out.println("Con Sobre Giro");
									tiempoMontoType = TiempoMontoType.MONTOS;
									
									if("1C".equals(enSession.getTipoSolicitud())){
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("11");
									}else{
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("12"); 
									}
									medicion = scoringService.obtenerObjetoMedicion(2);
									
									if(enSession.getCanal().KX.equals(TipoCuentasKupferType.KX)){
										canal = scoringService.obtenerCanalForCodigo(1);
									}else if(enSession.getCanal().MX.equals(TipoCuentasKupferType.MX)){
										canal = scoringService.obtenerCanalForCodigo(3);
									}else if(enSession.getCanal().GC.equals(TipoCuentasKupferType.GC)){
										canal = scoringService.obtenerCanalForCodigo(2);
									}
									
									List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, canal, tiempoMontoType, medicion);
									if(fechaInicial.equals(fechaFinal)){
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												contada = true;
												nocumplidas++;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito.longValue() < 0){/*con sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}else{/*Sin sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}
											if(hora<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												contada = true;
												nocumplidas++;
											}											
										}
									}else{
										
										Calendar cal = Calendar.getInstance();
										SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
										Date fecha = formatoDelTexto.parse(fechaInicial);
										cal.setTime(fecha);
										String convertido = "";
										if(cont > 1){
											for(int i = 0; i< cont; i++){
												cal.add(Calendar.DATE, i);
												DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
												convertido = fechaAux.format(cal.getTime());
												System.out.println(convertido);
												if(listaFeriados.contains(convertido)== false){
													horaTotales = horaTotales+9;
												}
											}
											
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}											
										}else{
											horaTotales = 9;
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}												
										}
									}
								}else{
									System.out.println("Sin Sobre Giro");
									tiempoMontoType = TiempoMontoType.DIAS;
									if("1C".equals(enSession.getTipoSolicitud())){
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("11");
									}else{
										tipo = scoringService.obtenerObjetoLcredTipoSolicitud("12"); 
									}
									medicion = scoringService.obtenerObjetoMedicion(1);
									
									List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, null, tiempoMontoType, medicion);	
									if(fechaInicial.equals(fechaFinal)){
										
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito.longValue() < 0){/*con sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}else{/*Sin sobre giro*/
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
														    paraCaclulo = nuevo;
														    break;
													}
												}
											}
											if(hora<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}
									}else{
										
										Calendar cal = Calendar.getInstance();
										SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
										Date fecha = formatoDelTexto.parse(fechaInicial);
										cal.setTime(fecha);
										String convertido = "";
										if(cont > 1){
											for(int i = 0; i< cont; i++){
												cal.add(Calendar.DATE, i);
												DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
												convertido = fechaAux.format(cal.getTime());
												System.out.println(convertido);
												if(listaFeriados.contains(convertido)== false){
													horaTotales = horaTotales+9;
												}
											}
											
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}	
										}else{
											horaTotales = 9;
											if(lista != null && lista.size() == 1){
												CondicionTipoSolicitudRango nuevo = lista.get(0);
												if(hora<=nuevo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}else if(lista != null && lista.size() > 1){
												CondicionTipoSolicitudRango paraCaclulo = null;
												if(lineaCredito.longValue() < 0){/*con sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() > 60 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() > 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}else{/*Sin sobre giro*/
													for(CondicionTipoSolicitudRango nuevo : lista){
														if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
																nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 60	){
															    paraCaclulo = nuevo;
															    break;
														}
													}
												}
												
												if(horaTotales<=paraCaclulo.getHoras().longValue()){
													cumplidas++;
													contada = true;
												}else{
													nocumplidas++;
													contada = true;
												}											
											}												
										}
									}									
								}
								
							}else if("2R".equals(enSession.getTipoSolicitud())){
								horaTotales = 0;
								System.out.println("Con Sobre Giro");
								tiempoMontoType = TiempoMontoType.MONTOS;

								tipo = scoringService.obtenerObjetoLcredTipoSolicitud("23");
								
								medicion = scoringService.obtenerObjetoMedicion(2);
								
								if(enSession.getCanal().KX.equals(TipoCuentasKupferType.KX)){
									canal = scoringService.obtenerCanalForCodigo(1);
								}else if(enSession.getCanal().MX.equals(TipoCuentasKupferType.MX)){
									canal = scoringService.obtenerCanalForCodigo(3);
								}else if(enSession.getCanal().GC.equals(TipoCuentasKupferType.GC)){
									canal = scoringService.obtenerCanalForCodigo(2);
								}
								
								List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, canal, tiempoMontoType, medicion);
								if(fechaInicial.equals(fechaFinal)){
									if(lista != null && lista.size() == 1){
										CondicionTipoSolicitudRango nuevo = lista.get(0);
										if(hora<=nuevo.getHoras().longValue()){
											cumplidas++;
											contada = true;
										}else{
											nocumplidas++;
											contada = true;
										}											
									}else if(lista != null && lista.size() > 1){
										CondicionTipoSolicitudRango paraCaclulo = null;
										if(lineaCredito != null){
											if(lineaCredito.longValue() < 0){
												lineaCredito = new BigDecimal(lineaCredito.longValue() * -1);
											}											
											 long valorReal = lineaCredito.longValue();
											
											for(CondicionTipoSolicitudRango nuevo : lista){
												if(nuevo.getMedicionCanalRango().getValorInicial().longValue() <= valorReal &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() >= valorReal){
													    paraCaclulo = nuevo;
													    break;
												}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() <= lineaCredito.longValue()){
													    paraCaclulo = nuevo;
													    break;													
												}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() >= lineaCredito.longValue()){
												        paraCaclulo = nuevo;
												        break;														
												}
											}
										}
										if(hora<=paraCaclulo.getHoras().longValue()){
											cumplidas++;
											contada = true;
										}else{
											nocumplidas++;
											contada = true;
										}											
									}
								}else{
									Calendar cal = Calendar.getInstance();
									SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
									Date fecha = formatoDelTexto.parse(fechaInicial);
									cal.setTime(fecha);
									String convertido = "";
									if(cont > 1){
										for(int i = 0; i< cont; i++){
											cal.add(Calendar.DATE, i);
											DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
											convertido = fechaAux.format(cal.getTime());
											System.out.println(convertido);
											if(listaFeriados.contains(convertido)== false){
												horaTotales = horaTotales+9;
											}
										}
										
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito != null){
												
												if(lineaCredito.longValue() < 0){
													lineaCredito = new BigDecimal(lineaCredito.longValue() * -1);
												}											
												 long valorReal = lineaCredito.longValue();												
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() <= valorReal &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= valorReal){
														    paraCaclulo = nuevo;
														    break;
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= lineaCredito.longValue()){
														    paraCaclulo = nuevo;
														    break;													
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= lineaCredito.longValue()){
													        paraCaclulo = nuevo;
													        break;														
													}
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}	
									}else{
										horaTotales = 9;
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											if(lineaCredito != null){
												if(lineaCredito.longValue() < 0){
													lineaCredito = new BigDecimal(lineaCredito.longValue() * -1);
												}											
												 long valorReal = lineaCredito.longValue();												
												for(CondicionTipoSolicitudRango nuevo : lista){
													if(nuevo.getMedicionCanalRango().getValorInicial().longValue() <= valorReal &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= valorReal){
														    paraCaclulo = nuevo;
														    break;
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() <= lineaCredito.longValue()){
														    paraCaclulo = nuevo;
														    break;													
													}else if(nuevo.getMedicionCanalRango().getValorInicial().longValue() < lineaCredito.longValue() &&
															nuevo.getMedicionCanalRango().getValorFinal().longValue() >= lineaCredito.longValue()){
													        paraCaclulo = nuevo;
													        break;														
													}
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}											
									}
								}
							}else{
								tiempoMontoType = TiempoMontoType.DIAS;
								if("2I".equals(enSession.getTipoSolicitud())){
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("21");
								}else if("2N".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("22");
								}else if("3R".equals(enSession.getTipoSolicitud()) || "3P".equals(enSession.getTipoSolicitud()) || "3RP".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("31");									
								}else if("4B".equals(enSession.getTipoSolicitud()) || "4D".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("41");
								}else if("4DM".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("43");
								}else if("4PR".equals(enSession.getTipoSolicitud())){
									
									tipo = scoringService.obtenerObjetoLcredTipoSolicitud("44");
								}
								medicion = scoringService.obtenerObjetoMedicion(3);
								
								List<CondicionTipoSolicitudRango> lista = scoringService.getCondicionTipoSolicitudRango(tipo, null, tiempoMontoType, medicion);
								if(fechaInicial.equals(fechaFinal)){

									if(lista != null && lista.size() == 1){
										CondicionTipoSolicitudRango nuevo = lista.get(0);
										if(hora<=nuevo.getHoras().longValue()){
											cumplidas++;
											contada = true;
										}else{
											nocumplidas++;
											contada = true;
										}											
									}else if(lista != null && lista.size() > 1){
										CondicionTipoSolicitudRango paraCaclulo = null;
										lineaCredito = new BigDecimal(lineaCredito.longValue()*-1);
										for(CondicionTipoSolicitudRango nuevo : lista){
											if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
													nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 63){
												    paraCaclulo = nuevo;
												    break;
											}
										}
										if(paraCaclulo != null){
											if(hora<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}	
										}else{
											nocumplidas++;
											contada = true;
										}
									}									
								
								}else{
									
									Calendar cal = Calendar.getInstance();
									SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
									Date fecha = formatoDelTexto.parse(fechaInicial);
									cal.setTime(fecha);
									String convertido = "";
									if(cont > 1){
										for(int i = 0; i< cont; i++){
											cal.add(Calendar.DATE, i);
											DateFormat fechaAux = new SimpleDateFormat("dd-MM-yyyy");
											convertido = fechaAux.format(cal.getTime());
											System.out.println(convertido);
											if(listaFeriados.contains(convertido)== false){
												horaTotales = horaTotales+9;
											}
										}
										
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											lineaCredito = new BigDecimal(lineaCredito.longValue()*-1);
											for(CondicionTipoSolicitudRango nuevo : lista){
												if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 63){
													    paraCaclulo = nuevo;
													    break;
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}
										}											
										
										

									}else{
										horaTotales = 9;
										if(lista != null && lista.size() == 1){
											CondicionTipoSolicitudRango nuevo = lista.get(0);
											if(hora<=nuevo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}											
										}else if(lista != null && lista.size() > 1){
											CondicionTipoSolicitudRango paraCaclulo = null;
											lineaCredito = new BigDecimal(lineaCredito.longValue()*-1);
											for(CondicionTipoSolicitudRango nuevo : lista){
												if(nuevo.getMedicionCanalRango().getValorInicial().longValue() >= 0 &&
														nuevo.getMedicionCanalRango().getValorFinal().longValue() <= 63){
													    paraCaclulo = nuevo;
													    break;
												}
											}
											
											if(horaTotales<=paraCaclulo.getHoras().longValue()){
												cumplidas++;
												contada = true;
											}else{
												nocumplidas++;
												contada = true;
											}
										}											
									}
								}
							}
							
							if(contada==false){
								log.debug("idSolicitud : #0", idSolicitud);
								log.debug("totales : #0", totalSolicitudes);
								log.debug("cumplidas : #0", cumplidas);
								log.debug("No cumplidas : #0", nocumplidas);
								
							}else{
								contada = false;
							}

							/*variables seteadas para el objeto sigueinte*/
							idSolicitud = obj.getIdSolicitud();
							hora1 = obj.getHoraCreacion();
							lineaCredito = obj.getLineaCreditoDisponible();
							fechaInicial = obj.getFechaCreacion();
							fechaFinal = obj.getFechaHitos();
							enSession = obj;
							hora = 0;
							cont = 0;
							horaTotales = 0;
							

						}
					}
				}
				log.debug("Si trae si problema los datos.");
			}
			
		
			/*setear valors al objeto procentaje*/
			if(porcentajeFinal != null){
				porcentajeFinal.setUsuario(usuario);
				porcentajeFinal.setCumplidas(cumplidas);
				porcentajeFinal.setNoCumplidas(nocumplidas);
				porcentajeFinal.setTotalSolicitudesIndividual(totalSolicitudes);
				porcentajeFinal.setTipoSolicitud(tipoSolicitudes);
				double dividir = (porcentajeFinal.getCumplidas()*100);
				if(dividir != 0){
					double dividir2 = (dividir/porcentajeFinal.getTotalSolicitudesIndividual());
					porcentajeFinal.setPorcentajes(dividir2);					
				}else{
					porcentajeFinal.setPorcentajes(0);	
				}
				
				switch (Integer.parseInt(tipoSolicitudes.getCodTipoSolicitud().trim())) {
				case 11:
					porcentajeFinal.setVentasNormal(true);
					break;
				case 12:
					porcentajeFinal.setVentaEnCuotas(true);
					break;
				case 21:
					porcentajeFinal.setInnomindas(true);
					break;
				case 22:
					porcentajeFinal.setNominadas(true);
					break;
				case 23:
					porcentajeFinal.setRiesgoKupfer(true);
					break;
				case 31:
					porcentajeFinal.setRiesgoPago(true);
					break;
				case 41:
					porcentajeFinal.setBloqueoDesbloqueo(true);
					break;
				case 43:
					porcentajeFinal.setCreacionDm(true);
					break;
				case 44:
					porcentajeFinal.setProrogas(true);
					break;
				default:
					break;
				}				
				
				
			}
		}catch (Exception e) {
			log.error("Error, se cae al verificar lka informacion de trabajo del usuario #0 y el error es el siguiente #1",usuario , e.getMessage());
		}
	
		return porcentajeFinal;
	}	
	
	
	public static int DiasDelMes(int ano, int mes) {
		int ndias = 0;
		int f = 0;

		int an = ano;

		if (an % 4 == 0) {
			f = 29;
		} else {
			f = 28;
		}
		switch (mes) {
		case 1:
			ndias = 31;
			break;
		case 2:
			ndias = f;
			break;
		case 3:
			ndias = 31;
			break;
		case 4:
			ndias = 30;
			break;
		case 5:
			ndias = 31;
			break;
		case 6:
			ndias = 30;
			break;
		case 7:
			ndias = 31;
			break;
		case 8:
			ndias = 31;
			break;
		case 9:
			ndias = 30;
			break;
		case 10:
			ndias = 31;
			break;
		case 11:
			ndias = 30;
			break;
		case 12:
			ndias = 31;
			break;
		}
		return ndias;
	}

	/* gets y Sets */
	public List<CPagoDTO> getListaCPagoDTOs() {
		return listaCPagoDTOs;
	}

	public void setListaCPagoDTOs(List<CPagoDTO> listaCPagoDTOs) {
		this.listaCPagoDTOs = listaCPagoDTOs;
	}

	public LcredEstado getEstado() {
		return estado;
	}

	public void setEstado(LcredEstado estado) {
		this.estado = estado;
	}

	public String getTsListaTodasSolicitudes() {
		return tsListaTodasSolicitudes;
	}

	public void setTsListaTodasSolicitudes(String tsListaTodasSolicitudes) {
		this.tsListaTodasSolicitudes = tsListaTodasSolicitudes;
	}

	public Date getFechaInical() {
		return fechaInical;
	}

	public void setFechaInical(Date fechaInical) {
		this.fechaInical = fechaInical;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public List<SolicitudDTO> getListaTodasSolicitudesDto() {
		return listaTodasSolicitudesDto;
	}

	public void setListaTodasSolicitudesDto(
			List<SolicitudDTO> listaTodasSolicitudesDto) {
		this.listaTodasSolicitudesDto = listaTodasSolicitudesDto;
	}

	public List<Sucursal> getListaSucursals() {
		return listaSucursals;
	}

	public void setListaSucursals(List<Sucursal> listaSucursals) {
		this.listaSucursals = listaSucursals;
	}

	public List<LcredEstado> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<LcredEstado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public List<String> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<String> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<String> getListaTiposSolicitudes() {
		return listaTiposSolicitudes;
	}

	public void setListaTiposSolicitudes(List<String> listaTiposSolicitudes) {
		this.listaTiposSolicitudes = listaTiposSolicitudes;
	}

	public List<String> getListaFormaPago() {
		return listaFormaPago;
	}

	public void setListaFormaPago(List<String> listaFormaPago) {
		this.listaFormaPago = listaFormaPago;
	}

	public String getUsuarioFilter() {
		return usuarioFilter;
	}

	public void setUsuarioFilter(String usuarioFilter) {
		this.usuarioFilter = usuarioFilter;
	}

	public String getTipoFilter() {
		return tipoFilter;
	}

	public void setTipoFilter(String tipoFilter) {
		this.tipoFilter = tipoFilter;
	}

	public String getFpagos() {
		return fpagos;
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

	public SolicitudHitos getSolicitudHitos() {
		return solicitudHitos;
	}

	public void setSolicitudHitos(SolicitudHitos solicitudHitos) {
		this.solicitudHitos = solicitudHitos;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public void setFpagos(String fpagos) {
		this.fpagos = fpagos;
	}

	public Long getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public String getRutAux() {
		return rutAux;
	}

	public void setRutAux(String rutAux) {
		this.rutAux = rutAux;
	}

	public String getRutAuxSinGuion() {
		return rutAuxSinGuion;
	}

	public void setRutAuxSinGuion(String rutAuxSinGuion) {
		this.rutAuxSinGuion = rutAuxSinGuion;
	}

	public UsuarioCargo getUsuarioCargoAux() {
		return usuarioCargoAux;
	}

	public void setUsuarioCargoAux(UsuarioCargo usuarioCargoAux) {
		this.usuarioCargoAux = usuarioCargoAux;
	}

	public LcredTipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(LcredTipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public List<LcredTipoSolicitud> getListaLcredTipoSolicituds() {
		return listaLcredTipoSolicituds;
	}

	public void setListaLcredTipoSolicituds(
			List<LcredTipoSolicitud> listaLcredTipoSolicituds) {
		this.listaLcredTipoSolicituds = listaLcredTipoSolicituds;
	}

	public List<Usuariosegur> getListaUsuariosegurs() {
		return listaUsuariosegurs;
	}

	public void setListaUsuariosegurs(List<Usuariosegur> listaUsuariosegurs) {
		this.listaUsuariosegurs = listaUsuariosegurs;
	}

	public Usuariosegur getUsuarioSegurCbo() {
		return usuarioSegurCbo;
	}

	public void setUsuarioSegurCbo(Usuariosegur usuarioSegurCbo) {
		this.usuarioSegurCbo = usuarioSegurCbo;
	}

	public List<Canal> getListaCanales() {
		return listaCanales;
	}

	public void setListaCanales(List<Canal> listaCanales) {
		this.listaCanales = listaCanales;
	}

	public List<Canal> getListaCanalesSeleccionada() {
		return listaCanalesSeleccionada;
	}

	public void setListaCanalesSeleccionada(List<Canal> listaCanalesSeleccionada) {
		this.listaCanalesSeleccionada = listaCanalesSeleccionada;
	}

	public List<Sucursal> getListaSucursalsSeleccionados() {
		return listaSucursalsSeleccionados;
	}

	public void setListaSucursalsSeleccionados(
			List<Sucursal> listaSucursalsSeleccionados) {
		this.listaSucursalsSeleccionados = listaSucursalsSeleccionados;
	}

	public List<LcredTipoSolicitud> getListaLcredTipoSolicitudsSeleccionados() {
		return listaLcredTipoSolicitudsSeleccionados;
	}

	public void setListaLcredTipoSolicitudsSeleccionados(
			List<LcredTipoSolicitud> listaLcredTipoSolicitudsSeleccionados) {
		this.listaLcredTipoSolicitudsSeleccionados = listaLcredTipoSolicitudsSeleccionados;
	}

	public List<Usuariosegur> getListaUsuariosegursSeleccionados() {
		return listaUsuariosegursSeleccionados;
	}

	public void setListaUsuariosegursSeleccionados(
			List<Usuariosegur> listaUsuariosegursSeleccionados) {
		this.listaUsuariosegursSeleccionados = listaUsuariosegursSeleccionados;
	}

	public List<ConceptosNegocio> getListaNegocios() {
		return listaNegocios;
	}

	public void setListaNegocios(List<ConceptosNegocio> listaNegocios) {
		this.listaNegocios = listaNegocios;
	}

	public List<ConceptosNegocio> getListaNegociosSeleccionados() {
		return listaNegociosSeleccionados;
	}

	public void setListaNegociosSeleccionados(
			List<ConceptosNegocio> listaNegociosSeleccionados) {
		this.listaNegociosSeleccionados = listaNegociosSeleccionados;
	}

	public Long getTotalSolicitudes() {
		return totalSolicitudes;
	}

	public void setTotalSolicitudes(Long totalSolicitudes) {
		this.totalSolicitudes = totalSolicitudes;
	}

	public Long getCumplidas() {
		return cumplidas;
	}

	public void setCumplidas(Long cumplidas) {
		this.cumplidas = cumplidas;
	}

	public Long getNocumplidas() {
		return nocumplidas;
	}

	public void setNocumplidas(Long nocumplidas) {
		this.nocumplidas = nocumplidas;
	}

	public Double getPorcentajeCumplidas() {
		return porcentajeCumplidas;
	}

	public void setPorcentajeCumplidas(Double porcentajeCumplidas) {
		this.porcentajeCumplidas = porcentajeCumplidas;
	}

	public Double getPorcentajeNoCumplidas() {
		return porcentajeNoCumplidas;
	}

	public void setPorcentajeNoCumplidas(Double porcentajeNoCumplidas) {
		this.porcentajeNoCumplidas = porcentajeNoCumplidas;
	}

	public List<PorcentajeDTO> getListaPorcentajes() {
		return listaPorcentajes;
	}

	public void setListaPorcentajes(List<PorcentajeDTO> listaPorcentajes) {
		this.listaPorcentajes = listaPorcentajes;
	}

	public List<PorcentajeDTO> getListaPorcentajeTipoSolicitudes() {
		return listaPorcentajeTipoSolicitudes;
	}

	public void setListaPorcentajeTipoSolicitudes(
			List<PorcentajeDTO> listaPorcentajeTipoSolicitudes) {
		this.listaPorcentajeTipoSolicitudes = listaPorcentajeTipoSolicitudes;
	}

	public boolean isVentasNormal() {
		return ventasNormal;
	}

	public void setVentasNormal(boolean ventasNormal) {
		this.ventasNormal = ventasNormal;
	}

	public boolean isVentaEnCuotas() {
		return ventaEnCuotas;
	}

	public void setVentaEnCuotas(boolean ventaEnCuotas) {
		this.ventaEnCuotas = ventaEnCuotas;
	}

	public boolean isNominadas() {
		return nominadas;
	}

	public void setNominadas(boolean nominadas) {
		this.nominadas = nominadas;
	}

	public boolean isInnomindas() {
		return innomindas;
	}

	public void setInnomindas(boolean innomindas) {
		this.innomindas = innomindas;
	}

	public boolean isRiesgoKupfer() {
		return riesgoKupfer;
	}

	public void setRiesgoKupfer(boolean riesgoKupfer) {
		this.riesgoKupfer = riesgoKupfer;
	}

	public boolean isRiesgoPago() {
		return riesgoPago;
	}

	public void setRiesgoPago(boolean riesgoPago) {
		this.riesgoPago = riesgoPago;
	}

	public boolean isBloqueoDesbloqueo() {
		return bloqueoDesbloqueo;
	}

	public void setBloqueoDesbloqueo(boolean bloqueoDesbloqueo) {
		this.bloqueoDesbloqueo = bloqueoDesbloqueo;
	}

	public boolean isCreacionDm() {
		return creacionDm;
	}

	public void setCreacionDm(boolean creacionDm) {
		this.creacionDm = creacionDm;
	}

	public boolean isProrogas() {
		return prorogas;
	}

	public void setProrogas(boolean prorogas) {
		this.prorogas = prorogas;
	}
	
	
	

}
