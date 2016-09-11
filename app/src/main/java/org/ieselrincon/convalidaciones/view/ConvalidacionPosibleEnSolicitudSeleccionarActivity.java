package org.ieselrincon.convalidaciones.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;

public class ConvalidacionPosibleEnSolicitudSeleccionarActivity extends AppCompatActivity
                                        implements ConvalidacionPosibleListFragment.OnConvalidacionPosibleSelectedListener {
    private static final String LOGTAG = "Tiburcio - ConvalidacionPosibleEnSolicitudSeleccionarActivity";
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
    private ViewPager mViewPager;
    public Context contexto = this;

    private int mIdCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_sin_tabs);

        Intent iin = getIntent();
        Bundle extras = iin.getExtras();

        mIdCurso = extras.getInt("idCurso");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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

        switch (item.getItemId()) {
            case android.R.id.home:
                //Intent upIntent = NavUtils.getParentActivityIntent(this);
                //upIntent.putExtra();
                //NavUtils.navigateUpTo(this,upIntent);
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

    }

    @Override
    public void onConvalidacionPosibleSelected(int ID) {
        Intent intent=new Intent();
        intent.putExtra("SeleccionadoID",ID);

        setResult(RESULT_OK, intent);
        finish();
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
                    ConvalidacionPosibleListFragment fragmento = ConvalidacionPosibleListFragment.newInstance(position + 1, G.OPERACION_SELECCIONAR_CONVALIDACIONES_DE_UN_CURSO, mIdCurso, G.ADMINISTRADOR); //falta arreglar esto
                    fragmento.setListenerSelected((ConvalidacionPosibleListFragment.OnConvalidacionPosibleSelectedListener) contexto);
                    return fragmento;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 1 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Todos";
            }
            return null;
        }
    }
}
