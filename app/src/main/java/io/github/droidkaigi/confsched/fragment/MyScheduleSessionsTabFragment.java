package io.github.droidkaigi.confsched.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Pair;
import android.view.View;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.droidkaigi.confsched.databinding.ItemSessionBinding;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.widget.BindingHolder;

public class MyScheduleSessionsTabFragment extends SessionsTabFragment {

    @NonNull
    public static MyScheduleSessionsTabFragment newInstance(List<Session> sessions) {
        MyScheduleSessionsTabFragment fragment = new MyScheduleSessionsTabFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SESSIONS, Parcels.wrap(sessions));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected SessionsAdapter createAdapter() {
        return new MyScheduleAdapter(getContext());
    }

    private class MyScheduleAdapter extends SessionsAdapter {

        private Map<Long, Pair<Integer, Integer>> rangeMap = new ArrayMap<>();

        public MyScheduleAdapter(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onBindViewHolder(BindingHolder<ItemSessionBinding> holder, int position) {
            super.onBindViewHolder(holder, position);

            ItemSessionBinding binding = holder.binding;

            if (isConflicted(position)) {
                binding.txtConflict.setVisibility(View.VISIBLE);
            } else {
                binding.txtConflict.setVisibility(View.GONE);
            }
        }

        private Pair<Integer, Integer> getRangeOfConflictCandidates(int position) {
            Session session = getItem(position);

            if (rangeMap.containsKey(session.stime.getTime())) {
                return rangeMap.get(session.stime.getTime());
            }

            List<Session> candidates = new ArrayList<>();
            candidates.add(session);

            int startOffset = 1;

            while (position - startOffset >= 0) {
                Session s = getItem(position - startOffset);

                if (s.stime.getTime() == session.stime.getTime()) {
                    candidates.add(s);
                } else {
                    break;
                }

                startOffset++;
            }

            for (int offset = 1, count = getItemCount(); position + offset < count; offset++) {
                Session s = getItem(position + offset);

                if (s.stime.getTime() == session.stime.getTime()) {
                    candidates.add(s);
                } else {
                    break;
                }
            }

            Pair<Integer, Integer> range = Pair.create(position - startOffset + 1, candidates.size());
            rangeMap.put(session.stime.getTime(), range);

            return range;
        }

        private boolean isConflicted(int position) {
            Session session = getItem(position);

            if (!session.checked) {
                return false;
            }

            Pair<Integer, Integer> range = getRangeOfConflictCandidates(position);

            for (int i = range.first; i < range.first + range.second; i++) {
                if (i == position) {
                    continue;
                }

                if (getItem(i).checked) {
                    return true;
                }
            }

            return false;
        }

        @Override
        protected void refresh(@NonNull Session session) {
            for (int i = 0, count = getItemCount(); i < count; i++) {
                Session s = getItem(i);
                if (session.equals(s)) {
                    s.checked = session.checked;
                    notifyItemChangedToAffected(i);
                    break;
                }
            }
        }

        private void notifyItemChangedToAffected(int position) {
            Pair<Integer, Integer> range = getRangeOfConflictCandidates(position);
            notifyItemRangeChanged(range.first, range.second);
        }

        @Override
        protected void onLikeChanged(@NonNull Session session, int position) {
            super.onLikeChanged(session, position);

            Pair<Integer, Integer> range = getRangeOfConflictCandidates(position);
            if (range.second > 1) {
                notifyItemRangeChanged(range.first, range.second);
            }
        }
    }

}
