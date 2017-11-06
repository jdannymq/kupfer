package org.domain.sck.dto;

import java.io.Serializable;
import java.util.List;

import org.domain.sck.entity.MatrizDetalleResponsabilidad;
import org.domain.sck.entity.MatrizUsuarioResponsable;

public class MatrizResposabilidadDTO implements Serializable{

	private MatrizUsuarioResponsable matrizUsuarioResp; 
	private String nombreUsuario;
	private List<MatrizDetalleResponsabilidad> listaMatrizDetalleResp;
	
	public MatrizUsuarioResponsable getMatrizUsuarioResp() {
		return matrizUsuarioResp;
	}
	public void setMatrizUsuarioResp(MatrizUsuarioResponsable matrizUsuarioResp) {
		this.matrizUsuarioResp = matrizUsuarioResp;
	}
	public List<MatrizDetalleResponsabilidad> getListaMatrizDetalleResp() {
		return listaMatrizDetalleResp;
	}
	public void setListaMatrizDetalleResp(
			List<MatrizDetalleResponsabilidad> listaMatrizDetalleResp) {
		this.listaMatrizDetalleResp = listaMatrizDetalleResp;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	

	
}
