package com.uminski.crypto_mobile.storage;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class ParametersStorage {

    private static ParametersStorage instance;

    private ParametersStorage(BigInteger n) {
        this.n = n;
    }

    public static ParametersStorage parametersStorage(BigInteger n) {
        if (instance == null) {
            instance = new ParametersStorage(n);
        }
        return instance;
    }

    private BigInteger n;

    private List<BigInteger> privateKey;

    private List<BigInteger> publicKey;

    private UUID userId;

    private String username;

    private BigInteger randomValue;

    public List<BigInteger> getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(List<BigInteger> privateKey) {
        this.privateKey = privateKey;
    }

    public List<BigInteger> getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(List<BigInteger> publicKey) {
        this.publicKey = publicKey;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(BigInteger randomValue) {
        this.randomValue = randomValue;
    }
}
