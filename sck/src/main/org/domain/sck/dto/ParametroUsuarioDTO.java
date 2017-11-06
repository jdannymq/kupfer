package org.domain.sck.dto;


public class ParametroUsuarioDTO {

	private String perfilUsuario;
	private String tipoUsuario;
	private int nivelCargo;
	
	private boolean gensol; 
	private boolean ejeAna; 
	private boolean aprRec; 
	private boolean analis; 
	private boolean agrCom; 
	private boolean solVis; 
	private boolean solAvi; 
	
	public ParametroUsuarioDTO (){
	}

	public String getPerfilUsuario() {
		return perfilUsuario;
	}

	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}

	public boolean isGensol() {
		return gensol;
	}

	public void setGensol(boolean gensol) {
		this.gensol = gensol;
	}

	public boolean isEjeAna() {
		return ejeAna;
	}

	public void setEjeAna(boolean ejeAna) {
		this.ejeAna = ejeAna;
	}

	public boolean isAprRec() {
		return aprRec;
	}

	public void setAprRec(boolean aprRec) {
		this.aprRec = aprRec;
	}

	public boolean isAnalis() {
		return analis;
	}

	public void setAnalis(boolean analis) {
		this.analis = analis;
	}

	public boolean isAgrCom() {
		return agrCom;
	}

	public void setAgrCom(boolean agrCom) {
		this.agrCom = agrCom;
	}

	public boolean isSolVis() {
		return solVis;
	}

	public void setSolVis(boolean solVis) {
		this.solVis = solVis;
	}

	public boolean isSolAvi() {
		return solAvi;
	}

	public void setSolAvi(boolean solAvi) {
		this.solAvi = solAvi;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public int getNivelCargo() {
		return nivelCargo;
	}

	public void setNivelCargo(int nivelCargo) {
		this.nivelCargo = nivelCargo;
	}
	
	
}
