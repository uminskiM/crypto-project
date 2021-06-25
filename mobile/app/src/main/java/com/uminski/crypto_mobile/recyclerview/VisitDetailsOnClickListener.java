package com.uminski.crypto_mobile.recyclerview;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.uminski.crypto_mobile.VisitDetailsActivity;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.startActivity;

public class VisitDetailsOnClickListener implements View.OnClickListener {

    private final String visitId;

    public VisitDetailsOnClickListener(String visitId) {
        this.visitId = visitId;
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "visitId " + visitId);
        Intent intent = new Intent(view.getContext(), VisitDetailsActivity.class);
        intent.putExtra("visitId", visitId);
        view.getContext().startActivity(intent);
    }
}
