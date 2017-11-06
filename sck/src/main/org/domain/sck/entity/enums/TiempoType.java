package org.domain.sck.entity.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TiempoType {
	
	D("Dias"),H("Hora(s)"),M("Minuto(s)");

	private String nombre; 
	
	private TiempoType(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}
	
	private static final List<TiempoType> funciones = new ArrayList<TiempoType>();
	
	static {
		funciones.addAll(Arrays.asList(TiempoType.values()));
	}

	public static List<TiempoType> getTipoTiempo() {
		return funciones;
	}
}
