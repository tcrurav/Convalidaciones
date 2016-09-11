package org.ieselrincon.convalidaciones.model.pojos;

import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.proveedor.Contrato;

/**
 * Created by Tiburcio on 31/07/2016.
 */
public class ConvalidacionPosibleEnSolicitud {
    private int ID;
    private ConvalidacionPosible convalidacionPosible;
    private Solicitud solicitud;

    public ConvalidacionPosibleEnSolicitud() {
        this.ID = G.SIN_VALOR_INT;
        this.convalidacionPosible = null;
        this.solicitud = null;
    }

    public ConvalidacionPosibleEnSolicitud(int ID, ConvalidacionPosible convalidacionPosible, Solicitud solicitud) {
        this.ID = ID;
        this.convalidacionPosible = convalidacionPosible;
        this.solicitud = solicitud;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public ConvalidacionPosible getConvalidacionPosible() {
        return convalidacionPosible;
    }

    public void setConvalidacionPosible(ConvalidacionPosible convalidacionPosible) {
        this.convalidacionPosible = convalidacionPosible;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
}
