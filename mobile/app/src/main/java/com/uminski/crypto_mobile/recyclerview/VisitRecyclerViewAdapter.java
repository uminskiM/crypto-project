package com.uminski.crypto_mobile.recyclerview;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uminski.crypto_mobile.R;

import java.util.List;

import static android.content.ContentValues.TAG;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class VisitRecyclerViewAdapter extends RecyclerView.Adapter<VisitRecyclerViewAdapter.ViewHolder> {

    private List<VisitListView> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView owner;
        private final TextView date;
        private final TextView status;
        private final TextView visitIdHolder;
        private final Button visitDetails;

        public ViewHolder(View view) {
            super(view);

            owner = view.findViewById(R.id.owner);
            date = view.findViewById(R.id.date);
            status = view.findViewById(R.id.status);
            visitDetails = view.findViewById(R.id.details_button);
            visitIdHolder = view.findViewById(R.id.visit_id_holder);
        }

        public TextView getOwner() {
            return owner;
        }

        public TextView getDate() {
            return date;
        }

        public Button getVisitDetails() {
            return visitDetails;
        }

        public TextView getVisitIdHolder() {
            return visitIdHolder;
        }

        public TextView getStatus() {
            return status;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public VisitRecyclerViewAdapter(List<VisitListView> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_row, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getOwner().setText(localDataSet.get(position).getOwner());
        viewHolder.getDate().setText(localDataSet.get(position).getDate());
        viewHolder.getStatus().setText(localDataSet.get(position).getStatus());
        viewHolder.getVisitIdHolder().setText(localDataSet.get(position).getVisitId().toString());
        Log.i(TAG, "visitId of view" + viewHolder.getVisitIdHolder().getText().toString());
        viewHolder.getVisitDetails().setOnClickListener(new VisitDetailsOnClickListener(viewHolder.getVisitIdHolder().getText().toString()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
