package org.domain.sck.session.mantenedores;

import java.math.BigDecimal;

import org.domain.sck.entity.TipoFormaPago;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tipoFormaPagoHome")
public class TipoFormaPagoHome extends EntityHome<TipoFormaPago> {

	public void setTipoFormaPagoIdTipoFormaPago(Long id) {
		setId(id);
	}

	public Long getTipoFormaPagoIdTipoFormaPago() {
		return (Long) getId();
	}

	@Override
	protected TipoFormaPago createInstance() {
		TipoFormaPago tipoFormaPago = new TipoFormaPago();
		return tipoFormaPago;
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

	public TipoFormaPago getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
