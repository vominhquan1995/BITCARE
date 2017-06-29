package asia.health.bitcare.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import asia.health.bitcare.R;
import asia.health.bitcare.prefs.DateTimeFormat;

/**
 * Created by HP on 13-Jan-17.
 */

public class DateTimeUtils {


    //Hide by Quan
//    /**
//     * Filter date from data(DateTime)
//     *
//     * @param datetime
//     * @param filter
//     * @return
//     */
//    public static boolean filterDate(String datetime, int filter) {
//        datetime = datetime.substring(0, 8);
//        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//        Calendar dayNow = Calendar.getInstance();
//        Calendar valueTime = Calendar.getInstance();
//        Date valueDay;
//        switch (filter) {
//
//            //case day
//            //Get latest 12 day
//            case R.id.menu_type_day:
//                dayNow.add(Calendar.DATE, -12);
//                try {
//                    valueDay = dateFormat.parse(datetime);
//                    valueTime.setTime(valueDay);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if (valueTime.after(dayNow)) { //compare value day blood pressure with day last week
//                    return true;
//                }
//                return false;
//
//            //case day
//            //Get latest 12 week
//            case R.id.menu_type_week:
//                dayNow.add(Calendar.DATE, -(7 * 12));
//                try {
//                    valueDay = dateFormat.parse(datetime);
//                    valueTime.setTime(valueDay);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if (valueTime.after(dayNow)) { //compare value day blood pressure with day last week
//                    return true;
//                }
//                return false;
//
//            //case day
//            //Get latest 12 month
//            case R.id.menu_type_month:
//                dayNow.add(Calendar.DATE, -365);
//                try {
//                    valueDay = dateFormat.parse(datetime);
//                    valueTime.setTime(valueDay);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if (valueTime.after(dayNow)) { //compare value day bloodpressure with day last week
//                    return true;
//                }
//                return false;
//
//            //Default is get all
//            default:
//                return true;
//        }
//    }
    //hide by Quan
//    public static boolean compareDate(String dateString1, String dateString2, int currentType) {
//        //Create an date format
//        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//        Calendar cal1 = Calendar.getInstance();
//        Calendar cal2 = Calendar.getInstance();
//
//        //Set time to calendar
//        Date date1;
//        Date date2;
//        try {
//            date1 = dateFormat.parse(dateString2);
//            date2 = dateFormat.parse(dateString1);
//            cal1.setTime(date1);
//            cal2.setTime(date2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        //Filter current Type , if true add value to marks Average
//        switch (currentType) {
//            case R.id.menu_type_day:
//                if (cal1.equals(cal2)) {
//                    return true;
//                }
//                return false;
//
//            case R.id.menu_type_week:
//                Calendar calDateNew = Calendar.getInstance();
//                calDateNew.setTime(cal2.getTime());
//                calDateNew.add(Calendar.DAY_OF_YEAR, -6);
//                if ((cal1.get(Calendar.DAY_OF_YEAR) > calDateNew.get(Calendar.DAY_OF_YEAR)) &&
//                        (cal1.get(Calendar.DAY_OF_YEAR) <= cal2.get(Calendar.DAY_OF_YEAR))) {
//                    return true;
//                }
//                return false;
//            case R.id.menu_type_month:
//                if ((cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))) {
//                    return true;
//                }
//                return false;
//
//            default:
//                return false;
//        }
//    }

    //add by Quan 20/02

    /**
     * @param dateString
     * @param i
     * @param currentType
     * @return
     */
    public static boolean compareTwoDate(String dateString, int i, int currentType) {
        //Create an date format
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        List<String> list = new ArrayList<>();
        switch (currentType) {
            //case day
            //Get latest 12 day
            case R.id.menu_type_day: {
                cal1.add(Calendar.DATE, -i);
                cal2.add(Calendar.DATE, (-i) - 1);
            }
            break;
            //case day
            //Get latest 12 week
            case R.id.menu_type_week:
                cal1.add(Calendar.WEEK_OF_YEAR, -i);
                cal2.add(Calendar.WEEK_OF_YEAR, (-i) - 1);
                break;

            //case day
            //Get latest 12 month
            case R.id.menu_type_month:
                cal1.add(Calendar.MONTH, -i);
                cal2.add(Calendar.MONTH, (-i) - 1);
                break;
        }
        int dateStart = Integer.parseInt(dateFormat.format(cal1.getTime()));
        int dateEnd = Integer.parseInt(dateFormat.format(cal2.getTime()));
        int dateCompare = Integer.parseInt(dateString);
        //Filter current Type , if true add value to marks Average
        if (dateStart >= dateCompare && dateEnd < dateCompare) {
            return true;
        }
        return false;
    }

    /**
     * Prepare list of date
     *
     * @param filter
     * @return
     */
    public static List<String> getDateListString(int filter) {
        List<String> list = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        int count = 0;
        switch (filter) {
            //case day
            //Get latest 12 day
            case R.id.menu_type_day:
                while (count < 12) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -count);
                    list.add(dateFormat.format(cal.getTime()));
                    count++;
                }
                break;

            //case day
            //Get latest 12 week
            case R.id.menu_type_week:
                while (count < 12) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.WEEK_OF_YEAR, -count);
                    list.add(dateFormat.format(cal.getTime()));
                    count++;
                }
                break;

            //case day
            //Get latest 12 month
            case R.id.menu_type_month:
                while (count < 12) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, -count);
                    list.add(dateFormat.format(cal.getTime()));
                    count++;
                }
                break;
        }
        return list;
    }

    /**
     * Get Date from Date Time
     *
     * @param dateTime
     * @return
     */
    public static String getDate(String dateTime) {
        String date = dateTime.substring(0, 8);
        StringBuilder s = new StringBuilder();
        s.append(date.substring(0, 4));
        s.append("-");
        s.append(date.substring(4, 6));
        s.append("-");
        s.append(date.substring(6, 8));

        return s.toString();
    }

    /**
     * Get Time from DateTime
     *
     * @param dateTime
     * @return
     */
    public static String getTime(Context context, String dateTime) {
        String time = dateTime.substring(8, 12);
        StringBuilder s = new StringBuilder();
        s.append(time.substring(0, 2));
        s.append(":");
        s.append(time.substring(2, 4));
        s.append(" " + getDateOfWeek(context, dateTime));
        return s.toString();
    }

    /**
     * Get Date name
     *
     * @param context
     * @param dateTime
     * @return
     */
    private static String getDateOfWeek(Context context, String dateTime) {
        String date = dateTime.substring(0, 8);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.US);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date valueDate = null;
        try {
            valueDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (sdf.format(valueDate)) {
            case "Mon":
                return context.getString(R.string.monday);
            case "Tue":
                return context.getString(R.string.tuesday);
            case "Wed":
                return context.getString(R.string.wednesday);
            case "Thu":
                return context.getString(R.string.thursday);
            case "Fri":
                return context.getString(R.string.friday);
            case "Sat":
                return context.getString(R.string.saturday);
            case "Sun":
                return context.getString(R.string.sunday);
            default:
                return null;
        }
    }

    public static String parseTime12to24(String time, String type) {
        try {
            String type_day = type.equals("[오전]") ? "AM " : "PM";
            Date date = DateTimeFormat.TIME_FORMAT_12H.parse(time + ":" + type_day);
            return DateTimeFormat.TIME_FORMAT_24H.format(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
