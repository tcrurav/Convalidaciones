package org.ieselrincon.convalidaciones.model.pojos;

import org.ieselrincon.convalidaciones.constantes.G;

/**
 * Created by Tiburcio on 31/07/2016.
 */
public class Curso {
    private int ID;
    private int anno;
    private Turno turno;
    private Ciclo ciclo;

    public Curso(){
        this.ID = G.SIN_VALOR_INT;
        this.anno = G.SIN_VALOR_INT;
        this.turno = null;
        this.ciclo = null;
    }

    public Curso(int ID, int anno, Turno turno, Ciclo ciclo) {
        this.ID = ID;
        this.anno = anno;
        this.turno = turno;
        this.ciclo = ciclo;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }
}
