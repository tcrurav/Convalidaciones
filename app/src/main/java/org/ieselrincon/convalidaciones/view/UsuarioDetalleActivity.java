package org.ieselrincon.convalidaciones.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.Usuario;
import org.ieselrincon.convalidaciones.model.proveedor.UsuarioProveedor;

/**
 * A login screen that offers login via email/password.
 */
public class UsuarioDetalleActivity extends AppCompatActivity {
    private static final String LOGTAG = "Tiburcio - UsuarioDetalleActivity";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserUsuarioDetalleTask mAuthTask = null;

    // UI references.
    private int mModo;
    private EditText mNombreView;
    private EditText mApellidosView;
    private EditText mDNIView;
    private EditText mTelefonoView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordRepeatView;
    private Spinner mTipoUsuarioView;
    //private View mProgressView;
    //private View mLoginFormView;

    private View mFormView;
    private View mStatusView;
    private TextView mStatusMessageView;
    
    private Usuario mUsuarioDeTrabajo;
    private int mSolicitudId;
    //private int tipoUsuarioLogin;
    private int mUsuarioLoginId;


    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_detalle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the login form.
        mNombreView = (EditText) findViewById(R.id.edittext_usuariodetalle_nombre);
        mApellidosView = (EditText) findViewById(R.id.edittext_usuariodetalle_apellidos);
        mDNIView = (EditText) findViewById(R.id.edittext_usuariodetalle_dni);
        mTelefonoView = (EditText) findViewById(R.id.edittext_usuariodetalle_telefono);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.edittext_usuariodetalle_email);
        mPasswordView = (EditText) findViewById(R.id.edittext_usuariodetalle_password);
        mPasswordRepeatView = (EditText) findViewById(R.id.edittext_usuariodetalle_password_repeat);
        mTipoUsuarioView = (Spinner) findViewById(R.id.spinner_usuariodetalle_tipousuario);

        contexto = this;

        ArrayAdapter<CharSequence> adapterTiposUsuario;
        adapterTiposUsuario = ArrayAdapter.createFromResource(contexto,
                R.array.tiposUsuario, android.R.layout.simple_spinner_item);
        adapterTiposUsuario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTipoUsuarioView.setAdapter(adapterTiposUsuario);



        Button mButtonActualizar = (Button) findViewById(R.id.button_usuariodetalle_actualizar);
        mButtonActualizar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptActualizar();
            }
        });

        Intent iin= getIntent();
        Bundle extras = iin.getExtras();

        //tipoUsuarioLogin = extras.getInt("tipoUsuarioLogin");
        mUsuarioLoginId = extras.getInt("usuarioLoginId");
        mSolicitudId = extras.getInt("solicitudID");

        if(extras.getInt("ID") != G.SIN_VALOR_INT)
        {
            // Se va a actualizar o Ver. El Modo lo dice.
            mModo = extras.getInt("Modo");

            mUsuarioDeTrabajo = UsuarioProveedor.getRecord(getContentResolver(),extras.getInt("ID"));
            mNombreView.setText(mUsuarioDeTrabajo.getNombre());
            mApellidosView.setText(mUsuarioDeTrabajo.getApellidos());
            mPasswordView.setText(mUsuarioDeTrabajo.getContrasena());
            mPasswordRepeatView.setText(mUsuarioDeTrabajo.getContrasena());
            mDNIView.setText(mUsuarioDeTrabajo.getDni());
            mEmailView.setText(mUsuarioDeTrabajo.getEmail());
            mTelefonoView.setText(mUsuarioDeTrabajo.getTelefono());
            mTipoUsuarioView.setSelection(mUsuarioDeTrabajo.getTipoUsuario().ordinal()-1);

            if(mModo==G.VER){
                mNombreView.setFocusable(false);
                mApellidosView.setFocusable(false);
                mPasswordView.setFocusable(false);
                mPasswordRepeatView.setFocusable(false);
                mDNIView.setFocusable(false);
                mEmailView.setFocusable(false);
                mTelefonoView.setFocusable(false);
                mTipoUsuarioView.setFocusable(false);
            }

        } else {
            // Se va a Insertar un registro
            mModo = G.INSERTAR;

            mUsuarioDeTrabajo = new Usuario();

        }
        
        //mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);

        mFormView = findViewById(R.id.UsuarioDetalle_Form);
        mStatusView = findViewById(R.id.UsuarioDetalle_Status);

        mStatusMessageView = (TextView) findViewById(R.id.UsuarioDetalle_StatusMessage);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptActualizar() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mNombreView.setError(null);
        mApellidosView.setError(null);
        mDNIView.setError(null);
        mTelefonoView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordRepeatView.setError(null);

        // Store values at the time of the login attempt.
        String nombre = mNombreView.getText().toString();
        String apellidos = mApellidosView.getText().toString();
        String dni = mDNIView.getText().toString();
        String telefono = mTelefonoView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordRepeat = mPasswordRepeatView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.

        if (TextUtils.isEmpty(nombre)) {
            mNombreView.setError(getString(R.string.error_field_required));
            focusView = mNombreView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(apellidos)) {
            mApellidosView.setError(getString(R.string.error_field_required));
            focusView = mApellidosView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(dni)) {
            mDNIView.setError(getString(R.string.error_field_required));
            focusView = mDNIView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(telefono)) {
            mTelefonoView.setError(getString(R.string.error_field_required));
            focusView = mTelefonoView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        Log.e(LOGTAG,"UNO");

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        Log.e(LOGTAG,"DOS");

        if (TextUtils.isEmpty(passwordRepeat)) {
            mPasswordRepeatView.setError(getString(R.string.error_field_required));
            focusView = mPasswordRepeatView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        Log.e(LOGTAG,"TRES");

        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        Log.e(LOGTAG,"CUATRO");

        if (!isPasswordValid(passwordRepeat)) {
            mPasswordRepeatView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordRepeatView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        Log.e(LOGTAG,"CINCO");

/*
        if (TextUtils.isEmpty(passwordRepeat)) {
            mPasswordRepeatView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordRepeatView;
            cancel = true;
        }
*/
        if (!password.equals(passwordRepeat)) {
            mPasswordRepeatView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordRepeatView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        Log.e(LOGTAG,"SEIS");

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
            focusView.requestFocus();
            return;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
            focusView.requestFocus();
            return;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            mStatusMessageView.setText(R.string.progress_cargando_datos);
            //showProgress(true);
            mAuthTask = new UserUsuarioDetalleTask(nombre, apellidos, dni, telefono, email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserUsuarioDetalleTask extends AsyncTask<Void, Void, Boolean> {

        private final String mNombre;
        private final String mApellidos;
        private final String mDNI;
        private final String mTelefono;
        private final String mEmail;
        private final String mPassword;


        UserUsuarioDetalleTask(String nombre, String apellidos, String dni, String telefono, String email, String password) {
            mNombre = nombre;
            mApellidos = apellidos;
            mDNI = dni;
            mTelefono = telefono;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            Usuario usuario = new Usuario();
            usuario.setID(mUsuarioDeTrabajo.getID());
            usuario.setNombre(mNombre);
            usuario.setApellidos(mApellidos);
            usuario.setDni(mDNI);
            usuario.setTelefono(mTelefono);
            usuario.setEmail(mEmail);
            usuario.setContrasena(mPassword);
            Log.e(LOGTAG,"eL TIPO DE USUARIO: "+mUsuarioDeTrabajo.getTipoUsuario().name());
            usuario.setTipoUsuario(mUsuarioDeTrabajo.getTipoUsuario());

            try {
                //if(mModo==G.INSERTAR) {
                //    UsuarioProveedor.insertRecord(getContentResolver(), usuario, new ArrayList<ContentProviderOperation>(), true);
                //}else{
                    UsuarioProveedor.updateRecord(getContentResolver(), usuario, true);
                //}
                return true;
            } catch (Exception e) {
                //e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false);

            if (success) {
                Intent actActualizar = new Intent(contexto, SolicitudAsistenteActivity.class);
                actActualizar.putExtra("ID", mSolicitudId);
                actActualizar.putExtra("Modo", G.ACTUALIZAR);
                actActualizar.putExtra("usuarioLoginId", mUsuarioLoginId);
                contexto.startActivity(actActualizar);
                finish();
            } else {
                mPasswordRepeatView.setError(getString(R.string.error_incorrect_password));
                mPasswordRepeatView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }

}

