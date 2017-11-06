package org.domain.sck.session.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.Sociedad;
import org.domain.sck.entity.TipoUsuario;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.UsuarioSociedad;
import org.domain.sck.entity.UsuarioTipo;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.entity.enums.EstadoEntityType;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;


@Name("usuarioSociedadAction")
@Scope(ScopeType.CONVERSATION)
public class UsuarioSociedadAction {


	
    @Logger private Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	private boolean proveedores;
	private boolean codigoGrupo;
	
	private String mensaje;
	
	
	private List<Usuariosegur>  listaUsuarios;
	
	private List<Usuariosegur>  listaUsuariosegur;
	private List<Usuariosegur>  listaUsuariosegurSeleccionado;
	
	private List<Sociedad> listaSociedades;
	private List<Sociedad> listaSociedadesSeleccionada;
	
	private Usuariosegur usuario;
	private List<UsuarioSociedad> listaUsuarioSociedades;

	
	
	@Create
    public void init(){
    	log.debug("iniciando UsuarioSociedadAction...");
    	try{
    		getLLenarLista();
    		consultarUsuarioSociedad();
   		
    	}catch (Exception e) {
    		log.error("Error, al sacar la lista de restricciones del archivo de carga de alumnos: #0", e.getMessage());
		}
    	return;    	
    	
    }
	
	@Destroy
    public void destroy() {
    	log.debug("destruyendo instancia UsuarioSociedadAction");
    }
	
	@SuppressWarnings("unchecked")
	public void getLLenarLista(){
    	try{
      		
    		List<Usuariosegur> lista = (List<Usuariosegur>)entityManager
				.createQuery("select u from Usuariosegur u where u.alias <> null and u.nombre <> null and u.nombre != 'ADMINISTRADOR' order by u.nombre")
				.getResultList();
    		
    		if(lista != null){
    			listaUsuarios = new ArrayList<Usuariosegur>(0);
    			listaUsuariosegur = new ArrayList<Usuariosegur>(0);
    			for(Usuariosegur usuario : lista){
    				if(usuario != null && !usuario.getAlias().equals("") && !usuario.getAlias().equals("xxxx") && !usuario.getAlias().equals("xxxxxx") ){
    					if(usuario.getNombre() != null && !usuario.getNombre().equals("")){
	    					if(!listaUsuarios.contains(usuario)){
	    						listaUsuarios.add(usuario);
	    						listaUsuariosegur.add(usuario);
	    					}
    					}
    			
    				}
    			}
    		}
    		
    		listaSociedades = (List<Sociedad>)entityManager.createQuery("select s from Sociedad s").getResultList();
    		
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar la lista de restricciones del archivo de carga de alumnos: #0", e.getMessage());
		}
	}
	
