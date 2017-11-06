package org.domain.sck.session.action;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.UsuarioTipo;
import org.domain.sck.entity.Usuariosegur;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


@Name("pantallaSeleccionAction")
@Scope(ScopeType.CONVERSATION)
public class PantallaSeleccionAction implements Serializable{

	@Logger
	Log log;
	

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	@In(value="#{identity.usuarioLogueado}", scope = ScopeType.SESSION)
	private Usuariosegur usuarioLogueado;
	
	private int habilitarMenu;
	
	private List<UsuarioTipo>  listaUsuarioTipos; 
	
	
	
	@SuppressWarnings("unchecked")
	@Create
	public void init(){
		log.debug("iniciando el componente pantallaSeleccionAction.....!!!!!");
		/*evaluar el usuario para hablitar el menu correspondiente*/
		
		try{
			if(usuarioLogueado != null ){
				listaUsuarioTipos = entityManager.createQuery("select ut from UsuarioTipo ut " +
						" where ut.usuario.login=:usuaAux " +
						" ORDER BY ut.tipoUsuario.idTipo ")
						.setParameter("usuaAux", usuarioLogueado.getAlias())
						.getResultList();
				
				if(listaUsuarioTipos != null && listaUsuarioTipos.size() > 0){
					this.habilitarMenu = 3;
				}else{
					this.habilitarMenu = 2;
				}
			}
		}catch (Exception e) {
			log.error("Error, al sacar los perfiles del usuario, error #0", e.getMessage());
		}
		log.debug("termino de carga de datos.....!!!!!");
		
	}




	public int getHabilitarMenu() {
		return habilitarMenu;
	}
	public void setHabilitarMenu(int habilitarMenu) {
		this.habilitarMenu = habilitarMenu;
	}

	public Usuariosegur getUsuarioLogueado() {
		return usuarioLogueado;
	}

	public void setUsuarioLogueado(Usuariosegur usuarioLogueado) {
		this.usuarioLogueado = usuarioLogueado;
	}

	public List<UsuarioTipo> getListaUsuarioTipos() {
		return listaUsuarioTipos;
	}

	public void setListaUsuarioTipos(List<UsuarioTipo> listaUsuarioTipos) {
		this.listaUsuarioTipos = listaUsuarioTipos;
	}

}
