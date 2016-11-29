package io.github.droidkaigi.confsched.di.scope;

import java.lang.annotation.Annotation;

import javax.inject.Qualifier;

@Qualifier
public @interface InScope {
    Class<? extends Annotation> value();
}
