package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private static final String FORMAT_MMDD = "MMMd";
    private static final String FORMAT_KKMM = "kk:mm";
    private static final String FORMAT_YYYYMMDDKKMM = "yyyyMMMdkkmm";

    @NonNull
    public static String getMonthDate(Date date, Context context) {
        return getMonthDate(date, LocaleUtil.getCurrentLocale(context), context);
    }

    @NonNull
    public static String getMonthDate(Date date, Locale locale, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String pattern = DateFormat.getBestDateTimePattern(locale, FORMAT_MMDD);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
            return sdf.format(date);
        } else {
            int flag = DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_NO_YEAR;
            Formatter f = new Formatter(new StringBuilder(50), LocaleUtil.getCurrentLocale(context));
            return DateUtils.formatDateRange(context, f, date.getTime(), date.getTime(), flag).toString();
        }
    }

    @NonNull
    public static String getHourMinute(Date date, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Locale locale = LocaleUtil.getCurrentLocale(context);
            String pattern = DateFormat.getBestDateTimePattern(locale, FORMAT_KKMM);
            return new SimpleDateFormat(pattern, locale).format(date);
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
            java.text.DateFormat dayOfWeekFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
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
