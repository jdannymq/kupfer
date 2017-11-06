package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.LcredTipoSolicitud;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("lcredTipoSolicitudList")
public class LcredTipoSolicitudList extends EntityQuery<LcredTipoSolicitud> {

	private static final String EJBQL = "select lcredTipoSolicitud from LcredTipoSolicitud lcredTipoSolicitud";

	private static final String[] RESTRICTIONS = {
			"lower(lcredTipoSolicitud.codTipoSolicitud) like lower(concat(#{lcredTipoSolicitudList.lcredTipoSolicitud.codTipoSolicitud},'%'))",
			"lower(lcredTipoSolicitud.desTipoSolicitud) like lower(concat(#{lcredTipoSolicitudList.lcredTipoSolicitud.desTipoSolicitud},'%'))",
			"lower(lcredTipoSolicitud.claveProceso) like lower(concat(#{lcredTipoSolicitudList.lcredTipoSolicitud.claveProceso},'%'))", };

	private LcredTipoSolicitud lcredTipoSolicitud = new LcredTipoSolicitud();

	public LcredTipoSolicitudList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public LcredTipoSolicitud getLcredTipoSolicitud() {
		return lcredTipoSolicitud;
	}
}
