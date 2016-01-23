package com.konifar.confsched.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private static final String FORMAT_MMDD = "MMMd";
    private static final String FORMAT_KKMM = "kk:mm";

    @NonNull
    public static String getMonthDate(Date date, Context context) {
        return getMonthDate(date, new Locale("en"), context);
    }

    @NonNull
    public static String getMonthDate(Date date, Locale locale, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String pattern = DateFormat.getBestDateTimePattern(locale, FORMAT_MMDD);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
            return sdf.format(date);
        } else {
            int flag = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;
            return DateUtils.formatDateTime(context, date.getTime(), flag);
        }
    }

    @NonNull
    public static String getHourMinute(Date date) {
        return String.valueOf(DateFormat.format(FORMAT_KKMM, date));
    }

}
