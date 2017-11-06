package org.domain.sck.session.action;

import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.entity.FormaPago;
import org.domain.sck.entity.FormaPagoDetalle;
import org.domain.sck.entity.FormaPagoEncabezado;
import org.domain.sck.entity.LcredTipoSolicitud;
import org.domain.sck.entity.TipoFormaPago;
import org.domain.sck.entity.enums.EstadoEntityType;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


@Name("asociarTipoAndFormaPagoAction")
@Scope(ScopeType.CONVERSATION)
public class AsociarTipoAndFormaPagoAction {


	
    @Logger private Log log;

	@In(value="#{entityManager}")
	EntityManager entityManager;
	
	private String mensaje;
	
	private TipoFormaPago tipoFormaPago;
	private LcredTipoSolicitud tipoSolicitud;
	private List<TipoFormaPago> listaTipoFormaPago;
	private List<FormaPago> listaFormaPagos;
	private List<FormaPago> listaFormaPagosSeleccionados;
	private List<FormaPagoEncabezado> listaFormaPagosEncabezado;
	
	private List<LcredTipoSolicitud> listaTipoSolictudes;
	
	
	
	@SuppressWarnings("unchecked")
	@Create
    public void init(){
    	log.debug("iniciando AsociarTipoAndFormaPagoAction...");
    	try{
  		
    		listaTipoFormaPago = ( List<TipoFormaPago>)entityManager
				.createQuery("select u from TipoFormaPago u")
				.getResultList();
    		
    		listaTipoSolictudes =(List<LcredTipoSolicitud>)entityManager
    				 .createQuery("select ts from LcredTipoSolicitud ts where ts.codTipoSolicitud  in  (11,12)")
    				 .getResultList();
    		
    		
    		sacarListaFormaDePagoencabezado(null,null);
    	}catch (Exception e) {
    		log.error("Error, al sacar la lista de restricciones del archivo de carga de alumnos: #0", e.getMessage());
		}
    	log.debug("finalizando AsociarTipoAndFormaPagoAction...");
    	return;    	
    	
    }
	
	@Destroy
    public void destroy() {
    	log.debug("destruyendo instancia AsociarTipoAndFormaPagoAction");
    }
	
	@SuppressWarnings("unchecked")
	public void eliminarFormaPagoEncabezado(FormaPagoEncabezado fpe){
		List<FormaPagoDetalle> lista = null;
		if(fpe != null){
			try{
			      lista = ( List<FormaPagoDetalle>)entityManager
						.createQuery("select u from FormaPagoDetalle u where u.encabezado.idEncabezado=:idEncabezado")
						.setParameter("idEncabezado", fpe.getIdEncabezado())
						.getResultList();
				
			}catch (Exception e) {
				log.error("Error, al sacar la lista de detalle del encanbezado.", e.getMessage());
			}
			
			try{
				if(lista != null){
					for(FormaPagoDetalle d : lista){
						entityManager.remove(d);
					}
					entityManager.flush();
				}
			}catch (Exception e) {
				log.error("Error, al eliminar los obejtos de encabezado detalle.", e.getMessage());
			}
			
			try{
				fpe.setEstado(EstadoEntityType.INACTIVO);
				entityManager.merge(fpe);
				entityManager.flush();
				setMensaje("El registro fue eliminado exitosamente.");
			}catch (Exception e) {
				log.error("Error, al eliminar el objeto de enzabezado de forma de pago, #0", e.getMessage());
				setMensaje("Error, al eliminar el objeto de enzabezado de forma de pago.");
			}
			
			if(tipoFormaPago != null){
				sacarListaFormaDePagoencabezado(tipoFormaPago.getIdTipoFormaPago(), this.tipoSolicitud.getCodTipoSolicitud());
			}
		}
	}
	
	
	@SuppressWarnings({"unchecked" })
	public void consultarFormaPago(){
		if(this.tipoFormaPago == null){
			setMensaje("Debe Seleccionar un tipo de forma de pago para consultar.");
			return;
		}
		
		if(this.tipoSolicitud ==  null){
			setMensaje("Debe Seleccionar un tipo de solicitud para la asignación.");
			return;			
		}
		
		try{
			if(listaFormaPagos != null){
				listaFormaPagos.clear();
			}
			if(listaFormaPagosSeleccionados != null){
				listaFormaPagosSeleccionados.clear();
			}
			
			listaFormaPagos = ( List<FormaPago>)entityManager
								.createQuery("select u from FormaPago u")
								.getResultList();
			
			if(this.tipoFormaPago != null && this.tipoSolicitud != null){
				sacarListaFormaDePagoencabezado(tipoFormaPago.getIdTipoFormaPago(), tipoSolicitud.getCodTipoSolicitud());
			}			
			
		}catch (Exception e) {
			log.error("Error, al sacar la lista de de forma de pagos.", e.getMessage());
		}
		
	}
	
