package com.app.workschedule.Retrofit;

import com.app.workschedule.Utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceFactory {

    private static WebService mService;


    public static WebService getInstance() {
        if (mService == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // add your other interceptors …
            httpClient.readTimeout(180, TimeUnit.SECONDS);
            httpClient.connectTimeout(180, TimeUnit.SECONDS);


            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            mService = retrofit.create(WebService.class);
        }
        return mService;
    }
}
