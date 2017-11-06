package org.domain.sck.session.action;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.base.GlobalHitosLogdService;
import org.domain.sck.dto.CPagoDTO;
import org.domain.sck.dto.SolicitudDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.entity.Estados;
import org.domain.sck.entity.LcredEstado;
import org.domain.sck.entity.LcredPerfiles;
import org.domain.sck.entity.LcredSolicitud;
import org.domain.sck.entity.LcredSolicitudBloqueos;
import org.domain.sck.entity.LcredSolicitudCondiciones;
import org.domain.sck.entity.LcredSolicitudDm;
import org.domain.sck.entity.LcredSolicitudLcredito;
import org.domain.sck.entity.LcredSolicitudProrroga;
import org.domain.sck.entity.LcredSolicitudVtapuntual;
import org.domain.sck.entity.LcredUsuarioNivelEnc;
import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudLogs;
import org.domain.sck.entity.SolicitudUsuarioDerivada;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.session.service.intranetsap.IntranetSapService;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;



@Name("listadosSolicitudesEstadosInicialAction")
@Scope(ScopeType.CONVERSATION)
public class ListadosSolicitudesEstadosInicialAction implements Serializable{
	

	@Logger
	Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	
	@In IntranetSapService intranetSapService;
	
	@In ScoringService scoringService;
	
	@In GlobalHitosLogdService globalHitosLogdService;
	
	
	@In(value="#{identity.lcredUsuarioNivelEnc}", scope = ScopeType.SESSION)
	private LcredUsuarioNivelEnc lcredUsuarioNivelEnc;
	
	@In(value="#{identity.usuarioSegur}", scope = ScopeType.SESSION)
	private UsuarioSegurDTO usuarioSegur;
	
	
	
	private List<SolicitudDTO> listaSolicitudesDto;
	private List<Sucursal> listaSucursals;
	private List<CPagoDTO> listaCPagoDTOs;
	private List<LcredEstado> listaEstados;

	

	private LcredEstado estado;
	
	@SuppressWarnings("unchecked")
	@Create
	public void init(){
		try{
		
			if(lcredUsuarioNivelEnc != null){
			   sacarSucursales();
			   sacarCPago();
			   sacarEstadosSolicitudes();
			   sacarSolicitudes();
			}		    		

 	   }catch (Exception e) {
		log.error("error al sacar las solictudes de usuario logeado #0", e.getMessage());
	   }
	
	}
	
	@SuppressWarnings("unchecked")
	public void sacarSolicitudes(){
		Sucursal suc = null;
		try{
			
			   List<LcredSolicitud> listaSolictudIngresdas = (List<LcredSolicitud>)entityManager
                       .createQuery("Select s from LcredSolicitud s " +
                       		" Where  s.estado=:estado " +
                       		" and s.evaluar=:evaluar" +
                       		" order by s.id.numSolicitud desc" )
                       .setParameter("estado","I")
                       .setParameter("evaluar", true)
                       .getResultList();			
			
			
			
			 if(listaSolictudIngresdas != null){  
				 listaSolicitudesDto = new ArrayList<SolicitudDTO>(0);
				 for(LcredSolicitud  sol : listaSolictudIngresdas){
					 
					 
					 
					 
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
					 
					 
					 if(sol.getId().getFecSolicitud() != null && sol.getHraSolicitud() != null){
						  
						 /*rescatar la fecha */
						  SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
						  String fecha = sdf.format(sol.getId().getFecSolicitud());

						 /*rescatar la hora */
						  SimpleDateFormat sdh= new SimpleDateFormat("HH:mm");
						  String hora = sdh.format(sol.getHraSolicitud());
						  
						  /*fecha y hora*/
						  log.debug(" Fecha String:"+ fecha);
						  log.debug(" hora String :"+ hora );
						  
						  DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
						  try {
							  Date d = formatoFecha.parse(fecha+" "+hora);
							  nuevo.setFechaEmision(d); 
							  nuevo.setHoraEmision(sol.getHraSolicitud());
						  }catch (Exception e) {
						   throw new Exception(e);
						  }						  
					}
					 
					if(sol.getEstado() != null){
						LcredEstado estado = obtenerObjetoLcredEstado(sol.getEstado());
						if(estado != null){
							nuevo.setEstado(estado.getDesEstado());
						}

						if(sol.getEstado().equals("I")){
							LcredPerfiles perfil = scoringService.obtenerPerfil("3");
							if(perfil != null){
								nuevo.setProceso(perfil.getDesPerfil());
							}
						}
					}
					
					if(sol.getSucursalEmisor() != null){
						   log.debug(" codigo de sucursal  :"+ sol.getSucursalEmisor());
						   suc = obtenerObjetoSucursal(sol.getSucursalEmisor());
						   if(suc != null){
							   nuevo.setSucursal(suc.getDescripcion()); 
						   }else{
							   nuevo.setSucursal(sol.getSucursalEmisor());
						   }						
						
					}
					
					if(sol.getConPago()  != null){
						   log.debug("condicion de pago  :"+ sol.getConPago());
						   CPagoDTO obj = obtenerObjetoCPago(sol.getConPago());
						   if(obj != null){
							   nuevo.setCondicionPago(obj.getDescripcion()); 
						   }else{
							   nuevo.setCondicionPago(sol.getConPago()); 
						   }
						
					}
					 
					if(sol.getMontoAsegurado() != null){
						nuevo.setMontoAsegurado(sol.getMontoAsegurado().doubleValue());
					}else{
						nuevo.setMontoAsegurado((double) 0);
					}
					 
					if(sol.getMonto() != null){
						nuevo.setMonto(sol.getMonto().doubleValue());
					}else{
						nuevo.setMonto((double) 0);
					}
						
					if(sol.getRiesgoKupfer() != null){
						nuevo.setMontoRiegoKupfer(sol.getRiesgoKupfer().doubleValue());
					}else{
						nuevo.setMontoRiegoKupfer((double) 0);
					}
					
					if(sol.getEvaluar() != null){
						nuevo.setEvaluar(sol.getEvaluar());
					}else{
						nuevo.setEvaluar(false);
					}					
					listaSolicitudesDto.add(nuevo);
					nuevo = null;
				 }
			   } 			
			
		}catch (Exception e) {
			log.error("Error, al sacar los solictudes pendiente de liberar. #0", e.getMessage());
		}
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void sacarSucursales(){
		try{
			listaSucursals = (List<Sucursal>)entityManager.createQuery("select suc from Sucursal suc ").getResultList();
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
	@SuppressWarnings("unchecked")
	public void sacarSolicitudUsuarioDerivada(Long idSolicitud){
		List<SolicitudUsuarioDerivada> lista = null;
		try{
			lista = (List<SolicitudUsuarioDerivada>)
					entityManager.createQuery("select sud " +
							"	from SolicitudUsuarioDerivada sud" +
							" 	where ").getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de sucursales #0", e.getMessage());
		}		
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
			sacarSolicitudes();
		}catch (Exception e) {
			log.error("Error al libar la solicitud sleccionada #0", e.getMessage());
		}
		
	}
	
	
	
	
	/*gets y Sets*/


	public List<SolicitudDTO> getListaSolicitudesDto() {
		return listaSolicitudesDto;
	}

	public void setListaSolicitudesDto(List<SolicitudDTO> listaSolicitudesDto) {
		this.listaSolicitudesDto = listaSolicitudesDto;
	}

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

	
	
	

}
