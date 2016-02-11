package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ViewMapSearchItemBinding;
import io.github.droidkaigi.confsched.model.PlaceMap;
import io.github.droidkaigi.confsched.util.LocaleUtil;

public class MapSearchViewItem extends FrameLayout {

    private ViewMapSearchItemBinding binding;

    public MapSearchViewItem(Context context) {
        this(context, null);
    }

    public MapSearchViewItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapSearchViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_map_search_item, this, true);
    }

    public void bindData(@NonNull PlaceMap placeMap, @NonNull OnClickListener listener) {
        binding.imgMarker.setImageResource(placeMap.markerRes);
        binding.txtName.setText(LocaleUtil.getRtlConsideredText(
                getContext().getString(placeMap.nameRes), getContext()));
        binding.txtBuilding.setText(LocaleUtil.getRtlConsideredText(
                getContext().getString(placeMap.buildingNameRes), getContext()));
        binding.rootView.setOnClickListener(listener);
    }

}
