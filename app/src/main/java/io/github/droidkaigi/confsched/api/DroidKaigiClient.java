package io.github.droidkaigi.confsched.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.droidkaigi.confsched.model.Session;
import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import rx.Observable;

@Singleton
public class DroidKaigiClient {

    private static final String END_POINT = "https://raw.githubusercontent.com";
    private static final String JSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final DroidKaigiService service;

    public static Gson createGson() {
        return new GsonBuilder().setDateFormat(JSON_DATE_FORMAT).create();
    }

    @Inject
    public DroidKaigiClient(OkHttpClient client) {
        Retrofit feedburnerRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(END_POINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build();
        service = feedburnerRetrofit.create(DroidKaigiService.class);
    }

    public Observable<List<Session>> getSessions() {
        return service.getSessions();
    }

    public interface DroidKaigiService {

        @GET("/konifar/droidkaigi2016/master/app/src/main/res/raw/sessions_ja.json")
        Observable<List<Session>> getSessions();

    }

}
