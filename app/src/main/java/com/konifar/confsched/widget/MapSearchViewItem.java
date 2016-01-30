package com.konifar.confsched.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.konifar.confsched.R;
import com.konifar.confsched.databinding.ViewMapSearchItemBinding;
import com.konifar.confsched.model.PlaceMap;

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
        binding.txtName.setText(placeMap.nameRes);
        binding.txtBuilding.setText(placeMap.buildingNameRes);
        binding.rootView.setOnClickListener(listener);
    }

}
