package org.domain.sck.entity.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MesesType {
  ENERO("Enero"),
  FEBRERO("Febrero"),
  MARZO("Marzo"),
  ABRIL("Abril"),
  MAYO("Mayo"),
  JUNIO("Junio"),
  JULIO("Julio"),
  AGOSTO("Agosto"),
  SEPTIEMBRE("Septiembre"),
  OCTUBRE("Octubre"),
  NOVIEMBRE("Noviembre"),
  DICIEMBRE("Diciembre");
//      |,NOVENO, DECIMO, UNDECIMO, DUODECIMO, DECIMOTERCERO
//	  ,DECIMOCUARTO, DECIMOQUINTO, DECIMOSEXTO, DECIMOSEPTIMO, DECIMOCTAVO, DECIMONOVENO, VIGESIMO, VIGESIMOPRIMERO
//	  ,VIGESIMOSEGUNDO,VIGESIMOTERCERO,VIGESIMOCUARTO,VIGESIMOQUINTO,VIGESIMOSEXTO,VIGESIMOSEPTIMO,VIGESIMOOCTAVO
//	  ,VIGESIMONOVENO,TRIGESIMO;	
	
	private static final List<MesesType> meses = new ArrayList<MesesType>();
	private String nombre;

	private MesesType(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}
	
	static {
		meses.addAll(Arrays.asList(MesesType.values()));
	}

	public static List<MesesType> getMeses() {
		return meses;
	}
}