package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.model.Category;

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
            categoryView.setTextColor(ContextCompat.getColor(categoryView.getContext(), category.getVividColorResId()));
            categoryView.setBackgroundResource(R.drawable.tag_language);
            categoryView.setText(category.name);
        } else {
            categoryView.setVisibility(INVISIBLE);
        }
    }

}
