package org.ieselrincon.convalidaciones.model.proveedor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionPosible;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionPosibleEnSolicitud;
import org.ieselrincon.convalidaciones.model.pojos.Solicitud;

import java.util.ArrayList;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class ConvalidacionPosibleEnSolicitudProveedor {
	
	private static final String LOGTAG = "Tiburcio - ConvalidacionPosibleEnSolicitudProveedor";
	
	public static ArrayList<ContentProviderOperation> updateRecord(ContentResolver resolvedor, ConvalidacionPosibleEnSolicitud registro,
																   boolean ejecutar) {
		
		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/ConvalidacionPosibleEnSolicitud/" + Integer.toString(registro.getID()));
		
		ContentValues values = new ContentValues();
		values.put(Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE, registro.getConvalidacionPosible().getID());
		values.put(Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD, registro.getSolicitud().getID());

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newUpdate(myUri).withValues(values).build());

		if (ejecutar) resolvedor.update(myUri, values, null, null);

		return ops;
	}

	public static Uri insertRecord(ContentResolver resolvedor, ConvalidacionPosibleEnSolicitud registro,
								   ArrayList<ContentProviderOperation> ops, boolean ejecutar) throws Exception {
		
		ContentValues values = new ContentValues();
		if(registro.getID()!= G.SIN_VALOR_INT) values.put(Contrato.ConvalidacionPosibleEnSolicitud._ID, registro.getID());
		values.put(Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE, registro.getConvalidacionPosible().getID());
		values.put(Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD, registro.getSolicitud().getID());

		if (ejecutar) {
			Uri result = resolvedor.insert(Contrato.ConvalidacionPosibleEnSolicitud.CONTENT_URI, values);
			return result;
		}
		else ops.add(ContentProviderOperation.newInsert(Contrato.ConvalidacionPosibleEnSolicitud.CONTENT_URI).withValues(values).build());

		return null;
	}

	public static ConvalidacionPosibleEnSolicitud getRecord(ContentResolver resolvedor, int id) {	

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.ConvalidacionPosibleEnSolicitud._ID,
										  Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE,
										  Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD
										};
		
		Uri myUri = Uri.parse("content://"+ Contrato.AUTHORITY + "/ConvalidacionPosibleEnSolicitud/" + Integer.toString(id));

		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				null // Order-by clause (ascending by name)
		);
		
		if (cur.moveToFirst()) {

			// Get the field values
			ConvalidacionPosibleEnSolicitud registro = new ConvalidacionPosibleEnSolicitud();

			registro.setID(cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosibleEnSolicitud._ID)));

			ConvalidacionPosible convalidacionPosible = ConvalidacionPosibleProveedor.getRecord(resolvedor,
					cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE)));
			registro.setConvalidacionPosible(convalidacionPosible);

			Solicitud solicitud = SolicitudProveedor.getRecord(resolvedor,
					cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD)));
			registro.setSolicitud(solicitud);

			cur.close();
			return registro;
		}
		cur.close();
		return null;
	}
	
	public static ArrayList<ContentProviderOperation> deleteRecord(ContentResolver resolvedor, int id, boolean ejecutar) throws Exception {

		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/ConvalidacionPosibleEnSolicitud/" + Integer.toString(id));

		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		ops.add(ContentProviderOperation.newDelete(myUri).build());

		if (ejecutar) resolvedor.delete(myUri, null, null);

		return ops;
	}

	public static ArrayList<ConvalidacionPosibleEnSolicitud> getRecords(ContentResolver resolvedor, String selection) {
		Uri myUri = Contrato.ConvalidacionPosibleEnSolicitud.CONTENT_URI;
		return getRecords(resolvedor, myUri, selection);
	}

	public static ArrayList<ConvalidacionPosibleEnSolicitud> getRecords(ContentResolver resolvedor//, String grid_currentQuery
																		) {
		Uri myUri = Contrato.ConvalidacionPosibleEnSolicitud.CONTENT_URI;
		return getRecords(resolvedor, myUri, null);
	}

	private static ArrayList<ConvalidacionPosibleEnSolicitud> getRecords(ContentResolver resolvedor, Uri myUri, String selection) {

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.ConvalidacionPosibleEnSolicitud._ID,
										Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE,
										Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD
										};

		//String selection = null;
		//if (grid_currentQuery != null)
		//	selection = Contrato.ConvalidacionPosibleEnSolicitud.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";
		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
			selection, // WHERE clause; which rows to return(all rows)
			null, // WHERE clause selection arguments (none)
			null // Order-by clause (ascending by name)
				);
		
		ArrayList<ConvalidacionPosibleEnSolicitud> registros = new ArrayList<ConvalidacionPosibleEnSolicitud>();
		if (cur.moveToFirst()) {
			
			do {
			// Get the field values
				ConvalidacionPosibleEnSolicitud registro = new ConvalidacionPosibleEnSolicitud();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosibleEnSolicitud._ID)));

				ConvalidacionPosible convalidacionPosible = ConvalidacionPosibleProveedor.getRecord(resolvedor,
						cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE)));
				registro.setConvalidacionPosible(convalidacionPosible);

				Solicitud solicitud = SolicitudProveedor.getRecord(resolvedor,
						cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD)));
				registro.setSolicitud(solicitud);
				
				registros.add(registro);
				
			} while (cur.moveToNext());
		}
		
		cur.close();
		return registros;
	}
	
}
