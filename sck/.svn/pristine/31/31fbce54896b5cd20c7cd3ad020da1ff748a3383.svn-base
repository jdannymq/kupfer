package org.domain.sck.entity.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TipoSolicitudCodigoType {

	VENTA_NORMAL("11","1N","Venta normal"),
    VENTA_CUOTAS("12","1C","Venta en cuotas"),
    INNOMINADA("21","2I","Linea Innominada"),
    NOMINADA("22","2N", "Linea Nominada"),
    RIESGO_KUPFER("23","2R", "Riesgo Küpfer"),
    RIESGO_PAGO("31","3RP,3P,3R", "Riesgo y pago"),
    BLOQUEO_DESBLOQUEO("41","4B,4D","Bloqueo y desbloqueo"),
    CREACION_DM("43", "4DM","Creación de DM"),
    PRORROGA_CHEQUE("44","4PR", "Prorroga de cheque");

	private String codigoTipo;
    private String  codigosSolicitud;
    private String descripcion;

	private TipoSolicitudCodigoType(String codigoTipo, String codigosSolicitud, String descripcion) {
		this.codigoTipo = codigoTipo;
        this.codigosSolicitud = codigosSolicitud;
        this.descripcion = descripcion;
    }


    public String getDescripcion() {
        return descripcion;
    }

    private static final List<TipoSolicitudCodigoType> tipos = new ArrayList<TipoSolicitudCodigoType>();
	
	static {
		tipos.addAll(Arrays.asList(TipoSolicitudCodigoType.values()));
	}

	public static List<TipoSolicitudCodigoType> getTipos() {
		return tipos;
	}

	public static String getCodigoTipoByCodigoSolicitud(String codigo) {
		String codigoTipo = null;
		for(TipoSolicitudCodigoType tipo : tipos ) {
			List<String> codigos = Arrays.asList(tipo.codigosSolicitud.toUpperCase().split(","));
			if(codigos.contains(codigo.toUpperCase())){
				codigoTipo = tipo.codigoTipo;
				break;
			}
		}
		return codigoTipo;
	}

    public static List<String> getCodigosSolicitudByCodigoTipo(String codigo) {

        List<String> codigos = new ArrayList<String>();
        for(TipoSolicitudCodigoType tipo : tipos ) {
            String codigoTipo = tipo.codigoTipo;
            if(codigoTipo.toUpperCase().equals(codigo.toUpperCase())) {
                codigos = Arrays.asList(tipo.codigosSolicitud.toUpperCase().split(","));
                break;
            }

        }
        return codigos;
    }

    public static TipoSolicitudCodigoType getTipoSolicitudByCodigo(String codigo) {
        TipoSolicitudCodigoType tipoSolicitudCodigoType = null;
        for(TipoSolicitudCodigoType tipo : tipos ) {
            String codigoTipo = tipo.codigoTipo;
            if(codigoTipo.toUpperCase().equals(codigo.toUpperCase())) {
                tipoSolicitudCodigoType = tipo;
                break;
            }

        }
        return tipoSolicitudCodigoType;
    }

    public static TipoSolicitudCodigoType getTipoSolicitudByCodigoTipo(String codigo) {
        TipoSolicitudCodigoType tipoSolicitudCodigoType = null;
        for(TipoSolicitudCodigoType tipo : tipos ) {
            List<String> codigos = Arrays.asList(tipo.codigosSolicitud.toUpperCase().split(","));
            if(codigos.contains(codigo.toUpperCase())){
                tipoSolicitudCodigoType = tipo;
                break;
            }
        }
        return tipoSolicitudCodigoType;
    }
}
