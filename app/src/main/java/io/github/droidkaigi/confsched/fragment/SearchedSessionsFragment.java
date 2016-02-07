package io.github.droidkaigi.confsched.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import org.parceler.Parcels;

import java.util.List;

import io.github.droidkaigi.confsched.model.SearchGroup;
import io.github.droidkaigi.confsched.model.Session;
import rx.Observable;
import rx.Subscription;

public class SearchedSessionsFragment extends SessionsFragment {

    private SearchGroup searchGroup;

    public static SearchedSessionsFragment newInstance(SearchGroup searchGroup) {
        SearchedSessionsFragment fragment = new SearchedSessionsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SearchGroup.class.getSimpleName(), Parcels.wrap(searchGroup));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchGroup = Parcels.unwrap(getArguments().getParcelable(SearchGroup.class.getSimpleName()));
    }

    @Override
    protected Subscription loadData() {
        return getSessionsAsObservable().subscribe(sessions -> {
            if (!sessions.isEmpty()) {
                groupByDateSessions(sessions);
            }
        });
    }

    private Observable<List<Session>> getSessionsAsObservable() {
        switch (searchGroup.getType()) {
            case CATEGORY:
                return dao.findByCategory(searchGroup.getId());
            case PLACE:
                return dao.findByPlace(searchGroup.getId());
            default:
                throw new IllegalStateException("Search type: " + searchGroup.getType() + " is invalid.");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Do nothing
    }

}
