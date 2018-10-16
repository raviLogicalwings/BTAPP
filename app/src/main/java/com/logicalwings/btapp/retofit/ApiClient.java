package com.logicalwings.btapp.retofit;

import com.logicalwings.btapp.utils.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pandit on 05/09/2018.
 */

public class ApiClient {
    public static Retrofit instance;

    public static ApiInterface getApiClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().
                readTimeout(AppConstants.READ_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(AppConstants.READ_TIMEOUT, TimeUnit.SECONDS).
                connectTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);

        OkHttpClient okHttpClient = clientBuilder.build();
        instance = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return instance.create(ApiInterface.class);
    }
}