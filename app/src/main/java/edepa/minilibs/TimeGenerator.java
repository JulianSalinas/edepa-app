package edepa.minilibs;

import android.content.Context;

import java.util.Date;
import java.util.Calendar;
import edepa.modelview.R;


public class TimeGenerator {

    private Context context;

    public TimeGenerator(Context context) {
        this.context = context;
    }

    /**
     * Se obtiene la fecha actual
     */
    private Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private String getTimeString(int resource){
        return context.getResources().getString(resource);
    }

    /**
     * Se obtiene un string indicando hace cuanto tiempo
     * sucedió un evento
     * @param time: Fecha del evento que ha sucedido
     * @return Descripción de hjace cuanto sucedió un evento
     */
    public String getTimeAgo(Long time) {

        Date curDate = currentDate();
        long now = curDate.getTime();

        if (time > now || time <= 0)
            return getTimeString(R.string.date_util_just_now);

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo;

        if (dim == 1)
            return "1 " + getTimeString(R.string.date_util_unit_minute);

        else if (dim == 0)
            timeAgo =
                    getTimeString(R.string.date_util_term_less) + " " +
                    getTimeString(R.string.date_util_term_a) + " " +
                    getTimeString(R.string.date_util_unit_minute);

        else if (dim >= 2 && dim <= 44)
            timeAgo = dim + " " + getTimeString(R.string.date_util_unit_minutes);

        else if (dim >= 45 && dim <= 89)
            timeAgo =
                    getTimeString(R.string.date_util_prefix_about) + " " +
                    getTimeString(R.string.date_util_term_an)+ " " +
                    getTimeString(R.string.date_util_unit_hour);

        else if (dim >= 90 && dim <= 1439)
            timeAgo =
                    getTimeString(R.string.date_util_prefix_about) + " " +
                    (Math.round(dim / 60)) + " " +
                    getTimeString(R.string.date_util_unit_hours);

        else if (dim >= 1440 && dim <= 2519)
            timeAgo = "1 " + getTimeString(R.string.date_util_unit_day);

        else if (dim >= 2520 && dim <= 43199)
            timeAgo =
                    (Math.round(dim / 1440)) + " " +
                    getTimeString(R.string.date_util_unit_days);

        else if (dim >= 43200 && dim <= 86399)
            timeAgo =
                    getTimeString(R.string.date_util_prefix_about) + " "+
                    getTimeString(R.string.date_util_term_a)+ " " +
                    getTimeString(R.string.date_util_unit_month);

        else if (dim >= 86400 && dim <= 525599)
            timeAgo =
                    (Math.round(dim / 43200)) + " " +
                    getTimeString(R.string.date_util_unit_months);

        else if (dim >= 525600 && dim <= 655199)
            timeAgo =
                    getTimeString(R.string.date_util_prefix_about) + " "+
                    getTimeString(R.string.date_util_term_a)+ " " +
                    getTimeString(R.string.date_util_unit_year);

        else if (dim >= 655200 && dim <= 914399)
            timeAgo =
                    getTimeString(R.string.date_util_prefix_over) + " "+
                    getTimeString(R.string.date_util_term_a)+ " " +
                    getTimeString(R.string.date_util_unit_year);

        else if (dim >= 914400 && dim <= 1051199)
            timeAgo =
                    getTimeString(R.string.date_util_prefix_almost) + " 2 " +
                    getTimeString(R.string.date_util_unit_years);

        else timeAgo =
                    getTimeString(R.string.date_util_prefix_about) + " " +
                    (Math.round(dim / 525600)) + " " +
                    getTimeString(R.string.date_util_unit_years);

        return timeAgo + " " + getTimeString(R.string.date_util_suffix);
    }

    private int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

}
