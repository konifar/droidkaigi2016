package io.github.droidkaigi.confsched.model;

public interface SearchGroup {

    int getId();

    String getName();

    Type getType();

    enum Type {CATEGORY, PLACE, TITLE}

}
