package com.uminski.crypto_mobile.ffs;

import android.util.Log;
import android.widget.Toast;

import com.uminski.crypto_mobile.client.RetrofitClient;
import com.uminski.crypto_mobile.client.keygen.KeyGenService;
import com.uminski.crypto_mobile.client.keygen.PublicKeyAsMapElementResponse;
import com.uminski.crypto_mobile.client.keygen.PublicKeyResponse;
import com.uminski.crypto_mobile.storage.ParametersStorage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class FeigeFiatShamir {

    private final ParametersStorage parametersStorage;

    private final KeyGenService keyGenService;

    private List<Integer> valuesOfPublicKey;


    public FeigeFiatShamir() {
        this.parametersStorage = ParametersStorage.parametersStorage(BigInteger.ZERO);
        this.keyGenService = RetrofitClient.getKeyGenRetrofit();
        this.valuesOfPublicKey = new ArrayList<>();
    }

    public BigInteger generateX() {
        int sign = new Random().nextInt(2) == 0 ? -1 : 1;
        BigInteger random = BigInteger.valueOf(new Random().nextInt());
        parametersStorage.setRandomValue(random);
        return random.multiply(random).multiply(BigInteger.valueOf(sign)).mod(parametersStorage.getN());
    }

    public List<Integer> generateRandomBits() {
        List<Integer> randomBits = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            randomBits.add(new Random().nextInt(2));
        }
        return randomBits;
    }

    public BigInteger generateY(List<Integer> verifierBits) {
        List<BigInteger> privateKey = parametersStorage.getPrivateKey();
        BigInteger result = parametersStorage.getRandomValue();
        for (int i = 0; i < privateKey.size(); i++) {
            result = result.multiply(privateKey.get(i).pow(verifierBits.get(i)));
        }
        return result.mod(parametersStorage.getN());
    }

    public Boolean verify(UUID guestId, BigInteger x, BigInteger y, List<Integer> verifierBits) {
        Call<Map<String, PublicKeyAsMapElementResponse>> response = keyGenService.publicKeys();
        response.enqueue(new Callback<Map<String, PublicKeyAsMapElementResponse>>() {
            @Override
            public void onResponse(Call<Map<String, PublicKeyAsMapElementResponse>> call, Response<Map<String, PublicKeyAsMapElementResponse>> response) {
                Map<String, PublicKeyAsMapElementResponse> responseMap = response.body();
                PublicKeyAsMapElementResponse publicKey = responseMap.get(guestId.toString());
                valuesOfPublicKey = publicKey.getPublicKey();
            }

            @Override
            public void onFailure(Call<Map<String, PublicKeyAsMapElementResponse>> call, Throwable t) {
                Log.i(TAG, "Error while getting public keys");
            }
        });
        BigInteger n = parametersStorage.getN();
        BigInteger result = x;
        for (int i = 0; i < valuesOfPublicKey.size(); i++) {
            result = result.multiply(BigInteger.valueOf(valuesOfPublicKey.get(i)).pow(verifierBits.get(i)));
        }
        BigInteger ySquare = y.modPow(BigInteger.valueOf(2), n);
        Log.w(TAG, n + " " + ySquare + " " + result.mod(n) + " " + result.negate().mod(n));
        return ySquare.compareTo(result.mod(n)) == 0 || ySquare.compareTo(result.negate().mod(n)) == 0;
    }
}
