package org.domain.sck.session.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.TipoUsuario;
import org.domain.sck.entity.UsuarioCargo;
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


@Name("usuarioTipoAction")
@Scope(ScopeType.CONVERSATION)
public class UsuarioTipoAction {


	
    @Logger private Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	private boolean proveedores;
	private boolean codigoGrupo;
	
	private String mensaje;
	
	
	private List<Usuariosegur>  listaUsuarios;
	private List<Usuariosegur>  listaUsuariosDefinidos;
	
	private List<TipoUsuario> listaTipoUsuarios;
	private List<TipoUsuario> listaTipoUsuariosSeleccionado;
	private List<UsuarioTipo> listaUsuarioTipos;
	
	private Usuariosegur usuario;
	
	
	
	@SuppressWarnings("unchecked")
	@Create
    public void init(){
    	log.debug("iniciando UsuarioTipoAction...");
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
    		
    		
    		List<UsuarioCargo> listaUsuarioCargos = (List<UsuarioCargo>)entityManager
				.createQuery("select u from UsuarioCargo u order by u.usuario.nombre")
				.getResultList();
    		
    		if(listaUsuarioCargos != null && listaUsuarioCargos.size() > 0){
    			listaUsuariosDefinidos = new ArrayList<Usuariosegur>(0);
    			for(UsuarioCargo us : listaUsuarioCargos){
    				listaUsuariosDefinidos.add(us.getUsuario());
    			}
    		}
    		
    		
    		
    		
    		
    		
    		
    	}catch (Exception e) {
    		log.error("Error, al sacar la lista de restricciones del archivo de carga de alumnos: #0", e.getMessage());
		}
    	return;    	
    	
    }
	
	@Destroy
    public void destroy() {
    	log.debug("destruyendo instancia alumnoMasivoAction");
    }
	
	public void eliminarUsuarioTipo(UsuarioTipo usutip){
		if(usutip != null){
			entityManager.remove(usutip);
			entityManager.flush();
			/*actualizar las lista y grilla*/
			if(listaTipoUsuariosSeleccionado != null){
				this.listaTipoUsuariosSeleccionado.clear();
			}
			if(listaTipoUsuarios != null){
				listaTipoUsuarios.clear();
			}
			consultarUsuarioTipo();
			sacarListaUsuarioTipoAsignados();
			
		}
	}
	
	
	@SuppressWarnings({"unchecked" })
	public void consultarUsuarioTipo(){
		String query=null;
		if(this.usuario == null){
			setMensaje("Debe Seleccionar un usuario para consultar.");
			return;
		}
		
		try{
			
			query = " select t from TipoUsuario t " +
			"  where t.idTipo not in ( select ut.tipoUsuario.idTipo  " +
			"                          from UsuarioTipo ut" +
			"                          where ut.usuario.alias=:usu)" +
			"  and t.estado=:estadoAux " ;
			log.debug("usuario #0",this.usuario.getAlias());
			List<TipoUsuario> lista = (List<TipoUsuario>)
			    entityManager.createQuery(query)
			    			 .setParameter("usu",this.usuario.getAlias())
			    			 .setParameter("estadoAux",EstadoEntityType.ACTIVO)
							 .getResultList();			
			
			if(lista == null){
				this.listaTipoUsuarios = (List<TipoUsuario>)
			    entityManager.createQuery(" select t from TipoUsuario t where t.estado=:estadoAux ")
			    	.setParameter("estadoAux",EstadoEntityType.ACTIVO)
			    	.getResultList();		
			}else{
				setListaTipoUsuarios(lista);			
			}
			sacarListaUsuarioTipoAsignados();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de tipo de usuario.", e.getMessage());
		}
		
	}
	
	public void asignarTipoUsuario(){
		if(this.listaTipoUsuariosSeleccionado == null){
			setMensaje("Tiene que seleccionar por lo menos un tipo de usuario para asignar.");
			return;
		}
		try{
			for(TipoUsuario tipo: listaTipoUsuariosSeleccionado){
				if(tipo != null){
					UsuarioTipo nuevo = new UsuarioTipo();
					nuevo.setTipoUsuario(tipo);
					nuevo.setUsuario(this.usuario);
					entityManager.persist(nuevo);
				}
			}
			entityManager.flush();
			setMensaje("Los tipo de usuarios fueron Asignado exitosamente...");
			
			/*se vuelve a sacar los datos...*/
			if(listaTipoUsuariosSeleccionado != null){
				this.listaTipoUsuariosSeleccionado.clear();
			}
			if(listaTipoUsuarios != null){
				listaTipoUsuarios.clear();
			}
			consultarUsuarioTipo();
			sacarListaUsuarioTipoAsignados();
			
		}catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios asigandos.", e.getMessage());
		}
	}

	public void asignarTipoUsuarioMasiva(){
		if(this.listaTipoUsuariosSeleccionado == null){
			setMensaje("Tiene que seleccionar por lo menos un tipo de usuario para asignar.");
			return;
		}
		try{
			if(listaUsuariosDefinidos != null){
			   for(Usuariosegur us : listaUsuariosDefinidos){
				   log.debug("us.getIdPersonal() : #0", us.getIdPersonal());
				   if(us.getIdPersonal() != 61 && us.getIdPersonal() != 1609 && us.getIdPersonal() != 400 && us.getIdPersonal() != 377 &&  us.getIdPersonal() != 40 ){
						for(TipoUsuario tipo: listaTipoUsuariosSeleccionado){
							if(tipo != null){
								try{
									boolean guadar = false;
									if(tipo != null && us != null){
										try{
											UsuarioTipo nuevo = (UsuarioTipo)entityManager
													.createQuery(" SELECT ut FROM UsuarioTipo ut " +
															     " WHERE ut.usuario.idPersonal=:idPersonal" +
															     " and  ut.tipoUsuario.idTipo=:idTipo ")
													.setParameter("idPersonal", us.getIdPersonal())
													.setParameter("idTipo", tipo.getIdTipo())
													.getSingleResult();
											
											if(nuevo != null){
												log.debug("tipo #0 y usuario #1", nuevo.getTipoUsuario().getDescripcion(), nuevo.getUsuario().getAlias());
												guadar = true;
											}
											
										}catch (Exception e) {
											log.error("Nose encontro registro #0", e.getMessage());
										}
									}
									
									if(!guadar){
										UsuarioTipo nuevo = new UsuarioTipo();
										nuevo.setTipoUsuario(tipo);
										nuevo.setUsuario(us);
										entityManager.persist(nuevo);
										entityManager.flush();
									}
								}catch (Exception e) {
									log.error("error al cruzar los datos del usuario #0, tipo #1", us.getAlias(), tipo.getDescripcion());
								}
							}
						}
				   }	
			   }
			}
			
			/**/
			if(listaUsuarios != null){
				   for(Usuariosegur us : listaUsuarios){
					   log.debug("us.getIdPersonal() : #0", us.getIdPersonal());
					   if(us.getIdPersonal() != 61 && us.getIdPersonal() != 1609 && us.getIdPersonal() != 400 && us.getIdPersonal() != 377 &&  us.getIdPersonal() != 40 ){
							for(TipoUsuario tipo: listaTipoUsuariosSeleccionado){
								if(tipo != null){
									try{
										boolean guadar = false;
										if(tipo != null && us != null){
											try{
												UsuarioTipo nuevo = (UsuarioTipo)entityManager
														.createQuery(" SELECT ut FROM UsuarioTipo ut " +
																     " WHERE ut.usuario.idPersonal=:idPersonal" +
																     " and  ut.tipoUsuario.idTipo=:idTipo ")
														.setParameter("idPersonal", us.getIdPersonal())
														.setParameter("idTipo", tipo.getIdTipo())
														.getSingleResult();
												
												if(nuevo != null){
													log.debug("tipo #0 y usuario #1", nuevo.getTipoUsuario().getDescripcion(), nuevo.getUsuario().getAlias());
													guadar = true;
												}
												
											}catch (Exception e) {
												log.error("Nose encontro registro #0", e.getMessage());
											}
										}
										
										if(!guadar){
											UsuarioTipo nuevo = new UsuarioTipo();
											try{
												nuevo.setTipoUsuario(tipo);
												nuevo.setUsuario(us);
												entityManager.persist(nuevo);
												entityManager.flush();
											}catch (Exception e) {
												log.error("no fue ingresado tipo #0 y usuario #1", nuevo.getTipoUsuario().getDescripcion(), nuevo.getUsuario().getAlias());
											}
										}
									}catch (Exception e) {
										log.error("error al cruzar los datos del usuario #0, tipo #1", us.getAlias(), tipo.getDescripcion());
									}
								}
							}
					   }	
				   }
				}			
		}catch (Exception e) {
			log.error("Error, al sacar la lista de asignarTipoUsuarioMasiva . #0", e.getMessage());
		}
	}
	
	public void obtenerUsuario(){
		if(usuario != null ){
			log.error("Usuario : #0", usuario.getNombre());
		}
	}	
	
	@SuppressWarnings("unchecked")
	public void sacarListaUsuarioTipoAsignados(){
		String query=null;
		try{
			if(this.usuario != null){
				query = " select ut from UsuarioTipo ut " +
				"  where ut.usuario.alias=:usu " ;
				log.debug("usuario #0",this.usuario.getAlias());
				listaUsuarioTipos = (List<UsuarioTipo>)
				    entityManager.createQuery(query)
				    			 .setParameter("usu",this.usuario.getAlias())
								 .getResultList();	
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios asigandos.", e.getMessage());
		}
	}
	
	

	
	public void limpiar(){
		if(listaTipoUsuarios != null){
			listaTipoUsuarios.clear();
		}
		if(listaTipoUsuariosSeleccionado != null){
			listaTipoUsuariosSeleccionado.clear();
		}
		if(listaUsuarioTipos != null){
			listaUsuarioTipos.clear();
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

	@BypassInterceptors public List<TipoUsuario> getListaTipoUsuarios() {
		return listaTipoUsuarios;
	}

	@BypassInterceptors public void setListaTipoUsuarios(List<TipoUsuario> listaTipoUsuarios) {
		this.listaTipoUsuarios = listaTipoUsuarios;
	}

	@BypassInterceptors public List<TipoUsuario> getListaTipoUsuariosSeleccionado() {
		return listaTipoUsuariosSeleccionado;
	}

	@BypassInterceptors public void setListaTipoUsuariosSeleccionado(
			List<TipoUsuario> listaTipoUsuariosSeleccionado) {
		this.listaTipoUsuariosSeleccionado = listaTipoUsuariosSeleccionado;
	}

	public Usuariosegur getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuariosegur usuario) {
		this.usuario = usuario;
	}

	public List<UsuarioTipo> getListaUsuarioTipos() {
		return listaUsuarioTipos;
	}

	public void setListaUsuarioTipos(List<UsuarioTipo> listaUsuarioTipos) {
		this.listaUsuarioTipos = listaUsuarioTipos;
	}

	public List<Usuariosegur> getListaUsuariosDefinidos() {
		return listaUsuariosDefinidos;
	}

	public void setListaUsuariosDefinidos(List<Usuariosegur> listaUsuariosDefinidos) {
		this.listaUsuariosDefinidos = listaUsuariosDefinidos;
	}
	

}
