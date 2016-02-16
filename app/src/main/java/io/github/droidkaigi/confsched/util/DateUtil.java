package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private static final String FORMAT_MMDD = "MMMd";
    private static final String FORMAT_KKMM = "kk:mm";
    private static final String FORMAT_YYYYMMDDKKMM = "yyyyMMMdkkmm";

    @NonNull
    public static String getMonthDate(Date date, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), FORMAT_MMDD);
            return new SimpleDateFormat(pattern).format(date);
        } else {
            int flag = DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_NO_YEAR;
            return DateUtils.formatDateTime(context, date.getTime(), flag);
        }
    }

    @NonNull
    public static String getHourMinute(Date date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), FORMAT_KKMM);
            return new SimpleDateFormat(pattern).format(date);
        } else {
            return String.valueOf(DateFormat.format(FORMAT_KKMM, date));
        }
    }

    @NonNull
    public static String getLongFormatDate(Date date, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), FORMAT_YYYYMMDDKKMM);
            return new SimpleDateFormat(pattern).format(date);
        } else {
            java.text.DateFormat dayOfWeekFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG);
            java.text.DateFormat shortTimeFormat = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT);
            dayOfWeekFormat.setTimeZone(LocaleUtil.getDisplayTimeZone(context));
            shortTimeFormat.setTimeZone(LocaleUtil.getDisplayTimeZone(context));
            return dayOfWeekFormat.format(date) + " " + shortTimeFormat.format(date);
        }
    }

    public static int getMinutes(Date stime, Date etime) {
        long range = etime.getTime() - stime.getTime();

        if (range > 0) {
            return (int) (range / TimeUnit.MINUTES.toMillis(1L));
        } else {
            return 0;
        }
    }

}
