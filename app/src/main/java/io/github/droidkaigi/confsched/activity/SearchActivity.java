package io.github.droidkaigi.confsched.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.databinding.ActivitySearchBinding;
import io.github.droidkaigi.confsched.databinding.ItemSearchResultBinding;
import io.github.droidkaigi.confsched.model.SearchResult;
import io.github.droidkaigi.confsched.util.AnalyticsUtil;
import io.github.droidkaigi.confsched.widget.ArrayRecyclerAdapter;
import io.github.droidkaigi.confsched.widget.BindingHolder;
import io.github.droidkaigi.confsched.widget.itemdecoration.DividerItemDecoration;
import rx.Observable;

public class SearchActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final int REQ_DETAIL = 1;

    @Inject
    AnalyticsUtil analyticsUtil;
    @Inject
    ActivityNavigator activityNavigator;
    @Inject
    SessionDao dao;

    private SearchResultsAdapter adapter;
    private ActivitySearchBinding binding;

    static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_fade_enter, R.anim.activity_fade_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        MainApplication.getComponent(this).inject(this);

        initToolbar();
        initRecyclerView();

        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        analyticsUtil.sendScreenView("search");
    }

    private void initToolbar() {
        setSupportActionBar(binding.searchToolbar.getToolbar());

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);
        }

        binding.searchToolbar.addTextChangedListener(this);
    }

    private void initRecyclerView() {
        adapter = new SearchResultsAdapter(this);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        List<SearchResult> searchResults = Observable.from(dao.findAll().toBlocking().single())
                .map(SearchResult::createTitleType)
                .toList()
                .toBlocking()
                .single();
        adapter.setAllList(searchResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_fade_exit);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Loading View
        adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Do nothing
    }


    private class SearchResultsAdapter extends ArrayRecyclerAdapter<SearchResult, BindingHolder<ItemSearchResultBinding>>
            implements Filterable {

        private List<SearchResult> filteredList;
        private List<SearchResult> allList;

        public SearchResultsAdapter(@NonNull Context context) {
            super(context);
            this.filteredList = new ArrayList<>();
        }

        public void setAllList(List<SearchResult> searchResults) {
            this.allList = searchResults;
        }

        @Override
        public BindingHolder<ItemSearchResultBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BindingHolder<>(getContext(), parent, R.layout.item_search_result);
        }

        @Override
        public void onBindViewHolder(BindingHolder<ItemSearchResultBinding> holder, int position) {
            SearchResult searchResult = getItem(position);
            ItemSearchResultBinding binding = holder.binding;
            binding.setSearchResult(searchResult);

            binding.getRoot().setOnClickListener(v ->
                    activityNavigator.showSessionDetail(SearchActivity.this, searchResult.session, REQ_DETAIL));
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    filteredList.clear();
                    FilterResults results = new FilterResults();

                    if (constraint.length() > 0) {
                        final String filterPattern = constraint.toString().toLowerCase().trim();
                        Observable.from(allList)
                                .filter(searchResult -> searchResult.text.toLowerCase().contains(filterPattern))
                                .forEach(filteredList::add);
                    }

                    results.values = filteredList;
                    results.count = filteredList.size();

                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    clear();
                    addAll((List<SearchResult>) results.values);
                    notifyDataSetChanged();
                }
            };
        }

    }

}
