package org.domain.sck.dto;

import java.util.Date;
import java.util.List;

import org.domain.sck.entity.Canal;
import org.domain.sck.entity.ConceptosNegocio;
import org.domain.sck.entity.LcredTipoSolicitud;
import org.domain.sck.entity.Sucursal;
import org.domain.sck.entity.Usuariosegur;



public class FiltroDTO {
	
	private List<Canal> listaCanalesSeleccionada;
	private List<ConceptosNegocio> listaNegociosSeleccionados;
	private List<LcredTipoSolicitud> listaLcredTipoSolicitudsSeleccionados;
	private List<Sucursal> listaSucursalsSeleccionados;
	private List<Usuariosegur> listaUsuariosegursSeleccionados;
	private List<Usuariosegur> listaAprobadoresSeleccionados;
	private Date fechaInicial;
	private Date fechaFinal;
	public List<Canal> getListaCanalesSeleccionada() {
		return listaCanalesSeleccionada;
	}
	public void setListaCanalesSeleccionada(List<Canal> listaCanalesSeleccionada) {
		this.listaCanalesSeleccionada = listaCanalesSeleccionada;
	}
	public List<ConceptosNegocio> getListaNegociosSeleccionados() {
		return listaNegociosSeleccionados;
	}
	public void setListaNegociosSeleccionados(
			List<ConceptosNegocio> listaNegociosSeleccionados) {
		this.listaNegociosSeleccionados = listaNegociosSeleccionados;
	}
	public List<LcredTipoSolicitud> getListaLcredTipoSolicitudsSeleccionados() {
		return listaLcredTipoSolicitudsSeleccionados;
	}
	public void setListaLcredTipoSolicitudsSeleccionados(
			List<LcredTipoSolicitud> listaLcredTipoSolicitudsSeleccionados) {
		this.listaLcredTipoSolicitudsSeleccionados = listaLcredTipoSolicitudsSeleccionados;
	}
	public List<Sucursal> getListaSucursalsSeleccionados() {
		return listaSucursalsSeleccionados;
	}
	public void setListaSucursalsSeleccionados(
			List<Sucursal> listaSucursalsSeleccionados) {
		this.listaSucursalsSeleccionados = listaSucursalsSeleccionados;
	}
	public List<Usuariosegur> getListaUsuariosegursSeleccionados() {
		return listaUsuariosegursSeleccionados;
	}
	public void setListaUsuariosegursSeleccionados(
			List<Usuariosegur> listaUsuariosegursSeleccionados) {
		this.listaUsuariosegursSeleccionados = listaUsuariosegursSeleccionados;
	}
	
	public List<Usuariosegur> getListaAprobadoresSeleccionados() {
		return listaAprobadoresSeleccionados;
	}
	
	public void setListaAprobadoresSeleccionados(
			List<Usuariosegur> listaAprobadoresSeleccionados) {
		this.listaAprobadoresSeleccionados = listaAprobadoresSeleccionados;
	}
	
	public Date getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public Date getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	
	
	
	
	
	
	
}
