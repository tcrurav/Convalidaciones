package org.ieselrincon.convalidaciones.model.pojos;

/**
 * Created by Tiburcio on 11/09/2016.
 */
public enum TipoUsuario {
    ADMINISTRADOR("Administrador"), ALUMNO("Alumno");

    String tipoUsuario;

    TipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
