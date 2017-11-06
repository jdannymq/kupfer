package org.domain.sck.session.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.Feriado;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


@Name("feriadosAction")
@Scope(ScopeType.CONVERSATION)
public class FeriadosAction implements Serializable{

	@Logger
	Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	

	@Create
	public void init(){
		log.debug("iniciando el componente FeriadosAction.....!!!!!");
		
		try{

		}catch (Exception e) {
			log.error("Error, al sacar los perfiles del usuario, error #0", e.getMessage());
		}
		log.debug("termino de carga de datos en componente FeriadosAction.....!!!!!");
		
	}


	private Feriado feriadoTarget;
    public Feriado getFeriadoTarget() { return feriadoTarget; }
    
    public void initGuardarFeriado() {
          feriadoTarget = new Feriado();
    }
    
    public void editFeriado(Feriado f) {
          feriadoTarget = f;
    }
    
    public void guardarFeriado() {
          if(entityManager.contains(feriadoTarget)) {
                 entityManager.merge(feriadoTarget);
          } else {
                 entityManager.persist(feriadoTarget);
                 if(feriadosActivosList != null) 
                        feriadosActivosList.add(feriadoTarget);
          }
          entityManager.flush();
    }
    
    
    @Out(value = "mantenedorMaestroAction_feriados", required = false)
    private List<Feriado> feriadosActivosList;
    
    @SuppressWarnings("unchecked")
	@Factory(value = "mantenedorMaestroAction_feriados", autoCreate = true)
    public void cargarFeriadosActivosList() {
          Calendar cal = Calendar.getInstance();
          cal.set(Calendar.HOUR_OF_DAY, 00);
          cal.set(Calendar.MINUTE, 00);
          cal.set(Calendar.SECOND, 00);
          cal.set(Calendar.MILLISECOND,0);
          feriadosActivosList = new ArrayList<Feriado>();
          List<Feriado> feriados = entityManager.createQuery("from Feriado order by fecha")
                 .getResultList();
          if(feriados != null && !feriados.isEmpty())
                 feriadosActivosList.addAll(feriados);
          
    }
    
    public void cambiarActivoFeriado(boolean t, Feriado f) {
          f.setDisabled(t);
          entityManager.merge(f);
          entityManager.flush();
    }
	

}
