package com.example.aibreathapp.api;

import android.util.Log;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String TAG = "ApiClient";

    // URL pour appareil physique (votre ordinateur)
//    private static final String BASE_URL = "http://172.20.10.4:8000/";
    private static final String BASE_URL = "http://172.20.10.4:8000/";

    // URL alternative pour émulateur
    // private static final String BASE_URL = "http://10.0.2.2:8000/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            Log.d(TAG, "Creating new Retrofit client with base URL: " + BASE_URL);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);

            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}