package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.LcredMotivoRechazo;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("lcredMotivoRechazoList")
public class LcredMotivoRechazoList extends EntityQuery<LcredMotivoRechazo> {

	private static final String EJBQL = "select lcredMotivoRechazo from LcredMotivoRechazo lcredMotivoRechazo";

	private static final String[] RESTRICTIONS = {
			"lower(lcredMotivoRechazo.codRechazo) like lower(concat(#{lcredMotivoRechazoList.lcredMotivoRechazo.codRechazo},'%'))",
			"lower(lcredMotivoRechazo.desRechazo) like lower(concat(#{lcredMotivoRechazoList.lcredMotivoRechazo.desRechazo},'%'))", };

	private LcredMotivoRechazo lcredMotivoRechazo;

	public LcredMotivoRechazoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(15);
	}

	public LcredMotivoRechazo getLcredMotivoRechazo() {
		return lcredMotivoRechazo;
	}
}
