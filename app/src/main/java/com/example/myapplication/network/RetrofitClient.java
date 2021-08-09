package com.example.myapplication.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private Response response;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Response.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        response = retrofit.create(Response.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public Response getResponse() {
        return response;
    }
}
