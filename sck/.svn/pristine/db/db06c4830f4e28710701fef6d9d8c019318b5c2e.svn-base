package org.domain.sck.session.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.domain.sck.dto.ConceptoUsuarioDTO;
import org.domain.sck.dto.NegocioUsuarioDTO;
import org.domain.sck.dto.SucursalesUsuarioDTO;
import org.domain.sck.dto.ZonaUsuarioDTO;
import org.domain.sck.entity.CanalUsuarioCargo;
import org.domain.sck.entity.ConceptosNegocio;
import org.domain.sck.entity.LcredCargos;
import org.domain.sck.entity.PerfilFuncionCanal;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.entity.ZonaSucursalNegocioConcepto;
import org.domain.sck.entity.enums.ConceptosType;
import org.domain.sck.entity.enums.FuncionesType;
import org.domain.sck.entity.enums.TipoCuentasKupferType;
import org.domain.sck.session.service.intranetsap.IntranetSapService;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

@Name("cargaMasivaAction")
@Scope(ScopeType.CONVERSATION)
@SuppressWarnings({"unused"})
public class CargaMasivaAction {


	
    @Logger private Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	@In(value="#{emIntranetSAPSck}")
	EntityManager emIntranetSAPSck;
	
	@In IntranetSapService intranetSapService;
	
	@In ScoringService scoringService;
	
	public List<Sucursal> ListaSucrsales;
	public List<ConceptosNegocio> listaconceptoNegocios;
	public List<LcredCargos> listasCargos;
	public List<Usuariosegur> listasUsuarios;
	private final int NUMERO = 6;
	
	
	private boolean administrador;
	private boolean otrasSolictudes;
	private boolean eliminado;
	
	private Usuariosegur usuario;
	private LcredCargos cargo;
	
	private String[] conceptos = {"Z","S","N","C"};
	private String[] canales;
	private String[] perfiles;
	private String[] perfiles2;

	private String[] perfilesGC;
	private String[] perfiles2GC;
	
	private String[] perfilesMX;
	private String[] perfiles2MX;
	
	private boolean zona = true;
	private boolean sucursal = true;
	private boolean negocio = true;
	private boolean concepto = true;
	
	private boolean pestanakupferExpres;
	private boolean pestanaGrandesCuentas;
	private boolean pestanaMixto;
	
	private Long montoDesdeKX;
	private Long montoHastaKX;
	private Long montoDesdeCompKX;
	private Long montoHastaCompKX;
	
	private Long montoDesdeGC;
	private Long montoHastaGC;
	private Long montoDesdeCompGC;
	private Long montoHastaCompGC;	
	
	private Long montoDesdeMX;
	private Long montoHastaMX;
	private Long montoDesdeCompMX;
	private Long montoHastaCompMX;	
	
	/*lista del kupfer express*/
	private List<ZonaUsuarioDTO> listaZonaKxGs;
	private List<ZonaUsuarioDTO> listaZonaKxEn;
	private List<ZonaUsuarioDTO> listaZonaKxAr;
	private List<ZonaUsuarioDTO> listaZonaKxAf;
	private List<ZonaUsuarioDTO> listaZonaKxAc;
	private List<ZonaUsuarioDTO> listaZonaKxSv;
	
	private List<SucursalesUsuarioDTO> listaSucursalKxGs;
	private List<SucursalesUsuarioDTO> listaSucursalKxEn;
	private List<SucursalesUsuarioDTO> listaSucursalKxAr;
	private List<SucursalesUsuarioDTO> listaSucursalKxAf;
	private List<SucursalesUsuarioDTO> listaSucursalKxAc;
	private List<SucursalesUsuarioDTO> listaSucursalKxSv;
	
	private List<NegocioUsuarioDTO> listaNegocioKxGs;
	private List<NegocioUsuarioDTO> listaNegocioKxEn;
	private List<NegocioUsuarioDTO> listaNegocioKxAr;
	private List<NegocioUsuarioDTO> listaNegocioKxAf;
	private List<NegocioUsuarioDTO> listaNegocioKxAc;
	private List<NegocioUsuarioDTO> listaNegocioKxSv;
	
	private List<ConceptoUsuarioDTO> listaConceptoKxGs;
	private List<ConceptoUsuarioDTO> listaConceptoKxEn;
	private List<ConceptoUsuarioDTO> listaConceptoKxAr;
	private List<ConceptoUsuarioDTO> listaConceptoKxAf;
	private List<ConceptoUsuarioDTO> listaConceptoKxAc;
	private List<ConceptoUsuarioDTO> listaConceptoKxSv;
	
	/*lista del Grandes Cuentas */
	private List<ZonaUsuarioDTO> listaZonaGcGs;
	private List<ZonaUsuarioDTO> listaZonaGcEn;
	private List<ZonaUsuarioDTO> listaZonaGcAr;
	private List<ZonaUsuarioDTO> listaZonaGcAf;
	private List<ZonaUsuarioDTO> listaZonaGcAc;
	private List<ZonaUsuarioDTO> listaZonaGcSv;
	
	private List<SucursalesUsuarioDTO> listaSucursalGcGs;
	private List<SucursalesUsuarioDTO> listaSucursalGcEn;
	private List<SucursalesUsuarioDTO> listaSucursalGcAr;
	private List<SucursalesUsuarioDTO> listaSucursalGcAf;
	private List<SucursalesUsuarioDTO> listaSucursalGcAc;
	private List<SucursalesUsuarioDTO> listaSucursalGcSv;
	
	private List<NegocioUsuarioDTO> listaNegocioGcGs;
	private List<NegocioUsuarioDTO> listaNegocioGcEn;
	private List<NegocioUsuarioDTO> listaNegocioGcAr;
	private List<NegocioUsuarioDTO> listaNegocioGcAf;
	private List<NegocioUsuarioDTO> listaNegocioGcAc;
	private List<NegocioUsuarioDTO> listaNegocioGcSv;
	
	private List<ConceptoUsuarioDTO> listaConceptoGcGs;
	private List<ConceptoUsuarioDTO> listaConceptoGcEn;
	private List<ConceptoUsuarioDTO> listaConceptoGcAr;
	private List<ConceptoUsuarioDTO> listaConceptoGcAf;
	private List<ConceptoUsuarioDTO> listaConceptoGcAc;
	private List<ConceptoUsuarioDTO> listaConceptoGcSv;	
	
	
	/*lista del Mixto */
	private List<ZonaUsuarioDTO> listaZonaMxGs;
	private List<ZonaUsuarioDTO> listaZonaMxEn;
	private List<ZonaUsuarioDTO> listaZonaMxAr;
	private List<ZonaUsuarioDTO> listaZonaMxAf;
	private List<ZonaUsuarioDTO> listaZonaMxAc;
	private List<ZonaUsuarioDTO> listaZonaMxSv;
	
	private List<SucursalesUsuarioDTO> listaSucursalMxGs;
	private List<SucursalesUsuarioDTO> listaSucursalMxEn;
	private List<SucursalesUsuarioDTO> listaSucursalMxAr;
	private List<SucursalesUsuarioDTO> listaSucursalMxAf;
	private List<SucursalesUsuarioDTO> listaSucursalMxAc;
	private List<SucursalesUsuarioDTO> listaSucursalMxSv;
	
	private List<NegocioUsuarioDTO> listaNegocioMxGs;
	private List<NegocioUsuarioDTO> listaNegocioMxEn;
	private List<NegocioUsuarioDTO> listaNegocioMxAr;
	private List<NegocioUsuarioDTO> listaNegocioMxAf;
	private List<NegocioUsuarioDTO> listaNegocioMxAc;
	private List<NegocioUsuarioDTO> listaNegocioMxSv;
	
	private List<ConceptoUsuarioDTO> listaConceptoMxGs;
	private List<ConceptoUsuarioDTO> listaConceptoMxEn;
	private List<ConceptoUsuarioDTO> listaConceptoMxAr;
	private List<ConceptoUsuarioDTO> listaConceptoMxAf;
	private List<ConceptoUsuarioDTO> listaConceptoMxAc;
	private List<ConceptoUsuarioDTO> listaConceptoMxSv;	
	
	
	private List<CanalUsuarioCargo> listaCanalUsuarioCargos;
	private List<PerfilFuncionCanal> listaPerfilFuncionCanals;
	private List<ZonaSucursalNegocioConcepto> listaZonaSucursalNegocioConceptos;
	
	
	
