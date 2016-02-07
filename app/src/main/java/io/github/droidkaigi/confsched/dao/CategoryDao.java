package io.github.droidkaigi.confsched.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.droidkaigi.confsched.model.Category;
import io.github.droidkaigi.confsched.model.Category_Relation;
import io.github.droidkaigi.confsched.model.OrmaDatabase;

@Singleton
public class CategoryDao {

    OrmaDatabase orma;

    @Inject
    public CategoryDao(OrmaDatabase orma) {
        this.orma = orma;
    }

    private Category_Relation categoryRelation() {
        return orma.relationOfCategory();
    }

    public List<Category> findAll() {
        return categoryRelation().selector().toList();
    }

}
