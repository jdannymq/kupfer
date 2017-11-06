package org.domain.sck.session.home;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;

@Name("emailAlertaFinanzaAperturaMensajeHelper")
@Scope(ScopeType.SESSION)
@AutoCreate
public class EmailAlertaFinanzaAperturaMensajeHelper {

	@Logger Log log;
	
	@In(create = true)
	private Renderer renderer;

	
	private ContenidoEmailSolicitudDTO mensajeGeneradorFinanza;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeGeneradorFinanza() { return mensajeGeneradorFinanza; }

	public boolean enviarRespuestaDeLaSolicitudFinanza(ContenidoEmailSolicitudDTO mensajeGeneradorFinanza) {
		try {
			this.mensajeGeneradorFinanza = mensajeGeneradorFinanza;
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorFinanza.xhtml");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarRespuestaDeLaSolicitudFinanza(). Causa: #0 " + e.getMessage());
			return false;
		}
	}
	
	private ContenidoEmailSolicitudDTO mensajeGeneradorApertura;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeGeneradorApertura() { return mensajeGeneradorApertura; }

	public boolean enviarRespuestaDeLaSolicitudApertura(ContenidoEmailSolicitudDTO mensajeGeneradorApertura) {
		try {
			this.mensajeGeneradorApertura = mensajeGeneradorApertura;
			renderer.render("/layout/contacto/EmailAlertaRespuestaGeneradorSolicitudApertura.xhtml");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarRespuestaDeLaSolicitudApertura(). Causa: #0 " + e.getMessage());
			return false;
		}
	}	
	
	private ContenidoEmailSolicitudDTO mensajeGeneradorAdjudicar;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeGeneradorAdjudicar() { return mensajeGeneradorAdjudicar; }

	public boolean enviarRespuestaDeLaSolicitudAdjudicar(ContenidoEmailSolicitudDTO mensajeGeneradorAdjudicar) {
		try {
			this.mensajeGeneradorAdjudicar = mensajeGeneradorAdjudicar;
			renderer.render("/layout/contacto/EmailAlertaRespuestaGeneradorSolicitudAdjudicar.xhtml");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarRespuestaDeLaSolicitudAdjudicar(). Causa: #0 " + e.getMessage());
			return false;
		}
	}		
}
