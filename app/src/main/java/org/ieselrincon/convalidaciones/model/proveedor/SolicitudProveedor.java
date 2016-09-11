package org.ieselrincon.convalidaciones.model.proveedor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.Curso;
import org.ieselrincon.convalidaciones.model.pojos.CursoAcademico;
import org.ieselrincon.convalidaciones.model.pojos.Solicitud;
import org.ieselrincon.convalidaciones.model.pojos.Usuario;

import java.util.ArrayList;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class SolicitudProveedor {
	
	private static final String LOGTAG = "Tiburcio - SolicitudProveedor";
	
	public static ArrayList<ContentProviderOperation> updateRecord(ContentResolver resolvedor, Solicitud registro,
																   boolean ejecutar) {
		
		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Solicitud/" + Integer.toString(registro.getID()));
		
		ContentValues values = new ContentValues();
		values.put(Contrato.Solicitud.FECHA, registro.getFecha());
		values.put(Contrato.Solicitud.CURSOACADEMICO, registro.getCursoAcademico().name());
		values.put(Contrato.Solicitud.FK_CURSO, registro.getCurso().getID());
		values.put(Contrato.Solicitud.FK_USUARIO, registro.getUsuario().getID());

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newUpdate(myUri).withValues(values).build());

		if (ejecutar) resolvedor.update(myUri, values, null, null);

		return ops;
	}

	public static Uri insertRecord(ContentResolver resolvedor, Solicitud registro,
								   ArrayList<ContentProviderOperation> ops, boolean ejecutar) throws Exception {
		
		ContentValues values = new ContentValues();
		if(registro.getID()!= G.SIN_VALOR_INT) values.put(Contrato.Solicitud._ID, registro.getID());
		values.put(Contrato.Solicitud.FECHA, registro.getFecha());
		values.put(Contrato.Solicitud.CURSOACADEMICO, registro.getCursoAcademico().name());
		values.put(Contrato.Solicitud.FK_CURSO, registro.getCurso().getID());
		values.put(Contrato.Solicitud.FK_USUARIO, registro.getUsuario().getID());

		if (ejecutar) {
			Uri result = resolvedor.insert(Contrato.Solicitud.CONTENT_URI, values);
			return result;
		}
		else ops.add(ContentProviderOperation.newInsert(Contrato.Solicitud.CONTENT_URI).withValues(values).build());

		return null; //ARREGLAR

	}

	public static Solicitud getRecord(ContentResolver resolvedor, int id) {	

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Solicitud._ID,
										  Contrato.Solicitud.FECHA,
										  Contrato.Solicitud.CURSOACADEMICO,
										  Contrato.Solicitud.FK_CURSO,
										  Contrato.Solicitud.FK_USUARIO
										};
		
		Uri myUri = Uri.parse("content://"+ Contrato.AUTHORITY + "/Solicitud/" + Integer.toString(id));

		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				null // Order-by clause (ascending by name)
		);
		
		if (cur.moveToFirst()) {

			// Get the field values
				Solicitud registro = new Solicitud();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Solicitud._ID)));
				registro.setFecha(cur.getInt(cur.getColumnIndex(Contrato.Solicitud.FECHA)));
				registro.setCursoAcademico(CursoAcademico.valueOf(cur.getString(cur.getColumnIndex(Contrato.Solicitud.CURSOACADEMICO))));

				Curso curso = CursoProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.Solicitud.FK_CURSO)));
				registro.setCurso(curso);

				Usuario usuario = UsuarioProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.Solicitud.FK_USUARIO)));
				registro.setUsuario(usuario);

				cur.close();
				return registro;
		}
		cur.close();
		return null;
	}
	
	public static ArrayList<ContentProviderOperation> deleteRecord(ContentResolver resolvedor, int id, boolean ejecutar) throws Exception {

		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Solicitud/" + Integer.toString(id));

		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		ops.add(ContentProviderOperation.newDelete(myUri).build());

		if (ejecutar) resolvedor.delete(myUri, null, null);

		return ops;
	}

	public static ArrayList<Solicitud> getRecords(ContentResolver resolvedor, String grid_currentQuery) {
		Uri myUri = Contrato.Solicitud.CONTENT_URI;
		return getRecords(resolvedor, myUri//, grid_currentQuery
				 );
	}

	private static ArrayList<Solicitud> getRecords(ContentResolver resolvedor, Uri myUri//, String grid_currentQuery
												   ) {

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Solicitud._ID,
										  Contrato.Solicitud.FECHA,
										  Contrato.Solicitud.CURSOACADEMICO,
										  Contrato.Solicitud.FK_CURSO,
										  Contrato.Solicitud.FK_USUARIO
										};

		String selection = null;
		//if (grid_currentQuery != null)
		//	selection = Contrato.Solicitud.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";
		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
			selection, // WHERE clause; which rows to return(all rows)
			null, // WHERE clause selection arguments (none)
			null // Order-by clause (ascending by name)
				);
		
		ArrayList<Solicitud> registros = new ArrayList<Solicitud>();
		if (cur.moveToFirst()) {
			
			do {
			// Get the field values
				Solicitud registro = new Solicitud();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Solicitud._ID)));
				registro.setFecha(cur.getInt(cur.getColumnIndex(Contrato.Solicitud.FECHA)));
				registro.setCursoAcademico(CursoAcademico.valueOf(cur.getString(cur.getColumnIndex(Contrato.Solicitud.CURSOACADEMICO))));

				Curso curso = CursoProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.Solicitud.FK_CURSO)));
				registro.setCurso(curso);

				Usuario usuario = UsuarioProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.Solicitud.FK_USUARIO)));
				registro.setUsuario(usuario);
				
				registros.add(registro);
				
			} while (cur.moveToNext());
		}
		
		cur.close();
		return registros;
	}
	
}
