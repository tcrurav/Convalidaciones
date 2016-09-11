package org.ieselrincon.convalidaciones.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;
import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.ieselrincon.convalidaciones.R;
import org.ieselrincon.convalidaciones.constantes.G;
import org.ieselrincon.convalidaciones.constantes.Utilidades;
import org.ieselrincon.convalidaciones.model.pojos.ConvalidacionPosibleEnSolicitud;
import org.ieselrincon.convalidaciones.model.pojos.Solicitud;
import org.ieselrincon.convalidaciones.model.proveedor.Contrato;
import org.ieselrincon.convalidaciones.model.proveedor.ConvalidacionPosibleEnSolicitudProveedor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

public class PDFFragment extends Fragment {
    private static final String LOGTAG = "Tiburcio - PDFFragment";
    private static final String ARG_NUM = "num";
    private static final String ARG_FUNCION = "funcion";
    private static final String ARG_TIPOUSUARIOLOGIN = "tipoUsuarioLogin";
    private static final String ARG_USUARIOLOGIN = "usuarioLogin";

    private int mNum;
    private int mFuncion;
    //private int mId;
    private int mTipoUsuarioLogin;
    private int mUsuarioLogin;

    View vistaTotal;

    PDFView mPdfView;

    //private static Solicitud mSolicitudDeTrabajo;

    //private static Button mCrearPDFView;
    //private static Button mMostrarPDFView;
    //private static Button mEnviarPDFView;

    private static ImageButton mMostrarPDFView;


    private int mModo = G.INSERTAR; //ARREGLAR

    //private static OnSolicitudFragmentInteractionListener mListener;

    public PDFFragment() {
        // Required empty public constructor
    }

