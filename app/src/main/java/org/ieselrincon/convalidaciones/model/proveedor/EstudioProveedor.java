package org.ieselrincon.convalidaciones.model.proveedor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.Estudio;

import java.util.ArrayList;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class EstudioProveedor {
	
	private static final String LOGTAG = "Tiburcio - EstudioProveedor";
	
	public static ArrayList<ContentProviderOperation> updateRecord(ContentResolver resolvedor, Estudio registro,
																   boolean ejecutar) {
		
		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Estudio/" + Integer.toString(registro.getID()));
		
		ContentValues values = new ContentValues();
		values.put(Contrato.Estudio.NOMBRE, registro.getNombre());
		values.put(Contrato.Estudio.CODIGO, registro.getCodigo());

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newUpdate(myUri).withValues(values).build());

		if (ejecutar) resolvedor.update(myUri, values, null, null);

		return ops;
	}

	public static void insertRecord(ContentResolver resolvedor, Estudio registro,
								   ArrayList<ContentProviderOperation> ops, boolean ejecutar) throws Exception {
		
		ContentValues values = new ContentValues();
		if(registro.getID()!= G.SIN_VALOR_INT) values.put(Contrato.Estudio._ID, registro.getID());
		values.put(Contrato.Estudio.NOMBRE, registro.getNombre());
		values.put(Contrato.Estudio.CODIGO, registro.getCodigo());

		if (ejecutar) resolvedor.insert(Contrato.Estudio.CONTENT_URI, values);
		else ops.add(ContentProviderOperation.newInsert(Contrato.Estudio.CONTENT_URI).withValues(values).build());

	}

	public static Estudio getRecord(ContentResolver resolvedor, int id) {	

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Estudio._ID,
										  Contrato.Estudio.NOMBRE,
				                          Contrato.Estudio.CODIGO
										};
		
		Uri myUri = Uri.parse("content://"+ Contrato.AUTHORITY + "/Estudio/" + Integer.toString(id));

		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				null // Order-by clause (ascending by name)
		);
		
		if (cur.moveToFirst()) {

			// Get the field values
				Estudio registro = new Estudio();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Estudio._ID)));
				registro.setNombre(cur.getString(cur.getColumnIndex(Contrato.Estudio.NOMBRE)));
				registro.setCodigo(cur.getString(cur.getColumnIndex(Contrato.Estudio.CODIGO)));

				cur.close();
				return registro;
		}
		cur.close();
		return null;
	}
	
	public static ArrayList<ContentProviderOperation> deleteRecord(ContentResolver resolvedor, int id, boolean ejecutar) throws Exception {

		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Estudio/" + Integer.toString(id));

		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		ops.add(ContentProviderOperation.newDelete(myUri).build());

		if (ejecutar) resolvedor.delete(myUri, null, null);

		return ops;
	}

	public static ArrayList<Estudio> getRecords(ContentResolver resolvedor, String grid_currentQuery) {
		Uri myUri = Contrato.Estudio.CONTENT_URI;
		return getRecords(resolvedor, myUri, grid_currentQuery);
	}

	private static ArrayList<Estudio> getRecords(ContentResolver resolvedor, Uri myUri, String grid_currentQuery) {

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Estudio._ID,
										  Contrato.Estudio.NOMBRE,
										  Contrato.Estudio.CODIGO
										};

		String selection = null;
		if (grid_currentQuery != null)
			selection = Contrato.Estudio.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";
		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
			selection, // WHERE clause; which rows to return(all rows)
			null, // WHERE clause selection arguments (none)
			null // Order-by clause (ascending by name)
				);
		
		ArrayList<Estudio> registros = new ArrayList<Estudio>();
		if (cur.moveToFirst()) {
			
			do {
			// Get the field values
				Estudio registro = new Estudio();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Estudio._ID)));
				registro.setNombre(cur.getString(cur.getColumnIndex(Contrato.Estudio.NOMBRE)));
				registro.setCodigo(cur.getString(cur.getColumnIndex(Contrato.Estudio.CODIGO)));
				
				registros.add(registro);
				
			} while (cur.moveToNext());
		}
		
		cur.close();
		return registros;
	}
	
}
