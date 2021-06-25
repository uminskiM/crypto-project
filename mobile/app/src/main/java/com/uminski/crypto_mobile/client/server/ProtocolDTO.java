package com.uminski.crypto_mobile.client.server;

import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ProtocolDTO {

    BigInteger x;

    BigInteger y;

    List<Integer> verifierBits;

    Boolean holds;

}
