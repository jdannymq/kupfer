package org.domain.sck.dto;

import java.io.Serializable;

import org.domain.sck.entity.Pantalla;

public class MenuDTO implements Serializable{

	
	/* menu principal*/
	private boolean menuPrincipal;
	
	/*sub menu de menu principal*/
	private boolean parametros;
	private boolean solicitud;
	private boolean reporte;
	
	/*link del menu paginas principales USUARIO*/
	private boolean mantenedorUsuario;
	private boolean listUsuario;
	private boolean editUsuario;
	private boolean usuario;
	
	/*link del menu paginas principales Motivo Rechazo*/
	private boolean mantenedorMotivoRechazo;
	private boolean listMotivoRechazo;
	private boolean editMotivoRechazo;
	private boolean motivoRechazo;	
	
	/*link del menu paginas principales Cargo*/
	private boolean mantenedorCargo;
	private boolean listCargo;
	private boolean editCargo;
	private boolean cargo;		
	
	/*link del menu paginas principales Tipo Solicitud*/
	private boolean mantenedorTipoSolicitud;
	private boolean listTipoSolicitud;
	private boolean editTipoSolicitud;
	private boolean tipoSolicitud;		
	
	
	/*link del menu paginas principales Pantalla*/
	private boolean mantenedorPantalla;
	private boolean listPantalla;
	private boolean editPantalla;
	private boolean pantalla;		
	
	/*link del menu paginas principales TipoUsuario*/
	private boolean mantenedorTipoUsuario;
	private boolean listTipoUsuario;
	private boolean editTipoUsuario;
	private boolean tipoUsuario;
	
	/*link del menu paginas principales usuario tipo */
	private boolean mantenedorUsuarioTipo;
	private boolean usuarioTipo;

	/*link del menu paginas principales tipo pantalla */
	private boolean mantenedorTipoPantalla;
	private boolean tipoPantalla;

	/*link del menu paginas principales ingreso de usuarios */
	private boolean mantenedorIngresoUsuarios;
	private boolean ingresoUsuarios;

	/*link del menu paginas principales Cargo*/
	private boolean mantenedorPantallaSeleccion;
	private boolean seleccion;
	private boolean generarSolicitudes;
	private boolean respuestaSolicitudes;
	private boolean listadoSolicitudes;
	
	/*link del menu paginas principales Cargo*/
	private boolean mantenedorEstado;
	private boolean listEstado;
	private boolean editEstado;
	private boolean estado;	

	/*link del menu paginas principales Tipo Forma Pago*/
	private boolean mantenedorTipoFormaPago;
	private boolean listTipoFormaPago;
	private boolean editTipoFormaPago;
	private boolean tipoFormaPago;	
	
	/*link del menu paginas principales Tipo Forma Pago*/
	private boolean mantenedorFormaPago;
	private boolean listFormaPago;
	private boolean editFormaPago;
	private boolean formaPago;	
	
	/*link del menu paginas principales ingreso de usuarios */
	private boolean mantenedorAsociarTipoAndFormaPago;
	private boolean asociarTipoAndFormaPago;
	
	/*variable de mantenedor de uf*/
	private boolean mantenedorUf;
	private boolean ufEdit;
	private boolean ufList;
	private boolean uf;	
	
	
	/*link del menu paginas principales liberar solicitudess */
	private boolean mantenedorLiberarSolicitudes;
	private boolean liberarSolicitudes;
	
