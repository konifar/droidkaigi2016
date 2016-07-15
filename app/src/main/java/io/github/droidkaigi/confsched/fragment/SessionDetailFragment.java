package io.github.droidkaigi.confsched.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.FragmentSessionDetailBinding;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AppUtil;
import io.github.droidkaigi.confsched.viewmodel.SessionDetailViewModel;
import io.github.droidkaigi.confsched.viewmodel.event.EventBus;
import io.github.droidkaigi.confsched.viewmodel.event.SessionSelectedChangedEvent;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class SessionDetailFragment extends BaseFragment {

    @Inject
    SessionDetailViewModel viewModel;
    @Inject
    EventBus eventBus;
    @Inject
    CompositeSubscription compositeSubscription;

    private FragmentSessionDetailBinding binding;
    private Session session;

    public static SessionDetailFragment create(@NonNull Session session) {
        SessionDetailFragment fragment = new SessionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Session.class.getSimpleName(), Parcels.wrap(session));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = Parcels.unwrap(getArguments().getParcelable(Session.class.getSimpleName()));
        Activity activity = getActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change theme by category
            activity.setTheme(session.category.getThemeResId());
        }
        AppUtil.setTaskDescription(activity, session.title, ContextCompat.getColor(activity, session.category.getVividColorResId()));
    }

    @Override
    public void onResume() {
        super.onResume();
        Subscription sub = eventBus.observe(SessionSelectedChangedEvent.class)
                .subscribe(event -> setResult(event.session));
        compositeSubscription.add(sub);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionDetailBinding.inflate(inflater, container, false);
        viewModel.setSession(session);
        binding.setViewModel(viewModel);
        setHasOptionsMenu(true);
        initToolbar();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeSubscription.unsubscribe();
        viewModel.destroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getComponent().inject(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (viewModel.shouldShowShareMenuItem()) {
            menuInflater.inflate(R.menu.menu_session_detail, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_share:
                viewModel.onClickShareMenuItem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(binding.toolbar);
        ActionBar bar = activity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);
        }
    }

    private void setResult(Session session) {
        Intent intent = new Intent();
        intent.putExtra(Session.class.getSimpleName(), Parcels.wrap(session));
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

}
