package com.uminski.crypto_mobile.client;

import com.uminski.crypto_mobile.client.keygen.KeyGenService;
import com.uminski.crypto_mobile.client.server.ServerService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit keyGenRetrofit;
    private static final String KEY_GEN_BASE_URL = "http://10.0.2.2:5001";

    private static Retrofit serverRetrofit;
    private static final String SERVER_BASE_URL = "http://10.0.2.2:5002";

    public static KeyGenService getKeyGenRetrofit() {
        if (keyGenRetrofit == null) {
            keyGenRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(KEY_GEN_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return keyGenRetrofit.create(KeyGenService.class);
    }

    public static ServerService getServerRetrofit() {
        if (serverRetrofit == null) {
            serverRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(SERVER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return serverRetrofit.create(ServerService.class);
    }
}
