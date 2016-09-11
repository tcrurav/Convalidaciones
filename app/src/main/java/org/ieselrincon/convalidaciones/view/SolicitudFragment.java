package org.ieselrincon.convalidaciones.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.constantes.Utilidades;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionPosibleEnSolicitud;
import org.ieselrincon.convalidaciones.model.pojos.Curso;
import org.ieselrincon.convalidaciones.model.pojos.Solicitud;
import org.ieselrincon.convalidaciones.model.pojos.Usuario;
import org.ieselrincon.convalidaciones.model.proveedor.Contrato;
import org.ieselrincon.convalidaciones.model.proveedor.ConvalidacionPosibleEnSolicitudProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.CursoProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.SolicitudProveedor;
import org.ieselrincon.convalidaciones.model.proveedor.UsuarioProveedor;

import java.util.ArrayList;
import java.util.Calendar;


public class SolicitudFragment extends Fragment {

    private static final String ARG_NUM = "num";
    private static final String ARG_FUNCION = "funcion";
    //private static final String ARG_ID = "id";
    //private static final String ARG_TIPOUSUARIOLOGIN = "tipoUsuarioLogin";
    //private static final String ARG_USUARIOLOGINID = "usuarioLoginId";

    private int mNum;
    private int mFuncion;
    //private int mId;
    //private int mTipoUsuarioLogin;
    //private int mUsuarioLoginId;

    //private static Solicitud mSolicitudDeTrabajo;

    private static TextView mFechaView;
    private static LinearLayout mLLFechaView;
    private TextView mCursoAcademicoView;
    private TextView mCursoView;
    private static LinearLayout mLLCursoView;
    private TextView mUsuarioView;
    private static LinearLayout mLLUsuarioView;
    private TextView mTelefonoView;
    private TextView mNifView;

    private int mModo = G.INSERTAR; //ARREGLAR

    private static OnSolicitudFragmentInteractionListener mListener;

    public SolicitudFragment() {
        // Required empty public constructor
    }

    public static SolicitudFragment newInstance(int num, int funcion//, //int id, int tipoUsuarioLogin,int usuarioLoginId
                                                ) {
        SolicitudFragment fragment = new SolicitudFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM, num);
        args.putInt(ARG_FUNCION, funcion);
        //args.putInt(ARG_ID, id);
        //args.putInt(ARG_TIPOUSUARIOLOGIN, tipoUsuarioLogin);
        //args.putInt(ARG_USUARIOLOGINID, usuarioLoginId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNum = getArguments().getInt(ARG_NUM);
            mFuncion = getArguments().getInt(ARG_FUNCION);
            //mId = getArguments().getInt(ARG_ID);
            //mTipoUsuarioLogin = getArguments().getInt(ARG_TIPOUSUARIOLOGIN);
            //mUsuarioLoginId = getArguments().getInt(ARG_USUARIOLOGINID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_solicitud, container, false);

        mFechaView = (TextView) v.findViewById(R.id.edittext_solicitud_fecha);
        mLLFechaView = (LinearLayout) v.findViewById(R.id.ll_solicitud_fecha);
        mCursoAcademicoView = (TextView) v.findViewById(R.id.edittext_solicitud_cursoacademico);
        mCursoView = (TextView) v.findViewById(R.id.edittext_solicitud_curso);
        mLLCursoView = (LinearLayout) v.findViewById(R.id.ll_solicitud_curso);

        mUsuarioView = (TextView) v.findViewById(R.id.edittext_solicitud_usuario);
        mTelefonoView = (TextView) v.findViewById(R.id.edittext_solicitud_telefono);
        mNifView = (TextView) v.findViewById(R.id.edittext_solicitud_dni);
        mLLUsuarioView = (LinearLayout) v.findViewById(R.id.ll_solicitud_usuario);

        llenarDatos();

        //mFormView = findViewById(R.id.SolicitudDetalle_Form);
        //mStatusView = findViewById(R.id.SolicitudDetalle_Status);

        //mStatusMessageView = (TextView) findViewById(R.id.SolicitudDetalle_StatusMessage);

