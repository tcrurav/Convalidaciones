package org.ieselrincon.convalidaciones.model.proveedor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.Ciclo;
import org.ieselrincon.convalidaciones.model.pojos.Curso;
import org.ieselrincon.convalidaciones.model.pojos.Turno;

import java.util.ArrayList;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class CursoProveedor {
	
	//private static final String LOGTAG = "Tiburcio - CursoProveedor";
	
	public static ArrayList<ContentProviderOperation> updateRecord(ContentResolver resolvedor, Curso registro,
																   boolean ejecutar) {
		
		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Curso/" + Integer.toString(registro.getID()));
		
		ContentValues values = new ContentValues();
		values.put(Contrato.Curso.ANNO, registro.getAnno());
		values.put(Contrato.Curso.TURNO, registro.getTurno().name());
		values.put(Contrato.Curso.FK_CICLO, registro.getCiclo().getID());

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newUpdate(myUri).withValues(values).build());

		if (ejecutar) resolvedor.update(myUri, values, null, null);

		return ops;
	}

	public static void insertRecord(ContentResolver resolvedor, Curso registro,
								   ArrayList<ContentProviderOperation> ops, boolean ejecutar) throws Exception {
		
		ContentValues values = new ContentValues();
		if(registro.getID()!= G.SIN_VALOR_INT) values.put(Contrato.Curso._ID, registro.getID());
		values.put(Contrato.Curso.ANNO, registro.getAnno());
		values.put(Contrato.Curso.TURNO, registro.getTurno().name());
		values.put(Contrato.Curso.FK_CICLO, registro.getCiclo().getID());

		if (ejecutar) resolvedor.insert(Contrato.Curso.CONTENT_URI, values);
		else ops.add(ContentProviderOperation.newInsert(Contrato.Curso.CONTENT_URI).withValues(values).build());

	}

	public static Curso getRecord(ContentResolver resolvedor, int id) {	

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Curso._ID,
										  Contrato.Curso.ANNO,
										  Contrato.Curso.TURNO,
										  Contrato.Curso.FK_CICLO
										};
		
		Uri myUri = Uri.parse("content://"+ Contrato.AUTHORITY + "/Curso/" + Integer.toString(id));

		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				null // Order-by clause (ascending by name)
		);
		
		if (cur.moveToFirst()) {

			// Get the field values
				Curso registro = new Curso();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Curso._ID)));
				registro.setAnno(cur.getInt(cur.getColumnIndex(Contrato.Curso.ANNO)));
				registro.setTurno(Turno.valueOf(cur.getString(cur.getColumnIndex(Contrato.Curso.TURNO))));

				Ciclo ciclo = CicloProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.Curso.FK_CICLO)));
				registro.setCiclo(ciclo);

				cur.close();
				return registro;
		}
		cur.close();
		return null;
	}
	
	public static ArrayList<ContentProviderOperation> deleteRecord(ContentResolver resolvedor, int id, boolean ejecutar) throws Exception {

		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Curso/" + Integer.toString(id));

		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		ops.add(ContentProviderOperation.newDelete(myUri).build());

		if (ejecutar) resolvedor.delete(myUri, null, null);

		return ops;
	}

	public static ArrayList<Curso> getRecords(ContentResolver resolvedor
			//, String grid_currentQuery
											  ) {
		Uri myUri = Contrato.Curso.CONTENT_URI;
		return getRecords(resolvedor, myUri
				//, grid_currentQuery
				         );
	}

	private static ArrayList<Curso> getRecords(ContentResolver resolvedor, Uri myUri
			//, String grid_currentQuery
											  ){

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Curso._ID,
										  Contrato.Curso.ANNO,
										  Contrato.Curso.TURNO,
										  Contrato.Curso.FK_CICLO
										};

		String selection = null;
		//if (grid_currentQuery != null)
		//	selection = Contrato.Curso.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";
		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
			selection, // WHERE clause; which rows to return(all rows)
			null, // WHERE clause selection arguments (none)
			null // Order-by clause (ascending by name)
				);
		
		ArrayList<Curso> registros = new ArrayList<Curso>();
		if (cur.moveToFirst()) {
			
			do {
			// Get the field values
				Curso registro = new Curso();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Curso._ID)));
				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Curso._ID)));
				registro.setAnno(cur.getInt(cur.getColumnIndex(Contrato.Curso.ANNO)));
				registro.setTurno(Turno.valueOf(cur.getString(cur.getColumnIndex(Contrato.Curso.TURNO))));

				Ciclo ciclo = CicloProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.Curso.FK_CICLO)));
				registro.setCiclo(ciclo);
				
				registros.add(registro);
				
			} while (cur.moveToNext());
		}
		
		cur.close();
		return registros;
	}
	
}