	public void asignarFormaPago(){
		if(this.tipoFormaPago == null){
			setMensaje("Tiene que seleccionar por obligación un tipo de forma de pago.");
			return;
		}		
		if(this.tipoSolicitud == null){
			setMensaje("Tiene que seleccionar por obligación un tipo de solicitud.");
			return;
		}	
		if(this.listaFormaPagosSeleccionados == null){
			setMensaje("Tiene que seleccionar por lo menos una forma de pago para asignar.");
			return;
		}
		try{
			StringBuffer cadena = new StringBuffer();
			/*generar el String descripcion para el encabezado*/
			int cantidad = listaFormaPagosSeleccionados.size();
			int contador = 0;
			for(FormaPago fp: listaFormaPagosSeleccionados){
				if(fp != null){
					if(cantidad == 1){
						cadena.append(fp.getDescripcion());
					}else{
						if(contador== 0){
							if(fp.getDias() == 0){
								cadena.append(fp.getDescripcion());
								cadena.append(",");
							}else{
								cadena.append(fp.getDescripcion());
								cadena.append(",");
							}
						}else{
							if(fp.getDias() == 0){
								cadena.append(fp.getDescripcion());
								cadena.append(",");
							}else{
								if(contador != (cantidad-1)){
									cadena.append( fp.getDias() +" Dias");
									cadena.append(",");									
								}else{
									cadena.append( fp.getDias() +" Dias");
									cadena.append(".");									
								}

							}							
						}
						
					}
				}
				contador++;
			}
			
			if(this.tipoSolicitud.getCodTipoSolicitud().equals("11")){
				if(contador > 2){
					setMensaje("Debe ingresar solo dos forma de pago para el tipo de solicitud seleccionado...");
				}else{
					boolean exito = false;
					FormaPago fp = listaFormaPagosSeleccionados.get(0);
					if(fp.getIdFormaPago().longValue() == 2){
						exito = true;
					}
					if(exito == false){
						setMensaje("Esta mal seleccionado las formas de pago, debe seguir el ejemplo : Efectivo,15 Dias.");
					}
				}
				
			}else if(this.tipoSolicitud.getCodTipoSolicitud().equals("12")){
				if(contador < 3){
					setMensaje("Debe ingresar mas dos forma de pago para el tipo de solicitud seleccionado...");
				}else{
					boolean exito = false;
					FormaPago fp = listaFormaPagosSeleccionados.get(0);
					if(fp.getIdFormaPago().longValue() == 2){
						exito = true;
					}
					if(exito){
						setMensaje("Esta mal seleccionado las formas de pago, debe seguir el ejemplo : 45 Dias,90 Dias,100 Dias.");
					}
				}
			}
			
			
			
			
			/*se inicia el guardado de los datos tanto como el encabezado como el detalle*/
			FormaPagoEncabezado encabezado = new FormaPagoEncabezado();
			encabezado.setTipoFormaPago(tipoFormaPago);
			encabezado.setDescripcion(cadena.toString());
			encabezado.setTipo(tipoSolicitud);
			encabezado.setEstado(EstadoEntityType.ACTIVO);
			entityManager.persist(encabezado);

			for(FormaPago fp: listaFormaPagosSeleccionados){
				FormaPagoDetalle detalle = new FormaPagoDetalle();
				detalle.setEncabezado(encabezado);
				detalle.setFormaPago(fp);
				entityManager.persist(detalle);
			}
			entityManager.flush();
			
			setMensaje("Los tipo de usuarios fueron Asignado exitosamente...");
			
			/*se vuelve a sacar los datos...*/
			if(listaFormaPagosSeleccionados != null){
				this.listaFormaPagosSeleccionados.clear();
			}
			consultarFormaPago();
			if(this.tipoFormaPago != null && tipoSolicitud != null){
				sacarListaFormaDePagoencabezado(tipoFormaPago.getIdTipoFormaPago(), tipoSolicitud.getCodTipoSolicitud());
			}
			
			
		}catch (Exception e) {
			log.error("Error, al ingresar los datos de forma de pago, verificar...", e.getMessage());
			setMensaje("Error, al ingresar los datos de forma de pago, verificar...");
		}
	}

