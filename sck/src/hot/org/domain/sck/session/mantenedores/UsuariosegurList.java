package org.domain.sck.session.mantenedores;

import org.domain.sck.entity.Usuariosegur;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;

@Name("usuariosegurList")
public class UsuariosegurList extends EntityQuery<Usuariosegur> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5155151324701881499L;

	private static final String EJBQL = "select usuariosegur from Usuariosegur usuariosegur order by usuariosegur.idPersonal desc";

	private static final String[] RESTRICTIONS = {
			"lower(usuariosegur.alias) like lower(concat(#{usuariosegurList.alias},'%'))",
			"lower(usuariosegur.nombre) like lower(concat(#{usuariosegurList.nombre},'%'))",
			"lower(usuariosegur.rut) like lower(concat(#{usuariosegurList.rut},'%'))", };

	private Usuariosegur usuariosegur = new Usuariosegur();
	private String alias;
	private String rut;
	private String nombre;
	
	public UsuariosegurList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(1000);
	}

	public Usuariosegur getUsuariosegur() {
		return usuariosegur;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}
