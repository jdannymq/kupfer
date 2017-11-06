package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.LcredCargos;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("lcredCargosList")
public class LcredCargosList extends EntityQuery<LcredCargos> {

	private static final String EJBQL = "select lcredCargos from LcredCargos lcredCargos";

	private static final String[] RESTRICTIONS = {
			"lower(lcredCargos.codCargo) like lower(concat(#{lcredCargosList.lcredCargos.codCargo},'%'))",
			"lower(lcredCargos.desCargo) like lower(concat(#{lcredCargosList.lcredCargos.desCargo},'%'))", };

	private LcredCargos lcredCargos = new LcredCargos();

	public LcredCargosList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(15);
	}

	public LcredCargos getLcredCargos() {
		return lcredCargos;
	}
}
