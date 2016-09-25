package io.github.droidkaigi.confsched.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import io.codetail.animation.ViewAnimationUtils;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ViewMapSearchBinding;
import io.github.droidkaigi.confsched.model.PlaceMap;
import io.github.droidkaigi.confsched.util.LocaleUtil;
import java.util.List;
import rx.Observable;

public class MapSearchView extends FrameLayout {

    private static final Interpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();

    private ViewMapSearchBinding binding;

    private OnVisibilityChangeListener onVisibilityChangeListener;

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

    public void setOnVisibilityChangeListener(OnVisibilityChangeListener listener) {
        onVisibilityChangeListener = listener;
    }

    public void toggle() {
        if (binding.mapListContainer.getVisibility() != VISIBLE) {
            revealOn();
        } else {
            revealOff();
        }
    }

    private int getRevealCenterX(View container) {
        if (LocaleUtil.shouldRtl()) {
            return container.getLeft();
        } else {
            return container.getRight();
        }
    }

    private void revealOn() {
        if (binding.mapListContainer.getVisibility() == VISIBLE) return;

        View container = binding.mapListContainer;
        Animator animator = ViewAnimationUtils.createCircularReveal(
                container,
                getRevealCenterX(container),
                container.getTop(),
                0,
                (float) Math.hypot(container.getWidth(), container.getHeight()));
        animator.setInterpolator(INTERPOLATOR);
        animator.setDuration(getResources().getInteger(R.integer.view_reveal_mills));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                binding.mapListContainer.setVisibility(VISIBLE);
                if (onVisibilityChangeListener != null) {
                    onVisibilityChangeListener.onChange();
                }
            }
        });

        animator.start();
    }

    public void revealOff() {
        if (binding.mapListContainer.getVisibility() != VISIBLE) return;

        View container = binding.mapListContainer;
        Animator animator = ViewAnimationUtils.createCircularReveal(
                container,
                getRevealCenterX(container),
                container.getTop(),
                (float) Math.hypot(container.getWidth(), container.getHeight()),
                0);
        animator.setInterpolator(INTERPOLATOR);
        animator.setDuration(getResources().getInteger(R.integer.view_reveal_mills));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.mapListContainer.setVisibility(INVISIBLE);
                if (onVisibilityChangeListener != null) {
                    onVisibilityChangeListener.onChange();
                }
            }
        });

        animator.start();
    }

    public interface OnItemClickListener {
        void onClick(PlaceMap placeMap);
    }

    public interface OnVisibilityChangeListener {
        void onChange();
    }

}
