package io.github.droidkaigi.confsched.dao;

import com.github.gfx.android.orma.Inserter;
import com.github.gfx.android.orma.TransactionTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.github.droidkaigi.confsched.model.Category;
import io.github.droidkaigi.confsched.model.Category_Relation;
import io.github.droidkaigi.confsched.model.Category_Selector;
import io.github.droidkaigi.confsched.model.OrmaDatabase;
import io.github.droidkaigi.confsched.model.Place;
import io.github.droidkaigi.confsched.model.Place_Relation;
import io.github.droidkaigi.confsched.model.Place_Selector;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.model.Session_Relation;
import io.github.droidkaigi.confsched.model.Speaker;
import io.github.droidkaigi.confsched.model.Speaker_Relation;
import io.github.droidkaigi.confsched.model.Speaker_Selector;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link SessionDao}.
 */
@RunWith(JUnit4.class)
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
        SESSION1.category = CATEGORY1;
        SESSION1.place = PLACE1;
        SESSION1.speaker = SPEAKER1;
        SESSIONS.add(SESSION1);
    }

    private @Mock OrmaDatabase mockOrmaDatabase;
    
    private @Mock Speaker_Relation mockSpeakerRelation;
    private @Mock Category_Relation mockCategoryRelation;
    private @Mock Place_Relation mockPlaceRelation;
    private @Mock Session_Relation mockSessionRelation;

    private @Mock Speaker_Selector mockSpeakerSelector;
    private @Mock Place_Selector mockPlaceSelector;
    private @Mock Category_Selector mockCategorySelector;

    private @Mock Inserter<Speaker> mockSpeakerInserter;
    private @Mock Inserter<Place> mockPlaceInserter;
    private @Mock Inserter<Category> mockCategoryInserter;
    private @Mock Inserter<Session> mockSessionInserter;

    private @Captor ArgumentCaptor<TransactionTask> transactionTaskCaptor;

    private SessionDao sessionDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionDao = new SessionDao(mockOrmaDatabase);

        when(mockOrmaDatabase.relationOfCategory()).thenReturn(mockCategoryRelation);
        when(mockOrmaDatabase.relationOfSpeaker()).thenReturn(mockSpeakerRelation);
        when(mockOrmaDatabase.relationOfPlace()).thenReturn(mockPlaceRelation);
        when(mockOrmaDatabase.relationOfSession()).thenReturn(mockSessionRelation);
        when(mockSpeakerRelation.selector()).thenReturn(mockSpeakerSelector);
        when(mockCategoryRelation.selector()).thenReturn(mockCategorySelector);
        when(mockPlaceRelation.selector()).thenReturn(mockPlaceSelector);
        when(mockSpeakerRelation.inserter()).thenReturn(mockSpeakerInserter);
        when(mockPlaceRelation.inserter()).thenReturn(mockPlaceInserter);
        when(mockCategoryRelation.inserter()).thenReturn(mockCategoryInserter);
        when(mockSessionRelation.inserter()).thenReturn(mockSessionInserter);
    }

    @Test
    public void testInsertAll() throws Exception {
        // This test assumes existing rows don't exist
        when(mockPlaceSelector.idEq(PLACE_ID1)).thenReturn(mockPlaceSelector);
        when(mockCategorySelector.idEq(CATEGORY_ID1)).thenReturn(mockCategorySelector);
        when(mockSpeakerSelector.idEq(SPEAKER_ID1)).thenReturn(mockSpeakerSelector);
        when(mockPlaceSelector.count()).thenReturn(0);
        when(mockCategorySelector.count()).thenReturn(0);
        when(mockSpeakerSelector.count()).thenReturn(0);

        sessionDao.insertAll(SESSIONS);

        verify(mockOrmaDatabase).transactionAsync(transactionTaskCaptor.capture());
        transactionTaskCaptor.getValue().execute();

        verify(mockSpeakerInserter).execute(SPEAKER1);
        verify(mockPlaceInserter).execute(PLACE1);
        verify(mockCategoryInserter).execute(CATEGORY1);
        verify(mockSessionInserter).executeAll(SESSIONS);
    }
}
