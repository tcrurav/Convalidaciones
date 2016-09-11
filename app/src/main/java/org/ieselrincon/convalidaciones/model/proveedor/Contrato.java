package org.ieselrincon.convalidaciones.model.proveedor;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tiburcio on 31/07/2016.
 */
public class Contrato {

    public static final String AUTHORITY = "org.ieselrincon.convalidaciones.model.proveedor.ProveedorDeContenido";

    public static final class Ciclo implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Ciclo");

        // Table column
        public static final String NOMBRE = "Nombre";
        public static final String ABREVIATURA = "Abreviatura";
    }

    public static final class Curso implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Curso");

        // Table column
        public static final String ANNO = "Anno";
        public static final String TURNO = "Turno";
        public static final String FK_CICLO = "FK_Ciclo";
    }

    public static final class Estudio implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Estudio");

        // Table column
        public static final String NOMBRE = "Nombre";
        public static final String CODIGO = "Codigo";
    }

    public static final class ConvalidacionPosible implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/ConvalidacionPosible");

        // Table column
        public static final String FK_ESTUDIOCONVALIDADO = "FK_EstudioConvalidado";
        public static final String FK_ESTUDIOAPORTADO = "FK_EstudioAportado";
        public static final String FK_CURSO = "FK_CURSO";
    }

    public static final class Usuario implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Usuario");

        // Table column
        public static final String NOMBRE = "Nombre";
        public static final String APELLIDOS = "Apellidos";
        public static final String EMAIL = "Email";
        public static final String CONTRASENA = "Contrasena";
        public static final String DNI = "Dni";
        public static final String TELEFONO = "Telefono";
        public static final String TIPOUSUARIO = "TipoUsuario";
    }

    public static final class Solicitud implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Solicitud");

        // Table column
        public static final String FECHA = "Fecha";
        public static final String CURSOACADEMICO = "CursoAcademico";
        public static final String FK_CURSO = "FK_Curso";
        public static final String FK_USUARIO = "FK_Usuario";
    }

    public static final class ConvalidacionNoPrevista implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/ConvalidacionNoPrevista");

        // Table column
        public static final String MODULOACONVALIDAR = "ModuloAConvalidar";
        public static final String ESTUDIOAPORTADO = "EstudioAportado";
        public static final String FK_SOLICITUD = "FK_Solicitud";
    }

    public static final class ConvalidacionPosibleEnSolicitud implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/ConvalidacionPosibleEnSolicitud");

        // Table column
        public static final String FK_CONVALIDACIONPOSIBLE = "FK_ConvalidacionPosible";
        public static final String FK_SOLICITUD = "FK_Solicitud";
    }

}
