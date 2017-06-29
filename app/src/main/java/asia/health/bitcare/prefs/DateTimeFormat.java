package asia.health.bitcare.prefs;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Leon on 12/21/2016.
 */

public final class DateTimeFormat {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd [aa]hh:mm:ss", Locale.ENGLISH);
    public static final SimpleDateFormat TIME_FORMAT_12H= new SimpleDateFormat("hh:mm:ss:aa",Locale.ENGLISH);
    public static final SimpleDateFormat TIME_FORMAT_24H= new SimpleDateFormat("HHmmss",Locale.ENGLISH);

}
