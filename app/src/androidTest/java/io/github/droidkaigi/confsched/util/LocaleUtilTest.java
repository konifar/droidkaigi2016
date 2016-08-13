package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import io.github.droidkaigi.confsched.prefs.DefaultPrefs;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class LocaleUtilTest {

    @Test
    public void testCurrentLanguageId() throws Exception {
        Context context = InstrumentationRegistry.getContext();
        // is not null value.
        assertThat(LocaleUtil.getCurrentLanguageId(context)).isNotNull();

        DefaultPrefs.get(context).putLanguageId("ja");
        // eq to languageID is put in SharedPreferences.
        assertThat(LocaleUtil.getCurrentLanguageId(context)).isEqualTo("ja");

        DefaultPrefs.get(context).removeLanguageId();
        String defaultLanguage = Locale.getDefault().getLanguage().toLowerCase();
        if (LocaleUtil.SUPPORT_LANG.contains(defaultLanguage)) {
            // eq to Locale.getDefault().getLanguage() when it is supported
            assertThat(LocaleUtil.getCurrentLanguageId(context)).isEqualTo(defaultLanguage);
        } else {
            // eq to "en" when Locale.getDefault().getLanguage() is not supported
            assertThat(LocaleUtil.getCurrentLanguageId(context)).isEqualTo("en");
        }
    }

}
