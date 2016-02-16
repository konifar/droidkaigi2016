package io.github.droidkaigi.confsched.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.dao.CategoryDao;
import io.github.droidkaigi.confsched.dao.PlaceDao;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.databinding.ActivitySearchBinding;
import io.github.droidkaigi.confsched.databinding.ItemSearchResultBinding;
import io.github.droidkaigi.confsched.model.SearchResult;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AnalyticsTracker;
import io.github.droidkaigi.confsched.util.LocaleUtil;
import io.github.droidkaigi.confsched.widget.ArrayRecyclerAdapter;
import io.github.droidkaigi.confsched.widget.BindingHolder;
import io.github.droidkaigi.confsched.widget.itemdecoration.DividerItemDecoration;
import rx.Observable;

public class SearchActivity extends AppCompatActivity implements TextWatcher {

    public static final String RESULT_STATUS_CHANGED_SESSIONS = "statusChangedSessions";
    private static final int REQ_DETAIL = 1;

    private static final int REQ_SEARCH_PLACES_AND_CATEGORIES_VIEW = 2;

    @Inject
    AnalyticsTracker analyticsTracker;
    @Inject
    ActivityNavigator activityNavigator;
    @Inject
    SessionDao sessionDao;
    @Inject
    PlaceDao placeDao;
    @Inject
    CategoryDao categoryDao;

    List<Session> statusChangedSessions = new ArrayList<>();

    private SearchResultsAdapter adapter;
    private ActivitySearchBinding binding;

    static void start(@NonNull Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), SearchActivity.class);
        fragment.startActivityForResult(intent, requestCode);
        fragment.getActivity().overridePendingTransition(0, R.anim.activity_fade_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        MainApplication.getComponent(this).inject(this);

        initToolbar();
        initRecyclerView();
        initPlacesAndCategoriesView();

        loadData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RESULT_STATUS_CHANGED_SESSIONS, Parcels.wrap(statusChangedSessions));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        statusChangedSessions = Parcels.unwrap(savedInstanceState.getParcelable(RESULT_STATUS_CHANGED_SESSIONS));
    }

    private void initPlacesAndCategoriesView() {
        binding.searchPlacesAndCategoriesView.addPlaces(placeDao.findAll());
        binding.searchPlacesAndCategoriesView.addCategories(categoryDao.findAll());
        binding.searchPlacesAndCategoriesView.setOnClickSearchGroup(searchGroup ->
                startActivityForResult(SearchedSessionsActivity.createIntent(SearchActivity.this, searchGroup),
                        REQ_SEARCH_PLACES_AND_CATEGORIES_VIEW));
    }

    @Override
    protected void onStart() {
        super.onStart();
        analyticsTracker.sendScreenView("search");
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
        // TODO It's waste logic...
        List<Session> sessions = sessionDao.findAll().toBlocking().single();

        List<SearchResult> titleResults = Observable.from(sessions)
                .map(SearchResult::createTitleType)
                .toList().toBlocking().single();

        List<SearchResult> descriptionResults = Observable.from(sessions)
                .map(SearchResult::createDescriptionType)
                .toList().toBlocking().single();

        List<SearchResult> speakerResults = Observable.from(sessions)
                .map(SearchResult::createSpeakerType)
                .toList().toBlocking().single();

        titleResults.addAll(descriptionResults);
        titleResults.addAll(speakerResults);
        adapter.setAllList(titleResults);
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
        Intent intent = new Intent();
        intent.putExtra(RESULT_STATUS_CHANGED_SESSIONS, Parcels.wrap(statusChangedSessions));
        setResult(Activity.RESULT_OK, intent);
        overridePendingTransition(0, R.anim.activity_fade_exit);
        super.finish();
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
        adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            binding.searchPlacesAndCategoriesView.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.searchPlacesAndCategoriesView.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_DETAIL: {
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }
                Session session = Parcels.unwrap(data.getParcelableExtra(Session.class.getSimpleName()));
                statusChangedSessions.add(session);
                break;
            }
            case REQ_SEARCH_PLACES_AND_CATEGORIES_VIEW: {
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }
                List<Session> sessions = Parcels.unwrap(data.getParcelableExtra(RESULT_STATUS_CHANGED_SESSIONS));
                statusChangedSessions.addAll(sessions);
                break;
            }
        }
    }

    private class SearchResultsAdapter
            extends ArrayRecyclerAdapter<SearchResult, BindingHolder<ItemSearchResultBinding>>
            implements Filterable {

        private static final String ELLIPSIZE_TEXT = "...";
        private static final int ELLIPSIZE_LIMIT_COUNT = 30;

        private TextAppearanceSpan textAppearanceSpan;
        private List<SearchResult> filteredList;
        private List<SearchResult> allList;

        public SearchResultsAdapter(@NonNull Context context) {
            super(context);
            this.filteredList = new ArrayList<>();
            this.textAppearanceSpan = new TextAppearanceSpan(context, R.style.SearchResultAppearance);
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
            ItemSearchResultBinding itemBinding = holder.binding;
            itemBinding.setSearchResult(searchResult);

            Drawable icon = ContextCompat.getDrawable(getContext(), searchResult.iconRes);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                itemBinding.txtType.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, null, null, null);
            } else if (LocaleUtil.shouldRtl()) {
                itemBinding.txtType.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
            } else {
                itemBinding.txtType.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            }

            bindText(itemBinding.txtSearchResult, searchResult, binding.searchToolbar.getText());

            itemBinding.getRoot().setOnClickListener(v ->
                    activityNavigator.showSessionDetail(SearchActivity.this, searchResult.session, REQ_DETAIL));
        }

        private void bindText(TextView textView, SearchResult searchResult, String searchText) {
            String text = searchResult.text;
            if (TextUtils.isEmpty(text)) return;

            text = text.replace("\n", "  ");

            if (TextUtils.isEmpty(searchText)) {
                textView.setText(text);
            } else {
                int idx = text.toLowerCase().indexOf(searchText.toLowerCase());
                if (idx >= 0) {
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append(text);
                    builder.setSpan(
                            textAppearanceSpan,
                            idx,
                            idx + searchText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                    if (idx > ELLIPSIZE_LIMIT_COUNT && searchResult.isDescriptionType()) {
                        builder.delete(0, idx - ELLIPSIZE_LIMIT_COUNT);
                        builder.insert(0, ELLIPSIZE_TEXT);
                    }

                    textView.setText(builder);
                } else {
                    textView.setText(text);
                }
            }
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
