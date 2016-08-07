package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import io.github.droidkaigi.confsched.R;

public final class IntentUtil {

    private static final String HASH_TAG = "#droidkaigi";

    private IntentUtil() {
        throw new AssertionError();
    }

    public static void share(Context context, @NonNull String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url + " " + HASH_TAG);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }

    public static void toBrowser(Context context, @NonNull String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

}
