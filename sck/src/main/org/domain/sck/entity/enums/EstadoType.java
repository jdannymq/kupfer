package org.domain.sck.entity.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EstadoType {
	SI, NO;
	
	
	private static final List<EstadoType> estados = new ArrayList<EstadoType>();
	
	static {
		estados.addAll(Arrays.asList(EstadoType.values()));
	}

	public static List<EstadoType> getTipos() {
		return estados;
	}
}