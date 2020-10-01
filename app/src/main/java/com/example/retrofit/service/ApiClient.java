package com.example.retrofit.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {





    private static String BASE_URL = "https://akustikaraoke.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){

        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(new OkHttpClient())
                        .build();
            return retrofit;
        }

        return retrofit;

    }


}
