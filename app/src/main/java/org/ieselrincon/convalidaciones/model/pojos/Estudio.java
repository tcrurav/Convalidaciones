package org.ieselrincon.convalidaciones.model.pojos;

import org.ieselrincon.convalidaciones.constantes.G;

/**
 * Created by Tiburcio on 31/07/2016.
 */
public class Estudio {
    private int ID;
    private String nombre;
    private String codigo;

    public Estudio() {
        this.ID = G.SIN_VALOR_INT;
        this.nombre = G.SIN_VALOR_STRING;
        this.codigo = G.SIN_VALOR_STRING;
    }

    public Estudio(int ID, String nombre, String codigo) {
        this.ID = ID;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
