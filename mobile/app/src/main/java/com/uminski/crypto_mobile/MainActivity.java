package com.uminski.crypto_mobile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uminski.crypto_mobile.client.RetrofitClient;
import com.uminski.crypto_mobile.client.keygen.KeyGenService;
import com.uminski.crypto_mobile.client.keygen.ParametersResponse;
import com.uminski.crypto_mobile.client.keygen.PublicKeyAsMapElementResponse;
import com.uminski.crypto_mobile.client.keygen.PublicKeyRequest;
import com.uminski.crypto_mobile.client.keygen.PublicKeyResponse;
import com.uminski.crypto_mobile.pki.KeyGenerator;
import com.uminski.crypto_mobile.pki.KeyPair;
import com.uminski.crypto_mobile.storage.ParametersStorage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private final KeyGenService keyGenService;

    private EditText username;

    private Button loadContactsButton;

    private Button initializeParametersButton;

    public MainActivity() {
        this.keyGenService = RetrofitClient.getKeyGenRetrofit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        loadContactsButton = findViewById(R.id.load_contacts_button);
        initializeParametersButton = findViewById(R.id.button);
    }

    public void initializeParameters(View view) throws IOException {
        this.keyGenService.getParameters().enqueue(new Callback<ParametersResponse>() {

            @Override
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onResponse(Call<ParametersResponse> call, Response<ParametersResponse> response) {
                ParametersResponse parameters = response.body();
                ParametersStorage parametersStorage = ParametersStorage.parametersStorage(BigInteger.valueOf(parameters.getN()));
                KeyPair keyPair = KeyGenerator.generate(BigInteger.valueOf(parameters.getN()));
                parametersStorage.setPrivateKey(keyPair.getPrivateKey());
                parametersStorage.setPublicKey(keyPair.getPublicKey());
                username.setEnabled(false);
                initializeParametersButton.setEnabled(false);
                postPublicKey(keyPair.getPublicKey());
                Log.i(TAG, "onResponse: " + parameters);
                Log.i(TAG, "keyPair: " + keyPair);
            }

            @Override
            public void onFailure(Call<ParametersResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error occurred while getting parameters", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postPublicKey(List<BigInteger> publicKey) {
        Call<PublicKeyResponse> call = this.keyGenService.addPublicKey(new PublicKeyRequest(publicKey, username.getText().toString()));
        call.enqueue(new Callback<PublicKeyResponse>() {
            @Override
            public void onResponse(Call<PublicKeyResponse> call, Response<PublicKeyResponse> response) {
                PublicKeyResponse publicKeyResponse = response.body();
                ParametersStorage storage = ParametersStorage.parametersStorage(BigInteger.ZERO); //ZERO SHOULD NOT BE TAKEN INTO ACCOUNT
                storage.setUserId(publicKeyResponse.getOwnerId());
                storage.setUsername(publicKeyResponse.getOwner());
                Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onResponse " + response.body());
                loadContactsButton.setEnabled(true);

            }

            @Override
            public void onFailure(Call<PublicKeyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error occurred while posting public key", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadContacts(View view) {
        Call<Map<String, PublicKeyAsMapElementResponse>> call = this.keyGenService.publicKeys();
        call.enqueue(new Callback<Map<String, PublicKeyAsMapElementResponse>>() {
            @Override
            public void onResponse(Call<Map<String, PublicKeyAsMapElementResponse>> call, Response<Map<String, PublicKeyAsMapElementResponse>> response) {
                Toast.makeText(getApplicationContext(), "Public keys received successfully", Toast.LENGTH_SHORT).show();
                loadContactsButton.setEnabled(false);
                Log.i(TAG, "onResponse " + response.body());
                loadContactsActivity(response.body());
            }

            @Override
            public void onFailure(Call<Map<String, PublicKeyAsMapElementResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error occurred while getting public keys", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadContactsActivity(Map<String, PublicKeyAsMapElementResponse> publicKeys) {
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("publicKeys", publicKeys.toString());

        startActivity(intent);
    }
}