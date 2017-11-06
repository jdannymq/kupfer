package org.domain.sck.session.mantenedores;


import org.domain.sck.entity.FormaPago;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("formaPagoHome")
public class FormaPagoHome extends EntityHome<FormaPago> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setFormaPagoIdFormaPago(Long id) {
		setId(id);
	}

	public Long getFormaPagoIdFormaPago() {
		return (Long) getId();
	}

	@Override
	protected FormaPago createInstance() {
		FormaPago formaPago = new FormaPago();
		return formaPago;
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

	public FormaPago getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
