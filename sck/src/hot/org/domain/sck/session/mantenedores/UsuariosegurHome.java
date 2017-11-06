package org.domain.sck.session.mantenedores;


import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("usuariosegurHome")
public class UsuariosegurHome extends EntityHome<Usuariosegur> {

	/**
	 * 
	 */
	@In ScoringService scoringService;
	
	private static final long serialVersionUID = 2837182419478163515L;

	public void setUsuariosegurIdPersonal(Long id) {
		setId(id);
	}

	public Long getUsuariosegurIdPersonal() {
		return (Long) getId();
	}

	@Override
	public String persist() {
		Usuariosegur usu = this.getInstance();
		if(usu != null){
			usu.setObligatorio(false);
			usu.setEliminado(false);
			usu.setCajero(null);
			usu.setAprueba(null);
			usu.setMarca(0);
			usu.setNegocio(0);
			usu.setAnexoIp("");
			usu.setCeco(null);
			usu.setIdPersonal(scoringService.getUltimaUsuarioMasUno());
			return super.persist();
		}else{
			return null;
		}
	}

	@Override
	protected Usuariosegur createInstance() {
		Usuariosegur usuariosegur = new Usuariosegur();
		return usuariosegur;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Usuariosegur getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
}
