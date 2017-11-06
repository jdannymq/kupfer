package org.domain.sck.session.helper;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.jboss.logging.Logger;




public class SendMail {
	
//    @Resource(mappedName="java:/CorreoDirectorio")
//    private Session mailSession;
//    
//    private Logger LOG = Logger.getLogger(SendMail.class);
//
//    public SendMail() {
//
//    }
//
//    public boolean EnviarCorreo(String para, String de, String Asunto, String cuerpo) {
//
//        try {
//            MimeMessage m = new MimeMessage(mailSession);
//            Address from = new InternetAddress(de);
//            Address[] to = new InternetAddress[] { new InternetAddress(para) };
//            //Address[] bcc = new InternetAddress[] { new InternetAddress("fparraguez@kupfer.cl") };
//            m.setFrom(from);
//            m.setRecipients(Message.RecipientType.TO, to);
//            //m.setRecipients(Message.RecipientType.BCC, bcc);
//            m.setSubject(Asunto);
//            m.setSentDate(new java.util.Date());
//            m.setContent(cuerpo, "text/html; charset=utf-8");
//            Transport.send(m);
//            LOG.info("Correo enviado correctamente");
//        } catch (javax.mail.MessagingException e) {
//            e.printStackTrace();
//            LOG.error("Error al enviar el correo: " + e);
//            return false;
//        }
//        return true;
//    }

}
