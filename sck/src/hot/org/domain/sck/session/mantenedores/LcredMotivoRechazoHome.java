package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.LcredMotivoRechazo;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("lcredMotivoRechazoHome")
public class LcredMotivoRechazoHome extends EntityHome<LcredMotivoRechazo> {

	public void setLcredMotivoRechazoCodRechazo(String id) {
		setId(id);
	}

	public String getLcredMotivoRechazoCodRechazo() {
		return (String) getId();
	}

	@Override
	protected LcredMotivoRechazo createInstance() {
		LcredMotivoRechazo lcredMotivoRechazo = new LcredMotivoRechazo();
		return lcredMotivoRechazo;
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

	public LcredMotivoRechazo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
