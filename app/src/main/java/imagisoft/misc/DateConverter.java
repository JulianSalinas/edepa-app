package imagisoft.misc;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import imagisoft.modelview.R;

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

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(Context ctx, Long time) {

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return ctx.getResources().getString(R.string.date_util_just_now);
        }

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (dim == 0) {
            timeAgo = ctx.getResources().getString(R.string.date_util_term_less) + " " +  ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim == 1) {
            return "1 " + ctx.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim >= 2 && dim <= 44) {
            timeAgo = dim + " " + ctx.getResources().getString(R.string.date_util_unit_minutes);
        } else if (dim >= 45 && dim <= 89) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " "+ctx.getResources().getString(R.string.date_util_term_an)+ " " + ctx.getResources().getString(R.string.date_util_unit_hour);
        } else if (dim >= 90 && dim <= 1439) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 60)) + " " + ctx.getResources().getString(R.string.date_util_unit_hours);
        } else if (dim >= 1440 && dim <= 2519) {
            timeAgo = "1 " + ctx.getResources().getString(R.string.date_util_unit_day);
        } else if (dim >= 2520 && dim <= 43199) {
            timeAgo = (Math.round(dim / 1440)) + " " + ctx.getResources().getString(R.string.date_util_unit_days);
        } else if (dim >= 43200 && dim <= 86399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " "+ctx.getResources().getString(R.string.date_util_term_a)+ " " + ctx.getResources().getString(R.string.date_util_unit_month);
        } else if (dim >= 86400 && dim <= 525599) {
            timeAgo = (Math.round(dim / 43200)) + " " + ctx.getResources().getString(R.string.date_util_unit_months);
        } else if (dim >= 525600 && dim <= 655199) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " "+ctx.getResources().getString(R.string.date_util_term_a)+ " " + ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 655200 && dim <= 914399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_over) + " "+ctx.getResources().getString(R.string.date_util_term_a)+ " " + ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 914400 && dim <= 1051199) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_almost) + " 2 " + ctx.getResources().getString(R.string.date_util_unit_years);
        } else {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 525600)) + " " + ctx.getResources().getString(R.string.date_util_unit_years);
        }

        return timeAgo + " " + ctx.getResources().getString(R.string.date_util_suffix);
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

}
