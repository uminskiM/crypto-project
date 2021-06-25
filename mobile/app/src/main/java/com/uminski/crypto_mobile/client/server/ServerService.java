package com.uminski.crypto_mobile.client.server;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ServerService {

    @POST("/visits")
    Call<Void> createVisit(@Body CreateVisitRequest createVisitRequest);

    @GET("/visits")
    Call<List<VisitResponse>> visits(@Query("status") String status);

    @PUT("/visits")
    Call<VisitResponse> updateVisit(@Query("status") String status,
                                    @Query("visit_id") String visitId,
                                    @Query("guest_id") String guestId,
                                    @Body ProtocolDTO protocolRequest);
}
