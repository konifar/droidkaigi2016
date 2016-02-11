package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ViewMapSearchBinding;
import io.github.droidkaigi.confsched.model.PlaceMap;
import io.github.droidkaigi.confsched.util.LocaleUtil;
import rx.Observable;

public class MapSearchView extends FrameLayout {

    private static final Interpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();

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

    public void bindData(List<PlaceMap> placeMaps, OnItemClickListener listener) {
        Observable.from(placeMaps).forEach(map -> {
            MapSearchViewItem item = new MapSearchViewItem(getContext());
            item.bindData(map, v -> {
                listener.onClick(map);
                revealOff();
            });
            binding.mapListContainer.addView(item);
        });

        binding.mapListContainer.setOnClickListener(v -> revealOff());
    }

    public boolean isVisible() {
        return binding.mapListContainer.getVisibility() == VISIBLE;
    }

    public void toggle() {
        if (binding.mapListContainer.getVisibility() != VISIBLE) {
            revealOn();
        } else {
            revealOff();
        }
    }

    private int getRevealCenterX(View container) {
        if (LocaleUtil.shouldRtl(getContext())) {
            return container.getLeft();
        } else {
            return container.getRight();
        }
    }

    private void revealOn() {
        if (binding.mapListContainer.getVisibility() == VISIBLE) return;

        View container = binding.mapListContainer;
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
                container,
                getRevealCenterX(container),
                container.getTop(),
                0,
                (float) Math.hypot(container.getWidth(), container.getHeight()));
        animator.setInterpolator(INTERPOLATOR);
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

    public void revealOff() {
        if (binding.mapListContainer.getVisibility() != VISIBLE) return;

        View container = binding.mapListContainer;
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
                container,
                getRevealCenterX(container),
                container.getTop(),
                (float) Math.hypot(container.getWidth(), container.getHeight()),
                0);
        animator.setInterpolator(INTERPOLATOR);
        animator.setDuration(getResources().getInteger(R.integer.view_reveal_mills));
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                // Do nothing
            }

            @Override
            public void onAnimationEnd() {
                binding.mapListContainer.setVisibility(INVISIBLE);
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

    public interface OnItemClickListener {
        void onClick(PlaceMap placeMap);
    }

}
