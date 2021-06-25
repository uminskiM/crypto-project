package com.uminski.crypto_mobile.pki;

import java.math.BigInteger;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class KeyPair {

    List<BigInteger> privateKey;

    List<BigInteger> publicKey;
}
