package org.domain.sck.session.jobs;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.domain.sck.dto.IndicadoresDTO;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.Ufs;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Name("processor")
@AutoCreate
@Scope(ScopeType.APPLICATION)
public class ScheduleProcessor {

	
    @Logger  private Log log;
    
	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	@In ScoringService scoringService;
	
    
    
    @Asynchronous
    @Transactional
    public QuartzTriggerHandle createQuartzTestTimer(@Expiration Date when, @IntervalCron String interval) {
      String date = new Date().toString();
      log.debug( "Quartz Test: " + date );
      return null;
    }

    @SuppressWarnings("unchecked")
	@Asynchronous
    @Transactional
    public QuartzTriggerHandle createQuartzTestTimer12Horas(@Expiration Date when, @IntervalCron String interval) {
    	log.debug( "inicia proceso automatico de consulta ");
    	try{
    	  List<Sucursal> listaSucursales = (List<Sucursal>)entityManager.createQuery("select suc from Sucursal suc ").getResultList();
    	  if(listaSucursales != null){
    		  for(Sucursal suc : listaSucursales){
    			  log.debug( "codigo:#0 descripcion: #1 ", suc.getCodigo(), suc.getDescripcion());
    		  }
    	  }
      }catch (Exception e) {
		log.error("error al sacar la sucursales");
	  }
    	
      String date = new Date().toString();
      log.debug( "Proceso ejecutado hora indicada: " + date );
      log.debug( "finalizat proceso automatico de consulta ");
      return null;
    }
    
 	 @Asynchronous
     @Transactional
     public QuartzTriggerHandle createToUpdateIndicadoresEconomicos(@Expiration Date when, @IntervalCron String interval) {
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
 		    InputStream is = url.openConnection().getInputStream();
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
 		    	boolean exito = scoringService.persistUfs(uf);
 		    	if(exito){
 		    		log.debug("valor : #0", exito);
 		    		return null;
 		    	}else{
 		    		return null;
 		    	}
 		    }
      		
 		    
 		    
      	}catch (Exception e) {
  		  log.error("error al sacar los endicadores ");
  	    }
      	
        String date = new Date().toString();
        
        log.debug( "Proceso ejecutado hora indicada: " + date );
        log.debug( "finaliza proceso automatico de consultar indicadores economicos");
        return null;
      }
    
     public String getTagValue(String tag, Element elemento) {
		 NodeList lista = elemento.getElementsByTagName(tag).item(0).getChildNodes();
		 Node valor = (Node) lista.item(0);
		 return valor.getNodeValue();
    }
    

}