package com.konifar.confsched.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konifar.confsched.databinding.FragmentAboutBinding;
import com.konifar.confsched.util.AppUtil;

public class AboutFragment extends Fragment {

    public static final String TAG = AboutFragment.class.getSimpleName();

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
        //
        binding.txtVersion.setText(AppUtil.getVersionName(getActivity()));
    }

}
