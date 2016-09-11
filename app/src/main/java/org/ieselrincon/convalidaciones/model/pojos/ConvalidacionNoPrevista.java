package org.ieselrincon.convalidaciones.model.pojos;

import org.ieselrincon.convalidaciones.constantes.G;

/**
 * Created by Tiburcio on 31/07/2016.
 */
public class ConvalidacionNoPrevista {
    int ID;
    String moduloAConvalidar;
    String estudioAportado;
    Solicitud solicitud;

    public ConvalidacionNoPrevista() {
        this.ID = G.SIN_VALOR_INT;
        this.moduloAConvalidar = G.SIN_VALOR_STRING;
        this.estudioAportado = G.SIN_VALOR_STRING;
        this.solicitud = null;
    }

    public ConvalidacionNoPrevista(int ID, String moduloAConvalidar, String estudioAportado, Solicitud solicitud) {
        this.ID = ID;
        this.moduloAConvalidar = moduloAConvalidar;
        this.estudioAportado = estudioAportado;
        this.solicitud = solicitud;
    }

    public String getEstudioAportado() {
        return estudioAportado;
    }

    public void setEstudioAportado(String estudioAportado) {
        this.estudioAportado = estudioAportado;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getModuloAConvalidar() {
        return moduloAConvalidar;
    }

    public void setModuloAConvalidar(String moduloAConvalidar) {
        this.moduloAConvalidar = moduloAConvalidar;
    }

}
