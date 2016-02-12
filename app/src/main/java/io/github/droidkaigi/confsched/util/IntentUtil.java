package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import io.github.droidkaigi.confsched.R;

public final class IntentUtil {

    private IntentUtil() {
        throw new AssertionError();
    }

    public static Intent toBrowser(String url) {
        Uri uri = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static void share(Context context, @NonNull String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }

}
