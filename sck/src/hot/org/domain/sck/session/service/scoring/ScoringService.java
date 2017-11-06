package org.domain.sck.session.service.scoring;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.domain.sck.dto.CabeceraCotPedDTO;
import org.domain.sck.dto.ConceptoDTO;
import org.domain.sck.dto.CotPedDTO;
import org.domain.sck.dto.DestinatarioDTO;
import org.domain.sck.dto.DetalleCotPedDTO;
import org.domain.sck.dto.HitosDTO;
import org.domain.sck.dto.IndicadoresDTO;
import org.domain.sck.dto.MatrizResposabilidadDTO;
import org.domain.sck.dto.SolicitudEstadoDTO;
import org.domain.sck.dto.UsuarioCorreoDTO;
import org.domain.sck.dto.UsuarioIngresoDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.entity.ArchivoAdjunto;
import org.domain.sck.entity.Canal;
import org.domain.sck.entity.CanalUsuarioCargo;
import org.domain.sck.entity.Comuna;
import org.domain.sck.entity.ConceptosNegocio;
import org.domain.sck.entity.CondicionTipoSolicitudRango;
import org.domain.sck.entity.CotizacionPedido;
import org.domain.sck.entity.CotizacionPedidoCabecera;
import org.domain.sck.entity.CotizacionPedidoConcepto;
import org.domain.sck.entity.CotizacionPedidoNegocio;
import org.domain.sck.entity.DetalleCp;
import org.domain.sck.entity.DeudaActual;
import org.domain.sck.entity.Estados;
import org.domain.sck.entity.FormaPagoDetalle;
import org.domain.sck.entity.FormaPagoEncabezado;
import org.domain.sck.entity.Jornada;
import org.domain.sck.entity.LcredArchivoAdjuntos;
import org.domain.sck.entity.LcredCargos;
import org.domain.sck.entity.LcredEstado;
import org.domain.sck.entity.LcredMotivoRechazo;
import org.domain.sck.entity.LcredPerfiles;
import org.domain.sck.entity.LcredSolicitud;
import org.domain.sck.entity.LcredSolicitudBloqueos;
import org.domain.sck.entity.LcredSolicitudCondiciones;
import org.domain.sck.entity.LcredSolicitudDm;
import org.domain.sck.entity.LcredSolicitudDmId;
import org.domain.sck.entity.LcredSolicitudId;
import org.domain.sck.entity.LcredSolicitudLcredito;
import org.domain.sck.entity.LcredSolicitudObservaciones;
import org.domain.sck.entity.LcredSolicitudOtras;
import org.domain.sck.entity.LcredSolicitudProrroga;
import org.domain.sck.entity.LcredSolicitudVtapuntual;
import org.domain.sck.entity.LcredTipoSolicitud;
import org.domain.sck.entity.LcredUsuarioNivelEnc;
import org.domain.sck.entity.LcredUsuarioNivelEncId;
import org.domain.sck.entity.LogsCorreos;
import org.domain.sck.entity.MatrizDetalleResponsabilidad;
import org.domain.sck.entity.MatrizUsuarioResponsable;
import org.domain.sck.entity.Medicion;
import org.domain.sck.entity.MedicionCanalRango;
import org.domain.sck.entity.PerfilFuncionCanal;
import org.domain.sck.entity.Provincia;
import org.domain.sck.entity.Region;
import org.domain.sck.entity.Sociedad;
import org.domain.sck.entity.SolicitudAccion;
import org.domain.sck.entity.SolicitudAprobaRechaza;
import org.domain.sck.entity.SolicitudConceptosNegocioLC;
import org.domain.sck.entity.SolicitudDiaReparo;
import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudLogs;
import org.domain.sck.entity.SolicitudMotivoRechazo;
import org.domain.sck.entity.SolicitudTipoFormaPago;
import org.domain.sck.entity.SolicitudUsuarioCorreo;
import org.domain.sck.entity.SolicitudUsuarioDerivada;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.UfSolicitudTipo;
import org.domain.sck.entity.Ufs;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.Usuarios;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.entity.ZonaSucursalNegocioConcepto;
import org.domain.sck.entity.enums.ArchivoAdjuntoType;
import org.domain.sck.entity.enums.FuncionesType;
import org.domain.sck.entity.enums.TiempoMontoType;
import org.domain.sck.entity.enums.TipoCuentasKupferType;
import org.domain.sck.session.service.segur.SegurService;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


@Name("scoringService")
@AutoCreate
public class ScoringService {

	@Logger Log log;
	
	@In(value="#{entityManager}")
	EntityManager entityManager;

	@In SegurService segurService;
	
	/*
	 * Parametros de entrada: user del usuario . 
	 * Return : objeto UsuarioSegurDTO.
	 * */		
	public UsuarioSegurDTO sacarDatosSessionUsuario(String user){
    	UsuarioSegurDTO datos = null;
    	Object[]  array = null;
    	int ver = 0;
    	try{
    		try{
		  	         array = (Object[])entityManager.createNativeQuery(" SELECT cast([Alias] as varchar) as alias," +
		  														  " cast([Correo] as varchar) as correo," +
		  														  " cast([centro] as varchar) as centro, " +
		  														  " cast([nombre] as varchar) as nombre ," +
		  														  " id_personal as codigoPersonal , " +
		  														  " obligatorio as colocarAutomatico ," +
		  														  " eliminado as eliminado "+
		  														  " FROM [SCORING_PRODUCION].[dbo].[USUARIOSEGUR] " +
		  														  " WHERE Alias=:username")
		  			          .setParameter("username", user)
		  			          .setMaxResults(1)
		  			          .getSingleResult();
    		}catch (Exception e) {
    			 ver = 0;
    			 log.error("Error, no existe en la base de datros prodducion #0", e.getMessage());
			}
    		
		  	if(array != null && array.length > 2){
		  		datos = new UsuarioSegurDTO();
		  		datos.setAlias(array[0].toString());
		  		datos.setCorreo(array[1].toString());
		  		datos.setCodigosucursal(array[2].toString());
		  		datos.setNombre(array[3].toString());
		  		datos.setIdPersonal(Long.parseLong(array[4].toString()));
		  		if(array[5] != null){
		  			ver = Integer.parseInt(array[5].toString());
		  			if(ver == 0){
		  				datos.setObligatorio(false);
		  			}else{
		  				datos.setObligatorio(true);
		  			}
		  		}
		  		//log.debug("nombre : #0", datos.getNombre());
		  		if(array[6] != null){
		  			 ver = Integer.parseInt(array[6].toString());
		  			if(ver != 0){
		  				datos = null;
		  			}
		  			
		  		}
		  	}else{
		  		if(ver == 0){
			  		UsuarioSegurDTO usuario = segurService.sacarDatosSessionUsuario(user);
			  		if(usuario != null){
			  			/*se debe ingresar*/
			  			Usuariosegur  usuarioIngresar = new Usuariosegur();
			  			usuarioIngresar.setAlias(usuario.getAlias());
			  			usuarioIngresar.setCargo(usuario.getCargo());
			  			usuarioIngresar.setIdPersonal(usuario.getIdPersonal());
			  			usuarioIngresar.setCorreo(usuario.getCorreo());
			  			usuarioIngresar.setNombre(usuario.getNombre());
			  			usuarioIngresar.setCentro(usuario.getCodigosucursal());
			  			usuarioIngresar.setObligatorio(false);
			  			usuarioIngresar.setEliminado(false);
			  			usuarioIngresar.setAnexo(usuario.getAnexo());
			  			usuarioIngresar.setCelular(usuario.getCelular());
			  			usuarioIngresar.setRut(usuario.getRut());
			  			entityManager.persist(usuarioIngresar);
			  			entityManager.flush();
			  			datos = usuario;
			  		}
		  		}
		  	}
    	}catch (Exception e) {
    		log.error("Error, al sacar los datos del usuario de la base de datos segur #0 ", e.getMessage());
    		log.debug("usuario no encontrado  : #0", user);
		}
    	return datos;
    }
	

	/*
	 * Parametros de entrada: idsucursal. 
	 * Return : obteto Sucursal o null
	 * */		

	public Sucursal obtenerSucursalForCodigo(String codigo){
		Sucursal suc = null;
		try{
			suc = entityManager.find(Sucursal.class, codigo);
		}catch (Exception e) {
			log.error("Error, al sacar la sucursal. #0", e.getMessage());
		}
		return suc;
	}
	/*
	 * Parametros de entrada: codigo de canal. 
	 * Return : obteto canal 
	 * */		

	public Canal obtenerCanalForCodigo(long codigo){
		Canal canal = null;
		try{
			canal = entityManager.find(Canal.class, codigo);
		}catch (Exception e) {
			log.error("Error, al sacar la canal. #0", e.getMessage());
		}
		return canal;
	}
	
	
	/*
	 * Parametros de entrada: codigo tipo de solicitud . 
	 * Return : obteto tipo solicitud  o null
	 * */		

	public LcredTipoSolicitud obtenerObjetoLcredTipoSolicitud(String codigo){
		try{
			log.info("obtenerObjetoLcredTipoSolicitud + "+ codigo);
			return entityManager.find(LcredTipoSolicitud.class, codigo);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al sacar la sucursal. #0", e.getMessage());
		}
		return null;
	}

	/*
	 * Parametros de entrada: codigo medicion. 
	 * Return : Medicion 
	 * */		
	public Medicion obtenerObjetoMedicion(long codigo){
		try{
			return entityManager.find(Medicion.class, codigo);
		}catch (Exception e) {
			log.error("Error, al sacar la medicion. #0", e.getMessage());
		}
		return null;
	}
	
	
	/*
	 * Parametros de entrada: String usuario, String codigo,String canal, String zona, String sucrsal, long monto, String tiposolicitud  . 
	 * Return : lista de objetos de DestinatarioDTO o valor null
	 * */		

