package org.ieselrincon.convalidaciones.model.proveedor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionNoPrevista;
import org.ieselrincon.convalidaciones.model.pojos.Solicitud;

import java.util.ArrayList;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class ConvalidacionNoPrevistaProveedor {
	
	private static final String LOGTAG = "Tiburcio - ConvalidacionNoPrevistaProveedor";
	
	public static ArrayList<ContentProviderOperation> updateRecord(ContentResolver resolvedor, ConvalidacionNoPrevista registro,
																   boolean ejecutar) {
		
		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/ConvalidacionNoPrevista/" + Integer.toString(registro.getID()));
		
		ContentValues values = new ContentValues();
		values.put(Contrato.ConvalidacionNoPrevista.MODULOACONVALIDAR, registro.getModuloAConvalidar());
		values.put(Contrato.ConvalidacionNoPrevista.ESTUDIOAPORTADO, registro.getEstudioAportado());
		values.put(Contrato.ConvalidacionNoPrevista.FK_SOLICITUD, registro.getSolicitud().getID());

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newUpdate(myUri).withValues(values).build());

		if (ejecutar) resolvedor.update(myUri, values, null, null);

		return ops;
	}

	public static void insertRecord(ContentResolver resolvedor, ConvalidacionNoPrevista registro,
								   ArrayList<ContentProviderOperation> ops, boolean ejecutar) throws Exception {
		
		ContentValues values = new ContentValues();
		if(registro.getID()!= G.SIN_VALOR_INT) values.put(Contrato.ConvalidacionNoPrevista._ID, registro.getID());
		values.put(Contrato.ConvalidacionNoPrevista.MODULOACONVALIDAR, registro.getModuloAConvalidar());
		values.put(Contrato.ConvalidacionNoPrevista.ESTUDIOAPORTADO, registro.getEstudioAportado());
		values.put(Contrato.ConvalidacionNoPrevista.FK_SOLICITUD, registro.getSolicitud().getID());

		if (ejecutar) resolvedor.insert(Contrato.ConvalidacionNoPrevista.CONTENT_URI, values);
		else ops.add(ContentProviderOperation.newInsert(Contrato.ConvalidacionNoPrevista.CONTENT_URI).withValues(values).build());

	}

	public static ConvalidacionNoPrevista getRecord(ContentResolver resolvedor, int id) {	

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.ConvalidacionNoPrevista._ID,
										  Contrato.ConvalidacionNoPrevista.MODULOACONVALIDAR,
										  Contrato.ConvalidacionNoPrevista.ESTUDIOAPORTADO,
									      Contrato.ConvalidacionNoPrevista.FK_SOLICITUD
										};
		
		Uri myUri = Uri.parse("content://"+ Contrato.AUTHORITY + "/ConvalidacionNoPrevista/" + Integer.toString(id));

		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				null // Order-by clause (ascending by name)
		);
		
		if (cur.moveToFirst()) {

			// Get the field values
				ConvalidacionNoPrevista registro = new ConvalidacionNoPrevista();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionNoPrevista._ID)));
				registro.setModuloAConvalidar(cur.getString(cur.getColumnIndex(Contrato.ConvalidacionNoPrevista.MODULOACONVALIDAR)));
				registro.setEstudioAportado(cur.getString(cur.getColumnIndex(Contrato.ConvalidacionNoPrevista.ESTUDIOAPORTADO)));

				Solicitud solicitud = SolicitudProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionNoPrevista.FK_SOLICITUD)));
				registro.setSolicitud(solicitud);

				cur.close();
				return registro;
		}
		cur.close();
		return null;
	}
	
	public static ArrayList<ContentProviderOperation> deleteRecord(ContentResolver resolvedor, int id, boolean ejecutar) throws Exception {

		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/ConvalidacionNoPrevista/" + Integer.toString(id));

		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		ops.add(ContentProviderOperation.newDelete(myUri).build());

		if (ejecutar) resolvedor.delete(myUri, null, null);

		return ops;
	}

	public static ArrayList<ConvalidacionNoPrevista> getRecords(ContentResolver resolvedor//
																// , String grid_currentQuery
																) {
		Uri myUri = Contrato.ConvalidacionNoPrevista.CONTENT_URI;
		return getRecords(resolvedor, myUri//, grid_currentQuery
										);
	}

	private static ArrayList<ConvalidacionNoPrevista> getRecords(ContentResolver resolvedor, Uri myUri//, String grid_currentQuery
																 ) {

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.ConvalidacionNoPrevista._ID,
										Contrato.ConvalidacionNoPrevista.MODULOACONVALIDAR,
										Contrato.ConvalidacionNoPrevista.ESTUDIOAPORTADO,
										Contrato.ConvalidacionNoPrevista.FK_SOLICITUD
										};

		String selection = null;
		//if (grid_currentQuery != null)
		//	selection = Contrato.ConvalidacionNoPrevista.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";
		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
			selection, // WHERE clause; which rows to return(all rows)
			null, // WHERE clause selection arguments (none)
			null // Order-by clause (ascending by name)
				);
		
		ArrayList<ConvalidacionNoPrevista> registros = new ArrayList<ConvalidacionNoPrevista>();
		if (cur.moveToFirst()) {
			
			do {
			// Get the field values
				ConvalidacionNoPrevista registro = new ConvalidacionNoPrevista();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionNoPrevista._ID)));
				registro.setModuloAConvalidar(cur.getString(cur.getColumnIndex(Contrato.ConvalidacionNoPrevista.MODULOACONVALIDAR)));
				registro.setEstudioAportado(cur.getString(cur.getColumnIndex(Contrato.ConvalidacionNoPrevista.ESTUDIOAPORTADO)));

				Solicitud solicitud = SolicitudProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionNoPrevista.FK_SOLICITUD)));
				registro.setSolicitud(solicitud);
				
				registros.add(registro);
				
			} while (cur.moveToNext());
		}
		
		cur.close();
		return registros;
	}
	
}
