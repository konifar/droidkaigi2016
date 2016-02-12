package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

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
        // AppUtil.getCurrentLanguageId does not return empty or null value.
        assertThat(AppUtil.getCurrentLanguageId(context)).isNotNull().isNotEmpty();
    }
}
