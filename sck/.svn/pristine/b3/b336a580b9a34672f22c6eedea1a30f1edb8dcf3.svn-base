package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.LcredEstado;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("lcredEstadoHome")
public class LcredEstadoHome extends EntityHome<LcredEstado> {

	public void setLcredEstadoCodEstado(String id) {
		setId(id);
	}

	public String getLcredEstadoCodEstado() {
		return (String) getId();
	}

	@Override
	protected LcredEstado createInstance() {
		LcredEstado lcredEstado = new LcredEstado();
		return lcredEstado;
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

	public LcredEstado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
