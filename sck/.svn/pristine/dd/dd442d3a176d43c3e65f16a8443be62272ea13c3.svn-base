package org.domain.sck.session.action;

import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.Pantalla;
import org.domain.sck.entity.TipoPantalla;
import org.domain.sck.entity.TipoUsuario;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;


@Name("tipoPaginaAction")
@Scope(ScopeType.CONVERSATION)
public class TipoPaginaAction {
	
    @Logger private Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	

	private String mensaje;
	
	
	private List<TipoUsuario>  listaTipoUsuarios;
	private List<Pantalla> listaPantallas;
	private List<Pantalla> listaPantallasSeleccionados;
	private List<TipoPantalla> listaTipoPantallas;
	
	private TipoUsuario tipoUsuario;
	
	
	
	@SuppressWarnings("unchecked")
	@Create
    public void init(){
    	log.debug("iniciando TipoPaginaAction...");
    	try{
  		
    		listaTipoUsuarios = (List<TipoUsuario>)entityManager
				.createQuery("select t from TipoUsuario t")
				.getResultList();
    		
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar la lista de restricciones del archivo de carga de alumnos: #0", e.getMessage());
		}
    	return;    	
    	
    }
	
	@Destroy
    public void destroy() {
    	log.debug("destruyendo instancia alumnoMasivoAction");
    }
	
	public void eliminarTipoPantalla(TipoPantalla tipPan){
		if(tipPan != null){
			entityManager.remove(tipPan);
			entityManager.flush();
			/*actualizar las lista y grilla*/
			if(listaPantallasSeleccionados != null){
				this.listaPantallasSeleccionados.clear();
			}
			if(listaPantallas != null){
				listaPantallas.clear();
			}
			consultarTipoPantalla();
			sacarListaTipoPantllasAsignados();
			
		}
	}
	
	
	@SuppressWarnings({"unchecked" })
	public void consultarTipoPantalla(){
		String query=null;
		if(this.tipoUsuario == null){
			setMensaje("Debe Seleccionar un usuario para consultar.");
			return;
		}
		
		try{
			
			query = " select p from Pantalla p " +
			"  where p.idPantalla not in ( select tp.pantalla.idPantalla  " +
			"                          from TipoPantalla tp" +
			"                          where tp.tipoUsuario.idTipo=:tipo)" ;

			log.debug("usuario #0",this.tipoUsuario.getIdTipo());
			
			List<Pantalla> lista = (List<Pantalla>)
			    entityManager.createQuery(query)
			    			 .setParameter("tipo",this.tipoUsuario.getIdTipo())
							 .getResultList();			
			
			if(lista == null){
				this.listaPantallas = (List<Pantalla>)
			    entityManager.createQuery(" select p from Pantalla p ").getResultList();		
			}else{
				setListaPantallas(lista);			
			}
			sacarListaTipoPantllasAsignados();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de tipo de usuario.", e.getMessage());
		}
		
	}
	
	public void asignarPantallas(){
		if(this.listaPantallasSeleccionados == null){
			setMensaje("Tiene que seleccionar por lo menos una p�gina para asignar.");
			return;
		}
		try{
			for(Pantalla panta: listaPantallasSeleccionados){
				if(panta != null){
					TipoPantalla nuevo = new TipoPantalla();
					nuevo.setPantalla(panta);
					nuevo.setTipoUsuario(this.tipoUsuario);
					entityManager.persist(nuevo);
				}
			}
			entityManager.flush();
			setMensaje("Las pantalla fueron Asignadas exitosamente...");
			
			/*se vuelve a sacar los datos...*/
			if(listaPantallasSeleccionados != null){
				this.listaPantallasSeleccionados.clear();
			}
			if(listaPantallas != null){
				listaPantallas.clear();
			}
			consultarTipoPantalla();
			sacarListaTipoPantllasAsignados();
			
		}catch (Exception e) {
			log.error("Error, asiganar las pantallas asociado aun tipo de usuario.", e.getMessage());
		}
	}

	public void obtenerTipoUsuario(){
		if(tipoUsuario != null ){
			log.error("tipoUsuario : #0", tipoUsuario.getDescripcion());
		}
	}	
	
	@SuppressWarnings("unchecked")
	public void sacarListaTipoPantllasAsignados(){
		String query=null;
		try{
			if(this.tipoUsuario != null){
				query = " select tp from TipoPantalla tp " +
				"  where tp.tipoUsuario.idTipo=:tipo " ;
				log.debug("tipo #0",this.tipoUsuario.getIdTipo());
				listaTipoPantallas = (List<TipoPantalla>)
				    entityManager.createQuery(query)
				    			 .setParameter("tipo",this.tipoUsuario.getIdTipo())
								 .getResultList();	
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de pantallas  asigandas.", e.getMessage());
		}
	}
	
	

	
	public void limpiar(){
		if(listaPantallas != null){
			listaPantallas.clear();
		}
		if(listaPantallasSeleccionados != null){
			listaPantallasSeleccionados.clear();
		}
		if(listaTipoPantallas != null){
			listaTipoPantallas.clear();
		}
		if(tipoUsuario != null){
			setTipoUsuario(null);
		}
		setMensaje(null);
	}
	
	

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<TipoUsuario> getListaTipoUsuarios() {
		return listaTipoUsuarios;
	}

	public void setListaTipoUsuarios(List<TipoUsuario> listaTipoUsuarios) {
		this.listaTipoUsuarios = listaTipoUsuarios;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	@BypassInterceptors public List<Pantalla> getListaPantallas() {
		return listaPantallas;
	}

	@BypassInterceptors public void setListaPantallas(List<Pantalla> listaPantallas) {
		this.listaPantallas = listaPantallas;
	}

	@BypassInterceptors public List<Pantalla> getListaPantallasSeleccionados() {
		return listaPantallasSeleccionados;
	}

	@BypassInterceptors public void setListaPantallasSeleccionados(
			List<Pantalla> listaPantallasSeleccionados) {
		this.listaPantallasSeleccionados = listaPantallasSeleccionados;
	}

	public List<TipoPantalla> getListaTipoPantallas() {
		return listaTipoPantallas;
	}

	public void setListaTipoPantallas(List<TipoPantalla> listaTipoPantallas) {
		this.listaTipoPantallas = listaTipoPantallas;
	}

}
