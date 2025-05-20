// java/com/example/aibreathapp/fragments/MedicalReportsFragment.java
package com.example.aibreathapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aibreathapp.R;
import com.example.aibreathapp.adapters.DocumentAdapter;
import com.example.aibreathapp.api.ApiClient;
import com.example.aibreathapp.api.ApiService;
import com.example.aibreathapp.models.Document;
import com.example.aibreathapp.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalReportsFragment extends Fragment implements DocumentAdapter.DocumentClickListener {

    private RecyclerView reportsRecyclerView;
    private EditText searchEditText;
    private TextView searchButton;
    private ProgressBar progressBar;
    private FloatingActionButton addReportFab;

    private List<Document> documentList = new ArrayList<>();
    private DocumentAdapter documentAdapter;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_reports, container, false);

        // Initialize UI components
        reportsRecyclerView = view.findViewById(R.id.reportsRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        progressBar = view.findViewById(R.id.progressBar);
        addReportFab = view.findViewById(R.id.addReportFab);

        // Set up RecyclerView
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        documentAdapter = new DocumentAdapter(getContext(), documentList, this);
        reportsRecyclerView.setAdapter(documentAdapter);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());



        // Set up click listeners
        searchButton.setOnClickListener(v -> searchDocuments());
        addReportFab.setOnClickListener(v -> navigateToPneumoniaPrediction());

        // Load documents
        loadDocuments(null);

        return view;
    }

    private void loadDocuments(String patientName) {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getDocuments(patientName).enqueue(new Callback<List<Document>>() {
            @Override
            public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    documentList.clear();
                    documentList.addAll(response.body());
                    documentAdapter.notifyDataSetChanged();

                    if (documentList.isEmpty()) {
                        Toast.makeText(getContext(),
                                "No documents found",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Failed to load documents",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Document>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchDocuments() {
        String patientName = searchEditText.getText().toString().trim();
        loadDocuments(patientName);
    }

    private void navigateToPneumoniaPrediction() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PneumoniaPredictionFragment())
                .commit();
    }

    @Override
    public void onDocumentClick(Document document) {
        if (document.getFileUrl() != null && !document.getFileUrl().isEmpty()) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(document.getFileUrl()));
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(getContext(),
                        "Error opening document: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(),
                    "Document URL not available",
                    Toast.LENGTH_SHORT).show();
        }
    }
}