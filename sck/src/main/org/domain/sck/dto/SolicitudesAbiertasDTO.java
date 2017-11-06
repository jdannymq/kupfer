package org.domain.sck.dto;

import org.domain.sck.entity.LcredSolicitud;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcastro on 30-04-14.
 */
public class SolicitudesAbiertasDTO {


    private List<PlanillaDTO> solicitudList;
    private String estado;
    private String descEstado;
    private Double porcentaje = 0d;

    public SolicitudesAbiertasDTO(String estado) {
        this.estado = estado;
        solicitudList = new ArrayList<PlanillaDTO>();
    }

    public List<PlanillaDTO> getSolicitudList() {
        return solicitudList;
    }

    public void setSolicitudList(List<PlanillaDTO> solicitudList) {
        this.solicitudList = solicitudList;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescEstado() {
		return descEstado;
	}
    
    public void setDescEstado(String descEstado) {
		this.descEstado = descEstado;
	}
    
    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getPorcentajeEstado() {
        Double resultado = porcentaje;
        return Math.round((resultado))  + "%";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SolicitudesAbiertasDTO that = (SolicitudesAbiertasDTO) o;

        if (estado != null ? !estado.equals(that.estado) : that.estado != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return estado != null ? estado.hashCode() : 0;
    }
}