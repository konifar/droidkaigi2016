package io.github.droidkaigi.confsched.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Locale;

import io.github.droidkaigi.confsched.R;

public class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();

    private static final String TWITTER_URL = "https://twitter.com/";
    private static final String GITHUB_URL = "https://github.com/";
    private static final String FACEBOOK_URL = "https://www.facebook.com/";

    private static final String LANG_STRING_RES_PREFIX = "lang_";
    private static final String STRING_RES_TYPE = "string";
    private static final String LANG_EN_ID = "en";
    public static final String[] SUPPORT_LANG = {LANG_EN_ID, "ja"};
    private static final Locale DEFAULT_LANG = new Locale(LANG_EN_ID);

    public static String getTwitterUrl(@NonNull String name) {
        return TWITTER_URL + name;
    }

    public static String getGitHubUrl(@NonNull String name) {
        return GITHUB_URL + name;
    }

    public static String getFacebookUrl(@NonNull String name) {
        return FACEBOOK_URL + name;
    }

    public static void initLocale(Context context) {
        setLocale(context, getCurrentLanguageId(context));
    }

    public static void setLocale(Context context, @NonNull String languageId) {
        Configuration config = new Configuration();
        PrefUtil.put(context, PrefUtil.KEY_CURRENT_LANGUAGE_ID, languageId);
        config.locale = new Locale(languageId);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static Locale getLocale() {
        return Locale.getDefault();
    }

    public static String getCurrentLanguageId(Context context) {
        String languageId = null;
        try {
            languageId = PrefUtil.get(context, PrefUtil.KEY_CURRENT_LANGUAGE_ID, null);
            if (languageId == null) {
                languageId = Locale.getDefault().getLanguage().toLowerCase();
            }
            if (!Arrays.asList(SUPPORT_LANG).contains(languageId)) {
                languageId = LANG_EN_ID;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (TextUtils.isEmpty(languageId)) {
                languageId = LANG_EN_ID;
            }
        }
        return languageId;
    }

    public static String getCurrentLanguage(Context context) {
        return getLanguage(context, getCurrentLanguageId(context));
    }

    public static String getLanguage(Context context, String languageId) {
        return getString(context, LANG_STRING_RES_PREFIX + languageId);
    }

    public static String getString(@NonNull Context context, @NonNull String resName) {
        try {
            int resourceId = context.getResources().getIdentifier(
                    resName, STRING_RES_TYPE, context.getPackageName());
            if (resourceId > 0) {
                return context.getString(resourceId);
            } else {
                Log.d(TAG, "String resource id: " + resName + " is not found.");
                return "";
            }
        } catch (Exception e) {
            Log.e(TAG, "String resource id: " + resName + " is not found.", e);
            return "";
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return context.getString(R.string.about_version_prefix, packageInfo.versionName);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return "";
        }
    }

    public static void linkify(Activity activity, TextView textView, String linkText, String url) {
        String text = textView.getText().toString();

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(text);
        builder.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        showWebPage(activity, url);
                    }
                },
                text.indexOf(linkText),
                text.indexOf(linkText) + linkText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textView.setText(builder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void showWebPage(Activity activity, @NonNull String url) {
        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(ContextCompat.getColor(activity, R.color.theme500))
                .build();

        intent.launchUrl(activity, Uri.parse(url));
    }

}
