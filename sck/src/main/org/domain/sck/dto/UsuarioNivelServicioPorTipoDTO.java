package org.domain.sck.dto;

import org.domain.sck.entity.enums.TipoSolicitudCodigoType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcastro on 30-04-14.
 */
public class UsuarioNivelServicioPorTipoDTO {

    private TipoSolicitudCodigoType tipoSolicitudCodigoType;
    private List<PlanillaDTO> cumplidasList;
    private List<PlanillaDTO> noCumplidasList;
    private List<PlanillaDTO> totalList;


    public UsuarioNivelServicioPorTipoDTO(TipoSolicitudCodigoType tipoSolicitudCodigoType) {
        this.tipoSolicitudCodigoType = tipoSolicitudCodigoType;
        cumplidasList = new ArrayList<PlanillaDTO>();
        noCumplidasList = new ArrayList<PlanillaDTO>();
    }

    public TipoSolicitudCodigoType getTipoSolicitudCodigoType() {
        return tipoSolicitudCodigoType;
    }

    public void setTipoSolicitudCodigoType(TipoSolicitudCodigoType tipoSolicitudCodigoType) {
        this.tipoSolicitudCodigoType = tipoSolicitudCodigoType;
    }

    public void setCumplidasList(List<PlanillaDTO> cumplidasList) {
        this.cumplidasList = cumplidasList;
    }

    public List<PlanillaDTO> getCumplidasList() {
        return cumplidasList;
    }

    public List<PlanillaDTO> getNoCumplidasList() {
        return noCumplidasList;
    }

    public void setNoCumplidasList(List<PlanillaDTO> noCumplidasList) {
        this.noCumplidasList = noCumplidasList;
    }

    public List<PlanillaDTO> getTotalList() {
    	if(totalList == null) {
    		totalList = new ArrayList<PlanillaDTO>();
    		totalList.addAll(this.cumplidasList);
    		totalList.addAll(this.noCumplidasList);
    	}
		return totalList;
	}
    
    public void setTotalList(List<PlanillaDTO> totalList) {
		this.totalList = totalList;
	}

    public Integer getTotal() {
        return cumplidasList.size() + noCumplidasList.size();
    }

    public String getPorcentajeCumplidas() {
        Double resultado;
        try {
            resultado = new Double((double) cumplidasList.size() / (double) getTotal());
        }catch (Exception e) {
            resultado = new Double(0d);
        }
        resultado = resultado * 100;
        return Math.round((resultado))  + "%";
    }

    public String getPorcentajeNoCumplidas() {
        Double resultado;
        try {
            resultado = new Double((double) noCumplidasList.size() / (double) getTotal());
        }catch (Exception e) {
            resultado = new Double(0d);
        }
        resultado = resultado * 100;
        return Math.round((resultado))  + "%";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioNivelServicioPorTipoDTO that = (UsuarioNivelServicioPorTipoDTO) o;

        if (tipoSolicitudCodigoType != that.tipoSolicitudCodigoType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return tipoSolicitudCodigoType.hashCode();
    }
}
