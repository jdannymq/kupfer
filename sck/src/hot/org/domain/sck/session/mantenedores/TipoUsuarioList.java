package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.TipoUsuario;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("tipoUsuarioList")
public class TipoUsuarioList extends EntityQuery<TipoUsuario> {

	private static final String EJBQL = "select tipoUsuario from TipoUsuario tipoUsuario";

	private static final String[] RESTRICTIONS = {
		"lower(tipoUsuario.descripcion) like lower(concat(#{tipoUsuarioList.tipoUsuario.descripcion},'%'))",
		"lower(tipoUsuario.abreviado) like lower(concat(#{tipoUsuarioList.tipoUsuario.abreviado},'%'))", };

	private TipoUsuario tipoUsuario = new TipoUsuario();

	public TipoUsuarioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}
}
