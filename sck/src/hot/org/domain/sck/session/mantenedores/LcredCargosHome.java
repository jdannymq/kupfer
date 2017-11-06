package org.domain.sck.session.mantenedores;

import java.util.List;


import org.domain.sck.entity.LcredCargos;
import org.domain.sck.entity.enums.EstadoType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

@Name("lcredCargosHome")
public class LcredCargosHome extends EntityHome<LcredCargos> {
	
	
	@Logger Log log;
	
	private List<EstadoType> listaEstado = EstadoType.getTipos();
	private boolean comboPrimerNivel;
	private boolean comboSegundoNivel;
	private EstadoType zona;
	private EstadoType sucursal;
	private EstadoType negocio;
	private EstadoType concepto;
	

//	private List<Long> listaNiveles;
//	private Long nivelCargo;
//	private Long nivelCargoSiguiente;
//	
//	private boolean seleccion;
//	
//	private static final  Integer INTERVALO=5;
	
	
	

	public void setLcredCargosCodCargo(String id) {
		setId(id);
		LcredCargos cargos = this.getInstance();
		if(cargos !=null){
			cargos = cargarConceptosDecargos(cargos);
			this.setInstance(cargos);
		}
	}

	
	
	public String getLcredCargosCodCargo() {
		return (String) getId();
	}

	@Override
	protected LcredCargos createInstance() {
		LcredCargos lcredCargos = new LcredCargos();
		return lcredCargos;
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

	public LcredCargos getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
	
	/*
	 * metodos de evaluaciones
	 * */
	
	@Override
	public String persist() {
		LcredCargos cargos = this.getInstance();
		try{
			String mensaje = evaluarNivelesDeCargos(cargos);
			if("".equals(mensaje)){
				cargos = evaluarConceptosDeCargos(cargos);
				this.setInstance(cargos);
				return super.persist();
			}else{
				return mensaje;
			}
		}catch (Exception e) {
			log.error("Error, al ingresar un cargo nuevo revisar el persist", e.getMessage());
			return "failed";
		}
		 
	}


	@Override
	public String update() {
		LcredCargos cargos = this.getInstance();
		try{
			String mensaje = evaluarNivelesDeCargos(cargos);
			if("".equals(mensaje)){
				cargos = evaluarConceptosDeCargos(cargos);
				this.setInstance(cargos);
				return  super.update();
			}else{
				return mensaje;
			}
		}catch (Exception e) {
			log.error("Error, al ingresar un cargo nuevo revisar el persist", e.getMessage());
			return "failed";
		}
		
	}



	public String evaluarNivelesDeCargos(LcredCargos cargos){
		try{
			if(cargos.getNivelCargo() == null || cargos.getNivelCargo() == 0){
				FacesMessages.instance().add(Severity.INFO, "El valor del nivel del cargo debe ser distinto de 0.");
				return "failed";					
			}
			if(cargos.getNivelSiguiente() == null || cargos.getNivelSiguiente() == 0){
				FacesMessages.instance().add(Severity.INFO, "El valor de nivel siguiente debe ser distinto de 0.");
				return "failed";					
			}	
			if(cargos.getNivelCargo() > cargos.getNivelSiguiente()){
				FacesMessages.instance().add(Severity.INFO, "El nivel de cargo debe ser menor al nivel de cargo siguiente.");
				return "failed";					
			}			
			if(cargos.getNivelCargo() == cargos.getNivelSiguiente()){
				FacesMessages.instance().add(Severity.INFO, "El nivel de cargo no debe ser igual al nivel de cargo siguiente.");
				return "failed";					
			}				
			return "";
			
				
		}catch (Exception e) {
			FacesMessages.instance().add(Severity.ERROR, "Error al obtener la instancia del objeto de cargos y la evaluaci�n de niveles.");
			return "failed";				
		}

	}
	
	public LcredCargos evaluarConceptosDeCargos(LcredCargos cargos){
		
		if(this.zona == null || this.zona.equals(EstadoType.NO)){
			cargos.setDetalleZona("N");
		}else if (this.zona != null && this.zona.equals(EstadoType.SI)){
			cargos.setDetalleZona("S");
		}
		if(this.sucursal == null || this.sucursal.equals(EstadoType.NO)){
			cargos.setDetalleSucursal("N");
		}else if (this.sucursal != null && this.sucursal.equals(EstadoType.SI)){
			cargos.setDetalleSucursal("S");
		}
		if(this.negocio == null || this.negocio.equals(EstadoType.NO)){
			cargos.setDetalleNegocio("N");
		}else if (this.negocio != null && this.negocio.equals(EstadoType.SI)){
			cargos.setDetalleNegocio("S");
		}
		if(this.concepto == null || this.concepto.equals(EstadoType.NO)){
			cargos.setDetalleConcepto("N");
		}else if (this.concepto != null && this.concepto.equals(EstadoType.SI)){
			cargos.setDetalleConcepto("S");
		}	
		return cargos;
	}
	
	public LcredCargos cargarConceptosDecargos(LcredCargos cargos){
		if(cargos.getDetalleZona() != null){
			if(cargos.getDetalleZona().equals("N")){
				setZona(EstadoType.NO);
			}else{
			    setZona(EstadoType.SI);
			}
		}else{ setZona(null);}
		if(cargos.getDetalleSucursal() != null){
			if(cargos.getDetalleSucursal().equals("N")){
				setSucursal(EstadoType.NO);
			}else{
				setSucursal(EstadoType.SI);
			}
		}else{setSucursal(null);}
		if(cargos.getDetalleNegocio() != null){
			if(cargos.getDetalleNegocio().equals("N")){
				setNegocio(EstadoType.NO);
			}else{
				setNegocio(EstadoType.SI);
			}
		}else{setNegocio(null);}	
		if(cargos.getDetalleConcepto() != null){
			if(cargos.getDetalleConcepto().equals("N")){
				setConcepto(EstadoType.NO);
			}else{
				setConcepto(EstadoType.SI);
			}
		}else{setConcepto(null);}	
		
		return cargos;
		
	}

	public List<EstadoType> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<EstadoType> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public boolean isComboPrimerNivel() {
		return comboPrimerNivel;
	}

	public void setComboPrimerNivel(boolean comboPrimerNivel) {
		this.comboPrimerNivel = comboPrimerNivel;
	}

	public boolean isComboSegundoNivel() {
		return comboSegundoNivel;
	}

	public void setComboSegundoNivel(boolean comboSegundoNivel) {
		this.comboSegundoNivel = comboSegundoNivel;
	}

	public EstadoType getZona() {
		return zona;
	}

	public void setZona(EstadoType zona) {
		this.zona = zona;
	}

	public EstadoType getSucursal() {
		return sucursal;
	}

	public void setSucursal(EstadoType sucursal) {
		this.sucursal = sucursal;
	}

	public EstadoType getNegocio() {
		return negocio;
	}

	public void setNegocio(EstadoType negocio) {
		this.negocio = negocio;
	}

	public EstadoType getConcepto() {
		return concepto;
	}

	public void setConcepto(EstadoType concepto) {
		this.concepto = concepto;
	}

}
