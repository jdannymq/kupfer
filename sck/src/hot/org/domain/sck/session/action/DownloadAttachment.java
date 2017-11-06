package org.domain.sck.session.action;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.domain.sck.entity.Attachment;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

@Name("downloadAttachment")
public class DownloadAttachment {
	
	@Logger
	private Log log;
	
	@In
	private EntityManager entityManager;
	
	@In(value="#{facesContext.externalContext}")
	private ExternalContext extCtx;
	
	@In(value="#{facesContext}")
	FacesContext facesContext;
	
	@RequestParameter
	private Long attachmentId;
	
	public String download() {
		Attachment attachment = entityManager.find(Attachment.class, attachmentId);
		HttpServletResponse response = (HttpServletResponse)extCtx.getResponse();
		response.setContentType(attachment.getContentType());
                response.addHeader("Content-disposition", "attachment; filename=\"" + attachment.getName() +"\"");
		try {
			ServletOutputStream os = response.getOutputStream();
			os.write(attachment.getData());
			os.flush();
			os.close();
			facesContext.responseComplete();
		} catch(Exception e) {
			log.error("\nFailure : " + e.toString() + "\n");
		}

		return null;
	}
}