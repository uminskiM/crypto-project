package com.uminski.crypto_mobile.client.keygen;

import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class PublicKeyRequest {

    List<BigInteger> values;

    String owner;
}
