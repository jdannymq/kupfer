package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.Feriado;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("feriadoList")
public class FeriadoList extends EntityQuery<Feriado> {

	private static final String EJBQL = "select feriado from Feriado feriado order by feriado.systemId asc ";

	private static final String[] RESTRICTIONS = {};

	private Feriado feriado = new Feriado();
	
	private String tsFeriados;
	

	public FeriadoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Feriado getFeriado() {
		return feriado;
	}

	public String getTsFeriados() {
		return tsFeriados;
	}

	public void setTsFeriados(String tsFeriados) {
		this.tsFeriados = tsFeriados;
	}
	
	
	
	
	
	
}
