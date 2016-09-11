package org.ieselrincon.convalidaciones.model.proveedor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.TipoUsuario;
import org.ieselrincon.convalidaciones.model.pojos.Usuario;

import java.util.ArrayList;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class UsuarioProveedor {
	
	private static final String LOGTAG = "Tiburcio - UsuarioProveedor";
	
	public static ArrayList<ContentProviderOperation> updateRecord(ContentResolver resolvedor, Usuario registro,
																   boolean ejecutar) {
		
		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Usuario/" + Integer.toString(registro.getID()));
		
		ContentValues values = new ContentValues();
		values.put(Contrato.Usuario.NOMBRE, registro.getNombre());
		values.put(Contrato.Usuario.APELLIDOS, registro.getApellidos());
		values.put(Contrato.Usuario.CONTRASENA, registro.getContrasena());
		values.put(Contrato.Usuario.EMAIL, registro.getEmail());
		values.put(Contrato.Usuario.DNI, registro.getDni());
		values.put(Contrato.Usuario.TELEFONO, registro.getTelefono());
		values.put(Contrato.Usuario.TIPOUSUARIO, registro.getTipoUsuario().name());

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newUpdate(myUri).withValues(values).build());

		if (ejecutar) resolvedor.update(myUri, values, null, null);

		return ops;
	}

	public static int insertRecord(ContentResolver resolvedor, Usuario registro,
								   ArrayList<ContentProviderOperation> ops, boolean ejecutar) throws Exception {
		
		ContentValues values = new ContentValues();
		if(registro.getID()!= G.SIN_VALOR_INT) values.put(Contrato.Usuario._ID, registro.getID());
		values.put(Contrato.Usuario.NOMBRE, registro.getNombre());
		values.put(Contrato.Usuario.APELLIDOS, registro.getApellidos());
		values.put(Contrato.Usuario.CONTRASENA, registro.getContrasena());
		values.put(Contrato.Usuario.EMAIL, registro.getEmail());
		values.put(Contrato.Usuario.DNI, registro.getDni());
		values.put(Contrato.Usuario.TELEFONO, registro.getTelefono());
		values.put(Contrato.Usuario.TIPOUSUARIO, registro.getTipoUsuario().name());

		if (ejecutar) {
			Uri uri = resolvedor.insert(Contrato.Usuario.CONTENT_URI, values);
			return ((int) ContentUris.parseId(uri));
		}
		else ops.add(ContentProviderOperation.newInsert(Contrato.Usuario.CONTENT_URI).withValues(values).build());

		return 0;
	}

	public static Usuario getRecord(ContentResolver resolvedor, int id) {	

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Usuario._ID,
										  Contrato.Usuario.NOMBRE,
										  Contrato.Usuario.APELLIDOS,
				                          Contrato.Usuario.CONTRASENA,
				                          Contrato.Usuario.EMAIL,
				                          Contrato.Usuario.DNI,
										  Contrato.Usuario.TELEFONO,
										  Contrato.Usuario.TIPOUSUARIO
										};
		
		Uri myUri = Uri.parse("content://"+ Contrato.AUTHORITY + "/Usuario/" + Integer.toString(id));

		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				null // Order-by clause (ascending by name)
		);
		
		if (cur.moveToFirst()) {

			// Get the field values
				Usuario registro = new Usuario();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Usuario._ID)));
				registro.setNombre(cur.getString(cur.getColumnIndex(Contrato.Usuario.NOMBRE)));
			    registro.setApellidos(cur.getString(cur.getColumnIndex(Contrato.Usuario.APELLIDOS)));
			    registro.setContrasena(cur.getString(cur.getColumnIndex(Contrato.Usuario.CONTRASENA)));
			    registro.setEmail(cur.getString(cur.getColumnIndex(Contrato.Usuario.EMAIL)));
			    registro.setDni(cur.getString(cur.getColumnIndex(Contrato.Usuario.DNI)));
			    registro.setTelefono(cur.getString(cur.getColumnIndex(Contrato.Usuario.TELEFONO)));
				registro.setTipoUsuario(TipoUsuario.valueOf(cur.getString(cur.getColumnIndex(Contrato.Usuario.TIPOUSUARIO))));

				cur.close();
				return registro;
		}
		cur.close();
		return null;
	}
	
	public static ArrayList<ContentProviderOperation> deleteRecord(ContentResolver resolvedor, int id, boolean ejecutar) throws Exception {

		Uri myUri = Uri.parse("content://" + Contrato.AUTHORITY + "/Usuario/" + Integer.toString(id));

		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		ops.add(ContentProviderOperation.newDelete(myUri).build());

		if (ejecutar) resolvedor.delete(myUri, null, null);

		return ops;
	}

	public static ArrayList<Usuario> getRecords(ContentResolver resolvedor, Uri myUri, String selection) {

		// An array specifying which columns to return.
		String columns[] = new String[] { Contrato.Usuario._ID,
										  Contrato.Usuario.NOMBRE,
										  Contrato.Usuario.APELLIDOS,
										  Contrato.Usuario.CONTRASENA,
										  Contrato.Usuario.EMAIL,
										  Contrato.Usuario.DNI,
										  Contrato.Usuario.TELEFONO,
										  Contrato.Usuario.TIPOUSUARIO
										};


		Cursor cur = resolvedor.query(myUri, columns, // Which columns to return
			selection, // WHERE clause; which rows to return(all rows)
			null, // WHERE clause selection arguments (none)
			null // Order-by clause (ascending by name)
				);
		
		ArrayList<Usuario> registros = new ArrayList<Usuario>();
		if (cur.moveToFirst()) {
			
			do {
			// Get the field values
				Usuario registro = new Usuario();

				registro.setID(cur.getInt(cur.getColumnIndex(Contrato.Usuario._ID)));
				registro.setNombre(cur.getString(cur.getColumnIndex(Contrato.Usuario.NOMBRE)));
				registro.setApellidos(cur.getString(cur.getColumnIndex(Contrato.Usuario.APELLIDOS)));
				registro.setContrasena(cur.getString(cur.getColumnIndex(Contrato.Usuario.CONTRASENA)));
				registro.setEmail(cur.getString(cur.getColumnIndex(Contrato.Usuario.EMAIL)));
				registro.setDni(cur.getString(cur.getColumnIndex(Contrato.Usuario.DNI)));
				registro.setTelefono(cur.getString(cur.getColumnIndex(Contrato.Usuario.TELEFONO)));
				registro.setTipoUsuario(TipoUsuario.valueOf(cur.getString(cur.getColumnIndex(Contrato.Usuario.TIPOUSUARIO))));
				
				registros.add(registro);
				
			} while (cur.moveToNext());
		}
		
		cur.close();
		return registros;
	}
	
}
