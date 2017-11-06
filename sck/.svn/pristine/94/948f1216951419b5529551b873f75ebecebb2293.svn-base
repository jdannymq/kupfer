package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.Medicion;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("medicionHome")
public class MedicionHome extends EntityHome<Medicion> {

	public void setMedicionSystemId(Long id) {
		setId(id);
	}

	public Long getMedicionSystemId() {
		return (Long) getId();
	}

	@Override
	protected Medicion createInstance() {
		Medicion medicion = new Medicion();
		return medicion;
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

	public Medicion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
