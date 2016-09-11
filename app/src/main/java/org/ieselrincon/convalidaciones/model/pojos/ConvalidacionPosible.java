package org.ieselrincon.convalidaciones.model.pojos;

import org.ieselrincon.convalidaciones.constantes.G;

/**
 * Created by Tiburcio on 31/07/2016.
 */
public class ConvalidacionPosible {
    private int ID;
    private Estudio estudioConvalidado;
    private Estudio estudioAportado;
    private Curso curso;

    public ConvalidacionPosible() {
        this.ID = G.SIN_VALOR_INT;
        this.estudioConvalidado = null;
        this.estudioAportado = null;
        this.curso = null;
    }

    public ConvalidacionPosible(int ID, Estudio estudioConvalidado, Estudio estudioAportado, Curso curso) {
        this.ID = ID;
        this.estudioConvalidado = estudioConvalidado;
        this.estudioAportado = estudioAportado;
        this.curso = curso;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Estudio getEstudioConvalidado() {
        return estudioConvalidado;
    }

    public void setEstudioConvalidado(Estudio estudioConvalidado) {
        this.estudioConvalidado = estudioConvalidado;
    }

    public Estudio getEstudioAportado() {
        return estudioAportado;
    }

    public void setEstudioAportado(Estudio estudioAportado) {
        this.estudioAportado = estudioAportado;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
