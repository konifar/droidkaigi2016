package io.github.droidkaigi.confsched.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import io.github.droidkaigi.confsched.BuildConfig;
import io.github.droidkaigi.confsched.R;

public class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();

    private static final String TWITTER_URL = "https://twitter.com/";
    private static final String GITHUB_URL = "https://github.com/";
    private static final String FACEBOOK_URL = "https://www.facebook.com/";

    private static final String STRING_RES_TYPE = "string";

    public static String getTwitterUrl(@NonNull String name) {
        return TWITTER_URL + name;
    }

    public static String getGitHubUrl(@NonNull String name) {
        return GITHUB_URL + name;
    }

    public static String getFacebookUrl(@NonNull String name) {
        return FACEBOOK_URL + name;
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
        return context.getString(R.string.about_version_prefix, BuildConfig.VERSION_NAME);
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

    public static void setTaskDescription(Activity activity, String label, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.setTaskDescription(new ActivityManager.TaskDescription(label, null, color));
        }
    }

    public static int getThemeColorPrimary(Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

}
