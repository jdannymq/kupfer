package org.domain.sck.seam.menu;

import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("panelMenuState")
@Scope(ScopeType.SESSION)
public class PanelMenuState {

	private Map<String, Boolean> menu;

	private String selectedMenuItem;

	
	public String getSelectedMenuItem() {
		return selectedMenuItem;
	}
	
	public void setSelectedMenuItem(String selectedMenuItem) {
		this.selectedMenuItem = selectedMenuItem;
	}
	
	public Map<String, Boolean> getMenu() {
		return menu;
	}
	public void setMenu(Map<String, Boolean> menu) {
		this.menu = menu;
	}

	


	@Create
	public void init() {
		menu = new HashMap<String, Boolean>();

		menu.put("grGestNom", true);
		menu.put("grGestNom_grNominaRetiro", false);
		
		menu.put("grGestDocumentos", false);
		menu.put("grGestDocumentosCustodia_grDoctoCustodia", false);
		menu.put("grGestDoctoTrazabilidad_grDoctoTrazabilidad", false);

		menu.put("grModFinanciero", false);
		menu.put("grModFinanciero_grBancos", false);
		menu.put("grModFinanciero_grSucursales", false);		
		
		menu.put("grGestClientes", false);
		menu.put("grGestClientes_grClientes", false);
		menu.put("grGestClientes_grClienteBanco", false);
		
		menu.put("grMantendores", false);

		menu.put("grAdmin", false);

	}
}