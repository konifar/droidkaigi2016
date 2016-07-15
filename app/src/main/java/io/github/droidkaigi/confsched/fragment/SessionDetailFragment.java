package io.github.droidkaigi.confsched.fragment;

import android.app.Activity;
import android.content.Context;
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
import io.github.droidkaigi.confsched.model.Category;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AppUtil;
import io.github.droidkaigi.confsched.viewmodel.SessionDetailViewModel;

public class SessionDetailFragment extends BaseFragment {

    @Inject
    SessionDetailViewModel viewModel;

    private FragmentSessionDetailBinding binding;

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
        Session session = Parcels.unwrap(getArguments().getParcelable(Session.class.getSimpleName()));
        viewModel.setSession(session);

        Activity activity = getActivity();

        Category category = viewModel.session.category;
        if (category != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.setTheme(category.getThemeResId());
            }
            AppUtil.setTaskDescription(activity, session.title, ContextCompat.getColor(activity, category.getVividColorResId()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionDetailBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        setHasOptionsMenu(true);
        initToolbar();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

}
