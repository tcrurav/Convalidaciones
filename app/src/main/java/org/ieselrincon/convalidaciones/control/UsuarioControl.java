package org.ieselrincon.convalidaciones.control;

import android.content.ContentResolver;
import android.net.Uri;

import org.ieselrincon.convalidaciones.model.pojos.Usuario;
import org.ieselrincon.convalidaciones.model.proveedor.Contrato;
import org.ieselrincon.convalidaciones.model.proveedor.UsuarioProveedor;

import java.util.ArrayList;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class UsuarioControl {
	
	//private static final String LOGTAG = "Tiburcio - UsuarioControl";

	public static Usuario login(ContentResolver resolvedor, String email, String contrasena) {
		Uri myUri = Contrato.Usuario.CONTENT_URI;
		String selection = Contrato.Usuario.EMAIL + " = '" + email + "' and " +
				Contrato.Usuario.CONTRASENA + " = '" + contrasena + "'";
		ArrayList<Usuario> registros = UsuarioProveedor.getRecords(resolvedor, myUri, selection);
		int numUsuarios = registros.size();
		if(numUsuarios==1){
			return registros.get(0);
		}
		return null;
	}
	
}
