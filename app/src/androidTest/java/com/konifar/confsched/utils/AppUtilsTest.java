package com.konifar.confsched.utils;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class AppUtilsTest {

    @Test
    public void testTwitterUrl() throws Exception {
        assertThat(AppUtils.getTwitterUrl("konifar")).isEqualTo("https://twitter.com/konifar");
    }

    @Test
    public void testGitHubUrl() throws Exception {
        assertThat(AppUtils.getGitHubUrl("konifar")).isEqualTo("https://github.com/konifar");
    }

}
