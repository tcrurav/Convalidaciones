package org.ieselrincon.convalidaciones.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.Estudio;
import org.ieselrincon.convalidaciones.model.proveedor.EstudioProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.Contrato;

import java.util.ArrayList;
import java.util.HashMap;

public class EstudioListFragment extends ListFragment
		implements LoaderManager.LoaderCallbacks<Cursor> {
	
	//private static final String LOGTAG = "Tiburcio - EstudioListFragment";
	int mNum;
	int mFuncion;
	
	//static ArrayList<Estudio> mRowItems;
	static Estudio mEstudioSeleccionado;

	EstudioCursorAdapter mAdapter;
	LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	//static int posicionListView;
	
	static int tipoUsuarioLogin;
	//int empleadoIdLogin;

	static Activity contexto;
	
	OnEstudioSelectedListener mCallbackSelected;

	public interface OnEstudioSelectedListener {
        public void onEstudioSelected(int position);
    }
	
	public void setListenerSelected(OnEstudioSelectedListener listener) {
        mCallbackSelected = listener;
    }
	
	OnEstudioAceptadoListener mCallbackAceptado = new OnEstudioAceptadoListener() {
		@Override
		public void onEstudioAceptado(HashMap<Integer,Integer> mapa){
			
		}
	};
	
	public interface OnEstudioAceptadoListener {
        public void onEstudioAceptado(HashMap<Integer, Integer> mapa);
    }
	
	public void setListenerAceptado(OnEstudioAceptadoListener listener) {
        mCallbackAceptado= listener;
    }
	
	public static EstudioListFragment newInstance(int num, int funcion, int tipoUsuarioLogin) {
		//Log.i(LOGTAG, "newInstance");
		EstudioListFragment f = new EstudioListFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();

		args.putInt("num", num);
		args.putInt("funcion",funcion);
		args.putInt("tipoUsuarioLogin",tipoUsuarioLogin);
		
		f.setArguments(args);

		return f;
	}
	
	public static EstudioListFragment newInstance(int num, int funcion, int id, int tipoUsuarioLogin) {
		//Log.i(LOGTAG, "newInstance"); //id puede ser o bien promocionId ó bien empleadoId
		EstudioListFragment f = new EstudioListFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();

		args.putInt("num", num);
		args.putInt("funcion",funcion);
		args.putInt("id",id);
		args.putInt("tipoUsuarioLogin",tipoUsuarioLogin);
		
		f.setArguments(args);

		return f;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.i(LOGTAG, "onCreate"+mFuncion);

		mNum = getArguments().getInt("num");
		mFuncion = getArguments().getInt("funcion");
		tipoUsuarioLogin = getArguments().getInt("tipoUsuarioLogin");

		contexto = getActivity();
		
		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
			default:
				//Todo el mundo debe poder buscar (hacer el search)
				//if (G.permisos[G.PERMISO_CENTROS_CUD_NUMERO][tipoUsuarioLogin-1]){
					setHasOptionsMenu(true);
				//} else {
				//	setHasOptionsMenu(false);
				//}
				break;
		}

	}

	private String grid_currentQuery = null; // holds the current query...

	final private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {

		@Override
		public boolean onQueryTextChange(String newText) {
			if (TextUtils.isEmpty(newText)) {
				if(mFuncion != G.OPERACION_SELECCIONAR) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Listado Completo");
				}
				grid_currentQuery = null;
			} else {
				if(mFuncion != G.OPERACION_SELECCIONAR) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("filtrado por: " + newText);
				}
				grid_currentQuery = newText;
			}
			getLoaderManager().restartLoader(0, null, EstudioListFragment.this);
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			Toast.makeText(getActivity(), "buscando por: " + query + "...", Toast.LENGTH_SHORT).show();
			return false;
		}
	};

	@Override
	//public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu, inflater);
		//Log.i(LOGTAG, "onCreateOptionsMenu"+mFuncion);
		
	/*	SearchView searchView =
				(SearchView)item.getActionView();
		SearchManager searchManager =
				(SearchManager)getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo info =
				searchManager.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(info);
	*/
	/*	SearchView searchView = (SearchView)menu.findItem(R.id.grid_default_search).getActionView();
		searchView.setOnQueryTextListener(queryListener);
*/
		// Para eliminar la selección en el subtítulo cuando se hace un search en GrupoCentrosClienteListFragment.


		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(getResources().getString(R.string.seleccione_el_estudio));
				break;
			default:
				// Para eliminar la selección en el subtítulo cuando se hace un search en GrupoCentrosClienteListFragment.
				((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);

				MenuItem item = menu.add("Search");
				item.setIcon(android.R.drawable.ic_menu_search);
				item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
						| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
				SearchView sv = new SearchView(getActivity());
				sv.setOnQueryTextListener(queryListener);
				item.setActionView(sv);

				int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
				TextView textView = (TextView) sv.findViewById(id);
				textView.setTextColor(Color.WHITE);

				if (TextUtils.isEmpty(grid_currentQuery)) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Listado Completo");
				} else {
					((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("filtrado por: " + grid_currentQuery);
				}

				if (tipoUsuarioLogin == G.ADMINISTRADOR){
					menu.add(0,G.INSERTAR, Menu.NONE,"INSERTAR")
						.setIcon(R.mipmap.ic_action_nuevo_registro)
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
				}
				break;
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case G.INSERTAR:
				if (tipoUsuarioLogin == G.ADMINISTRADOR){
					Intent actInsertar = new Intent(getActivity(), EstudioDetalleActivity.class);
					actInsertar.putExtra("ID", G.SIN_VALOR_INT);
					actInsertar.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
					startActivityForResult(actInsertar,G.INSERTAR);
					return true;
				}
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * The Fragment's UI is just a simple text view showing its
	 * instance number.
	 */
	
	@SuppressLint("ResourceAsColor")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//Log.i(LOGTAG, "onCreateView");
		View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
		SearchView sv = (SearchView) v.findViewById(R.id.searchViewDialog);

		//TextView tv = (TextView) v.findViewById(R.id.text);
		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				//tv.setText(R.string.seleccione_el_estudio);
				//tv.setVisibility(TextView.VISIBLE);
				sv.setQueryHint("Buscar...");
				sv.setOnQueryTextListener(queryListener);
				sv.setIconifiedByDefault(false);
				sv.setVisibility(SearchView.VISIBLE);
				break;
			default:
				break;
		}

		mAdapter = new EstudioCursorAdapter(getActivity());
		setListAdapter(mAdapter);

		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Log.i(LOGTAG, "onActivityCreated");

		mCallbacks = this;

		getLoaderManager().initLoader(0, null, mCallbacks);

		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				break;
			default:
				getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
												   int arg2, long arg3) {
	    	
						//posicionListView = arg2;
						mEstudioSeleccionado = EstudioProveedor.getRecord(contexto.getContentResolver(),(int)arg1.getTag());
						if (tipoUsuarioLogin == G.ADMINISTRADOR){
							DialogFragment newFragment = new DialogoVerActualizarBorrar();
							newFragment.show(getFragmentManager(), "dialog");
						} else {
							//showDialogVer();
							Intent actVer = new Intent(getActivity(), EstudioDetalleActivity.class);
							actVer.putExtra("ID", mEstudioSeleccionado.getID());
							actVer.putExtra("Modo", G.VER);
							actVer.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
							startActivityForResult(actVer,G.VER);
						}
						return true;
					}
				});
				break;
		
		}
	}

	public static class DialogoVerActualizarBorrar extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Estudio '"+mEstudioSeleccionado.getNombre()+"'")
    			//.setIcon(R.drawable.alert_dialog_icon)
    			.setItems(R.array.edicion, new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {
    					// The 'which' argument contains the index position
    					// of the selected item      	            	   
    					switch (which){
    						case G.DIALOGO_VER:
    							//showDialogVer();
								Intent actVer = new Intent(getActivity(), EstudioDetalleActivity.class);
								actVer.putExtra("ID", mEstudioSeleccionado.getID());
								actVer.putExtra("Modo", G.VER);
								actVer.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
								startActivityForResult(actVer,G.VER);
    							break;
    						case G.DIALOGO_ACTUALIZAR:
    							//showDialogActualizar();
								Intent actActualizar = new Intent(getActivity(), EstudioDetalleActivity.class);
								actActualizar.putExtra("ID", mEstudioSeleccionado.getID());
								actActualizar.putExtra("Modo", G.ACTUALIZAR);
								actActualizar.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
								startActivityForResult(actActualizar,G.ACTUALIZAR);
    							break;
    						case G.DIALOGO_BORRAR:
								//showDialogoBorrar();
								DialogFragment newFragment = new DialogoConfirmarBorrado();
								newFragment.show(getFragmentManager(), "dialog");
    							break;
    					}
    				}
    			});
			return builder.create();
		}
	}

	/*public void showDialogBorrar() {
		DialogFragment newFragment = new DialogoConfirmarBorrado();
		newFragment.show(getFragmentManager(), "dialog");
	}*/
	

	public static class DialogoConfirmarBorrado extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Va a borrar el registro del Estudio '"+mEstudioSeleccionado.getNombre()+"'")
				//.setIcon(R.drawable.alert_dialog_icon)
				.setTitle("¡ATENCIÓN!")
				.setPositiveButton(R.string.borrar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Se borra
						//borrarEstudio();
						new BorrarEstudioTask().execute();
					}
				})
				.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//No se borra
					}
				});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}

	/*public void showDialogVer(){
		Intent actVer = new Intent(getActivity(), EstudioDetalleActivity.class);
		actVer.putExtra("ID", mRowItems.get(posicionListView).getID());
		actVer.putExtra("Modo", G.VER);
		actVer.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
		startActivityForResult(actVer,G.VER);
	}*/
	
	/*public void showDialogActualizar(){
		Intent actActualizar = new Intent(getActivity(), EstudioDetalleActivity.class);
		actActualizar.putExtra("ID", mRowItems.get(posicionListView).getID());
		actActualizar.putExtra("Modo", G.ACTUALIZAR);
		actActualizar.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
		startActivityForResult(actActualizar,G.ACTUALIZAR);
	}*/

	/*public void showDialogContinuar() {
		DialogFragment newFragment = new DialogoContinuar();
		newFragment.show(getFragmentManager(), "dialog"); 
	}*/

	public static class DialogoContinuar extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
			builder.setMessage("No se ha podido borrar el Estudio '"+mEstudioSeleccionado.getNombre())
				//.setIcon(R.drawable.alert_dialog_icon)
				.setTitle("¡ATENCIÓN!")
				.setPositiveButton(R.string.action_continuar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Se continua
					}
				});
			// Create the AlertDialog object and return it
			return builder.create();
		}

	}

	/*private void borrarEstudio() {
		new BorrarEstudioTask().execute();	
	}*/

	public static class BorrarEstudioTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected void onPreExecute()
		{
			//hideKeyboard();
			//showProgress(true);
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			//showProgress(true);
		
			try {
				EstudioProveedor.deleteRecord(contexto.getContentResolver(),mEstudioSeleccionado.getID(),true);
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		@Override
		protected void onPostExecute(final Boolean success) {
			//showProgress(false);

			//mBorrarEstudioTask = null;
		
			if (success) {
				//mAdapter.notifyDataSetChanged();
				//Toast.makeText(this, "El registro se borró satisfactoriamente", Toast.LENGTH_SHORT).show();
			} else {
				//NO SE borraron LOS DATOS
				//showDialogContinuar();
				//DialogFragment newFragment = new DialogoContinuar();
				//newFragment.show(contexto.getFragmentManager(), "dialog");
			}
		}
		@Override
		protected void onCancelled() {
			//mBorrarEstudioTask = null;
			//showProgress(false);
		}
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		//Log.i(LOGTAG, "Item clicked: " + id+" y position es "+position);
		
		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				//Log.i(LOGTAG,"y el ID es "+mRowItems.get(position).getID());
				mCallbackSelected.onEstudioSelected((int) v.getTag());
				break;
			default:
				break;
		}
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.  This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		String columns[] = new String[] { Contrato.Estudio._ID,
										  Contrato.Estudio.NOMBRE,
										  Contrato.Estudio.CODIGO
										};

		Uri baseUri = Contrato.Estudio.CONTENT_URI;

		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				baseUri = Contrato.Estudio.CONTENT_URI;
				break;
			default:
				baseUri = Contrato.Estudio.CONTENT_URI;
				break;
		}
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.

		String selection = null;
		if (grid_currentQuery != null)
			selection = Contrato.Estudio.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";
		return new CursorLoader(getActivity(), baseUri,
				columns, selection, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)
		
		
		//switch(mFuncion){
		//	case G.OPERACION_SELECCIONAR:
		//		mRowItems = EstudioProveedor.getRecords(getActivity().getContentResolver(), grid_currentQuery);
		//		break;
		//	default:
		//		mRowItems = EstudioProveedor.getRecords(getActivity().getContentResolver(), grid_currentQuery);
		//		break;
		//}
		
		Uri laUriBase = Uri.parse("content://"+Contrato.AUTHORITY+"/Estudio");
		data.setNotificationUri(getActivity().getContentResolver(), laUriBase);
		
		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed.  We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}

	public class EstudioCursorAdapter extends CursorAdapter {
		public EstudioCursorAdapter(Context context) {
			super(context, null, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			int ID = cursor.getInt(cursor.getColumnIndex(Contrato.Estudio._ID));
			String nombre = cursor.getString(cursor.getColumnIndex(Contrato.Estudio.NOMBRE));
			String codigo = cursor.getString(cursor.getColumnIndex(Contrato.Estudio.CODIGO));
	
			TextView textviewNombre = (TextView) view.findViewById(R.id.textview_estudio_list_item_nombre);
			textviewNombre.setText(nombre);
			TextView textviewCodigo = (TextView) view.findViewById(R.id.textview_estudio_list_item_codigo);
			textviewCodigo.setText(codigo);
			if(codigo.equals("0") || codigo.equals("")){
				textviewCodigo.setVisibility(TextView.GONE);
			} else {
				textviewCodigo.setVisibility(TextView.VISIBLE);
			}

			ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
			String nombreCorto = nombre.substring(0,3).toUpperCase();
			int color = generator.getColor(nombre); //Genera un color según el nombre
			TextDrawable drawable = TextDrawable.builder()
					.beginConfig()
					//.textColor(Color.BLACK)
					//.useFont(Typeface.DEFAULT)
					.fontSize(50) /* size in px */
					//.bold()
					//.toUpperCase()
					.endConfig()
					.buildRound(nombreCorto, color);

			ImageView image = (ImageView) view.findViewById(R.id.image_view);
			image.setImageDrawable(drawable);

			view.setTag(ID);

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.estudio_list_item, parent, false);
			bindView(v, context, cursor);
			return v;
		}
	}
}
