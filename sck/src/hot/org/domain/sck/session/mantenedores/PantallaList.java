package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.Pantalla;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("pantallaList")
public class PantallaList extends EntityQuery<Pantalla> {

	private static final String EJBQL = "select pantalla from Pantalla pantalla";

	private static final String[] RESTRICTIONS = { "lower(pantalla.descripcion) like lower(concat(#{pantallaList.pantalla.descripcion},'%'))", };

	private Pantalla pantalla = new Pantalla();

	public PantallaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(13);
	}

	public Pantalla getPantalla() {
		return pantalla;
	}
}
