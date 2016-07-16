package io.github.droidkaigi.confsched.viewmodel;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.Calendar;

import io.github.droidkaigi.confsched.BuildConfig;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.model.Category;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AlarmUtil;
import io.github.droidkaigi.confsched.util.PageNavigator;
import io.github.droidkaigi.confsched.viewmodel.event.EventBus;
import io.github.droidkaigi.confsched.viewmodel.event.SessionSelectedChangedEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.KITKAT, manifest = "src/main/AndroidManifest.xml")
public class SessionDetailViewModelTest {

    private static final String DUMMY_URL = "https://github.com/konifar/droidkaigi2016";

    private SessionDetailViewModel viewModel;

    private Context context;
    @Mock
    private PageNavigator mockPageNavigator;
    @Mock
    private SessionDao mockSessionDao;
    @Mock
    private EventBus mockEventBus;
    @Mock
    private AlarmUtil mockAlarmUtil;
    @Mock
    private Session mockSession;

    @Before
    public void setUp() {
        context = ShadowApplication.getInstance().getApplicationContext();
        MockitoAnnotations.initMocks(this);
        viewModel = new SessionDetailViewModel(context, mockPageNavigator,
                mockSessionDao, mockEventBus, mockAlarmUtil);
    }

    @Test
    public void testSetSession() {
        viewModel.setSession(mockSession);
        assertThat(viewModel.session).isEqualTo(mockSession);
    }

    @Test
    public void testShouldShowShareMenuItem() {
        // When share url is not empty
        mockSession.shareUrl = DUMMY_URL;
        viewModel.setSession(mockSession);
        assertThat(viewModel.shouldShowShareMenuItem()).isTrue();

        // When share url is empty
        viewModel.setSession(mock(Session.class));
        assertThat(viewModel.shouldShowShareMenuItem()).isFalse();
    }

    @Test
    public void testOnClickShareMenuItem() {
        mockSession.shareUrl = DUMMY_URL;
        viewModel.setSession(mockSession);
        viewModel.onClickShareMenuItem();

        verify(mockPageNavigator, times(1)).showShareChooser(DUMMY_URL);
    }

    @Test
    public void testOnClickFeedbackButton() {
        viewModel.setSession(mockSession);
        viewModel.onClickFeedbackButton(null);

        verify(mockPageNavigator, times(1)).showFeedback(mockSession);
    }

    @Test
    public void testOnClickSlideIconWhenSlideUrlIsNotEmpty() {
        Session session = mock(Session.class);
        session.slideUrl = DUMMY_URL;
        viewModel.setSession(session);
        when(session.hasSlide()).thenReturn(true);
        viewModel.onClickSlideIcon(null);

        verify(mockPageNavigator, times(1)).showBrowser(DUMMY_URL);
    }

    @Test
    public void testOnClickSlideIconWhenSlideUrlIsEmpty() {
        viewModel.setSession(mockSession);
        when(mockSession.hasSlide()).thenReturn(false);
        viewModel.onClickSlideIcon(null);

        verify(mockPageNavigator, never()).showBrowser(DUMMY_URL);
    }

    @Test
    public void testOnClickMovieIconWhenMovieUrlIsNotEmpty() {
        mockSession.movieDashUrl = DUMMY_URL;
        viewModel.setSession(mockSession);
        when(mockSession.hasDashVideo()).thenReturn(true);
        viewModel.onClickMovieIcon(null);

        verify(mockPageNavigator, times(1)).showVideoPlayer(mockSession);
    }

    @Test
    public void testOnClickMovieIconWhenMovieUrlIsEmpty() {
        viewModel.setSession(mockSession);
        when(mockSession.hasDashVideo()).thenReturn(false);
        viewModel.onClickMovieIcon(null);

        verify(mockPageNavigator, never()).showVideoPlayer(mockSession);
    }

    @Test
    public void testOnClickFab() {
        viewModel.setSession(mockSession);
        viewModel.onClickFab(null);

        verify(mockSessionDao, times(1)).updateChecked(mockSession);
        verify(mockEventBus, times(1)).post(any(SessionSelectedChangedEvent.class));
        verify(mockAlarmUtil, times(1)).handleSessionAlarm(mockSession);
    }

    @Test
    public void testSessionTimeRange() {
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.set(2016, 2, 18, 10, 0, 0);
        Calendar eCalendar = Calendar.getInstance();
        eCalendar.set(2016, 2, 18, 11, 0, 0);
        when(mockSession.getDisplaySTime(context)).thenReturn(sCalendar.getTime());
        when(mockSession.getDisplayETime(context)).thenReturn(eCalendar.getTime());
        viewModel.setSession(mockSession);

        assertThat(viewModel.sessionTimeRange()).isEqualTo("10:00 午前 - 11:00 午前 (60min)");
    }

    @Test
    public void testFabRippleColorWhenCategoryIsNotNull() {
        mockSession.category = mock(Category.class);
        when(mockSession.category.getPaleColorResId()).thenReturn(R.color.amber500_alpha_54);
        viewModel.setSession(mockSession);

        assertThat(viewModel.fabRippleColor()).isEqualTo(ContextCompat.getColor(context, R.color.amber500_alpha_54));
    }

    @Test
    public void testFabRippleColorWhenCategoryNull() {
        viewModel.setSession(mockSession);
        assertThat(viewModel.fabRippleColor()).isEqualTo(ContextCompat.getColor(context, R.color.indigo500_alpha_54));
    }

    @Test
    public void testSlideIconVisibility() {
        // When session has slide url
        when(mockSession.hasSlide()).thenReturn(true);
        viewModel.setSession(mockSession);
        assertThat(viewModel.slideIconVisibility()).isEqualTo(View.VISIBLE);

        // When session doesn't have slide url
        when(mockSession.hasSlide()).thenReturn(false);
        viewModel.setSession(mockSession);
        assertThat(viewModel.slideIconVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void testDashVideoIconVisibility() {
        // When session has video url
        when(mockSession.hasDashVideo()).thenReturn(true);
        viewModel.setSession(mockSession);
        assertThat(viewModel.dashVideoIconVisibility()).isEqualTo(View.VISIBLE);

        // When session doesn't have video url
        when(mockSession.hasDashVideo()).thenReturn(false);
        viewModel.setSession(mockSession);
        assertThat(viewModel.dashVideoIconVisibility()).isEqualTo(View.GONE);
    }

}
