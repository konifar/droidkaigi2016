package io.github.droidkaigi.confsched.util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class AppUtilTest {

    @Test
    public void testTwitterUrl() throws Exception {
        assertThat(AppUtil.getTwitterUrl("konifar")).isEqualTo("https://twitter.com/konifar");
    }

    @Test
    public void testGitHubUrl() throws Exception {
        assertThat(AppUtil.getGitHubUrl("konifar")).isEqualTo("https://github.com/konifar");
    }

}
