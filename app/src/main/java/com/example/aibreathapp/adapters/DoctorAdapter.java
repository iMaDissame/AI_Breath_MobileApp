package com.example.aibreathapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aibreathapp.R;
import com.example.aibreathapp.models.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private static final String TAG = "DoctorAdapter";
    private Context context;
    private List<Doctor> doctorList;

    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);

        // Utilisez doctorName du backend
        holder.doctorNameTextView.setText(doctor.getDoctorName());

        // Utilisez Specialiter du backend
        holder.specialtyTextView.setText(doctor.getSpeciality());

        // Utilisez City du backend
        holder.cityTextView.setText(doctor.getCity());

        // Debugging
        Log.d(TAG, "Doctor: " + doctor.getDoctorName() + ", Image URL: " + doctor.getImage());

        // Chargez l'image si disponible (utilisant le champ image comme dans le backend)
        String imageUrl = doctor.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d(TAG, "Loading image: " + imageUrl);

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(holder.doctorImageView);
        } else {
            holder.doctorImageView.setImageResource(R.drawable.ic_image_placeholder);
        }

        // Bouton pour voir le profil du médecin
        holder.viewDoctorButton.setOnClickListener(v -> {
            String link = doctor.getLink();
            if (link != null && !link.isEmpty()) {
                // Si le lien ne commence pas par http/https, ajoutez-le
                if (!link.startsWith("http://") && !link.startsWith("https://")) {
                    link = "https://" + link;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No profile link available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList != null ? doctorList.size() : 0;
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorImageView;
        TextView doctorNameTextView;
        TextView specialtyTextView;
        TextView cityTextView;
        Button viewDoctorButton;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorImageView = itemView.findViewById(R.id.doctorImageView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            specialtyTextView = itemView.findViewById(R.id.specialtyTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            viewDoctorButton = itemView.findViewById(R.id.viewDoctorButton);
        }
    }
}