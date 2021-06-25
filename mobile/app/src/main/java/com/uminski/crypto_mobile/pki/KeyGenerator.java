package com.uminski.crypto_mobile.pki;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KeyGenerator {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static KeyPair generate(BigInteger n) {
        List<BigInteger> secretKey = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BigInteger random = new BigInteger(String.valueOf(new Random().nextInt())).mod(n);
            while (random.gcd(n).compareTo(BigInteger.ONE) != 0) {
                random = new BigInteger(String.valueOf(new Random().nextInt())).mod(n);
            }
            secretKey.add(random);
        }
        List<BigInteger> publicKey = secretKey.stream()
                .map(number -> number.modPow(BigInteger.valueOf(-2), n))
                .collect(Collectors.toList());
        return new KeyPair(secretKey, publicKey);
    }
}
