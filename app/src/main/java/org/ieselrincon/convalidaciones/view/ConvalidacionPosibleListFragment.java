package org.ieselrincon.convalidaciones.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
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
import com.anton46.stepsview.StepsView;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionPosible;
import org.ieselrincon.convalidaciones.model.pojos.Curso;
import org.ieselrincon.convalidaciones.model.pojos.Estudio;
import org.ieselrincon.convalidaciones.model.proveedor.Contrato;
import org.ieselrincon.convalidaciones.model.proveedor.ConvalidacionPosibleProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.CursoProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.EstudioProveedor;

import java.util.ArrayList;
import java.util.HashMap;

public class ConvalidacionPosibleListFragment extends ListFragment
		implements LoaderManager.LoaderCallbacks<Cursor> {
	
	//private static final String LOGTAG = "Tiburcio - ConvalidacionPosibleListFragment";
	int mNum;
	int mFuncion;
	int mIdCurso;
	
	//ArrayList<ConvalidacionPosible> mRowItems;
	static ConvalidacionPosible mConvalidacionPosibleSeleccionado;

	static Activity contexto;

	ConvalidacionPosibleCursorAdapter mAdapter;
	LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	//int posicionListView;
	
	static int tipoUsuarioLogin;
	//int empleadoIdLogin;
	
	OnConvalidacionPosibleSelectedListener mCallbackSelected = new OnConvalidacionPosibleSelectedListener() {
		@Override
		public void onConvalidacionPosibleSelected(int position){
			
		}
	};
	
	public interface OnConvalidacionPosibleSelectedListener {
        public void onConvalidacionPosibleSelected(int position);
    }
	
	public void setListenerSelected(OnConvalidacionPosibleSelectedListener listener) {
        mCallbackSelected = listener;
    }
	
	OnConvalidacionPosibleAceptadoListener mCallbackAceptado = new OnConvalidacionPosibleAceptadoListener() {
		@Override
		public void onConvalidacionPosibleAceptado(HashMap<Integer,Integer> mapa){
			
		}
	};
	
	public interface OnConvalidacionPosibleAceptadoListener {
        public void onConvalidacionPosibleAceptado(HashMap<Integer, Integer> mapa);
    }
	
	public void setListenerAceptado(OnConvalidacionPosibleAceptadoListener listener) {
        mCallbackAceptado= listener;
    }
	
	public static ConvalidacionPosibleListFragment newInstance(int num, int funcion, int tipoUsuarioLogin) {
		//Log.i(LOGTAG, "newInstance");
		ConvalidacionPosibleListFragment f = new ConvalidacionPosibleListFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();

		args.putInt("num", num);
		args.putInt("funcion",funcion);
		args.putInt("tipoUsuarioLogin",tipoUsuarioLogin);
		
		f.setArguments(args);

		return f;
	}
	
	public static ConvalidacionPosibleListFragment newInstance(int num, int funcion, int idCurso, int tipoUsuarioLogin) {
		//Log.i(LOGTAG, "newInstance"); //id puede ser o bien promocionId ó bien empleadoId
		ConvalidacionPosibleListFragment f = new ConvalidacionPosibleListFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();

		args.putInt("num", num);
		args.putInt("funcion",funcion);
		args.putInt("idCurso",idCurso);
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
			case G.OPERACION_SELECCIONAR_CONVALIDACIONES_DE_UN_CURSO:
				mIdCurso = getArguments().getInt("idCurso");
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
				if(mFuncion != G.OPERACION_SELECCIONAR_CONVALIDACIONES_DE_UN_CURSO) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Listado Completo");
				}
				grid_currentQuery = null;
			} else {
				if(mFuncion != G.OPERACION_SELECCIONAR_CONVALIDACIONES_DE_UN_CURSO) {
					((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("filtrado por: " + newText);
				}
				grid_currentQuery = newText;
			}
			getLoaderManager().restartLoader(0, null, ConvalidacionPosibleListFragment.this);
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
			case G.OPERACION_SELECCIONAR_CONVALIDACIONES_DE_UN_CURSO:
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
					Intent actInsertar = new Intent(getActivity(), ConvalidacionPosibleDetalleActivity.class);
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

		TextView tv = (TextView) v.findViewById(R.id.text);
		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				tv.setText(R.string.seleccione_la_convalidacionposible);
				tv.setVisibility(TextView.VISIBLE);
			case G.OPERACION_SELECCIONAR_CONVALIDACIONES_DE_UN_CURSO:
				sv.setQueryHint("Buscar...");
				sv.setOnQueryTextListener(queryListener);
				sv.setIconifiedByDefault(false);
				sv.setVisibility(SearchView.VISIBLE);
				break;
			default:
				break;
		}

		mAdapter = new ConvalidacionPosibleCursorAdapter(getActivity());
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
			case G.OPERACION_SELECCIONAR_CONVALIDACIONES_DE_UN_CURSO:
				break;
			case G.OPERACION_ASISTENTE:
				((SolicitudAsistenteActivity) getActivity()).mStepsView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
					}
				});
			default:
				getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
												   int arg2, long arg3) {
	    	
						//posicionListView = arg2;
						mConvalidacionPosibleSeleccionado = ConvalidacionPosibleProveedor.getRecord(contexto.getContentResolver(),(int)arg1.getTag());
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

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			//Toast.makeText(getActivity(), String.valueOf(mNum), Toast.LENGTH_LONG).show();
			if (mFuncion == G.OPERACION_ASISTENTE) {
				StepsView pasos = ((SolicitudAsistenteActivity) getActivity()).mStepsView;
				pasos.setLabels(((SolicitudAsistenteActivity) getActivity()).labels)
						.setBarColorIndicator(ContextCompat.getColor(getActivity(),R.color.colorPrimary))
						.setProgressColorIndicator(ContextCompat.getColor(getActivity(),R.color.colorAccent))
						.setLabelColorIndicator(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark))
						.setCompletedPosition(mNum - 1)
						.drawView();

				//((SolicitudAsistenteActivity) getActivity()).mFab.setImageResource(R.mipmap.ic_next);
			}
		}
	}

	public static class DialogoVerActualizarBorrar extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Convalidación '"+mConvalidacionPosibleSeleccionado.getID()+"'")
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

	//public static void showDialogBorrar() {
	//	DialogFragment newFragment = new DialogoConfirmarBorrado();
	//	newFragment.show(getFragmentManager(), "dialog");
	//}

	public static class DialogoConfirmarBorrado extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Va a borrar el registro de la Convalidación '"+mConvalidacionPosibleSeleccionado.getID()+"'")
				//.setIcon(R.drawable.alert_dialog_icon)
				.setTitle("¡ATENCIÓN!")
				.setPositiveButton(R.string.borrar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Se borra
						borrarConvalidacionPosible();
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
		Intent actVer = new Intent(contexto, ConvalidacionPosibleDetalleActivity.class);
		actVer.putExtra("ID", mConvalidacionPosibleSeleccionado.getID());
		actVer.putExtra("Modo", G.VER);
		actVer.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
		contexto.startActivityForResult(actVer,G.VER);
	}
	
	public static void showDialogActualizar(){
		Intent actActualizar = new Intent(contexto, ConvalidacionPosibleDetalleActivity.class);
		actActualizar.putExtra("ID", mConvalidacionPosibleSeleccionado.getID());
		actActualizar.putExtra("Modo", G.ACTUALIZAR);
		actActualizar.putExtra("tipoUsuarioLogin", tipoUsuarioLogin);
		contexto.startActivityForResult(actActualizar,G.ACTUALIZAR);
	}

	public void showDialogContinuar() {
		DialogFragment newFragment = new DialogoContinuar();
		newFragment.show(getFragmentManager(), "dialog"); 
	}

	@SuppressLint("ValidFragment")
	public class DialogoContinuar extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("No se ha podido borrar la Convalidación '"+mConvalidacionPosibleSeleccionado.getID()+
					"'. Hay visitas, horarios, promociones y/o comunicaciones para este centro.")
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

	private static void borrarConvalidacionPosible() {
		new BorrarConvalidacionPosibleTask().execute();	
	}

	public static class BorrarConvalidacionPosibleTask extends AsyncTask<Void, Void, Boolean> {
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
				ConvalidacionPosibleProveedor.deleteRecord(contexto.getContentResolver(),mConvalidacionPosibleSeleccionado.getID(),true);
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

			//mBorrarConvalidacionPosibleTask = null;
		
			if (success) {
				//mAdapter.notifyDataSetChanged();
				//Toast.makeText(this, "El registro se borró satisfactoriamente", Toast.LENGTH_SHORT).show();
			} else {
				//NO SE borraron LOS DATOS
				//showDialogContinuar();
			}
		}
		@Override
		protected void onCancelled() {
			//mBorrarConvalidacionPosibleTask = null;
			//showProgress(false);
		}
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		//Log.i(LOGTAG, "Item clicked: " + id+" y position es "+position);
		
		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
			case G.OPERACION_SELECCIONAR_CONVALIDACIONES_DE_UN_CURSO:
				//Log.i(LOGTAG,"y el ID es "+mRowItems.get(position).getID());
				mCallbackSelected.onConvalidacionPosibleSelected((int) v.getTag());
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
		String columns[] = new String[] { Contrato.ConvalidacionPosible._ID,
										  Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO,
										  Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO,
										  Contrato.ConvalidacionPosible.FK_CURSO
										};

		Uri baseUri = Contrato.ConvalidacionPosible.CONTENT_URI;
		String selection = null;

		switch(mFuncion){
			case G.OPERACION_SELECCIONAR:
				baseUri = Contrato.ConvalidacionPosible.CONTENT_URI;
				selection = null;
				break;
			case G.OPERACION_SELECCIONAR_CONVALIDACIONES_DE_UN_CURSO:
				baseUri = Contrato.ConvalidacionPosible.CONTENT_URI;
				selection = Contrato.ConvalidacionPosible.FK_CURSO + " = " + mIdCurso;
				break;
			default:
				baseUri = Contrato.ConvalidacionPosible.CONTENT_URI;
				selection = null;
				break;
		}
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.

		//String selection = null;
		//if (grid_currentQuery != null)
		//	selection = Contrato.ConvalidacionPosible.NOMBRE + " LIKE '%" + grid_currentQuery + "%'";

		return new CursorLoader(getActivity(), baseUri,
				columns, selection, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)
		
		
		//switch(mFuncion){
		//	case G.OPERACION_SELECCIONAR:
		//		mRowItems = ConvalidacionPosibleProveedor.getRecords(getActivity().getContentResolver()
		//				//, grid_currentQuery
		//															);
		//		break;
		//	default:
		//		mRowItems = ConvalidacionPosibleProveedor.getRecords(getActivity().getContentResolver()
						//, grid_currentQuery
		//															);
		//		break;
		//}
		
		Uri laUriBase = Uri.parse("content://"+Contrato.AUTHORITY+"/ConvalidacionPosible");
		data.setNotificationUri(getActivity().getContentResolver(), laUriBase);
		
		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed.  We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}

	public class ConvalidacionPosibleCursorAdapter extends CursorAdapter {
		public ConvalidacionPosibleCursorAdapter(Context context) {
			super(context, null, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			int ID = cursor.getInt(cursor.getColumnIndex(Contrato.ConvalidacionPosible._ID));
			int fk_estudioConvalidado = cursor.getInt(cursor.getColumnIndex(Contrato.ConvalidacionPosible.FK_ESTUDIOCONVALIDADO));
			int fk_estudioAportado = cursor.getInt(cursor.getColumnIndex(Contrato.ConvalidacionPosible.FK_ESTUDIOAPORTADO));
			int fk_curso = cursor.getInt(cursor.getColumnIndex(Contrato.ConvalidacionPosible.FK_CURSO));

			Estudio estudioConvalidado = EstudioProveedor.getRecord(getActivity().getContentResolver(),fk_estudioConvalidado);
			Estudio estudioAportado = EstudioProveedor.getRecord(getActivity().getContentResolver(),fk_estudioAportado);
			Curso curso = CursoProveedor.getRecord(getActivity().getContentResolver(),fk_curso);
	
			TextView textviewEstudioConvalidado = (TextView) view.findViewById(R.id.textview_convalidacionposible_list_item_estudioconvalidado);
			textviewEstudioConvalidado.setText(estudioConvalidado.getNombre());
			TextView textviewEstudioAportado = (TextView) view.findViewById(R.id.textview_convalidacionposible_list_item_estudioaportado);
			textviewEstudioAportado.setText(estudioAportado.getNombre());

			int anno = curso.getAnno();
			String turno = curso.getTurno().name();
			String abreviatura = curso.getCiclo().getAbreviatura();
			String nombreAbreviado = anno + "º" + abreviatura + "-" + turno;
			TextView textviewCurso = (TextView) view.findViewById(R.id.textview_convalidacionposible_list_item_curso);
			textviewCurso.setText(nombreAbreviado);

			String nombreAbreviadoCorto = anno + "º" + abreviatura + "-" + turno.substring(0,1);
			ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
			int color = generator.getColor(nombreAbreviadoCorto); //Genera un color según el nombre
			TextDrawable drawable = TextDrawable.builder()
					.beginConfig()
					//.textColor(Color.BLACK)
					//.useFont(Typeface.DEFAULT)
					.fontSize(36) /* size in px */
					//.bold()
					//.toUpperCase()
					.endConfig()
					.buildRound(nombreAbreviadoCorto, color);

			ImageView image = (ImageView) view.findViewById(R.id.image_view);
			image.setImageDrawable(drawable);

			view.setTag(ID);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.convalidacionposible_list_item, parent, false);
			bindView(v, context, cursor);
			return v;
		}
	}
}
