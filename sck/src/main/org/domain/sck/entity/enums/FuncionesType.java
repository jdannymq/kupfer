package org.domain.sck.entity.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum FuncionesType {
	GENERARSOLICITUD("Generar Solicitudes"),APRUEBARECHAZO("Aprueba/Rechazo"),AGREGARCOMENTARIO("Agrega Comentarios"), 
	ANALISTANEGOCIO("Analista de Negocio"),ANALISTAFINANCIERO("Analista Financiero"),SOLOVISUALIZAR("Solo Visualizar");
	
	private String nombre; 
	
	private FuncionesType(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}
	
	private static final List<FuncionesType> funciones = new ArrayList<FuncionesType>();
	
	static {
		funciones.addAll(Arrays.asList(FuncionesType.values()));
	}

	public static List<FuncionesType> getAcciones() {
		return funciones;
	}
}
