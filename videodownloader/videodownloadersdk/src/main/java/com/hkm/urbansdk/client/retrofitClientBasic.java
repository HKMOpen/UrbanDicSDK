package com.hkm.urbansdk.client;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hkm.urbansdk.Constant;
import com.hkm.urbansdk.gson.GsonFactory;
import com.hkm.urbansdk.gson.RealmExclusion;
import com.hkm.urbansdk.gson.WordpressConversion;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by zJJ on 1/16/2016.
 */
public abstract class retrofitClientBasic {
    protected Retrofit api;
    private Context context;
    protected Gson gsonsetup;
    protected Interceptor interceptor;
    protected HttpLoggingInterceptor loglevel;


    public retrofitClientBasic(Context context) {
        setContext(context);
        jsoncreate();
        createLogLevel();
        createIntercept();
        registerAdapter();
    }

    protected void setContext(Context c) {
        this.context = c;
    }

    public Context getContext() {
        return this.context;
    }

    protected void jsoncreate() {
        gsonsetup = new GsonBuilder()
                .setDateFormat(Constant.DATE_FORMAT)
                .registerTypeAdapterFactory(new GsonFactory.NullStringToEmptyAdapterFactory())
                .registerTypeAdapter(String.class, new WordpressConversion())
                .setExclusionStrategies(new RealmExclusion())
                .create();
    }

    protected void registerAdapter() {
        // Add the interceptor to OkHttpClient
        api = new Retrofit.Builder()
                .baseUrl(getBaseEndpoint())
                .addConverterFactory(GsonConverterFactory.create(gsonsetup))
                .client(createClient())
                .build();
    }

    protected abstract String getBaseEndpoint();

    private OkHttpClient createClient() {
        OkHttpClient http = new OkHttpClient();
        http.interceptors().add(interceptor);
        http.interceptors().add(loglevel);
        return http;
    }

    protected void createLogLevel() {
        loglevel = new HttpLoggingInterceptor();
        loglevel.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    protected void createIntercept() {
        interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = universal_header(chain.request().newBuilder()).build();
                return chain.proceed(newRequest);
            }
        };
    }

    protected Request.Builder universal_header(Request.Builder chain) {
        chain.addHeader("Accept", "application/json");
        return chain;
    }

}
