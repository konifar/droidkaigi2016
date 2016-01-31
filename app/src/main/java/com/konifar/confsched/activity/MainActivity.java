package com.konifar.confsched.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.konifar.confsched.MainApplication;
import com.konifar.confsched.R;
import com.konifar.confsched.databinding.ActivityMainBinding;
import com.konifar.confsched.fragment.AboutFragment;
import com.konifar.confsched.fragment.MapFragment;
import com.konifar.confsched.fragment.MyScheduleFragment;
import com.konifar.confsched.fragment.SessionsFragment;
import com.konifar.confsched.fragment.SettingsFragment;
import com.konifar.confsched.fragment.SponsorsFragment;
import com.konifar.confsched.util.AnalyticsUtil;
import com.konifar.confsched.util.AppUtil;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    AnalyticsUtil analyticsUtil;

    private ActivityMainBinding binding;

    static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_fade_enter, R.anim.activity_fade_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.initLocale(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainApplication.getComponent(this).inject(this);

        initView();

        replaceFragment(SessionsFragment.newInstance());
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawer, binding.toolbar, R.string.open, R.string.close);
        binding.drawer.setDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
    }

    private void replaceFragment(Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        ft.replace(R.id.content_view, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        analyticsUtil.sendScreenView("main");
    }

    @Override
    public void onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        binding.drawer.closeDrawer(GravityCompat.START);

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_all_sessions:
                toggleToolbarElevation(false);
                changePage(R.string.all_sessions, SessionsFragment.newInstance());
                break;
            case R.id.nav_my_schedule:
                toggleToolbarElevation(false);
                changePage(R.string.my_schedule, MyScheduleFragment.newInstance());
                break;
            case R.id.nav_map:
                toggleToolbarElevation(true);
                changePage(R.string.map, MapFragment.newInstance());
                break;
            case R.id.nav_settings:
                toggleToolbarElevation(true);
                changePage(R.string.settings, SettingsFragment.newInstance());
                break;
            case R.id.nav_sponsors:
                toggleToolbarElevation(true);
                changePage(R.string.sponsors, SponsorsFragment.newInstance());
                break;
            case R.id.nav_about:
                toggleToolbarElevation(true);
                changePage(R.string.about, AboutFragment.newInstance());
                break;
        }

        return true;
    }

    private void toggleToolbarElevation(boolean enable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float elevation = enable ? getResources().getDimension(R.dimen.elevation) : 0;
            binding.toolbar.setElevation(elevation);
        }
    }

    private void changePage(@StringRes int titleRes, @NonNull Fragment fragment) {
        new Handler().postDelayed(() -> {
            binding.toolbar.setTitle(titleRes);
            replaceFragment(fragment);
        }, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SessionsFragment.TAG);
        if (fragment != null) fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_enter, R.anim.activity_fade_exit);
    }

}
