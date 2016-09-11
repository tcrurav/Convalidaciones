package org.ieselrincon.convalidaciones.constantes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tiburcio on 25/08/2016.
 */
public class Utilidades {
    public static String transformarFechaString(int fecha) {
        //De Unix-Epoche a dd/MM/yyyy
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("es", "ES"));
        long date = fecha;
        Date fechaDate = new Date(date * 1000);
        return sdf.format(fechaDate);
    }
    public static int transformarFechaInt(Date fecha) {
        //De Date a Unix-Epoche
        return (int) (fecha.getTime() / 1000L);
    }

    public static Date transformarFechaDate(int fecha) {
        /* De Unix-Epoche a Date */
        Date expiry = new Date(fecha * 1000L);
        return expiry;
    }

    public static String transformarFechaString(int year, int month, int day){
        // de año, mes y día, a dd/MM/yyyy
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,day);
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("es", "ES"));
        String fecha = sdf.format(cal.getTime());
        return fecha;
    }

    public static int transformarFechaInt(int year, int month, int day){
        // de año, mes y día, a Unix-Epoche
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,day);
        long unixTime = cal.getTimeInMillis() / 1000;
        return (int) unixTime;
    }

    public static int getDayOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public static String getMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String month = new SimpleDateFormat("MMM").format(cal.getTime());
        return month;
    }

    public static int getYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }
}