        switch(mModo){
            case G.INSERTAR:
            case G.ACTUALIZAR:
                mLLFechaView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialogFecha();
                    }
                });

                mLLCursoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        elegirCurso();
                    }
                });

                mLLUsuarioView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editarUsuario();
                    }
                });

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
                                        ((SolicitudAsistenteActivity) getActivity()).mViewPager.setCurrentItem(1);
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
            case G.VER:
            default:
                break;
        }

        return v;
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

                //((SolicitudAsistenteActivity) getActivity()).mPaso.setText(getString(R.string.prompt_solicitud_asistente_paso1));
            }
        }
    }

    private void elegirCurso(){
        Usuario usuario = UsuarioProveedor.getRecord(getActivity().getContentResolver(),((SolicitudAsistenteActivity) getActivity()).mUsuarioLoginId);
        Intent act = new Intent(getActivity(), CursoSeleccionarActivity.class);
        act.putExtra("ID",G.SIN_VALOR_INT);
        act.putExtra("tipoUsuarioLogin", usuario.getTipoUsuario().name());
        startActivityForResult(act,G.SELECCIONADO_CURSO);
    }

    private void editarUsuario(){
        Solicitud solicitud = ((SolicitudAsistenteActivity)getActivity()).mSolicitudDeTrabajo;

        Intent act = new Intent(getActivity(), UsuarioDetalleActivity.class);
        act.putExtra("ID", solicitud.getUsuario().getID());
        act.putExtra("usuarioLoginId", ((SolicitudAsistenteActivity) getActivity()).mUsuarioLoginId);
        act.putExtra("solicitudID",solicitud.getID()); //Esto no debería ir - ARREGLAR
        //act.putExtra("tipoUsuarioLogin",mTipoUsuarioLogin);
        startActivityForResult(act,G.SELECCIONADO_CURSO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case G.SELECCIONADO_CURSO:
                if(data != null) { //Es decir si no viene después de que el usuario pulse el HomeUp
                    int cursoCambiado = data.getIntExtra("SeleccionadoID", 0);
                    if (cursoCambiado!=((SolicitudAsistenteActivity)getActivity()).mSolicitudDeTrabajo.getCurso().getID()) {
                        Curso curso = CursoProveedor.getRecord(getActivity().getContentResolver(), cursoCambiado);
                        ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.setCurso(curso);
                        mCursoView.setText(getNombreAbreviado(curso));
                        String selection = Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD + " = " +
                                ((SolicitudAsistenteActivity)getActivity()).mSolicitudDeTrabajo.getID();
                        ArrayList<ConvalidacionPosibleEnSolicitud> registrosObsoletos =
                                ConvalidacionPosibleEnSolicitudProveedor.getRecords(getActivity().getContentResolver(), selection);
                        for(ConvalidacionPosibleEnSolicitud registro: registrosObsoletos){
                            //Borrar todos los registros que eran de otro curso
                            try {
                                ConvalidacionPosibleEnSolicitudProveedor.deleteRecord(getActivity().getContentResolver(),
                                        registro.getID(),true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        SolicitudProveedor.updateRecord(getActivity().getContentResolver(),((SolicitudAsistenteActivity)getActivity()).mSolicitudDeTrabajo, true);
                    }

                }
                break;
        }
    }

    private void llenarDatos() {
        Solicitud solicitud = ((SolicitudAsistenteActivity)getActivity()).mSolicitudDeTrabajo;

        mFechaView.setText(Utilidades.transformarFechaString(solicitud.getFecha()));

        mCursoAcademicoView.setText(solicitud.getCursoAcademico().getCursoAcademico());

        mCursoView.setText(getNombreAbreviado(solicitud.getCurso()));

        mUsuarioView.setText(solicitud.getUsuario().getNombre()+" "+solicitud.getUsuario().getApellidos());

        mTelefonoView.setText(solicitud.getUsuario().getTelefono());

        mNifView.setText(solicitud.getUsuario().getDni());
    }

    private String getNombreAbreviado(Curso curso) {
        int anno = curso.getAnno();
        String turno = curso.getTurno().name();
        String abreviatura = curso.getCiclo().getAbreviatura();
        return anno + "º" + abreviatura + "-" + turno;
    }

    public static class FechaDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            Calendar cal = Calendar.getInstance();
            cal.setTime(Utilidades.transformarFechaDate(((SolicitudAsistenteActivity)getActivity()).mSolicitudDeTrabajo.getFecha()));
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            mFechaView.setText(Utilidades.transformarFechaString(year, month, day));

            ((SolicitudAsistenteActivity)getActivity()).mSolicitudDeTrabajo.setFecha(Utilidades.transformarFechaInt(year, month, day));

            SolicitudProveedor.updateRecord(getActivity().getContentResolver(),((SolicitudAsistenteActivity)getActivity()).mSolicitudDeTrabajo, true);

        }

    }

    public void showDatePickerDialogFecha() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogFragment newFragment = new FechaDatePickerFragment();
        newFragment.show(fragmentManager, "datePickerFecha");
    }

    // TODO: Rename method, update argument and hook method into UI event
    //public void onButtonPressed(Uri uri) {
    //    if (mListener != null) {
    //        mListener.onSolicitudFragmentInteractionCambiaCurso(uri);
    //    }
    //}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSolicitudFragmentInteractionListener) {
            mListener = (OnSolicitudFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSolicitudFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSolicitudFragmentInteractionListener {
        void onSolicitudFragmentInteractionCambiaSolicitud(Solicitud solicitud);
    }


}
