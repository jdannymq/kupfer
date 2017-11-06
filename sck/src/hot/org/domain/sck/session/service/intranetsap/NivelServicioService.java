package org.domain.sck.session.service.intranetsap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.domain.sck.dto.*;
import org.domain.sck.entity.*;
import org.domain.sck.entity.enums.TipoSolicitudCodigoType;
import org.domain.sck.session.service.scoring.ScoringService;
import org.domain.sck.utils.comparators.UsuarioNivelServicioComparator;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;


@Name("nivelServicioService")
@AutoCreate
@SuppressWarnings({"unchecked"})
public class NivelServicioService {

	@Logger Log log;
	@In(value="#{emIntranetSAPSck}")
	EntityManager emIntranetSAPSck;
	
	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	@In ScoringService scoringService;

    private String[] EXCLUDED_TYPES = {"AE", "RE"};

    private final static String[] HITOS_ANALISTA = {"B","N"}; // TODO deber��a ir B?
    private final static String[] HITOS_INICIAL = {"I","E","DC","DR","AD"};
            // ORIGINAL {"I","E","DC","DR","NC","DA","SA"};
    private final static String[] HITOS_EMISOR = {"DE"};
                    // ORIGINAL {"DE","B"};
    private final static String[] HITOS_APROBADORES = {"SA"};
                            // Original {"AP","RP"};
    private final static String[] HITOS_FINALES = {"A","R","NC"};
    private final static String[] HITOS_FINALES_ELIMINADOS = {"A","R","NC","NU"};
    private final static String[] HITOS_APRUEBA_RECHAZA = {"AP","RP"};
    private final static String SOLICITA_APROBACION = "SA";
    private final static String ENVIO_SEGURO = "ES";
    private final static String HITO_INICIAL = "I";
    private final static String HITO_ELIMINADO = "NU";
    private final static String HITO_ANALISIS_EJECUTIVO = "E";
    private final UsuarioNivelServicioComparator USUARIO_NS_COMPARATOR = new UsuarioNivelServicioComparator();
    private List<Usuariosegur> usuariosEncontrados = new ArrayList<Usuariosegur>();
    DateFormat sdf = new SimpleDateFormat("EEEEEE dd-MM-yyyy HH:mm:ss");
    DateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Calendar inicio;
    private Calendar fin;
    private Jornada jornada;
    private FiltroDTO filtro;

	 
	/*
	 * Parametros de entrada: nada. 
	 * Return : retorna la lista de las clasificacion de riesgo.
	 * */		
	@SuppressWarnings("unchecked")
	public List<ClsRiesgoDTO> getClasificacionRiesgo(){
		List<ClsRiesgoDTO> listaClasificacion = null;
		try{
			 List<Object[]> lista = (List<Object[]>)emIntranetSAPSck
					.createNativeQuery(" SELECT cast([CL_RIESGO] as varchar) as codigo,  cast([DESCRIPCION] as varchar) as des" +
							" FROM [INTRANET_SAP].[dbo].[CL_RIESGO] order by [CL_RIESGO]")
					.getResultList();
			 if(lista != null){
				 listaClasificacion = new ArrayList<ClsRiesgoDTO>(0);
				 for(Object[] array : lista){
					 ClsRiesgoDTO cls = new ClsRiesgoDTO();
					 if(array[0] != null && !"".equals(array[0].toString())){
						 cls.setCodigo(array[0].toString().replaceAll("\\s+", " "));
					 }
					 if(array[1]!= null &&  !"".equals(array[1].toString())){
						 cls.setDescripcion(array[1].toString().replaceAll("\\s+", " "));
					 }
					 
					 if(cls.getCodigo() != null && !"".equals(cls.getCodigo())){
						 listaClasificacion.add(cls); 
					 }
				 }
			 }
			
		}catch (Exception e) {
			log.error("Error, al sacar la descripcion del tipo de cliente cav #0 ", e.getMessage());
		}
		
		return listaClasificacion;
	}
	 
