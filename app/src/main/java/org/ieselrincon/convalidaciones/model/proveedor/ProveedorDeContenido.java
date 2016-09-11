package org.ieselrincon.convalidaciones.model.proveedor;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import org.ieselrincon.convalidaciones.model.pojos.Ciclo;
import org.ieselrincon.convalidaciones.model.pojos.TipoUsuario;
import org.ieselrincon.convalidaciones.model.pojos.Turno;

import java.util.ArrayList;

public class ProveedorDeContenido extends ContentProvider {
    private static final String LOGTAG = "Tiburcio - ProveedorDeContenido";

    private static final int CICLO_ONE_REG = 1;
    private static final int CICLO_ALL_REGS = 2;

    private static final int CURSO_ONE_REG = 10;
    private static final int CURSO_ALL_REGS = 11;

    private static final int ESTUDIO_ONE_REG = 20;
    private static final int ESTUDIO_ALL_REGS = 21;

    private static final int CONVALIDACIONPOSIBLE_ONE_REG = 30;
    private static final int CONVALIDACIONPOSIBLE_ALL_REGS = 31;

    private static final int USUARIO_ONE_REG = 40;
    private static final int USUARIO_ALL_REGS = 41;

    private static final int SOLICITUD_ONE_REG = 50;
    private static final int SOLICITUD_ALL_REGS = 51;

    private static final int CONVALIDACIONNOPREVISTA_ONE_REG = 60;
    private static final int CONVALIDACIONNOPREVISTA_ALL_REGS = 61;

    private static final int CONVALIDACIONPOSIBLEENSOLICITUD_ONE_REG = 70;
    private static final int CONVALIDACIONPOSIBLEENSOLICITUD_ALL_REGS = 71;

    private SQLiteDatabase sqlDB;
    public DatabaseHelper dbHelper;
    private static final String DATABASE_NAME = "Convalidaciones.db";
    private static final int DATABASE_VERSION = 30;

    private static final String CICLO_TABLE_NAME = "Ciclo";
    private static final String CURSO_TABLE_NAME = "Curso";
    private static final String ESTUDIO_TABLE_NAME = "Estudio";
    private static final String CONVALIDACIONPOSIBLE_TABLE_NAME = "ConvalidacionPosible";
    private static final String USUARIO_TABLE_NAME = "Usuario";
    private static final String SOLICITUD_TABLE_NAME = "Solicitud";
    private static final String CONVALIDACIONNOPREVISTA_TABLE_NAME = "ConvalidacionNoPrevista";
    private static final String CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME = "ConvalidacionPosibleEnSolicitud";

    // Indicates an invalid content URI
    public static final int INVALID_URI = -1;

    // Defines a helper object that matches content URIs to table-specific parameters
    private static final UriMatcher sUriMatcher;

    // Stores the MIME types served by this provider
    private static final SparseArray<String> sMimeTypes;

