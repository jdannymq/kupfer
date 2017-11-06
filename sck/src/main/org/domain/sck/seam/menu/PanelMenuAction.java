package org.domain.sck.seam.menu;

import javax.faces.event.ActionEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("panelMenuAction")
@Scope(ScopeType.EVENT)
public class PanelMenuAction {

	@In(value = "panelMenuState")
	private PanelMenuState menuState;

	public void select(ActionEvent event) {
		menuState.setSelectedMenuItem(event.getComponent().getId());
	}
}