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

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ActivitySearchBinding;
import io.github.droidkaigi.confsched.databinding.ItemSearchResultBinding;
import io.github.droidkaigi.confsched.model.SearchResult;
import io.github.droidkaigi.confsched.util.AnalyticsUtil;
import io.github.droidkaigi.confsched.widget.ArrayRecyclerAdapter;
import io.github.droidkaigi.confsched.widget.BindingHolder;

public class SearchActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final int REQ_DETAIL = 1;

    @Inject
    AnalyticsUtil analyticsUtil;
    @Inject
    ActivityNavigator activityNavigator;

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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        // TODO
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Do nothing
    }


    private class SearchResultsAdapter extends ArrayRecyclerAdapter<SearchResult, BindingHolder<ItemSearchResultBinding>> {

        public SearchResultsAdapter(@NonNull Context context) {
            super(context);
        }

        @Override
        public BindingHolder<ItemSearchResultBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BindingHolder<>(getContext(), parent, R.layout.item_session);
        }

        @Override
        public void onBindViewHolder(BindingHolder<ItemSearchResultBinding> holder, int position) {
            SearchResult searchResult = getItem(position);
            ItemSearchResultBinding binding = holder.binding;
            binding.setSearchResult(searchResult);

            binding.getRoot().setOnClickListener(v ->
                    activityNavigator.showSessionDetail(SearchActivity.this, searchResult.session, REQ_DETAIL));
        }

    }

}
