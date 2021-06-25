package com.uminski.crypto_mobile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.uminski.crypto_mobile.client.RetrofitClient;
import com.uminski.crypto_mobile.client.server.ProtocolDTO;
import com.uminski.crypto_mobile.client.server.ServerService;
import com.uminski.crypto_mobile.client.server.VisitResponse;
import com.uminski.crypto_mobile.ffs.FeigeFiatShamir;
import com.uminski.crypto_mobile.storage.ParametersStorage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class VisitDetailsActivity extends AppCompatActivity {

    private final ServerService serverService;

    private TextView authorId;
    private TextView author;
    private TextView date;
    private TextView guestId;
    private TextView protocolX;
    private TextView protocolBits;
    private TextView protocolY;
    private TextView status;
    private TextView visitId;

    private Button declare;
    private Button challenge;
    private Button confirm;
    private Button verify;


    private VisitResponse visit;

    private String visitIdExtra;

    public VisitDetailsActivity() {
        serverService = RetrofitClient.getServerRetrofit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_details);
        authorId = findViewById(R.id.author_id);
        author = findViewById(R.id.author);
        date = findViewById(R.id.date);
        guestId = findViewById(R.id.guest_id);
        protocolX = findViewById(R.id.protocol_x);
        protocolBits = findViewById(R.id.protocol_bits);
        protocolY = findViewById(R.id.protocol_y);
        status = findViewById(R.id.status);
        visitId = findViewById(R.id.visit_id);

        declare = findViewById(R.id.declare);
        challenge = findViewById(R.id.challenge);
        confirm = findViewById(R.id.confirm);
        verify = findViewById(R.id.verify);

        visitIdExtra = getIntent().getExtras().getString("visitId");
        Log.i(TAG, "from intent " + visitIdExtra);
        Call<List<VisitResponse>> visits = serverService.visits(null);
        visits.enqueue(new Callback<List<VisitResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<VisitResponse>> call, Response<List<VisitResponse>> response) {
                visit = response.body().stream()
                        .filter(visit -> visit.getVisitId().toString().equals(visitIdExtra))
                        .findFirst().get();
                authorId.setText("Author id: " + visit.getAuthorId().toString());
                author.setText("Author: " + visit.getAuthor());
                date.setText("Date: " + visit.getDate());
                guestId.setText(visit.getGuestId() != null ? "Guest id: " + visit.getGuestId().toString() : "");
                protocolX.setText(visit.getProtocol().getX() != null ? "Protocol X: " + visit.getProtocol().getX().toString() : "");
                protocolBits.setText(visit.getProtocol().getVerifierBits() != null ? "Protocol verifier bits: " + visit.getProtocol().getVerifierBits().toString() : "");
                protocolY.setText(visit.getProtocol().getY() != null ? "Protocol Y: " + visit.getProtocol().getY().toString() : "");
                status.setText("Status: " + visit.getStatus());
                visitId.setText("Visit id: " + visit.getVisitId().toString());

                switch (visit.getStatus()) {
                    case "FREE":
                        declare.setEnabled(true);
                        challenge.setEnabled(false);
                        confirm.setEnabled(false);
                        verify.setEnabled(false);
                        break;
                    case "DECLARED":
                        declare.setEnabled(false);
                        challenge.setEnabled(true);
                        confirm.setEnabled(false);
                        verify.setEnabled(false);
                        break;
                    case "CHALLENGED":
                        declare.setEnabled(false);
                        challenge.setEnabled(false);
                        confirm.setEnabled(true);
                        verify.setEnabled(false);
                        break;
                    case "CONFIRMED":
                        declare.setEnabled(false);
                        challenge.setEnabled(false);
                        confirm.setEnabled(false);
                        verify.setEnabled(true);
                        break;
                    default:
                        declare.setEnabled(false);
                        challenge.setEnabled(false);
                        confirm.setEnabled(false);
                        verify.setEnabled(false);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<VisitResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error occurred while downloading visits", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void declareVisit(View view) {
        FeigeFiatShamir feigeFiatShamir = new FeigeFiatShamir();
        Call<VisitResponse> call = serverService.updateVisit("DECLARED",
                visitIdExtra,
                ParametersStorage.parametersStorage(BigInteger.ZERO).getUserId().toString(),
                new ProtocolDTO(feigeFiatShamir.generateX(), BigInteger.ZERO, new ArrayList<>(), false));
        call.enqueue(new Callback<VisitResponse>() {
            @Override
            public void onResponse(Call<VisitResponse> call, Response<VisitResponse> response) {
                Log.i(TAG, "Visit declared");
                Intent intent = new Intent(view.getContext(), ContactsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<VisitResponse> call, Throwable t) {
                Log.i(TAG, "Error while declaring visit");
            }
        });
    }

    public void challengeVisit(View view) {
        FeigeFiatShamir feigeFiatShamir = new FeigeFiatShamir();
        Call<VisitResponse> call = serverService.updateVisit("CHALLENGED", visitIdExtra, null,
                new ProtocolDTO(visit.getProtocol().getX(), BigInteger.ZERO, feigeFiatShamir.generateRandomBits(), false));
        call.enqueue(new Callback<VisitResponse>() {
            @Override
            public void onResponse(Call<VisitResponse> call, Response<VisitResponse> response) {
                Log.i(TAG, "Visit challenged");
                Intent intent = new Intent(view.getContext(), ContactsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<VisitResponse> call, Throwable t) {
                Log.i(TAG, "Error while challenging visit");
            }
        });
    }

    public void confirmVisit(View view) {
        FeigeFiatShamir feigeFiatShamir = new FeigeFiatShamir();
        Call<VisitResponse> call = serverService.updateVisit("CONFIRMED", visitIdExtra, null,
                new ProtocolDTO(visit.getProtocol().getX(), feigeFiatShamir.generateY(visit.getProtocol().getVerifierBits()), visit.getProtocol().getVerifierBits(), false));
        call.enqueue(new Callback<VisitResponse>() {
            @Override
            public void onResponse(Call<VisitResponse> call, Response<VisitResponse> response) {
                Log.i(TAG, "Visit confirmed");
                Intent intent = new Intent(view.getContext(), ContactsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<VisitResponse> call, Throwable t) {
                Log.i(TAG, "Error while confirming visit");
            }
        });
    }

    public void verifyVisit(View view) {
        FeigeFiatShamir feigeFiatShamir = new FeigeFiatShamir();
        Call<VisitResponse> call = serverService.updateVisit("VERIFIED", visitIdExtra, null,
                new ProtocolDTO(visit.getProtocol().getX(), visit.getProtocol().getY(), visit.getProtocol().getVerifierBits(),
                        feigeFiatShamir.verify(visit.getGuestId(), visit.getProtocol().getX(), visit.getProtocol().getY(), visit.getProtocol().getVerifierBits())));
        call.enqueue(new Callback<VisitResponse>() {
            @Override
            public void onResponse(Call<VisitResponse> call, Response<VisitResponse> response) {
                Log.i(TAG, "Visit verified");
                Intent intent = new Intent(view.getContext(), ContactsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<VisitResponse> call, Throwable t) {
                Log.i(TAG, "Error while verifying visit");
            }
        });
    }

}