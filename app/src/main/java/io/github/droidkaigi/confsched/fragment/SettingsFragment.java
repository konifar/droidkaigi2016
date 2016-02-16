package io.github.droidkaigi.confsched.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.ActivityNavigator;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.databinding.FragmentSettingsBinding;
import io.github.droidkaigi.confsched.util.LocaleUtil;
import io.github.droidkaigi.confsched.util.PrefUtil;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
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
        binding.notificationSwitchRow.init(PrefUtil.KEY_NOTIFICATION_SETTING, true);
        binding.localTimeSwitchRow.init(PrefUtil.KEY_SHOW_LOCAL_TIME, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.headsUpSwitchRow.init(PrefUtil.KEY_HEADS_UP_SETTING, true);
            binding.headsUpSwitchRow.setVisibility(View.VISIBLE);
            binding.headsUpBorder.setVisibility(View.VISIBLE);
            binding.notificationSwitchRow.setOnCheckedChangeListener((buttonView, isChecked) -> {
                binding.headsUpSwitchRow.setEnabled(isChecked);
            });
        }
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
        activityNavigator.showMain(activity, false);
        activity.finish();
    }

}
