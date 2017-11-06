package org.domain.sck.session.mantenedores;

import java.util.List;

import org.domain.sck.entity.TipoUsuario;
import org.domain.sck.entity.enums.EstadoEntityType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

@Name("tipoUsuarioHome")
public class TipoUsuarioHome extends EntityHome<TipoUsuario> {

	private List<EstadoEntityType> listaEstados = EstadoEntityType.getTipos();
	private EstadoEntityType estadoAux;
	
	public void setTipoUsuarioIdTipo(Long id) {
		setId(id);
		TipoUsuario instacia = this.getInstance();
		if(instacia != null){
			setEstadoAux(instacia.getEstado());
		}
	}

	public Long getTipoUsuarioIdTipo() {
		return (Long) getId();
	}
	

	@Override
	protected TipoUsuario createInstance() {
		TipoUsuario tipoUsuario = new TipoUsuario();
		return tipoUsuario;
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
	@Override
	public String persist() {
		if(this.getEstadoAux() == null){
			FacesMessages.instance().add(org.jboss.seam.international.StatusMessage.Severity.WARN,"Debe Seleccionar el estado...!!!!");
			return "falied";
		}
		TipoUsuario instacia = this.getInstance();
		instacia.setEstado(this.estadoAux);
		this.setInstance(instacia);
		return super.persist();
	}

	@Override
	public String update() {
		if(this.getEstadoAux() == null){
			FacesMessages.instance().add(org.jboss.seam.international.StatusMessage.Severity.WARN,"Debe Seleccionar el estado...!!!!");
			return "falied";
		}
		TipoUsuario instacia = this.getInstance();
		instacia.setEstado(this.estadoAux);
		this.setInstance(instacia);
		
		return super.update();
	}
	public TipoUsuario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	public List<EstadoEntityType> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<EstadoEntityType> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public EstadoEntityType getEstadoAux() {
		return estadoAux;
	}

	public void setEstadoAux(EstadoEntityType estadoAux) {
		this.estadoAux = estadoAux;
	}
}
