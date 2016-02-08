package io.github.droidkaigi.confsched.util;

import android.content.Intent;
import android.net.Uri;

public final class IntentUtil {

    private IntentUtil() {
        throw new AssertionError();
    }

    public static Intent toBrowser(String url) {
        Uri uri = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

}
