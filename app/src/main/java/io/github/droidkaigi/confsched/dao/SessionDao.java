package io.github.droidkaigi.confsched.dao;

import com.github.gfx.android.orma.TransactionTask;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.droidkaigi.confsched.model.Category;
import io.github.droidkaigi.confsched.model.Category_Relation;
import io.github.droidkaigi.confsched.model.OrmaDatabase;
import io.github.droidkaigi.confsched.model.Place;
import io.github.droidkaigi.confsched.model.Place_Relation;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.model.Session_Relation;
import io.github.droidkaigi.confsched.model.Session_Updater;
import io.github.droidkaigi.confsched.model.Speaker;
import io.github.droidkaigi.confsched.model.Speaker_Relation;
import rx.Observable;

@Singleton
public class SessionDao {

    OrmaDatabase orma;

    @Inject
    public SessionDao(OrmaDatabase orma) {
        this.orma = orma;
    }

    public Session_Relation sessionRelation() {
        return orma.relationOfSession();
    }

    private Speaker_Relation speakerRelation() {
        return orma.relationOfSpeaker();
    }

    private Place_Relation placeRelation() {
        return orma.relationOfPlace();
    }

    private Category_Relation categoryRelation() {
        return orma.relationOfCategory();
    }

    public void insertAll(@NonNull List<Session> sessions) {
        orma.transactionAsync(new TransactionTask() {
            @Override
            public void execute() throws Exception {
                for (Session session : sessions) {
                    insertSpeaker(session.speaker);
                    insertCategory(session.category);
                    insertPlace(session.place);
                }

                sessionRelation().inserter().executeAll(sessions);
            }
        });
    }

    private void insertSpeaker(Speaker speaker) {
        if (speaker != null && speakerRelation().selector().idEq(speaker.id).isEmpty()) {
            speakerRelation().inserter().execute(speaker);
        }
    }

    private void insertPlace(Place place) {
        if (place != null && placeRelation().selector().idEq(place.id).isEmpty()) {
            placeRelation().inserter().execute(place);
        }
    }

    private void insertCategory(Category category) {
        if (category != null && categoryRelation().selector().idEq(category.id).isEmpty()) {
            categoryRelation().inserter().execute(category);
        }
    }

    public Observable<List<Session>> findAll() {
        return sessionRelation().selector().executeAsObservable()
                .toList();
    }

    public Observable<List<Session>> findByChecked() {
        return sessionRelation().selector().checkedEq(true).executeAsObservable()
                .toList();
    }

    public Observable<List<Session>> findByPlace(int placeId) {
        return sessionRelation().selector().placeEq(placeId).executeAsObservable()
                .toList();
    }

    public Observable<List<Session>> findByCategory(int categoryId) {
        return sessionRelation().selector().categoryEq(categoryId).executeAsObservable()
                .toList();
    }

    public void deleteAll() {
        sessionRelation().deleter().execute();
        speakerRelation().deleter().execute();
        categoryRelation().deleter().execute();
        placeRelation().deleter().execute();
    }

    public void updateAllSync(List<Session> sessions) {
        speakerRelation().deleter().execute();
        categoryRelation().deleter().execute();
        placeRelation().deleter().execute();

        for (Session session : sessions) {
            insertSpeaker(session.speaker);
            insertCategory(session.category);
            insertPlace(session.place);
            if (sessionRelation().idEq(session.id).isEmpty()) {
                sessionRelation().inserter().execute(session);
            } else {
                update(session);
            }
        }
    }


    public void updateAllAsync(List<Session> sessions) {
        orma.transactionAsync(new TransactionTask() {
            @Override
            public void execute() throws Exception {
                updateAllSync(sessions);
            }
        });
    }

    private void update(Session session) {
        Session_Updater updater = sessionRelation().updater()
                .idEq(session.id)
                .title(session.title)
                .description(session.description)
                .speaker(session.speaker)
                .stime(session.stime)
                .etime(session.etime)
                .place(session.place)
                .languageId(session.languageId)
                .slideUrl(session.slideUrl)
                .movieUrl(session.movieUrl)
                .shareUrl(session.shareUrl);

        if (session.category != null) {
            updater.category(session.category);
        }

        updater.execute();
    }

    public void updateChecked(Session session) {
        sessionRelation().updater()
                .idEq(session.id)
                .checked(session.checked)
                .execute();
    }

}
