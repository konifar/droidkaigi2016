package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ViewSettingSwitchRowBinding;
import io.github.droidkaigi.confsched.util.PrefUtil;

public class SettingSwitchRowView extends RelativeLayout {

    private static final String TAG = SettingSwitchRowView.class.getSimpleName();

    private ViewSettingSwitchRowBinding binding;

    private String prefKey;

    public SettingSwitchRowView(Context context) {
        this(context, null);
    }

    public SettingSwitchRowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingSwitchRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_setting_switch_row, this, true);

        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingSwitchRow);

            try {
                String title = a.getString(R.styleable.SettingSwitchRow_settingTitle);
                String description = a.getString(R.styleable.SettingSwitchRow_settingDescription);

                binding.settingTitle.setText(title);
                binding.settingDescription.setText(description);

                binding.getRoot().setOnClickListener(v -> switchSetting());
                binding.settingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setSetting(isChecked));
            } finally {
                a.recycle();
            }
        }
    }

    public void init(String prefKey, boolean defaultValue) {
        this.prefKey = prefKey;
        boolean isChecked = PrefUtil.get(getContext(), prefKey, defaultValue);
        binding.settingSwitch.setChecked(isChecked);
    }

    private void setSetting(boolean isChecked) {
        if (TextUtils.isEmpty(prefKey)) {
            Log.d(TAG, "PrefKey must be set. Call init()");
        } else {
            PrefUtil.put(getContext(), prefKey, isChecked);
        }
    }

    private void switchSetting() {
        if (TextUtils.isEmpty(prefKey)) {
            Log.d(TAG, "PrefKey must be set. Call init()");
        } else {
            boolean newValule = !PrefUtil.get(getContext(), prefKey, true);
            setSetting(newValule);
            binding.settingSwitch.setChecked(newValule);
        }
    }

}
