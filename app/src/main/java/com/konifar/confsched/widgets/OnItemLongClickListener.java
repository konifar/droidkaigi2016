package com.konifar.confsched.widgets;

import android.support.annotation.NonNull;
import android.view.View;

public interface OnItemLongClickListener<T> {

    boolean onItemLongClick(@NonNull View view, T item);

}
