package com.konifar.confsched.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.konifar.confsched.R;
import com.konifar.confsched.databinding.ViewMapSearchBinding;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class MapSearchView extends FrameLayout {

    private ViewMapSearchBinding binding;

    public MapSearchView(Context context) {
        this(context, null);
    }

    public MapSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_map_search, this, true);
    }

    public void showReveal() {
        ViewAnimationUtils.createCircularReveal(binding.mapListContainer, 0, 0, 0, 0);
        binding.mapListContainer.setVisibility(GONE);
        View container = binding.mapListContainer;

        int cx = container.getRight();
        int cy = container.getTop();

        int dx = container.getWidth();
        int dy = container.getHeight();
        float finalRadius = (float) Math.hypot(dx, dy);

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(container, cx, cy, 0, finalRadius);
        container.setVisibility(VISIBLE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(getResources().getInteger(R.integer.view_reveal_mills));
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                binding.mapListContainer.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd() {
                // Do nothing
            }

            @Override
            public void onAnimationCancel() {
                // Do nothing
            }

            @Override
            public void onAnimationRepeat() {
                // Do nothing
            }
        });
        animator.start();
    }

}
