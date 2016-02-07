package io.github.droidkaigi.confsched.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.droidkaigi.confsched.model.OrmaDatabase;
import io.github.droidkaigi.confsched.model.Place;
import io.github.droidkaigi.confsched.model.Place_Relation;

@Singleton
public class PlaceDao {

    OrmaDatabase orma;

    @Inject
    public PlaceDao(OrmaDatabase orma) {
        this.orma = orma;
    }

    private Place_Relation placeRelation() {
        return orma.relationOfPlace();
    }

    public List<Place> findAll() {
        return placeRelation().selector().toList();
    }

}