	public void eliminarUsuarioSociedad(UsuarioSociedad usutip){
		if(usutip != null){
			entityManager.remove(usutip);
			entityManager.flush();
			/*actualizar las lista y grilla*/
			limpiarInterno();
			consultarUsuarioSociedad();
		}
	}
	
	
	@SuppressWarnings({"unchecked" })
	public void consultarUsuarioSociedad(){
		String query=null;
		try{
			
			query = " select us from UsuarioSociedad us order by us.usuario.idPersonal asc ";
			List<UsuarioSociedad> lista = (List<UsuarioSociedad>)
			    entityManager.createQuery(query).getResultList();			
			
			if(lista != null){
				this.setListaUsuarioSociedades(lista);
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de tipo de usuario.", e.getMessage());
		}
		
	}
	
	public void asignarTipoUsuario(){
		if(this.listaUsuariosegurSeleccionado == null){
			setMensaje("Tiene que seleccionar por lo menos un usuario para asignar.");
			return;
		}
		if(this.listaSociedadesSeleccionada == null){
			setMensaje("Tiene que seleccionar por lo menos una sociedad para asignar.");
			return;
		}		
		try{
			StringBuffer cadena = new StringBuffer();
			for(Sociedad soc : listaSociedadesSeleccionada){
				cadena.append(soc.getRazonSocial()); cadena.append(" <<==>> ");
			}
			
			for(Usuariosegur usu: listaUsuariosegurSeleccionado){
				for(Sociedad soc : listaSociedadesSeleccionada){
					if(getVerificar(usu,soc)==false){
						UsuarioSociedad nuevo = new UsuarioSociedad();
						nuevo.setSociedad(soc);
						nuevo.setUsuario(usu);
						try{
							entityManager.persist(nuevo);
						}catch(Exception ex){
							log.error("Error, ingresar el usuariosociedad.", ex.getMessage());
						}
					}	
				}
			}

			entityManager.flush();
			setMensaje("Los tipo de usuarios fueron Asignado exitosamente...");
			
			/*se vuelve a sacar los datos...*/
			if(listaUsuarioSociedades != null){
				listaUsuarioSociedades.clear();
			}
			consultarUsuarioSociedad();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios asigandos.", e.getMessage());
		}
	}
	
	public boolean getVerificar(Usuariosegur usu, Sociedad soc){
		boolean existe = true; 
		try{
			UsuarioSociedad us = (UsuarioSociedad)entityManager
								.createQuery("select us from UsuarioSociedad us where us.usuario.idPersonal=:usu and us.sociedad.idSociedad=:soc")
								.setParameter("usu",usu.getIdPersonal())
								.setParameter("soc",soc.getIdSociedad())
								.getSingleResult();	
			if(us != null){
				log.info("existe registro en la base de datos.");
			}else{
				existe = false;
			}
			
		}catch(Exception ex){
			existe = false;
		}
		return existe;
	}

	public void obtenerUsuario(){
		if(usuario != null ){
			log.error("Usuario : #0", usuario.getNombre());
		}
	}	
	
	@SuppressWarnings("unchecked")
	public void sacarUsuarioSociedadAsignados(){
		String query=null;
		if(this.usuario == null){
			setMensaje("Tiene que seleccionar un usuario para revisar las sociedades asignadas.");
			return;
		}
		
		try{
			
			
			if(this.usuario != null){
				limpiarInterno();		
				query = " select us from UsuarioSociedad us " +
						" where us.usuario.alias=:usu " ;
				log.debug("usuario #0",this.usuario.getAlias());
				listaUsuarioSociedades = (List<UsuarioSociedad>)
				    entityManager.createQuery(query)
				    			 .setParameter("usu",this.usuario.getAlias())
								 .getResultList();	
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios asigandos.", e.getMessage());
		}
	}
	
	

	
	public void limpiar(){
		if(listaUsuariosegurSeleccionado != null){
			this.listaUsuariosegurSeleccionado.clear();
		}
		if(listaSociedadesSeleccionada != null){
			this.listaSociedadesSeleccionada.clear();
		}
		if(listaUsuarioSociedades != null){
			listaUsuarioSociedades.clear();
		}
		if(usuario != null){
			setUsuario(null);
		}
		setMensaje(null);
		consultarUsuarioSociedad();
	}
	
	public void limpiarInterno(){
		if(listaUsuariosegurSeleccionado != null){
			this.listaUsuariosegurSeleccionado.clear();
		}
		if(listaSociedadesSeleccionada != null){
			this.listaSociedadesSeleccionada.clear();
		}

	}
	

	public boolean isProveedores() {
		return proveedores;
	}

	public void setProveedores(boolean proveedores) {
		this.proveedores = proveedores;
	}

	public boolean isCodigoGrupo() {
		return codigoGrupo;
	}

	public void setCodigoGrupo(boolean codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<Usuariosegur> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuariosegur> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public Usuariosegur getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuariosegur usuario) {
		this.usuario = usuario;
	}

	public List<Usuariosegur> getListaUsuariosegur() {
		return listaUsuariosegur;
	}

	public void setListaUsuariosegur(List<Usuariosegur> listaUsuariosegur) {
		this.listaUsuariosegur = listaUsuariosegur;
	}

	public List<Usuariosegur> getListaUsuariosegurSeleccionado() {
		return listaUsuariosegurSeleccionado;
	}

	public void setListaUsuariosegurSeleccionado(
			List<Usuariosegur> listaUsuariosegurSeleccionado) {
		this.listaUsuariosegurSeleccionado = listaUsuariosegurSeleccionado;
	}

	public List<Sociedad> getListaSociedades() {
		return listaSociedades;
	}

	public void setListaSociedades(List<Sociedad> listaSociedades) {
		this.listaSociedades = listaSociedades;
	}

	public List<Sociedad> getListaSociedadesSeleccionada() {
		return listaSociedadesSeleccionada;
	}

	public void setListaSociedadesSeleccionada(
			List<Sociedad> listaSociedadesSeleccionada) {
		this.listaSociedadesSeleccionada = listaSociedadesSeleccionada;
	}

	public List<UsuarioSociedad> getListaUsuarioSociedades() {
		return listaUsuarioSociedades;
	}

	public void setListaUsuarioSociedades(
			List<UsuarioSociedad> listaUsuarioSociedades) {
		this.listaUsuarioSociedades = listaUsuarioSociedades;
	}
	
	
	

}
