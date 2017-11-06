package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.Medicion;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("medicionList")
public class MedicionList extends EntityQuery<Medicion> {

	private static final String EJBQL = "select medicion from Medicion medicion";

	private static final String[] RESTRICTIONS = { "lower(medicion.descripcion) like lower(concat(#{medicionList.medicion.descripcion},'%'))", };

	private Medicion medicion = new Medicion();

	public MedicionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Medicion getMedicion() {
		return medicion;
	}
}
