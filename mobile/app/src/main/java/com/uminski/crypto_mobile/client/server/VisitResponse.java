package com.uminski.crypto_mobile.client.server;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class VisitResponse {

    String author;

    UUID authorId;

    String date;

    UUID guestId;

    ProtocolDTO protocol;

    String status;

    UUID visitId;
}
