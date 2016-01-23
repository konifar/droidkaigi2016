package com.konifar.confsched.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
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
            categoryView.setTextColor(categoryView.getTextColorResId(category.id));
            categoryView.setBackgroundResource(R.drawable.tag_language);
            categoryView.setText(category.name);
        } else {
            categoryView.setVisibility(INVISIBLE);
        }
    }

    private int getTextColorResId(int categoryId) {
        switch (categoryId) {
            case 1:
                return R.color.amber500;
            case 2:
                return R.color.indigo500;
            case 3:
                return R.color.orange500;
            case 4:
                return R.color.pink500;
            case 5:
                return R.color.purple500;
            case 6:
                return R.color.teal500;
            default:
                return R.color.indigo500;
        }
    }

}
