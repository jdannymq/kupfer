package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.TipoFormaPago;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("tipoFormaPagoList")
public class TipoFormaPagoList extends EntityQuery<TipoFormaPago> {

	private static final String EJBQL = "select tipoFormaPago from TipoFormaPago tipoFormaPago";

	private static final String[] RESTRICTIONS = { "lower(tipoFormaPago.descripcion) like lower(concat(#{tipoFormaPagoList.tipoFormaPago.descripcion},'%'))", };

	private TipoFormaPago tipoFormaPago = new TipoFormaPago();

	public TipoFormaPagoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public TipoFormaPago getTipoFormaPago() {
		return tipoFormaPago;
	}
}
