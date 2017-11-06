package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.Canal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("canalHome")
public class CanalHome extends EntityHome<Canal> {

	public void setCanalSystemId(Long id) {
		setId(id);
	}

	public Long getCanalSystemId() {
		return (Long) getId();
	}

	@Override
	protected Canal createInstance() {
		Canal canal = new Canal();
		return canal;
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

	public Canal getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
