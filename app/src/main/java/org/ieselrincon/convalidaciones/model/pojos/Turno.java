package org.ieselrincon.convalidaciones.model.pojos;

/**
 * Created by Tiburcio on 11/08/2016.
 */
public enum Turno {
    MAÑANA("Mañana"), TARDE("Tarde"), NOCHE("Noche");

    String turno;

    Turno(String turno) {
        this.turno = turno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
