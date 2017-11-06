package org.domain.sck.session.service.intranetsap;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.dto.CPagoDTO;
import org.domain.sck.dto.ClsRiesgoDTO;
import org.domain.sck.dto.ConceptoUsuarioDTO;
import org.domain.sck.dto.NegocioUsuarioDTO;
import org.domain.sck.dto.SucursalesUsuarioDTO;
import org.domain.sck.dto.ZonaUsuarioDTO;
import org.domain.sck.entity.ConceptosNegocio;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;


@Name("intranetSapService")
@AutoCreate
public class IntranetSapService {

	@Logger Log log;
	@In(value="#{emIntranetSAPSck}")
	EntityManager emIntranetSAPSck;
	
	@In ScoringService scoringService;


	/*
	 * Parametros de entrada: clasificacion de opb. 
	 * Return : retorna la descricion del tipo de cliente.
	 * */		

	public Object descripcionTipoClienteCav(String clasifOPB){
		try{
			return (Object)emIntranetSAPSck
					.createNativeQuery("select cast(descripcion as varchar) as des from Tipo_cliente_cav  where codigo=:codigo")
					.setParameter("codigo", clasifOPB)
					.getSingleResult();
								
			
		}catch (Exception e) {
			log.error("Error, al sacar la descripcion del tipo de cliente cav #0 ", e.getMessage());
		}
		return null;
	}
	/*
	 * Parametros de entrada: codigo de riesgo. 
	 * Return : retorna la descripcion de la clasificacion de riesgo.
	 * */		

	public Object sacarDescripClasificacionRiesgo(String cod){
		try{
			return (Object)emIntranetSAPSck
					.createNativeQuery("select cast(descripcion as varchar) as des from cl_riesgo where cl_riesgo=:codigo")
					.setParameter("codigo", cod)
					.getSingleResult();
								
			
		}catch (Exception e) {
			log.error("Error, al sacar la descripcion del tipo de cliente cav #0 ", e.getMessage());
		}
		return null;
	}

	/*
	 * Parametros de entrada: codigo y jerarquia. 
	 * Return : objeto de concepto de negocio o un nulo.
	 * */		
	 @SuppressWarnings("unchecked")
	public ConceptosNegocio buscarNegocio(String codigo, String jerarquia) {
	         log.error("codigo #0 - jerarquia #1", codigo ,jerarquia);
	         ConceptosNegocio datosNegocio = scoringService.obtenerConceptoNegocioLocal(jerarquia);
	         if(datosNegocio == null){
		         List<ConceptosNegocio> negocios = scoringService.obtenerListaConceptoLocalNegocioPorCodigo(codigo);
		         if(negocios.isEmpty()) {
		               log.debug("negocio vacio");
		               List<Object[]> results = emIntranetSAPSck.createNativeQuery(
		                          " select cast([Negocio] as varchar) as negocio," +
		                          "    cast([Des_Negocio] as varchar) as desNegocio, " +
		                          "    cast([Concepto] as varchar) as concepto, " +
		                          "    cast([Descripcion] as varchar) as desconcepto," +
		                          "    [Orden]," +
		                          "    [Orden_Div]," +
		                          "    [EstaSolo]," +
		                          "    cast([Jerarquia] as varchar) as desjerarquia  " +
		                          " FROM [INTRANET_SAP].[dbo].[ConceptosNegocio] where jerarquia=:jerarquia")
		                          .setParameter("jerarquia", jerarquia)
		                          .getResultList();
		               if(!results.isEmpty()) {
		            	     datosNegocio = new ConceptosNegocio();
		                     Object[] result = results.get(0);
		                     datosNegocio.setNegocio((String) result[0]);
		                     datosNegocio.setDesNegocio((String) result[1]);
		                     datosNegocio.setConcepto((String) result[2]);
		                     datosNegocio.setDescripcion((String) result[3]);
		                     datosNegocio.setOrden((Integer) result[4]);
		                     datosNegocio.setOrdenDiv((Integer) result[5]);
		                     datosNegocio.setEstaSolo((Integer) result[6]);
		                     datosNegocio.setJerarquia((String) result[7]);
		               }
		               if(datosNegocio != null){
		            	   scoringService.persistConceptoNegocio(datosNegocio);   
		               }
		               
		         } else
		               datosNegocio = negocios.get(0);
		         
	         }
	         return datosNegocio;
	   }
	 
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

	public Object sacarDescripCondicionPago(String cod){
		try{
			return (Object)emIntranetSAPSck
					.createNativeQuery("select cast([descripcion] as varchar) as des from [INTRANET_SAP].[dbo].[CPAGO] where cpago=:codigo")
					.setParameter("codigo", cod)
					.getSingleResult();
								
			
		}catch (Exception e) {
			log.error("Error, al sacar la descripcion del tipo de cliente cav #0 ", e.getMessage());
		}
		return null;
	}
	
	/*
	 * Parametros de entrada: . 
	 * Return : Lista de Zonas.
	 * */		
	@SuppressWarnings("unchecked")
	public List<ZonaUsuarioDTO> sacarListaZonas(boolean ticket){
		List<ZonaUsuarioDTO> listaZonas = null;
		ZonaUsuarioDTO zona = null;
		try{
			List<Object[]> lista = (List<Object[]>)emIntranetSAPSck
					.createNativeQuery("select cast([Zona] as varchar) as codigo, " +
							" cast([Descripcion] as varchar) as des from [INTRANET_SAP].[dbo].[ZONAS] ")
					.getResultList();
			
			if(lista != null){
				listaZonas = new ArrayList<ZonaUsuarioDTO>(0);
				for(Object[] objeto : lista){
					if(objeto != null){
						zona = new ZonaUsuarioDTO();
						if(objeto[0] != null ){
							zona.setCodigo(objeto[0].toString());
						}
						if(objeto[1] != null ){
							zona.setDescripcion(objeto[1].toString());
						}
						
						zona.setStatus(ticket);
						listaZonas.add(zona);
					}
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al sacar la descripcion del tipo de cliente cav #0 ", e.getMessage());
		}
		return listaZonas;
	}
	
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
	
	
	
}
