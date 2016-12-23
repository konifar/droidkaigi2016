package io.github.droidkaigi.confsched.di.scope;

import static android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import dagger.android.ReleaseReferencesAt;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Scope;

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@ReleaseReferencesAt(TRIM_MEMORY_UI_HIDDEN)
@Scope
public @interface ReleaseWhenUiHidden {
}
