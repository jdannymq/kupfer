package org.domain.sck.entity.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TipoCuentasKupferType {
	KX("Kupfer Express"), GC("Grandes Cuentas"), MX("Mixto");
	
	private String nombre;

	private TipoCuentasKupferType(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}
	
	
	private static final List<TipoCuentasKupferType> tipos = new ArrayList<TipoCuentasKupferType>();
	
	static {
		tipos.addAll(Arrays.asList(TipoCuentasKupferType.values()));
	}

	public static List<TipoCuentasKupferType> getTipos() {
		return tipos;
	}
}
