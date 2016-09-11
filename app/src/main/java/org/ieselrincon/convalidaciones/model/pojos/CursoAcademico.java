package org.ieselrincon.convalidaciones.model.pojos;

/**
 * Created by Tiburcio on 16/08/2016.
 */
public enum CursoAcademico {
    CURSO_2016_2017("2016/2017");

    String cursoAcademico;

    CursoAcademico(String cursoAcademico) {
        this.cursoAcademico = cursoAcademico;
    }

    public String getCursoAcademico() {
        return cursoAcademico;
    }

    public void setCursoAcademico(String cursoAcademico) {
        this.cursoAcademico = cursoAcademico;
    }
}
