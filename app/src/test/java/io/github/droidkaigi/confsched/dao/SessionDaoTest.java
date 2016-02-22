package io.github.droidkaigi.confsched.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import io.github.droidkaigi.confsched.model.Category;
import io.github.droidkaigi.confsched.model.OrmaDatabase;
import io.github.droidkaigi.confsched.model.Place;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.model.Speaker;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SessionDao}.
 */
@RunWith(AndroidJUnit4.class)
public class SessionDaoTest {

    private static final int SESSION_ID1 = 11;
    private static final int SPEAKER_ID1 = 21;
    private static final int PLACE_ID1 = 31;
    private static final int CATEGORY_ID1 = 41;
    private static final Speaker SPEAKER1 = new Speaker();
    private static final Category CATEGORY1 = new Category();
    private static final Place PLACE1 = new Place();
    private static final Session SESSION1 = new Session();
    private static List<Session> SESSIONS = new ArrayList<>();

    static {
        SPEAKER1.id = SPEAKER_ID1;
        SPEAKER1.name = "speaker1";
        CATEGORY1.id = CATEGORY_ID1;
        CATEGORY1.name = "category1";
        PLACE1.id = PLACE_ID1;
        PLACE1.name = "place1";
        SESSION1.id = SESSION_ID1;
        SESSION1.title = "title1";
        SESSION1.description = "description1";
        SESSION1.stime = new Date();
        SESSION1.etime = new Date();
        SESSION1.languageId = "ja";
        SESSION1.category = CATEGORY1;
        SESSION1.place = PLACE1;
        SESSION1.speaker = SPEAKER1;
        SESSIONS.add(SESSION1);
    }

    private OrmaDatabase orma;

    private SessionDao sessionDao;

    private Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    @Before
    public void setUp() {
        orma = OrmaDatabase.builder(getContext()).name(null).build();
        sessionDao = new SessionDao(orma);
    }

    @Test
    public void testInsertAll() throws Exception {
        sessionDao.updateAllSync(SESSIONS);

        assertThat(orma.selectFromSpeaker().idEq(SPEAKER_ID1)).hasSize(1);
        assertThat(orma.selectFromPlace().idEq(PLACE_ID1)).hasSize(1);
        assertThat(orma.selectFromCategory().idEq(CATEGORY_ID1)).hasSize(1);
        assertThat(orma.selectFromSession().idEq(SESSION_ID1)).hasSize(1);
    }
}