	/*
	 * Parametros de entrada: nada. 
	 * Return : retorna la lista de condiciones de pago.
	 * */		
	@SuppressWarnings("unchecked")
	public List<CPagoDTO> getCondicionPago(){
		List<CPagoDTO> listaCondicionPago = null;
		try{
			 List<Object[]> lista = (List<Object[]>)emIntranetSAPSck
					.createNativeQuery(" SELECT cast([cpago] as varchar) as codigo,  cast([descripcion] as varchar) as des" +
							" FROM [INTRANET_SAP].[dbo].[CPAGO] order by [cpago]")
					.getResultList();
			 if(lista != null){
				 listaCondicionPago = new ArrayList<CPagoDTO>(0);
				 for(Object[] array : lista){
					 CPagoDTO cp = new CPagoDTO();
					 if(array[0] != null &&  !"".equals(array[0].toString())){
						 cp.setCodigo(array[0].toString().replaceAll("\\s+", " "));
					 }
					 if(array[1]!= null &&  !"".equals(array[1].toString())){
						 cp.setDescripcion(array[1].toString().replaceAll("\\s+", " "));
					 }
					 if(cp.getCodigo() != null && !"".equals(cp.getCodigo())){
						 listaCondicionPago.add(cp); 
					 }
				 }
			 }
		}catch (Exception e) {
			log.error("Error, al sacar la descripcion del tipo de cliente cav #0 ", e.getMessage());
		}
		
		return listaCondicionPago;
	}
	 	
	
	/*
	 * Parametros de entrada: codigo de pago. 
	 * Return : retorna la descripcion de la clasificacion de riesgo.
	 * */		


	

	
	/*
	 * Parametros de entrada: . 
	 * Return : Lista de Zonas.
	 * */		
	@SuppressWarnings("unchecked")
	public List<SucursalesUsuarioDTO> sacarListaSucursales(boolean ticket){
		List<SucursalesUsuarioDTO> listaSucursales = null;
		SucursalesUsuarioDTO sucursal = null;
		try{
			List<Object[]> lista = (List<Object[]>)emIntranetSAPSck
					.createNativeQuery("select cast([Codigo] as varchar) as codigo, " +
							" cast([Descripcion] as varchar) as des ," +
							" cast([Zona] as varchar) as zona ," +
							" cast([Zona2] as varchar) as zona2  " +
							" from [INTRANET_SAP].[dbo].[Sucursal] " +
							" where [Zona] is not null ")
					.getResultList();
			
			if(lista != null){
				listaSucursales = new ArrayList<SucursalesUsuarioDTO>(0);
				for(Object[] objeto : lista){
					if(objeto != null){
						sucursal = new SucursalesUsuarioDTO();
						if(objeto[0] != null ){
							sucursal.setCodigo(objeto[0].toString());
						}
						if(objeto[1] != null ){
							sucursal.setDescripcion(objeto[1].toString());
						}
						
						if(objeto[2] != null ){
							sucursal.setZona(objeto[2].toString());
						}
						if(objeto[3] != null ){
							sucursal.setZona2(objeto[3].toString());
						}						
						
						sucursal.setStatus(ticket);
						if(sucursal.getZona() != null && !sucursal.getZona().equals("")){
							listaSucursales.add(sucursal);
						}
					}
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al sacar la lista de las sucursales #0 ", e.getMessage());
		}
		return listaSucursales;
	}
	
	/*
	 * Parametros de entrada: . 
	 * Return : Lista de Negocios.
	 * */		
	@SuppressWarnings("unchecked")
	public List<NegocioUsuarioDTO> sacarListaNegocios(boolean ticket){
		List<NegocioUsuarioDTO> listaNegocios = null;
		NegocioUsuarioDTO negocio = null;
		try{
			List<Object[]> lista = (List<Object[]>)emIntranetSAPSck
					.createNativeQuery("select cast([Negocio] as varchar) as codigo, " +
							" cast([Des_Negocio] as varchar) as des from [INTRANET_SAP].[dbo].[ConceptosNegocio] group by [Negocio],[Des_Negocio] ")
					.getResultList();
			
			if(lista != null){
				listaNegocios = new ArrayList<NegocioUsuarioDTO>(0);
				for(Object[] objeto : lista){
					if(objeto != null){
						negocio = new NegocioUsuarioDTO();
						if(objeto[0] != null ){
							negocio.setCodigo(objeto[0].toString());
						}
						if(objeto[1] != null ){
							negocio.setDescripcion(objeto[1].toString());
						}
						negocio.setStatus(ticket);
						listaNegocios.add(negocio);
					}
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al sacar la descripcion del tipo de cliente cav #0 ", e.getMessage());
		}
		return listaNegocios;
	}
	
	/*
	 * Parametros de entrada: . 
	 * Return : Lista de Conceptos.
	 * */		
	@SuppressWarnings("unchecked")
	public List<ConceptoUsuarioDTO> sacarListaConceptos(boolean ticket){
		List<ConceptoUsuarioDTO> listaConceptos = null;
		ConceptoUsuarioDTO concepto = null;
		try{
			List<Object[]> lista = (List<Object[]>)emIntranetSAPSck
					.createNativeQuery("select cast([Negocio] as varchar) as codigo, " +
							"                  cast([Concepto] as varchar) as concepto, " +
							"				   cast([Descripcion] as varchar) as descripcion " +
							"			from [INTRANET_SAP].[dbo].[ConceptosNegocio] ")
					.getResultList();
			
			if(lista != null){
				listaConceptos = new ArrayList<ConceptoUsuarioDTO>(0);
				for(Object[] objeto : lista){
					if(objeto != null){
						concepto = new ConceptoUsuarioDTO();
						if(objeto[0] != null ){
							concepto.setCodigoNegocio(objeto[0].toString());
						}						
						
						if(objeto[1] != null ){
							concepto.setCodigo(objeto[1].toString());
						}
						if(objeto[2] != null ){
							concepto.setDescripcion(objeto[2].toString());
						}
						concepto.setStatus(ticket);
						listaConceptos.add(concepto);
					}
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al sacar la lista de conceptos  #0 ", e.getMessage());
		}
		return listaConceptos;
	}

    public NivelServicioDTO procesarSolicitudes(FiltroDTO filtros) {
        jornada = scoringService.getSacarUltimaJornada();
        filtro = filtros;
        NivelServicioDTO dto =  obtenerSolicitudesFiltrada();
        return dto;
    }

    private PlanillaDTO procesarHitos(PlanillaDTO planillaDTO) {
        Long tiempoEmisor = 0l;
        Long tiempoAnalista = 0l;
        Long tiempoAprobadores = 0l;
        Long tiempoInicial = 0l;
        Long tiempoTotal = 0l;
        PlanillaHitosDTO ultimoHito = planillaDTO.getListaHitos().get(0);
        PlanillaHitosDTO solicitaAprobacion = null;
        List<PlanillaHitosDTO> hitosDTOList = new ArrayList<PlanillaHitosDTO>(planillaDTO.getListaHitos().size());
        hitosDTOList.addAll(planillaDTO.getListaHitos());
        hitosDTOList.remove(0);
        planillaDTO.getListaHitos().clear();
        
        
        //log.debug("----------------- Solicitud #0 fecha #1----------------------- ", planillaDTO.getNumSolicitud(),sdf.format(planillaDTO.getFechaEmisor()));
        for(PlanillaHitosDTO hitosDTO : hitosDTOList) {
            Long resultado;
            planillaDTO.getTipoHitos().add(hitosDTO.getEstado());
            if((planillaDTO.getEmisor() == null || planillaDTO.getEmisor().isEmpty()) && hitosDTO.getEstado().equals(HITO_ANALISIS_EJECUTIVO)) {
                planillaDTO.setEmisor(hitosDTO.getUsuarioUsername());
                planillaDTO.setNombreEmisor(hitosDTO.getUsuarioNombre());
            }
            if(solicitaAprobacion != null && Arrays.asList(HITOS_APRUEBA_RECHAZA).contains(hitosDTO.getEstado())) {
                resultado = diffTime(solicitaAprobacion.getFechaHito(), hitosDTO.getFechaHito());
                hitosDTO.setTiempoHito(resultado);
            }else
                resultado = diffTime(ultimoHito.getFechaHito(), hitosDTO.getFechaHito());
            if(solicitaAprobacion != null) {
                if(Arrays.asList(HITOS_APRUEBA_RECHAZA).contains(hitosDTO.getEstado())) {
                    solicitaAprobacion.setTiempoHito(resultado);
                }else {
                	solicitaAprobacion = null;
                }
            }
            if(SOLICITA_APROBACION.equals(hitosDTO.getEstado())) {
                solicitaAprobacion = hitosDTO;
            }
            if(!Arrays.asList(HITOS_APRUEBA_RECHAZA).contains(ultimoHito.getEstado())) {
                ultimoHito.setTiempoHito(resultado);
            }
            
            planillaDTO.getListaHitos().add(ultimoHito);
            if(Arrays.asList(HITOS_APRUEBA_RECHAZA).contains(ultimoHito.getEstado()) && 
            		!Arrays.asList(HITOS_APRUEBA_RECHAZA).contains(hitosDTO.getEstado())) {
            	PlanillaHitosDTO hitoDR = new PlanillaHitosDTO();
            	hitoDR.setDescripcionEstado("Derivada Remitente");
            	hitoDR.setEstado("DR");
            	hitoDR.setFecha(ultimoHito.getFecha());
            	hitoDR.setFechaHito(ultimoHito.getFechaHito());
            	hitoDR.setFechaRealAsString(ultimoHito.getFechaRealAsString());
            	hitoDR.setFechaModificadaAsString(ultimoHito.getFechaModificadaAsString());
            	hitoDR.setUsuarioNombre(ultimoHito.getUsuarioNombre());
            	hitoDR.setUsuarioUsername(ultimoHito.getUsuarioUsername());
            	hitoDR.setTiempoHito(resultado);
            	planillaDTO.getListaHitos().add(hitoDR);
            	//ultimoHito = hitoDR;
            } 
        	ultimoHito = hitosDTO;
            
        }
        
        planillaDTO.getListaHitos().add(ultimoHito);
        	//log.debug("-------- RESULTADO HITOS CALCULA TIEMPO GLOBALES; ---------");
    	
		for(PlanillaHitosDTO hitosDTO : planillaDTO.getListaHitos()) { 
			//log.debug("hito #0 estado #4 #1 fecha #2 tiempo #3", hitosDTO.getIdSystem(), 
		    //		hitosDTO.getDescripcionEstado(), sdf.format(hitosDTO.getFechaHito()), 
		    //		hitosDTO.getTimeAsHHMMSS(), hitosDTO.getEstado());
			Long resultado = hitosDTO.getTiempoHito();
			if(!Arrays.asList(HITOS_APRUEBA_RECHAZA).contains(hitosDTO.getEstado())) 
	            tiempoTotal += resultado;
			if(Arrays.asList(HITOS_INICIAL).contains(hitosDTO.getEstado())) {
                tiempoInicial += resultado;
            }else if(Arrays.asList(HITOS_ANALISTA).contains(hitosDTO.getEstado())) {
                tiempoAnalista += resultado;
            }else if(Arrays.asList(HITOS_EMISOR).contains(hitosDTO.getEstado())) {
                tiempoEmisor += resultado;
            }else if(Arrays.asList(HITOS_APROBADORES).contains(hitosDTO.getEstado())) {
                tiempoAprobadores += resultado;
            }
		}
		planillaDTO.setTotalTime(tiempoTotal);
        planillaDTO.setTiempoInicial(tiempoInicial);
        planillaDTO.setTiempoAnalista(tiempoAnalista);
        planillaDTO.setTiempoAprobadores(tiempoAprobadores);
        planillaDTO.setTiempoEmisor(tiempoEmisor);
        //log.debug("-------- TIempo total #0  analista #1 emisor #2 aprobador #3 inicial #4----------",
        //        planillaDTO.getTotalTimeAsHHMMSS(), planillaDTO.getAnalystTimeAsHHMMSS(), planillaDTO.getEmisorTimeAsHHMMSS(),
        //        planillaDTO.getApproverTimeAsHHMMSS(), planillaDTO.getInitialTimeAsHHMMSS());
        return planillaDTO;
    }


	
	private NivelServicioDTO obtenerSolicitudesFiltrada(){
		List<LcredSolicitud> solicitudes = obtenerSolicitudesFiltradas();
		NivelServicioDTO nivelServicioDTO = new NivelServicioDTO();
		for(LcredSolicitud s: solicitudes){
			PlanillaDTO aux = adaptar(s);
			log.debug("id " + aux.getIdSolictud());
			aux.setListaHitos(getHitosByID(aux.getNumSolicitud()));
            aux = procesarHitos(aux);
            if(aux.getListaHitos().size() > 1) {
            	aux = comprobarEstadoSolicitud(aux);
            }
            
            /** Nivel Global **/ 
            if(aux.getCumplida().isCumplida()) {
            	nivelServicioDTO.getTotalCumplidas().add(aux);
            }else {
            	nivelServicioDTO.getTotalNoCumplidas().add(aux);
            }
            /** ANalisis por distinta area**/
            if(!aux.getCumplidoAnalista().isNoAplica()) {
            	if(aux.getCumplidoAnalista().isCumplida()) {
            		nivelServicioDTO.getTotalCumplidosAnalista().add(aux);
            	}else{
            		nivelServicioDTO.getTotalNoCumplidosAnalista().add(aux);
            	}
            	nivelServicioDTO.getTotalAnalista().add(aux);
            }
            if(!aux.getCumplidoAprobadores().isNoAplica()) {
            	if(aux.getCumplidoAprobadores().isCumplida()) {
            		nivelServicioDTO.getTotalCumplidasAprobador().add(aux);
            	}else{
            		nivelServicioDTO.getTotalNoCumplidasAprobador().add(aux);
            	}
            	nivelServicioDTO.getTotalAprobador().add(aux);
            }
            if(!aux.getCumplidoEmisor().isNoAplica()) {
            	if(aux.getCumplidoEmisor().isCumplida()) {
            		nivelServicioDTO.getTotalCumplidasEmisor().add(aux);
            	}else{
            		nivelServicioDTO.getTotalNoCumplidasEmisor().add(aux);
            	}
            	nivelServicioDTO.getTotalEmisor().add(aux);
            }
            if(!aux.getCumplidoInicial().isNoAplica()) {
            	if(aux.getCumplidoInicial().isCumplida()) {
            		nivelServicioDTO.getTotalCumplidasEjecutivo().add(aux);
            	}else{
            		nivelServicioDTO.getTotalNoCumplidasEjecutivo().add(aux);
            	}
            	nivelServicioDTO.getTotalEjecutivo().add(aux);
            }
            
            nivelServicioDTO.getTotal().add(aux);
        }

        nivelServicioDTO.setTotalSolicitudes(getTotalSolicitudes());
        nivelServicioDTO.setTotalActivas(getTotalActivas());
        nivelServicioDTO.setTotalEliminadas(getTotalEliminadas());
        nivelServicioDTO.setTotalAbiertas(getTotalAbiertas());
        nivelServicioDTO.setSolicitudesAbiertasList(procesarSolicitudesAbiertas((long)nivelServicioDTO.getTotalActivas().size()));
        nivelServicioDTO.setUsuarioNivelServicioDTOList(procesarSolicitudesUsuario(nivelServicioDTO.getTotalEjecutivo()));
        nivelServicioDTO.setUsuarioNivelServicioPorTipoDTOList(getTiposSolicitudes());
        log.debug("TOTAL: total #0 inicial #1 aprobadores #2 emisor #3 analista #4",
				nivelServicioDTO.getTotal().size(), nivelServicioDTO.getTotalEjecutivo().size(), nivelServicioDTO.getTotalAprobador().size(),
				nivelServicioDTO.getTotalEmisor().size(), nivelServicioDTO.getTotalAnalista().size());
		log.debug("CUMPLIDAS total #0 inicial #1 aprobadores #2 emisor #3 analista #4",
                nivelServicioDTO.getTotalCumplidas().size(), nivelServicioDTO.getTotalCumplidasEjecutivo().size(),
                nivelServicioDTO.getTotalCumplidasAprobador().size(),
                nivelServicioDTO.getTotalCumplidasEmisor().size(), nivelServicioDTO.getTotalCumplidosAnalista().size());
		log.debug("NO CUMPLIDAS total #0 inicial #1 aprobadores #2 emisor #3 analista #4",
                nivelServicioDTO.getTotalNoCumplidas().size(), nivelServicioDTO.getTotalNoCumplidasEjecutivo().size(),
                nivelServicioDTO.getTotalNoCumplidasAprobador().size(),
                nivelServicioDTO.getTotalNoCumplidasEmisor().size(), nivelServicioDTO.getTotalNoCumplidosAnalista().size());
		return nivelServicioDTO;
	}
	
    private List<SolicitudesAbiertasDTO> procesarSolicitudesAbiertas(Long totalActivas) {
        List<LcredSolicitud> solicitudesAbiertas = getSolicitudesAbiertas();
        List<SolicitudesAbiertasDTO> solicitudesAbiertasDTOList = new ArrayList<SolicitudesAbiertasDTO>();
        for(LcredSolicitud solicitud : solicitudesAbiertas) {
            String estado = solicitud.getEstado();
            SolicitudesAbiertasDTO s = new SolicitudesAbiertasDTO(estado);
            int indexOf = solicitudesAbiertasDTOList.indexOf(s);
            if(indexOf != -1) {
                s = solicitudesAbiertasDTOList.get(indexOf);
            }else {
            	LcredEstado lcredEstado = entityManager.find(LcredEstado.class, estado);
            	s.setDescEstado(lcredEstado.getDesEstado());
                solicitudesAbiertasDTOList.add(s);
            }
            s.getSolicitudList().add(adaptar(solicitud));
        }
        for(SolicitudesAbiertasDTO s : solicitudesAbiertasDTOList) {
            Double resultado;
            try {
                resultado = new Double((double) s.getSolicitudList().size() / (double) totalActivas);
            }catch (Exception e) {
                resultado = new Double(0d);
            }
            resultado = resultado * 100;
            s.setPorcentaje(resultado);
        }
        return solicitudesAbiertasDTOList;
    }

    private List<UsuarioNivelServicioDTO> procesarSolicitudesUsuario(List<PlanillaDTO> solicitudes) {
        List<UsuarioNivelServicioDTO> solicitudesUsuario = new ArrayList<UsuarioNivelServicioDTO>();
        for(PlanillaDTO solicitud : solicitudes) {
            String username = solicitud.getEmisor();
            UsuarioNivelServicioDTO u = new UsuarioNivelServicioDTO(username);
            int indexOf = solicitudesUsuario.indexOf(u);
            if(indexOf != -1) {
                u = solicitudesUsuario.get(indexOf);
            }else {
                u.setNombre(solicitud.getNombreEmisor());
                u.setUsuarioNivelServicioPorTipoDTOList(getTiposSolicitudes());
                solicitudesUsuario.add(u);
            }
            UsuarioNivelServicioPorTipoDTO tipoDTO = u.getTipoByType(solicitud.getTipoSolicitudCodigoType());
            if(solicitud.getCumplidoInicial().equals(EstadoNivelServicioSolicitud.CUMPLIDA)) {
                u.getCumplidasList().add(solicitud);
                if(tipoDTO != null) {
                    tipoDTO.getCumplidasList().add(solicitud);
                }

            }else {
                u.getNoCumplidasList().add(solicitud);
                if(tipoDTO != null) {
                    tipoDTO.getNoCumplidasList().add(solicitud);
                }
            }

        }
        Collections.sort(solicitudesUsuario,USUARIO_NS_COMPARATOR);
        return solicitudesUsuario;
    }

    private List<UsuarioNivelServicioPorTipoDTO> getTiposSolicitudes() {
        if(filtro.getListaLcredTipoSolicitudsSeleccionados() == null || filtro.getListaLcredTipoSolicitudsSeleccionados().isEmpty()) {
            return new ArrayList<UsuarioNivelServicioPorTipoDTO>();
        }
        List<UsuarioNivelServicioPorTipoDTO> tipoDTOs = new ArrayList<UsuarioNivelServicioPorTipoDTO>();
        for(LcredTipoSolicitud tipoSolicitud : filtro.getListaLcredTipoSolicitudsSeleccionados()) {
            TipoSolicitudCodigoType solicitudCodigoType = TipoSolicitudCodigoType.getTipoSolicitudByCodigo(tipoSolicitud.getCodTipoSolicitud()) ;
            UsuarioNivelServicioPorTipoDTO tipoDto = new UsuarioNivelServicioPorTipoDTO(solicitudCodigoType);
            tipoDTOs.add(tipoDto);
        }
        return tipoDTOs;
    }
	
	private PlanillaDTO comprobarEstadoSolicitud(PlanillaDTO aux) {
		String tipoSolicitud = TipoSolicitudCodigoType.getCodigoTipoByCodigoSolicitud(aux.getTipoSolicitud());
		MatrizNivelServicio matriz = getUltimaMatrizByTipoSolicitud(tipoSolicitud);
		Set<String> tipoHitos = aux.getTipoHitos();
		if(matriz != null) {
			if(existeTipoHito(tipoHitos, HITOS_ANALISTA)) {
				if(aux.getTiempoAnalista() > matriz.getTiempoAnalista()) {
					aux.setCumplidoAnalista(EstadoNivelServicioSolicitud.NO_CUMPLIDA);
				}else {
					aux.setCumplidoAnalista(EstadoNivelServicioSolicitud.CUMPLIDA);
				}
			}
			if(existeTipoHito(tipoHitos, HITOS_APROBADORES)) {
				if(aux.getTiempoAprobadores() > matriz.getTiempoAnalista()) {
					aux.setCumplidoAprobadores(EstadoNivelServicioSolicitud.NO_CUMPLIDA);
				}else {
					aux.setCumplidoAprobadores(EstadoNivelServicioSolicitud.CUMPLIDA);
				}
			}
			if(existeTipoHito(tipoHitos, HITOS_ANALISTA)) {
				if(aux.getTiempoEmisor() > matriz.getTiempoAnalista()) {
					aux.setCumplidoEmisor(EstadoNivelServicioSolicitud.NO_CUMPLIDA);
				}else {
					aux.setCumplidoEmisor(EstadoNivelServicioSolicitud.CUMPLIDA);
				}
			}
			
			if(aux.getTiempoInicial() > matriz.getTiempoAnalista()) {
				aux.setCumplidoInicial(EstadoNivelServicioSolicitud.NO_CUMPLIDA);
				aux.setCumplida(EstadoNivelServicioSolicitud.NO_CUMPLIDA);
			}else {
				aux.setCumplidoInicial(EstadoNivelServicioSolicitud.CUMPLIDA);
			}
			
		}else {
			log.error("No se pudo encontrar matriz de responsabilidad para tipo solicitud codigo #0", tipoSolicitud);
		}
		return aux;
		
	}
	
	private boolean existeTipoHito(Set<String> tipoHitos, String[] hitosBuscados) {
		for(String hitoBuscado : hitosBuscados) {
			if(tipoHitos.contains(hitoBuscado))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private List<LcredSolicitud> obtenerSolicitudesFiltradas(){
        /* Query para test
		List<LcredSolicitud> solicitudes = entityManager.createQuery(""
				+ "select l from LcredSolicitud l "
				+ "where (select count(h) from SolicitudHitos h where h.idSolicitud = l.id.numSolicitud) > 4 "
                + "order by l.id.numSolicitud desc  ")
				.setMaxResults(15)
				.getResultList();

        = entityManager.createQuery(
        "select l from LcredSolicitud l " +
        "where l.id.fecSolicitud between :fechaInicial and :fechaFinal " + // Por fecha
        "and (select count(h) from SolicitudHitos h " + // Por usuario
        "where h.idSolicitud = l.id.numSolicitud and h.codigoEstado = 'E' " + // Por usuario
        "and lower(h.usuarioActual) in (:usuarios)) > 0 " + // Por usuario
        "and l.codSucursal in (:sucursales)  " + // Por sucursales
        "and l.canal in (:canales)" + // Canales
        "and l.tipTiposol in (:tipos) " +
        "and (select count(s) from SolicitudConceptosNegocioLC s " +      // Por concepto Negocio
        "where s.solicitud.id.numSolicitud = l.id.numSolicitud " +
        "and s.conceptosNegocio.id in (:conceptos)) > 0 " +
        "order by l.id.numSolicitud desc  ")
        .setMaxResults(15)
        .getResultList();
        */


        String queryInicial =  "select l from LcredSolicitud l  " ;
        StringBuilder strQuery = getQueryStringBuilder(queryInicial);
        strQuery.append("and l.estado in (:finales) ");
        strQuery.append("order by l.id.numSolicitud desc  ");
        Query query = getQueryAndSetParameter(strQuery.toString())
                .setParameter("finales", Arrays.asList(HITOS_FINALES));

        List<LcredSolicitud> solicitudes = query
                .getResultList();
		return solicitudes;
	}

    private List<LcredSolicitud> getSolicitudesAbiertas() {
        String queryInicial =  "select l from LcredSolicitud l  " ;
        StringBuilder strQuery = getQueryStringBuilder(queryInicial);
        strQuery.append("and l.estado not in (:finales) ");
        strQuery.append("order by l.id.numSolicitud desc  ");
        Query query = getQueryAndSetParameter(strQuery.toString())
                .setParameter("finales", Arrays.asList(HITOS_FINALES_ELIMINADOS));
        List<LcredSolicitud> solicitudes = query
                .getResultList();
        return solicitudes;
    }
    private List<PlanillaDTO> adaptarList(List<LcredSolicitud> solicitudes) {
    	List<PlanillaDTO> planillaDTOs = new ArrayList<PlanillaDTO>();
    	if(solicitudes != null &&  !solicitudes.isEmpty()) {
		    for(LcredSolicitud s: solicitudes){
				planillaDTOs.add(adaptar(s));
		    }
    	}
	    return planillaDTOs;
    }
    
    private List<PlanillaDTO> getTotalAbiertas() {
        String queryInicial =  "select l from LcredSolicitud l  " ;
        StringBuilder strQuery = getQueryStringBuilder(queryInicial);
        strQuery.append("and l.estado not in (:finales) ");
        Query query = getQueryAndSetParameter(strQuery.toString())
                .setParameter("finales", Arrays.asList(HITOS_FINALES_ELIMINADOS));
        return adaptarList(query.getResultList());
    }
	
    private List<PlanillaDTO> getTotalSolicitudes(){
        String queryInicial =  "select l from LcredSolicitud l  " ;
        StringBuilder strQuery = getQueryStringBuilder(queryInicial);
        Query query = getQueryAndSetParameter(strQuery.toString());
        return adaptarList(query.getResultList());
	}

    private List<PlanillaDTO> getTotalActivas(){
        String queryInicial =  "select l from LcredSolicitud l  " ;
        StringBuilder strQuery = getQueryStringBuilder(queryInicial);
        strQuery.append("and l.estado <> :eliminado");
        Query query = getQueryAndSetParameter(strQuery.toString());
        query.setParameter("eliminado", HITO_ELIMINADO);
        return adaptarList(query.getResultList());
    }
	
    private List<PlanillaDTO> getTotalEliminadas() {
        String queryInicial =  "select l from LcredSolicitud l  " ;
        StringBuilder strQuery = getQueryStringBuilder(queryInicial);
        strQuery.append("and l.estado = :eliminado");
        Query query = getQueryAndSetParameter(strQuery.toString());
        query.setParameter("eliminado", HITO_ELIMINADO);
        return adaptarList(query.getResultList());
	}

    private StringBuilder getQueryStringBuilder(String queryInicial) {
        StringBuilder strQuery = new StringBuilder(queryInicial);
        strQuery.append("where l.id.fecSolicitud between :fechaInicial and :fechaFinal ");
        if(!filtro.getListaCanalesSeleccionada().isEmpty() ) {
            strQuery.append("and lower(l.canal) in (:canales) "); // Canales)
        }
        if(!filtro.getListaLcredTipoSolicitudsSeleccionados().isEmpty()) {
            strQuery.append("and l.tipTiposol in (:tipos) ");
        }
        if(!filtro.getListaUsuariosegursSeleccionados().isEmpty()) {
            strQuery.append("and (select count(h) from SolicitudHitos h " + // Por usuario
                    "where h.idSolicitud = l.id.numSolicitud and h.codigoEstado = 'E' " + // Por usuario
                    "and lower(h.usuarioActual) in (:usuarios)) > 0 ");
        }
        if(!filtro.getListaNegociosSeleccionados().isEmpty()) {
            strQuery.append("and (select count(s) from SolicitudConceptosNegocioLC s " +  // Por concepto Negocio
                    "where s.solicitud.id.numSolicitud = l.id.numSolicitud " +
                    "and s.conceptosNegocio.id in (:conceptos)) > 0 ");
        }
        if(!filtro.getListaAprobadoresSeleccionados().isEmpty()) {
        	strQuery.append("and (select count(s) from SolicitudUsuarioDerivada s where s.idSolicitud = l.id.numSolicitud "
        			+ "and lower(s.usuario.alias) in (:aprobadores)) > 0 ");
        }
        
        if(!filtro.getListaSucursalsSeleccionados().isEmpty()) {
            strQuery.append("and lower(l.codSucursal) in (:sucursales) ");
        }
        


        return strQuery;
    }

	private Query getQueryAndSetParameter(String queryInicial) {
        Calendar inicio = Calendar.getInstance();
        Calendar fin = Calendar.getInstance();
        inicio.setTime(filtro.getFechaInicial());
        fin.setTime(filtro.getFechaFinal());
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MILLISECOND, 0);
        inicio.set(Calendar.SECOND, 0);
        fin.set(Calendar.MINUTE, 59);
        fin.set(Calendar.HOUR_OF_DAY, 23);
        fin.set(Calendar.MILLISECOND, 99);
        fin.set(Calendar.SECOND, 59);

		Query query = entityManager.createQuery(queryInicial)
                .setParameter("fechaInicial",inicio.getTime())
                .setParameter("fechaFinal", fin.getTime());


        if(!filtro.getListaLcredTipoSolicitudsSeleccionados().isEmpty()) {
            List<String> tipos = new ArrayList<String>();
            for(LcredTipoSolicitud tipo : filtro.getListaLcredTipoSolicitudsSeleccionados()) {
                tipos.addAll(TipoSolicitudCodigoType.getCodigosSolicitudByCodigoTipo(tipo.getCodTipoSolicitud()));
            }
            query.setParameter("tipos", tipos);
        }

        if(!filtro.getListaCanalesSeleccionada().isEmpty() ) {
            List<String> canales = new ArrayList<String>(filtro.getListaCanalesSeleccionada().size());
            for(Canal canal : filtro.getListaCanalesSeleccionada()) {
                canales.add(canal.getDescripcion().toLowerCase());
            }
            query.setParameter("canales", canales);
        }
        if(!filtro.getListaUsuariosegursSeleccionados().isEmpty()) {
            List<String> usuarios = new ArrayList<String>();
            for(Usuariosegur u: filtro.getListaUsuariosegursSeleccionados()) {
                usuarios.add(u.getAlias().toLowerCase());
            }
            query.setParameter("usuarios", usuarios);
        }
        
        if(!filtro.getListaAprobadoresSeleccionados().isEmpty()) {
            List<String> usuarios = new ArrayList<String>();
            for(Usuariosegur u: filtro.getListaAprobadoresSeleccionados()) {
                usuarios.add(u.getAlias().toLowerCase());
            }
            query.setParameter("aprobadores", usuarios);
        }
        if(!filtro.getListaNegociosSeleccionados().isEmpty()) {
            List<Long> idNegocios = new ArrayList<Long>();
            for(ConceptosNegocio c : filtro.getListaNegociosSeleccionados()) {
                idNegocios.add(c.getSystemId());
            }
            query.setParameter("conceptos", idNegocios);
        }
        if(!filtro.getListaSucursalsSeleccionados().isEmpty()) {
            List<String> sucursales = new ArrayList<String>();
            for(Sucursal s: filtro.getListaSucursalsSeleccionados()) {
                sucursales.add(s.getCodigo().toLowerCase());
            }
            query.setParameter("sucursales", sucursales);
        }
        return query;
	}
	
	private String getAprobadores(Long numSolicitud) {
        StringBuilder str = new StringBuilder("");
        List<String> aprobadores = entityManager.createQuery(
                "select s.usuario.alias from SolicitudUsuarioDerivada s where s.idSolicitud = :idSolicitud")
                .setParameter("idSolicitud", numSolicitud)
                .getResultList();
        if(aprobadores != null && !aprobadores.isEmpty()) {
            for(String s : aprobadores) {
                str.append(s.toUpperCase());
                str.append(", ");
            }
            int indexOf = str.lastIndexOf(",");
            if(indexOf != -1) {
                str.deleteCharAt(indexOf);
            }
        }
		return str.toString();
	}
	
	private String getConceptos(Long numSolicitud) {
        StringBuilder str = new StringBuilder("");
        List<String> conceptos = entityManager.createQuery(
                "select s.conceptosNegocio.descripcion from SolicitudConceptosNegocioLC s where s.solicitud.id.numSolicitud = :idSolicitud order by s.conceptosNegocio.descripcion asc")
                .setParameter("idSolicitud", numSolicitud)
                .getResultList();
        if(conceptos != null && !conceptos.isEmpty()) {
            for(String s : conceptos) {
                str.append(s.toUpperCase().trim());
                str.append(", ");
            }
            int indexOf = str.lastIndexOf(",");
            if(indexOf != -1) {
                str.deleteCharAt(indexOf);
            }
        }
        return str.toString();
	}



    private List<PlanillaHitosDTO> getHitosByID(Long idSolicitud) {
        List<SolicitudHitos> hitos = getHitosBySolicitudId(idSolicitud);
        List<PlanillaHitosDTO> planillaHitosDTOs = new ArrayList<PlanillaHitosDTO>(hitos.size());
        for(SolicitudHitos h: hitos) {
        	log.debug(h.getFechaHora());
            PlanillaHitosDTO p = adaptar(h);
            planillaHitosDTOs.add(p);
        }
        return planillaHitosDTOs;

    }
	
	private PlanillaDTO adaptar(LcredSolicitud solicitud) {
		PlanillaDTO dto = new PlanillaDTO();
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		dto.setNumSolicitud(solicitud.getId().getNumSolicitud());
		dto.setIdSolictud(solicitud.getId().getNumSolicitud().toString());
		dto.setDescripcionEstado("Total");
		Date fechaEmision = solicitud.getId().getFecSolicitud();
        fechaEmision = calculateDate(fechaEmision);
        dto.setNumSolicitud(solicitud.getId().getNumSolicitud());
        dto.setFechaEmisor(fechaEmision);
		dto.setFechaEmision(sdf.format(fechaEmision));
		dto.setTipoSolicitud(solicitud.getTipTiposol());
        dto.setTipoSolicitudCodigoType(TipoSolicitudCodigoType.getTipoSolicitudByCodigoTipo(solicitud.getTipTiposol()));
		dto.setDespTipoSolictud(solicitud.getDesTiposol());
		dto.setHoraEmision(solicitud.getCodEmisor());
        dto.setRazonSocial(solicitud.getNomCliente());
        dto.setMonto(solicitud.getMonto().toBigInteger().toString());
        dto.setRut(solicitud.getRutCliente());
        dto.setAprobadores(getAprobadores(dto.getNumSolicitud()));
        dto.setConceptosInvolucrados(getConceptos(dto.getNumSolicitud()));
        dto.setTiempoHitoInicial(tiempoSolicitudToMS(solicitud.getHoraMinutosSegundos()));
		return dto;
	}

    private Long tiempoSolicitudToMS(String tiempo) {
        String[] tiempos = tiempo.split(":");
        long resultado = 0l;
        if(tiempos.length == 3) {
            Long hour = tiempos[0] != null ? Long.parseLong(tiempos[0]): 0l;
            Long minutes = tiempos[1] != null ? Long.parseLong(tiempos[1]) : 0l;
            Long seconds = tiempos[2] != null ? Long.parseLong(tiempos[2]) : 0l;
            resultado = TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds); 
		}
        return resultado;

    }

    private PlanillaHitosDTO adaptar(SolicitudHitos hito) {
        PlanillaHitosDTO dto = new PlanillaHitosDTO();
        dto.setDescripcionEstado(hito.getDescripcionEstado());
        dto.setEstado(hito.getCodigoEstado());
        DateFormat hourSDF = new SimpleDateFormat("HH:mm");
        DateFormat dateSDF = new SimpleDateFormat("dd-MM-yyyy");
        dto.setFechaRealHito(hito.getFechaHora());
        dto.setFechaRealAsString(sdf2.format(hito.getFechaHora()));
        Date fecha = calculateDate(hito.getFechaHora());
        dto.setFechaModificadaAsString(sdf2.format(fecha));
        dto.setFechaHito(fecha);
        dto.setFecha(dateSDF.format(fecha));
        dto.setHora(hourSDF.format(fecha));
        if(hito.getUsuarioActual() != null && !hito.getUsuarioActual().trim().isEmpty()) {
            dto.setUsuarioUsername(hito.getUsuarioActual());
        } else {
            dto.setUsuarioUsername(hito.getEmisor());
        }
        Usuariosegur user = getUsuarioUsuarioSegurForUsername(dto.getUsuarioUsername());
        if(user != null) {
        	dto.setUsuarioNombre(user.getNombre());
        }else {
        	dto.setUsuarioNombre(dto.getUsuarioUsername());
        }
        dto.setIdSystem(hito.getSystemId());
        dto.setTiempoHito(0l);
        return dto;
    }
	
	private void setCalendars(Jornada jornada, Calendar cal) {
        inicio = new GregorianCalendar();
        fin = new GregorianCalendar();
        fin.setTime(jornada.getHoraFinal());
        inicio.setTime(jornada.getHoraInicial());
        inicio.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));
        inicio.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        inicio.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        fin.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));
        fin.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        fin.set(Calendar.YEAR, cal.get(Calendar.YEAR));

	}

    private Date calculateDate(Date fechaComparada) {
        Calendar fechaAComparar = new GregorianCalendar();
        fechaAComparar.setTime(fechaComparada);
        setCalendars(jornada, fechaAComparar);
        if(fechaAComparar.before(inicio)) {
            fechaAComparar = inicio;
        }else if(fechaAComparar.after(fin)) {
            fechaAComparar = moveOneDay(fechaAComparar);
        }
        /** Datos agregados 21/10/2014 **/
        else if(isHolidayOrWeekend(fechaAComparar)) {
        	fechaAComparar = moveOneDay(fechaAComparar);
        }
        return fechaAComparar.getTime();
    }

    private Long diffTime(Date fInicial, Date fFinal) {
        Long resultado = 0l;
        Calendar calInicial = Calendar.getInstance();
        Calendar calFinal = Calendar.getInstance();
        calInicial.setTime(fInicial);
        calFinal.setTime(fFinal);
        if(calInicial.get(Calendar.DAY_OF_YEAR) == calFinal.get(Calendar.DAY_OF_YEAR)) {
            resultado = calFinal.getTimeInMillis() - calInicial.getTimeInMillis();
        }else {
            setCalendars(jornada,calInicial);
            resultado = fin.getTimeInMillis() - calInicial.getTimeInMillis();
            Calendar aux = calInicial;
            while(true) {
                aux.add(Calendar.DATE, 1);
                if(isHolidayOrWeekend(aux)) {
                    continue;
                }else if(aux.get(Calendar.DAY_OF_YEAR) == calFinal.get(Calendar.DAY_OF_YEAR)) {
                    setCalendars(jornada, aux);
                    resultado += calFinal.getTimeInMillis() - inicio.getTimeInMillis();
                    break;
                } else {
                    setCalendars(jornada,aux);
                    resultado += fin.getTimeInMillis() - inicio.getTimeInMillis();
                }
            }
        }
        return resultado;
    }

    private Calendar moveOneDay(Calendar cal) {
        Calendar nextDay = Calendar.getInstance();
        nextDay.setTime(jornada.getHoraInicial());
        nextDay.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));
        nextDay.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        nextDay.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        nextDay.add(Calendar.DATE, 1);
        while(isHolidayOrWeekend(nextDay)) {
            nextDay.add(Calendar.DATE,1);
        }
        return nextDay;
    }

