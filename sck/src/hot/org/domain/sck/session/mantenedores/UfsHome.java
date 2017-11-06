package org.domain.sck.session.mantenedores;

import java.util.Date;

import org.domain.sck.entity.Ufs;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("ufsHome")
public class UfsHome extends EntityHome<Ufs> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setUfsIdUfs(Long id) {
		setId(id);
	}

	public Long getUfsIdUfs() {
		return (Long) getId();
	}

	@Override
	protected Ufs createInstance() {
		Ufs ufs = new Ufs();
		ufs.setFecha(new Date());
		return ufs;
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

	public Ufs getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	@Override
	public String persist() {
		try{
			Ufs ufNew = this.getInstance();
			if(ufNew != null){
				return super.persist();
			}else{
				return null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
	}

}