	public void obtenerTipoFormaPago(){
		if(tipoFormaPago != null ){
			log.error("tipo de Forma de pago : #0", tipoFormaPago.getDescripcion());

		}
	}	
	
	public void obtenerTipoSolicitud(){
		if(this.tipoSolicitud != null){
			log.debug("tipo de solicitud  #0", tipoSolicitud.getDesTipoSolicitud());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sacarListaFormaDePagoencabezado(Long id, String idTipoSol){
		try{
			if(id != null){
				listaFormaPagosEncabezado = ( List<FormaPagoEncabezado>)entityManager
						.createQuery("select u from FormaPagoEncabezado u " +
								" where u.tipoFormaPago.idTipoFormaPago=:idtipo " +
								" and u.tipo.codTipoSolicitud=:codigoTipo " +
								" and u.estado=:estadoAux")
						.setParameter("idtipo", id)
						.setParameter("codigoTipo", idTipoSol)
						.setParameter("estadoAux", EstadoEntityType.ACTIVO)
						.getResultList();
			}
		}catch (Exception e) {
			log.error("Error, al sacar la lista de usuarios asigandos. #0", e.getMessage());
		}
	}
	
	

	
	@SuppressWarnings("unchecked")
	public void limpiar(){
		if(listaTipoFormaPago != null){
			listaTipoFormaPago.clear();
		}
			try{
	    		listaTipoFormaPago = ( List<TipoFormaPago>)entityManager
					.createQuery("select u from TipoFormaPago u")
					.getResultList();
			}catch (Exception e) {
				log.error("Error, al sacar la lista de tipo de forma de pago, #0", e.getMessage());
			}			
			
		
		if(listaFormaPagosSeleccionados != null){
			listaFormaPagosSeleccionados.clear();
		}
		if(listaFormaPagos != null){
			listaFormaPagos.clear();
		}	
		try{
			listaFormaPagos = ( List<FormaPago>)entityManager
				.createQuery("select u from FormaPago u")
				.getResultList();
		}catch (Exception e) {
			log.error("Error, al sacar la lista de tipo de forma de pago, #0", e.getMessage());
		}	
		
		
		if(tipoFormaPago != null){
			setTipoFormaPago(null);
		}
		if(this.tipoFormaPago != null && this.tipoSolicitud != null){
			sacarListaFormaDePagoencabezado(tipoFormaPago.getIdTipoFormaPago(), tipoSolicitud.getCodTipoSolicitud());
		}
	}
	
	public void limpiarTotal(){
		limpiar();
		setMensaje(null);
		setTipoSolicitud(null);
		if(listaFormaPagosEncabezado != null){
			listaFormaPagosEncabezado.clear();
		}
		
	}
	


	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	public List<TipoFormaPago> getListaTipoFormaPago() {
		return listaTipoFormaPago;
	}

	public void setListaTipoFormaPago(List<TipoFormaPago> listaTipoFormaPago) {
		this.listaTipoFormaPago = listaTipoFormaPago;
	}

	public TipoFormaPago getTipoFormaPago() {
		return tipoFormaPago;
	}

	public void setTipoFormaPago(TipoFormaPago tipoFormaPago) {
		this.tipoFormaPago = tipoFormaPago;
	}

	public List<FormaPago> getListaFormaPagos() {
		return listaFormaPagos;
	}

	public void setListaFormaPagos(List<FormaPago> listaFormaPagos) {
		this.listaFormaPagos = listaFormaPagos;
	}

	public List<FormaPago> getListaFormaPagosSeleccionados() {
		return listaFormaPagosSeleccionados;
	}

	public void setListaFormaPagosSeleccionados(
			List<FormaPago> listaFormaPagosSeleccionados) {
		this.listaFormaPagosSeleccionados = listaFormaPagosSeleccionados;
	}

	public List<FormaPagoEncabezado> getListaFormaPagosEncabezado() {
		return listaFormaPagosEncabezado;
	}

	public void setListaFormaPagosEncabezado(
			List<FormaPagoEncabezado> listaFormaPagosEncabezado) {
		this.listaFormaPagosEncabezado = listaFormaPagosEncabezado;
	}

	public List<LcredTipoSolicitud> getListaTipoSolictudes() {
		return listaTipoSolictudes;
	}

	public void setListaTipoSolictudes(List<LcredTipoSolicitud> listaTipoSolictudes) {
		this.listaTipoSolictudes = listaTipoSolictudes;
	}

	public LcredTipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(LcredTipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	
	
	

}
