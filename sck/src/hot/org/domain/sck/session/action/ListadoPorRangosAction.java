package org.domain.sck.session.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.domain.sck.base.GlobalHitosLogdService;
import org.domain.sck.base.GlobalService;
import org.domain.sck.dto.CPagoDTO;
import org.domain.sck.dto.SolicitudDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.entity.CanalUsuarioCargo;
import org.domain.sck.entity.Estados;
import org.domain.sck.entity.LcredEstado;
import org.domain.sck.entity.LcredPerfiles;
import org.domain.sck.entity.LcredSolicitud;
import org.domain.sck.entity.LcredSolicitudCondiciones;
import org.domain.sck.entity.LcredSolicitudLcredito;
import org.domain.sck.entity.LcredSolicitudObservaciones;
import org.domain.sck.entity.LcredSolicitudObservacionesId;
import org.domain.sck.entity.LcredSolicitudOtras;
import org.domain.sck.entity.LcredSolicitudVtapuntual;
import org.domain.sck.entity.LcredUsuarioNivelEnc;
import org.domain.sck.entity.PerfilFuncionCanal;
import org.domain.sck.entity.Sociedad;
import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudLogs;
import org.domain.sck.entity.SolicitudUsuarioDerivada;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.entity.ZonaSucursalNegocioConcepto;
import org.domain.sck.entity.enums.TipoCuentasKupferType;
import org.domain.sck.entity.enums.TipoSolicitudType;
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


@Name("listadoPorRangosAction")
@Scope(ScopeType.CONVERSATION)
public class ListadoPorRangosAction {
	

	@Logger
	Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	
	@In IntranetSapService intranetSapService;
	
	@In ScoringService scoringService;
	
	@In GlobalService globalService;
	
	@In GlobalHitosLogdService globalHitosLogdService;
	
	@In(value="#{identity.usuarioCargo}", scope = ScopeType.SESSION)
	private UsuarioCargo usuarioCargoAux;
	
	@In(value="#{identity.usuarioLogueado}", scope = ScopeType.SESSION)
	private Usuariosegur usuarioLogueado;
	
	
	
	@In(value="#{identity.lcredUsuarioNivelEnc}", scope = ScopeType.SESSION)
	private LcredUsuarioNivelEnc lcredUsuarioNivelEnc;
	
	
	@In(value="#{identity.usuarioSegur}", scope = ScopeType.SESSION)
	private UsuarioSegurDTO usuarioSegur;
	
	private LcredEstado  estado;
	private Sucursal sucursal;
	private Date fechaInical;
	private Date fechaFinal;
	
	private List<SolicitudDTO> listaTodasSolicitudesDto;
	private List<Sucursal> listaSucursals;
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
	
	
	private List<CanalUsuarioCargo> listaCanalUsuarioCargos;
	private List<PerfilFuncionCanal> listaPerfilFuncionCanals;
	private List<ZonaSucursalNegocioConcepto> listaZonaSucursalNegocioConceptos;
	
