package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.Ufs;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;

@Name("ufsList")
public class UfsList extends EntityQuery<Ufs> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select ufs from Ufs ufs order by ufs.idUfs desc";

	private static final String[] RESTRICTIONS = {};

	private Ufs ufs = new Ufs();

	public UfsList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(20);
	}

	public Ufs getUfs() {
		return ufs;
	}
}
