package com.konifar.confsched.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konifar.confsched.R;
import com.konifar.confsched.databinding.FragmentAboutBinding;
import com.konifar.confsched.util.AppUtil;

public class AboutFragment extends Fragment {

    public static final String TAG = AboutFragment.class.getSimpleName();
    private static final String REP_TWITTER_NAME = "mhidaka";
    private static final String SITE_URL = "https://droidkaigi.github.io/2016";
    private static final String CONF_TWITTER_NAME = "DroidKaigi";
    private static final String CONF_FACEBOOK_NAME = "DroidKaigi";

    private FragmentAboutBinding binding;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        String repText = getString(R.string.about_rep, REP_TWITTER_NAME);
        binding.txtRep.setText(repText);
        AppUtil.linkify(getActivity(), binding.txtRep, REP_TWITTER_NAME, AppUtil.getTwitterUrl(REP_TWITTER_NAME));

        binding.txtSiteUrl.setText(SITE_URL);
        AppUtil.linkify(getActivity(), binding.txtSiteUrl, SITE_URL, SITE_URL);

        binding.imgFacebookClicker.setOnClickListener(v ->
                AppUtil.showWebPage(getActivity(), AppUtil.getFacebookUrl(CONF_FACEBOOK_NAME)));
        binding.imgTwitterClicker.setOnClickListener(v ->
                AppUtil.showWebPage(getActivity(), AppUtil.getTwitterUrl(CONF_TWITTER_NAME)));

        binding.txtVersion.setText(AppUtil.getVersionName(getActivity()));
    }

}
