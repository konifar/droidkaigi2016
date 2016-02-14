package io.github.droidkaigi.confsched.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.apmem.tools.layouts.FlowLayout;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.model.Category;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.widget.transformation.CropCircleTransformation;

public class DataBindingAttributeUtil {

    public static void setImageUrlWithSize(ImageView imageView, @Nullable String imageUrl, float sizeInDimen, int placeholderResId) {
        if (TextUtils.isEmpty(imageUrl)) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), placeholderResId));
        } else {
            final int size = Math.round(sizeInDimen);
            imageView.setBackground(ContextCompat.getDrawable(imageView.getContext(), R.drawable.circle_border_grey200));
            Picasso.with(imageView.getContext())
                    .load(imageUrl)
                    .resize(size, size)
                    .centerInside()
                    .placeholder(placeholderResId)
                    .error(placeholderResId)
                    .transform(new CropCircleTransformation())
                    .into(imageView);
        }
    }

    @BindingAdapter({"speakerImageUrl", "speakerImageSize"})
    public static void setSpeakerImageUrlWithSize(ImageView imageView, @Nullable String imageUrl, float sizeInDimen) {
        setImageUrlWithSize(imageView, imageUrl, sizeInDimen, R.drawable.ic_speaker_placeholder);
    }

    @BindingAdapter({"contributorAvatarUrl", "contributorAvatarSize"})
    public static void setContributorAvatarUrlWithSize(ImageView imageView, @Nullable String imageUrl, float sizeInDimen) {
        setImageUrlWithSize(imageView, imageUrl, sizeInDimen, R.drawable.ic_speaker_placeholder);
    }

    @BindingAdapter("coverFadeBackground")
    public static void setCoverFadeBackground(View view, @NonNull Category category) {
        view.setBackgroundResource(category.getPaleColorResId());
    }

    @BindingAdapter("categoryVividColor")
    public static void setCategoryVividColor(CollapsingToolbarLayout view, @NonNull Category category) {
        view.setContentScrimColor(ContextCompat.getColor(view.getContext(), category.getVividColorResId()));
    }

    @BindingAdapter("categoryVividColor")
    public static void setCategoryVividColor(TextView view, @NonNull Category category) {
        view.setTextColor(ContextCompat.getColor(view.getContext(), category.getVividColorResId()));
    }

    @BindingAdapter("sessionTimeRange")
    public static void setSessionTimeRange(TextView textView, @NonNull Session session) {
        String timeRange = textView.getContext().getString(R.string.session_time_range,
                DateUtil.getHourMinute(session.stime, textView.getContext()),
                DateUtil.getHourMinute(session.etime, textView.getContext()),
                DateUtil.getMinutes(session.stime, session.etime));
        textView.setText(timeRange);
    }

    @BindingAdapter("sessionDetailTimeRange")
    public static void setSessionDetailTimeRange(TextView textView, @NonNull Session session) {
        String timeRange = textView.getContext().getString(R.string.session_time_range,
                DateUtil.getLongFormatDate(session.stime, textView.getContext()),
                DateUtil.getHourMinute(session.etime, textView.getContext()),
                DateUtil.getMinutes(session.stime, session.etime));
        textView.setText(timeRange);
    }

    @BindingAdapter("sessionDescription")
    public static void setSessionDescription(TextView textView, @NonNull Session session) {
        setTextRtlConsidered(textView, session.description);
        Linkify.addLinks(textView, Linkify.ALL);
    }

    @BindingAdapter("sessionFab")
    public static void setSessionFab(FloatingActionButton fab, @NonNull Session session) {
        fab.setRippleColor(ContextCompat.getColor(fab.getContext(), session.category.getPaleColorResId()));
        fab.setSelected(session.checked);
    }

    @BindingAdapter("forceRtlDirection")
    public static void setForceRtlDirection(FlowLayout flowLayout, boolean isCenterVertical) {
        if (LocaleUtil.shouldRtl(flowLayout.getContext())) {
            ViewCompat.setLayoutDirection(flowLayout, ViewCompat.LAYOUT_DIRECTION_RTL);

            int gravity = isCenterVertical ? GravityCompat.START | Gravity.CENTER_VERTICAL : GravityCompat.START;
            flowLayout.setGravity(gravity);
        }
    }

    @BindingAdapter("textRtlConsidered")
    public static void setTextRtlConsidered(TextView textView, String text) {
        textView.setText(LocaleUtil.getRtlConsideredText(text, textView.getContext()));
    }

}
