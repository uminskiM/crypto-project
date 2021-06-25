package com.uminski.crypto_mobile.client.server;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CreateVisitRequest {

    UUID authorId;

    String author;

    String date;
    
}
