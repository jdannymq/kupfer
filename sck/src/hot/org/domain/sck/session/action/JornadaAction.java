package org.domain.sck.session.action;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.Jornada;
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


@Name("jornadaAction")
@Scope(ScopeType.CONVERSATION)
public class JornadaAction implements Serializable{

	@Logger
	Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	
    private Integer horaInicial = 0;
    private Integer horaFinal = 0;
    private Integer minutoInicial = 0; 
    private Integer minutoFinal = 0;


	@Create
	public void init(){
		log.debug("iniciando el componente JornadaAction.....!!!!!");
		
		try{
			/*inicializar la variables*/
			limpiarDatosJornadaEditada();
			
		}catch (Exception e) {
			log.error("Error, al sacar los datos para utilizar en la paginas #0", e.getMessage());
		}
		log.debug("termino de carga de datos en componente JornadaAction.....!!!!!");
		
	}


	private Jornada jornadaTarget;
    public Jornada getJornadaTarget() { return jornadaTarget; }
    
    public void initGuardarJornada() {
          jornadaTarget = new Jornada();
    }
    
    public void editJornada(Jornada f) {
          jornadaTarget = f;
    }
    
    public void guardarJornada() {
    	
    	if(validacion()){
    	   jornadaTarget.setHoraInicial(new Time(hourToMs(horaInicial, minutoInicial)));
    	   jornadaTarget.setHoraFinal(new Time(hourToMs(horaFinal, minutoFinal)));
          if(entityManager.contains(jornadaTarget)) {
                 entityManager.merge(jornadaTarget);
          } else {
                 entityManager.persist(jornadaTarget);
                 if(jornadasActivosList != null) 
                	 jornadasActivosList.add(jornadaTarget);
          }
          entityManager.flush();
    	}  
    }
    
    
    public boolean validacion(){
    	try{
    		if(this.horaInicial == null){
        		FacesMessages.instance().add(Severity.WARN,"Debe ingresar la hora inicial, Ej(00  al 23 ).");
        		return false;    			
    		}
    		if(this.minutoInicial == null){
        		FacesMessages.instance().add(Severity.WARN,"Debe ingresar la minutos inicial, Ej(00  al 59 ).");
        		return false;       			
    		}
    		if(this.horaInicial == null){
        		FacesMessages.instance().add(Severity.WARN,"Debe ingresar la hora final, Ej(00  al 23 ).");
        		return false;    			
    		}
    		if(this.minutoInicial == null){
        		FacesMessages.instance().add(Severity.WARN,"Debe ingresar la minutos final, Ej(00  al 59 ).");
        		return false;       			
    		}    		
    		
    		
    	}catch (Exception e) {
    		log.error("Error, al validar los  datos que se van a organizar. mensaje #0", e.getMessage());
    	}
    	
    	return true;
    }
    
    
    
    @Out(value = "mantenedorMaestroAction_jornada", required = false)
    private List<Jornada> jornadasActivosList;
    
    @SuppressWarnings("unchecked")
	@Factory(value = "mantenedorMaestroAction_jornada", autoCreate = true)
    public void cargarJornadasActivosList() {
    	  jornadasActivosList = new ArrayList<Jornada>();
          List<Jornada> jornadas = entityManager.createQuery("from Jornada order by systemId")
                 .getResultList();
          if(jornadas != null && !jornadas.isEmpty())
        	  jornadasActivosList.addAll(jornadas);
          
    }
    
    public void cambiarActivoJornada(boolean t, Jornada jornada) {
          jornada.setDisabled(t);
          entityManager.merge(jornada);
          entityManager.flush();
    }
	
    public void limpiarDatosJornadaEditada(){
        
        horaInicial = 0;
        horaFinal = 0;
        minutoInicial = 0;
        minutoFinal = 0;
        
  }

    private String hourToString(Integer hour, Integer minutes) {
        StringBuilder sb = new StringBuilder();
        if(hour < 10);
               sb.append("0");
        sb.append(hour);
        sb.append(":");
        if(minutes < 10)
               sb.append("0");
        sb.append(minutes);
        return sb.toString();
  }
  
  private Long hourToMs(Integer hour, Integer minutes) {
        return hourToMs(hour, minutes, 0);
  }
  
  private Long hourToMs(Integer hour, Integer minutes,Integer seconds) {
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 0, 1, hour, minutes,seconds);
        log.debug("cal #0",cal.getTime());
        return cal.getTimeInMillis();
  }

    
    
    
  public Integer getHoraInicial() { return horaInicial; }
  public Integer getHoraFinal() { return horaFinal; }
  public Integer getMinutoInicial() { return minutoInicial; }
  public Integer getMinutoFinal() { return minutoFinal; }
  public void setHoraInicial(Integer horaInicial) { this.horaInicial = horaInicial; }
  public void setHoraFinal(Integer horaFinal) { this.horaFinal = horaFinal; }
  public void setMinutoInicial(Integer minutoInicial) { this.minutoInicial = minutoInicial; }
  public void setMinutoFinal(Integer minutoFinal) { this.minutoFinal = minutoFinal; }


    
    
}
