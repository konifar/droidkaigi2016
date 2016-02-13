package io.github.droidkaigi.confsched.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.api.DroidKaigiClient;
import io.github.droidkaigi.confsched.dao.ContributorDao;
import io.github.droidkaigi.confsched.databinding.ActivityContributorsBinding;
import io.github.droidkaigi.confsched.databinding.ItemContributorBinding;
import io.github.droidkaigi.confsched.model.Contributor;
import io.github.droidkaigi.confsched.util.AnalyticsTracker;
import io.github.droidkaigi.confsched.util.AppUtil;
import io.github.droidkaigi.confsched.widget.ArrayRecyclerAdapter;
import io.github.droidkaigi.confsched.widget.BindingHolder;
import io.github.droidkaigi.confsched.widget.itemdecoration.DividerItemDecoration;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ContributorsActivity extends AppCompatActivity {

    public static final String TAG = ContributorsActivity.class.getSimpleName();

    @Inject
    AnalyticsTracker analyticsTracker;

    @Inject
    DroidKaigiClient client;

    @Inject
    ContributorDao dao;

    @Inject
    CompositeSubscription compositeSubscription;

    private ActivityContributorsBinding binding;

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, ContributorsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contributors);
        MainApplication.getComponent(this).inject(this);
        initToolbar();
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.border_small));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        compositeSubscription.add(fetchContributors());
    }

    private Subscription fetchContributors() {
        return client.getContributors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> binding.progressBar.setVisibility(View.VISIBLE))
                .doOnCompleted(() -> binding.progressBar.setVisibility(View.GONE))
                .subscribe(
                        this::renderContributors,
                        this::renderSavedContributors
                );
    }

    private void renderContributors(List<Contributor> contributors) {
        ContributorsAdapter contributorsAdapter = new ContributorsAdapter(this);
        contributorsAdapter.addAll(contributors);
        binding.recyclerView.setAdapter(contributorsAdapter);
        dao.upserterAll(contributors);
    }

    // renderSavedContributors is called when error
    private void renderSavedContributors(Throwable throwable) {
        Log.e(TAG, "Failed to fetchContributors.", throwable);
        List<Contributor> contributors = dao.findAll();
        ContributorsAdapter contributorsAdapter = new ContributorsAdapter(this);
        contributorsAdapter.addAll(contributors);
        binding.recyclerView.setAdapter(contributorsAdapter);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);
        }
        binding.toolbar.setTitle(getString(R.string.about_contributors));
    }

    @Override
    protected void onStart() {
        super.onStart();
        analyticsTracker.sendScreenView("contributors");
    }

    @Override
    protected void onDestroy() {
        compositeSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ContributorsAdapter extends ArrayRecyclerAdapter<Contributor, BindingHolder<ItemContributorBinding>> {

        public ContributorsAdapter(Context context) {
            super(context);
        }

        @Override
        public BindingHolder<ItemContributorBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BindingHolder<>(getContext(), parent, R.layout.item_contributor);
        }

        @Override
        public void onBindViewHolder(BindingHolder<ItemContributorBinding> holder, int position) {
            Contributor contributor = getItem(position);
            ItemContributorBinding itemBinding = holder.binding;
            itemBinding.setContributor(contributor);
            itemBinding.getRoot().setOnClickListener(v ->
                    AppUtil.showWebPage(ContributorsActivity.this, contributor.htmlUrl));
        }

    }

}
