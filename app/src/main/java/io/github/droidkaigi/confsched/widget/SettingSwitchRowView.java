package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ViewSettingSwitchRowBinding;
import io.github.droidkaigi.confsched.util.PrefUtil;

public class SettingSwitchRowView extends RelativeLayout implements Checkable {

    private static final String TAG = SettingSwitchRowView.class.getSimpleName();

    private ViewSettingSwitchRowBinding binding;

    private String prefKey;

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

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

            String title = a.getString(R.styleable.SettingSwitchRow_settingTitle);
            String description = a.getString(R.styleable.SettingSwitchRow_settingDescription);

            binding.settingTitle.setText(title);
            binding.settingDescription.setText(description);

            binding.getRoot().setOnClickListener(v -> switchSetting());
            binding.settingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setSetting(isChecked);
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
                }
            });

            a.recycle();
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
            boolean newValue = !PrefUtil.get(getContext(), prefKey, true);
            setSetting(newValue);
            binding.settingSwitch.setChecked(newValue);
        }
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        binding.getRoot().setEnabled(enabled);
        binding.settingSwitch.setEnabled(enabled);
        if (enabled) {
            binding.settingTitle.setTextColor(getResources().getColor(R.color.black));
            binding.settingDescription.setTextColor(getResources().getColor(R.color.grey600));
        } else {
            int disabledTextColor = getResources().getColor(R.color.black_alpha_30);
            binding.settingTitle.setTextColor(disabledTextColor);
            binding.settingDescription.setTextColor(disabledTextColor);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        setSetting(checked);
        binding.settingSwitch.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return binding.settingSwitch.isChecked();
    }

    @Override
    public void toggle() {
        switchSetting();
    }
}
