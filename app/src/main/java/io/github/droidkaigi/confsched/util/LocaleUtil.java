package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

public class LocaleUtil {

    private static final String RTL_MARK = "\u200F";
    private static final String LANG_AR = "ar";


    public static boolean shouldRtl(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int direction = TextUtils.getLayoutDirectionFromLocale(AppUtil.getCurrentLocale(context));
            return direction == View.LAYOUT_DIRECTION_RTL;
        } else {
            String languageId = AppUtil.getCurrentLanguageId(context);
            return LANG_AR.equals(languageId);
        }
    }

    public static String getRtlConsideredText(String text, Context context) {
        if (shouldRtl(context)) {
            return RTL_MARK + text;
        } else {
            return text;
        }
    }

}
