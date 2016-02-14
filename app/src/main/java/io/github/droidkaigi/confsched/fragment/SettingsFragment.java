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
import io.github.droidkaigi.confsched.util.LocaleUtil;
import io.github.droidkaigi.confsched.util.PrefUtil;
import rx.Observable;

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
        binding.txtLanguage.setText(LocaleUtil.getCurrentLanguage(getActivity()));
        binding.languageSettingsContainer.setOnClickListener(v -> showLanguagesDialog());

        binding.notificationSettingContainer.setOnClickListener(v -> switchNotificationSetting());
        boolean notificationSetting = PrefUtil.get(getContext(), PrefUtil.KEY_NOTIFICATION_SETTING, true);
        binding.notificationSettingSwitch.setChecked(notificationSetting);
        binding.notificationSettingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setNotificationSetting(isChecked));
    }

    private void showLanguagesDialog() {
        List<String> languageIds = Arrays.asList(LocaleUtil.SUPPORT_LANG);
        List<String> languages = Observable.from(languageIds)
                .map(languageId -> LocaleUtil.getLanguage(getActivity(), languageId, languageId))
                .toList()
                .toBlocking()
                .single();

        String currentLanguageId = LocaleUtil.getCurrentLanguageId(getActivity());
        int defaultItem = languageIds.indexOf(currentLanguageId);
        String[] items = languages.toArray(new String[languages.size()]);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.settings_language)
                .setSingleChoiceItems(items, defaultItem, (dialog, which) -> {
                    String selectedLanguageId = languageIds.get(which);
                    if (!currentLanguageId.equals(selectedLanguageId)) {
                        Log.d(TAG, "Selected language_id: " + selectedLanguageId);
                        LocaleUtil.setLocale(getActivity(), selectedLanguageId);
                        dialog.dismiss();
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

    private void setNotificationSetting(boolean isChecked) {
        PrefUtil.put(getContext(), PrefUtil.KEY_NOTIFICATION_SETTING, isChecked);
    }

    private void switchNotificationSetting() {
        boolean newValule = !PrefUtil.get(getContext(), PrefUtil.KEY_NOTIFICATION_SETTING, true);
        setNotificationSetting(newValule);
        binding.notificationSettingSwitch.setChecked(newValule);
    }
}
