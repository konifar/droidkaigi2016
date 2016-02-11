package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class NumberRatingBar extends SeekBar {
    private OnSeekBarChangeListener userSeekBarChangeListener;

    public NumberRatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOnSeekBarChangeListener(seekBarChangeListener);
        updateSecondaryProgress();
        setThumb(null);
    }

    public NumberRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberRatingBar(Context context) {
        this(context, null, 0);
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        userSeekBarChangeListener = listener;
    }

    private OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateSecondaryProgress();
            if (userSeekBarChangeListener != null) {
                userSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (userSeekBarChangeListener != null) {
                userSeekBarChangeListener.onStartTrackingTouch(seekBar);
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (userSeekBarChangeListener != null) {
                userSeekBarChangeListener.onStopTrackingTouch(seekBar);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // A delectable hack.
        event.offsetLocation(getWidth() / 5, 0);
        return super.onTouchEvent(event);
    }

    private void updateSecondaryProgress() {
        // Another delectable hack.
        setSecondaryProgress(getProgress() - 1);
    }
}
