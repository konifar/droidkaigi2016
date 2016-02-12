package io.github.droidkaigi.confsched.activity;

/**
 * UI tests for {@link SessionDetailActivity} using Espresso.
 */

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.GregorianCalendar;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.model.Category;
import io.github.droidkaigi.confsched.model.Place;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.model.Speaker;
import io.github.droidkaigi.confsched.util.AppUtil;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

@Ignore
@RunWith(AndroidJUnit4.class)
@MediumTest
public class SessionDetailActivityTest {

    private static final Session SESSION = new Session();
    private static final String DESCRIPTION = "description";
    private static final Date STIME = new GregorianCalendar(2016, 2, 19, 10, 0).getTime();
    private static final Date ETIME = new GregorianCalendar(2016, 2, 19, 11, 0).getTime();

    private static final Speaker SPEAKER = new Speaker();
    private static final String SPEAKER_NAME = "speaker_name";
    private static final String GITHUB_NAME = "github_name";
    private static final String TWITTER_NAME = "twitter_name";

    private static final Category CATEGORY = new Category();
    private static final String CATEGORY_NAME = "category_name";

    private static final Place PLACE = new Place();
    private static final String PLACE_NAME = "place_name";

    static {
        SPEAKER.id = 1;
        SPEAKER.name = SPEAKER_NAME;
        SPEAKER.githubName = GITHUB_NAME;
        SPEAKER.twitterName = TWITTER_NAME;

        CATEGORY.id = 2;
        CATEGORY.name = CATEGORY_NAME;

        PLACE.id = 3;
        PLACE.name = PLACE_NAME;

        SESSION.id = 100;
        SESSION.description = DESCRIPTION;
        SESSION.speaker = SPEAKER;
        SESSION.category = CATEGORY;
        SESSION.place = PLACE;
        SESSION.stime = STIME;
        SESSION.etime = ETIME;
    }

    @Rule
    public IntentsTestRule<SessionDetailActivity> activityRule = new IntentsTestRule<>(
            SessionDetailActivity.class, true, false);

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = SessionDetailActivity.createIntent(context, SESSION);
        // The third argument of the constructor for ActivityTestRule needs to be false as explained
        // in the Javadoc for ActivityTestRule#launchActivity
        // http://developer.android.com/reference/android/support/test/rule/ActivityTestRule.html#launchActivity(android.content.Intent)
        activityRule.launchActivity(intent);

        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));


    }

    @Test
    public void testInitialViews() {
        onView(withId(R.id.txt_speaker_name)).check(matches(withText(SPEAKER_NAME)));
        onView(withId(R.id.txt_category)).check(matches(withText(CATEGORY_NAME)));
        onView(withId(R.id.txt_place)).check(matches(withText(PLACE_NAME)));
        onView(withId(R.id.txt_description)).check(matches(withText(DESCRIPTION)));
    }

    @Test
    public void testClickTwitterIcon() {
        onView(withId(R.id.img_twitter)).perform(click());

        intended(
                allOf(hasAction(Intent.ACTION_VIEW), hasData(AppUtil.getTwitterUrl(TWITTER_NAME))));
    }

    @Test
    public void testClickGitHubIcon() {
        onView(withId(R.id.img_github)).perform(click());

        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(AppUtil.getGitHubUrl(GITHUB_NAME))));
    }
}
