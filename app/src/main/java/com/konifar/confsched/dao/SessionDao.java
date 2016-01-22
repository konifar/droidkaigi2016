package com.konifar.confsched.dao;

import android.support.annotation.NonNull;

import com.github.gfx.android.orma.TransactionTask;
import com.konifar.confsched.model.Category;
import com.konifar.confsched.model.Category_Relation;
import com.konifar.confsched.model.OrmaDatabase;
import com.konifar.confsched.model.Place;
import com.konifar.confsched.model.Place_Relation;
import com.konifar.confsched.model.Session;
import com.konifar.confsched.model.Session_Relation;
import com.konifar.confsched.model.Speaker;
import com.konifar.confsched.model.Speaker_Relation;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class SessionDao {

    OrmaDatabase orma;

    @Inject
    public SessionDao(OrmaDatabase orma) {
        this.orma = orma;
    }

    private Session_Relation sessionRelation() {
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
                Observable.from(sessions).forEach(session -> {
                    session.prepareSave();
                    insertSpeaker(session.speaker);
                    insertCategory(session.category);
                    insertPlace(session.place);
                });

                sessionRelation().inserter().executeAll(sessions);
            }
        });
    }

    private void insertSpeaker(Speaker speaker) {
        if (speaker != null && speakerRelation().selector().idEq(speaker.id).count() == 0) {
            speakerRelation().inserter().execute(speaker);
        }
    }

    private void insertPlace(Place place) {
        if (place != null && placeRelation().selector().idEq(place.id).count() == 0) {
            placeRelation().inserter().execute(place);
        }
    }

    private void insertCategory(Category category) {
        if (category != null && categoryRelation().selector().idEq(category.id).count() == 0) {
            categoryRelation().inserter().execute(category);
        }
    }

    public List<Session> findAll() {
        return sessionRelation().selector().toList();
    }

    public void deleteAll() {
        sessionRelation().deleter().execute();
        speakerRelation().deleter().execute();
        categoryRelation().deleter().execute();
        placeRelation().deleter().execute();
    }

}
