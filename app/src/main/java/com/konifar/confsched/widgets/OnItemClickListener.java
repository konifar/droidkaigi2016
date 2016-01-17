package com.konifar.confsched.widgets;

import android.support.annotation.NonNull;
import android.view.View;

public interface OnItemClickListener<T> {

    void onItemClick(@NonNull View view, T item);

}
