package io.github.droidkaigi.confsched.model;

import android.support.annotation.NonNull;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.Table;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.github.droidkaigi.confsched.R;

@Parcel
@Table
public class Category {

    @Column(indexed = true)
    @SerializedName("id")
    public int id;

    @Column(indexed = true)
    @SerializedName("name")
    public String name;

    public Category() {
    }

    public int getTextColorResId() {
        switch (id) {
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

    @NonNull
    public int getCoverColorResId() {
        switch (id) {
            case 1:
                return R.color.amber500_alpha_54;
            case 2:
                return R.color.indigo500_alpha_54;
            case 3:
                return R.color.orange500_alpha_54;
            case 4:
                return R.color.pink500_alpha_54;
            case 5:
                return R.color.purple500_alpha_54;
            case 6:
                return R.color.teal500_alpha_54;
            default:
                return R.color.indigo500_alpha_54;
        }
    }

}
