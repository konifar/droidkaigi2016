package com.konifar.confsched.api;

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

    final DroidKaigiService service;

    @Inject
    public DroidKaigiClient(OkHttpClient client) {
        Retrofit feedburnerRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(END_POINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = feedburnerRetrofit.create(DroidKaigiService.class);
    }

    public interface DroidKaigiService {

        @GET("/konifar/conference-sched/master/app/src/main/res/raw/data.json")
        Observable<List<Session>> getSessions();

    }

}
