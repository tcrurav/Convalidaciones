package org.ieselrincon.convalidaciones.model.pojos;

import org.ieselrincon.convalidaciones.constantes.G;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tiburcio on 31/07/2016.
 */
public class Solicitud {
    int ID;
    int fecha;
    CursoAcademico cursoAcademico;
    Curso curso;
    Usuario usuario;

    public Solicitud() {
        this.ID = G.SIN_VALOR_INT;
        this.fecha = G.SIN_VALOR_INT;
        this.cursoAcademico = null;
        this.curso = null;
        this.usuario = null;
    }

    public Solicitud(int ID, int fecha, CursoAcademico cursoAcademico, Curso curso, Usuario usuario) {
        this.ID = ID;
        this.fecha = fecha;
        this.cursoAcademico = cursoAcademico;
        this.curso = curso;
        this.usuario = usuario;
    }

    public CursoAcademico getCursoAcademico() {
        return cursoAcademico;
    }

    public void setCursoAcademico(CursoAcademico cursoAcademico) {
        this.cursoAcademico = cursoAcademico;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

}
