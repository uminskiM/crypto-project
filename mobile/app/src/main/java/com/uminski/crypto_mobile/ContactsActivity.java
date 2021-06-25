package com.uminski.crypto_mobile;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uminski.crypto_mobile.client.RetrofitClient;
import com.uminski.crypto_mobile.client.server.CreateVisitRequest;
import com.uminski.crypto_mobile.client.server.ServerService;
import com.uminski.crypto_mobile.client.server.VisitResponse;
import com.uminski.crypto_mobile.recyclerview.VisitListView;
import com.uminski.crypto_mobile.recyclerview.VisitRecyclerViewAdapter;
import com.uminski.crypto_mobile.storage.ParametersStorage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ContactsActivity extends AppCompatActivity {

    private final ServerService serverService;

    private List<VisitListView> visits;

    private EditText date;

    public ContactsActivity() {
        serverService = RetrofitClient.getServerRetrofit();
        visits = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        date = findViewById(R.id.visit_date);
        updateListOfVisits();
    }

    public void addVisit(View view) {
        ParametersStorage parametersStorage = ParametersStorage.parametersStorage(BigInteger.ZERO);
        CreateVisitRequest request = new CreateVisitRequest(parametersStorage.getUserId(), parametersStorage.getUsername(), date.getText().toString());
        Call<Void> call = serverService.createVisit(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(), "Visit created successfully", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onResponse " + response.body());
                updateListOfVisits();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error while creating visit", Toast.LENGTH_SHORT).show();
            }
        });
        date.setText("");
    }

    public void updateListOfVisits() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Call<List<VisitResponse>> visitsResponse = serverService.visits(null);
        visitsResponse.enqueue(new Callback<List<VisitResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<VisitResponse>> call, Response<List<VisitResponse>> response) {
                visits = response.body().stream()
                        .map(VisitListView::fromVisitResponse)
                        .collect(Collectors.toList());
                Log.i(TAG, "onResponse " + visits);
                VisitRecyclerViewAdapter adapter = new VisitRecyclerViewAdapter(visits);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<VisitResponse>> call, Throwable t) {
                visits = new ArrayList<>();
                Toast.makeText(getApplicationContext(), "Error while loading visits", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
