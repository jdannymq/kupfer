package org.domain.sck.session.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.sck.dto.UsuarioCorreoDTO;
import org.domain.sck.entity.LogsCorreos;
import org.domain.sck.entity.Usuariosegur;
import org.domain.sck.session.service.scoring.ScoringService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;

@Name("emailAlertaContactoMensajeHelper")
@Scope(ScopeType.SESSION)
@AutoCreate
public class EmailAlertaContactoMensajeHelper implements Serializable{

	@Logger Log log;
	
	@In(create = true)
	private Renderer renderer;
	
	@In(value = "#{entityManager}")
    EntityManager entityManager;
	
	@In ScoringService scoringService;

	
	private ContenidoEmailSolicitudDTO mensajeGenerador;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeGenerador() { return mensajeGenerador; }
	@Asynchronous
	public void enviarSolicitud(ContenidoEmailSolicitudDTO contenidoEmail) {
		List<UsuarioCorreoDTO> listaCorreo = new ArrayList<UsuarioCorreoDTO>(0);
		
		//Modificación RHurtado Genera Log Email
		LogsCorreos logEmail = new LogsCorreos();
		
		logEmail.setSolicitud(contenidoEmail.getSolicitud().getId().getNumSolicitud().toString());
		logEmail.setAsunto("Creacion Solicitud");
		logEmail.setAccion("getMensajeGenerador()");
		String wvarDestinatario = "";
		//Modificación RHurtado Genera Log Email
		
		
		try {
			this.mensajeGenerador = contenidoEmail;
			log.debug("cantidad #0", contenidoEmail.getListaNuevosCorreos().size());
			for(Usuariosegur u : contenidoEmail.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}	
			//this.mensajeGenerador.setListaCorreos(listaCorreo);

			renderer.render("/layout/contacto/EmailAlertaSolicitudGenerador.xhtml");
			//renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorPrueba.xhtml");
			

			//Modificación RHurtado Genera Log Email
			logEmail.setDestinatarios(wvarDestinatario);
			logEmail.setEstado("OK");
			logEmail.setContenido("El contenido fue enviado por xhtml.");
			
			boolean exito = scoringService.persistLogEmail(logEmail);
			log.debug("se guardo #0", exito);
			//Modificación RHurtado Genera Log Email
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarSolicitud(). Causa: #0 " + e.getMessage());

			//Modificación RHurtado Genera Log Email
			try
			{
//				String wvarSQL;
				logEmail.setDestinatarios(wvarDestinatario);
				logEmail.setEstado("ER");
				logEmail.setContenido("El contenido no fue enviado por xhtml. porque no salio del renderenr");
				boolean exito = scoringService.persistLogEmail(logEmail);
				log.debug("se guardo #0", exito);
				//Modificación RHurtado Genera Log Email
				
			}catch (Exception er){
				er.printStackTrace();
				log.error("Error envio Escribe log de email(). Causa: #0 " + er.getMessage());
				
			}
		
		}

	}
	
	
	public boolean enviarSolicitudPrueba(ContenidoEmailSolicitudDTO contenidoEmail) {
		boolean existo = false;
		try {
			log.debug("antes de mandar el correo");
			this.mensajeGenerador = contenidoEmail;
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorPrueba.xhtml");
			log.debug("despues de mandar el correo");
			existo = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarSolicitudPrueba(). Causa: #0 " + e.getMessage());
		}
		return existo;
	}
	
	
	private ContenidoEmailSolicitudDTO mensajeLineaCredito;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeLineaCredito() { return mensajeLineaCredito;}
	@Asynchronous
	public void enviarSolicitudLineaCredito(ContenidoEmailSolicitudDTO mensajeLineaCredito) {
		try {
			log.debug("cantidad #0", mensajeLineaCredito.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeLineaCredito.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
				}
			}
			this.mensajeLineaCredito = mensajeLineaCredito;
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorLineaCredito.xhtml");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarSolicitudLineaCredito(). Causa: #0 " + e.getMessage());
		}
	}
	
	private ContenidoEmailSolicitudDTO mensajeCondiciones;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeCondiciones() { return mensajeCondiciones;}
	@Asynchronous
	public void enviarSolicitudCondiciones(ContenidoEmailSolicitudDTO mensajeCondiciones) {
		try {
			log.debug("cantidad #0", mensajeCondiciones.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeCondiciones.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}
			this.mensajeCondiciones = mensajeCondiciones;		
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorCondiciones.xhtml");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarSolicitudCondiciones(). Causa: #0 " + e.getMessage());
		}
	}

	private ContenidoEmailSolicitudDTO mensajeBloqueoDesbloqueo;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeBloqueoDesbloqueo() { return mensajeBloqueoDesbloqueo;}
	@Asynchronous
	public void enviarSolicitudBloqueoDesbloqueo(ContenidoEmailSolicitudDTO mensajeBloqueoDesbloqueo) {
		try {
			log.debug("cantidad #0", mensajeBloqueoDesbloqueo.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeBloqueoDesbloqueo.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}
			this.mensajeBloqueoDesbloqueo = mensajeBloqueoDesbloqueo;	
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorBloqueoDesbloqueo.xhtml");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarSolicitudBloqueoDesbloqueo(). Causa: #0 " + e.getMessage());
		}
	}
	
	private ContenidoEmailSolicitudDTO mensajeProrroga;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeProrroga() { return mensajeProrroga;}
	@Asynchronous
	public void enviarSolicitudProrroga(ContenidoEmailSolicitudDTO mensajeProrroga) {
		try {
			log.debug("cantidad #0", mensajeProrroga.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeProrroga.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}
			
			this.mensajeProrroga = mensajeProrroga;
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorProrroga.xhtml");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarSolicitudProrroga(). Causa: #0 " + e.getCause());
		}		
	}
		
	
	
	private ContenidoEmailSolicitudDTO mensajeDm;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeDm() { return mensajeDm;}
	@Asynchronous
	public void enviarSolicitudDm(ContenidoEmailSolicitudDTO mensajeDm) {
		try {
			log.debug("cantidad #0", mensajeDm.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeDm.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}
			this.mensajeDm = mensajeDm;
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorCreacionDM.xhtml");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error envio Email desde enviarSolicitudDm(). Causa: #0 " + e.getMessage());
		}
	}
			
	
