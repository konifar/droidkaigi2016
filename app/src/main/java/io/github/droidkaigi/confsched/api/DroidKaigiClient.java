package io.github.droidkaigi.confsched.api;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.droidkaigi.confsched.model.Contributor;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.model.SessionFeedback;
import io.github.droidkaigi.confsched.util.LocaleUtil;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

@Singleton
public class DroidKaigiClient {

    private static final String SESSIONS_API_ROUTES = "/konifar/droidkaigi2016/master/app/src/main/res/raw/";

    private final DroidKaigiService service;
    private final GoogleFormService googleFormService;
    private final GithubService githubService;

    @Inject
    public DroidKaigiClient(OkHttpClient client) {
        Retrofit feedburnerRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://raw.githubusercontent.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build();
        service = feedburnerRetrofit.create(DroidKaigiService.class);

        Retrofit googleFormRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://docs.google.com/forms/d/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build();
        googleFormService = googleFormRetrofit.create(GoogleFormService.class);

        Retrofit githubRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.github.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build();
        githubService = githubRetrofit.create(GithubService.class);
    }

    public static Gson createGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public Observable<List<Session>> getSessions(@NonNull String languageId) {
        switch (languageId) {
            case LocaleUtil.LANG_JA_ID:
                return service.getSessionsJa();
            case LocaleUtil.LANG_AR_ID:
                return service.getSessionsAr();
            case LocaleUtil.LANG_KO_ID:
                return service.getSessionsEn();
            case LocaleUtil.LANG_EN_ID:
                return service.getSessionsEn();
            default:
                return service.getSessionsEn();
        }
    }

    public Observable<Response<Void>> submitSessionFeedback(SessionFeedback f) {
        return googleFormService.submitSessionFeedback(f.sessionId, f.sessionName, f.relevancy, f.asExpected, f.difficulty, f.knowledgeable, f.comment);
    }

    public Observable<List<Contributor>> getContributors() {
        return githubService.getContributors("konifar", "droidkaigi2016");
    }

    public interface DroidKaigiService {

        @GET(SESSIONS_API_ROUTES + "sessions_ja.json")
        Observable<List<Session>> getSessionsJa();

        @GET(SESSIONS_API_ROUTES + "sessions_en.json")
        Observable<List<Session>> getSessionsEn();

        @GET(SESSIONS_API_ROUTES + "sessions_ar.json")
        Observable<List<Session>> getSessionsAr();

    }

    public interface GoogleFormService {
        @POST("1PrHh5PXH1NkBbDe7eFfOuu311X4LlyKF95TBYFFy6nw/formResponse")
        @FormUrlEncoded
        Observable<Response<Void>> submitSessionFeedback(
                @Field("entry.36792886") int id,
                @Field("entry.288043897") String name,
                @Field("entry.1914381797") int relevancy,
                @Field("entry.907172560") int asExpected,
                @Field("entry.1839418272") int difficulty,
                @Field("entry.675295234") int knowledgeable,
                @Field("entry.1455307059") String comment
        );
    }

    public interface GithubService {
        @GET("/repos/{owner}/{repo}/contributors?per_page=100")
        Observable<List<Contributor>> getContributors(@Path("owner") String owner, @Path("repo") String repo);
    }
}
