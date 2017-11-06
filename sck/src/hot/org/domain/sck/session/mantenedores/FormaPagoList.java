package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.FormaPago;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("formaPagoList")
public class FormaPagoList extends EntityQuery<FormaPago> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3629681092702016936L;

	private static final String EJBQL = "select formaPago from FormaPago formaPago";

	private static final String[] RESTRICTIONS = { "lower(formaPago.descripcion) like lower(concat(#{formaPagoList.formaPago.descripcion},'%'))", };

	private FormaPago formaPago = new FormaPago();

	public FormaPagoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public FormaPago getFormaPago() {
		return formaPago;
	}
}