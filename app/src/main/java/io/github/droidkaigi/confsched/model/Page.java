package io.github.droidkaigi.confsched.model;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.fragment.AboutFragment;
import io.github.droidkaigi.confsched.fragment.MapFragment;
import io.github.droidkaigi.confsched.fragment.MyScheduleFragment;
import io.github.droidkaigi.confsched.fragment.SessionsFragment;
import io.github.droidkaigi.confsched.fragment.SettingsFragment;
import io.github.droidkaigi.confsched.fragment.SponsorsFragment;

/**
 * @author KeishinYokomaku
 */
public enum Page {
    ALL_SESSIONS(R.id.nav_all_sessions, R.string.all_sessions, false, SessionsFragment.class.getSimpleName()) {
        @Override
        public Fragment createFragment() {
            return SessionsFragment.newInstance();
        }
    },
    MY_SCHEDULE(R.id.nav_my_schedule, R.string.my_schedule, false, MyScheduleFragment.class.getSimpleName()) {
        @Override
        public Fragment createFragment() {
            return MyScheduleFragment.newInstance();
        }
    },
    MAP(R.id.nav_map, R.string.map, true, MapFragment.class.getSimpleName()) {
        @Override
        public Fragment createFragment() {
            return MapFragment.newInstance();
        }
    },
    SETTINGS(R.id.nav_settings, R.string.settings, true, SettingsFragment.class.getSimpleName()) {
        @Override
        public Fragment createFragment() {
            return SettingsFragment.newInstance();
        }
    },
    SPONSORS(R.id.nav_sponsors, R.string.sponsors, true, SponsorsFragment.class.getSimpleName()) {
        @Override
        public Fragment createFragment() {
            return SponsorsFragment.newInstance();
        }
    },
    ABOUT(R.id.nav_about, R.string.about, true, AboutFragment.class.getSimpleName()) {
        @Override
        public Fragment createFragment() {
            return AboutFragment.newInstance();
        }
    };

    private final int menuId;
    private final int titleResId;
    private final boolean toggleToolbar;
    private final String pageName;

    Page(int menuId, int titleResId, boolean toggleToolbar, String pageName) {
        this.menuId = menuId;
        this.titleResId = titleResId;
        this.toggleToolbar = toggleToolbar;
        this.pageName = pageName;
    }

    public static Page forMenuId(MenuItem item) {
        int id = item.getItemId();
        for (Page page : values()) {
            if (page.menuId == id) {
                return page;
            }
        }
        throw new AssertionError("no menu enum found for the id. you forgot to implement?");
    }

    public static Page forName(Fragment fragment) {
        String name = fragment.getClass().getSimpleName();
        for (Page page : values()) {
            if (page.pageName.equals(name)) {
                return page;
            }
        }
        throw new AssertionError("no menu enum found for the id. you forgot to implement?");
    }

    public int getMenuId() {
        return menuId;
    }

    public boolean shouldToggleToolbar() {
        return toggleToolbar;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public String getPageName() {
        return pageName;
    }

    public abstract Fragment createFragment();

}
