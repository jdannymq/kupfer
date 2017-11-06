package org.domain.sck.entity.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum OrdinalesType {
  CERO("Cero"),PRIMERO("Primero"),SEGUNDO("Segundo"),TERCERO("Tercero"),CUARTO("Cuarto"),QUINTO("Quinto"),SEXTO("Sexto"),SEPTIMO("Septimo"),OCTAVO("(Octavo");
//      |,NOVENO, DECIMO, UNDECIMO, DUODECIMO, DECIMOTERCERO
//	  ,DECIMOCUARTO, DECIMOQUINTO, DECIMOSEXTO, DECIMOSEPTIMO, DECIMOCTAVO, DECIMONOVENO, VIGESIMO, VIGESIMOPRIMERO
//	  ,VIGESIMOSEGUNDO,VIGESIMOTERCERO,VIGESIMOCUARTO,VIGESIMOQUINTO,VIGESIMOSEXTO,VIGESIMOSEPTIMO,VIGESIMOOCTAVO
//	  ,VIGESIMONOVENO,TRIGESIMO;	
	
	private static final List<OrdinalesType> ordinales = new ArrayList<OrdinalesType>();
	private String nombre;

	private OrdinalesType(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}
	
	static {
		ordinales.addAll(Arrays.asList(OrdinalesType.values()));
	}

	public static List<OrdinalesType> getOrdinales() {
		return ordinales;
	}
}
