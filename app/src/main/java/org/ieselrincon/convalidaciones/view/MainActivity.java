package org.ieselrincon.convalidaciones.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOGTAG = "Tiburcio - MainActivity";

    int mUsuarioLoginId;
    Activity contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contexto = this;

        final AppBarLayout appbarLayout = (AppBarLayout)findViewById(R.id.appbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                appbarLayout.setExpanded(false,true);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CardView cvNuevaSolicitud = (CardView) findViewById(R.id.cv_nueva_solicitud);
        cvNuevaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act = new Intent(contexto, SolicitudAsistenteActivity.class);
                act.putExtra("ID", G.SIN_VALOR_INT);
                act.putExtra("usuarioLoginId", mUsuarioLoginId);
                contexto.startActivity(act);
            }
        });

        CardView cvSolicitudesAnteriores = (CardView) findViewById(R.id.cv_solicitudes_anteriores);
        cvSolicitudesAnteriores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act = new Intent(contexto, SolicitudesGestionActivity.class);
                act.putExtra("usuarioLoginId", mUsuarioLoginId);
                contexto.startActivity(act);
            }
        });

        CardView cvCursosCiclos = (CardView) findViewById(R.id.cv_cursos_ciclos);
        cvCursosCiclos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act = new Intent(contexto, CiclosCursosGestionActivity.class);
                contexto.startActivity(act);
            }
        });

        CardView cvEstudios = (CardView) findViewById(R.id.cv_estudios);
        cvEstudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act = new Intent(contexto, EstudiosGestionActivity.class);
                contexto.startActivity(act);
            }
        });

        CardView cvConvalidacionesPosibles = (CardView) findViewById(R.id.cv_convalidaciones_posibles);
        cvConvalidacionesPosibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act = new Intent(contexto, ConvalidacionesPosiblesGestionActivity.class);
                contexto.startActivity(act);
            }
        });

        //Para entender las siguientes líneas ir a https://developer.android.com/training/basics/activity-lifecycle/recreating.html
        if(savedInstanceState == null) {
            //Se crea una nueva instancia de la actividad. Por tanto hay que leer los datos que se pasan
            mUsuarioLoginId = this.getIntent().getExtras().getInt("usuarioLoginId");
            Log.e(LOGTAG,"null - El usuario es: "+mUsuarioLoginId);
        } else {
            //Se leen los datos que se guardaron cuando la actividad fue destruida
            mUsuarioLoginId = savedInstanceState.getInt("usuarioLoginId");
            Log.e(LOGTAG,"El usuario es: "+mUsuarioLoginId);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Lo que hay que guardar
        outState.putInt("usuarioLoginId", mUsuarioLoginId);
        Log.e(LOGTAG,"onSaveInstanceState. El usuario es: "+mUsuarioLoginId);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent act;
        int id = item.getItemId();

        if (id == R.id.nav_cursos_ciclos) {
            act = new Intent(this, CiclosCursosGestionActivity.class);
            this.startActivity(act);
        } else if (id == R.id.nav_estudios) {
            act = new Intent(this, EstudiosGestionActivity.class);
            this.startActivity(act);
        } else if (id == R.id.nav_convalidacionesposibles) {
            act = new Intent(this, ConvalidacionesPosiblesGestionActivity.class);
            this.startActivity(act);
        } else if (id == R.id.nav_nueva_solicitud) {
            act = new Intent(this, SolicitudAsistenteActivity.class);
            act.putExtra("usuarioLoginId", mUsuarioLoginId);
            act.putExtra("ID", G.SIN_VALOR_INT);
            this.startActivity(act);
        } else if (id == R.id.nav_todas_las_solicitudes) {
            act = new Intent(this, SolicitudesGestionActivity.class);
            act.putExtra("usuarioLoginId", mUsuarioLoginId);
            this.startActivity(act);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
