package com.uminski.crypto_mobile.recyclerview;

import com.uminski.crypto_mobile.client.server.VisitResponse;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class VisitListView {

    String owner;
    UUID visitId;
    String status;
    String date;
    Boolean verifiedProperly;

    public static VisitListView fromVisitResponse(VisitResponse visitResponse) {
        return new VisitListView(visitResponse.getAuthor(),
                visitResponse.getVisitId(),
                visitResponse.getStatus(),
                visitResponse.getDate(),
                visitResponse.getProtocol() != null && visitResponse.getProtocol().getHolds() != null && visitResponse.getProtocol().getHolds());
    }
}
