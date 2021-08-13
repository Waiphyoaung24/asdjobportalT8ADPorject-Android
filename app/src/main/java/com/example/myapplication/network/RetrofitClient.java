package com.example.myapplication.network;



import android.util.Log;

import com.example.myapplication.data.Token;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private Response response;
    private Token token;

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    private RetrofitClient() {
            response = new Retrofit.Builder()
                    .baseUrl(Response.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(Response.class);
    }

    public static synchronized RetrofitClient getInstance(Token token) {
        if (instance == null) {
            instance = new RetrofitClient(token);
            Log.i("instantiate success","");
        }
        return instance;
    }

    private RetrofitClient(Token token_){
        this.token = token_;
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token.getAccess_token())
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        response = new Retrofit.Builder().client(client)
                .baseUrl(Response.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Response.class);
    }

    public Response getResponse() {
        return response;
    }

}

