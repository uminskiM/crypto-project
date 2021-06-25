package com.uminski.crypto_mobile.client.keygen;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface KeyGenService {

    @GET("/parameters")
    Call<ParametersResponse> getParameters();

    @GET("/public-keys")
    Call<Map<String, PublicKeyAsMapElementResponse>> publicKeys();

    @POST("/public-keys")
    Call<PublicKeyResponse> addPublicKey(@Body PublicKeyRequest publicKeyRequest);

}
