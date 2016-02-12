package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class AppUtilsTest {

    @Test
    public void testTwitterUrl() throws Exception {
        assertThat(AppUtil.getTwitterUrl("konifar")).isEqualTo("https://twitter.com/konifar");
    }

    @Test
    public void testGitHubUrl() throws Exception {
        assertThat(AppUtil.getGitHubUrl("konifar")).isEqualTo("https://github.com/konifar");
    }

    @Test
    public void testCurrentLanguageId() throws Exception {
        Context context = InstrumentationRegistry.getContext();
        // is not null value.
        assertThat(AppUtil.getCurrentLanguageId(context)).isNotNull();

        PrefUtil.put(context, PrefUtil.KEY_CURRENT_LANGUAGE_ID, "ja");
        // eq to languageID is put in SharedPreferences.
        assertThat(AppUtil.getCurrentLanguageId(context)).isEqualTo("ja");

        PrefUtil.remove(context, PrefUtil.KEY_CURRENT_LANGUAGE_ID);
        String defaultLanguage = Locale.getDefault().getLanguage().toLowerCase();
        if (Arrays.asList(AppUtil.SUPPORT_LANG).contains(defaultLanguage)) {
            // eq to Locale.getDefault().getLanguage() when it is supported
            assertThat(AppUtil.getCurrentLanguageId(context)).isEqualTo(defaultLanguage);
        } else {
            // eq to "en" when Locale.getDefault().getLanguage() is not supported
            assertThat(AppUtil.getCurrentLanguageId(context)).isEqualTo("en");
        }
    }
}
