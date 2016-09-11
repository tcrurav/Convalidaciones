package org.ieselrincon.convalidaciones.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.Estudio;
import org.ieselrincon.convalidaciones.model.proveedor.EstudioProveedor;

import java.util.ArrayList;

public class EstudioDetalleActivity extends AppCompatActivity {
	//private static final String LOGTAG = "Tiburcio - EstudioDetalleActivity";
	
	private EstudioAddTask mEstudioAddTask = null;

	public Estudio mEstudioDeTrabajo;

	// UI references.
	private int mModo;
	private EditText mNombreView;
	private EditText mCodigoView;

	ScrollView EstudioDetalle_FormScroll;
	
	private View mFormView;
	private View mStatusView;
	private TextView mStatusMessageView;
	
	//ActionBar actionBar;
	//private Context contexto;
	
	int tipoUsuarioLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.estudio_detalle);

		//actionBar = getActionBar();

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//contexto = EstudioDetalleActivity.this;
		
		mNombreView = (EditText) findViewById(R.id.textview_estudio_detalle_nombre);
		mCodigoView = (EditText) findViewById(R.id.textview_estudio_detalle_codigo);
		
		EstudioDetalle_FormScroll = (ScrollView) findViewById(R.id.EstudioDetalle_FormScroll);
		
		Intent iin= getIntent();
	    Bundle extras = iin.getExtras();
	    
	    tipoUsuarioLogin = extras.getInt("tipoUsuarioLogin");

	    if(extras.getInt("ID") != G.SIN_VALOR_INT) 
	    {
	    	// Se va a actualizar o Ver. El Modo lo dice.
	    	mModo = extras.getInt("Modo");
	    	
	    	mEstudioDeTrabajo = EstudioProveedor.getRecord(getContentResolver(),extras.getInt("ID"));
	    	mNombreView.setText(mEstudioDeTrabajo.getNombre());
			mCodigoView.setText(mEstudioDeTrabajo.getCodigo());
	    	
	    	if(mModo==G.VER){
		    	mNombreView.setFocusable(false);
				mCodigoView.setFocusable(false);
	    	}
	    	
	    } else {
	    	// Se va a Insertar un registro
	    	mModo = G.INSERTAR;
	    	
	    	mEstudioDeTrabajo = new Estudio();

	    }

		mFormView = findViewById(R.id.EstudioDetalle_Form);
		mStatusView = findViewById(R.id.EstudioDetalle_Status);

		mStatusMessageView = (TextView) findViewById(R.id.EstudioDetalle_StatusMessage);

		switch(mModo){
			case G.INSERTAR:
			case G.ACTUALIZAR:
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
		if (mEstudioAddTask != null) {
			return;
		}

		// Reset errors.
		mNombreView.setError(null);

		// Store values at the time of the login attempt.
		mEstudioDeTrabajo.setNombre(mNombreView.getText().toString());
		mEstudioDeTrabajo.setCodigo(mCodigoView.getText().toString());

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(mEstudioDeTrabajo.getNombre())) {
			mNombreView.setError(getString(R.string.error_field_required));
			focusView = mNombreView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mStatusMessageView.setText(R.string.progress_cargando_datos);
	//		showProgress(true);

			mEstudioAddTask = new EstudioAddTask();
			mEstudioAddTask.execute();
			
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

	
	public class EstudioAddTask extends AsyncTask<Void, Void, Boolean> {
		protected void onPreExecute()
		{
			hideKeyboard();
			showProgress(true);
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			boolean resultado;
			
			if(mEstudioDeTrabajo.getID() != G.SIN_VALOR_INT) {
				try {
					EstudioProveedor.updateRecord(getContentResolver(), mEstudioDeTrabajo, true);
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
				//resultado=true; //FALTA CONTROLAR RESULTADO
			} else {
				try {
					EstudioProveedor.insertRecord(getContentResolver(), mEstudioDeTrabajo, new ArrayList<ContentProviderOperation>(), true);
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
			mEstudioAddTask = null;
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
			mEstudioAddTask = null;
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
			builder.setMessage("No se ha podido añadir/modificar el Estudio '"+mEstudioDeTrabajo.getNombre()+"'")
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
