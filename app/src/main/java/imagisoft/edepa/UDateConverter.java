package imagisoft.util;

import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    public static String extractDate(Long datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        return day + "/" + month + "/" + year;
    }

    public static String extractTime(Long datetime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String ampm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
        return hour + ":" + (minute.length() == 2 ? minute : "0" + minute)+ " " + ampm;
    }

    public static Long stringToLong(String string_datetime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yy h:mm a", Locale.ENGLISH);
        Date date = format.parse(string_datetime);
        return date.getTime();
    }

}