	public MenuDTO(){
		this.menuPrincipal = true;
	}
	

	
	public void habilitarMenu(Pantalla pantalla){
		if(pantalla.getIdPantalla()== 1 || pantalla.getIdPantalla() == 2 || pantalla.getIdPantalla() == 3){/*habilitar el menu de Usuario*/
			this.parametros = true;
			this.mantenedorUsuario = true;
		}else if(pantalla.getIdPantalla()== 4 || pantalla.getIdPantalla() == 5 || pantalla.getIdPantalla() == 6){/*habilitar el menu de Tipo de Usuario*/
			this.parametros = true;
			this.mantenedorTipoUsuario = true;
		}else if(pantalla.getIdPantalla()== 7 || pantalla.getIdPantalla() == 8 || pantalla.getIdPantalla() == 9){/*habilitar el menu de Pantlla*/
			this.parametros = true;
			this.mantenedorPantalla = true;
		}else if(pantalla.getIdPantalla()== 10){/*habilitar el menu Asociar Tipo de Usuario a Pantalla */
			this.parametros = true;
			this.mantenedorTipoPantalla = true;
		}else if(pantalla.getIdPantalla()== 11){/*habilitar el menu Asociar Usuario a tipo de  Usuario */
			this.parametros = true;
			this.mantenedorUsuarioTipo = true;
		}else if(pantalla.getIdPantalla()== 12 || pantalla.getIdPantalla() == 13 || pantalla.getIdPantalla() == 14){/*habilitar el menu de motivo de rechazo*/
			this.parametros = true;
			this.mantenedorMotivoRechazo = true;
		}else if(pantalla.getIdPantalla()== 15 || pantalla.getIdPantalla() == 16 || pantalla.getIdPantalla() == 17){/*habilitar el menu de cargos*/
			this.parametros = true;
			this.mantenedorCargo = true;
		}else if(pantalla.getIdPantalla()== 18 || pantalla.getIdPantalla() == 19 || pantalla.getIdPantalla() == 20){/*habilitar el menu de tipo de solicitud*/
			this.parametros = true;
			this.mantenedorTipoSolicitud = true;
		}else if(pantalla.getIdPantalla()== 21){/*habilitar el menu de usuario de perfiles de usuarios*/
			this.parametros = true;
			this.mantenedorIngresoUsuarios = true;
		}else if(pantalla.getIdPantalla() == 22 || pantalla.getIdPantalla() == 23 || pantalla.getIdPantalla() == 24 
				|| pantalla.getIdPantalla() == 25 ){/*habilitar el menu pantalla de seleccion*/
			this.solicitud = true;
			this.mantenedorPantallaSeleccion = true;
		}else if(pantalla.getIdPantalla()== 26 || pantalla.getIdPantalla() == 27 || pantalla.getIdPantalla() == 28){/*habilitar el menu de estados*/
			this.parametros = true;
			this.mantenedorEstado = true;
		}else if(pantalla.getIdPantalla()== 29 || pantalla.getIdPantalla() == 30 || pantalla.getIdPantalla() == 31){/*habilitar el menu de tipo de forma de pago*/
			this.parametros = true;
			this.mantenedorTipoFormaPago = true;
		}else if(pantalla.getIdPantalla()== 32 || pantalla.getIdPantalla() == 33 || pantalla.getIdPantalla() == 34){/*habilitar el menu de forma de pago*/
			this.parametros = true;
			this.mantenedorFormaPago = true;
		}else if(pantalla.getIdPantalla()== 35 ){/*habilitar el menu de asociar el tipo de pago con la forma de pago*/
			this.parametros = true;
			this.mantenedorAsociarTipoAndFormaPago = true;
		}else if(pantalla.getIdPantalla()== 36 || pantalla.getIdPantalla()== 37 || pantalla.getIdPantalla()== 38 ){/*habilitar el menu del mantenedor de uf*/
			this.parametros = true;
			this.mantenedorUf = true;
		}else if(pantalla.getIdPantalla()== 39){/*habilitar el menu del mantenedor de uf*/
			this.parametros = true;
			this.mantenedorLiberarSolicitudes = true;
		}
		
	}
	public void hablitarPantalla(Pantalla pantalla){
		switch (pantalla.getIdPantalla().intValue()) {
		case 1: 
			this.listUsuario = true;
			break;
		case 2: 
			this.editUsuario=true;
			break;
		case 3: 
			this.usuario = true;
			break;
		case 4: 
			this.listTipoUsuario = true;
			break;			
		case 5: 
			this.editTipoUsuario = true;
			break;	
		case 6: 
			this.tipoUsuario = true;
			break;	
		case 7: 
			this.listPantalla = true;
			break;			
		case 8: 
			this.editPantalla = true;
			break;	
		case 9: 
			this.pantalla = true;
			break;	
		case 10: 
			this.tipoPantalla = true;
			break;	
		case 11: 
			this.usuarioTipo = true;
			break;			
		case 12: 
			this.listMotivoRechazo = true;
			break;	
		case 13: 
			this.editMotivoRechazo = true;
			break;	
		case 14: 
			this.motivoRechazo = true;
			break;	
		case 15: 
			this.listCargo = true;
			break;	
		case 16: 
			this.editCargo = true;
			break;				
		case 17: 
			this.cargo = true;
			break;	
		case 18: 
			this.listTipoSolicitud = true;
			break;	
		case 19: 
			this.editTipoSolicitud = true;
			break;	
		case 20: 
			this.tipoSolicitud = true;
			break;	
		case 21: 
			this.ingresoUsuarios = true;
			break;	
		case 22: 
			this.seleccion = true;
			break;	
		case 23: 
			this.generarSolicitudes = true;
			break;	
		case 24: 
			this.respuestaSolicitudes = true;
			break;	
		case 25: 
			this.listadoSolicitudes = true;
			break;
		case 26: 
			this.listEstado = true;
			break;	
		case 27: 
			this.editEstado = true;
			break;	
		case 28: 
			this.estado = true;
			break;			
		case 29: 
			this.listTipoFormaPago = true;
			break;	
		case 30: 
			this.editTipoFormaPago = true;
			break;	
		case 31: 
			this.tipoFormaPago = true;
			break;	
		case 32: 
			this.listFormaPago = true;
			break;	
		case 33: 
			this.editFormaPago = true;
			break;	
		case 34: 
			this.formaPago = true;
			break;	
		case 35: 
			this.asociarTipoAndFormaPago = true;
			break;	
		case 36: 
			this.ufList = true;
			break;	
		case 37: 
			this.ufEdit = true;
			break;
		case 38: 
			this.uf = true;
			break;	
		case 39: 
			this.liberarSolicitudes = true;
			break;				
			
			
		default:
			break;
		}
		
	}


