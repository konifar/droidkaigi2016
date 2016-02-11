package io.github.droidkaigi.confsched;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import io.github.droidkaigi.confsched.api.DroidKaigiClient;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.di.AppModule;
import io.github.droidkaigi.confsched.model.OrmaDatabase;
import io.github.droidkaigi.confsched.model.Session;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
@TargetApi(Build.VERSION_CODES.KITKAT)
public class SessionTest {

    AppModule appModule;

    Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    List<Session> loadSessions() {
        try (InputStream is = getContext().getResources().openRawResource(R.raw.sessions_ja)) {

            Gson gson = DroidKaigiClient.createGson();
            Type t = new TypeToken<Collection<Session>>() {
            }.getType();
            return gson.fromJson(new InputStreamReader(is), t);

        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Before
    public void setUp() throws Exception {
         appModule = new AppModule((Application)getContext().getApplicationContext());
    }

    @Test
    public void testLoadingSessions() throws Exception {
        OrmaDatabase orma = appModule.provideOrmaDatabase(appModule.provideContext());
        SessionDao dao = new SessionDao(orma);
        dao.updateAllSync(loadSessions());

        assertThat(dao.sessionRelation().selector().toList()).isNotEmpty();

        Session session = dao.findAll().toBlocking().first().get(0);
        assertThat(session.category).isNotNull();
        assertThat(session.speaker).isNotNull();
        assertThat(session.place).isNotNull();
    }
}
