package io.github.droidkaigi.confsched.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.util.PageNavigator;
import io.github.droidkaigi.confsched.activity.ContributorsActivity;
import io.github.droidkaigi.confsched.databinding.FragmentAboutBinding;
import io.github.droidkaigi.confsched.util.AppUtil;
import io.github.droidkaigi.confsched.util.LocaleUtil;

public class AboutFragment extends BaseFragment {

    private static final String REP_TWITTER_NAME = "mhidaka";
    private static final String CONF_TWITTER_NAME = "DroidKaigi";
    private static final String CONF_FACEBOOK_NAME = "DroidKaigi";
    private static final String CONF_REPOSITORY_NAME = "konifar/droidkaigi2016";
    private static final String LICENSE_URL = "file:///android_asset/license.html";

    @Inject
    PageNavigator navigator;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getComponent().inject(this);
    }

    private void initView() {
        String repText = getString(R.string.about_rep, REP_TWITTER_NAME);
        binding.txtRep.setText(repText);
        AppUtil.linkify(getActivity(), binding.txtRep, REP_TWITTER_NAME, AppUtil.getTwitterUrl(REP_TWITTER_NAME));

        String siteUrl = getString(R.string.about_site_url);
        binding.txtSiteUrl.setText(LocaleUtil.getRtlConsideredText(siteUrl));
        AppUtil.linkify(getActivity(), binding.txtSiteUrl, siteUrl, siteUrl);

        binding.imgFacebookClicker.setOnClickListener(v ->
                AppUtil.showWebPage(getActivity(), AppUtil.getFacebookUrl(CONF_FACEBOOK_NAME)));
        binding.imgTwitterClicker.setOnClickListener(v ->
                AppUtil.showWebPage(getActivity(), AppUtil.getTwitterUrl(CONF_TWITTER_NAME)));

        binding.txtLicense.setOnClickListener(v ->
                navigator.showWebView(getContext(), LICENSE_URL, getString(R.string.about_license)));

        binding.txtGithubRepository.setOnClickListener(v ->
                AppUtil.showWebPage(getActivity(), AppUtil.getGitHubUrl(CONF_REPOSITORY_NAME)));

        binding.txtContributors.setOnClickListener(v ->
                        startActivity(ContributorsActivity.createIntent(getContext()))
        );
        binding.txtYoutube.setOnClickListener(v -> {
            AppUtil.showWebPage(getActivity(), "https://www.youtube.com/channel/UCgK6L-PKx2OZBuhrQ6mmQZw");
        });
        binding.txtVersion.setText(AppUtil.getVersionName(getContext()));
    }

}