    public static PDFFragment newInstance(int num, int funcion, int tipoUsuarioLogin, int usuarioLogin) {
        PDFFragment fragment = new PDFFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM, num);
        args.putInt(ARG_FUNCION, funcion);
        args.putInt(ARG_TIPOUSUARIOLOGIN, tipoUsuarioLogin);
        args.putInt(ARG_USUARIOLOGIN, usuarioLogin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNum = getArguments().getInt(ARG_NUM);
            mFuncion = getArguments().getInt(ARG_FUNCION);
            mTipoUsuarioLogin = getArguments().getInt(ARG_TIPOUSUARIOLOGIN);
            mUsuarioLogin = getArguments().getInt(ARG_USUARIOLOGIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pdf, container, false);

        vistaTotal = v;

        //vistaTotal = (LinearLayout) v.findViewById(R.id.ll_pdf);

        //mCrearPDFView = (Button) v.findViewById(R.id.button_pdf_crearpdf);
        //mMostrarPDFView = (Button) v.findViewById(R.id.button_pdf_mostrarpdf);
        //mEnviarPDFView = (Button) v.findViewById(R.id.button_pdf_enviarpdf);

        mMostrarPDFView = (ImageButton) v.findViewById(R.id.imageButtonMostrar);

        mPdfView = (PDFView) v.findViewById(R.id.pdfView);

        //mFormView = findViewById(R.id.SolicitudDetalle_Form);
        //mStatusView = findViewById(R.id.SolicitudDetalle_Status);

        //mStatusMessageView = (TextView) findViewById(R.id.SolicitudDetalle_StatusMessage);

        switch(mModo){
            case G.INSERTAR:
            case G.ACTUALIZAR:
                /*mCrearPDFView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pdfName = "pdfSolicitud"
                                //+ sdf.format(Calendar.getInstance().getTime())
                                + ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getID()
                                + ".pdf";
                        createPDFconiTextG(pdfName);
                    }
                });
                mMostrarPDFView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pdfName = "pdfSolicitud"
                                //+ sdf.format(Calendar.getInstance().getTime())
                                + ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getID()
                                + ".pdf";
                        mostrarPDF(pdfName);
                        //showPDFViewer(pdfName);
                    }
                });
                mEnviarPDFView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pdfName = "pdfSolicitud"
                            //+ sdf.format(Calendar.getInstance().getTime())
                            + ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getID()
                            + ".pdf";
                    enviarPDF(pdfName,
                            ((SolicitudAsistenteActivity)getActivity()).mSolicitudDeTrabajo.getUsuario().getEmail());
                }
            });
            */
                mMostrarPDFView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pdfName = "pdfSolicitud"
                                //+ sdf.format(Calendar.getInstance().getTime())
                                + ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getID()
                                + ".pdf";
                        createPDFconiTextG(pdfName);
                        mostrarPDF(pdfName);
                        //showPDFViewer(pdfName);
                    }
                });
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

                ((SolicitudAsistenteActivity) getActivity()).mFab.setImageResource(R.drawable.ic_email);

                //((SolicitudAsistenteActivity) getActivity()).mPaso.setText(getString(R.string.prompt_solicitud_asistente_paso3));
            }
        }
    }

    public void createPDFconiTextG(String pdfName){
        Document doc = new Document();
        //String outPath = Environment.getExternalStorageDirectory() + "/" + pdfName;
        String outPath = getContext().getExternalFilesDir(null).getAbsolutePath()+ "/" + pdfName;
        Log.e(LOGTAG,outPath);

        try{

            Font boldFontCabecera = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            Font boldFontSubCabecera = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            Font fontNormal = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
            Font fontReducida = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.ITALIC);

            PdfWriter.getInstance(doc, new FileOutputStream(outPath));
            doc.open();

            TextView texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_cabecera1);
            Paragraph parrafo = new Paragraph(texto.getText().toString(), boldFontCabecera);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            //parrafo.setFont(boldFontCabecera);
            doc.add(parrafo);

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_cabecera2);
            parrafo = new Paragraph(texto.getText().toString(), boldFontCabecera);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            //parrafo.setFont(boldFontCabecera);
            doc.add(parrafo);

            doc.add( Chunk.NEWLINE );
            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_datospersonales);
            String nombreCompleto = ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getUsuario().getNombre() + " " +
                    ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getUsuario().getApellidos();
            String nif = ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getUsuario().getDni();
            String telefono = ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getUsuario().getTelefono();
            texto.setText("D./Dª " + nombreCompleto + " con NIF " + nif + " y teléfono a efectos de comunicación " + telefono + ".");
            parrafo = new Paragraph(texto.getText().toString(), fontNormal);
            parrafo.setFont(fontNormal);
            doc.add(parrafo);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_expone);
            parrafo = new Paragraph(texto.getText().toString(), boldFontSubCabecera);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            parrafo.setFont(boldFontSubCabecera);
            doc.add(parrafo);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_punto1);
            parrafo = new Paragraph(texto.getText().toString(), fontNormal);
            parrafo.setFont(fontNormal);
            parrafo.setIndentationLeft(25);
            doc.add(parrafo);

            //Aquí van los estudios aportados

            Solicitud solicitud = ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo;
            String selection = Contrato.ConvalidacionPosibleEnSolicitud.FK_SOLICITUD + " = " + solicitud.getID();
            ArrayList<ConvalidacionPosibleEnSolicitud> convalidacionesPosiblesEnSolicitud = ConvalidacionPosibleEnSolicitudProveedor.getRecords(
                    getActivity().getContentResolver(), selection);
            List unOrderedList = new List(List.UNORDERED);
            if(convalidacionesPosiblesEnSolicitud.size()==0){
                unOrderedList.add(new ListItem("No se aporta nada", fontNormal));
            }
            for(ConvalidacionPosibleEnSolicitud registro: convalidacionesPosiblesEnSolicitud){
                ListItem listItem = new ListItem(registro.getConvalidacionPosible().getEstudioAportado().getNombre(), fontNormal);
                listItem.setFont(fontNormal);
                unOrderedList.add(listItem);
            }
            unOrderedList.setIndentationLeft(50);
            doc.add(unOrderedList);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_punto2);
            String cursoAcademico = ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getCursoAcademico().getCursoAcademico() + " " +
                    ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getUsuario().getApellidos();
            String curso = String.valueOf(((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getCurso().getAnno());
            String ciclo = ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getCurso().getCiclo().getNombre();
            String turno = ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getCurso().getTurno().getTurno();
            texto.setText("2. Que estoy matriculado/a en el IES El Rincón en el año académico " + cursoAcademico + " en el curso " + curso +
                    " del ciclo " + ciclo + " en el turno " + turno + ".");
            parrafo = new Paragraph(texto.getText().toString(), fontNormal);
            parrafo.setFont(fontNormal);
            parrafo.setIndentationLeft(25);
            doc.add(parrafo);

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_punto3);
            parrafo = new Paragraph(texto.getText().toString(), fontNormal);
            parrafo.setFont(fontNormal);
            parrafo.setIndentationLeft(25);
            doc.add(parrafo);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_solicita);
            parrafo = new Paragraph(texto.getText().toString(), boldFontSubCabecera);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            parrafo.setFont(boldFontSubCabecera);
            doc.add(parrafo);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_encabezado_estudiosconvalidados);
            parrafo = new Paragraph(texto.getText().toString(), fontNormal);
            parrafo.setFont(fontNormal);
            doc.add(parrafo);

            //Aquí van los estudios convalidados
            unOrderedList = new List(List.UNORDERED);
            if(convalidacionesPosiblesEnSolicitud.size()==0){
                unOrderedList.add(new ListItem("No se aporta nada"));
            }
            for(ConvalidacionPosibleEnSolicitud registro: convalidacionesPosiblesEnSolicitud){
                ListItem listItem = new ListItem(registro.getConvalidacionPosible().getEstudioConvalidado().getNombre(), fontNormal);
                listItem.setFont(fontNormal);
                unOrderedList.add(listItem);
            }
            unOrderedList.setIndentationLeft(50);
            doc.add(unOrderedList);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_indicaciones0);
            parrafo = new Paragraph(texto.getText().toString(), fontNormal);
            parrafo.setFont(fontNormal);
            doc.add(parrafo);

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_indicaciones1);
            unOrderedList = new List(List.UNORDERED);
            ListItem listItem = new ListItem(texto.getText().toString(), fontReducida);
            listItem.setFont(fontReducida);
            unOrderedList.add(listItem);

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_indicaciones2);
            listItem = new ListItem(texto.getText().toString(), fontReducida);
            listItem.setFont(fontReducida);
            unOrderedList.add(listItem);

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_indicaciones3);
            listItem = new ListItem(texto.getText().toString(), fontReducida);
            listItem.setFont(fontReducida);
            unOrderedList.add(listItem);

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_indicaciones4);
            listItem = new ListItem(texto.getText().toString(), fontReducida);
            listItem.setFont(fontReducida);
            unOrderedList.add(listItem);

            unOrderedList.setIndentationLeft(25);

            doc.add(unOrderedList);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_fecha);
            int fecha = ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getFecha();
            Date fechaDate = Utilidades.transformarFechaDate(fecha);
            String fechaString = "Las Palmas de Gran Canaria, a " + Utilidades.getDayOfMonth(fechaDate) +
                    " de " + Utilidades.getMonth(fechaDate) +
                    " de " + Utilidades.getYear(fechaDate);
            texto.setText(fechaString);
            parrafo = new Paragraph(fechaString, fontNormal);
            parrafo.setFont(fontNormal);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            doc.add(parrafo);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_interesado);
            parrafo = new Paragraph(texto.getText().toString(), fontNormal);
            parrafo.setFont(fontNormal);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            doc.add(parrafo);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_firmado);
            parrafo = new Paragraph(texto.getText().toString(), fontNormal);
            parrafo.setFont(fontNormal);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            doc.add(parrafo);

            doc.add( Chunk.NEWLINE );

            texto = (TextView) vistaTotal.findViewById(R.id.textview_pdf_pie);
            parrafo = new Paragraph(texto.getText().toString(), fontReducida);
            parrafo.setFont(fontReducida);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            doc.add(parrafo);

            doc.close();

        } catch(Exception e){
            e.printStackTrace();
        }

    }


    private void showPDFViewer(String pdfName){
        File file = new File(getContext().getExternalFilesDir(null),pdfName);
        mPdfView.fromFile(file);
        /*
        String pathToMyAttachedFile = getContext().getExternalFilesDir(null).getAbsolutePath() + "/" + pdfName;
        mPdfView.fromAsset(pathToMyAttachedFile)
                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                //.onDraw(onDrawListener)
                //.onLoad(onLoadCompleteListener)
                //.onPageChange(onPageChangeListener)
                //.onPageScroll(onPageScrollListener)
                //.onError(onErrorListener)
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                .load(); */
    }

    private void createPDF(){
        // Create a object of PdfDocument
        PdfDocument document = new PdfDocument();

        // content view is EditText for my case in which user enters pdf content
        View content = vistaTotal;

        // crate a page info with attributes as below
        // page number, height and width
        // i have used height and width to that of pdf content view
        int pageNumber = 1;

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();

        // create a new page from the PageInfo
        PdfDocument.Page page = document.startPage(pageInfo);

        // repaint the user's text into the page
        content.draw(page.getCanvas());

        // do final processing of the page
        document.finishPage(page);

        // saving pdf document to sdcard
        //SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
        String pdfName = "pdfSolicitud"
                //+ sdf.format(Calendar.getInstance().getTime())
                + ((SolicitudAsistenteActivity) getActivity()).mSolicitudDeTrabajo.getID()
                + ".pdf";

        // all created files will be saved at path /sdcard/PDFDemo_AndroidSRC/
        //File outputFile = new File("/sdcard/PDFDemo_AndroidSRC/", pdfName);

        //File outputDir;
        //if(isExternalStorageWritable()) {
            guardarEnMemoriaExterna(document, pdfName);

        //} else {
        //    guardarEnMemoriaInterna(document, pdfName);
        //}


    }

    void guardarEnMemoriaInterna(PdfDocument document, String pdfName){

        File outputDir = getInternalAlbumStorageDir("pdfs");
        File outputFile = new File(outputDir, pdfName);

        try {
            outputFile.createNewFile();
            OutputStream out = new FileOutputStream(outputFile);
            document.writeTo(out);
            document.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOGTAG,"guardarEnMemoriaInterna");
        }
    }

    void guardarEnMemoriaExterna(PdfDocument document, String pdfName){
        //File outputDir = getAlbumStorageDir("pdfs");
        //File outputFile = new File(outputDir, pdfName);
        File outputFile = new File(getContext().getExternalFilesDir(null), pdfName);

        try {
            outputFile.createNewFile();
            OutputStream out = new FileOutputStream(outputFile);
            document.writeTo(out);
            document.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOGTAG,"guardarEnMemoriaExterna");
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory() + "/" + albumName);
        if(file.exists()){
            Log.e(LOGTAG, "Existe esta mierda");
        }
        if (!file.mkdirs()) {
            Log.e(LOGTAG, "Directory not created");
        }
        return file;
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
        //if (context instanceof OnSolicitudFragmentInteractionListener) {
        //    mListener = (OnSolicitudFragmentInteractionListener) context;
        //} else {
        //    throw new RuntimeException(context.toString()
        //            + " must implement OnSolicitudFragmentInteractionListener");
        //}
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    //public interface OnSolicitudFragmentInteractionListener {
    //    void onSolicitudFragmentInteractionCambiaSolicitud();
    //}

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getInternalAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(getContext().getFilesDir(), albumName);
        if (!file.mkdirs()) {
            Log.e(LOGTAG, "Directory Interno not created");
        }
        return file;
    }



    public void mostrarPDF(String pdfName){

        //File pdfsPath = new File(getContext().getFilesDir(), "pdfs");
        //File newFile = new File(pdfsPath, pdfName);
        //Uri contentUri = getUriForFile(getContext(), "org.ieselrincon.org.fileprovider", newFile);

        //getContext().grantUriPermission(package, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //pdfOpenintent.setData(contentUri);
        //pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //pdfOpenintent.setType("application/pdf");

        File file = new File(getContext().getExternalFilesDir(null),pdfName);
        Uri path = Uri.fromFile(file);

        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        try {
            startActivity(pdfOpenintent);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void enviarPDF(String pdfName, String email){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email, email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de Convalidación");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email con fichero de Solicitud de convalidación adjunto.");
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = getContext().getExternalFilesDir(null).getAbsolutePath() + "/" + pdfName;
        File file = new File(///root,
                 pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Escoja un Proveedor de Email"));
    }
}