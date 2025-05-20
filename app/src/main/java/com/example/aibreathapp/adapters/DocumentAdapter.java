// java/com/example/aibreathapp/adapters/DocumentAdapter.java
package com.example.aibreathapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aibreathapp.R;
import com.example.aibreathapp.models.Document;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private Context context;
    private List<Document> documentList;
    private DocumentClickListener clickListener;

    public interface DocumentClickListener {
        void onDocumentClick(Document document);
    }

    public DocumentAdapter(Context context, List<Document> documentList, DocumentClickListener clickListener) {
        this.context = context;
        this.documentList = documentList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        Document document = documentList.get(position);

        // Utiliser des valeurs par défaut si les méthodes retournent null
        holder.documentTypeTextView.setText(document.getDocumentType());
        holder.patientNameTextView.setText("Patient: " + document.getPatientName());
        holder.doctorNameTextView.setText("Doctor: " + document.getDoctorName());
        holder.dateTextView.setText("Date: " + document.getCreatedDate());

        if (document.getDescription() != null && !document.getDescription().isEmpty()) {
            holder.descriptionTextView.setText("Description: " + document.getDescription());
        } else {
            holder.descriptionTextView.setText("Description: Not available");
        }

        holder.viewButton.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onDocumentClick(document);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentList != null ? documentList.size() : 0;
    }

    static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView documentTypeTextView;
        TextView patientNameTextView;
        TextView doctorNameTextView;
        TextView dateTextView;
        TextView descriptionTextView;
        Button viewButton;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            documentTypeTextView = itemView.findViewById(R.id.documentTypeTextView);
            patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            viewButton = itemView.findViewById(R.id.viewButton);
        }
    }
}