package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.UfSolicitudTipo;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("ufSolicitudTipoList")
public class UfSolicitudTipoList extends EntityQuery<UfSolicitudTipo> {

	private static final String EJBQL = "select ufSolicitudTipo from UfSolicitudTipo ufSolicitudTipo";

	private static final String[] RESTRICTIONS = {};

	private UfSolicitudTipo ufSolicitudTipo = new UfSolicitudTipo();

	public UfSolicitudTipoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public UfSolicitudTipo getUfSolicitudTipo() {
		return ufSolicitudTipo;
	}
}
