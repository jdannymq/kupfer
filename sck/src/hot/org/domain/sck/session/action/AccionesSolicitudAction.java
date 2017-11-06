package org.domain.sck.session.action;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


@Name("accionesSolicitudAction")
@Scope(ScopeType.EVENT)
public class AccionesSolicitudAction implements Serializable{
	
	@Logger
	Log log;
	

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	
	public void init(){
		log.debug("alargando sesión");
	}

}
