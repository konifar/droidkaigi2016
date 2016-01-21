package com.konifar.confsched.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.konifar.confsched.model.Session;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    @Inject
    public DroidKaigiClient(OkHttpClient client) {
        Gson gson = new GsonBuilder().setDateFormat(JSON_DATE_FORMAT).create();
        Retrofit feedburnerRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(END_POINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = feedburnerRetrofit.create(DroidKaigiService.class);
    }

    public Observable<List<Session>> getSessions() {
        return service.getSessions();
    }

    public interface DroidKaigiService {

        @GET("/konifar/conference-sched/master/app/src/main/res/raw/data.json")
        Observable<List<Session>> getSessions();

    }

}
