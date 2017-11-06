package org.domain.sck.seam;

import java.util.List;

import org.domain.sck.dto.MenuDTO;
import org.domain.sck.dto.ParametroUsuarioDTO;
import org.domain.sck.dto.UsuarioSegurDTO;
import org.domain.sck.entity.LcredUsuarioNivelEnc;
import org.domain.sck.entity.UsuarioCargo;
import org.domain.sck.entity.Usuarios;
import org.domain.sck.entity.Usuariosegur;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("org.jboss.seam.security.identity")
@Scope(ScopeType.SESSION)
@Install(precedence = Install.APPLICATION)
@BypassInterceptors
@Startup
public class CustomIdentity extends Identity {

	
	@Logger Log log;
	private Usuariosegur usuarioLogueado;
	private MenuDTO menu;
	private String usuarioLogin;
	private LcredUsuarioNivelEnc lcredUsuarioNivelEnc;
	private UsuarioSegurDTO usuarioSegur;
	private List<String> listaPerfiles;
	private ParametroUsuarioDTO parametros;
	private UsuarioCargo usuarioCargo;

	
	


	public void limpiaUsuarioInfo() {
		usuarioLogueado = null;
		log.debug("Limpia usuario");
		menu = null;
		log.debug("Limpia menu");
		lcredUsuarioNivelEnc = null;
		log.debug("Limpia lcredUsuarioNivelEnc");
		usuarioSegur = null;
		log.debug("Limpia usuarioSegur");
		listaPerfiles = null;
		log.debug("Limpia lista perfiles");	

		
	}
	
	public void guardarValores(Usuariosegur usuario) {
		usuarioLogueado = usuario;
		usuarioLogin = usuario.getAlias();
	}

	public void guardarValoresParametros(ParametroUsuarioDTO param) {
		parametros = param;
	}
	
	@Override
	public void logout() {
		limpiaUsuarioInfo();
		log.debug("Limpia usuario");
		super.logout();
	}
	
	public Usuariosegur getUsuarioLogueado() {
		return usuarioLogueado;
	}

	public MenuDTO getMenu() {
		return menu;
	}

	public void setMenu(MenuDTO menu) {
		this.menu = menu;
	}

	public String getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(String usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

	public void setUsuarioLogueado(Usuariosegur usuarioLogueado) {
		this.usuarioLogueado = usuarioLogueado;
	}

	public LcredUsuarioNivelEnc getLcredUsuarioNivelEnc() {
		return lcredUsuarioNivelEnc;
	}

	public void setLcredUsuarioNivelEnc(LcredUsuarioNivelEnc lcredUsuarioNivelEnc) {
		this.lcredUsuarioNivelEnc = lcredUsuarioNivelEnc;
	}
	
	public UsuarioSegurDTO getUsuarioSegur() {
		return usuarioSegur;
	}

	public void setUsuarioSegur(UsuarioSegurDTO usuarioSegur) {
		this.usuarioSegur = usuarioSegur;
	}

	public List<String> getListaPerfiles() {
		return listaPerfiles;
	}

	public void setListaPerfiles(List<String> listaPerfiles) {
		this.listaPerfiles = listaPerfiles;
	}

	public ParametroUsuarioDTO getParametros() {
		return parametros;
	}

	public void setParametros(ParametroUsuarioDTO parametros) {
		this.parametros = parametros;
	}

	public UsuarioCargo getUsuarioCargo() {
		return usuarioCargo;
	}

	public void setUsuarioCargo(UsuarioCargo usuarioCargo) {
		this.usuarioCargo = usuarioCargo;
	}

	
}
