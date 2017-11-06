package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.Ufs;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("ufHome")
public class UfHome extends EntityHome<Ufs> {

	public void setUfIdUf(Integer id) {
		setId(id);
	}

	public Integer getUfIdUf() {
		return (Integer) getId();
	}

	@Override
	protected Ufs createInstance() {
		Ufs uf = new Ufs();
		return uf;
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

	public Ufs getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
