package org.ieselrincon.convalidaciones.view;

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
import android.support.v4.app.ActivityCompat;
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
import org.ieselrincon.convalidaciones.model.pojos.Ciclo;
import org.ieselrincon.convalidaciones.model.proveedor.CicloProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.Contrato;

import java.util.ArrayList;
import java.util.HashMap;

public class CicloListFragment extends ListFragment
		implements LoaderManager.LoaderCallbacks<Cursor> {
	
	//private static final String LOGTAG = "Tiburcio - CicloListFragment";
	int mNum;
	int mFuncion;
	
	//static ArrayList<Ciclo> mRowItems;
	int mIdCicloSeleccionado;
	static Ciclo mCicloSeleccionado;


	CicloCursorAdapter mAdapter;
	LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	//static int posicionListView;
	
	static int tipoUsuarioLogin;
	//int empleadoIdLogin;

	static Activity contexto;
	
	OnCicloSelectedListener mCallbackSelected = new OnCicloSelectedListener() {
		@Override
		public void onCicloSelected(int position){
			
		}
	};
	
	public interface OnCicloSelectedListener {
        public void onCicloSelected(int position);
    }
	
	public void setListenerSelected(OnCicloSelectedListener listener) {
        mCallbackSelected = listener;
    }
	
	OnCicloAceptadoListener mCallbackAceptado = new OnCicloAceptadoListener() {
		@Override
		public void onCicloAceptado(HashMap<Integer,Integer> mapa){
			
		}
	};
	
	public interface OnCicloAceptadoListener {
        public void onCicloAceptado(HashMap<Integer, Integer> mapa);
    }
	
	public void setListenerAceptado(OnCicloAceptadoListener listener) {
        mCallbackAceptado= listener;
    }
	
	public static CicloListFragment newInstance(int num, int funcion, int tipoUsuarioLogin) {
		//Log.i(LOGTAG, "newInstance");
		CicloListFragment f = new CicloListFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();

		args.putInt("num", num);
		args.putInt("funcion",funcion);
		args.putInt("tipoUsuarioLogin",tipoUsuarioLogin);
		
		f.setArguments(args);

		return f;
	}
	
	public static CicloListFragment newInstance(int num, int funcion, int id, int tipoUsuarioLogin) {
		//Log.i(LOGTAG, "newInstance"); //id puede ser o bien promocionId ó bien empleadoId
		CicloListFragment f = new CicloListFragment();

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
				setHasOptionsMenu(false);
				break;
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
				((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Listado Completo");
				grid_currentQuery = null;
			} else {
				((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("filtrado por: " + newText);
				grid_currentQuery = newText;
			}
			getLoaderManager().restartLoader(0, null, CicloListFragment.this);
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
		((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);

		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				break;
			default:
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
					Intent actInsertar = new Intent(getActivity(), CicloDetalleActivity.class);
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//Log.i(LOGTAG, "onCreateView");
		View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
		SearchView sv = (SearchView) v.findViewById(R.id.searchViewDialog);

		TextView tv = (TextView) v.findViewById(R.id.text);
		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				tv.setText(R.string.seleccione_el_ciclo);
				tv.setVisibility(TextView.VISIBLE);
				sv.setQueryHint("Buscar...");
				sv.setOnQueryTextListener(queryListener);
				sv.setIconifiedByDefault(false);
				sv.setVisibility(SearchView.VISIBLE);
				break;
			default:
				break;
		}

		mAdapter = new CicloCursorAdapter(getActivity());
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
						mCicloSeleccionado = CicloProveedor.getRecord(getActivity().getContentResolver(),(int) arg1.getTag());
						if (tipoUsuarioLogin == G.ADMINISTRADOR){
							DialogFragment newFragment = new DialogoVerActualizarBorrar();
							newFragment.show(getFragmentManager(), "dialog");
						} else {
							showDialogVer();
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
			AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
			builder.setTitle("Ciclo '"+mCicloSeleccionado.getNombre()+"'")
    			//.setIcon(R.drawable.alert_dialog_icon)
    			.setItems(R.array.edicion, new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {
    					// The 'which' argument contains the index position
    					// of the selected item      	            	   
    					switch (which){
    						case G.DIALOGO_VER:
    							showDialogVer();
    							break;
    						case G.DIALOGO_ACTUALIZAR:
    							showDialogActualizar();
    							break;
    						case G.DIALOGO_BORRAR:
    							//showDialogBorrar();
								DialogFragment newFragment = new DialogoConfirmarBorrado();
								newFragment.show(getFragmentManager(), "dialog");
    							break;
    					}
    				}
    			});
			return builder.create();
		}
	}

	/*public static void showDialogBorrar() {
		DialogFragment newFragment = new DialogoConfirmarBorrado();
		newFragment.show(getFragmentManager(), "dialog");
	}*/

	public static class DialogoConfirmarBorrado extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
			builder.setMessage("Va a borrar el registro del Ciclo '"+mCicloSeleccionado.getNombre()+"'")
				//.setIcon(R.drawable.alert_dialog_icon)
				.setTitle("¡ATENCIÓN!")
				.setPositiveButton(R.string.borrar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Se borra
						borrarCiclo();
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

	public static void showDialogVer(){
		Intent actVer = new Intent(contexto, CicloDetalleActivity.class);
		actVer.putExtra("ID", mCicloSeleccionado.getID());
		actVer.putExtra("Modo", G.VER);
		actVer.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
		contexto.startActivityForResult(actVer,G.VER);
	}
	
	public static void showDialogActualizar(){
		Intent actActualizar = new Intent(contexto, CicloDetalleActivity.class);
		actActualizar.putExtra("ID", mCicloSeleccionado.getID());
		actActualizar.putExtra("Modo", G.ACTUALIZAR);
		actActualizar.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
		contexto.startActivityForResult(actActualizar,G.ACTUALIZAR);
	}

	/*public static void showDialogContinuar() {
		DialogFragment newFragment = new DialogoContinuar();
		newFragment.show(contexto.getFragmentManager(), "dialog");
	}*/

	public static class DialogoContinuar extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("No se ha podido borrar el Ciclo '"+mCicloSeleccionado.getNombre()+
					"'.")
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

	private static void borrarCiclo() {
		new BorrarCicloTask().execute();	
	}

	public static class BorrarCicloTask extends AsyncTask<Void, Void, Boolean> {
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
				CicloProveedor.deleteRecord(contexto.getContentResolver(),mCicloSeleccionado.getID(),true);
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

			//mBorrarCicloTask = null;
		
			if (success) {
				//mAdapter.notifyDataSetChanged();
				//Toast.makeText(this, "El registro se borró satisfactoriamente", Toast.LENGTH_SHORT).show();
			} else {
				//NO SE borraron LOS DATOS
				//showDialogContinuar();
				//DialogFragment newFragment = new DialogoContinuar();
				//newFragment.show(getFragmentManager(), "dialog");

			}
		}
		@Override
		protected void onCancelled() {
			//mBorrarCicloTask = null;
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
				mCallbackSelected.onCicloSelected((int) v.getTag());
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
		String columns[] = new String[] { Contrato.Ciclo._ID,
										  Contrato.Ciclo.NOMBRE,
				                          Contrato.Ciclo.ABREVIATURA
										};

		Uri baseUri = Contrato.Ciclo.CONTENT_URI;

		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				baseUri = Contrato.Ciclo.CONTENT_URI;
				break;
			default:
				baseUri = Contrato.Ciclo.CONTENT_URI;
				break;
		}
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.

		String selection = null;
		if (grid_currentQuery != null)
			selection = Contrato.Ciclo.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";
		return new CursorLoader(getActivity(), baseUri,
				columns, selection, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)
		
		
		//switch(mFuncion){
		//	case G.OPERACION_SELECCIONAR:
		//		mRowItems = CicloProveedor.getRecords(getActivity().getContentResolver(), grid_currentQuery);
		//		break;
		//	default:
		//		mRowItems = CicloProveedor.getRecords(getActivity().getContentResolver(), grid_currentQuery);
		//		break;
		//}
		
		Uri laUriBase = Uri.parse("content://"+Contrato.AUTHORITY+"/Ciclo");
		data.setNotificationUri(getActivity().getContentResolver(), laUriBase);
		
		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed.  We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}

	public class CicloCursorAdapter extends CursorAdapter {
		public CicloCursorAdapter(Context context) {
			super(context, null, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			int ID = cursor.getInt(cursor.getColumnIndex(Contrato.Ciclo._ID));
			String nombre = cursor.getString(cursor.getColumnIndex(Contrato.Ciclo.NOMBRE));
			String abreviatura = cursor.getString(cursor.getColumnIndex(Contrato.Ciclo.ABREVIATURA));
	
			TextView textviewNombre = (TextView) view.findViewById(R.id.textview_ciclo_list_item_nombre);
			textviewNombre.setText(nombre);

			TextView textviewAbreviatura = (TextView) view.findViewById(R.id.textview_ciclo_list_item_abreviatura);
			textviewAbreviatura.setText(abreviatura);

			ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
			int color = generator.getColor(abreviatura); //Genera un color según el nombre
			TextDrawable drawable = TextDrawable.builder()
					.buildRound(abreviatura.substring(0,1), color);

			ImageView image = (ImageView) view.findViewById(R.id.image_view);
			image.setImageDrawable(drawable);

			view.setTag(ID);

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.ciclo_list_item, parent, false);
			bindView(v, context, cursor);
			return v;
		}
	}
}
