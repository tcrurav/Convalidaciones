package org.ieselrincon.convalidaciones.model.proveedor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.Ciclo;

import java.util.ArrayList;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class CicloProveedor {
	
	private static final String LOGTAG = "Tiburcio - CicloProveedor";
	
	public static ArrayList<ContentProviderOperation> updateRecord(ContentResolver resolvedor, Ciclo registro,
																   boolean ejecutar) {
		
		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Ciclo/" + Integer.toString(registro.getID()));
		
		ContentValues values = new ContentValues();
		values.put(Contrato.Ciclo.NOMBRE, registro.getNombre());
		Log.e(LOGTAG,registro.getAbreviatura());
		values.put(Contrato.Ciclo.ABREVIATURA, registro.getAbreviatura());

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newUpdate(myUri).withValues(values).build());

		if (ejecutar) resolvedor.update(myUri, values, null, null);

		return ops;
	}

	public static void insertRecord(ContentResolver resolvedor, Ciclo registro,
								   ArrayList<ContentProviderOperation> ops, boolean ejecutar) throws Exception {
		
		ContentValues values = new ContentValues();
		if(registro.getID()!= G.SIN_VALOR_INT) values.put(Contrato.Ciclo._ID, registro.getID());
		values.put(Contrato.Ciclo.NOMBRE, registro.getNombre());
		values.put(Contrato.Ciclo.ABREVIATURA, registro.getAbreviatura());

		if (ejecutar) resolvedor.insert(Contrato.Ciclo.CONTENT_URI, values);
		else ops.add(ContentProviderOperation.newInsert(Contrato.Ciclo.CONTENT_URI).withValues(values).build());

	}

	public static Ciclo getRecord(ContentResolver resolvedor, int id) {	

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Ciclo._ID,
										  Contrato.Ciclo.NOMBRE,
										  Contrato.Ciclo.ABREVIATURA
										};
		
		Uri myUri = Uri.parse("content://"+ Contrato.AUTHORITY + "/Ciclo/" + Integer.toString(id));

		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				null // Order-by clause (ascending by name)
		);
		
		if (cur.moveToFirst()) {

			// Get the field values
				Ciclo registro = new Ciclo();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Ciclo._ID)));
				registro.setNombre(cur.getString(cur.getColumnIndex(Contrato.Ciclo.NOMBRE)));
				registro.setAbreviatura(cur.getString(cur.getColumnIndex(Contrato.Ciclo.ABREVIATURA)));

				cur.close();
				return registro;
		}
		cur.close();
		return null;
	}
	
	public static ArrayList<ContentProviderOperation> deleteRecord(ContentResolver resolvedor, int id, boolean ejecutar) throws Exception {

		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Ciclo/" + Integer.toString(id));

		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		ops.add(ContentProviderOperation.newDelete(myUri).build());

		if (ejecutar) resolvedor.delete(myUri, null, null);

		return ops;
	}

	public static ArrayList<Ciclo> getRecords(ContentResolver resolvedor, String grid_currentQuery) {
		Uri myUri = Contrato.Ciclo.CONTENT_URI;
		return getRecords(resolvedor, myUri, grid_currentQuery);
	}

	private static ArrayList<Ciclo> getRecords(ContentResolver resolvedor, Uri myUri, String grid_currentQuery) {

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Ciclo._ID,
										  Contrato.Ciclo.NOMBRE,
										  Contrato.Ciclo.ABREVIATURA
										};

		String selection = null;
		if (grid_currentQuery != null)
			selection = Contrato.Ciclo.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";
		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
			selection, // WHERE clause; which rows to return(all rows)
			null, // WHERE clause selection arguments (none)
			null // Order-by clause (ascending by name)
				);
		
		ArrayList<Ciclo> registros = new ArrayList<Ciclo>();
		if (cur.moveToFirst()) {
			
			do {
			// Get the field values
				Ciclo registro = new Ciclo();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Ciclo._ID)));
				registro.setNombre(cur.getString(cur.getColumnIndex(Contrato.Ciclo.NOMBRE)));
				registro.setAbreviatura(cur.getString(cur.getColumnIndex(Contrato.Ciclo.ABREVIATURA)));
				
				registros.add(registro);
				
			} while (cur.moveToNext());
		}
		
		cur.close();
		return registros;
	}
	
}
