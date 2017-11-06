package org.domain.sck.seam;

import java.util.Locale;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;


@Name("globalParameters")
@Scope(ScopeType.APPLICATION)
@Startup
@BypassInterceptors
public class GlobalParameters {
	
	@Logger
	Log log;
	
	private String attachedFilesAbsolutePath;
	private String attachedFilesWebPath;
	private String attachedFilesWebPathPrueba;
	private String nameSap;
	private String ipSap;
	private String clientSap;
	private String systemNumberSap;
	private String userSap;
	private String passwordSap;
	
	@Create
	public void init() {
		if (this.attachedFilesAbsolutePath == null || this.attachedFilesWebPath == null || this.attachedFilesWebPathPrueba == null )
			log.error("No se han iniciado todos los parametros");
		
		if (this.nameSap == null || this.nameSap.trim().isEmpty())
			log.error("No se ha iniciado parámetro global nameSap");
		if (this.ipSap == null || this.ipSap.trim().isEmpty())
			log.error("No se ha iniciado parámetro global ipSap");
		if (this.systemNumberSap == null || this.systemNumberSap.trim().isEmpty())
			log.error("No se ha iniciado parámetro global systemNumberSap");
		if (this.clientSap == null || this.clientSap.trim().isEmpty())
			log.error("No se ha iniciado parámetro global clientSap");
		if (this.userSap == null || this.userSap.trim().isEmpty())
			log.error("No se ha iniciado parámetro global userSap");
		if (this.passwordSap == null || this.passwordSap.trim().isEmpty())
			log.error("No se ha iniciado parámetro global passwordSap");
		
	}
	
	public String getAttachedFilesAbsolutePath() {
		return attachedFilesAbsolutePath;
	}
	
	public String getAttachedFilesWebPath() {
		return attachedFilesWebPath;
	}
	
	@Factory(value = "globalLocale", autoCreate = true)
	public Locale getLocale() {
		return new Locale("es","CL");	
	}

	public String getNameSap() {
		return nameSap;
	}

	public void setNameSap(String nameSap) {
		this.nameSap = nameSap;
	}

	public String getIpSap() {
		return ipSap;
	}

	public void setIpSap(String ipSap) {
		this.ipSap = ipSap;
	}

	public String getClientSap() {
		return clientSap;
	}

	public void setClientSap(String clientSap) {
		this.clientSap = clientSap;
	}

	public String getSystemNumberSap() {
		return systemNumberSap;
	}

	public void setSystemNumberSap(String systemNumberSap) {
		this.systemNumberSap = systemNumberSap;
	}

	public String getUserSap() {
		return userSap;
	}

	public void setUserSap(String userSap) {
		this.userSap = userSap;
	}

	public String getPasswordSap() {
		return passwordSap;
	}

	public void setPasswordSap(String passwordSap) {
		this.passwordSap = passwordSap;
	}

	public String getAttachedFilesWebPathPrueba() {
		return attachedFilesWebPathPrueba;
	}
	
	
	
	
}