	@SuppressWarnings("unchecked")
	@Create
    public void init(){
    	log.debug("iniciando alumnoMasivoAction...");
    	try{
    		ListaSucrsales = (List<Sucursal>)entityManager.createQuery("select suc from Sucursal suc order by suc.descripcion asc ").getResultList();
    		listaconceptoNegocios = (List<ConceptosNegocio>)
    									entityManager.createQuery("select cn from ConceptosNegocio cn ").getResultList();    		
    		listasUsuarios = (List<Usuariosegur>)entityManager
				.createQuery("select u from Usuariosegur u where u.alias <> null and u.nombre <> null  order by u.nombre")
				.getResultList();
    		
    		listasCargos = (List<LcredCargos>)entityManager.createQuery("select c from LcredCargos c order by c.desCargo asc ").getResultList();
    		
    		
    		
    		for(int i=0; i<this.NUMERO;i++){ /*llena de lista de zona*/
    			if(i == 0){
    				this.setListaZonaKxGs(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaGcGs(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaMxGs(intranetSapService.sacarListaZonas(true));
    			}else if(i == 1){
    				this.setListaZonaKxEn(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaGcEn(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaMxEn(intranetSapService.sacarListaZonas(true));
    			}else if(i == 2){
    				this.setListaZonaKxAr(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaGcAr(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaMxAr(intranetSapService.sacarListaZonas(true));
    			}else if(i == 3){
    				this.setListaZonaKxAf(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaGcAf(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaMxAf(intranetSapService.sacarListaZonas(true));
    			}else if(i == 4){
    				this.setListaZonaKxAc(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaGcAc(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaMxAc(intranetSapService.sacarListaZonas(true));
    			}else if(i == 5){
    				this.setListaZonaKxSv(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaGcSv(intranetSapService.sacarListaZonas(true));
    				this.setListaZonaMxSv(intranetSapService.sacarListaZonas(true));
    			}
    		}

    		for(int i=0; i<this.NUMERO;i++){/*llena de lista de sucursales*/
    			if(i == 0){
    				this.setListaSucursalKxGs(intranetSapService.sacarListaSucursales(true));
    				this.setListaSucursalGcGs(intranetSapService.sacarListaSucursales(true));
    				this.setListaSucursalMxGs(intranetSapService.sacarListaSucursales(true));
    			}else if(i == 1){
    				this.setListaSucursalKxEn(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalGcEn(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalMxEn(intranetSapService.sacarListaSucursales(false));
    			}else if(i == 2){
    				this.setListaSucursalKxAr(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalGcAr(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalMxAr(intranetSapService.sacarListaSucursales(false));
    			}else if(i == 3){
    				this.setListaSucursalKxAf(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalGcAf(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalMxAf(intranetSapService.sacarListaSucursales(false));
    			}else if(i == 4){
    				this.setListaSucursalKxAc(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalGcAc(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalMxAc(intranetSapService.sacarListaSucursales(false));
    			}else if(i == 5){
    				this.setListaSucursalKxSv(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalGcSv(intranetSapService.sacarListaSucursales(false));
    				this.setListaSucursalMxSv(intranetSapService.sacarListaSucursales(false));
    			}
    		}

    		for(int i=0; i<this.NUMERO;i++){/* sacar la lista de negocios*/
    			if(i == 0){
    				this.setListaNegocioKxGs(intranetSapService.sacarListaNegocios(true));
    				this.setListaNegocioGcGs(intranetSapService.sacarListaNegocios(true));
    				this.setListaNegocioMxGs(intranetSapService.sacarListaNegocios(true));
    			}else if(i == 1){
    				this.setListaNegocioKxEn(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioGcEn(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioMxEn(intranetSapService.sacarListaNegocios(false));    				
    			}else if(i == 2){
    				this.setListaNegocioKxAr(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioGcAr(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioMxAr(intranetSapService.sacarListaNegocios(false));    				
    			}else if(i == 3){
    				this.setListaNegocioKxAf(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioGcAf(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioMxAf(intranetSapService.sacarListaNegocios(false));    				
    			}else if(i == 4){
    				this.setListaNegocioKxAc(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioGcAc(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioMxAc(intranetSapService.sacarListaNegocios(false));    				
    			}else if(i == 5){
    				this.setListaNegocioKxSv(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioGcSv(intranetSapService.sacarListaNegocios(false));
    				this.setListaNegocioMxSv(intranetSapService.sacarListaNegocios(false));    				
    			}
    		}

    		for(int i=0; i<this.NUMERO;i++){/* sacar las listas conceptos*/
    			if(i == 0){
    				this.setListaConceptoKxGs(intranetSapService.sacarListaConceptos(true));
    				this.setListaConceptoGcGs(intranetSapService.sacarListaConceptos(true));
    				this.setListaConceptoMxGs(intranetSapService.sacarListaConceptos(true));
    			}else if(i == 1){
    				this.setListaConceptoKxEn(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoGcEn(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoMxEn(intranetSapService.sacarListaConceptos(false));
    			}else if(i == 2){
    				this.setListaConceptoKxAr(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoGcAr(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoMxAr(intranetSapService.sacarListaConceptos(false));
    			}else if(i == 3){
    				this.setListaConceptoKxAf(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoGcAf(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoMxAf(intranetSapService.sacarListaConceptos(false));
    			}else if(i == 4){
    				this.setListaConceptoKxAc(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoGcAc(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoMxAc(intranetSapService.sacarListaConceptos(false));
    			}else if(i == 5){
    				this.setListaConceptoKxSv(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoGcSv(intranetSapService.sacarListaConceptos(false));
    				this.setListaConceptoMxSv(intranetSapService.sacarListaConceptos(false));
    			}
    		}

    		
    		
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar la lista de restricciones del archivo de carga de alumnos: #0", e.getMessage());
		}
    	return;    	
    	
    }
	
	@Destroy
    public void destroy() {
    	log.debug("destruyendo instancia alumnoMasivoAction");
    }
	
	
	
	
	/**
	 * Archivo subido de la planilla de alumno
	 */
	private List<UploadItem> uploadXls = new ArrayList<UploadItem>();
	@BypassInterceptors public List<UploadItem> getUploadXls() { return uploadXls; }
	@BypassInterceptors public void setUploadXls(List<UploadItem> uploadXls) { this.uploadXls = uploadXls; }
	
	@BypassInterceptors
	public void uploadFile(UploadEvent uploadEvent) throws IOException{
		log.debug("uploadFile()...");
		try {
			if(uploadEvent == null) return;
	      
			for (UploadItem uploadItem : uploadEvent.getUploadItems()) {
				if(!this.uploadXls.contains(uploadItem))
					this.uploadXls.add(uploadItem);
			}
		} catch (Exception e) {
			log.debug("Error en listener de evento de subida de archivo. Detalle es: #0", e.getMessage());
		}
	}

	
	public void limpiaListas() {
		log.debug("limpiaListas()...");
	}	
	
	public void cancelar() {
		cargado = false;
		entityManager.clear();
	}
	/**
	 * This method is used to read the data's from an excel file.
	 * 
	 * @param fileName
	 *            - Name of the excel file.
	 */
	public void readExcelFile() {

		try {
			/**
			 * Create a new instance for FileInputStream class
			 */
			if(uploadXls == null || uploadXls.isEmpty()) {
				FacesMessages.instance().add(Severity.ERROR, "El archivo  no ha sido cargado." );
				return;
			}
			if(uploadXls.get(0) == null) {
				FacesMessages.instance().add(Severity.ERROR, "El archivo no ha sido cargado." );
				return;
			} 
			
			
			File archivo = uploadXls.get(0).getFile();
			Vector objeto = leeArchivo(archivo);

			if(objeto == null){
				FacesMessages.instance().add(Severity.ERROR, "No se pudo leer los datos el archivo subido." );
				return;			
				
			}else{
				 if(objeto.size() == 1){
						FacesMessages.instance().add(Severity.ERROR, "No existen registro en la planilla." );
						return;						 
				 }
				String mensaje = null;
				int fila = 1;
				if(objeto != null && !objeto.isEmpty()) {
					Iterator ite = objeto.iterator();
					while(ite.hasNext()) {
						Vector nuevo = (Vector) ite.next();
						if(fila == 1){
						    if(mensaje != null){
								FacesMessages.instance().add(Severity.ERROR, mensaje);
								break;							    	
						    }
						    fila++;
						}else{
							boolean ingresado = agregaAlumnos(nuevo,fila);
							if(ingresado == false){
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			log.debug("Error de carga");
			return;
		}


		cargado = true;
		uploadXls = new ArrayList<UploadItem>();
	}
	
	
		
	private boolean agregaAlumnos(Vector condatos, int fila) {
		String apruebaRechazo = null;String analsisEjecutivo = null;String analsisFinanciero = null; String comentar = null; 
		StringBuffer cadena = new StringBuffer();
		int cantidad = 0;
		log.debug("agregaApoderados()...");

		try{
		if(condatos != null && !condatos.isEmpty()){
			int columna = 1; 
			Iterator ite = condatos.iterator();
			while(ite.hasNext()) {
				Object nuevo = (Object) ite.next();	
				if(columna == 1){
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("usuario : #0", nuevo.toString());
						this.usuario = sacarUsuario(nuevo.toString());
						if(this.usuario != null){
							log.debug("nomnbre de usuario : #0", this.usuario.getNombre());
							obtenerUsuarioAndEliminar();
						}else{
							log.debug("no se encontro el usuario : #0", nuevo.toString());
							return false;
						}
					}	
				}else if(columna == 2){
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("cargos: #0", nuevo.toString());
						this.cargo = sacarCargo(nuevo.toString());
						if(this.cargo != null){
							log.debug("codigo del cargo: #0", this.cargo.getDesCargo());
						}else{
							if(this.usuario != null){
								log.debug("usuario que no tiene cargo: #0", this.usuario.getNombre());
								return false;
							}	
						}
					} 				
				}else if(columna == 3){
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("canales: #0", nuevo.toString());
						if((nuevo.toString()).equals("Todos")){
							this.canales = new String[3];
							this.canales[0]="KX";
							this.canales[1]="GC";
							this.canales[2]="MX";
						}else if((nuevo.toString()).equals("KEX-Mixto")){
							this.canales = new String[2];
							this.canales[0]="KX";
							this.canales[1]="MX";							
						}else if((nuevo.toString()).equals("KEX")){
							this.canales = new String[1];
							this.canales[0]="KX";
							
						}
					}					
				}else if(columna == 4){
					int i = 0;
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("negocios: #0", nuevo.toString());
						String[] negocio = (nuevo.toString()).split("-");
						if(negocio != null && negocio.length > 0){
							for(String neg : negocio){
								if(neg.equals("Todos")){
						    		for(int ii=0; ii<this.NUMERO;ii++){/* sacar la lista de negocios*/
						    			if(ii == 0){

						    			}else if(ii == 1){
						    				this.setListaNegocioKxEn(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioGcEn(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioMxEn(intranetSapService.sacarListaNegocios(true));    				
						    			}else if(ii == 2){
						    				this.setListaNegocioKxAr(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioGcAr(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioMxAr(intranetSapService.sacarListaNegocios(true));    				
						    			}else if(ii == 3){
						    				this.setListaNegocioKxAf(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioGcAf(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioMxAf(intranetSapService.sacarListaNegocios(true));    				
						    			}else if(ii == 4){
						    				this.setListaNegocioKxAc(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioGcAc(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioMxAc(intranetSapService.sacarListaNegocios(true));    				
						    			}else if(ii == 5){
						    				this.setListaNegocioKxSv(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioGcSv(intranetSapService.sacarListaNegocios(true));
						    				this.setListaNegocioMxSv(intranetSapService.sacarListaNegocios(true));    				
						    			}
						    		}									
								}else{
									ConceptosNegocio cn = sacarConceptoNegocio(neg);	
									if(cn != null){
										this.setListaNegocioGcAc(tickeatObjeto(this.listaNegocioGcAc, cn.getDesNegocio().trim()));
										this.setListaNegocioGcAf(tickeatObjeto(this.listaNegocioGcAf, cn.getDesNegocio().trim()));
										this.setListaNegocioGcAr(tickeatObjeto(this.listaNegocioGcAr, cn.getDesNegocio().trim()));
										this.setListaNegocioGcEn(tickeatObjeto(this.listaNegocioGcEn, cn.getDesNegocio().trim()));
										this.setListaNegocioGcSv(tickeatObjeto(this.listaNegocioGcSv, cn.getDesNegocio().trim()));
		
										this.setListaNegocioKxAc(tickeatObjeto(this.listaNegocioKxAc, cn.getDesNegocio().trim()));
										this.setListaNegocioKxAf(tickeatObjeto(this.listaNegocioKxAf, cn.getDesNegocio().trim()));
										this.setListaNegocioKxAr(tickeatObjeto(this.listaNegocioKxAr, cn.getDesNegocio().trim()));
										this.setListaNegocioKxEn(tickeatObjeto(this.listaNegocioKxEn, cn.getDesNegocio().trim()));
										this.setListaNegocioKxSv(tickeatObjeto(this.listaNegocioKxSv, cn.getDesNegocio().trim()));
		
										this.setListaNegocioMxAc(tickeatObjeto(this.listaNegocioMxAc, cn.getDesNegocio().trim()));
										this.setListaNegocioMxAf(tickeatObjeto(this.listaNegocioMxAf, cn.getDesNegocio().trim()));
										this.setListaNegocioMxAr(tickeatObjeto(this.listaNegocioMxAr, cn.getDesNegocio().trim()));
										this.setListaNegocioMxEn(tickeatObjeto(this.listaNegocioMxEn, cn.getDesNegocio().trim()));
										this.setListaNegocioMxSv(tickeatObjeto(this.listaNegocioMxSv, cn.getDesNegocio().trim()));
									}
									i++;									
								}
							}
						}
						

					} 				
				}else if(columna == 5){
					int i = 0;
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("conceptos : #0",nuevo.toString());
						String cadenaLimpia = (nuevo.toString()).replace(".0", "");
						String[] conceptos = (cadenaLimpia).split("-");
						if(conceptos != null && conceptos.length > 0){
							for(String conc : conceptos){
								if(conc.equals("Todos")){
						    		for(int ii=0; ii<this.NUMERO;ii++){/* sacar las listas conceptos*/
						    			if(ii == 0){

						    			}else if(ii == 1){
						    				this.setListaConceptoKxEn(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoGcEn(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoMxEn(intranetSapService.sacarListaConceptos(true));
						    			}else if(ii == 2){
						    				this.setListaConceptoKxAr(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoGcAr(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoMxAr(intranetSapService.sacarListaConceptos(true));
						    			}else if(ii == 3){
						    				this.setListaConceptoKxAf(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoGcAf(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoMxAf(intranetSapService.sacarListaConceptos(true));
						    			}else if(ii == 4){
						    				this.setListaConceptoKxAc(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoGcAc(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoMxAc(intranetSapService.sacarListaConceptos(true));
						    			}else if(ii == 5){
						    				this.setListaConceptoKxSv(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoGcSv(intranetSapService.sacarListaConceptos(true));
						    				this.setListaConceptoMxSv(intranetSapService.sacarListaConceptos(true));
						    			}
						    		}
								}else{
									ConceptosNegocio cn = sacarConceptoNegocioConcepto(conc);	
									if(cn != null){
										this.setListaConceptoGcAc(tickeatObjetoPorCodigoConcepto(this.listaConceptoGcAc, cn));
										this.setListaConceptoGcAf(tickeatObjetoPorCodigoConcepto(this.listaConceptoGcAf, cn));
										this.setListaConceptoGcAr(tickeatObjetoPorCodigoConcepto(this.listaConceptoGcAr, cn));
										this.setListaConceptoGcEn(tickeatObjetoPorCodigoConcepto(this.listaConceptoGcEn, cn));
										this.setListaConceptoGcSv(tickeatObjetoPorCodigoConcepto(this.listaConceptoGcSv, cn));
		
										this.setListaConceptoKxAc(tickeatObjetoPorCodigoConcepto(this.listaConceptoKxAc, cn));
										this.setListaConceptoKxAf(tickeatObjetoPorCodigoConcepto(this.listaConceptoKxAf, cn));
										this.setListaConceptoKxAr(tickeatObjetoPorCodigoConcepto(this.listaConceptoKxAr, cn));
										this.setListaConceptoKxEn(tickeatObjetoPorCodigoConcepto(this.listaConceptoKxEn, cn));
										this.setListaConceptoKxSv(tickeatObjetoPorCodigoConcepto(this.listaConceptoKxSv, cn));
		
										this.setListaConceptoMxAc(tickeatObjetoPorCodigoConcepto(this.listaConceptoMxAc, cn));
										this.setListaConceptoMxAf(tickeatObjetoPorCodigoConcepto(this.listaConceptoMxAf, cn));
										this.setListaConceptoMxAr(tickeatObjetoPorCodigoConcepto(this.listaConceptoMxAr, cn));
										this.setListaConceptoMxEn(tickeatObjetoPorCodigoConcepto(this.listaConceptoMxEn, cn));
										this.setListaConceptoMxSv(tickeatObjetoPorCodigoConcepto(this.listaConceptoMxSv, cn));
									}
								}
							}
						}
					} 				
				}else if(columna == 6){
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("sucursales : #0",nuevo.toString());
						String[] sucursales = (nuevo.toString()).split("-");
						if(sucursales != null && sucursales.length > 0){
							for(String suc : sucursales){
								if(suc.equals("Todas")){
						    		for(int i=0; i<this.NUMERO;i++){/*llena de lista de sucursales*/
						    			if(i == 0){

						    			}else if(i == 1){
						    				this.setListaSucursalKxEn(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalGcEn(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalMxEn(intranetSapService.sacarListaSucursales(true));
						    			}else if(i == 2){
						    				this.setListaSucursalKxAr(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalGcAr(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalMxAr(intranetSapService.sacarListaSucursales(true));
						    			}else if(i == 3){
						    				this.setListaSucursalKxAf(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalGcAf(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalMxAf(intranetSapService.sacarListaSucursales(true));
						    			}else if(i == 4){
						    				this.setListaSucursalKxAc(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalGcAc(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalMxAc(intranetSapService.sacarListaSucursales(true));
						    			}else if(i == 5){
						    				this.setListaSucursalKxSv(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalGcSv(intranetSapService.sacarListaSucursales(true));
						    				this.setListaSucursalMxSv(intranetSapService.sacarListaSucursales(true));
						    			}
						    		}
								}else{
									Sucursal sucur = sacarSucursal(suc);	
									if(sucur != null){
										this.setListaSucursalGcAc(tickeatObjetoPorCodigoSucursal(this.listaSucursalGcAc, sucur.getDescripcion()));
										this.setListaSucursalGcAf(tickeatObjetoPorCodigoSucursal(this.listaSucursalGcAf, sucur.getDescripcion()));
										this.setListaSucursalGcAr(tickeatObjetoPorCodigoSucursal(this.listaSucursalGcAr, sucur.getDescripcion()));
										this.setListaSucursalKxEn(tickeatObjetoPorCodigoSucursal(this.listaSucursalGcEn, sucur.getDescripcion()));
										this.setListaSucursalGcSv(tickeatObjetoPorCodigoSucursal(this.listaSucursalGcSv, sucur.getDescripcion()));
		
										this.setListaSucursalKxAc(tickeatObjetoPorCodigoSucursal(this.listaSucursalKxAc, sucur.getDescripcion()));
										this.setListaSucursalKxAf(tickeatObjetoPorCodigoSucursal(this.listaSucursalKxAf, sucur.getDescripcion()));
										this.setListaSucursalKxAr(tickeatObjetoPorCodigoSucursal(this.listaSucursalKxAr, sucur.getDescripcion()));
										this.setListaSucursalKxEn(tickeatObjetoPorCodigoSucursal(this.listaSucursalKxEn, sucur.getDescripcion()));
										this.setListaSucursalKxSv(tickeatObjetoPorCodigoSucursal(this.listaSucursalKxSv, sucur.getDescripcion()));
		
										this.setListaSucursalMxAc(tickeatObjetoPorCodigoSucursal(this.listaSucursalMxAc, sucur.getDescripcion()));
										this.setListaSucursalMxAf(tickeatObjetoPorCodigoSucursal(this.listaSucursalMxAf, sucur.getDescripcion()));
										this.setListaSucursalMxAr(tickeatObjetoPorCodigoSucursal(this.listaSucursalMxAr, sucur.getDescripcion()));
										this.setListaSucursalKxEn(tickeatObjetoPorCodigoSucursal(this.listaSucursalMxEn, sucur.getDescripcion()));
										this.setListaSucursalMxSv(tickeatObjetoPorCodigoSucursal(this.listaSucursalMxSv, sucur.getDescripcion()));
									}
								}
							}
						}
					} 				
				}else if(columna == 7 || columna == 8 || columna == 9 || columna == 10 ){
					if(columna == 7){
						if(nuevo != null && !nuevo.toString().isEmpty()) {
							log.debug("aprueba y rechaza: #0",nuevo.toString());
							if(nuevo.toString().equals("Si") || nuevo.toString().equals("SI")){
								for(String canal : this.canales){
									if(canal.equals("KX")){
										perfiles = new String[1];
										perfiles[0] =  "1";
									}else if(canal.equals("GC")){
										perfilesGC = new String[1];
										perfilesGC[0] =  "1";
									}else if(canal.equals("MX")){
										perfilesMX = new String[1];
										perfilesMX[0] =  "1";
									}
								}
							}
						} 						
					}else if(columna == 8){
						if(nuevo != null && !nuevo.toString().isEmpty()) {
							log.debug("analisis y ejecujtivo: #0",nuevo.toString());
							if(nuevo.toString().equals("Si") || nuevo.toString().equals("SI")){
								cadena.append("3");
								cadena.append(",");
							}
						} 							
					}else if(columna == 9){
						if(nuevo != null && !nuevo.toString().isEmpty()) {
							log.debug("analisis y financieros: #0",nuevo.toString());
							if(nuevo.toString().equals("Si") || nuevo.toString().equals("SI")){
								cadena.append("4");
								cadena.append(",");
							}
						}	

					}else if(columna == 10){
						if(nuevo != null && !nuevo.toString().isEmpty()) {
							log.debug("comentar: #0",nuevo.toString());
							if(nuevo.toString().equals("Si") || nuevo.toString().equals("SI")){
								cadena.append("5");
								cadena.append(",");
							}
						}	
					}
					
					if(columna == 10){
						if(!cadena.toString().equals("")){
							String[] array = (cadena.toString()).split(",");
							if(array != null && array.length > 0){
								for(String canal : this.canales){
									if(canal.equals("KX")){
										perfiles2 = array;
									}else if(canal.equals("GC")){
										perfiles2GC = array;
									}else if(canal.equals("MX")){
										perfiles2MX = array;
									}
								}
							}
						}
					}
				}else if(columna == 11){
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("Aprob Individual KEX : #0",nuevo.toString());
						String cadenaLimpia = (nuevo.toString()).replace("MM$", "");
						cadenaLimpia = cadenaLimpia.replace(" ", "");
						
						for(String canal : this.canales){
							if(canal.equals("KX")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeKX((long)0);
									this.setMontoHastaKX((long)0);
								}else if(cadenaLimpia.equals("0-3")){
									this.setMontoDesdeKX((long)0);
									this.setMontoHastaKX((long)3000000);									
								}else if(cadenaLimpia.equals("0-18")){
									this.setMontoDesdeKX((long)0);
									this.setMontoHastaKX((long)18000000);									
								}else if(cadenaLimpia.equals("15-18")){
									this.setMontoDesdeKX((long)15000000);
									this.setMontoHastaKX((long)18000000);									
								}else if(cadenaLimpia.equals("0-15")){
									this.setMontoDesdeKX((long)0);
									this.setMontoHastaKX((long)15000000);									
								}else if(cadenaLimpia.equals("0-10")){
									this.setMontoDesdeKX((long)0);
									this.setMontoHastaKX((long)10000000);									
								}else if(cadenaLimpia.equals("0-6")){
									this.setMontoDesdeKX((long)0);
									this.setMontoHastaKX((long)6000000);									
								}else if(cadenaLimpia.equals("0-1,5")){
									this.setMontoDesdeKX((long)0);
									this.setMontoHastaKX((long)1500000);									
								}else if(cadenaLimpia.equals("0-50")){
									this.setMontoDesdeKX((long)0);
									this.setMontoHastaKX((long)50000000);									
								}
							}else if(canal.equals("GC")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)0);
								}else if(cadenaLimpia.equals("0-3")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)3000000);									
								}else if(cadenaLimpia.equals("0-18")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)18000000);									
								}else if(cadenaLimpia.equals("15-18")){
									this.setMontoDesdeGC((long)15000000);
									this.setMontoHastaGC((long)18000000);									
								}else if(cadenaLimpia.equals("0-15")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)15000000);									
								}else if(cadenaLimpia.equals("0-10")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)10000000);									
								}else if(cadenaLimpia.equals("0-6")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)6000000);									
								}else if(cadenaLimpia.equals("0-1,5")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)1500000);									
								}else if(cadenaLimpia.equals("0-50")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)50000000);									
								}
								
							}else if(canal.equals("MX")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)0);
								}else if(cadenaLimpia.equals("0-3")){
									this.setMontoDesdeKX((long)0);
									this.setMontoHastaKX((long)3000000);									
								}else if(cadenaLimpia.equals("0-18")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)18000000);									
								}else if(cadenaLimpia.equals("15-18")){
									this.setMontoDesdeMX((long)15000000);
									this.setMontoHastaMX((long)18000000);									
								}else if(cadenaLimpia.equals("0-15")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)15000000);									
								}else if(cadenaLimpia.equals("0-10")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)10000000);									
								}else if(cadenaLimpia.equals("0-6")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)6000000);									
								}else if(cadenaLimpia.equals("0-1,5")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)1500000);									
								}else if(cadenaLimpia.equals("0-50")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)50000000);									
								}
								
							}
						}						
					}	
				}else if(columna == 12){
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("Aprob Compartida KEX : #0",nuevo.toString());
						String cadenaLimpia = (nuevo.toString()).replace("MM$", "");
						cadenaLimpia = cadenaLimpia.replace("MM", "");
						cadenaLimpia = cadenaLimpia.replace("M", "");
						cadenaLimpia = cadenaLimpia.replace("$", "");
						cadenaLimpia = cadenaLimpia.replace(" ", "");
						
						for(String canal : this.canales){
							if(canal.equals("KX")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeCompKX((long)0);
									this.setMontoHastaCompKX((long)0);
								}else if(cadenaLimpia.equals("3,1-1")){
									this.setMontoDesdeCompKX((long)3000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompKX(monto.longValue());									
								}else if(cadenaLimpia.equals("18,1-1")){
									this.setMontoDesdeCompKX((long)18000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompKX(monto.longValue());									
								}else if(cadenaLimpia.equals("15,1-1")){
									this.setMontoDesdeCompKX((long)15000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompKX(monto.longValue());									
								}else if(cadenaLimpia.equals("10,1-1")){
									this.setMontoDesdeCompKX((long)10000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompKX(monto.longValue());									
								}else if(cadenaLimpia.equals("6,1-1")){
									this.setMontoDesdeCompKX((long)6000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompKX(monto.longValue());									
								}else if(cadenaLimpia.equals("50,1-1")){
									this.setMontoDesdeCompKX((long)50000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompKX(monto.longValue());									
								}
							}else if(canal.equals("GC")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeCompGC((long)0);
									this.setMontoHastaCompGC((long)0);
								}else if(cadenaLimpia.equals("3,1-1")){
									this.setMontoDesdeCompKX((long)3000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompKX(monto.longValue());									
								}else if(cadenaLimpia.equals("18,1-1")){
									this.setMontoDesdeCompGC((long)18000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());									
								}else if(cadenaLimpia.equals("15,1-1")){
									this.setMontoDesdeCompGC((long)15000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());									
								}else if(cadenaLimpia.equals("10,1-1")){
									this.setMontoDesdeCompGC((long)10000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());									
								}else if(cadenaLimpia.equals("6,1-1")){
									this.setMontoDesdeCompGC((long)6000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());									
								}else if(cadenaLimpia.equals("50,1-1")){
									this.setMontoDesdeCompGC((long)50000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());									
								}								
							}else if(canal.equals("MX")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeCompMX((long)0);
									this.setMontoHastaCompMX((long)0);
								}else if(cadenaLimpia.equals("3,1-1")){
									this.setMontoDesdeCompKX((long)3000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompKX(monto.longValue());									
								}else if(cadenaLimpia.equals("18,1-1")){
									this.setMontoDesdeCompMX((long)18000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());									
								}else if(cadenaLimpia.equals("15,1-1")){
									this.setMontoDesdeCompMX((long)15000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());									
								}else if(cadenaLimpia.equals("10,1-1")){
									this.setMontoDesdeCompMX((long)10000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());									
								}else if(cadenaLimpia.equals("6,1-1")){
									this.setMontoDesdeCompMX((long)6000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());									
								}else if(cadenaLimpia.equals("50,1-1")){
									this.setMontoDesdeCompMX((long)50000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());									
								}								
							}
						}
						

					}	
				}else if(columna == 13){
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("Aprob Individual GC y Mixto : #0",nuevo.toString());
						String cadenaLimpia = (nuevo.toString()).replace("MM$", "");
						cadenaLimpia = cadenaLimpia.replace("MM", "");
						cadenaLimpia = cadenaLimpia.replace("M", "");
						cadenaLimpia = cadenaLimpia.replace("$", "");
						cadenaLimpia = cadenaLimpia.replace(" ", "");					
						
						for(String canal : this.canales){
							if(canal.equals("KX")){

								
							}else if(canal.equals("GC")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)0);
								}else if(cadenaLimpia.equals("0-18")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)18000000);									
								}else if(cadenaLimpia.equals("15,1-18")){
									this.setMontoDesdeGC((long)15000001);
									this.setMontoHastaGC((long)18000000);									
								}else if(cadenaLimpia.equals("0-15")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)15000000);									
								}else if(cadenaLimpia.equals("0-10")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)10000000);									
								}else if(cadenaLimpia.equals("0-3")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)3000000);									
								}else if(cadenaLimpia.equals("0-50")){
									this.setMontoDesdeGC((long)0);
									this.setMontoHastaGC((long)50000000);									
								}
							}else if(canal.equals("MX")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)0);
								}else if(cadenaLimpia.equals("0-18")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)18000000);									
								}else if(cadenaLimpia.equals("15,1-18")){
									this.setMontoDesdeMX((long)15000001);
									this.setMontoHastaMX((long)18000000);									
								}else if(cadenaLimpia.equals("0-15")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)15000000);									
								}else if(cadenaLimpia.equals("0-10")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)10000000);									
								}else if(cadenaLimpia.equals("0-3")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)3000000);									
								}else if(cadenaLimpia.equals("0-50")){
									this.setMontoDesdeMX((long)0);
									this.setMontoHastaMX((long)50000000);									
								}
							}
						}
					}	
				}else if(columna == 14){
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("Aprob Compartida GC y Mixto : #0",nuevo.toString());
						String cadenaLimpia = (nuevo.toString()).replace("MM$", "");
						cadenaLimpia = cadenaLimpia.replace("MM", "");
						cadenaLimpia = cadenaLimpia.replace("M", "");
						cadenaLimpia = cadenaLimpia.replace("$", "");
						cadenaLimpia = cadenaLimpia.replace(" ", "");
						
						for(String canal : this.canales){
							if(canal.equals("KX")){

							}else if(canal.equals("GC")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeCompGC((long)0);
									this.setMontoHastaCompGC((long)0);									
								}else if(cadenaLimpia.equals("18,1-1")){
									this.setMontoDesdeCompGC((long)18000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());	
									
								}else if(cadenaLimpia.equals("15,1-1")){
									this.setMontoDesdeCompGC((long)15000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());	
									
								}else if(cadenaLimpia.equals("10,1-1")){
									this.setMontoDesdeCompGC((long)10000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());	
									
								}else if(cadenaLimpia.equals("3,1-1")){
									this.setMontoDesdeCompGC((long)3000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());	
								}else if(cadenaLimpia.equals("50,1-1")){
									this.setMontoDesdeCompGC((long)50000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompGC(monto.longValue());	
								}
							}else if(canal.equals("MX")){
								if(cadenaLimpia.equals("0") || cadenaLimpia.equals("0.0")){
									this.setMontoDesdeCompMX((long)0);
									this.setMontoHastaCompMX((long)0);									
								}else if(cadenaLimpia.equals("18,1-1")){
									this.setMontoDesdeCompMX((long)18000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());	
									
								}else if(cadenaLimpia.equals("15,1-1")){
									this.setMontoDesdeCompMX((long)15000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());	
									
								}else if(cadenaLimpia.equals("10,1-1")){
									this.setMontoDesdeCompMX((long)10000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());	
									
								}else if(cadenaLimpia.equals("3,1-1")){
									this.setMontoDesdeCompMX((long)3000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());
									
								}else if(cadenaLimpia.equals("50,1-1")){
									this.setMontoDesdeCompMX((long)50000001);
									BigDecimal monto = new BigDecimal(String.valueOf("1000000000000000"));
									this.setMontoHastaCompMX(monto.longValue());	
								}
							}
						}						
					}	
				}else if(columna == 15){
					if(nuevo != null && !nuevo.toString().isEmpty()) {
						log.debug("envío automatico otras solicitudes: #0",nuevo.toString());
						if(nuevo.toString().equals("SI") || nuevo.toString().equals("Si")){
							this.otrasSolictudes = true;
						}else{
							this.otrasSolictudes = false;
						}
					}	
				}
				columna++;
			}
			return insertarInformacionUsuario();
		}
		
		}catch (Exception e) {
			log.error("error al sacar los datos #0", e.getMessage());
			return false;
		}
		return true;
	}	
	
	
    public boolean insertarInformacionUsuario(){
    	boolean existe = false;
    	if(validarInformacion()){
    		try{
    			
    			if(this.usuario != null){
    				this.usuario.setCargo(this.cargo.getDesCargo());
    				this.usuario.setObligatorio(false);
    				this.usuario.setEliminado(false);
        			entityManager.merge(this.usuario);
        			entityManager.flush();
        			entityManager.refresh(this.usuario);
    				
    			}

    			
    			
    			UsuarioCargo usuarioAux = scoringService.getUsuarioCargo(usuario.getIdPersonal());
    			if(usuarioAux == null){
    				usuarioAux = new UsuarioCargo();
    				usuarioAux.setUsuario(usuario);
    				existe = false;
    			}else{
    				existe = true;
    			}
    			
    			usuarioAux.setCargo(cargo);
				usuarioAux.setZona(ConceptosType.ZONA);
				usuarioAux.setSucursal(ConceptosType.SUCURSAL);
				usuarioAux.setNegocio(ConceptosType.NEGOCIO);
				usuarioAux.setConcepto(ConceptosType.CONCEPTO);
   				usuarioAux.setAdministrador(false);
   				
    			if(this.otrasSolictudes){
    				usuarioAux.setEnvioAutomatico(this.otrasSolictudes);
    			}else{
    				usuarioAux.setEnvioAutomatico(false);
    			}
    			
    			if(existe){
    				entityManager.merge(usuarioAux);
    			}else{
    				entityManager.persist(usuarioAux);
    			}
    			
    			if(usuarioAux != null){
    				for(String canal : this.canales){
    					if(canal.equals(TipoCuentasKupferType.KX.name())){/* seleccion del perfil de kupfer express*/
    						CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
    						canalUsuarioCargo.setUsuarioCargo(usuarioAux);
    						canalUsuarioCargo.setTipoCuenta(TipoCuentasKupferType.KX);
    						entityManager.persist(canalUsuarioCargo);
    						if(perfiles != null){
	    						if(canalUsuarioCargo != null){
	        						/*ingreso especial de generar solicitud KX*/
	        						try{
	    								PerfilFuncionCanal perfilFuncionCanalKX = new PerfilFuncionCanal();
	    								perfilFuncionCanalKX.setCanalUsuarioCargo(canalUsuarioCargo);
	    								perfilFuncionCanalKX.setFuncion(FuncionesType.GENERARSOLICITUD);
	    								perfilFuncionCanalKX.setMontoMinimo(this.montoDesdeKX);
	    								perfilFuncionCanalKX.setMontoMaximo(this.montoHastaKX);
	    								perfilFuncionCanalKX.setMontoMinimoCompartido(this.montoDesdeCompKX);
	    								perfilFuncionCanalKX.setMontoMaximoCompartido(this.montoHastaCompKX);
	    								entityManager.persist(perfilFuncionCanalKX);
	    								if(perfilFuncionCanalKX != null){
    										for(ZonaUsuarioDTO z : this.listaZonaKxGs){
    											if(z.isStatus()==true){
    												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    												zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
    												zsnc.setTipoConcepto(ConceptosType.ZONA);
    												zsnc.setCodigo(z.getCodigo());
    												zsnc.setDescripcion(z.getDescripcion());
    												entityManager.persist(zsnc);
    											}
    										}
    										for(SucursalesUsuarioDTO s : this.listaSucursalKxGs){
    											if(s.isStatus()==true){
    												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    												zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
    												zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
    												zsnc.setCodigo(s.getCodigo());
    												zsnc.setDescripcion(s.getDescripcion());
    												entityManager.persist(zsnc);
    											}
    										}
    										for(NegocioUsuarioDTO n : this.listaNegocioKxGs){
    											if(n.isStatus()==true){
    												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    												zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
    												zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
    												zsnc.setCodigo(n.getCodigo());
    												zsnc.setDescripcion(n.getDescripcion());
    												entityManager.persist(zsnc);
    											}
    										}
    										for(ConceptoUsuarioDTO c : this.listaConceptoKxGs){
    											if(c.isStatus()==true){
    												ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    												zsnc.setPerfilFuncionCanal(perfilFuncionCanalKX);
    												zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
    												zsnc.setCodigo(c.getCodigo());
    												zsnc.setDescripcion(c.getDescripcion());
    												entityManager.persist(zsnc);
    											}
    										}
	    								}
	        						}catch (Exception e) {
	    									log.error("Error, al ingresar de la información generar solicitudes ", e.getMessage());
	    							}	
	        						/*fin del ingreso es*/	    							
	    							for(String perfil: perfiles){
	    								if(perfil.equals("1")){//perfil para aprobar y/o rechazar
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.APRUEBARECHAZO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeKX);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaKX);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompKX);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompKX);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaKxAr){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalKxAr){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioKxAr){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoKxAr){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	
	    								}else if(perfil.equals("2")){//perfil para Agregar Comenatrios
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.AGREGARCOMENTARIO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeKX);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaKX);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompKX);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompKX);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaKxAc){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalKxAc){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioKxAc){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoKxAc){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	    								}
	    							}
	    							//fin de recorrido de perfiles
	    							
	    							if(perfiles2 != null){
	    								//comienzo de recorrido de perfiles2
		       							for(String perfil2: perfiles2){
		    								if(perfil2.equals("3")){//perfil de analista de negocio.
		    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
		    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
		    	    							perfilFuncionCanal.setFuncion(FuncionesType.ANALISTANEGOCIO);
		    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeKX);
		    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaKX);
		    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompKX);
		    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompKX);
		    	    							entityManager.persist(perfilFuncionCanal);
		    	    							if(perfilFuncionCanal != null){
		    	    								if(this.zona){
		    	    									for(ZonaUsuarioDTO z : this.listaZonaKxEn){
		    	    										if(z.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
		    	    											zsnc.setCodigo(z.getCodigo());
		    	    											zsnc.setDescripcion(z.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}
		    	    								if(this.sucursal){
		    	    									for(SucursalesUsuarioDTO s : this.listaSucursalKxEn){
		    	    										if(s.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
		    	    											zsnc.setCodigo(s.getCodigo());
		    	    											zsnc.setDescripcion(s.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}
		    	    								if(this.negocio){
		    	    									for(NegocioUsuarioDTO n : this.listaNegocioKxEn){
		    	    										if(n.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
		    	    											zsnc.setCodigo(n.getCodigo());
		    	    											zsnc.setDescripcion(n.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}    	    							
		    	    								if(this.concepto){
		    	    									for(ConceptoUsuarioDTO c : this.listaConceptoKxEn){
		    	    										if(c.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
		    	    											zsnc.setCodigo(c.getCodigo());
		    	    											zsnc.setDescripcion(c.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}      	    								
		    	    							}
		    	    							
		    								}else if(perfil2.equals("4")){//perfil para analisis financiero
		    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
		    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
		    	    							perfilFuncionCanal.setFuncion(FuncionesType.ANALISTAFINANCIERO);
		    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeKX);
		    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaKX);
		    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompKX);
		    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompKX);
		    	    							entityManager.persist(perfilFuncionCanal);
		    	    							if(perfilFuncionCanal != null){
		    	    								if(this.zona){
		    	    									for(ZonaUsuarioDTO z : this.listaZonaKxAf){
		    	    										if(z.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
		    	    											zsnc.setCodigo(z.getCodigo());
		    	    											zsnc.setDescripcion(z.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}
		    	    								if(this.sucursal){
		    	    									for(SucursalesUsuarioDTO s : this.listaSucursalKxAf){
		    	    										if(s.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
		    	    											zsnc.setCodigo(s.getCodigo());
		    	    											zsnc.setDescripcion(s.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}
		    	    								if(this.negocio){
		    	    									for(NegocioUsuarioDTO n : this.listaNegocioKxAf){
		    	    										if(n.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
		    	    											zsnc.setCodigo(n.getCodigo());
		    	    											zsnc.setDescripcion(n.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}    	    							
		    	    								if(this.concepto){
		    	    									for(ConceptoUsuarioDTO c : this.listaConceptoKxAf){
		    	    										if(c.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
		    	    											zsnc.setCodigo(c.getCodigo());
		    	    											zsnc.setDescripcion(c.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}      	    								
		    	    							}
		
		    								}else if(perfil2.equals("5")){//perfil para solo visualizar
		    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
		    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
		    	    							perfilFuncionCanal.setFuncion(FuncionesType.SOLOVISUALIZAR);
		    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeKX);
		    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaKX);
		    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompKX);
		    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompKX);
		    	    							entityManager.persist(perfilFuncionCanal);
		    	    							if(perfilFuncionCanal != null){
		    	    								if(this.zona){
		    	    									for(ZonaUsuarioDTO z : this.listaZonaKxSv){
		    	    										if(z.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
		    	    											zsnc.setCodigo(z.getCodigo());
		    	    											zsnc.setDescripcion(z.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}
		    	    								if(this.sucursal){
		    	    									for(SucursalesUsuarioDTO s : this.listaSucursalKxSv){
		    	    										if(s.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
		    	    											zsnc.setCodigo(s.getCodigo());
		    	    											zsnc.setDescripcion(s.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}
		    	    								if(this.negocio){
		    	    									for(NegocioUsuarioDTO n : this.listaNegocioKxSv){
		    	    										if(n.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
		    	    											zsnc.setCodigo(n.getCodigo());
		    	    											zsnc.setDescripcion(n.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}    	    							
		    	    								if(this.concepto){
		    	    									for(ConceptoUsuarioDTO c : this.listaConceptoKxSv){
		    	    										if(c.isStatus()==true){
		    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
		    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
		    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
		    	    											zsnc.setCodigo(c.getCodigo());
		    	    											zsnc.setDescripcion(c.getDescripcion());
		    	    											entityManager.persist(zsnc);
		    	    										}
		    	    									}
		    	    								}      	    								
		    	    							}
		    								}
		    							}//fin de recorrido de perfiles2   
	    						  }	
	    						}
    						}
    					}else if(canal.equals(TipoCuentasKupferType.GC.name())){/* seleccion del perfil de Grandes Cuentas*/
    						CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
    						canalUsuarioCargo.setUsuarioCargo(usuarioAux);
    						canalUsuarioCargo.setTipoCuenta(TipoCuentasKupferType.GC);
    						entityManager.persist(canalUsuarioCargo);
    						if(canalUsuarioCargo != null){
    							
        						/*ingreso especial de generar solicitud de GC*/
        						try{
        							PerfilFuncionCanal perfilFuncionCanalGC = new PerfilFuncionCanal();
        							perfilFuncionCanalGC.setCanalUsuarioCargo(canalUsuarioCargo);
        							perfilFuncionCanalGC.setFuncion(FuncionesType.GENERARSOLICITUD);
        							perfilFuncionCanalGC.setMontoMinimo(this.montoDesdeGC);
        							perfilFuncionCanalGC.setMontoMaximo(this.montoHastaGC);
        							perfilFuncionCanalGC.setMontoMinimoCompartido(this.montoDesdeCompGC);
        							perfilFuncionCanalGC.setMontoMaximoCompartido(this.montoHastaCompGC);
	    							entityManager.persist(perfilFuncionCanalGC);
	    							if(perfilFuncionCanalGC != null){
	    								
    									for(ZonaUsuarioDTO z : this.listaZonaGcGs){
    										if(z.isStatus()==true){
    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    											zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
    											zsnc.setTipoConcepto(ConceptosType.ZONA);
    											zsnc.setCodigo(z.getCodigo());
    											zsnc.setDescripcion(z.getDescripcion());
    											entityManager.persist(zsnc);
    										}
    									}
    								
    									for(SucursalesUsuarioDTO s : this.listaSucursalGcGs){
    										if(s.isStatus()==true){
    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    											zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
    											zsnc.setCodigo(s.getCodigo());
    											zsnc.setDescripcion(s.getDescripcion());
    											entityManager.persist(zsnc);
    										}
    									}
    								
    									for(NegocioUsuarioDTO n : this.listaNegocioGcGs){
    										if(n.isStatus()==true){
    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    											zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
    											zsnc.setCodigo(n.getCodigo());
    											zsnc.setDescripcion(n.getDescripcion());
    											entityManager.persist(zsnc);
    										}
    									}

    									for(ConceptoUsuarioDTO c : this.listaConceptoGcGs){
    										if(c.isStatus()==true){
    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    											zsnc.setPerfilFuncionCanal(perfilFuncionCanalGC);
    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
    											zsnc.setCodigo(c.getCodigo());
    											zsnc.setDescripcion(c.getDescripcion());
    											entityManager.persist(zsnc);
    										}
    									}
	    							}
        						}catch (Exception e) {
									log.error("Error, al ingresar la información de generar solicitudes de GC", e.getMessage());
								}
    							/*fin ingreso especial de  generar solicitud de GC*/
    							
    							
    							
    							if(perfilesGC != null){
	    							for(String perfil: perfilesGC){
	    								if(perfil.equals("1")){//perfil para aprobar y/o rechazar
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.APRUEBARECHAZO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeGC);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaGC);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompGC);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompGC);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaGcAr){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalGcAr){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioGcAr){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoGcAr){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	
	    								}else if(perfil.equals("2")){//perfil para Agregar Comenatrios
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.AGREGARCOMENTARIO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeGC);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaGC);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompGC);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompGC);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaGcAc){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalGcAc){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioGcAc){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoGcAc){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	    								}
	    							}
	    							//fin de recorrido de perfilesGC
    							}
    							
    							if(perfiles2GC != null){
    								//comienzo de recorrido de perfiles2GC
	       							for(String perfil2: perfiles2GC){
	    								if(perfil2.equals("3")){//perfil de analista de negocio.
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.ANALISTANEGOCIO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeGC);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaGC);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompGC);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompGC);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaGcEn){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalGcEn){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioGcEn){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoGcEn){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	    	    							
	    								}else if(perfil2.equals("4")){//perfil para analisis financiero
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.ANALISTAFINANCIERO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeGC);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaGC);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompGC);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompGC);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaGcAf){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalGcAf){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioGcAf){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoGcAf){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	
	    								}else if(perfil2.equals("5")){//perfil para solo visualizar
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.SOLOVISUALIZAR);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeGC);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaGC);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompGC);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompGC);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaGcSv){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalGcSv){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioGcSv){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoGcSv){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	    								}
	    							}//fin de recorrido de perfiles2GC  
    							}	
    						}
    					}else if(canal.equals(TipoCuentasKupferType.MX.name())){/* seleccion del perfil de Mixto*/
    						CanalUsuarioCargo canalUsuarioCargo = new CanalUsuarioCargo();
    						canalUsuarioCargo.setUsuarioCargo(usuarioAux);
    						canalUsuarioCargo.setTipoCuenta(TipoCuentasKupferType.MX);
    						entityManager.persist(canalUsuarioCargo);
    						if(canalUsuarioCargo != null){
        						/*ingreso especial de generar solicitud de MX*/
        						try{
        							
	    							PerfilFuncionCanal perfilFuncionCanalMX = new PerfilFuncionCanal();
	    							perfilFuncionCanalMX.setCanalUsuarioCargo(canalUsuarioCargo);
	    							perfilFuncionCanalMX.setFuncion(FuncionesType.GENERARSOLICITUD);
	    							perfilFuncionCanalMX.setMontoMinimo(this.montoDesdeMX);
	    							perfilFuncionCanalMX.setMontoMaximo(this.montoHastaMX);
	    							perfilFuncionCanalMX.setMontoMinimoCompartido(this.montoDesdeCompMX);
	    							perfilFuncionCanalMX.setMontoMaximoCompartido(this.montoHastaCompMX);
	    							entityManager.persist(perfilFuncionCanalMX);
	    							if(perfilFuncionCanalMX != null){
    									for(ZonaUsuarioDTO z : this.listaZonaMxGs){
    										if(z.isStatus()==true){
    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    											zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
    											zsnc.setTipoConcepto(ConceptosType.ZONA);
    											zsnc.setCodigo(z.getCodigo());
    											zsnc.setDescripcion(z.getDescripcion());
    											entityManager.persist(zsnc);
    										}
    									}
    									for(SucursalesUsuarioDTO s : this.listaSucursalMxGs){
    										if(s.isStatus()==true){
    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    											zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
    											zsnc.setCodigo(s.getCodigo());
    											zsnc.setDescripcion(s.getDescripcion());
    											entityManager.persist(zsnc);
    										}
    									}
    									for(NegocioUsuarioDTO n : this.listaNegocioMxGs){
    										if(n.isStatus()==true){
    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    											zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
    											zsnc.setCodigo(n.getCodigo());
    											zsnc.setDescripcion(n.getDescripcion());
    											entityManager.persist(zsnc);
    										}
    									}
    									for(ConceptoUsuarioDTO c : this.listaConceptoMxGs){
    										if(c.isStatus()==true){
    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
    											zsnc.setPerfilFuncionCanal(perfilFuncionCanalMX);
    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
    											zsnc.setCodigo(c.getCodigo());
    											zsnc.setDescripcion(c.getDescripcion());
    											entityManager.persist(zsnc);
    										}
    									}
	    							}
        						}catch (Exception e) {
									log.error("Error, al ingresar la información de generar solicitudes de MX", e.getMessage());
								}
    							/*fin ingreso especial de  generar solicitud de MX*/    							
    							
    							
    							if(perfilesMX != null){
	    							for(String perfil: perfilesMX){
	    								if(perfil.equals("1")){//perfil para aprobar y/o rechazar
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.APRUEBARECHAZO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeMX);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaMX);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompMX);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompMX);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaMxAr){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalMxAr){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioMxAr){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoMxAr){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	
	    								}else if(perfil.equals("2")){//perfil para Agregar Comenatrios
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.AGREGARCOMENTARIO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeMX);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaMX);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompMX);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompMX);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaMxAc){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalMxAc){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioMxAc){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoMxAc){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	    								}
	    							}
	    							//fin de recorrido de perfilesMX
    							}
    							
    							if(perfiles2MX != null){
    								//comienzo de recorrido de perfiles2MX
	       							for(String perfil2: perfiles2MX){
	    								if(perfil2.equals("3")){//perfil de analista de negocio.
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.ANALISTANEGOCIO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeMX);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaMX);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompMX);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompMX);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaMxEn){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalMxEn){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioMxEn){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoMxEn){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	    	    							
	    								}else if(perfil2.equals("4")){//perfil para analisis financiero
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.ANALISTAFINANCIERO);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeMX);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaMX);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompMX);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompMX);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaMxAf){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalMxAf){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioMxAf){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoMxAf){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	
	    								}else if(perfil2.equals("5")){//perfil para solo visualizar
	    	    							PerfilFuncionCanal perfilFuncionCanal = new PerfilFuncionCanal();
	    	    							perfilFuncionCanal.setCanalUsuarioCargo(canalUsuarioCargo);
	    	    							perfilFuncionCanal.setFuncion(FuncionesType.SOLOVISUALIZAR);
	    	    							perfilFuncionCanal.setMontoMinimo(this.montoDesdeMX);
	    	    							perfilFuncionCanal.setMontoMaximo(this.montoHastaMX);
	    	    							perfilFuncionCanal.setMontoMinimoCompartido(this.montoDesdeCompMX);
	    	    							perfilFuncionCanal.setMontoMaximoCompartido(this.montoHastaCompMX);
	    	    							entityManager.persist(perfilFuncionCanal);
	    	    							if(perfilFuncionCanal != null){
	    	    								if(this.zona){
	    	    									for(ZonaUsuarioDTO z : this.listaZonaMxSv){
	    	    										if(z.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.ZONA);
	    	    											zsnc.setCodigo(z.getCodigo());
	    	    											zsnc.setDescripcion(z.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.sucursal){
	    	    									for(SucursalesUsuarioDTO s : this.listaSucursalMxSv){
	    	    										if(s.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.SUCURSAL);
	    	    											zsnc.setCodigo(s.getCodigo());
	    	    											zsnc.setDescripcion(s.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}
	    	    								if(this.negocio){
	    	    									for(NegocioUsuarioDTO n : this.listaNegocioMxSv){
	    	    										if(n.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.NEGOCIO);
	    	    											zsnc.setCodigo(n.getCodigo());
	    	    											zsnc.setDescripcion(n.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}    	    							
	    	    								if(this.concepto){
	    	    									for(ConceptoUsuarioDTO c : this.listaConceptoMxSv){
	    	    										if(c.isStatus()==true){
	    	    											ZonaSucursalNegocioConcepto zsnc = new ZonaSucursalNegocioConcepto();
	    	    											zsnc.setPerfilFuncionCanal(perfilFuncionCanal);
	    	    											zsnc.setTipoConcepto(ConceptosType.CONCEPTO);
	    	    											zsnc.setCodigo(c.getCodigo());
	    	    											zsnc.setDescripcion(c.getDescripcion());
	    	    											entityManager.persist(zsnc);
	    	    										}
	    	    									}
	    	    								}      	    								
	    	    							}
	    								}
	    							}//fin de recorrido de perfiles2MX 
    							}	
    						}
    					}
    				}
    			}
    			entityManager.flush();
    			limpiarDatos();
    			FacesMessages.instance().add(Severity.INFO,"Los datos fueron asociado al usuario exitosamente...!!");
    			entityManager.clear();
    			return true;
    		}catch (Exception e) {
    			log.error("Error, al insertar los datos #0 ", e.getMessage());
    			return false;
    		}
    		
    	}else{
    		return false;
    	}
    }
    public boolean validarInformacion(){
    	
    	if(this.usuario == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar el usuario .");
			return false;
    	}
    	
    	if(this.cargo == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar el cargo del usuario .");
			return false;
    		
    	}

    	if(this.conceptos == null){
			FacesMessages.instance().add(Severity.ERROR,"Seleccione un cargo que tenga algun concepto asociado.");
			return false;
    	}else{
    		boolean existen = false;
    		if(this.conceptos.length == 0){
    			FacesMessages.instance().add(Severity.ERROR,"Seleccione un cargo que tenga algun concepto asociado.");
    			return false;    			
    		}else{
    			for(String dato : this.conceptos){
    				if(dato != null && !"".equals(dato)){
    					existen = true;
    					break;
    				}
    			}
    			if(existen == false){
        			FacesMessages.instance().add(Severity.ERROR,"Seleccione un cargo que tenga algun concepto asociado.");
        			return false;     				
    			}
    		}
    	}
    	
    	
    	if(this.canales == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun canal para asociar los datos.");
			return false;
    	}else{
    		boolean existen = false;
    		if(this.canales.length == 0){
    			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun canal para asociar los datos.");
    			return false;    			
    		}else{
    			for(String dato : this.canales){
    				if(dato != null && !"".equals(dato)){
    					existen = validarCanal(dato);
    					if(existen == false) break;
    				}
    			}
    			if(existen == false){
        			return false;     				
    			}
    			
    		}
    	}
    	return true;
    }
    
    public boolean validarCanal(String canal){
    	boolean validar = false;
    	if(canal.equals(TipoCuentasKupferType.KX.name())){
    		validar = validarKx();
    	}else if(canal.equals(TipoCuentasKupferType.GC.name())){
    		validar = validarGc();
    	}else if(canal.equals(TipoCuentasKupferType.MX.name())){
    		validar = validarMx();
    	}
    	
    	return validar;
    }
    public boolean validarKx(){
		if(this.perfiles == null && this.perfiles2 == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en kupfer express.");
			return false;    		
		}
		
	   	if(this.perfiles != null){
			boolean existen = false;
			int cant = this.perfiles.length;
			int contNulo =  0;
				for(String dato : this.perfiles){
					if(dato != null && !"".equals(dato)){
	    					existen = validarListasKx(dato);
	    					if(existen == false) break;
    				}else{
    					contNulo++;
    				}
    			}
				if(cant == contNulo){
					existen = true;
				}
    			if(existen == false){
        			return false;     				
    			}
	    	}
//	   	    else{
//				FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en kupfer express.");
//				return false;   	    		
//	    	}
	 
	       	if(this.perfiles2 != null){
	    		boolean existen = false;
	    		if(this.perfiles2.length == 0){
	    			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario.");
				return false;    			
			}else{
				int cant = this.perfiles2.length;
				int contNulo =  0;
				for(String dato : this.perfiles2){
					if(dato != null && !"".equals(dato)){
						existen = validarListasKx(dato);
						if(existen == false) break;
					}else{
						contNulo++;
					}
				}
				if(cant == contNulo){
					existen = true;
				}				
				if(existen == false){
	    			return false;     				
				}
				
			}
		}       	
		
	   	if(this.montoDesdeKX == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto minimo de aprobación del canal kupfer express.");
			return false;       		
	   	}
	   	if(this.montoHastaKX == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto maximo de aprobación del canal kupfer express.");
			return false;       		
	   	}       	
	   	
	   	
	   	if(this.montoDesdeCompKX == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto minimo de aprobación compartido del canal kupfer express.");
			return false;       		
	   	}
	   	if(this.montoHastaCompKX == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto maximo de aprobación compartido del canal kupfer express.");
			return false;       		
	   	} 
	   	
    	
    	return true;
    }
    public boolean validarGc(){
    	if(this.perfilesGC == null && this.perfiles2GC == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
			return false;    		
    	}
    	
    	
       	if(this.perfilesGC != null){
    		boolean existen = false;
			int cant = this.perfilesGC.length;
			int contNulo =  0;
			for(String dato : this.perfilesGC){
				if(dato != null && !"".equals(dato)){
					existen = validarListasGc(dato);
					if(existen == false) break;
				}else{
					contNulo++;
				}
			}
			if(cant == contNulo){
				existen = true;
			}
			if(existen == false){
    			return false;     				
			}
    	}
//       	else{
//			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
//			return false;      		
//    	}
 
       	if(this.perfiles2GC != null){
    		boolean existen = false;
    		if(this.perfiles2GC.length == 0){
    			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
    			   			
    		}else{
				int cant = this.perfiles2GC.length;
				int contNulo =  0;
    			for(String dato : this.perfiles2GC){
    				if(dato != null && !"".equals(dato)){
    					existen = validarListasKx(dato);
    					if(existen == false) break;
    				}else{
    					contNulo++;
    				}
    			}
    			if(cant == contNulo){
    				existen = true;
    			}
    			if(existen == false){
        			return false;     				
    			}
    			
    		}
    	}       	
    	
       	if(this.montoDesdeGC == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto minimo de aprobación del canal Grande Cuentas.");
			return false;       		
       	}
       	if(this.montoHastaGC == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto maximo de aprobación del canal Grande Cuentas.");
			return false;       		
       	}       	
       	
       	
       	if(this.montoDesdeCompGC == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto minimo de aprobación compartido del canal Grande Cuentas.");
			return false;       		
       	}
       	if(this.montoHastaCompGC == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto maximo de aprobación compartido del canal Grande Cuentas.");
			return false;       		
       	} 
       	

    	
    	return true;   	
    }
    public boolean validarMx(){
    	if(this.perfilesMX == null && this.perfiles2MX == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
			return false;    		
    	}
    	
    	
       	if(this.perfilesMX != null){
    		boolean existen = false;
    		if(this.perfilesMX.length == 0){
    			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
    			return false;    			
    		}else{
				int cant = this.perfilesMX.length;
				int contNulo =  0;
    			for(String dato : this.perfilesMX){
    				if(dato != null && !"".equals(dato)){
    					existen = validarListasMx(dato);
    					if(existen == false) break;
    				
    				}else{
    					contNulo++;
    				}
    			}
    			if(cant == contNulo){
    				existen = true;
    			}
    			
    			if(existen == false){
        			return false;     				
    			}
    			
    		}
    	}
 
       	if(this.perfiles2MX != null){
    		boolean existen = false;
    		if(this.perfiles2MX.length == 0){
    			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna función para el usuario en Grande Cuentas.");
    			return false;    			
    		}else{
				int cant = this.perfiles2MX.length;
				int contNulo =  0;
    			for(String dato : this.perfiles2MX){
    				if(dato != null && !"".equals(dato)){
    					existen = validarListasMx(dato);
    					if(existen == false) break;
    				}else{
    					contNulo++;
    				}
    			}
    			if(cant == contNulo){
    				existen = true;
    			}    			
    			
    			if(existen == false){
        			return false;     				
    			}
    			
    		}
    	}       	
    	
       	if(this.montoDesdeMX == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto minimo de aprobación del canal Mixto.");
			return false;       		
       	}
       	if(this.montoHastaMX == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto maximo de aprobación del canal Mixto.");
			return false;       		
       	}       	
       	

       	
       	if(this.montoDesdeCompMX == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto minimo de aprobación compartido del canal Mixto.");
			return false;       		
       	}
       	if(this.montoHastaCompMX == null){
			FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el monto maximo de aprobación compartido del canal Mixto.");
			return false;       		
       	} 
       	
	
    	
    	return true;   	
    }	
    
    
    public boolean validarListasKx(String codigo){
    	int zona =0; int sucursal = 0; int negocio = 0; int concepto = 0;
        if(codigo.equals("1")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaKxAr);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Kupfer Express de función Aprueba/Rechaza.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalKxAr);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Aprueba/Rechaza.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioKxAr);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Kupfer Express de función Aprueba/Rechaza.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoKxAr);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Kupfer Express de función Aprueba/Rechaza.");
        			return false;     			
        		}  		

    		}    		
    		
    		
    	}else if(codigo.equals("2")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaKxAc);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Kupfer Express de función Agregar Cometarios.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalKxAc);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Agregar Cometarios.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioKxAc);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Kupfer Express de función Agregar Cometarios.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoKxAc);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Kupfer Express de función Agregar Cometarios.");
        			return false;     			
        		}  		

    		}      	
    		
    	}else if(codigo.equals("3")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaKxEn);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Kupfer Express de función Ejecutivo Negocio.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalKxEn);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Ejecutivo Negocio.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioKxEn);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Kupfer Express de función Ejecutivo Negocio.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoKxEn);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Kupfer Express de función Ejecutivo Negocio.");
        			return false;     			
        		}  		

    		}    	
    	}else if(codigo.equals("4")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaKxAf);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Kupfer Express de función Analista Financiero.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalKxAf);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Analista Financiero.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioKxAf);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Kupfer Express de función Analista Financiero.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoKxAf);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Kupfer Express de función Analista Financiero.");
        			return false;     			
        		}  		

    		}   
    		
    		
    	}else if(codigo.equals("5")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaKxSv);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Kupfer Express de función Solo Visualizar.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalKxSv);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Kupfer Express de función Solo Visualizar.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioKxSv);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Kupfer Express de función Solo Visualizar.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoKxSv);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Kupfer Express de función Solo Visualizar.");
        			return false;     			
        		}  		

    		}    		
    	}
    	return true;
    }
    public boolean validarListasGc(String codigo){
    	int zona =0; int sucursal = 0; int negocio = 0; int concepto = 0;
    	if(codigo.equals("1")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaGcAr);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Grande Cuentas de función Aprueba/Rechaza.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalGcAr);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Aprueba/Rechaza.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioGcAr);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Grande Cuentas de función Aprueba/Rechaza.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoGcAr);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Grande Cuentas de función Aprueba/Rechaza.");
        			return false;     			
        		}  		

    		}    		
    		
    		
    	}else if(codigo.equals("2")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaGcAc);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Grande Cuentas de función Agregar Cometarios.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalGcAc);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Agregar Cometarios.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioGcAc);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Grande Cuentas de función Agregar Cometarios.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoGcAc);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Grande Cuentas de función Agregar Cometarios.");
        			return false;     			
        		}  		

    		}      	
    		
    	}else if(codigo.equals("3")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaGcEn);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Grande Cuentas de función Ejecutivo Negocio.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalGcEn);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Ejecutivo Negocio.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioGcEn);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Grande Cuentas de función Ejecutivo Negocio.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoGcEn);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Grande Cuentas de función Ejecutivo Negocio.");
        			return false;     			
        		}  		

    		}    	
    	}else if(codigo.equals("4")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaGcAf);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Grande Cuentas de función Analista Financiero.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalGcAf);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Analista Financiero.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioGcAf);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Grande Cuentas de función Analista Financiero.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoGcAf);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Grande Cuentas de función Analista Financiero.");
        			return false;     			
        		}  		

    		}   
    		
    		
    	}else if(codigo.equals("5")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaGcSv);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Grande Cuentas de función Solo Visualizar.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalGcSv);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Grande Cuentas de función Solo Visualizar.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioGcSv);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Grande Cuentas de función Solo Visualizar.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoGcSv);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Grande Cuentas de función Solo Visualizar.");
        			return false;     			
        		}  		

    		}    		
    	}
    	return true;
    }
    public boolean validarListasMx(String codigo){
    	int zona =0; int sucursal = 0; int negocio = 0; int concepto = 0;
        if(codigo.equals("1")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaMxAr);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Mixto de función Aprueba/Rechaza.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalMxAr);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Mixto de función Aprueba/Rechaza.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioMxAr);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Mixto de función Aprueba/Rechaza.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoMxAr);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Mixto de función Aprueba/Rechaza.");
        			return false;     			
        		}  		

    		}    		
    		
    		
    	}else if(codigo.equals("2")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaMxAc);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Mixto de función Agregar Cometarios.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalMxAc);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Mixto de función Agregar Cometarios.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioMxAc);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Mixto de función Agregar Cometarios.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoMxAc);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Mixto de función Agregar Cometarios.");
        			return false;     			
        		}  		

    		}      	
    		
    	}else if(codigo.equals("3")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaMxEn);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Mixto de función Ejecutivo Negocio.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalMxEn);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Mixto de función Ejecutivo Negocio.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioMxEn);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Mixto de función Ejecutivo Negocio.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoMxEn);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Mixto de función Ejecutivo Negocio.");
        			return false;     			
        		}  		

    		}    	
    	}else if(codigo.equals("4")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaMxAf);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Mixto de función Analista Financiero.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalMxAf);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Mixto de función Analista Financiero.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioMxAf);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Mixto de función Analista Financiero.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoMxAf);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Mixto de función Analista Financiero.");
        			return false;     			
        		}  		

    		}   
    		
    		
    	}else if(codigo.equals("5")){
    		if(this.zona){
    			zona = validarZona(this.listaZonaMxSv);
        		if(zona == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna zona en el canal Mixto de función Solo Visualizar.");
        			return false;     			
        		}    			
    		}
    		if(this.sucursal){
    			sucursal = validarSucursal(this.listaSucursalMxSv);
        		if(sucursal == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar alguna sucursal en el canal Mixto de función Solo Visualizar.");
        			return false;     			
        		}  		
    		}
    		if(this.negocio){
    			negocio = validarNegocio(this.listaNegocioMxSv);
        		if(negocio == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun negocio en el canal Mixto de función Solo Visualizar.");
        			return false;     			
        		}  		
    		}
    		if(this.concepto){
    			concepto = validarConcepto(this.listaConceptoMxSv);
        		if(concepto == 0){
        			FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar algun concepto en el canal Mixto de función Solo Visualizar.");
        			return false;     			
        		}  		

    		}    		
    	}
    	return true;
    }

    
    public int validarZona(List<ZonaUsuarioDTO> listaZona){
    	int cantidad = 0;
    	for(ZonaUsuarioDTO z : listaZona){
    		if(z.isStatus() == true) cantidad++;
    	}
    	return cantidad;
    }
    public int validarSucursal(List<SucursalesUsuarioDTO> listaSucursal){
    	int cantidad = 0;
    	for(SucursalesUsuarioDTO s : listaSucursal){
    		if(s.isStatus() == true) cantidad++;
    	}
    	return cantidad;
    }
    public int validarNegocio(List<NegocioUsuarioDTO> listaNegocio){
    	int cantidad = 0;
    	for(NegocioUsuarioDTO n : listaNegocio){
    		if(n.isStatus() == true) cantidad++;
    	}
    	return cantidad;
    }    
    public int validarConcepto(List<ConceptoUsuarioDTO> listaConcepto){
    	int cantidad = 0;
    	for(ConceptoUsuarioDTO c : listaConcepto){
    		if(c.isStatus() == true) cantidad++;
    	}
    	return cantidad;
    }    
	
    
    
    
    
    public void limpiarDatos(){
    	this.setUsuario(null);
    	this.setCargo(null);
    	this.setEliminado(false);
    	this.administrador = false;
    	this.otrasSolictudes = false;
    	this.canales = null;
    	this.montoDesdeKX = (long)0;
    	this.montoHastaKX = (long)0;
    	this.montoDesdeCompKX = (long)0;
    	this.montoHastaCompKX = (long)0;
    	this.montoDesdeGC = (long)0;
    	this.montoHastaGC = (long)0;
    	this.montoDesdeCompGC = (long)0;
    	this.montoHastaCompGC = (long)0;	
    	this.montoDesdeMX = (long)0;
    	this.montoHastaMX = (long)0;
    	this.montoDesdeCompMX = (long)0;
    	this.montoHastaCompMX = (long)0;
    	this.perfiles=null;
    	this.perfiles2 = null;
    	this.perfilesGC = null;
    	this.perfiles2GC = null;
    	this.perfiles2MX = null;
    	this.perfilesMX = null;
    	
		/*desahabiltacion de los canales*/
		this.setPestanakupferExpres(true);
		this.setPestanaGrandesCuentas(true);
		this.setPestanaMixto(true);
    	
    }
    
	private Boolean cargado;
	@BypassInterceptors
	public Boolean getCargado() {
		if(cargado == null) 
			return false;
		return cargado;
	}
	@BypassInterceptors public void setCargado(Boolean cargado) { this.cargado = cargado; }		
	
	private Boolean validarRut(String rut) {
		if(rut.isEmpty() || rut.length()<8) {
			log.debug("validarRut()... false");
			return false;
		}
		log.debug("rut: #0", rut);
		log.debug("rut: #0", rut);
		String[] arg = rut.split("-");
		int M = 0, S = 1;
		int T = Integer.parseInt(arg[0]);
		char x;
		for (; T != 0; T /= 10)
			S = (S + T % 10 * (9 - M++ % 6)) % 11;

		x = (char) (S != 0 ? S + 47 : 75);
		if (arg[1].length() != 0) {
			if (arg[1].toUpperCase().charAt(0) != x) {
				log.debug("validarRut()... false");
				return false;
			}
		}
		log.debug("validarRut()... true");
		return true;
	}
	
	private String limpiaString(String cadena) {
		log.debug("Cadena sin limpiar #0", cadena);
		cadena = cadena.replaceAll("[^0-9/(k|K)/\\-]", "");
		log.debug(cadena);
		return cadena;
	}

	
	
	
	public String normalizaPalabra(String s)
	{
	  String normalizedString = Normalizer.normalize(s, Normalizer.Form.NFD);
	  StringBuilder stringBuilder = new StringBuilder();
	  log.debug("String normalizada: #0",normalizedString);
	  for (int i = 0; i < normalizedString.length(); i++)
	  {
	    char c = normalizedString.charAt(i);
	    if (Character.isLetter(c))
	      stringBuilder.append(c);
	  }
	  log.debug("StringBuilder normalizada: #0",stringBuilder.toString());
	  return stringBuilder.toString();
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public  Vector leeArchivo(File archivo){
		Vector informacionArchivo = new Vector();
		try{	
			POIFSFileSystem poifsFileSystem = null;
			try {
				poifsFileSystem = new POIFSFileSystem(new FileInputStream(archivo));
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			HSSFWorkbook hssfWorkbook = null;
			try {
				hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			Iterator iterator = hssfSheet.rowIterator();
			// Recorro datos de fila en fila
			while(iterator.hasNext()){
				HSSFRow hssfRow = (HSSFRow)iterator.next();
				Iterator iteratorAuxiliar = hssfRow.cellIterator();
				Vector informacionFila = new Vector();
					//Me barro todos los elementos de una fila
					for(short i = hssfRow.getFirstCellNum(); i < hssfRow.getLastCellNum(); i++){
						HSSFCell hssfCell = hssfRow.getCell(i);
						if(hssfCell != null){
							switch(hssfCell.getCellType()){
								case HSSFCell.CELL_TYPE_BLANK: informacionFila.add(""); break;
								case HSSFCell.CELL_TYPE_BOOLEAN: informacionFila.add(hssfCell.getBooleanCellValue() ); break;
								case HSSFCell.CELL_TYPE_FORMULA: informacionFila.add(hssfCell.getStringCellValue()) ; break;
								case HSSFCell.CELL_TYPE_NUMERIC: informacionFila.add(hssfCell.getNumericCellValue() ); break;
								case HSSFCell.CELL_TYPE_STRING: informacionFila.add(hssfCell.getStringCellValue()) ; break;
								default:
							}
						}
					}
					informacionArchivo.add(informacionFila);
			}
		}catch (Exception e) {
			log.error("Error, al sacar los datos de la planilla excel", e);
		}
		return informacionArchivo; 
	}
	
	public Usuariosegur sacarUsuario(String user){
		Usuariosegur usuasrio= null;
		try{
			if(user != null){
				for(Usuariosegur u : listasUsuarios){
					if((u.getAlias().toLowerCase()).equals(user.toLowerCase())){
						log.debug("alias #0, user #1", u.getAlias().toLowerCase(), user);
						usuasrio = u;
						break;
					}
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al buscar el usuario", e.getMessage());
		}
		return usuasrio;
	}
	public LcredCargos sacarCargo(String descripcion){
		LcredCargos cargo= null;
		try{
			if(descripcion != null){
				for(LcredCargos c : listasCargos){
					if((c.getDesCargo().trim()).equals(descripcion)){
						cargo = c;
						break;
					}
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al buscar el cargo", e.getMessage());
		}
		return cargo;
	}
	public Sucursal sacarSucursal(String descripcion){
		Sucursal sucursal= null;
		try{
			if(descripcion != null){
				for(Sucursal s : ListaSucrsales){
					if((s.getDescripcion().trim()).equals(descripcion)){
						sucursal = s;
						break;
					}
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al buscar el cargo", e.getMessage());
		}
		return sucursal;
	}
	public ConceptosNegocio sacarConceptoNegocio(String descripcion){
		ConceptosNegocio conceptoNegocio= null;
		try{
			if(descripcion != null){
				for(ConceptosNegocio cn : listaconceptoNegocios){
					if(((cn.getDesNegocio().trim()).toLowerCase()).equals(descripcion.toLowerCase())){
						log.debug("negocio #0 , descripcion #1", cn.getDesNegocio().trim(), descripcion);
						conceptoNegocio = cn;
						break;
					}
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al buscar el cargo", e.getMessage());
		}
		return conceptoNegocio;
	}
	public ConceptosNegocio sacarConceptoNegocioConcepto(String codigo){
		ConceptosNegocio conceptoNegocio= null;
		try{
			if(codigo != null){
				for(ConceptosNegocio cn : listaconceptoNegocios){
					if((cn.getConcepto().trim()).equals(codigo)){
						log.debug("codigo actual #0, codigo #1", cn.getConcepto().trim(),codigo);
						conceptoNegocio = cn;
						break;
					}
				}
			}
			
		}catch (Exception e) {
			log.error("Error, al buscar el cargo", e.getMessage());
		}
		return conceptoNegocio;
	}

	

	
	public void habilitarPertanas(){
		if(this.canales != null){
			log.debug("seleccionado #0", this.canales.length);
			
				if(verificarPestanaCanal(TipoCuentasKupferType.KX.name()) == true){
					this.pestanakupferExpres = false;
				}else{
					this.pestanakupferExpres = true;
				}
					
				if(verificarPestanaCanal(TipoCuentasKupferType.GC.name()) == true){
					this.pestanaGrandesCuentas = false;
				}else{
					this.pestanaGrandesCuentas = true;
					
				}
				if(verificarPestanaCanal(TipoCuentasKupferType.MX.name()) == true){
					this.pestanaMixto = false;
				}else{
					this.pestanaMixto = true;
				}
			
		}
	}
	public boolean verificarPestanaCanal(String canal){
		boolean exito = false;
		for(String codigo : canales){
			if(codigo.equals(canal)){
				exito = true;
				break;
			}
		}
		return exito;
	}
	
	public List<NegocioUsuarioDTO> tickeatObjeto(List<NegocioUsuarioDTO> listaActual, String descripcion){
		List<NegocioUsuarioDTO> listaFinal = new ArrayList<NegocioUsuarioDTO>();
		try{
			if(listaActual != null){
				for(NegocioUsuarioDTO dto : listaActual){
					if((dto.getDescripcion().trim()).equals(descripcion)){
						log.debug("dato #0, descripcion #1", dto.getDescripcion().trim(), descripcion);
						dto.setStatus(true);
						listaFinal.add(dto);
					}else{
						listaFinal.add(dto);
					}
				}
			}
		}catch (Exception e) {
			log.error("Error, al tickear el el objeto #0", e.getMessage());
		}
		return listaFinal;
	}
	
	public List<ConceptoUsuarioDTO> tickeatObjetoPorCodigoConcepto(List<ConceptoUsuarioDTO> listaActual, ConceptosNegocio cn){
		List<ConceptoUsuarioDTO> listaFinal = new ArrayList<ConceptoUsuarioDTO>();
		try{
			if(listaActual != null){
				for(ConceptoUsuarioDTO dto : listaActual){
					if((dto.getCodigo().trim()).equals(cn.getConcepto()) && (dto.getCodigoNegocio().trim()).equals(cn.getNegocio().trim()) ){
						log.debug("concepto #0, codigo_negocio #1 negocio #2", dto.getCodigo().trim(), dto.getCodigoNegocio().trim(),cn.getNegocio().trim());
						dto.setStatus(true);
						listaFinal.add(dto);
					}else{
						listaFinal.add(dto);
					}
				}
			}
		}catch (Exception e) {
			log.error("Error, al tickear el el objeto #0", e.getMessage());
		}
		return listaFinal;
	}

	public List<SucursalesUsuarioDTO> tickeatObjetoPorCodigoSucursal(List<SucursalesUsuarioDTO> listaActual, String descripcion){
		List<SucursalesUsuarioDTO> listaFinal = new ArrayList<SucursalesUsuarioDTO>();
		try{
			if(listaActual != null){
				for(SucursalesUsuarioDTO dto : listaActual){
					if((dto.getDescripcion().trim()).equals(descripcion.trim())){
						log.debug("sucursal  #0 ", descripcion);
						dto.setStatus(true);
						listaFinal.add(dto);
					}else{
						listaFinal.add(dto);
					}
				}
			}
		}catch (Exception e) {
			log.error("Error, al tickear el el objeto #0", e.getMessage());
		}
		return listaFinal;
	}

	public void obtenerUsuarioAndEliminar(){
		if(usuario != null ){
			log.error("Usuario : #0", usuario.getNombre());
			UsuarioCargo usuarioCargo = scoringService.getUsuarioCargo(usuario.getIdPersonal());
			if(usuarioCargo != null){
				this.setCargo(usuarioCargo.getCargo());
				
				listaCanalUsuarioCargos = scoringService.getListaCanalUsuarioCargo(usuarioCargo.getIdUsuarioCargo());
				if(listaCanalUsuarioCargos != null && listaCanalUsuarioCargos.size() > 0){
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
					
		    		if(listaZonaSucursalNegocioConceptos != null){
		    			for(ZonaSucursalNegocioConcepto zsnc : listaZonaSucursalNegocioConceptos){
		    				entityManager.remove(zsnc);
		    			}
		    			if(listaPerfilFuncionCanals != null){
		    				for(PerfilFuncionCanal pfc : listaPerfilFuncionCanals){
		    					entityManager.remove(pfc);
		    				}
		    				
		    				if(listaCanalUsuarioCargos != null){
		    					for(CanalUsuarioCargo cuc: listaCanalUsuarioCargos){
		    						entityManager.remove(cuc);
		    					}
		    				}
		    			}
		    			entityManager.flush();
		    			entityManager.clear();
		    		}
				  }else{
					  return;
				  }
			}else{
				this.setCargo(null);
			}
		}
	}	
	


	
	public boolean isAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}

	public boolean isOtrasSolictudes() {
		return otrasSolictudes;
	}

	public void setOtrasSolictudes(boolean otrasSolictudes) {
		this.otrasSolictudes = otrasSolictudes;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public Usuariosegur getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuariosegur usuario) {
		this.usuario = usuario;
	}

	public LcredCargos getCargo() {
		return cargo;
	}

	public void setCargo(LcredCargos cargo) {
		this.cargo = cargo;
	}

	public String[] getConceptos() {
		return conceptos;
	}

	public void setConceptos(String[] conceptos) {
		this.conceptos = conceptos;
	}

	public String[] getCanales() {
		return canales;
	}

	public void setCanales(String[] canales) {
		this.canales = canales;
	}

	public String[] getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(String[] perfiles) {
		this.perfiles = perfiles;
	}

	public String[] getPerfiles2() {
		return perfiles2;
	}

	public void setPerfiles2(String[] perfiles2) {
		this.perfiles2 = perfiles2;
	}

	public String[] getPerfilesGC() {
		return perfilesGC;
	}

	public void setPerfilesGC(String[] perfilesGC) {
		this.perfilesGC = perfilesGC;
	}

	public String[] getPerfiles2GC() {
		return perfiles2GC;
	}

	public void setPerfiles2GC(String[] perfiles2gc) {
		perfiles2GC = perfiles2gc;
	}

	public String[] getPerfilesMX() {
		return perfilesMX;
	}

	public void setPerfilesMX(String[] perfilesMX) {
		this.perfilesMX = perfilesMX;
	}

	public String[] getPerfiles2MX() {
		return perfiles2MX;
	}

	public void setPerfiles2MX(String[] perfiles2mx) {
		perfiles2MX = perfiles2mx;
	}

	public boolean isZona() {
		return zona;
	}

	public void setZona(boolean zona) {
		this.zona = zona;
	}

	public boolean isSucursal() {
		return sucursal;
	}

	public void setSucursal(boolean sucursal) {
		this.sucursal = sucursal;
	}

	public boolean isNegocio() {
		return negocio;
	}

	public void setNegocio(boolean negocio) {
		this.negocio = negocio;
	}

	public boolean isConcepto() {
		return concepto;
	}

	public void setConcepto(boolean concepto) {
		this.concepto = concepto;
	}

	public boolean isPestanakupferExpres() {
		return pestanakupferExpres;
	}

	public void setPestanakupferExpres(boolean pestanakupferExpres) {
		this.pestanakupferExpres = pestanakupferExpres;
	}

	public boolean isPestanaGrandesCuentas() {
		return pestanaGrandesCuentas;
	}

	public void setPestanaGrandesCuentas(boolean pestanaGrandesCuentas) {
		this.pestanaGrandesCuentas = pestanaGrandesCuentas;
	}

	public boolean isPestanaMixto() {
		return pestanaMixto;
	}

	public void setPestanaMixto(boolean pestanaMixto) {
		this.pestanaMixto = pestanaMixto;
	}

	public Long getMontoDesdeKX() {
		return montoDesdeKX;
	}

	public void setMontoDesdeKX(Long montoDesdeKX) {
		this.montoDesdeKX = montoDesdeKX;
	}

	public Long getMontoHastaKX() {
		return montoHastaKX;
	}

	public void setMontoHastaKX(Long montoHastaKX) {
		this.montoHastaKX = montoHastaKX;
	}

	public Long getMontoDesdeCompKX() {
		return montoDesdeCompKX;
	}

	public void setMontoDesdeCompKX(Long montoDesdeCompKX) {
		this.montoDesdeCompKX = montoDesdeCompKX;
	}

	public Long getMontoHastaCompKX() {
		return montoHastaCompKX;
	}

	public void setMontoHastaCompKX(Long montoHastaCompKX) {
		this.montoHastaCompKX = montoHastaCompKX;
	}

	public Long getMontoDesdeGC() {
		return montoDesdeGC;
	}

	public void setMontoDesdeGC(Long montoDesdeGC) {
		this.montoDesdeGC = montoDesdeGC;
	}

	public Long getMontoHastaGC() {
		return montoHastaGC;
	}

	public void setMontoHastaGC(Long montoHastaGC) {
		this.montoHastaGC = montoHastaGC;
	}

	public Long getMontoDesdeCompGC() {
		return montoDesdeCompGC;
	}

	public void setMontoDesdeCompGC(Long montoDesdeCompGC) {
		this.montoDesdeCompGC = montoDesdeCompGC;
	}

	public Long getMontoHastaCompGC() {
		return montoHastaCompGC;
	}

	public void setMontoHastaCompGC(Long montoHastaCompGC) {
		this.montoHastaCompGC = montoHastaCompGC;
	}

	public Long getMontoDesdeMX() {
		return montoDesdeMX;
	}

	public void setMontoDesdeMX(Long montoDesdeMX) {
		this.montoDesdeMX = montoDesdeMX;
	}

	public Long getMontoHastaMX() {
		return montoHastaMX;
	}

	public void setMontoHastaMX(Long montoHastaMX) {
		this.montoHastaMX = montoHastaMX;
	}

	public Long getMontoDesdeCompMX() {
		return montoDesdeCompMX;
	}

	public void setMontoDesdeCompMX(Long montoDesdeCompMX) {
		this.montoDesdeCompMX = montoDesdeCompMX;
	}

	public Long getMontoHastaCompMX() {
		return montoHastaCompMX;
	}

	public void setMontoHastaCompMX(Long montoHastaCompMX) {
		this.montoHastaCompMX = montoHastaCompMX;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxGs() {
		return listaZonaKxGs;
	}

	public void setListaZonaKxGs(List<ZonaUsuarioDTO> listaZonaKxGs) {
		this.listaZonaKxGs = listaZonaKxGs;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxEn() {
		return listaZonaKxEn;
	}

	public void setListaZonaKxEn(List<ZonaUsuarioDTO> listaZonaKxEn) {
		this.listaZonaKxEn = listaZonaKxEn;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxAr() {
		return listaZonaKxAr;
	}

	public void setListaZonaKxAr(List<ZonaUsuarioDTO> listaZonaKxAr) {
		this.listaZonaKxAr = listaZonaKxAr;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxAf() {
		return listaZonaKxAf;
	}

	public void setListaZonaKxAf(List<ZonaUsuarioDTO> listaZonaKxAf) {
		this.listaZonaKxAf = listaZonaKxAf;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxAc() {
		return listaZonaKxAc;
	}

	public void setListaZonaKxAc(List<ZonaUsuarioDTO> listaZonaKxAc) {
		this.listaZonaKxAc = listaZonaKxAc;
	}

	public List<ZonaUsuarioDTO> getListaZonaKxSv() {
		return listaZonaKxSv;
	}

	public void setListaZonaKxSv(List<ZonaUsuarioDTO> listaZonaKxSv) {
		this.listaZonaKxSv = listaZonaKxSv;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxGs() {
		return listaSucursalKxGs;
	}

	public void setListaSucursalKxGs(List<SucursalesUsuarioDTO> listaSucursalKxGs) {
		this.listaSucursalKxGs = listaSucursalKxGs;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxEn() {
		return listaSucursalKxEn;
	}

	public void setListaSucursalKxEn(List<SucursalesUsuarioDTO> listaSucursalKxEn) {
		this.listaSucursalKxEn = listaSucursalKxEn;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxAr() {
		return listaSucursalKxAr;
	}

	public void setListaSucursalKxAr(List<SucursalesUsuarioDTO> listaSucursalKxAr) {
		this.listaSucursalKxAr = listaSucursalKxAr;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxAf() {
		return listaSucursalKxAf;
	}

	public void setListaSucursalKxAf(List<SucursalesUsuarioDTO> listaSucursalKxAf) {
		this.listaSucursalKxAf = listaSucursalKxAf;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxAc() {
		return listaSucursalKxAc;
	}

	public void setListaSucursalKxAc(List<SucursalesUsuarioDTO> listaSucursalKxAc) {
		this.listaSucursalKxAc = listaSucursalKxAc;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalKxSv() {
		return listaSucursalKxSv;
	}

	public void setListaSucursalKxSv(List<SucursalesUsuarioDTO> listaSucursalKxSv) {
		this.listaSucursalKxSv = listaSucursalKxSv;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxGs() {
		return listaNegocioKxGs;
	}

	public void setListaNegocioKxGs(List<NegocioUsuarioDTO> listaNegocioKxGs) {
		this.listaNegocioKxGs = listaNegocioKxGs;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxEn() {
		return listaNegocioKxEn;
	}

	public void setListaNegocioKxEn(List<NegocioUsuarioDTO> listaNegocioKxEn) {
		this.listaNegocioKxEn = listaNegocioKxEn;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxAr() {
		return listaNegocioKxAr;
	}

	public void setListaNegocioKxAr(List<NegocioUsuarioDTO> listaNegocioKxAr) {
		this.listaNegocioKxAr = listaNegocioKxAr;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxAf() {
		return listaNegocioKxAf;
	}

	public void setListaNegocioKxAf(List<NegocioUsuarioDTO> listaNegocioKxAf) {
		this.listaNegocioKxAf = listaNegocioKxAf;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxAc() {
		return listaNegocioKxAc;
	}

	public void setListaNegocioKxAc(List<NegocioUsuarioDTO> listaNegocioKxAc) {
		this.listaNegocioKxAc = listaNegocioKxAc;
	}

	public List<NegocioUsuarioDTO> getListaNegocioKxSv() {
		return listaNegocioKxSv;
	}

	public void setListaNegocioKxSv(List<NegocioUsuarioDTO> listaNegocioKxSv) {
		this.listaNegocioKxSv = listaNegocioKxSv;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxGs() {
		return listaConceptoKxGs;
	}

	public void setListaConceptoKxGs(List<ConceptoUsuarioDTO> listaConceptoKxGs) {
		this.listaConceptoKxGs = listaConceptoKxGs;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxEn() {
		return listaConceptoKxEn;
	}

	public void setListaConceptoKxEn(List<ConceptoUsuarioDTO> listaConceptoKxEn) {
		this.listaConceptoKxEn = listaConceptoKxEn;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxAr() {
		return listaConceptoKxAr;
	}

	public void setListaConceptoKxAr(List<ConceptoUsuarioDTO> listaConceptoKxAr) {
		this.listaConceptoKxAr = listaConceptoKxAr;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxAf() {
		return listaConceptoKxAf;
	}

	public void setListaConceptoKxAf(List<ConceptoUsuarioDTO> listaConceptoKxAf) {
		this.listaConceptoKxAf = listaConceptoKxAf;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxAc() {
		return listaConceptoKxAc;
	}

	public void setListaConceptoKxAc(List<ConceptoUsuarioDTO> listaConceptoKxAc) {
		this.listaConceptoKxAc = listaConceptoKxAc;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoKxSv() {
		return listaConceptoKxSv;
	}

	public void setListaConceptoKxSv(List<ConceptoUsuarioDTO> listaConceptoKxSv) {
		this.listaConceptoKxSv = listaConceptoKxSv;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcGs() {
		return listaZonaGcGs;
	}

	public void setListaZonaGcGs(List<ZonaUsuarioDTO> listaZonaGcGs) {
		this.listaZonaGcGs = listaZonaGcGs;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcEn() {
		return listaZonaGcEn;
	}

	public void setListaZonaGcEn(List<ZonaUsuarioDTO> listaZonaGcEn) {
		this.listaZonaGcEn = listaZonaGcEn;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcAr() {
		return listaZonaGcAr;
	}

	public void setListaZonaGcAr(List<ZonaUsuarioDTO> listaZonaGcAr) {
		this.listaZonaGcAr = listaZonaGcAr;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcAf() {
		return listaZonaGcAf;
	}

	public void setListaZonaGcAf(List<ZonaUsuarioDTO> listaZonaGcAf) {
		this.listaZonaGcAf = listaZonaGcAf;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcAc() {
		return listaZonaGcAc;
	}

	public void setListaZonaGcAc(List<ZonaUsuarioDTO> listaZonaGcAc) {
		this.listaZonaGcAc = listaZonaGcAc;
	}

	public List<ZonaUsuarioDTO> getListaZonaGcSv() {
		return listaZonaGcSv;
	}

	public void setListaZonaGcSv(List<ZonaUsuarioDTO> listaZonaGcSv) {
		this.listaZonaGcSv = listaZonaGcSv;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcGs() {
		return listaSucursalGcGs;
	}

	public void setListaSucursalGcGs(List<SucursalesUsuarioDTO> listaSucursalGcGs) {
		this.listaSucursalGcGs = listaSucursalGcGs;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcEn() {
		return listaSucursalGcEn;
	}

	public void setListaSucursalGcEn(List<SucursalesUsuarioDTO> listaSucursalGcEn) {
		this.listaSucursalGcEn = listaSucursalGcEn;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcAr() {
		return listaSucursalGcAr;
	}

	public void setListaSucursalGcAr(List<SucursalesUsuarioDTO> listaSucursalGcAr) {
		this.listaSucursalGcAr = listaSucursalGcAr;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcAf() {
		return listaSucursalGcAf;
	}

	public void setListaSucursalGcAf(List<SucursalesUsuarioDTO> listaSucursalGcAf) {
		this.listaSucursalGcAf = listaSucursalGcAf;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcAc() {
		return listaSucursalGcAc;
	}

	public void setListaSucursalGcAc(List<SucursalesUsuarioDTO> listaSucursalGcAc) {
		this.listaSucursalGcAc = listaSucursalGcAc;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalGcSv() {
		return listaSucursalGcSv;
	}

	public void setListaSucursalGcSv(List<SucursalesUsuarioDTO> listaSucursalGcSv) {
		this.listaSucursalGcSv = listaSucursalGcSv;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcGs() {
		return listaNegocioGcGs;
	}

	public void setListaNegocioGcGs(List<NegocioUsuarioDTO> listaNegocioGcGs) {
		this.listaNegocioGcGs = listaNegocioGcGs;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcEn() {
		return listaNegocioGcEn;
	}

	public void setListaNegocioGcEn(List<NegocioUsuarioDTO> listaNegocioGcEn) {
		this.listaNegocioGcEn = listaNegocioGcEn;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcAr() {
		return listaNegocioGcAr;
	}

	public void setListaNegocioGcAr(List<NegocioUsuarioDTO> listaNegocioGcAr) {
		this.listaNegocioGcAr = listaNegocioGcAr;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcAf() {
		return listaNegocioGcAf;
	}

	public void setListaNegocioGcAf(List<NegocioUsuarioDTO> listaNegocioGcAf) {
		this.listaNegocioGcAf = listaNegocioGcAf;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcAc() {
		return listaNegocioGcAc;
	}

	public void setListaNegocioGcAc(List<NegocioUsuarioDTO> listaNegocioGcAc) {
		this.listaNegocioGcAc = listaNegocioGcAc;
	}

	public List<NegocioUsuarioDTO> getListaNegocioGcSv() {
		return listaNegocioGcSv;
	}

	public void setListaNegocioGcSv(List<NegocioUsuarioDTO> listaNegocioGcSv) {
		this.listaNegocioGcSv = listaNegocioGcSv;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcGs() {
		return listaConceptoGcGs;
	}

	public void setListaConceptoGcGs(List<ConceptoUsuarioDTO> listaConceptoGcGs) {
		this.listaConceptoGcGs = listaConceptoGcGs;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcEn() {
		return listaConceptoGcEn;
	}

	public void setListaConceptoGcEn(List<ConceptoUsuarioDTO> listaConceptoGcEn) {
		this.listaConceptoGcEn = listaConceptoGcEn;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcAr() {
		return listaConceptoGcAr;
	}

	public void setListaConceptoGcAr(List<ConceptoUsuarioDTO> listaConceptoGcAr) {
		this.listaConceptoGcAr = listaConceptoGcAr;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcAf() {
		return listaConceptoGcAf;
	}

	public void setListaConceptoGcAf(List<ConceptoUsuarioDTO> listaConceptoGcAf) {
		this.listaConceptoGcAf = listaConceptoGcAf;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcAc() {
		return listaConceptoGcAc;
	}

	public void setListaConceptoGcAc(List<ConceptoUsuarioDTO> listaConceptoGcAc) {
		this.listaConceptoGcAc = listaConceptoGcAc;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoGcSv() {
		return listaConceptoGcSv;
	}

	public void setListaConceptoGcSv(List<ConceptoUsuarioDTO> listaConceptoGcSv) {
		this.listaConceptoGcSv = listaConceptoGcSv;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxGs() {
		return listaZonaMxGs;
	}

	public void setListaZonaMxGs(List<ZonaUsuarioDTO> listaZonaMxGs) {
		this.listaZonaMxGs = listaZonaMxGs;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxEn() {
		return listaZonaMxEn;
	}

	public void setListaZonaMxEn(List<ZonaUsuarioDTO> listaZonaMxEn) {
		this.listaZonaMxEn = listaZonaMxEn;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxAr() {
		return listaZonaMxAr;
	}

	public void setListaZonaMxAr(List<ZonaUsuarioDTO> listaZonaMxAr) {
		this.listaZonaMxAr = listaZonaMxAr;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxAf() {
		return listaZonaMxAf;
	}

	public void setListaZonaMxAf(List<ZonaUsuarioDTO> listaZonaMxAf) {
		this.listaZonaMxAf = listaZonaMxAf;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxAc() {
		return listaZonaMxAc;
	}

	public void setListaZonaMxAc(List<ZonaUsuarioDTO> listaZonaMxAc) {
		this.listaZonaMxAc = listaZonaMxAc;
	}

	public List<ZonaUsuarioDTO> getListaZonaMxSv() {
		return listaZonaMxSv;
	}

	public void setListaZonaMxSv(List<ZonaUsuarioDTO> listaZonaMxSv) {
		this.listaZonaMxSv = listaZonaMxSv;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxGs() {
		return listaSucursalMxGs;
	}

	public void setListaSucursalMxGs(List<SucursalesUsuarioDTO> listaSucursalMxGs) {
		this.listaSucursalMxGs = listaSucursalMxGs;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxEn() {
		return listaSucursalMxEn;
	}

	public void setListaSucursalMxEn(List<SucursalesUsuarioDTO> listaSucursalMxEn) {
		this.listaSucursalMxEn = listaSucursalMxEn;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxAr() {
		return listaSucursalMxAr;
	}

	public void setListaSucursalMxAr(List<SucursalesUsuarioDTO> listaSucursalMxAr) {
		this.listaSucursalMxAr = listaSucursalMxAr;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxAf() {
		return listaSucursalMxAf;
	}

	public void setListaSucursalMxAf(List<SucursalesUsuarioDTO> listaSucursalMxAf) {
		this.listaSucursalMxAf = listaSucursalMxAf;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxAc() {
		return listaSucursalMxAc;
	}

	public void setListaSucursalMxAc(List<SucursalesUsuarioDTO> listaSucursalMxAc) {
		this.listaSucursalMxAc = listaSucursalMxAc;
	}

	public List<SucursalesUsuarioDTO> getListaSucursalMxSv() {
		return listaSucursalMxSv;
	}

	public void setListaSucursalMxSv(List<SucursalesUsuarioDTO> listaSucursalMxSv) {
		this.listaSucursalMxSv = listaSucursalMxSv;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxGs() {
		return listaNegocioMxGs;
	}

	public void setListaNegocioMxGs(List<NegocioUsuarioDTO> listaNegocioMxGs) {
		this.listaNegocioMxGs = listaNegocioMxGs;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxEn() {
		return listaNegocioMxEn;
	}

	public void setListaNegocioMxEn(List<NegocioUsuarioDTO> listaNegocioMxEn) {
		this.listaNegocioMxEn = listaNegocioMxEn;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxAr() {
		return listaNegocioMxAr;
	}

	public void setListaNegocioMxAr(List<NegocioUsuarioDTO> listaNegocioMxAr) {
		this.listaNegocioMxAr = listaNegocioMxAr;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxAf() {
		return listaNegocioMxAf;
	}

	public void setListaNegocioMxAf(List<NegocioUsuarioDTO> listaNegocioMxAf) {
		this.listaNegocioMxAf = listaNegocioMxAf;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxAc() {
		return listaNegocioMxAc;
	}

	public void setListaNegocioMxAc(List<NegocioUsuarioDTO> listaNegocioMxAc) {
		this.listaNegocioMxAc = listaNegocioMxAc;
	}

	public List<NegocioUsuarioDTO> getListaNegocioMxSv() {
		return listaNegocioMxSv;
	}

	public void setListaNegocioMxSv(List<NegocioUsuarioDTO> listaNegocioMxSv) {
		this.listaNegocioMxSv = listaNegocioMxSv;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxGs() {
		return listaConceptoMxGs;
	}

	public void setListaConceptoMxGs(List<ConceptoUsuarioDTO> listaConceptoMxGs) {
		this.listaConceptoMxGs = listaConceptoMxGs;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxEn() {
		return listaConceptoMxEn;
	}

	public void setListaConceptoMxEn(List<ConceptoUsuarioDTO> listaConceptoMxEn) {
		this.listaConceptoMxEn = listaConceptoMxEn;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxAr() {
		return listaConceptoMxAr;
	}

	public void setListaConceptoMxAr(List<ConceptoUsuarioDTO> listaConceptoMxAr) {
		this.listaConceptoMxAr = listaConceptoMxAr;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxAf() {
		return listaConceptoMxAf;
	}

	public void setListaConceptoMxAf(List<ConceptoUsuarioDTO> listaConceptoMxAf) {
		this.listaConceptoMxAf = listaConceptoMxAf;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxAc() {
		return listaConceptoMxAc;
	}

	public void setListaConceptoMxAc(List<ConceptoUsuarioDTO> listaConceptoMxAc) {
		this.listaConceptoMxAc = listaConceptoMxAc;
	}

	public List<ConceptoUsuarioDTO> getListaConceptoMxSv() {
		return listaConceptoMxSv;
	}

	public void setListaConceptoMxSv(List<ConceptoUsuarioDTO> listaConceptoMxSv) {
		this.listaConceptoMxSv = listaConceptoMxSv;
	}
	
	
	
	
	
	
}