	public void habilitarPorDefecto(){
		this.menuPrincipal = true;
		this.parametros = true;
		
		this.mantenedorUsuario = true;
		this.listUsuario = true;
		this.editUsuario = true;
		this.usuario = true;
		
		this.mantenedorPantalla = true;
		this.listPantalla = true;
		this.editPantalla = true;
		this.pantalla = true;
		
		this.mantenedorTipoUsuario = true;
		this.listTipoUsuario = true;
		this.editTipoUsuario = true;
		this.tipoUsuario = true;
		
		this.mantenedorUsuarioTipo = true;
		this.usuarioTipo = true;
		
		this.mantenedorTipoPantalla = true;
		this.tipoPantalla = true;
		
		
	}
	
	
	
    /*gets y sets*/
	public boolean isMenuPrincipal() {
		return menuPrincipal;
	}


	public void setMenuPrincipal(boolean menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
	}


	public boolean isParametros() {
		return parametros;
	}


	public void setParametros(boolean parametros) {
		this.parametros = parametros;
	}


	public boolean isSolicitud() {
		return solicitud;
	}


	public void setSolicitud(boolean solicitud) {
		this.solicitud = solicitud;
	}


	public boolean isReporte() {
		return reporte;
	}


	public void setReporte(boolean reporte) {
		this.reporte = reporte;
	}


	public boolean isMantenedorUsuario() {
		return mantenedorUsuario;
	}


	public void setMantenedorUsuario(boolean mantenedorUsuario) {
		this.mantenedorUsuario = mantenedorUsuario;
	}


	public boolean isListUsuario() {
		return listUsuario;
	}


	public void setListUsuario(boolean listUsuario) {
		this.listUsuario = listUsuario;
	}


	public boolean isEditUsuario() {
		return editUsuario;
	}


	public void setEditUsuario(boolean editUsuario) {
		this.editUsuario = editUsuario;
	}


	public boolean isUsuario() {
		return usuario;
	}


	public void setUsuario(boolean usuario) {
		this.usuario = usuario;
	}


	public boolean isMantenedorMotivoRechazo() {
		return mantenedorMotivoRechazo;
	}


	public void setMantenedorMotivoRechazo(boolean mantenedorMotivoRechazo) {
		this.mantenedorMotivoRechazo = mantenedorMotivoRechazo;
	}


	public boolean isListMotivoRechazo() {
		return listMotivoRechazo;
	}


	public void setListMotivoRechazo(boolean listMotivoRechazo) {
		this.listMotivoRechazo = listMotivoRechazo;
	}


	public boolean isEditMotivoRechazo() {
		return editMotivoRechazo;
	}


	public void setEditMotivoRechazo(boolean editMotivoRechazo) {
		this.editMotivoRechazo = editMotivoRechazo;
	}


	public boolean isMotivoRechazo() {
		return motivoRechazo;
	}


	public void setMotivoRechazo(boolean motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}


	public boolean isMantenedorCargo() {
		return mantenedorCargo;
	}


	public void setMantenedorCargo(boolean mantenedorCargo) {
		this.mantenedorCargo = mantenedorCargo;
	}


	public boolean isListCargo() {
		return listCargo;
	}


	public void setListCargo(boolean listCargo) {
		this.listCargo = listCargo;
	}


	public boolean isEditCargo() {
		return editCargo;
	}


	public void setEditCargo(boolean editCargo) {
		this.editCargo = editCargo;
	}


	public boolean isCargo() {
		return cargo;
	}


	public void setCargo(boolean cargo) {
		this.cargo = cargo;
	}


