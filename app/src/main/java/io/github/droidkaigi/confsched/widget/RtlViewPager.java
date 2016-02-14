package io.github.droidkaigi.confsched.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import io.github.droidkaigi.confsched.util.LocaleUtil;

public class RtlViewPager extends ViewPager {

    @NonNull
    private final Map<OnPageChangeListener, ReverseOnPageChangeListener> reverseOnPageChangeListeners;

    @Nullable
    private DataSetObserver dataSetObserver;

    private boolean suppressOnPageChangeListeners;

    public RtlViewPager(Context context) {
        this(context, null);
    }

    public RtlViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        reverseOnPageChangeListeners = new ArrayMap<>(1);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerRtlDataSetObserver(super.getAdapter());
    }

    @Override
    protected void onDetachedFromWindow() {
        unregisterRtlDataSetObserver();
        super.onDetachedFromWindow();
    }

    private void registerRtlDataSetObserver(PagerAdapter adapter) {
        if (adapter instanceof ReverseAdapter && dataSetObserver == null) {
            dataSetObserver = new RevalidateIndicesOnContentChange((ReverseAdapter) adapter);
            adapter.registerDataSetObserver(dataSetObserver);
            ((ReverseAdapter) adapter).revalidateIndices();
        }
    }

    private void unregisterRtlDataSetObserver() {
        final PagerAdapter adapter = super.getAdapter();

        if (adapter instanceof ReverseAdapter && dataSetObserver != null) {
            adapter.unregisterDataSetObserver(dataSetObserver);
            dataSetObserver = null;
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(convert(item), smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        return convert(super.getCurrentItem());
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(convert(item));
    }

    private int convert(int position) {
        if (position >= 0 && isRtl()) {
            return getAdapter() == null ? 0 : getAdapter().getCount() - position - 1;
        } else {
            return position;
        }
    }

    @Nullable
    @Override
    public PagerAdapter getAdapter() {
        final PagerAdapter adapter = super.getAdapter();
        return adapter instanceof ReverseAdapter ? ((ReverseAdapter) adapter).getInnerAdapter() : adapter;
    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        unregisterRtlDataSetObserver();

        final boolean rtlReady = adapter != null && isRtl();
        if (rtlReady) {
            adapter = new ReverseAdapter(adapter);
            registerRtlDataSetObserver(adapter);
        }
        super.setAdapter(adapter);
        if (rtlReady) {
            setCurrentItemWithoutNotification(0);
        }
    }

    @Override
    public void fakeDragBy(float xOffset) {
        super.fakeDragBy(isRtl() ? xOffset : -xOffset);
    }

    private void setCurrentItemWithoutNotification(int index) {
        suppressOnPageChangeListeners = true;
        setCurrentItem(index, false);
        suppressOnPageChangeListeners = false;
    }

    protected boolean isRtl() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(LocaleUtil.getCurrentLocale(getContext())) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public void addOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        if (isRtl()) {
            final ReverseOnPageChangeListener reverseListener = new ReverseOnPageChangeListener(listener);
            reverseOnPageChangeListeners.put(listener, reverseListener);
            listener = reverseListener;
        }
        super.addOnPageChangeListener(listener);
    }

    @Override
    public void removeOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        if (isRtl()) {
            listener = reverseOnPageChangeListeners.remove(listener);
        }
        super.removeOnPageChangeListener(listener);
    }

    private static class RevalidateIndicesOnContentChange extends DataSetObserver {
        @NonNull
        private final ReverseAdapter adapter;

        private RevalidateIndicesOnContentChange(@NonNull ReverseAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onChanged() {
            super.onChanged();
            adapter.revalidateIndices();
        }
    }

    private class ReverseAdapter extends PagerAdapterWrapper {

        private int lastCount;

        public ReverseAdapter(@NonNull PagerAdapter adapter) {
            super(adapter);
            lastCount = adapter.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(reverse(position));
        }

        @Override
        public float getPageWidth(int position) {
            return super.getPageWidth(reverse(position));
        }

        @Override
        public int getItemPosition(Object object) {
            final int itemPosition = super.getItemPosition(object);
            return itemPosition < 0 ? itemPosition : reverse(itemPosition);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, reverse(position));
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, reverse(position), object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, lastCount - position - 1, object);
        }

        private int reverse(int position) {
            return getCount() - position - 1;
        }

        private void revalidateIndices() {
            final int newCount = getCount();
            if (newCount != lastCount) {
                setCurrentItemWithoutNotification(Math.max(0, lastCount - 1));
                lastCount = newCount;
            }
        }
    }

    private class ReverseOnPageChangeListener implements OnPageChangeListener {

        @NonNull
        private final OnPageChangeListener original;
        private int pagerPosition = -1;

        private ReverseOnPageChangeListener(@NonNull OnPageChangeListener original) {
            this.original = original;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (!suppressOnPageChangeListeners) {

                if (positionOffset == 0f && positionOffsetPixels == 0) {
                    pagerPosition = reverse(position);
                } else {
                    pagerPosition = reverse(position + 1);
                }

                original.onPageScrolled(pagerPosition, positionOffset > 0 ? 1f - positionOffset : positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (!suppressOnPageChangeListeners) {
                original.onPageSelected(reverse(position));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (!suppressOnPageChangeListeners) {
                original.onPageScrollStateChanged(state);
            }
        }

        private int reverse(int position) {
            final PagerAdapter adapter = getAdapter();
            return adapter == null ? position : adapter.getCount() - position - 1;
        }
    }


    class PagerAdapterWrapper extends PagerAdapter {

        @NonNull
        private final PagerAdapter adapter;

        protected PagerAdapterWrapper(@NonNull PagerAdapter adapter) {
            this.adapter = adapter;
        }

        @NonNull
        public PagerAdapter getInnerAdapter() {
            return adapter;
        }

        @Override
        public int getCount() {
            return adapter.getCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return adapter.isViewFromObject(view, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return adapter.getPageTitle(position);
        }

        @Override
        public float getPageWidth(int position) {
            return adapter.getPageWidth(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return adapter.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return adapter.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            adapter.destroyItem(container, position, object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            adapter.setPrimaryItem(container, position, object);
        }

        @Override
        public void notifyDataSetChanged() {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            adapter.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            adapter.unregisterDataSetObserver(observer);
        }

        @Override
        public Parcelable saveState() {
            return adapter.saveState();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            adapter.restoreState(state, loader);
        }

        @Override
        public void startUpdate(ViewGroup container) {
            adapter.startUpdate(container);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            adapter.finishUpdate(container);
        }
    }

}