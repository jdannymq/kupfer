package org.domain.sck.session.mantenedores;

import java.util.Arrays;

import org.domain.sck.entity.Usuarios;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("usuariosList")
public class UsuariosList extends EntityQuery<Usuarios> {

	private static final String EJBQL = "select usuarios from Usuarios usuarios";

	private static final String[] RESTRICTIONS = {
			"lower(usuarios.login) like lower(concat(#{usuariosList.usuarios.login},'%'))",
			"lower(usuarios.pass) like lower(concat(#{usuariosList.usuarios.pass},'%'))",
			"lower(usuarios.nom) like lower(concat(#{usuariosList.usuarios.nom},'%'))",
			"lower(usuarios.ape) like lower(concat(#{usuariosList.usuarios.ape},'%'))", };

	private Usuarios usuarios = new Usuarios();

	public UsuariosList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(15);
	}

	public Usuarios getUsuarios() {
		return usuarios;
	}
}
