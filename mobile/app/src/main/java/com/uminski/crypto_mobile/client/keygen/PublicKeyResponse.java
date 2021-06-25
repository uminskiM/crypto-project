package com.uminski.crypto_mobile.client.keygen;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class PublicKeyResponse {

    UUID ownerId;

    List<Integer> values;

    String owner;

}
