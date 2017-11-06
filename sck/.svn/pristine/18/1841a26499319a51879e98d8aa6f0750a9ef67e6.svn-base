package org.domain.sck.session.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.Canal;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


@Name("canalAction")
@Scope(ScopeType.CONVERSATION)
public class CanalAction implements Serializable{

	@Logger
	Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	

	@Create
	public void init(){
		log.debug("iniciando el componente canalAction.....!!!!!");
		
		try{

		}catch (Exception e) {
			log.error("Error, al sacar los perfiles del usuario, error #0", e.getMessage());
		}
		log.debug("termino de carga de datos en componente canalAction.....!!!!!");
		
	}


	private Canal canalTarget;
    public Canal getCanalTarget() { return canalTarget; }
    
    public void initGuardarCanal() {
    	canalTarget = new Canal();
    }
    
    public void editFeriado(Canal f) {
          canalTarget = f;
    }
    
    public void guardarCanal() {
          if(entityManager.contains(canalTarget)) {
                 entityManager.merge(canalTarget);
          } else {
                 entityManager.persist(canalTarget);
                 if(canalesActivosList != null) 
                        canalesActivosList.add(canalTarget);
          }
          entityManager.flush();
    }
    
    
    @Out(value = "mantenedorMaestroAction_canales", required = false)
    private List<Canal> canalesActivosList;
    
    @SuppressWarnings("unchecked")
	@Factory(value = "mantenedorMaestroAction_canales", autoCreate = true)
    public void cargarFeriadosActivosList() {
          Calendar cal = Calendar.getInstance();
          cal.set(Calendar.HOUR_OF_DAY, 00);
          cal.set(Calendar.MINUTE, 00);
          cal.set(Calendar.SECOND, 00);
          cal.set(Calendar.MILLISECOND,0);
          canalesActivosList = new ArrayList<Canal>();
          List<Canal> canales = entityManager.createQuery("from Canal order by systemId")
                 .getResultList();
          if(canales != null && !canales.isEmpty())
                 canalesActivosList.addAll(canales);
          
    }
    
    public void cambiarActivoCanal(boolean t, Canal c) {
          c.setDisabled(t);
          entityManager.merge(c);
          entityManager.flush();
    }
	

}
