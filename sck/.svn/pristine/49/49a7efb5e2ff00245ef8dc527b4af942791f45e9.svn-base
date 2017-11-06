package org.domain.sck.session.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.Medicion;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


@Name("medicionAction")
@Scope(ScopeType.CONVERSATION)
public class MedicionAction implements Serializable{

	@Logger
	Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	

	@Create
	public void init(){
		log.debug("iniciando el componente MedicionAction.....!!!!!");
		try{
			

		}catch (Exception e) {
			log.error("Error,  #0", e.getMessage());
		}
		log.debug("termino de carga de datos en componente MedicionAction.....!!!!!");
		
	}


	private Medicion medicionTarget;
    public Medicion getMedicionTarget() { return medicionTarget; }
    
    public void initGuardarMedicion() {
    	medicionTarget = new Medicion();
    }
    
    public void editMedicion(Medicion f) {
    	medicionTarget = f;
    }
    
    public void guardarMedicion() {
          if(entityManager.contains(medicionTarget)) {
                 entityManager.merge(medicionTarget);
          } else {
                 entityManager.persist(medicionTarget);
                 if(medicionActivosList != null) 
                	 medicionActivosList.add(medicionTarget);
          }
          entityManager.flush();
    }
    
    
    @Out(value = "mantenedorMaestroAction_medicion", required = false)
    private List<Medicion> medicionActivosList;
    
    @SuppressWarnings("unchecked")
	@Factory(value = "mantenedorMaestroAction_medicion", autoCreate = true)
    public void cargarMedicionActivosList() {
          medicionActivosList = new ArrayList<Medicion>();
          List<Medicion> Mediciones = entityManager.createQuery("from Medicion order by systemId")
                 .getResultList();
          if(Mediciones != null && !Mediciones.isEmpty())
        	  medicionActivosList.addAll(Mediciones);
          
    }
    
    public void cambiarActivoMedicion(boolean t, Medicion m) {
          m.setDisabled(t);
          entityManager.merge(m);
          entityManager.flush();
    }
	

}
