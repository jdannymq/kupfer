package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.Canal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("canalList")
public class CanalList extends EntityQuery<Canal> {

	private static final String EJBQL = "select canal from Canal canal";

	private static final String[] RESTRICTIONS = { "lower(canal.descripcion) like lower(concat(#{canalList.canal.descripcion},'%'))", };

	private Canal canal = new Canal();

	public CanalList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Canal getCanal() {
		return canal;
	}
}