	private List<Sociedad> listaSociedades;
	private Sociedad sociedad;
	private String[] arrayCanales;
    
    

	
	@Create
	public void init(){
		try{
		
			if(lcredUsuarioNivelEnc != null){
				limpiarParametos();
			    sacarSucursales();
			    sacarCPago();
			    sacarEstadosSolicitudes();
			    sacarSociedades();
			    
				if(usuarioLogueado != null){
					usuarioCargoAux = scoringService.getUsuarioCargo(usuarioLogueado.getIdPersonal());
					if(usuarioCargoAux != null){
						listaCanalUsuarioCargos = scoringService.getListaCanalUsuarioCargo(usuarioCargoAux.getIdUsuarioCargo());
						if(listaCanalUsuarioCargos != null){
							StringBuffer cadenaCanales = new StringBuffer();
							long codigo = 0;
							for(CanalUsuarioCargo cuc : listaCanalUsuarioCargos){
								cadenaCanales.append(cuc.getTipoCuenta().getNombre());
								cadenaCanales.append(",");
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
							arrayCanales = (cadenaCanales.toString()).split(",");
						}
					}
				}
			}		    		

 	   }catch (Exception e) {
		log.error("error al sacar las solictudes de usuario logeado #0", e.getMessage());
	   }
	
	}
	
	public void sacarEstado(){
		if(this.estado != null){
			log.error(" el estado seleccionado #0", this.estado.getDesEstado());
		}
	}
	public void sacarSucursal(){
		if(this.sucursal != null){
			log.error(" la sucursal  seleccionado #0", this.sucursal.getDescripcion());
		}
	}	
	public void sacarSociedad(){
		if(this.sociedad != null){
			log.error(" la sociedad  seleccionado #0", this.sociedad.getRazonSocial());
		}
	}	
	
	public boolean validarParametros(){
		try{
			
			if(this.idSolicitud == null && (this.rutAux == null || "".equals(this.rutAux)) && this.fechaInical == null 
			&& this.fechaFinal == null && this.estado == null && this.sucursal == null && this.sociedad == null){
				
				FacesMessages.instance().add(Severity.INFO,"Debe ingresar por lo menos un criterio de busqueda.");
				return false;				
			}
			
			if("".equals(this.rutAux)){
				this.setRutAux(null);
			}
			
			
			if(this.idSolicitud!= null){
				if(this.idSolicitud.longValue() != 0){
					return true;
				}
			}
			if(this.getRutAux() != null){
				if(!"".equals(this.getRutAux())){
					//verificar que el rut no tenga menos de 8 caracteres
					if(this.getRutAux().length() < 8) {
						FacesMessages.instance().add(Severity.ERROR,"Rut invalido, debe tener al menos 8 caracteres");
						return false;
					}
					
					//sacar el cero inicial del rut.
					this.setRutAux(globalService.sacarCeroRut(this.getRutAux()));	
					//agregar guin en caso de ser necesario
					String rut = globalService.agregarGuionRut(this.getRutAux());
					log.debug("rut #0 #1", rut,globalService.validarRut(rut));	
					this.rutAuxSinGuion = globalService.sacarGuionRut(rut);
					
					this.setRutAux(rut);
				}else {
					this.setRutAux(null);
				}
				
			}

			if(this.fechaInical == null && this.fechaFinal != null){
				FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar la fecha inicial.");
				return false;				
			}else if(this.fechaInical != null && this.fechaFinal != null){
				Calendar calInicial = Calendar.getInstance();
				calInicial.setTime(fechaInical);
				Long valorInicial = calInicial.getTimeInMillis();
				Calendar calFinal = Calendar.getInstance();
				calFinal.setTime(this.fechaFinal);
				/*evaluacion final*/
				Long valorFinal = calFinal.getTimeInMillis();
				if(valorFinal < valorInicial){
					FacesMessages.instance().add(Severity.ERROR,"Fecha Final debe ser mayor que la Fecha Final.");
					return false;						
				}			
			}
			
		}catch (Exception e) {
			log.error("Error, al validar los parametros #0 ", e.getMessage());
		}
		return true;
	}
	
	public void consultarSolicitudes(){
		if(validarParametros()){
			sacarSolicitudesPorRangos();
		}
	}

	public void limpiarParametos(){
		this.setIdSolicitud(null);
		this.setRutAux(null);
		this.setFechaFinal(null);
		this.setFechaInical(null);
		this.setEstado(null);
		this.setSucursal(null);
		this.setSociedad(null);
		this.setListaTodasSolicitudesDto(new ArrayList<SolicitudDTO>());
		this.setTsListaTodasSolicitudes(null);
		listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>();
	}
	
	
	@SuppressWarnings("unchecked")
	public void sacarSolicitudesPorRangos(){
		List<LcredSolicitud> listaTodasSolicitudes = null;
		Sucursal suc = null;
		try {
			LcredPerfiles perfil1 = scoringService.obtenerPerfil("1");
			LcredPerfiles perfil2 = scoringService.obtenerPerfil("6");
			
			
			Date fechaBusqueda = null;
			DateFormat formatoFechaBusqueda = new SimpleDateFormat("yyyy-MM-dd");
			try {
				fechaBusqueda = formatoFechaBusqueda.parse("2013-05-25");
				log.debug(fechaBusqueda);
			} catch (Exception e) {
				throw new Exception(e);
			}
			
			if (lcredUsuarioNivelEnc != null) {
				List<Integer>  array  = scoringService.obtenerPerfilesDelUsuario(usuarioSegur.getIdPersonal());
				sacarEstadosSolicitudes();
				/************Todas las solicitudes **********************/
				if (usuarioLogueado != null) {
					String query = "Select s from LcredSolicitud s where ";
					
					   if(this.idSolicitud != null){
						   query += " s.id.numSolicitud=:idSolicitud "; 
					   }

					   if(this.idSolicitud != null && this.rutAux != null){
						   query += " and ( s.rutCliente=:rutAux or  s.rutCliente=:rutAuxSinGuion ) "; 
					   }else if(this.idSolicitud == null && this.rutAux != null){
						   query += " s.rutCliente=:rutAux or s.rutCliente=:rutAuxSinGuion "; 
					   }
					   
					   
					   /*validar la consulta de fechas inicial y fecha final*/
					   if((this.idSolicitud != null || this.rutAux != null) 
						 && this.fechaInical != null && this.fechaFinal == null){
						  
						   query += " and s.id.fecSolicitud >= :fechaInicio ";   
					   
					   }else if((this.idSolicitud == null && this.rutAux == null) 
							  && this.fechaInical != null && this.fechaFinal != null){
						   
						   query += " s.id.fecSolicitud BETWEEN  :fechaInicio and :fechaFinal "; 
						   
					   }else if((this.idSolicitud == null && this.rutAux == null) 
								  && this.fechaInical != null && this.fechaFinal == null ){
						   
						   query += "  and s.id.fecSolicitud >= :fechaInicio) ";   
					   }

					   /*validar la consulta de estado*/
					   if((this.idSolicitud != null || this.rutAux != null || this.fechaInical != null) 
						 && this.estado != null){
						   if(!"TO".equals(this.estado.getCodEstado())){
							   query += " and s.estado=:estadoAux ";  
						   }else{
							   query += " and s.estado=:estadoAux not in ('')";  
						   }
						   
					   
					   }else if((this.idSolicitud == null && this.rutAux == null && this.fechaInical == null) 
							   && this.estado != null){
						   if(!"TO".equals(this.estado.getCodEstado())){
							   query += "  s.estado=:estadoAux ";  
						   }else{
							   query += "  s.estado not in ('')";  
						   }
						   
					   }									   
					   
					   /*validar la consulta de sucursal*/
					   if((this.idSolicitud != null || this.rutAux != null || this.fechaInical != null || this.estado != null) 
						 && this.sucursal != null){
						   if(!"TDS1".equals(this.sucursal.getCodigo())){
							   query += " and s.codSucursal=:codSucursal ";   
						   }else{
							   query += " and s.codSucursal not in ('') "; 
						   }
					   }else if((this.idSolicitud == null && this.rutAux == null && this.fechaInical == null &&  this.estado == null) 
							   && this.sucursal != null){
						   
						   if(!"TDS1".equals(this.sucursal.getCodigo())){
							   query += "  s.codSucursal=:codSucursal ";   
						   }else{
							   query += " s.codSucursal not in ('') "; 
						   }										  
					   }
					
					   /*validar la consulta de sociedad*/
					   if((this.idSolicitud != null || this.rutAux != null || this.fechaInical != null || this.estado != null || this.sucursal != null ) && this.sociedad != null){
						    query += " and s.sociedad.idSociedad=:codSociedad ";   
					   
					   }else if((this.idSolicitud == null && this.rutAux == null && this.fechaInical == null &&  this.estado == null  && this.sucursal == null && this.sociedad != null)){
							   query += "  s.sociedad.idSociedad=:codSociedad ";   
					   }
					   
					   
					   query+=" order by s.id.numSolicitud desc";
					   
					   Query consulta =  entityManager.createQuery(query);
					   
					   if(this.idSolicitud != null){
						   consulta.setParameter("idSolicitud", this.idSolicitud);
					   }
					   if(this.rutAux != null){
						   consulta.setParameter("rutAux", this.rutAux);
						   consulta.setParameter("rutAuxSinGuion", this.rutAuxSinGuion);
					   }

					   
					   /*validar la consulta de fechas inicial y fecha final*/
					   if(this.fechaInical != null && this.fechaFinal == null){
				             Calendar calDesde = Calendar.getInstance();
				             calDesde.setTime(this.fechaInical);
				             calDesde.set(Calendar.HOUR_OF_DAY, 0);
				             calDesde.set(Calendar.MINUTE, 0);
				             calDesde.set(Calendar.SECOND, 0);
				             this.fechaInical = calDesde.getTime();
						     consulta.setParameter("fechaInicio", this.fechaInical);
					   
					   }else if( this.fechaInical != null && this.fechaFinal != null){
				             Calendar calDesde = Calendar.getInstance();
				             calDesde.setTime(this.fechaInical);
				             calDesde.set(Calendar.HOUR_OF_DAY, 0);
				             calDesde.set(Calendar.MINUTE, 0);
				             calDesde.set(Calendar.SECOND, 0);
				             this.fechaInical = calDesde.getTime();
				             
						     Calendar calHasta = Calendar.getInstance();
				             calHasta.setTime(this.fechaFinal);
				             calHasta.set(Calendar.HOUR_OF_DAY, 23);
				             calHasta.set(Calendar.MINUTE, 59);
				             calHasta.set(Calendar.SECOND, 59);
				             this.fechaFinal = calHasta.getTime();
						   consulta.setParameter("fechaInicio", this.fechaInical);
						   consulta.setParameter("fechaFinal", this.fechaFinal);
					   }

					   
					   /*validar la consulta de estado*/
					   if(this.estado != null){
						   if(!"TO".equals(this.estado.getCodEstado())){
							   consulta.setParameter("estadoAux", this.estado.getCodEstado()); 
						   }
					   }									   
					   
					   /*validar la consulta de sucursal*/
					   if(this.sucursal != null){
						   if(!"TDS1".equals(this.sucursal.getCodigo())){  
							   consulta.setParameter("codSucursal", this.sucursal.getCodigo()); 
						   }
					   }

					   /*validar la consulta de sucursal*/
					   if(this.sociedad != null){
						   consulta.setParameter("codSociedad", this.sociedad.getIdSociedad()); 
					   }
					   
					  try{ 
						  listaTodasSolicitudes = (List<LcredSolicitud>)consulta.getResultList();

					  }catch (Exception e) {
						log.error("Error al armar la query por favor revisar. #0", e.getMessage());
						e.printStackTrace();
						FacesMessages.instance().add(Severity.INFO,"La consulta genero demasaida información, favor aplicar otro fitro mas.");
						return ;	
					  } 

					if (listaTodasSolicitudes != null) {
						listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>(0);
						listaUsuarios = new ArrayList<String>(0);
						listaTiposSolicitudes = new ArrayList<String>(0);
						listaFormaPago = new ArrayList<String>(0);

						for (LcredSolicitud sol : listaTodasSolicitudes) {
								SolicitudDTO nuevo = new SolicitudDTO();
								nuevo.setIdSolictud(sol.getId().getNumSolicitud());
								nuevo.setRut(sol.getRutCliente().replace("-", ""));
								nuevo.setRazonSocial(sol.getNomCliente());
								nuevo.setEmisor(sol.getCodEmisor());
								nuevo.setDespTipoSolictud(sol.getSubTiposol());
								nuevo.setFechaSalida(sol.getFecSalida());
								nuevo.setUsuarioProceso(sol.getUsuarioActual());
								nuevo.setTipoSolicitud(sol.getTipTiposol());
								nuevo.setCanal(sol.getCanal());
								nuevo.setCondicionPago(sol.getFormaPago());
								nuevo.setCodSucursalEmisor(sol.getSucursalEmisor());
								nuevo.setSociedad(sol.getSociedad()!=null?sol.getSociedad().getRazonSocial():"");
								
					
								int tipoCuenta = 0;
								boolean autorizacion = false;
								if(nuevo.getCanal() != null){
									if("Mixto".equals((nuevo.getCanal().toString()))){
										autorizacion = verificaCanal(TipoCuentasKupferType.MX);
										tipoCuenta = TipoCuentasKupferType.MX.ordinal();
									}else if("Kupfer Express".equals((nuevo.getCanal().toString()))){
										autorizacion = verificaCanal(TipoCuentasKupferType.KX);
										tipoCuenta = TipoCuentasKupferType.KX.ordinal();
									}else if("Grandes Cuentas".equals((nuevo.getCanal().toString()))){
										autorizacion = verificaCanal(TipoCuentasKupferType.GC);
										tipoCuenta = TipoCuentasKupferType.GC.ordinal();
									}	
								}
								boolean exito = false;
								//log.debug("solicitud #0  y sucursal : #1 ", sol.getId().getNumSolicitud(),sol.getCodSucursal());
								boolean aprobar = false;
								boolean ejecutivo = false;
								boolean financiero = false;
								boolean comenta = false;

						  		for(Integer obj :array){
						  			if(obj!= null && obj.intValue() != 0){
						  				exito = scoringService.obtenerHabilitacionUsuario(usuarioLogueado.getIdPersonal(), sol.getSucursalEmisor(), tipoCuenta, obj.intValue());
						  				//log.debug(" sucursal #0 -- canal #1 -- exito #2", sol[8].toString(),obj.intValue(),exito);
						  				if(obj.intValue() ==1 && exito == true){
						  					aprobar = true;
						  				}else if(obj.intValue() ==3 && exito == true){
						  					ejecutivo = true;
						  				}else if(obj.intValue() ==4 && exito == true){
						  					financiero = true;
						  				}else if(obj.intValue() ==5 && exito == true){
						  					comenta = true;
						  				}
						  			}
						  		}
						  		if(aprobar == true || ejecutivo == true || financiero == true){
						  			exito = true;
						  		}else if(comenta == true){
						  			exito = false;
						  		}
								
						  		if(autorizacion == true && exito == true){
								
								
								if (sol.getId().getFecSolicitud() != null
										&& sol.getHraSolicitud() != null) {
	
									/* rescatar la fecha */
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									String fecha = sdf.format(sol.getId().getFecSolicitud());
	
									/* rescatar la hora */
									SimpleDateFormat sdh = new SimpleDateFormat("HH:mm");
									String hora = sdh.format(sol.getHraSolicitud());
	
									/* fecha y hora */
									//log.debug((" Fecha String:" + fecha);
									//log.debug((" hora String :" + hora);
	
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
									LcredEstado estado = obtenerObjetoLcredEstado(sol.getEstado());
									if (estado != null) {
										nuevo.setCodigoEstado(estado.getCodEstado());
										nuevo.setEstado(estado.getDesEstado());
									}
	
									if(array != null){
								  		for(Integer obj :array){
								  			if(obj!= null){
								  				if(obj.intValue() == 1 || obj.intValue() == 3 || obj.intValue() == 4){
								  					nuevo.setProceso(perfil1.getDesPerfil());
								  					break;
								  				}else {
								  					nuevo.setProceso(perfil2.getDesPerfil());
								  					break;
								  				}
								  			}
								  		}	
								  	}
								}
	
								if (sol.getSucursalEmisor() != null) {
									//log.debug((" codigo de sucursal  :"	+ sol.getSucursalEmisor());
									suc = obtenerObjetoSucursal(sol.getSucursalEmisor());
									if (suc != null) {
										nuevo.setSucursal(suc.getDescripcion());
									} else {
										nuevo.setSucursal(sol.getCodSucursal());
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
								
								LcredSolicitud solicitudAux = null;
								try{
									solicitudAux = (LcredSolicitud)entityManager.
											createQuery("select s from LcredSolicitud s where s.id.numSolicitud=:numSolicitud")
											.setParameter("numSolicitud", nuevo.getIdSolictud()).getSingleResult();
								}catch (Exception e) {
									log.error("no encontro la solicitud #0", e.getMessage());
								}
								
								
								//(sol.getUsuarioActual() == null || "".equals(sol.getUsuarioActual()))
								if(usuarioCargoAux != null && usuarioCargoAux.getAdministrador().booleanValue()){
									 if (nuevo.getCodigoEstado() != null) {
										 if (nuevo.getCodigoEstado().equals("A") || nuevo.getCodigoEstado().equals("NU") || nuevo.getCodigoEstado().equals("R")) {
												nuevo.setControlador(2);
											} else if(nuevo.getCodigoEstado().equals("DE")){
												nuevo.setControlador(3);
											}else{
												nuevo.setControlador(1);
											}
									 }
								}else{								   
									if(usuarioCargoAux != null && !usuarioCargoAux.getCargo().getCodCargo().equals("015")){
										if (nuevo.getCodigoEstado() != null) {
											if (nuevo.getCodigoEstado().equals("I") || nuevo.getCodigoEstado().equals("DC") || nuevo.getCodigoEstado().equals("DR")) {
												nuevo.setControlador(1);
											} else if (nuevo.getCodigoEstado().equals("E")) {
												if (usuarioLogueado.getAlias().equals(nuevo.getUsuarioProceso())) {
													nuevo.setControlador(1);
												} else {
													if (usuarioCargoAux != null && usuarioCargoAux.getCargo().getNivelCargo() == 10) {
														nuevo.setControlador(1);
													} else {
														nuevo.setControlador(2);
													}
												}
											} else if ( nuevo.getCodigoEstado().equals("DE")) {
												boolean evaluacion = evaluarFechaSolicitud(nuevo.getIdSolictud(),nuevo.getCodigoEstado());
												if(evaluacion){
													solicitudAux.setEstado("NC");
													 if((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.V1N.getNombre()) || 
															    (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.V1C.getNombre())){
															try{ 
																LcredSolicitudVtapuntual  venta = (LcredSolicitudVtapuntual)entityManager
																		 .createQuery("Select vp from LcredSolicitudVtapuntual vp where vp.numSolicitud=:solAux")
																		 .setParameter("solAux", solicitudAux.getId().getNumSolicitud()).getSingleResult();
																
																if(venta != null){
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
															}catch (Exception e) {
																log.debug("no existe datos de venta puntual");
															} 
													 }else if((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.LC1.getNombre()) || (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.LC2.getNombre()) ||
															 (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.LC3.getNombre())){
														    try{ 
														    	LcredSolicitudLcredito credito = (LcredSolicitudLcredito)entityManager
																		 .createQuery("Select lc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
																		 .setParameter("solAux", solicitudAux.getId().getNumSolicitud()).getSingleResult();
															
														    	if(credito != null){
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
														    
														    }catch (Exception e) {
																log.debug("no existe datos de linea credito.");
															 }
														 
														 
													 }else if((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.CR1.getNombre()) || (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.CR2.getNombre()) ||
															 (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.CRP3.getNombre()) ||  (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.CRP4.getNombre())){
														 
														  try{ 
															  LcredSolicitudCondiciones condicion = (LcredSolicitudCondiciones)entityManager
																	 .createQuery("Select sc from LcredSolicitudCondiciones sc where sc.numSolicitud=:solAux")
																	 .setParameter("solAux", solicitudAux.getId().getNumSolicitud()).getSingleResult();
															  
															  if(condicion != null){
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
														  }catch (Exception e) {
															log.debug("no existe datos de condiciones.");
														  }	
													 }else if((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS1.getNombre()) || (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS2.getNombre()) || 
															 (solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS.getNombre())){/***************** Bloqueo y Desbloqueo ***********************/
														 	try{
														 		LcredSolicitudOtras solicitudOtra = scoringService.getSolicitudOtrasId(solicitudAux.getId().getNumSolicitud());
														 		if(solicitudOtra != null){
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
															}catch (Exception e) {
																log.debug("no existe datos de bloqueo o desbloqueo.");
															}
													 }else if((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS3.getNombre())){/***************** Creacion DM ***********************/
														 	try{
														 		LcredSolicitudOtras solicitudOtra = scoringService.getSolicitudOtrasId(solicitudAux.getId().getNumSolicitud());
														 		if(solicitudOtra != null){
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
															}catch (Exception e) {
																log.debug("no existe datos de creacion de dm.");
															}																 
														 
													 }else if((solicitudAux.getTipTiposol().trim()).equals(TipoSolicitudType.OS4.getNombre())){/********************* Prorroga ********************/
														 	try{
														 		LcredSolicitudOtras solicitudOtra = scoringService.getSolicitudOtrasId(solicitudAux.getId().getNumSolicitud());
														 		if(solicitudOtra != null){
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
															}catch (Exception e) {
																log.debug("no existe datos de prorroga.");
															}																 
													 }
												}else{
													nuevo.setControlador(3);
												}
											}else if (nuevo.getCodigoEstado().equals("B") || nuevo.getCodigoEstado().equals("SA")) {
												nuevo.setControlador(2);
											}else if (nuevo.getCodigoEstado().equals("N") || nuevo.getCodigoEstado().equals("NC")) {
												nuevo.setControlador(2);
											}else{
												nuevo.setControlador(2);
											}
										}
									}else{
										nuevo.setControlador(2);
									}
								}	
								
								
								/*verificar si tiene mas un derivado o aprbadores */								
								if(sol.getEstadoEvaluacion() != null){
									Long numero = scoringService.getCantidadSolicitudUsuarioDerivacion(sol.getId().getNumSolicitud());
									if(numero != null && numero.longValue() > 1){
									    nuevo.setEstadoEvaluacion(sol.getEstadoEvaluacion());
										nuevo.setHabilitaLista(true);
									}else{
										List<SolicitudUsuarioDerivada> lista = scoringService.getSolicitudUsuarioDerivacionForSolicitud(sol.getId().getNumSolicitud());
										if(lista != null && lista.size() > 0){
											SolicitudUsuarioDerivada obj = lista.get(0);
											if(obj != null){
											  nuevo.setUsuarioProceso(obj.getUsuario().getAlias());
											}
										}									
										nuevo.setHabilitaLista(false);
									}
								}							
								
								/*guarda los emisores*/
								if(listaUsuarios != null && listaUsuarios.size() > 0 ){
									if(nuevo.getEmisor() != null){
										if(!listaUsuarios.contains(nuevo.getEmisor().trim())){
											listaUsuarios.add(nuevo.getEmisor());
										}
									}
								}else{
									if(nuevo.getEmisor() != null){
										listaUsuarios.add(nuevo.getEmisor());
									}
								}
									
								
								/*guarda los tipo de solicitudes*/
								if(listaTiposSolicitudes != null && listaTiposSolicitudes.size() > 0 ){
									if(nuevo.getDespTipoSolictud() != null){
										if(!listaTiposSolicitudes.contains(nuevo.getDespTipoSolictud().trim())){
											listaTiposSolicitudes.add(nuevo.getDespTipoSolictud().trim());
										}
									}
								}else{
									if(nuevo.getDespTipoSolictud() != null){
										listaTiposSolicitudes.add(nuevo.getDespTipoSolictud().trim());
									}
								}
								
								/*guarda las forma de pago*/
								if(listaFormaPago != null && listaFormaPago.size() > 0 ){
									if(nuevo.getCondicionPago() != null){
										if(!listaFormaPago.contains(nuevo.getCondicionPago().trim())){
											listaFormaPago.add(nuevo.getCondicionPago().trim());
										}
									}
								}else{
									if(nuevo.getCondicionPago() != null){
										listaFormaPago.add(nuevo.getCondicionPago().trim());
									}
								}
								
								
								listaTodasSolicitudesDto.add(nuevo);
								nuevo = null;
						
						    }else{
						    	nuevo.setControlador(2);
						    	nuevo.setProceso(perfil2.getDesPerfil());
						    	listaTodasSolicitudesDto.add(nuevo);
						    }
							if(listaTodasSolicitudesDto != null && listaTodasSolicitudesDto.size() == 0){
								listaTodasSolicitudesDto = null;
							}
						}
						
						if(listaTodasSolicitudesDto == null || listaTodasSolicitudesDto.size() == 0){
							FacesMessages.instance().add(Severity.ERROR, "Usted no tiene atribuciones para revisar la solicitud...!!!" );
							return;	
						}
						
						
					}else{
						listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>(0);
						if(listaTodasSolicitudesDto == null || listaTodasSolicitudesDto.size() == 0){
							FacesMessages.instance().add(Severity.ERROR, "Usted no tiene atribuciones para revisar la solicitud...!!!" );
							return;	
						}
					}
				}
			}else{
				listaTodasSolicitudesDto = new ArrayList<SolicitudDTO>(0);
			}
		} catch (Exception e) {
			log.error("error al sacar todas las solictudes en generar #0",e.getMessage());
		}
	
	}
	
	
	public boolean evaluarFechaSolicitud(Long idSolicitud, String codigoEstado){
		boolean  cambiar = false;
		try{
			Long numeroDias = scoringService.getSolicitudDiaReparoMaximo();
			SolicitudHitos hitoEstado = null;
			List<SolicitudHitos> listaSolicitudesHitos = scoringService.getSolicitudHitos(idSolicitud);
			if(listaSolicitudesHitos != null){
				for(SolicitudHitos sh: listaSolicitudesHitos){
					if(sh != null && sh.getCodigoEstado().equals(codigoEstado)){
						hitoEstado = sh;
						break;
					}
				}
			}
			
			/*
			 * recordatorio : 1 = Domingo, 2 = Lunes, 3 = Martes, 4 = Miercoles, 5 = Jueves, 6 = Viernes, 7 = Sabado
			 * */
			if(hitoEstado != null && numeroDias != null){
				log.debug("fecha hito : #0, dias ha evaluar #1", hitoEstado.getFechaHora(), numeroDias);

				SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
				Calendar cal = Calendar.getInstance();
				cal.setTime(hitoEstado.getFechaHora());
				log.debug("Fecha hitos base de datos : #0", formatoDeFecha.format(cal.getTime()));
				log.debug("Calendar.DAY_OF_WEEK Hitos : #0",cal.get(Calendar.DAY_OF_WEEK));
				Long valorHito = cal.getTimeInMillis();
				cal.add(Calendar.DATE, numeroDias.intValue());
				log.debug("Fecha hitos + dias : #0", formatoDeFecha.format(cal.getTime()));
				log.debug("Calendar.DAY_OF_WEEK Hitos + Dias: #0",cal.get(Calendar.DAY_OF_WEEK));
				
				
				
				Calendar calActual = Calendar.getInstance();
				calActual.setTime(new Date());
				log.debug("Calendar.DAY_OF_WEEK Actual: #0",calActual.get(Calendar.DAY_OF_WEEK));
				log.debug("Fecha Fecha Actual : #0", formatoDeFecha.format(calActual.getTime()));
				
				/*evaluacion final*/
				Long valor1 = cal.getTimeInMillis();
				Long valor2 = calActual.getTimeInMillis();
				log.debug("valorHito: #0",valorHito);
				log.debug("valor1 + dias : #0",valor1);
				log.debug("valor2 Actual: #0",valor2);
				
				if(valor2 > valor1){
					cambiar = true;
					log.debug("se cambia el estado DE pasarlo a NC");
				}else if((valorHito <= valor2) && (valor2 <= valor1)){
					log.debug("se habilitar link para reparar la solicitud");
					cambiar = false;
				}else{
					
				}
			}
		}catch (Exception e) {
			log.error("Error al evaluar de la fecha de devulucion #0", e.getMessage());
		}
		
	    return cambiar;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public void sacarSucursales(){
		try{
			listaSucursals = (List<Sucursal>)entityManager.createQuery("select suc from Sucursal suc " +
					" where suc.codigo in ('C212','C246','N103','N206','S133','S148','S224','C115'," +
					"                      'C227','N101','N142','N209','S118','S145','S221','S243','C138','TDS1') order by suc.descripcion asc ").getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de sucursales #0", e.getMessage());
		}		
	}
	public Sucursal obtenerObjetoSucursal(String codigo){
		Sucursal suc = null;
		if(listaSucursals != null && codigo != null){
			for(Sucursal s: listaSucursals){
				if(s.getCodigo().equals(codigo)){
					suc= s;
					break;
				}
			}
		}
		return suc;
	}

	public void sacarCPago(){
		try{
			listaCPagoDTOs = intranetSapService.getCondicionPago();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de condiciones de pagos #0", e.getMessage());
		}		
	}
	
	public CPagoDTO obtenerObjetoCPago(String codigo){
		CPagoDTO obj = null;
		if(listaCPagoDTOs != null && codigo != null){
			for(CPagoDTO s: listaCPagoDTOs){
				if(s.getCodigo().equals(codigo)){
					obj= s;
					break;
				}
			}
		}
		return obj;
	}

	public void sacarEstadosSolicitudes(){
		try{
			listaEstados = scoringService.obtenerEstados();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de estados de pagos #0", e.getMessage());
		}		
	}

	public void sacarSociedades(){
		try{
			listaSociedades = scoringService.obtenerSociedades();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de estados de pagos #0", e.getMessage());
		}		
	}
	
	
	public LcredEstado obtenerObjetoLcredEstado(String codigo){
		LcredEstado obj = null;
		if(listaEstados != null && codigo != null){
			for(LcredEstado s: listaEstados){
				if(s.getCodEstado().equals(codigo)){
					obj= s;
					break;
				}
			}
		}
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolicitudUsuarioDerivada> sacarSolicitudUsuarioDerivada(Long idSolicitud){
		List<SolicitudUsuarioDerivada> lista = null;
		try{
			lista = (List<SolicitudUsuarioDerivada>)
					entityManager.createQuery("select sud " +
							"	from SolicitudUsuarioDerivada sud" +
							" 	where sud.codEstadoDerivada=:codEstado " +
							"	and sud.confirmacionObligatoria=1 " +
							"   and sud.idSolicitud=:solAux" +
							"   and sud.confirmacion is null ")
					.setParameter("codEstado","SA")
					.setParameter("solAux",idSolicitud)
					.getResultList();
			if(lista != null && lista.size() > 0){
				log.debug("cantidad #0", lista.size());
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de sucursales #0", e.getMessage());
		}	
		return lista;
	}	
	
	@SuppressWarnings("unchecked")
	public List<SolicitudUsuarioDerivada> sacarSolicitudUsuarioDerivadaAnalisis(Long idSolicitud){
		List<SolicitudUsuarioDerivada> lista = null;
		try{
			lista = (List<SolicitudUsuarioDerivada>)
					entityManager.createQuery("select sud " +
							"	from SolicitudUsuarioDerivada sud" +
							" 	where sud.codEstadoDerivada=:codEstado " +
							"	and sud.confirmacionObligatoria=1 " +
							"   and sud.idSolicitud=:solAux" +
							"   and sud.confirmacion is null ")
					.setParameter("codEstado","B")
					.setParameter("solAux",idSolicitud)
					.getResultList();
			if(lista != null && lista.size() > 0){
				log.debug("cantidad #0", lista.size());
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de sucursales #0", e.getMessage());
		}	
		return lista;
	}
	
	
	public void sacarLogSolicitud(SolicitudDTO sol){
		try{
			if(sol != null){
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
	public void sacarLogHitos(SolicitudDTO sol){
		try{
			if(sol != null){
				
				this.setSolicitudAux(sol);
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
	public void liberarSolicitud(Long idSolicitud){
		try{
			if(idSolicitud != null){
				entityManager.createQuery("update LcredSolicitud set evaluar=0 where id.numSolicitud=:sol").setParameter("sol", idSolicitud).executeUpdate();
				entityManager.flush();
				
				String codigo = "L";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Se libera la solicitud que quedo tomada.");
					exito = scoringService.persistSolicitudLogs(idSolicitud, usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Se da aprobación la solicitud.");
					exito = scoringService.persistSolicitudLogs(idSolicitud, usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
						
				log.debug("verificacion si inserto registro logs #0", exito);
				
			}
			
		}catch (Exception e) {
			log.error("Error al libar la solicitud sleccionada #0", e.getMessage());
		}
		
	}
	
	public void reAsignarSolicitudDerivadaUsuario(SolicitudDTO solicitudDTO){
		try{
			if(solicitudDTO != null){
				
				if(solicitudDTO.getDerivado() == null){
					FacesMessages.instance().add(Severity.WARN,"Debe seleccionar al usuario aprobador a reemplazar.");
					return;				
				}
				if(solicitudDTO.getUsuarioAsignar() == null){
					FacesMessages.instance().add(Severity.WARN,"Debe seleccionar el usuario a reasignar.");
					return;				
				}	
				
				SolicitudUsuarioDerivada sud = solicitudDTO.getDerivado();
				if(sud != null){
					SolicitudUsuarioDerivada sudNueva = new SolicitudUsuarioDerivada();
					Long numero = scoringService.getSolicitudUsuarioDerivacionMaximo();
					sudNueva.setSystemId(numero);
					sudNueva.setIdSolicitud(sud.getIdSolicitud());
					sudNueva.setEstado(sud.getEstado());
					sudNueva.setFechaHora(new Date());
					sudNueva.setUsuario(solicitudDTO.getUsuarioAsignar());
					sudNueva.setConfirmacionObligatoria(true);
					sudNueva.setConfirmacion(null);
					sudNueva.setCodEstadoDerivada(sud.getCodEstadoDerivada());
					boolean exitoingreso = scoringService.persitSolicitudUsuarioDerivacion(sudNueva);
					log.debug(exitoingreso);

					
					sud.setConfirmacionObligatoria(false);
					sud.setConfirmacion(true);
					entityManager.merge(sud);
					entityManager.flush();
					entityManager.refresh(sud);
					
					
					/*se agrega la observaciones final de una aprobacion final */
					LcredSolicitudObservacionesId id = new LcredSolicitudObservacionesId();
					Long correlativo = scoringService.obtenerCorrelativoObservaciones(solicitudDTO.getIdSolictud());
					if(correlativo != null){
						id.setCorrelativo(correlativo);
						id.setFecha(new Date());
						id.setHora(new Date());
						id.setNumSolicitud(solicitudDTO.getIdSolictud());
						id.setTipoSol(solicitudDTO.getTipoSolicitud());
						id.setObservacion("Se libera la solicitud asignada al usuario "+ solicitudDTO.getDerivado().getUsuario().getNombre() +" y se reasigna al usuario "+solicitudDTO.getUsuarioAsignar().getNombre());
						id.setUsuario(usuarioSegur.getAlias());
						LcredSolicitudObservaciones obser = new LcredSolicitudObservaciones();
						obser.setId(id);
						scoringService.persistSolicitudObservaciones(obser);
					}
				}
				
				String codigo = "L";
				Estados estadoLogs = scoringService.obtenerEstados(codigo.charAt(0));
				StringBuffer cadena = new StringBuffer();
				boolean exito = false;
				if(estadoLogs != null){
					cadena.append("Se libera la solicitud y se reasigna a otro usuario.");
					exito = scoringService.persistSolicitudLogs(solicitudDTO.getIdSolictud(), usuarioSegur.getAlias(), new Date(), String.valueOf(estadoLogs.getCodEstado()), estadoLogs.getDesEstado(), cadena.toString());
				}else{
					cadena.append("Se libera la solicitud y se reasigna a otro usuario.");
					exito = scoringService.persistSolicitudLogs(solicitudDTO.getIdSolictud(), usuarioSegur.getAlias(), new Date(), "", "", cadena.toString());
				}
				log.debug("verificacion si inserto registro logs #0", exito);
			}

		}catch (Exception e) {
			log.error("Error al libar la solicitud sleccionada #0", e.getMessage());
		}
		
	}
	
	public void sacarListaUsuarioDerivado(SolicitudDTO sol){
		try{
			if(sol != null){
				this.setSolicitudAux(sol);
			    this.listUsuariosDerivados = scoringService.getSolicitudUsuarioDerivacionForSolicitudEstado(sol.getIdSolictud(), sol.getEstadoEvaluacion());
				if(this.listUsuariosDerivados != null){
					log.debug("cantidad #0", this.listUsuariosDerivados.size());
				}
			}
		}catch (Exception e) {
			log.error("Error, al sacar los usuarios que fueron derivado la solicitud, #0", e.getMessage());
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

	public void limpiarVariblesUsuarioDerevida(){
		this.listUsuariosDerivados = null;
		this.solicitudAux = null;
		
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
	
	
	/*gets y Sets*/


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

	public void obtenerObjetoSolicitudDerivadoUsuario(SolicitudDTO objeto){
		if(objeto != null){
			if(objeto.getDerivado() != null){
				log.debug("el Objeto seleccionado el usuario es #0", objeto.getDerivado().getUsuario().getNombre());
			}
		}
		
	}
	public void obtenerObjetoUsuario(SolicitudDTO objeto){
		if(objeto != null){
			if(objeto.getUsuarioAsignar() != null){
				log.debug("el Objeto seleccionado el usuario a asignar es #0", objeto.getUsuarioAsignar().getNombre());
			}
		}
		
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
	
    public List<Sociedad> getListaSociedades() {
		return listaSociedades;
	}

	public void setListaSociedades(List<Sociedad> listaSociedades) {
		this.listaSociedades = listaSociedades;
	}

	public Sociedad getSociedad() {
		return sociedad;
	}

	public void setSociedad(Sociedad sociedad) {
		this.sociedad = sociedad;
	}
	

}