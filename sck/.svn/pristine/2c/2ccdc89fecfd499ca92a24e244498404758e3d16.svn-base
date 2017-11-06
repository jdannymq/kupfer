package org.domain.sck.session.mantenedores;

import javax.persistence.EntityManager;

import org.domain.sck.entity.Usuarios;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

@Name("usuariosHome")
public class UsuariosHome extends EntityHome<Usuarios> {
	
	
	@In(value="#{entityManager}")
	EntityManager entityManager;

	@Logger Log log;
	
	private String confirmacion;
	

	public void setUsuariosLogin(String id) {
		setId(id);
	}

	@Override
	public String persist() {
		
		if(!this.instance.getPass().equals(this.confirmacion)){
			FacesMessages.instance().add(Severity.INFO, "La password es distinta a la confirmacion.");
			return "failed";			
		}
		
		
		Usuarios usuario = entityManager.find(Usuarios.class, this.instance.getLogin());
		if(usuario == null) {
			return super.persist();
		} else {
			FacesMessages.instance().add(Severity.ERROR, "Ya existe un usuario con ese login");
			return "failed";
		}
		
		
	}
	public String getUsuariosLogin() {
		return (String) getId();
	}

	@Override
	protected Usuarios createInstance() {
		Usuarios usuarios = new Usuarios();
		return usuarios;
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

	public Usuarios getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	
	public String getConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(String confirmacion) {
		this.confirmacion = confirmacion;
	}
	
	
	
}
