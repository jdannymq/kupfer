package org.domain.sck.session.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.Canal;
import org.domain.sck.entity.Medicion;
import org.domain.sck.entity.MedicionCanalRango;
import org.domain.sck.entity.enums.TiempoMontoType;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;


@Name("medicionCanalRangoAction")
@Scope(ScopeType.CONVERSATION)
public class MedicionCanalRangoAction implements Serializable{

	@Logger
	Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	
	private List<TiempoMontoType> listaTiempoMontoTypes;
	private TiempoMontoType tiempoMontoType;
	
	private List<Canal> listaCanales;
	private Canal canal;
	private List<Medicion> listaMediciones;
	private Medicion medicion;
	private boolean habilitarCanal;

	@SuppressWarnings("unchecked")
	@Create
	public void init(){
		log.debug("iniciando el componente MedicionCanalRangoAction.....!!!!!");
		try{
			if(listaTiempoMontoTypes == null){
				listaTiempoMontoTypes = TiempoMontoType.getAcciones();
			}
		
			listaCanales = entityManager.createQuery("select c from Canal c where c.disabled=0  order by c.systemId").getResultList();
			if(listaCanales == null || listaCanales.size() == 0){			
				listaCanales = new ArrayList<Canal>(0);
			}

			listaMediciones = entityManager.createQuery("select m from Medicion m where m.disabled=0  order by m.systemId").getResultList();
			if(listaMediciones == null || listaMediciones.size() == 0){			
				listaMediciones = new ArrayList<Medicion>(0);
			}
			
		}catch (Exception e) {
			log.error("Error, al sacar los perfiles del usuario, error #0", e.getMessage());
		}
		log.debug("termino de carga de datos en componente MedicionCanalRangoAction.....!!!!!");
		
	}


	private MedicionCanalRango medicionCanalRangoTarget;
    public MedicionCanalRango getMedicionCanalRangoTarget() { return medicionCanalRangoTarget; }
    
    public void initGuardarMedicionCanalRango() {
    	medicionCanalRangoTarget = new MedicionCanalRango();
    	this.medicion = null;
    	this.canal = null;
    	this.tiempoMontoType = null;
    	
    }
    
    public void editMedicionCanalRango(MedicionCanalRango mcr) {
    	medicionCanalRangoTarget = mcr;
    	this.canal = mcr.getCanal();
    	this.medicion = mcr.getMedicion();
    	this.tiempoMontoType = mcr.getTiempoMontoType();
    	obtenerMedicionSeleccionado();
    	
    }
    
    public void guardarMedicionCanalRango() {
    	
    	if(validarDatosAntesSetar()){
    		medicionCanalRangoTarget.setCanal(this.canal);
    		medicionCanalRangoTarget.setMedicion(this.medicion);
    		medicionCanalRangoTarget.setTiempoMontoType(this.tiempoMontoType);
    		
    		
          if(entityManager.contains(medicionCanalRangoTarget)) {
                 entityManager.merge(medicionCanalRangoTarget);
          } else {
                 entityManager.persist(medicionCanalRangoTarget);
                 if(medicionCanalRangoActivosList != null) 
                	 medicionCanalRangoActivosList.add(medicionCanalRangoTarget);
          }
          entityManager.flush();
    	} 
    }
    
    public boolean validarDatosAntesSetar(){
    	
    	if(this.medicion == null){
    		FacesMessages.instance().add(Severity.WARN,"Debe seleccionar la medición del registro.");
    		return false;
    	}
    	
		String searchMe = this.medicion.getDescripcion();
		String findMe = "Con";
		
		int searchMeLength = searchMe.length();
		int findMeLength = findMe.length();
		boolean foundIt = false;
		for (int i = 0; i <= (searchMeLength - findMeLength); i++) {
		   if (searchMe.regionMatches(i, findMe, 0, findMeLength)) {
		      foundIt = true;
		      System.out.println(searchMe.substring(i, i + findMeLength));
		      break;	
		   }
		}
		if (!foundIt){
			log.debug("Sin SobreGiro");
			this.canal = null;
			if(this.tiempoMontoType == null){
	    		FacesMessages.instance().add(Severity.WARN,"Debe seleccionar el tipo del registro.");
	    		return false;				
			} 
			if(!this.tiempoMontoType.equals(TiempoMontoType.DIAS)){
	    		FacesMessages.instance().add(Severity.WARN,"Debe seleccionar el tipo como "+ TiempoMontoType.DIAS.getNombre());
	    		return false;					
			}
		}else{
			log.debug("Con SobreGiro");
			if(this.canal == null){
	    		FacesMessages.instance().add(Severity.WARN,"Debe seleccionar el canal del registro.");
	    		return false;				
			}
			if(this.tiempoMontoType == null){
	    		FacesMessages.instance().add(Severity.WARN,"Debe seleccionar el tipo del registro.");
	    		return false;				
			} 
			if(!this.tiempoMontoType.equals(TiempoMontoType.MONTOS)){
	    		FacesMessages.instance().add(Severity.WARN,"Debe seleccionar el tipo como "+ TiempoMontoType.MONTOS.getNombre());
	    		return false;					
			}
		}
    	
		if(this.medicionCanalRangoTarget.getValorInicial() == null){
    		FacesMessages.instance().add(Severity.WARN,"Debe ingresar el valor inicial.");
    		return false;				
		}
		
		if(this.medicionCanalRangoTarget.getValorFinal() == null){
    		FacesMessages.instance().add(Severity.WARN,"Debe ingresar el valor final.");
    		return false;				
		}
		
		if(this.medicionCanalRangoTarget.getValorInicial() == null && this.medicionCanalRangoTarget.getValorFinal() == null ){
    		FacesMessages.instance().add(Severity.WARN,"Debe ingresar valores de inicio y final para el registro.");
    		return false;	
		}

		if(this.medicionCanalRangoTarget.getValorInicial().longValue() >= this.medicionCanalRangoTarget.getValorFinal().longValue()){
    		FacesMessages.instance().add(Severity.WARN,"El valor final es mayor o igual al valor inicial.");
    		return false;	
		}
		
    	
    	return true;
    }
    
    
    
    
    
