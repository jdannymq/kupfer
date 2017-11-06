package org.domain.sck.session.mantenedores;


import java.math.BigDecimal;

import org.domain.sck.entity.Sociedad;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sociedadHome")
public class SociedadHome extends EntityHome<Sociedad> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setSociedadIdSociedad(Long id) {
		setId(id);
	}

	public Long getSociedadIdSociedad() {
		return (Long) getId();
	}

	@Override
	protected Sociedad createInstance() {
		Sociedad sociedad = new Sociedad();
		return sociedad;
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

	public Sociedad getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
