package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.LcredTipoSolicitud;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("lcredTipoSolicitudHome")
public class LcredTipoSolicitudHome extends EntityHome<LcredTipoSolicitud> {

	public void setLcredTipoSolicitudCodTipoSolicitud(String id) {
		setId(id);
	}

	public String getLcredTipoSolicitudCodTipoSolicitud() {
		return (String) getId();
	}

	@Override
	protected LcredTipoSolicitud createInstance() {
		LcredTipoSolicitud lcredTipoSolicitud = new LcredTipoSolicitud();
		return lcredTipoSolicitud;
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

	public LcredTipoSolicitud getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