    private boolean isHolidayOrWeekend(Calendar cal) {
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        } else {
            Calendar inicio = Calendar.getInstance();
            Calendar fin = Calendar.getInstance();
            inicio.setTimeInMillis(cal.getTimeInMillis());
            fin.setTimeInMillis(cal.getTimeInMillis());
            inicio.set(Calendar.MINUTE, 0);
            inicio.set(Calendar.HOUR_OF_DAY, 0);
            inicio.set(Calendar.MILLISECOND, 0);
            inicio.set(Calendar.SECOND, 0);
            fin.set(Calendar.MINUTE, 59);
            fin.set(Calendar.HOUR_OF_DAY, 23);
            fin.set(Calendar.MILLISECOND, 99);
            fin.set(Calendar.SECOND, 59);
            Long total = (Long)entityManager.createQuery("select count(f) from Feriado f where fecha between :inicio and :fin")
                    .setParameter("inicio",inicio.getTime())
                    .setParameter("fin", fin.getTime())
                    .getSingleResult();
            if(total > 0)
                return true;
        }
        return false;
    }

	private List<SolicitudHitos> getHitosBySolicitudId(Long idSolicitud){
        List<SolicitudHitos> hitos = entityManager.createQuery("from SolicitudHitos " +
                "where idSolicitud = :idSolicitud and codigoEstado not in (:excluded)  order by systemId asc")
                .setParameter("idSolicitud", idSolicitud)
                .setParameter("excluded", Arrays.asList(EXCLUDED_TYPES))
                .getResultList();
        return hitos;
	}
	
	public MatrizNivelServicio getUltimaMatrizByTipoSolicitud(String codigoTipoSolicitud) {
	    List<MatrizNivelServicio> list = entityManager.createQuery("from MatrizNivelServicio m where m.tipoSolicitud.codTipoSolicitud = :codigoTipoSolicitud order by m.systemId desc")
                .setParameter("codigoTipoSolicitud", codigoTipoSolicitud)
                .setMaxResults(1)
                .getResultList();
	    if(list != null && !list.isEmpty())
	    	return list.get(0);
	    else
	    	return null;
	    			
	}

    public Usuariosegur getUsuarioUsuarioSegurForUsername(String user){
        Usuariosegur objeto = null;
        List<Usuariosegur> lista = null;
        
        if(!usuariosEncontrados.isEmpty()) {
        	Usuariosegur aux = new Usuariosegur();
            aux.setAlias(user);
            int indexOf = usuariosEncontrados.indexOf(aux);
            if(indexOf != -1) {
            	return usuariosEncontrados.get(indexOf);
            }
        }
        
        try{
            lista = (List<Usuariosegur>)entityManager
                    .createQuery("Select uc from Usuariosegur uc where uc.alias=:user order by idPersonal desc ")
                    .setParameter("user", user).getResultList();
            if(lista != null && !lista.isEmpty()){
                objeto = lista.get(0);
                usuariosEncontrados.add(objeto);
            }
        }catch (Exception e) {
        }
        return objeto;
    }
    
    public void generaDataPlanillaParaExcel(PlanillaDTO planillaDTO) {
        String estado = planillaDTO.getTipoHito();
        String tiempo = planillaDTO.getTotalTimeAsHHMMSS();
        if(Arrays.asList(HITOS_INICIAL).contains(estado)) {
            planillaDTO.setHitoEjecutivo(tiempo);
        }else if(Arrays.asList(HITOS_ANALISTA).contains(estado)) {
            planillaDTO.setHitoAnalista(tiempo);
        }else if(Arrays.asList(HITOS_EMISOR).contains(estado)) {
            planillaDTO.setHitoEmisor(tiempo);
        }else if(Arrays.asList(HITOS_APROBADORES).contains(estado)) {
            planillaDTO.setHitoAprobadores(tiempo);
        }
        if(Arrays.asList(HITOS_APRUEBA_RECHAZA).contains(estado)){
        	planillaDTO.setHitoAprobadores(tiempo);
        	planillaDTO.setTiempoTotal("00:00:00");
        }else {
        	planillaDTO.setTiempoTotal(tiempo);
        }
    }
}