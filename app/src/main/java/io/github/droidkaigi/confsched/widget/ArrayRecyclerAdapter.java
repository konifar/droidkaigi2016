package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class ArrayRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Iterable<T> {

    final Context context;
    final ArrayList<T> list;

    public ArrayRecyclerAdapter(@NonNull Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public T getItem(int position) {
        return list.get(position);
    }

    public void addItem(T item) {
        list.add(item);
    }

    public void addAll(Collection<T> items) {
        list.addAll(items);
    }

    public void clear() {
        list.clear();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

}
