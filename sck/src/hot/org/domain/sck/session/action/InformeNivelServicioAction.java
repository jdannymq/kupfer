package org.domain.sck.session.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.base.GlobalHitosLogdService;
import org.domain.sck.base.GlobalService;
import org.domain.sck.dto.CPagoDTO;
import org.domain.sck.dto.SolicitudDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.entity.Canal;
import org.domain.sck.entity.CanalUsuarioCargo;
import org.domain.sck.entity.ConceptosNegocio;
import org.domain.sck.entity.Estados;
import org.domain.sck.entity.LcredEstado;
import org.domain.sck.entity.LcredSolicitudObservaciones;
import org.domain.sck.entity.LcredSolicitudObservacionesId;
import org.domain.sck.entity.LcredTipoSolicitud;
import org.domain.sck.entity.LcredUsuarioNivelEnc;
import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudLogs;
import org.domain.sck.entity.SolicitudUsuarioDerivada;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.Usuariosegur;
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


@Name("informeNivelServicioAction")
@Scope(ScopeType.CONVERSATION)
public class InformeNivelServicioAction {
	

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
	
	private List<CanalUsuarioCargo> listaCanalUsuarioCargos;
   
	@Create
	public void init(){
		try{
		
			if(lcredUsuarioNivelEnc != null){
				sacarNegocios();
				sacarCanales();
				sacarUsuarios();
				sacarTipoSolicitudes();
			    sacarSucursales();
			    sacarCPago();
			    sacarEstadosSolicitudes();
			    
			}		    		
 	   }catch (Exception e) {
		log.error("error al sacar las solictudes de usuario logeado #0", e.getMessage());
	   }
	}
	
	@SuppressWarnings("unchecked")
	public void sacarUsuarios(){
		try{
    		List<Usuariosegur> lista = (List<Usuariosegur>)entityManager
				.createQuery("select u from Usuariosegur u where u.alias <> null and u.nombre <> null  order by u.nombre")
				.getResultList();
    		
    		if(lista != null){
    			listaUsuariosegurs = new ArrayList<Usuariosegur>(0);
    			for(Usuariosegur usuario : lista){
    				if(usuario != null && !usuario.getAlias().equals("") && !usuario.getAlias().equals("xxxx") && !usuario.getAlias().equals("xxxxxx") ){
    					if(usuario.getNombre() != null && !usuario.getNombre().equals("")){
	    					if(!listaUsuariosegurs.contains(usuario)){
	    						listaUsuariosegurs.add(usuario);
	    					}
    					}
    				}
    			}
    		}			
		}catch (Exception e) {
			log.error("Error, al sacar los datos de usuarios #0", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void sacarCanales(){
		try{
			listaCanales = entityManager.createQuery("select c from Canal c where c.disabled=0  order by c.systemId").getResultList();
			if(listaCanales == null || listaCanales.size() == 0){			
				listaCanales = new ArrayList<Canal>(0);
			}			
		}catch (Exception e) {
			log.error("Error, al sacar los datos del canal #0", e.getMessage());
		}
	}	

	public void sacarNegocios(){
		try{
			listaNegocios = scoringService.obtenerNegocios();
			if(listaNegocios == null || listaNegocios.size() == 0){			
				listaNegocios = new ArrayList<ConceptosNegocio>(0);
			}			
		}catch (Exception e) {
			log.error("Error, al sacar los datos del canal #0", e.getMessage());
		}
	}
	
	public void sacarTipoSolicitudes(){
		try{
			listaLcredTipoSolicituds = scoringService.getListadoLcredTipoSolicitudOrdenadoPorCorrelativo();
			if(listaLcredTipoSolicituds == null && listaLcredTipoSolicituds.size() == 0){
				listaLcredTipoSolicituds = new ArrayList<LcredTipoSolicitud>(0);
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de tipo de solicitudes #0", e.getMessage());
		}		
	}	

	public void seleccionarTipoSolicitud(){
		if(this.tipoSolicitud != null){
			log.error(" el tipo de solicitud es: #0", this.tipoSolicitud.getDesTipoSolicitud());
		}
	}	

	public void seleccionarUsuario(){
		if(this.tipoSolicitud != null){
			log.error(" el tipo de solicitud es: #0", this.tipoSolicitud.getDesTipoSolicitud());
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
	
	public boolean validarParametros(){
		try{
			
			if(this.idSolicitud == null && (this.rutAux == null || "".equals(this.rutAux)) && this.fechaInical == null 
			&& this.fechaFinal == null && this.estado == null && this.sucursal == null){
				
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

		}
	}

	public void limpiarParametos(){
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

	
	public void sacarUsuariosSegunSucursalesSeleccionadas(){
		int contador = 0;
		StringBuffer cadena = new StringBuffer();
		try{
			if(listaSucursalsSeleccionados != null && listaSucursalsSeleccionados.size() != 0){
				int cantidad = listaSucursalsSeleccionados.size();
				for(Sucursal s : listaSucursalsSeleccionados){
					if((contador + 1) == cantidad){
						cadena.append("'");cadena.append(s.getCodigo());cadena.append("'");
					}else{
						cadena.append("'");cadena.append(s.getCodigo());cadena.append("'");cadena.append(",");
						contador++;
					}
				}
				log.debug("cadena #0", cadena.toString());
				
				if(cadena != null && !"".equals(cadena.toString())){
					listaUsuariosegurs = scoringService.getListaUsuariosSegunMatrizResponsabilidades(cadena.toString());
				}
			}else{
				listaUsuariosegurs = new ArrayList<Usuariosegur>();
			}
		}catch (Exception e) {
			log.error("error, al sacar los usuarios de segun responsabilidad #0", e.getMessage());
		}
		
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
	
	
	

}