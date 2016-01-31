package com.konifar.confsched.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konifar.confsched.R;
import com.konifar.confsched.databinding.FragmentSponsorsBinding;
import com.konifar.confsched.model.Sponsor;
import com.konifar.confsched.util.AppUtil;
import com.konifar.confsched.widget.SponsorImageView;

import org.apmem.tools.layouts.FlowLayout;

import rx.Observable;

public class SponsorsFragment extends Fragment {

    public static final String TAG = SponsorsFragment.class.getSimpleName();

    private FragmentSponsorsBinding binding;

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
        imageView.bindData(sponsor, v -> AppUtil.showWebPage(getActivity(), sponsor.url));
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.spacing_small);
        params.setMargins(margin, margin, 0, 0);
        container.addView(imageView, params);
    }

}
