package com.konifar.confsched.util;

import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.konifar.confsched.R;
import com.konifar.confsched.widget.transformation.CropCircleTransformation;
import com.squareup.picasso.Picasso;

public class DataBindingAttributeUtil {

    @SuppressWarnings("unused")
    @BindingAdapter("speakerImageUrl")
    public static void setSpeakerImageUrl(ImageView imageView, @Nullable String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) return;

        Picasso.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_speaker_placeholder)
                .transform(new CropCircleTransformation())
                .into(imageView);
    }

}
