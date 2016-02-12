package io.github.droidkaigi.confsched.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.ActivityNavigator;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.databinding.FragmentSettingsBinding;
import io.github.droidkaigi.confsched.util.AppUtil;
import rx.Observable;

import static io.github.droidkaigi.confsched.util.IntentUtil.toBrowser;

public class SettingsFragment extends Fragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    @Inject
    ActivityNavigator activityNavigator;
    @Inject
    SessionDao dao;

    private FragmentSettingsBinding binding;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainApplication.getComponent(this).inject(this);
    }

    private void initView() {
        binding.txtLanguage.setText(AppUtil.getCurrentLanguage(getActivity()));
        binding.languageSettingsContainer.setOnClickListener(v -> showLanguagesDialog());
        binding.txtBugreport.setOnClickListener(v -> showBugReport());
    }

    private void showLanguagesDialog() {
        List<String> languageIds = Arrays.asList(AppUtil.SUPPORT_LANG);
        List<String> languages = Observable.from(languageIds)
                .map(languageId -> AppUtil.getLanguage(getActivity(), languageId))
                .toList()
                .toBlocking()
                .single();

        String currentLanguageId = AppUtil.getCurrentLanguageId(getActivity());
        int defaultItem = languageIds.indexOf(currentLanguageId);
        String[] items = languages.toArray(new String[languages.size()]);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.settings_language)
                .setSingleChoiceItems(items, defaultItem, (dialog, which) -> {
                    String selectedLanguageId = languageIds.get(which);
                    if (!currentLanguageId.equals(selectedLanguageId)) {
                        Log.d(TAG, "Selected language_id: " + selectedLanguageId);
                        AppUtil.setLocale(getActivity(), selectedLanguageId);
                        restart();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void restart() {
        Activity activity = getActivity();
        activityNavigator.showMain(activity, true);
        activity.finish();
    }

    private void showBugReport() {
        startActivity(toBrowser(getString(R.string.bug_report_url)));
    }

}
