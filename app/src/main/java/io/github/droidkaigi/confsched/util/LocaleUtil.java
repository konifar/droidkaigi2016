package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;

public class LocaleUtil {

    private static final String RTL_MARK = "\u200F";

    public static boolean shouldRtl(Context context) {
        return TextUtilsCompat.getLayoutDirectionFromLocale(AppUtil.getCurrentLocale(context)) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    public static String getRtlConsideredText(String text, Context context) {
        if (shouldRtl(context)) {
            return RTL_MARK + text;
        } else {
            return text;
        }
    }

}
