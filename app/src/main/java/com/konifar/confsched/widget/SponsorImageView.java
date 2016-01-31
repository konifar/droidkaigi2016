package com.konifar.confsched.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.konifar.confsched.R;
import com.konifar.confsched.databinding.ViewSponsorImageBinding;
import com.konifar.confsched.model.Sponsor;
import com.squareup.picasso.Picasso;

public class SponsorImageView extends FrameLayout {

    private ViewSponsorImageBinding binding;

    public SponsorImageView(Context context) {
        this(context, null);
    }

    public SponsorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SponsorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_sponsor_image, this, true);
    }

    public void bindData(@NonNull Sponsor sponsor, @Nullable OnClickListener listener) {
        Picasso.with(getContext())
                .load(sponsor.imageUrl)
                .placeholder(R.color.grey200)
                .into(binding.imgLogo);

        binding.rootView.setOnClickListener(listener);
    }

}