    /*
     * Initializes meta-data used by the content provider:
     * - UriMatcher that maps content URIs to codes
     * - MimeType array that returns the custom MIME type of a table
     */
    static {

        // Creates an object that associates content URIs with numeric codes
        sUriMatcher = new UriMatcher(0);

        /*
         * Sets up an array that maps content URIs to MIME types, via a mapping between the
         * URIs and an integer code. These are custom MIME types that apply to tables and rows
         * in this particular provider.
         */
        sMimeTypes = new SparseArray<String>();

        // Adds a URI "match" entry that maps picture URL content URIs to a numeric code

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CICLO_TABLE_NAME,
                CICLO_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CICLO_TABLE_NAME + "/#",
                CICLO_ONE_REG);

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CURSO_TABLE_NAME,
                CURSO_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CURSO_TABLE_NAME + "/#",
                CURSO_ONE_REG);

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                ESTUDIO_TABLE_NAME,
                ESTUDIO_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                ESTUDIO_TABLE_NAME + "/#",
                ESTUDIO_ONE_REG);

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CONVALIDACIONPOSIBLE_TABLE_NAME,
                CONVALIDACIONPOSIBLE_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CONVALIDACIONPOSIBLE_TABLE_NAME + "/#",
                CONVALIDACIONPOSIBLE_ONE_REG);

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                USUARIO_TABLE_NAME,
                USUARIO_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                USUARIO_TABLE_NAME + "/#",
                USUARIO_ONE_REG);

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                SOLICITUD_TABLE_NAME,
                SOLICITUD_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                SOLICITUD_TABLE_NAME + "/#",
                SOLICITUD_ONE_REG);

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CONVALIDACIONNOPREVISTA_TABLE_NAME,
                CONVALIDACIONNOPREVISTA_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CONVALIDACIONNOPREVISTA_TABLE_NAME + "/#",
                CONVALIDACIONNOPREVISTA_ONE_REG);

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME,
                CONVALIDACIONPOSIBLEENSOLICITUD_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME + "/#",
                CONVALIDACIONPOSIBLEENSOLICITUD_ONE_REG);

        // Specifies a custom MIME type for the picture URL table

        sMimeTypes.put(
                CICLO_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + CICLO_TABLE_NAME);
        sMimeTypes.put(
                CICLO_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + CICLO_TABLE_NAME);

        sMimeTypes.put(
                CURSO_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + CURSO_TABLE_NAME);
        sMimeTypes.put(
                CURSO_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + CURSO_TABLE_NAME);

        sMimeTypes.put(
                ESTUDIO_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + ESTUDIO_TABLE_NAME);
        sMimeTypes.put(
                ESTUDIO_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + ESTUDIO_TABLE_NAME);

        sMimeTypes.put(
                CONVALIDACIONPOSIBLE_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + CONVALIDACIONPOSIBLE_TABLE_NAME);
        sMimeTypes.put(
                CONVALIDACIONPOSIBLE_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + CONVALIDACIONPOSIBLE_TABLE_NAME);

        sMimeTypes.put(
                USUARIO_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + USUARIO_TABLE_NAME);
        sMimeTypes.put(
                USUARIO_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + USUARIO_TABLE_NAME);

        sMimeTypes.put(
                SOLICITUD_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + SOLICITUD_TABLE_NAME);
        sMimeTypes.put(
                SOLICITUD_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + SOLICITUD_TABLE_NAME);

        sMimeTypes.put(
                CONVALIDACIONNOPREVISTA_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + CONVALIDACIONNOPREVISTA_TABLE_NAME);
        sMimeTypes.put(
                CONVALIDACIONNOPREVISTA_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + CONVALIDACIONNOPREVISTA_TABLE_NAME);

        sMimeTypes.put(
                CONVALIDACIONPOSIBLEENSOLICITUD_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME);
        sMimeTypes.put(
                CONVALIDACIONPOSIBLEENSOLICITUD_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public ArrayList<Cursor> getData(String Query){
            //get writable database
            SQLiteDatabase sqlDB = this.getWritableDatabase();
            String[] columns = new String[] { "mesage" };
            //an array list of cursor to save two cursors one has results from the query
            //other cursor stores error message if any errors are triggered
            ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
            MatrixCursor Cursor2= new MatrixCursor(columns);
            alc.add(null);
            alc.add(null);


            try{
                String maxQuery = Query ;
                //execute the query results will be save in Cursor c
                Cursor c = sqlDB.rawQuery(maxQuery, null);


                //add value to cursor2
                Cursor2.addRow(new Object[] { "Success" });

                alc.set(1,Cursor2);
                if (null != c && c.getCount() > 0) {

                    alc.set(0,c);
                    c.moveToFirst();

                    return alc ;
                }
                return alc;
            } catch(SQLException sqlEx){
                Log.d("printing exception", sqlEx.getMessage());
                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
                alc.set(1,Cursor2);
                return alc;
            } catch(Exception ex){

                Log.d("printing exception", ex.getMessage());

                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+ex.getMessage() });
                alc.set(1,Cursor2);
                return alc;
            }

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);

            //if (!db.isReadOnly()){
            //Habilitamos la integridad referencial
            db.execSQL("PRAGMA foreign_keys=ON;");
            //}
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create table to store

            db.execSQL("Create table "
                            + CICLO_TABLE_NAME
                            + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                            + Contrato.Ciclo.NOMBRE + " TEXT , "
                            + Contrato.Ciclo.ABREVIATURA + " TEXT ); "
            );

            db.execSQL("Create table "
                    + CURSO_TABLE_NAME
                    + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contrato.Curso.ANNO + " INTEGER , "
                    + Contrato.Curso.TURNO + " TEXT , "
                    + Contrato.Curso.FK_CICLO + " INTEGER , "
                    + "FOREIGN KEY(" + Contrato.Curso.FK_CICLO
                    + ") REFERENCES " + CICLO_TABLE_NAME + "(" + Contrato.Ciclo._ID + ")"
                    + " );"
            );

            db.execSQL("Create table "
                    + ESTUDIO_TABLE_NAME
                    + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contrato.Estudio.NOMBRE + " TEXT , "
                    + Contrato.Estudio.CODIGO + " TEXT ); "
            );

            db.execSQL("Create table "
                    + CONVALIDACIONPOSIBLE_TABLE_NAME
                    + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + " INTEGER , "
                    + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + " INTEGER , "
                    + Contrato.ConvalidacionPosible.FK_CURSO + " INTEGER , "
                    + "FOREIGN KEY(" + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO
                    + ") REFERENCES " + ESTUDIO_TABLE_NAME + "(" + Contrato.Estudio._ID + "), "
                    + "FOREIGN KEY(" + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO
                    + ") REFERENCES " + ESTUDIO_TABLE_NAME + "(" + Contrato.Estudio._ID + "), "
                    + "FOREIGN KEY(" + Contrato.ConvalidacionPosible.FK_CURSO
                    + ") REFERENCES " + CURSO_TABLE_NAME + "(" + Contrato.Curso._ID + ") "
                    + " );"
            );

            db.execSQL("Create table "
                    + USUARIO_TABLE_NAME
                    + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contrato.Usuario.NOMBRE + " TEXT , "
                    + Contrato.Usuario.APELLIDOS + " TEXT , "
                    + Contrato.Usuario.CONTRASENA + " TEXT , "
                    + Contrato.Usuario.EMAIL + " TEXT , "
                    + Contrato.Usuario.DNI + " TEXT , "
                    + Contrato.Usuario.TELEFONO + " TEXT , "
                    + Contrato.Usuario.TIPOUSUARIO + " TEXT ); "
            );

            db.execSQL("Create table "
                    + SOLICITUD_TABLE_NAME
                    + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contrato.Solicitud.FECHA + " INTEGER , "
                    + Contrato.Solicitud.CURSOACADEMICO + " TEXT , "
                    + Contrato.Solicitud.FK_CURSO + " INTEGER , "
                    + Contrato.Solicitud.FK_USUARIO + " INTEGER , "
                    + "FOREIGN KEY(" + Contrato.Solicitud.FK_CURSO
                    + ") REFERENCES " + CURSO_TABLE_NAME + "(" + Contrato.Curso._ID + "), "
                    + "FOREIGN KEY(" + Contrato.Solicitud.FK_USUARIO
                    + ") REFERENCES " + USUARIO_TABLE_NAME + "(" + Contrato.Usuario._ID + ") "
                    + " );"
            );

            db.execSQL("Create table "
                    + CONVALIDACIONNOPREVISTA_TABLE_NAME
                    + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contrato.ConvalidacionNoPrevista.MODULOACONVALIDAR + " TEXT , "
                    + Contrato.ConvalidacionNoPrevista.ESTUDIOAPORTADO + " TEXT , "
                    + Contrato.ConvalidacionNoPrevista.FK_SOLICITUD + " INTEGER , "
                    + "FOREIGN KEY(" + Contrato.ConvalidacionNoPrevista.FK_SOLICITUD
                    + ") REFERENCES " + SOLICITUD_TABLE_NAME + "(" + Contrato.Solicitud._ID + ") "
                    + " );"
            );

            db.execSQL("Create table "
                    + CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME
                    + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE + " INTEGER , "
                    + Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD + " INTEGER , "
                    + "FOREIGN KEY(" + Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE
                    + ") REFERENCES " + CONVALIDACIONPOSIBLE_TABLE_NAME + "(" + Contrato.ConvalidacionPosible._ID + "), "
                    + "FOREIGN KEY(" + Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD
                    + ") REFERENCES " + SOLICITUD_TABLE_NAME + "(" + Contrato.Solicitud._ID + ") "
                    + " );"
            );

            inicializarDatos(db);

        }

        void inicializarDatos(SQLiteDatabase db){

            db.execSQL("INSERT INTO " + CICLO_TABLE_NAME + " (" +  Contrato.Ciclo._ID + "," + Contrato.Ciclo.NOMBRE + "," + Contrato.Ciclo.ABREVIATURA + ") " +
                    "VALUES (1,'Administración de Sistemas Informáticos en Red','ASIR')");
            db.execSQL("INSERT INTO " + CICLO_TABLE_NAME + " (" +  Contrato.Ciclo._ID + "," + Contrato.Ciclo.NOMBRE + "," + Contrato.Ciclo.ABREVIATURA + ") " +
                    "VALUES (2,'Desarrollo de Aplicaciones Web','DAW')");
            db.execSQL("INSERT INTO " + CICLO_TABLE_NAME + " (" +  Contrato.Ciclo._ID + "," + Contrato.Ciclo.NOMBRE + "," + Contrato.Ciclo.ABREVIATURA + ") " +
                    "VALUES (3,'Desarrollo de Aplicaciones Multiplataforma','DAM')");
            db.execSQL("INSERT INTO " + CICLO_TABLE_NAME + " (" +  Contrato.Ciclo._ID + "," + Contrato.Ciclo.NOMBRE + "," + Contrato.Ciclo.ABREVIATURA + ") " +
                    "VALUES (4,'Sistemas Microinformáticos y Redes','SMR')");

            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (1,1,'" + Turno.MAÑANA + "',1)"); //1ºASIR-Mañana
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (2,2,'" + Turno.MAÑANA + "',1)"); //2ºASIR-Mañana
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (3,1,'" + Turno.NOCHE + "',1)"); //1ºASIR-Noche
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (4,2,'" + Turno.NOCHE + "',1)"); //2ºASIR-Noche
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (5,3,'" + Turno.NOCHE + "',1)"); //3ºASIR-Noche

            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (6,1,'" + Turno.MAÑANA + "',2)"); //1ºDAW-Mañana
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (7,2,'" + Turno.MAÑANA + "',2)"); //2ºDAW-Mañana

            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (8,1,'" + Turno.MAÑANA + "',3)"); //1ºDAM-Mañana
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (9,2,'" + Turno.MAÑANA + "',3)"); //2ºDAM-Mañana
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (10,1,'" + Turno.NOCHE + "',3)"); //1ºDAM-Noche
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (11,2,'" + Turno.NOCHE + "',3)"); //2ºDAM-Noche
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (12,3,'" + Turno.NOCHE + "',3)"); //3ºDAM-Noche
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (13,1,'" + Turno.TARDE + "',3)"); //1ºDAM-Tarde
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (14,2,'" + Turno.TARDE + "',3)"); //2ºDAM-Tarde

            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (15,1,'" + Turno.MAÑANA + "',4)"); //1ºSMR-Mañana
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (16,2,'" + Turno.MAÑANA + "',4)"); //2ºSMR-Mañana
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (17,1,'" + Turno.TARDE + "',4)"); //1ºSMR-Tarde
            db.execSQL("INSERT INTO " + CURSO_TABLE_NAME + " (" +  Contrato.Curso._ID + "," + Contrato.Curso.ANNO + "," + Contrato.Curso.TURNO  + "," + Contrato.Curso.FK_CICLO + ") " +
                    "VALUES (18,2,'" + Turno.TARDE + "',4)"); //2ºSMR-Tarde

            db.execSQL("INSERT INTO " + USUARIO_TABLE_NAME + " (" +  Contrato.Usuario._ID + "," + Contrato.Usuario.NOMBRE + "," + Contrato.Usuario.APELLIDOS  + "," +
                    Contrato.Usuario.DNI  + "," + Contrato.Usuario.EMAIL + "," + Contrato.Usuario.CONTRASENA + "," + Contrato.Usuario.TELEFONO + "," + Contrato.Usuario.TIPOUSUARIO + ") " +
                    "VALUES (1,'Tiburcio','Cruz Ravelo','52845821d','t@t.es','HolaHola','928655538543','" + TipoUsuario.ADMINISTRADOR +"')"); //Usuario Administrador

            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (1,483,'Sistemas Informáticos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (2,484,'Bases de Datos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (3,485,'Programación')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (4,373,'Lenguajes de Marcas y Sistemas de Gestión de Información')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (5,487,'Entornos de Desarrollo')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (6,614,'Despliegue de Aplicaciones Web')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (7,619,'Formación en Centros de Trabajo')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (8,486,'Acceso a Datos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (9,488,'Desarrollo de Interfaces')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (10,369,'Implantación de Sistemas Operativos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (11,371,'Fundamentos de Hardware')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (12,370,'Planificación y Administración de Redes')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (13,382,'Formación en Centros de Trabajo')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (14,372,'Gestión de Bases de Datos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (15,377,'Administración de Sistemas Gestores de Bases de Datos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (16,374,'Administración de Sistemas Operativos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (17,376,'Implantación de Aplicaciones Web')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (18,225,'Redes locales')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (19,221,'Montaje y reparación de equipos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (20,223,'Aplicaciones ofimáticas')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (21,227,'Servicios de red')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (22,228,'Aplicaciones web')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (23,222,'Sistemas operativos monopuesto')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (24,224,'Sistemas operativos en red')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (25,0,'Inglés (Grado Superior LOE)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (26,0,'EMR (Grado Superior y Grado Medio)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (27,0,'EMR (Grado Medio)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (28,0,'EMR (Grado Superior)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (29,0,'FOL')");

            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (30,0,'Sistemas Informáticos Multiusuario y en Red (LOGSE-CDA)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (31,0,'Ciclo Completo Administración de Sistemas Informáticos en Red (LOE)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (32,0,'Sistemas Informáticos monousuario y multiusuario (LOGSE-CAS) y Redes de Área Local (LOGSE-CAS)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (33,0,'Sistemas Informáticos (DAM)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (34,0,'Implantación de Sistemas Operativos (ASIR) y también Fundamentos de Hardware (ASIR)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (35,0,'Desarrollo de Aplicaciones en Entornos de 4ª Generación y con Herramientas CASE (LOGSE-CDA)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (36,0,'Sistemas Gestores de Bases de Datos (LOGSE-CAS)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (37,0,'FOL')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (38,0,'Bases de Datos (DAM)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (39,0,'Programación (DAM)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (40,0,'Programación en Lenguajes Estructurados (LOGSE-CDA)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (41,0,'Lenguajes de Marcas y Sistemas de Gestión de Información (ASIR)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (42,0,'Ciclo Completo Desarrollo de Aplicaciones Informáticas (LOGSE-CDA)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (43,0,'Fundamentos de Programación (LOGSE-CAS)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (44,0,'Lenguajes de Marcas y Sistemas de Gestión de Información (DAM)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (45,0,'Análisis y Diseño detallado de Aplicaciones Informáticas de Gestión (LOGSECDA)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (46,0,'Entornos de Desarrollo (DAM)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (47,0,'Implantación de Aplicaciones Web (ASIR)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (48,0,'FCT de Desarrollo de Aplicaciones Web (LOGSE-CDA)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (49,0,'Sistemas Informáticos (DAW)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (50,0,'Bases de Datos (DAW)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (51,0,'Programación (DAW)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (52,0,'Lenguajes de Marcas y Sistemas de Gestión de Información (DAW)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (53,0,'Entornos de Desarrollo (DAW)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (54,0,'Diseño y Realización de Servicios de Presentación en Entornos Gráficos (LOGSE-CDA)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (55,0,'Programación en Lenguajes Estructurados (LOGSE-CDA) y Desarrollo de Aplicaciones en Entornos de 4ª Generación y con Herramientas CASE (LOGSECDA)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (56,0,'Sistemas Informáticos monousuario y multiusuario (LOGSE-CAS)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (57,0,'Redes de Área Local (LOGSE-CAS)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (58,0,'FCT de Administración de Sistemas Informáticos (LOGSE-CAS)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (59,0,'Desarrollo de Funciones en el Sistema Informático (LOGSE-CAS)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (60,0,'Despliegue de Aplicaciones Web (DAW)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (61,0,'Implantación de Aplicaciones Informáticas de Gestión (LOGSE-CAS)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (62,0,'Instalación y mantenimiento de servicios de redes locales')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (63,0,'Instalación y mantenimiento de equipos y sistemas informáticos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (64,0,'Tratamiento Informático de la Información (CFGM Gestión Administrativa)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (65,0,'Implantación y mantenimiento de aplicaciones ofimáticas y corporativas')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (66,0,'Instalación y mantenimiento de servicios de internet')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (67,0,'Mantenimiento de portales de información')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (68,0,'Sistemas operativos en entornos monousuario y multiusuario')");

            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (70,0,'Cualquier Módulo de Inglés de FP LOGSE ó LOE de Grado Superior de la misma familia profesional de Informática')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (71,0,'Inglés de estudio universitario de la misma familia profesional')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (72,0,'Filología Inglesa ó Traductores e Intérpretes (con Inglés como lengua A, B ó C)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (73,0,'B2 por EOI ó por entidad ALTE (University of Cambridge, Trinity College London)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (74,0,'Inglés del CFGS de Sistemas de Telecomunicaciones e Informáticos')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (75,0,'Cualquier EMR (LOE)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (76,0,'Organización y gestión de una explotación agraria familiar - Ciclo Trabajos Forestales y de Conservación del Medio Natural. (Agraria)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (77,0,'Organización y gestión de una explotación agraria familiar - Ciclo Jardinería. (Agraria)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (78,0,'Organización y gestión de una explotación agraria familiar - Ciclo Explotaciones Agrarias Extensivas. (Agraria)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (79,0,'Organización y gestión de una explotación agraria familiar - Ciclo Explotaciones Agrícolas Intensivas. (Agraria)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (80,0,'Organización y gestión de una explotación agraria familiar - Ciclo Explotaciones Ganaderas. (Agraria)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (81,0,'Administración y gestión de un pequeño establecimiento comercial - Ciclo Comercio. (Comercio y Marketing)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (82,0,'Ciclo completo de Gestión administrativa')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (83,0,'Administración, gestión y comercialización en la pequeña empresa (Cualquier ciclo formativo)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (84,0,'Organización y gestión de una pequeña empresa de actividades de tiempo libre y socioeducativas - Ciclo Animación Sociocultural (Servicios Socioculturales y a la Comunidad)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (85,0,'Organización y gestión de una pequeña empresa de actividades de tiempo libre y socioeducativas - Ciclo Animación de Actividades Físicas y Deportivas (Actividades Físicas y Deportivas)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (86,0,'Organización y gestión de una pequeña empresa de actividades de tiempo libre y socioeducativas - Ciclo Animación Turística (Hostelería y Turismo)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (87,0,'Organización y control en agencias de viajes - Ciclo Agencias de Viajes (Hostelería y Turismo)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (88,0,'Administración de establecimientos de restauración - Ciclo Restauración (Hostelería y Turismo)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (89,0,'Administración y gestión de una unidad/gabinete de Ortoprotésica - Ciclo Ortoprotésica (Sanidad)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (90,0,'Organización, administración y gestión de una unidad/gabinete de prótesis dentales - Ciclo Prótesis Dentales (Sanidad)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (91,0,'Administración y gestión de un gabinete audioprotésico - Ciclo Audioprótesis (Sanidad)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (92,0,'Organización y gestión de una empresa agraria - Ciclo Gestión y Organización de los Recursos Naturales y Paisajísticos. (Agraria)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (93,0,'Organización y gestión de una empresa agraria - Ciclo Gestión y Organización de Empresas Agropecuarias. (Agraria)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (94,0,'Administración, gestión y comercialización en la pequeña empresa (Cualquier\n" +
                    "ciclo formativo)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (95,0,'Ciclo completo de Administración y finanzas.')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (96,0,'Cualquier FOL (LOE)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (97,0,'Cualquier Formación y Orientación Laboral (LOGSE) + Certificado de Técnico en Prevención de Riesgos Laborales (Nivel Básico)')");
            db.execSQL("INSERT INTO " + ESTUDIO_TABLE_NAME + " (" +  Contrato.Estudio._ID + "," + Contrato.Estudio.CODIGO + "," + Contrato.Estudio.NOMBRE + ") " +
                    "VALUES (98,0,'Ciclo Completo GS Prevención de Riesgos Profesionales')");

            //Convalidaciones de 1ºASIR-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (1,1,4,44)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (2,1,4,55)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (3,1,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (4,1,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (5,1,10,56)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (6,1,11,33)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (7,1,11,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (8,1,12,57)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (9,1,14,36)");
            //Otras Convalidaciones de 1ºASIR-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (10,1,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (11,1,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (12,1,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (13,1,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (14,1,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (15,1,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (16,1,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (17,1,29,98)");

            //Convalidaciones de 2ºASIR-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (20,2,4,44)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (21,2,4,55)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (22,2,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (23,2,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (24,2,10,56)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (25,2,11,33)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (26,2,11,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (27,2,12,57)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (28,2,13,58)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (29,2,14,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (30,2,15,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (31,2,16,59)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (32,2,17,60)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (33,2,17,61)");
            //Otras Convalidaciones de 2ºASIR-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (34,2,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (35,2,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (36,2,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (37,2,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (38,2,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (39,2,26,75)"); //EMR (Grado Superior y Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (40,2,28,84)"); //EMR (Grado Superior)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (41,2,28,85)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (42,2,28,86)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (43,2,28,87)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (44,2,28,88)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (45,2,28,89)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (46,2,28,90)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (47,2,28,91)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (48,2,28,92)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (49,2,28,93)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (50,2,28,94)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (51,2,28,95)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (52,2,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (53,2,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (54,2,29,98)");

            //Convalidaciones de 1ºASIR-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (60,3,10,56)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (61,3,11,33)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (62,3,11,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (63,3,12,57)");
            //Otras Convalidaciones de 1ºASIR-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (64,3,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (65,3,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (66,3,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (67,3,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (68,3,25,74)");

            //Convalidaciones de 2ºASIR-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (70,4,4,44)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (71,4,4,55)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (72,4,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (73,4,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (74,4,10,56)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (75,4,11,33)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (76,4,11,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (77,4,12,57)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (78,4,14,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (79,4,15,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (80,4,16,59)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (81,4,17,60)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (82,4,17,61)");
            //Otras Convalidaciones de 2ºASIR-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (83,4,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (84,4,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (85,4,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (86,4,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (87,4,25,74)");

            //Convalidaciones de 3ºASIR-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (90,5,4,44)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (91,5,4,55)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (92,5,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (93,5,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (94,5,10,56)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (95,5,11,33)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (96,5,11,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (97,5,12,57)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (98,5,13,58)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (99,5,14,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (100,5,15,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (101,5,16,59)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (102,5,17,60)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (103,5,17,61)");
            //Otras Convalidaciones de 3ºASIR-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (104,5,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (105,5,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (106,5,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (107,5,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (108,5,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (109,5,26,75)"); //EMR (Grado Superior y Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (110,5,28,84)"); //EMR (Grado Superior)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (111,5,28,85)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (112,5,28,86)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (113,5,28,87)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (114,5,28,88)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (115,5,28,89)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (116,5,28,90)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (117,5,28,91)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (118,5,28,92)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (119,5,28,93)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (120,5,28,94)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (121,5,28,95)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (122,5,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (123,5,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (124,5,29,98)");

            //Convalidaciones Posibles de 1ºDAW-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (130,6,1,30)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (131,6,1,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (132,6,1,32)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (133,6,1,33)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (134,6,1,34)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (135,6,2,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (136,6,2,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (137,6,2,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (138,6,2,38)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (139,6,3,39)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (140,6,3,40)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (141,6,4,41)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (142,6,4,42)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (143,6,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (144,6,4,44)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (145,6,5,45)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (146,6,5,46)");
            //Otras Convalidaciones de 1ºDAW-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (147,6,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (148,6,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (149,6,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (150,6,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (151,6,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (152,6,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (153,6,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (154,6,29,98)");

            //Convalidaciones Posibles de 2ºDAW-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (160,7,1,30)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (161,7,1,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (162,7,1,32)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (163,7,1,33)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (164,7,1,34)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (165,7,2,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (166,7,2,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (167,7,2,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (168,7,2,38)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (169,7,3,39)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (170,7,3,40)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (171,7,4,41)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (172,7,4,42)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (173,7,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (174,7,4,44)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (175,7,5,45)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (176,7,5,46)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (177,7,6,47)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (178,7,7,48)");
            //Otras Convalidaciones de 2ºDAW-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (179,7,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (180,7,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (181,7,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (182,7,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (183,7,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (184,7,26,75)"); //EMR (Grado Superior y Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (185,7,28,84)"); //EMR (Grado Superior)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (186,7,28,85)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (187,7,28,86)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (188,7,28,87)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (189,7,28,88)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (190,7,28,89)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (191,7,28,90)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (192,7,28,91)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (193,7,28,92)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (194,7,28,93)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (195,7,28,94)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (196,7,28,95)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (197,7,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (198,7,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (199,7,29,98)");

            //Convalidaciones Posibles de 1ºDAM-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (200,8,1,30)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (201,8,1,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (202,8,1,32)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (203,8,1,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (204,8,1,34)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (205,8,2,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (206,8,2,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (207,8,2,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (208,8,2,50)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (209,8,3,51)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (210,8,3,40)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (211,8,4,41)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (212,8,4,42)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (213,8,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (214,8,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (215,8,5,53)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (216,8,5,45)");
            //Otras Convalidaciones de 1ºDAM-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (217,8,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (218,8,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (219,8,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (220,8,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (221,8,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (222,8,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (223,8,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (224,8,29,98)");

            //Convalidaciones Posibles de 2ºDAM-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (230,9,1,30)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (231,9,1,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (232,9,1,32)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (233,9,1,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (234,9,1,34)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (235,9,2,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (236,9,2,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (237,9,2,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (238,9,2,50)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (239,9,3,51)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (240,9,3,40)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (241,9,4,41)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (242,9,4,42)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (243,9,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (244,9,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (245,9,5,45)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (246,9,5,53)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (247,9,7,48)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (248,9,8,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (249,9,9,54)");
            //Otras Convalidaciones de 2ºDAM-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (250,9,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (251,9,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (252,9,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (253,9,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (254,9,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (255,9,26,75)"); //EMR (Grado Superior y Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (256,9,28,84)"); //EMR (Grado Superior)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (257,9,28,85)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (258,9,28,86)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (259,9,28,87)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (260,9,28,88)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (261,9,28,89)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (262,9,28,90)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (263,9,28,91)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (264,9,28,92)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (265,9,28,93)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (266,9,28,94)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (267,9,28,95)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (268,9,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (269,9,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (270,9,29,98)");

            //Convalidaciones Posibles de 1ºDAM-TARDE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (280,13,1,30)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (281,13,1,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (282,13,1,32)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (283,13,1,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (284,13,1,34)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (285,13,2,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (286,13,2,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (287,13,2,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (288,13,2,50)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (289,13,3,51)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (290,13,3,40)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (291,13,4,41)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (292,13,4,42)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (293,13,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (294,13,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (295,13,5,53)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (296,13,5,45)");
            //Otras Convalidaciones de 1ºDAM-TARDE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (297,13,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (298,13,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (299,13,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (300,13,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (301,13,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (302,13,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (303,13,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (304,13,29,98)");

            //Convalidaciones Posibles de 2ºDAM-TARDE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (400,14,1,30)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (401,14,1,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (402,14,1,32)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (403,14,1,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (404,14,1,34)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (405,14,2,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (406,14,2,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (407,14,2,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (408,14,2,50)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (409,14,3,51)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (410,14,3,40)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (411,14,4,41)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (412,14,4,42)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (413,14,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (414,14,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (415,14,5,45)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (416,14,5,53)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (417,14,7,48)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (418,14,8,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (419,14,9,54)");
            //Otras Convalidaciones de 2ºDAM-TARDE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (420,14,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (421,14,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (422,14,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (423,14,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (424,14,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (425,14,26,75)"); //EMR (Grado Superior y Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (426,14,28,84)"); //EMR (Grado Superior)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (427,14,28,85)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (428,14,28,86)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (429,14,28,87)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (430,14,28,88)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (431,14,28,89)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (432,14,28,90)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (433,14,28,91)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (434,14,28,92)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (435,14,28,93)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (436,14,28,94)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (437,14,28,95)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (438,14,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (439,14,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (440,14,29,98)");

            //Convalidaciones Posibles de 1ºDAM-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (500,10,1,30)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (501,10,1,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (502,10,1,32)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (503,10,1,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (504,10,1,34)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (505,10,3,51)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (506,10,3,40)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (507,10,4,41)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (508,10,4,42)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (509,10,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (510,10,4,52)");
            //Otras Convalidaciones de 1ºDAM-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (511,10,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (512,10,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (513,10,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (514,10,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (515,10,25,74)");

            //Convalidaciones Posibles de 2ºDAM-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (520,11,1,30)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (521,11,1,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (522,11,1,32)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (523,11,1,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (524,11,1,34)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (525,11,2,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (526,11,2,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (527,11,2,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (528,11,2,50)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (529,11,3,51)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (530,11,3,40)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (531,11,4,41)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (532,11,4,42)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (533,11,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (534,11,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (535,11,5,45)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (536,11,5,53)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (537,11,8,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (538,11,9,54)");
            //Otras Convalidaciones de 2ºDAM-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (539,11,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (540,11,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (541,11,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (542,11,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (543,11,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (544,11,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (545,11,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (546,11,29,98)");

            //Convalidaciones Posibles de 3ºDAM-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (550,12,1,30)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (551,12,1,49)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (552,12,1,32)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (553,12,1,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (554,12,1,34)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (555,12,2,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (556,12,2,31)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (557,12,2,36)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (558,12,2,50)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (559,12,3,51)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (560,12,3,40)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (561,12,4,41)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (562,12,4,42)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (563,12,4,43)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (564,12,4,52)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (565,12,5,45)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (566,12,5,53)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (567,12,7,48)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (568,12,8,35)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (569,12,9,54)");
            //Otras Convalidaciones de 3ºDAM-NOCHE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (570,12,25,70)"); //Inglés (Grado Superior LOE)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (571,12,25,71)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (572,12,25,72)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (573,12,25,73)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (574,12,25,74)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (575,12,26,75)"); //EMR (Grado Superior y Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (576,12,28,84)"); //EMR (Grado Superior)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (577,12,28,85)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (578,12,28,86)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (579,12,28,87)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (580,12,28,88)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (581,12,28,89)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (582,12,28,90)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (583,12,28,91)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (584,12,28,92)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (585,12,28,93)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (586,12,28,94)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (587,12,28,95)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (588,12,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (589,12,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (590,12,29,98)");

            // Convalidaciones posibles de 1ºSMR-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (600,15,18,62)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (601,15,19,63)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (602,15,20,64)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (603,15,20,65)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (604,15,23,68)");
            //Otras Convalidaciones de 1ºSMR-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (605,15,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (606,15,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (607,15,29,98)");

            // Convalidaciones posibles de 2ºSMR-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (609,16,18,62)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (610,16,19,63)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (611,16,20,64)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (612,16,20,65)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (613,16,21,66)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (614,16,22,67)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (615,16,23,68)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (616,16,24,68)");
            //Otras Convalidaciones de 2ºSMR-MAÑANA
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (617,16,26,75)"); //EMR (Grado Superior y Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (618,16,27,76)"); //EMR (Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (619,16,27,77)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (620,16,27,78)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (621,16,27,79)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (622,16,27,80)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (623,16,27,81)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (624,16,27,82)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (625,16,27,83)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (626,16,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (627,16,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (628,16,29,98)");

            // Convalidaciones posibles de 1ºSMR-TARDE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (700,17,18,62)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (701,17,19,63)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (702,17,20,64)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (703,17,20,65)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (704,17,23,68)");
            //Otras Convalidaciones de 1ºSMR-TARDE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (705,17,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (706,17,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (707,17,29,98)");

            // Convalidaciones posibles de 2ºSMR-TARDE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (708,18,18,62)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (709,18,19,63)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (710,18,20,64)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (711,18,20,65)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (712,18,21,66)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (713,18,22,67)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (714,18,23,68)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (715,18,24,68)");
            //Otras Convalidaciones de 2ºSMR-TARDE
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (716,18,26,75)"); //EMR (Grado Superior y Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (717,18,27,76)"); //EMR (Grado Medio)
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (718,18,27,77)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (719,18,27,78)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (720,18,27,79)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (721,18,27,80)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (722,18,27,81)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (723,18,27,82)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (724,18,27,83)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (725,18,29,96)"); //FOL
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (726,18,29,97)");
            db.execSQL("INSERT INTO " + CONVALIDACIONPOSIBLE_TABLE_NAME + " (" +  Contrato.ConvalidacionPosible._ID + "," + Contrato.ConvalidacionPosible.FK_CURSO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO + "," + Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO + ") " +
                    "VALUES (727,18,29,98)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + CICLO_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CURSO_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ESTUDIO_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CONVALIDACIONPOSIBLE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + USUARIO_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SOLICITUD_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CONVALIDACIONNOPREVISTA_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME);

            onCreate(db);
        }

    }

    public ProveedorDeContenido() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        sqlDB = dbHelper.getWritableDatabase();
        // insert record in user table and get the row number of recently inserted record

        String table = "";
        switch (sUriMatcher.match(uri)) {
            case CICLO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Ciclo._ID + " = "
                        + uri.getLastPathSegment();
                table = CICLO_TABLE_NAME;
                break;
            case CICLO_ALL_REGS:
                table = CICLO_TABLE_NAME;
                break;
            case CURSO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Curso._ID + " = "
                        + uri.getLastPathSegment();
                table = CURSO_TABLE_NAME;
                break;
            case CURSO_ALL_REGS:
                table = CURSO_TABLE_NAME;
                break;
            case ESTUDIO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Estudio._ID + " = "
                        + uri.getLastPathSegment();
                table = ESTUDIO_TABLE_NAME;
                break;
            case ESTUDIO_ALL_REGS:
                table = ESTUDIO_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLE_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.ConvalidacionPosible._ID + " = "
                        + uri.getLastPathSegment();
                table = CONVALIDACIONPOSIBLE_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLE_ALL_REGS:
                table = CONVALIDACIONPOSIBLE_TABLE_NAME;
                break;
            case USUARIO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Usuario._ID + " = "
                        + uri.getLastPathSegment();
                table = USUARIO_TABLE_NAME;
                break;
            case USUARIO_ALL_REGS:
                table = USUARIO_TABLE_NAME;
                break;
            case SOLICITUD_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Solicitud._ID + " = "
                        + uri.getLastPathSegment();
                table = SOLICITUD_TABLE_NAME;
                break;
            case SOLICITUD_ALL_REGS:
                table = SOLICITUD_TABLE_NAME;
                break;
            case CONVALIDACIONNOPREVISTA_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.ConvalidacionNoPrevista._ID + " = "
                        + uri.getLastPathSegment();
                table = CONVALIDACIONNOPREVISTA_TABLE_NAME;
                break;
            case CONVALIDACIONNOPREVISTA_ALL_REGS:
                table = CONVALIDACIONNOPREVISTA_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLEENSOLICITUD_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.ConvalidacionPosibleEnSolicitud._ID + " = "
                        + uri.getLastPathSegment();
                table = CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLEENSOLICITUD_ALL_REGS:
                table = CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME;
                break;

        }
        int rows = sqlDB.delete(table, selection, selectionArgs);
        if (rows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return rows;
        }
        throw new SQLException("Failed to delete row into " + uri);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        sqlDB = dbHelper.getWritableDatabase();

        String table = "";
        switch (sUriMatcher.match(uri)) {
            case CICLO_ALL_REGS:
                table = CICLO_TABLE_NAME;
                break;
            case CURSO_ALL_REGS:
                table = CURSO_TABLE_NAME;
                break;
            case ESTUDIO_ALL_REGS:
                table = ESTUDIO_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLE_ALL_REGS:
                table = CONVALIDACIONPOSIBLE_TABLE_NAME;
                break;
            case USUARIO_ALL_REGS:
                table = USUARIO_TABLE_NAME;
                break;
            case SOLICITUD_ALL_REGS:
                table = SOLICITUD_TABLE_NAME;
                break;
            case CONVALIDACIONNOPREVISTA_ALL_REGS:
                table = CONVALIDACIONNOPREVISTA_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLEENSOLICITUD_ALL_REGS:
                table = CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME;
                break;
        }

        long rowId = sqlDB.insert(table, "", values);

        if (rowId > 0) {
            Uri rowUri = ContentUris.appendId(
                    uri.buildUpon(), rowId).build();
            getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return (dbHelper == null) ? false : true;
    }

    public void resetDatabase() {
        dbHelper.close();
        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = null;

        switch (sUriMatcher.match(uri)) {
            case CICLO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Ciclo._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(CICLO_TABLE_NAME);
                break;
            case CICLO_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.Ciclo._ID + " ASC";
                qb.setTables(CICLO_TABLE_NAME);
                break;
            case CURSO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Curso._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(CURSO_TABLE_NAME);
                break;
            case CURSO_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.Curso._ID + " ASC";
                qb.setTables(CURSO_TABLE_NAME);
                break;
            case ESTUDIO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Estudio._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(ESTUDIO_TABLE_NAME);
                break;
            case ESTUDIO_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.Estudio._ID + " ASC";
                qb.setTables(ESTUDIO_TABLE_NAME);
                break;
            case CONVALIDACIONPOSIBLE_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.ConvalidacionPosible._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(CONVALIDACIONPOSIBLE_TABLE_NAME);
                break;
            case CONVALIDACIONPOSIBLE_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.ConvalidacionPosible._ID + " ASC";
                qb.setTables(CONVALIDACIONPOSIBLE_TABLE_NAME);
                break;
            case USUARIO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Usuario._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(USUARIO_TABLE_NAME);
                break;
            case USUARIO_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.Usuario._ID + " ASC";
                qb.setTables(USUARIO_TABLE_NAME);
                break;
            case SOLICITUD_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Solicitud._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(SOLICITUD_TABLE_NAME);
                break;
            case SOLICITUD_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.Solicitud._ID + " ASC";
                qb.setTables(SOLICITUD_TABLE_NAME);
                break;
            case CONVALIDACIONNOPREVISTA_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.ConvalidacionNoPrevista._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(CONVALIDACIONNOPREVISTA_TABLE_NAME);
                break;
            case CONVALIDACIONNOPREVISTA_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.ConvalidacionNoPrevista._ID + " ASC";
                qb.setTables(CONVALIDACIONNOPREVISTA_TABLE_NAME);
                break;
            case CONVALIDACIONPOSIBLEENSOLICITUD_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.ConvalidacionPosibleEnSolicitud._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME);
                break;
            case CONVALIDACIONPOSIBLEENSOLICITUD_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.ConvalidacionPosibleEnSolicitud._ID + " ASC";
                qb.setTables(CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME);
                break;
        }

        Cursor c;
        c = qb.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        sqlDB = dbHelper.getWritableDatabase();
        // insert record in user table and get the row number of recently inserted record

        Log.e(LOGTAG,"update: "+values.toString()+":"+selection+":"+uri.toString());
        String table = "";
        switch (sUriMatcher.match(uri)) {
            case CICLO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Ciclo._ID + " = "
                        + uri.getLastPathSegment();
                table = CICLO_TABLE_NAME;
                break;
            case CICLO_ALL_REGS:
                table = CICLO_TABLE_NAME;
                break;
            case CURSO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Curso._ID + " = "
                        + uri.getLastPathSegment();
                table = CURSO_TABLE_NAME;
                break;
            case CURSO_ALL_REGS:
                table = CURSO_TABLE_NAME;
                break;
            case ESTUDIO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Estudio._ID + " = "
                        + uri.getLastPathSegment();
                table = ESTUDIO_TABLE_NAME;
                break;
            case ESTUDIO_ALL_REGS:
                table = ESTUDIO_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLE_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.ConvalidacionPosible._ID + " = "
                        + uri.getLastPathSegment();
                table = CONVALIDACIONPOSIBLE_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLE_ALL_REGS:
                table = CONVALIDACIONPOSIBLE_TABLE_NAME;
                break;
            case USUARIO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Usuario._ID + " = "
                        + uri.getLastPathSegment();
                table = USUARIO_TABLE_NAME;
                Log.e(LOGTAG,table+":"+selection+":"+uri.toString());
                break;
            case USUARIO_ALL_REGS:
                table = USUARIO_TABLE_NAME;
                break;
            case SOLICITUD_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Solicitud._ID + " = "
                        + uri.getLastPathSegment();
                table = SOLICITUD_TABLE_NAME;
                break;
            case SOLICITUD_ALL_REGS:
                table = SOLICITUD_TABLE_NAME;
                break;
            case CONVALIDACIONNOPREVISTA_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.ConvalidacionNoPrevista._ID + " = "
                        + uri.getLastPathSegment();
                table = CONVALIDACIONNOPREVISTA_TABLE_NAME;
                break;
            case CONVALIDACIONNOPREVISTA_ALL_REGS:
                table = CONVALIDACIONNOPREVISTA_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLEENSOLICITUD_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.ConvalidacionPosibleEnSolicitud._ID + " = "
                        + uri.getLastPathSegment();
                table = CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME;
                break;
            case CONVALIDACIONPOSIBLEENSOLICITUD_ALL_REGS:
                table = CONVALIDACIONPOSIBLEENSOLICITUD_TABLE_NAME;
                break;
        }

        Log.e(LOGTAG,"ANTES");
        int rows = sqlDB.update(table, values, selection, selectionArgs);
        Log.e(LOGTAG,"DESPUÉS");
        if (rows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            return rows;
        }
        throw new SQLException("Failed to update row into " + uri);
    }
}
