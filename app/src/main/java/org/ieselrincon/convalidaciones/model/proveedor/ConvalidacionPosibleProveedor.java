package org.ieselrincon.convalidaciones.model.proveedor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionPosible;
import org.ieselrincon.convalidaciones.model.pojos.Curso;
import org.ieselrincon.convalidaciones.model.pojos.Estudio;

import java.util.ArrayList;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class ConvalidacionPosibleProveedor {
	
	//private static final String LOGTAG = "Tiburcio - ConvalidacionPosibleProveedor";
	
	public static ArrayList<ContentProviderOperation> updateRecord(ContentResolver resolvedor, ConvalidacionPosible registro,
																   boolean ejecutar) {
		
		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/ConvalidacionPosible/" + Integer.toString(registro.getID()));
		
		ContentValues values = new ContentValues();
		values.put(Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO, registro.getEstudioConvalidado().getID());
		values.put(Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO, registro.getEstudioAportado().getID());
		values.put(Contrato.ConvalidacionPosible.FK_CURSO, registro.getCurso().getID());

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newUpdate(myUri).withValues(values).build());

		if (ejecutar) resolvedor.update(myUri, values, null, null);

		return ops;
	}

	public static void insertRecord(ContentResolver resolvedor, ConvalidacionPosible registro,
								   ArrayList<ContentProviderOperation> ops, boolean ejecutar) throws Exception {
		
		ContentValues values = new ContentValues();
		if(registro.getID()!= G.SIN_VALOR_INT) values.put(Contrato.ConvalidacionPosible._ID, registro.getID());
		values.put(Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO, registro.getEstudioConvalidado().getID());
		values.put(Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO, registro.getEstudioAportado().getID());
		values.put(Contrato.ConvalidacionPosible.FK_CURSO, registro.getCurso().getID());

		if (ejecutar) resolvedor.insert(Contrato.ConvalidacionPosible.CONTENT_URI, values);
		else ops.add(ContentProviderOperation.newInsert(Contrato.ConvalidacionPosible.CONTENT_URI).withValues(values).build());

	}

	public static ConvalidacionPosible getRecord(ContentResolver resolvedor, int id) {

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.ConvalidacionPosible._ID,
										  Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO,
										  Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO,
										  Contrato.ConvalidacionPosible.FK_CURSO
										};
		
		Uri myUri = Uri.parse("content://"+ Contrato.AUTHORITY + "/ConvalidacionPosible/" + Integer.toString(id));

		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				null // Order-by clause (ascending by name)
		);
		
		if (cur.moveToFirst()) {

			// Get the field values
				ConvalidacionPosible registro = new ConvalidacionPosible();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosible._ID)));

				Estudio estudioConvalidado = EstudioProveedor.getRecord(resolvedor,
						cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO)));
				registro.setEstudioConvalidado(estudioConvalidado);

				Estudio estudioAportado = EstudioProveedor.getRecord(resolvedor,
						cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO)));
				registro.setEstudioAportado(estudioAportado);

				Curso curso = CursoProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosible.FK_CURSO)));
				registro.setCurso(curso);

				cur.close();
				return registro;
		}
		cur.close();
		return null;
	}
	
	public static ArrayList<ContentProviderOperation> deleteRecord(ContentResolver resolvedor, int id, boolean ejecutar) throws Exception {

		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/ConvalidacionPosible/" + Integer.toString(id));

		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		ops.add(ContentProviderOperation.newDelete(myUri).build());

		if (ejecutar) resolvedor.delete(myUri, null, null);

		return ops;
	}

	public static ArrayList<ConvalidacionPosible> getRecords(ContentResolver resolvedor
																	  //, String grid_currentQuery
											  ) {
		Uri myUri = Contrato.ConvalidacionPosible.CONTENT_URI;
		return getRecords(resolvedor, myUri
				//, grid_currentQuery
				         );
	}

	private static ArrayList<ConvalidacionPosible> getRecords(ContentResolver resolvedor, Uri myUri
																	   //, String grid_currentQuery
											  ){

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.ConvalidacionPosible._ID,
											Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO,
											Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO,
											Contrato.ConvalidacionPosible.FK_CURSO
										};

		String selection = null;
		//if (grid_currentQuery != null)
		//	selection = Contrato.ConvalidacionPosibleProveedor.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";
		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
			selection, // WHERE clause; which rows to return(all rows)
			null, // WHERE clause selection arguments (none)
			null // Order-by clause (ascending by name)
				);
		
		ArrayList<ConvalidacionPosible> registros = new ArrayList<>();
		if (cur.moveToFirst()) {
			
			do {
			// Get the field values
				ConvalidacionPosible registro = new ConvalidacionPosible();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosible._ID)));

				Estudio estudioConvalidado = EstudioProveedor.getRecord(resolvedor,
						cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO)));
				registro.setEstudioConvalidado(estudioConvalidado);

				Estudio estudioAportado = EstudioProveedor.getRecord(resolvedor,
						cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO)));
				registro.setEstudioAportado(estudioAportado);

				Curso curso = CursoProveedor.getRecord(resolvedor, cur.getInt(cur.getColumnIndex(Contrato.ConvalidacionPosible.FK_CURSO)));
				registro.setCurso(curso);
				
				registros.add(registro);
				
			} while (cur.moveToNext());
		}
		
		cur.close();
		return registros;
	}
	
}
