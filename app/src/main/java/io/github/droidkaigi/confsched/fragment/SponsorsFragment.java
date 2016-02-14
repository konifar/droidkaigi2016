package io.github.droidkaigi.confsched.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apmem.tools.layouts.FlowLayout;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.FragmentSponsorsBinding;
import io.github.droidkaigi.confsched.model.Sponsor;
import io.github.droidkaigi.confsched.util.AnalyticsTracker;
import io.github.droidkaigi.confsched.util.AppUtil;
import io.github.droidkaigi.confsched.widget.SponsorImageView;
import rx.Observable;

public class SponsorsFragment extends Fragment {

    private FragmentSponsorsBinding binding;

    @Inject
    AnalyticsTracker analyticsTracker;

    public static SponsorsFragment newInstance() {
        return new SponsorsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSponsorsBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainApplication.getComponent(this).inject(this);
    }

    private void initView() {
        Observable.from(Sponsor.createPlatinumList())
                .forEach(sponsor -> addView(sponsor, binding.platinumContainer));
        Observable.from(Sponsor.createVideoList())
                .forEach(sponsor -> addView(sponsor, binding.videoContainer));
        Observable.from(Sponsor.createFoodsList())
                .forEach(sponsor -> addView(sponsor, binding.foodsContainer));
        Observable.from(Sponsor.createNormalList())
                .forEach(sponsor -> addView(sponsor, binding.normalContainer));
    }

    private void addView(Sponsor sponsor, FlowLayout container) {
        SponsorImageView imageView = new SponsorImageView(getActivity());
        imageView.bindData(sponsor, v -> {
            if (TextUtils.isEmpty(sponsor.url))
                return;
            analyticsTracker.sendEvent("sponsor", sponsor.url);
            AppUtil.showWebPage(getActivity(), sponsor.url);
        });
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.spacing_small);
        params.setMargins(margin, margin, 0, 0);
        container.addView(imageView, params);
    }

}
