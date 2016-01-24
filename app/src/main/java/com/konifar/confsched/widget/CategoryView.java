package com.konifar.confsched.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.konifar.confsched.R;
import com.konifar.confsched.model.Category;

import javax.annotation.Nullable;

public class CategoryView extends TextView {

    public CategoryView(Context context) {
        this(context, null);
    }

    public CategoryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @BindingAdapter("category")
    public static void setCategory(CategoryView categoryView, @Nullable Category category) {
        if (category != null) {
            categoryView.setTextColor(ContextCompat.getColor(categoryView.getContext(), category.getTextColorResId()));
            categoryView.setBackgroundResource(R.drawable.tag_language);
            categoryView.setText(category.name);
        } else {
            categoryView.setVisibility(INVISIBLE);
        }
    }

}
