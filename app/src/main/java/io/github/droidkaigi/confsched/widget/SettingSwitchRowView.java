package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ViewSettingSwitchRowBinding;

public class SettingSwitchRowView extends RelativeLayout {

    private static final String TAG = SettingSwitchRowView.class.getSimpleName();

    private ViewSettingSwitchRowBinding binding;

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

            binding.getRoot().setOnClickListener(v -> toggle());
            binding.settingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
                }
            });

            a.recycle();
        }
    }

    public void init(boolean defaultValue, CompoundButton.OnCheckedChangeListener listener) {
        binding.settingSwitch.setChecked(defaultValue);
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

    private void toggle() {
        boolean isChecked = binding.settingSwitch.isChecked();
        binding.settingSwitch.setChecked(!isChecked);
    }
}
