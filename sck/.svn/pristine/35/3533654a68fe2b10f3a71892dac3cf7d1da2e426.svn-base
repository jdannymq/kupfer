package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.LcredEstado;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("lcredEstadoList")
public class LcredEstadoList extends EntityQuery<LcredEstado> {

	private static final String EJBQL = "select lcredEstado from LcredEstado lcredEstado";

	private static final String[] RESTRICTIONS = {
			"lower(lcredEstado.codEstado) like lower(concat(#{lcredEstadoList.lcredEstado.codEstado},'%'))",
			"lower(lcredEstado.desEstado) like lower(concat(#{lcredEstadoList.lcredEstado.desEstado},'%'))",
			"lower(lcredEstado.cierraProceso) like lower(concat(#{lcredEstadoList.lcredEstado.cierraProceso},'%'))", };

	private LcredEstado lcredEstado = new LcredEstado();

	public LcredEstadoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public LcredEstado getLcredEstado() {
		return lcredEstado;
	}
}
