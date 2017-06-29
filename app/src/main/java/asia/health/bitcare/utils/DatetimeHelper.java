package asia.health.bitcare.utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import asia.health.bitcare.R;

public class DatetimeHelper {

    @SuppressLint("SimpleDateFormat")
    public static Calendar getInternationalCalendar() {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = df.format(new Date());
        return getCalendar(gmtTime, new SimpleDateFormat("yyyyMMddHHmmssSSS"));
    }

    public static Calendar getVietNamCalendar() {

        Calendar calendar = getInternationalCalendar();
        long timeInMilis = calendar.getTimeInMillis() + (7 * 60 * 60 * 1000);
        calendar.setTimeInMillis(timeInMilis);
        return calendar;
    }


    public static Calendar getVietNamCalendar1() {

        Calendar calendar = Calendar.getInstance();
//        long timeInMilis = calendar.getTimeInMillis() + (7 * 60 * 60 * 1000);
//        calendar.setTimeInMillis(timeInMilis);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+07"));
        return calendar;
    }

    public static Calendar getCalendar(String dateString, SimpleDateFormat dateFormat) {
        try {

            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance();
    }

    public static String getString(Calendar calendar, SimpleDateFormat dateFormat) {

        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    public static void addOnClickDatePicker(final EditText editText, final SimpleDateFormat dateFormat,
                                            final String saveStr, final String cancelStr, boolean showCurrentTime, final OnDateSeletionListener onDateSeletionListener) {

        if (showCurrentTime) {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            editText.setText(dateFormat.format(date));
        }

        editText.setFocusable(false);
        editText.setCursorVisible(false);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerFromEditText(editText, dateFormat, saveStr, cancelStr, onDateSeletionListener);
            }
        });
    }

    public static void addFocusDatePicker(final EditText editText, final SimpleDateFormat dateFormat,
                                          final String saveStr, final String cancelStr, boolean showCurrentTime, final OnDateSeletionListener onDateSeletionListener) {

        if (showCurrentTime) {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            editText.setText(dateFormat.format(date));
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDateTimePickerFromEditText(editText, dateFormat, saveStr, cancelStr, onDateSeletionListener);
                }
            }
        });
    }

    public static void addFocusDatePicker(final EditText editText, final SimpleDateFormat dateFormat,
                                          final String saveStr, final String cancelStr, boolean showCurrentTime) {
        addFocusDatePicker(editText, dateFormat, saveStr, cancelStr, showCurrentTime, null);
    }

    private static void showDateTimePickerFromEditText(final EditText editText, final DateFormat dateFormat, final String saveStr, final String cancelStr, final OnDateSeletionListener onDateSeletionListener) {
        final DatePicker datePicker = new DatePicker(editText.getContext());
        datePicker.setCalendarViewShown(false);
        datePicker.setMaxDate(new Date().getTime());

        if (!editText.getText().toString().equals("")) {

            try {
                Date date = dateFormat.parse(editText.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                datePicker.updateDate(year, month, day);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            datePicker.updateDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }

        new AlertDialog.Builder(editText.getContext())
                .setView(datePicker)
                .setNegativeButton(cancelStr, null)
                .setPositiveButton(saveStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        Date date = cal.getTime();
                        String dateStr = dateFormat.format(date);
                        editText.setText(dateStr);

                        if (onDateSeletionListener != null) {
                            onDateSeletionListener.onDateSelected(cal, dateStr);
                        }
                    }
                })
                .show();

        editText.clearFocus();
    }

    public interface OnDateSeletionListener {

        void onDateSelected(Calendar dateVal, String dateStr);
    }

    public static void addOnClickDatePicker(final Button button, final SimpleDateFormat dateFormat,
                                            final String saveStr, final String cancelStr, boolean showCurrentTime) {

        if (showCurrentTime) {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            button.setText(dateFormat.format(date));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerFromButton(button, dateFormat, saveStr, cancelStr);
            }
        });
    }
    public static void addOnClickDatePicker(final LinearLayout linearLayout,final TextView dateTime,final TextView dayOfWeek, final TextView time, final SimpleDateFormat dateFormat,
                                            final String saveStr, final String cancelStr, boolean showCurrentTime) {

        if (showCurrentTime) {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            String day=dateFormat.format(date).substring(12,14).equals("AM") ? "[오전]" : "[오후]" ;
            dateTime.setText(dateFormat.format(date).substring(0,10));
            time.setText(dateFormat.format(date).substring(15,23));
            dayOfWeek.setText(day);
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerFromButton(linearLayout,dateTime,dayOfWeek,time, dateFormat, saveStr, cancelStr);
            }
        });
    }

    public static void addFocusDatePicker(final Button button, final SimpleDateFormat dateFormat,
                                          final String saveStr, final String cancelStr, boolean showCurrentTime) {

        if (showCurrentTime) {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            button.setText(dateFormat.format(date));
        }

        button.setFocusable(true);

        button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerFromButton(button, dateFormat, saveStr, cancelStr);
                }
            }
        });
    }

    private static void showDatePickerFromButton(final Button button, final SimpleDateFormat dateFormat,
                                                 final String saveStr, final String cancelStr) {

        final DatePicker datePicker = new DatePicker(button.getContext());
        datePicker.setCalendarViewShown(false);
        datePicker.setMaxDate(new Date().getTime());

        if (!button.getText().toString().equals("")) {

            try {
                Date date = dateFormat.parse(button.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                datePicker.updateDate(year, month, day);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            datePicker.updateDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }

        new AlertDialog.Builder(button.getContext())
                .setView(datePicker)
                .setNegativeButton(cancelStr, null)
                .setPositiveButton(saveStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        Date date = cal.getTime();
                        button.setText(dateFormat.format(date));
                    }
                })
                .show();
    }
    private static void showDatePickerFromButton(final LinearLayout linearLayout,final TextView dateTime,final TextView dayOfWeek, final TextView time, final SimpleDateFormat dateFormat,
                                                 final String saveStr, final String cancelStr) {
        SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-ddhh:mm:ss");
        String Time=(dateTime.getText().toString()+time.getText().toString());
        final DatePicker datePicker = new DatePicker(linearLayout.getContext());
        datePicker.setCalendarViewShown(false);
        datePicker.setMaxDate(new Date().getTime());

        if (!Time.toString().equals("")) {

            try {
                Date date = DATE_TIME_FORMAT.parse(Time.toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                datePicker.updateDate(year, month, day);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            datePicker.updateDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }

        new AlertDialog.Builder(linearLayout.getContext())
                .setView(datePicker)
                .setNegativeButton(cancelStr, null)
                .setPositiveButton(saveStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        Date date = cal.getTime();
                        String day=dateFormat.format(date).substring(12,14).equals("AM") ? "[오전]" : "[오후]" ;
                        dateTime.setText(dateFormat.format(date).substring(0,10));
                        time.setText(dateFormat.format(date).substring(15,23));
                        dayOfWeek.setText(day);
                    }
                })
                .show();
    }
}
