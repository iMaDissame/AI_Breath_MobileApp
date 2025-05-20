package com.example.aibreathapp.api;

import com.example.aibreathapp.models.ChatbotRequest;
import com.example.aibreathapp.models.ChatbotResponse;
import com.example.aibreathapp.models.Doctor;
import com.example.aibreathapp.models.DoctorFilters;
import com.example.aibreathapp.models.Document;
import com.example.aibreathapp.models.HeartbeatRequest;
import com.example.aibreathapp.models.HeartbeatResponse;
import com.example.aibreathapp.models.LoginRequest;
import com.example.aibreathapp.models.LoginResponse;
import com.example.aibreathapp.models.PneumoniaResponse;
import com.example.aibreathapp.models.RegisterRequest;
import com.example.aibreathapp.models.RegisterResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    // Authentication endpoints
    @POST("api/login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/register/")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("api/logout/")
    Call<Void> logout();

    // Doctor search endpoints
    @GET("api/doctor-filters/")
    Call<DoctorFilters> getDoctorFilters();

    @GET("api/doctors/")
    Call<List<Doctor>> searchDoctors(
            @Query("Specialiter") String specialty,
            @Query("ville") String city
    );

    // Pneumonia prediction
    @Multipart
    @POST("api/predict-pneumonia/")
    Call<PneumoniaResponse> predictPneumonia(
            @Part MultipartBody.Part image_file  // Remarquez le nom correct "image_file" au lieu de "image"
    );

    // Heartbeat classification
    @POST("api/heartbeat-classification/")
    Call<HeartbeatResponse> classifyHeartbeat(@Body HeartbeatRequest request);

    // Chatbot endpoint
    @POST("api/chatbot/")
    Call<ChatbotResponse> getChatbotResponse(@Body ChatbotRequest request);

    // Documents endpoints
    @GET("api/documents/")
    Call<List<Document>> getDocuments(
            @Query("firstname") String firstname
    );

    // Report generation
    @Multipart
    @POST("api/generate-report/")
    Call<Document> generateReport(
            @Part MultipartBody.Part image_file,
            @Part("firstname") RequestBody firstname,
            @Part("lastname") RequestBody lastname,
            @Part("case_description") RequestBody caseDescription
    );
}