	public boolean isMantenedorTipoSolicitud() {
		return mantenedorTipoSolicitud;
	}


	public void setMantenedorTipoSolicitud(boolean mantenedorTipoSolicitud) {
		this.mantenedorTipoSolicitud = mantenedorTipoSolicitud;
	}


	public boolean isListTipoSolicitud() {
		return listTipoSolicitud;
	}


	public void setListTipoSolicitud(boolean listTipoSolicitud) {
		this.listTipoSolicitud = listTipoSolicitud;
	}


	public boolean isEditTipoSolicitud() {
		return editTipoSolicitud;
	}


	public void setEditTipoSolicitud(boolean editTipoSolicitud) {
		this.editTipoSolicitud = editTipoSolicitud;
	}


	public boolean isTipoSolicitud() {
		return tipoSolicitud;
	}


	public void setTipoSolicitud(boolean tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}


	public boolean isMantenedorPantallaSeleccion() {
		return mantenedorPantallaSeleccion;
	}


	public void setMantenedorPantallaSeleccion(boolean mantenedorPantallaSeleccion) {
		this.mantenedorPantallaSeleccion = mantenedorPantallaSeleccion;
	}


	public boolean isSeleccion() {
		return seleccion;
	}


	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}


	public boolean isGenerarSolicitudes() {
		return generarSolicitudes;
	}


	public void setGenerarSolicitudes(boolean generarSolicitudes) {
		this.generarSolicitudes = generarSolicitudes;
	}


	public boolean isRespuestaSolicitudes() {
		return respuestaSolicitudes;
	}


	public void setRespuestaSolicitudes(boolean respuestaSolicitudes) {
		this.respuestaSolicitudes = respuestaSolicitudes;
	}


	public boolean isMantenedorPantalla() {
		return mantenedorPantalla;
	}


	public void setMantenedorPantalla(boolean mantenedorPantalla) {
		this.mantenedorPantalla = mantenedorPantalla;
	}


	public boolean isListPantalla() {
		return listPantalla;
	}


	public void setListPantalla(boolean listPantalla) {
		this.listPantalla = listPantalla;
	}


	public boolean isEditPantalla() {
		return editPantalla;
	}


	public void setEditPantalla(boolean editPantalla) {
		this.editPantalla = editPantalla;
	}


	public boolean isPantalla() {
		return pantalla;
	}


	public void setPantalla(boolean pantalla) {
		this.pantalla = pantalla;
	}


	public boolean isMantenedorTipoUsuario() {
		return mantenedorTipoUsuario;
	}


	public void setMantenedorTipoUsuario(boolean mantenedorTipoUsuario) {
		this.mantenedorTipoUsuario = mantenedorTipoUsuario;
	}


	public boolean isListTipoUsuario() {
		return listTipoUsuario;
	}


	public void setListTipoUsuario(boolean listTipoUsuario) {
		this.listTipoUsuario = listTipoUsuario;
	}


	public boolean isEditTipoUsuario() {
		return editTipoUsuario;
	}


	public void setEditTipoUsuario(boolean editTipoUsuario) {
		this.editTipoUsuario = editTipoUsuario;
	}


	public boolean isTipoUsuario() {
		return tipoUsuario;
	}


	public void setTipoUsuario(boolean tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}


	public boolean isMantenedorUsuarioTipo() {
		return mantenedorUsuarioTipo;
	}


	public void setMantenedorUsuarioTipo(boolean mantenedorUsuarioTipo) {
		this.mantenedorUsuarioTipo = mantenedorUsuarioTipo;
	}


	public boolean isUsuarioTipo() {
		return usuarioTipo;
	}


	public void setUsuarioTipo(boolean usuarioTipo) {
		this.usuarioTipo = usuarioTipo;
	}


	public boolean isMantenedorTipoPantalla() {
		return mantenedorTipoPantalla;
	}


	public void setMantenedorTipoPantalla(boolean mantenedorTipoPantalla) {
		this.mantenedorTipoPantalla = mantenedorTipoPantalla;
	}


	public boolean isTipoPantalla() {
		return tipoPantalla;
	}


	public void setTipoPantalla(boolean tipoPantalla) {
		this.tipoPantalla = tipoPantalla;
	}



	public boolean isMantenedorIngresoUsuarios() {
		return mantenedorIngresoUsuarios;
	}



	public void setMantenedorIngresoUsuarios(boolean mantenedorIngresoUsuarios) {
		this.mantenedorIngresoUsuarios = mantenedorIngresoUsuarios;
	}



	public boolean isIngresoUsuarios() {
		return ingresoUsuarios;
	}



	public void setIngresoUsuarios(boolean ingresoUsuarios) {
		this.ingresoUsuarios = ingresoUsuarios;
	}



	public boolean isListadoSolicitudes() {
		return listadoSolicitudes;
	}



	public void setListadoSolicitudes(boolean listadoSolicitudes) {
		this.listadoSolicitudes = listadoSolicitudes;
	}



	public boolean isMantenedorEstado() {
		return mantenedorEstado;
	}



	public void setMantenedorEstado(boolean mantenedorEstado) {
		this.mantenedorEstado = mantenedorEstado;
	}



	public boolean isListEstado() {
		return listEstado;
	}



	public void setListEstado(boolean listEstado) {
		this.listEstado = listEstado;
	}



	public boolean isEditEstado() {
		return editEstado;
	}



	public void setEditEstado(boolean editEstado) {
		this.editEstado = editEstado;
	}



	public boolean isEstado() {
		return estado;
	}



	public void setEstado(boolean estado) {
		this.estado = estado;
	}



	public boolean isMantenedorTipoFormaPago() {
		return mantenedorTipoFormaPago;
	}



	public void setMantenedorTipoFormaPago(boolean mantenedorTipoFormaPago) {
		this.mantenedorTipoFormaPago = mantenedorTipoFormaPago;
	}



	public boolean isListTipoFormaPago() {
		return listTipoFormaPago;
	}



	public void setListTipoFormaPago(boolean listTipoFormaPago) {
		this.listTipoFormaPago = listTipoFormaPago;
	}



	public boolean isEditTipoFormaPago() {
		return editTipoFormaPago;
	}



	public void setEditTipoFormaPago(boolean editTipoFormaPago) {
		this.editTipoFormaPago = editTipoFormaPago;
	}



	public boolean isTipoFormaPago() {
		return tipoFormaPago;
	}



	public void setTipoFormaPago(boolean tipoFormaPago) {
		this.tipoFormaPago = tipoFormaPago;
	}



	public boolean isMantenedorFormaPago() {
		return mantenedorFormaPago;
	}



	public void setMantenedorFormaPago(boolean mantenedorFormaPago) {
		this.mantenedorFormaPago = mantenedorFormaPago;
	}



	public boolean isListFormaPago() {
		return listFormaPago;
	}



	public void setListFormaPago(boolean listFormaPago) {
		this.listFormaPago = listFormaPago;
	}



	public boolean isEditFormaPago() {
		return editFormaPago;
	}



	public void setEditFormaPago(boolean editFormaPago) {
		this.editFormaPago = editFormaPago;
	}


	public boolean isFormaPago() {
		return formaPago;
	}


	public void setFormaPago(boolean formaPago) {
		this.formaPago = formaPago;
	}



	public boolean isMantenedorAsociarTipoAndFormaPago() {
		return mantenedorAsociarTipoAndFormaPago;
	}



	public void setMantenedorAsociarTipoAndFormaPago(
			boolean mantenedorAsociarTipoAndFormaPago) {
		this.mantenedorAsociarTipoAndFormaPago = mantenedorAsociarTipoAndFormaPago;
	}



	public boolean isAsociarTipoAndFormaPago() {
		return asociarTipoAndFormaPago;
	}



	public void setAsociarTipoAndFormaPago(boolean asociarTipoAndFormaPago) {
		this.asociarTipoAndFormaPago = asociarTipoAndFormaPago;
	}



	public boolean isMantenedorUf() {
		return mantenedorUf;
	}



	public void setMantenedorUf(boolean mantenedorUf) {
		this.mantenedorUf = mantenedorUf;
	}



	public boolean isUfEdit() {
		return ufEdit;
	}



	public void setUfEdit(boolean ufEdit) {
		this.ufEdit = ufEdit;
	}



	public boolean isUfList() {
		return ufList;
	}



	public void setUfList(boolean ufList) {
		this.ufList = ufList;
	}



	public boolean isUf() {
		return uf;
	}



	public void setUf(boolean uf) {
		this.uf = uf;
	}



	public boolean isMantenedorLiberarSolicitudes() {
		return mantenedorLiberarSolicitudes;
	}



	public void setMantenedorLiberarSolicitudes(boolean mantenedorLiberarSolicitudes) {
		this.mantenedorLiberarSolicitudes = mantenedorLiberarSolicitudes;
	}



	public boolean isLiberarSolicitudes() {
		return liberarSolicitudes;
	}



	public void setLiberarSolicitudes(boolean liberarSolicitudes) {
		this.liberarSolicitudes = liberarSolicitudes;
	}
	

	
	
}
