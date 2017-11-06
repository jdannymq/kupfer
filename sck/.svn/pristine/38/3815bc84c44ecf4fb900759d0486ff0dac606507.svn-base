package org.domain.sck.session.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.LcredPerfiles;
import org.domain.sck.entity.UsuarioPerfil;
import org.domain.sck.entity.Usuariosegur;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;


@Name("usuarioPerfilAction")
@Scope(ScopeType.CONVERSATION)
public class UsuarioPerfilAction {


	
    @Logger private Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	private boolean proveedores;
	private boolean codigoGrupo;
	
	private String mensaje;
	
	
	private List<Usuariosegur>  listaUsuarios;
	private List<LcredPerfiles> listaPerfiles;
	private List<LcredPerfiles> listaUsuariosPerfilesSeleccionado;
	private List<UsuarioPerfil> listaUsuarioPerfiles;
	
	private Usuariosegur usuario;
	
	
	
	@SuppressWarnings("unchecked")
	@Create
    public void init(){
    	log.debug("iniciando UsuarioPerfilAction...");
    	try{
  		
    		List<Usuariosegur> lista = (List<Usuariosegur>)entityManager
				.createQuery("select u from Usuariosegur u where u.alias <> null and u.nombre <> null and u.nombre != 'ADMINISTRADOR' order by u.nombre")
				.getResultList();
    		
    		if(lista != null){
    			listaUsuarios = new ArrayList<Usuariosegur>(0);
    			for(Usuariosegur usuario : lista){
    				if(usuario != null && !usuario.getAlias().equals("") && !usuario.getAlias().equals("xxxx") && !usuario.getAlias().equals("xxxxxx") ){
    					if(usuario.getNombre() != null && !usuario.getNombre().equals("")){
	    					if(!listaUsuarios.contains(usuario)){
	    						listaUsuarios.add(usuario);
	    					}
    					}
    			
    				}
    			}
    		}
    	}catch (Exception e) {
    		log.error("Error, al sacar la lista de usuarios del sistema: #0", e.getMessage());
		}
    	return;    	
    	
    }
	
	@Destroy
    public void destroy() {
    	log.debug("destruyendo instancia UsuarioPerfilAction");
    }
	
	public void eliminarUsuarioPerfil(UsuarioPerfil usutip){
		if(usutip != null){
			entityManager.remove(usutip);
			entityManager.flush();
			/*actualizar las lista y grilla*/
			if(listaUsuariosPerfilesSeleccionado != null){
				this.listaUsuariosPerfilesSeleccionado.clear();
			}
			if(listaPerfiles != null){
				listaPerfiles.clear();
			}
			consultarUsuarioPerfil();
			sacarListaUsuarioPerfilesAsignados();
			
		}
	}
	
	
	@SuppressWarnings({"unchecked" })
	public void consultarUsuarioPerfil(){
		String query=null;
		if(this.usuario == null){
			setMensaje("Debe Seleccionar un usuario para consultar.");
			return;
		}
		
		try{
			query = " select lcp from LcredPerfiles lcp " +
			"  where lcp.codPerfil not in ( select ut.perfil.codPerfil  " +
			"                          from UsuarioPerfil ut" +
			"                          where ut.usuario.alias=:usu)" ;
			
			log.debug("usuario #0",this.usuario.getAlias());
			List<LcredPerfiles> lista = (List<LcredPerfiles>)
			    entityManager.createQuery(query)
			    			 .setParameter("usu",this.usuario.getAlias())
							 .getResultList();			
			
			if(lista == null){
				this.listaPerfiles = (List<LcredPerfiles>)
			    entityManager.createQuery(" select t from LcredPerfiles t ").getResultList();		
			}else{
				setListaPerfiles(lista);			
			}
			sacarListaUsuarioPerfilesAsignados();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de tipo de usuario. #0", e.getMessage());
		}
		
	}
	
	public void asignarUsuarioPerfiles(){
		if(this.listaUsuariosPerfilesSeleccionado == null){
			setMensaje("Tiene que seleccionar por lo menos un perfil de usuario antes de asignar.");
			return;
		}
		try{
			for(LcredPerfiles tipo: listaUsuariosPerfilesSeleccionado){
				if(tipo != null){
					UsuarioPerfil nuevo = new UsuarioPerfil();
					nuevo.setPerfil(tipo);
					nuevo.setUsuario(this.usuario);
					entityManager.persist(nuevo);
				}
			}
			entityManager.flush();
			setMensaje("Los los perfiles de usuarios fueron asignado exitosamente...!!!");
			
			/*se vuelve a sacar los datos...*/
			if(listaUsuariosPerfilesSeleccionado != null){
				this.listaUsuariosPerfilesSeleccionado.clear();
			}
			if(listaPerfiles != null){
				listaPerfiles.clear();
			}
			consultarUsuarioPerfil();
			sacarListaUsuarioPerfilesAsignados();
			
		}catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios asigandos.", e.getMessage());
		}
	}

	public void obtenerUsuario(){
		if(usuario != null ){
			log.error("Usuario : #0", usuario.getNombre());
		}
	}	
	
	@SuppressWarnings("unchecked")
	public void sacarListaUsuarioPerfilesAsignados(){
		String query=null;
		try{
			if(this.usuario != null){
				
				query = " select ut from UsuarioPerfil ut " +
				"  where ut.usuario.alias=:usu " ;
				log.debug("usuario #0",this.usuario.getAlias());
				listaUsuarioPerfiles = (List<UsuarioPerfil>)
				    entityManager.createQuery(query)
				    			 .setParameter("usu",this.usuario.getAlias())
								 .getResultList();	
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios asigandos.", e.getMessage());
		}
	}
	
	

	
	public void limpiar(){
		if(listaPerfiles != null){
			listaPerfiles.clear();
		}
		if(listaUsuariosPerfilesSeleccionado != null){
			listaUsuariosPerfilesSeleccionado.clear();
		}
		if(listaUsuarioPerfiles != null){
			listaUsuarioPerfiles.clear();
		}
		if(usuario != null){
			setUsuario(null);
		}
		setMensaje(null);
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

	@BypassInterceptors public List<LcredPerfiles> getListaPerfiles() {
		return listaPerfiles;
	}

	@BypassInterceptors public void setListaPerfiles(List<LcredPerfiles> listaPerfiles) {
		this.listaPerfiles = listaPerfiles;
	}

	public Usuariosegur getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuariosegur usuario) {
		this.usuario = usuario;
	}

	@BypassInterceptors public List<LcredPerfiles> getListaUsuariosPerfilesSeleccionado() {
		return listaUsuariosPerfilesSeleccionado;
	}

	@BypassInterceptors  public void setListaUsuariosPerfilesSeleccionado(
			List<LcredPerfiles> listaUsuariosPerfilesSeleccionado) {
		this.listaUsuariosPerfilesSeleccionado = listaUsuariosPerfilesSeleccionado;
	}

	@BypassInterceptors  public List<UsuarioPerfil> getListaUsuarioPerfiles() {
		return listaUsuarioPerfiles;
	}

	@BypassInterceptors  public void setListaUsuarioPerfiles(List<UsuarioPerfil> listaUsuarioPerfiles) {
		this.listaUsuarioPerfiles = listaUsuarioPerfiles;
	}


	
	
	

}
