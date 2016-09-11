package org.ieselrincon.convalidaciones.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.Ciclo;
import org.ieselrincon.convalidaciones.model.pojos.Curso;
import org.ieselrincon.convalidaciones.model.pojos.Turno;
import org.ieselrincon.convalidaciones.model.proveedor.CicloProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.CursoProveedor;

import java.util.ArrayList;
import java.util.Arrays;

public class CursoDetalleActivity extends AppCompatActivity {
	//private static final String LOGTAG = "Tiburcio - CursoDetalleActivity";
	
	private CursoAddTask mCursoAddTask = null;

	private Curso mCursoDeTrabajo;

	// UI references.
	private int mModo;
	private Spinner mTurnoView;
	private Spinner mAnnoView;
	private Spinner mCicloView;

	private ArrayList<Ciclo> mCiclos;

	ScrollView CursoDetalle_FormScroll;
	
	private View mFormView;
	private View mStatusView;
	private TextView mStatusMessageView;
	
	//ActionBar actionBar;
	private Context contexto;
	
	int tipoUsuarioLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.curso_detalle);

		//actionBar = getActionBar();

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		contexto = CursoDetalleActivity.this;

		mTurnoView = (Spinner) findViewById(R.id.spinner_detalle_curso_turno);
		ArrayAdapter<CharSequence> adapterTurnos = ArrayAdapter.createFromResource(contexto,
				R.array.turnos, android.R.layout.simple_spinner_item);
		adapterTurnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mTurnoView.setAdapter(adapterTurnos);

		mAnnoView = (Spinner) findViewById(R.id.spinner_detalle_curso_anno);

		mCicloView = (Spinner) findViewById(R.id.spinner_detalle_curso_ciclo);
		mCiclos = CicloProveedor.getRecords(getContentResolver(),null);
		String [] arrayCiclos = new String[mCiclos.size()];
		for (int i=0; i<mCiclos.size();i++){
			arrayCiclos[i] = mCiclos.get(i).getNombre();
		}
		ArrayAdapter adapterCiclos = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayCiclos);
		adapterCiclos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCicloView.setAdapter(adapterCiclos);

		CursoDetalle_FormScroll = (ScrollView) findViewById(R.id.CursoDetalle_FormScroll);
		
		Intent iin= getIntent();
	    Bundle extras = iin.getExtras();
	    
	    tipoUsuarioLogin = extras.getInt("tipoUsuarioLogin");

	    if(extras.getInt("ID") != G.SIN_VALOR_INT) 
	    {
	    	// Se va a actualizar o Ver. El Modo lo dice.
	    	mModo = extras.getInt("Modo");
	    	
	    	mCursoDeTrabajo = CursoProveedor.getRecord(getContentResolver(),extras.getInt("ID"));

	    	mTurnoView.setSelection(mCursoDeTrabajo.getTurno().ordinal());
			ponerAnnosSegunTurno(mCursoDeTrabajo.getTurno().ordinal());

			mCicloView.setSelection(Arrays.asList(mCiclos).indexOf(mCursoDeTrabajo.getCiclo().getNombre()));
	    	
	    	if(mModo==G.VER){
		    	mTurnoView.setFocusable(false);
				mAnnoView.setFocusable(false);
				mCicloView.setFocusable(false);
	    	}
	    	
	    } else {
	    	// Se va a Insertar un registro
	    	mModo = G.INSERTAR;
	    	
	    	mCursoDeTrabajo = new Curso();
			mCursoDeTrabajo.setAnno(1); //Por defecto lo ponemos en el primer curso
			mCursoDeTrabajo.setTurno(Turno.MAÑANA);

			mTurnoView.setSelection(Turno.MAÑANA.ordinal());
			ponerAnnosSegunTurno(Turno.MAÑANA.ordinal());
			mAnnoView.setSelection(0);
			mCicloView.setSelection(0);

	    }

		mFormView = findViewById(R.id.CursoDetalle_Form);
		mStatusView = findViewById(R.id.CursoDetalle_Status);

		mStatusMessageView = (TextView) findViewById(R.id.CursoDetalle_StatusMessage);

		switch(mModo){
			case G.INSERTAR:
			case G.ACTUALIZAR:
				mTurnoView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
						if( i != Turno.NOCHE.ordinal() && mCursoDeTrabajo.getAnno() == 3 ){
							mCursoDeTrabajo.setAnno(1);
						}
						ponerAnnosSegunTurno(i);
					}

					@Override
					public void onNothingSelected(AdapterView<?> adapterView) {

					}
				});
				FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
				fab.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Snackbar snackbar = Snackbar
								.make(view, "Se guardarán los datos", Snackbar.LENGTH_LONG)
								.setAction("Cancelar", new View.OnClickListener(){
									@Override
									public void onClick(View view) {
									}
								});
						snackbar.setActionTextColor(Color.RED);
						snackbar.setAction("Guardar", new View.OnClickListener(){
							@Override
							public void onClick(View view) {
								attemptGuardar();
							}
						}).show();
					}
				});
				break;
			case G.VER:
			default:
				break;
		}

	}

	private void ponerAnnosSegunTurno(int turno) {
		ArrayAdapter<CharSequence> adapterAnnos;
		if(turno == Turno.NOCHE.ordinal()){
            adapterAnnos = ArrayAdapter.createFromResource(contexto,
                    R.array.annosNoche, android.R.layout.simple_spinner_item);
        } else {
            adapterAnnos = ArrayAdapter.createFromResource(contexto,
                    R.array.annosMananaTarde, android.R.layout.simple_spinner_item);
        }
		adapterAnnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mAnnoView.setAdapter(adapterAnnos);
		mAnnoView.setSelection(mCursoDeTrabajo.getAnno()-1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//Log.i(LOGTAG, "onCreateOptionsMenu");
		
		switch(mModo){
			case G.INSERTAR:
			case G.ACTUALIZAR:
				menu.add(0,G.GUARDAR,0,"GUARDAR")
					.setIcon(R.mipmap.ic_action_guardar)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
				break;
			case G.VER:
			default:
				break;
		}

		// No hace falta cuando está el HomeUp
		//menu.add(0,G.CANCELAR,0,"CANCELAR")
		//	.setIcon(R.mipmap.ic_action_cancelar)
		//	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case G.GUARDAR:
				attemptGuardar();
				return true;
			//case G.CANCELAR: //Es equivalente al botón BACK que es diferente al HomeUp
			//	finish();
			//	return true;
			case android.R.id.home:
		        NavUtils.navigateUpFromSameTask(this);
		        return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void attemptGuardar() {
		if (mCursoAddTask != null) {
			return;
		}

		// Reset errors.
		//mAnnoView.setError(null);

		// Store values at the time of validation
		//mCursoDeTrabajo.setAnno(Integer.parseInt(mAnnoView.getText().toString()));

		boolean cancel = false;
		View focusView = null;

		mCursoDeTrabajo.setTurno(Turno.values()[mTurnoView.getSelectedItemPosition()]);
		mCursoDeTrabajo.setAnno(mAnnoView.getSelectedItemPosition()+1);
		mCursoDeTrabajo.setCiclo(mCiclos.get(mCicloView.getSelectedItemPosition()));

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mStatusMessageView.setText(R.string.progress_cargando_datos);
	//		showProgress(true);

			mCursoAddTask = new CursoAddTask();
			mCursoAddTask.execute();
			
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mStatusView.setVisibility(View.VISIBLE);
			mStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						public void onAnimationEnd(Animator animation) {
							mStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mFormView.setVisibility(View.VISIBLE);
			mFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	private void hideKeyboard(){
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	    
	    // Pasar de nombre a imagen
	    // Bitmap image = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier( "imagename" , "drawable", getPackageName()));
	}

	
	public class CursoAddTask extends AsyncTask<Void, Void, Boolean> {
		protected void onPreExecute()
		{
			hideKeyboard();
			showProgress(true);
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			boolean resultado;
			
			if(mCursoDeTrabajo.getID() != G.SIN_VALOR_INT) {
				try {
					CursoProveedor.updateRecord(getContentResolver(), mCursoDeTrabajo,true);
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
				//resultado=true; //FALTA CONTROLAR RESULTADO
			} else {
				try {
					CursoProveedor.insertRecord(getContentResolver(), mCursoDeTrabajo, new ArrayList<ContentProviderOperation>(), true);
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false; 
			}
						
			//return resultado;
		}
		@Override
		protected void onPostExecute(final Boolean success) {
			mCursoAddTask = null;
			showProgress(false);

			if (success) {
				Intent returnIntent = new Intent();
				setResult(RESULT_OK,returnIntent);     
				finish();
			} else {
				showDialogContinuar();
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED,returnIntent);     
				//finish();
			}
		}
		@Override
		protected void onCancelled() {
			mCursoAddTask = null;
			showProgress(false);
		}
	}
	
	public void showDialogContinuar() {
		DialogFragment newFragment = new DialogoContinuar();
		newFragment.show(getSupportFragmentManager(), "dialog");
	}
	
	@SuppressLint("ValidFragment")
	public class DialogoContinuar extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("No se ha podido añadir/modificar el Curso '"
								+mCursoDeTrabajo.getAnno()+"º"+mCursoDeTrabajo.getCiclo().getAbreviatura()+mCursoDeTrabajo.getTurno().getTurno()
								+"'")
					.setTitle("¡ATENCIÓN!")
					.setPositiveButton(R.string.action_continuar, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// Se continua
							finish();
						}
					});
			// Create the AlertDialog object and return it
			return builder.create();
		}

	}
}
