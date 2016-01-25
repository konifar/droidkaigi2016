package com.konifar.confsched.util;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.konifar.confsched.R;
import com.konifar.confsched.model.Category;
import com.konifar.confsched.model.Session;
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
                .error(R.drawable.ic_speaker_placeholder)
                .transform(new CropCircleTransformation())
                .into(imageView);
    }

    @SuppressWarnings("unused")
    @BindingAdapter("coverFadeBackground")
    public static void setSpeakerImageUrl(View view, @NonNull Category category) {
        view.setBackgroundResource(category.getCoverColorResId());
    }

    @SuppressWarnings("unused")
    @BindingAdapter("sessionTimeRange")
    public static void setSessionTimeRange(TextView textView, @NonNull Session session) {
        String timeRange = textView.getContext().getString(R.string.session_time_range,
                DateUtil.getHourMinute(session.stime),
                DateUtil.getHourMinute(session.etime),
                DateUtil.getMinutes(session.stime, session.etime));
        textView.setText(timeRange);
    }

    @SuppressWarnings("unused")
    @BindingAdapter("sessionDescription")
    public static void setSessionDescription(TextView textView, @NonNull Session session) {
        textView.setText(session.description);
        Linkify.addLinks(textView, Linkify.ALL);
    }

}
