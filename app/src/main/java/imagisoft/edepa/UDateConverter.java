package imagisoft.edepa;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class UDateConverter {

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
        return hour + ":" + (minute.length() == 2 ? minute : "0" + minute)+ " " + ampm;
    }

    /**
     * Convierte un fecha en string a un Long
     * SIEMPRE debe tener el siguiente formato dd/mm/yy hh:mm <am|pm>
     * @param datetime Fecha como el siguiente ejemplo "12/12/18 2:30 pm"
     * @return Long de la fecha especificada. Si hay error retorna 0L.
     */
    public static Long stringToLong(String datetime) {
        Long value= 0L;
        try{ value = stringToLongWrapper(datetime); }
        catch (Exception e){ }
        return value;
    }

    /**
     * Cubierta para lanzar el error en caso de una fecha inválida
     * @param datetime Fecha como el siguiente ejemplo "12/12/18 2:30 pm"
     */
    private static Long stringToLongWrapper(String datetime) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yy h:mm a", Locale.ENGLISH);
        return format.parse(datetime).getTime();
    }

}
