package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ToolbarSearchBinding;

public class SearchToolbar extends FrameLayout {

    private static final int DRAWABLE_RIGHT = 2;

    private ToolbarSearchBinding binding;

    public SearchToolbar(Context context) {
        this(context, null);
    }

    public SearchToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.toolbar_search, this, true);

        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchToolbar);

            try {
                boolean focus = a.getBoolean(R.styleable.SearchToolbar_searchFocus, false);
                int hintResId = a.getResourceId(R.styleable.SearchToolbar_searchHint, R.string.search_hint);
                setHint(hintResId);
                if (focus) {
                    binding.editSearch.requestFocus();
                } else {
                    clearFocus();
                }
                toggleCloseButtonVisible(false);
                initView();
            } finally {
                a.recycle();
            }
        }
    }

    private void toggleCloseButtonVisible(boolean visible) {
        Drawable closeDrawable = binding.editSearch.getCompoundDrawables()[DRAWABLE_RIGHT];
        closeDrawable.setAlpha(visible ? 255 : 0);
    }

    private void initView() {
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean visible = count > 0 || start > 0;
                toggleCloseButtonVisible(visible);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        binding.editSearch.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable closeDrawable = binding.editSearch.getCompoundDrawables()[DRAWABLE_RIGHT];
                int leftEdgeOfRightDrawable = binding.editSearch.getRight() - closeDrawable.getBounds().width();
                if (event.getRawX() >= leftEdgeOfRightDrawable) {
                    clearText();
                    return true;
                }
            }
            return false;
        });
    }

    private void clearText() {
        binding.editSearch.setText("");
    }

    public void setHint(int resId) {
        binding.editSearch.setHint(resId);
    }

    public String getText() {
        return binding.editSearch.getText().toString();
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        binding.editSearch.addTextChangedListener(textWatcher);
    }

    public Toolbar getToolbar() {
        return binding.toolbar;
    }

}
