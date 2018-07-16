package imagisoft.misc;

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

        return (hour.equals("0")  ? "12": hour)
                + ":" + (minute.length() == 2
                ? minute : "0" + minute) + " " + ampm;
    }

    /**
     * Convierte un fecha en string a un Long
     * SIEMPRE debe tener el siguiente formato dd/mm/yy hh:mm <am|pm>
     * @param datetime Fecha como el siguiente ejemplo "12/12/18 2:30 pm"
     * @return Long de la fecha especificada. Si hay error retorna 0L.
     */
    public static Long stringToLong(String datetime) throws Exception{
        Locale locale = Locale.ENGLISH;
        String pattern = "dd/M/yy h:mm a";
        SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
        return format.parse(datetime).getTime();
    }

    /**
     * Dada una fecha, se obtiene el inicio del día
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

    /**
     * Dada una fecha, se obtiene el fin del día
     * Por ejemplo "12/12/18 2:30 pm" es redondado a "12/12/18 11:59 pm"
     * @param datetime: Fecha a redondear
     * @return Fecha redondeada
     */
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
