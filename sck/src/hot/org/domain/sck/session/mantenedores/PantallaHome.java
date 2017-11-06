package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.Pantalla;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("pantallaHome")
public class PantallaHome extends EntityHome<Pantalla> {

	public void setPantallaIdPantalla(Long id) {
		setId(id);
	}

	public Long getPantallaIdPantalla() {
		return (Long) getId();
	}

	@Override
	protected Pantalla createInstance() {
		Pantalla pantalla = new Pantalla();
		return pantalla;
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

	public Pantalla getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
