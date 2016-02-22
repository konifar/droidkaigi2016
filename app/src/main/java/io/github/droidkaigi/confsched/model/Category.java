package io.github.droidkaigi.confsched.model;

import com.google.gson.annotations.SerializedName;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import org.parceler.Parcel;

import io.github.droidkaigi.confsched.R;

@Parcel
@Table
public class Category implements SearchGroup {

    @PrimaryKey(auto = false)
    @Column(indexed = true)
    @SerializedName("id")
    public int id;

    @Column(indexed = true)
    @SerializedName("name")
    public String name;

    public Category() {
    }

    public int getThemeResId() {
        switch (id) {
            case 1:
                return R.style.AppTheme_NoActionBar_Amber;
            case 2:
                return R.style.AppTheme_NoActionBar_Indigo;
            case 3:
                return R.style.AppTheme_NoActionBar_Orange;
            case 4:
                return R.style.AppTheme_NoActionBar_Pink;
            case 5:
                return R.style.AppTheme_NoActionBar_Purple;
            case 6:
                return R.style.AppTheme_NoActionBar_Teal;
            case 7:
                return R.style.AppTheme_NoActionBar_LightGreen;
            case 8:
                return R.style.AppTheme_NoActionBar_Red;
            default:
                return R.style.AppTheme_NoActionBar_Indigo;
        }
    }

    public int getVividColorResId() {
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
            case 7:
                return R.color.lightgreen500;
            case 8:
                return R.color.red500;
            default:
                return R.color.indigo500;
        }
    }

    public int getPaleColorResId() {
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
            case 7:
                return R.color.lightgreen500_alpha_54;
            case 8:
                return R.color.red500_alpha_54;
            default:
                return R.color.indigo500_alpha_54;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return Type.CATEGORY;
    }

}
