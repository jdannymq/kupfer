package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.Feriado;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("feriadoHome")
public class FeriadoHome extends EntityHome<Feriado> {

	public void setFeriadoSystemId(Long id) {
		setId(id);
	}

	public Long getFeriadoSystemId() {
		return (Long) getId();
	}

	@Override
	protected Feriado createInstance() {
		Feriado feriado = new Feriado();
		return feriado;
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

	public Feriado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