    @Out(value = "mantenedorMaestroAction_medicionCanalRango", required = false)
    private List<MedicionCanalRango> medicionCanalRangoActivosList;
    
    @SuppressWarnings("unchecked")
	@Factory(value = "mantenedorMaestroAction_medicionCanalRango", autoCreate = true)
    public void cargarMedicionCanalRangoActivosList() {
          medicionCanalRangoActivosList = new ArrayList<MedicionCanalRango>();
          List<MedicionCanalRango> medicionCanalRango = entityManager.createQuery("select mcr from MedicionCanalRango mcr order by mcr.medicion.systemId asc ")
                 .getResultList();
          if(medicionCanalRango != null && !medicionCanalRango.isEmpty())
        	  medicionCanalRangoActivosList.addAll(medicionCanalRango);
          
    }
    
    public void cambiarActivoMedicionCanalRango(boolean t, MedicionCanalRango mcr) {
          mcr.setDisabled(t);
          entityManager.merge(mcr);
          entityManager.flush();
    }

	public void obtenerTipoSeleccionado(){
		if(tiempoMontoType != null){
			log.debug("el tipo seleccionado es : "+ this.tiempoMontoType);
		}
	}
	public void obtenerMedicionSeleccionado(){
		if(medicion != null){
			log.debug("la medicion seleccionado es : "+ this.medicion.getDescripcion());
			
			String searchMe = this.medicion.getDescripcion();
			String findMe = "Sin";
			
			int searchMeLength = searchMe.length();
			int findMeLength = findMe.length();
			boolean foundIt = false;
			for (int i = 0; i <= (searchMeLength - findMeLength); i++) {
			   if (searchMe.regionMatches(i, findMe, 0, findMeLength)) {
			      foundIt = true;
			      System.out.println(searchMe.substring(i, i + findMeLength));
			      break;	
			   }
			}
			if (!foundIt){
				log.debug("fue selccionado con sobregiro debemos habilitar el canal");
				this.setCanal(null);
				this.habilitarCanal = false;
			}else{
				log.debug("fue selccionado sin sobregiro debemos desahabilitar el canal");
				this.setCanal(null);
				this.habilitarCanal = true;
			}
		}
	}    
	public void obtenerCanalSeleccionado(){
		if(canal != null){
			log.debug("el canal seleccionado es : "+ this.canal.getDescripcion());
		}
	}  
	
    
	public List<TiempoMontoType> getListaTiempoMontoTypes() {
		return listaTiempoMontoTypes;
	}

	public void setListaTiempoMontoTypes(List<TiempoMontoType> listaTiempoMontoTypes) {
		this.listaTiempoMontoTypes = listaTiempoMontoTypes;
	}

	public TiempoMontoType getTiempoMontoType() {
		return tiempoMontoType;
	}

	public void setTiempoMontoType(TiempoMontoType tiempoMontoType) {
		this.tiempoMontoType = tiempoMontoType;
	}

	public List<Canal> getListaCanales() {
		return listaCanales;
	}

	public void setListaCanales(List<Canal> listaCanales) {
		this.listaCanales = listaCanales;
	}

	public List<Medicion> getListaMediciones() {
		return listaMediciones;
	}

	public void setListaMediciones(List<Medicion> listaMediciones) {
		this.listaMediciones = listaMediciones;
	}

	public Canal getCanal() {
		return canal;
	}

	public void setCanal(Canal canal) {
		this.canal = canal;
	}

	public Medicion getMedicion() {
		return medicion;
	}

	public void setMedicion(Medicion medicion) {
		this.medicion = medicion;
	}

	public boolean isHabilitarCanal() {
		return habilitarCanal;
	}

	public void setHabilitarCanal(boolean habilitarCanal) {
		this.habilitarCanal = habilitarCanal;
	}
	
    
    
    

}
