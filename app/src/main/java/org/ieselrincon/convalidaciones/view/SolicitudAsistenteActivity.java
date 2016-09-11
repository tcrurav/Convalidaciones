package org.ieselrincon.convalidaciones.view;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionPosibleEnSolicitud;
import org.ieselrincon.convalidaciones.model.pojos.Curso;
import org.ieselrincon.convalidaciones.model.pojos.CursoAcademico;
import org.ieselrincon.convalidaciones.model.pojos.Solicitud;
import org.ieselrincon.convalidaciones.model.pojos.Usuario;

import org.ieselrincon.convalidaciones.model.proveedor.ConvalidacionPosibleEnSolicitudProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.CursoProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.SolicitudProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.UsuarioProveedor;

import java.util.ArrayList;
import java.util.Date;

public class SolicitudAsistenteActivity extends AppCompatActivity implements SolicitudFragment.OnSolicitudFragmentInteractionListener {
    private static final String LOGTAG = "Tiburcio - SolicitudAsistenteActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;
    //public TextView mPaso;
    //private ProgressBar mProgressBar;

    public StepsView mStepsView;
    public final String[] labels = {"Paso 1", "Paso 2", "Paso 3"};
    public FloatingActionButton mFab;

    public Solicitud mSolicitudDeTrabajo;

    //private int mTipoUsuarioLogin;
    private int mModo;
    public int mUsuarioLoginId;

    private PDFFragment mFragmentoPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_asistente);

        //mPaso = (TextView) findViewById(R.id.textview_gestion_asistente_paso);

        Intent iin= getIntent();
        Bundle extras = iin.getExtras();

        mUsuarioLoginId = extras.getInt("usuarioLoginId");
        //mTipoUsuarioLogin = extras.getInt("tipoUsuarioLogin");

        if(extras.getInt("ID") != G.SIN_VALOR_INT)
        {
            // Se va a actualizar o Ver. El Modo lo dice.
            mModo = extras.getInt("Modo");

            mSolicitudDeTrabajo = SolicitudProveedor.getRecord(getContentResolver(),extras.getInt("ID"));

        } else {
            // Se va a Insertar un registro
            mModo = G.INSERTAR;

            mSolicitudDeTrabajo = new Solicitud();
            Date currentDate = new Date(); //Fecha Actual
            int fechaActual = (int) (currentDate.getTime() / 1000L);
            mSolicitudDeTrabajo.setFecha(fechaActual);
            mSolicitudDeTrabajo.setCursoAcademico(CursoAcademico.CURSO_2016_2017);

            ArrayList<Curso> cursos = CursoProveedor.getRecords(getContentResolver());
            mSolicitudDeTrabajo.setCurso(cursos.get(0));

            Usuario usuario = UsuarioProveedor.getRecord(getContentResolver(), mUsuarioLoginId);
            mSolicitudDeTrabajo.setUsuario(usuario);

            try { //Cuando entra se crea una solicitud vacía
                Uri result = SolicitudProveedor.insertRecord(getContentResolver(),mSolicitudDeTrabajo, new ArrayList<ContentProviderOperation>(),true);
                int id = Integer.parseInt(result.getLastPathSegment()); //ARREGLAR
                mSolicitudDeTrabajo.setID(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.e(LOGTAG, String.valueOf(mSolicitudDeTrabajo.getID()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mStepsView = (StepsView) findViewById(R.id.stepsView);

        mStepsView.setLabels(labels)
                .setBarColorIndicator(ContextCompat.getColor(this,R.color.cardview_shadow_start_color))
                .setProgressColorIndicator(ContextCompat.getColor(this,R.color.colorPrimaryLight))
                .setLabelColorIndicator(ContextCompat.getColor(this,R.color.colorPrimaryLight))
                .setCompletedPosition(0)
                .drawView();

        //TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(mViewPager);

        //mProgressBar = (ProgressBar) findViewById(R.id.progressbar_asistente);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch(mViewPager.getCurrentItem()){
                    case 0: //Datos Generales
                    case 1: //Seleccionar Convalidaciones a solicitar
                        Snackbar snackbar = Snackbar
                                .make(view, "¿Ha revisado los datos?", Snackbar.LENGTH_LONG)
                                .setAction("Revisar",null)
                                .setAction("Seguir", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                                    }
                                });
                        // Changing message text color
                        //snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                        break;
                    case 2: //Enviar PDF
                        String pdfName = "pdfSolicitud"
                                //+ sdf.format(Calendar.getInstance().getTime())
                                + mSolicitudDeTrabajo.getID()
                                + ".pdf";
                        mFragmentoPDF.createPDFconiTextG(pdfName);
                        mFragmentoPDF.enviarPDF(pdfName, mSolicitudDeTrabajo.getUsuario().getEmail());
                        break;
                }



            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gestion_ciclos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSolicitudFragmentInteractionCambiaSolicitud(Solicitud solicitud) {
        mSolicitudDeTrabajo = solicitud;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0:

                    //mProgressBar.setProgress(0);
                    //return SolicitudDetalleFragment.newInstance(position + 1, G.OPERACION_CONSULTAR, G.ADMINISTRADOR); //falta arreglar esto
                    return SolicitudFragment.newInstance(position + 1, G.OPERACION_ASISTENTE//, G.ADMINISTRADOR, 1
                             );
                case 1:
                    //mProgressBar.setProgress(50);
                    return ConvalidacionPosibleEnSolicitudListFragment.newInstance(position + 1, G.OPERACION_ASISTENTE
                            , G.ADMINISTRADOR); //falta arreglar esto
                case 2:
                    //mProgressBar.setProgress(100);
                    //return CrearYEnviarPDFFragment.newInstance(position + 1, G.OPERACION_CONSULTAR, G.ADMINISTRADOR); //falta arreglar esto
                    mFragmentoPDF = PDFFragment.newInstance(position + 1, G.OPERACION_ASISTENTE, G.ADMINISTRADOR, 1); //falta arreglar esto
                    return mFragmentoPDF;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 1 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Datos Generales";
                case 1:
                    return "Convalidaciones";
                case 2:
                    return "Terminar";
            }
            return null;
        }
    }

}
