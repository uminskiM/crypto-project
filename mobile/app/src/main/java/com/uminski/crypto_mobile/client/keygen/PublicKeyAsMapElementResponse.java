package com.uminski.crypto_mobile.client.keygen;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class PublicKeyAsMapElementResponse {

    List<Integer> publicKey;

    String owner;

}
