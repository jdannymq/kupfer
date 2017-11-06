package org.domain.sck.session.mantenedores;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.LcredTipoSolicitud;
import org.domain.sck.entity.UfSolicitudTipo;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

@Name("ufSolicitudTipoHome")
public class UfSolicitudTipoHome extends EntityHome<UfSolicitudTipo> {
	
	
	@Logger
	Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	@In ScoringService scoringService;
	
	
	private List<LcredTipoSolicitud> listaSolicitudes;
	private LcredTipoSolicitud tipoSolicitud;
	
	

	public void setUfSolicitudTipoIdUfSolicitudTipo(Long id) {
		cargaTipoSolicitudes();
		setId(id);
		
		UfSolicitudTipo instancia = this.getInstance();
		if(instancia != null){
			encontrarObjetoSolicitud(instancia.getTipoSolicitud());
		}
	}

	public Long getUfSolicitudTipoIdUfSolicitudTipo() {
		return (Long) getId();
	}

	@Override
	protected UfSolicitudTipo createInstance() {
		cargaTipoSolicitudes();
		UfSolicitudTipo ufSolicitudTipo = new UfSolicitudTipo();
		return ufSolicitudTipo;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public UfSolicitudTipo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
	
	

	@Override
	public String persist() {
		try{
			UfSolicitudTipo instancia = this.getInstance();
			if(instancia != null){
				if(this.tipoSolicitud == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe seleccionar el tipo de solicitud es obligatorio");
					return null;  
				}

				log.info(this.tipoSolicitud.getCodTipoSolicitud());
				int codigo = Integer.parseInt(this.tipoSolicitud.getCodTipoSolicitud());
				instancia.setTipoSolicitud(codigo);
				
				if(instancia.getValorUf() == null){
					FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el valor de UF");
					return null;  					
				}else{
					if(instancia.getValorUf().longValue() == 0){
						FacesMessages.instance().add(Severity.ERROR,"Debe ingresar el valor de UF");
						return null;  
					}
				}
				this.setInstance(instancia);
			}
		}catch (Exception e) {
			log.error("Error al hacer un persit ala objeto #0", e.getMessage());	// TODO: handle exception
		}
		return super.persist();
	}

	@SuppressWarnings("unchecked")
	public void cargaTipoSolicitudes(){
		List<LcredTipoSolicitud> lista = null;
		try{
			lista = entityManager.createQuery("select lts from LcredTipoSolicitud lts ").getResultList();
			if(lista != null){
				listaSolicitudes = new ArrayList<LcredTipoSolicitud>(0);
				for(LcredTipoSolicitud lts : lista){
					if(!lts.getCodTipoSolicitud().equals("1") && !lts.getCodTipoSolicitud().equals("2") && !lts.getCodTipoSolicitud().equals("3")
					  && !lts.getCodTipoSolicitud().equals("4")	){
						
						listaSolicitudes.add(lts);
					}
				}
			}
		}catch (Exception e) {
			log.error("Error al cargar los datos dela solicitudes #0", e.getMessage());// TODO: handle exception
		}
	}
	
	public void obtenerTipoSolicitud(){
		try{
			if(this.tipoSolicitud != null){
				log.info("El tipo de solicitud es #0", this.tipoSolicitud.getDesTipoSolicitud());
			}
		}catch (Exception e) {
			log.error("Error al seleccionar el tipo de solicitud #0", e.getMessage());// TODO: handle exception
		}
	}
	
	public void encontrarObjetoSolicitud(int codigo){
		try{
			LcredTipoSolicitud tipo = scoringService.obtenerObjetoLcredTipoSolicitud(String.valueOf(codigo));
			if(tipo != null){
				this.setTipoSolicitud(tipo);
			}
		}catch (Exception e) {
			log.error("Error, al sacar los datos para las solcitudes #0", e.getMessage());
		}
	}
	
	public List<LcredTipoSolicitud> getListaSolicitudes() {
		return listaSolicitudes;
	}

	public void setListaSolicitudes(List<LcredTipoSolicitud> listaSolicitudes) {
		this.listaSolicitudes = listaSolicitudes;
	}

	public LcredTipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(LcredTipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	
	
}
