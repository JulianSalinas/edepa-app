package edepa.minilibs;

import android.util.Log;
import android.content.Context;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import edepa.modelview.R;


public class TimeConverter {

    /**
     * Extrae la fecha (sin hora) de un tipo de dato Long
     * @param datetime fecha en formato Long
     * @return String con el formato dd/mm/yyyy
     */
    public static String extractDate(Long datetime) {
        Instant instant = Instant.ofEpochMilli(datetime);
        ZonedDateTime date = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC-06:00"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy");
        return date.format(dtf).toLowerCase();
    }

    /**
     * Extrae únicamente la hora de una fecha dada en formato Long
     * @param datetime fecha en formato long
     * @return String con el formato hh:mm <pm|am>
     */
    public static String extractTime(Long datetime){
        Instant instant = Instant.ofEpochMilli(datetime);
        ZonedDateTime date = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC-06:00"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");
        return date.format(dtf).toLowerCase();
    }

    /**
     * Convierte un fecha en Long a String
     * @param datetime Fecha en milisegundos
     * @return String con el formato formato dd/mm/yy hh:mm <am|pm>
     */
    public static String longToString(Long datetime){
        String date = extractDate(datetime);
        String time = extractTime(datetime);
        return String.format("%s %s", date, time);
    }

    /**
     * Convierte un fecha en string a un Long
     * SIEMPRE debe tener el siguiente formato dd/mm/yy hh:mm <am|pm>
     * @param datetime Fecha como el siguiente ejemplo "12/12/18 2:30 pm"
     * @return Long de la fecha especificada. Si hay error retorna 0L.
     */
    public static Long stringToLong(String datetime) {
        try {
            Locale locale = Locale.ENGLISH;
            String pattern = "dd/M/yy h:mm a";
            SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
            return format.parse(datetime).getTime();
        }
        catch (Exception e){
            Log.i("TimeConverter", e.getMessage());
            return System.currentTimeMillis();
        }

    }

    /**
     * Dada una fecha, se obtiene el inicio del día
     * Por ejemplo "12/12/18 2:30 pm" es redondado a "12/12/18 0:00 pm"
     * @param datetime: Fecha a redondear
     * @return Fecha redondeada
     */
    public static Long atStartOfDay(Long datetime) {
        Instant instant = Instant.ofEpochMilli(datetime);
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
        LocalDate start = zdt.toLocalDate();
        ZonedDateTime zdtStart = start.atStartOfDay(ZoneId.of("UTC"));
        return zdtStart.toInstant().toEpochMilli();
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

    /**
     * Toma la hora de inicio y crea un string para identificar
     * el inicio de un bloque de eventos
     * @param context: Del que se puede tomar R.string.text_block
     * @param start: Inicio del evento que abre el bloque
     * @return String con el formato "Bloque 12:30 PM"
     * @see #getBlockString(Context, long, long)
     */
    public static String getBlockString(Context context, long start){
        return String.format("%s %s",
                context.getString(R.string.text_block),
                TimeConverter.extractTime(start));
    }

    /**
     * Toma la hora de inicio y la hora fin  y crea un string para
     * identificar un evento
     * @param context: Del que se puede tomar R.string.text_from
     * @param start: Inicio y fin del evento
     * @return String con el formato "Desde 12:30 pm hasta 1:30 pm"
     * @see #getBlockString(Context, long)
     */
    public static String getBlockString(Context context, long start, long end){
        return String.format("%s %s %s %s",
                context.getString(R.string.text_from),
                TimeConverter.extractTime(start),
                context.getString(R.string.text_to),
                TimeConverter.extractTime(end));
    }

}
