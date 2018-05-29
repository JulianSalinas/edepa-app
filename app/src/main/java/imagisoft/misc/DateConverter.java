package imagisoft.misc;

import android.util.Log;

import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateConverter {

    /**
     * Extrae la fecha (sin hora) de un tipo de dato Long
     * @param datetime fecha en formato Long
     * @return String con el formato dd/mm/yyyy
     */
    public static String extractDate(Long datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        return day + "/" + month + "/" + year;
    }

    /**
     * Extrae únicamente la hora de una fecha dada en formato Long
     * @param datetime fecha en formato long
     * @return String con el formato hh:mm <pm|am>
     */
    public static String extractTime(Long datetime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String ampm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
        return (hour.equals("0")  ? "12": hour) + ":" + (minute.length() == 2 ? minute : "0" + minute) + " " + ampm;
    }

    /**
     * Convierte un fecha en string a un Long
     * SIEMPRE debe tener el siguiente formato dd/mm/yy hh:mm <am|pm>
     * @param datetime Fecha como el siguiente ejemplo "12/12/18 2:30 pm"
     * @return Long de la fecha especificada. Si hay error retorna 0L.
     */
    public static Long stringToLong(String datetime) {
        try{ return stringToLongWrapper(datetime); }
        catch (Exception e){ Log.i(DateConverter.class.getName(), e.getMessage()); }
        return 0L;
    }

    /**
     * Cubierta para lanzar el error en caso de una fecha inválida
     * @param datetime Fecha como el siguiente ejemplo "12/12/18 2:30 pm"
     */
    private static Long stringToLongWrapper(String datetime) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yy h:mm a", Locale.ENGLISH);
        return format.parse(datetime).getTime();
    }

    /**
     * Redondea al dia más cercano.
     * Por ejemplo "12/12/18 2:30 pm" es redondado a "12/12/18 0:00 pm"
     * @param datetime: Fecha a redondear
     * @return Fecha redondeada
     */
    public static Long atStartOfDay(Long datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Long atEndOFDay(Long datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

}
