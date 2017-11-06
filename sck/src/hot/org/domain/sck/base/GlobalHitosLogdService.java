package org.domain.sck.base;

import java.util.List;

import org.domain.sck.dto.SolicitudDTO;
import org.domain.sck.entity.SolicitudAprobaRechaza;
import org.domain.sck.entity.SolicitudHitos;
import org.domain.sck.entity.SolicitudLogs;
import org.domain.sck.entity.SolicitudUsuarioDerivada;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name(value = "globalHitosLogdService")
@Scope(ScopeType.SESSION)
@AutoCreate
public class GlobalHitosLogdService {
	
	private SolicitudDTO solicitud;
	private List<SolicitudHitos> listaHitos;
	private List<SolicitudLogs> listaLogs;
	private List<SolicitudAprobaRechaza> listaAprobadoToRechazado;
	private List<SolicitudUsuarioDerivada> listUsuariosDerivados;
	
	
	
	public SolicitudDTO getSolicitud() {
		return solicitud;
	}
	public void setSolicitud(SolicitudDTO solicitud) {
		this.solicitud = solicitud;
	}
	public List<SolicitudHitos> getListaHitos() {
		return listaHitos;
	}
	public void setListaHitos(List<SolicitudHitos> listaHitos) {
		this.listaHitos = listaHitos;
	}
	public List<SolicitudLogs> getListaLogs() {
		return listaLogs;
	}
	public void setListaLogs(List<SolicitudLogs> listaLogs) {
		this.listaLogs = listaLogs;
	}
	public List<SolicitudAprobaRechaza> getListaAprobadoToRechazado() {
		return listaAprobadoToRechazado;
	}
	public void setListaAprobadoToRechazado(
			List<SolicitudAprobaRechaza> listaAprobadoToRechazado) {
		this.listaAprobadoToRechazado = listaAprobadoToRechazado;
	}
	public List<SolicitudUsuarioDerivada> getListUsuariosDerivados() {
		return listUsuariosDerivados;
	}
	public void setListUsuariosDerivados(
			List<SolicitudUsuarioDerivada> listUsuariosDerivados) {
		this.listUsuariosDerivados = listUsuariosDerivados;
	}
	
	public void limpiarHitos(){
		this.solicitud = null;
		this.listaHitos = null;
		this.listaLogs = null;
		this.listaAprobadoToRechazado = null;
		this.listUsuariosDerivados = null;
		
	}
	public void limpiarAprobaRechaza(){
		this.solicitud = null;
		this.listaAprobadoToRechazado = null;
		this.listUsuariosDerivados = null;
		
	}

	
	
}