//	private ContenidoEmailSolicitudDTO respuestaMensajeGenerador;
//	@BypassInterceptors public ContenidoEmailSolicitudDTO getRespuestaMensajeGenerador() { return respuestaMensajeGenerador; }
//
//	public boolean  enviarRespuestaDeLaSolicitud(ContenidoEmailSolicitudDTO respuestaMensajeGenerador) {
//		try {
//			this.respuestaMensajeGenerador = respuestaMensajeGenerador;
//			renderer.render("/layout/contacto/EmailAlertaRespuestaGeneradorSolicitud.xhtml");
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("Error envio Email desde enviarRespuestaDeLaSolicitud(). Causa: #0 " + e.getMessage());
//			return false;
//		}
//	}
	
	
	/*#########################################################################  meodos de resepuestas #########################################################*/
	
	
	
	private ContenidoEmailSolicitudDTO mensajeRespuesta;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeRespuesta() { return mensajeRespuesta;	}
	@Asynchronous
	public void enviarRespuestaSolicitudVenta(ContenidoEmailSolicitudDTO mensajeRespuesta) {
		try {
			log.debug("cantidad #0", mensajeRespuesta.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeRespuesta.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}
			this.mensajeRespuesta = mensajeRespuesta;
			renderer.render("/layout/contacto/EmailAlertaSolicitudRespuestaVta.xhtml");
		} catch (Exception e) {
			log.error("Error envio Email desde enviarRespuestaSolicitudVenta(). Causa: #0 " + e.getMessage());
			e.printStackTrace();
		}
	}

	
	private ContenidoEmailSolicitudDTO mensajeRespuestaLinea;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeRespuestaLinea() { return mensajeRespuestaLinea;}
	@Asynchronous
	public void enviarRespuestaSolicitudLinea(ContenidoEmailSolicitudDTO mensajeRespuestaLinea) {
		try {
			log.debug("cantidad #0", mensajeRespuestaLinea.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeRespuestaLinea.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}
			this.mensajeRespuestaLinea = mensajeRespuestaLinea;
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorLineaCreditoRespuesta.xhtml");
			
		} catch (Exception e) {
			log.error("Error envio Email desde enviarRespuestaSolicitudLinea(). Causa: #0 " + e.getMessage());
			e.printStackTrace();
		}
	}	
	

	private ContenidoEmailSolicitudDTO mensajeRespuestaCondiciones;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeRespuestaCondiciones() { return mensajeRespuestaCondiciones;}
	@Asynchronous
	public void enviarRespuestaSolicitudCondiciones(ContenidoEmailSolicitudDTO mensajeRespuestaCondiciones) {
		try {
			log.debug("cantidad #0", mensajeRespuestaCondiciones.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeRespuestaCondiciones.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}
			this.mensajeRespuestaCondiciones = mensajeRespuestaCondiciones;
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorCondicionesRespuesta.xhtml");
			
		} catch (Exception e) {
			log.error("Error envio Email desde enviarRespuestaSolicitudCondiciones(). Causa: #0 " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	private ContenidoEmailSolicitudDTO mensajeRespuestaBloqueoDesbloqueo;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeRespuestaBloqueoDesbloqueo() { return mensajeRespuestaBloqueoDesbloqueo;}
	@Asynchronous
	public void enviarRespuestaSolicitudBloqueoDesbloqueo(ContenidoEmailSolicitudDTO mensajeRespuestaBloqueoDesbloqueo) {
		try {
			log.debug("cantidad #0", mensajeRespuestaBloqueoDesbloqueo.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeRespuestaBloqueoDesbloqueo.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}
			this.mensajeRespuestaBloqueoDesbloqueo = mensajeRespuestaBloqueoDesbloqueo;
			renderer.render("/layout/contacto/EmailAlertaSolicitudRespuestaBloqueoDesbloqueo.xhtml");
			                                  
			                                  
		} catch (Exception e) {
			log.error("Error envio Email desde enviarRespuestaSolicitudBloqueoDesbloqueo(). Causa: #0 " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	private ContenidoEmailSolicitudDTO mensajeRespuestaDm;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeRespuestaDm() { return mensajeRespuestaDm;}
	@Asynchronous
	public void enviarSolicitudRespuestaDm(ContenidoEmailSolicitudDTO mensajeRespuestaDm) {
		try {
			log.debug("cantidad #0", mensajeRespuestaDm.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeRespuestaDm.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}
			this.mensajeRespuestaDm = mensajeRespuestaDm;
			renderer.render("/layout/contacto/EmailAlertaSolicitudGeneradorRespuestaCreacionDM.xhtml");
			
		} catch (Exception e) {
			log.error("Error envio Email desde enviarSolicitudDm(). Causa: #0 " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	private ContenidoEmailSolicitudDTO mensajeRespuestaProrroga;
	@BypassInterceptors public ContenidoEmailSolicitudDTO getMensajeRespuestaProrroga() { return mensajeRespuestaProrroga;}
	@Asynchronous
	public void enviarSolicitudRespuestaProrroga(ContenidoEmailSolicitudDTO mensajeRespuestaProrroga) {
		try {
			log.debug("cantidad #0", mensajeRespuestaProrroga.getListaNuevosCorreos().size());
			for(Usuariosegur u : mensajeRespuestaProrroga.getListaNuevosCorreos()){
				if(!u.getCorreo().equals("fherrera@kupfer.cl")){
					log.debug("username #0 mail #1", u.getNombre(), u.getCorreo());
					//listaCorreo.add(u);
					//Modificación RHurtado Genera Log Email				
					//wvarDestinatario += u.getCorreo() + ";";
					//Modificación RHurtado Genera Log Email
				}
			}	
			this.mensajeRespuestaProrroga = mensajeRespuestaProrroga;
			renderer.render("/layout/contacto/EmailAlertaSolicitudRespuestaGeneradorProrroga.xhtml");
			
		} catch (Exception e) {
			log.error("Error envio Email desde enviarSolicitudRespuestaProrroga(). Causa: #0 " + e.getMessage());
			e.printStackTrace();
		}
	}	
	
	
	
}
