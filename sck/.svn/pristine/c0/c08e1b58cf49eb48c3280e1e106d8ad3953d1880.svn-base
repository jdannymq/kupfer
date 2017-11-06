package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.Ufs;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("ufList")
public class UfList extends EntityQuery<Ufs> {

	private static final String EJBQL = "select uf from Ufs uf order by uf.fecha desc";

	private static final String[] RESTRICTIONS = {};

	private Ufs uf = new Ufs();

	public UfList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(15);
	}

	public Ufs getUf() {
		return uf;
	}
}
