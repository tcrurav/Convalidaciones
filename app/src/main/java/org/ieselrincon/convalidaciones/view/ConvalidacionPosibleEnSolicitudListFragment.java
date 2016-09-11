package org.ieselrincon.convalidaciones.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.anton46.stepsview.StepsView;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionPosible;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionPosibleEnSolicitud;
import org.ieselrincon.convalidaciones.model.proveedor.ConvalidacionPosibleEnSolicitudProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.Contrato;
import org.ieselrincon.convalidaciones.model.proveedor.ConvalidacionPosibleProveedor;

import java.util.ArrayList;
import java.util.HashMap;

public class ConvalidacionPosibleEnSolicitudListFragment extends ListFragment
		implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String LOGTAG = "Tiburcio - ConvalidacionPosibleEnSolicitudListFragment";
	int mNum;
	int mFuncion;
	//int mIdSolicitud;
	//Solicitud mSolicitudDeTrabajo;
	
	//static ArrayList<ConvalidacionPosibleEnSolicitud> mRowItems;

	static ConvalidacionPosibleEnSolicitud mConvalidacionPosibleEnSolicitudSeleccionado;

	ConvalidacionPosibleEnSolicitudCursorAdapter mAdapter;
	LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	//static int posicionListView;
	
	static int tipoUsuarioLogin;
	//int empleadoIdLogin;

	static Activity contexto;
	
	OnConvalidacionPosibleEnSolicitudSelectedListener mCallbackSelected = new OnConvalidacionPosibleEnSolicitudSelectedListener() {
		@Override
		public void onConvalidacionPosibleEnSolicitudSelected(int position){
			
		}
	};
	
	public interface OnConvalidacionPosibleEnSolicitudSelectedListener {
        public void onConvalidacionPosibleEnSolicitudSelected(int position);
    }
	
	public void setListenerSelected(OnConvalidacionPosibleEnSolicitudSelectedListener listener) {
        mCallbackSelected = listener;
    }
	
	OnConvalidacionPosibleEnSolicitudAceptadoListener mCallbackAceptado = new OnConvalidacionPosibleEnSolicitudAceptadoListener() {
		@Override
		public void onConvalidacionPosibleEnSolicitudAceptado(HashMap<Integer,Integer> mapa){
			
		}
	};
	
	public interface OnConvalidacionPosibleEnSolicitudAceptadoListener {
        public void onConvalidacionPosibleEnSolicitudAceptado(HashMap<Integer, Integer> mapa);
    }
	
	public void setListenerAceptado(OnConvalidacionPosibleEnSolicitudAceptadoListener listener) {
        mCallbackAceptado= listener;
    }

	public static ConvalidacionPosibleEnSolicitudListFragment newInstance(int num, int funcion//, int idSolicitud
																		  , int tipoUsuarioLogin) {
		ConvalidacionPosibleEnSolicitudListFragment f = new ConvalidacionPosibleEnSolicitudListFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();

		args.putInt("num", num);
		args.putInt("funcion",funcion);
		//args.putInt("idSolicitud",idSolicitud);
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
		//mIdSolicitud = getArguments().getInt("idSolicitud");
		tipoUsuarioLogin = getArguments().getInt("tipoUsuarioLogin");

		//if(mIdSolicitud != G.SIN_VALOR_INT) {
		//	mSolicitudDeTrabajo = SolicitudProveedor.getRecord(getActivity().getContentResolver(), mIdSolicitud);
		//} else {
		//	mSolicitudDeTrabajo = null;
		//}

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
			getLoaderManager().restartLoader(0, null, ConvalidacionPosibleEnSolicitudListFragment.this);
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
					Intent actInsertar = new Intent(getActivity(), ConvalidacionPosibleEnSolicitudSeleccionarActivity.class);
					actInsertar.putExtra("idCurso", ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getCurso().getID());
					startActivityForResult(actInsertar,G.INSERTAR);
					return true;
				}
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode){
			case G.SELECCIONADO_CURSO:
				if(data != null) { //Es decir si no viene después de que el usuario pulse el HomeUp
					ConvalidacionPosible convalidacionPosible = ConvalidacionPosibleProveedor.getRecord(getActivity().getContentResolver(),
							data.getIntExtra("SeleccionadoID", 0));

					ConvalidacionPosibleEnSolicitud convalidacionPosibleEnSolicitud = new ConvalidacionPosibleEnSolicitud();
					convalidacionPosibleEnSolicitud.setConvalidacionPosible(convalidacionPosible);
					Log.e(LOGTAG,String.valueOf(((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getID()));
					convalidacionPosibleEnSolicitud.setSolicitud(((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo);

					try {
						ConvalidacionPosibleEnSolicitudProveedor.insertRecord(getActivity().getContentResolver(), convalidacionPosibleEnSolicitud,
                                new ArrayList<ContentProviderOperation>(), true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
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

		TextView paso = (TextView) v.findViewById(R.id.textview_fragment_pager_list_paso);
		paso.setVisibility(TextView.VISIBLE);

		TextView tv = (TextView) v.findViewById(R.id.text);
		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				tv.setText(R.string.seleccione_la_convalidacionposible);
				tv.setVisibility(TextView.VISIBLE);
				sv.setQueryHint("Buscar...");
				sv.setOnQueryTextListener(queryListener);
				sv.setIconifiedByDefault(false);
				sv.setVisibility(SearchView.VISIBLE);
				break;
			default:
				/*FloatingActionButton mFab = (FloatingActionButton) v.findViewById(R.id.fab);
				mFab.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Snackbar snackbar = Snackbar
								.make(view, "¿Ha revisado los datos?", Snackbar.LENGTH_LONG)
								.setAction("Revisar",null)
								.setAction("Seguir", new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										((SolicitudAsistenteActivity) getActivity()).mViewPager.setCurrentItem(2);
									}
								});
						// Changing message text color
						//snackbar.setActionTextColor(Color.RED);

						// Changing action button text color
						View sbView = snackbar.getView();
						TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
						textView.setTextColor(Color.YELLOW);
						snackbar.show();
					}
				});*/
				break;
		}

		mAdapter = new ConvalidacionPosibleEnSolicitudCursorAdapter(getActivity());
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
						mConvalidacionPosibleEnSolicitudSeleccionado = ConvalidacionPosibleEnSolicitudProveedor.getRecord(
								getActivity().getContentResolver(),(int)arg1.getTag());
						if (tipoUsuarioLogin == G.ADMINISTRADOR){
							DialogFragment newFragment = new DialogoVerActualizarBorrar();
							newFragment.show(getFragmentManager(), "dialog");
						} else {
							//showDialogVer();
						}
						return true;
					}
				});
				break;
		
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			//Toast.makeText(getActivity(), String.valueOf(mNum), Toast.LENGTH_LONG).show();
			if (mFuncion == G.OPERACION_ASISTENTE) {
				StepsView pasos = ((SolicitudAsistenteActivity) getActivity()).mStepsView;
				pasos.setLabels(((SolicitudAsistenteActivity) getActivity()).labels)
						.setBarColorIndicator(ContextCompat.getColor(getActivity(),R.color.cardview_shadow_start_color))
						.setProgressColorIndicator(ContextCompat.getColor(getActivity(),R.color.colorPrimaryLight))
						.setLabelColorIndicator(ContextCompat.getColor(getActivity(),R.color.colorPrimaryLight))
						.setCompletedPosition(mNum - 1)
						.drawView();

				((SolicitudAsistenteActivity) getActivity()).mFab.setImageResource(R.drawable.ic_next);

				//((SolicitudAsistenteActivity) getActivity()).mPaso.setText(getString(R.string.prompt_solicitud_asistente_paso2));
			}
		}
	}

	public static class DialogoVerActualizarBorrar extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
			builder.setTitle("Convalidación '"+((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getID()+"'")
    			//.setIcon(R.drawable.alert_dialog_icon)
    			.setItems(R.array.edicion, new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {
    					// The 'which' argument contains the index position
    					// of the selected item      	            	   
    					switch (which){
    						case G.DIALOGO_VER:
    							//showDialogVer();
    							break;
    						case G.DIALOGO_ACTUALIZAR:
    							//showDialogActualizar();
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
			builder.setMessage("Va a borrar para esta Solicitud el registro de la Convalidación '"+mConvalidacionPosibleEnSolicitudSeleccionado.getID()+"'")
				//.setIcon(R.drawable.alert_dialog_icon)
				.setTitle("¡ATENCIÓN!")
				.setPositiveButton(R.string.borrar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Se borra
						borrarConvalidacionPosibleEnSolicitud();
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

	//public static void showDialogVer(){
	//	Intent actVer = new Intent(contexto, ConvalidacionPosibleEnSolicitudSeleccionarActivity.class);
	//	actVer.putExtra("ID", mConvalidacionPosibleEnSolicitudSeleccionado.getID());
	//	actVer.putExtra("Modo", G.VER);
	//	actVer.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
	//	contexto.startActivityForResult(actVer,G.VER);
	//}
	
	//public static void showDialogActualizar(){
	//	Intent actActualizar = new Intent(contexto, ConvalidacionPosibleEnSolicitudSeleccionarActivity.class);
	//	actActualizar.putExtra("ID", ((SolicitudAsistenteActivity) contexto).mSolicitudDeTrabajo.getID());
	//	actActualizar.putExtra("Modo", G.ACTUALIZAR);
	//	actActualizar.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
	//	contexto.startActivityForResult(actActualizar,G.ACTUALIZAR);
	//}

	/*public static void showDialogContinuar() {
		DialogFragment newFragment = new DialogoContinuar();
		newFragment.show(contexto.getFragmentManager(), "dialog");
	}*/

	public static class DialogoContinuar extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("No se ha podido borrar la Convalidación '"+mConvalidacionPosibleEnSolicitudSeleccionado.getID()+
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

	private static void borrarConvalidacionPosibleEnSolicitud() {
		new BorrarConvalidacionPosibleEnSolicitudTask().execute();	
	}

	public static class BorrarConvalidacionPosibleEnSolicitudTask extends AsyncTask<Void, Void, Boolean> {
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
				ConvalidacionPosibleEnSolicitudProveedor.deleteRecord(contexto.getContentResolver(),mConvalidacionPosibleEnSolicitudSeleccionado.getID(),true);
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

			//mBorrarConvalidacionPosibleEnSolicitudTask = null;
		
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
			//mBorrarConvalidacionPosibleEnSolicitudTask = null;
			//showProgress(false);
		}
	}
	//@Override
	//public void onListItemClick(ListView l, View v, int position, long id) {
	//	super.onListItemClick(l, v, position, id);
	//	//Log.i(LOGTAG, "Item clicked: " + id+" y position es "+position);
	//
	//	switch(mFuncion){
	//		case G.OPERACION_SELECCIONAR:
				//Log.i(LOGTAG,"y el ID es "+mRowItems.get(position).getID());
	//			mCallbackSelected.onConvalidacionPosibleEnSolicitudSelected(((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getID());
	//			break;
	//		default:
	//			break;
	//	}
	//}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.  This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		String columns[] = new String[] { Contrato.ConvalidacionPosibleEnSolicitud._ID,
										  Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE,
				                          Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD
										};

		Uri baseUri = Contrato.ConvalidacionPosibleEnSolicitud.CONTENT_URI;

		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				baseUri = Contrato.ConvalidacionPosibleEnSolicitud.CONTENT_URI;
				break;
			default:
				baseUri = Contrato.ConvalidacionPosibleEnSolicitud.CONTENT_URI;
				break;
		}
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.

		//String selection = null;
		//if (grid_currentQuery != null)
		//	selection = Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD + " LIKE '%" + grid_currentQuery + "%'";


		String selection = Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD + " = " +
				((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getID(); //Sería mejor implementar una interfaz
		return new CursorLoader(getActivity(), baseUri,
				columns, selection, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)
		
		
		//switch(mFuncion){
		//	case G.OPERACION_SELECCIONAR:
		//		mRowItems = ConvalidacionPosibleEnSolicitudProveedor.getRecords(getActivity().getContentResolver()// , grid_currentQuery
		//				);
		//		break;
		//	default:
		//		mRowItems = ConvalidacionPosibleEnSolicitudProveedor.getRecords(getActivity().getContentResolver()//, grid_currentQuery
		//				 );
		//		break;
		//}
		
		Uri laUriBase = Uri.parse("content://"+Contrato.AUTHORITY+"/ConvalidacionPosibleEnSolicitud");
		data.setNotificationUri(getActivity().getContentResolver(), laUriBase);
		
		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed.  We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}

	public class ConvalidacionPosibleEnSolicitudCursorAdapter extends CursorAdapter {
		public ConvalidacionPosibleEnSolicitudCursorAdapter(Context context) {
			super(context, null, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			int ID = cursor.getInt(cursor.getColumnIndex(Contrato.ConvalidacionPosibleEnSolicitud._ID));
			int fk_convalidacionPosible = cursor.getInt(cursor.getColumnIndex(Contrato.ConvalidacionPosibleEnSolicitud.FK_CONVALIDACIONPOSIBLE));
			//String fk_solicitud = cursor.getString(cursor.getColumnIndex(Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD));

			ConvalidacionPosible convalidacionPosible = ConvalidacionPosibleProveedor.getRecord(getActivity().getContentResolver(),fk_convalidacionPosible);

			TextView textviewEstudioConvalidado = (TextView) view.findViewById(R.id.textview_convalidacionposibleensolicitud_list_item_estudioconvalidado);
			textviewEstudioConvalidado.setText(convalidacionPosible.getEstudioConvalidado().getNombre());

			TextView textviewEstudioAportado = (TextView) view.findViewById(R.id.textview_convalidacionposibleensolicitud_list_item_estudioaportado);
			textviewEstudioAportado.setText(convalidacionPosible.getEstudioAportado().getNombre());

			ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
			int color = generator.getColor(convalidacionPosible.getEstudioConvalidado().getNombre()); //Genera un color según el nombre
			TextDrawable drawable = TextDrawable.builder()
					.buildRound(convalidacionPosible.getEstudioConvalidado().getNombre().substring(0,3), color);

			ImageView image = (ImageView) view.findViewById(R.id.image_view);
			image.setImageDrawable(drawable);

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.convalidacionposibleensolicitud_list_item, parent, false);
			bindView(v, context, cursor);
			return v;
		}
	}
}