	@SuppressWarnings("unchecked")
	public List<DestinatarioDTO> obtenerListaDestinatario(String tipoSolicitud,
														  String canal, 
														  String zona, 
														  String sucrsal, 
														  String negocio, 
														  String concepto, 
														  String inicial){
		List<DestinatarioDTO> listaFinal = null;
		try{
			String query = " EXEC [SCORING_PRODUCION].dbo.pccorp_busca_destinatarios " +
					" @tiposol  ='"+ tipoSolicitud +"',"+
					" @canal ='" + canal +"'," +
					" @zona ='" + zona +"'," +
					" @sucursal ='" + sucrsal +"'," +
					" @negocio = '" + negocio +"' ," +
					" @concepto ='" + concepto +"', " +
					" @inicial = " + inicial;
			
			
			List<Object[]> lista = 	(List<Object[]>)entityManager.createNativeQuery(query).getResultList();
			
			if(lista != null && lista.size() > 0){
				listaFinal = new ArrayList<DestinatarioDTO>(0);
				for(Object[] obj : lista){
					if(obj != null){
						DestinatarioDTO d = new DestinatarioDTO();
						d.setTipoSolicitud(tipoSolicitud);
						if(obj[0] != null){
							d.setUsername(obj[0].toString().trim());
						}
						if(obj[1] != null){
							d.setNombre(obj[1].toString().trim());
						}
						if(obj[2] != null){
							d.setCorreo(obj[2].toString().trim());
						}
						if(obj[3] != null){
							d.setIdPersonal( new Long(obj[3].toString().trim()));
						}
						
						if(d.getIdPersonal() != 1654){
							listaFinal.add(d);
						}
						
					}
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al sacar la sucursal. #0", e.getMessage());
		}
		return listaFinal;
	}

	
	/*
	 * Parametros de entrada: codigo de concepto de negocio . 
	 * Return :litsa de concepto de negocio;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<ConceptosNegocio> obtenerListaConceptoLocalNegocioPorCodigo(String codigo){
		try{
		    return (List<ConceptosNegocio>)entityManager.createQuery("select cn from ConceptosNegocio cn where cn.concepto=:codConcepto")
		            .setParameter("codConcepto", codigo)
		            .getResultList();   
		}catch (Exception e) {
			log.error("Error, al sacar la lista de concepto de Negocio. #0", e.getMessage());
		}
		return null;
	}
	/*
	 * Parametros de entrada: codigo de concepto de negocio . 
	 * Return :objeto de concepto de negocio o nulo.
	 * */		
	public  ConceptosNegocio obtenerConceptoNegocioLocal(String jerarquia){
		try{
			log.debug("jerarquia : #0", jerarquia);
		    return (ConceptosNegocio)entityManager
		    		.createQuery("select cn from ConceptosNegocio cn where cn.jerarquia=:jerarquiaAux ")
		            .setParameter("jerarquiaAux", jerarquia)
		            .getSingleResult();   
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al sacar la lista de concepto de Negocio. #0", e.getMessage());
		}
		return null;
	}

	/*
	 * Parametros de entrada: codigo de concepto de negocio . 
	 * Return :objeto de concepto de negocio o nulo.
	 * */		
	@SuppressWarnings("unchecked")
	public  ConceptosNegocio obtenerConceptoNegocioLocalForCodigoNegocio(String codigoNegocio){
		List<ConceptosNegocio> lista = null;
		try{
			lista = (List<ConceptosNegocio>)entityManager
		    		.createQuery("select cn from ConceptosNegocio cn where cn.negocio=:codigoNegocio order by cn.orden")
		            .setParameter("codigoNegocio", codigoNegocio)
		            .setMaxResults(1)
		            .getResultList();
			if(lista != null && lista.size() > 0){
				return (lista.get(0));
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al sacar el objeto de concepto de Negocio por codigo. #0", e.getMessage());
		}
		return null;
	}	

	/*
	 * Parametros de entrada: vacio en la entrada. 
	 * Return :lista de objetos de negocio ordenado por el orden previo de la tabla..
	 * */		
	@SuppressWarnings("unchecked")
	public  List<ConceptosNegocio> obtenerListaConceptoNegocio(){
		List<ConceptosNegocio> lista = null;
		try{
			lista = (List<ConceptosNegocio>)entityManager
		    		.createQuery("select cn from ConceptosNegocio cn order by cn.orden")
		            .getResultList();
			if(lista != null && lista.size() > 0){
				return lista;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al sacar el objeto de concepto de Negocio por codigo. #0", e.getMessage());
		}
		return null;
	}	
	
	
	/*
	 * Parametros de entrada: codigo de concepto de negocio . 
	 * Return :objeto de concepto de negocio o nulo.
	 * */		
	public  ConceptosNegocio obtenerConceptoNegocioLocalForNegocioAndConcepto(String codigoNegocio, String concepto){
		
		try{
			return (ConceptosNegocio)entityManager
		    		.createQuery("select cn from ConceptosNegocio cn where cn.negocio=:codigoNegocio and cn.concepto=:conceptoAux")
		            .setParameter("codigoNegocio", codigoNegocio)
		            .setParameter("conceptoAux", concepto)
		            .getSingleResult();
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al sacar el objeto de concepto de Negocio por codigo de negocio y concepto. #0", e.getMessage());
		}
		return null;
	}
	
	/*
	 * Parametros de entrada: objeto de ConceptoNegocio . 
	 * Return :verdadero o falso segun;
	 * */		
	public  boolean persistConceptoNegocio(ConceptosNegocio datosNegocio){
		try{
            entityManager.persist(datosNegocio);
            entityManager.flush(); 
            return true;
		}catch (Exception e) {
			log.error("Error, al persister el objeto de concepto de negocio. #0", e.getMessage());
		}
		return false;
	}
	
	/*
	 * Parametros de entrada: user del usuario . 
	 * Return : retorna un objeto de tipo LcredUsuarioNivelEnc;
	 * */		
	public  LcredUsuarioNivelEnc obtenerLcredUsuarioNivelEnc(String user){
		LcredUsuarioNivelEnc lcred = null;
		try{
			 lcred = (LcredUsuarioNivelEnc)entityManager.createQuery("select lune from LcredUsuarioNivelEnc lune " +
					   " where lune.id.idUsuario=:usuarioIdAux")
                       .setParameter("usuarioIdAux", user)
                       .getSingleResult();					   
           
		}catch (Exception e) {
			log.error("Error, al persister el objeto de concepto  de negocio. #0", e.getMessage());
		}
		return lcred;
	}	

	
	/*
	 * Parametros de entrada: user del usuario . 
	 * Return : retorna un objeto de tipo LcredUsuarioNivelEnc;
	 * */		
	public  LcredUsuarioNivelEnc obtenerLcredUsuarioNivelEncNativo(String user){
		LcredUsuarioNivelEnc lcred = null;
		try{
			 Object[]  obejto  = (Object[])entityManager
					 .createNativeQuery(" SELECT  [id_usuario] " +
					   " ,[nombre_usuario] " +
					   " ,[correo] " +
					   " ,[cargo] " +
					   " ,[administrador] " +
					   " ,[envio_autom_otras_solc]" +
					   " FROM [SCORING_PRODUCION].[dbo].[lcred_usuario_nivel_enc]" +
					   "  where [id_usuario]=:usuarioIdAux")
                       .setParameter("usuarioIdAux", user)
                       .getSingleResult();	
			 if(obejto!= null){
				 lcred = new LcredUsuarioNivelEnc();
				 LcredUsuarioNivelEncId id = new LcredUsuarioNivelEncId();
				 if(obejto[0] != null){
					 id.setIdUsuario(obejto[0].toString());
				 }
				 if(obejto[1] != null){
					 id.setNombreUsuario(obejto[1].toString());
				 }
				 if(obejto[2] != null){
					 id.setCorreo(obejto[2].toString());
				 }
				 if(obejto[3] != null){
					 id.setCargo(obejto[3].toString());
				 }	
				 if(obejto[4] != null){
					 id.setAdministrador(obejto[4].toString().charAt(0));
				 }else{
					 id.setAdministrador(null);
				 }
				 if(obejto[5] != null){
					 id.setEnvioAutomOtrasSolc(obejto[5].toString().charAt(0));
				 }else{
					 id.setEnvioAutomOtrasSolc(null); 
				 }
				 
				 lcred.setId(id);
				 
			 }
           

			 
		}catch (Exception e) {
			log.error("Error, al persister el objeto de concepto  de negocio. #0", e.getMessage());
		}
		return lcred;
	}
	
	/*
	 * Parametros de entrada: persist de solicitud. 
	 * Return : retorna el objeto LcredSolicitud ;
	 * */		
	public  LcredSolicitud  persistSolicitud(LcredSolicitud solicitud){
		LcredSolicitudId id = new LcredSolicitudId();
		BigDecimal idMas = getUltimaSolicitudMasUno();
		id.setNumSolicitud(idMas.longValue());
		id.setFecSolicitud(new Date());
		try{
			solicitud.setId(id);
			entityManager.persist(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al persister el objeto de solict. #0", e.getMessage());
			solicitud = null;
		}
		return solicitud;
	}	
	
	/*
	 * Parametros de entrada: merge de solicitud. 
	 * Return : retorna verdadero o falso ;
	 * */		
	public  boolean  mergerSolicitud(LcredSolicitud solicitud){
		boolean exito = false;
		try{
			entityManager.merge(solicitud);
			entityManager.flush();
			entityManager.refresh(solicitud);
			exito = true;
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al persister el objeto de solict. #0", e.getMessage());
			exito = false;
		}
		return exito;
	}	
	
	
	/*
	 * Parametros de entrada: encabezado. 
	 * Return : boolean;
	 * */		

	@SuppressWarnings("unchecked")
	public  FormaPagoDetalle  sacarUltimaFormaDePago(FormaPagoEncabezado encabezado){
		FormaPagoDetalle detalle = null;
		long variable = 0;
		try{
			
			List<FormaPagoDetalle> listaDetalle = (List<FormaPagoDetalle>)
					entityManager.createQuery("select fpd " +
					   "from FormaPagoDetalle fpd where fpd.encabezado.idEncabezado=:idEncabezadoAux")
					   .setParameter("idEncabezadoAux", encabezado.getIdEncabezado())
					   .getResultList();
			if(listaDetalle != null){
				for(FormaPagoDetalle fpd : listaDetalle){
					if(variable == 0){
						detalle = fpd;
						variable = fpd.getFormaPago().getDias();
					}else if(fpd.getFormaPago().getDias() > variable){
						detalle = fpd;
						variable = fpd.getFormaPago().getDias();						
					}
				}
			}
		}catch (Exception e) {
			log.error("Error, al persister el objeto de solict. #0", e.getMessage());
			detalle = null;
		}
		return detalle;
	}	
	
	/*
	 * Parametros de entrada: persist de solicitud. 
	 * Return : retorna el objeto LcredSolicitud ;
	 * */		
	public  boolean  persistSolicitudVentaPuntual(LcredSolicitudVtapuntual solicitud){
		try{
			entityManager.persist(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de venta puntual. #0", e.getMessage());
			return false;
		}
		return true;
	}	

	/*
	 * Parametros de entrada: vacia. 
	 * Return : numero ultima solictud + 1 ;
	 * */		
	@SuppressWarnings("unchecked")
	public  BigDecimal  getUltimaSolicitudMasUno(){
		List<BigDecimal> id = null;
		BigDecimal idAux = null;
		try{
			 id = (List<BigDecimal>)entityManager.createNativeQuery("SELECT TOP 1 [num_solicitud] FROM [SCORING_PRODUCION].[dbo].[lcred_solicitud] order by num_solicitud desc").getResultList();
			 if(id != null){
				 BigDecimal valor = id.get(0);
				 if(valor != null){
					 long numero = valor.longValue();
					 numero++;
					 idAux = new BigDecimal(numero);
				 }
			 }
		}catch (Exception e) {
			log.error("Error, al persister el objeto de venta puntual. #0", e.getMessage());
			return null;
		}
		return idAux;
	}
	
	/*
	 * Parametros de entrada: vacia. 
	 * Return : numero ultima usuario + 1 ;
	 * */		
	@SuppressWarnings("unchecked")
	public  Long  getUltimaUsuarioMasUno(){
		List<BigDecimal> id = null;
		Long idAux = null;
		try{
			 id = (List<BigDecimal>)entityManager.createNativeQuery("SELECT TOP 1 [id_personal] + 1  FROM [SCORING_PRODUCION].[dbo].[USUARIOSEGUR]"
			 														+ "  where id_personal < 10000  order by id_personal desc").getResultList();
			 if(id != null){
				 BigDecimal valor = id.get(0);
				 if(valor != null){
					 idAux = valor.longValue();
				 }
			 }
		}catch (Exception e) {
			log.error("Error, al sacar el numero de usuario mas uno. #0", e.getMessage());
			return null;
		}
		return idAux;
	}
	
	/*
	 * Parametros de entrada: codigo del cliente y fecha 
	 * Return : verdadero si existe registro y falso si no existe registro;
	 * */	
	public  boolean  getVerificarHistorialCuentaCorriente(String codigo,  Date date){
		boolean existe = true;
		try{
			Long cantidad = (Long)entityManager.
					 		createQuery("SELECT count(da.systemId) FROM DeudaActual da "+
		                                "WHERE da.rut=:rutAux  and da.fechaIngreso=:fechaAux ")
		                                .setParameter("rutAux",codigo)
		                                .setParameter("fechaAux", date)
		                                .getSingleResult();
			if(cantidad != null){
				if(cantidad.intValue() == 0){
					existe = false;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al persister el objeto de venta puntual. #0", e.getMessage());
			return true;
		}
		return existe;
	}	
	/*
	 * Parametros de entrada: rut del cliente  
	 * Return : lista de fechas o null;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<String>  getFechasCuentaCorriente(String rut){
		List<Date> listaFechas = null;
		List<String> listaFechasString = new ArrayList<String>();
		try{
			 listaFechas = (List<Date>)entityManager.
					 		createQuery("SELECT distinct(da.fechaIngreso) FROM DeudaActual da "+
		                                "WHERE da.rut=:rutAux ")
		                                .setParameter("rutAux",rut)
		                                .getResultList();
			if(listaFechas != null){
				for(Date fecha : listaFechas){
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					String fechaAux1 = format.format(fecha);
					listaFechasString.add(fechaAux1.toString());
				}
				log.debug("cantidad de fechas en la lista #0", listaFechas.size());
			}
		}catch (Exception e) {
			
			log.error("Error, al sacar la lista de fechas del historial de cuenta corriente. #0", e.getMessage());
			return null;
		}
		return listaFechasString;
	}
	
	/*
	 * Parametros de entrada: codigo del cliente y fecha 
	 * Return : verdadero si existe registro y falso si no existe registro;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<DeudaActual>  getConsultarHistorialCuentaCorriente(String codigo,  Date date){
		List<DeudaActual> listaDeudas = null; 
		try{
			listaDeudas = (List<DeudaActual>)entityManager.
					 		createQuery("SELECT da FROM DeudaActual da "+
		                                "WHERE da.rut=:codigoAux  and da.fechaIngreso=:fechaAux ")
		                                .setParameter("codigoAux",codigo)
		                                .setParameter("fechaAux", date)
		                                .getResultList();

		}catch (Exception e) {
			log.error("Error, al persister el objeto de getConsultarHistorialCuentaCorriente. #0", e.getMessage());
			return null;
		}
		return listaDeudas;
	}
	
	/*
	 * Parametros de entrada: lista de cotizaciones o pedido con calculo de grilla y solictud ya ingresada
	 * Return : verdadero si existe registro y falso si no existe registro;
	 * */	
	public  boolean  insertCotizacionPedidoCalculo(List<CotPedDTO> lista,  LcredSolicitud solicitud){
		
		try{
			if(lista != null && solicitud != null){
				log.debug(" verificacando datos : "+ lista.size());
				log.debug(" verificacando solictud  : "+ solicitud.getId().getNumSolicitud());
				for(CotPedDTO encabezado : lista){
					CotizacionPedidoCabecera cabecera = new CotizacionPedidoCabecera();
					cabecera.setSolicitud(solicitud);
					cabecera.setNeto(encabezado.getMontoNeto());
					cabecera.setTotal(encabezado.getMontoTotal());
					cabecera.setPorcentaje(encabezado.getPonderacion());
					entityManager.persist(cabecera);
					CotizacionPedido cotizacionPedido = new CotizacionPedido();
					cotizacionPedido.setCotizacionPedidoCabecera(cabecera);
					cotizacionPedido.setNCotiPed(Long.parseLong(encabezado.getNumeroCotizacion()));
					cotizacionPedido.setMargenGlobal(encabezado.getMargenGlobal());
					entityManager.persist(cotizacionPedido);
					for(CabeceraCotPedDTO subCabecera: encabezado.getListaCabeceraCotPeds()){
						CotizacionPedidoNegocio negocio = new CotizacionPedidoNegocio();
						negocio.setCotizacionPedido(cotizacionPedido);
						negocio.setDesNegocio(subCabecera.getNegocio().getDesNegocio());
						negocio.setNegocio(subCabecera.getNegocio().getNegocio());
						negocio.setMargenNegocio(subCabecera.getPorcentaje());
						negocio.setRepresentacion(subCabecera.getRepresentacion());
						entityManager.persist(negocio);
						for(DetalleCotPedDTO subsubCabecera: subCabecera.getListaDetalle()){
							CotizacionPedidoConcepto concepto = new CotizacionPedidoConcepto();
							concepto.setCotizacionPedidoNegocio(negocio);
							concepto.setCodigoConcepto(subsubCabecera.getNegocio().getConcepto());
							concepto.setDesConcepto(subsubCabecera.getNegocio().getDescripcion());
							concepto.setMargenConcepto(subsubCabecera.getMargenConcepto());
							concepto.setMargenPoderado(subsubCabecera.getPorceDelTotal());
							concepto.setMargenPorcePorConcepto(subsubCabecera.getPorcePorConcepto());
							concepto.setMontoMasIva(subsubCabecera.getMonto());
							entityManager.persist(concepto);
							for(DetalleCp dcp :subsubCabecera.getListaDetalleCp()){
								dcp.setCotizacionPedidoConcepto(concepto);
								entityManager.persist(dcp);
							}
						}
					}
				}
				
				entityManager.flush();
				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			log.error("Error, al persister el objeto de getConsultarHistorialCuentaCorriente. #0", e.getMessage());
			return false;
		}
	}	
	
	/*
	 * Parametros de entrada: idSolicitud 
	 * Return :  List<ConceptoDTO> de los concento y los montos ingresados
	 * */	
	@SuppressWarnings("unchecked")
	public  List<ConceptoDTO>  getConceptoMonto(Long idSolicitud, Long montoTotal){
		List<ConceptoDTO> primeraLista = new ArrayList<ConceptoDTO>(0); 
		Locale locale = new Locale("es","CL");
		DecimalFormat formateador = new DecimalFormat("###0.00");
		NumberFormat numberFormatter;
		numberFormatter = NumberFormat.getNumberInstance(locale);
		try{
			List<Object[]> listaAux = (List<Object[]>)entityManager.
					 		createNativeQuery("SELECT [des_concepto] as concepto " +
					 				" ,[monto_mas_iva] as monto_con_iva " +
					 				" ,[margen_concepto] as mangen " +
					 				" FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido_concepto] " +
					 				" WHERE [cotizacion_pedido_negocio_id]	in ( " +
					 				"  SELECT [system_id] " +
					 				"   FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido_negocio] " +
					 				"    WHERE [cotizacion_pedido_id] in ( " +
					 				" SELECT [system_id] " +
					 				" FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido]	" +
					 				" WHERE  [cot_ped_cab_id] in (" +
					 				" SELECT [system_id] " +
					 				" FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido_cabecera] " +
					 				" WHERE solicitud_id=:idSolicitud " +
					 				" )" +
					 				" )" +
					 				" ) ; ")
                                .setParameter("idSolicitud",idSolicitud)
                                .getResultList();
			if(listaAux != null){
				
				for(Object[] array : listaAux){
					if(array != null){
						Double amount = new Double(array[1].toString());
						
						String amountOut;
						amountOut = numberFormatter.format(amount);
						log.info(amountOut + " " + locale.toString());
						
						ConceptoDTO objeto = new ConceptoDTO();
						objeto.setCodigo(array[0].toString());
						objeto.setDescripcion(modificarTexto(array[0].toString()));
						
						
						Long valor = Long.parseLong(array[1].toString());
						Double valorMargen = Double.parseDouble(array[2].toString());
						log.info("monto " +valor +" --> margen :" + valorMargen);
						
						objeto.setMargenOriginal(formateador.format(valorMargen));
						//objeto.setMargen(String.valueOf(formateador.format (valorMargen)));
						
						Double representacion = ((valor.doubleValue() * 100)/montoTotal.doubleValue());
						log.info("representacion del monto :" +representacion);
						
						Double comoInfluye = ((representacion/100) * (valorMargen / 100));
						log.info("comoInfluye :" +comoInfluye);
						
						Double representacionFinal = comoInfluye*100;
						log.info("representacionFinal :" +representacionFinal);
						
						
						objeto.setMonto(valor);
						objeto.setMontoFormateado(amountOut);
						objeto.setMargenParaSuma(representacionFinal);
						objeto.setMontoFormateado(numberFormatter.format(valor));
						objeto.setMargenOriginal(formateador.format(valorMargen));
						
						primeraLista.add(objeto);
					}
				}
				
				/*String codigo = null;
				ConceptoDTO anterior = null;
				Double sumaMargenes = (double)0;
				long sumaMontos = (long)0;
				long cantidad = 0;
				long contador = 0;
				if(primeraLista!= null && primeraLista.size() > 0){
					lista = new ArrayList<ConceptoDTO>(0);
					cantidad = primeraLista.size();
					for(ConceptoDTO con : primeraLista){
						if(codigo == null){
							codigo = con.getDescripcion();
							anterior = con;
							sumaMargenes += con.getMargenParaSuma();
							sumaMontos += con.getMonto();
							contador++;
						}else{
							if(codigo.equals(con.getDescripcion())){
								sumaMargenes += con.getMargenParaSuma();
								sumaMontos += con.getMonto();
								contador++;
							}else{
								anterior.setMargenParaSuma(sumaMargenes);
								anterior.setMonto(sumaMontos);
								anterior.setMargen(String.valueOf(formateador.format (sumaMargenes)));
								anterior.setMontoFormateado(numberFormatter.format(sumaMontos));
								anterior.setMargenOriginal(formateador.format(sumaMargenes));
								lista.add(anterior);
								
								anterior= null;
								sumaMargenes = (double)0;
								sumaMontos = (long)0;
								codigo = con.getDescripcion();
								anterior = con;
								sumaMargenes += con.getMargenParaSuma();
								sumaMontos += con.getMonto();
								contador++;
							}
						}
					}
					
					if(contador == cantidad){
						anterior.setMargenParaSuma(sumaMargenes);
						anterior.setMargen(String.valueOf(formateador.format (sumaMargenes)));
						anterior.setMontoFormateado(numberFormatter.format(sumaMontos));
						anterior.setMargenOriginal(formateador.format(sumaMargenes));
						lista.add(anterior);						
					}
				}
				*/
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al persister el objeto de venta puntual. #0", e.getMessage());
			return null;
		}
		return primeraLista;
	}		
	
	
	/*
	 * Parametros de entrada: idSolicitud 
	 * Return :  List<ConceptoDTO> de los concento y los montos ingresados
	 * */	
	@SuppressWarnings("unchecked")
	public  List<ConceptoDTO>  getProductos(Long idSolicitud){
		List<ConceptoDTO> lista = null;
		try{
			List<Object[]> listaAux = (List<Object[]>)entityManager.
					 		createNativeQuery(" SELECT  [codigo_articulo], [nombre]  " +
							 				"   FROM [SCORING_PRODUCION].[dbo].[detalle_cp] " +
							 				"   WHERE cotizacion_pedido_concepto_id in (" +
							 				"         SELECT [system_id] " +
							 				"          FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido_concepto] " +
							 				"          WHERE [cotizacion_pedido_negocio_id]	in ( " +
							 				"			   SELECT [system_id] " +
							 				"              FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido_negocio] " +
							 				"               WHERE cotizacion_pedido_id in (" +
							 				"                SELECT [system_id] " +
							 				"                 FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido]  " +
							 				"                 WHERE  [cot_ped_cab_id] in (" +
							 				"                      SELECT [system_id] " +
							 				" 					    FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido_cabecera] " +
							 				"                       WHERE solicitud_id=:idSolicitud )" +
							 				"        )" +
							 				"      )" +
							 				") ")
                                .setParameter("idSolicitud",idSolicitud)
                                .getResultList();
			if(listaAux != null){
				lista = new ArrayList<ConceptoDTO>(0);
				for(Object[] array : listaAux){
					if(array != null){
						ConceptoDTO objeto = new ConceptoDTO();
						if(array[0] != null){
							objeto.setCodigo(array[0].toString());
						}
						if(array[1] != null){
							objeto.setDescripcion(modificarTexto(array[1].toString()));
						}
						lista.add(objeto);
					}
				}
			}
		}catch (Exception e) {
			
			log.error("Error, al persister el objeto de venta puntual. #0", e.getMessage());
			return null;
		}
		return lista;
	}		
		

	/*
	 * Parametros de entrada: vacio . 
	 * Return : lista de concepto  y d negocio.
	 * */		
	@SuppressWarnings("unchecked")
	public List<ConceptosNegocio> obtenerConceptosNegocios(){
		try{
			return (List<ConceptosNegocio>)entityManager.createQuery("select cn from ConceptosNegocio cn where cn.systemId not in (1)").getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la sucursal. #0", e.getMessage());
		}
		return null;
	}	

	/*
	 * Parametros de entrada: vacio . 
	 * Return : lista de solicitud ususario  correo 
	 * */		
	@SuppressWarnings("unchecked")
	public List<SolicitudUsuarioCorreo> obtenerAllSolicitudUsuarioCorreo(){
		try{
			return (List<SolicitudUsuarioCorreo>)entityManager.createQuery("select cn from SolicitudUsuarioCorreo cn ").getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la sucursal. #0", e.getMessage());
		}
		return null;
	}
	
	/*
	 * Parametros de entrada: fecha del dia  
	 * Return : devuelve el objeto de la uf;
	 * */	

	@SuppressWarnings("unchecked")
	public Ufs sacarUFDelDia(Date fecha){
		Ufs uf = null;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-dd-MM");
		
		Calendar cal = Calendar.getInstance();
		String newdate = dateformat.format(cal.getTime());
		String newdate1 = dateformat1.format(cal.getTime());
		//log.debug(newdate);
		//log.debug(newdate1);
		List<Ufs> listaUf = null;
		try{
			listaUf = (List<Ufs>) entityManager.createQuery("select u from Ufs u order by u.fecha desc").setMaxResults(10).getResultList();
			if(listaUf != null && listaUf.size() > 0){
				SimpleDateFormat dateformatbase = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat dateformatbase1 = new SimpleDateFormat("yyyy-dd-MM");
				for(Ufs u : listaUf){
					String newdatebase = dateformatbase.format(u.getFecha());
					String newdatebase1 = dateformatbase1.format(u.getFecha());
					//log.debug("newdate:#0 -> newdatebase:#1 --> newdatebase1:#2 ", newdate , newdatebase, newdatebase1);
					if((newdate.trim()).equals(newdatebase.trim())  || (newdate1.trim()).equals(newdatebase1.trim())){
						uf=u;
						break;
					}
					newdatebase = null;
				}
				if(uf == null){
					uf =  createToUpdateIndicadoresEconomicos();
					if(uf == null){
						uf = getObtenerUltimaUfs();
				    	boolean exito = persistUfs(uf);
				    	if(exito){
				    		log.debug("valor : #0", exito);
				    		return uf;
				    	}else{
				    		return uf;
				    	}
					}
				}
			}else{
				if(uf == null){
					uf =  createToUpdateIndicadoresEconomicos();
					if(uf == null){
						uf = getObtenerUltimaUfs();
				    	boolean exito = persistUfs(uf);
				    	if(exito){
				    		log.debug("valor : #0", exito);
				    		return uf;
				    	}else{
				    		return uf;
				    	}
					}
				}				
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de archivos de base para la solicitud #0", e.getMessage());
			if(uf == null){
				uf = createToUpdateIndicadoresEconomicos();
				if(uf == null){
					uf = getObtenerUltimaUfs();
			    	boolean exito = persistUfs(uf);
			    	if(exito){
			    		log.debug("valor : #0", exito);
			    		return uf;
			    	}else{
			    		return uf;
			    	}
				}
			}
		}
		return uf;
	}	
	
	public void imprimirPdf(){
		log.debug("paso por el metodo.");
		
	}
	
	@SuppressWarnings("unchecked")
	public Ufs getObtenerUltimaUfs(){
		Ufs ufsAux= null;
		List<Ufs> listaUf = null;
		try{
			listaUf = (List<Ufs>) entityManager.createQuery("select u from Ufs u order by u.fecha desc").setMaxResults(1).getResultList();
			if(listaUf != null && listaUf.size() > 0){
				Ufs ufsNew = listaUf.get(0);
				if(ufsNew != null){
					ufsAux= new Ufs();
					ufsAux.setDolar(ufsNew.getDolar());
					ufsAux.setEuro(ufsNew.getEuro());
					ufsAux.setFecha(new Date());
					ufsAux.setUtm(ufsNew.getUtm());
					ufsAux.setValor(ufsNew.getValor());
				}
			}
			
		}catch(Exception e){
			log.error("Error, al sacar la lista de archivos de base para la solicitud #0", e.getMessage());
			return ufsAux;
		}
		return ufsAux;
		
	}
     public Ufs createToUpdateIndicadoresEconomicos() {
     	log.debug( "inicia proceso automatico de consultar indicadores economicos");
     	List<IndicadoresDTO> listaIdicadores = new ArrayList<IndicadoresDTO>(0);
     	Ufs uf = null;
     	String dolar = null;
     	String dolar_clp = null;
     	String euro = null;
     	String ufs = null;
     	String utm = null;
     	try{
			URL url = new URL("http://indicadoresdeldia.cl/webservice/indicadores.xml");
			InputStream is = null;
		    try{
			    URLConnection urlConnection = url.openConnection(); 
		        urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36");
		        urlConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8,application/octet-stream"); //application/octet-stream
		        urlConnection.connect();
		        
		        is = urlConnection.getInputStream();
		        log.error("is de la URLConnection : "+ is.toString());
		    }catch(Exception ex){
		    	log.error("Error al conectar la url", ex.getMessage());
		    	ex.printStackTrace();
		    }
		    
	        //url.openConnection().getInputStream();
		    try{
		    	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    	DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    	Document doc = docBuilder.parse(is);
		    	doc.getDocumentElement ().normalize ();
		    	log.debug("El elemento raíz es " + doc.getDocumentElement().getNodeName());

		    	/*moneda*/
		    	NodeList listaMonedas = doc.getElementsByTagName("moneda");
				int totalMonedas = listaMonedas.getLength();
				log.debug("Número total de personas : " + totalMonedas);
				for (int i = 0; i < totalMonedas ; i ++) {
					Node moneda = listaMonedas.item(i);
					if (moneda.getNodeType() == Node.ELEMENT_NODE){
						log.debug(Node.ELEMENT_NODE);
					}
					 
					Element elemento = (Element) moneda;
					dolar = getTagValue("dolar",elemento);
					dolar = dolar.replace("$", "");
					dolar = dolar.replace(",", ".");
					log.debug("dolar  : "  + dolar);
					
					IndicadoresDTO indDolar = new IndicadoresDTO();
					indDolar.setCodigo("Dolar Observado");
					indDolar.setValor(Double.parseDouble(dolar));
					listaIdicadores.add(indDolar);
					
					dolar_clp = getTagValue("dolar_clp",elemento);
					dolar_clp = dolar_clp.replace("$", "");
					dolar_clp = dolar_clp.replace(",", ".");
					log.debug("dolar_clp : "  + dolar_clp);


					euro = getTagValue("euro",elemento);
					euro = euro.replace("$", "");
					euro = euro.replace(",", ".");					
					log.debug("euro  : "  + euro);
					IndicadoresDTO indEuro = new IndicadoresDTO();
					indEuro.setCodigo("Euro");
					indEuro.setValor(Double.parseDouble(euro));
					listaIdicadores.add(indEuro);
				}

				
		    	NodeList listaIndica = doc.getElementsByTagName("indicador");
				int totalIndica = listaIndica.getLength();
				log.debug("Número total de indicadores : " + totalIndica);
				for (int i = 0; i < totalIndica ; i ++) {
					Node indicador = listaIndica.item(i);
					if (indicador.getNodeType() == Node.ELEMENT_NODE){
						log.debug(Node.ELEMENT_NODE);
					}
					 
					Element elemento = (Element) indicador;
					ufs = getTagValue("uf",elemento);
					ufs = ufs.replace("$", "");
					ufs = ufs.replace(".", "");
					ufs = ufs.replace(",", ".");
					log.debug("ufs  : "  + ufs);
					IndicadoresDTO indUf = new IndicadoresDTO();
					indUf.setCodigo("UF");
					indUf.setValor(Double.parseDouble(ufs));
					listaIdicadores.add(indUf);	
					

					utm = getTagValue("utm",elemento);
					utm = utm.replace("$", "");
					utm = utm.replace(".", "");
					utm = utm.replace(",", ".");					
					log.debug("utm  : "  + utm);
					
					IndicadoresDTO indUtm = new IndicadoresDTO();
					indUtm.setCodigo("UTM");
					indUtm.setValor(Double.parseDouble(utm));
					listaIdicadores.add(indUtm);		
					
				}
		    }catch (Exception e) {
		    	log.error("no se puede parsear: ", e.getMessage());
		    	
		    }
		    
		    if(listaIdicadores != null && listaIdicadores.size() > 0){
		    	uf = new Ufs();
		    	uf.setFecha(new Date());
		    	for(IndicadoresDTO indicador : listaIdicadores){
		    		if(indicador.getCodigo().equals("Euro")){
		    			uf.setEuro(indicador.getValor());
		    		}else if(indicador.getCodigo().equals("Dolar Observado")){
		    			uf.setDolar(indicador.getValor());
		    		}else if(indicador.getCodigo().equals("UF")){
		    			uf.setValor(indicador.getValor());
		    		}else if(indicador.getCodigo().equals("UTM")){
		    			uf.setUtm(indicador.getValor());
		    		}
		    	}
		    	boolean exito = persistUfs(uf);
		    	if(exito){
		    		log.debug("valor : #0", exito);
		    		return uf;
		    	}else{
		    		return uf;
		    	}
		    }
		    
     	}catch (Exception e) {
     	  e.printStackTrace();
 		  log.debug("error al sacar los indicadores ");
 	    }
     	
       String date = new Date().toString();
       
       log.debug( "Proceso ejecutado hora indicada: " + date );
       log.debug( "finaliza proceso automatico de consultar indicadores economicos");
       return uf;
     }	
	
	
     public String getTagValue(String tag, Element elemento) {
		 NodeList lista = elemento.getElementsByTagName(tag).item(0).getChildNodes();
		 Node valor = (Node) lista.item(0);
		 return valor.getNodeValue();
    }
     
     
	
	
	/*
	 * Parametros de entrada: persist de LcredSolicitudLcredito. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudLineaCredito(LcredSolicitudLcredito solicitud){
		try{
			entityManager.persist(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de linea de Crédito. #0", e.getMessage());
			return false;
		}
		return true;
	}	
	
	/*
	 * Parametros de entrada: persist de LcredSolicitudCondiciones. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudCondiciones(LcredSolicitudCondiciones solicitud){
		try{
			entityManager.persist(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de linea de Crédito. #0", e.getMessage());
			return false;
		}
		return true;
	}	
	
	/*
	 * Parametros de entrada: persist de LcredSolicitudBloqueos. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudBloqueoDesbloqueo(LcredSolicitudBloqueos solicitud){
		try{
			entityManager.persist(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de linea de Crédito. #0", e.getMessage());
			return false;
		}
		return true;
	}	

	/*
	 * Parametros de entrada: persist de LcredSolicitudProrroga. 
	 * Return : verdadero o un falso ;
	 * */		
	public  LcredSolicitudProrroga  getSolicitudProrroga(String numero, Long solicitud){
		LcredSolicitudProrroga lsp = null;
		try{
			lsp = (LcredSolicitudProrroga)entityManager.
					 		createQuery("SELECT da FROM LcredSolicitudProrroga da "+
		                                "WHERE da.id.nroCheque=:numero  and da.id.numSolicitud=:idSolicitud ")
		                                .setParameter("numero",numero)
		                                .setParameter("idSolicitud", solicitud)
		                                .getSingleResult();

		}catch (Exception e) {
			log.error("Error, al persister el objeto de getSolicitudProrroga. #0", e.getMessage());
			return null;
		}
		return lsp;
	}
	
	/*
	 * Parametros de entrada: persist de LcredSolicitudProrroga. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudProrroga(LcredSolicitudProrroga solicitud){
		try{
			entityManager.persist(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de la prorroga. #0", e.getMessage());
			return false;
		}
		return true;
	}	

	/*
	 * Parametros de entrada: persist de LcredSolicitudProrroga. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  mergeSolicitudProrroga(LcredSolicitudProrroga solicitud){
		try{
			
			entityManager.createQuery("update LcredSolicitudProrroga set id.vencNuevo=:nuevo  where id.numSolicitud=:sol and id.nroCheque=:cheque")
				.setParameter("sol", solicitud.getId().getNumSolicitud())
				.setParameter("cheque", solicitud.getId().getNroCheque())
				.setParameter("nuevo", solicitud.getId().getVencNuevo())
				.executeUpdate();
			entityManager.flush();

			entityManager.merge(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto de la prorroga. #0", e.getMessage());
			return false;
		}
		return true;
	}	
	
	/*
	 * Parametros de entrada: persist de LcredSolicitudDm. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudDM(LcredSolicitudDm solicitud){
		try{
			entityManager.persist(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de creacion de DM. #0", e.getMessage());
			return false;
		}
		return true;
	}
	
	/*
	 * Parametros de entrada: vacio. 
	 * Return : lista de estados.
	 * */		
	@SuppressWarnings("unchecked")
	public List<LcredEstado> obtenerEstados(){
		try{
			return (List<LcredEstado>)entityManager.createQuery("select le from LcredEstado le order by le.grupoBarraAnalista asc").getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la lista estados. #0", e.getMessage());
		}
		return null;
	}	

	/*
	 * Parametros de entrada: vacio. 
	 * Return : lista de sociedades.
	 * */		
	@SuppressWarnings("unchecked")
	public List<Sociedad> obtenerSociedades(){
		try{
			return (List<Sociedad>)entityManager.createQuery("select s from Sociedad s order by s.idSociedad asc").getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la lista sociedades. #0", e.getMessage());
		}
		return null;
	}	

	
	/*
	 * Parametros de entrada: codigo de perfil. 
	 * Return : lista de estados.
	 * */		
	public LcredPerfiles obtenerPerfil(String codigo){
		try{
			return (LcredPerfiles)entityManager.find(LcredPerfiles.class, codigo);
		}catch (Exception e) {
			log.error("Error, al sacar el objeto  de perfiles. #0", e.getMessage());
		}
		return null;
	}	

	
	/*
	 * Parametros de entrada: persist de SolicitudHitos. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudHitos(SolicitudHitos solicitudHitos){
		try{
			entityManager.persist(solicitudHitos);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de solictud hitos #0", e.getMessage());
			return false;
		}
		return true;
	}

	/*
	 * Parametros de entrada: persist de SolicitudConceptosNegocioLC. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudConceptosNegocioLC(SolicitudConceptosNegocioLC solicitudConceptosNegocioLC){
		try{
			entityManager.persist(solicitudConceptosNegocioLC);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de solicitud conceptos negocio LC #0", e.getMessage());
			return false;
		}
		return true;
	}	
	
	/*
	 * Parametros de entrada: codigo de la solicitud y la jerarquia del conceptoNegocio. 
	 * Return : verdadero si existe el concepto ingresado y falso si el concepto no esta ingresa para una solicitud;
	 * */		

	public  boolean  getSolicitudConceptosNegocioLCEvaluar(Long idSol, String jerarquia){
		boolean existe = false;
		try{
			SolicitudConceptosNegocioLC scn  =  (SolicitudConceptosNegocioLC)
					  entityManager.createQuery(" select le from SolicitudConceptosNegocioLC le " +
											    " where le.solicitud.id.numSolicitud=:idSol " +
											    " and le.conceptosNegocio.jerarquia=:jerarquia")
					.setParameter("idSol", idSol)
					.setParameter("jerarquia", jerarquia)
					.getSingleResult();

					if(scn != null){
						log.debug("getSolicitudConceptosNegocioLC cantidad #0", scn.getConceptosNegocio().getConcepto());
						existe = true;
					}
		}catch (NoResultException noe) {
			log.error("no existe concepto ingresado");
		}catch (Exception e) {
			log.error("Error, al persister el objeto de solicitud conceptos negocio LC #0", e.getMessage());
		}
		return existe;
	}
	

	/*
	 * Parametros de entrada: codigo de la solicitud. 
	 * Return : Lista de concepto de negocio asociado en lc ;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<SolicitudConceptosNegocioLC>  getSolicitudConceptosNegocioLC(Long idSol){
		List<SolicitudConceptosNegocioLC> lista = null;
		try{
			
			lista =  (List<SolicitudConceptosNegocioLC>)
					entityManager.createQuery("select le from SolicitudConceptosNegocioLC le " +
											  " where le.solicitud.id.numSolicitud=:idSol")
					.setParameter("idSol", idSol)		
					.getResultList();;

					if(lista != null && lista.size() > 0){
						log.debug("getSolicitudConceptosNegocioLC cantidad #0", lista.size());
					}else{
						lista = null;
					}
		}catch (Exception e) {
			log.error("Error, al persister el objeto de solicitud conceptos negocio LC #0", e.getMessage());
			return null;
		}
		return lista;
	}	

	
	
	/*
	 * Parametros de entrada: codigo de solicitud. 
	 * Return : Lista de los hitos de la solicitud;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<SolicitudHitos> getSolicitudHitos(Long idSolicitud){
		List<SolicitudHitos> lista = null;
		try{
			lista = (List<SolicitudHitos>)entityManager.createQuery("" +
					"select sh from SolicitudHitos sh where sh.idSolicitud=:solAux order by sh.fechaHora ")
					.setParameter("solAux", idSolicitud)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de solictud hitos #0", e.getMessage());
		}
		return lista;
	}

	/*
	 * Parametros de entrada: codigo de solicitud, opicon "Aprobador" = PA o "Rechazaron" = SA. 
	 * Return : Lista de usuarios aprobador y re de la solicitud;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<String> getTheAprobadoresRechazaronSolicitud(Long idSolicitud, String opcion){
		List<String> listaFinal = null;
		try{
			List<Object[]> lista = null;
			if(opcion != null){
				try{
					lista = (List<Object[]>)entityManager
							.createNativeQuery(" select cast([num_solicitud] as varchar) as num_solicitud, " +
									    "  cast([usuario_destino] as varchar) as usuario_destino   "+
										"  from [SCORING_PRODUCION].[dbo].[lcred_trafico_estados] "+
										"  where num_solicitud=:solicitudAux " +
										"  and estado_destino=:Opcion ")
							.setParameter("solicitudAux", idSolicitud)
							.setParameter("Opcion", opcion)
							.getResultList();
					if(lista != null && lista.size() > 0){
						listaFinal = new ArrayList<String>(0);
						for(Object[] obj : lista){
							if(obj != null && obj.length > 1){
								listaFinal.add(obj[1].toString());
							}
						}
					}else{
						log.debug("no tiene ningun registro la solicitud consultada ");
						listaFinal = new ArrayList<String>(0);
					}
				}catch (Exception e) {
					log.error("error, al sacar a los aprobaron y los que rechazaron de las antiguas solicitudes #0", e.getMessage());
					listaFinal = new ArrayList<String>(0);
				}
			}
		}catch (Exception e) {
			log.error("Error, al persister el objeto de solictud hitos #0", e.getMessage());
		}
		return listaFinal;
	}
	
	/*
	 * Parametros de entrada: codigo de solicitud. 
	 * Return : numero si existe algun hitos de derivacion ;
	 * */		
	public  Long getEvaluarSolicitudHitosDerivadaAprobacionAnalisis(Long idSolicitud){
		Long numero = null;
		try{
			numero = (Long)entityManager.createQuery("" +
					 " SELECT COUNT(sh.systemId) " +
					 " FROM SolicitudHitos sh " +
					 " WHERE sh.idSolicitud=:solAux " +
					 " and sh.codigoEstado in ('SA','B')")
			.setParameter("solAux", idSolicitud)
			.getSingleResult();
			if(numero != null){
				log.debug("getEvaluarSolicitudHitosDerivadaAprobacionAnalisis evalucion #0", numero);
			}
			
		}catch (Exception e) {
			log.error("Error, al obtener el dato de derivavion a aprobacion o analisis #0", e.getMessage());
		}
		return numero;
	}
	
	/*
	 * Parametros de entrada: codigo de solicitud. 
	 * Return : numero de ususarios que faltan por aprobar la solicitud ;
	 * */		
	@SuppressWarnings("unchecked")
	public  Long getEvaluarSolicitudAprobacionAnalisisEvaluarTodosObligatotios(Long idSolicitud){
		Long numero = null;
		long cantidad = 0;
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
				for(SolicitudUsuarioDerivada sud : lista){
					if(sud.getConfirmacionObligatoria().booleanValue()){
						if(sud.getConfirmacion()== null){
							cantidad++;
						}
					}
				}
				if(cantidad != 0){
					numero = new Long(cantidad);
				}
				log.debug("cantidad de usuarios que no ha aprobado #0", numero);
			}
		}catch (Exception e) {
			log.error("Error, al obtener el dato de derivavion a aprobacion o analisis #0", e.getMessage());
		}	
		return numero;
	}
	
	
	
	/*
	 * Parametros de entrada: datos de el logs. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudLogs(long idSolicitud, String usuario, Date fechaHora, String codigoEstado, String descripcionEstado, String accion){
		SolicitudLogs logs = new SolicitudLogs();
		logs.setIdSolicitud(idSolicitud);
		logs.setUsuario(usuario);
		logs.setFechaHora(fechaHora);
		logs.setCodigoEstado(codigoEstado);
		logs.setDescripcionEstado(descripcionEstado);
		logs.setAccion(accion);
		
		try{
			long numero  = obtenerNumeroHitoToLog(2);
			logs.setSystemId(numero);
			entityManager.persist(logs);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de solictud logs #0", e.getMessage());
			return false;
		}
		return true;
	}
	
	/*
	 * Parametros de entrada: persist de SolicitudTipoFormaPago. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudTipoFormaPago(SolicitudTipoFormaPago solicitud){
		try{
			entityManager.persist(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de Solicitud Tipo Forma Pago. #0", e.getMessage());
			return false;
		}
		return true;
	}	
	
	/*
	 * Parametros de entrada: codigo de la solicitud. 
	 * Return : un tipo de objeto;
	 * */		
	public  SolicitudTipoFormaPago  getSolicitudTipoFormaPago(Long idSolicitud){
		SolicitudTipoFormaPago objeto = null;
		try{
			objeto = (SolicitudTipoFormaPago)entityManager.createQuery("" +
					"select stfp from SolicitudTipoFormaPago stfp where stfp.idSolicitud=:solAux")
					.setParameter("solAux", idSolicitud)
					.getSingleResult();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de objeto de solictud logs #0", e.getMessage());
		}
		return objeto;
	}

	/*
	 * Parametros de entrada: persist de SolicitudTipoFormaPago. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  mergerSolicitudTipoFormaPago(SolicitudTipoFormaPago solicitud){
		try{
			entityManager.merge(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merger el objeto de Solicitud Tipo Forma Pago. #0", e.getMessage());
			return false;
		}
		return true;
	}		
	
	
	
	
	/*
	 * Parametros de entrada: codigo de la solicitud. 
	 * Return : Lista de los logs de la solicitud;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<SolicitudLogs>  getSolicitudLogs(Long idSolicitud){
		List<SolicitudLogs> lista = null;
		try{
			lista = (List<SolicitudLogs>)entityManager.createQuery("" +
					"select sl from SolicitudLogs sl where sl.idSolicitud=:solAux").setParameter("solAux", idSolicitud).getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de objeto de solictud logs #0", e.getMessage());
		}
		return lista;
	}
		
	
	
	/*
	 * Parametros de entrada: String usuario, String codigo,String canal, String zona, String sucrsal, long monto, String tiposolicitud  . 
	 * Return : lista de objetos de DestinatarioDTO o valor null
	 * */		

	@SuppressWarnings("unchecked")
	public List<SolicitudEstadoDTO> obtenerSolicitudesEstados(String usuario, String codigo, String fechaTope){
		List<SolicitudEstadoDTO> listaFinal = null;
		try{
			
			List<Object[]> lista = 	(List<Object[]>)
					entityManager.createNativeQuery("EXEC [SCORING_PRODUCION].dbo.lcred_busca_solicitud_estado " +
					" @emisor ='"+ usuario  +"', @tipo ='"+codigo +"', " +
					" @fechatope ='" + fechaTope +"'").getResultList();
			
			if(lista != null && lista.size() > 0){
				listaFinal = new ArrayList<SolicitudEstadoDTO>(0);
				for(Object[] obj : lista){
					if(obj != null){
						SolicitudEstadoDTO se = new SolicitudEstadoDTO();
						if(obj[0] != null){
							se.setIdSolictud(Long.parseLong(obj[0].toString()));
						}
						if(obj[1] != null){
							se.setRut(obj[1].toString());
						}
						if(obj[2] != null){
							se.setRazonSocial(obj[2].toString());
						}
						if(obj[3] != null){
							se.setEmisor(obj[3].toString());
						}						
						if(obj[4] != null){
							se.setDespTipoSolictud(obj[4].toString());
						}							
						if(obj[5] != null){
							se.setFechaRecepcion(obj[5].toString());
						}	
						if(obj[6] != null){
							se.setFechaSalida(obj[6].toString());
						}						
						if(obj[7] != null){
							se.setEstado(obj[7].toString());
						}						
						if(obj[8] != null){
							se.setUsuarioEnProceso(obj[8].toString());
						}						
						
						listaFinal.add(se);
					}
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al sacar la sucursal. #0", e.getMessage());
		}
		return listaFinal;
	}	
	
	/*
	 * Parametros de entrada: character. 
	 * Return : objeto de estados.
	 * */		
	public Estados obtenerEstados(char caracter){
		Estados estado = null;
		try{
			estado =  (Estados)entityManager.createQuery("select e from Estados e where e.codEstado=:codigo").setParameter("codigo", caracter).getSingleResult();
		}catch (Exception e) {
			log.error("Error, al sacar la lista estados. #0", e.getMessage());
		}
		return estado;
	}	
	
	/*
	 * Parametros de entrada: opcion de sacar el nuemro . 
	 * Return : objeto de estados.
	 * */		
	public Long obtenerNumeroHitoToLog(int opcion){
		Long numero = null;
		Long num = null;
		try{
			if(opcion == 1){
				num =  (Long)entityManager.createQuery("select max(sh.systemId) from SolicitudHitos sh ").getSingleResult();
			}else{
				num =  (Long)entityManager.createQuery("select max(sh.systemId) from SolicitudLogs sh ").getSingleResult();
			}
			
			if(num != null){
				numero = num.longValue();
				numero++;
			}else{
				numero = new Long(1);
			}
 
		}catch (Exception e) {
			log.error("Error, al sacar el numero de hito o del log. #0", e.getMessage());
			numero = new Long(1);
			
		}
		return numero;
	}	
	
	/*
	 * Parametros de entrada: numero de la solicitud . 
	 * Return : lista de archivos adjuntos.
	 * */		
	@SuppressWarnings("unchecked")
	public List<ArchivoAdjunto> obtenerArchivosSolicitud(Long idSolicitud, ArchivoAdjuntoType tipo){
		List<ArchivoAdjunto> lista = null;
		try{
			lista =  (List<ArchivoAdjunto>)entityManager
					.createQuery("select aa from ArchivoAdjunto aa where aa.solicitud.id.numSolicitud=:sol and aa.tipo=:tipo")
					.setParameter("tipo", tipo)
					.setParameter("sol", idSolicitud).getResultList();
			if(lista != null && lista.size()>0){
				return lista;
			}else{
				return null;
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de archivos Adjuntos. #0", e.getMessage());
		}
		return lista;
	}	
	
	/*
	 * Parametros de entrada: numero de la solicitud y el t. 
	 * Return : lista de archivos adjuntos.
	 * */		
	@SuppressWarnings("unchecked")
	public List<LcredArchivoAdjuntos> obtenerArchivosSolicitudAntiguas(Long idSolicitud, String codigo){
		List<LcredArchivoAdjuntos> lista = null;
		try{
			lista =  (List<LcredArchivoAdjuntos>)entityManager
					.createQuery("select laa from LcredArchivoAdjuntos laa where laa.id.numSolicitud=:sol")
					.setParameter("sol", idSolicitud).getResultList();
			if(lista != null){
				for(LcredArchivoAdjuntos laa : lista){
					log.debug("nombre del archivo :#0", laa.getId().getArchivoAdjunto());
				}
				
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de Archivos Adjuntos Antiguos. #0", e.getMessage());
		}
		return lista;
	}	
	
	
	/*
	 * Parametros de entrada: numero de la solicitud. 
	 * Return : lista de observaciones.
	 * */		
	@SuppressWarnings("unchecked")
	public List<LcredSolicitudObservaciones> obtenerListaSolicitudObservaciones(Long idSolicitud){
		List<LcredSolicitudObservaciones> lista = null;
		try{
			lista =  (List<LcredSolicitudObservaciones>)entityManager
					.createQuery("select ls from LcredSolicitudObservaciones ls where ls.id.numSolicitud=:sol order by ls.id.correlativo ")
					.setParameter("sol", idSolicitud)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de observaciones. #0", e.getMessage());
		}
		return lista;
	}	

	/*
	 * Parametros de entrada: numero de la solicitud. 
	 * Return : lista de observaciones con order descendente.
	 * */		
	@SuppressWarnings("unchecked")
	public List<LcredSolicitudObservaciones> obtenerListaSolicitudObservacionesDesc(Long idSolicitud){
		List<LcredSolicitudObservaciones> lista = null;
		try{
			lista =  (List<LcredSolicitudObservaciones>)entityManager
					.createQuery("select ls from LcredSolicitudObservaciones ls where ls.id.numSolicitud=:sol order by ls.id.correlativo desc")
					.setParameter("sol", idSolicitud)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de observaciones. #0", e.getMessage());
		}
		return lista;
	}	
	
	
	/*
	 * Parametros de entrada: numero de la solicitud. 
	 * Return : correlativo.
	 * */		

	public Long obtenerCorrelativoObservaciones(Long idSolicitud){
		Long objeto = null;
		try{
			objeto =  (Long)entityManager
					.createQuery("select max(ls.id.correlativo) from LcredSolicitudObservaciones ls where ls.id.numSolicitud=:sol")
					.setParameter("sol", idSolicitud)
					.getSingleResult();
			
			if(objeto != null){
				objeto++;
			}else{
				objeto = new Long("1");
			}
			
		}catch (Exception e) {
			log.error("Error, al sacar el correlativo de la observaciones. #0", e.getMessage());
			objeto = new Long("1");
		}
		return objeto;
	}
	
	/*
	 * Parametros de entrada: persist de LcredSolicitudObservaciones. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudObservaciones(LcredSolicitudObservaciones observacion){
		try{
			entityManager.persist(observacion);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de LcredSolicitudObservaciones  #0", e.getMessage());
			return false;
		}
		return true;
	}
	
	/*
	 * Parametros de entrada: persist de SolicitudUsuarioCorreo. 
	 * Return : verdadero o un falso ;
	 * */		
	public  SolicitudUsuarioCorreo  persistSolicitudCorreo(SolicitudUsuarioCorreo solicitudCorreo){
		try{
			entityManager.persist(solicitudCorreo);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de SolicitudUsuarioCorreo  #0", e.getMessage());
			return null;
		}
		return solicitudCorreo;
	}
	
	/*
	 * Parametros de entrada: merge de SolicitudUsuarioCorreo. 
	 * Return : verdadero o un falso ;
	 * */		
	public  SolicitudUsuarioCorreo  mergerSolicitudCorreo(SolicitudUsuarioCorreo solicitudCorreo){
		try{
			entityManager.merge(solicitudCorreo);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merger el objeto de SolicitudUsuarioCorreo  #0", e.getMessage());
			return null;
		}
		return solicitudCorreo;
	}
	
	/*
	 * Parametros de entrada: username. 
	 * Return : verdadero o un falso ;
	 * */	
	public boolean  verificarExistenciaSolicitudCorreo(String username){
		boolean objeto = false;
		try{
			SolicitudUsuarioCorreo correo =  (SolicitudUsuarioCorreo)entityManager
					.createQuery("select suc from SolicitudUsuarioCorreo suc where suc.username=:user")
					.setParameter("user", username)
					.getSingleResult();
			if(correo != null){
				log.debug("nombre del usaurio #0", correo.getNombre());
				objeto = true;
			}
			
		}catch (Exception e) {
			log.error("Error, al sacar si existe el obejeto consultado. #0", e.getMessage());
			objeto = false;
		}
		return objeto;
	}
	
	/*
	 * Parametros de entrada: username. 
	 * Return : verdadero o un falso ;
	 * */	
	public SolicitudUsuarioCorreo  getSolicitudUsuarioCorreo(String username){
		SolicitudUsuarioCorreo objeto = null;
		try{
			 objeto =  (SolicitudUsuarioCorreo)entityManager
					.createQuery("select suc from SolicitudUsuarioCorreo suc where suc.username=:user")
					.setParameter("user", username)
					.getSingleResult();
		
		}catch (Exception e) {
			log.error("Error, al sacar el objeto  SolicitudUsuarioCorreo. #0", e.getMessage());
			objeto = null;
		}
		return objeto;
	}
	
	
	/*
	 * Parametros de entrada: vacio. 
	 * Return : lista de motivos de rechazos ;
	 * */	
	@SuppressWarnings("unchecked")
	public List<LcredMotivoRechazo> getMotivosRechazos(){
		List<LcredMotivoRechazo> lista = null;
		try{
			 lista =  (List<LcredMotivoRechazo>)entityManager
					.createQuery("select lmr from LcredMotivoRechazo lmr order by lmr.desRechazo asc ")
					.getResultList();
			 if(lista != null){
				 log.debug("lista de rechazo cantidad de registro  #0", lista.size());
			 }
		
		}catch (Exception e) {
			log.error("Error, al sacar lista de motivos de rechazos. #0", e.getMessage());
			lista = null;
		}
		return lista;
	}
	
	
	/*
	 * Parametros de entrada: vacio. 
	 * Return : lista de motivos de rechazos ;
	 * */	
	public LcredMotivoRechazo getMotivosRechazosCodigo(String codRechazo ){
		LcredMotivoRechazo lmr = null;
		try{
			lmr =  (LcredMotivoRechazo)entityManager
					.createQuery("select lmr from LcredMotivoRechazo lmr " +
							" where lmr.codRechazo=:codigo")
							.setParameter("codigo", codRechazo)
							.getSingleResult();
		
		}catch (Exception e) {
			log.error("Error, al sacar lista de motivos de rechazos. #0", e.getMessage());
			
		}
		return lmr;
	}
	
	
	/*
	 * Parametros de entrada: merger de solicitud. 
	 * Return : retorna el objeto LcredSolicitud ;
	 * */		
	public  boolean  mergerSolicitudVentaPuntual(LcredSolicitudVtapuntual solicitud){
		try{
			entityManager.merge(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto de venta puntual. #0", e.getMessage());
			return false;
		}
		return true;
	}	

	/*
	 * Parametros de entrada: merger de LcredSolicitudLcredito. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  mergerSolicitudLineaCredito(LcredSolicitudLcredito solicitud){
		try{
			entityManager.merge(solicitud);
			entityManager.flush();
			entityManager.refresh(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto de Linea Credito. #0", e.getMessage());
			return false;
		}
		return true;
	}	
	

	/*
	 * Parametros de entrada: merger de LcredSolicitudLcredito. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  mergerSolicitudCondiciones(LcredSolicitudCondiciones solicitud){
		try{
			entityManager.merge(solicitud);
			entityManager.flush();
			entityManager.refresh(solicitud);
		}catch (Exception e) {
			log.error("Error, al merge el objeto de Solicitud Condiciones. #0", e.getMessage());
			return false;
		}
		return true;
	}	
	

	/*
	 * Parametros de entrada: merger de LcredSolicitudBloqueos. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  mergerSolicitudBloqueoToDesbloqueo(LcredSolicitudBloqueos solicitud){
		try{
			entityManager.merge(solicitud);
			entityManager.flush();
			entityManager.refresh(solicitud);
		}catch (Exception e) {
			log.error("Error, al merge el objeto de Solicitud Bloqueo To Desbloqueo. #0", e.getMessage());
			return false;
		}
		return true;
	}	
	
	/*
	 * Parametros de entrada: merger de LcredSolicitudBloqueos. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  mergerSolicitudOtrasSolicitudes(LcredSolicitudOtras solicitud){
		try{
			entityManager.merge(solicitud);
			entityManager.flush();
			entityManager.refresh(solicitud);
		}catch (Exception e) {
			log.error("Error, al merge el objeto de Solicitud Otras Solicitudes. #0", e.getMessage());
			return false;
		}
		return true;
	}
	
	/*
	 * Parametros de entrada: codigo de cargo. 
	 * Return :  ;
	 * */	
	public LcredCargos getCargo(String codigo){
		LcredCargos cargo = null;
		try{
				cargo =  (LcredCargos)entityManager
					.createQuery("select lc from LcredCargos lc where lc.codCargo=:codigo")
					.setParameter("codigo", codigo)
					.getSingleResult();
			 if(cargo != null){
				 log.debug(" nivel cargo  #0", cargo.getNivelCargo());
			 }
		
		}catch (Exception e) {
			log.error("Error, al sacar lista de motivos de rechazos. #0", e.getMessage());
			cargo = null;
		}
		return cargo;
	}
	
	/*
	 * Parametros de entrada: vacio. 
	 * Return : lista de cargos;
	 * */	
	@SuppressWarnings("unchecked")
	public List<LcredCargos> getListaCargos(){
		List<LcredCargos> lista = null;
		try{
			 lista =  (List<LcredCargos>)entityManager
					.createQuery("select lc from LcredCargos lc")
					.getResultList();
			 if(lista != null){
				 log.debug("lista de cargo cantidad de registro  #0", lista.size());
			 }
		
		}catch (Exception e) {
			log.error("Error, al sacar lista de cargo de rechazos. #0", e.getMessage());
			lista = null;
		}
		return lista;
	}
	
	/*
	 * Parametros de entrada: persist de LcredSolicitudBloqueos. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudOtras(LcredSolicitudOtras solicitud){
		try{
			entityManager.persist(solicitud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de solicitud otras. #0", e.getMessage());
			return false;
		}
		return true;
	}	

	/*
	 * Parametros de entrada: codigo de la solicitud. 
	 * Return : verdadero o un falso ;
	 * */		
	public  LcredSolicitudOtras  getSolicitudOtrasId(Long solicitud){
		LcredSolicitudOtras nuevo = null;
	 	try{
	 		nuevo = (LcredSolicitudOtras)entityManager
					 .createQuery("Select lso from LcredSolicitudOtras lso where lso.numSolicitud=:solAux")
					 .setParameter("solAux", solicitud).getSingleResult();
		 	
	 		if(nuevo != null){
		 		log.debug("getLcredSolicitudOtras #0",nuevo.getObservacionesInicial());
		 	}
	 	}catch (Exception e) {
			log.debug("no existe datos de LcredSolicitudOtras. #0", e.getMessage());
		}	
	 	return nuevo;
	}

	
	/*
	 * Parametros de entrada: codigo de la solicitud. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  getEliminarListaDms(Long solicitud){
		 try{
			Integer numero = entityManager
					 .createNativeQuery(" DELETE [SCORING_PRODUCION].[dbo].[lcred_solicitud_dm] " +
					 					" WHERE [num_solicitud]=:solAux ")
					 .setParameter("solAux", solicitud)
					 .executeUpdate();
			   if(numero != null && numero.intValue() > 0){
				   return true;
			   }
		}catch (Exception e) {
			log.debug("no existe datos de los dm. #0",e.getMessage() );
		}
		return false;
	}		
	
	
	
	/*
	 * Parametros de entrada: codigo de la solicitud. 
	 * Return : verdadero o un falso ;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<LcredSolicitudDm>  getSacarListaDms(Long solicitud){
		LcredSolicitudDm dm = null;
		LcredSolicitudDmId id = null;
		List<LcredSolicitudDm> lista = null;
		 try{
			 List<Object[]> listaObjetos = (List<Object[]>)entityManager
					 .createNativeQuery(" SELECT  [num_solicitud]" +
					 					" ,[rut]" +
					 					" ,[nombre]" +
					 					" ,[oficina_ventas]" +
					 					" ,[zona] " +
					 					" ,[lista_precio]" +
					 					" ,[sector] " +
					 					" ,[vend_telefono] " +
					 					" ,[vend_terreno]" +
					 					" ,[cobrador]" +
					 					" ,[cond_expedicion]" +
					 					" ,[cond_pago] " +
					 					" FROM [SCORING_PRODUCION].[dbo].[lcred_solicitud_dm]" +
					 					" WHERE [num_solicitud]=:solAux")
					 .setParameter("solAux", solicitud)
					 .getResultList();
			   if(listaObjetos != null && listaObjetos.size() > 0){
				   lista = new ArrayList<LcredSolicitudDm>(0);
				   for(Object[] obj : listaObjetos){
					   id = new LcredSolicitudDmId();
					   if(obj != null && obj[0]!=null){
						   id.setNumSolicitud(Long.parseLong(obj[0].toString())); 
					   }else{
						   id.setNumSolicitud(0);
					   }
					   if(obj != null && obj[1]!=null){
						   id.setRut(obj[1].toString());
					   }else{
						   id.setRut(null);
					   }
					   if(obj != null && obj[2]!=null){
						   id.setNombre(obj[2].toString());
					   }else{
						   id.setNombre(null);
					   }
					   
					   if(obj != null && obj[3]!=null){
						   id.setOficinaVentas(obj[3].toString()); 
					   }else{
						   id.setOficinaVentas(null); 
					   }
					   
					   if(obj != null && obj[4]!=null){
						  id.setZona(obj[4].toString()); 
					   }else{
						  id.setZona(null);  
					   }
					   
					   if(obj != null && obj[5]!=null){
						  id.setListaPrecio(obj[5].toString()); 
					   }else{
						   id.setListaPrecio(null);  
					   }
					   
					   if(obj != null && obj[6]!=null){
						   id.setSector(obj[6].toString());
					   }else{
						   id.setSector(null);
					   }
					   
					   if(obj != null && obj[7]!=null){
						   id.setVendTelefono(obj[7].toString());
					   }else{
						   id.setVendTelefono(null);
					   }
					   
					   if(obj != null && obj[8]!=null){
						   id.setVendTerreno(obj[8].toString());
					   }else{
						   id.setVendTerreno(null);
					   }
					   
					   if(obj != null && obj[9]!=null){
						   id.setCobrador(obj[9].toString());
					   }else{
						   id.setCobrador(null); 
					   }
					   
					   if(obj != null && obj[10]!=null){
						  id.setCondExpedicion(obj[10].toString()); 
					   }else{
						  id.setCondExpedicion(null);  
					   }
					   
					   if(obj != null && obj[11]!=null){
						  id.setCondPago(obj[11].toString());
					   }else{
						  id.setCondPago(null); 
					   }
					   dm = new LcredSolicitudDm();
					   dm.setId(id);
					   lista.add(dm);  
				   }
				 log.debug("Existen registro de dms", lista.size());
			   }
		}catch (Exception e) {
			log.debug("no existe datos de los dm. #0",e.getMessage() );
		}
		return lista;
	}	
	
	/*
	 * Parametros de entrada: String usuario, String concepto,String canal, String codigo 
	 * Return : lista de objetos o valor null
	 * */		
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerDatosLcredBuscaNivelUsuario(String usuario, String concepto, String canal, String codigo){
		 List<Object[]> listaFinal = null;
		try{
			List<Object[]> lista = 	(List<Object[]>)
					entityManager.createNativeQuery("EXEC [SCORING_PRODUCION].dbo.lcred_busca_nivel_usuario " +
					" @usuario ='"+ usuario  +"'," +
					" @concepto ='"+concepto +"'," +
					" @canal ='" + canal +"'," +
					" @codigo ='" + codigo +"'")
					.getResultList();
			
			if(lista != null && lista.size() > 0){
				listaFinal = lista;
			}else{
				listaFinal = null;
			}

		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al sacar los datos . #0", e.getMessage());
		}
		return listaFinal;
	}
	
	/*
	 * Parametros de entrada: codigo de la solicitud. 
	 * Return : verdadero o un falso ;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<UsuarioIngresoDTO>  getSacarListaUsuarios(){
		List<UsuarioIngresoDTO> lista = null;
		UsuarioIngresoDTO usuario = null;
		 try{
			 List<Object[]> listaObjetos = (List<Object[]>)entityManager
					 .createNativeQuery(" SELECT [Usuario]" +
					 					" ,[Nombre]" +
					 					" ,[correo]" +
					 					" ,[cargo]" +
					 					" ,[des_cargo] " +
					 					" ,[nivel_cargo]" +
					 					" ,[nivel_siguiente] " +
					 					" ,[zona] " +
					 					" ,[sucursal]" +
					 					" ,[negocio]" +
					 					" ,[concepto]" +
					 					" ,[administrador] " +
					 					" ,[envio_autom_otras_solc] " +
					 					"  FROM [SCORING_PRODUCION].[dbo].[lcred_v_nivel_usuarios_enc]" +
					 					"  ORDER BY [Usuario]")
					 .getResultList();
			   if(listaObjetos != null && listaObjetos.size() > 0){
				   lista = new ArrayList<UsuarioIngresoDTO>(0);
				   for(Object[] obj : listaObjetos){
					   usuario = new UsuarioIngresoDTO();
					   if(obj != null && obj[0]!=null){
						   usuario.setUsuario(obj[0].toString()); 
					   }else{
						   usuario.setUsuario(null);
					   }
					   if(obj != null && obj[1]!=null){
						   usuario.setNombre(obj[1].toString());
					   }else{
						   usuario.setNombre(null);
					   }
					   if(obj != null && obj[2]!=null){
						   usuario.setCorreo(obj[2].toString());
					   }else{
						   usuario.setCorreo(null);
					   }
					   
					   if(obj != null && obj[3]!=null){
						   usuario.setCargo(obj[3].toString()); 
					   }else{
						   usuario.setCargo(null); 
					   }
					   
					   if(obj != null && obj[4]!=null){
						   usuario.setDescripcionCargo(obj[4].toString()); 
					   }else{
						   usuario.setDescripcionCargo(null);  
					   }
					   
					   if(obj != null && obj[5]!=null){
						   usuario.setNivelCargo(obj[5].toString()); 
					   }else{
						   usuario.setNivelCargo(null);  
					   }
					   
					   if(obj != null && obj[6]!=null){
						   usuario.setNivelSiguiente(obj[6].toString());
					   }else{
						   usuario.setNivelSiguiente(null);
					   }
					   
					   if(obj != null && obj[7]!=null){
						   usuario.setZona(obj[7].toString());
					   }else{
						   usuario.setZona(null);
					   }
					   
					   if(obj != null && obj[8]!=null){
						   usuario.setSucursal(obj[8].toString());
					   }else{
						   usuario.setSucursal(null);
					   }
					   
					   if(obj != null && obj[9]!=null){
						   usuario.setNegocio(obj[9].toString());
					   }else{
						   usuario.setNegocio(null); 
					   }
					   
					   if(obj != null && obj[10]!=null){
						   usuario.setConcepto(obj[10].toString()); 
					   }else{
						   usuario.setConcepto(null);  
					   }
					   
					   if(obj != null && obj[11]!=null){
						   usuario.setAdminstrador(obj[11].toString());
					   }else{
						   usuario.setAdminstrador(null); 
					   }
					   
					   if(obj != null && obj[12]!=null){
						   usuario.setEnvio(obj[12].toString());
					   }else{
						   usuario.setEnvio(null); 
					   }

					   lista.add(usuario);  
				   }
				 log.debug("Existen registro de usuarios", lista.size());
			   }
		}catch (Exception e) {
			log.debug("no existe datos de los usuarios. #0",e.getMessage() );
		}
		return lista;
	}	

	/*
	 * Parametros de entrada: persist de UsuarioCargo. 
	 * Return : verdadero o un falso ;
	 * */		
	public  UsuarioCargo  persistUsuarioCargo(UsuarioCargo usuarioCargo){
		try{
			entityManager.persist(usuarioCargo);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de UsuarioCargo. #0", e.getMessage());
			return null;
		}
		return usuarioCargo;
	}	
	

	/*
	 * Parametros de entrada: idusername. 
	 * Return : objeto de UsuarioCargo;
	 * */		
	public  UsuarioCargo  getUsuarioCargo(Long idPersonal){
		UsuarioCargo nuevo = null;
	 	try{
	 		nuevo = (UsuarioCargo)entityManager
					 .createQuery("Select uc from UsuarioCargo uc where uc.usuario.idPersonal=:idPersonal")
					 .setParameter("idPersonal", idPersonal).getSingleResult();
		 	
	 		if(nuevo != null){
		 		log.debug("descripcion de cargo #0",nuevo.getCargo().getDesCargo());
		 	}
	 	}catch (Exception e) {
			log.debug("no existe datos de UsuarioCargo. #0", e.getMessage());
		}	
	 	return nuevo;
	}
	/*
	 * Parametros de entrada: idusername. 
	 * Return : objeto de UsuarioCargo;
	 * */		
	public  UsuarioCargo  getUsuarioCargoForUsername(String user){
		UsuarioCargo nuevo = null;
	 	try{
	 		nuevo = (UsuarioCargo)entityManager
					 .createQuery("Select uc from UsuarioCargo uc where uc.usuario.alias=:user")
					 .setParameter("user", user).getSingleResult();
		 	
	 		if(nuevo != null){
	 			log.debug("descripcion de cargo #0",nuevo.getCargo().getDesCargo());
		 	}
	 	}catch (Exception e) {
			log.debug("no existe datos de UsuarioCargo. #0", e.getMessage());
		}	
	 	return nuevo;
	}
	/*
	 * Parametros de entrada: idusername. 
	 * Return : objeto de UsuarioCargo;
	 * */		
	public  UsuarioCargo  getUsuarioCargoModificable(Long idPersonal){
		UsuarioCargo nuevoFinal = new UsuarioCargo();
		UsuarioCargo nuevo = null;
	 	try{
	 		nuevo = (UsuarioCargo)entityManager
					 .createQuery("Select uc from UsuarioCargo uc where uc.usuario.idPersonal=:idPersonal")
					 .setParameter("idPersonal", idPersonal).getSingleResult();
		 	
	 		if(nuevo != null){
	 			nuevoFinal.setIdUsuarioCargo(nuevo.getIdUsuarioCargo());
	 			nuevoFinal.setAdministrador(false);
	 			nuevoFinal.setCargo(nuevo.getCargo());
	 			nuevoFinal.setConcepto(nuevo.getConcepto());
	 			nuevoFinal.setEnvioAutomatico(nuevo.getEnvioAutomatico());
	 			nuevoFinal.setNegocio(nuevo.getNegocio());
	 			nuevoFinal.setSucursal(nuevo.getSucursal());
	 			nuevoFinal.setUsuario(nuevo.getUsuario());
	 			nuevoFinal.setZona(nuevo.getZona());
		 		log.debug("descripcion de cargo #0",nuevoFinal.getCargo().getDesCargo());
		 		nuevo = null;
		 	}
	 	}catch (Exception e) {
			log.debug("no existe datos de UsuarioCargo. #0", e.getMessage());
		}	
	 	return nuevoFinal;
	}
	
	
	
	
	/*
	 * Parametros de entrada: Usuariosegur. 
	 * Return : objeto de UsuarioCargo;
	 * */		
	@SuppressWarnings("unchecked")
	public  Usuariosegur  getUsuarioUsuarioSegurForUsername(String user, String correo){
		Usuariosegur objeto = null;
		List<Usuariosegur> lista = null;
	 	try{
	 		lista = (List<Usuariosegur>)entityManager
					 .createQuery("Select uc from Usuariosegur uc where uc.alias=:user")
					 .setParameter("user", user).getResultList();
		 	
	 		if(lista != null){
	 			for(Usuariosegur obj : lista){
	 				if((obj.getCorreo().trim()).equals(correo.trim())){
	 					objeto = obj;
	 					break;
	 				}
	 			}
		 	}
	 	}catch (Exception e) {
			log.debug("no existe datos de UsuarioCargo. #0", e.getMessage());
		}	
	 	return objeto;
	}	

	
	/*
	 * Parametros de entrada: Usuariosegur. 
	 * Return : objeto de UsuarioCargo;
	 * */		
	public  boolean  getUsuarioCargoEnvioAutomatico(Long idPersonal){
		boolean  objeto = false;
		UsuarioCargo usuarioCargo = null;
	 	try{
	 		usuarioCargo = (UsuarioCargo)entityManager
					 .createQuery("Select uc from UsuarioCargo uc where uc.usuario.idPersonal=:idPersonal")
					 .setParameter("idPersonal", idPersonal).getResultList();
		 	
	 		if(usuarioCargo != null){
	 			log.debug(usuarioCargo.getEnvioAutomatico());
	 			if(usuarioCargo.getEnvioAutomatico() == null || usuarioCargo.getEnvioAutomatico().booleanValue() == true){
	 				return true;
	 			}else{
	 				return false;
	 			}
	 		}
	 	}catch (Exception e) {
			log.debug("no existe datos de UsuarioCargo. #0", e.getMessage());
			return false;
	 	}	
	 	return objeto;
	}
	
	
	/*
	 * Parametros de entrada: Usuariosegur. 
	 * Return : objeto de UsuarioCargo;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<UsuarioCorreoDTO>  getListaUsuariosSegur(){
		List<UsuarioCorreoDTO> listaUsuarios = new ArrayList<UsuarioCorreoDTO>(0);
		
		List<Usuariosegur> lista = null;
	 	try{
	 		lista = (List<Usuariosegur>)entityManager
					 .createQuery("Select uc from Usuariosegur uc where uc.obligatorio=1 and uc.eliminado <> 1 ")
					 .getResultList();
		 	
	 		if(lista != null){
	 			for(Usuariosegur obj : lista){
	 				UsuarioCorreoDTO obj1 = new UsuarioCorreoDTO();
					obj1.setStatus(false);
					obj1.setCorrelativo(0);
					obj1.setNombreUsuario(obj.getNombre());
					obj1.setAlias(obj.getAlias());
					obj1.setCorreoElectronico(obj.getCorreo());
					Usuarios usuario = entityManager.find(Usuarios.class, (obj.getAlias()).trim());
					obj1.setUsuario(usuario);
					listaUsuarios.add(obj1);
	 			}
		 	}
	 	}catch (Exception e) {
			log.debug("no existe datos de UsuarioCargo. #0", e.getMessage());
		}	
	 	return listaUsuarios;
	}	
	
	
	
	/*
	 * Parametros de entrada: Long idUsuarioCargo. 
	 * Return : List<CanalUsuarioCargo>;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<CanalUsuarioCargo>  getListaCanalUsuarioCargo(Long idUsuarioCargo){
		List<CanalUsuarioCargo> lista = null;
	 	try{
	 		lista = (List<CanalUsuarioCargo>)entityManager
					 .createQuery("Select cuc from CanalUsuarioCargo cuc where cuc.usuarioCargo.idUsuarioCargo=:idUsuarioCargo")
					 .setParameter("idUsuarioCargo", idUsuarioCargo).getResultList();
		 	
	 		if(lista != null){
		 		log.debug("lista canal usuario cargo: lista.size" + lista.size());
		 	}
	 	}catch (Exception e) {
			log.error("no existe datos de UsuarioCargo. #0", e.getMessage());
			lista = null;
		}	
	 	return lista;
	}
	

	
	/*
	 * Parametros de entrada: Long idCanalUsuarioCargo,List<PerfilFuncionCanal> listaFuncionCanals. 
	 * Return : List<PerfilFuncionCanal>;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<PerfilFuncionCanal>  getListaPerfilFuncionCanal(Long idCanalUsuarioCargo,List<PerfilFuncionCanal> listaFuncionCanals){
		if(listaFuncionCanals == null){
			listaFuncionCanals =  new ArrayList<PerfilFuncionCanal>(0);
		}
		List<PerfilFuncionCanal> lista = null;
	 	try{
	 		lista = (List<PerfilFuncionCanal>)entityManager
					 .createQuery("Select pfc from PerfilFuncionCanal pfc where pfc.canalUsuarioCargo.idCanalUsuarioCargo=:idCuc")
					 .setParameter("idCuc", idCanalUsuarioCargo).getResultList();
		 	
	 		if(lista != null){
		 		//log.debug(lista.size());
		 		for(PerfilFuncionCanal pfc: lista){
		 			if(!listaFuncionCanals.contains(pfc)){
		 				listaFuncionCanals.add(pfc);
		 			}
		 		}
		 	}
	 	}catch (Exception e) {
			log.debug("no existe datos de UsuarioCargo. #0", e.getMessage());
		}	
	 	return listaFuncionCanals;
	}

	
	
	/*
	 * Parametros de entrada: Long idCanalUsuarioCargo,List<PerfilFuncionCanal> listaFuncionCanals. 
	 * Return : List<PerfilFuncionCanal>;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<ZonaSucursalNegocioConcepto>  getListaZonaSucursalNegocioConcepto(Long idPerfilFuncionCanal,List<ZonaSucursalNegocioConcepto> listaZonaSucursalNegocioConceptos){
		if(listaZonaSucursalNegocioConceptos == null){
			listaZonaSucursalNegocioConceptos =  new ArrayList<ZonaSucursalNegocioConcepto>(0);
		}
		List<ZonaSucursalNegocioConcepto> lista = null;
	 	try{
	 		lista = (List<ZonaSucursalNegocioConcepto>)entityManager
					 .createQuery("Select zsnc from ZonaSucursalNegocioConcepto zsnc where zsnc.perfilFuncionCanal.idPerfilFuncionCanal=:idPfc")
					 .setParameter("idPfc", idPerfilFuncionCanal).getResultList();
		 	
	 		if(lista != null){
		 		log.debug(lista.size());
		 		for(ZonaSucursalNegocioConcepto pfc: lista){
		 			if(!listaZonaSucursalNegocioConceptos.contains(pfc)){
		 				listaZonaSucursalNegocioConceptos.add(pfc);
		 			}	
		 		}
		 	}
	 	}catch (Exception e) {
			log.debug("no existe datos de UsuarioCargo. #0", e.getMessage());
		}	
	 	return listaZonaSucursalNegocioConceptos;
	}	
	
	/*
	 * Parametros de entrada: Long[] lista de idNuevo. 
	 * Return : List<ZonaSucursalNegocioConcepto>;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<ZonaSucursalNegocioConcepto>  getListaZonaSucursalNegocioConceptoToArray(Long[] array){
		List<ZonaSucursalNegocioConcepto> lista = null;
	 	try{
	 		lista = (List<ZonaSucursalNegocioConcepto>)entityManager
					 .createQuery("Select zsnc from ZonaSucursalNegocioConcepto zsnc where zsnc.perfilFuncionCanal.idPerfilFuncionCanal in (:array)")
					 .setParameter("array", array).getResultList();
		 	
	 		if(lista != null){
		 		log.debug("getListaZonaSucursalNegocioConceptoToArray cantidad  #0", lista.size());
		 	}
	 	}catch (Exception e) {
			log.debug("no existe datos de UsuarioCargo. #0", e.getMessage());
		}	
	 	return lista;
	}		
	
	
	/*
	 * Parametros de entrada: persist de SolicitudAprobaRechaza. 
	 * Return : verdadero o un falso ;
	 * */		
	public  boolean  persistSolicitudAprobaRechazo(SolicitudAprobaRechaza objeto){
		try{
			entityManager.persist(objeto);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persister el objeto de SolicitudAprobaRechaza #0", e.getMessage());
			return false;
		}
		return true;
	}
	

	
	/*
	 * Parametros de entrada: Long idSolicitud. 
	 * Return : Lista de los solicitud de aprobaciones o rechazos;
	 * */		
	@SuppressWarnings("unchecked")
	public  List<SolicitudAprobaRechaza> getSolicitudAprobaRechazo(Long idSolicitud){
		List<SolicitudAprobaRechaza> lista = null;
		try{
			lista = (List<SolicitudAprobaRechaza>)entityManager.createQuery("" +
					"select sar from SolicitudAprobaRechaza sar where sar.idSolicitud=:solAux").setParameter("solAux", idSolicitud).getResultList();
		}catch (Exception e) {
			log.error("Error, al lista de los objetos solicitud de aprobaciones o rechazos #0", e.getMessage());
		}
		return lista;
	}
	
	
	/*
	 * Parametros de entrada: vacio. 
	 * Return : lista de estados.
	 * */		
	public LcredEstado obtenerEstado(String codigo){
		try{
			return (LcredEstado)entityManager.createQuery("select le from LcredEstado le where le.codEstado=:estadoAux")
					.setParameter("estadoAux", codigo)
					.getSingleResult();
		}catch (Exception e) {
			log.error("Error, al sacar el estado. #0", e.getMessage());
		}
		return null;
	}
	
	
	/*
	 * Parametros de entrada: codigo personal , codigo , funcion, opcion. 
	 * Return : verdadero o falso.
	 * */		
	@SuppressWarnings("unchecked")
	public Boolean obtenerHabilitacionUsuario(Long idLong, String codigo, int tipoCuenta, int funcion){
    	boolean valido = false; 
    	try{
    		List<Integer>  array = (List<Integer>)entityManager
		  			.createNativeQuery(" SELECT Count([id_system]) as cantidad " +
		  							  "  FROM [SCORING_PRODUCION].[dbo].[zona_sucursal_negocio_concepto] " +
		  							  "  where perfil_funcion_canal in  " +
									  "  (SELECT [id_canal_usuario_cargo] " +
									  "   FROM [SCORING_PRODUCION].[dbo].[perfil_funcion_canal]" +
									  "   where canal_usuario_cargo in " +
									  "   (SELECT [id_canal_usuario_cargo]" +
									  "    FROM [SCORING_PRODUCION].[dbo].[canal_usuario_cargo]" +
									  "    where usuario_cargo in " +
									  "    (SELECT [id_usuario_cargo]" +
									  "     FROM [SCORING_PRODUCION].[dbo].[usuario_cargo]" +
									  "      where usuario_username=:codigoUsuario " +
									  "    ) and tipo_cuenta=:tipoCuenta " +
									  "   ) and funcion=:funcion" +
									  "  ) and codigo=:codigo")
		  			           .setParameter("codigoUsuario", idLong)
		  			           .setParameter("codigo", codigo)
		  			           .setParameter("tipoCuenta", tipoCuenta)
		  			           .setParameter("funcion", funcion)
		  			           .getResultList();
		  	
		  	if(array != null && array.size() > 0){
		  		for(Integer obj :array){
		  			if(obj!= null && obj.intValue() != 0){
		  				valido = true;
		  			}
		  		}
		  	}
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar los datos del usuario de la base de datos segur #0 ", e.getMessage());
		}
    	return valido;
    }

	/*
	 * Parametros de entrada: codigo personal , codigo , funcion, opcion. 
	 * Return : verdadero o falso.
	 * */		
	@SuppressWarnings("unchecked")
	public Boolean obtenerHabilitacionUsuarioNegocio(Long idLong, String codigo, int tipoCuenta, int funcion){
    	boolean valido = false; 
    	try{
    		List<Integer>  array = (List<Integer>)entityManager
		  			.createNativeQuery(" SELECT Count([id_system]) as cantidad " +
		  							  "  FROM [SCORING_PRODUCION].[dbo].[zona_sucursal_negocio_concepto] " +
		  							  "  where perfil_funcion_canal in  " +
									  "  (SELECT [id_canal_usuario_cargo] " +
									  "   FROM [SCORING_PRODUCION].[dbo].[perfil_funcion_canal]" +
									  "   where canal_usuario_cargo in " +
									  "   (SELECT [id_canal_usuario_cargo]" +
									  "    FROM [SCORING_PRODUCION].[dbo].[canal_usuario_cargo]" +
									  "    where usuario_cargo in " +
									  "    (SELECT [id_usuario_cargo]" +
									  "     FROM [SCORING_PRODUCION].[dbo].[usuario_cargo]" +
									  "      where usuario_username=:codigoUsuario " +
									  "    ) and tipo_cuenta=:tipoCuenta " +
									  "   ) and funcion=:funcion " +
									  "  ) and codigo=:codigo and funcion = 2 ")
		  			           .setParameter("codigoUsuario", idLong)
		  			           .setParameter("codigo", codigo)
		  			           .setParameter("tipoCuenta", tipoCuenta)
		  			           .setParameter("funcion", funcion)
		  			           .getResultList();
		  	
		  	if(array != null && array.size() > 0){
		  		for(Integer obj :array){
		  			if(obj!= null && obj.intValue() != 0){
		  				valido = true;
		  			}
		  		}
		  	}
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar los datos del usuario del perfil de usuario en base a los negocios #0 ", e.getMessage());
		}
    	return valido;
    }	
	
	/*
	 * Parametros de entrada: codigo personal , codigo sucursal , tipoCuenta. 
	 * Return : verdadero o falso.
	 * */		
	@SuppressWarnings("unchecked")
	public Boolean obtenerHabilitacionPorSucursal(Long idLong, String codigo, int tipoCuenta){
    	boolean valido = false; 
    	try{
    		List<Integer>  array = (List<Integer>)entityManager
		  			.createNativeQuery(" SELECT Count([id_system]) as cantidad " +
		  							  "  FROM [SCORING_PRODUCION].[dbo].[zona_sucursal_negocio_concepto] " +
		  							  "  where perfil_funcion_canal in  " +
									  "  (SELECT [id_canal_usuario_cargo] " +
									  "   FROM [SCORING_PRODUCION].[dbo].[perfil_funcion_canal]" +
									  "   where canal_usuario_cargo in " +
									  "   (SELECT [id_canal_usuario_cargo]" +
									  "    FROM [SCORING_PRODUCION].[dbo].[canal_usuario_cargo]" +
									  "    where usuario_cargo in " +
									  "    (SELECT [id_usuario_cargo]" +
									  "     FROM [SCORING_PRODUCION].[dbo].[usuario_cargo]" +
									  "      where usuario_username=:codigoUsuario " +
									  "    ) and tipo_cuenta=:tipoCuenta " +
									  "   ) and funcion in (1,3,4) " +
									  "  ) and codigo=:codigo and funcion=1;")
		  			           .setParameter("codigoUsuario", idLong)
		  			           .setParameter("codigo", codigo)
		  			           .setParameter("tipoCuenta", tipoCuenta)
		  			           .getResultList();
		  	
		  	if(array != null && array.size() > 0){
		  		for(Integer obj :array){
		  			if(obj!= null && obj.intValue() != 0){
		  				valido = true;
		  			}
		  		}
		  	}
		  	log.debug("************** codigo usuario #0", idLong);
		  	log.debug("************** codigo sucursal  #0", codigo);
		  	log.debug("************** validacion #2", valido);
    	}catch (Exception e) {
    		log.error("Error, al validar si el usuario esta habiliotacion del usuario #0 ", e.getMessage());
		}
    	return valido;
    }	
	
	
	
	/*
	 * Parametros de entrada: codigo personal , codigo , funcion, opcion. 
	 * Return : lista de codigo de negocios asociado a la solicitud.
	 * */		
	@SuppressWarnings("unchecked")
	public List<String> obtenerNegociosAsociadoToSolicitud(Long idSolicitud){
		List<String> listaValida = null; 
    	try{
    		List<Object>  lista = (List<Object>)entityManager
		  			.createNativeQuery(" SELECT [Negocio] as codigo_negocio " +
		  							  "  FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido_negocio] " +
		  							  "  where [cotizacion_pedido_id] in (  " +
									  "    SELECT [system_id]  " +
									  "    FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido] " +
									  "    where [cot_ped_cab_id] in ( " +
									  "     SELECT [system_id] " +
									  "     FROM [SCORING_PRODUCION].[dbo].[cotizacion_pedido_cabecera]" +
									  "     where solicitud_id=:solicitudAux " +
									  "    )" +
									  "  ) ")
		           .setParameter("solicitudAux", idSolicitud)
		           .getResultList();
		  	
		  	if(lista != null && lista.size() > 0){
		  		listaValida = new ArrayList<String>(0);
		  		for(Object obj :lista){
		  			if(obj != null ){
		  				listaValida.add(obj.toString());
		  			}
		  		}
		  	}
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar los datos del usuario del perfil de usuario en base a los negocios #0 ", e.getMessage());
		}
    	return listaValida;
    }	

	/*
	 * Parametros de entrada: numero de la solicitud . 
	 * Return : lista de codigo de negocios asociado a la solicitud.
	 * */		
	public List<String> obtenerNegociosAsociadoToSolicitudLineaCredito(Long idSolicitud, List<ConceptosNegocio> conceptosNegociosSessions){
		List<String> listaValida = null; 
		String dpConceptosInvoluc= null; 
    	try{
		    try{ 
		    	dpConceptosInvoluc = (String)entityManager
						 .createQuery("Select lc.dpConceptosInvoluc from LcredSolicitudLcredito lc where lc.numSolicitud=:solAux")
						 .setParameter("solAux", idSolicitud).getSingleResult();
			 }catch (Exception e) {
				log.debug("no existe datos de linea credito antiguas.");
			 }
    		 
		     if(dpConceptosInvoluc != null){
		    	 listaValida = new ArrayList<String>(0);
				 String[] arrayNombreNegocio = dpConceptosInvoluc.split("-");
				 if(arrayNombreNegocio != null){
					for(String descripcion : arrayNombreNegocio){
						String codigo = getObtenerCodigoNegocios(descripcion, conceptosNegociosSessions);
						if(codigo != null){
							//log.debug("negocio #0", codigo +"-"+ descripcion);
							if(listaValida != null){
								if(!listaValida.contains(codigo)){
									listaValida.add(codigo);
								}
							}else{
								listaValida = new ArrayList<String>(0);
								listaValida.add(codigo);
							}
						}
					}
				 }
		     }
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar los datos del usuario del perfil de usuario en base a los negocios #0 ", e.getMessage());
		}
    	return listaValida;
    }		
	
	public String getObtenerCodigoNegocios(String descripcion, List<ConceptosNegocio> conceptosNegociosSessions){
		String codigo= null;
		if(conceptosNegociosSessions != null){
			for(ConceptosNegocio cn : conceptosNegociosSessions){
				if(descripcion.equals("")){
					
				}
				//log.debug("descripcion : #0, desnegocio #1",descripcion, cn.getDescripcion().trim()); 
				if((cn.getDescripcion().trim()).equals(descripcion.trim())){
					codigo = cn.getNegocio().trim();
					break;
				}
			}
		}
		return codigo;	
	}
	

	
	/*
	 * Parametros de entrada: codigo personal
	 * Return : Lista de perfiles asignados.
	 * */		
	@SuppressWarnings("unchecked")
	public List<Integer> obtenerPerfilesDelUsuario(Long idLong){
		List<Integer>  array = null;
    	try{
    		 array = (List<Integer>)entityManager
		  			.createNativeQuery("  SELECT distinct(funcion) " +
									  "   FROM [SCORING_PRODUCION].[dbo].[perfil_funcion_canal]" +
									  "   where canal_usuario_cargo in " +
									  "   (SELECT [id_canal_usuario_cargo]" +
									  "    FROM [SCORING_PRODUCION].[dbo].[canal_usuario_cargo]" +
									  "    where usuario_cargo in " +
									  "    (SELECT [id_usuario_cargo]" +
									  "     FROM [SCORING_PRODUCION].[dbo].[usuario_cargo]" +
									  "      where usuario_username=:codigoUsuario " +
									  "    )  " +
									  "   ) and funcion not in(0) " +
									  "  ")
		  			           .setParameter("codigoUsuario", idLong)
		  			           .getResultList();
		  	if(array != null && array.size() > 0){
		  		log.debug("informacion : #0", array.size());
		  	}
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar los datos del usuario de la base de datos segur #0 ", e.getMessage());
		}
    	return array;
    }
	
	/*
	 * Parametros de entrada: codigo personal
	 * Return : verdadero o falso.
	 * */		
	public boolean obtenerAsignacionCanalVentaDelUsuario(Long idLong, Integer canalVentaAux){
		boolean  existe = false;
    	try{
    		 Integer canalVenta = (Integer)entityManager
		  			.createNativeQuery("  SELECT distinct(tipo_cuenta) " +
									  "   FROM [SCORING_PRODUCION].[dbo].[canal_usuario_cargo]" +
									  "   WHERE usuario_cargo in  " +
									  "   (SELECT [id_usuario_cargo]" +
									  "     FROM [SCORING_PRODUCION].[dbo].[usuario_cargo] " +
									  "     WHERE usuario_username=:idPersonal " +
									  "     ) and tipo_cuenta=:tipoCanalVenta")
		  			           .setParameter("idPersonal", idLong)
		  			           .setParameter("tipoCanalVenta", canalVentaAux)
		  			           .getSingleResult();
		  	
    		if(canalVenta != null){
		  		log.debug("informacion : #0", canalVenta.intValue());
		  		existe = true;
		  	}
    	}catch (Exception e) {
    		log.error("Error, al sacar los datos del usuario de la base de datos segur #0 ", e.getMessage());
		}
    	return existe;
    }	
	
	
	/*
	 * Parametros de entrada: codigo de ipPersonal. 
	 * Return : lista de funciones.
	 * */		
	@SuppressWarnings("unchecked")
	public List<FuncionesType> obtenerFuncionesUsuario(Long idPersonal){
		List<FuncionesType> lista = null;
		try{
			lista =  (List<FuncionesType>)entityManager
					 .createQuery("select distinct(pfc.funcion) " +
					 		"      from PerfilFuncionCanal pfc " +
					 		"      where pfc.canalUsuarioCargo.idCanalUsuarioCargo in " +
					 		"    (" +
					 		"        select cuc.idCanalUsuarioCargo " +
					 		"        from  CanalUsuarioCargo cuc " +
					 		"        where cuc.usuarioCargo.idUsuarioCargo in" +
					 		"        (" +
					 		"           select uc.idUsuarioCargo " +
					 		"           from UsuarioCargo uc" +
					 		"           where uc.usuario.idPersonal=:idCodigo" +
					 		"         )" +
					 		"    )")
					.setParameter("idCodigo", idPersonal)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos perfiles de usuarios. #0", e.getMessage());
		}
		return lista;
	}	

	
	
	
	/*
	 * Parametros de entrada: codigo personal , int tipo_cuenta. 
	 * Return : lista de montos verdadero o falso.
	 * */		
	@SuppressWarnings("unchecked")
	public List<Long[]> obtenerMontosSegunIdPersonalAndTipoCuenta(Long idPersonal, int tipoCuenta){
		List<Long[]>  array = null;
    	try{
    		 array = (List<Long[]>)entityManager
		  			.createNativeQuery(" SELECT MAX([monto_minimo]) as minimo, " +
		  							   "        MAX([monto_maximo]) as maximo, " +
		  							   "        MAX([monto_minino_compartido]) as minimo_compartida," +
		  							   "        MAX([monto_maximo_compartido]) as maximo_compartida" +
		  							   " FROM [SCORING_PRODUCION].[dbo].[perfil_funcion_canal]" +
		  							   " WHERE canal_usuario_cargo in " +
		  							   "       (SELECT [id_canal_usuario_cargo]" +
		  							   "        FROM [SCORING_PRODUCION].[dbo].[canal_usuario_cargo]" +
		  							   "        WHERE usuario_cargo in" +
		  							   "              (SELECT [id_usuario_cargo] " +
		  							   "               FROM [SCORING_PRODUCION].[dbo].[usuario_cargo] " +
		  							   "               WHERE usuario_username=:idPersonal " +
		  							   "              )" +
		  							   "        AND tipo_cuenta=:tipoCuenta " +
		  							   "        )")
		  			           .setParameter("idPersonal", idPersonal)
		  			           .setParameter("tipoCuenta", tipoCuenta)
		  			           .getResultList();
		  	if(array != null && array.size() > 0){
		  		log.debug("informacion : #0", array.size());
		  	}
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar los datos del usuario de la base de datos segur #0 ", e.getMessage());
		}
    	return array;
    }
	
	
	
	
	
	/*
	 * Parametros de entrada: codigo de ipPersonal. 
	 * Return : lista de funciones.
	 * */		
	
	@SuppressWarnings("unchecked")
	public List<SolicitudConceptosNegocioLC> getListarSolicitudConceptosNegocioLC(Long idSolicitud){
		List<SolicitudConceptosNegocioLC> lista = null;
		try{
			lista =  (List<SolicitudConceptosNegocioLC>)entityManager
					 .createQuery("select  scn " +
					 		"      from SolicitudConceptosNegocioLC scn " +
					 		"      where scn.solicitud.id.numSolicitud=:solicitudAux")
					.setParameter("solicitudAux", idSolicitud)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getListarSolicitudConceptosNegocioLC. #0", e.getMessage());
		}
		return lista;
	}		
	
	/*
	 * Parametros de entrada: objeto UF. 
	 * Return : boolean de verdadero o falso.
	 * */		
	public boolean persistUfs(Ufs ufs){
		boolean exito = false;
		try{
			entityManager.persist(ufs);
			entityManager.flush();
			entityManager.refresh(ufs);
		}catch (Exception e) {
			log.error("Error, persist Uf. #0", e.getMessage());
		}
		return exito;
	}	
 	/*
 	 * Parametros de entrada: fecha del dia  
 	 * Return : devuelve el objeto de la uf;
 	 * */	
 	public  Integer  getIdMaximo(){
 		Integer numero = null;
 		try{
 			 numero =  (Integer)entityManager.
 					 		createNativeQuery("SELECT count([id_uf]) FROM [SCORING_PRODUCION].[dbo].[uf] ").getSingleResult();
 			
 			if(numero != null ){
 				numero++;
 				return numero;
 			}
 			
 		}catch (Exception e) {
 			log.error("Error, al sacar la UF del dia . #0", e.getMessage());
 			return numero;
 		}
 		return numero;
 	}	
	
	/*
	 * Parametros de entrada: merger de Uf. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  mergerUf(Ufs ufs){
		try{
			entityManager.merge(ufs);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto uf. #0", e.getMessage());
			return false;
		}
		return true;
	}
	
	/*
	 * Parametros de entrada: Long idSolicitud. 
	 * Return : lista de CotizacionPedidoCabecera.
	 * */		
	@SuppressWarnings("unchecked")
	public List<CotizacionPedidoCabecera> getCotizacionPedidoCobecera(Long idSolicitud){
		List<CotizacionPedidoCabecera> lista = null;
		try{
			lista =  (List<CotizacionPedidoCabecera>)entityManager
					 .createQuery("select  cpc " +
					 		"      from CotizacionPedidoCabecera cpc " +
					 		"      where cpc.solicitud.id.numSolicitud=:solicitudAux")
					.setParameter("solicitudAux", idSolicitud)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getCotizacionPedidoCobecera. #0", e.getMessage());
		}
		return lista;
	}		
	
	/*
	 * Parametros de entrada: codigo de idCotizacionPedidoCabecera. 
	 * Return : lista de CotizacionPedido.
	 * */		
	@SuppressWarnings("unchecked")
	public List<CotizacionPedido> getCotizacionPedido(Long idCotizacionPedidoCabecera){
		List<CotizacionPedido> lista = null;
		try{
			lista =  (List<CotizacionPedido>)entityManager
					 .createQuery("select  cp " +
					 		"      from CotizacionPedido cp " +
					 		"      where cp.cotizacionPedidoCabecera.systemId=:idAux")
					.setParameter("idAux", idCotizacionPedidoCabecera)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getCotizacionPedido. #0", e.getMessage());
		}
		return lista;
	}		
	

	/*
	 * Parametros de entrada: codigo de idCotizacionPedido. 
	 * Return : lista de CotizacionPedidoNegocio.
	 * */		
	@SuppressWarnings("unchecked")
	public List<CotizacionPedidoNegocio> getCotizacionPedidoNegocio(Long idCotizacionPedido){
		List<CotizacionPedidoNegocio> lista = null;
		try{
			lista =  (List<CotizacionPedidoNegocio>)entityManager
					 .createQuery("select  cpn " +
					 		"      from CotizacionPedidoNegocio cpn " +
					 		"      where cpn.cotizacionPedido.systemId=:idAux")
					.setParameter("idAux", idCotizacionPedido)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getCotizacionPedido. #0", e.getMessage());
		}
		return lista;
	}		

	/*
	 * Parametros de entrada: codigo de idCotizacionPedidoNegocio. 
	 * Return : lista de CotizacionPedidoConcepto.
	 * */		
	@SuppressWarnings("unchecked")
	public List<CotizacionPedidoConcepto> getCotizacionPedidoConcepto(Long idCotizacionPedidoNegocio){
		List<CotizacionPedidoConcepto> lista = null;
		try{
			lista =  (List<CotizacionPedidoConcepto>)entityManager
					 .createQuery("select  cpc " +
					 		"      from CotizacionPedidoConcepto cpc " +
					 		"      where cpc.cotizacionPedidoNegocio.systemId=:idAux")
					.setParameter("idAux", idCotizacionPedidoNegocio)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getCotizacionPedidoConcepto. #0", e.getMessage());
		}
		return lista;
	}		
		
	
	/*
	 * Parametros de entrada: codigo de idCotizacionPedidoNegocio. 
	 * Return : lista de CotizacionPedidoConcepto.
	 * */		
	@SuppressWarnings("unchecked")
	public List<DetalleCp> getDetalleCp(Long idCotizacionPedidoConcepto){
		List<DetalleCp> lista = null;
		try{
			lista =  (List<DetalleCp>)entityManager
					 .createQuery("select  dc " +
					 		"      from DetalleCp dc " +
					 		"      where dc.cotizacionPedidoConcepto.systemId=:idAux")
					.setParameter("idAux", idCotizacionPedidoConcepto)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getDetalleCp. #0", e.getMessage());
		}
		return lista;
	}		
			
	/*
	 * Parametros de entrada: persist de SolicitudUsuarioDerivada. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  persitSolicitudUsuarioDerivacion(SolicitudUsuarioDerivada sud){
		try{
			entityManager.persist(sud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto SolicitudUsuarioDerivada. #0", e.getMessage());
			return false;
		}
		return true;
	}

	/*
	 * Parametros de entrada:vacio. 
	 * Return : retorna el numero de indice principal system_id ;
	 * */
	@SuppressWarnings("unchecked")
	public  Long  getSolicitudUsuarioDerivacionMaximo(){
		List<SolicitudUsuarioDerivada> lista = null;
		try{
			lista =  (List<SolicitudUsuarioDerivada>)entityManager
					 .createQuery("select  dc " +
					 		"      from SolicitudUsuarioDerivada dc order by dc.systemId desc ")
					.getResultList();
			if(lista != null && lista.size() > 0){
				SolicitudUsuarioDerivada obj = lista.get(0);
				if(obj != null){
					return  new Long(obj.getSystemId()+1);
				}else{
					return new Long(1);
				}
			}else{
				return new Long(1);
			}
		}catch (Exception e) {
			log.error("Error, los datos getDetalleCp. #0", e.getMessage());
		}
		return null;
	}

	/*
	 * Parametros de entrada: codigo la solicitud. 
	 * Return : retorna el numero de cantidad usuarios a derivar  ;
	 * */
	public  Long  getCantidadSolicitudUsuarioDerivacion(Long idSol){
		Long cantidad = null;
		try{
			cantidad =  (Long)entityManager
					 .createQuery("select  COUNT(dc.systemId) " +
					 		"      from SolicitudUsuarioDerivada dc where dc.idSolicitud=:idSol ")
					 .setParameter("idSol", idSol)		
					.getSingleResult();
			if(cantidad != null){
				return new Long(cantidad);
			}else{
				return new Long(0);
			}
		}catch (Exception e) {
			log.error("Error, los datos getCantidadSolicitudUsuarioDerivacion. #0", e.getMessage());
			cantidad = new Long(0);
		}
		return cantidad;
	}
	
	/*
	 * Parametros de entrada: codigo de solicitud . 
	 * Return : lista de usuarios derivados;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<SolicitudUsuarioDerivada>  getSolicitudUsuarioDerivacionForSolicitud(Long id,String estado){
		
		try{
			return  (List<SolicitudUsuarioDerivada>)entityManager
					 .createQuery("select  dc " +
					 		"      from SolicitudUsuarioDerivada dc " +
					 		"      where dc.idSolicitud=:idSol " +
					 		"      and dc.codEstadoDerivada=:estado")
					 .setParameter("idSol", id)
					 .setParameter("estado", estado)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getDetalleCp. #0", e.getMessage());
		}
		return null;
	}
	/*
	 * Parametros de entrada: codigo de solicitud. 
	 * Return : lista de usuarios derivados;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<SolicitudUsuarioDerivada>  getSolicitudUsuarioDerivacionForSolicitudMasEjecutivo(Long id){
		
		try{
			return  (List<SolicitudUsuarioDerivada>)entityManager
					 .createQuery("select  dc " +
					 		"      from SolicitudUsuarioDerivada dc " +
					 		"      where dc.idSolicitud=:idSol " +
					 		"      and dc.codEstadoDerivada in ('SA','AE','RE')")
					 .setParameter("idSol", id)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getDetalleCp. #0", e.getMessage());
		}
		return null;
	}
	
	/*
	 * Parametros de entrada: codigo de solicitud . 
	 * Return : lista de usuarios derivados;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<SolicitudUsuarioDerivada>  getSolicitudUsuarioDerivacionForSolicitud(Long id){
		
		try{
			return  (List<SolicitudUsuarioDerivada>)entityManager
					 .createQuery("select  dc " +
					 		"      from SolicitudUsuarioDerivada dc " +
					 		"      where dc.idSolicitud=:idSol " )
					 .setParameter("idSol", id)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getDetalleCp. #0", e.getMessage());
		}
		return null;
	}

	/*
	 * Parametros de entrada: codigo de solicitud . 
	 * Return : lista de usuarios derivados;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<SolicitudUsuarioDerivada>  getSolicitudUsuarioDerivacionForSolicitudEstado(Long id, String estado){
		
		try{
			return  (List<SolicitudUsuarioDerivada>)entityManager
					 .createQuery("select  dc " +
					 		"      from SolicitudUsuarioDerivada dc " +
					 		"      where dc.idSolicitud=:idSol " +
					 		"      and dc.codEstadoDerivada=:estado " )
					 .setParameter("idSol", id)
					 .setParameter("estado", estado)
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getDetalleCp. #0", e.getMessage());
		}
		return null;
	}
	
	/*
	 * Parametros de entrada: codigo de solicitud y codigo de personal. 
	 * Return : lista de usuarios derivados;
	 * */	
	public SolicitudUsuarioDerivada getSolicitudUsuarioDerivacionForSolicitudIpPersonal(Long idSol, Long idPersonal){
		SolicitudUsuarioDerivada suc = null;
		try{
			suc =  (SolicitudUsuarioDerivada)entityManager
					 .createQuery("select  dc " +
					 		"      from SolicitudUsuarioDerivada dc " +
					 		"      where dc.idSolicitud=:idSol " +
					 		"      and dc.usuario.idPersonal=:idPersonal" +
					 		"      and dc.confirmacion is null")
					 .setParameter("idSol", idSol)		
					 .setParameter("idPersonal", idPersonal)
					.getSingleResult();
			if(suc != null){
				log.debug("el datos rescatado #0", suc.getUsuario().getAlias());
			}
			
		}catch (Exception e) {
			log.error("Error, los datos getDetalleCp. #0", e.getMessage());
		}
		return suc;
	}	

	/*
	 * Parametros de entrada: codigo de solicitud y codigo de personal. 
	 * Return : lista de usuarios derivados;
	 * */	
	public SolicitudUsuarioDerivada getSolicitudUsuarioDerivacionForSolicitudIpPersonal(Long idSol, Long idPersonal, String estadoAux){
		SolicitudUsuarioDerivada suc = null;
		try{
			suc =  (SolicitudUsuarioDerivada)entityManager
					 .createQuery("select  dc " +
					 		"      from SolicitudUsuarioDerivada dc " +
					 		"      where dc.idSolicitud=:idSol " +
					 		"      and dc.usuario.idPersonal=:idPersonal" +
					 		"      and dc.confirmacion is null" +
					 		"      and dc.codEstadoDerivada=:estado")
					 .setParameter("idSol", idSol)		
					 .setParameter("idPersonal", idPersonal)
					 .setParameter("estado", estadoAux)
					.getSingleResult();
			if(suc != null){
				log.debug("el datos rescatado #0", suc.getUsuario().getAlias());
			}
			
		}catch (Exception e) {
			log.error("Error, los datos getDetalleCp. #0", e.getMessage());
		}
		return suc;
	}		
	/*
	 * Parametros de entrada: merger de SolicitudUsuarioDerivada. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  mergerSolicitudUsuarioDerivacion(SolicitudUsuarioDerivada sud){
		try{
			entityManager.merge(sud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto mergerSolicitudUsuarioDerivacion. #0", e.getMessage());
			return false;
		}
		return true;
	}
	
	/*
	 * Parametros de entrada: codigo la solicitud. 
	 * Return : retorna el numero de cantidad usuarios que an intervenido la solcitud;
	 * */
	public  Long  getCantidadSolicitudUsuarioDerivacionToModificado(Long idSol, String codigoEstado){
		Long cantidad = null;
		try{
			cantidad =  (Long)entityManager
					 .createQuery("select  COUNT(dc.systemId) " +
					 		"      from SolicitudUsuarioDerivada dc " +
					 		"      where dc.idSolicitud=:idSol " +
					 		"      and dc.estado.codEstado=:estadoAux  ")
					 .setParameter("idSol", idSol)		
					 .setParameter("estadoAux", codigoEstado)
					.getSingleResult();
			if(cantidad != null){
				return new Long(cantidad);
			}else{
				return new Long(0);
			}
		}catch (Exception e) {
			log.error("Error, los datos getCantidadSolicitudUsuarioDerivacion. #0", e.getMessage());
			cantidad = new Long(0);
		}
		return cantidad;
	}
	
	/*
	 * Parametros de entrada: codigo la solicitud. 
	 * Return : retorna el numero de cantidad usuarios que an intervenido la solcitud;
	 * */
	public  Long  getCantidadSolicitudUsuarioDerivacionIngresada(Long idSol){
		Long cantidad = null;
		try{
			cantidad =  (Long)entityManager
					 .createQuery("select  COUNT(dc.systemId) " +
					 		"      from SolicitudUsuarioDerivada dc " +
					 		"      where dc.idSolicitud=:idSol ")
					 .setParameter("idSol", idSol)		
					 .getSingleResult();
			if(cantidad != null){
				return new Long(cantidad);
			}else{
				return new Long(0);
			}
		}catch (Exception e) {
			log.error("Error, los datos getCantidadSolicitudUsuarioDerivacion. #0", e.getMessage());
			cantidad = new Long(0);
		}
		return cantidad;
	}
	
	
	
	
	/*
	 * Parametros de entrada: persist de SolicitudMotivoRechazo. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  persistSolicitudMotivoRechazo(SolicitudMotivoRechazo smr){
		try{
			entityManager.persist(smr);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto persitSolicitudMotivoRechazo. #0", e.getMessage());
			return false;
		}
		return true;
	}
	
	/*
	 * Parametros de entrada: merger de SolicitudMotivoRechazo. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  mergerSolicitudMotivoRechazo(SolicitudMotivoRechazo sud){
		try{
			entityManager.merge(sud);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto mergerSolicitudMotivoRechazo #0", e.getMessage());
			return false;
		}
		return true;
	}
	
	/*
	 * Parametros de entrada: codigo de solicitud . 
	 * Return : lista de usuarios derivados;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<SolicitudMotivoRechazo>  getSolicitudMotivoRechazoForSolicitud(Long id){
		try{
			return  (List<SolicitudMotivoRechazo>)entityManager
					 .createQuery("select  smr " +
					 		"      from SolicitudMotivoRechazo smr where smr.idSolicitud=:idSol")
					 .setParameter("idSol", id)		
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getSolicitudMotivoRechazoForSolicitud. #0", e.getMessage());
		}
		return null;
	}
	
	
	
	/*
	 * Parametros de entrada: persist de SolicitudAccion. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  persistSolicitudAccion(SolicitudAccion sa){
		try{
			entityManager.persist(sa);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al persist el objeto persistSolicitudAccion. #0", e.getMessage());
			return false;
		}
		return true;
	}	
	
	/*
	 * Parametros de entrada: merger de SolicitudAccion. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  mergerSolicitudAccion(SolicitudAccion sa){
		try{
			entityManager.merge(sa);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto mergerSolicitudAccion #0", e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/*
	 * Parametros de entrada:vacio. 
	 * Return : retorna el numero de indice principal system_id ;
	 * */
	@SuppressWarnings("unchecked")
	public  Long  getSolicitudAccionMaximo(){
		List<SolicitudAccion> lista = null;
		try{
			lista =  (List<SolicitudAccion>)entityManager
					 .createQuery("select  dc " +
					 		"      from SolicitudAccion dc order by dc.systemId desc ")
					.getResultList();
			if(lista != null && lista.size() > 0){
				SolicitudAccion obj = lista.get(0);
				if(obj != null){
					return  new Long(obj.getSystemId()+1);
				}else{
					return new Long(1);
				}
			}else{
				return new Long(1);
			}
		}catch (Exception e) {
			log.error("Error, los datos getSolicitudAccionMaximo. #0", e.getMessage());
		}
		return null;
	}
	

	/*
	 * Parametros de entrada:vacio. 
	 * Return : retorna el numero de indice principal system_id ;
	 * */
	@SuppressWarnings("unchecked")
	public  Long  getSolicitudDiaReparoMaximo(){
		List<SolicitudDiaReparo> lista = null;
		try{
			lista =  (List<SolicitudDiaReparo>)entityManager
					 .createQuery("select  dc " +
					 		"      from SolicitudDiaReparo dc order by dc.idSolicitudDiaReparo desc ")
					.getResultList();
			if(lista != null && lista.size() > 0){
				SolicitudDiaReparo obj = lista.get(0);
				if(obj != null){
					return  obj.getDias();
				}else{
					return new Long(4);
				}
			}else{
				return new Long(4);
			}
		}catch (Exception e) {
			log.error("Error, los datos getSolicitudAccionMaximo. #0", e.getMessage());
		}
		return null;
	}	
	
	
	
	
	/*
	 * Parametros de entrada: codigo de solicitud y numero de cheque. 
	 * Return : objeto de Prorroga;
	 * */	

	public  LcredSolicitudProrroga getDoctoProrrogaForSolicitud(Long id, String numeroChecque){
		try{
			return  (LcredSolicitudProrroga)entityManager
					 .createQuery("select  lsp " +
					 		"      from LcredSolicitudProrroga lsp where lsp.id.numSolicitud=:idSol" +
					 		"      and  lsp.id.nroCheque=:numero")
					 .setParameter("idSol", id)		
					 .setParameter("numero", numeroChecque)		
					.getSingleResult();
		}catch (Exception e) {
			log.error("Error, los datos getDoctoProrrogaForSolicitud. #0", e.getMessage());
		}
		return null;
	}
	
	/*
	 * Parametros de entrada: codigo de solicitud y numero de cheque. 
	 * Return : objeto de Prorroga;
	 * */	

	@SuppressWarnings("unchecked")
	public  UfSolicitudTipo getUfSolicitudTipo(int tipoSolicitud){
		List<UfSolicitudTipo> lista= null;
		UfSolicitudTipo exito = null;
		try{
			lista = (List<UfSolicitudTipo>)entityManager
					 .createQuery("select  ust " +
					 		"      from UfSolicitudTipo ust where ust.tipoSolicitud=:numero " +
					 		" order by ust.idUfSolicitudTipo desc")
					 .setParameter("numero", tipoSolicitud)		
					.getResultList();
			if(lista != null && lista.size() > 0){
				exito = lista.get(0);
			}
			
			
		}catch (Exception e) {
			log.error("Error, los datos getDoctoProrrogaForSolicitud. #0", e.getMessage());
		}
		return exito;
	}
	
	
	/*
	 * Parametros de entrada: remove de LcredSolicitudProrroga. 
	 * Return : retorna el verdadero o falso ;
	 * */		
	public  boolean  removeLcredSolicitudProrroga(LcredSolicitudProrroga lsp){
		try{
			entityManager.remove(lsp);
			entityManager.flush();
		}catch (Exception e) {
			log.error("Error, al merge el objeto removeLcredSolicitudProrroga #0", e.getMessage());
			return false;
		}
		return true;
	}	
	
	
	/*
	 * Parametros de entrada: nada . 
	 * Return : lista de regiones;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<Region>  getRegiones(){
		try{
			return  (List<Region>)entityManager
					 .createQuery("select  r " +
					 		"      from Region r")
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getRegiones. #0", e.getMessage());
		}
		return null;
	}

	/*
	 * Parametros de entrada: Usuariosegur . 
	 * Return : lista de sociedes;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<Sociedad>  getSociedades(Usuariosegur usuarioLogueado){
		List<Sociedad> listaActual = new ArrayList<Sociedad>(0);
		try{
			
			List<Sociedad> lista =  ( List<Sociedad>)entityManager
											.createQuery("select  us.sociedad from UsuarioSociedad us "
											+ " where us.usuario.idPersonal=:usu")
											.setParameter("usu", usuarioLogueado.getIdPersonal())
											.getResultList();
			if(lista != null){
				listaActual = lista;
			}
			
			
		}catch (Exception e) {
			log.error("Error, los datos getSociedades. #0", e.getMessage());
			
		}
		return listaActual;
	}
	
	/*
	 * Parametros de entrada: DestinatarioDTO, Sociedad. 
	 * Return : lista de sociedes;
	 * */	
	@SuppressWarnings("unchecked")
	public boolean  verificarUsuarioSociedades(DestinatarioDTO usuario, Sociedad soc){
		boolean existeAsociacion = false;
		try{
			
			List<Sociedad> lista =  ( List<Sociedad>)entityManager
											.createQuery("select  us.sociedad from UsuarioSociedad us "
											+ " where us.usuario.idPersonal=:usu"
											+ " and us.sociedad.idSociedad=:soc")
											.setParameter("usu", usuario.getIdPersonal())
											.setParameter("soc", soc.getIdSociedad())
											.getResultList();
			if(lista != null && lista.size() > 0){
				existeAsociacion = true;
			}
		}catch (Exception e) {
			log.error("Error, los datos getSociedades. #0", e.getMessage());
			
		}
		return existeAsociacion;
	}
	
	/*
	 * Parametros de entrada: DestinatarioDTO, Sociedad. 
	 * Return : true o false;
	 * */	
	@SuppressWarnings("unchecked")
	public boolean  verificarIdUsuarioCodigoSociedades(long idUsuario, String soc){
		boolean existeAsociacion = false;
		try{
			
			List<Sociedad> lista =  ( List<Sociedad>)entityManager
											.createQuery("select  us.sociedad from UsuarioSociedad us "
											+ " where us.usuario.idPersonal=:usu"
											+ " and us.sociedad.codigoSociedad=:soc")
											.setParameter("usu", idUsuario)
											.setParameter("soc", soc)
											.getResultList();
			if(lista != null && lista.size() > 0){
				existeAsociacion = true;
			}
		}catch (Exception e) {
			log.error("Error, los datos getSociedades. #0", e.getMessage());
			
		}
		return existeAsociacion;
	}	
	
	
	/*
	 * Parametros de entrada: codigo la region . 
	 * Return : lista de provincias;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<Provincia>  getProvincias(String CodigoRegion){
		try{
			return  (List<Provincia>)entityManager
					 .createQuery("select  p " +
					 		"      from Provincia p where p.region.codigo=:codigoAux")
					 .setParameter("codigoAux", CodigoRegion)		
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getProvincias. #0", e.getMessage());
		}
		return null;
	}
	

	/*
	 * Parametros de entrada: codigo la provincia . 
	 * Return : lista de comunas;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<Comuna>  getComunas(String codigoProvincia){
		try{
			return  (List<Comuna>)entityManager
					 .createQuery("select  c " +
					 		"      from Comuna c where c.provincia.codigo=:codigoAux")
					 .setParameter("codigoAux", codigoProvincia)		
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos getComunas. #0", e.getMessage());
		}
		return null;
	}	
	
	/*
	 * Parametros de entrada: user del usuario . 
	 * Return : objeto UsuarioCorreoDTO.
	 * */		
	public UsuarioCorreoDTO sacarDatosUsuarioCorreo(String user){
		UsuarioCorreoDTO datos = new UsuarioCorreoDTO();
    	Object[]  array = null;
		try{
	  	         array = (Object[])entityManager
	  	        		 .createNativeQuery(" SELECT cast([nombre] as varchar) as nombre , cast([Correo] as varchar) as correo" +
											" FROM [SCORING_PRODUCION].[dbo].[USUARIOSEGUR] " +
											" WHERE Alias=:username")
	  			          .setParameter("username", user)
	  			          .setMaxResults(1)
	  			          .getSingleResult();
	  	         
	  	         if(array != null){
	  	        	 if(array[0] != null && array[1] != null){
		  	        	
	  	        		datos.setNombreUsuario((array[0].toString()).trim());
	  	        		datos.setCorreoElectronico((array[1].toString()).trim());
		  	        	return datos;
	  	        	}
	  	         }else{
	  	        	 log.debug("nNo se encontro el nombre ni el correo del usuario #0", user);
	  	        	datos = null;
	  	         }
		}catch (Exception e) {
			 log.error("Error, no existe en la base de datros prodducion #0", e.getMessage());
			 datos = null;
		}
		return datos;
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
    
	/*
	 * Parametros de entrada: LogEmail logEmail . 
	 * Return : verdadero y falso;
	 * */	
	public boolean   persistLogEmail(LogsCorreos logEmail){
		boolean exito = false;
		try{
			if(logEmail != null){
				entityManager.persist(logEmail);
				entityManager.flush();
				exito=true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, los datos persistLogEmail. #0", e.getMessage());
		}
		return exito;
	}	
    
	/*
	 * Parametros de entrada: numero de la solicitud 
	 * Return : verdadero o falso.
	 * */		
	public String verificarSiEstaTomadaSolicitud(Long idLong, String usuarioAux, boolean administrador){
		String usuarioDevuelve = null;
    	try{
    		//if(administrador == false){
    		 Object usuarioSolicitud = (Object)entityManager
    				 .createNativeQuery("  SELECT  [cod_usuario_devuelve] " +
									   "  FROM [SCORING_PRODUCION].[dbo].[lcred_solicitud]" +
									   "  WHERE [num_solicitud]=:idSolicitudAux ")
		  			           .setParameter("idSolicitudAux", idLong)
		  			           .getSingleResult();
		  	
    		 if(usuarioSolicitud == null){
    			 return null;
    		 }else{
    			 
    			  if((usuarioAux.trim().toUpperCase()).equals((usuarioSolicitud.toString()).trim().toUpperCase())){
    				  return null;
    			  }else{
    				  return "1-" + usuarioSolicitud.toString().toUpperCase();
    			  }
    				  //sacar el nombre del usuario
    			/*	  try{
    			    		 Object nombreUsuario = (Object)entityManager
    			 		  			.createNativeQuery(" SELECT [NOMBRE] " +
    			 		  							   " FROM [SCORING_PRODUCION].[dbo].[USUARIOSEGUR] " +
    			 		  							   " WHERE [Alias] IN ( " +
    			 		  							   "       SELECT  [cod_usuario_devuelve] " +
    			 									   "       FROM [SCORING_PRODUCION].[dbo].[lcred_solicitud]" +
    			 									   "       WHERE [num_solicitud]=:idSolicitudAux )" +
    			 									   " ")
    			 		  			           .setParameter("idSolicitudAux", idLong)
    			 		  			           .getSingleResult();
    			    		 
    			    		 if(nombreUsuario != null){
    			    			 usuarioDevuelve = nombreUsuario.toString();
    			    			 
    			    			 return "1-"+nombreUsuario.toString();
    			    		 }
    					  
    				  }catch (Exception e) {
						log.error("Error al sacar el nombre del usuario #0, error #1", usuarioSolicitud.toString(), e.getMessage());
						return "1-"+usuarioSolicitud.toString();
					}
    			  }*/
    		 }
    	   //} 
    	}catch (Exception e) {
    		log.error("2-Error, al sacar los datos del usuario de la base de datos segur #0 ", e.getMessage());
    		usuarioDevuelve = "2-Error, no se encontro el usuario que tomo la solicitud.";
    	}

    	return usuarioDevuelve;
    }	
	
	
	/*
	 * Parametros de entrada: codigo la region . 
	 * Return : lista de provincias;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<LcredTipoSolicitud>  getListadoLcredTipoSolicitudOrdenadoPorCorrelativo(){
		try{
			return  (List<LcredTipoSolicitud>)entityManager
					 .createQuery("select  lts " +
					 		"      from LcredTipoSolicitud lts where lts.correlativo is not null order by lts.correlativo asc")
					.getResultList();
		}catch (Exception e) {
			log.error("Error, los datos los tipo de solicitudes. #0", e.getMessage());
		}
		return null;
	}
	
	
	/*
	 * Parametros de entrada: codigo la region . 
	 * Return : lista de provincias;
	 * */	
	@SuppressWarnings("unchecked")
	public  Jornada  getSacarUltimaJornada(){
		try{
			List<Jornada> listaJornadas = (List<Jornada>)entityManager
					 .createQuery("select  j " +
					 		"      from Jornada j where j.disabled=0 order by j.systemId desc")
					 .setMaxResults(1)
					 .getResultList();
			if(listaJornadas != null && listaJornadas.size() > 0){
				return listaJornadas.get(0);
			}else{
				return null;
			}
		}catch (Exception e) {
			log.error("Error, los datos los tipo de solicitudes. #0", e.getMessage());
		}
		return null;
	}
	
	/*
	 * Parametros de entrada: vacio . 
	 * Return : retorna lista de String, con los negocios;
	 * */		
	@SuppressWarnings("unchecked")
	public List<ConceptosNegocio> obtenerNegocios(){
		List<ConceptosNegocio> lista = new ArrayList<ConceptosNegocio>();
		try{
			 List<Object[]>  list  = ( List<Object[]>)entityManager
		  		.createNativeQuery("  SELECT distinct([Negocio]) " +
						     " FROM [SCORING_PRODUCION].[dbo].[ConceptosNegocios] " +
						     " where Negocio not in ('00') " +
						     " order by  Negocio asc; ")
                .getResultList();	
			 if(list!= null){
				 for(Object data: list){
					 if(data != null){
						 ConceptosNegocio cp = obtenerConceptoNegocioLocalForCodigoNegocio(data.toString());
						 if(cp != null){
							 lista.add(cp);
						 }
					 }
				 }
			 }
		}catch (Exception e) {
			log.error("Error, al obtener Negocios. #0", e.getMessage());
		}
		return lista;
	}
	
	
	/*
	 * Parametros de entrada: ususario . 
	 * Return : retorna false sino existe y true si existe;
	 * */	
	public  boolean  getConsultaUsuarioEnLaMatrizResponsabilidades(Usuariosegur user){
		try{
			if(user != null){
				MatrizUsuarioResponsable mur = (MatrizUsuarioResponsable)entityManager
									 			.createQuery("select mur " +
										 		"      from MatrizUsuarioResponsable mur where mur.usuariosegur.idPersonal=:idPersonal")
										 		.setParameter("idPersonal", user.getIdPersonal())		
										 		.getSingleResult();
				if(mur != null){
					return true;
				}else{
					return false;
				}
			}
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de responsabilidades. #0", e.getMessage());
			return false;
		}
		return false;
		
	}	
	/*
	 * Parametros de entrada: ususario . 
	 * Return : retorna objeto MatrizUsuarioResponsable;
	 * */	
	public  MatrizUsuarioResponsable  getMatrizUsuarioResponsable(Usuariosegur user){
		try{
			if(user != null){
				MatrizUsuarioResponsable mur = (MatrizUsuarioResponsable)entityManager
									 			.createQuery("select mur " +
										 		"      from MatrizUsuarioResponsable mur where mur.usuariosegur.idPersonal=:idPersonal")
										 		.setParameter("idPersonal", user.getIdPersonal())		
										 		.getSingleResult();
				if(mur != null){
					return mur;
				}else{
					return null;
				}
			}
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de responsabilidades. #0", e.getMessage());
			return null;
		}
		return null;
	}
	
	/*
	 * Parametros de entrada: ususario . 
	 * Return : retorna false sino existe y true si existe;
	 * */	
	@SuppressWarnings("unchecked")
	public  boolean  getConsultaLaMatrizDetalleResponsabilidadesSucursal(MatrizUsuarioResponsable mur, Sucursal suc){
		boolean existe = false;
		try{
			if(mur != null){
				List<MatrizDetalleResponsabilidad> list = (List<MatrizDetalleResponsabilidad>)entityManager
											 			.createQuery("select mdr " +
												 		"      from MatrizDetalleResponsabilidad mdr where mdr.matrizUsuarioResponsable.systemId=:idMur")
												 		.setParameter("idMur", mur.getSystemId())		
												 		.getResultList();
				
				if(list != null && list.size() > 0){
					for(MatrizDetalleResponsabilidad mdr :list){
						if(mdr != null && mdr.getSucursal() != null){
							if(mdr.getSucursal().getCodigo().equals(suc.getCodigo())){
								existe = true;
								break;
							}
						}
					}
				}else{
					return existe;
				}
			}
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de responsabilidades. #0", e.getMessage());
			return existe;
		}
		return existe;
		
	}
	
	/*
	 * Parametros de entrada: MatrizUsuarioResponsable mur, Sucursal suc . 
	 * Return : retorna objeto MatrizDetalleResponsabilidad;
	 * */	
	@SuppressWarnings("unchecked")
	public  MatrizDetalleResponsabilidad  getMatrizDetalleResponsabilidad(MatrizUsuarioResponsable mur, Sucursal suc){
		MatrizDetalleResponsabilidad existe = null;
		try{
			if(mur != null){
				List<MatrizDetalleResponsabilidad> list = (List<MatrizDetalleResponsabilidad>)entityManager
											 			.createQuery("select mdr " +
												 		"      from MatrizDetalleResponsabilidad mdr where mdr.matrizUsuarioResponsable.systemId=:idMur")
												 		.setParameter("idMur", mur.getSystemId())		
												 		.getResultList();
				
				if(list != null && list.size() > 0){
					for(MatrizDetalleResponsabilidad mdr :list){
						if(mdr != null && mdr.getSucursal() != null){
							if(mdr.getSucursal().getCodigo().equals(suc.getCodigo())){
								existe = mdr;
								break;
							}
						}
					}
				}else{
					return existe;
				}
			}
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de responsabilidades. #0", e.getMessage());
			return existe;
		}
		return existe;
		
	}
	
	
	/*
	 * Parametros de entrada: MatrizUsuarioResponsable. 
	 * Return : verdadero y falso;
	 * */	
	public MatrizUsuarioResponsable   persistMatrizUsuarioResponsable(MatrizUsuarioResponsable mur){
		try{
			if(mur != null){
				entityManager.persist(mur);
				entityManager.flush();
				return mur;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, los datos MatrizUsuarioResponsable. #0", e.getMessage());
		}
		return null;
	}	

	/*
	 * Parametros de entrada: MatrizDetalleResponsabilidad. 
	 * Return : verdadero y falso;
	 * */	
	public MatrizDetalleResponsabilidad   persistMatrizDetalleResponsable(MatrizDetalleResponsabilidad mdr){
		try{
			if(mdr != null){
				entityManager.persist(mdr);
				entityManager.flush();
				return mdr;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, los datos MatrizUsuarioResponsable. #0", e.getMessage());
		}
		return null;
	}	
	
	/*
	 * Parametros de entrada: vacio . 
	 * Return : retorna false sino existe y true si existe;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<MatrizResposabilidadDTO>  getListaMatrizResponsabilidades(){
		List<MatrizResposabilidadDTO> listaFinal = new ArrayList<MatrizResposabilidadDTO>();
		try{

				List<MatrizUsuarioResponsable> listaMur = (List<MatrizUsuarioResponsable>)entityManager
									 			.createQuery("select mur " +
										 		"      from MatrizUsuarioResponsable mur ")
										 		.getResultList();
				if(listaMur != null && listaMur.size() > 0){
					for(MatrizUsuarioResponsable mur:listaMur){
						MatrizResposabilidadDTO mrdto = new MatrizResposabilidadDTO();
						mrdto.setMatrizUsuarioResp(mur);
						mrdto.setNombreUsuario(mur.getUsuariosegur().getNombre().toUpperCase());
						 try{
							  List<MatrizDetalleResponsabilidad> listaDetalle = (List<MatrizDetalleResponsabilidad>)entityManager
							 			.createQuery("select mdr " +
								 		"      from MatrizDetalleResponsabilidad mdr where mdr.matrizUsuarioResponsable.systemId=:idMur")
								 		.setParameter("idMur", mur.getSystemId())		
								 		.getResultList();
							  if(listaDetalle != null){
								  mrdto.setListaMatrizDetalleResp(listaDetalle); 
							  }
						 }catch (Exception e) {
							 log.error("no existen sucursales asignadas para el usuario #0",mur.getUsuariosegur().getAlias());
							 mrdto.setListaMatrizDetalleResp(null);
						 }
						 listaFinal.add(mrdto);
					}
				}
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de responsabilidades. #0", e.getMessage());
			return null;
		}
		return listaFinal;
	}
	
	
	/*
	 * Parametros de entrada: String de sucursales para la consultas Ejmplo ('XXXX','XXXX') . 
	 * Return : Lista de Usuarios Responsables;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<Usuariosegur>  getListaUsuariosSegunMatrizResponsabilidades(String sucursales){
		List<Usuariosegur> listaFinal = new ArrayList<Usuariosegur>();
		try{
			String query = " select mur " +
					       " from MatrizUsuarioResponsable mur" +
					       " where systemId in ( " +
					       "   select distinct(mdr.matrizUsuarioResponsable.systemId) " +
					       "   from MatrizDetalleResponsabilidad mdr " +
					       "   where mdr.sucursal.codigo in ("+ sucursales +")"+
					       " )"; 
			List<MatrizUsuarioResponsable> listaMur 
						= (List<MatrizUsuarioResponsable>)
						entityManager.createQuery(query).getResultList();
			if(listaMur != null && listaMur.size() > 0){
				for(MatrizUsuarioResponsable mur : listaMur){
					listaFinal.add(mur.getUsuariosegur());
				}
			}
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de responsabilidades. #0", e.getMessage());
			return null;
		}
		return listaFinal;
	}
	
	/*
	 * Parametros de entrada: String de negocios para la consultas Ejmplo ('XXXX','XXXX') . 
	 * Return : lista cotizacion pedido negocios;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<CotizacionPedidoNegocio>  getVerificarExisteNegocios(String negocios, Long idSolicitud){
		List<CotizacionPedidoNegocio> listaFinal = new ArrayList<CotizacionPedidoNegocio>();
		try{
			String query = " select cpn " +
					       " from CotizacionPedidoNegocio cpn" +
					       " where cpn.cotizacionPedido.systemId in ( " +
					       "   select cp.systemId " +
					       "   from CotizacionPedido cp " +
					       "   where cp.cotizacionPedidoCabecera.systemId in ( " +
					       "     select cpc.systemId  from CotizacionPedidoCabecera cpc " +
					       " 	 where cpc.solicitud.id.numSolicitud=:sol " +
					       "   )"+
					       " ) and cpn.negocio("+ negocios +")"; 
				listaFinal = (List<CotizacionPedidoNegocio>)
						entityManager.createQuery(query).setParameter("sol", idSolicitud)
						.getResultList();
			
			
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de responsabilidades. #0", e.getMessage());
			return null;
		}
		return listaFinal;
	}
	
	/*
	 * Parametros de entrada: String de negocios para la consultas Ejmplo ('XXXX','XXXX') . 
	 * Return : lista cotizacion pedido negocios;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<HitosDTO>  getConsultaMatrizResponsabilidades(
			String usuarios, String fechaInicio, String fechaTermino, String tipoSolicitudes, 
			String canales, String negocios, String sucursales){
		List<HitosDTO> listaFinal;
		try{
			String query = ""; 
			query =  "   SELECT solhit.[system_id] ";
			query += "  	 ,solhit.[cod_estado_derivada] ";
			query += "       ,solhit.[codigo_estado] ";
			query += "       ,solhit.[descripcion_estado] ";
			query += "       ,solhit.[emisor] ";
			query += "       ,convert(varchar,lsol.[fec_solicitud] ,105) as fechasol_creacion ";
			query += "       ,convert(varchar,lsol.[hra_solicitud],8) as horasol_creacion ";
			query += "       ,convert(varchar,solhit.[fecha_hora],105) as fecha_hitos ";
			query += "       ,convert(varchar,solhit.[fecha_hora],8) as hora_hitos ";
			query += "       ,solhit.[solicitud_id] ";
			query += "       ,solhit.[usuario_actual] ";
			query += "       ,solhit.[accion_administrador] "; 
			query += "       ,lsol.lin_credito_disp as lineacredito ";
			query += "       ,lsol.canal as canal ";
			query += "       ,lsol.tip_tiposol as tipo ";
			query += "   FROM [SCORING_PRODUCION].[dbo].[solicitud_hitos] solhit ";
			query += "   left join [SCORING_PRODUCION].[dbo].[lcred_solicitud] lsol on solhit.solicitud_id = lsol.num_solicitud ";
			query += "   left join [SCORING_PRODUCION].[dbo].[cotizacion_pedido_cabecera] cotcab on lsol.num_solicitud = cotcab.solicitud_id ";
			query += "   left join [SCORING_PRODUCION].[dbo].[cotizacion_pedido] cotped on cotcab.system_id = cotped.cot_ped_cab_id ";
			query += "   left join [SCORING_PRODUCION].[dbo].[cotizacion_pedido_negocio] cotpneg on cotped.system_id = cotpneg.cotizacion_pedido_id ";
			query += "   WHERE  solhit.[codigo_estado]  <> 'E' ";
			
			if(usuarios != null && !"".equals(usuarios)){
				query += "   and solhit.[usuario_actual] IN ("+usuarios+") ";
				//query += "   and solhit.[usuario_actual] IN ('MAGUILERA','MASTUDILLO') ";
			}
			
			if(fechaInicio != null && fechaTermino != null){
				query += "   AND lsol.[fec_solicitud]   BETWEEN CAST('"+fechaInicio+" 00:00:00' AS datetime) AND CAST('"+fechaTermino+" 23:59:59' as datetime) ";
				//query += "   AND lsol.[fec_solicitud]   BETWEEN CAST('20130901' AS datetime) AND CAST('20131031' as datetime) ";
			}
			
			if(tipoSolicitudes != null && !"".equals(tipoSolicitudes)){
				query += "   AND lsol.tip_tiposol IN ("+tipoSolicitudes+") ";
			}
			
			if(canales != null){
				query += "   AND lsol.canal IN ('Mixto','Kupfer Express','Grandes Cuentas') ";
				//query += "   AND lsol.canal IN ('Mixto','Kupfer Express','Grandes Cuentas') ";
			}
			
			if(negocios != null && !"".equals(negocios)){
				query += "   and cotpneg.Negocio IN ("+ negocios +") ";
				//query += "   and cotpneg.Negocio IN ('04') ";
			}
			
			if(sucursales != null && !"".equals(sucursales)){
				query += "   and lsol.sucursal_emisor in ("+sucursales+") ";
				//and lsol.sucursal_emisor in ('C115')
			}
			
			
			query += "   ORDER BY solhit.[solicitud_id]  ASC; ";	
			
			//System.out.println(query);
			log.debug(query, "");
			
			List<Object[]> lista = (List<Object[]>)
						entityManager.createNativeQuery(query).getResultList();
			
			if(lista != null && lista.size() > 0){
				listaFinal = new ArrayList<HitosDTO>();
				for(Object[] obj : lista ){
					HitosDTO objetoHitos = new HitosDTO();
					if(obj[0] != null){
						objetoHitos.setIdSystem(Long.parseLong(obj[0].toString()));
					}
					if(obj[1] != null){
						objetoHitos.setCodEstadoDeriva(obj[1].toString());
					}
					if(obj[2] != null){
						objetoHitos.setEstado(obj[2].toString());
					}					
					if(obj[3] != null){
						objetoHitos.setDescripcionEstado(obj[3].toString());
					}						
					if(obj[4] != null){
						objetoHitos.setEmisor(obj[4].toString());
					}						
					if(obj[5] != null){
						objetoHitos.setFechaCreacion(obj[5].toString());
					}
					if(obj[6] != null){
						objetoHitos.setHoraCreacion(obj[6].toString());
					}					
					if(obj[7] != null){
						objetoHitos.setFechaHitos(obj[7].toString());
					}
					if(obj[8] != null){
						objetoHitos.setHoraHitos(obj[8].toString());
					}
					if(obj[9] != null){
						objetoHitos.setIdSolicitud(Long.parseLong(obj[9].toString()));
					}	
					if(obj[10] != null){
						objetoHitos.setUsuarioActualHitos(obj[10].toString());
					}
					if(obj[11] != null){
						objetoHitos.setAccionAdmiistrador(obj[11].toString());
					}
					if(obj[12] != null){
						objetoHitos.setLineaCreditoDisponible(new BigDecimal(obj[12].toString()));
					}
					if(obj[13] != null){
						if(TipoCuentasKupferType.KX.getNombre().equals(obj[13].toString())){
							objetoHitos.setCanal(TipoCuentasKupferType.KX);
						}else if(TipoCuentasKupferType.MX.getNombre().equals(obj[13].toString())){
							objetoHitos.setCanal(TipoCuentasKupferType.MX);
						}else if(TipoCuentasKupferType.GC.getNombre().equals(obj[13].toString())){
							objetoHitos.setCanal(TipoCuentasKupferType.GC);
						}
					}
					if(obj[14] != null){
						objetoHitos.setTipoSolicitud(obj[14].toString());
					}
					listaFinal.add(objetoHitos);
				}
			}else{
				listaFinal = null;
			}
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de responsabilidades. #0", e.getMessage());
			return null;
		}
		return listaFinal;
	}
	
	
	/*
	 * Parametros de entrada: fecha inicial y fecha terminar  
	 * Return : lista String fechas de feriados;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<String>  getConsultaFeriadosSegunRangoFechas(String fechaInicio, String fechaTermino){
		List<String> listaFeriados;
		try{
			String query = ""; 
			query =  "   SELECT convert(varchar,[fecha] ,105) as fechasol_creacion ";
			query += "   FROM [SCORING_PRODUCION].[dbo].[feriado] ";
			query += "   WHERE [fecha] BETWEEN CAST('"+fechaInicio+"' AS datetime) AND CAST('"+fechaTermino+"' as datetime) ";
			query += "   ORDER BY fecha  ";
					
			log.debug(query);
			
			List<Object> lista = (List<Object>)
						entityManager.createNativeQuery(query).getResultList();
			
			if(lista != null && lista.size() > 0){
				listaFeriados = new ArrayList<String>();
				for(Object obj : lista ){
					if(obj != null){
						listaFeriados.add(obj.toString());
					}
				}
				
				SimpleDateFormat wvarformat = new SimpleDateFormat("yyyyMMdd");
		        Calendar wvarCalFI = Calendar.getInstance();
		        Calendar wvarCalFF = Calendar.getInstance();
		        wvarCalFI.setTime(wvarformat.parse(fechaInicio));
		        wvarCalFF.setTime(wvarformat.parse(fechaTermino));
		        while (wvarCalFI.before(wvarCalFF) || wvarCalFI.equals(wvarCalFF)){
		            if (wvarCalFI.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || wvarCalFI.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
		                log.debug("Cargando Sabados y Domingo " + wvarformat.format(wvarCalFI.getTime()));
		                String wvarFecha = wvarformat.format(wvarCalFI.getTime());
		                log.debug("Cargando Lista de feriados " + wvarFecha.substring(6, 8) + "-" + wvarFecha.substring(4, 6) + "-" + wvarFecha.substring(0, 4));
		                listaFeriados.add(wvarFecha.substring(6, 8) + "-" + wvarFecha.substring(4, 6) + "-" + wvarFecha.substring(0, 4));
		            }
		            wvarCalFI.add(Calendar.DATE, 1);
		        }
			}else{
				listaFeriados = null;
			}
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de responsabilidades. #0", e.getMessage());
			return null;
		}
		return listaFeriados;
	}

	/*
	 * Parametros de entrada: fecha inicial y fecha terminar  
	 * Return : lista String fechas de feriados;
	 * */	
	@SuppressWarnings("unchecked")
	public  List<CondicionTipoSolicitudRango>  getCondicionTipoSolicitudRango(LcredTipoSolicitud tipo, Canal canal,TiempoMontoType tiempoMonto, Medicion medicion){
		log.debug("lo que viene");
		List<MedicionCanalRango> listaMedicionCanalRangos=null;
		List<CondicionTipoSolicitudRango> listaCondiciones = new ArrayList<CondicionTipoSolicitudRango>(0);
		try{
			try{
				if(canal != null && medicion != null){
					listaMedicionCanalRangos = (List<MedicionCanalRango>)
							entityManager.createQuery(" select m from MedicionCanalRango m " +
								  " where m.medicion.systemId=:medicion " +
								  " and m.canal.systemId=:canal " +
								  " and m.tiempoMontoType=:tiempoMonto " +
								  " and m.disabled=0 ")
								  .setParameter("medicion", medicion.getSystemId())
								  .setParameter("canal", canal.getSystemId())
								  .setParameter("tiempoMonto", tiempoMonto)
								  .getResultList();
				}else{
					if(medicion != null){
					listaMedicionCanalRangos = (List<MedicionCanalRango>)
					entityManager.createQuery(" select m from MedicionCanalRango m " +
							  " where m.medicion.systemId=:medicion " +
							  " and m.tiempoMontoType=:tiempoMonto " +
							  " and m.disabled=0 ")
							  .setParameter("medicion", medicion.getSystemId())
							  .setParameter("tiempoMonto", tiempoMonto)
							  .getResultList();	
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				log.error("Error, al sacar la mediciones. #0", e.getMessage());
			}
			

			if(listaMedicionCanalRangos != null && listaMedicionCanalRangos.size() > 0){
				List<CondicionTipoSolicitudRango>
					lista = (List<CondicionTipoSolicitudRango>)
					entityManager.createQuery(" select ctsr from CondicionTipoSolicitudRango ctsr " +
											  " where ctsr.tipoSolicitud.codTipoSolicitud=:tipo" +
											  " and ctsr.medicionCanalRango in (:rango)" )
											  .setParameter("tipo", tipo.getCodTipoSolicitud())
											  .setParameter("rango", listaMedicionCanalRangos)
											  .getResultList();
				
				if(lista != null && lista.size() > 0){
					listaCondiciones = new ArrayList<CondicionTipoSolicitudRango>();
					for(CondicionTipoSolicitudRango objeto : lista ){
						if(objeto != null){
							listaCondiciones.add(objeto);
						}
					}
				}else{
					listaCondiciones = null;
				}
			}
		}catch (Exception e) {
			log.error("Error, al realizar la consulta en la matriz de CondicionTipoSolicitudRango. #0", e.getMessage());

		}
		return listaCondiciones;
	}
	
	/*
	 * Parametros de entrada: idSolicitud . 
	 * Return : lista cotizacion pedido negocios;
	 * */	
	@SuppressWarnings({ "unchecked", "unused" })
	public  List<HitosDTO>  getObtenerDatosHitosPorSolicitud(long idSolicitud){
		List<HitosDTO> listaFinal;
		try{
			String query = ""; 
			query =  "	SELECT lsol.num_solicitud ";
			query +="	 ,solhit.system_id ";
            query +="    ,cast(lsol.rut_cliente as varchar) as rut_cliente ";
            query +="    ,cast(lsol.nom_cliente as varchar) as nom_cliente ";
            query +="    ,lsol.monto as monto ";
            query +="    ,cast(lsol.tip_tiposol as varchar) as tipo ";
            query +="    ,cast(stuff((SELECT '/' + rtrim([Alias]) AS [text()] ";
            query +="                     FROM ";
			query +="					 (select alias ";
			query +="					 from [SCORING_PRODUCION].[dbo].[USUARIOSEGUR] ";
			query +="					 where id_personal in ( ";
			query +="						   SELECT distinct([usuario_id]) ";
			query +="						   FROM [SCORING_PRODUCION].[dbo].[solicitud_usuario_derivada] ";
			query +="						   WHERE solicitud_id = solhit.solicitud_id ";
			query +="						   AND estado_id IN ('AE','RE','AP'))) x ";
			query +="					 For XML PATH ('')),1,1,'') as varchar) as aprobadores ";
			query +="	 ,cast(stuff ((SELECT '/' + rtrim(des_concepto) AS [text()] ";
            query +="                      FROM ";
			query +="						 (select DISTINCT(con.des_concepto) from [SCORING_PRODUCION].[dbo].cotizacion_pedido_concepto con ";
			query +="						 inner join [SCORING_PRODUCION].[dbo].cotizacion_pedido_negocio neg on con.cotizacion_pedido_negocio_id=neg.system_id ";
			query +="						 inner join [SCORING_PRODUCION].[dbo].cotizacion_pedido ped on neg.cotizacion_pedido_id=ped.system_id ";
			query +="						 inner join [SCORING_PRODUCION].[dbo].cotizacion_pedido_cabecera cab on ped.cot_ped_cab_id=cab.system_id ";
			query +="						 where cab.solicitud_id = solhit.solicitud_id ";
			query +="						 ) x ";
			query +="						 For XML PATH ('')),1,1,'')  as varchar)  as conceptos ";
			query +="            ,cast(lsol.hora_minuto_segundo as varchar) as tiempo_inicial ";			
			query +="			 ,convert(varchar,lsol.[fec_solicitud] ,105) as fechasol_creacion ";
			query +="			 ,convert(varchar,lsol.[hra_solicitud],8) as horasol_creacion "; 			 
            query +="            ,convert(varchar,solhit.[fecha_hora],105) as fecha_hitos ";
            query +="            ,convert(varchar,solhit.[fecha_hora],8) as hora_hitos  ";
            query +="            ,cast(solhit.[codigo_estado] as varchar) as codigo_estado";
            query +="            ,cast(solhit.[descripcion_estado] as varchar) as descripcion_estado";
            query +="            ,cast(lsol.sub_tiposol as varchar) as subtiposol ";
            query +="            ,cast(lsol.canal as varchar) as canal ";
            query +="         FROM [SCORING_PRODUCION].[dbo].[solicitud_hitos] solhit ";
            query +="         left join [SCORING_PRODUCION].[dbo].[lcred_solicitud] lsol on solhit.solicitud_id = lsol.num_solicitud ";
            query +="         left join [SCORING_PRODUCION].[dbo].[cotizacion_pedido_cabecera] cotcab on lsol.num_solicitud = cotcab.solicitud_id ";
            query +="         left join [SCORING_PRODUCION].[dbo].[cotizacion_pedido] cotped on cotcab.system_id = cotped.cot_ped_cab_id ";
            query +="         left join [SCORING_PRODUCION].[dbo].[cotizacion_pedido_negocio] cotpneg on cotped.system_id = cotpneg.cotizacion_pedido_id ";
            query +="         WHERE  solhit.solicitud_id="+ idSolicitud +"";
            query +="         ORDER BY solhit.[solicitud_id],solhit.system_id  ASC ; ";
			
			System.out.println(query);
			
			List<Object[]> lista = (List<Object[]>)
						entityManager.createNativeQuery(query).getResultList();
			
			if(lista != null && lista.size() > 0){
				listaFinal = new ArrayList<HitosDTO>();
				for(Object[] obj : lista ){
					HitosDTO objetoHitos = new HitosDTO();
					if(obj[0] != null){
						objetoHitos.setIdSolicitud(Long.parseLong(obj[0].toString()));
					}
					if(obj[1] != null){
						objetoHitos.setIdSystem(Long.parseLong(obj[1].toString()));
					}
					if(obj[2] != null){
						objetoHitos.setRut(obj[2].toString());
					}					
					if(obj[3] != null){
						objetoHitos.setNombre(obj[3].toString());
					}						
					if(obj[4] != null){
						BigDecimal monto = new BigDecimal(obj[4].toString());
						if(monto != null){
							objetoHitos.setMonto(monto.longValue());
						}else{
							objetoHitos.setMonto(new Long(0));
						}
					}						
					if(obj[5] != null){
						objetoHitos.setTipoSolicitud(obj[5].toString());
					}
					if(obj[6] != null){
						objetoHitos.setAprobadores(obj[6].toString());
					}					
					if(obj[7] != null){
						objetoHitos.setConceptos(obj[7].toString());
					}
					if(obj[8] != null){
						objetoHitos.setTiempoInicial(obj[8].toString());
					}
					if(obj[9] != null){
						objetoHitos.setFechaCreacion(obj[9].toString());
					}	
					if(obj[10] != null){
						objetoHitos.setHoraCreacion(obj[10].toString());
					}
					if(obj[11] != null){
						objetoHitos.setFechaHitos(obj[11].toString());
					}
					if(obj[12] != null){
						objetoHitos.setHoraHitos(obj[12].toString());
					}
					if(obj[13] != null){
						objetoHitos.setEstado(obj[13].toString());
					}
					if(obj[14] != null){
						objetoHitos.setDescripcionEstado(obj[14].toString());
					}					
					if(obj[15] != null){
						objetoHitos.setDescripcionTipoSolicitud(obj[15].toString());
					}					
					if(obj[16] != null){
						if(TipoCuentasKupferType.KX.getNombre().equals(obj[16].toString())){
							objetoHitos.setCanal(TipoCuentasKupferType.KX);
						}else if(TipoCuentasKupferType.MX.getNombre().equals(obj[16].toString())){
							objetoHitos.setCanal(TipoCuentasKupferType.MX);
						}else if(TipoCuentasKupferType.GC.getNombre().equals(obj[16].toString())){
							objetoHitos.setCanal(TipoCuentasKupferType.GC);
						}
					}
					listaFinal.add(objetoHitos);
				}
			}else{
				listaFinal = null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Error, al realizar la consulta de los hitos de las solicitud. #0", e.getMessage());
			return null;
		}
		return listaFinal;
	}

}