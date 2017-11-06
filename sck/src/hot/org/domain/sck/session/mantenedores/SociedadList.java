package org.domain.sck.session.mantenedores;


import org.domain.sck.entity.Sociedad;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;

@Name("sociedadList")
public class SociedadList extends EntityQuery<Sociedad> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select sociedad from Sociedad sociedad";

	private static final String[] RESTRICTIONS = {
			"lower(sociedad.codigoSociedad) like lower(concat(#{sociedadList.sociedad.codigoSociedad},'%'))",
			"lower(sociedad.razonSocial) like lower(concat(#{sociedadList.sociedad.razonSocial},'%'))",
			"lower(sociedad.rut) like lower(concat(#{sociedadList.sociedad.rut},'%'))", };

	private Sociedad sociedad = new Sociedad();

	public SociedadList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Sociedad getSociedad() {
		return sociedad;
	}
}
