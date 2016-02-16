package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.like.LikeButton;

public class CustomLikeButton extends LikeButton {

    public CustomLikeButton(Context context) {
        this(context, null);
    }

    public CustomLikeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(event);
    }

}
