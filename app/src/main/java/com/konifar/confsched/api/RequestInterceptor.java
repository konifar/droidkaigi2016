package com.konifar.confsched.api;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class RequestInterceptor implements Interceptor {

    final ConnectivityManager connectivityManager;

    @Inject
    public RequestInterceptor(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder r = chain.request().newBuilder();
        if (isConnected()) {
            int maxAge = 2 * 60;
            r.addHeader("cache-control", "public, max-age=" + maxAge);
        } else {
            int maxStale = 30 * 24 * 60 * 60; // 30 days
            r.addHeader("cache-control", "public, only-if-cached, max-stale=" + maxStale);
        }

        return chain.proceed(r.build());
    }

    protected boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}
