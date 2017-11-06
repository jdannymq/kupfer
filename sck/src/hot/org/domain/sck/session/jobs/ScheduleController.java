package org.domain.sck.session.jobs;

import java.io.Serializable;
import java.util.Date;
import static org.jboss.seam.ScopeType.APPLICATION;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.jboss.seam.log.Log;


@Name("controller")
@Scope(APPLICATION)
@AutoCreate
@Startup
public class ScheduleController implements Serializable {

    private static final long serialVersionUID = 7609983147081676186L;
    @In ScheduleProcessor processor;
    @Logger Log log;
    private QuartzTriggerHandle quartzTestTriggerHandle;
    private QuartzTriggerHandle quartzTestTriggerHandle12Horas;
    private QuartzTriggerHandle quartzTestTriggerHandlIndicadores;
    /*
     * 	"0 0 23 * * ?"	        La tarea será ejecutada todos los días a las 23:00
	 *	"0 20 15 * * 1 2007"	La tarea será ejecutada todos los domingos del año 2007 a las 15:20
     * */
    
    private static String CRON_INTERVAL = "0 * * * * ?";
    private static String CRON_INTERVAL12HORAS="0 15 16 * * ? ";
    private static String CRON_INTERVAL_INDICADORES="0 10 01 * * ? ";
    @Create
    public void scheduleTimer() {
       //quartzTestTriggerHandle = processor.createQuartzTestTimer(new Date(), CRON_INTERVAL);
       
       //quartzTestTriggerHandle12Horas = processor.createQuartzTestTimer12Horas(new Date(), CRON_INTERVAL12HORAS);
       
       //quartzTestTriggerHandlIndicadores = processor.createToUpdateIndicadoresEconomicos(new Date(), CRON_INTERVAL_INDICADORES);
       
   }
    
}

