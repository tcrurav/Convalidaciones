package org.ieselrincon.convalidaciones.model.pojos;

import org.ieselrincon.convalidaciones.constantes.G;

/**
 * Created by Tiburcio on 31/07/2016.
 */
public class Usuario {
    private int ID;
    private String nombre;
    private String apellidos;
    private String email;
    private String contrasena;
    private String dni;
    private String telefono;
    private TipoUsuario tipoUsuario;

    public Usuario() {
        this.ID = G.SIN_VALOR_INT;
        this.nombre = G.SIN_VALOR_STRING;
        this.apellidos = G.SIN_VALOR_STRING;
        this.email = G.SIN_VALOR_STRING;
        this.contrasena = G.SIN_VALOR_STRING;
        this.dni = G.SIN_VALOR_STRING;
        this.telefono = G.SIN_VALOR_STRING;
        this.tipoUsuario = null;
    }

    public Usuario(int ID, String nombre, String apellidos, String email, String contrasena, String dni, String telefono, TipoUsuario tipoUsuario) {
        this.ID = ID;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.contrasena = contrasena;
        this.dni = dni;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
