package io.github.droidkaigi.confsched.widget;

import com.github.gfx.android.orma.exception.InvalidStatementException;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ItemSearchCategoryBinding;
import io.github.droidkaigi.confsched.databinding.ItemSearchPlaceBinding;
import io.github.droidkaigi.confsched.databinding.ItemSearchTitleBinding;
import io.github.droidkaigi.confsched.databinding.ViewSearchPlacesAndCategoriesBinding;
import io.github.droidkaigi.confsched.model.Category;
import io.github.droidkaigi.confsched.model.Place;
import io.github.droidkaigi.confsched.model.SearchGroup;
import io.github.droidkaigi.confsched.widget.itemdecoration.DividerItemDecoration;

public class SearchPlacesAndCategoriesView extends FrameLayout {

    public interface OnClickSearchGroup {

        void onClickSearchGroup(SearchGroup searchGroup);
    }

    private ViewSearchPlacesAndCategoriesBinding binding;

    private SearchGroupsAdapter adapter;

    public SearchPlacesAndCategoriesView(Context context) {
        this(context, null);
    }

    public SearchPlacesAndCategoriesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchPlacesAndCategoriesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.view_search_places_and_categories, this, true);

        initRecyclerView();
    }

    public void addPlaces(List<Place> places) {
        adapter.addItem(new SearchTitle(getContext().getString(R.string.search_by_place)));
        adapter.addAll(new ArrayList<>(places));
    }

    public void addCategories(List<Category> categories) {
        adapter.addItem(new SearchTitle(getContext().getString(R.string.search_by_category)));
        adapter.addAll(new ArrayList<>(categories));
    }

    private void initRecyclerView() {
        adapter = new SearchGroupsAdapter(getContext());

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
    }

    public void setOnClickSearchGroup(OnClickSearchGroup onClickSearchGroup) {
        adapter.setOnClickSearchGroup(onClickSearchGroup);
    }

    private class SearchGroupsAdapter extends ArrayRecyclerAdapter<SearchGroup, BindingHolder<ViewDataBinding>> {

        private static final int TYPE_CATEGORY = 0;
        private static final int TYPE_PLACE = 1;
        private static final int TYPE_TITLE = 2;

        private OnClickSearchGroup onClickSearchGroup = searchGroup -> {
            // no op
        };

        public SearchGroupsAdapter(@NonNull Context context) {
            super(context);
        }

        public void setOnClickSearchGroup(OnClickSearchGroup onClickSearchGroup) {
            this.onClickSearchGroup = onClickSearchGroup;
        }

        @Override
        public BindingHolder<ViewDataBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_CATEGORY:
                    return new BindingHolder<>(getContext(), parent, R.layout.item_search_category);
                case TYPE_PLACE:
                    return new BindingHolder<>(getContext(), parent, R.layout.item_search_place);
                case TYPE_TITLE:
                    return new BindingHolder<>(getContext(), parent, R.layout.item_search_title);
                default:
                    throw new InvalidStatementException("ViewType is invalid: " + viewType);
            }
        }

        @Override
        public void onBindViewHolder(BindingHolder<ViewDataBinding> holder, int position) {
            SearchGroup searchGroup = getItem(position);
            switch (searchGroup.getType()) {
                case CATEGORY:
                    ItemSearchCategoryBinding categoryBinding = (ItemSearchCategoryBinding) holder.binding;
                    categoryBinding.setCategory((Category) searchGroup);
                    categoryBinding.getRoot().setOnClickListener(v -> showSearchedSessions(searchGroup));
                    break;
                case PLACE:
                    ItemSearchPlaceBinding placeBinding = (ItemSearchPlaceBinding) holder.binding;
                    placeBinding.setPlace((Place) searchGroup);
                    placeBinding.getRoot().setOnClickListener(v -> showSearchedSessions(searchGroup));
                    break;
                default:
                    ItemSearchTitleBinding titleBinding = (ItemSearchTitleBinding) holder.binding;
                    titleBinding.txtTitle.setText(searchGroup.getName());
                    break;
            }
        }

        private void showSearchedSessions(SearchGroup searchGroup) {
            onClickSearchGroup.onClickSearchGroup(searchGroup);
        }

        @Override
        public int getItemViewType(int position) {
            SearchGroup searchGroup = getItem(position);

            switch (searchGroup.getType()) {
                case CATEGORY:
                    return TYPE_CATEGORY;
                case PLACE:
                    return TYPE_PLACE;
                case TITLE:
                    return TYPE_TITLE;
                default:
                    throw new IllegalStateException("ViewType: " + searchGroup.getType() + " is invalid.");
            }
        }

    }

    private class SearchTitle implements SearchGroup {

        private String title;

        public SearchTitle(String title) {
            this.title = title;
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public String getName() {
            return title;
        }

        @Override
        public Type getType() {
            return Type.TITLE;
        }

    }

}
